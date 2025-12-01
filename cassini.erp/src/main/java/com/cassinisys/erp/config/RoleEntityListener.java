package com.cassinisys.erp.config;

import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;

/**
 * Created by reddy on 9/10/15.
 */
@Component
public class RoleEntityListener {

    private static SecurityService securityService;

    @PostLoad
    public void postLoad(ERPRole role) {
        if(securityService != null) {
            securityService.loadRolePermissions(role);
        }
    }


    @Autowired(required = true)
    @Qualifier("securityService")
    public void setSecurityService(SecurityService securityService) {
        RoleEntityListener.securityService = securityService;
    }
}
