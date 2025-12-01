package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class LifecyclesExporter extends AbstractExporter {
    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    public LifecyclesExporter() {
        super();
    }

    public void exportData(ZipOutputStream zout) {
        try {
            List<PLMLifeCycle> lcps = lifeCycleRepository.findAll();
            String json = getObjectMapper().writeValueAsString(lcps);
            zout.putNextEntry(new ZipEntry("lifecycles.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
