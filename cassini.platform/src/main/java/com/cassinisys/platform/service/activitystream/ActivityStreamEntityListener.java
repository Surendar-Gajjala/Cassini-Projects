package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.config.ApplicationContextProvider;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.service.security.SessionManager;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import java.util.Date;

@Component
public class ActivityStreamEntityListener {
    @Autowired
    private SessionWrapper sessionWrapper;

    @PrePersist
    public void prePersist(ActivityStream as) {
        Session session = getCurrentSession();

        if(session != null) {
            as.setActor(session.getLogin().getPerson());
            as.setTimestamp(new Date());
            as.setSession(session.getSessionId());
        }
    }

    private Session getCurrentSession() {
        Session session = null;

        session = SessionManager.get().getThreadSession();

        if(session == null) {
            if (sessionWrapper == null) {
                ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
            }

            if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                session = sessionWrapper.getSession();
            }
        }

        return session;
    }
}
