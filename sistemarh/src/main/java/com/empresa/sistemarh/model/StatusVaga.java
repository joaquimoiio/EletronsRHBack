package com.empresa.sistemarh.model;

public enum StatusVaga {
    ATIVA("Ativa"),
    INATIVA("Inativa"),
    CONTRATADA("Contratada");

    private final String descricao;

    StatusVaga(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}