package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.MBOMEvents;
import com.cassinisys.plm.filtering.MBOMCriteria;
import com.cassinisys.plm.filtering.MBOMPredicateBuilder;
import com.cassinisys.plm.model.cm.PLMMCO;
import com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.*;
import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.repo.cm.MCOProductAffectedItemRepository;
import com.cassinisys.plm.repo.cm.MCORepository;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStartRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
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
import java.util.stream.Collectors;


@Service
public class MBOMService implements CrudService<MESMBOM, Integer> {
    @Autowired
    private MESMBOMRepository mesmbomRepository;
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
    private MBOMTypeRepository mbomTypeRepository;
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
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private MBOMPredicateBuilder mbomPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private MESBOMItemRepository mesbomItemRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private PhantomRepository phantomRepository;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private WorkflowEventService workflowEventService;
    @Autowired
    private MCOProductAffectedItemRepository mcoProductAffectedItemRepository;
    @Autowired
    private MBOMRevisionStatusHistoryRepository mbomRevisionStatusHistoryRepository;
    @Autowired
    private MCORepository mcoRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private MCOService mcoService;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private BOPRevisionRepository bopRevisionRepository;
    @Autowired
    private MESBOPRepository mesbopRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesMbom,'create')")
    public MESMBOM create(MESMBOM mesMbom) {
        Integer workflowDef = null;
        Integer itemRevision = mesMbom.getItemRevision();

        if (mesMbom.getWorkflowDefId() != null) {
            workflowDef = mesMbom.getWorkflowDefId();
        }

        MESMBOM existingMbom = mesmbomRepository.findByName(mesMbom.getName());
        MESMBOM existMbomNumber = mesmbomRepository.findByNumber(mesMbom.getNumber());
        if (existMbomNumber != null) {
            String message = messageSource.getMessage("number_already_exists", null, "{0} number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existMbomNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingMbom != null) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMbom.getName());
            throw new CassiniException(result);

        }
        /*MESMBOMRevision existItemRevisionMBOM = mbomRevisionRepository.findByItemRevision(itemRevision);
        if (existItemRevisionMBOM != null) {
            PLMItem item = itemRepository.findOne(mesMbom.getItem());
            PLMItemRevision itemRevision1 = itemRevisionRepository.findOne(itemRevision);
            String message = messageSource.getMessage("item_revision_already_exist_with_mbom", null, "{0} - {1} item already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", item.getItemName(), itemRevision1.getRevision());
            throw new CassiniException(result);
        }*/
        autoNumberService.saveNextNumber(mesMbom.getType().getAutoNumberSource().getId(), mesMbom.getNumber());
        mesMbom = mesmbomRepository.save(mesMbom);

        MESMBOMRevision revision = new MESMBOMRevision();
        revision.setMaster(mesMbom.getId());
        revision.setItemRevision(itemRevision);
        MESMBOMType mesmbomType = mbomTypeRepository.findOne(mesMbom.getType().getId());
        Lov lov = mesmbomType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(mesmbomType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setLifeCyclePhase(lifeCyclePhase);
        if (lov.getValues().length == 0) {
            String message = messageSource.getMessage("no_values_found_for_lov", null, "No values found for LOV {0}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", lov.getName());
            throw new CassiniException(result);
        }
        revision.setRevision(lov.getValues()[0]);
        revision = mbomRevisionRepository.save(revision);
        MESMBOMRevisionStatusHistory statusHistory = new MESMBOMRevisionStatusHistory();
        statusHistory.setMbomRevision(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(revision.getCreatedBy());
        statusHistory = mbomRevisionStatusHistoryRepository.save(statusHistory);
        mesMbom.setLatestRevision(revision.getId());
        mesMbom = mesmbomRepository.save(mesMbom);

        if (workflowDef != null) {
            attachMBOMWorkflow(revision.getId(), workflowDef);
        }

        applicationEventPublisher.publishEvent(new MBOMEvents.MbomCreatedEvent(mesMbom, revision));
        return mesMbom;
    }


    @Transactional
    public PLMWorkflow attachMBOMWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (mesmbomRevision != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(MESEnumObject.MBOMREVISION, mesmbomRevision.getId(), wfDef);
            mesmbomRevision.setWorkflow(workflow.getId());
            mesmbomRevision = mbomRevisionRepository.save(mesmbomRevision);
//            applicationEventPublisher.publishEvent(new ItemEvents.ItemWorkflowChangeEvent(itemRevision, workflow1, workflow));
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mbom.id ,'edit')")
    public MESMBOM update(MESMBOM mbom) {
        MESMBOM oldMbom = JsonUtils.cloneEntity(mesmbomRepository.findOne(mbom.getId()), MESMBOM.class);
        MESMBOM existingMbom = mesmbomRepository.findByName(mbom.getName());
        if (existingMbom != null && !mbom.getId().equals(existingMbom.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMbom.getName());
            throw new CassiniException(result);

        }
        mbom = mesmbomRepository.save(mbom);
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbom.getLatestRevision());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(mbomRevision.getItemRevision());
        PLMItem item = itemRepository.findOne(mbom.getItem());
        mbom.setItemName(item.getItemName());
        mbom.setItemRevision(itemRevision.getId());
        mbom.setRevision(itemRevision.getRevision());
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomBasicInfoUpdatedEvent(oldMbom, mbom, mbomRevision));
        return mbom;
    }

    @Override
    @PreAuthorize("hasPermission(#mbomId,'delete')")
    public void delete(Integer mbomId) {
        mesmbomRepository.delete(mbomId);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMBOM get(Integer mbomId) {
        MESMBOM mbom = mesmbomRepository.findOne(mbomId);
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(mbom.getLatestRevision());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(mbomRevision.getItemRevision());
        PLMItem item = itemRepository.findOne(mbom.getItem());
        mbom.setItemName(item.getItemName());
        mbom.setItemRevision(itemRevision.getId());
        mbom.setRevision(itemRevision.getRevision());
        String toRevision = getNextRevisionSequence(mbom);
        Integer count = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsCountByItemAndToRevision(mbomRevision.getId(), toRevision);
        if (count == 0) {
            count = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsCountByToItemAndToRevision(mbomRevision.getId(), mbomRevision.getRevision());
        }
        if (count > 0) {
            mbom.setPendingMco(true);
        } else {
            if (mbom.getLatestReleasedRevision() != null) {
                count = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsCountByItemAndToRevision(mbom.getLatestReleasedRevision(), toRevision);
                if (count > 0) {
                    mbom.setPendingMco(true);
                }
            }
        }
        return mbom;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESMBOM> getAll() {
        return mesmbomRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MBOMDto> getAllMbomsByPageable(Pageable pageable, MBOMCriteria mbomCriteria) {
        Predicate predicate = mbomPredicateBuilder.build(mbomCriteria, QMESMBOM.mESMBOM);
        Page<MESMBOM> mboms = mesmbomRepository.findAll(predicate, pageable);
        List<MBOMDto> mbomDtos = new LinkedList<>();
        mboms.getContent().forEach(mesmbom -> {
            MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesmbom.getLatestRevision());
            PLMItem item = itemRepository.findOne(mesmbom.getItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(mesmbomRevision.getItemRevision());
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(mesmbomRevision.getId(), PLMObjectType.MBOM, false);
            MBOMDto dto = new MBOMDto();
            dto.setMbomFiles(objectFileDto.getObjectFiles());
            dto.setId(mesmbom.getId());
            dto.setItem(mesmbom.getItem());
            dto.setName(mesmbom.getName());
            dto.setNumber(mesmbom.getNumber());
            dto.setDescription(mesmbom.getDescription());
            dto.setTypeName(mesmbom.getType().getName());
            dto.setRevision(mesmbomRevision.getRevision());
            dto.setLatestRevision(mesmbomRevision.getId());
            dto.setLatestReleasedRevision(mesmbom.getLatestReleasedRevision());
            dto.setLifeCyclePhase(mesmbomRevision.getLifeCyclePhase());
            dto.setModifiedByName(personRepository.findOne(mesmbomRevision.getModifiedBy()).getFullName());
            dto.setModifiedDate(mesmbomRevision.getModifiedDate());
            dto.setCreatedByName(personRepository.findOne(mesmbomRevision.getCreatedBy()).getFullName());
            dto.setModifiedDate(mesmbomRevision.getCreatedDate());
            dto.setItemName(item.getItemName());
            dto.setItemNumber(item.getItemNumber());
            dto.setItemRevision(itemRevision.getRevision());
            dto.setItemRevisionId(itemRevision.getId());
            dto.setReleased(mesmbomRevision.getReleased());
            dto.setRejected(mesmbomRevision.getRejected());
            dto.setReleasedDate(mesmbomRevision.getReleasedDate());

            if (!Criteria.isEmpty(mbomCriteria.getMco())) {
                String toRevision = getNextRevisionSequence(mesmbom);
                dto.setToRevision(toRevision);
                List<PLMMCO> plmmcos = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsByItemAndToRevision(mesmbomRevision.getId(), toRevision);
                if (plmmcos.size() == 0) {
                    plmmcos = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsByToItemAndToRevision(mesmbomRevision.getId(), mesmbomRevision.getRevision());
                }
                if (plmmcos.size() > 0) {
                    dto.setMcoNumber(plmmcos.get(0).getMcoNumber());
                    dto.setMco(plmmcos.get(0).getId());
                    dto.setPendingMco(true);
                } else {
                    if (mesmbom.getLatestReleasedRevision() != null) {
                        plmmcos = mcoProductAffectedItemRepository.getAffectedItemAddedMCOsByItemAndToRevision(mesmbom.getLatestReleasedRevision(), toRevision);
                        if (plmmcos.size() > 0) {
                            dto.setMcoNumber(plmmcos.get(0).getMcoNumber());
                            dto.setMco(plmmcos.get(0).getId());
                            dto.setPendingMco(true);
                        }
                    }
                }

                if (mesmbomRevision.getRejected() && mesmbom.getLatestReleasedRevision() != null) {
                    MESMBOMRevision revision = mbomRevisionRepository.findOne(mesmbom.getLatestReleasedRevision());
                    dto.setRevision(revision.getRevision());
                    dto.setLifeCyclePhase(revision.getLifeCyclePhase());
                    dto.setRejected(revision.getRejected());
                    dto.setReleased(revision.getReleased());
                }
            }

            mbomDtos.add(dto);
        });
        return new PageImpl<MBOMDto>(mbomDtos, pageable, mboms.getTotalElements());

    }

    public String getNextRevisionSequence(MESMBOM item) {
        String nextRev = null;
        List<String> revs = getRevisions(item);
        String lastRev = revs.get(revs.size() - 1);
        Lov lov = item.getType().getRevisionSequence();
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

    private List<String> getRevisions(MESMBOM item) {
        List<String> revs = new ArrayList<>();
        MESMBOMRevision revisions = mbomRevisionRepository.findOne(item.getLatestRevision());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESMBOMRevision getMBOMRevision(Integer mbomId) {
        MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mbomId);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(mesmbomRevision.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    mesmbomRevision.setWorkflowStarted(true);
                }
            }
        }
        return mesmbomRevision;
    }

    @Transactional(readOnly = true)
    public List<MESMBOMRevision> getMBOMRevisionHistory(Integer mbomId) {
        List<MESMBOMRevision> revisions = mbomRevisionRepository.findByMasterOrderByCreatedDateDesc(mbomId);
        revisions.forEach(revision -> {
            List<MESMBOMRevisionStatusHistory> history = mbomRevisionStatusHistoryRepository.findByMbomRevisionOrderByTimestampDesc(revision.getId());
            List<PLMMCOProductAffectedItem> productAffectedItems = mcoProductAffectedItemRepository.findByToItem(revision.getId());
            productAffectedItems.forEach(productAffectedItem -> {
                PLMMCO mco = mcoRepository.findOne(productAffectedItem.getMco());
                revision.setMcoId(mco.getId());
                revision.setMcoNumber(mco.getMcoNumber());
                revision.setMcoTitle(mco.getTitle());
                revision.setMcoDescription(mco.getDescription());
                revision.setOldRevision(productAffectedItem.getFromRevision());
            });
            revision.setStatusHistory(history);
        });
        return revisions;
    }


    @Transactional
    public void saveMbomAttributes(List<MESObjectAttribute> attributes) {
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
    public MESMBOM saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESMBOM mbom = mesmbomRepository.findOne(objectId);
        if (mbom != null) {
            MESObjectAttribute mesObjectAttribute = new MESObjectAttribute();
            mesObjectAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, mesObjectAttribute);

        }

        return mbom;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateMbomAttribute(MESObjectAttribute attribute) {
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

        MESMBOM mbom = mesmbomRepository.findOne(attribute.getId().getObjectId());
        return attribute;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESMBOM> getMBOMsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESMBOMType type = mbomTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return mesmbomRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESMBOMType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESMBOMType> children = mbomTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESMBOMType child : children) {
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
    public MBOMItemDto createMBOMItem(Integer id, MESBOMItem mesbomItem, Boolean single) {
        if (mesbomItem.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = new MESPhantom();
            phantom.setName(mesbomItem.getPhantomName());
            phantom.setNumber(mesbomItem.getPhantomNumber());
            phantom = phantomRepository.save(phantom);
            mesbomItem.setPhantom(phantom.getId());
            mesbomItem = mesbomItemRepository.save(mesbomItem);
            AutoNumber autoNumber = autoNumberService.getByName("Default Phantom Number Source");
            autoNumberService.saveNextNumber(autoNumber.getId(), phantom.getNumber());
        } else {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            MESBOMItem existMBomItem = null;
            MESBOMItem parent = null;
            String parentName = "";

            if (mesbomItem.getParent() != null) {
                MESBOMItem parentMbomItem = mesbomItemRepository.findOne(mesbomItem.getParent());
                if (parentMbomItem.getType().equals(MESBomItemType.NORMAL)) {
                    PLMBom parentBom = bomRepository.findOne(parentMbomItem.getBomItem());
                    PLMBom itemBom = bomRepository.findOne(mesbomItem.getBomItem());

                    if (parentBom.getItem().getId().equals(itemBom.getItem().getId())) {
                        String message = messageSource.getMessage("item_already_exist_with_mbomItem", null, "[ {0} - {1} ] item already exist in {2}", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", itemBom.getItem().getItemNumber(), itemBom.getItem().getItemName(), parentBom.getItem().getItemName());
                        throw new CassiniException(result);
                    }
                }
                existMBomItem = mesbomItemRepository.findByMbomRevisionAndParentAndBomItem(mesbomItem.getMbomRevision(), mesbomItem.getParent(), mesbomItem.getBomItem());
                if (existMBomItem != null) {
                    parent = mesbomItemRepository.findOne(mesbomItem.getParent());
                    if (parent.getType().equals(MESBomItemType.PHANTOM)) {
                        MESPhantom phantom = phantomRepository.findOne(parent.getPhantom());
                        parentName = phantom.getNumber() + " - " + phantom.getName();
                    } else {
                        PLMBom plmBom = bomRepository.findOne(parent.getBomItem());
                        parentName = plmBom.getItem().getItemNumber() + " - " + plmBom.getItem().getItemName();
                    }
                }
            } else {
                existMBomItem = mesbomItemRepository.findByMbomRevisionAndBomItemAndParentIsNull(mesbomItem.getMbomRevision(), mesbomItem.getBomItem());
                if (existMBomItem != null) {
                    MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesbomItem.getMbomRevision());
                    MESMBOM mesmbom = mesmbomRepository.findOne(mesmbomRevision.getMaster());
                    PLMItem item = itemRepository.findOne(mesmbom.getItem());
                    parentName = item.getItemName();
                }
            }
            if (existMBomItem != null) {
                String message = messageSource.getMessage("item_already_exist_with_mbomItem", null, "[ {0} - {1} ] item already exist in {2}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), bom.getItem().getItemName(), parentName);
                throw new CassiniException(result);
            }

            List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndBomItem(mesbomItem.getMbomRevision(), mesbomItem.getBomItem());
            Integer consumedQty = 0;
            for (MESBOMItem item : mesbomItems) {
                consumedQty = consumedQty + item.getQuantity();
            }
            if (mesbomItem.getQuantity() > (bom.getQuantity() - consumedQty)) {
                String message = messageSource.getMessage("quantity_exceeded", null, "{0} quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), (bom.getQuantity() - consumedQty));
                throw new CassiniException(result);
            } else {
                mesbomItem = mesbomItemRepository.save(mesbomItem);
            }
            List<PLMBom> boms = bomRepository.findByParentIdOrderBySequenceAsc(bom.getAsReleasedRevision());
            for (PLMBom plmBom : boms) {
                MESBOMItem item = new MESBOMItem();
                item.setParent(mesbomItem.getId());
                item.setBomItem(plmBom.getId());
                item.setType(mesbomItem.getType());
                item.setQuantity(plmBom.getQuantity());
                item.setMbomRevision(mesbomItem.getMbomRevision());
                mesbomItems = mesbomItemRepository.findByMbomRevisionAndBomItem(item.getMbomRevision(), item.getBomItem());
                consumedQty = 0;
                for (MESBOMItem child : mesbomItems) {
                    consumedQty = consumedQty + child.getQuantity();
                }
                /*if (item.getQuantity() > (plmBom.getQuantity() - consumedQty)) {
                    String message = messageSource.getMessage("quantity_exceeded", null, "{0} quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmBom.getItem().getItemNumber(), (item.getQuantity() - consumedQty));
                    throw new CassiniException(result);
                } else {*/
                if ((plmBom.getQuantity() - consumedQty) > 0) {
                    item.setQuantity(plmBom.getQuantity() - consumedQty);
                    item = mesbomItemRepository.save(item);
                    copyBomChildrenToMBOMItem(item, plmBom);
                }
//                }
            }
            if (boms.size() == 0) {
                List<Integer> plmItemManufacturerParts = itemManufacturerPartRepository.getPartIdsByStatusAndItem(ManufacturerPartStatus.PREFERRED, bom.getAsReleasedRevision());
                if (plmItemManufacturerParts.size() == 0) {
                    plmItemManufacturerParts = itemManufacturerPartRepository.getPartIdsByStatusAndItem(ManufacturerPartStatus.ALTERNATE, bom.getAsReleasedRevision());
                }
                if (plmItemManufacturerParts.size() > 0) {
                    mesbomItem.setManufacturerPart(plmItemManufacturerParts.get(0));
                    mesbomItem = mesbomItemRepository.save(mesbomItem);
                }
            }
        }
        if (single) {
            List<MESBOMItem> mesbomItems = new ArrayList<>();
            mesbomItems.add(mesbomItem);
            MESMBOMRevision mesmbomRevision = mbomRevisionRepository.findOne(mesbomItem.getMbomRevision());
            applicationEventPublisher.publishEvent(new MBOMEvents.MbomItemsCreatedEvent(mesbomItems, mesmbomRevision));
        }
        return convertMBomItemToDto(mesbomItem);
    }

    @Transactional
    public List<MESBOMItem> createMultipleMBOMItems(Integer id, List<MESBOMItem> mesbomItems) {
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = createMBOMItem(id, mesbomItem, false);
            mesbomItem.setId(mbomItemDto.getId());
        });
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(id);
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomItemsCreatedEvent(mesbomItems, mbomRevision));
        return mesbomItems;
    }

    @Transactional
    public MESBOMItem updateMBOMItem(Integer id, MESBOMItem mesbomItem) {
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(id);
        MESBOMItem oldBomItem = JsonUtils.cloneEntity(mesbomItemRepository.findOne(mesbomItem.getId()), MESBOMItem.class);
        if (mesbomItem.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            phantom.setName(mesbomItem.getPhantomName());
            phantom = phantomRepository.save(phantom);
        } else {
            PLMBom bom = bomRepository.findOne(mesbomItem.getBomItem());
            List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndBomItem(mesbomItem.getMbomRevision(), mesbomItem.getBomItem());
            Integer consumedQty = 0;
            for (MESBOMItem item : mesbomItems) {
                if (!item.getId().equals(mesbomItem.getId())) {
                    consumedQty = consumedQty + item.getQuantity();
                }
            }
            if (mesbomItem.getQuantity() > (bom.getQuantity() - consumedQty)) {
                String message = messageSource.getMessage("quantity_exceeded", null, "{0} quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", bom.getItem().getItemNumber(), (bom.getQuantity() - consumedQty));
                throw new CassiniException(result);
            }
        }
        mesbomItem = mesbomItemRepository.save(mesbomItem);
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomItemUpdatedEvent(oldBomItem, mesbomItem, mbomRevision));
        return mesbomItem;
    }

    @Transactional
    private void copyBomChildrenToMBOMItem(MESBOMItem mesbomItem, PLMBom bom) {
        List<PLMBom> boms = bomRepository.findByParentIdOrderBySequenceAsc(bom.getAsReleasedRevision());
        for (PLMBom plmBom : boms) {
            MESBOMItem item = new MESBOMItem();
            item.setParent(mesbomItem.getId());
            item.setBomItem(plmBom.getId());
            item.setType(mesbomItem.getType());
            item.setQuantity(plmBom.getQuantity());
            item.setMbomRevision(mesbomItem.getMbomRevision());
            List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndBomItem(item.getMbomRevision(), item.getBomItem());
            Integer consumedQty = 0;
            for (MESBOMItem child : mesbomItems) {
                consumedQty = consumedQty + child.getQuantity();
            }
            /*if (item.getQuantity() > (plmBom.getQuantity() - consumedQty)) {
                String message = messageSource.getMessage("quantity_exceeded", null, "{0} quantity cannot be greater than {1}", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmBom.getItem().getItemNumber(), (item.getQuantity() - consumedQty));
                throw new CassiniException(result);
            } else {*/
            if ((plmBom.getQuantity() - consumedQty) > 0) {
                item.setQuantity(plmBom.getQuantity() - consumedQty);
                item = mesbomItemRepository.save(item);
                copyBomChildrenToMBOMItem(item, plmBom);
            }
//            }
        }

        if (boms.size() == 0) {
            List<Integer> plmItemManufacturerParts = itemManufacturerPartRepository.getPartIdsByStatusAndItem(ManufacturerPartStatus.PREFERRED, bom.getAsReleasedRevision());
            if (plmItemManufacturerParts.size() == 0) {
                plmItemManufacturerParts = itemManufacturerPartRepository.getPartIdsByStatusAndItem(ManufacturerPartStatus.ALTERNATE, bom.getAsReleasedRevision());
            }
            if (plmItemManufacturerParts.size() > 0) {
                mesbomItem.setManufacturerPart(plmItemManufacturerParts.get(0));
                mesbomItem = mesbomItemRepository.save(mesbomItem);
            }
        }
    }

    @Transactional
    public void deleteMBOMItem(Integer id, Integer itemId) {
        MESBOMItem mesbomItem = mesbomItemRepository.findOne(itemId);
        MESMBOMRevision mbomRevision = mbomRevisionRepository.findOne(id);
        mesbomItemRepository.delete(itemId);
        if (mesbomItem.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            mesbomItem.setPhantomName(phantom.getName());
            mesbomItem.setPhantomNumber(phantom.getNumber());
            phantomRepository.delete(mesbomItem.getPhantom());
        }
        applicationEventPublisher.publishEvent(new MBOMEvents.MbomItemDeletedEvent(mesbomItem, mbomRevision));
    }

    @Transactional(readOnly = true)
    public MESBOMItem getMBOMItem(Integer id, Integer itemId) {
        return mesbomItemRepository.findOne(itemId);
    }

    @Transactional(readOnly = true)
    public List<MBOMItemDto> getMBOMItems(Integer id, Boolean hierarchy) {
        List<MBOMItemDto> mbomItemDtos = new LinkedList<>();
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(id);
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDto(mesbomItem);
            mbomItemDtos.add(mbomItemDto);
            if (hierarchy) {
                mbomItemDto = visitBomItemChildren(mbomItemDto, mesbomItem);
            }
        });

        return mbomItemDtos;
    }

    private MBOMItemDto visitBomItemChildren(MBOMItemDto dto, MESBOMItem item) {

        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByParentOrderByCreatedDateAsc(item.getId());
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDto(mesbomItem);
            dto.getChildren().add(mbomItemDto);
            mbomItemDto = visitBomItemChildren(mbomItemDto, mesbomItem);
        });
        return dto;
    }

    private MBOMItemDto convertMBomItemToDto(MESBOMItem mesbomItem) {
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
            mbomItemDto.setMakeOrBuy(bom.getItem().getMakeOrBuy());
        } else if (mbomItemDto.getType().equals(MESBomItemType.PHANTOM)) {
            MESPhantom phantom = phantomRepository.findOne(mesbomItem.getPhantom());
            if (phantom != null) {
                mbomItemDto.setPhantomName(phantom.getName());
                mbomItemDto.setPhantomNumber(phantom.getNumber());
            }
        }
        Integer count = mesbomItemRepository.getChildCountByParent(mesbomItem.getId());
        if (mesbomItem.getManufacturerPart() != null) {
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(mesbomItem.getManufacturerPart());
            mbomItemDto.setMfrPartName(manufacturerPart.getPartName());
            mbomItemDto.setMfrPartNumber(manufacturerPart.getPartNumber());
            mbomItemDto.setMfrId(manufacturerPart.getManufacturer());
            mbomItemDto.setMfrPartId(manufacturerPart.getId());
        }
        if (count > 0) {
            mbomItemDto.setHasBom(true);
        }
        return mbomItemDto;
    }

    @Transactional(readOnly = true)
    public List<MBOMItemDto> getMBOMItemChildren(Integer id, Integer itemId) {
        List<MBOMItemDto> mbomItemDtos = new LinkedList<>();
        List<MESBOMItem> mesbomItems = mesbomItemRepository.findByParentOrderByCreatedDateAsc(itemId);
        mesbomItems.forEach(mesbomItem -> {
            MBOMItemDto mbomItemDto = convertMBomItemToDto(mesbomItem);
            mbomItemDtos.add(mbomItemDto);
        });

        return mbomItemDtos;
    }

    @Transactional(readOnly = true)
    public List<BomDto> getReleasedBom(Integer id, Integer itemId, Boolean hierarchy) {
        List<BomDto> bomList = new LinkedList<>();
        Map<Integer, PLMItemRevision> revisionMap = new LinkedHashMap();
        Map<Integer, List<PLMBom>> allBomItemMap = new LinkedHashMap();
        Map<Integer, List<MESBOMItem>> allmBomItemMap = new LinkedHashMap();
        Map<Integer, PLMItemManufacturerPart> preferredMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMItemManufacturerPart>> alternateMfrPartMap = new LinkedHashMap();
        List<PLMBom> plmBoms = bomRepository.findByParentIdOrderBySequenceAsc(itemId);

        List<Integer> asReleasedRevisionIds = bomRepository.getBomItemAsReleasedRevisionIds(itemId);
        List<Integer> asBomIds = bomRepository.getBomIdsAsReleasedRevisionIdsByParent(itemId);
        if (asReleasedRevisionIds.size() > 0) {
            List<Integer> revisions = new LinkedList<>();
            List<Integer> bomIds = new LinkedList<>();
            revisions.addAll(asReleasedRevisionIds);
            bomIds.addAll(asBomIds);
            if (hierarchy) {
                asReleasedRevisionIds.forEach(latestRevisionId -> {
                    collectReleasedBomChildrenIds(latestRevisionId, revisions, bomIds);
                });
                List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                List<MESBOMItem> mesbomItems = mesbomItemRepository.getBomItemsByMBOMRevisionAndBomIds(id, bomIds);
                allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                allmBomItemMap = mesbomItems.stream().collect(Collectors.groupingBy(d -> d.getBomItem()));
            } else {
                List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                List<MESBOMItem> mesbomItems = mesbomItemRepository.getBomItemsByMBOMRevisionAndBomIds(id, bomIds);
                allmBomItemMap = mesbomItems.stream().collect(Collectors.groupingBy(d -> d.getBomItem()));
            }
            List<PLMItemRevision> asReleasedRevisions = itemRevisionRepository.findByIdIn(revisions);
            revisionMap = asReleasedRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
            List<PLMItemManufacturerPart> latestRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
            List<PLMItemManufacturerPart> latestRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
            preferredMfrPartMap = latestRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
            alternateMfrPartMap = latestRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
        }
        Map<Integer, PLMItemRevision> map = revisionMap;
        Map<Integer, List<PLMBom>> allBomMap = allBomItemMap;
        Map<Integer, List<MESBOMItem>> allmBomMap = allmBomItemMap;
        Map<Integer, PLMItemManufacturerPart> preferredPartMap = preferredMfrPartMap;
        Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap = alternateMfrPartMap;

        plmBoms.forEach(bom -> {
            BomDto bomDto = convertToEBomDto(bom, map, allmBomMap, preferredPartMap, alternatePartMap);
            bomDto.setLevel(0);
            bomDto.setExpanded(true);
            if (hierarchy && bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildrenList(bomDto, map, allBomMap, allmBomMap, preferredPartMap, alternatePartMap);
            }
            List<PLMBom> children = allBomMap.containsKey(bomDto.getLatestRevision()) ? allBomMap.get(bomDto.getLatestRevision()) : new ArrayList<>();
            bomDto.setCount(children.size());
            bomList.add(bomDto);
        });
        return bomList;
    }

    private BomDto visitChildrenForItemBomChildrenList(BomDto dto, Map<Integer, PLMItemRevision> revisionMap, Map<Integer, List<PLMBom>> allBomMap, Map<Integer, List<MESBOMItem>> allmBomMap,
                                                       Map<Integer, PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap) {
        List<PLMBom> bomList = allBomMap.containsKey(dto.getLatestRevision()) ? allBomMap.get(dto.getLatestRevision()) : new ArrayList<>();
        bomList.forEach(bom -> {
            BomDto bomDto = convertToEBomDto(bom, revisionMap, allmBomMap, preferredPartMap, alternatePartMap);
            bomDto.setLevel(dto.getLevel() + 1);
            bomDto.setExpanded(true);
            dto.getChildren().add(bomDto);
            if (bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildrenList(bomDto, revisionMap, allBomMap, allmBomMap, preferredPartMap, alternatePartMap);
            }
        });
        return dto;
    }

    private void collectReleasedBomChildrenIds(Integer itemId, List<Integer> revisionIds, List<Integer> bomIds) {
        List<Integer> asReleasedIds = bomRepository.getBomItemAsReleasedRevisionIds(itemId);
        List<Integer> asBomIds = bomRepository.getBomIdsAsReleasedRevisionIdsByParent(itemId);
        revisionIds.addAll(asReleasedIds);
        bomIds.addAll(asBomIds);
        asReleasedIds.forEach(id -> {
            collectReleasedBomChildrenIds(id, revisionIds, bomIds);
        });
    }

    @Transactional(readOnly = true)
    public BomDto convertToEBomDto(PLMBom bom, Map<Integer, PLMItemRevision> revisionMap, Map<Integer, List<MESBOMItem>> mbomItemMap, Map<Integer,
            PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap) {
        PLMItemRevision itemRevision = null;
        if (revisionMap == null) {
            itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
        } else {
            itemRevision = revisionMap.get(bom.getAsReleasedRevision());
        }
        BomDto bomDto = new BomDto();
        bomDto.setId(bom.getId());
        bomDto.setItem(bom.getItem().getId());
        if (bom.getParent() != null) {
            bomDto.setParent(bom.getParent().getId());
            bomDto.setParentItemMaster(bom.getParent().getItemMaster());
            bomDto.setParentLifecyclePhaseType(bom.getParent().getLifeCyclePhase().getPhaseType());
        }
        bomDto.setQuantity(bom.getQuantity());
        bomDto.setAsReleasedRevision(bom.getAsReleasedRevision());

        bomDto.setItemNumber(bom.getItem().getItemNumber());
        bomDto.setItemName(bom.getItem().getItemName());
        bomDto.setItemTypeName(bom.getItem().getItemType().getName());
        if (itemRevision != null) {
            bomDto.setRevision(itemRevision.getRevision());
            bomDto.setHasBom(itemRevision.getHasBom());
            bomDto.setLatestRevision(itemRevision.getId());
        }
        bomDto.setMakeOrBuy(bom.getItem().getMakeOrBuy());
        List<MESBOMItem> mesbomItems = mbomItemMap.containsKey(bom.getId()) ? mbomItemMap.get(bom.getId()) : new ArrayList<>();
        Integer count = 0;
        for (MESBOMItem mesbomItem : mesbomItems) {
            count += mesbomItem.getQuantity();
        }
        PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
        if (itemManufacturerPart == null) {
            List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
            if (manufacturerParts != null && manufacturerParts.size() > 0) {
                bomDto.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                bomDto.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                bomDto.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
            }
        } else {
            bomDto.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
            bomDto.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
            bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
            bomDto.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
        }
        bomDto.setConsumedQty(count);
        return bomDto;
    }


    @Transactional(readOnly = true)
    public List<EBOMValidate> getValidatedReleasedBom(Integer id, Integer itemId, Boolean hierarchy) {
        List<EBOMValidate> validates = new LinkedList<>();
        Map<Integer, PLMItemRevision> revisionMap = new LinkedHashMap();
        Map<Integer, List<PLMBom>> allBomItemMap = new LinkedHashMap();
        Map<Integer, List<MESBOMItem>> allmBomItemMap = new LinkedHashMap();
        Map<Integer, PLMItemManufacturerPart> preferredMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMItemManufacturerPart>> alternateMfrPartMap = new LinkedHashMap();
        List<PLMBom> plmBoms = bomRepository.findByParentIdOrderBySequenceAsc(itemId);

        List<Integer> asReleasedRevisionIds = bomRepository.getBomItemAsReleasedRevisionIds(itemId);
        List<Integer> asBomIds = bomRepository.getBomIdsAsReleasedRevisionIdsByParent(itemId);
        if (asReleasedRevisionIds.size() > 0) {
            List<Integer> revisions = new LinkedList<>();
            List<Integer> bomIds = new LinkedList<>();
            revisions.addAll(asReleasedRevisionIds);
            bomIds.addAll(asBomIds);
            if (hierarchy) {
                asReleasedRevisionIds.forEach(latestRevisionId -> {
                    collectReleasedBomChildrenIds(latestRevisionId, revisions, bomIds);
                });
                List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                List<MESBOMItem> mesbomItems = mesbomItemRepository.getBomItemsByMBOMRevisionAndBomIds(id, bomIds);
                allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                allmBomItemMap = mesbomItems.stream().collect(Collectors.groupingBy(d -> d.getBomItem()));
            } else {
                List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                List<MESBOMItem> mesbomItems = mesbomItemRepository.getBomItemsByMBOMRevisionAndBomIds(id, bomIds);
                allmBomItemMap = mesbomItems.stream().collect(Collectors.groupingBy(d -> d.getBomItem()));
            }
            List<PLMItemRevision> asReleasedRevisions = itemRevisionRepository.findByIdIn(revisions);
            revisionMap = asReleasedRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
            List<PLMItemManufacturerPart> latestRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
            List<PLMItemManufacturerPart> latestRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
            preferredMfrPartMap = latestRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
            alternateMfrPartMap = latestRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
        }
        Map<Integer, PLMItemRevision> map = revisionMap;
        Map<Integer, List<PLMBom>> allBomMap = allBomItemMap;
        Map<Integer, List<MESBOMItem>> allmBomMap = allmBomItemMap;
        Map<Integer, EBOMValidate> ebomValidateMap = new HashMap<>();
        Map<Integer, PLMItemManufacturerPart> preferredPartMap = preferredMfrPartMap;
        Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap = alternateMfrPartMap;

        plmBoms.forEach(bom -> {
            bom.setPath(itemId.toString() + "/" + bom.getAsReleasedRevision());
            PLMItemRevision itemRevision = map.get(bom.getAsReleasedRevision());
            if (itemRevision != null && !itemRevision.getHasBom()) {
                List<MESBOMItem> mesbomItems = allmBomMap.containsKey(bom.getId()) ? allmBomMap.get(bom.getId()) : new ArrayList<>();
                Integer count = 0;
                for (MESBOMItem mesbomItem : mesbomItems) {
                    count += mesbomItem.getQuantity();
                }
                EBOMValidate validate = ebomValidateMap.containsKey(itemRevision.getId()) ? ebomValidateMap.get(itemRevision.getId()) : null;
                if (validate == null) {
                    EBOMValidate ebomValidate = convertToEBomValidateDto(bom, map, allmBomMap, preferredPartMap, alternatePartMap);
                    ebomValidate.setTotalQuantity(ebomValidate.getQuantity());
                    ebomValidate.setConsumedQty(count);
                    ebomValidateMap.put(itemRevision.getId(), ebomValidate);
                } else {
                    validate.setTotalQuantity(validate.getTotalQuantity() + bom.getQuantity());
                    validate.setConsumedQty(count);
                    ebomValidateMap.put(itemRevision.getId(), validate);
                }
            }
            if (hierarchy && itemRevision != null) {
                visitChildrenForItemBomChildrenReleasedList(bom, map, allBomMap, allmBomMap, ebomValidateMap, preferredPartMap, alternatePartMap);
            }
        });
        validates.addAll(ebomValidateMap.values());
        return validates;
    }

    private void visitChildrenForItemBomChildrenReleasedList(PLMBom plmBom, Map<Integer, PLMItemRevision> revisionMap, Map<Integer, List<PLMBom>> allBomMap,
                                                             Map<Integer, List<MESBOMItem>> allmBomMap, Map<Integer, EBOMValidate> ebomValidateMap, Map<Integer,
            PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap) {
        List<PLMBom> bomList = allBomMap.containsKey(plmBom.getAsReleasedRevision()) ? allBomMap.get(plmBom.getAsReleasedRevision()) : new ArrayList<>();
        bomList.forEach(bom -> {
            bom.setPath(plmBom.getPath() + "/" + bom.getAsReleasedRevision());
            PLMItemRevision itemRevision = revisionMap.get(bom.getAsReleasedRevision());
            if (itemRevision != null && !itemRevision.getHasBom()) {
                List<MESBOMItem> mesbomItems = allmBomMap.containsKey(bom.getId()) ? allmBomMap.get(bom.getId()) : new ArrayList<>();
                Integer count = 0;
                for (MESBOMItem mesbomItem : mesbomItems) {
                    PLMBom bom1 = bomRepository.findOne(mesbomItem.getBomItem());
                    if (mesbomItem.getParent() != null) {
                        MESBOMItem mesbomItem1 = mesbomItemRepository.findOne(mesbomItem.getParent());
                    }
                    count += mesbomItem.getQuantity();
                }
                EBOMValidate validate = ebomValidateMap.containsKey(itemRevision.getId()) ? ebomValidateMap.get(itemRevision.getId()) : null;
                if (validate == null) {
                    EBOMValidate ebomValidate = convertToEBomValidateDto(bom, revisionMap, allmBomMap, preferredPartMap, alternatePartMap);
                    ebomValidate.setTotalQuantity(ebomValidate.getQuantity());
                    ebomValidate.setConsumedQty(count);
                    ebomValidateMap.put(itemRevision.getId(), ebomValidate);
                } else {
                    validate.setTotalQuantity(validate.getTotalQuantity() + bom.getQuantity());
                    validate.setConsumedQty(count);
                    ebomValidateMap.put(itemRevision.getId(), validate);
                }
            }
            if (itemRevision != null) {
                visitChildrenForItemBomChildrenReleasedList(bom, revisionMap, allBomMap, allmBomMap, ebomValidateMap, preferredPartMap, alternatePartMap);
            }
        });
    }


    @Transactional(readOnly = true)
    public EBOMValidate convertToEBomValidateDto(PLMBom bom, Map<Integer, PLMItemRevision> revisionMap, Map<Integer, List<MESBOMItem>> mbomItemMap, Map<Integer,
            PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap) {
        PLMItemRevision itemRevision = null;
        if (revisionMap == null) {
            itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
        } else {
            itemRevision = revisionMap.get(bom.getAsReleasedRevision());
        }
        EBOMValidate bomDto = new EBOMValidate();
        bomDto.setId(bom.getId());
        bomDto.setQuantity(bom.getQuantity());
        bomDto.setAsReleasedRevision(bom.getAsReleasedRevision());

        bomDto.setItemNumber(bom.getItem().getItemNumber());
        bomDto.setItemName(bom.getItem().getItemName());
        bomDto.setItemTypeName(bom.getItem().getItemType().getName());
        bomDto.setDescription(bom.getItem().getDescription());
        bomDto.setMakeOrBuy(bom.getItem().getMakeOrBuy());
        if (itemRevision != null) {
            bomDto.setRevision(itemRevision.getRevision());
            bomDto.setLatestRevision(itemRevision.getId());
        }
        PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
        if (itemManufacturerPart == null) {
            List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
            if (manufacturerParts != null && manufacturerParts.size() > 0) {
                bomDto.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                bomDto.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                bomDto.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
            }
        } else {
            bomDto.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
            bomDto.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
            bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
            bomDto.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
        }
        return bomDto;
    }

    @Transactional(readOnly = true)
    public List<MBOMDto> getReleasedMboms() {
        List<MBOMDto> dtoList = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();
        itemIds = mbomRevisionRepository.getLatestReleasedMasterIds();
        if (itemIds.size() > 0) {
            List<MESMBOM> mboms = mesmbomRepository.findByIdIn(itemIds);
            mboms.forEach(plmItem -> {
                MBOMDto mbomDto = new MBOMDto();
                mbomDto.setId(plmItem.getId());
                mbomDto.setName(plmItem.getName());
                mbomDto.setNumber(plmItem.getNumber());
                List<MESMBOMRevision> mesmbomRevisions = mbomRevisionRepository.findByMasterAndReleasedTrueOrderByCreatedDateDesc(plmItem.getId());
                mesmbomRevisions.forEach(mesmbomRevision -> {
                    MBOMRevisionDto revisionDto = new MBOMRevisionDto();
                    revisionDto.setId(mesmbomRevision.getId());
                    revisionDto.setRevision(mesmbomRevision.getRevision());
                    revisionDto.setMaster(mesmbomRevision.getMaster());
                    mbomDto.getMbomRevisions().add(revisionDto);
                });
                dtoList.add(mbomDto);
            });
        }
        return dtoList;

    }

    @Transactional(readOnly = true)
    public List<BOPDto> getMBOMReleasedBOPs(Integer mbomRevision) {
        List<BOPDto> dtoList = new ArrayList<>();
        List<Integer> itemIds = new ArrayList<>();
        itemIds = bopRevisionRepository.getLatestReleasedMasterIdsByMbomRevision(mbomRevision);
        if (itemIds.size() > 0) {
            List<MESBOP> bops = mesbopRepository.findByIdIn(itemIds);
            bops.forEach(mesbop -> {
                BOPDto bopDto = new BOPDto();
                bopDto.setId(mesbop.getId());
                bopDto.setName(mesbop.getName());
                bopDto.setNumber(mesbop.getNumber());
                List<MESBOPRevision> revisions = bopRevisionRepository.findByMasterAndReleasedTrueOrderByCreatedDateDesc(mesbop.getId());
                revisions.forEach(mesmbomRevision -> {
                    BOPRevisionDto revisionDto = new BOPRevisionDto();
                    revisionDto.setId(mesmbomRevision.getId());
                    revisionDto.setRevision(mesmbomRevision.getRevision());
                    revisionDto.setMaster(mesmbomRevision.getMaster());
                    bopDto.getBopRevisions().add(revisionDto);
                });
                dtoList.add(bopDto);
            });
        }

        return dtoList;

    }

    @Transactional(readOnly = true)
    public List<BOPDto> getMBOMWhereUsed(Integer mbomId) {
        List<BOPDto> list = new ArrayList<>();
        List<MESBOPRevision> revisions = bopRevisionRepository.findByMbomRevisionOrderByIdDesc(mbomId);
        revisions.forEach(mesbopRevision -> {
            BOPDto bopDto = new BOPDto();
            MESBOP mesbop = mesbopRepository.findOne(mesbopRevision.getMaster());
            bopDto.setId(mesbop.getId());
            bopDto.setName(mesbop.getName());
            bopDto.setNumber(mesbop.getNumber());
            bopDto.setDescription(mesbop.getDescription());
            bopDto.setType(mesbop.getType().getId());
            bopDto.setTypeName(mesbop.getType().getName());
            bopDto.setLatestRevision(mesbopRevision.getId());
            bopDto.setLatestReleasedRevision(mesbop.getLatestReleasedRevision());
            bopDto.setRevision(mesbopRevision.getRevision());
            bopDto.setLifeCyclePhase(mesbopRevision.getLifeCyclePhase());
            bopDto.setMbomRevisionId(mesbopRevision.getMbomRevision());
            bopDto.setRejected(mesbopRevision.getRejected());
            bopDto.setReleased(mesbopRevision.getReleased());
            bopDto.setReleasedDate(mesbopRevision.getReleasedDate());
            bopDto.setStatus(mesbopRevision.getStatus());
            bopDto.setStatusType(mesbopRevision.getStatusType());
            bopDto.setCreatedDate(mesbop.getCreatedDate());
            bopDto.setModifiedDate(mesbop.getModifiedDate());
            bopDto.setCreatedByName(personRepository.findOne(mesbop.getCreatedBy()).getFullName());
            bopDto.setModifiedByName(personRepository.findOne(mesbop.getModifiedBy()).getFullName());
            list.add(bopDto);
        });
        return list;
    }


    public ItemDetailsDto getMBOMTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setItemFiles(mbomFileRepository.getFilesCountByMbomRevisionAndFileTypeAndLatestTrue(id, "FILE"));
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        detailsDto.setAffectedItems(mcoService.findByMCOsByMbomRevision(id).size());
        detailsDto.setWhereUsedItems(bopRevisionRepository.getBopCountByMbomRevision(id));
        return detailsDto;
    }
}
