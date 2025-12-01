package com.cassinisys.is;

import com.cassinisys.is.config.ISConfig;
import com.cassinisys.is.service.login.LoginService;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.common.PersonService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.util.List;

/**
 * Created by Nageshreddy on 15-02-2018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ISConfig.class})
@WebAppConfiguration
@Transactional
public class LoginImporter extends BaseTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PersonService personService;

    @Test
    @Rollback(false)
    public void importLoginData() throws Exception {

        String DATA_FILE = FILE_PATH + "admin.csv";
        CSVParser parser = new CSVParser(new FileReader(DATA_FILE), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;
            Person person = new Person();
            person.setFirstName(csvRecord.get(5));
            person.setPersonType(1);
            person.setEmail(csvRecord.get(6));
            person.setPhoneMobile(csvRecord.get(8));
            person.setPhoneOffice(csvRecord.get(9));

            person = personService.create(person);

            Login login1 = loginRepository.findByLoginName(csvRecord.get(1));

            if (login1 == null) {
                Login login = new Login();
                login.setId(Integer.parseInt(csvRecord.get(0)));
                login.setPerson(person);
                login.setIsActive(Boolean.TRUE);
                login.setLoginName(csvRecord.get(1));
                login.setPassword("cassini");

                String createName = csvRecord.get(13);

                login.setCreatedBy(getPersonId(createName));

                String modifiedName = csvRecord.get(15);
                login.setModifiedBy(getPersonId(modifiedName));
                String createDate = csvRecord.get(11);
                login.setCreatedDate(getDateFormat(createDate, "dd-MM-yyyy"));
                String modifiedDate = csvRecord.get(12);
                login.setModifiedDate(getDateFormat(modifiedDate, "dd-MM-yyyy"));

                loginService.create(login);
            }
        }
    }
}
