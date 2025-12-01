package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.plm.model.plm.PLMRelationship;
import com.cassinisys.plm.service.plm.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class RelationshipsExporter extends AbstractExporter {
    @Autowired
    private RelationshipService relationshipService;

    @Override
    public void exportData(ZipOutputStream zout) {
        try {
            List<PLMRelationship> relationships = relationshipService.getAll();
            String json = getObjectMapper().writeValueAsString(relationships);
            zout.putNextEntry(new ZipEntry("relationships.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
