package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.service.CandidatoService;
import com.empresa.sistemarh.service.EventoService;
import com.empresa.sistemarh.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/api")
public class ApiController {

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EventoService eventoService;

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

    @GetMapping("/notificacoes")
    public ResponseEntity<Object[]> getNotificacoes() {
        // Implementar lógica de notificações
        return ResponseEntity.ok(new Object[0]);
    }

    @GetMapping("/vagas/{vagaId}/candidatos")
    public ResponseEntity<Object> getCandidatos(@PathVariable Long vagaId,
                                                @RequestParam(required = false) String filtro) {
        try {
            var candidatos = candidatoService.buscarPorVagaENome(vagaId, filtro);
            return ResponseEntity.ok(candidatos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar candidatos");
        }
    }
}