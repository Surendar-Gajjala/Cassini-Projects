package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.ProblemItemDto;
import com.cassinisys.plm.model.dto.ProblemSourceDto;
import com.cassinisys.plm.model.dto.RelatedItemDto;
import com.cassinisys.plm.model.dto.print.CheckListDTO;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.dto.print.QualityAttributeDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.QualityTypeAttributeDto;
import com.cassinisys.plm.repo.cm.AffectedItemRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.SupplierPartRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.service.classification.ClassificationService;
import com.cassinisys.plm.service.classification.QualityTypeService;
import com.cassinisys.plm.service.pqm.InspectionPlanService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.pqm.SupplierAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QualityPrintService {
    public static Map fileMap = new HashMap();
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private PrintService printService;
    private PrintDTO printDTO;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private QualityAttributeDTO qualityAttributeDTO;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private QualityTypeService qualityTypeService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private InspectionPlanAttributeRepository inspectionPlanAttributeRepository;
    @Autowired
    private ProblemReportAttributeRepository problemReportAttributeRepository;
    @Autowired
    private QCRAttributeRepository qcrAttributeRepository;
    @Autowired
    private NCRAttributeRepository ncrAttributeRepository;
    @Autowired
    private InspectionPlanService inspectionPlanService;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private PRRelatedItemRepository prRelatedItemRepository;
    @Autowired
    private NCRProblemItemRepository ncrProblemItemRepository;
    @Autowired
    private NCRRelatedItemRepository ncrRelatedItemRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private QCRProblemMaterialRepository qcrProblemMaterialRepository;
    @Autowired
    private QCRRelatedItemRepository qcrRelatedItemRepository;
    @Autowired
    private QCRRelatedMaterialRepository qcrRelatedMaterialRepository;
    @Autowired
    private QCRAggregateNCRRepository qcrAggregateNCRRepository;
    @Autowired
    private QCRAggregatePRRepository qcrAggregatePRRepository;
    @Autowired
    private QCRCAPARepository qcrcapaRepository;
    @Autowired
    private ItemInspectionRelatedItemRepository itemInspectionRelatedItemRepository;
    @Autowired
    private MaterialInspectionRelatedItemRepository materialInspectionRelatedItemRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private SupplierAuditService supplierAuditService;

    public String getInspectionPlanTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(print.getObjectId());
        PQMInspectionPlan inspectionPlan = inspectionPlanRevision.getPlan();
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                inspectionPlan = getInspectionPlanDetails(print.getObjectId(), print.getObjectType());
                this.printDTO.setInspectionPlan(inspectionPlan);
            } else if (option.equals("Attributes")) {
                QualityTypeAttributeDto dto = null;
                PLMObjectType plmObjectType = PLMObjectType.valueOf(print.getObjectType());
                if (print.getObjectType().equals("MATERIALINSPECTIONPLAN")) {
                    PQMMaterialInspectionPlan materialInspectionPlan = (PQMMaterialInspectionPlan) inspectionPlan;
                    dto = classificationService.getObjectTypeAttributes(plmObjectType, materialInspectionPlan.getPlanType().getId(), inspectionPlan.getId(), true);
                }
                if (print.getObjectType().equals("PRODUCTINSPECTIONPLAN")) {
                    PQMProductInspectionPlan productInspectionPlan = (PQMProductInspectionPlan) inspectionPlan;
                    dto = classificationService.getObjectTypeAttributes(plmObjectType, productInspectionPlan.getPlanType().getId(), inspectionPlan.getId(), true);
                }
                PrintDTO dto1 = this.getQualityCustomAttributes(inspectionPlanRevisionRepository.findOne(print.getObjectId()).getPlan().getId());
                PrintDTO dto2 = this.getInspectionPlanAttributes(inspectionPlan, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
            } else if (option.equals("Check List")) {
                List<CheckListDTO> checkListDTOs = new LinkedList<>();
                List<PQMInspectionPlanChecklist> inspectionPlanChecklists = inspectionPlanService.getInspectionPlanChecklists(inspectionPlanRevision.getId());
                for (PQMInspectionPlanChecklist inspectionPlanChecklist : inspectionPlanChecklists) {
                    CheckListDTO checkListDTO = new CheckListDTO();
                    checkListDTO.setTitle(inspectionPlanChecklist.getTitle());
                    checkListDTO.setSummary(inspectionPlanChecklist.getSummary());
                    checkListDTO.setParams(inspectionPlanChecklist.getParamsCount().toString());
                    checkListDTO.setLevel(0);
                    checkListDTOs.add(checkListDTO);
                    if (inspectionPlanChecklist.getChildren().size() > 0) {
                        for (PQMInspectionPlanChecklist inspectionPlanChecklist1 : inspectionPlanChecklist.getChildren()) {
                            CheckListDTO checkListDTO1 = new CheckListDTO();
                            checkListDTO1.setTitle(inspectionPlanChecklist1.getTitle());
                            checkListDTO1.setSummary(inspectionPlanChecklist1.getSummary());
                            checkListDTO1.setParams(inspectionPlanChecklist1.getParamsCount().toString());
                            checkListDTO1.setLevel(1);
                            checkListDTOs.add(checkListDTO1);
                        }
                    }
                }
                this.printDTO.setCheckLists(checkListDTOs);
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getInspectionTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMInspection inspection = inspectionRepository.getOne(print.getObjectId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PQMInspection inspection1 = getInspectionDetails(print.getObjectId(), print.getObjectType());
                this.printDTO.setInspection(inspection1);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setGroupQualityAttributes(dto1.getGroupQualityAttributes());
                this.printDTO.setGroupQualityAttributes(dto1.getGroupQualityAttributes());
            } else if (option.equals("Check List")) {
                List<CheckListDTO> checkListDTOs = new LinkedList<>();
                List<PQMInspectionPlanChecklist> inspectionPlanChecklists = inspectionPlanService.getInspectionPlanChecklists(inspectionPlanRevision.getId());
                for (PQMInspectionPlanChecklist inspectionPlanChecklist : inspectionPlanChecklists) {
                    CheckListDTO checkListDTO = new CheckListDTO();
                    checkListDTO.setTitle(inspectionPlanChecklist.getTitle());
                    checkListDTO.setSummary(inspectionPlanChecklist.getSummary());
                    checkListDTO.setParams(inspectionPlanChecklist.getParamsCount().toString());
                    checkListDTO.setLevel(0);
                    checkListDTOs.add(checkListDTO);
                    if (inspectionPlanChecklist.getChildren().size() > 0) {
                        for (PQMInspectionPlanChecklist inspectionPlanChecklist1 : inspectionPlanChecklist.getChildren()) {
                            CheckListDTO checkListDTO1 = new CheckListDTO();
                            checkListDTO1.setTitle(inspectionPlanChecklist1.getTitle());
                            checkListDTO1.setSummary(inspectionPlanChecklist1.getSummary());
                            checkListDTO1.setParams(inspectionPlanChecklist1.getParamsCount().toString());
                            checkListDTO1.setLevel(1);
                            checkListDTOs.add(checkListDTO1);
                        }
                    }
                }
                this.printDTO.setCheckLists(checkListDTOs);
            } else if (option.equals("Related Items")) {
                List<RelatedItemDto> items = getInspectionRelatedtems(print.getObjectId(), print.getObjectType());
                this.printDTO.setRelatedItems(items);
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getProblemReportTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMProblemReport problemReport = getProblemReportDetails(print.getObjectId());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                this.printDTO.setProblemReport(problemReport);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                PLMObjectType plmObjectType = PLMObjectType.valueOf("PROBLEMREPORT");
                QualityTypeAttributeDto dto = classificationService.getObjectTypeAttributes(plmObjectType, problemReport.getPrType().getId(), problemReport.getId(), true);
                PrintDTO dto2 = this.getProblemReportAttributes(problemReport, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
            } else if (option.equals("Problem Items")) {
                List<ProblemItemDto> items = getPRProblemItems(print.getObjectId());
                this.printDTO.setProblemItems(items);
            } else if (option.equals("Related Items")) {
                List<RelatedItemDto> items = getPRRelatedtems(print.getObjectId());
                this.printDTO.setRelatedItems(items);
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getNCRTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMNCR pqmncr = getNcrDetails(print.getObjectId());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                this.printDTO.setPqmncr(pqmncr);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                PLMObjectType plmObjectType = PLMObjectType.valueOf("NCR");
                QualityTypeAttributeDto dto = classificationService.getObjectTypeAttributes(plmObjectType, pqmncr.getNcrType().getId(), pqmncr.getId(), true);
                PrintDTO dto2 = this.getNCRAttributes(pqmncr, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
            } else if (option.equals("Problem Items")) {
                List<ProblemItemDto> items = getNCRProblemItems(print.getObjectId());
                this.printDTO.setProblemItems(items);
            } else if (option.equals("Related Items")) {
                List<RelatedItemDto> items = getNCRRelatedtems(print.getObjectId());
                this.printDTO.setRelatedItems(items);
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getQCRTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMQCR pqmqcr = getQcrDetails(print.getObjectId());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                this.printDTO.setPqmqcr(pqmqcr);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                PLMObjectType plmObjectType = PLMObjectType.valueOf("NCR");
                QualityTypeAttributeDto dto = classificationService.getObjectTypeAttributes(plmObjectType, pqmqcr.getQcrType().getId(), pqmqcr.getId(), true);
                PrintDTO dto2 = this.getQCRAttributes(pqmqcr, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
            } else if (option.equals("Problem Items")) {
                List<ProblemItemDto> items = getQCRProblemItems(print.getObjectId());

                this.printDTO.setProblemItems(items);
            } else if (option.equals("Related Items")) {
                List<RelatedItemDto> items = getQCRRelatedtems(print.getObjectId());
                this.printDTO.setRelatedItems(items);
            } else if (option.equals("Problem Sources")) {
                List<ProblemSourceDto> items = getQCRProblemSources(print.getObjectId());
                this.printDTO.setProblemSources(items);
            } else if (option.equals("CAPA")) {
                List<PQMQCRCAPA> pqmqcrcapas = qcrcapaRepository.findByQcrOrderByLatestDescModifiedDateDesc(print.getObjectId());
                for (PQMQCRCAPA pqmqcrcapa : pqmqcrcapas) {
                    Person created = personRepository.findOne(pqmqcrcapa.getCreatedBy());
                    if (pqmqcrcapa.getAuditedBy() != null) {
                        Person auditBy = personRepository.findOne(pqmqcrcapa.getAuditedBy());
                        pqmqcrcapa.setModifiedPerson(auditBy.getFirstName());
                    }
                    pqmqcrcapa.setCreatedPerson(created.getFirstName());
                    pqmqcrcapa.setAuditResult(pqmqcrcapa.getResult().name());
                }
                this.printDTO.setPqmqcrcapas(pqmqcrcapas);
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getPPAPTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMPPAP pqmppap = getPPAPDetails(print.getObjectId());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                this.printDTO.setPqmppap(pqmppap);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                PLMObjectType plmObjectType = PLMObjectType.valueOf("PPAP");
                QualityTypeAttributeDto dto = classificationService.getObjectTypeAttributes(plmObjectType, pqmppap.getType().getId(), pqmppap.getId(), true);
                PrintDTO dto2 = this.getPPAPAttributes(pqmppap, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
            } else if (option.equals("Checklist")) {
                this.printDTO.setFileDto(this.objectFileService.getObjectFiles(print.getObjectId(), PLMObjectType.PPAPCHECKLIST,false));
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getSupplierAuditTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        PQMSupplierAudit supplierAudit = getSupplierAuditDetails(print.getObjectId());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                this.printDTO.setSupplierAudit(supplierAudit);
            } else if (option.equals("Attributes")) {
                PrintDTO dto1 = this.getQualityCustomAttributes(print.getObjectId());
                PLMObjectType plmObjectType = PLMObjectType.valueOf("PPAP");
                QualityTypeAttributeDto dto = classificationService.getObjectTypeAttributes(plmObjectType, supplierAudit.getType().getId(), supplierAudit.getId(), true);
                PrintDTO dto2 = this.getSupplierAuditAttributes(supplierAudit, dto.getQualityTypeAttributes());
                this.printDTO.setQualityTypeAttributes(dto2.getQualityTypeAttributes());
                this.printDTO.setGroupQualityAttributes(dto2.getGroupQualityAttributes());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
            } else if (option.equals("Plan")) {
                this.printDTO.setAuditPlans(this.supplierAuditService.getAllSupplierAuditPlans(supplierAudit.getId()));
            } else if (option.equals("Files")) {
                this.printDTO.setFileDto(this.objectFileService.getObjectFiles(print.getObjectId(), PLMObjectType.SUPPLIERAUDIT,false));
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "qualityTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    private PQMPPAP getPPAPDetails(Integer ppapId) {
        PQMPPAP pqmppap = ppapRepository.findOne(ppapId);
        pqmppap.setSupplierName(supplierRepository.findOne(pqmppap.getSupplier()).getName());
        pqmppap.setMfrPart(supplierPartRepository.findOne(pqmppap.getSupplierPart()).getManufacturerPart());
        Person created = personRepository.findOne(pqmppap.getCreatedBy());
        Person modified = personRepository.findOne(pqmppap.getModifiedBy());
        pqmppap.setCreatedPerson(created.getFirstName());
        pqmppap.setModifiedPerson(modified.getFirstName());
        return pqmppap;
    }

    private PQMSupplierAudit getSupplierAuditDetails(Integer id) {
        PQMSupplierAudit pqmppap = supplierAuditRepository.findOne(id);
        Person created = personRepository.findOne(pqmppap.getCreatedBy());
        Person modified = personRepository.findOne(pqmppap.getModifiedBy());
        pqmppap.setCreatedPerson(created.getFirstName());
        pqmppap.setModifiedPerson(modified.getFirstName());
        return pqmppap;
    }


    public List<ProblemItemDto> getPRProblemItems(Integer ecoID) {
        List<ProblemItemDto> problemItemDtos = new LinkedList<>();
        for (PQMPRProblemItem affectedItem : prProblemItemRepository.findByProblemReport(ecoID)) {
            ProblemItemDto problemItemDto = new ProblemItemDto();
            PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            problemItemDto.setItemNumber(item.getItemNumber());
            problemItemDto.setItemName(item.getItemName());
            problemItemDto.setItemType(item.getItemType().getName());
            problemItemDto.setDescription(item.getDescription());
            problemItemDto.setLifeCyclePhase(revision.getLifeCyclePhase().getPhase());
            problemItemDto.setRevision(revision.getRevision());
            problemItemDto.setNotes(affectedItem.getNotes());
            problemItemDtos.add(problemItemDto);
        }

        return problemItemDtos;
    }

    public List<ProblemItemDto> getNCRProblemItems(Integer ecoID) {
        List<ProblemItemDto> problemItemDtos = new LinkedList<>();
        for (PQMNCRProblemItem affectedItem : ncrProblemItemRepository.findByNcr(ecoID)) {
            ProblemItemDto problemItemDto = new ProblemItemDto();
            PLMManufacturerPart manufacturerPart = affectedItem.getMaterial();
            problemItemDto.setItemNumber(manufacturerPart.getPartNumber());
            problemItemDto.setItemName(manufacturerPart.getPartName());
            problemItemDto.setItemType(manufacturerPart.getMfrPartType().getName());
            problemItemDto.setDescription(affectedItem.getReceivedQty().toString());
            problemItemDto.setLifeCyclePhase(affectedItem.getInspectedQty().toString());
            problemItemDto.setRevision(affectedItem.getDefectiveQty().toString());
            problemItemDto.setNotes(affectedItem.getNotes());
            problemItemDtos.add(problemItemDto);
        }

        return problemItemDtos;
    }

    public List<ProblemItemDto> getQCRProblemItems(Integer ecoID) {
        List<ProblemItemDto> problemItemDtos = new LinkedList<>();
        PQMQCR pqmqcr = qcrRepository.findOne(ecoID);
        if (pqmqcr.getQcrFor().equals(QCRFor.PR)) {
            for (PQMQCRProblemItem affectedItem : qcrProblemItemRepository.findByQcr(ecoID)) {
                ProblemItemDto problemItemDto = new ProblemItemDto();
                PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                problemItemDto.setItemNumber(item.getItemNumber());
                problemItemDto.setItemName(item.getItemName());
                problemItemDto.setItemType(item.getItemType().getName());
                if (affectedItem.getProblemReport() != null) {
                    PQMProblemReport problemReport = problemReportRepository.findOne(affectedItem.getProblemReport());
                    problemItemDto.setDescription(problemReport.getPrNumber());
                }
                problemItemDto.setLifeCyclePhase(revision.getLifeCyclePhase().getPhase());
                problemItemDto.setRevision(revision.getRevision());
                problemItemDto.setNotes(affectedItem.getNotes());
                problemItemDtos.add(problemItemDto);
            }
            this.printDTO.setQcrFor("PR");
        } else if (pqmqcr.getQcrFor().equals(QCRFor.NCR)) {
            for (PQMQCRProblemMaterial affectedItem : qcrProblemMaterialRepository.findByQcr(ecoID)) {
                ProblemItemDto problemItemDto = new ProblemItemDto();
                PLMManufacturerPart manufacturerPart = affectedItem.getMaterial();
                problemItemDto.setItemNumber(manufacturerPart.getPartNumber());
                problemItemDto.setItemName(manufacturerPart.getPartName());
                problemItemDto.setItemType(manufacturerPart.getMfrPartType().getName());
                if (affectedItem.getNcr() != null) {
                    PQMNCR pqmncr = ncrRepository.findOne(affectedItem.getNcr());
                    problemItemDto.setDescription(pqmncr.getNcrNumber());
                }
                problemItemDto.setNotes(affectedItem.getNotes());
                problemItemDtos.add(problemItemDto);
            }
            this.printDTO.setQcrFor("NCR");
        }


        return problemItemDtos;
    }

    public List<RelatedItemDto> getPRRelatedtems(Integer ecoID) {
        List<RelatedItemDto> relatedItemDtos = new LinkedList<>();
        for (PQMPRRelatedItem affectedItem : prRelatedItemRepository.findByProblemReport(ecoID)) {
            RelatedItemDto relatedItemDto = new RelatedItemDto();
            PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            relatedItemDto.setItemNumber(item.getItemNumber());
            relatedItemDto.setItemName(item.getItemName());
            relatedItemDto.setItemType(item.getItemType().getName());
            relatedItemDto.setDescription(item.getDescription());
            relatedItemDto.setPhase(revision.getLifeCyclePhase().getPhase());
            relatedItemDto.setRevision(revision.getRevision());
            relatedItemDtos.add(relatedItemDto);
        }
        return relatedItemDtos;
    }

    public List<RelatedItemDto> getInspectionRelatedtems(Integer ecoID, String type) {
        PQMInspection inspection = inspectionRepository.findOne(ecoID);
        List<RelatedItemDto> relatedItemDtos = new LinkedList<>();
        if (type.equals("ITEMINSPECTION")) {
            for (PQMItemInspectionRelatedItem affectedItem : itemInspectionRelatedItemRepository.findByInspection(ecoID)) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setPhase(revision.getLifeCyclePhase().getPhase());
                relatedItemDto.setRevision(revision.getRevision());
                relatedItemDtos.add(relatedItemDto);
            }
            this.printDTO.setQcrFor("ITEM");
        } else if (type.equals("MATERIALINSPECTION")) {
            for (PQMMaterialInspectionRelatedItem affectedItem : materialInspectionRelatedItemRepository.findByInspection(ecoID)) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(affectedItem.getMaterial());
                relatedItemDto.setItemNumber(manufacturerPart.getPartNumber());
                relatedItemDto.setItemName(manufacturerPart.getPartName());
                relatedItemDto.setItemType(manufacturerPart.getMfrPartType().getName());
                relatedItemDtos.add(relatedItemDto);
            }
            this.printDTO.setQcrFor("PART");
        }
        return relatedItemDtos;
    }

    public List<RelatedItemDto> getNCRRelatedtems(Integer ecoID) {
        List<RelatedItemDto> relatedItemDtos = new LinkedList<>();
        for (PQMNCRRelatedItem affectedItem : ncrRelatedItemRepository.findByNcr(ecoID)) {
            RelatedItemDto relatedItemDto = new RelatedItemDto();
            PLMManufacturerPart manufacturerPart = affectedItem.getMaterial();
            relatedItemDto.setItemNumber(manufacturerPart.getPartNumber());
            relatedItemDto.setItemName(manufacturerPart.getPartName());
            relatedItemDto.setItemType(manufacturerPart.getMfrPartType().getName());
            relatedItemDto.setRevision(affectedItem.getNotes());
            relatedItemDtos.add(relatedItemDto);
        }
        return relatedItemDtos;
    }

    public List<RelatedItemDto> getQCRRelatedtems(Integer ecoID) {
        PQMQCR pqmqcr = qcrRepository.findOne(ecoID);
        List<RelatedItemDto> relatedItemDtos = new LinkedList<>();
        if (pqmqcr.getQcrFor().equals(QCRFor.PR)) {
            for (PQMQCRRelatedItem affectedItem : qcrRelatedItemRepository.findByQcr(ecoID)) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                relatedItemDto.setItemNumber(item.getItemNumber());
                relatedItemDto.setItemName(item.getItemName());
                relatedItemDto.setItemType(item.getItemType().getName());
                relatedItemDto.setPhase(revision.getLifeCyclePhase().getPhase());
                relatedItemDto.setRevision(revision.getRevision());
                relatedItemDtos.add(relatedItemDto);
            }
            this.printDTO.setQcrFor("PR");
        } else if (pqmqcr.getQcrFor().equals(QCRFor.NCR)) {
            for (PQMQCRRelatedMaterial affectedItem : qcrRelatedMaterialRepository.findByQcr(ecoID)) {
                RelatedItemDto relatedItemDto = new RelatedItemDto();
                PLMManufacturerPart manufacturerPart = affectedItem.getMaterial();
                relatedItemDto.setItemNumber(manufacturerPart.getPartNumber());
                relatedItemDto.setItemName(manufacturerPart.getPartName());
                relatedItemDto.setItemType(manufacturerPart.getMfrPartType().getName());
                relatedItemDtos.add(relatedItemDto);
            }
            this.printDTO.setQcrFor("NCR");
        }
        return relatedItemDtos;
    }

    public List<ProblemSourceDto> getQCRProblemSources(Integer ecoID) {
        List<ProblemSourceDto> problemSourceDtos = new LinkedList<>();
        PQMQCR pqmqcr = qcrRepository.findOne(ecoID);
        if (pqmqcr.getQcrFor().equals(QCRFor.PR)) {
            for (PQMQCRAggregatePR affectedItem : qcrAggregatePRRepository.findByQcr(ecoID)) {
                ProblemSourceDto problemSourceDto = new ProblemSourceDto();
                PQMProblemReport problemReport = affectedItem.getPr();
                problemSourceDto.setObjectNumer(problemReport.getPrNumber());
                problemSourceDto.setObjectType(problemReport.getPrType().getName());
                if (problemReport.getProduct() != null) {
                    PLMItemRevision revision = itemRevisionRepository.findOne(problemReport.getProduct());
                    PLMItem item = itemRepository.findOne(revision.getItemMaster());
                    problemSourceDto.setProduct(item.getItemName());
                }
                problemSourceDto.setProblem(problemReport.getProblem());
                problemSourceDto.setSeverity(problemReport.getSeverity());
                problemSourceDto.setFailureType(problemReport.getFailureType());
                problemSourceDto.setDisposition(problemReport.getDisposition());
                problemSourceDtos.add(problemSourceDto);
            }
            this.printDTO.setQcrFor("PR");
        } else if (pqmqcr.getQcrFor().equals(QCRFor.NCR)) {
            for (PQMQCRAggregateNCR affectedItem : qcrAggregateNCRRepository.findByQcr(ecoID)) {
                ProblemSourceDto problemSourceDto = new ProblemSourceDto();
                PQMNCR manufacturerPart = affectedItem.getNcr();
                problemSourceDto.setObjectNumer(manufacturerPart.getNcrNumber());
                problemSourceDto.setObjectType(manufacturerPart.getNcrType().getName());
                problemSourceDto.setProduct(manufacturerPart.getTitle());
                problemSourceDto.setProblem(manufacturerPart.getDescription());
                problemSourceDto.setSeverity(manufacturerPart.getSeverity());
                problemSourceDto.setFailureType(manufacturerPart.getFailureType());
                problemSourceDto.setDisposition(manufacturerPart.getDisposition());
                problemSourceDtos.add(problemSourceDto);
            }
            this.printDTO.setQcrFor("NCR");
        }
        return problemSourceDtos;
    }

    private PQMInspectionPlan getInspectionPlanDetails(Integer ecoId, String type) {
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(ecoId);
        PQMInspectionPlan inspectionPlan = inspectionPlanRevision.getPlan();
        Person created = personRepository.findOne(inspectionPlan.getCreatedBy());
        Person modified = personRepository.findOne(inspectionPlan.getModifiedBy());
        inspectionPlan.setCreatedPerson(created.getFirstName());
        inspectionPlan.setModifiedPerson(modified.getFirstName());
        inspectionPlan.setLifeCycleName(inspectionPlanRevision.getLifeCyclePhase().getPhase());
        inspectionPlan.setRevisionName(inspectionPlanRevision.getRevision());
        if (type.equals("MATERIALINSPECTIONPLAN")) {
            PQMMaterialInspectionPlan materialInspectionPlan = (PQMMaterialInspectionPlan) inspectionPlan;
            inspectionPlan.setTypeName(materialInspectionPlan.getMaterial().getPartName());
            inspectionPlan.setType("Material Inspection Plan");
        }
        if (type.equals("PRODUCTINSPECTIONPLAN")) {
            PQMProductInspectionPlan productInspectionPlan = (PQMProductInspectionPlan) inspectionPlan;
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(productInspectionPlan.getProduct());
            inspectionPlan.setTypeName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            inspectionPlan.setType("Product Inspection Plan");
        }

        return inspectionPlan;
    }

    private PQMInspection getInspectionDetails(Integer ecoId, String type) {
        PQMInspection inspection = inspectionRepository.findOne(ecoId);
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
        PQMInspectionPlan inspectionPlan = inspectionPlanRevision.getPlan();
        PQMInspection inspection1 = new PQMInspection();
        Person created = personRepository.findOne(inspection.getCreatedBy());
        Person modified = personRepository.findOne(inspection.getModifiedBy());
        Person assginedTo = personRepository.findOne(inspection.getAssignedTo());
        inspection1.setInspectionNumber(inspection.getInspectionNumber());
        inspection1.setDeviationSummary(inspection.getDeviationSummary());
        inspection1.setNotes(inspection.getNotes());
        inspection1.setCreatedPerson(created.getFirstName());
        inspection1.setModifiedPerson(modified.getFirstName());
        inspection1.setAssginedTo(assginedTo.getFirstName());
        inspection1.setInspectionPlanName(inspectionPlan.getName());
        inspection1.setCreatedDate(inspectionPlan.getCreatedDate());
        inspection1.setModifiedDate(inspectionPlan.getModifiedDate());
        return inspection1;
    }

    private PQMProblemReport getProblemReportDetails(Integer ecoId) {
        PQMProblemReport problemReport = problemReportRepository.findOne(ecoId);
        Person created = personRepository.findOne(problemReport.getCreatedBy());
        Person modified = personRepository.findOne(problemReport.getModifiedBy());
        if(problemReport.getReportedBy() != null){
        Person reportedBy = personRepository.findOne(problemReport.getReportedBy());
        if (reportedBy == null) {
            PQMCustomer pqmreportedBy = pqmCustomerRepository.findOne(problemReport.getReportedBy());
            problemReport.setReportedByPerson(pqmreportedBy.getName());
        } else {
            problemReport.setReportedByPerson(reportedBy.getFirstName());
        }
    }
        Person printAnalyst = personRepository.findOne(problemReport.getQualityAnalyst());
        if (problemReport.getProduct() != null) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
            problemReport.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
        }
        if (problemReport.getInspection() != null) {
            PQMInspection inspection = inspectionRepository.findOne(problemReport.getInspection());
            problemReport.setPrintWorkFlow(inspection.getInspectionNumber());
        }
        problemReport.setType(problemReport.getReporterType().name());
        problemReport.setCreatedPerson(created.getFirstName());
        problemReport.setModifiedPerson(modified.getFirstName());
        problemReport.setPrintAnalyst(printAnalyst.getFirstName());
        return problemReport;
    }

    private PQMNCR getNcrDetails(Integer ecoId) {
        PQMNCR pqmncr = ncrRepository.findOne(ecoId);
        Person created = personRepository.findOne(pqmncr.getCreatedBy());
        Person modified = personRepository.findOne(pqmncr.getModifiedBy());
        Person reportedBy = personRepository.findOne(pqmncr.getReportedBy());
        Person printAnalyst = personRepository.findOne(pqmncr.getQualityAnalyst());
        if (pqmncr.getInspection() != null) {
            PQMInspection inspection = inspectionRepository.findOne(pqmncr.getInspection());
            pqmncr.setInspectionName(inspection.getInspectionNumber());
        }
        pqmncr.setCreatedPerson(created.getFirstName());
        pqmncr.setModifiedPerson(modified.getFirstName());
        pqmncr.setReportedByPerson(reportedBy.getFirstName());
        pqmncr.setPrintAnalyst(printAnalyst.getFirstName());
        pqmncr.setCreatedDate(pqmncr.getCreatedDate());
        pqmncr.setModifiedDate(pqmncr.getModifiedDate());
        pqmncr.setReportedDate(pqmncr.getReportedDate());
        return pqmncr;
    }

    private PQMQCR getQcrDetails(Integer ecoId) {
        PQMQCR pqmncr = qcrRepository.findOne(ecoId);
        Person created = personRepository.findOne(pqmncr.getCreatedBy());
        Person modified = personRepository.findOne(pqmncr.getModifiedBy());
        Person printAnalyst = personRepository.findOne(pqmncr.getQualityAdministrator());
        pqmncr.setCreatedPerson(created.getFirstName());
        pqmncr.setModifiedPerson(modified.getFirstName());
        pqmncr.setPrintAnalyst(printAnalyst.getFirstName());
        pqmncr.setCreatedDate(pqmncr.getCreatedDate());
        pqmncr.setModifiedDate(pqmncr.getModifiedDate());
        pqmncr.setQcrForPrint(pqmncr.getQcrFor().name());
        return pqmncr;
    }

    private PrintDTO getQualityCustomAttributes(Integer ecoId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("QUALITY"));
        for (int k = 0; k < objTypeAttr.size(); k++) {
            ObjectTypeAttribute type = objTypeAttr.get(k);
            ObjectAttribute objAttr = objectAttributeRepository.findByObjectIdAndAttributeDefId(ecoId, type.getId());
            if (objAttr != null && type != null) {
                PrintAttributesDTO itemCust = printAttributesDTO.getItemCustomAttributes(type, objAttr);
                itemCustomAttributes.add(itemCust);
            }

        }

        dto.setChangeTypeCustomAttributes(itemCustomAttributes);
        return dto;
    }


    public Map<String, List<QualityAttributeDTO>> getAttributeByGroup(List<QualityAttributeDTO> masters) {
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = new HashMap<>();
        for (QualityAttributeDTO model : masters) {
            if (!attributesMaterForGroup.containsKey(model.getGroup())) {
                List<QualityAttributeDTO> list = new ArrayList<QualityAttributeDTO>();
                list.add(model);
                attributesMaterForGroup.put(model.getGroup(), list);
            } else {
                attributesMaterForGroup.get(model.getGroup()).add(model);
            }
        }
        return attributesMaterForGroup;
    }

    private PrintDTO getInspectionPlanAttributes(PQMInspectionPlan inspectionPlan, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = inspectionPlanAttributeRepository.findByInspectionAndAttribute(inspectionPlan.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }

        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

    private PrintDTO getProblemReportAttributes(PQMProblemReport pqmProblemReport, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = problemReportAttributeRepository.findByProblemReportAndAttribute(pqmProblemReport.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }
        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

    private PrintDTO getQCRAttributes(PQMQCR pqmqcr, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = qcrAttributeRepository.findByQcrAndAttribute(pqmqcr.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }
        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

    private PrintDTO getPPAPAttributes(PQMPPAP pqmppap, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = qcrAttributeRepository.findByQcrAndAttribute(pqmppap.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }
        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

    private PrintDTO getSupplierAuditAttributes(PQMSupplierAudit supplierAudit, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = qcrAttributeRepository.findByQcrAndAttribute(supplierAudit.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }
        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

    private PrintDTO getNCRAttributes(PQMNCR pqmncr, List<PQMQualityTypeAttribute> qualityTypeAttributes) {
        PrintDTO dto = new PrintDTO();
        List<QualityAttributeDTO> masters = new ArrayList<>();
        List<QualityAttributeDTO> changeAttribute = new LinkedList<>();
        for (PQMQualityTypeAttribute qualityTypeAttribute : qualityTypeAttributes) {
            ObjectAttribute objectAttribute = ncrAttributeRepository.findByNcrAndAttribute(pqmncr.getId(), qualityTypeAttribute.getId());
            QualityAttributeDTO master = qualityAttributeDTO.getQualityAttributes(qualityTypeAttribute, objectAttribute);
            if (qualityTypeAttribute.getAttributeGroup() != null && qualityTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(qualityTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttribute.add(master);
            }
        }
        Map<String, List<QualityAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupQualityAttributes(attributesMaterForGroup);
        dto.setQualityTypeAttributes(changeAttribute);
        return dto;
    }

}