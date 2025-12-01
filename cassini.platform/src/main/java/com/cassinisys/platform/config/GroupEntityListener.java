package com.cassinisys.platform.config;

import com.cassinisys.platform.service.security.SecurityService;
import org.springframework.stereotype.Component;

/**
 * Created by lakshmi on 10/31/2016.
 */

@Component
public class GroupEntityListener {

    private static SecurityService securityService;

    /*@PostLoad
    public void postLoad(PersonGroup group) {
        if(securityService != null) {
            securityService.loadGroupPermissions(group);
        }
    }


    @Autowired(required = true)
    @Qualifier("securityService")
    public void setSecurityService(SecurityService securityService) {
        GroupEntityListener.securityService = securityService;
    }*/
}
