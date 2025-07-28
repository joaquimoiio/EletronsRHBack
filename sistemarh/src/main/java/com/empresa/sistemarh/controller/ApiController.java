package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.dto.VagaDTO;
import com.empresa.sistemarh.model.*;
import com.empresa.sistemarh.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EventoService eventoService;

    // === ESTATÍSTICAS ===
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        Map<String, Object> stats = new HashMap<>();

        List<Area> areas = areaService.listarTodas();
        List<Vaga> vagasAtivas = vagaService.listarVagasAtivas();
        List<Evento> eventos = eventoService.listarTodos();

        // Contar total de candidatos usando o método do service
        long totalCandidatos = candidatoService.contarTotalCandidatos();

        stats.put("totalAreas", areas.size());
        stats.put("vagasAtivas", vagasAtivas.size());
        stats.put("totalEventos", eventos.size());
        stats.put("totalCandidatos", totalCandidatos);

        return ResponseEntity.ok(stats);
    }

    // === ÁREAS ===
    @GetMapping("/areas")
    public ResponseEntity<List<Area>> listarAreas() {
        return ResponseEntity.ok(areaService.listarTodas());
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<Area> buscarArea(@PathVariable Long id) {
        return areaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/areas")
    public ResponseEntity<?> criarArea(@RequestBody Map<String, String> request) {
        try {
            String nome = request.get("nome");
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }

            Area area = new Area(nome.trim());
            Area areaSalva = areaService.salvar(area);
            return ResponseEntity.ok(areaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/areas/{id}")
    public ResponseEntity<?> atualizarArea(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String nome = request.get("nome");
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }

            Area area = new Area(nome.trim());
            Area areaAtualizada = areaService.atualizar(id, area);
            return ResponseEntity.ok(areaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/areas/{id}")
    public ResponseEntity<?> deletarArea(@PathVariable Long id) {
        try {
            areaService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === VAGAS ===
    @GetMapping("/vagas")
    public ResponseEntity<List<VagaDTO>> listarTodasVagas() {
        List<Vaga> vagas = vagaService.listarTodas();
        List<VagaDTO> vagasDTO = vagas.stream()
                .map(vaga -> {
                    long candidatosCount = candidatoService.contarCandidatosPorVaga(vaga.getId());
                    long candidatosChamadosCount = candidatoService.contarCandidatosChamadosPorVaga(vaga.getId());
                    return new VagaDTO(vaga, candidatosCount, candidatosChamadosCount);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(vagasDTO);
    }

    @GetMapping("/vagas/ativas")
    public ResponseEntity<List<Vaga>> listarVagasAtivas() {
        return ResponseEntity.ok(vagaService.listarVagasAtivas());
    }

    @GetMapping("/vagas/{id}")
    public ResponseEntity<Vaga> buscarVaga(@PathVariable Long id) {
        return vagaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/vagas")
    public ResponseEntity<?> criarVaga(@RequestBody Map<String, Object> request) {
        try {
            String titulo = (String) request.get("titulo");
            String descricao = (String) request.get("descricao");
            Map<String, Object> areaMap = (Map<String, Object>) request.get("area");

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
            String descricao = (String) request.get("descricao");
            Map<String, Object> areaMap = (Map<String, Object>) request.get("area");

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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === CANDIDATOS ===
    @GetMapping("/vagas/{vagaId}/candidatos")
    public ResponseEntity<List<Candidato>> listarCandidatos(@PathVariable Long vagaId,
                                                            @RequestParam(required = false) String filtro,
                                                            @RequestParam(required = false) String status) {
        List<Candidato> candidatos;

        if (status != null && !status.isEmpty()) {
            StatusCandidato statusCandidato = StatusCandidato.valueOf(status.toUpperCase());
            candidatos = candidatoService.buscarPorVagaEStatus(vagaId, statusCandidato, filtro);
        } else {
            candidatos = candidatoService.buscarPorVagaENome(vagaId, filtro);
        }

        return ResponseEntity.ok(candidatos);
    }

    @GetMapping("/vagas/{vagaId}/candidatos/count")
    public ResponseEntity<Map<String, Long>> contarCandidatos(@PathVariable Long vagaId) {
        long count = candidatoService.contarCandidatosPorVaga(vagaId);
        long countChamados = candidatoService.contarCandidatosChamadosPorVaga(vagaId);

        Map<String, Long> response = new HashMap<>();
        response.put("total", count);
        response.put("chamados", countChamados);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/candidaturas")
    public ResponseEntity<?> criarCandidatura(@RequestParam String nome,
                                              @RequestParam String email,
                                              @RequestParam Long vagaId,
                                              @RequestParam(required = false) MultipartFile curriculo) {
        try {
            Candidato candidato = candidatoService.inscreverCandidato(vagaId, nome, email, curriculo);
            return ResponseEntity.ok(candidato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/candidatos/{candidatoId}/chamar")
    public ResponseEntity<?> chamarCandidatoParaEntrevista(@PathVariable Long candidatoId) {
        try {
            Candidato candidato = candidatoService.chamarParaEntrevista(candidatoId);
            return ResponseEntity.ok(candidato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/candidatos/{candidatoId}/status")
    public ResponseEntity<?> alterarStatusCandidato(@PathVariable Long candidatoId,
                                                    @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            StatusCandidato novoStatus = StatusCandidato.valueOf(status.toUpperCase());
            Candidato candidato = candidatoService.alterarStatusCandidato(candidatoId, novoStatus);
            return ResponseEntity.ok(candidato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === EVENTOS ===
    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> listarEventos() {
        return ResponseEntity.ok(eventoService.listarTodos());
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<Evento> buscarEvento(@PathVariable Long id) {
        return eventoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/eventos")
    public ResponseEntity<?> criarEvento(@RequestParam String titulo,
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable Long id) {
        try {
            eventoService.deletarEvento(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/eventos/{id}/imagens")
    public ResponseEntity<List<ImagemEvento>> listarImagensEvento(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.listarImagensEvento(id));
    }

    @PostMapping("/eventos/{id}/imagens")
    public ResponseEntity<?> adicionarImagemEvento(@PathVariable Long id,
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === NOTIFICAÇÕES ===
    @PostMapping("/notificacoes")
    public ResponseEntity<?> criarNotificacao(@RequestBody Map<String, Object> request) {
        // Implementar se necessário - por enquanto apenas retorna sucesso
        return ResponseEntity.ok().build();
    }
}