package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class WorkflowsExporter extends AbstractExporter {
    @Autowired
    private PLMWorkflowDefinitionService plmWorkflowDefinitionService;

    @Override
    public void exportData(ZipOutputStream zout) {
        try {
            List<PLMWorkflowDefinition> workflows = plmWorkflowDefinitionService.getAll();
            String json = getObjectMapper().writeValueAsString(workflows);
            zout.putNextEntry(new ZipEntry("workflows.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
