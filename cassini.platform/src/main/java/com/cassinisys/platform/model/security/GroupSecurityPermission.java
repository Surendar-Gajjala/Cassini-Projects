package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lakshmi on 10/18/2016.
 */

@Entity
@Table(name = "GROUP_SECURITY_PERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class GroupSecurityPermission {

    @EmbeddedId
    private GroupSecurityPermissionId id;

}