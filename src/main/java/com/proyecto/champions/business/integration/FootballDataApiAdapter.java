package com.proyecto.champions.business.integration;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Fuente REAL de partidos: API publica de football-data.org (v4).
 *
 * Consume por HTTP el endpoint /competitions/{competicion}/matches usando el
 * encabezado de autenticacion X-Auth-Token. Se activa con match.api.mode=real.
 *
 * Requiere un token gratuito (variable MATCH_API_TOKEN). La competicion por
 * defecto es CL (UEFA Champions League).
 */
@Component
@ConditionalOnProperty(
    name = "match.api.mode",
    havingValue = "real"
)
public class FootballDataApiAdapter implements PartidoApiAdapter {

    private static final String ORIGEN = "FOOTBALL_DATA";

    private final String baseUrl;
    private final String token;
    private final String competicion;


    public FootballDataApiAdapter(
            @Value("${match.api.base-url}") String baseUrl,
            @Value("${match.api.token:}") String token,
            @Value("${match.api.competition:CL}") String competicion
    ) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.competicion = competicion;
    }


    @Override
    public String origen() {
        return ORIGEN;
    }


    @Override
    public List<PartidoExterno> obtenerPartidos() {

        if (token == null || token.isBlank()) {
            throw new IllegalStateException(
                "Falta MATCH_API_TOKEN para consumir la API real de football-data.org");
        }

        RestClient client = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("X-Auth-Token", token)
            .build();

        RespuestaApi respuesta = client.get()
            .uri("/competitions/{c}/matches", competicion)
            .retrieve()
            .body(RespuestaApi.class);

        List<PartidoExterno> partidos = new ArrayList<>();

        if (respuesta == null || respuesta.matches() == null) {
            return partidos;
        }

        for (PartidoApi m : respuesta.matches()) {

            Integer golesLocal = null;
            Integer golesVisitante = null;
            if (m.score() != null && m.score().fullTime() != null) {
                golesLocal = m.score().fullTime().home();
                golesVisitante = m.score().fullTime().away();
            }

            partidos.add(new PartidoExterno(
                ORIGEN,
                String.valueOf(m.id()),
                nombre(m.homeTeam()),
                nombre(m.awayTeam()),
                parseFecha(m.utcDate()),
                mapearFase(m.stage()),
                mapearEstado(m.status()),
                golesLocal,
                golesVisitante
            ));
        }

        return partidos;
    }


    private String nombre(EquipoApi equipo) {
        return (equipo == null || equipo.name() == null)
            ? "Por definir"
            : equipo.name();
    }


    private LocalDateTime parseFecha(String utcDate) {
        if (utcDate == null) {
            return LocalDateTime.now();
        }
        return OffsetDateTime.parse(utcDate).toLocalDateTime();
    }


    private String mapearFase(String stage) {
        if (stage == null) {
            return "Champions League";
        }
        return switch (stage) {
            case "LEAGUE_STAGE" -> "Fase de liga";
            case "PLAYOFFS", "PLAY_OFFS" -> "Play-offs";
            case "LAST_16" -> "Octavos de final";
            case "QUARTER_FINALS" -> "Cuartos de final";
            case "SEMI_FINALS" -> "Semifinal";
            case "FINAL" -> "Final";
            default -> stage;
        };
    }


    private String mapearEstado(String status) {
        if (status == null) {
            return "PROGRAMADO";
        }
        return switch (status) {
            case "SCHEDULED", "TIMED" -> "PROGRAMADO";
            case "IN_PLAY", "PAUSED" -> "EN_JUEGO";
            case "FINISHED" -> "FINALIZADO";
            default -> status;
        };
    }


    // ---- Estructuras para deserializar la respuesta JSON ----

    @JsonIgnoreProperties(ignoreUnknown = true)
    record RespuestaApi(List<PartidoApi> matches) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record PartidoApi(
            long id,
            String utcDate,
            String status,
            String stage,
            EquipoApi homeTeam,
            EquipoApi awayTeam,
            MarcadorApi score
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record EquipoApi(String name) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MarcadorApi(TiempoCompletoApi fullTime) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record TiempoCompletoApi(Integer home, Integer away) {
    }
}
