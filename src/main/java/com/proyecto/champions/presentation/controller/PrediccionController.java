package com.proyecto.champions.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.champions.business.service.PrediccionService;
import com.proyecto.champions.data.entity.ResultadoEsperado;


@Controller
@RequestMapping("/predicciones")
public class PrediccionController {

    private final PrediccionService prediccionService;


    public PrediccionController(
            PrediccionService prediccionService
    ) {
        this.prediccionService = prediccionService;
    }


    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
            "predicciones",
            prediccionService.listarTodas()
        );

        return "predicciones";
    }


    @PostMapping
    public String registrar(
            @RequestParam Long partidoId,
            @RequestParam ResultadoEsperado resultado,
            @RequestParam Integer golesLocal,
            @RequestParam Integer golesVisitante,
            RedirectAttributes ra
    ) {

        try {
            prediccionService.registrar(
                partidoId, resultado, golesLocal, golesVisitante);
            ra.addFlashAttribute(
                "exito", "Prediccion registrada correctamente.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/partidos";
    }
}
