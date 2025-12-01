package com.cassinisys.plm.item;

import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.service.exim.importer.ItemImporter;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemImporterTest extends BaseTest {
    @Autowired
    private ItemImporter itemImporter;
    @Autowired
    private SecurityService securityService;

    @BeforeAll
    public static void setup() throws Exception {

    }

    @AfterAll
    public static void tearDown() throws Exception {
    }

    @Test
    public void testCreateItemTypeByPath() throws Exception {
    }
}
