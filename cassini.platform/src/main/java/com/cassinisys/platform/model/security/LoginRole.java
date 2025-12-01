package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "LOGINROLE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRole {
	@EmbeddedId
	private LoginRoleId id;

}
