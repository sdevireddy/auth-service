package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Permissions;
import com.zen.auth.repository.PermissionRepository;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired private PermissionRepository permissionRepository;

    @GetMapping
    public List<Permissions> getPermissions() {
        return permissionRepository.findAll();
    }

    @PostMapping
    public Permissions createPermission(@RequestBody Permissions permission) {
        return permissionRepository.save(permission);
    }
}
