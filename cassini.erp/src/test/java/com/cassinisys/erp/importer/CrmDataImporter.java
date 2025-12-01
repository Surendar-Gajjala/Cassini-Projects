package com.cassinisys.erp.importer;

import com.cassinisys.erp.model.crm.ERPVehicle;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.repo.crm.VehicleRepository;
import com.cassinisys.erp.repo.security.LoginRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;

/**
 * Created by reddy on 10/28/15.
 */
@Component
public class CrmDataImporter {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private ERPLogin login = null;


    @Test
    @Rollback(false)
    public void importCrmData() throws Exception {
        System.out.println();

        login = loginRepository.findByLoginName("admin");

        importVehicles();

        System.out.println();
    }

    private void importVehicles() throws Exception {
        String[][] vehicles = {
                {"TS08UA2351", "ISUZU"},
                {"TS08UA2355", "ISUZU"},
                {"AP28TA4610", "MAHINDRA BOLERO"},
                {"AP29V9872", "TATA AC"},
                {"AP29W1051", "TATA ZIP"},
                {"AP29W1052", "TATA ZIP"},
                {"AP29V7840", "ASHOK LEYLAND"},
            };

        for(String[] vehicleData : vehicles) {
            ERPVehicle vehicle = new ERPVehicle();
            vehicle.setNumber(vehicleData[0]);
            vehicle.setDescription(vehicleData[1]);

            vehicle.setCreatedBy(login.getPerson().getId());
            vehicle.setModifiedBy(login.getPerson().getId());

            vehicleRepository.save(vehicle);
        }
    }
}
