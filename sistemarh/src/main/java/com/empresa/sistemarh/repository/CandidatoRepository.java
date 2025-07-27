package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    List<Candidato> findByVagaId(Long vagaId);

    @Query("SELECT c FROM Candidato c WHERE c.vaga.id = :vagaId AND c.nome LIKE %:nome%")
    List<Candidato> findByVagaIdAndNomeContaining(@Param("vagaId") Long vagaId, @Param("nome") String nome);
}