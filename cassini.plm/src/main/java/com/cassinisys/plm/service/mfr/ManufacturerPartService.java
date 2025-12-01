package com.cassinisys.plm.service.mfr;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.ManufacturerEvents;
import com.cassinisys.plm.event.ManufacturerPartEvents;
import com.cassinisys.plm.filtering.ItemManufacturerPartCriteria;
import com.cassinisys.plm.filtering.ItemManufacturerPartPredicateBuilder;
import com.cassinisys.plm.filtering.ManufacturerPartCriteria;
import com.cassinisys.plm.filtering.ManufacturerPartPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.ItemMfrPartsDto;
import com.cassinisys.plm.model.dto.SubscribeMailDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.activitystream.ManufacturerPartActivityStream;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASVersionedFileDTO;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Home on 4/25/2016.
 */
@Service
public class ManufacturerPartService implements CrudService<PLMManufacturerPart, Integer> {

    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private ManufacturerPartAttributeRepository manufacturerPartAttributeRepository;
    @Autowired
    private ManufacturerPartPredicateBuilder partPredicateBuilder;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private ItemManufacturerPartPredicateBuilder itemManufacturerPartPredicateBuilder;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkFlowStatusAssignmentRepository workFlowStatusAssignmentRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private ManufacturerPartActivityStream manufacturerPartActivityStream;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private ObjectFileService objectFileService;


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#manufacturerPart,'create')")
    public PLMManufacturerPart create(PLMManufacturerPart manufacturerPart) {
        checkNotNull(manufacturerPart);
        manufacturerPart.setId(null);
        Integer workflowDef = null;
        if (manufacturerPart.getWorkflowDefId() != null) {
            workflowDef = manufacturerPart.getWorkflowDefId();
        }
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(manufacturerPart.getMfrPartType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        manufacturerPart.setLifeCyclePhase(lifeCyclePhase);
        manufacturerPart = manufacturerPartRepository.save(manufacturerPart);
        if (workflowDef != null) {
            attachMfrPartWorkflow(manufacturerPart.getId(), workflowDef);
        }
        PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerPart.getManufacturer());
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartCreatedEvent(manufacturerPart));
        applicationEventPublisher.publishEvent(new ManufacturerEvents.MfrPartCreatedEvent(manufacturerPart, manufacturer));
        return manufacturerPart;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#manufacturerPart.id ,'edit')")
    public PLMManufacturerPart update(PLMManufacturerPart manufacturerPart) {
        PLMManufacturerPart olmMfrPart = JsonUtils.cloneEntity(manufacturerPartRepository.findOne(manufacturerPart.getId()), PLMManufacturerPart.class);
        checkNotNull(manufacturerPart);
        checkNotNull(manufacturerPart.getId());
        manufacturerPart = manufacturerPartRepository.save(manufacturerPart);
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartBasicInfoUpdatedEvent(olmMfrPart, manufacturerPart));
        try {
            sendMfrPartSubscribeNotification(manufacturerPart, manufacturerPartActivityStream.getManufacturerPartBasicInfoUpdatedJson(olmMfrPart, manufacturerPart), "basic");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return manufacturerPart;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        PLMManufacturer manufacturer = manufacturerRepository.findOne(manufacturerPart.getManufacturer());
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(manufacturerPart.getId());
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        if (manufacturerPart == null) {
            throw new ResourceNotFoundException();
        }
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerPartDeletedEvent(manufacturer, manufacturerPart));
        manufacturerPartRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerPart get(Integer id) {
        checkNotNull(id);
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        if (manufacturerPart == null) {
            throw new ResourceNotFoundException();
        }
         //Adding workflow relavent settings
         WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(manufacturerPart.getId());
         manufacturerPart.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
         manufacturerPart.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
         manufacturerPart.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return manufacturerPart;
    }

