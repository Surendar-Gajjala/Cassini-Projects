package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.InspectionEvents;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.filtering.ItemInspectionPredicateBuilder;
import com.cassinisys.plm.filtering.MaterialInspectionPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.InspectionsDto;
import com.cassinisys.plm.model.pqm.dto.PRItemsDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@Service
public class InspectionService implements CrudService<PQMInspection, Integer> {

    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private ItemInspectionPredicateBuilder itemInspectionPredicateBuilder;
    @Autowired
    private MaterialInspectionPredicateBuilder materialInspectionPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private InspectionPlanChecklistRepository inspectionPlanChecklistRepository;
    @Autowired
    private InspectionPlanChecklistParameterRepository inspectionPlanChecklistParameterRepository;
    @Autowired
    private InspectionChecklistRepository inspectionChecklistRepository;
    @Autowired
    private InspectionAttributeRepository inspectionAttributeRepository;
    @Autowired
    private InspectionFileRepository inspectionFileRepository;
    @Autowired
    private ParamActualValueRepository paramActualValueRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private ItemInspectionRelatedItemRepository itemInspectionRelatedItemRepository;
    @Autowired
    private MaterialInspectionRelatedItemRepository materialInspectionRelatedItemRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowStatusRepository workflowStatusRepository;
    @Autowired
    private ItemInspectionRepository itemInspectionRepository;
    @Autowired
    private MaterialInspectionRepository materialInspectionRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#inspection,'create')")
    public PQMInspection create(PQMInspection inspection) {

        Integer workflowId = inspection.getWorkflow();
        inspection.setWorkflow(null);
        PQMInspection existPlanNumber = inspectionRepository.findByInspectionNumber(inspection.getInspectionNumber());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(inspection.getInspectionNumber() + " : " + "number_already_exists", null, "Number already exist", LocaleContextHolder.getLocale()));
        }
        inspection.setStatus("NONE");
        inspection = inspectionRepository.save(inspection);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.INSPECTION, inspection.getId(), wfDef);
                inspection.setWorkflow(workflow.getId());
                inspection = inspectionRepository.save(inspection);
            }
        }

        List<PQMInspectionPlanChecklist> planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(inspection.getInspectionPlan());

        for (PQMInspectionPlanChecklist planChecklist : planChecklists) {
            PQMInspectionChecklist inspectionChecklist = new PQMInspectionChecklist();
            inspectionChecklist.setPlanChecklist(planChecklist);
            inspectionChecklist.setInspection(inspection.getId());
            inspectionChecklist = inspectionChecklistRepository.save(inspectionChecklist);

            List<PQMInspectionPlanChecklist> childrens = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getId());
            for (PQMInspectionPlanChecklist children : childrens) {
                PQMInspectionChecklist checklist = new PQMInspectionChecklist();
                checklist.setPlanChecklist(children);
                checklist.setInspection(inspection.getId());
                checklist = inspectionChecklistRepository.save(checklist);

                List<PQMInspectionPlanChecklistParameter> checklistParameters = inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(children.getId());

                for (PQMInspectionPlanChecklistParameter checklistParameter : checklistParameters) {
                    PQMParamActualValue paramActualValue = new PQMParamActualValue();
                    paramActualValue.setChecklist(checklist.getId());
                    paramActualValue.setParam(checklistParameter);

                    paramActualValue = paramActualValueRepository.save(paramActualValue);
                }

            }
        }

        return inspection;
    }

    @Transactional
    @PreAuthorize("hasPermission(#itemInspection,'create')")
    public PQMItemInspection createItemInspection(PQMItemInspection itemInspection) {
        Integer workflowId = itemInspection.getWorkflow();
        itemInspection.setWorkflow(null);
        PQMItemInspection existPlanNumber = itemInspectionRepository.findByInspectionNumber(itemInspection.getInspectionNumber());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(itemInspection.getInspectionNumber() + " : " + "number_already_exists", null, "Number already exist", LocaleContextHolder.getLocale()));
        }
        itemInspection = itemInspectionRepository.save(itemInspection);
        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.ITEMINSPECTION, itemInspection.getId(), wfDef);
                itemInspection.setWorkflow(workflow.getId());
                itemInspection = itemInspectionRepository.save(itemInspection);
            }
        }
        PQMInspection inspection = inspectionRepository.findOne(itemInspection.getId());
        copyInspectionPlanChecklists(inspection);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionCreatedEvent(inspection));
        return itemInspection;
    }

    @Transactional
    @PreAuthorize("hasPermission(#materialInspection,'create')")
    public PQMMaterialInspection createMaterialInspection(PQMMaterialInspection materialInspection) {
        Integer workflowId = materialInspection.getWorkflow();
        materialInspection.setWorkflow(null);
        PQMMaterialInspection existPlanNumber = materialInspectionRepository.findByInspectionNumber(materialInspection.getInspectionNumber());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(materialInspection.getInspectionNumber() + " : " + "number_already_exists", null, "Number already exist", LocaleContextHolder.getLocale()));
        }
        materialInspection = materialInspectionRepository.save(materialInspection);
        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.MATERIALINSPECTION, materialInspection.getId(), wfDef);
                materialInspection.setWorkflow(workflow.getId());
                materialInspection = materialInspectionRepository.save(materialInspection);
            }
        }
        PQMInspection inspection = inspectionRepository.findOne(materialInspection.getId());
        copyInspectionPlanChecklists(inspection);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionCreatedEvent(inspection));
        return materialInspection;
    }

    private void copyInspectionPlanChecklists(PQMInspection inspection) {
        List<PQMInspectionPlanChecklist> planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(inspection.getInspectionPlan());
        for (PQMInspectionPlanChecklist planChecklist : planChecklists) {
            PQMInspectionChecklist inspectionChecklist = new PQMInspectionChecklist();
            inspectionChecklist.setPlanChecklist(planChecklist);
            inspectionChecklist.setInspection(inspection.getId());
            inspectionChecklist = inspectionChecklistRepository.save(inspectionChecklist);
            List<PQMInspectionPlanChecklist> childrens = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getId());
            for (PQMInspectionPlanChecklist children : childrens) {
                PQMInspectionChecklist checklist = new PQMInspectionChecklist();
                checklist.setPlanChecklist(children);
                checklist.setInspection(inspection.getId());
                checklist = inspectionChecklistRepository.save(checklist);

                List<PQMInspectionPlanChecklistParameter> checklistParameters = inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(children.getId());

                for (PQMInspectionPlanChecklistParameter checklistParameter : checklistParameters) {
                    PQMParamActualValue paramActualValue = new PQMParamActualValue();
                    paramActualValue.setChecklist(checklist.getId());
                    paramActualValue.setParam(checklistParameter);

                    paramActualValue = paramActualValueRepository.save(paramActualValue);
                }
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#inspection.id,'edit')")
    public PQMInspection update(PQMInspection inspection) {
        return inspectionRepository.save(inspection);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        inspectionRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMInspection get(Integer id) {
        PQMInspection inspection = inspectionRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
        inspection.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        inspection.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        inspection.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return inspection;
    }

    @Transactional(readOnly = true)
    public InspectionsDto getInspectionDetails(Integer id) {
        PQMInspection pqmInspection = inspectionRepository.findOne(id);
        InspectionsDto dto = new InspectionsDto();
        if (pqmInspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            PQMItemInspection inspection = itemInspectionRepository.findOne(id);
            dto.setId(inspection.getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setInspectionPlan(productInspectionPlan.getName());
            if (inspection.getItem() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(inspection.getItem());
                dto.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            }
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            dto.setDeviationSummary(inspection.getDeviationSummary());
            dto.setAssignedTo(personRepository.findOne(inspection.getAssignedTo()).getFullName());
            dto.setCreatedBy(personRepository.findOne(inspection.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(inspection.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspection.getModifiedDate());
            dto.setCreatedDate(inspection.getCreatedDate());
            dto.setNotes(inspection.getNotes());
            dto.setReleased(inspection.getReleased());
            dto.setObjectType(inspection.getObjectType().toString());
            dto.setWorkflow(inspection.getWorkflow());
            //Adding workflow relavent settings
            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        } else {
            PQMMaterialInspection inspection = materialInspectionRepository.findOne(id);
            dto.setId(inspection.getId());

            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setInspectionPlan(materialInspectionPlan.getName());
            if (inspection.getMaterial() != null) {
                dto.setMaterialName(inspection.getMaterial().getPartName());
            }

            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            dto.setDeviationSummary(inspection.getDeviationSummary());
            dto.setAssignedTo(personRepository.findOne(inspection.getAssignedTo()).getFullName());
            dto.setCreatedBy(personRepository.findOne(inspection.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(inspection.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspection.getModifiedDate());
            dto.setCreatedDate(inspection.getCreatedDate());
            dto.setNotes(inspection.getNotes());
            dto.setReleased(inspection.getReleased());
            dto.setObjectType(inspection.getObjectType().toString());
            dto.setWorkflow(inspection.getWorkflow());
           //Adding workflow relavent settings
           WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
           dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
           dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
           dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        }
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspection> getAll() {
        return inspectionRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspection> findMultiple(List<Integer> ids) {
        return inspectionRepository.findByIdIn(ids);
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspection.id ,'edit')")
    public PQMItemInspection updateItemInspection(PQMItemInspection inspection) {
        PQMInspection oldInspection = JsonUtils.cloneEntity(inspectionRepository.findOne(inspection.getId()), PQMInspection.class);
        inspection = itemInspectionRepository.save(inspection);
        PQMInspection pqmInspection = inspectionRepository.findOne(inspection.getId());
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionBasicInfoUpdatedEvent(oldInspection, pqmInspection));
        return inspection;
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteItemInspection(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        itemInspectionRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMItemInspection getItemInspection(Integer id) {
        PQMItemInspection inspection = itemInspectionRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
        inspection.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        inspection.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        inspection.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return inspection;
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<InspectionsDto> getAllItemInspections(Pageable pageable, InspectionPlanCriteria criteria) {
        Predicate predicate = itemInspectionPredicateBuilder.build(criteria, QPQMItemInspection.pQMItemInspection);
        Page<PQMItemInspection> inspections = itemInspectionRepository.findAll(predicate, pageable);
        List<InspectionsDto> inspectionsDtos = new LinkedList<>();
        inspections.getContent().forEach(inspection -> {
            InspectionsDto dto = new InspectionsDto();
            dto.setId(inspection.getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setInspectionPlan(productInspectionPlan.getName());
            if (inspection.getItem() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(inspection.getItem());
                dto.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            }
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            dto.setDeviationSummary(inspection.getDeviationSummary());
            dto.setAssignedTo(personRepository.findOne(inspection.getAssignedTo()).getFullName());
            dto.setCreatedBy(personRepository.findOne(inspection.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(inspection.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspection.getModifiedDate());
            dto.setCreatedDate(inspection.getCreatedDate());
            dto.setNotes(inspection.getNotes());
            dto.setReleased(inspection.getReleased());
            dto.setObjectType(inspection.getObjectType().toString());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
            dto.setTagsCount(inspection.getTags().size());
            inspectionsDtos.add(dto);
        });

        return new PageImpl<InspectionsDto>(inspectionsDtos, pageable, inspections.getTotalElements());
    }

    @Transactional
    @PreAuthorize("hasPermission(#inspection.id ,'edit')")
    public PQMMaterialInspection updateMaterialInspection(PQMMaterialInspection inspection) {
        PQMInspection oldInspection = JsonUtils.cloneEntity(inspectionRepository.findOne(inspection.getId()), PQMInspection.class);
        inspection = materialInspectionRepository.save(inspection);
        PQMInspection pqmInspection = inspectionRepository.findOne(inspection.getId());
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionBasicInfoUpdatedEvent(oldInspection, pqmInspection));
        return inspection;
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMaterialInspection(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        materialInspectionRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMMaterialInspection getMaterialInspection(Integer id) {
        PQMMaterialInspection inspection = materialInspectionRepository.findOne(id);
        PLMWorkflow workflow = workflowRepository.findByAttachedTo(inspection.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = workflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    inspection.setStartWorkflow(true);
                }
            }
        }
        return inspection;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<InspectionsDto> getAllMaterialInspections(Pageable pageable, InspectionPlanCriteria criteria) {
        Predicate predicate = materialInspectionPredicateBuilder.build(criteria, QPQMMaterialInspection.pQMMaterialInspection);
        Page<PQMMaterialInspection> inspections = materialInspectionRepository.findAll(predicate, pageable);

        List<InspectionsDto> inspectionsDtos = new LinkedList<>();
        inspections.getContent().forEach(inspection -> {
            InspectionsDto dto = new InspectionsDto();
            dto.setId(inspection.getId());

            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setInspectionPlan(materialInspectionPlan.getName());
            if (inspection.getMaterial() != null) {
                dto.setMaterialName(inspection.getMaterial().getPartName());
            }

            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            dto.setDeviationSummary(inspection.getDeviationSummary());
            dto.setAssignedTo(personRepository.findOne(inspection.getAssignedTo()).getFullName());
            dto.setCreatedBy(personRepository.findOne(inspection.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(inspection.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspection.getModifiedDate());
            dto.setCreatedDate(inspection.getCreatedDate());
            dto.setNotes(inspection.getNotes());
            dto.setReleased(inspection.getReleased());
            dto.setObjectType(inspection.getObjectType().toString());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspection.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
            
            dto.setTagsCount(inspection.getTags().size());
            inspectionsDtos.add(dto);
        });

        return new PageImpl<InspectionsDto>(inspectionsDtos, pageable, inspections.getTotalElements());
    }

    @Transactional
    public PQMInspectionAttribute createInspectionAttribute(PQMInspectionAttribute attribute) {
        return inspectionAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMInspectionAttribute updateInspectionAttribute(PQMInspectionAttribute attribute) {
        attribute = inspectionAttributeRepository.save(attribute);
        return attribute;
    }


    @Transactional(readOnly = true)
    public List<PQMInspectionChecklist> getInspectionChecklists(Integer planId) {
        List<PQMInspectionChecklist> planChecklists = new LinkedList<>();
        planChecklists = inspectionChecklistRepository.findByInspectionAndParentIsNullOrderBySeqAsc(planId);
        planChecklists.forEach(planChecklist -> {
            planChecklist.setChildren(inspectionChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getInspection(), planChecklist.getPlanChecklist().getId()));

            for (PQMInspectionChecklist checklist : planChecklist.getChildren()) {
                checklist.setParamsCount(paramActualValueRepository.findByChecklistOrderByIdAsc(checklist.getId()).size());
                checklist.setAttachmentCount(mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getPlanChecklist().getId()).size());
                checklist.setAttachmentCount(checklist.getAttachmentCount() + mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getId()).size());
                checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getPlanChecklist().getId(), ObjectType.ATTACHMENT).size());
                checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getId(), ObjectType.ATTACHMENT).size());

            }
        });
        return planChecklists;
    }

    @Transactional
    public PQMInspectionChecklist updateInspectionChecklist(Integer id, Integer checklistId, PQMInspectionChecklist inspectionChecklist) {

        PQMInspectionChecklist oldChecklist = JsonUtils.cloneEntity(inspectionChecklistRepository.findOne(inspectionChecklist.getId()), PQMInspectionChecklist.class);
        PQMInspection inspection = inspectionRepository.findOne(id);
        if (inspectionChecklist.getResult().equals(ChecklistResult.PASS) || inspectionChecklist.getResult().equals(ChecklistResult.FAIL)) {
            inspectionChecklist.setStatus(ChecklistStatus.FINISHED);
        }
        /*if (checklist != null && checklist.getAssignedTo() == null && inspectionChecklist.getAssignedTo() != null) {
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistAssignedToEvent(inspection, inspectionChecklist));
        } else if (checklist != null && checklist.getAssignedTo() != inspectionChecklist.getAssignedTo()) {
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistAssignedToEvent(inspection, inspectionChecklist));
        }*/
        inspectionChecklist = inspectionChecklistRepository.save(inspectionChecklist);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistUpdatedEvent(inspection, oldChecklist, inspectionChecklist));
        return inspectionChecklist;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionChecklist> getInspectionChecklistChildren(Integer planId, Integer checklistId) {
        List<PQMInspectionChecklist> planChecklists = new ArrayList<>();
        PQMInspectionChecklist inspectionChecklist = inspectionChecklistRepository.findOne(checklistId);
        planChecklists = inspectionChecklistRepository.findByParentOrderBySeqAsc(planId, inspectionChecklist.getPlanChecklist().getId());
        for (PQMInspectionChecklist checklist : planChecklists) {
            checklist.setParamsCount(paramActualValueRepository.findByChecklistOrderByIdAsc(checklist.getId()).size());
            checklist.setAttachmentCount(mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getPlanChecklist().getId()).size());
            checklist.setAttachmentCount(checklist.getAttachmentCount() + mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getId()).size());
            checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getPlanChecklist().getId(), ObjectType.ATTACHMENT).size());
            checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getId(), ObjectType.ATTACHMENT).size());

        }
        return planChecklists;
    }

    @Transactional(readOnly = true)
    public List<PQMParamActualValue> getInspectionChecklistParams(Integer id, Integer checklistId) {
        return paramActualValueRepository.findByChecklistOrderByIdAsc(checklistId);
    }

    @Transactional
    public PQMParamActualValue updateInspectionChecklistParams(Integer inspectionId, Integer checklistId, PQMParamActualValue paramActualValue) {

        PQMParamActualValue pqmParamActualValue = paramActualValueRepository.findOne(paramActualValue.getId());

        if (pqmParamActualValue != null) {
            if (pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.TEXT)) {
                if ((pqmParamActualValue.getParam().getPassCriteria().equals("="))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getTextValue().equals(paramActualValue.getTextValue())) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("!="))) {
                    if (!pqmParamActualValue.getParam().getExpectedValue().getTextValue().equals(paramActualValue.getTextValue())) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                }
            } else if (pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.INTEGER) || pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.DOUBLE)) {
                Double expectedValue = 0.0;
                Double actualValue = 0.0;
                if (pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.INTEGER)) {
                    expectedValue = pqmParamActualValue.getParam().getExpectedValue().getIntegerValue().doubleValue();
                    actualValue = paramActualValue.getIntegerValue().doubleValue();
                } else {
                    expectedValue = pqmParamActualValue.getParam().getExpectedValue().getDoubleValue();
                    actualValue = paramActualValue.getDoubleValue();
                }
                if ((pqmParamActualValue.getParam().getPassCriteria().equals("="))) {
                    if (expectedValue.equals(actualValue)) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("!="))) {
                    if (!expectedValue.equals(actualValue)) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals(">"))) {
                    if (expectedValue < actualValue) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("<"))) {
                    if (expectedValue > actualValue) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                }
            } else if (pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.BOOLEAN)) {
                if ((pqmParamActualValue.getParam().getPassCriteria().equals("="))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getBooleanValue().equals(paramActualValue.getBooleanValue())) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("!="))) {
                    if (!pqmParamActualValue.getParam().getExpectedValue().getBooleanValue().equals(paramActualValue.getBooleanValue())) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                }
            } else if (pqmParamActualValue.getParam().getExpectedValueType().equals(DataType.DATE)) {
                if ((pqmParamActualValue.getParam().getPassCriteria().equals("="))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getDateValue().compareTo(paramActualValue.getDateValue()) == 0) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("!="))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getDateValue().compareTo(paramActualValue.getDateValue()) != 0) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals(">"))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getDateValue().compareTo(paramActualValue.getDateValue()) < 0) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                } else if ((pqmParamActualValue.getParam().getPassCriteria().equals("<"))) {
                    if (pqmParamActualValue.getParam().getExpectedValue().getDateValue().compareTo(paramActualValue.getDateValue()) > 0) {
                        paramActualValue.setResult(ChecklistResult.PASS);
                    } else {
                        paramActualValue.setResult(ChecklistResult.FAIL);
                    }
                }
            }
        }
        PQMInspection inspection = inspectionRepository.findOne(inspectionId);
        paramActualValue = paramActualValueRepository.save(paramActualValue);
        PQMInspectionChecklist oldChecklist = JsonUtils.cloneEntity(inspectionChecklistRepository.findOne(checklistId), PQMInspectionChecklist.class);
        PQMInspectionChecklist inspectionChecklist = inspectionChecklistRepository.findOne(checklistId);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistParameterUpdatedEvent(inspection, inspectionChecklist, paramActualValue));
        List<PQMParamActualValue> paramActualValues = paramActualValueRepository.findByChecklistOrderByIdAsc(checklistId);
        Boolean noneExist = false;
        Boolean failedExist = false;
        for (PQMParamActualValue actualValue : paramActualValues) {
            if (actualValue.getResult().equals(ChecklistResult.NONE)) {
                noneExist = true;
            } else if (actualValue.getResult().equals(ChecklistResult.FAIL)) {
                failedExist = true;
            }
        }

        if (!noneExist && failedExist) {
            inspectionChecklist.setStatus(ChecklistStatus.FINISHED);
            inspectionChecklist.setResult(ChecklistResult.FAIL);
            inspectionChecklist = inspectionChecklistRepository.save(inspectionChecklist);
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistUpdatedEvent(inspection, oldChecklist, inspectionChecklist));
        }

        if (!noneExist && !failedExist) {
            inspectionChecklist.setStatus(ChecklistStatus.FINISHED);
            inspectionChecklist.setResult(ChecklistResult.PASS);
            inspectionChecklist = inspectionChecklistRepository.save(inspectionChecklist);
            applicationEventPublisher.publishEvent(new InspectionEvents.InspectionChecklistUpdatedEvent(inspection, oldChecklist, inspectionChecklist));
        }

        return paramActualValue;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getDetailsCount(Integer planId) {
        PQMInspection inspection = inspectionRepository.findOne(planId);
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setChecklists(inspectionChecklistRepository.findByInspectionAndType(planId, "CHECKLIST").size());
        detailsDto.setItemFiles(inspectionFileRepository.findByInspectionAndFileTypeAndLatestTrueOrderByModifiedDateDesc(planId, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(planId));
        if (inspection.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ITEMINSPECTION.toString()))) {
            detailsDto.setRelatedItems(itemInspectionRelatedItemRepository.findByInspection(planId).size());
        } else {
            detailsDto.setRelatedItems(materialInspectionRelatedItemRepository.findByInspection(planId).size());
        }
        detailsDto.setTotalChecklists(inspectionChecklistRepository.findByInspectionAndType(planId, "CHECKLIST").size());
        detailsDto.setPendingChecklists(inspectionChecklistRepository.findByInspectionAndStatusAndType(planId, ChecklistStatus.PENDING, "CHECKLIST").size());
        detailsDto.setFinishedChecklists(inspectionChecklistRepository.findByInspectionAndStatusAndType(planId, ChecklistStatus.FINISHED, "CHECKLIST").size());
        detailsDto.setPassChecklists(inspectionChecklistRepository.findByInspectionAndResultAndType(planId, ChecklistResult.PASS, "CHECKLIST").size());
        detailsDto.setFailChecklist(inspectionChecklistRepository.findByInspectionAndResultAndType(planId, ChecklistResult.FAIL, "CHECKLIST").size());

        return detailsDto;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionFile> getInspectionFiles(Integer id) {
        return inspectionFileRepository.findByInspectionAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PQMItemInspectionRelatedItem> createItemRelatedItems(Integer id, List<PQMItemInspectionRelatedItem> relatedItems) {
        PQMInspection inspection = inspectionRepository.findOne(id);
        relatedItems = itemInspectionRelatedItemRepository.save(relatedItems);
        applicationEventPublisher.publishEvent(new InspectionEvents.ItemInspectionRelatedItemAddedEvent(inspection, relatedItems));
        return relatedItems;
    }

    @Transactional
    public PQMItemInspectionRelatedItem updateItemRelatedItem(Integer id, PQMItemInspectionRelatedItem relatedItem) {
        return itemInspectionRelatedItemRepository.save(relatedItem);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteItemRelatedItem(Integer id, Integer relatedItem) {
        PQMInspection inspection = inspectionRepository.findOne(id);
        PQMItemInspectionRelatedItem itemInspectionRelatedItem = itemInspectionRelatedItemRepository.findOne(relatedItem);
        applicationEventPublisher.publishEvent(new InspectionEvents.ItemInspectionRelatedItemDeletedEvent(inspection, itemInspectionRelatedItem));
        itemInspectionRelatedItemRepository.delete(relatedItem);
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getItemRelatedItems(Integer id) {
        List<PQMItemInspectionRelatedItem> relatedItems = itemInspectionRelatedItemRepository.findByInspection(id);

        List<PRItemsDto> itemsDtos = new LinkedList<>();

        for (PQMItemInspectionRelatedItem relatedItem : relatedItems) {
            PRItemsDto itemsDto = new PRItemsDto();
            itemsDto.setId(relatedItem.getId());
            itemsDto.setInspection(relatedItem.getInspection());
            itemsDto.setItem(relatedItem.getItem());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getItem());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

            itemsDto.setItemName(item.getItemName());
            itemsDto.setItemNumber(item.getItemNumber());
            itemsDto.setItemType(item.getItemType().getName());
            itemsDto.setDescription(item.getDescription());
            itemsDto.setRevision(itemRevision.getRevision());
            itemsDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            itemsDto.setNotes(relatedItem.getNotes());
            itemsDto.setDescription(item.getDescription());
            itemsDtos.add(itemsDto);
        }

        return itemsDtos;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','relateditem')")
    public List<PQMMaterialInspectionRelatedItem> createMaterialRelatedItems(Integer id, List<PQMMaterialInspectionRelatedItem> relatedItems) {
        relatedItems = materialInspectionRelatedItemRepository.save(relatedItems);
        PQMInspection inspection = inspectionRepository.findOne(id);
        applicationEventPublisher.publishEvent(new InspectionEvents.MaterialInspectionRelatedItemAddedEvent(inspection, relatedItems));
        return relatedItems;
    }

    @Transactional
    public PQMMaterialInspectionRelatedItem updateMaterialRelatedItem(Integer id, PQMMaterialInspectionRelatedItem relatedItem) {
        return materialInspectionRelatedItemRepository.save(relatedItem);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','relateditem')")
    public void deleteMaterialRelatedItem(Integer id, Integer relatedItem) {
        PQMInspection inspection = inspectionRepository.findOne(id);
        PQMMaterialInspectionRelatedItem materialInspectionRelatedItem = materialInspectionRelatedItemRepository.findOne(relatedItem);
        applicationEventPublisher.publishEvent(new InspectionEvents.MaterialInspectionRelatedItemDeletedEvent(inspection, materialInspectionRelatedItem));

        materialInspectionRelatedItemRepository.delete(relatedItem);
    }

    @Transactional(readOnly = true)
    public List<PRItemsDto> getMaterialRelatedItems(Integer id) {
        List<PQMMaterialInspectionRelatedItem> relatedItems = materialInspectionRelatedItemRepository.findByInspection(id);

        List<PRItemsDto> itemsDtos = new LinkedList<>();

        for (PQMMaterialInspectionRelatedItem relatedItem : relatedItems) {
            PRItemsDto itemsDto = new PRItemsDto();
            itemsDto.setId(relatedItem.getId());
            itemsDto.setInspection(relatedItem.getInspection());
            itemsDto.setMaterial(relatedItem.getMaterial());
            PLMManufacturerPart manufacturerPart = manufacturerPartRepository.findOne(relatedItem.getMaterial());

            itemsDto.setPartName(manufacturerPart.getPartName());
            itemsDto.setPartNumber(manufacturerPart.getPartNumber());
            itemsDto.setPartType(manufacturerPart.getMfrPartType().getName());
            itemsDto.setLifeCyclePhase(manufacturerPart.getLifeCyclePhase());
            itemsDto.setNotes(relatedItem.getNotes());
            itemsDto.setDescription(manufacturerPart.getDescription());
            itemsDto.setManufacturerName(manufacturerRepository.findOne(manufacturerPart.getManufacturer()).getName());
            itemsDto.setManufacturer(manufacturerPart.getManufacturer());
            itemsDtos.add(itemsDto);
        }

        return itemsDtos;
    }

    @Transactional(readOnly = true)
    public List<InspectionsDto> getRejectedItemInspectionByProduct(Integer productId) {

        List<PQMItemInspection> itemInspections = itemInspectionRepository.findByItemAndStatusType(productId, WorkflowStatusType.REJECTED);

        List<InspectionsDto> inspectionsDtos = new LinkedList<>();
        itemInspections.forEach(inspection -> {
            InspectionsDto dto = new InspectionsDto();
            dto.setId(inspection.getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setItem(inspection.getItem());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setInspectionPlan(inspectionPlan.getName());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            inspectionsDtos.add(dto);
        });

        return inspectionsDtos;
    }

    @Transactional(readOnly = true)
    public List<InspectionsDto> getRejectedMaterialInspections() {

        List<PQMMaterialInspection> itemInspections = materialInspectionRepository.findByStatusType(WorkflowStatusType.REJECTED);

        List<InspectionsDto> inspectionsDtos = new LinkedList<>();
        itemInspections.forEach(inspection -> {
            InspectionsDto dto = new InspectionsDto();
            dto.setId(inspection.getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setMaterial(inspection.getMaterial().getId());
            dto.setMaterialName(inspection.getMaterial().getPartName());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setInspectionPlan(inspectionPlan.getName());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            inspectionsDtos.add(dto);
        });

        return inspectionsDtos;
    }

    @Transactional(readOnly = true)
    public List<InspectionsDto> getInspectionsByItem(Integer productId) {

        List<PQMItemInspection> itemInspections = itemInspectionRepository.findByItem(productId);

        List<InspectionsDto> inspectionsDtos = new LinkedList<>();
        itemInspections.forEach(inspection -> {
            InspectionsDto dto = new InspectionsDto();
            dto.setId(inspection.getId());

            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspection.getInspectionPlan());
            PQMProductInspectionPlan inspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            dto.setInspectionNumber(inspection.getInspectionNumber());
            dto.setInspectionPlan(inspectionPlan.getName());
            dto.setStatus(inspection.getStatus());
            dto.setStatusType(inspection.getStatusType());
            dto.setDeviationSummary(inspection.getDeviationSummary());
            dto.setAssignedTo(personRepository.findOne(inspection.getAssignedTo()).getFullName());
            dto.setCreatedBy(personRepository.findOne(inspectionPlan.getCreatedBy()).getFullName());
            dto.setModifiedBy(personRepository.findOne(inspectionPlan.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspection.getModifiedDate());
            dto.setCreatedDate(inspection.getCreatedDate());
            if (inspectionPlan.getProduct() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(inspectionPlan.getProduct());
                dto.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            }
            dto.setNotes(inspection.getNotes());
            dto.setReleased(inspection.getReleased());
            inspectionsDtos.add(dto);
        });

        return inspectionsDtos;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowStartedEvent(inspection, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        inspection.setStatus(fromStatus.getName());
        inspection.setStatusType(fromStatus.getType());
        if (inspection.getStatusType().equals(WorkflowStatusType.REJECTED) || inspection.getStatusType().equals(WorkflowStatusType.RELEASED)) {
            Integer pendingChecklists = inspectionChecklistRepository.findByInspectionAndStatusAndType(inspection.getId(), ChecklistStatus.PENDING, "CHECKLIST").size();
            if (pendingChecklists > 0) {
                throw new CassiniException(messageSource.getMessage("finish_checklists_to_promote", null, "Finish all checklist to promote workflow", LocaleContextHolder.getLocale()));
            }
            if (inspection.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                inspection.setReleased(true);
            }
        }
        update(inspection);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowPromotedEvent(inspection, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflowStatus toStatus = event.getToStatus();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        inspection.setStatus(toStatus.getName());
        inspection.setStatusType(toStatus.getType());
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            inspection.setReleased(false);
        }
        update(inspection);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowDemotedEvent(inspection, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        inspection.setStatus(fromStatus.getName());
        inspection.setStatusType(fromStatus.getType());
        inspection.setReleased(true);
        update(inspection);
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowFinishedEvent(inspection, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowHoldEvent(inspection, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'ITEMINSPECTION' || #event.attachedToType.name() == 'MATERIALINSPECTION'")
    public void inspectionWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMInspection inspection = (PQMInspection) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowUnholdEvent(inspection, plmWorkflow, fromStatus));
    }
}
