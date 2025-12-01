package com.cassinisys.erp.importer;

import com.cassinisys.erp.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

/**
 * Created by reddy on 10/28/15.
 */
public class DataImporter extends BaseTest {
    @Autowired
    private CustomerDataImporter customerDataImporter;
    @Autowired
    private ProductDataImporter productDataImporter;
    @Autowired
    private ShipperDataImporter shipperDataImporter;
    @Autowired
    private HrmDataImporter hrmDataImporter;
    @Autowired
    private CrmDataImporter crmDataImporter;

    @Autowired
    private ERPAttendenceImporter attendenceImporter;

    @Test
    @Rollback(false)
    public void importTestData() throws Exception {
//        System.out.println("Importing HRM data..");
//        hrmDataImporter.importHrmData();
//
//        System.out.println("Importing CRM data..");
//        crmDataImporter.importCrmData();
//
//        System.out.println("Importing customer data..");
//        customerDataImporter.importCustomerData();

        System.out.println("Importing product data..");
        productDataImporter.importProducts();

//        System.out.println("Importing shipper data..");
//        shipperDataImporter.importShippers();

        //System.out.println("Importing ERP Employee Attendance data..");
        //attendenceImporter.importAttendenceData();
    }
}
