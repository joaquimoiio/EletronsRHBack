package com.empresa.sistemarh.repository;

import com.empresa.sistemarh.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    List<Candidato> findByVagaId(Long vagaId);

    @Query("SELECT c FROM Candidato c WHERE c.vaga.id = :vagaId AND LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Candidato> findByVagaIdAndNomeContaining(@Param("vagaId") Long vagaId, @Param("nome") String nome);

    // Método para contar candidatos por vaga
    long countByVagaId(Long vagaId);
}