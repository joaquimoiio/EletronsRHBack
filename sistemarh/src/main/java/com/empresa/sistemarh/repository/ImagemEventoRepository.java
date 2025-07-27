package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.ImagemEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImagemEventoRepository extends JpaRepository<ImagemEvento, Long> {
    List<ImagemEvento> findByEventoId(Long eventoId);
}