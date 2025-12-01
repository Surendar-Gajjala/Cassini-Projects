package com.cassinisys.erp.config;

import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;

/**
 * Created by reddy on 9/10/15.
 */
@Component
public class LoginEntityListener {
    private static SecurityService securityService;

    @PostLoad
    public void postLoad(ERPLogin login) {
        if(securityService != null) {
            securityService.loadLoginRoles(login);
        }
    }

    @Autowired(required = true)
    @Qualifier("securityService")
    public void setSecurityService(SecurityService securityService) {
        LoginEntityListener.securityService = securityService;
    }
}
