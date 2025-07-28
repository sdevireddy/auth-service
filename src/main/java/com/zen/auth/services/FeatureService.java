package com.zen.auth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zen.auth.common.entity.Feature;
import com.zen.auth.common.entity.Module;
import com.zen.auth.dto.FeatureDTO;
import com.zen.auth.repository.FeatureRepository;
import com.zen.auth.repository.ModuleRepository;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<FeatureDTO> createFeatures(List<FeatureDTO> dtos) {
        List<Feature> features = dtos.stream().map(dto -> {
            Feature f = new Feature();
            f.setName(dto.getName());
            f.setDescription(dto.getDescription());

            Module module = moduleRepository.findById(dto.getModuleId())
                    .orElseThrow(() -> new RuntimeException("Module not found: " + dto.getModuleId()));
            f.setModule(module);

            return f;
        }).collect(Collectors.toList());

        List<Feature> saved = featureRepository.saveAll(features);
        return saved.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FeatureDTO> getAllFeatures() {
        return featureRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FeatureDTO convertToDTO(Feature f) {
        FeatureDTO dto = new FeatureDTO();
        dto.setId(f.getId());
        dto.setName(f.getName());
        dto.setDescription(f.getDescription());
        dto.setModuleId(f.getModule().getId());
        return dto;
    }
}
