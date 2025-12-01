package com.cassinisys.plm.service.print;

import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Created by Suresh Cassini on 18-09-2020.
* */
@Service
public class PrintService {
    public static Map fileMap = new HashMap();

    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private ItemPrintService itemPrintService;
    @Autowired
    private ChangePrintService changePrintService;
    @Autowired
    private QualityPrintService qualityPrintService;
    @Autowired
    private ProjectPrintService projectPrintService;
    @Autowired
    private ManufacturingPrintService manufacturingPrintService;
    @Autowired
    private MaintenancePrintService maintenancePrintService;
    @Autowired
    private CompliancePrintService compliancePrintService;
    @Autowired
    private SourcingPrintService sourcingPrintService;
    @Autowired
    private ProgramPrintService programPrintService;
    @Autowired
    private BopPrintService bopPrintService;

    public Map<String, List<PrintAttributesDTO>> getAttributeByGroup(List<PrintAttributesDTO> masters) {
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = new HashMap<>();
        for (PrintAttributesDTO model : masters) {
            if (!attributesMaterForGroup.containsKey(model.getGroup())) {
                List<PrintAttributesDTO> list = new ArrayList<PrintAttributesDTO>();
                list.add(model);
                attributesMaterForGroup.put(model.getGroup(), list);
            } else {
                attributesMaterForGroup.get(model.getGroup()).add(model);
            }
        }
        return attributesMaterForGroup;
    }

