package com.cassinisys.erp.print;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.config.TenantManager;
import com.cassinisys.erp.service.print.PrintService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by reddy on 10/1/15.
 */
public class TestPrintService extends BaseTest {

    @Autowired
    private PrintService printService;


    @BeforeClass
    public static void init() {
        TenantManager.get().setTenantId("dppl");
    }

    @Test
    public void testPrintOrder() throws Exception {
        System.out.println(printService.printOrder(null));
    }

}
