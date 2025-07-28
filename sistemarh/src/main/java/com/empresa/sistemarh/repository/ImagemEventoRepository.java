package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.ImagemEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImagemEventoRepository extends JpaRepository<ImagemEvento, Long> {
    List<ImagemEvento> findByEventoId(Long eventoId);
}