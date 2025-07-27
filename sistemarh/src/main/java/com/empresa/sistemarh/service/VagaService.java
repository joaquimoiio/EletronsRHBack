package com.empresa.sistemarh.service;

import com.empresa.sistemarh.model.StatusVaga;
import com.empresa.sistemarh.model.Vaga;
import com.empresa.sistemarh.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    public List<Vaga> listarTodas() {
        return vagaRepository.findAll();
    }

    public List<Vaga> listarVagasAtivas() {
        return vagaRepository.findVagasAtivas();
    }

    public Optional<Vaga> buscarPorId(Long id) {
        return vagaRepository.findById(id);
    }

    public Vaga salvar(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    public Vaga atualizar(Long id, Vaga vagaAtualizada) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        vaga.setTitulo(vagaAtualizada.getTitulo());
        vaga.setDescricao(vagaAtualizada.getDescricao());
        vaga.setArea(vagaAtualizada.getArea());

        return vagaRepository.save(vaga);
    }

    public void alterarStatus(Long id, StatusVaga novoStatus) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        vaga.setStatus(novoStatus);
        vagaRepository.save(vaga);
    }

    public void deletar(Long id) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        vagaRepository.delete(vaga);
    }
}