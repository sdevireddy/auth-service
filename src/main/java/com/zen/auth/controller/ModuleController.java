package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Module;
import com.zen.auth.repository.ModuleRepository;

@RestController
@RequestMapping("/modules")
public class ModuleController {
    @Autowired private ModuleRepository moduleRepository;

    @GetMapping
    public List<Module> getModules() {
        return moduleRepository.findAll();
    }

    @PostMapping
    public Module createModule(@RequestBody Module module) {
        return moduleRepository.save(module);	
    }
}
