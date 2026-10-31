package com.proyecto.champions.presentation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.champions.business.service.PartidoService;
import com.proyecto.champions.business.service.PrediccionService;
import com.proyecto.champions.data.entity.Partido;


@Controller
@RequestMapping("/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    private final PrediccionService prediccionService;


    public PartidoController(
            PartidoService partidoService,
            PrediccionService prediccionService
    ) {
        this.partidoService = partidoService;
        this.prediccionService = prediccionService;
    }


    @GetMapping
    public String listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String fase,
            Model model
    ) {

        var partidos = partidoService.buscar(q, fase);

        // Numero de predicciones por partido (para la tarjeta).
        Map<Long, Long> conteos = new HashMap<>();
        for (Partido p : partidos) {
            conteos.put(p.getId(), prediccionService.contarPorPartido(p.getId()));
        }

        model.addAttribute("partidos", partidos);
        model.addAttribute("conteos", conteos);
        model.addAttribute("fases", partidoService.fases());
        model.addAttribute("q", q);
        model.addAttribute("faseSel", fase);

        return "partidos";
    }
}
