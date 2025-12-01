package com.cassinisys.is;

import com.cassinisys.is.config.ISConfig;
import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 6/26/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ISConfig.class})
@WebAppConfiguration
@Transactional
public abstract class BaseTest {

    public String FILE_PATH = "F:\\railways\\";
    ZoneId defaultZoneId = ZoneId.systemDefault();
    @Autowired
    private PersonRepository personRepository;

    @BeforeClass
    public static void init() {
        String tenant = System.getProperty("cassini.app.tenant");
        tenant = "cassini_is";
        if (tenant == null) {
            throw new RuntimeException("Tenant id is required");
        }
        TenantManager.get().setTenantId(tenant);
    }

    public Integer getPersonId(String person) {
        if (!person.equals("")) {
            List<Person> person1 = personRepository.findByFirstName(person);
            if (person1 != null && person1.size() > 0) {
                return person1.get(0).getId();
            }
        } else {
            return 1;
        }
        return 1;
    }

    public Date getDateFormat(String date, String format) {
        if (!date.equals("")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
            Date date1 = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
            return date1;
        } else {
            return new Date();
        }
    }

}