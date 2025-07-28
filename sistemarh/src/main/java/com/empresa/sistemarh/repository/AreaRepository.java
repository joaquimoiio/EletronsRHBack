package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    boolean existsByNome(String nome);
}