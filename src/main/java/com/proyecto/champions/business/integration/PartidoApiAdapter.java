package com.proyecto.champions.business.integration;

import java.util.List;


/**
 * Puerto hacia la fuente de partidos. Permite intercambiar entre una API
 * real (football-data.org) y una API simulada sin tocar el resto del negocio.
 */
public interface PartidoApiAdapter {

    /** Identificador del origen de datos (p. ej. SIMULADO). */
    String origen();

    /** Recupera los partidos desde la fuente. */
    List<PartidoExterno> obtenerPartidos();
}
