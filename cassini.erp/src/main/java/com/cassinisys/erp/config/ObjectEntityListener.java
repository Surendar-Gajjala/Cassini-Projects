package com.cassinisys.erp.config;

import java.util.Date;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cassinisys.erp.model.core.ERPObject;
import com.cassinisys.erp.service.security.SessionWrapper;

/**
 * Created by reddy on 8/23/15.
 */

@Component
public class ObjectEntityListener {

    @Autowired
    private SessionWrapper sessionWrapper;


    @PrePersist
    public void prePersist(ERPObject object) {
        if(sessionWrapper == null) {
            ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
        }

        if(sessionWrapper != null && sessionWrapper.getSession() != null) {
            Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
            if(object.getId() == null) {
                object.setCreatedDate(new Date());
                object.setCreatedBy(personId);
            }
            object.setModifiedBy(personId);
            object.setModifiedDate(new Date());
        }
    }

}
