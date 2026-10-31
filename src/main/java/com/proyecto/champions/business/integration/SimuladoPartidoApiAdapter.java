package com.proyecto.champions.business.integration;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


/**
 * Fuente SIMULADA de partidos (datos ficticios claramente identificados).
 *
 * Se activa cuando match.api.mode=simulado (valor por defecto). Para usar una
 * API real bastaria con crear otro PartidoApiAdapter activado con
 * match.api.mode=real, sin cambiar servicios ni controladores.
 */
@Component
@ConditionalOnProperty(
    name = "match.api.mode",
    havingValue = "simulado",
    matchIfMissing = true
)
public class SimuladoPartidoApiAdapter implements PartidoApiAdapter {

    private static final String ORIGEN = "SIMULADO";


    @Override
    public String origen() {
        return ORIGEN;
    }


    @Override
    public List<PartidoExterno> obtenerPartidos() {

        return List.of(
            partido("SIM-1", "Real Madrid", "Manchester City",
                LocalDateTime.of(2026, 11, 4, 19, 0), "Fase de liga"),
            partido("SIM-2", "Bayern Munich", "Barcelona",
                LocalDateTime.of(2026, 11, 4, 21, 0), "Fase de liga"),
            partido("SIM-3", "Liverpool", "Paris Saint-Germain",
                LocalDateTime.of(2026, 11, 5, 19, 0), "Fase de liga"),
            partido("SIM-4", "Inter", "Arsenal",
                LocalDateTime.of(2026, 11, 5, 21, 0), "Fase de liga"),
            partido("SIM-5", "Atletico de Madrid", "Borussia Dortmund",
                LocalDateTime.of(2026, 11, 25, 21, 0), "Octavos de final"),
            partido("SIM-6", "Benfica", "Juventus",
                LocalDateTime.of(2026, 11, 26, 21, 0), "Octavos de final")
        );
    }


    private PartidoExterno partido(
            String idExterno,
            String local,
            String visitante,
            LocalDateTime fecha,
            String fase
    ) {

        return new PartidoExterno(
            ORIGEN,
            idExterno,
            local,
            visitante,
            fecha,
            fase,
            "PROGRAMADO",
            null,
            null
        );
    }
}
