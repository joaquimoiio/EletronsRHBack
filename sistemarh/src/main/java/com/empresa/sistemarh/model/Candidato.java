package com.empresa.sistemarh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatos")
public class Candidato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Column
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(name = "nome_arquivo_curriculo")
    private String nomeArquivoCurriculo;

    @Column(name = "caminho_curriculo")
    private String caminhoCurriculo;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao = LocalDateTime.now();

    // Construtores
    public Candidato() {}

    public Candidato(String nome, String email, Vaga vaga) {
        this.nome = nome;
        this.email = email;
        this.vaga = vaga;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Vaga getVaga() { return vaga; }
    public void setVaga(Vaga vaga) { this.vaga = vaga; }

    public String getNomeArquivoCurriculo() { return nomeArquivoCurriculo; }
    public void setNomeArquivoCurriculo(String nomeArquivoCurriculo) { this.nomeArquivoCurriculo = nomeArquivoCurriculo; }

    public String getCaminhoCurriculo() { return caminhoCurriculo; }
    public void setCaminhoCurriculo(String caminhoCurriculo) { this.caminhoCurriculo = caminhoCurriculo; }

    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }
}