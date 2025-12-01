package com.cassinisys.plm.service.exim.exporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

@Component
public class Exporter {
    @Autowired
    private AutonumbersExporter autonumbersExporter;
    @Autowired
    private LovsExporter lovsExporter;
    @Autowired
    private LifecyclesExporter lifecyclesExporter;
    @Autowired
    private ClassificationExporter classificationExporter;
    @Autowired
    private GroupsExporter groupsExporter;
    @Autowired
    private RelationshipsExporter relationshipsExporter;
    @Autowired
    private WorkflowsExporter workflowsExporter;

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'export','all')")
    public void exportData(ZipOutputStream zout, List<String> processed, String... entities) {
        if (processed == null) {
            processed = new ArrayList<>();
        }
        for (String entity : entities) {
            if (entity.equalsIgnoreCase("autonumbers") && !processed.contains("autonumbers")) {
                autonumbersExporter.exportData(zout);
                processed.add("autonumbers");
            } else if (entity.equalsIgnoreCase("lovs") && !processed.contains("lovs")) {
                lovsExporter.exportData(zout);
                processed.add("lovs");
            } else if (entity.equalsIgnoreCase("lifecycles") && !processed.contains("lifecycles")) {
                lifecyclesExporter.exportData(zout);
                processed.add("lifecycles");
            } else if (entity.equalsIgnoreCase("classification") && !processed.contains("classification")) {
                exportData(zout, processed, "autonumbers", "lovs", "lifecycles");
                classificationExporter.exportData(zout);
                processed.add("classification");
            } else if (entity.equalsIgnoreCase("groups") && !processed.contains("groups")) {
                groupsExporter.exportData(zout);
                processed.add("groups");
            } else if (entity.equalsIgnoreCase("relationships") && !processed.contains("relationships")) {
                exportData(zout, processed, "classification");
                relationshipsExporter.exportData(zout);
                processed.add("relationships");
            } else if (entity.equalsIgnoreCase("workflows") && !processed.contains("workflows")) {
                exportData(zout, processed, "classification");
                workflowsExporter.exportData(zout);
                processed.add("workflows");
            }
        }
    }
}
