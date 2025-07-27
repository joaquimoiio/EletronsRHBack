package com.empresa.sistemarh.service;

import com.empresa.sistemarh.model.Area;
import com.empresa.sistemarh.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public List<Area> listarTodas() {
        return areaRepository.findAll();
    }

    public Optional<Area> buscarPorId(Long id) {
        return areaRepository.findById(id);
    }

    public Area salvar(Area area) {
        if (areaRepository.existsByNome(area.getNome())) {
            throw new IllegalArgumentException("Já existe uma área com este nome");
        }
        return areaRepository.save(area);
    }

    public Area atualizar(Long id, Area areaAtualizada) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Área não encontrada"));

        // Verificar se o novo nome já existe (exceto para a própria área)
        Optional<Area> areaExistente = areaRepository.findByNome(areaAtualizada.getNome());
        if (areaExistente.isPresent() && !areaExistente.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe uma área com este nome");
        }

        area.setNome(areaAtualizada.getNome());
        return areaRepository.save(area);
    }

    public void deletar(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Área não encontrada"));

        if (area.getVagas() != null && !area.getVagas().isEmpty()) {
            throw new IllegalArgumentException("Não é possível deletar área que possui vagas cadastradas");
        }

        areaRepository.delete(area);
    }
}