package com.empresa.sistemarh.service;

import com.empresa.sistemarh.model.Evento;
import com.empresa.sistemarh.model.ImagemEvento;
import com.empresa.sistemarh.repository.EventoRepository;
import com.empresa.sistemarh.repository.ImagemEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ImagemEventoRepository imagemEventoRepository;

    @Autowired
    private FileService fileUploadService;

    public List<Evento> listarTodos() {
        return eventoRepository.findAllOrderByDataCriacaoDesc();
    }

    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento criarEvento(String titulo, MultipartFile imagemCapa) throws IOException {
        Evento evento = new Evento(titulo);

        if (imagemCapa != null && !imagemCapa.isEmpty()) {
            String caminhoImagem = fileUploadService.salvarArquivo(imagemCapa, "eventos");
            evento.setImagemCapa(caminhoImagem);
        }

        return eventoRepository.save(evento);
    }

    public Evento atualizarEvento(Long id, String descricao) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        evento.setDescricao(descricao);
        return eventoRepository.save(evento);
    }

    public ImagemEvento adicionarImagemEvento(Long eventoId, MultipartFile imagem) throws IOException {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        String caminhoImagem = fileUploadService.salvarArquivo(imagem, "eventos");
        ImagemEvento imagemEvento = new ImagemEvento(evento, imagem.getOriginalFilename(), caminhoImagem);

        return imagemEventoRepository.save(imagemEvento);
    }

    public List<ImagemEvento> listarImagensEvento(Long eventoId) {
        return imagemEventoRepository.findByEventoId(eventoId);
    }

    public void deletarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        // Deletar arquivos de imagem
        if (evento.getImagemCapa() != null) {
            fileUploadService.deletarArquivo(evento.getImagemCapa());
        }

        if (evento.getImagens() != null) {
            evento.getImagens().forEach(img ->
                    fileUploadService.deletarArquivo(img.getCaminhoArquivo()));
        }

        eventoRepository.delete(evento);
    }

    public void deletarImagemEvento(Long imagemId) {
        ImagemEvento imagem = imagemEventoRepository.findById(imagemId)
                .orElseThrow(() -> new IllegalArgumentException("Imagem não encontrada"));

        fileUploadService.deletarArquivo(imagem.getCaminhoArquivo());
        imagemEventoRepository.delete(imagem);
    }
}