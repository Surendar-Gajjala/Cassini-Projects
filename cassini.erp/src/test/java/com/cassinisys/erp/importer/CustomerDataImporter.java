package com.cassinisys.erp.importer;

import com.cassinisys.erp.model.common.*;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.crm.*;
import com.cassinisys.erp.model.hrm.ERPDepartment;
import com.cassinisys.erp.model.hrm.ERPEmployeeType;
import com.cassinisys.erp.model.hrm.EmployeeStatus;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.model.security.ERPLoginRole;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.model.security.LoginRoleId;
import com.cassinisys.erp.repo.common.*;
import com.cassinisys.erp.repo.crm.*;
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
 * Created by reddy on 8/21/15.
 */
@Component
public class CustomerDataImporter {
    private String DATA_FILE = "/Users/reddy/MyHome/CassiniSys/Customers/DPPL/Data/customers_salesreps_regions_with_geo.csv";

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    private SalesRegionRepository salesRegionRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private ObjectGeoLocationRepository objectGeoLocationRepository;

    @Autowired
    private LoginRoleRepository loginRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginRepository loginRepository;

    private ERPLogin login;

    @Test
    @Rollback(false)
    public void importCustomerData() throws Exception {

        login = loginRepository.findByLoginName("admin");

        CSVParser parser = new CSVParser(new FileReader(DATA_FILE), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        long totalCount = records.size();


        System.out.println();
        for (CSVRecord csvRecord : records) {
            if(csvRecord.getRecordNumber() == 1) continue;;

            String msg = MessageFormat.format("Processing {0} of {1}", csvRecord.getRecordNumber()-1, totalCount-1);
            System.out.println(msg);


            ERPSalesRep salesRep = getSalesRep(csvRecord);
            salesRepRepository.save(salesRep);


            ERPSalesRegion salesRegion = getSalesRegion(csvRecord);
            salesRegionRepository.save(salesRegion);

            ERPCustomer customer = getCustomer(csvRecord);
            ERPPerson customerContact = getCustomerContactPerson(csvRecord);
            if(customerContact != null) {
                customer.setContactPerson((customerContact));
            }
            customer.setSalesRegion(salesRegion);
            customer.setSalesRep(salesRep);


            ERPObjectGeoLocation geo = getGeoLocation(csvRecord);
            if(geo != null) {
                customer.setGeoLocation(geo);
                geo.setObject(customer);
            }
            customerRepository.save(customer);
        }
        System.out.println();
    }

    private ERPSalesRep getSalesRep(CSVRecord record) {
        String firstName = record.get(11);

        ERPSalesRep salesRep = salesRepRepository.findByFirstName(firstName);
        if(salesRep == null) {
            salesRep = new ERPSalesRep();

            ERPPersonType personType = personTypeRepository.findByName("Employee");
            ERPEmployeeType employeeType = employeeTypeRepository.findByName("Full Time");
            ERPDepartment dept = departmentRepository.findByName("Sales");

            salesRep.setDepartment(dept);
            salesRep.setPersonType(personType);
            salesRep.setEmployeeType(employeeType);

            salesRep.setFirstName(firstName);
            salesRep.setJobTitle("Sales Rep");
            salesRep.setStatus(EmployeeStatus.ACTIVE);

            salesRep = salesRepRepository.save(salesRep);

            createSalesRepLogin(salesRep);
        }


        return salesRep;
    }

    private void createSalesRepLogin(ERPSalesRep salesRep) {
        ERPLogin newLogin = new ERPLogin();
        newLogin.setLoginName(salesRep.getFirstName().split(" ")[0].trim().toLowerCase());
        newLogin.setPassword(BCrypt.hashpw("cassini", BCrypt.gensalt()));
        newLogin.setPerson(salesRep);

        newLogin.setCreatedBy(login.getPerson().getId());
        newLogin.setModifiedBy(login.getPerson().getId());

        newLogin = loginRepository.save(newLogin);

        ERPLoginRole loginRole = new ERPLoginRole();
        ERPRole role = roleRepository.findByName("Sales Executive");
        loginRole.setId(new LoginRoleId(newLogin, role));
        loginRoleRepository.save(loginRole);
    }

    private ERPSalesRegion getSalesRegion(CSVRecord record) {
        ERPSalesRegion salesRegion = null;

        String name = record.get(7);
        String district = record.get(8);

        if(name != null && district != null) {
            salesRegion = salesRegionRepository.findByNameAndDistrict(name, district);
            if(salesRegion == null) {
                salesRegion = new ERPSalesRegion();
                salesRegion.setName(name);
                salesRegion.setDistrict(district);

                String c = record.get(10);
                String s = record.get(9);

                ERPCountry country = countryRepository.findByName("India");
                ERPState state = stateRepository.findByNameAndCountry(s, country);

                salesRegion.setCountry(country);
                salesRegion.setState(state);
                salesRegion.setDistrict(district);
            }
        }

        return salesRegion;
    }

    private ERPPerson getCustomerContactPerson(CSVRecord record) {
        ERPPerson customerContact = null;

        String firstName = record.get(3);
        if(firstName != null && !firstName.trim().isEmpty()) {
            ERPPersonType personType = personTypeRepository.findByName("Customer");
            customerContact = personRepository.findByFirstNameAndPersonType(firstName, personType);

            if (customerContact == null) {
                customerContact = new ERPPerson();
                customerContact.setPersonType(personType);
                customerContact.setFirstName(firstName);

                String phone = record.get(5);
                if(phone != null) {
                    customerContact.setPhoneOffice(phone);
                }

                phone = record.get(6);
                if(phone != null) {
                    customerContact.setPhoneMobile(phone);
                }

                customerContact = personRepository.save(customerContact);
            }
        }

        return customerContact;
    }

    private ERPCustomer getCustomer(CSVRecord record) {
        String name = record.get(0);
        String type = record.get(2);

        ERPCustomerType customerType = customerTypeRepository.findByName(type);
        if(customerType == null) {
            customerType = new ERPCustomerType();
            customerType.setName(type);
            customerType.setDescription(type);
            customerType = customerTypeRepository.save(customerType);
        }

        ERPCustomer customer = new ERPCustomer();
        customer.setName(name);
        customer.setCustomerType(customerType);

        String phone = record.get(1);
        if(phone != null) {
            customer.setOfficePhone(phone);
        }

        return customer;
    }

    private ERPObjectGeoLocation getGeoLocation(CSVRecord record) {
        String s = record.get(13);
        if(s != null && !s.trim().isEmpty()) {
            String[] arr = s.split(",");
            if(arr.length == 2) {
                ERPObjectGeoLocation geo = new ERPObjectGeoLocation();
                geo.setObjectType(ObjectType.CUSTOMER);
                geo.setLatitude(Double.parseDouble(arr[0].trim()));
                geo.setLongitude(Double.parseDouble(arr[1].trim()));
                return geo;
            }
        }

        return null;
    }

}
