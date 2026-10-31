package com.proyecto.champions.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.champions.business.service.EstadoSincronizacion;
import com.proyecto.champions.business.service.PartidoService;
import com.proyecto.champions.business.service.PrediccionService;


@Controller
public class DashboardController {

    private final PartidoService partidoService;

    private final PrediccionService prediccionService;

    private final EstadoSincronizacion estado;


    public DashboardController(
            PartidoService partidoService,
            PrediccionService prediccionService,
            EstadoSincronizacion estado
    ) {
        this.partidoService = partidoService;
        this.prediccionService = prediccionService;
        this.estado = estado;
    }


    @GetMapping("/")
    public String dashboard(Model model) {

        var partidos = partidoService.listarTodos();

        long pendientes = partidos.stream()
            .filter(p -> !prediccionService.existePrediccionPara(p.getId()))
            .count();

        model.addAttribute("totalPartidos", partidos.size());
        model.addAttribute("totalPredicciones", prediccionService.contar());
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("proximos", partidos.stream().limit(3).toList());
        model.addAttribute("estado", estado);

        return "dashboard";
    }
}
