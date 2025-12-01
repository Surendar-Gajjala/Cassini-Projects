package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.dto.BomComplianceData;
import com.cassinisys.plm.model.dto.BomComplianceReport;
import com.cassinisys.plm.model.dto.ComplianceSpecification;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.pgc.dto.BosItemDto;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import com.cassinisys.plm.model.pqm.PQMProblemReport;
import com.cassinisys.plm.model.pqm.PQMQCR;
import com.cassinisys.plm.model.pqm.PQMQCRProblemItem;
import com.cassinisys.plm.model.pqm.dto.InspectionsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pqm.PRProblemItemRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
import com.cassinisys.plm.repo.pqm.QCRProblemItemRepository;
import com.cassinisys.plm.repo.pqm.QCRRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.pqm.InspectionService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import freemarker.template.Configuration;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
* Created by Suresh Cassini on 23-09-2020.
* */
@Service
public class ItemPrintService {
    public static Map fileMap = new HashMap();
    @Autowired
    Configuration fmConfiguration;
    List<PLMBom> individualList = new LinkedList<>();
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    private PrintDTO printDTO;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private ECRAffectedItemRepository ecrAffectedItemRepository;
    @Autowired
    private DCRAffectedItemRepository dcrAffectedItemRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private PRProblemItemRepository prProblemItemRepository;
    @Autowired
    private QCRProblemItemRepository qcrProblemItemRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private PrintService printService;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private PGCDeclarationPartRepository pgcDeclarationPartRepository;
    @Autowired
    private PGCItemSpecificationRepository pgcItemSpecificationRepository;
    @Autowired
    private PGCDeclarationPartComplianceRepository pgcDeclarationPartComplianceRepository;
    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private PGCBosItemRepository pgcBosItemRepository;
    @Autowired
    private PGCSubstanceRepository pgcSubstanceRepository;
    @Autowired
    private PGCSpecificationSubstanceRepository pgcSpecificationSubstanceRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private BomService bomService;

