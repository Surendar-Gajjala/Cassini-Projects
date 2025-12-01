package com.cassinisys.platform.service.security;

import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.repo.security.SessionRepository;
import com.cassinisys.platform.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

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
    private Map<Integer, Session> sessions = new HashMap<Integer, Session>();

    private ThreadLocal<Session> threadSession = new ThreadLocal<>();

    private SessionManager(){}

    public static SessionManager get() {
        if(INSTANCE == null) {
            INSTANCE = new SessionManager();
        }

        return INSTANCE;
    }

    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void addSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    @Transactional
    public void removeSession(Session session) {
        if(sessionRepository == null) {
            sessionRepository = BeanUtil.getBean(SessionRepository.class);
        }
        if(sessionRepository == null) return;

        if(session != null && session.getLogin() != null) {
            session.setLogoutTime(new Date());
            sessionRepository.save(session);
            sessions.remove(session.getSessionId());
            session = null;
        }
    }

    public void invalidateAllSessions() {
        LOGGER.info("System is shutting down. Logging out every one.");

        if(sessionRepository != null) {
            Collection<Session> sessions = this.sessions.values();
            for (Session session : sessions) {
                removeSession(session);
            }
        }
    }

    public Session getThreadSession() {
        return threadSession.get();
    }

    public void setThreadSession(Session threadSession) {
        this.threadSession.set(threadSession);
    }
}
