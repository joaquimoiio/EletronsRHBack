package com.empresa.sistemarh.model;

public enum StatusCandidato {
    INSCRITO("Inscrito"),
    CHAMADO("Chamado para Entrevista"),
    REJEITADO("Rejeitado");

    private final String descricao;

    StatusCandidato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}