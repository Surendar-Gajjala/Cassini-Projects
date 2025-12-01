package com.cassinisys.plm.service.req;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.service.wfm.WorkFlowService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.RequirementDocumentEvents;
import com.cassinisys.plm.event.RequirementEvents;
import com.cassinisys.plm.event.RequirementWorkflowEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.RequirementDocumentsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.req.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.plm.ItemServiceException;
import com.cassinisys.plm.service.tm.UserTaskEvents;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.apache.commons.collections4.IteratorUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by GSR on 09-06-2020.
 */
@Service
public class ReqDocumentService implements CrudService<PLMRequirementDocument, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMRequirementDocumentRepository requirementDocumentRepository;
    @Autowired
    private PLMRequirementObjectRepository requirementObjectRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementObjectAttributeRepository requirementObjectAttributeRepository;
    @Autowired
    private RequirementDocumentPredicateBuilder reqDocumentPredicateBuilder;
    @Autowired
    private PLMRequirementDocumentRevisionRepository requirementDocumentRevisionRepository;
    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMRequirementRepository requirementRepository;
    @Autowired
    private PLMRequirementVersionRepository requirementVersionRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;
    @Autowired
    private PLMRequirementReviewerRepository requirementReviewerRepository;
    @Autowired
    private PLMRequirementDocumentReviewerRepository requirementDocumentReviewerRepository;
    @Autowired
    private RequirementPredicateBuilder requirementPredicateBuilder;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMRequirementTemplateRepository requirementTemplateRepository;
    @Autowired
    private PLMRequirementDocumentTemplateRepository requirementDocumentTemplateRepository;
    @Autowired
    private PLMRequirementDocumentTemplateReviewerRepository requirementDocumentTemplateReviewerRepository;
    @Autowired
    private PLMRequirementTemplateReviewerRepository requirementTemplateReviewerRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private PLMRequirementItemRepository requirementItemRepository;
    @Autowired
    private RequirementDocumentRevisionStatusHistoryRepository requirementDocumentRevisionStatusHistoryRepository;
    @Autowired
    private RequirementVersionStatusHistoryRepository requirementVersionStatusHistoryRepository;
    @Autowired
    private RequirementDocumentChildrenRepository requirementDocumentChildrenRepository;
    @Autowired
    private RequirementDocumentChildrenPredicateBuilder requirementDocumentChildrenPredicateBuilder;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#reqDocument,'create')")
    public PLMRequirementDocument create(PLMRequirementDocument reqDocument) {
        List<PLMRequirementObjectAttribute> requirementObjectAttributes = reqDocument.getRequirementObjectAttributes();
        List<ObjectAttribute> objectAttributes = reqDocument.getObjectAttributes();
        Integer workflowDef = null;
        if (reqDocument.getWorkflowDefId() != null) {
            workflowDef = reqDocument.getWorkflowDefId();
        }
        Integer person = reqDocument.getPerson();
        PLMRequirementDocument existSubstance = requirementDocumentRepository.findByNumber(reqDocument.getNumber());
        if (existSubstance != null) {
            String message = messageSource.getMessage("reqDocument_number_already_exists", null, "{0} Requirement Document number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existSubstance.getNumber());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(reqDocument.getType().getAutoNumberSource().getId(), reqDocument.getNumber());
        reqDocument = requirementDocumentRepository.save(reqDocument);

        PLMRequirementDocumentRevision revision = new PLMRequirementDocumentRevision();
        revision.setMaster(reqDocument);
        revision.setName(reqDocument.getName());
        revision.setLatest(true);
        PLMRequirementDocumentType documentType = requirementDocumentTypeRepository.findOne(reqDocument.getType().getId());
        Lov lov = documentType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(documentType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setLifeCyclePhase(lifeCyclePhase);
        revision.setRevision(lov.getValues()[0]);
        revision.setOwner(personRepository.findOne(person));
        revision = requirementDocumentRevisionRepository.save(revision);
        reqDocument.setLatestRevision(revision.getId());
        requirementDocumentRepository.save(reqDocument);

        if (reqDocument.getTemplate() != null) {
            copyDocumentTemplate(revision);
        }

        if (workflowDef != null) {
            attachReqDoctWorkflow(revision.getId(), workflowDef);
        }

        PLMRequirementDocumentRevisionStatusHistory statusHistory = new PLMRequirementDocumentRevisionStatusHistory();
        statusHistory.setDocumentRevision(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(revision.getCreatedBy());
        statusHistory = requirementDocumentRevisionStatusHistoryRepository.save(statusHistory);

        for (PLMRequirementObjectAttribute attribute : requirementObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(revision.getId(), attribute.getId().getAttributeDef()));
                requirementObjectAttributeRepository.save(attribute);
            }
        }
        dcoService.saveObjectAttributes(objectAttributes, reqDocument);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentCreatedEvent(reqDocument, revision));
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentRevisionCreatedEvent(reqDocument, revision));
        return reqDocument;
    }

    @Transactional
    public PLMWorkflow attachReqDoctWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMRequirementDocumentRevision reqDocRevision = requirementDocumentRevisionRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (reqDocRevision != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(RequirementEnumObject.REQUIREMENTDOCUMENTREVISION, reqDocRevision.getId(), wfDef);
            reqDocRevision.setWorkflow(workflow.getId());
            requirementDocumentRevisionRepository.save(reqDocRevision);
        }
        return workflow;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#reqDocument.id ,'edit')")
    public PLMRequirementDocument update(PLMRequirementDocument reqDocument) {
        PLMRequirementDocument oldreq = JsonUtils.cloneEntity(requirementDocumentRepository.findOne(reqDocument.getId()), PLMRequirementDocument.class);
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(reqDocument.getLatestRevision());
        revision.setName(reqDocument.getName());
        revision.setDescription(reqDocument.getDescription());
        reqDocument = requirementDocumentRepository.save(reqDocument);
        revision = requirementDocumentRevisionRepository.save(revision);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentBasicInfoUpdatedEvent(oldreq, reqDocument, revision));
        return reqDocument;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMRequirementDocument requirementDocument = requirementDocumentRepository.findOne(id);
        List<Integer> requirementDocumentRevisions = requirementDocumentRevisionRepository.getRevisionIdsByReqDocId(requirementDocument.getId());
        for (Integer requirementDocumentRevision : requirementDocumentRevisions) {
            requirementObjectRepository.delete(requirementDocumentRevision);
        }
        requirementDocumentRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMRequirementDocument get(Integer id) {
        PLMRequirementDocument reqDocument = requirementDocumentRepository.findOne(id);
        return reqDocument;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMRequirementDocument> getAll() {
        return requirementDocumentRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMRequirementDocument> findMultiple(List<Integer> ids) {
        return requirementDocumentRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<RequirementDocumentsDto> getAllRequirementDocuments(Pageable pageable, RequirementDocumentCriteria reqDocumentCriteria) {
        Predicate predicate = reqDocumentPredicateBuilder.build(reqDocumentCriteria, QPLMRequirementDocument.pLMRequirementDocument);
        Page<PLMRequirementDocument> reqDocuments = requirementDocumentRepository.findAll(predicate, pageable);
        List<RequirementDocumentsDto> requirementDocuments = new ArrayList<>();
        reqDocuments.getContent().stream().forEach(document -> {
            RequirementDocumentsDto itemDto = convertToItemsDto(document);
            itemDto.setTagsCount(document.getTags().size());
            requirementDocuments.add(itemDto);
        });
        return new PageImpl<RequirementDocumentsDto>(requirementDocuments, pageable, reqDocuments.getTotalElements());
    }

    private RequirementDocumentsDto convertToItemsDto(PLMRequirementDocument document) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(document.getLatestRevision());
        RequirementDocumentsDto requirementDocumentsDto = new RequirementDocumentsDto();
        requirementDocumentsDto.setId(document.getId());
        requirementDocumentsDto.setName(document.getName());
        requirementDocumentsDto.setNumber(document.getNumber());
        requirementDocumentsDto.setType(document.getType().getName());
        requirementDocumentsDto.setDescription(document.getDescription());
        requirementDocumentsDto.setRevision(revision);
        requirementDocumentsDto.setLatestRevision(document.getLatestRevision());
        requirementDocumentsDto.setLifeCyclePhase(revision.getLifeCyclePhase());
        requirementDocumentsDto.setCreatedByName(personRepository.findOne(document.getCreatedBy()).getFullName());
        requirementDocumentsDto.setModifiedByName(personRepository.findOne(document.getModifiedBy()).getFullName());
        requirementDocumentsDto.setOwner(revision.getOwner().getFullName());
        requirementDocumentsDto.setModifiedDate(document.getModifiedDate());
        requirementDocumentsDto.setCreatedDate(document.getCreatedDate());
        return requirementDocumentsDto;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMRequirementDocument> getAllReqDocuments(Pageable pageable, RequirementDocumentCriteria reqDocumentCriteria) {
        Predicate predicate = reqDocumentPredicateBuilder.build(reqDocumentCriteria, QPLMRequirementDocument.pLMRequirementDocument);
        return requirementDocumentRepository.findAll(predicate, pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMRequirementDocument> getReqDocObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMRequirementDocumentType type = requirementDocumentTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return requirementDocumentRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMRequirementDocumentType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMRequirementDocumentType> children = requirementDocumentTypeRepository.findByParentOrderByIdAsc(type.getId());
            for (PLMRequirementDocumentType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentRevision> getDocumentRevisionsByIds(List<Integer> ids) {
        return requirementDocumentRevisionRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<Integer> getReqDocRevisionIds(Integer reqDocId) {
        PLMRequirementDocumentRevision docRevision = requirementDocumentRevisionRepository.findOne(reqDocId);
        return requirementDocumentRevisionRepository.getRevisionIdsByReqDocId(docRevision.getMaster().getId());
    }

    @Transactional(readOnly = true)
    public PLMRequirementDocumentRevision getReqDocumentRevision(Integer revisionId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(revisionId);
        PLMRequirementDocumentReviewer reviewer = requirementDocumentReviewerRepository.findByRequirementDocumentRevisionAndReviewer(revision.getId(), person.getId());
        List<PLMRequirementDocumentChildren> requirementDocumentChildrens = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersionLatestTrue(revision.getId());
        for (PLMRequirementDocumentChildren requirement : requirementDocumentChildrens) {
            if (!requirement.getRequirementVersion().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                revision.setRequirementApproveButton(true);
                break;
            }
        }
        if (reviewer != null) {
            revision.setReviewer(reviewer);
        }
            //Adding workflow relavent settings
            WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(revision.getId());
            revision.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
            revision.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
            revision.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
            revision.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        
        return revision;
    }

    @Transactional
    public PLMRequirementDocumentRevision updateDocumentRevision(PLMRequirementDocumentRevision reqDocumentRevision) {
        PLMRequirementDocumentRevision oldreq = JsonUtils.cloneEntity(requirementDocumentRevisionRepository.findOne(reqDocumentRevision.getId()), PLMRequirementDocumentRevision.class);
        reqDocumentRevision = requirementDocumentRevisionRepository.save(reqDocumentRevision);
        return reqDocumentRevision;
    }


    /*-------------------------- requirement ---------------------------------*/

    @Transactional
    public PLMRequirementDocumentChildren createRequirement(PLMRequirementVersion requirementVersion) {
        PLMRequirement requirementObject = requirementVersion.getMaster();
        Integer workflowDef = null;
        if (requirementVersion.getWorkflowDefId() != null) {
            workflowDef = requirementVersion.getWorkflowDefId();
        }
        List<PLMRequirementObjectAttribute> requirementObjectAttributes = requirementVersion.getRequirementObjectAttributes();
        List<ObjectAttribute> objectAttributes = requirementVersion.getObjectAttributes();
        PLMRequirement existRequirement = requirementRepository.findByNumber(requirementObject.getNumber());
        if (existRequirement != null) {
            String message = messageSource.getMessage("requirement_number_already_exists", null, "{0} Requirement Number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existRequirement.getNumber());
            throw new CassiniException(result);
        }
        PLMRequirementType type = requirementTypeRepository.findOne(requirementObject.getType().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(type.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        requirementObject = createReqSequence(requirementVersion, requirementObject);
        autoNumberService.saveNextNumber(requirementObject.getType().getAutoNumberSource().getId(), requirementObject.getNumber());
        PLMRequirement requirement = requirementRepository.save(requirementObject);

        PLMRequirementVersion version = new PLMRequirementVersion();
        version.setMaster(requirement);
        version.setName(requirement.getName());
        version.setDescription(requirement.getDescription());
        version.setAssignedTo(requirementVersion.getAssignedTo());
        version.setParent(requirementVersion.getParent());
        version.setLatest(true);
        version.setVersion(1);
        version.setLifeCyclePhase(lifeCyclePhase);
        version.setPriority(requirementVersion.getPriority());
        version.setRequirementDocumentRevision(requirementVersion.getRequirementDocumentRevision());
        version.setPlannedFinishDate(requirementVersion.getPlannedFinishDate());
        version = requirementVersionRepository.save(version);
        requirement.setLatestVersion(version.getId());
        requirement = requirementRepository.save(requirement);

        PLMRequirementDocumentChildren documentChildren = new PLMRequirementDocumentChildren();
        documentChildren.setDocument(version.getRequirementDocumentRevision().getId());
        documentChildren.setRequirementVersion(version);
        documentChildren.setParent(version.getParent());
        documentChildren = requirementDocumentChildrenRepository.save(documentChildren);

        if (workflowDef != null) {
            attachRequirementWorkflow(documentChildren.getId(), workflowDef);
        }

        createReqVersionStatusHistory(version, version.getLifeCyclePhase());

        for (PLMRequirementObjectAttribute attribute : requirementObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(version.getId(), attribute.getId().getAttributeDef()));
                requirementObjectAttributeRepository.save(attribute);
            }
        }
        dcoService.saveObjectAttributes(objectAttributes, requirement);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementCreatedEvent(requirement, documentChildren, version));
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementCreatedEvent(requirement, version.getRequirementDocumentRevision()));
        return documentChildren;
    }

    @Transactional
    public PLMWorkflow attachRequirementWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (documentChildren != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.REQUIREMENT, documentChildren.getId(), wfDef);
            documentChildren.setWorkflow(workflow.getId());
            requirementDocumentChildrenRepository.save(documentChildren);
        }
        return workflow;
    }

    private PLMRequirement createReqSequence(PLMRequirementVersion version, PLMRequirement requirement) {
        if (version.getParent() != null) {
            List<PLMRequirementVersion> childrens = requirementVersionRepository.findByParent(version.getParent());
            PLMRequirementVersion parentReq = requirementVersionRepository.findOne(version.getParent());
            Integer seq = childrens.size() + 1;
            requirement.setSequence(seq);
            requirement.setPath(parentReq.getMaster().getPath() + "." + requirement.getSequence());
        } else {
            List<PLMRequirementVersion> requirements = requirementVersionRepository.findByRequirementDocumentRevisionAndParentIsNull(version.getRequirementDocumentRevision());
            Integer seq = requirements.size() + 1;
            requirement.setSequence(seq);
            requirement.setPath(seq.toString());
        }

        return requirement;
    }


    @Transactional
    public PLMRequirement updateRequirement(PLMRequirement requirement) {
        return requirementRepository.save(requirement);
    }

    @Transactional
    public PLMRequirementVersion updateRequirementVersion(Integer id, PLMRequirementVersion requirementVersion) {
        PLMRequirementVersion oldReqVersion = JsonUtils.cloneEntity(requirementVersionRepository.findOne(requirementVersion.getId()), PLMRequirementVersion.class);
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(id);
        requirementVersion.getMaster().setName(requirementVersion.getName());
        requirementVersion.getMaster().setDescription(requirementVersion.getDescription());
        PLMRequirement requirement = requirementRepository.save(requirementVersion.getMaster());
        PLMRequirementVersion reqVersion = requirementVersionRepository.save(requirementVersion);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementBasicInfoUpdatedEvent(null, null, documentChildren, oldReqVersion, reqVersion));
        return reqVersion;
    }

    @Transactional
    public void deleteRequirement(Integer id) {
        PLMRequirement req = requirementRepository.findOne(id);
        PLMRequirementVersion version = requirementVersionRepository.findOne(req.getLatestVersion());
        updateSiblingSequences(version);
        requirementRepository.delete(id);
        requirementObjectRepository.delete(id);
    }

    private void updateSiblingSequences(PLMRequirementVersion req) {
        List<PLMRequirementVersion> children;
        if (req.getParent() == null) {
            children = requirementVersionRepository.findByRequirementDocumentRevisionAndParentIsNull(req.getRequirementDocumentRevision());
        } else {
            children = requirementVersionRepository.findByParent(req.getParent());
        }

        if (children == null) return;

        for (int i = 1; i < children.size(); i++) {
            PLMRequirementVersion child = children.get(i);

            if (child.getId().equals(req.getId())) continue;

            child.getMaster().setSequence(i);

            if (child.getParent() != null) {
                PLMRequirementVersion parent = requirementVersionRepository.findOne(child.getParent());
                child.getMaster().setPath(parent.getMaster().getPath() + "." + child.getMaster().getSequence());
            } else {
                child.getMaster().setPath("" + child.getMaster().getSequence());
            }
            requirementRepository.save(child.getMaster());
            updateChildrenPaths(child);
        }
    }

    private void updateChildrenPaths(PLMRequirementVersion req) {
        List<PLMRequirementVersion> children = requirementVersionRepository.findByParent(req.getId());

        if (children != null && children.size() > 0) {
            children.forEach(child -> {
                child.getMaster().setPath(req.getMaster().getPath() + "." + child.getMaster().getSequence());
                requirementRepository.save(child.getMaster());
                updateChildrenPaths(child);
            });
        }
    }

    @Transactional(readOnly = true)
    public PLMRequirement getRequirement(Integer id) {
        PLMRequirement reqDocument = requirementRepository.findOne(id);
        return reqDocument;
    }


    @Transactional(readOnly = true)
    public List<PLMRequirement> findMultipleRequirement(List<Integer> ids) {
        return requirementRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementVersion> getRequirementTree(Integer revisionId) {
        List<PLMRequirementVersion> versions = requirementVersionRepository.getVersionByDocumentAndParentIsNullAndLatestTrue(revisionId);
        for (PLMRequirementVersion version : versions) {
            List<PLMRequirementReviewer> requirementReviewers = requirementReviewerRepository.findByRequirementVersionAndStatus(version.getMaster().getId(), RequirementApprovalStatus.REJECTED);
            if (requirementReviewers.size() > 0) {
                version.getMaster().setIgnoreReqBtn(true);
            }
            visitRequirementVersionChildren(version);
        }
        return versions;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementVersion> getRequirementVersionChildren(Integer id) {
        return requirementVersionRepository.getVersionByParent(id);
    }

    private void visitRequirementVersionChildren(PLMRequirementVersion version) {
        List<PLMRequirementVersion> childrens = getRequirementVersionChildren(version.getId());
        for (PLMRequirementVersion child : childrens) {
            visitRequirementVersionChildren(child);
        }
        version.setChildren(childrens);
    }

    @Transactional(readOnly = true)
    public PLMRequirementVersion getRequirementVersion(Integer id) {
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementReviewer reviewer = requirementReviewerRepository.findByRequirementVersionAndReviewer(version.getId(), person.getId());
        if (reviewer != null) {
            version.setReviewer(reviewer);
        }
        return version;
    }

    @Transactional(readOnly = true)
    public RequirementDto getRequirementVersionDetails(Integer id) {
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementReviewer reviewer = requirementReviewerRepository.findByRequirementVersionAndReviewerAndApproverTrue(version.getMaster().getId(), person.getId());
        if (reviewer != null) {
            version.setReviewer(reviewer);
        }

        RequirementDto requirementDto = new RequirementDto();
        requirementDto.setId(version.getMaster().getId());
        requirementDto.setLatestVersion(id);
        requirementDto.setName(version.getName());
        requirementDto.setDescription(version.getDescription());
        requirementDto.setNumber(version.getMaster().getNumber());
        requirementDto.setType(version.getMaster().getType().getName());
        requirementDto.setAssignedToName(personRepository.findOne(version.getAssignedTo()).getFullName());
        requirementDto.setCreatedByPerson(personRepository.findOne(version.getCreatedBy()).getFullName());
        requirementDto.setModifiedByPerson(personRepository.findOne(version.getModifiedBy()).getFullName());
        requirementDto.setPlannedFinishDate(version.getPlannedFinishDate());
        requirementDto.setPriority(version.getPriority());
        requirementDto.setVersion(version.getVersion());
        requirementDto.setLifeCyclePhase(version.getLifeCyclePhase());
        requirementDto.setCreatedDate(version.getCreatedDate());
        requirementDto.setModifiedDate(version.getModifiedDate());
        requirementDto.setReviewer(version.getReviewer());
        return requirementDto;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementVersion> getAllRequirements(RequirementCriteria requirementCriteria) {
        Predicate predicate = requirementPredicateBuilder.build(requirementCriteria, QPLMRequirementVersion.pLMRequirementVersion);
        return IteratorUtils.toList(requirementVersionRepository.findAll(predicate).iterator());
    }

    public PLMRequirementReviewer addReviewer(Integer requirement, PLMRequirementReviewer reviewer) {
        reviewer = requirementReviewerRepository.save(reviewer);
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirement);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reviewer.getReqDoc(), version);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerAddedEvent(version, children, reviewer));
        return reviewer;
    }

    public PLMRequirementReviewer updateReviewer(Integer requirement, PLMRequirementReviewer reviewer) {
        Integer reqDoc = reviewer.getReqDoc();
        reviewer = requirementReviewerRepository.save(reviewer);
        PLMRequirementVersion version = requirementVersionRepository.findOne(requirement);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDoc, version);
        applicationEventPublisher.publishEvent(new UserTaskEvents.RequirementAssignedEvent(version, reviewer));
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerUpdateEvent(version, children, reviewer));
        return reviewer;
    }

    public void deleteReviewer(Integer versionId, Integer reviewerId) {
        PLMRequirementReviewer requirementReviewer = requirementReviewerRepository.findOne(reviewerId);
        PLMRequirementVersion version = requirementVersionRepository.findOne(versionId);
        Integer reqDocId = null;
        if (requirementReviewer.getReqDoc() != null) {
            reqDocId = requirementReviewer.getReqDoc();
        } else {
            reqDocId = version.getRequirementDocumentRevision().getId();
        }
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDocId, version);
        applicationEventPublisher.publishEvent(new UserTaskEvents.RequirementDeletedEvent(requirementReviewer.getRequirementVersion(), requirementReviewer));
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerDeletedEvent(version, children, requirementReviewer));
        requirementReviewerRepository.delete(reviewerId);
    }


    @Transactional
    public PLMRequirementDocumentRevision submitReqDocument(Integer id) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        PLMLifeCyclePhase oldStatus = revision.getLifeCyclePhase();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(revision.getMaster().getType().getLifecycle().getId());
        Integer lifeCyclePhase = revision.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase1 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase1);
        if (index != -1) {
            index++;
            PLMLifeCyclePhase lcPhase = lifeCyclePhases.get(index);
            if (lcPhase != null) {
                revision.setLifeCyclePhase(lcPhase);
                revision = requirementDocumentRevisionRepository.save(revision);
            }
            PLMRequirementDocumentRevisionStatusHistory statusHistory = new PLMRequirementDocumentRevisionStatusHistory();
            statusHistory.setDocumentRevision(revision.getId());
            statusHistory.setOldStatus(oldStatus);
            statusHistory.setNewStatus(lcPhase);
            statusHistory.setUpdatedBy(revision.getCreatedBy());
            statusHistory = requirementDocumentRevisionStatusHistoryRepository.save(statusHistory);
        }

        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersionLatestTrue(revision.getId());
        childrens.forEach(reqChildren -> {
            if (!reqChildren.getRequirementVersion().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                PLMLifeCyclePhase oldPhase = reqChildren.getRequirementVersion().getLifeCyclePhase();
                PLMLifeCycle plmLifeCycle = lifeCycleRepository.findOne(reqChildren.getRequirementVersion().getMaster().getType().getLifecycle().getId());
                List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(plmLifeCycle.getId());
                Integer phase = reqChildren.getRequirementVersion().getLifeCyclePhase().getId();
                PLMLifeCyclePhase plmLifeCyclePhase = plmLifeCyclePhases.stream().
                        filter(p -> p.getId().toString().equals(phase.toString())).
                        findFirst().get();
                Integer index1 = plmLifeCyclePhases.indexOf(plmLifeCyclePhase);
                if (index1 != -1) {
                    index1++;
                    PLMLifeCyclePhase phase1 = plmLifeCyclePhases.get(index1);
                    if (phase1 != null) {
                        reqChildren.getRequirementVersion().setLifeCyclePhase(phase1);
                        requirementVersionRepository.save(reqChildren.getRequirementVersion());
                    }
                }
                createReqVersionStatusHistory(reqChildren.getRequirementVersion(), oldPhase);
            }

        });

        return revision;
    }

    @Transactional
    public PLMRequirementDocumentRevision releaseReqDocument(Integer id) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(revision.getId());
        if (notReleasedDocumentCount > 0) {
            String message = messageSource.getMessage("req_doc_has_unreleased_documents", null, "Requirement document has some unreleased documents", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", revision.getMaster().getNumber());
            throw new CassiniException(result);
        }
        List<PLMRequirementDocumentReviewer> reqDocApprovers = requirementDocumentReviewerRepository.findByRequirementDocumentRevisionAndApproverTrue(revision.getId());
        List<PLMRequirementDocumentReviewer> approverTrue = requirementDocumentReviewerRepository.findByRequirementDocumentRevisionAndStatusAndApproverTrue(revision.getId(), RequirementApprovalStatus.APPROVED);
        if (reqDocApprovers.size() != approverTrue.size()) {
            String message = messageSource.getMessage("req_doc_approves_not_approve_documents", null, "Requirement document approvers not approve document - {0}", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", revision.getMaster().getNumber());
            throw new CassiniException(result);
        }

        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersionLatestTrue(revision.getId());
        for (PLMRequirementDocumentChildren version : childrens) {
            if (!version.getRequirementVersion().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                PLMLifeCyclePhase oldPhase = version.getRequirementVersion().getLifeCyclePhase();
                List<PLMRequirementReviewer> reviewers = requirementReviewerRepository.findByRequirementVersionAndApproverFalse(version.getRequirementVersion().getId());
                List<PLMRequirementReviewer> approvers = requirementReviewerRepository.findByRequirementVersionAndApproverTrue(version.getRequirementVersion().getId());
                if (approvers.size() == 0) {
                    PLMLifeCycle plmLifeCycle = lifeCycleRepository.findOne(version.getRequirementVersion().getMaster().getType().getLifecycle().getId());
                    List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(plmLifeCycle.getId());
                    Integer phase = version.getRequirementVersion().getLifeCyclePhase().getId();
                    PLMLifeCyclePhase plmLifeCyclePhase = plmLifeCyclePhases.stream().
                            filter(p -> p.getId().toString().equals(phase.toString())).
                            findFirst().get();
                    Integer index1 = plmLifeCyclePhases.indexOf(plmLifeCyclePhase);
                    if (index1 != -1) {
                        index1++;
                        PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index1);
                        if (lifeCyclePhase != null) {
                            version.getRequirementVersion().setLifeCyclePhase(lifeCyclePhase);
                            requirementVersionRepository.save(version.getRequirementVersion());
                        }
                    }
                    createReqVersionStatusHistory(version.getRequirementVersion(), oldPhase);

                } else {
                    if (!version.getRequirementVersion().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED) /*&& plmRequirement.getIgnoreForRelease().equals(false)*/) {
                        String message = messageSource.getMessage("requirement_not_approved", null, "{0}- Requirement not approved ", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", version.getRequirementVersion().getMaster().getNumber());
                        throw new CassiniException(result);
                    }
                }
            }

            checkRequirementWorkflowRelease(version);

        }

        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(revision.getMaster().getType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = revision.getLifeCyclePhase().getId();
        PLMLifeCyclePhase oldStatus = revision.getLifeCyclePhase();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        if (index != -1) {
            index++;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                revision.setLifeCyclePhase(lifeCyclePhase);
                revision = requirementDocumentRevisionRepository.save(revision);
            }
            PLMRequirementDocumentRevisionStatusHistory statusHistory = new PLMRequirementDocumentRevisionStatusHistory();
            statusHistory.setDocumentRevision(revision.getId());
            statusHistory.setOldStatus(oldStatus);
            statusHistory.setNewStatus(revision.getLifeCyclePhase());
            statusHistory.setUpdatedBy(revision.getCreatedBy());
            statusHistory = requirementDocumentRevisionStatusHistoryRepository.save(statusHistory);
        }
        return revision;
    }


    public void checkRequirementWorkflowRelease(PLMRequirementDocumentChildren children) {
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(children.getId());
        if (workflow != null) {
            if (workflow.getFinish() != null) {
                PLMWorkflowStatus workflowStatus1 = plmWorkflowStatusRepository.findOne(workflow.getFinish().getId());
                if (workflowStatus1 != null && !workflowStatus1.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    String message = messageSource.getMessage("please_approve_the_workflow", null, "Please approve {0} requirement Workflow", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", children.getRequirementVersion().getMaster().getNumber());
                    throw new CassiniException(result);
                }
            }
        }
    }


    @Transactional
    public PLMRequirementReviewer approveRequirement(Integer id, PLMRequirementReviewer reviewer, Boolean approved) {
        Integer reqDoc = reviewer.getReqDoc();
        reviewer.setStatus(RequirementApprovalStatus.APPROVED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        requirementReviewerRepository.save(reviewer);
        PLMLifeCyclePhase oldPhase = version.getLifeCyclePhase();
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDoc, version);
        List<PLMRequirementReviewer> approvers = requirementReviewerRepository.findByRequirementVersionAndApproverTrue(reviewer.getRequirementVersion());
        List<PLMRequirementReviewer> approverTrue = requirementReviewerRepository.findByRequirementVersionAndStatusAndApproverTrue(reviewer.getRequirementVersion(), RequirementApprovalStatus.APPROVED);
        checkRequirementWorkflowRelease(children);
        if (approvers.size() == approverTrue.size()) {
            PLMRequirementVersion requirement = requirementVersionRepository.findOne(reviewer.getRequirementVersion());
            PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(requirement.getMaster().getType().getLifecycle().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
            Integer lifeCyclePhase1 = requirement.getLifeCyclePhase().getId();
            PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                    filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                    findFirst().get();
            Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
            if (index != -1) {
                index++;
                PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                if (lifeCyclePhase != null) {
                    requirement.setLifeCyclePhase(lifeCyclePhase);
                    requirementVersionRepository.save(requirement);
                }
            }
            if (approved) {
                PLMRequirementVersionStatusHistory statusHistory = new PLMRequirementVersionStatusHistory();
                statusHistory.setRequirementVersion(version.getId());
                statusHistory.setOldStatus(oldPhase);
                statusHistory.setNewStatus(requirement.getLifeCyclePhase());
                statusHistory.setUpdatedBy(version.getCreatedBy());
                statusHistory = requirementVersionStatusHistoryRepository.save(statusHistory);
            }
            createReqVersionStatusHistory(version, oldPhase);
        }
        //PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDoc, version);
        applicationEventPublisher.publishEvent(new UserTaskEvents.RequirementFinishedEvent(reviewer.getRequirementVersion(), reviewer));
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerApprovedEvent(version, children, reviewer));
        return reviewer;
    }

    @Transactional
    public PLMRequirementReviewer rejectRequirement(Integer id, PLMRequirementReviewer reviewer) {
        Integer reqDoc = reviewer.getReqDoc();
        reviewer.setStatus(RequirementApprovalStatus.REJECTED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDoc, version);
        applicationEventPublisher.publishEvent(new UserTaskEvents.RequirementFinishedEvent(reviewer.getRequirementVersion(), reviewer));
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerApprovedEvent(version, children, reviewer));
        return requirementReviewerRepository.save(reviewer);
    }

    @Transactional
    public PLMRequirementReviewer reviewRequirement(Integer id, PLMRequirementReviewer reviewer) {
        Integer reqDoc = reviewer.getReqDoc();
        reviewer.setStatus(RequirementApprovalStatus.REVIEWED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(reqDoc, version);
        applicationEventPublisher.publishEvent(new UserTaskEvents.RequirementFinishedEvent(reviewer.getRequirementVersion(), reviewer));
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementReviewerApprovedEvent(version, children, reviewer));
        return requirementReviewerRepository.save(reviewer);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementReviewer> getReviewers(Integer reqId) {
        PLMRequirementDocumentChildren version = requirementDocumentChildrenRepository.findOne(reqId);
        List<PLMRequirementReviewer> reviewers = requirementReviewerRepository.findByRequirementVersion(version.getRequirementVersion().getId());
        reviewers.forEach(reviewer -> {
            reviewer.setReviewerName(personRepository.findOne(reviewer.getReviewer()).getFullName());
        });
        return reviewers;
    }

    @Transactional
    public Page<PLMRequirement> getRequirements(Pageable pageable) {
        Page<PLMRequirement> requirements = requirementRepository.findAll(pageable);
        return requirements;
    }

    @Transactional(readOnly = true)
    public Page<PLMRequirement> freeTextSearch(Pageable pageable, RequirementCriteria criteria) {
        Predicate predicate = requirementPredicateBuilder.getPredicate(criteria, QPLMRequirement.pLMRequirement);
        return requirementRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<PLMRequirementVersion> getAllRequirementVersions(Pageable pageable, RequirementCriteria criteria) {
        Predicate predicate = requirementPredicateBuilder.build(criteria, QPLMRequirementVersion.pLMRequirementVersion);
        return requirementVersionRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getReqDocumentTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        List<PLMRequirementDocumentFile> files = requirementDocumentFileRepository.findByDocumentRevisionAndFileTypeAndLatestTrueOrderByModifiedDateDesc(revision, "FILE");
        detailsDto.setRequirements(requirementDocumentChildrenRepository.getChildrenCountByDocumentAndParentIsNullAndVersionLatestTrue(revision.getId()));
        detailsDto.setReviewer(requirementDocumentReviewerRepository.findByRequirementDocumentRevision(revision.getId()).size());
        detailsDto.setItemFiles(files.size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getRequirementTabCounts(Integer id) {
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findOne(id);
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        PLMRequirementVersion version = children.getRequirementVersion();
        List<PLMRequirementFile> files = requirementFileRepository.findByRequirementAndFileTypeAndLatestTrueOrderByModifiedDateDesc(version, "FILE");
        detailsDto.setReviewer(requirementReviewerRepository.findByRequirementVersion(version.getId()).size());
        detailsDto.setItemFiles(files.size());
        detailsDto.setItems(requirementItemRepository.getItemsByRequirementCount(version.getId()));
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(version.getId()));
        return detailsDto;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMRequirement> getReqObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMRequirementType type = requirementTypeRepository.findOne(typeId);
        collectReqHierarchyTypeIds(ids, type);
        return requirementRepository.getByTypeIds(ids, pageable);
    }

    private void collectReqHierarchyTypeIds(List<Integer> collector, PLMRequirementType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMRequirementType> children = requirementTypeRepository.findByParentOrderByIdAsc(type.getId());
            for (PLMRequirementType child : children) {
                collectReqHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public PLMRequirementDocumentReviewer addRequirementDocumentReviewer(Integer reqDoc, PLMRequirementDocumentReviewer reviewer) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(reqDoc);
        reviewer = requirementDocumentReviewerRepository.save(reviewer);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentReviewerAddedEvent(revision, reviewer));
        return reviewer;
    }

    @Transactional
    public PLMRequirementDocumentReviewer updateRequirementDocumentReviewer(Integer reqDoc, PLMRequirementDocumentReviewer reviewer) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(reqDoc);
        reviewer = requirementDocumentReviewerRepository.save(reviewer);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentReviewerUpdateEvent(revision, reviewer));
        return reviewer;
    }

    public void deleteRequirementDocumentReviewer(Integer reqDoc, Integer reviewerId) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(reqDoc);
        PLMRequirementDocumentReviewer requirementReviewer = requirementDocumentReviewerRepository.findOne(reviewerId);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentReviewerDeletedEvent(revision, requirementReviewer));
        requirementDocumentReviewerRepository.delete(requirementReviewer);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentReviewer> getRequirementDocumentReviewers(Integer reqId) {
        return requirementDocumentReviewerRepository.findByRequirementDocumentRevision(reqId);
    }

    @Transactional
    public PLMRequirementDocumentReviewer approveRequirementDocument(Integer id, PLMRequirementDocumentReviewer reviewer) {
        reviewer.setStatus(RequirementApprovalStatus.APPROVED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        reviewer = requirementDocumentReviewerRepository.save(reviewer);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentApprovedEvent(revision, reviewer));
        return reviewer;
    }

    @Transactional
    public PLMRequirementDocumentReviewer rejectRequirementDocument(Integer id, PLMRequirementDocumentReviewer reviewer) {
        reviewer.setStatus(RequirementApprovalStatus.REJECTED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        reviewer = requirementDocumentReviewerRepository.save(reviewer);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentApprovedEvent(revision, reviewer));
        return reviewer;
    }

    @Transactional
    public PLMRequirementDocumentReviewer reviewRequirementDocument(Integer id, PLMRequirementDocumentReviewer reviewer) {
        reviewer.setStatus(RequirementApprovalStatus.REVIEWED);
        reviewer.setVoteTimestamp(new Date());
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        reviewer = requirementDocumentReviewerRepository.save(reviewer);
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentApprovedEvent(revision, reviewer));
        return reviewer;
    }


    @Transactional
    public PLMRequirementDocumentRevision approveAllRequirement(Integer id) {
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(id);
        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersionLatestTrue(revision.getId());
        for (PLMRequirementDocumentChildren version : childrens) {
            checkRequirementWorkflowRelease(version);
            if (!version.getRequirementVersion().getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                List<PLMRequirementReviewer> requirementReviewers = requirementReviewerRepository.findByRequirementVersionAndApproverTrue(version.getId());
                if (requirementReviewers.size() > 0) {
                    for (PLMRequirementReviewer requirementReviewer : requirementReviewers) {
                        approveRequirement(version.getId(), requirementReviewer, true);
                    }
                } else {
                    PLMLifeCyclePhase oldPhase = version.getRequirementVersion().getLifeCyclePhase();
                    PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(version.getRequirementVersion().getMaster().getType().getLifecycle().getId());
                    List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
                    Integer lifeCyclePhase1 = version.getRequirementVersion().getLifeCyclePhase().getId();
                    PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                            filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                            findFirst().get();
                    Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
                    if (index != -1) {
                        index++;
                        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
                        if (lifeCyclePhase != null) {
                            version.getRequirementVersion().setLifeCyclePhase(lifeCyclePhase);
                            requirementVersionRepository.save(version.getRequirementVersion());
                        }
                    }

                    PLMRequirementVersionStatusHistory statusHistory = new PLMRequirementVersionStatusHistory();
                    statusHistory.setRequirementVersion(version.getRequirementVersion().getId());
                    statusHistory.setOldStatus(oldPhase);
                    statusHistory.setNewStatus(version.getRequirementVersion().getLifeCyclePhase());
                    statusHistory.setUpdatedBy(version.getRequirementVersion().getCreatedBy());
                    statusHistory = requirementVersionStatusHistoryRepository.save(statusHistory);

                }
            } else {

            }
        }
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementsApprovedEvent(revision));
        return revision;
    }

    @Transactional
    private void copyDocumentTemplate(PLMRequirementDocumentRevision revision) {
        PLMRequirementDocumentTemplate documentTemplate = requirementDocumentTemplateRepository.findOne(revision.getMaster().getTemplate());
        List<PLMRequirementTemplate> requirementTemplates = requirementTemplateRepository.findByDocumentTemplateAndParentIsNullOrderByCreatedDateAsc(documentTemplate);
        for (PLMRequirementTemplate plmRequirementTemplate : requirementTemplates) {
            PLMRequirement requirement = new PLMRequirement();
            requirement.setName(plmRequirementTemplate.getName());
            requirement.setDescription(plmRequirementTemplate.getDescription());
            requirement.setType(plmRequirementTemplate.getType());
            requirement.setPath(plmRequirementTemplate.getPath());
            requirement.setSequence(plmRequirementTemplate.getSequence());

            PLMRequirementType type = requirementTypeRepository.findOne(requirement.getType().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(type.getLifecycle().getId());
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);

            String number = autoNumberService.getNextNumberByName("Default Requirement Number Source");
            requirement.setNumber(number);
            autoNumberService.saveNextNumber(requirement.getType().getAutoNumberSource().getId(), requirement.getNumber());
            requirement = requirementRepository.save(requirement);

            PLMRequirementVersion version = new PLMRequirementVersion();
            version.setMaster(requirement);
            version.setName(requirement.getName());
            version.setParent(null);
            version.setRequirementDocumentRevision(revision);
            version.setDescription(requirement.getDescription());
            version.setAssignedTo(plmRequirementTemplate.getAssignedTo());
            version.setLatest(true);
            version.setPriority(plmRequirementTemplate.getPriority());
            version.setLifeCyclePhase(lifeCyclePhase);
            version.setVersion(1);
            version = requirementVersionRepository.save(version);
            requirement.setLatestVersion(version.getId());
            requirement = requirementRepository.save(requirement);

            PLMRequirementDocumentChildren children = new PLMRequirementDocumentChildren();
            children.setRequirementVersion(version);
            children.setParent(null);
            children.setDocument(revision.getId());
            requirementDocumentChildrenRepository.save(children);

            if (revision.getMaster().getRequirementReviewer()) {
                List<PLMRequirementTemplateReviewer> requirementTemplateReviewers = requirementTemplateReviewerRepository.findByRequirementTemplate(plmRequirementTemplate.getId());
                for (PLMRequirementTemplateReviewer plmRequirementTemplateReviewer : requirementTemplateReviewers) {
                    PLMRequirementReviewer requirementReviewer = new PLMRequirementReviewer();
                    requirementReviewer.setRequirementVersion(version.getId());
                    requirementReviewer.setReviewer(plmRequirementTemplateReviewer.getReviewer());
                    requirementReviewer.setApprover(plmRequirementTemplateReviewer.getApprover());
                    requirementReviewerRepository.save(requirementReviewer);
                }
            }

            Boolean reqReviewer = revision.getMaster().getRequirementReviewer();
            copyRequirementChildrens(plmRequirementTemplate, version, reqReviewer);

        }
        if (revision.getMaster().getDocumentReviewer()) {
            List<PLMRequirementDocumentTemplateReviewer> documentTemplateReviewers = requirementDocumentTemplateReviewerRepository.findByDocumentTemplate(documentTemplate.getId());
            for (PLMRequirementDocumentTemplateReviewer plmRequirementDocumentTemplateReviewer : documentTemplateReviewers) {
                PLMRequirementDocumentReviewer requirementDocumentReviewer = new PLMRequirementDocumentReviewer();
                requirementDocumentReviewer.setRequirementDocumentRevision(revision.getId());
                requirementDocumentReviewer.setReviewer(plmRequirementDocumentTemplateReviewer.getReviewer());
                requirementDocumentReviewer.setApprover(plmRequirementDocumentTemplateReviewer.getApprover());
                requirementDocumentReviewerRepository.save(requirementDocumentReviewer);
            }
        }

    }

    private void copyRequirementChildrens(PLMRequirementTemplate template, PLMRequirementVersion requirementVersion, Boolean reqReviewer) {
        List<PLMRequirementTemplate> childrenReqs = requirementTemplateRepository.findByParent(template.getId());
        for (PLMRequirementTemplate plmRequirementTemplate : childrenReqs) {
            PLMRequirement requirement = new PLMRequirement();
            requirement.setName(plmRequirementTemplate.getName());
            requirement.setType(plmRequirementTemplate.getType());
            requirement.setPath(plmRequirementTemplate.getPath());
            requirement.setSequence(plmRequirementTemplate.getSequence());

            PLMRequirementType type = requirementTypeRepository.findOne(requirement.getType().getId());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(type.getLifecycle().getId());
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);

            String number = autoNumberService.getNextNumberByName("Default Requirement Number Source");
            requirement.setNumber(number);
            autoNumberService.saveNextNumber(requirement.getType().getAutoNumberSource().getId(), requirement.getNumber());
            requirement = requirementRepository.save(requirement);

            PLMRequirementVersion version = new PLMRequirementVersion();
            version.setMaster(requirement);
            version.setName(requirement.getName());
            version.setDescription(requirement.getDescription());
            version.setAssignedTo(plmRequirementTemplate.getAssignedTo());
            version.setLatest(true);
            version.setVersion(1);
            version.setParent(requirementVersion.getId());
            version.setPriority(plmRequirementTemplate.getPriority());
            version.setLifeCyclePhase(lifeCyclePhase);
            version.setRequirementDocumentRevision(requirementVersion.getRequirementDocumentRevision());
            version = requirementVersionRepository.save(version);
            requirement.setLatestVersion(version.getId());
            requirement = requirementRepository.save(requirement);

            PLMRequirementDocumentChildren children = new PLMRequirementDocumentChildren();
            children.setRequirementVersion(version);
            children.setParent(requirementVersion.getId());
            children.setDocument(requirementVersion.getRequirementDocumentRevision().getId());
            requirementDocumentChildrenRepository.save(children);

            if (reqReviewer) {
                List<PLMRequirementTemplateReviewer> requirementTemplateReviewers = requirementTemplateReviewerRepository.findByRequirementTemplate(plmRequirementTemplate.getId());
                for (PLMRequirementTemplateReviewer plmRequirementTemplateReviewer : requirementTemplateReviewers) {
                    PLMRequirementReviewer requirementReviewer = new PLMRequirementReviewer();
                    requirementReviewer.setRequirementVersion(version.getId());
                    requirementReviewer.setReviewer(plmRequirementTemplateReviewer.getReviewer());
                    requirementReviewer.setApprover(plmRequirementTemplateReviewer.getApprover());
                    requirementReviewerRepository.save(requirementReviewer);
                }
            }
            copyRequirementChildrens(plmRequirementTemplate, version, reqReviewer);
        }

    }

    @Transactional
    public PLMRequirementDocumentTemplate saveAsReqDocumentTemplate(Integer reqDocId, PLMRequirementDocumentTemplate requirementDocumentTemplate) {
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(requirementDocumentTemplate.getType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        requirementDocumentTemplate.setLifeCyclePhase(lifeCyclePhase);
        PLMRequirementDocumentTemplate template = requirementDocumentTemplateRepository.save(requirementDocumentTemplate);
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(reqDocId);
        if (revision != null) {
            List<PLMRequirementDocumentChildren> requirementDocumentChildrens = requirementDocumentChildrenRepository.getByDocumentAndParentIsNullAndVersionLatestOrderByCreatedDateAsc(revision.getId());
            for (PLMRequirementDocumentChildren requirement : requirementDocumentChildrens) {
                PLMRequirementTemplate reqTemplate = new PLMRequirementTemplate();
                reqTemplate.setName(requirement.getRequirementVersion().getMaster().getName());
                reqTemplate.setParent(null);
                reqTemplate.setType(requirement.getRequirementVersion().getMaster().getType());
                reqTemplate.setSequence(requirement.getRequirementVersion().getMaster().getSequence());
                reqTemplate.setPath(requirement.getRequirementVersion().getMaster().getPath());
                reqTemplate.setPriority(requirement.getRequirementVersion().getPriority());
                reqTemplate.setDocumentTemplate(template);
                List<PLMLifeCyclePhase> lifeCyclePhases1 = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(reqTemplate.getType().getLifecycle().getId());
                PLMLifeCyclePhase phase = lifeCyclePhases1.get(0);
                reqTemplate.setLifeCyclePhase(phase);
                reqTemplate.setAssignedTo(requirement.getRequirementVersion().getAssignedTo());
                reqTemplate = requirementTemplateRepository.save(reqTemplate);

                if (requirementDocumentTemplate.getRequirementReviewer()) {
                    List<PLMRequirementReviewer> requirementReviewers = requirementReviewerRepository.findByRequirementVersion(requirement.getRequirementVersion().getId());
                    for (PLMRequirementReviewer plmRequirementReviewer : requirementReviewers) {
                        PLMRequirementTemplateReviewer templateReviewer = new PLMRequirementTemplateReviewer();
                        templateReviewer.setReviewer(plmRequirementReviewer.getReviewer());
                        templateReviewer.setRequirementTemplate(reqTemplate.getId());
                        templateReviewer.setApprover(plmRequirementReviewer.getApprover());
                        requirementTemplateReviewerRepository.save(templateReviewer);
                    }
                }
                Boolean reqReviewer = requirementDocumentTemplate.getRequirementReviewer();
                copyReqTemplateChildrens(requirement, reqTemplate, reqReviewer);

            }
            if (requirementDocumentTemplate.getDocumentReviewer()) {
                List<PLMRequirementDocumentReviewer> documentReviewers = requirementDocumentReviewerRepository.findByRequirementDocumentRevision(revision.getId());
                for (PLMRequirementDocumentReviewer plmRequirementDocumentReviewer : documentReviewers) {
                    PLMRequirementDocumentTemplateReviewer documentTemplateReviewer = new PLMRequirementDocumentTemplateReviewer();
                    documentTemplateReviewer.setReviewer(plmRequirementDocumentReviewer.getReviewer());
                    documentTemplateReviewer.setDocumentTemplate(template.getId());
                    documentTemplateReviewer.setApprover(plmRequirementDocumentReviewer.getApprover());
                    requirementDocumentTemplateReviewerRepository.save(documentTemplateReviewer);
                }
            }
        }
        return template;
    }

    private void copyReqTemplateChildrens(PLMRequirementDocumentChildren children, PLMRequirementTemplate template, Boolean reqReviewer) {
        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.findByParent(children.getRequirementVersion().getId());
        for (PLMRequirementDocumentChildren requirement : childrens) {
            PLMRequirementTemplate reqTemplate = new PLMRequirementTemplate();
            reqTemplate.setName(requirement.getRequirementVersion().getMaster().getName());
            reqTemplate.setParent(template.getId());
            reqTemplate.setType(requirement.getRequirementVersion().getMaster().getType());
            reqTemplate.setPriority(requirement.getRequirementVersion().getPriority());
            reqTemplate.setSequence(requirement.getRequirementVersion().getMaster().getSequence());
            reqTemplate.setPath(requirement.getRequirementVersion().getMaster().getPath());
            reqTemplate.setAssignedTo(requirement.getRequirementVersion().getAssignedTo());
            reqTemplate.setDocumentTemplate(template.getDocumentTemplate());
            List<PLMLifeCyclePhase> lifeCyclePhases1 = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(reqTemplate.getType().getLifecycle().getId());
            PLMLifeCyclePhase phase = lifeCyclePhases1.get(0);
            reqTemplate.setLifeCyclePhase(phase);
            reqTemplate = requirementTemplateRepository.save(reqTemplate);

            if (reqReviewer) {
                List<PLMRequirementReviewer> requirementReviewers = requirementReviewerRepository.findByRequirementVersion(requirement.getRequirementVersion().getId());
                for (PLMRequirementReviewer plmRequirementReviewer : requirementReviewers) {
                    PLMRequirementTemplateReviewer templateReviewer = new PLMRequirementTemplateReviewer();
                    templateReviewer.setReviewer(plmRequirementReviewer.getReviewer());
                    templateReviewer.setRequirementTemplate(reqTemplate.getId());
                    templateReviewer.setApprover(plmRequirementReviewer.getApprover());
                    requirementTemplateReviewerRepository.save(templateReviewer);
                }
            }
            copyReqTemplateChildrens(requirement, reqTemplate, reqReviewer);

        }
    }


    @Transactional
    public List<PLMRequirementItem> createRequirementItems(Integer reqId, List<PLMRequirementItem> requirementItems) {
        requirementItems = requirementItemRepository.save(requirementItems);
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementItemAddedEvent(documentChildren.getRequirementVersion(), documentChildren, requirementItems));
        return requirementItems;
    }

    @Transactional
    public PLMRequirementItem createRequirementItem(Integer reqId, PLMRequirementItem requirementDocument) {
        requirementDocument = requirementItemRepository.save(requirementDocument);
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqId);
        List<PLMRequirementItem> requirementItems = new ArrayList<>();
        requirementItems.add(requirementDocument);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementItemAddedEvent(requirementDocument.getRequirementVersion(), documentChildren, requirementItems));
        return requirementDocument;
    }

    @Transactional
    public PLMRequirementItem updateRequirementItem(Integer reqItemId, PLMRequirementItem requirementDocument) {
        return requirementItemRepository.save(requirementDocument);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementItem> getRequirementItems(Integer reqId) {
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findOne(reqId);
        return requirementItemRepository.getItemsByRequirement(children.getRequirementVersion().getId());
    }

    @Transactional
    public void deleteRequirementItem(Integer reqId, Integer reqItemId) {
        PLMRequirementItem requirementItem = requirementItemRepository.findOne(reqItemId);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findOne(reqId);
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementItemDeletedEvent(children, requirementItem.getRequirementVersion(), requirementItem));
        requirementItemRepository.delete(reqItemId);
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementItem> getItemRequirements(Integer itemId) {
        List<PLMRequirementItem> requirementItems = requirementItemRepository.getRequirementsByItem(itemId);
        requirementItems.forEach(plmRequirementItem -> {
            plmRequirementItem.setPersonName(personRepository.findOne(plmRequirementItem.getRequirementVersion().getAssignedTo()).getFullName());
            PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(plmRequirementItem.getRequirementVersion().getRequirementDocumentRevision().getId(), plmRequirementItem.getRequirementVersion());
            if (children != null) {
                plmRequirementItem.setDocumentChildren(children.getId());
            }
        });
        return requirementItems;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentRevision> getReqDocumentRevisionStatusHistory(Integer id) {
        PLMRequirementDocument document = requirementDocumentRepository.findOne(id);
        List<PLMRequirementDocumentRevision> requirementDocumentRevisions = requirementDocumentRevisionRepository.getByMasterOrderByCreatedDateDesc(document);
        requirementDocumentRevisions.forEach(revision -> {
            List<PLMRequirementDocumentRevisionStatusHistory> history = requirementDocumentRevisionStatusHistoryRepository.findByDocumentRevisionOrderByTimestampDesc(revision.getId());
            revision.setStatusHistory(history);
        });
        return requirementDocumentRevisions;
    }


    @Transactional
    public PLMRequirementDocumentRevision reviseReqDocument(PLMRequirementDocumentRevision rev, Integer revisionId) {
        PLMRequirementDocument document = requirementDocumentRepository.findOne(revisionId);
        String notes = rev.getComment();
        PLMRequirementDocumentRevision revision = requirementDocumentRevisionRepository.findOne(document.getLatestRevision());
        if (revision != null) {
            return reviseRevisionDocument(revision, null, notes);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }
    }


    @Transactional(readOnly = true)
    public List<PLMRequirementObjectAttribute> getReqDocumentUsedAttributes(Integer attributeId) {
        List<PLMRequirementObjectAttribute> reqDocumentAttributes = requirementObjectAttributeRepository.findByAttributeId(attributeId);
        return reqDocumentAttributes;
    }

    @Transactional
    public PLMRequirementDocumentRevision reviseRevisionDocument(PLMRequirementDocumentRevision revision, String nextRev, String notes) {
        PLMRequirementDocument document = revision.getMaster();
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(document);
        }
        if (nextRev != null) {
            PLMRequirementDocumentRevision copyRevision = createNextRev(revision, nextRev, notes);
            revision.setLatest(false);
            requirementDocumentRevisionRepository.save(revision);
            document.setLatestRevision(copyRevision.getId());
            requirementDocumentRepository.save(document);
            //Copy the related
            copyAttributes(revision.getId(), copyRevision.getId());
            copyRequirements(revision, copyRevision);
            copyReqDocumentReviewers(revision, copyRevision);
            if (revision.getWorkflow() != null) {
                attachReqDocReviseWorkflow(copyRevision.getId(), revision.getWorkflow());
            }

            return copyRevision;
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    @Transactional
    public void attachReqDocReviseWorkflow(Integer id, Integer workflowId) {
        PLMRequirementDocumentRevision reqDocRevision = requirementDocumentRevisionRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findOne(workflowId);
        if (workflow != null) {
            PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowDefinition.getId());
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(RequirementEnumObject.REQUIREMENTDOCUMENTREVISION, reqDocRevision.getId(), wfDef);
                reqDocRevision.setWorkflow(workflow1.getId());
                requirementDocumentRevisionRepository.save(reqDocRevision);
            }
        }

    }


    private PLMRequirementDocumentRevision createNextRev(PLMRequirementDocumentRevision documentRevision, String nextRev, String notes) {
        Integer notReleasedDocumentCount = objectDocumentRepository.getNotReleasedDocumentsCount(documentRevision.getId());
        if (notReleasedDocumentCount > 0) {
            String message = messageSource.getMessage("req_doc_has_unreleased_documents", null, "Requirement document has some unreleased documents", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", documentRevision.getMaster().getNumber());
            throw new CassiniException(result);
        }
        PLMRequirementDocumentRevision revision = new PLMRequirementDocumentRevision();
        PLMRequirementDocument reqDocument = documentRevision.getMaster();
        PLMRequirementDocumentType type = requirementDocumentTypeRepository.findOne(reqDocument.getType().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(type.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        revision.setMaster(reqDocument);
        revision.setName(reqDocument.getName());
        revision.setLatest(true);
        revision.setNotes(notes);
        PLMRequirementDocumentType documentType = requirementDocumentTypeRepository.findOne(reqDocument.getType().getId());
        Lov lov = documentType.getRevisionSequence();
        revision.setLifeCyclePhase(lifeCyclePhase);
        revision.setRevision(nextRev);
        revision.setOwner(documentRevision.getOwner());
        revision = requirementDocumentRevisionRepository.save(revision);

        PLMRequirementDocumentRevisionStatusHistory statusHistory = new PLMRequirementDocumentRevisionStatusHistory();
        statusHistory.setDocumentRevision(revision.getId());
        statusHistory.setOldStatus(revision.getLifeCyclePhase());
        statusHistory.setNewStatus(revision.getLifeCyclePhase());
        statusHistory.setUpdatedBy(revision.getCreatedBy());
        statusHistory = requirementDocumentRevisionStatusHistoryRepository.save(statusHistory);

        return revision;
    }

    public String getNextRevisionSequence(PLMRequirementDocument item) {
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

    private List<String> getRevisions(PLMRequirementDocument item) {
        List<String> revs = new ArrayList<>();
        PLMRequirementDocumentRevision revisions = requirementDocumentRevisionRepository.findOne(item.getLatestRevision());
        String rev = revisions.getRevision();
        if (!revs.contains(rev)) {
            revs.add(rev);
        }
        Collections.sort(revs);
        return revs;
    }


    private void copyAttributes(Integer oldRevision, Integer newRevision) {
        List<PLMRequirementObjectAttribute> attrs = requirementObjectAttributeRepository.findByObjectId(oldRevision);
        for (PLMRequirementObjectAttribute attr : attrs) {
            PLMRequirementObjectAttribute revisionAttribute = JsonUtils.cloneEntity(attr, PLMRequirementObjectAttribute.class);
            revisionAttribute.setId(new ObjectAttributeId(newRevision, attr.getId().getAttributeDef()));
            copyAttachments(oldRevision, revisionAttribute);
            revisionAttribute = requirementObjectAttributeRepository.save(revisionAttribute);
        }

    }

    private void copyAttachments(Integer oldRevisionId, ObjectAttribute objectAttribute) {
        if (objectAttribute.getAttachmentValues().length > 0) {
            List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute.getAttachmentValues()));
            List<Integer> integers = new ArrayList<>();
            for (AttributeAttachment attachment : attachments) {
                AttributeAttachment attachment1 = new AttributeAttachment();
                attachment1.setObjectId(objectAttribute.getId().getObjectId());
                attachment1.setAttributeDef(attachment.getAttributeDef());
                attachment1.setExtension(attachment.getExtension());
                attachment1.setAddedBy(attachment.getAddedBy());
                attachment1.setObjectType(attachment.getObjectType());
                attachment1.setAddedOn(new Date());
                attachment1.setName(attachment.getName());
                attachment1.setSize(attachment.getSize());
                attachment1 = attributeAttachmentRepository.save(attachment1);
                integers.add(attachment1.getId());
                try {
                    String dir = "";
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + objectAttribute.getId().getObjectId();
                    File folder = new File(dir);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    dir = dir + File.separator + attachment1.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    String oldFileDir = "";

                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator + "filesystem" + File.separator + "attachments" + File.separator + oldRevisionId;
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    File infile = new File(oldFileDir);
                    File outfile = new File(dir);
                    instream = new FileInputStream(infile);
                    outstream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = instream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, length);
                    }
                    instream.close();
                    outstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            objectAttribute.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
        }
    }


    private void copyRequirements(PLMRequirementDocumentRevision oldRevision, PLMRequirementDocumentRevision newRevision) {
        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.getByDocumentAndLatestTrueOrderByCreatedDateAsc(oldRevision.getId());
        childrens.forEach(plmRequirementDocumentChildren -> {
            PLMRequirementDocumentChildren copy = JsonUtils.cloneEntity(plmRequirementDocumentChildren, PLMRequirementDocumentChildren.class);
            copy.setId(null);
            copy.setDocument(newRevision.getId());
            requirementDocumentChildrenRepository.save(copy);
        });
    }

    private void copyReqDocumentReviewers(PLMRequirementDocumentRevision oldRevision, PLMRequirementDocumentRevision newRevision) {
        List<PLMRequirementDocumentReviewer> reviewers = requirementDocumentReviewerRepository.findByRequirementDocumentRevision(oldRevision.getId());
        if (reviewers.size() > 0) {
            reviewers.forEach(requirementDocumentReviewer -> {
                PLMRequirementDocumentReviewer documentReviewer = new PLMRequirementDocumentReviewer();
                documentReviewer.setApprover(requirementDocumentReviewer.getApprover());
                documentReviewer.setRequirementDocumentRevision(newRevision.getId());
                documentReviewer.setReviewer(requirementDocumentReviewer.getReviewer());
                documentReviewer.setStatus(RequirementApprovalStatus.NONE);
                requirementDocumentReviewerRepository.save(documentReviewer);
            });
        }

    }

    @Transactional
    public PLMRequirementDocumentChildren reviseRequirement(PLMRequirementDocumentChildren documentChildren, Integer reqId) {
        PLMRequirementDocumentRevision documentRevision = requirementDocumentRevisionRepository.findOne(documentChildren.getDocument());
        PLMRequirementVersion reqVersion = requirementVersionRepository.findOne(reqId);
        String notes = documentChildren.getRequirementVersion().getComment();
        reqVersion.setLatest(false);
        //reqVersion.setRequirementDocumentRevision(documentRevision);
        reqVersion = requirementVersionRepository.save(reqVersion);
        PLMRequirement requirement = reqVersion.getMaster();
        PLMRequirementVersion version = new PLMRequirementVersion();
        version.setMaster(reqVersion.getMaster());
        version.setName(reqVersion.getName());
        version.setDescription(reqVersion.getDescription());
        version.setAssignedTo(reqVersion.getAssignedTo());
        version.setRequirementDocumentRevision(documentRevision);
        version.setLatest(true);
        version.setParent(documentChildren.getParent());
        version.setNotes(notes);
        version.setVersion(reqVersion.getVersion() + 1);
        version.setPriority(reqVersion.getPriority());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(requirement.getType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        version.setLifeCyclePhase(lifeCyclePhase);
        version = requirementVersionRepository.save(version);
        PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(documentChildren.getDocument(), reqVersion);
        PLMRequirementDocumentChildren reqChildren = null;
        if (children != null) {
            PLMRequirementDocumentChildren newChildren = JsonUtils.cloneEntity(children, PLMRequirementDocumentChildren.class);
            newChildren.setId(null);
            newChildren.setRequirementVersion(version);
            reqChildren = requirementDocumentChildrenRepository.save(newChildren);
            List<PLMRequirementDocumentChildren> childrensList = requirementDocumentChildrenRepository.findByDocumentAndParent(documentChildren.getDocument(), reqVersion.getId());
            if (childrensList.size() > 0) {
                for (PLMRequirementDocumentChildren children1 : childrensList) {
                    children1.setParent(version.getId());
                    requirementDocumentChildrenRepository.save(children1);
                    //PLMRequirementVersion version1 = requirementVersionRepository.findOne(children1.getRequirementVersion().getId());
                    //version1.setParent(version.getId());
                    //requirementVersionRepository.save(version1);
                }
            }
        }
        requirement.setLatestVersion(version.getId());
        requirement = requirementRepository.save(requirement);
        copyAttributes(reqVersion.getId(), version.getId());
        createRequirementReviewers(reqVersion, version);
        createReqVersionStatusHistory(version, version.getLifeCyclePhase());
        if (documentChildren.getWorkflow() != null && reqChildren != null) {
            attachReqReviseWorkflow(reqChildren.getId(), documentChildren.getWorkflow());
        }
        return reqChildren;
    }

    @Transactional
    public void attachReqReviseWorkflow(Integer id, Integer workflowId) {
        PLMRequirementDocumentChildren reqDocChildren = requirementDocumentChildrenRepository.findOne(id);
        reqDocChildren.setWorkflow(null);
        PLMWorkflow workflow = plmWorkflowRepository.findOne(workflowId);
        if (workflow != null) {
            PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowDefinition.getId());
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(PLMObjectType.REQUIREMENT, reqDocChildren.getId(), wfDef);
                reqDocChildren.setWorkflow(workflow1.getId());
                requirementDocumentChildrenRepository.save(reqDocChildren);
            }
        }

    }

    @Transactional(readOnly = true)
    public List<PLMRequirementVersion> getReqVersionStatusHistory(Integer id) {
        PLMRequirement requirement = requirementRepository.findOne(id);
        List<PLMRequirementVersion> requirementVersions = requirementVersionRepository.getByMasterOrderByCreatedDateDesc(requirement);
        requirementVersions.forEach(version -> {
            PLMRequirementDocumentChildren children = requirementDocumentChildrenRepository.findByDocumentAndRequirementVersion(version.getRequirementDocumentRevision().getId(), version);
            List<PLMRequirementVersionStatusHistory> history = requirementVersionStatusHistoryRepository.findByRequirementVersionOrderByTimestampDesc(version.getId());
            version.setStatusHistory(history);
            version.setReqChild(children.getId());
        });
        return requirementVersions;
    }

    private void createReqVersionStatusHistory(PLMRequirementVersion version, PLMLifeCyclePhase oldLifeCyclePhase) {
        PLMRequirementVersionStatusHistory statusHistory = new PLMRequirementVersionStatusHistory();
        statusHistory.setRequirementVersion(version.getId());
        statusHistory.setOldStatus(oldLifeCyclePhase);
        statusHistory.setNewStatus(version.getLifeCyclePhase());
        statusHistory.setUpdatedBy(version.getCreatedBy());
        statusHistory = requirementVersionStatusHistoryRepository.save(statusHistory);
    }

    private void createRequirementReviewers(PLMRequirementVersion oldVersion, PLMRequirementVersion version) {
        List<PLMRequirementReviewer> requirementReviewers = requirementReviewerRepository.findByRequirementVersion(oldVersion.getId());
        if (requirementReviewers.size() > 0) {
            for (PLMRequirementReviewer plmRequirementReviewer : requirementReviewers) {
                PLMRequirementReviewer requirementReviewer = new PLMRequirementReviewer();
                requirementReviewer.setRequirementVersion(version.getId());
                requirementReviewer.setReviewer(plmRequirementReviewer.getReviewer());
                requirementReviewer.setApprover(plmRequirementReviewer.getApprover());
                requirementReviewerRepository.save(requirementReviewer);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentChildren> getRequirementVersionsTree(Integer revisionId) {
        List<PLMRequirementDocumentChildren> childrens = requirementDocumentChildrenRepository.getByDocumentAndParentIsNullAndVersionLatestOrderByCreatedDateAsc(revisionId);
        for (PLMRequirementDocumentChildren version : childrens) {
            visitRequirementVersionChildren(version);
        }
        return childrens;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentChildren> getRequirementVersionChildren(PLMRequirementDocumentChildren children) {
        return requirementDocumentChildrenRepository.findByDocumentAndParentAndVersionLatestOrderByCreatedDateAsc(children.getDocument(), children.getRequirementVersion().getId());
    }

    private void visitRequirementVersionChildren(PLMRequirementDocumentChildren children) {
        List<PLMRequirementDocumentChildren> childrens = getRequirementVersionChildren(children);
        for (PLMRequirementDocumentChildren child : childrens) {
            visitRequirementVersionChildren(child);
        }
        children.setChildren(childrens);
    }

    @Transactional(readOnly = true)
    public PLMRequirementDocumentChildren getRequirementChildrenVersion(Integer id) {
        PLMRequirementDocumentChildren version = requirementDocumentChildrenRepository.findOne(id);
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMRequirementReviewer reviewer = requirementReviewerRepository.findByRequirementVersionAndReviewer(version.getRequirementVersion().getId(), person.getId());
        if (reviewer != null) {
            version.getRequirementVersion().setReviewer(reviewer);
        }
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(version.getId());
        version.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        version.setFinishWorkflow(workFlowStatusDto.getFinishWorkflow());
        version.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        version.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return version;
    }

    @Transactional(readOnly = true)
    public List<PLMRequirementDocumentChildren> getAllReqDocChildrens(RequirementCriteria requirementCriteria) {
        Predicate predicate = requirementDocumentChildrenPredicateBuilder.build(requirementCriteria, QPLMRequirementDocumentChildren.pLMRequirementDocumentChildren);
        return IteratorUtils.toList(requirementDocumentChildrenRepository.findAll(predicate).iterator());
    }

    @Transactional
    public void deleteRequirementDocChildren(Integer id) {
        PLMRequirementDocumentChildren req = requirementDocumentChildrenRepository.findOne(id);
        updateSiblingSequences(req.getRequirementVersion());
        requirementDocumentChildrenRepository.delete(id);
    }

    @Transactional
    public PLMRequirementVersion submitReqVersion(Integer id) {
        PLMRequirementVersion version = requirementVersionRepository.findOne(id);
        PLMLifeCyclePhase oldStatus = version.getLifeCyclePhase();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(version.getMaster().getType().getLifecycle().getId());
        Integer lifeCyclePhase = version.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase1 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase1);
        if (index != -1) {
            index++;
            PLMLifeCyclePhase lcPhase = lifeCyclePhases.get(index);
            if (lcPhase != null) {
                version.setLifeCyclePhase(lcPhase);
                version = requirementVersionRepository.save(version);
            }
            PLMRequirementVersionStatusHistory statusHistory = new PLMRequirementVersionStatusHistory();
            statusHistory.setRequirementVersion(version.getId());
            statusHistory.setOldStatus(oldStatus);
            statusHistory.setNewStatus(lcPhase);
            statusHistory.setUpdatedBy(version.getCreatedBy());
            statusHistory = requirementVersionStatusHistoryRepository.save(statusHistory);
        }

        return version;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getReqDocumentTemplateTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        PLMRequirementDocumentTemplate template = requirementDocumentTemplateRepository.findOne(id);
        detailsDto.setRequirements(requirementTemplateRepository.findByDocumentTemplate(template).size());
        detailsDto.setReviewer(requirementDocumentTemplateReviewerRepository.findByDocumentTemplate(template.getId()).size());
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getRequirementTemplateTabCounts(Integer id) {
        PLMRequirementTemplate requirementTemplate = requirementTemplateRepository.findOne(id);
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setReviewer(requirementTemplateReviewerRepository.findByRequirementTemplate(requirementTemplate.getId()).size());
        return detailsDto;
    }

    //reqDocument//

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowStartedEvent(revision, event.getPlmWorkflow()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowPromotedEvent(revision, plmWorkflow, fromStatus, toStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) throws JsonProcessingException {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowDemotedEvent(revision, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowFinishedEvent(revision, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) throws JsonProcessingException {
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowHoldEvent(revision, plmWorkflow, fromStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENTDOCUMENTREVISION'")
    public void partWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) throws JsonProcessingException {
        PLMRequirementDocumentRevision revision = (PLMRequirementDocumentRevision) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new RequirementDocumentEvents.RequirementDocumentWorkflowUnholdEvent(revision, plmWorkflow, fromStatus));
    }

    //requirement//
    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowStarted(RequirementWorkflowEvents.ReqWorkflowStartedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowStartedEvent(requirement, event.getPlmWorkflow()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowPromoted(RequirementWorkflowEvents.ReqWorkflowPromotedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowPromotedEvent(requirement, plmWorkflow, fromStatus, toStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowDemoted(RequirementWorkflowEvents.ReqWorkflowDemotedEvent event) throws JsonProcessingException {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowDemotedEvent(requirement, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowFinished(RequirementWorkflowEvents.ReqWorkflowFinishedEvent event) throws JsonProcessingException {
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowFinishedEvent(requirement, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowHold(RequirementWorkflowEvents.ReqWorkflowHoldEvent event) throws JsonProcessingException {
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowHoldEvent(requirement, plmWorkflow, fromStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'REQUIREMENT'")
    public void requirementWorkflowUnHold(RequirementWorkflowEvents.ReqWorkflowUnHoldEvent event) throws JsonProcessingException {
        PLMRequirementDocumentChildren requirement = (PLMRequirementDocumentChildren) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new RequirementEvents.RequirementWorkflowUnholdEvent(requirement, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getReqDocHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMRequirementDocumentType reqDocType = requirementDocumentTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (reqDocType.getParent() != null) {
            getReqDocWorkflowsFromHierarchy(workflowDefinitions, reqDocType.getParent(), type);
        }
        return workflowDefinitions;
    }


    private void getReqDocWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMRequirementDocumentType reqDocType = requirementDocumentTypeRepository.findOne(typeId);
        if (reqDocType != null) {
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
            if (reqDocType.getParent() != null) {
                getReqDocWorkflowsFromHierarchy(definitions, reqDocType.getParent(), type);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getRequirementHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMRequirementType manufacturerType = requirementTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (manufacturerType.getParent() != null) {
            getReqWorkflowsFromHierarchy(workflowDefinitions, manufacturerType.getParent(), type);
        }
        return workflowDefinitions;
    }


    private void getReqWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PLMRequirementType manufacturerType = requirementTypeRepository.findOne(typeId);
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
            if (manufacturerType.getParent() != null) {
                getReqWorkflowsFromHierarchy(definitions, manufacturerType.getParent(), type);
            }
        }
    }

    @Transactional
    public PLMWorkflow attachReqDocWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMRequirementDocumentRevision reqDocRevision = requirementDocumentRevisionRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (reqDocRevision != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(RequirementEnumObject.REQUIREMENTDOCUMENTREVISION, reqDocRevision.getId(), wfDef);
            reqDocRevision.setWorkflow(workflow.getId());
            requirementDocumentRevisionRepository.save(reqDocRevision);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachReqWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (documentChildren != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.REQUIREMENT, documentChildren.getId(), wfDef);
            documentChildren.setWorkflow(workflow.getId());
            requirementDocumentChildrenRepository.save(documentChildren);
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<Integer> getReqVersionIds(Integer reqDocId) {
        PLMRequirementDocumentChildren documentChildren = requirementDocumentChildrenRepository.findOne(reqDocId);
        List<Integer> reqIds = requirementVersionRepository.getVersionIdsByReqId(documentChildren.getRequirementVersion().getMaster().getId());
         return requirementDocumentChildrenRepository.getChildrenIdsByVersionIds(reqIds);     
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCycle> getReqDocTypeLifecycles() {
        List<PLMLifeCycle> plmLifeCycles = requirementDocumentTypeRepository.getUniqueReqDocTypeLifeCycles();
        return plmLifeCycles;
    }

    public List<Person> getReqDocOwners() {
        return requirementDocumentRevisionRepository.getReqDocOwnerIds();
    }

    public List<Person> getAssignedTo(Integer id) {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = requirementDocumentChildrenRepository.getAssignedTo(id);
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public List<String> getReqPriorities(Integer id) {
        return requirementDocumentChildrenRepository.getUniqueRequirementPriorities(id);
    }

    @Transactional(readOnly = true)
    public List<PLMLifeCyclePhase> getReqLifecycles(Integer id) {
        List<PLMLifeCyclePhase> plmLifeCyclePhases = requirementDocumentChildrenRepository.getLifeCyclePhases(id);
        return plmLifeCyclePhases;
    }

}