    @Transactional
    public String getContentFromTemplate(String templatePath, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/email/print/");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate(templatePath), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional
    public String print(String fileType, Print print,
                        HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        if (fileType != null && fileType.equalsIgnoreCase("html")) {

            fileId = "template" + ".html";


            try {
                response.setHeader("Content-Disposition", "inline; filename=\"" + fileId + "\"");
                response.setContentType("text/html");

                String htmlResponse = previewHtml(print);
                response.setContentLength(htmlResponse.getBytes().length);
                is = new ByteArrayInputStream(htmlResponse.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileMap.put(fileId, is);
        return fileId;
    }


    private String getEcoTemplate(Print print) {
        Map<String, Object> model = new HashMap<>();
        model.put("exportHeaders", print.getOptions());
        model.put("exportData", print.getOptions());
        String templatePath = "ecoTemplate.html";
        String exportHtmlData = getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    @Transactional
    public String previewHtml(Print print) throws ParseException {
        String exportHtmlData = "";
        if (print.getObjectType().equals("ITEM")) {
            exportHtmlData = this.itemPrintService.getItemTemplate(print);
        } else if (print.getObjectType().equals("ECO")) {
            exportHtmlData = this.changePrintService.getEcoTemplate(print);
        } else if (print.getObjectType().equals("ECR")) {
            exportHtmlData = this.changePrintService.getEcrTemplate(print);
        } else if (print.getObjectType().equals("DCR")) {
            exportHtmlData = this.changePrintService.getDcrTemplate(print);
        } else if (print.getObjectType().equals("DCO")) {
            exportHtmlData = this.changePrintService.getDcoTemplate(print);
        } else if (print.getObjectType().equals("MCO")) {
            exportHtmlData = this.changePrintService.getMcoTemplate(print);
        } else if (print.getObjectType().equals("DEVIATION") || print.getObjectType().equals("WAIVER")) {
            exportHtmlData = this.changePrintService.getDeviationAndWaiverTemplate(print);
        } else if (print.getObjectType().equals("MATERIALINSPECTIONPLAN") || print.getObjectType().equals("PRODUCTINSPECTIONPLAN")) {
            exportHtmlData = this.qualityPrintService.getInspectionPlanTemplate(print);
        } else if (print.getObjectType().equals("MATERIALINSPECTION") || print.getObjectType().equals("ITEMINSPECTION")) {
            exportHtmlData = this.qualityPrintService.getInspectionTemplate(print);
        } else if (print.getObjectType().equals("PR")) {
            exportHtmlData = this.qualityPrintService.getProblemReportTemplate(print);
        } else if (print.getObjectType().equals("NCR")) {
            exportHtmlData = this.qualityPrintService.getNCRTemplate(print);
        } else if (print.getObjectType().equals("QCR")) {
            exportHtmlData = this.qualityPrintService.getQCRTemplate(print);
        } else if (print.getObjectType().equals("PPAP")) {
            exportHtmlData = this.qualityPrintService.getPPAPTemplate(print);
        } else if (print.getObjectType().equals("SUPPLIERAUDIT")) {
            exportHtmlData = this.qualityPrintService.getSupplierAuditTemplate(print);
        } else if (print.getObjectType().equals("PPAP")) {
            exportHtmlData = this.qualityPrintService.getPPAPTemplate(print);
        } else if (print.getObjectType().equals("PROJECT")) {
            exportHtmlData = this.projectPrintService.getProjectTemplate(print);
        } else if (print.getObjectType().equals("PLANT")) {
            exportHtmlData = this.manufacturingPrintService.getPlantTemplate(print);
        } else if (print.getObjectType().equals("ASSEMBLYLINE")) {
            exportHtmlData = this.manufacturingPrintService.getAssemblyLineTemplate(print);
        } else if (print.getObjectType().equals("WORKCENTER")) {
            exportHtmlData = this.manufacturingPrintService.getWorkcenterTemplate(print);
        } else if (print.getObjectType().equals("MACHINE")) {
            exportHtmlData = this.manufacturingPrintService.getMachineTemplate(print);
        } else if (print.getObjectType().equals("EQUIPMENT")) {
            exportHtmlData = this.manufacturingPrintService.getEquipmentTemplate(print);
        } else if (print.getObjectType().equals("INSTRUMENT")) {
            exportHtmlData = this.manufacturingPrintService.getInstrumentTemplate(print);
        } else if (print.getObjectType().equals("TOOL")) {
            exportHtmlData = this.manufacturingPrintService.getToolTemplate(print);
        } else if (print.getObjectType().equals("JIGFIXTURE")) {
            exportHtmlData = this.manufacturingPrintService.getJigsFixtureTemplate(print);
        } else if (print.getObjectType().equals("MATERIAL")) {
            exportHtmlData = this.manufacturingPrintService.getMaterialTemplate(print);
        } else if (print.getObjectType().equals("SHIFT")) {
            exportHtmlData = this.manufacturingPrintService.getShiftTemplate(print);
        } else if (print.getObjectType().equals("OPERATION")) {
            exportHtmlData = this.manufacturingPrintService.getOperationTemplate(print);
        } else if (print.getObjectType().equals("MANPOWER")) {
            exportHtmlData = this.manufacturingPrintService.getManpowerTemplate(print);
        } else if (print.getObjectType().equals("MROASSET")) {
            exportHtmlData = this.maintenancePrintService.getAssetTemplate(print);
        } else if (print.getObjectType().equals("MROMETER")) {
            exportHtmlData = this.maintenancePrintService.getMeterTemplate(print);
        } else if (print.getObjectType().equals("MROSPAREPART")) {
            exportHtmlData = this.maintenancePrintService.getSparePartTemplate(print);
        } else if (print.getObjectType().equals("MROMAINTENANCEPLAN")) {
            exportHtmlData = this.maintenancePrintService.getMaintenanceTemplate(print);
        } else if (print.getObjectType().equals("MROWORKREQUEST")) {
            exportHtmlData = this.maintenancePrintService.getWorkRequestTemplate(print);
        } else if (print.getObjectType().equals("MROWORKORDER")) {
            exportHtmlData = this.maintenancePrintService.getWorkOrderTemplate(print);
        } else if (print.getObjectType().equals("PGCSUBSTANCE")) {
            exportHtmlData = this.compliancePrintService.getSubstanceTemplate(print);
        } else if (print.getObjectType().equals("PGCSPECIFICATION")) {
            exportHtmlData = this.compliancePrintService.getSpecificationTemplate(print);
        } else if (print.getObjectType().equals("PGCDECLARATION")) {
            exportHtmlData = this.compliancePrintService.getDeclarationTemplate(print);
        } else if (print.getObjectType().equals("MANUFACTURER")) {
            exportHtmlData = this.sourcingPrintService.getManufacturerTemplate(print);
        } else if (print.getObjectType().equals("MANUFACTURERPART")) {
            exportHtmlData = this.sourcingPrintService.getManufacturerPartTemplate(print);
        } else if (print.getObjectType().equals("MFRSUPPLIER")) {
            exportHtmlData = this.sourcingPrintService.getSupplierTemplate(print);
        } else if (print.getObjectType().equals("PROGRAM")) {
            exportHtmlData = this.programPrintService.getProgramTemplate(print);
        } else if (print.getObjectType().equals("BOP")) {
            exportHtmlData = this.bopPrintService.getBopTemplate(print);
        }


        return exportHtmlData;

    }

    @Transactional
    public void previewFile(String fileName, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "inline; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException var6) {
            response.setHeader("Content-disposition", "inline; filename=" + fileName);
        }

        try {
            ServletOutputStream e = response.getOutputStream();
            org.apache.commons.io.IOUtils.copy((InputStream) fileMap.get(fileName), e);
            e.flush();

        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

}