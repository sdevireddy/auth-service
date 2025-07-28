package com.zen.auth.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.persistence.*;


import java.time.Instant;

@Entity
@Table(name = "record_permissions")
public class RecordPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_type", length = 50)
    private String recordType;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "permission_type")
    private String permissionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ZenUser user;

    @Column(name = "granted_by")
    private Long grantedBy;

    @Column(name = "created_at", updatable = false, insertable = false)
    private Instant createdAt;

    public RecordPermission() {}

    public RecordPermission(ZenUser user, String recordType, Long recordId, String permissionType) {
        this.user = user;
        this.recordType = recordType;
        this.recordId = recordId;
        this.permissionType = permissionType;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public ZenUser getUser() {
        return user;
    }

    public void setUser(ZenUser user) {
        this.user = user;
    }

    public Long getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(Long grantedBy) {
        this.grantedBy = grantedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

	public void setId(Long id) {
		this.id = id;
	}
    
    
}
