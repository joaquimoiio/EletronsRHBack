package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByNome(String nome);
    boolean existsByNome(String nome);
}