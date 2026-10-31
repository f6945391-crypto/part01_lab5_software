package com.proyecto.champions.business.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.champions.data.entity.Partido;
import com.proyecto.champions.data.entity.Prediccion;
import com.proyecto.champions.data.entity.ResultadoEsperado;
import com.proyecto.champions.data.repository.PartidoRepository;
import com.proyecto.champions.data.repository.PrediccionRepository;


/**
 * Servicio de predicciones (capa de negocio): registro con validacion y
 * consulta. Valida que el resultado esperado coincida con el marcador.
 */
@Service
public class PrediccionService {

    private final PrediccionRepository prediccionRepository;

    private final PartidoRepository partidoRepository;


    public PrediccionService(
            PrediccionRepository prediccionRepository,
            PartidoRepository partidoRepository
    ) {
        this.prediccionRepository = prediccionRepository;
        this.partidoRepository = partidoRepository;
    }


    public List<Prediccion> listarTodas() {
        return prediccionRepository.findAllByOrderByFechaRegistroDesc();
    }


    public long contar() {
        return prediccionRepository.count();
    }


    public boolean existePrediccionPara(Long partidoId) {
        return prediccionRepository.countByPartidoId(partidoId) > 0;
    }


    public long contarPorPartido(Long partidoId) {
        return prediccionRepository.countByPartidoId(partidoId);
    }


    @Transactional
    public Prediccion registrar(
            Long partidoId,
            ResultadoEsperado resultado,
            Integer golesLocal,
            Integer golesVisitante
    ) {

        Partido partido = partidoRepository
            .findById(partidoId)
            .orElseThrow(() ->
                new IllegalArgumentException("Partido no encontrado"));

        validar(resultado, golesLocal, golesVisitante);

        Prediccion prediccion = new Prediccion(
            partido,
            resultado,
            golesLocal,
            golesVisitante,
            LocalDateTime.now()
        );

        return prediccionRepository.save(prediccion);
    }


    private void validar(
            ResultadoEsperado resultado,
            Integer golesLocal,
            Integer golesVisitante
    ) {

        if (resultado == null) {
            throw new IllegalArgumentException(
                "Debes seleccionar un resultado esperado");
        }

        if (
            golesLocal == null || golesVisitante == null ||
            golesLocal < 0 || golesVisitante < 0 ||
            golesLocal > 99 || golesVisitante > 99
        ) {
            throw new IllegalArgumentException(
                "Los goles deben ser enteros entre 0 y 99");
        }

        // El resultado debe ser coherente con el marcador.
        ResultadoEsperado real =
            golesLocal > golesVisitante ? ResultadoEsperado.LOCAL
            : golesLocal < golesVisitante ? ResultadoEsperado.VISITANTE
            : ResultadoEsperado.EMPATE;

        if (resultado != real) {
            throw new IllegalArgumentException(
                "El resultado esperado no coincide con el marcador");
        }
    }
}
