package com.empresa.sistemarh.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    private final String UPLOAD_DIR = "uploads/";

    public String salvarArquivo(MultipartFile arquivo, String subdiretorio) throws IOException {
        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode estar vazio");
        }

        // Criar diretório se não existir
        Path uploadPath = Paths.get(UPLOAD_DIR + subdiretorio);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gerar nome único para o arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeUnico = UUID.randomUUID().toString() + extensao;

        // Salvar arquivo
        Path caminhoArquivo = uploadPath.resolve(nomeUnico);
        Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

        return subdiretorio + "/" + nomeUnico;
    }

    public void deletarArquivo(String caminhoArquivo) {
        try {
            Path arquivo = Paths.get(UPLOAD_DIR + caminhoArquivo);
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            // Log do erro mas não falha a operação
            System.err.println("Erro ao deletar arquivo: " + e.getMessage());
        }
    }
}