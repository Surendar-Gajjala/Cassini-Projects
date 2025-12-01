package com.cassinisys.erp.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by reddy on 9/8/15.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRoleId implements Serializable {
    @ApiObjectField(required = true)
    private ERPLogin login;
    @ApiObjectField(required = true)
    private ERPRole role;

    public LoginRoleId() {

    }

    public LoginRoleId(ERPLogin login, ERPRole role) {
        this.login = login;
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOGIN", nullable = false)
    public ERPLogin getLogin() {
        return login;
    }

    public void setLogin(ERPLogin login) {
        this.login = login;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE", nullable = false)
    public ERPRole getRole() {
        return role;
    }

    public void setRole(ERPRole role) {
        this.role = role;
    }
}
