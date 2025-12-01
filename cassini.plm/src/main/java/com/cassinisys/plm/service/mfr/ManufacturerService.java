package com.cassinisys.plm.service.mfr;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ManufacturerEvents;
import com.cassinisys.plm.filtering.ManufacturerCriteria;
import com.cassinisys.plm.filtering.ManufacturerPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.cm.MCOService;
import com.cassinisys.plm.service.cm.PLMVarianceService;
import com.cassinisys.plm.service.pqm.NCRService;
import com.cassinisys.plm.service.pqm.QCRService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Home on 4/25/2016.
 */
@Service
public class ManufacturerService implements CrudService<PLMManufacturer, Integer>, PageableService<PLMManufacturer, Integer> {

    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ManufacturerPredicateBuilder predicateBuilder;
    @Autowired
    private ManufacturerAttributeRepository manufacturerAttributeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private ManufacturerPartAttributeRepository manufacturerPartAttributeRepository;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
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
    private MCOService mcoService;
    @Autowired
    private NCRService ncrService;
    @Autowired
    private QCRService qcrService;
    @Autowired
    private PLMVarianceService varianceService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#manufacturer,'create')")
    public PLMManufacturer create(PLMManufacturer manufacturer) {
        checkNotNull(manufacturer);
        manufacturer.setId(null);
        Integer workflowDef = null;
        if (manufacturer.getWorkflowDefId() != null) {
            workflowDef = manufacturer.getWorkflowDefId();
        }
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(manufacturer.getMfrType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        manufacturer.setLifeCyclePhase(lifeCyclePhase);
        manufacturer = manufacturerRepository.save(manufacturer);
        if (workflowDef != null) {
            attachMfrWorkflow(manufacturer.getId(), workflowDef);
        }
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerCreatedEvent(manufacturer));
        return manufacturer;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#manufacturer.id ,'edit')")
    public PLMManufacturer update(PLMManufacturer manufacturer) {
        PLMManufacturer oldmfr = JsonUtils.cloneEntity(manufacturerRepository.findOne(manufacturer.getId()), PLMManufacturer.class);
        checkNotNull(manufacturer);
        checkNotNull(manufacturer.getId());
        manufacturer = manufacturerRepository.save(manufacturer);
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerBasicInfoUpdatedEvent(oldmfr, manufacturer));
        return manufacturer;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        PLMManufacturer manufacturer = manufacturerRepository.findOne(id);
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(manufacturer.getId());
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        if (manufacturer == null) {
            throw new ResourceNotFoundException();
        }
        manufacturerRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturer get(Integer id) {
        checkNotNull(id);
        PLMManufacturer manufacturer = manufacturerRepository.findOne(id);
        if (manufacturer == null) {
            throw new ResourceNotFoundException();
        }
       //Adding workflow relavent settings
       WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(manufacturer.getId());
       manufacturer.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
       manufacturer.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
       manufacturer.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        return manufacturer;
    }

    public List<PLMManufacturer> getMfrs(List<Integer> ids) {
        return manufacturerRepository.findByIdIn(ids);
    }

    public List<PLMManufacturerType> getMultipleMfrTypes(List<Integer> ids) {
        return manufacturerTypeRepository.findByIdIn(ids);
    }

    public List<PLMManufacturerPartType> getMultipleMfrPartTypes(List<Integer> ids) {
        return manufacturerPartTypeRepository.findByIdIn(ids);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturer> getAll() {
        return manufacturerRepository.findAll();
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturer> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return manufacturerRepository.findAll(pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturer> freeTextSearch(Pageable pageable, ManufacturerCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMManufacturer.pLMManufacturer);
        return manufacturerRepository.findAll(predicate, pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturer> find(ManufacturerCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMManufacturer.pLMManufacturer);
        return manufacturerRepository.findAll(predicate, pageable);
    }

    @Transactional
    public void saveMfrAttributes(List<PLMManufacturerAttribute> attributes) {
        for (PLMManufacturerAttribute attribute : attributes) {
            PLMManufacturerTypeAttribute mfrTypeAttr = manufacturerTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                    attribute.getStringValue() != null || attribute.getHyperLinkValue() != null || attribute.getIntegerValue() != null || attribute.getLongTextValue() != null ||
                    attribute.getRichTextValue() != null || attribute.getBooleanValue() || mfrTypeAttr.getDataType().toString().equals("FORMULA")) {
                manufacturerAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public PLMManufacturerAttribute createMfrAttributes(PLMManufacturerAttribute attributes) {
        return manufacturerAttributeRepository.save(attributes);

    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PLMManufacturerAttribute updateMfrAttribute(PLMManufacturerAttribute attribute) {
        PLMManufacturerAttribute oldValue = manufacturerAttributeRepository.findByManufacturerAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PLMManufacturerAttribute.class);
        attribute = manufacturerAttributeRepository.save(attribute);
          /* App events */
        PLMManufacturer mfr = manufacturerRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerAttributesUpdatedEvent(mfr, oldValue, attribute));
        return attribute;
    }

    public List<PLMManufacturerAttribute> getMfrAttributes(Integer mfrId) {
        return manufacturerAttributeRepository.findByIdIn(mfrId);
    }

    public PLMManufacturer getByName(String name) {
        checkNotNull(name);
        return manufacturerRepository.findByName(name);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMManufacturer> getMfrsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMManufacturerType type = manufacturerTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return manufacturerRepository.getByMfrTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMManufacturerType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMManufacturerType> children = manufacturerTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMManufacturerType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    public PLMManufacturerType getByMfrTypeName(String name) {
        return manufacturerTypeRepository.findByName(name);
    }

    public List<PLMManufacturerAttribute> getMfrUsedAttributes(Integer attributeId) {
        List<PLMManufacturerAttribute> manufacturerAttributes = manufacturerAttributeRepository.findByAttributeId(attributeId);
        return manufacturerAttributes;
    }

    @Transactional
    public PLMManufacturerAttribute saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMManufacturer manufacturer = manufacturerRepository.findOne(objectId);
        PLMManufacturerAttribute manufacturerAttribute = new PLMManufacturerAttribute();
        if (manufacturer != null) {
            setImageAndSave(manufacturerAttribute, objectId, attributeId, fileMap);
        }
        return manufacturerAttribute;
    }

    @Transactional
    public PLMManufacturerPartAttribute saveMfrPartImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMManufacturerPart manufacturer = manufacturerPartRepository.findOne(objectId);
        PLMManufacturerPartAttribute manufacturerPartAttribute = new PLMManufacturerPartAttribute();
        if (manufacturer != null) {
            setImageAndSave(manufacturerPartAttribute, objectId, attributeId, fileMap);
        }

        return manufacturerPartAttribute;
    }

    private void setImageAndSave(ObjectAttribute attribute, Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        attribute.setId(new ObjectAttributeId(objectId, attributeId));
        List<MultipartFile> files = new ArrayList<>(fileMap.values());
        dcoService.setImage(files, attribute);
    }

    @Transactional(readOnly = true)
    public DetailsCount getMfrDetails(Integer mfrId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(manufacturerFileRepository.findByManufacturerAndFileTypeAndLatestTrueOrderByModifiedDateDesc(mfrId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(mfrId));
        detailsCount.setParts(manufacturerPartRepository.findByManufacturerOrderByModifiedDateDesc(mfrId).size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public DetailsCount getMfrPartDetails(Integer mfrId) {
        PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mfrId);
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(manufacturerPartFileRepository.findByManufacturerPartAndFileTypeAndLatestTrueOrderByModifiedDateDesc(mfrId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectIdAndDocumentType(mfrId, "FILE"));
        detailsCount.setInspectionReports(mfrPartInspectionReportRepository.findByManufacturerPartAndFileTypeAndLatestTrueOrderByModifiedDateDesc(mfrId, "FILE").size());
        detailsCount.setInspectionReports(detailsCount.getInspectionReports() + objectDocumentRepository.getDocumentsCountByObjectIdAndDocumentType(mfrId, PLMObjectType.MFRPARTINSPECTIONREPORT.name()));
        detailsCount.setWhereUsed(itemManufacturerPartRepository.findByManufacturerPart(manufacturerPart).size());
        detailsCount.setChanges(mcoService.findByAffectedItem(mfrId).size() + varianceService.getVariances(mfrId).size());
        detailsCount.setQuality(qcrService.findByProblemPart(mfrId).size() + ncrService.findByProblemPart(mfrId).size());
        return detailsCount;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','manufacturer')")
    public PLMManufacturer promoteManufacturer(Integer id, PLMManufacturer manufacturer) {
        PLMManufacturer plmManufacturer = manufacturerRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(plmManufacturer.getMfrType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = plmManufacturer.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index++;
        setLifeCyclePhases(index, plmManufacturer, manufacturer, plmLifeCyclePhases);
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerPromotedEvent(manufacturer, manufacturer.getLifeCyclePhase(), plmManufacturer.getLifeCyclePhase()));
        return plmManufacturer;
    }

    public void setLifeCyclePhases(Integer index, PLMManufacturer plmManufacturer, PLMManufacturer manufacturer, List<PLMLifeCyclePhase> plmLifeCyclePhases) {
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = manufacturer.getLifeCyclePhase();
            PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                plmManufacturer.setLifeCyclePhase(lifeCyclePhase);
                plmManufacturer = manufacturerRepository.save(plmManufacturer);
            }
        }
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','manufacturer')")
    public PLMManufacturer demoteManufacturer(Integer id, PLMManufacturer manufacturer) {
        PLMManufacturer plmManufacturer = manufacturerRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(plmManufacturer.getMfrType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = plmManufacturer.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index--;
        setLifeCyclePhases(index, plmManufacturer, manufacturer, plmLifeCyclePhases);
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerDemotedEvent(manufacturer, manufacturer.getLifeCyclePhase(), plmManufacturer.getLifeCyclePhase()));
        return plmManufacturer;
    }

    @Transactional
    public PLMWorkflow attachMfrWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMManufacturer manufacturer = manufacturerRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (manufacturer != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.MANUFACTURER, manufacturer.getId(), wfDef);
            manufacturer.setWorkflow(workflow.getId());
            manufacturerRepository.save(manufacturer);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewMfrWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMManufacturer manufacturer = manufacturerRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (manufacturer != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.MANUFACTURER, manufacturer.getId(), wfDef);
            manufacturer.setWorkflow(workflow.getId());
            manufacturerRepository.save(manufacturer);
            applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowChangeEvent(manufacturer, null, workflow));
        }
        return workflow;
    }


    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (manufacturerType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, manufacturerType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.findOne(typeId);
        if (manufacturerType != null) {
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
            if (manufacturerType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, manufacturerType.getParentType(), type);
            }
        }
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowStartedEvent(mfr, event.getPlmWorkflow()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowPromotedEvent(mfr, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowDemotedEvent(mfr, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowFinishedEvent(mfr, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowHoldEvent(mfr, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'MANUFACTURER'")
    public void mfrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMManufacturer mfr = (PLMManufacturer) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ManufacturerEvents.ManufacturerWorkflowUnholdEvent(mfr, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = true)
    public Page<PLMManufacturer> getMfrsByMfrType(Pageable pageable, ManufacturerCriteria manufacturerCriteria) {
        Predicate predicate = predicateBuilder.getPredicates(manufacturerCriteria, QPLMManufacturer.pLMManufacturer);
        return manufacturerRepository.findAll(predicate, pageable);
    }
}
