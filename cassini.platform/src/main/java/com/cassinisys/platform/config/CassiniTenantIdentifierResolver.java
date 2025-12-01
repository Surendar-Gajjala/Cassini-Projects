package com.cassinisys.platform.config;

import com.cassinisys.platform.service.security.SessionWrapper;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by reddy on 9/22/15.
 */
@Component("currentTenantIdentifierResolver")
public class CassiniTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    public String resolveCurrentTenantIdentifier() {
        if(TenantManager.get().getTenantId() != null) {
            return TenantManager.get().getTenantId();
        }

        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            String tenantId =  (String) request.getSession().getAttribute("CASSINI_TENANT_IDENTIFIER");
            if(tenantId != null) {
                return tenantId;
            }
        } catch (IllegalStateException e) {
        }


        try {
            if(sessionWrapper != null &&
                    sessionWrapper.getTenantId() != null &&
                    !sessionWrapper.getTenantId().isEmpty()) {
                return sessionWrapper.getTenantId();
            }
        } catch (Exception e) {
        }

        return "public";

    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}