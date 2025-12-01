package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class AutonumbersExporter extends AbstractExporter {
    @Autowired
    private AutoNumberRepository autoNumberRepository;

    public AutonumbersExporter() {
        super();
    }

    public void exportData(ZipOutputStream zout) {
        try {
            List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
            String json = getObjectMapper().writeValueAsString(autoNumbers);
            zout.putNextEntry(new ZipEntry("autonumbers.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
