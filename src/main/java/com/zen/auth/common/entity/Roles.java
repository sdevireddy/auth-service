package com.zen.auth.common.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
@Entity(name = "roles")
public class Roles {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_module_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permissions> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoleFeaturePermission> featurePermissions = new HashSet<>();
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FieldPermission> fieldPermissions = new HashSet<>();
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_bundle_assignments",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "bundle_id")
    )
    private Set<PermissionBundle> permissionBundles = new HashSet<>();

    public Set<PermissionBundle> getPermissionBundles() {
        return permissionBundles;
    }

    public void setPermissionBundles(Set<PermissionBundle> permissionBundles) {
        this.permissionBundles = permissionBundles;
    }
    
    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public void setFeaturePermissions(Set<RoleFeaturePermission> featurePermissions) {
        this.featurePermissions = featurePermissions;
    }
    
    public Set<Permissions> getPermissions() {
        return permissions;
    }


    public Set<RoleFeaturePermission> getFeaturePermissions() {
        return featurePermissions;
    }

    public Set<FieldPermission> getFieldPermissions() {
        return fieldPermissions;
    }

    public void setFieldPermissions(Set<FieldPermission> fieldPermissions) {
        this.fieldPermissions = fieldPermissions;
    }
    

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
}
