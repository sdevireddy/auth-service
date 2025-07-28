package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Module;
import com.zen.auth.common.entity.TenantModule;
import com.zen.auth.repository.ModuleRepository;
import com.zen.auth.repository.TenantModuleRepository;

@RestController
@RequestMapping("/tenantmodules")
public class TenantModuleController {
    @Autowired private TenantModuleRepository tenantModuleRepository;

    @GetMapping
    public List<TenantModule> getModules() {
        return tenantModuleRepository.findAll();
    }

    @PostMapping
    public TenantModule createModule(@RequestBody TenantModule module) {
        return tenantModuleRepository.save(module);	
    }
}
