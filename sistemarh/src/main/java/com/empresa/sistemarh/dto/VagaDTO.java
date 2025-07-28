package com.empresa.sistemarh.dto;

import com.empresa.sistemarh.model.Area;
import com.empresa.sistemarh.model.StatusVaga;
import com.empresa.sistemarh.model.Vaga;
import java.time.LocalDateTime;

public class VagaDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusVaga status;
    private LocalDateTime dataCriacao;
    private Area area;
    private long candidatosCount;
    private long candidatosChamadosCount;

    // Construtores
    public VagaDTO() {}

    public VagaDTO(Vaga vaga, long candidatosCount, long candidatosChamadosCount) {
        this.id = vaga.getId();
        this.titulo = vaga.getTitulo();
        this.descricao = vaga.getDescricao();
        this.status = vaga.getStatus();
        this.dataCriacao = vaga.getDataCriacao();
        this.area = vaga.getArea();
        this.candidatosCount = candidatosCount;
        this.candidatosChamadosCount = candidatosChamadosCount;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusVaga getStatus() { return status; }
    public void setStatus(StatusVaga status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public long getCandidatosCount() { return candidatosCount; }
    public void setCandidatosCount(long candidatosCount) { this.candidatosCount = candidatosCount; }

    public long getCandidatosChamadosCount() { return candidatosChamadosCount; }
    public void setCandidatosChamadosCount(long candidatosChamadosCount) { this.candidatosChamadosCount = candidatosChamadosCount; }
}