package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.pqm.PQMCustomer;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class CustomersImporter {

    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PersonRepository personRepository;

    Map<String, Person> dbPersonMap = new LinkedHashMap();

    public void importCustomers(TableData tableData) throws ParseException {
        dbPersonMap = new LinkedHashMap();
        Map<String, PQMCustomer> dbCustomerMap = new LinkedHashMap();
        List<Person> dbPersons = personRepository.findAll();
        dbPersonMap = dbPersons.stream().collect(Collectors.toMap(x -> x.getFirstName() + x.getLastName(), x -> x));
        List<Person> persons = createPersons(tableData, dbPersonMap);
        for (Person person : persons) {
            dbPersonMap.put(person.getFirstName() + person.getLastName(), person);
        }
        List<PQMCustomer> dbCustomers = pqmCustomerRepository.findAll();
        dbCustomerMap = dbCustomers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        List<PQMCustomer> customers = createCustomers(tableData, dbCustomerMap);
    }


    private List<Person> createPersons(TableData tableData, Map<String, Person> dbPersonMap) {
        List<Person> persons = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Contact Person First Name")) {
                String firstName = stringListHashMap.get("Contact Person First Name");
                String lastName = stringListHashMap.get("Contact Person Last Name");
                if (firstName != null && !firstName.trim().equals("")) {
                    Person person = dbPersonMap.get(firstName + lastName);
                    if (person != null) {
                        persons.add(person);
                    } else {
                        Person person1 = createPerson(i, stringListHashMap);
                        persons.add(person1);
                    }


                }
            } else {
                throw new CassiniException("Please provide Name and Contact Person First Name Column also");
            }
            i++;
        }

        if (persons.size() > 0) {
            persons = personRepository.save(persons);
        }
        return persons;
    }

    private List<PQMCustomer> createCustomers(TableData tableData, Map<String, PQMCustomer> dbCustomerMap) {
        List<PQMCustomer> customers = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Name") && stringListHashMap.containsKey("Contact Person First Name")) {
                String name = stringListHashMap.get("Name");
                if (name != null && !name.trim().equals("")) {
                    PQMCustomer customer = dbCustomerMap.get(name);
                    if (customer != null) {
                        customers.add(customer);
                    } else {
                        PQMCustomer pqmCustomer = createCustomer(i, name, stringListHashMap);
                        customers.add(pqmCustomer);
                        dbCustomerMap.put(pqmCustomer.getName(), pqmCustomer);
                    }


                } else {
                    customers.add(null);
                }
            } else {
                throw new CassiniException("Please provide Name and Contact Person First Name Column also");
            }
            i++;
        }

        if (customers.size() > 0) {
            customers = pqmCustomerRepository.save(customers);
        }
        return customers;
    }

    private PQMCustomer createCustomer(Integer i, String name, RowData stringListHashMap) {
        PQMCustomer customer = new PQMCustomer();
        String phoneNumber = stringListHashMap.get("Phone Number");
        String address = stringListHashMap.get("Address");
        String emailAddress = stringListHashMap.get("E-Mail");
        String notes = stringListHashMap.get("Notes");
        String firstName = stringListHashMap.get("Contact Person First Name");
        String lastName = stringListHashMap.get("Contact Person Last Name");
        String phoneNo = stringListHashMap.get("Contact Person Phone Number");
        String email = stringListHashMap.get("Contact Person E-Mail");
        Person personObject = new Person();
        personObject.setFirstName(firstName);
        personObject.setLastName(lastName);
        personObject.setPhoneMobile(phoneNo);
        personObject.setPersonType(1);
        personObject.setEmail(email);
        customer.setPerson(personObject);
        Person person = dbPersonMap.get(firstName + lastName);
       /* if (customer.getPerson().getId() == null) {
            Person existPerson1 = dbPersonMap.get(customer.getPerson().getFirstName() + customer.getPerson().getLastName());
            if (existPerson1 != null) {
                String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existPerson1.getFirstName() + " " + existPerson1.getLastName());
                throw new CassiniException(result);
            }
        }*/
        //Person person = personRepository.save(customer.getPerson());
        //dbPersonMap.put(person.getFirstName() + person.getLastName(), person);
        customer.setContactPerson(person.getId());
        customer.setName(name);
        customer.setPhone(phoneNumber);
        customer.setAddress(address);
        customer.setNotes(notes);
        customer.setEmail(emailAddress);


        return customer;
    }

    private Person createPerson(Integer i, RowData stringListHashMap) {
        String firstName = stringListHashMap.get("Contact Person First Name");
        String lastName = stringListHashMap.get("Contact Person Last Name");
        String phoneNo = stringListHashMap.get("Contact Person Phone Number");
        String email = stringListHashMap.get("Contact Person E-Mail");
        Person personObject = new Person();
        personObject.setFirstName(firstName);
        personObject.setLastName(lastName);
        personObject.setPhoneMobile(phoneNo);
        personObject.setPersonType(1);
        personObject.setEmail(email);
        Person existPerson1 = dbPersonMap.get(firstName + lastName);
        if (existPerson1 != null) {
            String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existPerson1.getFirstName() + " " + existPerson1.getLastName());
            throw new CassiniException(result);
        }
        return personObject;
    }

}
