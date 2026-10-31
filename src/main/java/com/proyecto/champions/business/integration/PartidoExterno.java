package com.proyecto.champions.business.integration;

import java.time.LocalDateTime;


/**
 * Datos de un partido tal como llegan de la fuente externa (API real o
 * simulada), antes de transformarse en la entidad Partido.
 */
public record PartidoExterno(
        String origen,
        String idExterno,
        String equipoLocal,
        String equipoVisitante,
        LocalDateTime fechaHora,
        String fase,
        String estado,
        Integer golesLocal,
        Integer golesVisitante
) {
}
