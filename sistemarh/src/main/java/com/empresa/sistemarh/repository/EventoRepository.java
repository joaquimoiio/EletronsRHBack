package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    @Query("SELECT e FROM Evento e ORDER BY e.dataCriacao DESC")
    List<Evento> findAllOrderByDataCriacaoDesc();
}