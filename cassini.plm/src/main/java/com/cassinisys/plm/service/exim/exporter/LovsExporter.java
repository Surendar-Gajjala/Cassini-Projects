package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.core.LovRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class LovsExporter extends AbstractExporter {
    @Autowired
    private LovRepository lovRepository;

    public LovsExporter() {
        super();
    }

    public void exportData(ZipOutputStream zout) {
        try {
            List<Lov> lovs = lovRepository.findAll();
            String json = getObjectMapper().writeValueAsString(lovs);
            zout.putNextEntry(new ZipEntry("lovs.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
