package com.cassinisys.erp.scripting;

import com.cassinisys.erp.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Created by reddy on 9/14/15.
 */
public class TestScripting extends BaseTest {
    @Autowired
    private ScriptingEngine scriptingEngine;

    @Value("classpath:reports/crm/customerOrders.groovy")
    private Resource scriptResource;


    @Test
    public void testScripting() throws Exception {
        Object o = scriptingEngine.executeScript(new ResourceScriptSource(scriptResource));
        System.out.println(o);
    }
}
