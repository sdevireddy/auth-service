package com.zen.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zen.auth.common.entity.Branch;
import com.zen.auth.repository.BranchRepository;

@RestController
@RequestMapping("/branches")
public class BranchController {
    @Autowired private BranchRepository branchRepository;

    @GetMapping
    public List<Branch> getBranches() {
        return branchRepository.findAll();
    }

    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) {
        return branchRepository.save(branch);
    }
}
