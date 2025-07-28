package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.RoleModulePermission;
import com.zen.auth.repository.RoleModulePermissionRepository;

@RestController
@RequestMapping("/role-module-permissions")
public class RoleModulePermissionController {

    @Autowired private RoleModulePermissionRepository repo;

    @GetMapping
    public List<RoleModulePermission> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public RoleModulePermission create(@RequestBody RoleModulePermission permission) {
        return repo.save(permission);
    }
}

