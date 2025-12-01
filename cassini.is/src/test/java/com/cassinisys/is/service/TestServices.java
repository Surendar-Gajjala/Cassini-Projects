package com.cassinisys.is.service;

import com.cassinisys.is.BaseTest;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.common.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by reddy on 7/21/15.
 */
public class TestServices extends BaseTest {
    @Autowired
    private PersonService personService;


    @Test
    public void testPersonService() throws Exception {
        Person person = new Person();
        person.setCreatedDate(new Date());
        
        
        //person.setCreatedBy(1);
        person.setModifiedDate(new Date());
        //person.setModifiedBy(1);
        person.setPersonType(1);
        person.setObjectType(ObjectType.PERSON);

        person.setFirstName("John");
        person.setLastName("Doe");

        //personService.save(person);
    }
}
