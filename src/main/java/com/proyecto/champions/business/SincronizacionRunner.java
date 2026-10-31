package com.proyecto.champions.business;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.proyecto.champions.business.service.SincronizacionService;


/**
 * Al iniciar la aplicacion, sincroniza los partidos desde la fuente hacia la
 * base de datos (requerimiento: almacenar los partidos al arrancar).
 */
@Component
public class SincronizacionRunner implements ApplicationRunner {

    private final SincronizacionService sincronizacionService;


    public SincronizacionRunner(
            SincronizacionService sincronizacionService
    ) {
        this.sincronizacionService = sincronizacionService;
    }


    @Override
    public void run(ApplicationArguments args) {
        sincronizacionService.sincronizar();
    }
}
