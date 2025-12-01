package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.BOPEvents;
import com.cassinisys.plm.event.BOPOperationEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.MESObjectDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.cm.MCOService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.cassinisys.plm.service.wf.WorkflowEventService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.*;


@Service
public class BOPService implements CrudService<MESBOP, Integer> {
    @Autowired
    private MESBOPRepository mesbopRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private BOPTypeRepository bopTypeRepository;
    @Autowired
    private MESObjectTypeRepository mesObjectTypeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private MESMBOMRevisionRepository mbomRevisionRepository;
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private BOPRevisionStatusHistoryRepository bopRevisionStatusHistoryRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private MESOperationRepository mesOperationRepository;
    @Autowired
    private PhantomRepository phantomRepository;
    @Autowired
    private BOPPredicateBuilder bopPredicateBuilder;
    @Autowired
    private MESMBOMRepository mbomRepository;
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;
    @Autowired
    private BOPOperationInstructionsRepository bopOperationInstructionsRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MachineService machineService;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ToolService toolService;
    @Autowired
    private JigsFixtureService jigsFixtureService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ManpowerService manpowerService;
    @Autowired
    private OperationResourcesRepository operationResourcesRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private BOPOperationFileRepository bopOperationFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private BOPOperationPartRepository bopOperationPartRepository;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private BOPPlanFileService bopPlanFileService;
    @Autowired
    private BOPFileService bopFileService;
    @Autowired
    private MCOService mcoService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesBop,'create')")
    public MESBOP create(MESBOP mesBop) {
        Integer workflowDef = null;
        Integer mbomRevision = mesBop.getMbomRevision();
        if (mesBop.getWorkflowDefinition() != null) {
            workflowDef = mesBop.getWorkflowDefinition();
        }

        MESBOP existingBop = mesbopRepository.findByName(mesBop.getName());
        MESBOP existBopNumber = mesbopRepository.findByNumber(mesBop.getNumber());
        if (existBopNumber != null) {
            String message = messageSource.getMessage("number_already_exists", null, "{0} number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existBopNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingBop != null) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingBop.getName());
            throw new CassiniException(result);

        }
        autoNumberService.saveNextNumber(mesBop.getType().getAutoNumberSource().getId(), mesBop.getNumber());
        mesBop = mesbopRepository.save(mesBop);
        MESBOPRevision revision = new MESBOPRevision();
        revision.setMaster(mesBop.getId());
        revision.setMbomRevision(mbomRevision);
        MESBOPType mesbopType = bopTypeRepository.findOne(mesBop.getType().getId());
        Lov lov = mesbopType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(mesbopType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setLifeCyclePhase(lifeCyclePhase);
        if (lov.getValues().length == 0) {
            String message = messageSource.getMessage("no_values_found_for_lov", null, "No values found for LOV {0}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", lov.getName());
            throw new CassiniException(result);
        }
        revision.setRevision(lov.getValues()[0]);
        revision = bopRevisionRepository.save(revision);
        MESBOPRevisionStatusHistory statusHistory = new MESBOPRevisionStatusHistory();
        statusHistory.setBopRevision(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(revision.getCreatedBy());
        statusHistory = bopRevisionStatusHistoryRepository.save(statusHistory);
        mesBop.setLatestRevision(revision.getId());
        mesBop = mesbopRepository.save(mesBop);
        if (workflowDef != null) {
            attachBOPWorkflow(revision.getId(), workflowDef);
        }
        applicationEventPublisher.publishEvent(new BOPEvents.BOPCreatedEvent(mesBop, revision.getId()));
        applicationEventPublisher.publishEvent(new BOPEvents.BOPRevisionCreatedEvent(mesBop, revision.getId()));
        return mesBop;
    }


    @Transactional
    public PLMWorkflow attachBOPWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (mesbopRevision != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(MESEnumObject.BOPREVISION, mesbopRevision.getId(), wfDef);
            mesbopRevision.setWorkflow(workflow.getId());
            bopRevisionRepository.save(mesbopRevision);
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#bop.id ,'edit')")
    public MESBOP update(MESBOP bop) {
        MESBOP oldbop = JsonUtils.cloneEntity(mesbopRepository.findOne(bop.getId()), MESBOP.class);
        MESBOP existingBop = mesbopRepository.findByName(bop.getName());
        if (existingBop != null && !bop.getId().equals(existingBop.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingBop.getName());
            throw new CassiniException(result);

        }
        bop = mesbopRepository.save(bop);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPBasicInfoUpdatedEvent(oldbop, bop, bop.getLatestRevision()));
        return bop;
    }

    @Override
    @PreAuthorize("hasPermission(#bopId,'delete')")
    public void delete(Integer bopId) {
        mesbopRepository.delete(bopId);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESBOP get(Integer bopId) {
        MESBOP bop = mesbopRepository.findOne(bopId);
        return bop;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getBOPCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setItemFiles(bopFileRepository.findByBopAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setOperations(bopRouteOperationRepository.getPlanOperationCountByBop(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getBOPPlanCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setItemFiles(bopOperationFileRepository.findByBopOperationAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setItems(bopOperationPartRepository.getItemsCountByPlan(id));
        detailsDto.setResourcesCount(bopOperationResourceRepository.getBopOperationResourcesCountByPlan(id));
        return detailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESBOP> getAll() {
        return mesbopRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<BOPDto> getAllBopsByPageable(Pageable pageable, BOPCriteria bopCriteria) {
        Predicate predicate = bopPredicateBuilder.build(bopCriteria, QMESBOP.mESBOP);
        List<BOPDto> bopDtos = new LinkedList<>();
        Page<MESBOP> bops = mesbopRepository.findAll(predicate, pageable);
        bops.getContent().forEach(mesbop -> {
            BOPDto bopDto = convertBopToDto(mesbop);
            bopDtos.add(bopDto);
        });
        return new PageImpl<BOPDto>(bopDtos, pageable, bops.getTotalElements());

    }

    @Transactional(readOnly = true)
    public BOPDto convertBopToDto(MESBOP mesbop) {
        BOPDto bopDto = new BOPDto();
        bopDto.setId(mesbop.getId());
        bopDto.setName(mesbop.getName());
        bopDto.setNumber(mesbop.getNumber());
        bopDto.setDescription(mesbop.getDescription());
        bopDto.setType(mesbop.getType().getId());
        bopDto.setTypeName(mesbop.getType().getName());
        bopDto.setLatestRevision(mesbop.getLatestRevision());
        bopDto.setLatestReleasedRevision(mesbop.getLatestReleasedRevision());
        MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(mesbop.getLatestRevision());
        bopDto.setRevision(mesbopRevision.getRevision());
        bopDto.setLifeCyclePhase(mesbopRevision.getLifeCyclePhase());
        bopDto.setMbomRevisionId(mesbopRevision.getMbomRevision());
        bopDto.setRejected(mesbopRevision.getRejected());
        bopDto.setReleased(mesbopRevision.getReleased());
        bopDto.setReleasedDate(mesbopRevision.getReleasedDate());
        bopDto.setStatus(mesbopRevision.getStatus());
        bopDto.setStatusType(mesbopRevision.getStatusType());
        MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesbopRevision.getMbomRevision());
        MESMBOM mesmbom = mbomRepository.findOne(mesmbomRevision.getMaster());
        bopDto.setMbomRevisionId(mesmbomRevision.getId());
        bopDto.setMbomRevision(mesmbomRevision.getRevision());
        bopDto.setMbomName(mesmbom.getName());
        bopDto.setMbomNumber(mesmbom.getNumber());
        bopDto.setObjectType("BOPREVISION");
        bopDto.setCreatedDate(mesbop.getCreatedDate());
        bopDto.setModifiedDate(mesbop.getModifiedDate());
        bopDto.setCreatedByName(personRepository.findOne(mesbop.getCreatedBy()).getFullName());
        bopDto.setModifiedByName(personRepository.findOne(mesbop.getModifiedBy()).getFullName());
        ObjectFileDto objectFileDto = objectFileService.getObjectFiles(mesbopRevision.getId(), PLMObjectType.BOPREVISION, false);
        bopDto.setItemFiles(objectFileDto.getObjectFiles());
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(mesbopRevision.getId());
        bopDto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        bopDto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
        bopDto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        bopDto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        bopDto.setOnHold(workFlowStatusDto.getOnHold());
        bopDto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());


        return bopDto;
    }


    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESBOPRevision getBOPRevision(Integer revisionId) {
        MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(revisionId);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(mesbopRevision.getId());
        mesbopRevision.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        mesbopRevision.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
        mesbopRevision.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        mesbopRevision.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesbopRevision.getMbomRevision());
        MESMBOM mesmbom = mbomRepository.findOne(mesmbomRevision.getMaster());
        mesbopRevision.setMbomName(mesmbom.getName());
        mesbopRevision.setMbomNumber(mesmbom.getNumber());
        mesbopRevision.setMbomRevisionName(mesmbomRevision.getRevision());
        return mesbopRevision;
    }

    @Transactional(readOnly = true)
    public List<MESBOPRevision> getBOPRevisionHistory(Integer bopId) {
        List<MESBOPRevision> revisions = bopRevisionRepository.findByMasterOrderByCreatedDateDesc(bopId);
        revisions.forEach(revision -> {
            MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(revision.getMbomRevision());
            MESMBOM mesmbom = mbomRepository.findOne(mesmbomRevision.getMaster());
            revision.setMbomName(mesmbom.getName());
            revision.setMbomNumber(mesmbom.getNumber());
            revision.setMbomRevisionName(mesmbomRevision.getRevision());
            List<MESBOPRevisionStatusHistory> history = bopRevisionStatusHistoryRepository.findByBopRevisionOrderByTimestampDesc(revision.getId());
            revision.setStatusHistory(history);
        });
        return revisions;
    }


    @Transactional
    public void saveBopAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public MESBOP saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESBOP bop = mesbopRepository.findOne(objectId);
        if (bop != null) {
            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mesObjectAttribute);

        }

        return bop;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateBopAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        MESObjectTypeAttribute mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mesObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mesObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mesObjectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = mesObjectAttributeRepository.save(attribute);

        MESBOP boP = mesbopRepository.findOne(attribute.getId().getObjectId());
        return attribute;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESBOP> getBOPsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESBOPType type = bopTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesbopRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESBOPType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESBOPType> children = bopTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESBOPType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        MESObjectType mesObjectType = mesObjectTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (mesObjectType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, mesObjectType.getParentType(), type);
        }
        return workflowDefinitions;
    }

    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        MESObjectType mesObjectType = mesObjectTypeRepository.findOne(typeId);
        if (mesObjectType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workflowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (mesObjectType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, mesObjectType.getParentType(), type);
            }
        }
    }

    @Transactional
    public MESBOPRouteOperation createBopRouteItem(MESBOPRouteOperation mesBopRouteOperation) {
        if (mesBopRouteOperation.getType().equals(BOPPlanTypeEnum.PHANTOM)) {
            MESPhantom phantom = new MESPhantom();
            phantom.setName(mesBopRouteOperation.getName());
            phantom.setNumber(mesBopRouteOperation.getNumber());
            phantom.setDescription(mesBopRouteOperation.getDescription());
            phantom = phantomRepository.save(phantom);
            mesBopRouteOperation.setPhantom(phantom.getId());
            AutoNumber autoNumber = autoNumberService.getByName("Default Phantom Number Source");
            autoNumberService.saveNextNumber(autoNumber.getId(), phantom.getNumber());
        }
        mesBopRouteOperation = bopRouteOperationRepository.save(mesBopRouteOperation);
        List<MESBOPRouteOperation> routeItems = new ArrayList<>();
        routeItems.add(mesBopRouteOperation);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPRouteOperationsAddedEvent(mesBopRouteOperation.getBop(), routeItems));
        return mesBopRouteOperation;
    }

    @Transactional
    public List<MESBOPRouteOperation> createBopRouteItems(List<MESBOPRouteOperation> mesBopRouteOperations) {
        mesBopRouteOperations.forEach(plan -> {
            if (plan.getType().equals(BOPPlanTypeEnum.PHANTOM)) {
                MESPhantom phantom = new MESPhantom();
                phantom.setName(plan.getName());
                phantom.setNumber(plan.getNumber());
                phantom.setDescription(plan.getDescription());
                phantom = phantomRepository.save(phantom);
                plan.setPhantom(phantom.getId());
                AutoNumber autoNumber = autoNumberService.getByName("Default Phantom Number Source");
                autoNumberService.saveNextNumber(autoNumber.getId(), phantom.getNumber());
            }
        });
        mesBopRouteOperations = bopRouteOperationRepository.save(mesBopRouteOperations);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPRouteOperationsAddedEvent(mesBopRouteOperations.get(0).getBop(), mesBopRouteOperations));
        return mesBopRouteOperations;
    }

    @Transactional
    public MESBOPRouteOperation updateBopRouteItem(MESBOPRouteOperation mesBopRouteOperation) {
        MESBOPRouteOperation routeItem = JsonUtils.cloneEntity(bopRouteOperationRepository.findOne(mesBopRouteOperation.getId()), MESBOPRouteOperation.class);
        if (mesBopRouteOperation.getType().equals(BOPPlanTypeEnum.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesBopRouteOperation.getPhantom());
            phantom.setName(mesBopRouteOperation.getName());
            phantom.setNumber(mesBopRouteOperation.getNumber());
            phantom.setDescription(mesBopRouteOperation.getDescription());
            phantom = phantomRepository.save(phantom);
            mesBopRouteOperation.setPhantom(phantom.getId());
        }
        mesBopRouteOperation = bopRouteOperationRepository.save(mesBopRouteOperation);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPRouteItemUpdatedEvent(mesBopRouteOperation.getBop(), routeItem, mesBopRouteOperation));
        return mesBopRouteOperation;
    }

    @Transactional
    public void deleteBopRouteItem(Integer bopPlanId) {
        MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(bopPlanId);
        Integer operationCount = bopRouteOperationRepository.getPlanOperationCountByBop(plan.getBop());
        MESBOPRevision mesbopRevision = bopRevisionRepository.findOne(plan.getBop());
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(mesbopRevision.getId());
        if (workflow != null && workflow.getStart() != null) {
            PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
            if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                mesbopRevision.setWorkflowStarted(true);
            }
        }
        if (mesbopRevision.getWorkflowStarted() && operationCount == 1) {
            String message = messageSource.getMessage("atleast_one_operation_available", null, "Atleast one operation should be available", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        } else {
            applicationEventPublisher.publishEvent(new BOPEvents.BOPRouteItemDeletedEvent(plan.getBop(), plan));
            bopRouteOperationRepository.delete(bopPlanId);
            if (plan.getType().equals(BOPPlanTypeEnum.PHANTOM)) {
                phantomRepository.delete(plan.getPhantom());
            }
        }
    }

    @Transactional(readOnly = true)
    public BOPRouteDto getBopRouteItem(Integer bopPlanId) {
        MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(bopPlanId);
        return convertBOPPlanToDto(plan);
    }

    @Transactional(readOnly = true)
    public List<BOPRouteDto> getBopRouteItemChildren(Integer id) {
        List<BOPRouteDto> list = new ArrayList<>();
        List<MESBOPRouteOperation> plans = bopRouteOperationRepository.findByParentOrderByIdAsc(id);
        plans.forEach(plan -> {
            BOPRouteDto planDto = convertBOPPlanToDto(plan);
            list.add(planDto);
        });
        return list;
    }

    @Transactional(readOnly = true)
    public List<BOPRouteDto> getBopRoutes(Integer id) {
        List<BOPRouteDto> list = new ArrayList<>();
        List<MESBOPRouteOperation> plans = bopRouteOperationRepository.findByBopAndParentIsNullOrderByIdAsc(id);
        plans.forEach(plan -> {
            BOPRouteDto planDto = convertBOPPlanToDto(plan);
            planDto = visitBOPPlanChildren(planDto, plan);
            list.add(planDto);
        });
        return list;
    }

    private BOPRouteDto visitBOPPlanChildren(BOPRouteDto planDto, MESBOPRouteOperation plan) {
        List<MESBOPRouteOperation> childList = bopRouteOperationRepository.findByParentOrderByIdAsc(plan.getId());
        childList.forEach(child -> {
            BOPRouteDto bopRouteDto = convertBOPPlanToDto(child);
            bopRouteDto = visitBOPPlanChildren(bopRouteDto, child);
            planDto.getChildren().add(bopRouteDto);
        });
        return planDto;
    }

    private BOPRouteDto convertBOPPlanToDto(MESBOPRouteOperation plan) {
        BOPRouteDto planDto = new BOPRouteDto();
        planDto.setId(plan.getId());
        planDto.setBop(plan.getBop());
        planDto.setOperation(plan.getOperation());
        planDto.setPhantom(plan.getPhantom());
        planDto.setParent(plan.getParent());
        planDto.setSetupTime(plan.getSetupTime());
        planDto.setCycleTime(plan.getCycleTime());
        planDto.setSequenceNumber(plan.getSequenceNumber());
        planDto.setType(plan.getType());
        if (planDto.getType().equals(BOPPlanTypeEnum.OPERATION)) {
            MESOperation operation = mesOperationRepository.findOne(plan.getOperation());
            planDto.setName(operation.getName());
            planDto.setNumber(operation.getNumber());
            planDto.setTypeName(operation.getType().getName());
            planDto.setDescription(operation.getDescription());
        } else {
            MESPhantom phantom = phantomRepository.findOne(plan.getPhantom());
            planDto.setName(phantom.getName());
            planDto.setNumber(phantom.getNumber());
            planDto.setTypeName("Phantom");
            planDto.setDescription(phantom.getDescription());
        }
        planDto.setModifiedDate(plan.getModifiedDate());
        planDto.setCreatedDate(plan.getCreatedDate());
        planDto.setCreatedByName(personRepository.findOne(plan.getCreatedBy()).getFullName());
        planDto.setModifiedByName(personRepository.findOne(plan.getModifiedBy()).getFullName());
        planDto.setCount(bopRouteOperationRepository.getPlanCountByParent(plan.getId()));
        planDto.setResourceCount(bopOperationResourceRepository.getBopOperationResourcesCountByPlan(plan.getId()));
        planDto.setPartCount(bopOperationPartRepository.getItemsCountByPlan(plan.getId()));
        return planDto;
    }

    @Transactional(readOnly = true)
    public List<ResourceDto> getBopOperationResources(Integer planId) {
        List<ResourceDto> list = new ArrayList<>();

        Map<String, Map<Integer, List<BOPOperationResourceDto>>> resourceMap = new HashMap<>();
        Map<Integer, Integer> resourceQuantityMap = new HashMap<>();

        List<String> planResourceTypes = bopOperationResourceRepository.getUniqueTypesByBopOperation(planId);
        planResourceTypes.forEach(type -> {
            resourceMap.put(type, new HashMap<>());
        });
        List<MESBOPOperationResource> planResources = bopOperationResourceRepository.findByBopOperationOrderByIdAsc(planId);
        planResources.forEach(planResource -> {
            Map<Integer, List<BOPOperationResourceDto>> resourceTypeMap = resourceMap.get(planResource.getType());
            List<BOPOperationResourceDto> resourceDtos = resourceTypeMap.containsKey(planResource.getResourceType()) ? resourceTypeMap.get(planResource.getResourceType()) : new ArrayList<BOPOperationResourceDto>();
            resourceDtos.add(convertBopOperationResourceToDto(planResource));
            MESOperationResources operationResource = operationResourcesRepository.findByOperationAndResourceType(planResource.getOperation(), planResource.getResourceType());
            resourceQuantityMap.put(planResource.getResourceType(), operationResource.getQuantity());
            resourceTypeMap.put(planResource.getResourceType(), resourceDtos);
            resourceMap.put(planResource.getType(), resourceTypeMap);
        });

        for (String key : resourceMap.keySet()) {
            Map<Integer, List<BOPOperationResourceDto>> resourceTypeMap = resourceMap.get(key);
            ResourceDto resourceDto = new ResourceDto();
            resourceDto.setResource(key);

            for (Integer resourceType : resourceTypeMap.keySet()) {
                OperationResourceDto operationResourceDto = new OperationResourceDto();
                operationResourceDto.setResourceType(mesObjectTypeRepository.findOne(resourceType).getName());
                operationResourceDto.setQuantity(resourceQuantityMap.get(resourceType));
                operationResourceDto.getResources().addAll(resourceTypeMap.get(resourceType));
                resourceDto.setCount(resourceDto.getCount() + operationResourceDto.getResources().size());
                resourceDto.getResourceTypes().add(operationResourceDto);
            }
            list.add(resourceDto);
        }
        return list;
    }

    private BOPOperationResourceDto convertBopOperationResourceToDto(MESBOPOperationResource planResource) {
        BOPOperationResourceDto resourceDto = new BOPOperationResourceDto();
        resourceDto.setId(planResource.getId());
        resourceDto.setBopOperation(planResource.getBopOperation());
        resourceDto.setOperation(planResource.getOperation());
        resourceDto.setType(planResource.getType());
        resourceDto.setResourceType(planResource.getResourceType());
        resourceDto.setResource(planResource.getResource());
        resourceDto.setNotes(planResource.getNotes());
        resourceDto.setTypeName(mesObjectTypeRepository.findOne(planResource.getResourceType()).getName());
        MESObject mesObject = mesObjectRepository.findOne(planResource.getResource());
        resourceDto.setName(mesObject.getName());
        resourceDto.setNumber(mesObject.getNumber());
        resourceDto.setDescription(mesObject.getDescription());
        return resourceDto;
    }

    @Transactional(readOnly = true)
    public BOPOperationResourceDto getBopOperationResource(Integer planId) {
        BOPOperationResourceDto resourceDto = new BOPOperationResourceDto();

        return resourceDto;
    }

    @Transactional
    public List<MESBOPOperationResource> createBopOperationResources(Integer planId, List<MESBOPOperationResource> planResources) {
        MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(planId);
        planResources.forEach(planResource -> {
            MESOperationResources operationResource = operationResourcesRepository.findByOperationAndResourceType(plan.getOperation(), planResource.getResourceType());
            Integer consumedQty = bopOperationResourceRepository.getBopOperationResourceTypeObjectCount(plan.getId(), plan.getOperation(), planResource.getResourceType());
            if (operationResource.getQuantity() > consumedQty) {
                planResource = bopOperationResourceRepository.save(planResource);
            } else {
                MESObjectType mesObjectType = mesObjectTypeRepository.findOne(operationResource.getResourceType());
                String message = messageSource.getMessage("resource_type_qty_already_added", null, "{0} resource type quantity {1} already allocated", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", mesObjectType.getName(), operationResource.getQuantity());
                throw new CassiniException(result);
            }
        });
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationResourcesAddedEvent(planId, planResources));
        return planResources;
    }

    @Transactional
    public MESBOPOperationResource createBopOperationResource(Integer planId, MESBOPOperationResource planResource) {
        MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(planId);
        List<MESBOPOperationResource> planResources = new ArrayList<>();
        MESOperationResources operationResource = operationResourcesRepository.findByOperationAndResourceType(plan.getOperation(), planResource.getResourceType());
        Integer consumedQty = bopOperationResourceRepository.getBopOperationResourceTypeObjectCount(plan.getId(), plan.getOperation(), planResource.getResourceType());
        if (operationResource.getQuantity() > consumedQty) {
            planResource = bopOperationResourceRepository.save(planResource);
            planResources.add(planResource);
        } else {
            MESObjectType mesObjectType = mesObjectTypeRepository.findOne(operationResource.getResourceType());
            String message = messageSource.getMessage("resource_type_qty_already_added", null, "{0} resource type quantity {1} already allocated", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", mesObjectType.getName(), operationResource.getQuantity());
            throw new CassiniException(result);
        }
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationResourcesAddedEvent(planId, planResources));
        return planResource;
    }

    @Transactional
    public MESBOPOperationResource updateBopOperationResource(Integer planId, MESBOPOperationResource planResource) {
        planResource = bopOperationResourceRepository.save(planResource);
        return planResource;
    }

    @Transactional
    public void deleteBopOperationResource(Integer resourceId) {
        MESBOPOperationResource operationResource = bopOperationResourceRepository.findOne(resourceId);
        bopOperationResourceRepository.delete(resourceId);
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationResourceDeletedEvent(operationResource.getBopOperation(), operationResource));
    }

    @Transactional
    public MESBOPOperationInstructions createBopOperationInstructions(Integer planId, MESBOPOperationInstructions planInstructions) {
        MESBOPOperationInstructions instructions = bopOperationInstructionsRepository.findByBopOperation(planId);
        if (instructions != null) {
            instructions.setInstructions(planInstructions.getInstructions());
            planInstructions = bopOperationInstructionsRepository.save(instructions);
        } else {
            planInstructions = bopOperationInstructionsRepository.save(planInstructions);
        }
        return planInstructions;
    }

    @Transactional
    public MESBOPOperationInstructions updateBopOperationInstructions(Integer planId, MESBOPOperationInstructions planInstructions) {
        planInstructions = bopOperationInstructionsRepository.save(planInstructions);
        return planInstructions;
    }

    @Transactional
    public MESBOPOperationInstructions getBopOperationInstructions(Integer planId) {
        return bopOperationInstructionsRepository.findByBopOperation(planId);
    }

    @Transactional
    public void deleteBopOperationInstructions(Integer resourceId) {
        bopOperationInstructionsRepository.delete(resourceId);
    }

    @Transactional(readOnly = true)
    public MESObjectDto searchBopOperationResources(Integer planId, Pageable pageable, MESObjectCriteria criteria) {
        MESObjectDto objectDto = new MESObjectDto();

        if (criteria.getResource().equals("MACHINES")) {
            MachineCriteria machineCriteria = new MachineCriteria();
            machineCriteria.setMachineType(criteria.getType());
            machineCriteria.setBopRoute(planId);
            Page<MESMachine> machines = machineService.getAllMachinesByPageable(pageable, machineCriteria);
            objectDto.setMachines(machines);
        } else if (criteria.getResource().equals("EQUIPMENTS")) {
            EquipmentCriteria equipmentCriteria = new EquipmentCriteria();
            equipmentCriteria.setEquipmentType(criteria.getType());
            equipmentCriteria.setBopRoute(planId);
            Page<MESEquipment> equipments = equipmentService.getAllEquipmentsByPageable(pageable, equipmentCriteria);
            objectDto.setEquipments(equipments);
        } else if (criteria.getResource().equals("INSTRUMENTS")) {
            InstrumentCriteria instrumentCriteria = new InstrumentCriteria();
            instrumentCriteria.setInstrumentType(criteria.getType());
            instrumentCriteria.setBopRoute(planId);
            Page<MESInstrument> instruments = instrumentService.getAllInstrumentsByPageable(pageable, instrumentCriteria);
            objectDto.setInstruments(instruments);
        } else if (criteria.getResource().equals("TOOLS")) {
            ToolCriteria toolCriteria = new ToolCriteria();
            toolCriteria.setToolType(criteria.getType());
            toolCriteria.setBopRoute(planId);
            Page<MESTool> tools = toolService.getAllToolsByPageable(pageable, toolCriteria);
            objectDto.setTools(tools);
        } else if (criteria.getResource().equals("JIGS_FIXTURES")) {
            JigsFixtureCriteria jigsFixtureCriteria = new JigsFixtureCriteria();
            jigsFixtureCriteria.setType(criteria.getType());
            jigsFixtureCriteria.setBopRoute(planId);
            Page<JigsFixtureDto> jigs = jigsFixtureService.getAllJigsFixtures(pageable, jigsFixtureCriteria);
            objectDto.setJigsFixtures(jigs);
        } else if (criteria.getResource().equals("MATERIALS")) {
            MaterialCriteria materialCriteria = new MaterialCriteria();
            materialCriteria.setType(criteria.getType());
            materialCriteria.setBopRoute(planId);
            Page<MaterialDto> materials = materialService.getAllMaterials(pageable, materialCriteria);
            objectDto.setMaterials(materials);
        } else if (criteria.getResource().equals("MANPOWER")) {
            ManpowerCriteria manpowerCriteria = new ManpowerCriteria();
            manpowerCriteria.setType(criteria.getType());
            manpowerCriteria.setBopRoute(planId);
            Page<MESManpower> manpowers = manpowerService.getAllManpowersByPageable(pageable, manpowerCriteria);
            objectDto.setManpowers(manpowers);
        }

        return objectDto;
    }

    @Transactional(readOnly = true)
    public OperationPartDto getBopOperationItems(Integer planId) {
        OperationPartDto operationPartDto = new OperationPartDto();
        List<MESBOPOperationPart> parts = bopOperationPartRepository.findByBopOperationAndType(planId, OperationPartType.CONSUMED);
        parts.forEach(mesbopPlanItem -> {
            operationPartDto.getConsumedParts().add(convertBOPOperationPartToDto(mesbopPlanItem));
        });
        parts = bopOperationPartRepository.findByBopOperationAndType(planId, OperationPartType.PRODUCED);
        parts.forEach(mesbopPlanItem -> {
            operationPartDto.getProducedParts().add(convertBOPOperationPartToDto(mesbopPlanItem));
        });
        return operationPartDto;
    }

    @Transactional(readOnly = true)
    public List<BOPOperationPartDto> getBopOperationItemsByType(Integer planId, OperationPartType type) {
        List<BOPOperationPartDto> itemDtos = new ArrayList<>();
        List<MESBOPOperationPart> planItems = bopOperationPartRepository.findByBopOperationAndType(planId, type);
        planItems.forEach(mesbopPlanItem -> {
            itemDtos.add(convertBOPOperationPartToDto(mesbopPlanItem));
        });
        return itemDtos;
    }

    private BOPOperationPartDto convertBOPOperationPartToDto(MESBOPOperationPart planItem) {
        BOPOperationPartDto planItemDto = new BOPOperationPartDto();
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(planItem.getMbomItem());
        PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
        planItemDto.setId(planItem.getId());
        planItemDto.setBopOperation(planItem.getBopOperation());
        planItemDto.setMbomItem(planItem.getMbomItem());
        planItemDto.setQuantity(planItem.getQuantity());
        planItemDto.setNotes(planItem.getNotes());
        planItemDto.setType(planItem.getType());
        planItemDto.setItemName(bom.getItem().getItemName());
        planItemDto.setItemNumber(bom.getItem().getItemNumber());
        planItemDto.setItemTypeName(bom.getItem().getItemType().getName());
        planItemDto.setRevision(bom.getItem().getItemType().getName());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
        planItemDto.setRevision(itemRevision.getRevision());
        planItemDto.setAsReleasedRevision(itemRevision.getId());
        planItemDto.setHasBom(itemRevision.getHasBom());
        return planItemDto;
    }

    @Transactional
    public List<MESBOPOperationPart> createBopOperationItems(Integer planId, List<MESBOPOperationPart> planItems) {
        planItems.forEach(planItem -> {
            MESBOMItem mesbomItem = mesbomItemRepository.findOne(planItem.getMbomItem());
            Integer consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndType(planItem.getBopOperation(), planItem.getMbomItem(), planItem.getType());
            if (consumed == null) {
                consumed = 0;
            }
            if ((mesbomItem.getQuantity() - consumed) >= planItem.getQuantity()) {
                planItem = bopOperationPartRepository.save(planItem);
            } else {
                PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
                String message = messageSource.getMessage("quantity_exceeded", null, "{0} : quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), mesbomItem.getQuantity() - consumed);
                throw new CassiniException(result);
            }
        });
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationPartsAddedEvent(planItems.get(0).getBopOperation(), planItems));
        return planItems;
    }

    @Transactional
    public MESBOPOperationPart createBopOperationItem(Integer planId, MESBOPOperationPart planItem) {
        Integer consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndType(planItem.getBopOperation(), planItem.getMbomItem(), planItem.getType());
        List<MESBOPOperationPart> mesbopOperationParts = new ArrayList<>();
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(planItem.getMbomItem());
        if (consumed == null) {
            consumed = 0;
        }
        if ((mesbomItem.getQuantity() - consumed) >= planItem.getQuantity()) {
            planItem = bopOperationPartRepository.save(planItem);
            mesbopOperationParts.add(planItem);
            applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationPartsAddedEvent(planItem.getBopOperation(), mesbopOperationParts));
            return planItem;
        } else {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            String message = messageSource.getMessage("quantity_exceeded", null, "{0} : quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), mesbomItem.getQuantity() - consumed);
            throw new CassiniException(result);
        }
    }

    @Transactional
    public MESBOPOperationPart updateBopOperationItem(Integer planItemId, MESBOPOperationPart planItem) {
        MESBOPOperationPart oldPart = JsonUtils.cloneEntity(bopOperationPartRepository.findOne(planItem.getId()), MESBOPOperationPart.class);
        Integer consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndTypeWithoutUpdateItem(planItem.getBopOperation(), planItem.getMbomItem(), planItem.getType(), planItem.getId());
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(planItem.getMbomItem());
        if (consumed == null) {
            consumed = 0;
        }
        if ((mesbomItem.getQuantity() - consumed) >= planItem.getQuantity()) {
            planItem = bopOperationPartRepository.save(planItem);
            applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationPartUpdatedEvent(planItem.getBopOperation(), oldPart, planItem));
            return planItem;
        } else {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            String message = messageSource.getMessage("quantity_exceeded", null, "{0} : quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), mesbomItem.getQuantity() - consumed);
            throw new CassiniException(result);
        }
    }

    @Transactional
    public BOPOperationPartDto getBopOperationItem(Integer planItemId) {
        MESBOPOperationPart planItem = bopOperationPartRepository.findOne(planItemId);
        return convertBOPOperationPartToDto(planItem);
    }

    @Transactional
    public void deleteBopOperationItem(Integer planId, Integer planItemId) {
        MESBOPOperationPart operationPart = bopOperationPartRepository.findOne(planItemId);
        bopOperationPartRepository.delete(planItemId);
        applicationEventPublisher.publishEvent(new BOPOperationEvents.BOPOperationPartDeletedEvent(operationPart.getBopOperation(), operationPart));
    }

    @Transactional(readOnly = true)
    public List<MBOMItemDto> getBOPMBOMItems(Integer planId, Integer operationId, Integer mbomRevision, Boolean hierarchy) {
        List<MBOMItemDto> mbomItemDtos = new LinkedList<>();
        List<Integer> operationIds = bopRouteOperationRepository.getIdsByBopAndOperation(planId);
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(mbomRevision);
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDto(mesbomItem, operationIds, operationId);
            mbomItemDtos.add(mbomItemDto);
            if (hierarchy) {
                mbomItemDto = visitBomItemChildren(mbomItemDto, mesbomItem, operationIds, operationId);
            }
        });

        return mbomItemDtos;
    }

    private MBOMItemDto visitBomItemChildren(MBOMItemDto dto, MESBOMItem item, List<Integer> operationIds, Integer operationId) {

        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByParentOrderByCreatedDateAsc(item.getId());
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDto(mesbomItem, operationIds, operationId);
            dto.getChildren().add(mbomItemDto);
            mbomItemDto = visitBomItemChildren(mbomItemDto, mesbomItem, operationIds, operationId);
        });
        return dto;
    }

    private MBOMItemDto convertMBomItemToDto(MESBOMItem mesbomItem, List<Integer> operationIds, Integer operationId) {
        MBOMItemDto mbomItemDto = new MBOMItemDto();
        mbomItemDto.setId(mesbomItem.getId());
        mbomItemDto.setMbomRevision(mesbomItem.getMbomRevision());
        mbomItemDto.setPhantom(mesbomItem.getPhantom());
        mbomItemDto.setBomItem(mesbomItem.getBomItem());
        mbomItemDto.setParent(mesbomItem.getParent());
        mbomItemDto.setType(mesbomItem.getType());
        mbomItemDto.setQuantity(mesbomItem.getQuantity());
        mbomItemDto.setManufacturerPart(mesbomItem.getManufacturerPart());
        if (mbomItemDto.getType().equals(MESBomItemType.NORMAL)) {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
            mbomItemDto.setItemName(bom.getItem().getItemName());
            mbomItemDto.setItemNumber(bom.getItem().getItemNumber());
            mbomItemDto.setItemTypeName(bom.getItem().getItemType().getName());
            mbomItemDto.setRevision(itemRevision.getRevision());
            mbomItemDto.setAsReleasedRevision(itemRevision.getId());
            mbomItemDto.setHasBom(itemRevision.getHasBom());
            mbomItemDto.setItemRevisionHasBom(itemRevision.getHasBom());
        } else if (mbomItemDto.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            if (phantom != null) {
                mbomItemDto.setPhantomName(phantom.getName());
                mbomItemDto.setPhantomNumber(phantom.getNumber());
            }
        }
        Integer count = mesbomItemRepository.getChildCountByParent(mesbomItem.getId());
        if (count > 0) {
            mbomItemDto.setHasBom(true);
        }
        if (operationIds.size() > 0) {
            Integer consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndIdsAndType(operationIds, mesbomItem.getId(), OperationPartType.CONSUMED);
            if (consumed != null && consumed > 0) {
                mbomItemDto.setConsumedQty(consumed);
            }
            consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndIdsAndType(operationIds, mesbomItem.getId(), OperationPartType.PRODUCED);
            if (consumed != null && consumed > 0) {
                mbomItemDto.setProducedQty(consumed);
            }
        }
        if (operationId != null) {
            MESBOPOperationPart planItem = bopOperationPartRepository.findByBopOperationAndMbomItem(operationId, mesbomItem.getId());
            if (planItem != null) {
                mbomItemDto.setAlreadyExist(true);
            }
        }
        return mbomItemDto;
    }


    @Transactional(readOnly = true)
    public List<MBOMItemDto> getBOPMBOMItemsByType(Integer planId, Integer operationId, Integer mbomRevision, OperationPartType type, Boolean hierarchy) {
        List<MBOMItemDto> mbomItemDtos = new LinkedList<>();
        List<Integer> operationIds = bopRouteOperationRepository.getIdsByBopAndOperation(planId);
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(mbomRevision);
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDtoByType(mesbomItem, operationIds, operationId, type);
            mbomItemDtos.add(mbomItemDto);
            if (hierarchy) {
                mbomItemDto = visitBomItemChildrenByType(mbomItemDto, mesbomItem, operationIds, operationId, type);
            }
        });

        return mbomItemDtos;
    }

    private MBOMItemDto visitBomItemChildrenByType(MBOMItemDto dto, MESBOMItem item, List<Integer> operationIds, Integer operationId, OperationPartType type) {

        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByParentOrderByCreatedDateAsc(item.getId());
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDtoByType(mesbomItem, operationIds, operationId, type);
            dto.getChildren().add(mbomItemDto);
            mbomItemDto = visitBomItemChildrenByType(mbomItemDto, mesbomItem, operationIds, operationId, type);
        });
        return dto;
    }

    private MBOMItemDto convertMBomItemToDtoByType(MESBOMItem mesbomItem, List<Integer> operationIds, Integer operationId, OperationPartType type) {
        MBOMItemDto mbomItemDto = new MBOMItemDto();
        mbomItemDto.setId(mesbomItem.getId());
        mbomItemDto.setMbomRevision(mesbomItem.getMbomRevision());
        mbomItemDto.setPhantom(mesbomItem.getPhantom());
        mbomItemDto.setBomItem(mesbomItem.getBomItem());
        mbomItemDto.setParent(mesbomItem.getParent());
        mbomItemDto.setType(mesbomItem.getType());
        mbomItemDto.setQuantity(mesbomItem.getQuantity());
        mbomItemDto.setManufacturerPart(mesbomItem.getManufacturerPart());
        if (mbomItemDto.getType().equals(MESBomItemType.NORMAL)) {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
            mbomItemDto.setItemName(bom.getItem().getItemName());
            mbomItemDto.setItemNumber(bom.getItem().getItemNumber());
            mbomItemDto.setItemTypeName(bom.getItem().getItemType().getName());
            mbomItemDto.setRevision(itemRevision.getRevision());
            mbomItemDto.setAsReleasedRevision(itemRevision.getId());
            mbomItemDto.setHasBom(itemRevision.getHasBom());
            mbomItemDto.setItemRevisionHasBom(itemRevision.getHasBom());
        } else if (mbomItemDto.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            if (phantom != null) {
                mbomItemDto.setPhantomName(phantom.getName());
                mbomItemDto.setPhantomNumber(phantom.getNumber());
            }
        }
        Integer count = mesbomItemRepository.getChildCountByParent(mesbomItem.getId());
        if (count > 0) {
            mbomItemDto.setHasBom(true);
        }
        if (operationIds.size() > 0) {
            Integer consumed = bopOperationPartRepository.getTotalQuantityByMBomItemAndIdsByType(operationIds, mesbomItem.getId(), type);
            if (consumed != null && consumed > 0) {
                mbomItemDto.setConsumedQty(consumed);
            }
        }
        if (operationId != null) {
            MESBOPOperationPart planItem = bopOperationPartRepository.findByBopOperationAndMbomItemAndType(operationId, mesbomItem.getId(), type);
            if (planItem != null) {
                mbomItemDto.setAlreadyExist(true);
            }
        }
        return mbomItemDto;
    }


    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void mcoWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(plmWorkflow.getCurrentStatus());
        workflowEventService.workflowStart("BOPREVISION", plmWorkflow);
        workflowEventService.workflowActivityStart("BOPREVISION", plmWorkflow, workflowStatus);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowStartedEvent(mesbopRevision.getId(), plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void mcoWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        mesbopRevision.setStatus(fromStatus.getName());
        mesbopRevision.setStatusType(fromStatus.getType());
        MESBOP mesbop = mesbopRepository.findOne(mesbopRevision.getMaster());

        workflowEventService.workflowActivityFinish("BOPREVISION", plmWorkflow, fromStatus);
        if (toStatus != null) {
            workflowEventService.workflowActivityStart("BOPREVISION", plmWorkflow, toStatus);
        }
        if (fromStatus.getType() == WorkflowStatusType.RELEASED) {
            workflowEventService.workflowFinish("BOPREVISION", plmWorkflow);
        }
        if (mesbopRevision.getStatusType() == WorkflowStatusType.RELEASED) {
            checkBopResourcesAndPart(mesbopRevision);
            mesbop.setLatestReleasedRevision(mesbopRevision.getId());
            if (mesbopRevision.getRevision().equals("-")) {
                MESBOPType mesbopType = bopTypeRepository.findOne(mesbop.getType().getId());
                Lov lov = mesbopType.getRevisionSequence();
                mesbopRevision.setRevision(lov.getValues()[1]);
            }
            mesbopRevision.setReleased(true);
            mesbopRevision.setReleasedDate(new Date());
        } else if (mesbopRevision.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            mesbopRevision.setRejected(true);
            mesbopRevision.setReleasedDate(new Date());
        }
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowPromotedEvent(mesbopRevision.getId(), plmWorkflow, fromStatus, toStatus));
        mesbopRepository.save(mesbop);
        bopRevisionRepository.save(mesbopRevision);
    }

    @Transactional
    private void checkBopResourcesAndPart(MESBOPRevision mesbopRevision) {
        List<Integer> bopPlanIds = bopRouteOperationRepository.getIdsByBopAndOperation(mesbopRevision.getId());
        if (bopPlanIds.size() > 0) {
            MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesbopRevision.getMbomRevision());
            MESMBOM mesmbom = mbomRepository.findOne(mesmbomRevision.getMaster());
            Integer mbomLeafPartsCount = mcoService.getMBomRevisionTotalLeafItemsCount(mesmbomRevision, mesmbom);
            Integer bopTotalPartsCount = bopOperationPartRepository.getTotalQuantityByBopOperationIds(bopPlanIds);
            if (bopTotalPartsCount == null || bopTotalPartsCount == 0) {
                String message = messageSource.getMessage("no_mbom_parts_consumed", null, "No MBOM parts consumed for BOP", LocaleContextHolder.getLocale());
                throw new CassiniException(message);
            } else if (!mbomLeafPartsCount.equals(bopTotalPartsCount)) {
                String message = messageSource.getMessage("bop_mbom_consumed_qty_not_same", null, "BOP cannot be released, until all the MBOM parts consumed", LocaleContextHolder.getLocale());
                throw new CassiniException(message);
            }
        } else {
            String message = messageSource.getMessage("no_operation_found_for_bop", null, "No operations found for BOP", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }

        List<MESBOPRouteOperation> plans = bopRouteOperationRepository.getOperationsByBop(mesbopRevision.getId());
        if (plans.size() == 0) {
            String message = messageSource.getMessage("no_operation_found_for_bop", null, "No operations found for BOP", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        plans.forEach(plan -> {
            Integer operationQuantityCount = operationResourcesRepository.getResourcesQuantityCountByOperation(plan.getOperation());
            Integer planResourcesCount = bopOperationResourceRepository.getBopOperationResourcesCountByPlanAndOperation(plan.getId(), plan.getOperation());
            MESOperation operation = mesOperationRepository.findOne(plan.getOperation());
            if (operationQuantityCount == null || operationQuantityCount == 0) {
                String message = messageSource.getMessage("no_resources_found_for_operation", null, "No resources found for operation {0}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", operation.getNumber());
                throw new CassiniException(result);
            } else if (planResourcesCount == null || planResourcesCount == 0) {
                String message = messageSource.getMessage("no_resources_found_for_operation_instance", null, "No resources found for operation instance {0}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plan.getSequenceNumber() + " - " + operation.getNumber());
                throw new CassiniException(result);
            } else if (!operationQuantityCount.equals(planResourcesCount)) {
                String message = messageSource.getMessage("operation_instance_resources_not_consumed", null, "Operation instance {0} resources not consumed", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plan.getSequenceNumber() + " - " + operation.getNumber());
                throw new CassiniException(result);
            }
        });
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void mcoWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        mesbopRevision.setStatus(toStatus.getName());
        mesbopRevision.setStatusType(toStatus.getType());
        bopRevisionRepository.save(mesbopRevision);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowDemotedEvent(mesbopRevision.getId(), plmWorkflow, fromStatus, toStatus));
        workflowEventService.workflowActivityFinishDemote("BOPREVISION", plmWorkflow, toStatus);
        workflowEventService.workflowActivityStartDemote("BOPREVISION", plmWorkflow, fromStatus);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void mcoWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        MESBOP mesbop = mesbopRepository.findOne(mesbopRevision.getMaster());
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        mesbopRevision.setStatus(fromStatus.getName());
        mesbopRevision.setStatusType(fromStatus.getType());
        workflowEventService.workflowActivityFinish("BOPREVISION", plmWorkflow, fromStatus);
        workflowEventService.workflowFinish("BOPREVISION", plmWorkflow);
        if (mesbopRevision.getStatusType() == WorkflowStatusType.RELEASED) {
            checkBopResourcesAndPart(mesbopRevision);
            mesbop.setLatestReleasedRevision(mesbopRevision.getId());
            if (mesbopRevision.getRevision().equals("-")) {
                MESBOPType mesbopType = bopTypeRepository.findOne(mesbop.getType().getId());
                Lov lov = mesbopType.getRevisionSequence();
                mesbopRevision.setRevision(lov.getValues()[1]);
            }
            mesbopRevision.setReleased(true);
            mesbopRevision.setReleasedDate(new Date());
        } else if (mesbopRevision.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            mesbopRevision.setRejected(true);
            mesbopRevision.setReleasedDate(new Date());
        }
        mesbopRepository.save(mesbop);
        bopRevisionRepository.save(mesbopRevision);
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowFinishedEvent(mesbopRevision.getId(), plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void dcrWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowHoldEvent(mesbopRevision.getId(), plmWorkflow, fromStatus));
        workflowEventService.workflowHold("BOPREVISION", plmWorkflow);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'BOPREVISION'")
    public void dcrWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        MESBOPRevision mesbopRevision = (MESBOPRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new BOPEvents.BOPWorkflowUnholdEvent(mesbopRevision.getId(), plmWorkflow, fromStatus));
        workflowEventService.workflowUnhold("BOPREVISION", plmWorkflow);
    }


    @Transactional(readOnly = true)
    public List<BOPRouteDto> getBOPPlanValidate(Integer id) {
        List<BOPRouteDto> list = new ArrayList<>();
        List<MESBOPRouteOperation> plans = bopRouteOperationRepository.findByBopAndParentIsNullOrderByIdAsc(id);
        plans.forEach(plan -> {
            BOPRouteDto planDto = convertBOPPlanToDto(plan);
            planDto.getResources().addAll(visitBopOperationValidate(plan.getId()));
            planDto = visitBOPPlanValidateChildren(planDto, plan);
            list.add(planDto);
        });
        return list;
    }


    private BOPRouteDto visitBOPPlanValidateChildren(BOPRouteDto planDto, MESBOPRouteOperation plan) {
        List<MESBOPRouteOperation> childList = bopRouteOperationRepository.findByParentOrderByIdAsc(plan.getId());
        childList.forEach(child -> {
            BOPRouteDto bopRouteDto = convertBOPPlanToDto(child);
            bopRouteDto.getResources().addAll(visitBopOperationValidate(child.getId()));
            bopRouteDto = visitBOPPlanValidateChildren(bopRouteDto, child);
            planDto.getChildren().add(bopRouteDto);
        });
        return planDto;
    }


    public List<BOPResourceDto> visitBopOperationValidate(Integer planId) {
        List<BOPResourceDto> list = new ArrayList<>();
        Map<String, BOPResourceDto> resourceMap = new HashMap<>();

        MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(planId);
        List<String> operationResources = operationResourcesRepository.getUniqueTypesByOperation(plan.getOperation());

        operationResources.forEach(type -> {
            BOPResourceDto bopResourceDto = resourceMap.containsKey(type) ? resourceMap.get(type) : new BOPResourceDto();
            List<MESOperationResources> resourcesList = operationResourcesRepository.findByOperationAndResource(plan.getOperation(), type);
            resourcesList.forEach(resource -> {
                bopResourceDto.setQuantity(bopResourceDto.getQuantity() + resource.getQuantity());
            });
            bopResourceDto.setConsumedQty(bopOperationResourceRepository.getBopOperationTypeObjectCount(plan.getId(), plan.getOperation(), type));
            bopResourceDto.setResource(type);
            resourceMap.put(type, bopResourceDto);
        });

        list = new ArrayList<>(resourceMap.values());

        return list;
    }


    @Transactional
    public MESBOPRevision reviseBOPRevision(MESBOPRevision revision, String nextRev, Integer mbomRevision) {
        MESBOP mesbop = mesbopRepository.findOne(revision.getMaster());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(mesbop);
        }
        if (nextRev != null) {
            MESBOPRevision copy = createNextRev(revision, nextRev, mbomRevision);
            mesbop.setLatestRevision(copy.getId());
            mesbopRepository.save(mesbop);
            //Copy the related
            copyRoutes(revision, copy);
            copyBOPFiles(revision, copy);
            copyWorkflow(revision, copy);
            return copy;
        } else {
            throw new CassiniException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public String getNextRevisionSequence(MESBOP mesbop) {
        String nextRev = null;
        List<String> revs = getRevisions(mesbop);
        String lastRev = revs.get(revs.size() - 1);
        Lov lov = mesbop.getType().getRevisionSequence();
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private List<String> getRevisions(MESBOP mesbop) {
        List<String> revs = new ArrayList<>();
        MESBOPRevision revisions = bopRevisionRepository.findOne(mesbop.getLatestRevision());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }


    @PreAuthorize("hasPermission(#bop,'create')")
    public MESBOPRevision createNextRev(MESBOPRevision mesbopRevision, String nextRev, Integer mbomRevision) {
        Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(mesbopRevision.getId());
        MESBOP mesbop = mesbopRepository.findOne(mesbopRevision.getMaster());
        if (notReleasedDocumentCount > 0) {
            String message = messageSource.getMessage("mbom_has_unreleased_documents", null, "[{0}] BOP has some unreleased documents", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", mesbop.getNumber());
            throw new CassiniException(result);
        }
        MESBOPRevision copy = new MESBOPRevision();
        MESBOPType mesbopType = bopTypeRepository.findOne(mesbop.getType().getId());
        PLMLifeCyclePhase lifeCyclePhase = mesbopType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
        copy.setMaster(mesbopRevision.getMaster());
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        if (mbomRevision != null) {
            copy.setMbomRevision(mbomRevision);
        } else {
            copy.setMbomRevision(mesbopRevision.getMbomRevision());
        }
        copy.setOldRevision(mesbopRevision.getRevision());
        copy = bopRevisionRepository.save(copy);
        MESBOPRevisionStatusHistory statusHistory = new MESBOPRevisionStatusHistory();
        statusHistory.setBopRevision(copy.getId());
        statusHistory.setOldStatus(copy.getLifeCyclePhase());
        statusHistory.setNewStatus(copy.getLifeCyclePhase());
        statusHistory.setUpdatedBy(copy.getCreatedBy());
        statusHistory = bopRevisionStatusHistoryRepository.save(statusHistory);

        return copy;
    }

    private void copyRoutes(MESBOPRevision oldRevision, MESBOPRevision newRevision) {
        List<MESBOPRouteOperation> routeItems = bopRouteOperationRepository.findByBopAndParentIsNullOrderByIdAsc(oldRevision.getId());
        for (MESBOPRouteOperation routeItem : routeItems) {
            MESBOPRouteOperation mesbopRouteOperation = JsonUtils.cloneEntity(routeItem, MESBOPRouteOperation.class);
            mesbopRouteOperation.setId(null);
            mesbopRouteOperation.setBop(newRevision.getId());
            mesbopRouteOperation.setCreatedDate(new Date());
            mesbopRouteOperation.setModifiedDate(new Date());
            mesbopRouteOperation.setCreatedBy(newRevision.getCreatedBy());
            mesbopRouteOperation.setModifiedBy(newRevision.getModifiedBy());
            mesbopRouteOperation = bopRouteOperationRepository.save(mesbopRouteOperation);

            if (mesbopRouteOperation.getType().equals(BOPPlanTypeEnum.OPERATION)) {
                copyResources(routeItem.getId(), mesbopRouteOperation.getId());
                if (oldRevision.getMbomRevision().equals(newRevision.getMbomRevision())) {
                    copyParts(routeItem.getId(), mesbopRouteOperation.getId());
                }
                copyInstructions(routeItem.getId(), mesbopRouteOperation.getId());
                copyRouteFiles(routeItem, mesbopRouteOperation);
            }

            List<MESBOPRouteOperation> childRoutes = bopRouteOperationRepository.findByParentOrderByIdAsc(routeItem.getId());
            for (MESBOPRouteOperation childRoute : childRoutes) {
                MESBOPRouteOperation item = JsonUtils.cloneEntity(childRoute, MESBOPRouteOperation.class);
                item.setId(null);
                item.setParent(mesbopRouteOperation.getId());
                item.setBop(newRevision.getId());
                item.setCreatedDate(new Date());
                item.setModifiedDate(new Date());
                item.setCreatedBy(newRevision.getCreatedBy());
                item.setModifiedBy(newRevision.getModifiedBy());
                item = bopRouteOperationRepository.save(item);

                if (item.getType().equals(BOPPlanTypeEnum.OPERATION)) {
                    copyResources(childRoute.getId(), item.getId());
                    if (oldRevision.getMbomRevision().equals(newRevision.getMbomRevision())) {
                        copyParts(childRoute.getId(), item.getId());
                    }
                    copyInstructions(childRoute.getId(), item.getId());
                    copyRouteFiles(childRoute, item);
                }
            }
        }
    }

    private void copyResources(Integer oldRoute, Integer newRoute) {
        List<MESBOPOperationResource> resourceList = new ArrayList<>();
        List<MESBOPOperationResource> operationResources = bopOperationResourceRepository.findByBopOperationOrderByIdAsc(oldRoute);
        for (MESBOPOperationResource operationResource : operationResources) {
            MESBOPOperationResource resource = JsonUtils.cloneEntity(operationResource, MESBOPOperationResource.class);
            resource.setId(null);
            resource.setBopOperation(newRoute);
            resourceList.add(resource);
        }
        if (resourceList.size() > 0) {
            resourceList = bopOperationResourceRepository.save(resourceList);
        }
    }

    private void copyParts(Integer oldRoute, Integer newRoute) {
        List<MESBOPOperationPart> operationParts = new ArrayList<>();
        List<MESBOPOperationPart> parts = bopOperationPartRepository.findByBopOperation(oldRoute);
        for (MESBOPOperationPart part : parts) {
            MESBOPOperationPart operationPart = JsonUtils.cloneEntity(part, MESBOPOperationPart.class);
            operationPart.setId(null);
            operationPart.setBopOperation(newRoute);
            operationParts.add(operationPart);
        }
        if (operationParts.size() > 0) {
            bopOperationPartRepository.save(operationParts);
        }
    }

    private void copyInstructions(Integer oldRoute, Integer newRoute) {
        MESBOPOperationInstructions operationInstructions = bopOperationInstructionsRepository.findByBopOperation(oldRoute);
        if (operationInstructions != null) {
            MESBOPOperationInstructions instructions = new MESBOPOperationInstructions();
            instructions.setBopOperation(newRoute);
            instructions.setInstructions(operationInstructions.getInstructions());
            instructions = bopOperationInstructionsRepository.save(instructions);
        }
    }

    private void copyRouteFiles(MESBOPRouteOperation oldRoute, MESBOPRouteOperation newRoute) {
        List<MESBOPOperationFile> operationFiles = bopOperationFileRepository.findByBopOperationAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(oldRoute.getId());
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObject(oldRoute.getId());
        List<PLMObjectDocument> documents = new ArrayList<>();
        for (PLMObjectDocument plmObjectDocument : objectDocuments) {
            PLMObjectDocument objectDocument = new PLMObjectDocument();
            objectDocument.setDocument(plmObjectDocument.getDocument());
            objectDocument.setObject(newRoute.getId());
            objectDocument.setFolder(plmObjectDocument.getFolder());
            objectDocument.setDocumentType(plmObjectDocument.getDocumentType());
            documents.add(objectDocument);
        }
        if (documents.size() > 0) {
            objectDocumentRepository.save(documents);
        }
        for (MESBOPOperationFile operationFile : operationFiles) {
            bopPlanFileService.copyBOPPlanFile(oldRoute, newRoute, operationFile);
        }
    }

    private void copyBOPFiles(MESBOPRevision oldRevision, MESBOPRevision newRevision) {
        List<MESBOPFile> mesbopFiles = bopFileRepository.findByBopAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(oldRevision.getId());
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByObject(oldRevision.getId());
        List<PLMObjectDocument> documents = new ArrayList<>();
        for (PLMObjectDocument plmObjectDocument : objectDocuments) {
            PLMObjectDocument objectDocument = new PLMObjectDocument();
            objectDocument.setDocument(plmObjectDocument.getDocument());
            objectDocument.setObject(newRevision.getId());
            objectDocument.setFolder(plmObjectDocument.getFolder());
            objectDocument.setDocumentType(plmObjectDocument.getDocumentType());
            documents.add(objectDocument);
        }
        if (documents.size() > 0) {
            objectDocumentRepository.save(documents);
        }
        for (MESBOPFile mesbopFile : mesbopFiles) {
            bopFileService.copyMBOMFile(oldRevision, newRevision, mesbopFile);
        }
    }

    private void copyWorkflow(MESBOPRevision oldRevision, MESBOPRevision newRevision) {
        PLMWorkflow workflow = null;
        if (oldRevision.getWorkflow() != null) {
            PLMWorkflow workflow1 = workflowRepository.findOne(oldRevision.getWorkflow());
            if (workflow1 != null) {
                PLMWorkflowDefinition workflowDefinition = workflowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef1 != null) {
                    workflow = workflowService.attachWorkflow(PLMObjectType.BOPREVISION, newRevision.getId(), wfDef1);
                    newRevision.setWorkflow(workflow.getId());
                    bopRevisionRepository.save(newRevision);
                }
            }
        }
    }
}
