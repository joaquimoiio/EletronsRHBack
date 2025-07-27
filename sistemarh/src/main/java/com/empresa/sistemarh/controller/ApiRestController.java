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
@CrossOrigin(origins = "*") // Para desenvolvimento - remover em produção
public class ApiRestController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EventoService eventoService;

    // === ENDPOINTS DE ÁREAS ===
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listarAreas() {
        return ResponseEntity.ok(areaService.listarTodas());
    }

    @PostMapping("/areas")
    public ResponseEntity<?> criarArea(@RequestBody Map<String, String> request) {
        try {
            String nome = request.get("nome");
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome da área é obrigatório");
            }

            Area area = new Area(nome.trim());
            Area areaSalva = areaService.salvar(area);
            return ResponseEntity.ok(areaSalva);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<?> atualizarArea(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String nome = request.get("nome");
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome da área é obrigatório");
            }

            Area area = new Area(nome.trim());
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

    // === ENDPOINTS DE VAGAS ===
    @GetMapping("/vagas")
    public ResponseEntity<List<Vaga>> listarTodasVagas() {
        return ResponseEntity.ok(vagaService.listarTodas());
    }

    @GetMapping("/vagas/ativas")
    public ResponseEntity<List<Vaga>> listarVagasAtivas() {
        return ResponseEntity.ok(vagaService.listarVagasAtivas());
    }

    @GetMapping("/vagas/{id}")
    public ResponseEntity<?> buscarVaga(@PathVariable Long id) {
        return vagaService.buscarPorId(id)
                .map(vaga -> ResponseEntity.ok(vaga))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vagas")
    public ResponseEntity<?> criarVaga(@RequestBody Map<String, Object> request) {
        try {
            String titulo = (String) request.get("titulo");
            Map<String, Object> areaMap = (Map<String, Object>) request.get("area");
            String descricao = (String) request.get("descricao");

            if (titulo == null || titulo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Título é obrigatório");
            }

            if (areaMap == null || areaMap.get("id") == null) {
                return ResponseEntity.badRequest().body("Área é obrigatória");
            }

            Long areaId = Long.valueOf(areaMap.get("id").toString());
            Area area = areaService.buscarPorId(areaId)
                    .orElseThrow(() -> new IllegalArgumentException("Área não encontrada"));

            Vaga vaga = new Vaga(titulo.trim(), area, descricao);
            Vaga vagaSalva = vagaService.salvar(vaga);
            return ResponseEntity.ok(vagaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/vagas/{id}")
    public ResponseEntity<?> atualizarVaga(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String titulo = (String) request.get("titulo");
            Map<String, Object> areaMap = (Map<String, Object>) request.get("area");
            String descricao = (String) request.get("descricao");

            if (titulo == null || titulo.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Título é obrigatório");
            }

            if (areaMap == null || areaMap.get("id") == null) {
                return ResponseEntity.badRequest().body("Área é obrigatória");
            }

            Long areaId = Long.valueOf(areaMap.get("id").toString());
            Area area = areaService.buscarPorId(areaId)
                    .orElseThrow(() -> new IllegalArgumentException("Área não encontrada"));

            Vaga vagaAtualizada = new Vaga(titulo.trim(), area, descricao);
            Vaga vaga = vagaService.atualizar(id, vagaAtualizada);
            return ResponseEntity.ok(vaga);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/vagas/{id}/status")
    public ResponseEntity<?> alterarStatusVaga(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            StatusVaga statusVaga = StatusVaga.valueOf(status);
            vagaService.alterarStatus(id, statusVaga);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
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

    // === ENDPOINTS DE CANDIDATOS ===
    @GetMapping("/candidatos")
    public ResponseEntity<List<Candidato>> listarTodosCandidatos() {
        // Implementar método no service se necessário
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/vagas/{vagaId}/candidatos")
    public ResponseEntity<List<Candidato>> listarCandidatosPorVaga(@PathVariable Long vagaId) {
        return ResponseEntity.ok(candidatoService.listarPorVaga(vagaId));
    }

    @PostMapping("/candidaturas")
    public ResponseEntity<?> criarCandidatura(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam Long vagaId,
            @RequestParam(required = false) MultipartFile curriculo) {
        try {
            Candidato candidato = candidatoService.inscreverCandidato(vagaId, nome, curriculo);
            return ResponseEntity.ok(candidato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === ENDPOINTS DE EVENTOS ===
    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<?> buscarEvento(@PathVariable Long id) {
        return eventoService.buscarPorId(id)
                .map(evento -> ResponseEntity.ok(evento))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/eventos")
    public ResponseEntity<?> criarEvento(
            @RequestParam String titulo,
            @RequestParam(required = false) MultipartFile imagemCapa) {
        try {
            Evento evento = eventoService.criarEvento(titulo, imagemCapa);
            return ResponseEntity.ok(evento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/eventos/{id}")
    public ResponseEntity<?> atualizarEvento(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String descricao = request.get("descricao");
            Evento evento = eventoService.atualizarEvento(id, descricao);
            return ResponseEntity.ok(evento);
        } catch (IllegalArgumentException e) {
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

    @GetMapping("/eventos/{id}/imagens")
    public ResponseEntity<List<ImagemEvento>> listarImagensEvento(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.listarImagensEvento(id));
    }

    @PostMapping("/eventos/{id}/imagens")
    public ResponseEntity<?> adicionarImagemEvento(
            @PathVariable Long id,
            @RequestParam MultipartFile imagem) {
        try {
            ImagemEvento imagemEvento = eventoService.adicionarImagemEvento(id, imagem);
            return ResponseEntity.ok(imagemEvento);
        } catch (Exception e) {
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

    // === ENDPOINTS DE NOTIFICAÇÕES ===
    @PostMapping("/notificacoes")
    public ResponseEntity<?> criarNotificacao(@RequestBody Map<String, Object> request) {
        // Implementar lógica de notificações se necessário
        return ResponseEntity.ok().build();
    }

    // === ENDPOINT DE ESTATÍSTICAS ===
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        Map<String, Object> stats = new HashMap<>();

        List<Vaga> todasVagas = vagaService.listarTodas();
        List<Vaga> vagasAtivas = vagaService.listarVagasAtivas();
        List<Evento> todosEventos = eventoService.listarTodos();
        List<Area> todasAreas = areaService.listarTodas();

        // Contar total de candidatos
        long totalCandidatos = todasVagas.stream()
                .mapToLong(vaga -> vaga.getCandidatos() != null ? vaga.getCandidatos().size() : 0)
                .sum();

        stats.put("totalVagas", todasVagas.size());
        stats.put("vagasAtivas", vagasAtivas.size());
        stats.put("totalCandidatos", totalCandidatos);
        stats.put("totalEventos", todosEventos.size());
        stats.put("totalAreas", todasAreas.size());

        return ResponseEntity.ok(stats);
    }
}