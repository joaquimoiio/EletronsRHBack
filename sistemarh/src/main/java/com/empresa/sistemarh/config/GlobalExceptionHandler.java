package com.empresa.sistemarh.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc,
                                         RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("erro",
                "Arquivo muito grande! Tamanho máximo permitido: 10MB");
        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException exc,
                                        Model model) {
        model.addAttribute("erro", exc.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception exc, Model model) {
        model.addAttribute("erro", "Ocorreu um erro interno. Tente novamente.");
        return "error";
    }
}