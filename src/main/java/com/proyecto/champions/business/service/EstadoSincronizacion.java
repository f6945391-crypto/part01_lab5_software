package com.proyecto.champions.business.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;


/**
 * Guarda en memoria el resultado de la ultima sincronizacion, para mostrarlo
 * en el panel (ultima sincronizacion, origen, cuantos partidos, errores).
 */
@Component
public class EstadoSincronizacion {

    private LocalDateTime ultima;
    private String origen;
    private int recibidos;
    private int nuevos;
    private int actualizados;
    private boolean ok;
    private String mensajeError;


    public synchronized void registrarExito(
            String origen,
            int recibidos,
            int nuevos,
            int actualizados
    ) {
        this.ultima = LocalDateTime.now();
        this.origen = origen;
        this.recibidos = recibidos;
        this.nuevos = nuevos;
        this.actualizados = actualizados;
        this.ok = true;
        this.mensajeError = null;
    }


    public synchronized void registrarError(String mensaje) {
        this.ultima = LocalDateTime.now();
        this.ok = false;
        this.mensajeError = mensaje;
    }


    public LocalDateTime getUltima() {
        return ultima;
    }

    public String getOrigen() {
        return origen;
    }

    public int getRecibidos() {
        return recibidos;
    }

    public int getNuevos() {
        return nuevos;
    }

    public int getActualizados() {
        return actualizados;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
