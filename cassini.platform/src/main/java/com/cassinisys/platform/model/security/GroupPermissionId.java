package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.common.PersonGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by lakshmi on 10/18/2016.
 */

@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupPermissionId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSONGROUP", nullable = false)
    private PersonGroup group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERMISSION", nullable = false)
    private Permission permission;

    public GroupPermissionId() {

    }

    public GroupPermissionId(PersonGroup group, Permission permission) {
        this.group = group;
        this.permission = permission;
    }

}