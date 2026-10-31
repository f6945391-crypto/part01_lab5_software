package com.proyecto.champions.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.champions.data.entity.Partido;
import com.proyecto.champions.data.repository.PartidoRepository;


/**
 * Servicio de partidos (capa de negocio): listado, busqueda y consulta.
 */
@Service
public class PartidoService {

    private final PartidoRepository repository;


    public PartidoService(PartidoRepository repository) {
        this.repository = repository;
    }


    public List<Partido> listarTodos() {
        return repository.findAllByOrderByFechaHoraAsc();
    }


    /** Busqueda por equipo (texto libre) y filtro por fase. */
    public List<Partido> buscar(String texto, String fase) {

        String q = texto == null ? "" : texto.trim().toLowerCase();

        return listarTodos()
            .stream()
            .filter(p ->
                q.isEmpty() ||
                (p.getEquipoLocal() + " " + p.getEquipoVisitante())
                    .toLowerCase().contains(q))
            .filter(p ->
                fase == null || fase.isBlank() ||
                fase.equals(p.getFase()))
            .toList();
    }


    public Partido obtenerPorId(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Partido no encontrado"));
    }


    public long contar() {
        return repository.count();
    }


    public List<String> fases() {
        return listarTodos()
            .stream()
            .map(Partido::getFase)
            .distinct()
            .sorted()
            .toList();
    }
}
