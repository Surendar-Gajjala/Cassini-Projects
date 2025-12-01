package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.service.common.PersonGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class GroupsExporter extends AbstractExporter {
    @Autowired
    private PersonGroupService personGroupService;

    @Override
    public void exportData(ZipOutputStream zout) {
        try {
            List<PersonGroup> groups = personGroupService.getGroupTree();
            String json = getObjectMapper().writeValueAsString(groups);
            zout.putNextEntry(new ZipEntry("groups.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
