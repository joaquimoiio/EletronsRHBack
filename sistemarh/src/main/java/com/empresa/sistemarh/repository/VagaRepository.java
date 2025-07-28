package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Vaga;
import com.empresa.sistemarh.model.StatusVaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
    List<Vaga> findByStatus(StatusVaga status);

    @Query("SELECT v FROM Vaga v WHERE v.status = 'ATIVA' ORDER BY v.dataCriacao DESC")
    List<Vaga> findVagasAtivas();
}