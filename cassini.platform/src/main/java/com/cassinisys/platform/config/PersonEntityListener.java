package com.cassinisys.platform.config;

import com.cassinisys.platform.model.common.Person;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;

@Component
public class PersonEntityListener {

    @PostLoad
    public void postLoad(Person person) {
        person.setHasImage(person.getImage() != null);
    }
}
