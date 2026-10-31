package com.proyecto.champions.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.champions.data.entity.Prediccion;


public interface PrediccionRepository
        extends JpaRepository<Prediccion, Long> {

    List<Prediccion> findAllByOrderByFechaRegistroDesc();

    long countByPartidoId(Long partidoId);
}
