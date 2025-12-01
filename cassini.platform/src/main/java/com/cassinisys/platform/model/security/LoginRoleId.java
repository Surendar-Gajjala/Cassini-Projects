package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.core.Login;
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
public class LoginRoleId implements Serializable {


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOGIN", nullable = false)
	private Login login;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE", nullable = false)
	private Role role;

	public LoginRoleId() {

	}

	public LoginRoleId(Login login, Role role) {
		this.login = login;
		this.role = role;
	}

}
