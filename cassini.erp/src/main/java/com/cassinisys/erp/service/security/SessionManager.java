package com.cassinisys.erp.service.security;

import com.cassinisys.erp.config.api.APIKeyGenerator;
import com.cassinisys.erp.model.security.ERPSession;
import com.cassinisys.erp.repo.security.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reddy on 7/28/15.
 */
public class SessionManager {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SessionManager.class);

    private static SessionManager INSTANCE = null;
    private SessionRepository sessionRepository = null;
    private Map<Integer, ERPSession> erpSessions = new HashMap<Integer, ERPSession>();


    private SessionManager(){};

    public static SessionManager get() {
        if(INSTANCE == null) {
            INSTANCE = new SessionManager();
        }

        return INSTANCE;
    }

    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void addSession(ERPSession session) {
        erpSessions.put(session.getId(), session);
    }

    public void removeSession(ERPSession session) {
        if(session != null && session.getLogin() != null) {
            LOGGER.info("Logging out session: " + session.getId());
            session.setLogoutTime(new Date());
            sessionRepository.save(session);
            erpSessions.remove(session.getId());

            String apiKey = session.getApiKey();
            if(apiKey != null) {
                LOGGER.info("Removing API key: " + apiKey);
                APIKeyGenerator.get().remove(apiKey);
            }
        }
    }

    public void invalidateAllSessions() {
        LOGGER.info("System is shutting down. Logging out every one.");

        if(sessionRepository != null) {
            Collection<ERPSession> sessions = erpSessions.values();
            for (ERPSession session : sessions) {
                removeSession(session);
            }
        }
    }
}
