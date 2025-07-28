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

        area.setNome(areaAtualizada.getNome());
        return areaRepository.save(area);
    }

    public void deletar(Long id) {
        if (!areaRepository.existsById(id)) {
            throw new IllegalArgumentException("Área não encontrada");
        }
        areaRepository.deleteById(id);
    }
}