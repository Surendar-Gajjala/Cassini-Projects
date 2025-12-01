package com.cassinisys.platform.config;

import com.cassinisys.platform.service.security.SecurityService;
import org.springframework.stereotype.Component;

/**
 * Created by reddy on 9/10/15.
 */
@Component
public class RoleEntityListener {

    private static SecurityService securityService;

   /* @PostLoad
    public void postLoad(Role role) {
        if(securityService != null) {
            securityService.loadRolePermissions(role);
        }
    }


    @Autowired(required = true)
    @Qualifier("securityService")
    public void setSecurityService(SecurityService securityService) {
        RoleEntityListener.securityService = securityService;
    }*/
}
