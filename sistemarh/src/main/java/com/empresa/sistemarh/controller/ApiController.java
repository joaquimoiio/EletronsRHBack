package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.model.*;
import com.empresa.sistemarh.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private AreaService areaService;

    // === ESTATÍSTICAS ===
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        Map<String, Object> stats = new HashMap<>();

        var todasVagas = vagaService.listarTodas();
        var vagasAtivas = vagaService.listarVagasAtivas();
        var todosEventos = eventoService.listarTodos();

        // Contar total de candidatos
        long totalCandidatos = todasVagas.stream()
                .mapToLong(vaga -> vaga.getCandidatos() != null ? vaga.getCandidatos().size() : 0)
                .sum();

        stats.put("totalVagas", todasVagas.size());
        stats.put("vagasAtivas", vagasAtivas.size());
        stats.put("totalCandidatos", totalCandidatos);
        stats.put("totalEventos", todosEventos.size());

        return ResponseEntity.ok(stats);
    }

    // === ÁREAS ===
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listarAreas() {
        List<Area> areas = areaService.listarTodas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> buscarAreaPorId(@PathVariable Long id) {
        return areaService.buscarPorId(id)
                .map(area -> ResponseEntity.ok(area))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/areas")
    public ResponseEntity<?> criarArea(@RequestBody Area area) {
        try {
            Area novaArea = areaService.salvar(area);
            return ResponseEntity.ok(novaArea);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<?> atualizarArea(@PathVariable Long id, @RequestBody Area area) {
        try {
            Area areaAtualizada = areaService.atualizar(id, area);
            return ResponseEntity.ok(areaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<?> deletarArea(@PathVariable Long id) {
        try {
            areaService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === VAGAS ===
    @GetMapping("/vagas")
    public ResponseEntity<List<Vaga>> listarVagas() {
        List<Vaga> vagas = vagaService.listarTodas();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/vagas/ativas")
    public ResponseEntity<List<Vaga>> listarVagasAtivas() {
        List<Vaga> vagas = vagaService.listarVagasAtivas();
        return ResponseEntity.ok(vagas);
    }

    @GetMapping("/vagas/{id}")
    public ResponseEntity<Vaga> buscarVagaPorId(@PathVariable Long id) {
        return vagaService.buscarPorId(id)
                .map(vaga -> ResponseEntity.ok(vaga))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vagas")
    public ResponseEntity<?> criarVaga(@RequestBody Vaga vaga) {
        try {
            Vaga novaVaga = vagaService.salvar(vaga);
            return ResponseEntity.ok(novaVaga);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/vagas/{id}")
    public ResponseEntity<?> atualizarVaga(@PathVariable Long id, @RequestBody Vaga vaga) {
        try {
            Vaga vagaAtualizada = vagaService.atualizar(id, vaga);
            return ResponseEntity.ok(vagaAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/vagas/{id}/status")
    public ResponseEntity<?> alterarStatusVaga(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        try {
            String statusStr = statusData.get("status");
            StatusVaga novoStatus = StatusVaga.valueOf(statusStr);
            vagaService.alterarStatus(id, novoStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/vagas/{id}")
    public ResponseEntity<?> deletarVaga(@PathVariable Long id) {
        try {
            vagaService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === CANDIDATOS ===
    @GetMapping("/vagas/{vagaId}/candidatos")
    public ResponseEntity<List<Candidato>> getCandidatos(@PathVariable Long vagaId,
                                                         @RequestParam(required = false) String filtro) {
        try {
            List<Candidato> candidatos = candidatoService.buscarPorVagaENome(vagaId, filtro);
            return ResponseEntity.ok(candidatos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @PostMapping("/candidaturas")
    public ResponseEntity<?> criarCandidatura(@RequestParam Long vagaId,
                                              @RequestParam String nome,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) MultipartFile curriculo) {
        try {
            Candidato novoCandidato = candidatoService.inscreverCandidato(vagaId, nome, curriculo);
            return ResponseEntity.ok(novoCandidato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/candidatos")
    public ResponseEntity<List<Candidato>> listarCandidatos() {
        // Para estatísticas - retorna lista vazia por enquanto
        return ResponseEntity.ok(List.of());
    }

    // === EVENTOS ===
    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> listarEventos() {
        List<Evento> eventos = eventoService.listarTodos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<Evento> buscarEventoPorId(@PathVariable Long id) {
        return eventoService.buscarPorId(id)
                .map(evento -> ResponseEntity.ok(evento))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/eventos")
    public ResponseEntity<?> criarEvento(@RequestParam String titulo,
                                         @RequestParam(required = false) MultipartFile imagemCapa) {
        try {
            Evento novoEvento = eventoService.criarEvento(titulo, imagemCapa);
            return ResponseEntity.ok(novoEvento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/eventos/{id}")
    public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody Map<String, String> dados) {
        try {
            String descricao = dados.get("descricao");
            Evento eventoAtualizado = eventoService.atualizarEvento(id, descricao);
            return ResponseEntity.ok(eventoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/eventos/{id}/imagens")
    public ResponseEntity<List<ImagemEvento>> listarImagensEvento(@PathVariable Long id) {
        List<ImagemEvento> imagens = eventoService.listarImagensEvento(id);
        return ResponseEntity.ok(imagens);
    }

    @PostMapping("/eventos/{id}/imagens")
    public ResponseEntity<?> adicionarImagemEvento(@PathVariable Long id,
                                                   @RequestParam MultipartFile imagem) {
        try {
            ImagemEvento novaImagem = eventoService.adicionarImagemEvento(id, imagem);
            return ResponseEntity.ok(novaImagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
        try {
            eventoService.deletarEvento(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eventos/imagens/{imagemId}")
    public ResponseEntity<?> deletarImagemEvento(@PathVariable Long imagemId) {
        try {
            eventoService.deletarImagemEvento(imagemId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === NOTIFICAÇÕES ===
    @GetMapping("/notificacoes")
    public ResponseEntity<Object[]> getNotificacoes() {
        // Implementar lógica de notificações se necessário
        return ResponseEntity.ok(new Object[0]);
    }

    @PostMapping("/notificacoes")
    public ResponseEntity<?> cadastrarNotificacao(@RequestBody Map<String, Object> dados) {
        // Implementar cadastro de notificações se necessário
        return ResponseEntity.ok().build();
    }
}