package com.cassinisys.erp.query;

import com.cassinisys.erp.model.crm.QERPCustomer;
import com.cassinisys.erp.repo.crm.CustomerOrderDetailsRepository;
import com.mysema.query.BooleanBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by reddy on 05/02/16.
 */
public class TestQueryDSL {

    @Autowired
    CustomerOrderDetailsRepository orderDetailsRepository;

    @Test
    public void testNestedPredicates() throws Exception {
        BooleanBuilder builder = new BooleanBuilder();
        QERPCustomer pathBase = QERPCustomer.eRPCustomer;

        BooleanBuilder builderInner = new BooleanBuilder();

        String q = "mno";
        builder.andAnyOf(pathBase.name.containsIgnoreCase(q), pathBase.salesRep.isNotNull().and(pathBase.salesRep.firstName.containsIgnoreCase(q)));

        System.out.println(builder.toString());

    }

    @Test
    public void testProbemOrders() throws Exception {

    }
}
