package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by reddy on 9/8/15.
 */
@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermissionId implements Serializable {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE", nullable = false)
	private Role role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PERMISSION", nullable = false)
	private Permission permission;

	public RolePermissionId() {

	}

	public RolePermissionId(Role role, Permission permission) {
		this.role = role;
		this.permission = permission;
	}

}