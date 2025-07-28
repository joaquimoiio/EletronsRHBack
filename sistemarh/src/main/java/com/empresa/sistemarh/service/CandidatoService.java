package com.empresa.sistemarh.service;

import com.empresa.sistemarh.model.Candidato;
import com.empresa.sistemarh.model.Vaga;
import com.empresa.sistemarh.repository.CandidatoRepository;
import com.empresa.sistemarh.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Autowired
    private FileService fileService;

    public List<Candidato> listarPorVaga(Long vagaId) {
        return candidatoRepository.findByVagaId(vagaId);
    }

    public List<Candidato> buscarPorVagaENome(Long vagaId, String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return candidatoRepository.findByVagaId(vagaId);
        }
        return candidatoRepository.findByVagaIdAndNomeContaining(vagaId, nome.trim());
    }

    public Candidato inscreverCandidato(Long vagaId, String nome, String email, MultipartFile curriculo) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        Candidato candidato = new Candidato(nome, email, vaga);

        if (curriculo != null && !curriculo.isEmpty()) {
            String caminhoArquivo = fileService.salvarArquivo(curriculo, "curriculos");
            candidato.setCaminhoCurriculo(caminhoArquivo);
            candidato.setNomeArquivoCurriculo(curriculo.getOriginalFilename());
        }

        return candidatoRepository.save(candidato);
    }
}