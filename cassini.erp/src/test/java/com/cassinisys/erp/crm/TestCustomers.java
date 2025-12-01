package com.cassinisys.erp.crm;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.config.TenantManager;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.model.crm.ERPCustomer;
import com.cassinisys.erp.repo.common.ObjectGeoLocationRepository;
import com.cassinisys.erp.service.crm.CustomerService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by reddy on 9/29/15.
 */
public class TestCustomers extends BaseTest {

    @Autowired
    private ObjectGeoLocationRepository objectGeoLocationRepository;

    @Autowired
    private CustomerService customerService;

    @BeforeClass
    public static void init() {
        TenantManager.get().setTenantId("cassini");
    }

    @Test
    public void testRadiusSearch() throws Exception {
        List<Object[]> customers = objectGeoLocationRepository.radiusSearch(17.448766, 78.68155, 5000f, ObjectType.CUSTOMER.getType());
        for(Object[] customer : customers) {
            System.out.println(customer[0]);
        }
    }

    @Test
    public void testBoxSearch() throws Exception {
        List<Object[]> customers = objectGeoLocationRepository.boxSearch(17.471473919387385, 78.74679314770515,
                17.274877766269213,78.18786370434577, ObjectType.CUSTOMER.getType());
        for(Object[] customer : customers) {
            System.out.println(customer[0]);
        }
    }

    @Test
    public void testCustomerByNameAndRegion(){
        Boolean aBoolean = customerService.getCustomerByNameAndRegion("Spring School","kukatpally");
        System.out.println(aBoolean);
    }
}
