package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.service.CandidatoService;
import com.empresa.sistemarh.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vagas")
public class VagaPublicaController {

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @GetMapping
    public String listarVagas(Model model) {
        model.addAttribute("vagas", vagaService.listarVagasAtivas());
        return "publico/vagas";
    }

    @GetMapping("/{id}")
    public String detalhesVaga(@PathVariable Long id, Model model) {
        var vaga = vagaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        model.addAttribute("vaga", vaga);
        return "publico/detalhes-vaga";
    }

    @PostMapping("/{id}/candidatar")
    public String candidatar(@PathVariable Long id,
                             @RequestParam String nome,
                             @RequestParam(required = false) MultipartFile curriculo,
                             RedirectAttributes redirectAttributes) {
        try {
            candidatoService.inscreverCandidato(id, nome, curriculo);
            redirectAttributes.addFlashAttribute("sucesso", "Candidatura realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao realizar candidatura: " + e.getMessage());
        }
        return "redirect:/vagas/" + id;
    }
}