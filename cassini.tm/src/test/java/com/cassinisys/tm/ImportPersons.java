package com.cassinisys.tm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.tm.model.*;
import com.cassinisys.tm.repo.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by reddy on 9/1/16.
 */
public class ImportPersons extends BaseTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Autowired
    private PersonOtherInfoRepository otherInfoRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentPersonRepository departmentPersonRepository;

    @Test
    @Rollback(false)
    public void importPersons() throws Exception {
        String fileName = "/Users/reddy/Downloads/persons.csv";
        CSVParser parser = new CSVParser(new FileReader(fileName),
                CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();

        System.out.println();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
            System.out.println(msg);

            String name = csvRecord.get(0);
            String role = csvRecord.get(1);
            String devision = csvRecord.get(2);
            String dept = csvRecord.get(3);
            String designation = csvRecord.get(4);
            String phoneMobile = csvRecord.get(5);
            String phoneOffice = csvRecord.get(6);
            String email = csvRecord.get(7);
            String parentWorkUnit = csvRecord.get(8);
            String reportingOfficer = csvRecord.get(9);
            String reportingOfficerContact = csvRecord.get(10);
            String bloodGroup = csvRecord.get(11);
            String medicalProblems = csvRecord.get(12);

            if(name != null && !name.trim().isEmpty()) {
                String [] arr = name.split(",");
                String fName = arr[0].trim();
                String lName = arr[1].trim();

                Person person = personRepository.findByFirstNameAndLastName(fName, lName);
                if(person == null) {
                    person = new Person();
                    person.setFirstName(fName);
                    person.setLastName(lName);
                    person.setPersonType(1);
                    person.setPhoneMobile(phoneMobile);
                    person.setPhoneOffice(phoneOffice);
                    person.setEmail(email);
                    person = personRepository.save(person);
                }

                TMDepartment department = departmentRepository.findByName(dept);
                if(department == null) {
                    department = new TMDepartment();
                    department.setName(dept);
                    department.setDescription(dept);
                    department = departmentRepository.save(department);
                }

                TMDepartmentPerson deptPerson = departmentPersonRepository.findByPersonAndDepartment(person.getId(), department.getId());
                if(deptPerson == null) {
                    deptPerson = new TMDepartmentPerson();
                    deptPerson.setDepartment(department.getId());
                    deptPerson.setPerson(person.getId());
                    deptPerson = departmentPersonRepository.save(deptPerson);

                }

                TMEmergencyContact emergencyContact = emergencyContactRepository.findByPerson(person.getId());
                if(emergencyContact == null) {
                    emergencyContact = new TMEmergencyContact();
                    emergencyContact.setPersonType(5);
                    emergencyContact.setPerson(person.getId());
                    emergencyContact.setFirstName("NA");
                    emergencyContact.setLastName("NA");
                    emergencyContact.setRelation("NA");
                    emergencyContact.setPhoneMobile("NA");
                    emergencyContact = emergencyContactRepository.save(emergencyContact);
                }

                TMPersonOtherInfo otherInfo = otherInfoRepository.findByPerson(person.getId());
                if(otherInfo == null) {
                    otherInfo = new TMPersonOtherInfo();

                    otherInfo.setPerson(person.getId());
                    otherInfo.setBloodGroup(bloodGroup);
                    otherInfo.setControllingOfficer(reportingOfficer);
                    otherInfo.setControllingOfficerContact(reportingOfficerContact);
                    otherInfo.setDepartment(department.getId());
                    otherInfo.setRole(role);
                    otherInfo.setDevision(devision);
                    otherInfo.setDesignation(designation);
                    otherInfo.setMedicalProblems(medicalProblems);
                    otherInfo.setParentUnit(parentWorkUnit);

                    otherInfo = otherInfoRepository.save(otherInfo);
                }
            }


        }
    }
}
