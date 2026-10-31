package com.proyecto.champions.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.champions.business.integration.PartidoApiAdapter;
import com.proyecto.champions.business.integration.PartidoExterno;
import com.proyecto.champions.data.entity.Partido;
import com.proyecto.champions.data.repository.PartidoRepository;


/**
 * Servicio de sincronizacion (capa de negocio).
 *
 * Consulta la fuente de partidos y hace UPSERT por (origen, id_externo):
 * inserta los nuevos y actualiza los existentes. Nunca borra, para no romper
 * la asociacion con las predicciones ya registradas.
 *
 * Si la fuente (API real) falla, NO se cae la aplicacion: se registra el error
 * y se conservan los partidos que ya hubiera en la base.
 */
@Service
public class SincronizacionService {

    private static final Logger log =
        LoggerFactory.getLogger(SincronizacionService.class);

    private final PartidoApiAdapter adapter;

    private final PartidoRepository partidoRepository;

    private final EstadoSincronizacion estado;


    public SincronizacionService(
            PartidoApiAdapter adapter,
            PartidoRepository partidoRepository,
            EstadoSincronizacion estado
    ) {
        this.adapter = adapter;
        this.partidoRepository = partidoRepository;
        this.estado = estado;
    }


    @Transactional
    public void sincronizar() {

        try {
            var externos = adapter.obtenerPartidos();

            int nuevos = 0;
            int actualizados = 0;

            for (PartidoExterno e : externos) {

                var existente = partidoRepository
                    .findByOrigenAndIdExterno(e.origen(), e.idExterno());

                Partido p = existente.orElseGet(Partido::new);
                boolean esNuevo = existente.isEmpty();

                p.setOrigen(e.origen());
                p.setIdExterno(e.idExterno());
                p.setEquipoLocal(e.equipoLocal());
                p.setEquipoVisitante(e.equipoVisitante());
                p.setFechaHora(e.fechaHora());
                p.setFase(e.fase());
                p.setEstado(e.estado());
                p.setGolesLocal(e.golesLocal());
                p.setGolesVisitante(e.golesVisitante());

                partidoRepository.save(p);

                if (esNuevo) {
                    nuevos++;
                } else {
                    actualizados++;
                }
            }

            estado.registrarExito(
                adapter.origen(),
                externos.size(),
                nuevos,
                actualizados
            );

            log.info("Sincronizacion OK ({}): {} recibidos, {} nuevos, {} actualizados",
                adapter.origen(), externos.size(), nuevos, actualizados);

        } catch (Exception ex) {
            estado.registrarError(ex.getMessage());
            log.error("Fallo la sincronizacion de partidos: {}", ex.getMessage());
        }
    }
}
