package com.cassinisys.plm.migration;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.service.exim.exporter.Exporter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class TestExporter extends BaseTest {
    @Autowired
    private Exporter exporter;

    @Autowired
    private ObjectRepository objectRepository;

    @Test
    public void testExporter() throws Exception {
        String fname = "/Users/reddy/Downloads/cassini-plm-exportData.zip";
        FileOutputStream fos = new FileOutputStream(fname);
        ZipOutputStream zout = new ZipOutputStream(fos);
        String[] entities = {
                "autonumbers",
                "lovs",
                "lifecycles",
                "classification",
                "groups",
                "relationships",
                "workflows"
        };
        List<String> processed = new ArrayList<>();
        exporter.exportData(zout, null, entities);
        zout.close();
        fos.flush();
    }

    @Test
    public void testObjects() throws Exception {
        TenantManager.get().setTenantId("emm");
        List<CassiniObject> objects = objectRepository.findAll();
        for (CassiniObject object : objects) {
            System.out.println(object.getClass().getName());
        }
    }
}