    public Boolean setWorkflowStart(PLMWorkflow workflow) {
        Boolean val = false;
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    val = true;
                }
            }
        }
        return val;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPart> getPartsByType(Integer id) {
        return manufacturerPartRepository.findBYMfrPartTypeAndLifeCyclePhase(id, LifeCyclePhaseType.RELEASED);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPart> findMultipleMfrParts(List<Integer> ids) {
        return manufacturerPartRepository.findByIdIn(ids);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPart> getAll() {
        return manufacturerPartRepository.findAll();
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturerPart> findByItem(Pageable pageable, ItemManufacturerPartCriteria itemManufacturerPartCriteria) {
        Predicate predicate = itemManufacturerPartPredicateBuilder.build(itemManufacturerPartCriteria, QPLMManufacturerPart.pLMManufacturerPart);
        Page<PLMManufacturerPart> parts = manufacturerPartRepository.findAll(predicate, pageable);
      /*  List<PLMManufacturerPart> parts = new ArrayList<>();
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        Page<PLMManufacturerPart> manufacturerParts =  manufacturerPartRepository.findAll(pageable);
        for(PLMManufacturerPart manufacturerPart: manufacturerParts){
            if(manufacturerPart.getLifeCyclePhase().getPhase().equals("Qualified")){
                parts.add(manufacturerPart);
            }
        }
        return manufacturerParts;*/
        return parts;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPart> findAllPartsByMfr(Integer manufacturer) {
        return manufacturerPartRepository.findByManufacturerOrderByModifiedDateDesc(manufacturer);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturerPart> find(ManufacturerPartCriteria criteria, Pageable pageable) {
        Predicate predicate = partPredicateBuilder.build(criteria, QPLMManufacturerPart.pLMManufacturerPart);
        return manufacturerPartRepository.findAll(predicate, pageable);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerPart getByPartNumber(String partNumber) {
        checkNotNull(partNumber);
        return manufacturerPartRepository.findByPartNumber(partNumber);
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerPart partByNumberAndType(String partNumber, Integer type) {
        checkNotNull(partNumber);
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.findOne(type);
        return manufacturerPartRepository.findByPartNumberAndMfrPartType(partNumber, manufacturerPartType);
    }

    public PLMManufacturerPart getPartByMfrAndNumberAndType(Integer mfrId, String partNumber, Integer type) {
        checkNotNull(partNumber);
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.findOne(type);
        return manufacturerPartRepository.findByManufacturerAndPartNumberAndMfrPartType(mfrId, partNumber, manufacturerPartType);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturerPart> freeTextSearch(Pageable pageable, ManufacturerPartCriteria criteria) {
        Predicate predicate = partPredicateBuilder.build(criteria, QPLMManufacturerPart.pLMManufacturerPart);
        return manufacturerPartRepository.findAll(predicate, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMItemType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMItemType> children = itemTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMItemType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public void saveMfrPartAttributes(List<PLMManufacturerPartAttribute> attributes) {
        for (PLMManufacturerPartAttribute attribute : attributes) {
            PLMManufacturerPartTypeAttribute mfrTypeAttr = manufacturerPartTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || mfrTypeAttr.getDataType().toString().equals("FORMULA") ||
                    attribute.getStringValue() != null || attribute.getHyperLinkValue() != null || attribute.getIntegerValue() != null || attribute.getRichTextValue() != null || attribute.getLongTextValue() != null || attribute.getBooleanValue()) {
                manufacturerPartAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public PLMManufacturerPartAttribute createMfrPartAttributes(PLMManufacturerPartAttribute attributes) {
        return manufacturerPartAttributeRepository.save(attributes);

    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PLMManufacturerPartAttribute updateMfrPartAttribute(PLMManufacturerPartAttribute attribute) {
        PLMManufacturerPartAttribute oldValue = manufacturerPartAttributeRepository.findByMfrPartAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMManufacturerPartAttribute.class);
        attribute = manufacturerPartAttributeRepository.save(attribute);
          /* App events */
        PLMManufacturerPart part = manufacturerPartRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartAttributesUpdatedEvent(part, oldValue, attribute));
        return attribute;
    }

    public List<PLMManufacturerPartAttribute> getMfrPartAttributes(Integer mfrPartId) {
        List<PLMManufacturerPartAttribute> partAttributes = manufacturerPartAttributeRepository.findByIdIn(mfrPartId);
        return partAttributes;
    }

    @Transactional
    public Page<PLMManufacturerPart> getMostUsedMfrParts(Pageable pageable) {
        List<PLMItemManufacturerPart> parts = itemManufacturerPartRepository.findAll();
        List<Integer> partsDisnt = itemManufacturerPartRepository.findByManufacturerPartDistinct();
        Map<Integer, Integer> maps = new TreeMap<>();
        List<PLMManufacturerPart> manufacturerParts = new ArrayList();
        for (Integer part : partsDisnt) {
            Integer count = 0;
            for (PLMItemManufacturerPart part3 : parts) {
                if (part.equals(part3.getManufacturerPart())) {
                    count++;
                }
            }
            maps.put(part, count);
        }
        List<Map.Entry<Integer, Integer>> entries = entriesSortedByValues(maps);
        for (Map.Entry<Integer, Integer> integerEntry : entries) {
            Integer partId = integerEntry.getKey();
            PLMManufacturerPart plmManufacturerPart = manufacturerPartRepository.findOne(partId);
            plmManufacturerPart.setCount(integerEntry.getValue());
            manufacturerParts.add(plmManufacturerPart);
        }
        Page<PLMManufacturerPart> manufacturerParts1 = new PageImpl<PLMManufacturerPart>(manufacturerParts, pageable, manufacturerParts.size());
        return manufacturerParts1;
    }

    List<Map.Entry<Integer, Integer>> entriesSortedByValues(Map<Integer, Integer> map) {
        List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<Map.Entry<Integer, Integer>>(map.entrySet());
        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<Integer, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Integer> e1, Map.Entry<Integer, Integer> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );
        return sortedEntries;
    }

    public Page<PLMManufacturerPart> getMfrPartsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMManufacturerPartType type = manufacturerPartTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return manufacturerPartRepository.getByMfrPartTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMManufacturerPartType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMManufacturerPartType> children = manufacturerPartTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMManufacturerPartType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerPartType getByPartTypeName(String name) {
        return manufacturerPartTypeRepository.findByName(name);
    }

    public Integer getMfrPartUsedCount(Integer id, Integer partId){
        Integer count= itemManufacturerPartRepository.getMfrPartCount(partId);
        count = count + supplierPartRepository.getMfrPartCount(partId);
        return count;
    }

    public List<PLMManufacturerPartAttribute> getMfrPartUsedAttributes(Integer attributeId) {
        List<PLMManufacturerPartAttribute> partAttributes = manufacturerPartAttributeRepository.findByAttributeId(attributeId);
        return partAttributes;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','manufacturerpart')")
    public PLMManufacturerPart promoteManufacturerPart(Integer id, PLMManufacturerPart part) {
        PLMManufacturerPart plmManufacturerPart = manufacturerPartRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(part.getMfrPartType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = plmManufacturerPart.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = part.getLifeCyclePhase();
            index++;
            PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                plmManufacturerPart.setLifeCyclePhase(lifeCyclePhase);
                plmManufacturerPart = manufacturerPartRepository.save(plmManufacturerPart);
            }
        }
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartPromotedEvent(plmManufacturerPart, part.getLifeCyclePhase(), plmManufacturerPart.getLifeCyclePhase()));
        try {
            sendMfrPartSubscribeNotification(part, part.getPartNumber() + "-" + part.getLifeCyclePhase().getPhase() + "-" + plmManufacturerPart.getLifeCyclePhase().getPhase(), "lifeCyclePromoted");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return plmManufacturerPart;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','manufacturerpart')")
    public PLMManufacturerPart demoteManufacturerPart(Integer id, PLMManufacturerPart part) {
        PLMManufacturerPart plmManufacturerPart = manufacturerPartRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(plmManufacturerPart.getMfrPartType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = plmManufacturerPart.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = part.getLifeCyclePhase();
            if (oldStatus.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                Integer count = itemManufacturerPartRepository.getMfrPartCount(part.getId());
                if (count > 0) {
                    String message = messageSource.getMessage("manufacturer_part_already_in_use", null, "[{0}] part already in use", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", part.getPartNumber());
                    throw new CassiniException(result);
                }
            }
            index--;
            PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                plmManufacturerPart.setLifeCyclePhase(lifeCyclePhase);
                plmManufacturerPart = manufacturerPartRepository.save(plmManufacturerPart);
            }
        }
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartDemotedEvent(plmManufacturerPart, part.getLifeCyclePhase(), plmManufacturerPart.getLifeCyclePhase()));
        try {
            sendMfrPartSubscribeNotification(part, part.getPartNumber() + "-" + part.getLifeCyclePhase().getPhase() + "-" + plmManufacturerPart.getLifeCyclePhase().getPhase(), "lifeCycleDemoted");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return plmManufacturerPart;
    }

    @Transactional
    public List<PLMItemManufacturerPart> getMfrPartItems(Integer id, List<PLMItemManufacturerPart> itemManufacturerParts) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        for (PLMItemManufacturerPart itemManufacturerPart1 : itemManufacturerParts) {
            List<PLMItemManufacturerPart> itemManufacturerParts2 = itemManufacturerPartRepository.findByItem(itemManufacturerPart1.getId());
            for (PLMItemManufacturerPart part : itemManufacturerParts2) {
                if (part.getStatus().equals(ManufacturerPartStatus.PREFERRED)) {
                    part.setStatus(ManufacturerPartStatus.ALTERNATE);
                    itemManufacturerPartRepository.save(part);
                }
                if (part.getManufacturerPart().getId().equals(manufacturerPart.getId())) {
                    part.setStatus(ManufacturerPartStatus.PREFERRED);
                    itemManufacturerPartRepository.save(part);
                }
            }
        }
        return itemManufacturerParts;
    }

    @Transactional(readOnly = true)
    public List<ItemMfrPartsDto> getManufacturerPartItems(Integer id) {
        List<ItemMfrPartsDto> itemMfrPartsDtos = new ArrayList<>();
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByManufacturerPartAndStatus(manufacturerPart, ManufacturerPartStatus.PREFERRED);
        itemManufacturerParts.forEach(itemManufacturerPart -> {
            ItemMfrPartsDto itemMfrPartsDto = new ItemMfrPartsDto();
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemManufacturerPart.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            if (item.getLatestRevision().equals(itemRevision.getId())) {
                itemMfrPartsDto.setItemId(itemRevision.getId());
                itemMfrPartsDto.setItemName(item.getItemName());
                itemMfrPartsDto.setItemNumber(item.getItemNumber());
                itemMfrPartsDto.setRevision(itemRevision.getRevision());
                List<PLMItemManufacturerPart> itemManufacturerParts1 = itemManufacturerPartRepository.getPartWithOutPreferredItems(itemRevision.getId(), manufacturerPart.getId());
                itemMfrPartsDto.setItemManufacturerParts(itemManufacturerParts1);
                itemMfrPartsDtos.add(itemMfrPartsDto);
            }

        });
        return itemMfrPartsDtos;
    }

    @Transactional
    public List<ItemMfrPartsDto> saveItemMfrParts(Integer id, List<ItemMfrPartsDto> itemMfrPartsDtos) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        itemMfrPartsDtos.forEach(itemMfrPartsDto -> {
            if (itemMfrPartsDto.getItemManufacturerPart() != null) {
                itemMfrPartsDto.getItemManufacturerPart().setStatus(ManufacturerPartStatus.PREFERRED);
                itemManufacturerPartRepository.save(itemMfrPartsDto.getItemManufacturerPart());
            }
            itemManufacturerPartRepository.deleteByItemAndManufacturerPart(itemMfrPartsDto.getItemId(), manufacturerPart);
        });
        promoteManufacturerPart(id, manufacturerPart);
        return itemMfrPartsDtos;
    }


    @Transactional
    public PLMWorkflow attachMfrPartWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (manufacturerPart != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.MANUFACTURERPART, manufacturerPart.getId(), wfDef);
            manufacturerPart.setWorkflow(workflow.getId());
            manufacturerPartRepository.save(manufacturerPart);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewMfrPartWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (manufacturerPart != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.MANUFACTURERPART, manufacturerPart.getId(), wfDef);
            manufacturerPart.setWorkflow(workflow.getId());
            manufacturerPartRepository.save(manufacturerPart);
            applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowChangeEvent(manufacturerPart, null, workflow));
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (manufacturerPartType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, manufacturerPartType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.findOne(typeId);
        if (manufacturerPartType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (manufacturerPartType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, manufacturerPartType.getParentType(), type);
            }
        }
    }

    @Transactional
    public List<PLMItemManufacturerPart> getItemMfrParts(Integer id, PLMManufacturerPart part) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(id);
        List<PLMItemManufacturerPart> itemManufacturerParts = itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart);
        itemManufacturerParts.forEach(itemManufacturerPart -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemManufacturerPart.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            if (item.getLatestRevision().equals(itemRevision.getId())) {
                itemManufacturerPartRepository.deleteByItemAndManufacturerPart(itemManufacturerPart.getItem(), manufacturerPart);
            }
        });
        demoteManufacturerPart(id, manufacturerPart);
        return itemManufacturerParts;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturerPart> getAllMfrParts(Pageable pageable, ManufacturerPartCriteria criteria) {
        Predicate predicate = partPredicateBuilder.build(criteria, QPLMManufacturerPart.pLMManufacturerPart);
        Page<PLMManufacturerPart> parts = manufacturerPartRepository.findAll(predicate, pageable);
        parts.forEach(manufacturerPart -> {
            manufacturerPart.setMfrName(manufacturerRepository.findOne(manufacturerPart.getManufacturer()).getName());
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(manufacturerPart.getId(), PLMObjectType.MFRPARTINSPECTIONREPORT,false);
            manufacturerPart.setInspectionReportFiles(objectFileDto.getObjectFiles());
        });

        return parts;
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) throws JsonProcessingException {
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowStartedEvent(part, event.getPlmWorkflow()));
        sendMfrPartSubscribeNotification(part, event.getPlmWorkflow().getName(), "workflowStarted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) throws JsonProcessingException {
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowPromotedEvent(part, plmWorkflow, fromStatus, toStatus));
        sendMfrPartSubscribeNotification(part, plmWorkflow.getName() + "-" + fromStatus.getName() + "-" + toStatus.getName(), "workflowPromoted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) throws JsonProcessingException {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowDemotedEvent(part, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
        sendMfrPartSubscribeNotification(part, event.getPlmWorkflow().getName() + "-" + fromStatus.getName() + "-" + event.getToStatus().getName(), "workflowDemoted");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) throws JsonProcessingException {
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowFinishedEvent(part, plmWorkflow));
        sendMfrPartSubscribeNotification(part, event.getPlmWorkflow().getName(), "workflowFinished");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) throws JsonProcessingException {
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowHoldEvent(part, plmWorkflow, fromStatus));
        sendMfrPartSubscribeNotification(part, event.getPlmWorkflow().getName(), "workflowHold");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURERPART'")
    public void partWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) throws JsonProcessingException {
        PLMManufacturerPart part = (PLMManufacturerPart) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ManufacturerPartEvents.ManufacturerPartWorkflowUnholdEvent(part, plmWorkflow, fromStatus));
        sendMfrPartSubscribeNotification(part, event.getPlmWorkflow().getName(), "workflowUnHold");
    }

    @Transactional
    @PreAuthorize("hasPermission(#id ,'edit')")
    public PLMManufacturerPart uploadPartImage(Integer id, MultipartHttpServletRequest request) {
        PLMManufacturerPart part = manufacturerPartRepository.findOne(id);
        if (part != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    part.setThumbnail(file.getBytes());
                    part = manufacturerPartRepository.save(part);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return part;
    }

    public void downloadPartImage(Integer machineId, HttpServletResponse response) {
        PLMManufacturerPart part = manufacturerPartRepository.findOne(machineId);
        if (part != null) {
            InputStream is = new ByteArrayInputStream(part.getThumbnail());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<PLMManufacturerPart> getMfrPartsByPartType(Pageable pageable, ManufacturerPartCriteria partCriteria) {
        Predicate predicate = partPredicateBuilder.getPredicates(partCriteria, QPLMManufacturerPart.pLMManufacturerPart);
        return manufacturerPartRepository.findAll(predicate, pageable);
    }

    @Transactional
    public PLMSubscribe createSubscribeMfrPart(Integer partId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(person, partId);
        PLMManufacturerPart part = manufacturerPartRepository.findOne(partId);
        if (subscribe == null) {
            subscribe = new PLMSubscribe();
            subscribe.setPerson(person);
            subscribe.setObjectId(partId);
            subscribe.setObjectType(part.getObjectType().name());
            subscribe.setSubscribe(true);
            subscribe = subscribeRepository.save(subscribe);
//            applicationEventPublisher.publishEvent(new ItemEvents.ItemSubscribeEvent(itemRevision, item));
        } else {
            if (subscribe.getSubscribe()) {
                subscribe.setSubscribe(false);
                subscribe = subscribeRepository.save(subscribe);
//                applicationEventPublisher.publishEvent(new ItemEvents.ItemUnSubscribeEvent(itemRevision, item));
            } else {
                subscribe.setSubscribe(true);
                subscribe = subscribeRepository.save(subscribe);
//                applicationEventPublisher.publishEvent(new ItemEvents.ItemSubscribeEvent(itemRevision, item));
            }
        }
        return subscribe;
    }

    public void sendMfrPartSubscribeNotification(PLMManufacturerPart part, String messageJson, String type) throws JsonProcessingException {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(part.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") email = subscribe.getPerson().getEmail();
                else email = email + "," + subscribe.getPerson().getEmail();
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            SubscribeMailDto subscribeMailDto = getMessageAndSubject(part, messageJson, type);
            final String messageContent = subscribeMailDto.getMessage();
            final String finalMailSubject = subscribeMailDto.getMailSubject();
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(finalMailSubject);
                mail.setTemplatePath("email/subscribeNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    public SubscribeMailDto getMessageAndSubject(PLMManufacturerPart part, String messageJson, String type) throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        SubscribeMailDto subscribeMailDto = new SubscribeMailDto();
        List<String> names = new ArrayList<>();
        String message;
        String mailSubject;
        String arrayNames[];
        switch (type) {
            case "basic":
                messageJson = messageJson.replaceAll("\\[|\\]", "");
                ASPropertyChangeDTO changeDTO = objectMapper.readValue(messageJson, ASPropertyChangeDTO.class);
                message = person.getFullName().trim() + " has updated property " + changeDTO.getProperty() + " from " + changeDTO.getOldValue() + " to " + changeDTO.getNewValue() + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Basic Information updated : Notification";
                break;
            case "fileAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewFileDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added new file(s) " + names + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File added : Notification";
                break;
            case "fileDeleted":
                message = person.getFullName() + "has deleted file " + messageJson + " of manufacturer part : " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File deleted : Notification";
                break;
            case "fileVersioned":
                message = person.getFullName() + "has updated file(s) " + messageJson + " of manufacturer part : " + part.getPartNumber();
                final String[] s = {""};
                objectMapper.readValue(messageJson, new TypeReference<List<ASVersionedFileDTO>>() {
                }).forEach(f -> {
                    s[0] = s[0] + f.getName() + "from version " + f.getOldVersion() + " to " + f.getNewVersion();
                });
                message = message + s[0];
                mailSubject = part.getPartNumber() + " - File updated : Notification";
                break;
            case "fileRename":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has renamed from " + arrayNames[0] + " to " + arrayNames[1] + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File renamed : Notification";
                break;
            case "fileReplace":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has replace from " + arrayNames[0] + " to " + arrayNames[1] + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File replace : Notification";
                break;
            case "fileLocked":
                message = person.getFullName().trim() + " has locked file " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File locked : Notification";
                break;
            case "fileUnLocked":
                message = person.getFullName().trim() + " has unlocked file " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File unlocked : Notification";
                break;
            case "fileDownloaded":
                message = person.getFullName().trim() + " has downloaded file " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - File downloaded : Notification";
                break;
            case "folderAdded":
                message = person.getFullName().trim() + " has added folder " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Folder added : Notification";
                break;
            case "folderDeleted":
                message = person.getFullName().trim() + " has deleted folder " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Folder deleted : Notification";
                break;
            case "workflowStarted":
                message = person.getFullName().trim() + " has stated workflow " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow started : Notification";
                break;
            case "workflowPromoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has promoted workflow " + arrayNames[0] + " from " + arrayNames[1] + " to " + arrayNames[2] + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow promoted : Notification";
                break;
            case "workflowDemoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has demoted workflow " + arrayNames[0] + " from " + arrayNames[1] + " to " + arrayNames[2] + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow demoted : Notification";
                break;
            case "workflowFinished":
                message = person.getFullName().trim() + " has finished workflow " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow finished : Notification";
                break;
            case "workflowHold":
                message = person.getFullName().trim() + " has hold workflow " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow hold : Notification";
                break;
            case "workflowUnHold":
                message = person.getFullName().trim() + " has unhold workflow " + messageJson + " of manufacturer part " + part.getPartNumber();
                mailSubject = part.getPartNumber() + " - Workflow unhold : Notification";
                break;
            case "lifeCyclePromoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has promoted manufacturer part " + arrayNames[0] + " lifeCycle from " + arrayNames[1] + " to " + arrayNames[2];
                mailSubject = part.getPartNumber() + " - LifeCycle promoted : Notification";
                break;
            case "lifeCycleDemoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has demoted manufacturer part" + arrayNames[0] + " lifeCycle from " + arrayNames[1] + " to " + arrayNames[2];
                mailSubject = part.getPartNumber() + " - LifeCycle demoted : Notification";
                break;
            default:
                message = "";
                mailSubject = "";
        }
        subscribeMailDto.setMailSubject(mailSubject);
        subscribeMailDto.setMessage(message);
        return subscribeMailDto;
    }
}