package com.proyecto.champions.data.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Entidad PREDICCION (capa de datos).
 * Cada prediccion queda asociada a un partido mediante clave foranea.
 */
@Entity
@Table(name = "predicciones")
public class Prediccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partido_id")
    private Partido partido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResultadoEsperado resultadoEsperado;

    @Column(nullable = false)
    private Integer golesLocal;

    @Column(nullable = false)
    private Integer golesVisitante;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;


    public Prediccion() {
    }


    public Prediccion(
            Partido partido,
            ResultadoEsperado resultadoEsperado,
            Integer golesLocal,
            Integer golesVisitante,
            LocalDateTime fechaRegistro
    ) {
        this.partido = partido;
        this.resultadoEsperado = resultadoEsperado;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.fechaRegistro = fechaRegistro;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public ResultadoEsperado getResultadoEsperado() {
        return resultadoEsperado;
    }

    public void setResultadoEsperado(ResultadoEsperado resultadoEsperado) {
        this.resultadoEsperado = resultadoEsperado;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
