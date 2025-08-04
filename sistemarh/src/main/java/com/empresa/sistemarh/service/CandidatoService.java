package com.empresa.sistemarh.service;

import com.empresa.sistemarh.model.Candidato;
import com.empresa.sistemarh.model.StatusCandidato;
import com.empresa.sistemarh.model.Vaga;
import com.empresa.sistemarh.repository.CandidatoRepository;
import com.empresa.sistemarh.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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

    public List<Candidato> buscarPorVagaEStatus(Long vagaId, StatusCandidato status, String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return candidatoRepository.findByVagaIdAndStatus(vagaId, status);
        }
        return candidatoRepository.findByVagaIdAndStatusAndNomeContaining(vagaId, status, nome.trim());
    }

    public long contarCandidatosPorVaga(Long vagaId) {
        return candidatoRepository.countByVagaId(vagaId);
    }

    public long contarCandidatosChamadosPorVaga(Long vagaId) {
        return candidatoRepository.countByVagaIdAndStatus(vagaId, StatusCandidato.CHAMADO);
    }

    public long contarTotalCandidatos() {
        return candidatoRepository.count();
    }

    public Candidato inscreverCandidato(Long vagaId, String nome, String email, String telefone, MultipartFile curriculo) {
        Vaga vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório");
        }

        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone é obrigatório");
        }

        // Validar formato do telefone (apenas números, deve ter 10 ou 11 dígitos)
        String telefoneNumeros = telefone.replaceAll("\\D", "");
        if (telefoneNumeros.length() < 10 || telefoneNumeros.length() > 11) {
            throw new IllegalArgumentException("Telefone deve ter entre 10 e 11 dígitos");
        }

        Candidato candidato = new Candidato(nome.trim(), email.trim(), telefone.trim(), vaga);

        if (curriculo != null && !curriculo.isEmpty()) {
            // Validar tipo de arquivo
            String originalFilename = curriculo.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("Apenas arquivos PDF são aceitos para o currículo");
            }

            // Validar tamanho do arquivo (5MB)
            if (curriculo.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("O arquivo do currículo deve ter no máximo 5MB");
            }

            String caminhoArquivo = fileService.salvarArquivo(curriculo, "curriculos");
            candidato.setCaminhoCurriculo(caminhoArquivo);
            candidato.setNomeArquivoCurriculo(curriculo.getOriginalFilename());
        }

        return candidatoRepository.save(candidato);
    }

    public Candidato chamarParaEntrevista(Long candidatoId) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado"));

        if (candidato.getStatus() == StatusCandidato.CHAMADO) {
            throw new IllegalArgumentException("Candidato já foi chamado para entrevista");
        }

        candidato.setStatus(StatusCandidato.CHAMADO);
        candidato.setDataChamada(LocalDateTime.now());

        return candidatoRepository.save(candidato);
    }

    public Candidato alterarStatusCandidato(Long candidatoId, StatusCandidato novoStatus) {
        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new IllegalArgumentException("Candidato não encontrado"));

        candidato.setStatus(novoStatus);

        if (novoStatus == StatusCandidato.CHAMADO && candidato.getDataChamada() == null) {
            candidato.setDataChamada(LocalDateTime.now());
        }

        return candidatoRepository.save(candidato);
    }
}