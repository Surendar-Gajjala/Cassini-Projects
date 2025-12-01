package com.cassinisys.irste.config;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class IRSTEWebListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent he) {
        he.getSession().setAttribute("CASSINI_TENANT_IDENTIFIER", "cassini_irste");
    }

    public void sessionDestroyed(HttpSessionEvent he) {

    }
}
