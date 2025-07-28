package com.zen.auth.common.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Feature> features = new HashSet<>();
    
    
    
    @Column(name = "modulekey", nullable = false, unique = true)
    private String moduleKey;

    public String getModuleKey() { return moduleKey; }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}

	public Module() {}

    public Module(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setName(String name) {
        this.name = name;
    }
}
