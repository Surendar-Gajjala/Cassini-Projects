package com.cassinisys.erp.importer;

import com.cassinisys.erp.model.common.ERPPersonType;
import com.cassinisys.erp.model.crm.ERPSalesRep;
import com.cassinisys.erp.model.hrm.ERPBusinessUnit;
import com.cassinisys.erp.model.hrm.ERPDepartment;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.hrm.ERPEmployeeType;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.model.security.ERPLoginRole;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.model.security.LoginRoleId;
import com.cassinisys.erp.repo.common.PersonTypeRepository;
import com.cassinisys.erp.repo.crm.SalesRepRepository;
import com.cassinisys.erp.repo.hrm.BusinessUnitRepository;
import com.cassinisys.erp.repo.hrm.DepartmentRepository;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.hrm.EmployeeTypeRepository;
import com.cassinisys.erp.repo.security.LoginRepository;
import com.cassinisys.erp.repo.security.LoginRoleRepository;
import com.cassinisys.erp.repo.security.RoleRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

import java.io.FileReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by reddy on 10/26/15.
 */
@Component
public class HrmDataImporter {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginRoleRepository loginRoleRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

    private ERPLogin login = null;


    @Test
    @Rollback(false)
    public void importHrmData() throws Exception {
        System.out.println();

        login = loginRepository.findByLoginName("admin");

        //importBusinessUnitData();
        importEmployeeData();
        //importDpplEmployeeData();

        System.out.println();
    }

    private void importBusinessUnitData() throws Exception {
        businessUnitRepository.deleteAll();

        String[] bus = {"Apple Book Company", "School Book Company", "Orange Leaf Publishers"};
        for(String bu : bus) {
            ERPBusinessUnit buObject = businessUnitRepository.findByName(bu);
            if(buObject == null) {
                buObject = new ERPBusinessUnit();

                buObject.setName(bu);
                buObject.setDescription(bu);
                buObject.setCreatedBy(login.getPerson().getId());
                buObject.setModifiedBy(login.getPerson().getId());

                businessUnitRepository.save(buObject);
            }
        }
    }

    private void importEmployeeData() throws Exception {
        CSVParser parser = new CSVParser(new FileReader("/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/logins.csv"),
                CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();


        System.out.println();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
            System.out.println(msg);

            ERPEmployee employee = new ERPEmployee();

            String jobTitle = csvRecord.get(3);
            if(jobTitle.equalsIgnoreCase("Sales Executive")) {
                employee = new ERPSalesRep();
            }

            employee.setFirstName(csvRecord.get(5));
            employee.setLastName(csvRecord.get(6));

            ERPPersonType personType = personTypeRepository.findByName("Employee");
            employee.setPersonType(personType);

            ERPEmployeeType empType = employeeTypeRepository.findByName(csvRecord.get(2));
            employee.setEmployeeType(empType);

            ERPDepartment dept = departmentRepository.findByName(csvRecord.get(4));
            employee.setDepartment(dept);

            employee.setJobTitle(jobTitle);

            employee.setCreatedBy(login.getPerson().getId());
            employee.setModifiedBy(login.getPerson().getId());

            if(jobTitle.equalsIgnoreCase("Sales Executive")) {
                employee = salesRepRepository.save((ERPSalesRep) employee);
            }
            else {
                employee = employeeRepository.save(employee);
            }


            ERPLogin newLogin = new ERPLogin();
            newLogin.setLoginName(csvRecord.get(0));
            newLogin.setPassword(BCrypt.hashpw("cassini", BCrypt.gensalt()));
            newLogin.setPerson(employee);

            newLogin.setCreatedBy(login.getPerson().getId());
            newLogin.setModifiedBy(login.getPerson().getId());

            newLogin = loginRepository.save(newLogin);

            ERPLoginRole loginRole = new ERPLoginRole();
            ERPRole role = roleRepository.findByName(csvRecord.get(1));
            loginRole.setId(new LoginRoleId(newLogin, role));
            loginRoleRepository.save(loginRole);

        }
    }

    private void importDpplEmployeeData() throws Exception {
        CSVParser parser = new CSVParser(new FileReader("/Users/reddy/Downloads/dppl-employees.csv"),
                CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();
        ERPEmployeeType empType = employeeTypeRepository.findByName("Full Time");


        System.out.println();
        for (CSVRecord csvRecord : records) {
            if (csvRecord.getRecordNumber() == 1) continue;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber() - 1, totalCount - 1);
            System.out.println(msg);

            ERPEmployee employee = new ERPEmployee();

            String empNumber = csvRecord.get(0);
            if(empNumber != null) {
                employee.setEmployeeNumber(empNumber);
            }

            String jobTitle = csvRecord.get(4);
            if(jobTitle.equalsIgnoreCase("Sales Executive")) {
                employee = new ERPSalesRep();
            }

            employee.setFirstName(csvRecord.get(1));
            employee.setLastName(csvRecord.get(2));

            ERPPersonType personType = personTypeRepository.findByName("Employee");
            employee.setPersonType(personType);

            employee.setEmployeeType(empType);

            ERPDepartment dept = departmentRepository.findByName(jobTitle);
            if(dept == null) {
                dept = new ERPDepartment();
                dept.setName(jobTitle);
                dept.setDescription(jobTitle);
                dept = departmentRepository.save(dept);
            }
            employee.setDepartment(dept);

            employee.setJobTitle(jobTitle);

            employee.setCreatedBy(login.getPerson().getId());
            employee.setModifiedBy(login.getPerson().getId());

            if(jobTitle.equalsIgnoreCase("Sales Executive")) {
                employee = salesRepRepository.save((ERPSalesRep) employee);
            }
            else {
                employee = employeeRepository.save(employee);
            }

        }
    }
}
