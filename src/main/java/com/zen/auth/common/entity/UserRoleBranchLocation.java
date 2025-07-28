package com.zen.auth.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_role_branch_location")
public class UserRoleBranchLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ZenUser user;

    @ManyToOne
    private Roles roles;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Location location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZenUser getUser() {
		return user;
	}

	public void setUser(ZenUser user) {
		this.user = user;
	}

	public Roles getRole() {
		return roles;
	}

	public void setRole(Roles role) {
		this.roles = roles;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


}
