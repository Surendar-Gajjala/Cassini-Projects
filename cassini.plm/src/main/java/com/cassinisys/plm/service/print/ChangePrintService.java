package com.cassinisys.plm.service.print;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.config.MessageResolverMethod;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.dto.Print;
import com.cassinisys.plm.model.dto.print.ChangeAttributeDTO;
import com.cassinisys.plm.model.dto.print.PrintAttributesDTO;
import com.cassinisys.plm.model.dto.print.PrintDTO;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
import com.cassinisys.plm.service.cm.*;
import com.cassinisys.plm.service.plm.RelatedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChangePrintService {
    public static Map fileMap = new HashMap();
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private AffectedItemRepository affectedItemRepository;
    @Autowired
    private ChangeAttributeRepository changeAttributeRepository;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private DCRRepository dcrRepository;
    @Autowired
    private DCORepository dcoRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private ECRRepository ecrRepository;
    @Autowired
    private VarianceRepository varianceRepository;
    @Autowired
    private PrintService printService;
    private PrintDTO printDTO;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ChangeAttributeDTO changeAttributeDTO;
    @Autowired
    private PrintAttributesDTO printAttributesDTO;
    @Autowired
    private ECRService ecrService;
    @Autowired
    private ECOService ecoService;
    @Autowired
    private RelatedItemService relatedItemService;
    @Autowired
    private DCRService dcrService;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private PLMVarianceService plmVarianceService;
    @Autowired
    private MCORelatedItemRepository mcoRelatedItemRepository;
    @Autowired
    private ChangeFileService changeFileService;
    @Autowired
    private MessageSource messageSource;

    public String getEcoTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMECO eco = this.getEcoDetails(print.getObjectId());
                this.printDTO.setEco(eco);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
            } else if (option.equals("Change Requests")) {
                this.printDTO.setEcoChangeReqs(this.ecrService.getChangeReqItems(print.getObjectId()));

            } else if (option.equals("Affected Items")) {
                this.printDTO.setChangesAffectedItems(this.ecoService.getEcoAffectedItems(print.getObjectId()));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getEcrTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMECR ecr = this.getEcrDetails(print.getObjectId());
                this.printDTO.setEcr(ecr);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
            } else if (option.equals("Affected Items")) {
                this.printDTO.setEcrAffectedItems(this.ecrService.getAffectedItem(print.getObjectId()));

            } else if (option.equals("Problem Reports")) {
                this.printDTO.setEcrProblemReports(this.ecrService.getECRProblemReports(print.getObjectId()));

            } else if (option.equals("Related Items")) {
                this.printDTO.setRelatedItems(this.relatedItemService.getRelatedItemsByObject(print.getObjectId(), PLMObjectType.ECR));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }

    public String getDcrTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMDCR dcr = this.getDcrDetails(print.getObjectId());
                this.printDTO.setDcr(dcr);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
            } else if (option.equals("Affected Items")) {
                this.printDTO.setDcrAffectedItems(this.dcrService.getAffectedItem(print.getObjectId()));

            } else if (option.equals("Related Items")) {
                this.printDTO.setRelatedItems(this.relatedItemService.getRelatedItemsByObject(print.getObjectId(), PLMObjectType.DCR));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    public String getDcoTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMDCO dco = this.getDcoDetails(print.getObjectId());
                this.printDTO.setDco(dco);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
            } else if (option.equals("Change Requests")) {
                this.printDTO.setDcoChangeReqs(this.dcoService.getChangeReqItems(print.getObjectId()));

            } else if (option.equals("Affected Items")) {
                this.printDTO.setDcoAffectedItems(this.dcoService.getDcoAffectedItem(print.getObjectId()));

            } else if (option.equals("Related Items")) {
                this.printDTO.setRelatedItems(this.relatedItemService.getRelatedItemsByObject(print.getObjectId(), PLMObjectType.DCO));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    public String getMcoTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMMCO mco = this.getMcoDetails(print.getObjectId());
                this.printDTO.setMco(mco);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
            } else if (option.equals("Affected Items")) {
                this.printDTO.setMcoAffectedItems(this.mcoService.getAffectedItem(print.getObjectId()));

            }else if (option.equals("Affected Mbom")) {
                this.printDTO.setMcoAffectedMbom(this.mcoService.getProductAffectedItem(print.getObjectId()));

            } else if (option.equals("Related Items")) {
                this.printDTO.setMcoRelatedItems(getMcoRelatedItems(print.getObjectId()));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    public String getDeviationAndWaiverTemplate(Print print) {
        this.printDTO = new PrintDTO();
        this.printDTO.setObjectType(print.getObjectType());
        for (String option : print.getOptions()) {
            if (option.equals("Basic")) {
                PLMVariance variance = this.getVarianceDetails(print.getObjectId());
                this.printDTO.setVariance(variance);
            } else if (option.equals("Attributes")) {
                PrintDTO dto = this.getChangeAttributes(print.getObjectId());
                PrintDTO dto1 = this.getChangeCustomAttributes(print.getObjectId());
                this.printDTO.setChangeTypeCustomAttributes(dto1.getChangeTypeCustomAttributes());
                this.printDTO.setChangeTypeAttributes(dto.getChangeTypeAttributes());
                this.printDTO.setGroupChangeAttributes(dto.getGroupChangeAttributes());
            } else if (option.equals("Affected Items")) {
                if (this.printDTO.getVariance() != null) {
                    if (this.printDTO.getVariance().getWaiverAndDeviationFor().equals("ITEMS")) {
                        this.printDTO.setVarianceAffectedItems(this.plmVarianceService.getAffectedItems(print.getObjectId()));
                    }
                    if (this.printDTO.getVariance().getWaiverAndDeviationFor().equals("MATERIALS")) {
                        this.printDTO.setVarianceAffectedItems(this.plmVarianceService.getAffectedParts(print.getObjectId()));

                    }
                }


            } else if (option.equals("Related Items")) {
                this.printDTO.setRelatedItems(this.relatedItemService.getRelatedItemsByObject(print.getObjectId(), PLMObjectType.VARIANCE));

            } else if (option.equals("Files")) {
                this.printDTO.setChangeFiles(this.changeFileService.getChangeFiles(print.getObjectId()));

            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("msg", new MessageResolverMethod(messageSource, Locale.forLanguageTag(LocaleContextHolder.getLocale().toLanguageTag())));
        model.put("selectedOptions", print.getOptions());
        model.put("itemData", this.printDTO);
        String templatePath = "changeTemplate.html";
        String exportHtmlData = this.printService.getContentFromTemplate(templatePath, model);

        return exportHtmlData;
    }


    private PLMVariance getVarianceDetails(Integer varienceId) {
        PLMVariance variance = varianceRepository.findOne(varienceId);
        if (variance != null) {
            Person created = personRepository.findOne(variance.getCreatedBy());
            Person modified = personRepository.findOne(variance.getModifiedBy());
            Person originator = personRepository.findOne(variance.getOriginator());
            variance.setCreatedPerson(created.getFirstName());
            variance.setModifiedPerson(modified.getFirstName());
            variance.setPrintOriginator(originator.getFirstName());
            variance.setType(variance.getVarianceType().name().toString());
            variance.setWaiverAndDeviationFor(variance.getVarianceFor().name());
        }
        return variance;
    }

    private PLMECO getEcoDetails(Integer ecoId) {
        PLMECO plmeco = ecoRepository.findOne(ecoId);
        if (plmeco != null) {
            Person created = personRepository.findOne(plmeco.getCreatedBy());
            Person modified = personRepository.findOne(plmeco.getModifiedBy());
            Person analyst = personRepository.findOne(plmeco.getEcoOwner());
            plmeco.setCreatedPerson(created.getFirstName());
            plmeco.setModifiedPerson(modified.getFirstName());
            plmeco.setPrintAnalyst(analyst.getFirstName());
            plmeco.setType(plmeco.getChangeType().name().toString());
            plmeco.setRule(plmeco.getRevisionCreationType().toString());

        }
        return plmeco;
    }


    private PrintDTO getChangeCustomAttributes(Integer ecoId) {
        PrintDTO dto = new PrintDTO();
        List<PrintAttributesDTO> itemCustomAttributes = new LinkedList<>();
        List<ObjectTypeAttribute> objTypeAttr = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf("CHANGE"));
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

    public Map<String, List<ChangeAttributeDTO>> getAttributeByGroup(List<ChangeAttributeDTO> masters) {
        Map<String, List<ChangeAttributeDTO>> attributesMaterForGroup = new HashMap<>();
        for (ChangeAttributeDTO model : masters) {
            if (!attributesMaterForGroup.containsKey(model.getGroup())) {
                List<ChangeAttributeDTO> list = new ArrayList<ChangeAttributeDTO>();
                list.add(model);
                attributesMaterForGroup.put(model.getGroup(), list);
            } else {
                attributesMaterForGroup.get(model.getGroup()).add(model);
            }
        }
        return attributesMaterForGroup;
    }

    private PrintDTO getChangeAttributes(Integer ecoId) {
        PrintDTO dto = new PrintDTO();
        List<ChangeAttributeDTO> changeAttributes = new LinkedList<>();
        List<PLMChangeAttribute> masterAttributes = changeAttributeRepository.findByChangeIdIn(ecoId);
        List<ChangeAttributeDTO> masters = new ArrayList<>();
        for (int i = 0; i < masterAttributes.size(); i++) {
            PLMChangeAttribute masterAttr = masterAttributes.get(i);
            PLMChangeTypeAttribute changeTypeAttribute = changeTypeAttributeRepository.findOne(masterAttr.getId().getAttributeDef());
            ChangeAttributeDTO master = changeAttributeDTO.getChangeAttributes(changeTypeAttribute, masterAttr);
            if (changeTypeAttribute.getAttributeGroup() != null && changeTypeAttribute.getAttributeGroup() != "") {
                master.setGroup(changeTypeAttribute.getAttributeGroup());
                masters.add(master);
            } else {
                changeAttributes.add(master);
            }
        }

        Map<String, List<ChangeAttributeDTO>> attributesMaterForGroup = getAttributeByGroup(masters);
        dto.setGroupChangeAttributes(attributesMaterForGroup);
        dto.setChangeTypeAttributes(changeAttributes);
        return dto;
    }


    public List<PLMItem> getAffectedItems(Integer ecoID) {
        List<PLMItem> affectedItemForPrint = new LinkedList<>();
        for (PLMAffectedItem affectedItem : affectedItemRepository.findByChange(ecoID)) {
            PLMItemRevision revision = itemRevisionRepository.findOne(affectedItem.getItem());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());
            item.setMake(revision.getLifeCyclePhase().getPhase());
            item.setRev(revision);
            affectedItemForPrint.add(item);
        }

        return affectedItemForPrint;
    }

    private PLMECR getEcrDetails(Integer ecrId) {
        PLMECR plmecr = ecrRepository.findOne(ecrId);
        if (plmecr != null) {
            Person created = personRepository.findOne(plmecr.getCreatedBy());
            Person modified = personRepository.findOne(plmecr.getModifiedBy());
            Person analyst = personRepository.findOne(plmecr.getChangeAnalyst());
            Person originator = personRepository.findOne(plmecr.getOriginator());
            if (plmecr.getRequesterType().equals(RequesterType.INTERNAL) && plmecr.getRequestedBy() != null) {
                Person requestedBy = personRepository.findOne(plmecr.getRequestedBy());
                plmecr.setRequestedByPrint(requestedBy.getFirstName());
            } else if (plmecr.getRequesterType().equals(RequesterType.CUSTOMER) && plmecr.getRequestedBy() != null) {
                plmecr.setRequestedByPrint(pqmCustomerRepository.findOne(plmecr.getRequestedBy()).getName());
            } else {
                plmecr.setRequestedByPrint(plmecr.getOtherRequested());
            }
            plmecr.setCreatedPerson(created.getFirstName());
            plmecr.setModifiedPersonPrint(modified.getFirstName());
            plmecr.setPrintAnalyst(analyst.getFirstName());
            plmecr.setType(plmecr.getChangeType().name().toString());
            plmecr.setOriginatorPrint(originator.getFirstName());
            if (plmecr.getQcr() != null) {
                plmecr.setQcrPrint(plmecr.getQcr().getQcrFor().toString());
            }

            plmecr.setUrgencyPrint(plmecr.getUrgency().toString());
        }
        return plmecr;
    }

    private PLMDCR getDcrDetails(Integer dcrId) {
        PLMDCR plDcr = dcrRepository.findOne(dcrId);
        if (plDcr != null) {
            Person created = personRepository.findOne(plDcr.getCreatedBy());
            Person modified = personRepository.findOne(plDcr.getModifiedBy());
            Person analyst = personRepository.findOne(plDcr.getChangeAnalyst());
            Person originator = personRepository.findOne(plDcr.getOriginator());
            Person requestedBy = personRepository.findOne(plDcr.getRequestedBy());
            plDcr.setCreatedPerson(created.getFirstName());
            plDcr.setModifiedPersonPrint(modified.getFirstName());
            plDcr.setPrintAnalyst(analyst.getFirstName());
            plDcr.setType(plDcr.getChangeType().name().toString());
            plDcr.setOriginatorPrint(originator.getFirstName());
            plDcr.setRequestedByPrint(requestedBy.getFirstName());
            plDcr.setUrgencyPrint(plDcr.getUrgency().toString());
        }
        return plDcr;
    }

    private PLMDCO getDcoDetails(Integer dcoId) {
        PLMDCO dco = dcoRepository.findOne(dcoId);
        if (dco != null) {
            Person created = personRepository.findOne(dco.getCreatedBy());
            Person modified = personRepository.findOne(dco.getModifiedBy());
            Person analyst = personRepository.findOne(dco.getChangeAnalyst());
            dco.setCreatedPerson(created.getFirstName());
            dco.setModifiedPersonPrint(modified.getFirstName());
            dco.setPrintAnalyst(analyst.getFirstName());
            dco.setRule(dco.getRevisionCreationType().toString());
            dco.setType(dco.getChangeType().name().toString());
        }
        return dco;
    }

    private PLMMCO getMcoDetails(Integer mcoId) {
        PLMMCO mco = mcoRepository.findOne(mcoId);
        if (mco != null) {
            Person created = personRepository.findOne(mco.getCreatedBy());
            Person modified = personRepository.findOne(mco.getModifiedBy());
            Person analyst = personRepository.findOne(mco.getChangeAnalyst());
            mco.setCreatedPerson(created.getFirstName());
            mco.setModifiedPersonPrint(modified.getFirstName());
            mco.setPrintAnalyst(analyst.getFirstName());
            mco.setType(mco.getChangeType().name().toString());
        }
        return mco;
    }

    @Transactional(readOnly = true)
    public List<PLMMCORelatedItem> getMcoRelatedItems(Integer mcr) {
        List<PLMMCORelatedItem> mcoRelatedItems = mcoRelatedItemRepository.findByMco(mcr);
        for (PLMMCORelatedItem relatedItem : mcoRelatedItems) {
            relatedItem.setPartNumberPrint(relatedItem.getPart().getPartNumber());
            relatedItem.setPartNamePrint(relatedItem.getPart().getPartName());
            relatedItem.setPartTypePrint(relatedItem.getPart().getMfrPartType().getName());
        }
        return mcoRelatedItems;
    }
}