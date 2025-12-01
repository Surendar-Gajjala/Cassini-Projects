package com.cassinisys.plm.config;

import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
@Component
public class PLMWebListener implements HttpSessionListener {
    public void sessionCreated(HttpSessionEvent he) {
        String defaultSchema = PLMConfig.schema;
        he.getSession().setAttribute("CASSINI_TENANT_IDENTIFIER", defaultSchema);
    }

    public void sessionDestroyed(HttpSessionEvent he) {
    }
}
