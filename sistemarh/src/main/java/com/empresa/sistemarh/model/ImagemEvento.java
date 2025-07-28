package com.empresa.sistemarh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagens_evento")
public class ImagemEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(name = "caminho_arquivo", nullable = false)
    private String caminhoArquivo;

    // Construtores
    public ImagemEvento() {}

    public ImagemEvento(Evento evento, String nomeArquivo, String caminhoArquivo) {
        this.evento = evento;
        this.nomeArquivo = nomeArquivo;
        this.caminhoArquivo = caminhoArquivo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
}