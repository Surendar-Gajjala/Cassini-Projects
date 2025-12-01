package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.col.MediaType;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.activitystream.ActivityStreamObjectRepository;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.InspectionPlanEvents;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.filtering.MaterialInspectionPlanPredicateBuilder;
import com.cassinisys.plm.filtering.ProductInspectionPlanPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.InspectionPlansDto;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.cm.QualityWorkflowService;
import com.cassinisys.plm.service.plm.ItemServiceException;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.util.IOUtils;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyam on 04-06-2020.
 */
@Service
public class InspectionPlanService implements CrudService<PQMInspectionPlan, Integer> {

    @Autowired
    private InspectionPlanTypeRepository inspectionPlanTypeRepository;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ItemInspectionRepository itemInspectionRepository;
    @Autowired
    private MaterialInspectionRepository materialInspectionRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private ProductInspectionPlanPredicateBuilder productInspectionPlanPredicateBuilder;
    @Autowired
    private MaterialInspectionPlanPredicateBuilder materialInspectionPlanPredicateBuilder;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private InspectionPlanFileRepository inspectionPlanFileRepository;
    @Autowired
    private InspectionPlanAttributeRepository inspectionPlanAttributeRepository;
    @Autowired
    private InspectionPlanRevisionAttributeRepository inspectionPlanRevisionAttributeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private InspectionPlanChecklistRepository inspectionPlanChecklistRepository;
    @Autowired
    private InspectionPlanChecklistParameterRepository inspectionPlanChecklistParameterRepository;
    @Autowired
    private ParamValueRepository paramValueRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private InspectionChecklistRepository inspectionChecklistRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private InspectionPlanHistoryRepository inspectionPlanHistoryRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private QualityWorkflowService qualityWorkflowService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ActivityStreamObjectRepository activityStreamObjectRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlan,'create')")
    public PQMInspectionPlan create(PQMInspectionPlan inspectionPlan) {
        inspectionPlanRepository.save(inspectionPlan);
        return inspectionPlan;
    }

    @Transactional
    @PreAuthorize("hasPermission(#productInspectionPlan,'create')")
    public PQMProductInspectionPlan createProductInspectionPlan(PQMProductInspectionPlan productInspectionPlan) {
        Integer workflowId = productInspectionPlan.getWorkflow();
        PQMProductInspectionPlan existPlanNumber = productInspectionPlanRepository.findByNumber(productInspectionPlan.getNumber());
        PQMProductInspectionPlan existPlanName = productInspectionPlanRepository.findByName(productInspectionPlan.getName());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(productInspectionPlan.getNumber() + " : " + "number_already_exists", null, productInspectionPlan.getNumber() + " Number already exist", LocaleContextHolder.getLocale()));
        }
        if (existPlanName != null) {
            throw new CassiniException(messageSource.getMessage(productInspectionPlan.getName() + " : " + "plan_name_already_exists", null, productInspectionPlan.getName() + " Name already exist", LocaleContextHolder.getLocale()));
        }
        productInspectionPlan = productInspectionPlanRepository.save(productInspectionPlan);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(productInspectionPlan.getId());
        PQMInspectionPlanRevision inspectionPlanRevision = new PQMInspectionPlanRevision();
        inspectionPlanRevision.setPlan(inspectionPlan);
        inspectionPlanRevision.setObjectType(PLMObjectType.INSPECTIONPLANREVISION);
        PQMInspectionPlanType inspectionPlanType = inspectionPlanTypeRepository.findOne(productInspectionPlan.getPlanType().getId());
        Lov lov = inspectionPlanType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(inspectionPlanType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        inspectionPlanRevision.setLifeCyclePhase(lifeCyclePhase);
        inspectionPlanRevision.setRevision(lov.getDefaultValue());
        inspectionPlanRevision.setStatus("NONE");
        inspectionPlanRevision = inspectionPlanRevisionRepository.save(inspectionPlanRevision);

        PQMInspectionPlanHistory inspectionPlanHistory = new PQMInspectionPlanHistory();
        inspectionPlanHistory.setInspectionPlan(inspectionPlanRevision.getId());
        inspectionPlanHistory.setOldStatus(inspectionPlanRevision.getLifeCyclePhase());
        inspectionPlanHistory.setNewStatus(inspectionPlanRevision.getLifeCyclePhase());
        inspectionPlanHistory.setTimestamp(new Date());
        inspectionPlanHistory.setUpdatedBy(inspectionPlanRevision.getCreatedBy());
        inspectionPlanHistory = inspectionPlanHistoryRepository.save(inspectionPlanHistory);

        productInspectionPlan.setLatestRevision(inspectionPlanRevision.getId());
        autoNumberService.saveNextNumber(productInspectionPlan.getPlanType().getNumberSource().getId(), productInspectionPlan.getNumber());
        productInspectionPlan = productInspectionPlanRepository.save(productInspectionPlan);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.INSPECTIONPLANREVISION, productInspectionPlan.getLatestRevision(), wfDef);
                inspectionPlanRevision.setWorkflow(workflow.getId());
                inspectionPlanRevision = inspectionPlanRevisionRepository.save(inspectionPlanRevision);
            }
        }

        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanCreatedEvent(inspectionPlan, inspectionPlanRevision));
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanRevisionCreatedEvent(inspectionPlan, inspectionPlanRevision));

        return productInspectionPlan;
    }

    @Transactional
    @PreAuthorize("hasPermission(#materialInspectionPlan,'create')")
    public PQMMaterialInspectionPlan createMaterialInspectionPlan(PQMMaterialInspectionPlan materialInspectionPlan) {
        Integer workflowId = materialInspectionPlan.getWorkflow();

        PQMMaterialInspectionPlan existPlanNumber = materialInspectionPlanRepository.findByNumber(materialInspectionPlan.getNumber());
        PQMMaterialInspectionPlan existPlanName = materialInspectionPlanRepository.findByName(materialInspectionPlan.getName());
        if (existPlanNumber != null) {
            throw new CassiniException(messageSource.getMessage(materialInspectionPlan.getNumber() + " : " + "number_already_exists", null, materialInspectionPlan.getNumber() + " Number already exist", LocaleContextHolder.getLocale()));
        }
        if (existPlanName != null) {
            throw new CassiniException(messageSource.getMessage(materialInspectionPlan.getName() + " : " + "plan_name_already_exists", null, materialInspectionPlan.getName() + " Name already exist", LocaleContextHolder.getLocale()));
        }
        materialInspectionPlan = materialInspectionPlanRepository.save(materialInspectionPlan);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(materialInspectionPlan.getId());
        PQMInspectionPlanRevision inspectionPlanRevision = new PQMInspectionPlanRevision();
        inspectionPlanRevision.setPlan(inspectionPlan);
        inspectionPlanRevision.setObjectType(PLMObjectType.INSPECTIONPLANREVISION);
        PQMInspectionPlanType inspectionPlanType = inspectionPlanTypeRepository.findOne(materialInspectionPlan.getPlanType().getId());
        Lov lov = inspectionPlanType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(inspectionPlanType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        inspectionPlanRevision.setLifeCyclePhase(lifeCyclePhase);
        inspectionPlanRevision.setRevision(lov.getDefaultValue());
        inspectionPlanRevision.setStatus("NONE");
        inspectionPlanRevision = inspectionPlanRevisionRepository.save(inspectionPlanRevision);

        PQMInspectionPlanHistory inspectionPlanHistory = new PQMInspectionPlanHistory();
        inspectionPlanHistory.setInspectionPlan(inspectionPlanRevision.getId());
        inspectionPlanHistory.setOldStatus(inspectionPlanRevision.getLifeCyclePhase());
        inspectionPlanHistory.setNewStatus(inspectionPlanRevision.getLifeCyclePhase());
        inspectionPlanHistory.setTimestamp(new Date());
        inspectionPlanHistory.setUpdatedBy(inspectionPlanRevision.getCreatedBy());
        inspectionPlanHistory = inspectionPlanHistoryRepository.save(inspectionPlanHistory);

        materialInspectionPlan.setLatestRevision(inspectionPlanRevision.getId());
        autoNumberService.saveNextNumber(materialInspectionPlan.getPlanType().getNumberSource().getId(), materialInspectionPlan.getNumber());
        materialInspectionPlan = materialInspectionPlanRepository.save(materialInspectionPlan);

        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.INSPECTIONPLANREVISION, materialInspectionPlan.getLatestRevision(), wfDef);
                inspectionPlanRevision.setWorkflow(workflow.getId());
                inspectionPlanRevision = inspectionPlanRevisionRepository.save(inspectionPlanRevision);
            }
        }
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanCreatedEvent(inspectionPlan, inspectionPlanRevision));
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanRevisionCreatedEvent(inspectionPlan, inspectionPlanRevision));

        return materialInspectionPlan;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#inspectionPlan.id,'edit')")
    public PQMInspectionPlan update(PQMInspectionPlan inspectionPlan) {
        inspectionPlan = inspectionPlanRepository.save(inspectionPlan);
        return inspectionPlan;
    }

    @Transactional
    @PreAuthorize("hasPermission(#productInspectionPlan.id ,'edit')")
    public PQMProductInspectionPlan updateProductInspectionPlan(PQMProductInspectionPlan productInspectionPlan) {
        PQMInspectionPlan oldInspectionPlan = JsonUtils.cloneEntity(inspectionPlanRepository.findOne(productInspectionPlan.getId()), PQMInspectionPlan.class);
        PQMProductInspectionPlan existPlanName = productInspectionPlanRepository.findByName(productInspectionPlan.getName());
        if (existPlanName != null && !existPlanName.getId().equals(productInspectionPlan.getId())) {
            throw new CassiniException(messageSource.getMessage(productInspectionPlan.getName() + " : " + "plan_name_already_exists", null, productInspectionPlan.getName() + " Name already exist", LocaleContextHolder.getLocale()));
        }
        productInspectionPlan = productInspectionPlanRepository.save(productInspectionPlan);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(productInspectionPlan.getId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(productInspectionPlan.getLatestRevision());
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanBasicInfoUpdatedEvent(oldInspectionPlan, inspectionPlan, inspectionPlanRevision));
        return productInspectionPlan;
    }

    @Transactional
    @PreAuthorize("hasPermission(#materialInspectionPlan.id ,'edit')")
    public PQMMaterialInspectionPlan updateMaterialInspectionPlan(PQMMaterialInspectionPlan materialInspectionPlan) {
        PQMInspectionPlan oldInspectionPlan = JsonUtils.cloneEntity(inspectionPlanRepository.findOne(materialInspectionPlan.getId()), PQMInspectionPlan.class);
        PQMProductInspectionPlan existPlanName = productInspectionPlanRepository.findByName(materialInspectionPlan.getName());
        if (existPlanName != null && !existPlanName.getId().equals(materialInspectionPlan.getId())) {
            throw new CassiniException(messageSource.getMessage(materialInspectionPlan.getName() + " : " + "plan_name_already_exists", null, materialInspectionPlan.getName() + " Name already exist", LocaleContextHolder.getLocale()));
        }
        materialInspectionPlan = materialInspectionPlanRepository.save(materialInspectionPlan);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(materialInspectionPlan.getId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(materialInspectionPlan.getLatestRevision());
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanBasicInfoUpdatedEvent(oldInspectionPlan, inspectionPlan, inspectionPlanRevision));

        return materialInspectionPlan;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(id);
        List<PQMItemInspection> itemInspections = itemInspectionRepository.findByInspectionPlan(inspectionPlan.getLatestRevision());
        List<PQMMaterialInspection> materialInspections = materialInspectionRepository.findByInspectionPlan(inspectionPlan.getLatestRevision());
        if (itemInspections.size() > 0 || materialInspections.size() > 0) {
            throw new CassiniException(messageSource.getMessage("inspection_plan_already_in_use", null, "Product Inspection Plan already in use", LocaleContextHolder.getLocale()));
        } else {
            List<PQMInspectionPlanRevision> planRevisions = inspectionPlanRevisionRepository.findByPlan(inspectionPlan);
            inspectionPlanRepository.delete(id);
            for (PQMInspectionPlanRevision revision : planRevisions) {
                activityStreamObjectRepository.deleteActivityStreamObjectByObjectId(revision.getId());
                CassiniObject object = objectRepository.findById(revision.getId());
                if (object != null) {
                    applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(object.getId()));
                    objectRepository.deleteById(revision.getId());
                }
            }
        }
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteProductInspectionPlan(Integer id) {

        PQMProductInspectionPlan inspectionPlan = productInspectionPlanRepository.findOne(id);
        List<PQMItemInspection> itemInspections = itemInspectionRepository.findByInspectionPlan(inspectionPlan.getLatestRevision());
        if (itemInspections.size() > 0) {
            throw new CassiniException(messageSource.getMessage("inspection_plan_already_in_use", null, "Product Inspection Plan already in use", LocaleContextHolder.getLocale()));
        } else {
            List<PQMInspectionPlanRevision> planRevisions = inspectionPlanRevisionRepository.findByPlan(inspectionPlan);
            planRevisions.forEach(pqmInspectionPlanRevision -> {
                activityStreamObjectRepository.deleteActivityStreamObjectByObjectId(pqmInspectionPlanRevision.getId());
            });
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            productInspectionPlanRepository.delete(id);
        }
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteMaterialInspectionPlan(Integer id) {
        PQMProductInspectionPlan inspectionPlan = productInspectionPlanRepository.findOne(id);
        PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(id);
        List<PQMMaterialInspection> materialInspections = materialInspectionRepository.findByInspectionPlan(materialInspectionPlan.getId());
        if (materialInspections.size() > 0) {
            throw new CassiniException(messageSource.getMessage("inspection_plan_already_in_use", null, "Material Inspection Plan already in use", LocaleContextHolder.getLocale()));
        } else {
            List<PQMInspectionPlanRevision> planRevisions = inspectionPlanRevisionRepository.findByPlan(inspectionPlan);
            planRevisions.forEach(pqmInspectionPlanRevision -> {
                activityStreamObjectRepository.deleteActivityStreamObjectByObjectId(pqmInspectionPlanRevision.getId());
            });
            applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
            materialInspectionPlanRepository.delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMInspectionPlan get(Integer id) {
        return inspectionPlanRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public InspectionPlansDto getPlanDetails(Integer id) {
        InspectionPlansDto dto = new InspectionPlansDto();
        PQMInspectionPlanRevision plan = inspectionPlanRevisionRepository.findOne(id);
        if (plan.getPlan().getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
            PQMProductInspectionPlan inspectionPlan = productInspectionPlanRepository.findOne(plan.getPlan().getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
            dto.setId(inspectionPlan.getId());
            dto.setLatestRevision(inspectionPlanRevision.getId());
            dto.setName(inspectionPlan.getName());
            dto.setNumber(inspectionPlan.getNumber());
            dto.setDescription(inspectionPlan.getDescription());
            dto.setType(inspectionPlan.getPlanType().getName());
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setLifeCyclePhase(inspectionPlanRevision.getLifeCyclePhase());
            dto.setModifiedBy(personRepository.findOne(inspectionPlan.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspectionPlan.getModifiedDate());
            dto.setCreatedBy(personRepository.findOne(inspectionPlan.getCreatedBy()).getFullName());
            dto.setCreatedDate(inspectionPlan.getCreatedDate());
            dto.setNotes(inspectionPlan.getNotes());
            if (inspectionPlan.getProduct() != null) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(inspectionPlan.getProduct());
                dto.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            }
            dto.setStatus(inspectionPlanRevision.getStatus());
            dto.setStatusType(inspectionPlanRevision.getStatusType());
            dto.setReleased(inspectionPlanRevision.getReleased());
            dto.setRejected(inspectionPlanRevision.getRejected());
            dto.setObjectType(inspectionPlan.getObjectType().toString());
            dto.setWorkflow(inspectionPlan.getWorkflow());
            if (inspectionPlan.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(inspectionPlan.getWorkflow());
                if (plmWorkflow != null) {
                    dto.setOnHold(plmWorkflow.getOnhold());
                }
            }
        } else {
            PQMMaterialInspectionPlan inspectionPlan = materialInspectionPlanRepository.findOne(plan.getPlan().getId());
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
            dto.setId(inspectionPlan.getId());
            dto.setLatestRevision(inspectionPlanRevision.getId());
            dto.setName(inspectionPlan.getName());
            dto.setNumber(inspectionPlan.getNumber());
            dto.setDescription(inspectionPlan.getDescription());
            dto.setType(inspectionPlan.getPlanType().getName());
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setLifeCyclePhase(inspectionPlanRevision.getLifeCyclePhase());
            dto.setModifiedBy(personRepository.findOne(inspectionPlan.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspectionPlan.getModifiedDate());
            dto.setCreatedBy(personRepository.findOne(inspectionPlan.getCreatedBy()).getFullName());
            dto.setCreatedDate(inspectionPlan.getCreatedDate());
            dto.setNotes(inspectionPlan.getNotes());
            if (inspectionPlan.getMaterial() != null) {
                dto.setMaterialName(inspectionPlan.getMaterial().getPartName());
            }
            dto.setStatus(inspectionPlanRevision.getStatus());
            dto.setStatusType(inspectionPlanRevision.getStatusType());
            dto.setReleased(inspectionPlanRevision.getReleased());
            dto.setRejected(inspectionPlanRevision.getRejected());
            dto.setObjectType(inspectionPlan.getObjectType().toString());
            dto.setWorkflow(inspectionPlan.getWorkflow());
            if (inspectionPlan.getWorkflow() != null) {
                PLMWorkflow plmWorkflow = plmWorkflowRepository.findOne(inspectionPlan.getWorkflow());
                if (plmWorkflow != null) {
                    dto.setOnHold(plmWorkflow.getOnhold());
                }
            }
        }
        return dto;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMProductInspectionPlan getProductInspectionPlan(Integer id) {
        PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(id);
        return productInspectionPlan;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMMaterialInspectionPlan getMaterialInspectionPlan(Integer id) {
        PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(id);
        return materialInspectionPlan;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspectionPlan> getAll() {
        return inspectionPlanRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMInspectionPlan> findMultiple(List<Integer> ids) {
        return null;
    }

    @Transactional
    public PQMInspectionPlanRevision revisePlan(Integer id) {
        PQMInspectionPlanRevision revision = inspectionPlanRevisionRepository.findOne(id);
        if (revision != null) {
            return reviseInspectionPlan(revision, null);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanRevision> getPlanRevisionHistory(Integer id) {
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(id);
        List<PQMInspectionPlanRevision> revisions = inspectionPlanRevisionRepository.getByPlanOrderByCreatedDateDesc(inspectionPlan);
        revisions.forEach(revision -> {
            List<PQMInspectionPlanHistory> history = inspectionPlanHistoryRepository.findByInspectionPlanOrderByTimestampDesc(revision.getId());
            revision.setStatusHistory(history);
        });
        return revisions;
    }

    @Transactional
    public PQMInspectionPlanRevision reviseInspectionPlan(PQMInspectionPlanRevision revision, String nextRev) {
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(revision.getPlan().getId());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(inspectionPlan);
        }
        if (nextRev != null) {
            PQMInspectionPlanRevision copy = createNextRev(revision, nextRev);
            inspectionPlan.setLatestRevision(copy.getId());
            inspectionPlanRepository.save(inspectionPlan);
            PLMWorkflow workflow = plmWorkflowRepository.findOne(revision.getWorkflow());
            if (workflow != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
                PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef != null) {
                    if (copy.getPlan().getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
                        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(copy.getId());
                        workflow = workflowService.attachWorkflow(PLMObjectType.INSPECTIONPLANREVISION, inspectionPlanRevision.getId(), wfDef);
                        inspectionPlanRevision.setWorkflow(workflow.getId());
                        inspectionPlanRevisionRepository.save(inspectionPlanRevision);
                    } else if (copy.getPlan().getObjectType().equals(ObjectType.valueOf(PLMObjectType.MATERIALINSPECTIONPLAN.toString()))) {
                        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(copy.getId());
                        workflow = workflowService.attachWorkflow(PLMObjectType.INSPECTIONPLANREVISION, inspectionPlanRevision.getId(), wfDef);
                        inspectionPlanRevision.setWorkflow(workflow.getId());
                        inspectionPlanRevisionRepository.save(inspectionPlanRevision);
                    }
                }
            }
            //Copy the related
            copyItemRevisionAttributes(revision, copy);
            copyInspectionPlanChecklists(revision, copy);
            copyFolderStructure(revision, copy);
            return copy;
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public String getNextRevisionSequence(PQMInspectionPlan inspectionPlan) {
        String nextRev = null;
        Lov lov = null;
        PQMInspectionPlanRevision planRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
        if (inspectionPlan.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlan.getId());
            lov = productInspectionPlan.getPlanType().getRevisionSequence();
        } else {
            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlan.getId());
            lov = materialInspectionPlan.getPlanType().getRevisionSequence();
        }
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(planRevision.getRevision())) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }

        return nextRev;
    }

    private PQMInspectionPlanRevision createNextRev(PQMInspectionPlanRevision inspectionPlanRevision, String nextRev) {
        PQMInspectionPlanRevision copy = new PQMInspectionPlanRevision();
        PLMLifeCyclePhase lifeCyclePhase = null;
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
        if (inspectionPlan.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
            PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(productInspectionPlan.getPlanType().getLifecycle().getId());
            lifeCyclePhase = lifeCyclePhases.get(0);
        } else {
            PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(materialInspectionPlan.getPlanType().getLifecycle().getId());
            lifeCyclePhase = lifeCyclePhases.get(0);
        }

        copy.setPlan(inspectionPlanRevision.getPlan());
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setStatus("NONE");
        copy = inspectionPlanRevisionRepository.save(copy);
        PQMInspectionPlanHistory inspectionPlanHistory = new PQMInspectionPlanHistory();
        inspectionPlanHistory.setInspectionPlan(copy.getId());
        inspectionPlanHistory.setOldStatus(copy.getLifeCyclePhase());
        inspectionPlanHistory.setNewStatus(copy.getLifeCyclePhase());
        inspectionPlanHistory.setTimestamp(new Date());
        inspectionPlanHistory.setUpdatedBy(copy.getCreatedBy());
        inspectionPlanHistory = inspectionPlanHistoryRepository.save(inspectionPlanHistory);

        return copy;
    }

    private void copyItemRevisionAttributes(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision) {
        List<PQMInspectionPlanRevisionAttribute> attrs = inspectionPlanRevisionAttributeRepository.findByInspectionPlanRevisionIdIn(oldRevision.getId());
        for (PQMInspectionPlanRevisionAttribute attr : attrs) {
            PQMInspectionPlanRevisionAttribute revisionAttribute = new PQMInspectionPlanRevisionAttribute();
            revisionAttribute.setId(new ObjectAttributeId(newRevision.getId(), attr.getId().getAttributeDef()));
            revisionAttribute.setStringValue(attr.getStringValue());
            revisionAttribute.setIntegerValue(attr.getIntegerValue());
            revisionAttribute.setLongTextValue(attr.getLongTextValue());
            revisionAttribute.setRichTextValue(attr.getRichTextValue());
            revisionAttribute.setBooleanValue(attr.getBooleanValue());
            revisionAttribute.setDateValue(attr.getDateValue());
            revisionAttribute.setDoubleValue(attr.getDoubleValue());
            revisionAttribute.setListValue(attr.getListValue());
            revisionAttribute.setTimeValue(attr.getTimeValue());
            revisionAttribute.setTimestampValue(attr.getTimestampValue());
            revisionAttribute.setImageValue(attr.getImageValue());
            revisionAttribute.setAttachmentValues(attr.getAttachmentValues());
            revisionAttribute.setRefValue(attr.getRefValue());
            revisionAttribute.setCurrencyValue(attr.getCurrencyValue());
            inspectionPlanRevisionAttributeRepository.save(revisionAttribute);
        }
    }

    private void copyInspectionPlanChecklists(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision) {
        List<PQMInspectionPlanChecklist> planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(oldRevision.getId());
        planChecklists.forEach(planChecklist -> {
            PQMInspectionPlanChecklist inspectionPlanChecklist = new PQMInspectionPlanChecklist();
            inspectionPlanChecklist.setInspectionPlan(newRevision.getId());
            inspectionPlanChecklist.setTitle(planChecklist.getTitle());
            inspectionPlanChecklist.setSummary(planChecklist.getSummary());
            inspectionPlanChecklist.setProcedure(planChecklist.getProcedure());
            inspectionPlanChecklist.setSeq(planChecklist.getSeq());
            inspectionPlanChecklist.setType(planChecklist.getType());

            inspectionPlanChecklist = inspectionPlanChecklistRepository.save(inspectionPlanChecklist);

            List<PQMInspectionPlanChecklist> childrenChecklists = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getId());
            for (PQMInspectionPlanChecklist children : childrenChecklists) {
                PQMInspectionPlanChecklist checklist = new PQMInspectionPlanChecklist();
                checklist.setInspectionPlan(newRevision.getId());
                checklist.setTitle(children.getTitle());
                checklist.setSummary(children.getSummary());
                checklist.setProcedure(children.getProcedure());
                checklist.setSeq(children.getSeq());
                checklist.setType(children.getType());
                checklist.setParent(inspectionPlanChecklist.getId());
                checklist = inspectionPlanChecklistRepository.save(checklist);

                List<PQMInspectionPlanChecklistParameter> checklistParameters = inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(children.getId());
                for (PQMInspectionPlanChecklistParameter checklistParameter : checklistParameters) {
                    PQMInspectionPlanChecklistParameter planChecklistParameter = new PQMInspectionPlanChecklistParameter();
                    planChecklistParameter.setName(checklistParameter.getName());
                    planChecklistParameter.setDescription(checklistParameter.getDescription());
                    planChecklistParameter.setInspectionPlan(newRevision.getId());
                    planChecklistParameter.setExpectedValue(checklistParameter.getExpectedValue());
                    planChecklistParameter.setChecklist(checklist.getId());
                    planChecklistParameter.setUnits(checklistParameter.getUnits());
                    planChecklistParameter.setExpectedValueType(checklistParameter.getExpectedValueType());
                    planChecklistParameter.setPassCriteria(checklistParameter.getPassCriteria());
                    PQMParamValue paramValue = (PQMParamValue) Utils.cloneObject(checklistParameter.getExpectedValue(), PQMParamValue.class);
                    paramValue.setId(null);
                    paramValue = paramValueRepository.save(paramValue);

                    planChecklistParameter.setExpectedValue(paramValue);

                    planChecklistParameter = inspectionPlanChecklistParameterRepository.save(planChecklistParameter);

                }
            }
        });
    }

    public void copyFolderStructure(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision) {
        List<PQMInspectionPlanFile> inspectionPlanFileList = inspectionPlanFileRepository.findByInspectionPlanAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(oldRevision.getId());
        for (PQMInspectionPlanFile inspectionPlanFile : inspectionPlanFileList) {
            copyItemFile(oldRevision, newRevision, inspectionPlanFile);
        }
    }

    @Transactional
    public PQMInspectionPlanFile copyItemFile(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision, PQMInspectionPlanFile inspectionPlanFile) {
        PQMInspectionPlanFile newInspectionFile = null;
        File file = getInspectionFile(oldRevision.getId(), inspectionPlanFile.getId());
        if (file != null) {
            newInspectionFile = new PQMInspectionPlanFile();
            Login login = sessionWrapper.getSession().getLogin();
            newInspectionFile.setName(inspectionPlanFile.getName());
            newInspectionFile.setFileNo(inspectionPlanFile.getFileNo());
            newInspectionFile.setFileType(inspectionPlanFile.getFileType());
            newInspectionFile.setCreatedBy(login.getPerson().getId());
            newInspectionFile.setModifiedBy(login.getPerson().getId());
            newInspectionFile.setInspectionPlan(newRevision.getId());
            newInspectionFile.setVersion(inspectionPlanFile.getVersion());
            newInspectionFile.setSize(inspectionPlanFile.getSize());
            newInspectionFile.setLatest(inspectionPlanFile.getLatest());
            newInspectionFile = inspectionPlanFileRepository.save(newInspectionFile);
            if (newInspectionFile.getFileType().equals("FOLDER")) {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + qualityFileService.getParentFileSystemPath(newRevision.getId(), newInspectionFile.getId(), PLMObjectType.INSPECTIONPLAN);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
            } else {
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + qualityFileService.getReplaceFileSystemPath(newRevision.getId(), newInspectionFile.getId(), PLMObjectType.INSPECTIONPLAN);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newInspectionFile.getId();
                saveFileToDisk(file, dir);
                saveOldVersionItemFiles(oldRevision, newRevision, inspectionPlanFile);
            }
        }
        saveInspectionFileChildren(oldRevision, newRevision, inspectionPlanFile, newInspectionFile);
        return newInspectionFile;
    }

    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            IOUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    private void saveOldVersionItemFiles(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision, PQMInspectionPlanFile inspectionPlanFile) {
        List<PQMInspectionPlanFile> oldVersionFiles = inspectionPlanFileRepository.findByInspectionPlanAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldRevision.getId(), inspectionPlanFile.getFileNo());
        for (PQMInspectionPlanFile oldVersionFile : oldVersionFiles) {
            PQMInspectionPlanFile newItemFile = null;
            File file = getInspectionFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new PQMInspectionPlanFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setInspectionPlan(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile = inspectionPlanFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + qualityFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.INSPECTIONPLAN);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    @Transactional
    private void saveInspectionFileChildren(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision, PQMInspectionPlanFile itemFile, PQMInspectionPlanFile plmItemFile) {
        List<PQMInspectionPlanFile> childrenFiles = inspectionPlanFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(itemFile.getId());
        for (PQMInspectionPlanFile childrenFile : childrenFiles) {
            PQMInspectionPlanFile newItemFile = null;
            File file = getInspectionFile(oldRevision.getId(), childrenFile.getId());
            if (file != null) {
                newItemFile = new PQMInspectionPlanFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(childrenFile.getName());
                newItemFile.setFileNo(childrenFile.getFileNo());
                newItemFile.setFileType(childrenFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setInspectionPlan(newRevision.getId());
                newItemFile.setVersion(childrenFile.getVersion());
                newItemFile.setSize(childrenFile.getSize());
                newItemFile.setLatest(childrenFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = inspectionPlanFileRepository.save(newItemFile);
                if (newItemFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + qualityFileService.getParentFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.INSPECTIONPLAN);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + qualityFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.INSPECTIONPLAN);
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newItemFile.getId();
                    saveFileToDisk(file, dir);
                    saveChildrenOldVersionItemFiles(oldRevision, newRevision, childrenFile, plmItemFile);
                }
            }
            saveInspectionFileChildren(oldRevision, newRevision, childrenFile, newItemFile);
        }

    }

    private void saveChildrenOldVersionItemFiles(PQMInspectionPlanRevision oldRevision, PQMInspectionPlanRevision newRevision, PQMInspectionPlanFile itemFile, PQMInspectionPlanFile plmItemFile) {
        List<PQMInspectionPlanFile> oldVersionFiles = inspectionPlanFileRepository.findByInspectionPlanAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldRevision.getId(), itemFile.getFileNo());
        for (PQMInspectionPlanFile oldVersionFile : oldVersionFiles) {
            PQMInspectionPlanFile newItemFile = null;
            File file = getInspectionFile(oldRevision.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new PQMInspectionPlanFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setInspectionPlan(newRevision.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = inspectionPlanFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + qualityFileService.getReplaceFileSystemPath(newRevision.getId(), newItemFile.getId(), PLMObjectType.INSPECTIONPLAN);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    public File getInspectionFile(Integer itemId, Integer fileId) {
        checkNotNull(itemId);
        checkNotNull(fileId);
        PQMInspectionPlanRevision revision = inspectionPlanRevisionRepository.findOne(itemId);
        if (revision == null) {
            throw new ResourceNotFoundException();
        }
        PQMInspectionPlanFile itemFile = inspectionPlanFileRepository.findOne(fileId);
        if (itemFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + qualityFileService.getParentFileSystemPath(itemId, fileId, PLMObjectType.INSPECTIONPLAN);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<PQMProductInspectionPlan> getMultipleProductInspectionPlans(List<Integer> ids) {
        return productInspectionPlanRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication,filterObject,'view')")
    public Page<InspectionPlansDto> getAllProductInspectionPlans(Pageable pageable, InspectionPlanCriteria planCriteria) {
        Predicate predicate = productInspectionPlanPredicateBuilder.build(planCriteria, QPQMProductInspectionPlan.pQMProductInspectionPlan);
        Page<PQMProductInspectionPlan> inspectionPlans = productInspectionPlanRepository.findAll(predicate, pageable);

        List<InspectionPlansDto> plansDto = new LinkedList<>();
        inspectionPlans.getContent().forEach(inspectionPlan -> {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
            InspectionPlansDto dto = new InspectionPlansDto();
            dto.setId(inspectionPlan.getId());
            dto.setLatestRevision(inspectionPlanRevision.getId());
            dto.setName(inspectionPlan.getName());
            dto.setNumber(inspectionPlan.getNumber());
            dto.setDescription(inspectionPlan.getDescription());
            dto.setType(inspectionPlan.getPlanType().getName());
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setLifeCyclePhase(inspectionPlanRevision.getLifeCyclePhase());
            dto.setModifiedBy(personRepository.findOne(inspectionPlan.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspectionPlan.getModifiedDate());
            dto.setCreatedBy(personRepository.findOne(inspectionPlan.getCreatedBy()).getFullName());
            dto.setCreatedDate(inspectionPlan.getCreatedDate());
            dto.setNotes(inspectionPlan.getNotes());
            if (inspectionPlan.getProduct() != null) {
                dto.setProduct(inspectionPlan.getProduct());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(inspectionPlan.getProduct());
                dto.setProductName(itemRepository.findOne(itemRevision.getItemMaster()).getItemName());
            }
            dto.setStatus(inspectionPlanRevision.getStatus());
            dto.setStatusType(inspectionPlanRevision.getStatusType());
            dto.setReleased(inspectionPlanRevision.getReleased());
            dto.setRejected(inspectionPlanRevision.getRejected());
            dto.setObjectType(inspectionPlan.getObjectType().toString());

            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspectionPlanRevision.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());

            dto.setTagsCount(inspectionPlan.getTags().size());
            plansDto.add(dto);
        });

        return new PageImpl<InspectionPlansDto>(plansDto, pageable, inspectionPlans.getTotalElements());
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<InspectionPlansDto> getAllMaterialInspectionPlans(Pageable pageable, InspectionPlanCriteria planCriteria) {
        Predicate predicate = materialInspectionPlanPredicateBuilder.build(planCriteria, QPQMMaterialInspectionPlan.pQMMaterialInspectionPlan);
        Page<PQMMaterialInspectionPlan> materialInspectionPlans = materialInspectionPlanRepository.findAll(predicate, pageable);

        List<InspectionPlansDto> plansDto = new LinkedList<>();
        materialInspectionPlans.getContent().forEach(inspectionPlan -> {
            InspectionPlansDto dto = new InspectionPlansDto();
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
            dto.setId(inspectionPlan.getId());
            dto.setLatestRevision(inspectionPlanRevision.getId());
            dto.setName(inspectionPlan.getName());
            dto.setNumber(inspectionPlan.getNumber());
            dto.setDescription(inspectionPlan.getDescription());
            dto.setType(inspectionPlan.getPlanType().getName());
            dto.setRevision(inspectionPlanRevision.getRevision());
            dto.setLifeCyclePhase(inspectionPlanRevision.getLifeCyclePhase());
            dto.setModifiedBy(personRepository.findOne(inspectionPlan.getModifiedBy()).getFullName());
            dto.setModifiedDate(inspectionPlan.getModifiedDate());
            dto.setCreatedBy(personRepository.findOne(inspectionPlan.getCreatedBy()).getFullName());
            dto.setCreatedDate(inspectionPlan.getCreatedDate());
            dto.setNotes(inspectionPlan.getNotes());
            if (inspectionPlan.getMaterial() != null) {
                dto.setMaterial(inspectionPlan.getMaterial().getId());
                dto.setMaterialName(inspectionPlan.getMaterial().getPartName());
            }
            dto.setStatus(inspectionPlanRevision.getStatus());
            dto.setStatusType(inspectionPlanRevision.getStatusType());
            dto.setReleased(inspectionPlanRevision.getReleased());
            dto.setRejected(inspectionPlanRevision.getRejected());
            dto.setObjectType(inspectionPlan.getObjectType().toString());
             WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspectionPlanRevision.getId());
            dto.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            dto.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            dto.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
            dto.setOnHold(workFlowStatusDto.getOnHold());
            dto.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            dto.setCancelWorkflow(workFlowStatusDto.getCancelWorkflow());
            dto.setTagsCount(inspectionPlan.getTags().size());
            plansDto.add(dto);
        });

        return new PageImpl<InspectionPlansDto>(plansDto, pageable, materialInspectionPlans.getTotalElements());
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlan> getReleasedInspectionPlans() {
        List<PQMInspectionPlan> inspectionPlans = new ArrayList<>();
        List<PQMInspectionPlanRevision> inspectionPlanRevisions = inspectionPlanRevisionRepository.findByReleasedTrue();
        inspectionPlanRevisions.forEach(pqmInspectionPlanRevision -> {
            inspectionPlans.add(inspectionPlanRepository.findOne(pqmInspectionPlanRevision.getPlan().getId()));
        });

        return inspectionPlans;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlan> getInspectionPlans() {
        List<PQMInspectionPlan> inspectionPlans = inspectionPlanRepository.findAll();
        return inspectionPlans;
    }

    @Transactional(readOnly = true)
    public List<PQMProductInspectionPlan> getProductInspectionPlansByProduct(Integer product) {
        List<PQMProductInspectionPlan> inspectionPlans = productInspectionPlanRepository.findByProduct(product);
        List<PQMProductInspectionPlan> releasedPlans = new LinkedList<>();
        inspectionPlans.forEach(pqmInspectionPlan -> {
            PQMInspectionPlanRevision planRevision = inspectionPlanRevisionRepository.findOne(pqmInspectionPlan.getLatestRevision());
            if (planRevision.getReleased()) {
                releasedPlans.add(pqmInspectionPlan);
            }
        });

        return releasedPlans;
    }

    @Transactional(readOnly = true)
    public List<PQMMaterialInspectionPlan> getMaterialInspectionPlansByProduct(Integer part) {
        List<PQMMaterialInspectionPlan> inspectionPlans = materialInspectionPlanRepository.findByMaterialId(part);
        List<PQMMaterialInspectionPlan> releasedPlans = new LinkedList<>();
        inspectionPlans.forEach(pqmInspectionPlan -> {
            PQMInspectionPlanRevision planRevision = inspectionPlanRevisionRepository.findOne(pqmInspectionPlan.getLatestRevision());
            if (planRevision.getReleased()) {
                releasedPlans.add(pqmInspectionPlan);
            }
        });
        return releasedPlans;
    }

    @Transactional(readOnly = true)
    public PQMInspectionPlanRevision getInspectionPlanRevision(Integer id) {
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(id);
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(inspectionPlanRevision.getId());
        inspectionPlanRevision.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        inspectionPlanRevision.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        inspectionPlanRevision.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return inspectionPlanRevision;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanFile> getInspectionPlanFiles(Integer id) {
        return inspectionPlanFileRepository.findByInspectionPlanAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(id);
    }

    @Transactional
    public PQMInspectionPlanAttribute createPlanAttribute(PQMInspectionPlanAttribute attribute) {
        attribute = inspectionPlanAttributeRepository.save(attribute);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(attribute.getId().getObjectId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanAttributesUpdatedEvent(inspectionPlan, inspectionPlanRevision, null, attribute));
        return attribute;
    }

    @Transactional
    public PQMInspectionPlanRevisionAttribute createPlanRevisionAttribute(PQMInspectionPlanRevisionAttribute attribute) {
        attribute = inspectionPlanRevisionAttributeRepository.save(attribute);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(attribute.getId().getObjectId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
        return attribute;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMInspectionPlanAttribute updatePlanAttribute(PQMInspectionPlanAttribute attribute) {
        PQMInspectionPlanAttribute oldValue = inspectionPlanAttributeRepository.findByInspectionAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMInspectionPlanAttribute.class);

        attribute = inspectionPlanAttributeRepository.save(attribute);
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(attribute.getId().getObjectId());
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(inspectionPlan.getLatestRevision());
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanAttributesUpdatedEvent(inspectionPlan, inspectionPlanRevision, oldValue, attribute));
        return attribute;
    }

    @Transactional

    public PQMInspectionPlanRevisionAttribute updatePlanRevisionAttribute(PQMInspectionPlanRevisionAttribute attribute) {
        PQMInspectionPlanRevisionAttribute oldValue = inspectionPlanRevisionAttributeRepository.findByInspectionPlanRevisionAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMInspectionPlanRevisionAttribute.class);

        attribute = inspectionPlanRevisionAttributeRepository.save(attribute);
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(attribute.getId().getObjectId());
        PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(inspectionPlanRevision.getPlan().getId());
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanRevisionAttributesUpdatedEvent(inspectionPlan, inspectionPlanRevision, oldValue, attribute));
        return attribute;
    }

    @Transactional
    public PQMInspectionPlanChecklist createInspectionPlanChecklist(Integer planId, PQMInspectionPlanChecklist planChecklist) {
        List<PQMInspectionPlanChecklist> planChecklists = new ArrayList<>();
        PQMInspectionPlanChecklist existChecklist = null;
        if (planChecklist.getType().equals("SECTION")) {
            planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(planId);
            existChecklist = inspectionPlanChecklistRepository.findByInspectionPlanAndTitleAndParentIsNull(planId, planChecklist.getTitle());
            if (existChecklist != null) {
                throw new CassiniException(messageSource.getMessage(planChecklist.getTitle() + " : " + "section_already_exist",
                        null, planChecklist.getTitle() + " : Section already exist", LocaleContextHolder.getLocale()));
            }
        } else {
            planChecklists = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getParent());
            existChecklist = inspectionPlanChecklistRepository.findByParentAndTitle(planChecklist.getParent(), planChecklist.getTitle());
            if (existChecklist != null) {
                throw new CassiniException(messageSource.getMessage(planChecklist.getTitle() + " : " + "checklist_already_exist",
                        null, planChecklist.getTitle() + " : Checklist already exist", LocaleContextHolder.getLocale()));
            }
        }

        planChecklist.setSeq(planChecklists.size() + 1);
        planChecklist = inspectionPlanChecklistRepository.save(planChecklist);
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(planId);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanChecklistAddEvent(inspectionPlanRevision, planChecklist));
        return planChecklist;
    }

    @Transactional
    public PQMInspectionPlanChecklist updateInspectionPlanChecklist(Integer planId, PQMInspectionPlanChecklist planChecklist) {
        PQMInspectionPlanChecklist oldChecklist = inspectionPlanChecklistRepository.findOne(planChecklist.getId());

        PQMInspectionPlanChecklist existChecklist = null;
        if (planChecklist.getType().equals("SECTION")) {
            existChecklist = inspectionPlanChecklistRepository.findByInspectionPlanAndTitleAndParentIsNull(planId, planChecklist.getTitle());
            if (existChecklist != null && !existChecklist.getId().equals(planChecklist.getId())) {
                throw new CassiniException(messageSource.getMessage(planChecklist.getTitle() + " : " + "section_already_exist",
                        null, planChecklist.getTitle() + " : Section already exist", LocaleContextHolder.getLocale()));
            }
        } else {
            existChecklist = inspectionPlanChecklistRepository.findByParentAndTitle(planChecklist.getParent(), planChecklist.getTitle());
            if (existChecklist != null && !existChecklist.getId().equals(planChecklist.getId())) {
                throw new CassiniException(messageSource.getMessage(planChecklist.getTitle() + " : " + "checklist_already_exist",
                        null, planChecklist.getTitle() + " : Checklist already exist", LocaleContextHolder.getLocale()));
            }
        }
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(planId);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.InspectionPlanChecklistUpdatedEvent(inspectionPlanRevision, oldChecklist, planChecklist));
        planChecklist = inspectionPlanChecklistRepository.save(planChecklist);
        return planChecklist;
    }

    @Transactional
    public void deleteInspectionPlanChecklist(Integer planId, Integer checklistId) {

        PQMInspectionPlanChecklist checklist = inspectionPlanChecklistRepository.findOne(checklistId);
        if (checklist != null && checklist.getType().equals("SECTION")) {
            List<PQMInspectionPlanChecklist> planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(planId);
            for (PQMInspectionPlanChecklist planChecklist : planChecklists) {
                if (planChecklist.getSeq() > checklist.getSeq()) {
                    planChecklist.setSeq(planChecklist.getSeq() - 1);
                    planChecklist = inspectionPlanChecklistRepository.save(planChecklist);
                }
            }
        } else if (checklist != null && checklist.getType().equals("CHECKLIST")) {
            List<PQMInspectionPlanChecklist> planChecklists = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(checklist.getParent());
            for (PQMInspectionPlanChecklist planChecklist : planChecklists) {
                if (planChecklist.getSeq() > checklist.getSeq()) {
                    planChecklist.setSeq(planChecklist.getSeq() - 1);
                    planChecklist = inspectionPlanChecklistRepository.save(planChecklist);
                }
            }
        }
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(planId);
        inspectionPlanChecklistRepository.delete(checklistId);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanChecklistDeleteEvent(inspectionPlanRevision, checklist));
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanChecklist> getInspectionPlanChecklists(Integer planId) {
        List<PQMInspectionPlanChecklist> planChecklists = new LinkedList<>();
        planChecklists = inspectionPlanChecklistRepository.findByInspectionPlanAndParentIsNullOrderBySeqAsc(planId);
        planChecklists.forEach(planChecklist -> {
            planChecklist.setChildren(inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(planChecklist.getId()));

            for (PQMInspectionPlanChecklist checklist : planChecklist.getChildren()) {
                checklist.setParamsCount(inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(checklist.getId()).size());
                checklist.setAttachmentCount(mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getId()).size());
                checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getId(), ObjectType.ATTACHMENT).size());
            }
        });
        return planChecklists;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanChecklist> getInspectionPlanChecklistChildren(Integer planId, Integer checklistId) {
        List<PQMInspectionPlanChecklist> planChecklists = new ArrayList<>();
        planChecklists = inspectionPlanChecklistRepository.findByParentOrderBySeqAsc(checklistId);

        for (PQMInspectionPlanChecklist checklist : planChecklists) {
            checklist.setParamsCount(inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(checklist.getId()).size());
            checklist.setAttachmentCount(mediaRepository.findByObjectIdOrderByCreatedDateDesc(checklist.getId()).size());
            checklist.setAttachmentCount(checklist.getAttachmentCount() + attachmentRepository.findByObjectIdAndObjectTypeOrderByAddedOnAsc(checklist.getId(), ObjectType.ATTACHMENT).size());
        }
        return planChecklists;
    }

    @Transactional(readOnly = true)
    public List<PQMInspectionPlanChecklistParameter> getChecklistParams(Integer id, Integer checklist) {
        List<PQMInspectionPlanChecklistParameter> checklistParameters = inspectionPlanChecklistParameterRepository.findByChecklistOrderByIdAsc(checklist);
        return checklistParameters;
    }

    @Transactional
    public PQMInspectionPlanChecklistParameter createChecklistParams(Integer planId, Integer checklistId, PQMInspectionPlanChecklistParameter parameter) {
        PQMParamValue paramValue = paramValueRepository.save(parameter.getExpectedValue());
        parameter.setExpectedValue(paramValue);
        parameter = inspectionPlanChecklistParameterRepository.save(parameter);
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(planId);
        PQMInspectionPlanChecklist planChecklist = inspectionPlanChecklistRepository.findOne(checklistId);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanChecklistParameterAddEvent(inspectionPlanRevision, planChecklist, parameter));
        return parameter;
    }

    @Transactional
    public PQMInspectionPlanChecklistParameter updateChecklistParams(Integer planId, Integer checklistId, PQMInspectionPlanChecklistParameter parameter) {
        PQMParamValue paramValue = paramValueRepository.save(parameter.getExpectedValue());
        return inspectionPlanChecklistParameterRepository.save(parameter);
    }

    @Transactional
    public void deleteChecklistParams(Integer planId, Integer checklistId, Integer parameterId) {
        PQMInspectionPlanChecklistParameter parameter = inspectionPlanChecklistParameterRepository.findOne(parameterId);
        PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(planId);
        PQMInspectionPlanChecklist planChecklist = inspectionPlanChecklistRepository.findOne(checklistId);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanChecklistParameterDeleteEvent(inspectionPlanRevision, planChecklist, parameter));

        inspectionPlanChecklistParameterRepository.delete(parameterId);
        if (parameter.getExpectedValue() != null) {
            PQMParamValue paramValue = paramValueRepository.findOne(parameter.getExpectedValue().getId());
            paramValueRepository.delete(paramValue.getId());
        }
    }

    @Transactional
    public ObjectFileDto uploadAttributeAttachments(Integer objectId, Integer checklistId, MultipartHttpServletRequest request) throws Exception {
        Map<String, MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        ObjectFileDto qualityFileDto = new ObjectFileDto();
        if (files.size() > 0) {
            for (MultipartFile file : files) {

                String[] fileType = file.getContentType().split("/");
                if (fileType[0].equals("image") || fileType[0].equals("video")) {
                    String fname = file.getOriginalFilename();
                    Media existAttachment = mediaRepository.findByObjectIdAndFileName(checklistId, fname);
                    if (existAttachment == null) {
                        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                        try {
                            byte[] picData = file.getBytes();

                            Media media = new Media();
                            media.setFileName(fname);
                            media.setData(picData);
                            media.setExtension(extension);
                            media.setObjectId(checklistId);

                            if (fileType[0].equals("image")) {
                                media.setMediaType(MediaType.IMAGE);
                            } else {
                                media.setMediaType(MediaType.VIDEO);
                            }

                            media = mediaRepository.save(media);
                            qualityFileDto.getChecklistImages().add(media);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    String fname = file.getOriginalFilename();
                    Attachment existAttachment = attachmentRepository.findByObjectIdAndName(checklistId, fname);
                    if (existAttachment == null) {
                        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                        Attachment attachment = new Attachment();
                        attachment.setAddedBy(sessionWrapper.getSession().getLogin().getPerson().getId());
                        attachment.setAddedOn(new Date());
                        attachment.setName(fname);

                        int index = fname.lastIndexOf('.');
                        if (index != -1) {
                            String ext = fname.substring(index);
                            ext = ext.toLowerCase();
                            attachment.setExtension(ext);
                        }

                        attachment.setSize(new Long(file.getSize()).intValue());
                        attachment.setObjectId(checklistId);
                        attachment.setObjectType(ObjectType.ATTACHMENT);

                        attachment = attachmentRepository.save(attachment);

                        saveAttachmentToDisk(attachment, file);
                        qualityFileDto.getChecklistAttachments().add(attachment);
                    }
                }
            }
        }
        return qualityFileDto;
    }


    private void saveAttachmentToDisk(Attachment attachment,
                                      MultipartFile multipartFile) {
        try {
            File file = getAttachmentFile(attachment.getObjectId(), attachment);
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(
                    file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public File getAttachmentFile(Integer objectId, Attachment attachment) {
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "attachments" + File.separator + objectId;
        File attachmentsFolder = new File(path);
        if (!attachmentsFolder.exists()) {
            attachmentsFolder.mkdirs();
        }
        return new File(attachmentsFolder, Integer.toString(attachment.getId()));
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getPlanDetailsCount(Integer planId) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setChecklists(inspectionPlanChecklistRepository.findByInspectionPlanAndType(planId, "CHECKLIST").size());
        detailsDto.setSections(inspectionPlanChecklistRepository.findByInspectionPlanAndType(planId, "SECTION").size());
        detailsDto.setItemFiles(inspectionPlanFileRepository.findByInspectionPlanAndFileTypeAndLatestTrueOrderByModifiedDateDesc(planId, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(planId));
        return detailsDto;
    }

    public Page<PQMInspection> getInspectionPlanTasks(Integer personId, PageRequest pageRequest) {
        List<PQMInspectionChecklist> inspectionChecklists = inspectionChecklistRepository.findByAssignedToAndResult(personId, ChecklistResult.NONE);
        List<PQMInspection> inspections = new ArrayList<>();
        pageRequest.setSize(pageRequest.getSize() + inspectionChecklists.size());
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        inspectionChecklists.forEach(pqmInspectionChecklist -> {
            inspections.add(inspectionRepository.findOne(pqmInspectionChecklist.getInspection()));
        });
        Page<PQMInspection> inspectionPage = new PageImpl<PQMInspection>(inspections, pageable, inspections.size());
        return inspectionPage;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void inspectionPlanWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowStartedEvent(revision, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void inspectionPlanWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        revision.setStatus(fromStatus.getName());
        revision.setStatusType(fromStatus.getType());
        if (revision.getStatusType().equals(WorkflowStatusType.RELEASED)) {
            revision.setReleased(true);
            revision.setReleasedDate(new Date());
        } else if (revision.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            revision.setRejected(true);
            revision.setReleasedDate(new Date());
        }
        updatePlanRevisionHistory(revision);
        inspectionPlanRevisionRepository.save(revision);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowPromotedEvent(revision, plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void inspectionPlanWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        revision.setStatus(fromStatus.getName());
        revision.setStatusType(fromStatus.getType());
        PLMWorkflowStatus toStatus = event.getToStatus();

        revision.setStatus(toStatus.getName());
        revision.setStatusType(toStatus.getType());
        updatePlanRevisionHistory(revision);
        inspectionPlanRevisionRepository.save(revision);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowDemotedEvent(revision, plmWorkflow, fromStatus, toStatus));
    }

    public List<String> getStatus(){
        return inspectionPlanRevisionRepository.getStatus();
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCycle> getInspectionTypeLifecycles() {
        List<PLMLifeCycle> plmLifeCycles = inspectionPlanTypeRepository.getUniqueInspectionTypeLifeCycles();
        return plmLifeCycles;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void inspectionPlanWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        revision.setStatus(fromStatus.getName());
        revision.setStatusType(fromStatus.getType());
        if (revision.getStatusType().equals(WorkflowStatusType.RELEASED)) {
            revision.setReleased(true);
            revision.setReleasedDate(new Date());
        } else if (revision.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            revision.setRejected(true);
            revision.setReleasedDate(new Date());
        }
        updatePlanRevisionHistory(revision);
        inspectionPlanRevisionRepository.save(revision);
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowFinishedEvent(revision, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void planWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowHoldEvent(revision, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'INSPECTIONPLANREVISION'")
    public void planWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMInspectionPlanRevision revision = (PQMInspectionPlanRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowUnholdEvent(revision, plmWorkflow, fromStatus));
    }

    private void updatePlanRevisionHistory(PQMInspectionPlanRevision revision) {
        if (revision.getStatusType().equals(WorkflowStatusType.RELEASED) || revision.getStatusType().equals(WorkflowStatusType.REJECTED)) {
            PQMInspectionPlanHistory inspectionPlanHistory = new PQMInspectionPlanHistory();
            List<PQMInspectionPlanHistory> inspectionPlanHistories = inspectionPlanHistoryRepository.findByInspectionPlanOrderByTimestampDesc(revision.getId());
            if (inspectionPlanHistories.size() > 0) {
                inspectionPlanHistory.setOldStatus(inspectionPlanHistories.get(0).getNewStatus());
            }
            PQMInspectionPlan inspectionPlan = inspectionPlanRepository.findOne(revision.getPlan().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = new ArrayList<>();
            if (inspectionPlan.getObjectType().equals(ObjectType.valueOf(PLMObjectType.PRODUCTINSPECTIONPLAN.toString()))) {
                PQMProductInspectionPlan productInspectionPlan = productInspectionPlanRepository.findOne(revision.getPlan().getId());
                PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(productInspectionPlan.getPlanType().getLifecycle().getId());
                if (revision.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                    lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseType(lifeCycle.getId(), LifeCyclePhaseType.valueOf(revision.getStatusType().toString()));
                }
            } else {
                PQMMaterialInspectionPlan materialInspectionPlan = materialInspectionPlanRepository.findOne(revision.getPlan().getId());
                PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(materialInspectionPlan.getPlanType().getLifecycle().getId());
                if (revision.getStatusType().equals(WorkflowStatusType.RELEASED)) {
                    lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseType(lifeCycle.getId(), LifeCyclePhaseType.valueOf(revision.getStatusType().toString()));
                }
            }

            if (lifeCyclePhases.size() > 0) {
                revision.setLifeCyclePhase(lifeCyclePhases.get(0));
                inspectionPlanHistory.setNewStatus(revision.getLifeCyclePhase());
                inspectionPlanHistory.setInspectionPlan(revision.getId());
                inspectionPlanHistory.setTimestamp(new Date());
                inspectionPlanHistory.setUpdatedBy(revision.getCreatedBy());
                inspectionPlanHistory = inspectionPlanHistoryRepository.save(inspectionPlanHistory);
            }

        }
    }
}
