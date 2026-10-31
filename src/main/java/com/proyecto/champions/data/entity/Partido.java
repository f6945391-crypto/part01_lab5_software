package com.proyecto.champions.data.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


/**
 * Entidad PARTIDO (capa de datos).
 *
 * La combinacion (origen, id_externo) es unica: identifica de forma estable
 * cada partido recuperado de la API y evita duplicados al re-sincronizar.
 */
@Entity
@Table(
    name = "partidos",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"origen", "id_externo"}
    )
)
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String origen;

    @Column(name = "id_externo", nullable = false, length = 50)
    private String idExterno;

    @Column(nullable = false, length = 100)
    private String equipoLocal;

    @Column(nullable = false, length = 100)
    private String equipoVisitante;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 50)
    private String fase;

    @Column(nullable = false, length = 30)
    private String estado;

    // Pueden ser null mientras el partido no se ha jugado.
    private Integer golesLocal;

    private Integer golesVisitante;


    public Partido() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
