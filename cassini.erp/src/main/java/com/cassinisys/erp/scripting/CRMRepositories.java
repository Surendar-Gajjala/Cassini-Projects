package com.cassinisys.erp.scripting;

import com.cassinisys.erp.repo.crm.CustomerOrderRepository;
import com.cassinisys.erp.repo.crm.CustomerRepository;
import com.cassinisys.erp.repo.crm.SalesRegionRepository;
import com.cassinisys.erp.repo.crm.SalesRepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by reddy on 9/14/15.
 */
@Component
public class CRMRepositories {
    @Autowired
    public CustomerRepository customer;
    @Autowired
    public CustomerOrderRepository order;
    @Autowired
    public SalesRepRepository salesRep;
    @Autowired
    public SalesRegionRepository salesRegion;
}
