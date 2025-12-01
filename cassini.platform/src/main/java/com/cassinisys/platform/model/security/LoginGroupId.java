package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.common.PersonGroup;
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
 * Created by lakshmi on 10/18/2016.
 */

@Embeddable
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginGroupId implements Serializable {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOGIN", nullable = false)
    private Login login;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSONGROUP", nullable = false)
    private PersonGroup group;

    public LoginGroupId() {

    }

    public LoginGroupId(Login login, PersonGroup group) {
        this.login = login;
        this.group = group;
    }

}