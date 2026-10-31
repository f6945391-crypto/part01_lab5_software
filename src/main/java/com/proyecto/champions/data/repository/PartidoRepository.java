package com.proyecto.champions.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.champions.data.entity.Partido;


public interface PartidoRepository
        extends JpaRepository<Partido, Long> {

    // Usado por la sincronizacion para hacer upsert sin duplicar.
    Optional<Partido> findByOrigenAndIdExterno(
        String origen,
        String idExterno
    );

    List<Partido> findAllByOrderByFechaHoraAsc();
}
