package com.cassinisys.platform.service.security;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.model.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by reddy on 7/28/15.
 */
@WebListener
@Component
public class SessionListener implements HttpSessionListener {
    private Logger LOGGER = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String id = httpSessionEvent.getSession().getId();
        String tenantId = (String) httpSessionEvent.getSession().getAttribute("CASSINI_TENANT_IDENTIFIER");
        Session session = (Session) httpSessionEvent.getSession().getAttribute("CASSINI_SESSION");
        if(tenantId != null && session != null) {
            TenantManager.get().setTenantId(tenantId);
            SessionManager.get().removeSession(session);
        }
    }
}
