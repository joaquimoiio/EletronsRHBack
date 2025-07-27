package com.empresa.sistemarh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vagas")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título da vaga é obrigatório")
    @Column(nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusVaga status = StatusVaga.ATIVA;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "vaga", cascade = CascadeType.ALL)
    private List<Candidato> candidatos;

    // Construtores
    public Vaga() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Vaga(String titulo, Area area, String descricao) {
        this();
        this.titulo = titulo;
        this.area = area;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusVaga getStatus() { return status; }
    public void setStatus(StatusVaga status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public List<Candidato> getCandidatos() { return candidatos; }
    public void setCandidatos(List<Candidato> candidatos) { this.candidatos = candidatos; }
}