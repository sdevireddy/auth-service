package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Roles;
import com.zen.auth.repository.RoleRepository;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired private RoleRepository roleRepository;

    @GetMapping
    public List<Roles> getRoles() {
        return roleRepository.findAll();
    }

    @PostMapping(name="/create")
    public Roles createRole(@RequestBody Roles role) {
        return roleRepository.save(role);
    }
}
