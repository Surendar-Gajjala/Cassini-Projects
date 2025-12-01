package com.cassinisys.tm;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.tm.config.TMConfig;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by reddy on 6/26/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TMConfig.class})
@WebAppConfiguration
@Transactional
public abstract class BaseTest {

    @BeforeClass
    public static void init() {
        //String tenant = System.getProperty("cassini.app.tenant");
        String tenant = "scr";
        if(tenant == null) {
            throw new RuntimeException("Tenant id is required");
        }
        TenantManager.get().setTenantId(tenant);
    }

}

