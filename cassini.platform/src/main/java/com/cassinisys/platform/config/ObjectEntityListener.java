package com.cassinisys.platform.config;

import com.cassinisys.platform.events.CassiniObjectEvents;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.repo.activitystream.ActivityStreamObjectRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.service.security.SessionManager;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 8/23/15.
 */

@Component
public class ObjectEntityListener {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ActivityStreamObjectRepository activityStreamObjectRepository;

    @PrePersist
    public void prePersist(CassiniObject object) {
        Session session = getCurrentSession();

        if (session != null) {
            Integer personId = session.getLogin().getPerson().getId();
            if (object.getId() == null) {
                object.setCreatedDate(new Date());
                object.setCreatedBy(personId);
            }
            object.setModifiedBy(personId);
            object.setModifiedDate(new Date());
        }
    }

    @PostPersist
    public void postPersist(CassiniObject object) {
        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new CassiniObjectEvents.CassiniObjectCreatedEvent(object));
        }
    }

    @PreUpdate
    public void preUpdate(CassiniObject object) {
        Session session = getCurrentSession();

        if (session != null) {
            Integer personId = session.getLogin().getPerson().getId();
            object.setModifiedBy(personId);
            object.setModifiedDate(new Date());
        }
    }

    @PostUpdate
    public void postUpdate(CassiniObject object) {
        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new CassiniObjectEvents.CassiniObjectUpdatedEvent(object));
        }
    }

    @PreRemove
    public void preRemove(CassiniObject object) {
        if (commentRepository != null) commentRepository.deleteCommentByObjectId(object.getId());
        if (activityStreamObjectRepository != null) {
            activityStreamObjectRepository.deleteActivityStreamObjectByObjectId(object.getId());
        }
    }

    @PostRemove
    public void postRemove(CassiniObject object) {
        if (applicationEventPublisher != null) {
            applicationEventPublisher.publishEvent(new CassiniObjectEvents.CassiniObjectDeletedEvent(object));
        }
    }

    private Session getCurrentSession() {
        Session session  = SessionManager.get().getThreadSession();
        if (session == null) {
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
