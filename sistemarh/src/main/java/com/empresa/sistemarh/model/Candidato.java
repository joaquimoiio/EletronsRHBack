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

    @Column
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaga_id", nullable = false)
    private Vaga vaga;

    @Column(name = "nome_arquivo_curriculo")
    private String nomeArquivoCurriculo;

    @Column(name = "caminho_curriculo")
    private String caminhoCurriculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCandidato status = StatusCandidato.INSCRITO;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDateTime dataInscricao = LocalDateTime.now();

    @Column(name = "data_chamada")
    private LocalDateTime dataChamada;

    // Construtores
    public Candidato() {}

    public Candidato(String nome, String email, String telefone, Vaga vaga) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.vaga = vaga;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Vaga getVaga() { return vaga; }
    public void setVaga(Vaga vaga) { this.vaga = vaga; }

    public String getNomeArquivoCurriculo() { return nomeArquivoCurriculo; }
    public void setNomeArquivoCurriculo(String nomeArquivoCurriculo) { this.nomeArquivoCurriculo = nomeArquivoCurriculo; }

    public String getCaminhoCurriculo() { return caminhoCurriculo; }
    public void setCaminhoCurriculo(String caminhoCurriculo) { this.caminhoCurriculo = caminhoCurriculo; }

    public StatusCandidato getStatus() { return status; }
    public void setStatus(StatusCandidato status) { this.status = status; }

    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(LocalDateTime dataInscricao) { this.dataInscricao = dataInscricao; }

    public LocalDateTime getDataChamada() { return dataChamada; }
    public void setDataChamada(LocalDateTime dataChamada) { this.dataChamada = dataChamada; }
}