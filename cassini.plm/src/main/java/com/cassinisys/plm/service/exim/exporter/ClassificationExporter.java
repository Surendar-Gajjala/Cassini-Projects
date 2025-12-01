package com.cassinisys.plm.service.exim.exporter;

import com.cassinisys.plm.service.classification.ChangeTypeService;
import com.cassinisys.plm.service.classification.ManufacturerPartTypeService;
import com.cassinisys.plm.service.classification.ManufacturerTypeService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import com.cassinisys.plm.service.wf.PLMWorkflowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ClassificationExporter extends AbstractExporter {
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ChangeTypeService changeTypeService;
    @Autowired
    private RequirementsService requirementsService;
    @Autowired
    private SpecificationsService specificationsService;
    @Autowired
    private ManufacturerTypeService manufacturerTypeService;
    @Autowired
    private ManufacturerPartTypeService manufacturerPartTypeService;
    @Autowired
    private PLMWorkflowTypeService workflowTypeService;

    @Override
    public void exportData(ZipOutputStream zout) {
        try {
            LinkedHashMap<String, Object> rootNode = new LinkedHashMap<>();
            rootNode.put("item", itemTypeService.getAllItemTypesWithAttributes());
            rootNode.put("change", changeTypeService.getAllChangeTypesWithAttributes());
            rootNode.put("requirement", requirementsService.getAllRequirementTypesWithAttributes());
            rootNode.put("specification", specificationsService.getSpecificationTypesWithAttributes());
            rootNode.put("manufacturer", manufacturerTypeService.getAllManufacturerTypesWithAttributes());
            rootNode.put("manufacturerpart", manufacturerPartTypeService.getAllManufacturerPartTypesWithAttributes());
            rootNode.put("workflow", workflowTypeService.getAllWorkflowTypesWithAttributes());
            String json = getObjectMapper().writeValueAsString(rootNode);
            zout.putNextEntry(new ZipEntry("classification.json"));
            byte[] fileContent = json.getBytes();
            zout.write(fileContent, 0, fileContent.length);
            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
