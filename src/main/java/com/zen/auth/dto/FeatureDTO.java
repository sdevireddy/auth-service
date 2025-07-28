package com.zen.auth.dto;

public class FeatureDTO {
    private Long id;
    private String name;
    private String description;
    private Long moduleId;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getModuleId() {
        return moduleId;
    }
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
}
