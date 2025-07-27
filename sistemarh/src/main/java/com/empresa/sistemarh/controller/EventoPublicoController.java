package com.empresa.sistemarh.controller;

import com.empresa.sistemarh.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eventos")
public class EventoPublicoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping
    public String listarEventos(Model model) {
        model.addAttribute("eventos", eventoService.listarTodos());
        return "publico/eventos";
    }

    @GetMapping("/{id}")
    public String detalhesEvento(@PathVariable Long id, Model model) {
        var evento = eventoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        model.addAttribute("evento", evento);
        model.addAttribute("imagens", eventoService.listarImagensEvento(id));
        return "publico/detalhes-evento";
    }
}