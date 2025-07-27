package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.StatusVaga;
import com.empresa.sistemarh.model.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByStatus(StatusVaga status);
    List<Vaga> findByAreaId(Long areaId);

    @Query("SELECT v FROM Vaga v WHERE v.status = 'ATIVA' ORDER BY v.dataCriacao DESC")
    List<Vaga> findVagasAtivas();
}