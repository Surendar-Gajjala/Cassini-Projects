package com.cassinisys.erp.exporter;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.model.hrm.ERPEmployee;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.repo.hrm.EmployeeRepository;
import com.cassinisys.erp.repo.security.LoginRepository;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.List;

/**
 * Created by reddy on 11/8/15.
 */
public class DataExporter extends BaseTest{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LoginRepository loginRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private ObjectNode rootNode;


    @Test
    @Rollback(false)
    public void exportData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.createObjectNode();

        rootNode.putPOJO("employees", employeeRepository.findAll());
        rootNode.putPOJO("logins", loginRepository.findAll());

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("/Users/reddy/cassini.erp.json"), rootNode);
    }

    @Test
    @Rollback(false)
    public void importData() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ExporterJson root = mapper.readValue(new File("/Users/reddy/cassini.erp.json"), ExporterJson.class);


        List<ERPLogin> logins = root.getLogins();
        for(ERPLogin login : logins) {
            entityManager.merge(login);
        }

        List<ERPEmployee> employees = root.getEmployees();
        for(ERPEmployee emp : employees) {
            entityManager.merge(emp);
        }
        //employeeRepository.save(employees);
        //loginRepository.save(logins);
    }

}
