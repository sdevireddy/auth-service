package com.zen.auth.common.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "role_feature_permissions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "feature_id", "permission_id"})
})
public class RoleFeaturePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permissions permission;

    public Long getId() { return id; }

    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }

    public Feature getFeature() { return feature; }
    public void setFeature(Feature feature) { this.feature = feature; }

    public Permissions getPermission() { return permission; }
    public void setPermission(Permissions permission) { this.permission = permission; }
}

