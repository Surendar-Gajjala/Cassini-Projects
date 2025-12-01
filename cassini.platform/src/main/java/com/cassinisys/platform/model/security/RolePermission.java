package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Data
@Table(name = "ROLEPERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermission {
	
    @EmbeddedId
    private RolePermissionId id;

}