    public String getItemTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setItemClass(this.getItemClass(print.getObjectId()));
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMItem plmItem = this.getItemDetails(print.getObjectId());
                this.printDTO.setItem(plmItem);

            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getItemAttributes(print.getObjectId());
                PrintDTO dto1 = this.getItemCustomAttributes(print.getObjectId());
                this.printDTO.setItemCustomAttributes(dto1.getItemCustomAttributes());
                this.printDTO.setItemCustomRevAttributes(dto1.getItemCustomRevAttributes());
                this.printDTO.setItemMasterAttributes(dto.getItemMasterAttributes());
                this.printDTO.setItemRevAttributes(dto.getItemRevAttributes());
                this.printDTO.setGroupMasterAttributes(dto.getGroupMasterAttributes());
                this.printDTO.setGroupRevisionAttributes(dto.getGroupRevisionAttributes());

            } else if (option.equals("BOM")) {
                this.printDTO.setPlmBom(bomService.getTotalBomForPrint(print.getObjectId()));

            } else if (option.equals("Changes")) {
                this.printDTO.setChanges(this.getChanges(print.getObjectId()));
                this.printDTO.setEcrs(this.getEcrs(print.getObjectId()));
                this.printDTO.setDcos(this.getDcos(print.getObjectId()));
                this.printDTO.setDcrs(this.getDcrs(print.getObjectId()));

            } else if (option.equals("Quality")) {
                this.printDTO.setItemPrs(this.getItemPrs(print.getObjectId()));
                this.printDTO.setProductPrs(this.getProductItemPRs(print.getObjectId()));
                this.printDTO.setItemQcrs(this.getItemQcrs(print.getObjectId()));

            } else if (option.equals("Files")) {
                this.printDTO.setFileDto(this.objectFileService.getObjectFiles(print.getObjectId(), PLMObjectType.ITEM,false));

            } else if (option.equals("Projects")) {
                this.printDTO.setItemProjects(this.itemService.getProjectItems(print.getObjectId()));
            } else if (option.equals("Manufacturer Parts")) {
                this.printDTO.setItemmParts(this.itemService.getItemMfrParts(print.getObjectId()));

            } else if (option.equals("Requirements")) {
                this.printDTO.setItemReqs(this.itemService.getItemRequirements(print.getObjectId()));
            } else if (option.equals("Inspections")) {
                List<InspectionsDto> inspections = this.inspectionService.getInspectionsByItem(print.getObjectId());
                this.printDTO.setItemInspections(this.inspectionService.getInspectionsByItem(print.getObjectId()));
            } else if (option.equals("Specifications")) {
                this.printDTO.setItemSpecifications(this.itemService.getItemSpecifications(print.getObjectId()));
            } else if (option.equals("Compliance Report")) {
                this.printDTO.setBomComplianceReport(this.getBomItemComplianceReport(print.getObjectId()));
            }
        }

        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "itemTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private void push(Object object, List list) {
        list.add(list.size(), object);
    }


    private PrintDTO getItemCustomAttributes(Integer itemId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<PrintAttributesDTO> itemCustomRevisionAttributes = new LinkedList<>();
        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevisionRepository.findOne(itemId).getItemMaster());
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("ITEM"));
        List<ObjectTypeAttribute> objTypeRevAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("ITEMREVISION"));

        setAttributeDTO(objTypeRevAttr, revision, itemCustomAttributes);
        setAttributeDTO(objTypeRevAttr, revision, itemCustomRevisionAttributes);
        dto.setItemCustomAttributes(itemCustomAttributes);
        dto.setItemCustomRevAttributes(itemCustomRevisionAttributes);
        return dto;
    }

    private void setAttributeDTO(List<ObjectTypeAttribute> objTypeRevAttr, PLMItemRevision revision, List<PrintAttributesDTO> itemCustomRevisionAttributes) {
        for (int k = 0; k < objTypeRevAttr.size(); k++) {
            ObjectTypeAttribute type = objTypeRevAttr.get(k);
            ObjectAttribute objAttr = objectAttributeRepository.findByObjectIdAndAttributeDefId(revision.getId(), type.getId());
            if (objAttr != null && type != null) {
                PrintAttributesDTO rev = printAttributesDTO.getItemCustomAttributes(type, objAttr);
                itemCustomRevisionAttributes.add(rev);
            }

        }
    }

    private String getThumbnail(byte[] thumbnail) {
        String baseString = "";
        byte[] imgBytesAsBase64 = Base64.encodeBase64(thumbnail);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        baseString = "data:image/png;base64," + imgDataAsBase64;
        return baseString;
    }

    private PLMItem getItemClass(Integer itemId) {
        PLMItem item = itemRepository.findOne(itemRevisionRepository.findOne(itemId).getItemMaster());
        if (item.getItemType().getItemClass() != null) {
            item.setiClass(item.getItemType().getItemClass().toString());
        }
        item.setMake(item.getMakeOrBuy().toString());
        return item;
    }

    private PLMItem getItemDetails(Integer itemId) {
        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevisionRepository.findOne(itemId).getItemMaster());
        if (item.getThumbnail() != null) {
            item.setThumbnailString(getThumbnail(item.getThumbnail()));
        } else {
            item.setThumbnailString(null);
        }
        Person created = personRepository.findOne(item.getCreatedBy());
        Person modified = personRepository.findOne(item.getModifiedBy());
        item.setCreatedPerson(created.getFirstName());
        item.setModifiedPerson(modified.getFirstName());
        item.setRev(revision);
        return item;
    }


    private List<BomDto> getBom(Integer itemId) {
        individualList = new LinkedList<>();

        List<BomDto> dtoList = bomService.getTotalBomForPrint(itemId);
        /*PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevisionRepository.findOne(itemId).getItemMaster());
        List<PLMBom> imediateChilds = bomRepository.findByParentOrderBySequenceAsc(revision);
        for (int i = 0; i < imediateChilds.size(); i++) {
            PLMBom bom = imediateChilds.get(i);
            bom.setLevel(0);
            PLMItemRevision rev = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            if (bom.getItem().getThumbnail() != null) {
                bom.getItem().setThumbnailString(getThumbnail(bom.getItem().getThumbnail()));
            } else {
                bom.getItem().setThumbnailString(null);
            }
            bom.setRev(rev.getRevision());
            bom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            push(bom, this.individualList);
            visitBomChildren(bom);
        }*/
        return dtoList;
    }

    private PLMBom visitBomChildren(PLMBom bom) {
        PLMItemRevision parentItem = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
        List<PLMBom> children = bomRepository.findByParentOrderBySequenceAsc(parentItem);
        children.forEach(plmBom -> {
            plmBom.setLevel(bom.getLevel() + 1);
            PLMItemRevision rev = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
            if (bom.getItem().getThumbnail() != null) {
                bom.getItem().setThumbnailString(getThumbnail(bom.getItem().getThumbnail()));
            } else {
                bom.getItem().setThumbnailString(null);
            }
            plmBom.setRev(rev.getRevision());
            plmBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            push(plmBom, this.individualList);
            plmBom = visitBomChildren(plmBom);

        });

        bom.getChildrens().addAll(children);
        return bom;
    }


    private PrintDTO getItemAttributes(Integer itemId) {
        PrintDTO dto = new PrintDTO();

        List<PrintAttributesDTO> masters = new ArrayList<>();
        List<PrintAttributesDTO> revisions = new ArrayList<>();
        List<PrintAttributesDTO> itemMasterAttributes = new LinkedList<>();
        List<PrintAttributesDTO> itemrevisionAttributes = new LinkedList<>();
        PLMItemRevision revision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevisionRepository.findOne(itemId).getItemMaster());
        List<PLMItemAttribute> masterAttributes = itemAttributeRepository.getByItemId(item.getId());
        List<PLMItemRevisionAttribute> revAttributes = itemRevisionAttributeRepository.getByItemId(revision.getId());
        for (int i = 0; i < masterAttributes.size(); i++) {
            PLMItemAttribute masterAttr = masterAttributes.get(i);
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            PrintAttributesDTO master = printAttributesDTO.getMasterAttributes(itemTypeAttribute, masterAttr);
            if (itemTypeAttribute.getAttributeGroup() != null && itemTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(itemTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                itemMasterAttributes.add(master);
            }
        }
        Map<String, List<PrintAttributesDTO>> attributesMaterForGroup = this.printService.getAttributeByGroup(masters);
        for (int i = 0; i < revAttributes.size(); i++) {
            PLMItemRevisionAttribute revAttr = revAttributes.get(i);
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(revAttr.getId().getAttributeDef());
            PrintAttributesDTO rev = printAttributesDTO.getRevisionAttributes(itemTypeAttribute, revAttr);
            if (itemTypeAttribute.getAttributeGroup() != null && itemTypeAttribute.getAttributeGroup() != "") {
                rev.setGroup(itemTypeAttribute.getAttributeGroup());
                revisions.add(rev);
            } else {
                itemrevisionAttributes.add(rev);
            }
        }
        Map<String, List<PrintAttributesDTO>> attributesRevsionForGroup = this.printService.getAttributeByGroup(revisions);
        dto.setItemMasterAttributes(itemMasterAttributes);
        dto.setItemRevAttributes(itemrevisionAttributes);
        dto.setGroupMasterAttributes(attributesMaterForGroup);
        dto.setGroupRevisionAttributes(attributesRevsionForGroup);
        return dto;
    }


    @Transactional(readOnly = true)
    public List<PLMDCO> findMultiple(List<Integer> ids) {
        List<PLMDCO> plmDcos = dcoRepository.findByIdIn(ids);
        for (PLMDCO dco : plmDcos) {
            if (dco != null) {
                Person person = personRepository.findOne(dco.getChangeAnalyst());
                if (dco.getWorkflow() != null) {
                    PLMWorkflow wfl = plmWorkflowRepository.findOne(dco.getWorkflow());
                    dco.setPrintWorkFlow(wfl.getName());
                }
                dco.setType(dco.getChangeType().toString());
                dco.setPrintAnalyst(person.getFirstName());

            }
        }
        return plmDcos;
    }

    @Transactional(readOnly = true)
    public List<PLMECO> getChanges(Integer itemId) {
        List<PLMECO> ecos = new ArrayList<>();
        List<PLMAffectedItem> affectedItems = new ArrayList<>();
        affectedItems.addAll(affectedItemRepository.findByItem(itemId));
        affectedItems.addAll(affectedItemRepository.findByToItem(itemId));

        for (PLMAffectedItem item : affectedItems) {
            PLMECO plmeco = ecoRepository.findOne(item.getChange());
            if (plmeco != null) {
                Person person = personRepository.findOne(plmeco.getEcoOwner());
                if (plmeco.getWorkflow() != null) {
                    PLMWorkflow wfl = plmWorkflowRepository.findOne(plmeco.getWorkflow());
                    plmeco.setPrintWorkFlow(wfl.getName());
                }
                plmeco.setType(plmeco.getChangeType().toString());
                plmeco.setPrintAnalyst(person.getFirstName());
                plmeco.setFromRev(item.getFromRevision());
                plmeco.setToRev(item.getToRevision());
                ecos.add(plmeco);
            }
        }
        return ecos;
    }


    @Transactional(readOnly = true)
    public List<PLMECR> getEcrs(Integer item) {
        List<PLMECR> plmecrs = new ArrayList<>();
        List<PLMECRAffectedItem> affectedItems = ecrAffectedItemRepository.findByItem(item);
        for (PLMECRAffectedItem affectedItem : affectedItems) {
            PLMECR plmecr = ecrRepository.findOne(affectedItem.getEcr());
            Person person = personRepository.findOne(plmecr.getOriginator());
            if (plmecr.getWorkflow() != null) {
                PLMWorkflow wfl = plmWorkflowRepository.findOne(plmecr.getWorkflow());
                plmecr.setPrintWorkFlow(wfl.getName());
            }
            plmecr.setType(plmecr.getChangeType().toString());
            plmecr.setPrintAnalyst(person.getFirstName());
            plmecrs.add(plmecr);
        }

        return plmecrs;
    }


    @Transactional(readOnly = true)
    public List<PLMDCO> getDcos(Integer itemId) {
        List<Integer> ids = new ArrayList<>();
        List<PLMAffectedItem> affectedItems = new ArrayList<>();
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        List<PLMItemRevision> revisions = itemRevisionRepository.getByItemMasterOrderByCreatedDateDesc(plmItem.getId());
        if (revisions.size() > 0) {
            for (PLMItemRevision itemRevision : revisions) {
                List<PLMAffectedItem> affectedItem = affectedItemRepository.findByItem(itemRevision.getId());
                affectedItems.addAll(affectedItem);
            }
        }

        for (PLMAffectedItem item : affectedItems) {
            ids.add(item.getChange());
        }
        return findMultiple(ids);
    }


    @Transactional(readOnly = true)
    public List<PLMDCR> getDcrs(Integer item) {
        List<PLMDCR> plmdcrs = new ArrayList<>();
        List<PLMDCRAffectedItem> affectedItems = dcrAffectedItemRepository.findByItem(item);
        for (PLMDCRAffectedItem affectedItem : affectedItems) {
            PLMDCR dcr = dcrRepository.findOne(affectedItem.getDcr());
            if (dcr != null) {
                Person person = personRepository.findOne(dcr.getOriginator());
                if (dcr.getWorkflow() != null) {
                    PLMWorkflow wfl = plmWorkflowRepository.findOne(dcr.getWorkflow());
                    dcr.setPrintWorkFlow(wfl.getName());
                }
                dcr.setType(dcr.getChangeType().toString());
                dcr.setPrintAnalyst(person.getFirstName());

            }
            plmdcrs.add(dcr);
        }

        return plmdcrs;
    }


    @Transactional(readOnly = true)
    public List<PQMProblemReport> getItemPrs(Integer itemId) {
        List<PQMPRProblemItem> pqmprProblemItems = prProblemItemRepository.findByItem(itemId);
        List<PQMProblemReport> pqmProblemReports = new ArrayList<>();
        for (PQMPRProblemItem pqmprProblemItem : pqmprProblemItems) {
            PQMProblemReport prItem = problemReportRepository.findOne(pqmprProblemItem.getProblemReport());
            if (prItem != null) {
                Person person = personRepository.findOne(prItem.getCreatedBy());
                if (prItem.getWorkflow() != null) {
                    PLMWorkflow wfl = plmWorkflowRepository.findOne(prItem.getWorkflow());
                    prItem.setPrintWorkFlow(wfl.getName());
                }
                prItem.setType(prItem.getPrType().getName());
                prItem.setPrintAnalyst(person.getFirstName());
            }
            pqmProblemReports.add(prItem);
        }
        return pqmProblemReports;
    }


    @Transactional(readOnly = true)
    public List<PQMProblemReport> getProductItemPRs(Integer itemId) {
        List<PQMProblemReport> problemReports = new ArrayList<>();
        for (PQMProblemReport pr : problemReportRepository.findByProduct(itemId)) {
            Person person = personRepository.findOne(pr.getCreatedBy());
            if (pr.getWorkflow() != null) {
                PLMWorkflow wfl = plmWorkflowRepository.findOne(pr.getWorkflow());
                pr.setPrintWorkFlow(wfl.getName());
            }
            pr.setType(pr.getPrType().getName());
            pr.setPrintAnalyst(person.getFirstName());
            problemReports.add(pr);
        }
        return problemReports;
    }


    @Transactional(readOnly = true)
    public List<PQMQCR> getItemQcrs(Integer itemId) {
        List<PQMQCRProblemItem> pqmprProblemItems = qcrProblemItemRepository.findByItem(itemId);
        List<PQMQCR> pqmqcrs = new ArrayList<>();
        for (PQMQCRProblemItem pqmprProblemItem : pqmprProblemItems) {
            PQMQCR qcr = qcrRepository.findOne(pqmprProblemItem.getQcr());
            if (qcr != null) {
                Person person = personRepository.findOne(qcr.getCreatedBy());
                if (qcr.getWorkflow() != null) {
                    PLMWorkflow wfl = plmWorkflowRepository.findOne(qcr.getWorkflow());
                    qcr.setPrintWorkFlow(wfl.getName());
                }
                qcr.setQcrForPrint(qcr.getQcrFor().name().toString());
                qcr.setType(qcr.getQcrType().getName());
                qcr.setPrintAnalyst(person.getFirstName());
                pqmqcrs.add(qcr);
            }

        }
        return pqmqcrs;
    }


    @Transactional(readOnly = true)
    public List<PLMItemManufacturerPart> getItemMfrParts(Integer id) {
        List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByItem(id);
        itemManufacturerParts.forEach(plmItemManufacturerPart -> {
            PLMManufacturer mfr = manufacturerRepository.findOne(plmItemManufacturerPart.getManufacturerPart().getManufacturer());
            plmItemManufacturerPart.getManufacturerPart().setStatusPrint(mfr.getMfrType().getName());
            plmItemManufacturerPart.getManufacturerPart().setMfrName(mfr.getName());
        });
        return itemManufacturerParts;
    }

    @Transactional
    public BomComplianceReport getBomItemComplianceReport(Integer itemId) {
        BomComplianceReport bomComplianceReport = new BomComplianceReport();
        List<BomComplianceData> bomItems = new LinkedList<>();

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PGCItemSpecification> itemSpecifications = pgcItemSpecificationRepository.getItemSpecifications(itemId);

        BomComplianceData bomComplianceData = new BomComplianceData();
        bomComplianceData.setItemName(item.getItemName());
        bomComplianceData.setItemNumber(item.getItemNumber());
        bomComplianceData.setItemId(item.getId());
        bomComplianceData.setItemRevision(itemRevision.getId());
        bomComplianceData.setLevel(0);
        for (PGCItemSpecification itemSpecification : itemSpecifications) {
            ComplianceSpecification specification = new ComplianceSpecification();
            specification.setId(itemSpecification.getSpecification().getId());
            specification.setName(itemSpecification.getSpecification().getName());

            ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);

            bomComplianceData.getSpecifications().add(complianceSpecification);
        }
        bomComplianceReport.getBomItems().add(bomComplianceData);
        bomComplianceData = visitChildrenForBomComplianceReport(bomComplianceReport.getBomItems(), bomComplianceData, bomComplianceData.getSpecifications());

        bomComplianceReport.setBomComplianceData(bomComplianceData);
        itemSpecifications.forEach(pgcItemSpecification -> {
            ComplianceSpecification specification = new ComplianceSpecification();
            specification.setId(pgcItemSpecification.getSpecification().getId());
            specification.setName(pgcItemSpecification.getSpecification().getName());
            ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);
            bomComplianceReport.getSpecifications().add(complianceSpecification);
        });

        for (ComplianceSpecification specification : bomComplianceData.getSpecifications()) {
            Integer nonComplaintCount = 0;
            for (BomComplianceData childItem : bomComplianceData.getChildren()) {
                for (ComplianceSpecification childSpec : childItem.getSpecifications()) {
                    if (childSpec.getId().equals(specification.getId())) {
                        if (!childSpec.getCompliant() && !childSpec.getExempt()) {
                            nonComplaintCount++;
                        }
                    }
                }
            }
            if (nonComplaintCount == 0) {
                specification.setCompliant(true);
            }
        }

        return bomComplianceReport;
    }

    private BomComplianceData visitChildrenForBomComplianceReport(List<BomComplianceData> bomItems, BomComplianceData bomComplianceData, List<ComplianceSpecification> itemSpecifications) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomComplianceData.getItemRevision());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        if (bomList.size() > 0) {
            bomComplianceData.setHasBom(true);
        }
        bomList.forEach(bom -> {
            BomComplianceData complianceData = new BomComplianceData();
            PLMItemRevision revision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());

            complianceData.setItemName(item.getItemName());
            complianceData.setItemNumber(item.getItemNumber());
            complianceData.setItemId(item.getId());
            complianceData.setItemRevision(revision.getId());
            complianceData.setParent(bom.getParent().getId());
            complianceData.setRequireCompliance(bom.getItem().getRequireCompliance());
            for (ComplianceSpecification itemSpecification : itemSpecifications) {
                ComplianceSpecification specification = new ComplianceSpecification();
                specification.setId(itemSpecification.getId());
                specification.setName(itemSpecification.getName());
                ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);
                complianceData.getSpecifications().add(complianceSpecification);
            }
            PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(bom.getItem().getLatestRevision(), ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = itemManufacturerPartRepository.findByStatusAndItemOrderByIdAsc(ManufacturerPartStatus.ALTERNATE, bom.getItem().getLatestRevision());
                if (manufacturerParts.size() > 0) {
                    complianceData.setPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    complianceData.setPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                    complianceData.setPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                    complianceData.setMfrName(manufacturerRepository.findOne(manufacturerParts.get(0).getManufacturerPart().getManufacturer()).getName());
                }
            } else {
                complianceData.setPartId(itemManufacturerPart.getManufacturerPart().getId());
                complianceData.setPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
                complianceData.setPartName(itemManufacturerPart.getManufacturerPart().getPartName());
                complianceData.setMfrName(manufacturerRepository.findOne(itemManufacturerPart.getManufacturerPart().getManufacturer()).getName());
            }
            if (bom.getItem().getMakeOrBuy().equals(MakeOrBuy.MAKE)) {
                complianceData.getSpecifications().forEach(complianceSpecification -> {
                    complianceSpecification.setExempt(true);
                });
            } else {
                if (complianceData.getRequireCompliance()) {
                    if (complianceData.getPartId() != null) {
                        List<PGCDeclarationPart> declarationParts = pgcDeclarationPartRepository.getDeclarationPartByPart(complianceData.getPartId());
                        if (declarationParts.size() > 0) {
                            for (PGCDeclarationPart declarationPart : declarationParts) {
                                List<PGCBosItem> bosItems = new ArrayList<PGCBosItem>();
                                if (declarationPart.getBos() != null) {
                                    bosItems = pgcBosItemRepository.findByBos(declarationPart.getBos());
                                }
                                if (bosItems.size() > 0) {
                                    List<PGCDeclarationPartCompliance> declarationPartCompliances = pgcDeclarationPartComplianceRepository.findByDeclarationPart(declarationPart.getId());
                                    if (declarationPartCompliances.size() > 0) {
                                        for (ComplianceSpecification itemSpecification : complianceData.getSpecifications()) {
                                            if (!complianceData.getRequireCompliance()) {
                                                itemSpecification.setCompliant(false);
                                            } else {
                                                for (PGCDeclarationPartCompliance declarationPartCompliance : declarationPartCompliances) {
                                                    PGCDeclarationSpecification declarationSpecification = pgcDeclarationSpecificationRepository.findOne(declarationPartCompliance.getDeclarationSpec());
                                                    if (declarationSpecification != null && declarationSpecification.getSpecification().getId().equals(itemSpecification.getId())) {
                                                        itemSpecification.setCompliant(declarationPartCompliance.getCompliant());

                                                        for (PGCBosItem bosItem : bosItems) {
                                                            PGCSubstance substance = pgcSubstanceRepository.findOne(bosItem.getSubstance());
                                                            PGCSpecificationSubstance specificationSubstance = pgcSpecificationSubstanceRepository.findBySpecificationAndSubstance(itemSpecification.getId(), substance);
                                                            if (specificationSubstance != null) {
                                                                PGCBosItem pgcBosItem = JsonUtils.cloneEntity(bosItem, PGCBosItem.class);
                                                                BosItemDto bosItemDto = new BosItemDto();
                                                                bosItemDto.setId(pgcBosItem.getId());
                                                                bosItemDto.setBos(pgcBosItem.getBos());
                                                                bosItemDto.setBosItemType(pgcBosItem.getBosItemType());
                                                                if (pgcBosItem.getBosItemType().equals(BosItemType.SUBSTANCE)) {
                                                                    bosItemDto.setSubstance(substance.getId());
                                                                    bosItemDto.setCasNumber(substance.getCasNumber());
                                                                    bosItemDto.setSubstanceName(substance.getName());
                                                                    bosItemDto.setSubstanceType(substance.getType().getName());
                                                                }
                                                                bosItemDto.setMass(pgcBosItem.getMass());
                                                                if (pgcBosItem.getUom() != null) {
                                                                    MeasurementUnit measurementUnit = measurementUnitRepository.findOne(pgcBosItem.getUom());
                                                                    bosItemDto.setUnitName(measurementUnit.getName());
                                                                    bosItemDto.setUnitSymbol(measurementUnit.getSymbol());
                                                                }
                                                                bosItemDto.setSpecMass(specificationSubstance.getThresholdMass());
                                                                itemSpecification.getBillOfSubstances().add(bosItemDto);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    complianceData.getSpecifications().forEach(complianceSpecification -> {
                        complianceSpecification.setExempt(true);
                    });
                }
            }
            complianceData.setLevel(bomComplianceData.getLevel() + 1);
            bomItems.add(complianceData);
            complianceData = visitChildrenForBomComplianceReport(bomItems, complianceData, itemSpecifications);


            for (ComplianceSpecification specification : complianceData.getSpecifications()) {
                if (complianceData.getChildren().size() > 0) {
                    Integer nonComplaintCount = 0;
                    for (BomComplianceData childItem : complianceData.getChildren()) {
                        for (ComplianceSpecification childSpec : childItem.getSpecifications()) {
                            if (childSpec.getId().equals(specification.getId())) {
                                if (!childSpec.getCompliant() && !childSpec.getExempt()) {
                                    nonComplaintCount++;
                                }
                            }
                        }
                    }
                    if (nonComplaintCount == 0) {
                        specification.setCompliant(true);
                    }
                }
            }

        });

        return bomComplianceData;
    }


}