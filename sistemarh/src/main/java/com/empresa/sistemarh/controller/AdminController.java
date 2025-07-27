package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.model.Area;
import com.empresa.sistemarh.model.StatusVaga;
import com.empresa.sistemarh.model.Vaga;
import com.empresa.sistemarh.service.AreaService;
import com.empresa.sistemarh.service.CandidatoService;
import com.empresa.sistemarh.service.EventoService;
import com.empresa.sistemarh.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private VagaService vagaService;

    @Autowired
    private CandidatoService candidatoService;

    @Autowired
    private EventoService eventoService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // === ÁREA CONTROLLERS ===
    @GetMapping("/areas")
    public String areas(Model model) {
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("novaArea", new Area());
        return "admin/areas";
    }

    @PostMapping("/areas")
    public String salvarArea(@ModelAttribute Area area, RedirectAttributes redirectAttributes) {
        try {
            areaService.salvar(area);
            redirectAttributes.addFlashAttribute("sucesso", "Área cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/areas";
    }

    @PostMapping("/areas/{id}/editar")
    public String editarArea(@PathVariable Long id, @ModelAttribute Area area, RedirectAttributes redirectAttributes) {
        try {
            areaService.atualizar(id, area);
            redirectAttributes.addFlashAttribute("sucesso", "Área atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/areas";
    }

    @PostMapping("/areas/{id}/deletar")
    public String deletarArea(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            areaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Área deletada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/areas";
    }

    // === VAGA CONTROLLERS ===
    @GetMapping("/vagas")
    public String vagas(Model model) {
        model.addAttribute("vagas", vagaService.listarTodas());
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("novaVaga", new Vaga());
        return "admin/vagas";
    }

    @PostMapping("/vagas")
    public String salvarVaga(@ModelAttribute Vaga vaga, RedirectAttributes redirectAttributes) {
        try {
            vagaService.salvar(vaga);
            redirectAttributes.addFlashAttribute("sucesso", "Vaga cadastrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar vaga: " + e.getMessage());
        }
        return "redirect:/admin/vagas";
    }

    @GetMapping("/vagas/{id}/editar")
    public String editarVagaForm(@PathVariable Long id, Model model) {
        Vaga vaga = vagaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaga não encontrada"));

        model.addAttribute("vaga", vaga);
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("candidatos", candidatoService.listarPorVaga(id));
        return "admin/editar-vaga";
    }

    @PostMapping("/vagas/{id}/editar")
    public String editarVaga(@PathVariable Long id, @ModelAttribute Vaga vaga, RedirectAttributes redirectAttributes) {
        try {
            vagaService.atualizar(id, vaga);
            redirectAttributes.addFlashAttribute("sucesso", "Vaga atualizada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/vagas/" + id + "/editar";
    }

    @PostMapping("/vagas/{id}/inativar")
    public String inativarVaga(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vagaService.alterarStatus(id, StatusVaga.INATIVA);
            redirectAttributes.addFlashAttribute("sucesso", "Vaga inativada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/vagas";
    }

    @PostMapping("/vagas/{id}/contratar")
    public String contratarVaga(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vagaService.alterarStatus(id, StatusVaga.CONTRATADA);
            redirectAttributes.addFlashAttribute("sucesso", "Vaga marcada como contratada!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/vagas";
    }

    @PostMapping("/vagas/{id}/deletar")
    public String deletarVaga(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vagaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Vaga deletada com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/vagas";
    }

    @GetMapping("/vagas/{vagaId}/candidatos")
    @ResponseBody
    public String filtrarCandidatos(@PathVariable Long vagaId, @RequestParam(required = false) String nome) {
        // Este método será usado via AJAX para filtrar candidatos
        return "redirect:/admin/vagas/" + vagaId + "/editar";
    }

    // === EVENTO CONTROLLERS ===
    @GetMapping("/eventos")
    public String eventos(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "admin/eventos";
    }

    @PostMapping("/eventos")
    public String criarEvento(@RequestParam String titulo,
                              @RequestParam(required = false) MultipartFile imagemCapa,
                              RedirectAttributes redirectAttributes) {
        try {
            var evento = eventoService.criarEvento(titulo, imagemCapa);
            redirectAttributes.addFlashAttribute("sucesso", "Evento criado com sucesso!");
            return "redirect:/admin/eventos/" + evento.getId() + "/editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao criar evento: " + e.getMessage());
            return "redirect:/admin/eventos";
        }
    }

    @GetMapping("/eventos/{id}/editar")
    public String editarEventoForm(@PathVariable Long id, Model model) {
        var evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        model.addAttribute("evento", evento);
        model.addAttribute("imagens", eventoService.listarImagensEvento(id));
        return "admin/editar-evento";
    }

    @PostMapping("/eventos/{id}/editar")
    public String editarEvento(@PathVariable Long id, @RequestParam String descricao,
                               RedirectAttributes redirectAttributes) {
        try {
            eventoService.atualizarEvento(id, descricao);
            redirectAttributes.addFlashAttribute("sucesso", "Evento atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/eventos/" + id + "/editar";
    }

    @PostMapping("/eventos/{id}/imagens")
    public String adicionarImagemEvento(@PathVariable Long id,
                                        @RequestParam MultipartFile imagem,
                                        RedirectAttributes redirectAttributes) {
        try {
            eventoService.adicionarImagemEvento(id, imagem);
            redirectAttributes.addFlashAttribute("sucesso", "Imagem adicionada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao adicionar imagem: " + e.getMessage());
        }
        return "redirect:/admin/eventos/" + id + "/editar";
    }

    @PostMapping("/eventos/{id}/deletar")
    public String deletarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.deletarEvento(id);
            redirectAttributes.addFlashAttribute("sucesso", "Evento deletado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/eventos";
    }
}