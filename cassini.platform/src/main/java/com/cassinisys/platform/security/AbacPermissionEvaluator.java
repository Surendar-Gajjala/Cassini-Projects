package com.cassinisys.platform.security;

import com.cassinisys.platform.model.core.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class AbacPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private PolicyEnforcement policy;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object action) {
        Login user = (Login) authentication.getPrincipal();
        Map<String, Object> environment = new HashMap<>();
        environment.put("time", new Date());
       return user.getIsAdmin() || policy.checkObject(user, targetDomainObject, action, environment);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objId, String action, Object attrDef) {
        Login user = (Login)authentication.getPrincipal();
        Map<String, Object> environment = new HashMap<>();
        environment.put("time", new Date());
        Integer objectId = (Integer) objId;
        return user.getIsAdmin() || policy.checkAttribute(user, objectId, attrDef, action, environment);
    }

}