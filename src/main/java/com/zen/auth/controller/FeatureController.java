package com.zen.auth.controller;

import com.zen.auth.dto.FeatureDTO;
import com.zen.auth.services.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @PostMapping("/bulk")
    public List<FeatureDTO> createFeatures(@RequestBody List<FeatureDTO> features) {
        return featureService.createFeatures(features);
    }

    @GetMapping
    public List<FeatureDTO> getAllFeatures() {
        return featureService.getAllFeatures();
    }
}

