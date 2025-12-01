package com.cassinisys.erp.service.security;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.cassinisys.erp.model.security.ERPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        ERPSession erpSession = (ERPSession) httpSessionEvent.getSession().getAttribute("CASSINI_SESSION");
        SessionManager.get().removeSession(erpSession);
    }
}
