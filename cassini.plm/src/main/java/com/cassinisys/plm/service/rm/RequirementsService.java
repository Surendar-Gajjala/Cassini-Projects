package com.cassinisys.plm.service.rm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivity;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMTask;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pm.TaskRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.classification.ClassificationTypeService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class RequirementsService implements CrudService<RequirementType, Integer>,
        PageableService<RequirementType, Integer>, ClassificationTypeService<RequirementType, RmObjectTypeAttribute> {

    @Autowired
    private RmObjectTypeRepository rmObjectTypeRepository;
    @Autowired
    private RmObjectTypeAttributeRepository rmObjectTypeAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private RequirementTypeRepository requirementTypeRepository;
    @Autowired
    private RmObjectAttributeRepository rmObjectAttributeRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AttributeAttachmentService attributeAttachmentService;
    @Autowired
    private RequirementDeliverableRepository requirementDeliverableRepository;
    @Autowired
    private ProjectRequirementDeliverableBuilder projectRequirementDeliverableBuilder;
    @Autowired
    private RequirementEditRepository requirementEditRepository;
    @Autowired
    private SpecRequirementRepository specRequirementRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private SpecificationsService specificationsService;
    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private RequirementItemDeliverablePredicateBuilder requirementItemDeliverablePredicateBuilder;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private RequirementSearchPredicateBuilder requirementSearchPredicateBuilder;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    public RequirementType create(RequirementType rmObjectType) {
        checkNotNull(rmObjectType);
        rmObjectType = requirementTypeRepository.save(rmObjectType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.REQUIREMENTTYPE, rmObjectType));
        return rmObjectType;
    }

    @Override
    public RequirementType update(RequirementType rmObjectType) {
        checkNotNull(rmObjectType);
        checkNotNull(rmObjectType.getId());
        RequirementType existReqType = requirementTypeRepository.findOne(rmObjectType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.REQUIREMENTTYPE, existReqType, rmObjectType));
        rmObjectType = requirementTypeRepository.save(rmObjectType);
        return rmObjectType;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        RequirementType requirementType = requirementTypeRepository.findOne(id);
        if (requirementType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.REQUIREMENTTYPE, requirementType));
        }
        requirementTypeRepository.delete(id);
    }

    @Override
    public RequirementType get(Integer id) {
        checkNotNull(id);
        RequirementType rmObjectType = requirementTypeRepository.findOne(id);
        if (rmObjectType == null) {
            throw new ResourceNotFoundException();
        }
        return rmObjectType;
    }

    public List<RequirementType> findMultiple(List<Integer> ids) {
        return requirementTypeRepository.findByIdIn(ids);
    }

    public List<RequirementType> getRootTypes() {
        return requirementTypeRepository.findByParentTypeIsNullOrderByNameCreatedDateAsc();
    }

    public List<RequirementType> getChildren(Integer parent) {
        return requirementTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<RequirementType> getClassificationTree() {
        List<RequirementType> types = getRootTypes();
        for (RequirementType type : types) {
            visitChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<RequirementType> getAllRequirementTypesWithAttributes() {
        List<RequirementType> types = getAll();
        for (RequirementType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(requirementTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(RequirementType parent) {
        List<RequirementType> children = getChildren(parent.getId());
        for (RequirementType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<RequirementType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Override
    public List<RequirementType> getAll() {
        return requirementTypeRepository.findAll();
    }

    @Override
    public Page<RequirementType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return requirementTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<RmObjectTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return rmObjectTypeAttributeRepository.findByRmObjectTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<RmObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<RmObjectTypeAttribute> collector = new ArrayList<>();
        List<RmObjectTypeAttribute> atts = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<RmObjectTypeAttribute> collector, Integer typeId) {
        RequirementType rmObjectType = requirementTypeRepository.findOne(typeId);
        if (rmObjectType != null) {
            Integer parentType = rmObjectType.getParentType();
            if (parentType != null) {
                List<RmObjectTypeAttribute> atts = rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    public RmObjectTypeAttribute getAttribute(Integer id) {
        return rmObjectTypeAttributeRepository.findOne(id);
    }

    @Transactional
    public RmObjectTypeAttribute createAttribute(RmObjectTypeAttribute attribute) {
        List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectType(attribute.getRmObjectType());
        if (rmObjectTypeAttributes.size() >= 0) {
            attribute.setSeq(rmObjectTypeAttributes.size() + 1);
        }
        RmObjectTypeAttribute rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(attribute);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.REQUIREMENTTYPE, rmObjectTypeAttribute));
        return rmObjectTypeAttribute;
    }

    public RmObjectTypeAttribute updateAttribute(RmObjectTypeAttribute attribute) {
        RmObjectTypeAttribute existAttribute = rmObjectTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.REQUIREMENTTYPE, existAttribute, attribute));
        RmObjectTypeAttribute rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(attribute);
        return rmObjectTypeAttribute;

    }

    public void deleteAttribute(Integer id) {
        RmObjectTypeAttribute attribute = rmObjectTypeAttributeRepository.findOne(id);
        List<RmObjectTypeAttribute> rmObjectTypeAttributes = rmObjectTypeAttributeRepository.findByRmObjectType(attribute.getRmObjectType());
        if (rmObjectTypeAttributes.size() > 0) {
            for (RmObjectTypeAttribute rmObjectTypeAttribute : rmObjectTypeAttributes) {
                if (rmObjectTypeAttribute.getSeq() > attribute.getSeq()) {
                    rmObjectTypeAttribute.setSeq(rmObjectTypeAttribute.getSeq() - 1);
                    rmObjectTypeAttribute = rmObjectTypeAttributeRepository.save(rmObjectTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.REQUIREMENTTYPE, attribute));
        rmObjectTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    public RequirementType getItemTypeName(String name) {
        return requirementTypeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<RmObjectTypeAttribute> getSpecificationTypeAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return rmObjectTypeAttributeRepository.findByRmObjectTypeOrderByName(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    /*-----------------Requirement Methods ----------------------*/
    @Transactional
    public Requirement createRequirement(Requirement requirement) {
        Integer integer = 0;
        Integer workflowDef = null;
        if (requirement.getWorkflowDefId() != null) {
            workflowDef = requirement.getWorkflowDefId();
        }
        if (requirement.getAssignedTo() != null) {
            requirement.setStatus(RequirementStatus.PENDING);
        } else {
            requirement.setStatus(RequirementStatus.NONE);
        }
        if (requirement.getObjectNumber() == null || requirement.getObjectNumber().trim().isEmpty()) {
            RequirementType requirementType = requirementTypeRepository.findOne(requirement.getType().getId());
            AutoNumber autoNumber = autoNumberRepository.findOne(requirementType.getNumberSource().getId());
            String number = autoNumber.next();
            autoNumber = autoNumberRepository.save(autoNumber);
            requirement.setObjectNumber(number);
            requirement.setType(requirementType);
        }
        requirement.setVersion(0);
        requirement.setLatest(true);
        autoNumberService.saveNextNumber(requirement.getType().getNumberSource().getId(), requirement.getObjectNumber());
        requirement = requirementRepository.save(requirement);
        if (workflowDef != null) {
            attachReqWorkflow(requirement.getId(), workflowDef);
        }
        //Create Requirement Edit
        RequirementEdit requirementEdit = new RequirementEdit();
        requirementEdit.setEditedName(requirement.getName());
        requirementEdit.setEditedDescription(requirement.getDescription());
        requirementEdit.setRequirement(requirement.getId());
        requirementEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
        requirementEdit.setVersion(0);
        requirementEdit.setEditedDate(new Date());
        requirementEdit = requirementEditRepository.save(requirementEdit);
        List<RequirementEdit> requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateAsc(requirementEdit.getRequirement());
        integer = integer + requirementEdits.size();
        requirement.setRequirementEditLength(integer);
        return requirement;
    }

    @Transactional(readOnly = true)
    public Requirement findById(Integer id) {
        Requirement requirement = requirementRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(requirement.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    requirement.setStartWorkflow(true);
                }
            }
        }
        return requirement;
    }

    @Transactional(readOnly = true)
    public Page<Requirement> findByAssignedTo(Integer id, Pageable pageable) {
        Person person = personRepository.findOne(id);
        Page<Requirement> requirements = requirementRepository.findByAssignedTo(person, pageable);
        for (Requirement requirement : requirements.getContent()) {
            Specification specification = specificationRepository.findOne(requirement.getSpecification());
            requirement.setSpecificationObject(specification);
        }
        return requirements;
    }

    /*-------------------Requirement deliverables------------------*/

    @Transactional(readOnly = true)
    public List<Requirement> getProjectRequirementDeliverablesByProjectId(Integer projectId) {
        List<Requirement> requirements = new ArrayList<>();
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByObjectId(projectId);
        List<Requirement> totalRequirements = requirementRepository.findAll();
        if (requirementDeliverables.size() != 0) {
            for (Requirement glossary : totalRequirements) {
                Boolean exist = false;
                for (RequirementDeliverable glossaryDeliverable : requirementDeliverables) {
                    if (glossaryDeliverable.getRequirement().equals(glossary.getId())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    requirements.add(glossary);
                }
            }
        } else {
            requirements.addAll(totalRequirements);
        }
        return requirements;
    }

    @Transactional
    public List<RequirementDeliverable> createProjectRequirementDeliverables(Integer projectId, String objectType, List<Requirement> requirements) {
        String specNames = null;
        List<RequirementDeliverable> deliverables = new ArrayList<>();
        for (Requirement glossary : requirements) {
            RequirementDeliverable glossaryDeliverable1 = new RequirementDeliverable();
            glossaryDeliverable1.setObjectId(projectId);
            glossaryDeliverable1.setObjectType(objectType);
            glossaryDeliverable1.setRequirement(glossary);
            deliverables.add(glossaryDeliverable1);
            if (specNames == null) {
                specNames = glossary.getName();
            } else {
                specNames = specNames + " , " + glossary.getName();
            }
        }
        PLMProject project = projectRepository.findOne(projectId);
        if (project != null) {
            deliverables = requirementDeliverableRepository.save(deliverables);
        }
        PLMActivity activity = activityRepository.findOne(projectId);
        if (activity != null) {
            deliverables = requirementDeliverableRepository.save(deliverables);
        }
        PLMTask task = taskRepository.findOne(projectId);
        if (task != null) {
            deliverables = requirementDeliverableRepository.save(deliverables);
        }
        return deliverables;

    }

    @Transactional(readOnly = true)
    public Page<Requirement> getProjectRequirementDeliverables(Integer project, Pageable pageable, RequirementBuildCriteria criteria) {
        criteria.setProject(project);
        List<Requirement> requirementList = new ArrayList<>();
        Predicate predicate = projectRequirementDeliverableBuilder.build(criteria, QRequirement.requirement);
        Page<Requirement> requirements = requirementRepository.findAll(predicate, pageable);
        if (requirements.getContent().size() > 0) {
            for (Requirement requirement : requirements) {
                if (requirement.getLatest()) {
                    requirementList.add(requirement);
                }

            }
        }
        Page<Requirement> requirementPage = new PageImpl<>(requirementList,
                new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
                requirementList.size());
        return requirementPage;

    }

    @Transactional
    public void deleteRequirementDeliverable(Integer projectId, Integer reqId) {
        Requirement requirement = requirementRepository.findOne(reqId);
        RequirementDeliverable requirementDeliverable = requirementDeliverableRepository.findByObjectIdAndRequirement(projectId, requirement);
        requirementDeliverableRepository.delete(requirementDeliverable);
    }

    public Requirement updateReqirementEdit(Integer reqId, Requirement requirement, String notes) {
        Requirement requirement1 = requirementRepository.findOne(reqId);
        Integer integer = 0;
        RequirementEdit requirementEdit = new RequirementEdit();
        if (requirement != null) {
            requirementEdit.setRequirement(requirement.getId());
            requirementEdit.setEditedName(requirement.getName());
            requirementEdit.setEditedDescription(requirement.getDescription());
            requirementEdit.setVersion(requirement1.getVersion());
            requirementEdit.setEditedDate(new Date());
            requirementEdit.setEditNotes(notes);
            requirementEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
            requirementEdit = requirementEditRepository.save(requirementEdit);
            List<RequirementEdit> requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateAsc(requirementEdit.getRequirement());
            integer = integer + requirementEdits.size();
            requirement.setRequirementEditLength(integer);

        }
        return requirement;

    }

    public RequirementEdit getLastAcceptedRequirementEdit(Integer reqId) {
        RequirementEdit lastAcceptedEntryEdit = null;
        RequirementEdit requirementEdit1 = requirementEditRepository.findByRequirementAndStatus(reqId, RequirementEditStatus.FINAL);
        if (requirementEdit1 == null) {
            List<RequirementEdit> requirementEdits = requirementEditRepository.
                    findByRequirementAndStatusOrderByAcceptedDateDesc(reqId, RequirementEditStatus.ACCEPTED);
            if ((requirementEdits.size() > 0)) {
                lastAcceptedEntryEdit = requirementEdits.get(0);
            }
        }
        return lastAcceptedEntryEdit;
    }

    public List<RequirementEdit> getRequirementEdits(Integer reqId) {
        List<RequirementEdit> requirementEdits = new ArrayList<>();
        requirementEdits = requirementEditRepository.findByRequirementOrderByEditedDateDesc(reqId);
        return requirementEdits;
    }

    public RequirementEdit acceptEntryEditChange(Integer editId, RequirementEdit requirementEdit) {
        requirementEdit.setStatus(RequirementEditStatus.ACCEPTED);
        requirementEdit.setAcceptedDate(new Date());
        requirementEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
        requirementEdit = requirementEditRepository.save(requirementEdit);
        return requirementEdit;
    }

    public RequirementEdit rejectEntryEditChange(Integer editId, RequirementEdit requirementEdit) {
        requirementEdit.setStatus(RequirementEditStatus.REJECTED);
        requirementEdit.setRejectedDate(new Date());
        requirementEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
        requirementEdit = requirementEditRepository.save(requirementEdit);
        return requirementEdit;
    }

    public RequirementEdit approveEntryEdits(Integer specReqId, Integer editId, RequirementEdit requirementEdit) {
        requirementEdit.setStatus(RequirementEditStatus.FINAL);
        requirementEdit.setFinalDate(new Date());
        requirementEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
        requirementEdit = requirementEditRepository.save(requirementEdit);
        Requirement requirement1 = requirementRepository.findOne(requirementEdit.getRequirement());
        requirement1.setLatest(false);
        requirementRepository.save(requirement1);
        SpecRequirement specRequirement = specRequirementRepository.findOne(specReqId);
        if (requirement1 != null) {
            Requirement requirement = new Requirement();
            requirement.setName(requirementEdit.getEditedName());
            requirement.setDescription(requirementEdit.getEditedDescription());
            requirement.setVersion(requirement1.getVersion() + 1);
            requirement.setLatest(true);
            requirement.setType(requirement1.getType());
            requirement.setSpecification(specRequirement.getSpecification());
            requirement.setObjectNumber(requirement1.getObjectNumber());
            requirementRepository.save(requirement);
            specRequirement.setRequirement(requirement);
            specRequirementRepository.save(specRequirement);
            requirementEdit.setRequirements(requirement);
            copyAttributes(requirement1, requirement);
            copyDelivarables(requirement1, requirement);
            copyFolderStructure(requirement1, requirement);
        }
        return requirementEdit;
    }

    @Transactional
    public void copyFolderStructure(Requirement oldRequirement, Requirement newRequirement) {
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(oldRequirement.getId());
        for (RmObjectFile rmObjectFile : rmObjectFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileByRequirement(oldRequirement.getId(), rmObjectFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(rmObjectFile.getName());
                newRmObjectFile.setFileNo(rmObjectFile.getFileNo());
                newRmObjectFile.setFileType(rmObjectFile.getFileType());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newRequirement.getId());
                newRmObjectFile.setVersion(rmObjectFile.getVersion());
                newRmObjectFile.setSize(rmObjectFile.getSize());
                newRmObjectFile.setLatest(rmObjectFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                if (newRmObjectFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newRequirement.getId(), newRmObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newRequirement.getId(), newRmObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newRmObjectFile.getId();
                    saveFileToDisk(file, dir);
                    saveOldVersionItemFiles(oldRequirement, newRequirement, rmObjectFile);
                }
            }
            saveItemFileChildren(oldRequirement, newRequirement, rmObjectFile, newRmObjectFile);
        }

    }

    private void saveItemFileChildren(Requirement oldItem, Requirement newItem, RmObjectFile rmObjectFile, RmObjectFile plmRmObjectFile) {
        List<RmObjectFile> childrenFiles = rmObjectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(rmObjectFile.getId());
        for (RmObjectFile childrenFile : childrenFiles) {
            RmObjectFile newObjectFile = null;
            File file = getFileByRequirement(oldItem.getId(), childrenFile.getId());
            if (file != null) {
                newObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newObjectFile.setName(childrenFile.getName());
                newObjectFile.setFileNo(childrenFile.getFileNo());
                newObjectFile.setFileType(childrenFile.getFileType());
                newObjectFile.setCreatedBy(login.getPerson().getId());
                newObjectFile.setModifiedBy(login.getPerson().getId());
                newObjectFile.setObject(newItem.getId());
                newObjectFile.setVersion(childrenFile.getVersion());
                newObjectFile.setSize(childrenFile.getSize());
                newObjectFile.setLatest(childrenFile.getLatest());
                newObjectFile.setParentFile(plmRmObjectFile.getId());
                newObjectFile = rmObjectFileRepository.save(newObjectFile);
                if (newObjectFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newItem.getId(), newObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newItem.getId(), newObjectFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newObjectFile.getId();
                    saveFileToDisk(file, dir);
                    saveChildrenOldVersionItemFiles(oldItem, newItem, childrenFile, plmRmObjectFile);
                }
            }
            saveItemFileChildren(oldItem, newItem, childrenFile, newObjectFile);
        }

    }

    private void saveChildrenOldVersionItemFiles(Requirement oldItem, Requirement newItem, RmObjectFile itemFile, RmObjectFile plmItemFile) {
        List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldItem.getId(), itemFile.getFileNo());
        for (RmObjectFile oldVersionFile : oldVersionFiles) {
            RmObjectFile newItemFile = null;
            File file = getFileByRequirement(oldItem.getId(), oldVersionFile.getId());
            if (file != null) {
                newItemFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newItemFile.setName(oldVersionFile.getName());
                newItemFile.setFileNo(oldVersionFile.getFileNo());
                newItemFile.setFileType(oldVersionFile.getFileType());
                newItemFile.setCreatedBy(login.getPerson().getId());
                newItemFile.setModifiedBy(login.getPerson().getId());
                newItemFile.setObject(newItem.getId());
                newItemFile.setVersion(oldVersionFile.getVersion());
                newItemFile.setSize(oldVersionFile.getSize());
                newItemFile.setLatest(oldVersionFile.getLatest());
                newItemFile.setParentFile(plmItemFile.getId());
                newItemFile = rmObjectFileRepository.save(newItemFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newItem.getId(), newItemFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newItemFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }


    private void saveOldVersionItemFiles(Requirement oldSpecification, Requirement newSpecification, RmObjectFile rmObjectFile) {
        List<RmObjectFile> oldVersionFiles = rmObjectFileRepository.findByObjectAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldSpecification.getId(), rmObjectFile.getFileNo());
        for (RmObjectFile oldVersionFile : oldVersionFiles) {
            RmObjectFile newRmObjectFile = null;
            File file = getFileByRequirement(oldSpecification.getId(), oldVersionFile.getId());
            if (file != null) {
                newRmObjectFile = new RmObjectFile();
                Login login = sessionWrapper.getSession().getLogin();
                newRmObjectFile.setName(oldVersionFile.getName());
                newRmObjectFile.setFileNo(oldVersionFile.getFileNo());
                newRmObjectFile.setFileType(oldVersionFile.getFileType());
                newRmObjectFile.setCreatedBy(login.getPerson().getId());
                newRmObjectFile.setModifiedBy(login.getPerson().getId());
                newRmObjectFile.setObject(newSpecification.getId());
                newRmObjectFile.setVersion(oldVersionFile.getVersion());
                newRmObjectFile.setSize(oldVersionFile.getSize());
                newRmObjectFile.setLatest(oldVersionFile.getLatest());
                newRmObjectFile = rmObjectFileRepository.save(newRmObjectFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newSpecification.getId(), newRmObjectFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newRmObjectFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    private String getReplaceFileSystemPath(Integer projectId, Integer fileId) {
        String path = "";
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
        if (rmObjectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(projectId, rmObjectFile.getParentFile(), path);
        } else {
            path = File.separator + projectId;
        }
        return path;
    }

    private File getFileByRequirement(Integer reqId, Integer fileId) {
        checkNotNull(reqId);
        checkNotNull(fileId);
        Requirement requirement = requirementRepository.findOne(reqId);
        if (requirement == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(reqId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        }
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (rmObjectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, rmObjectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + rmObjectFile.getId();
        }
        return path;
    }

    @Transactional
    public void copyDelivarables(Requirement oldRequirement, Requirement newRequirement) {
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByRequirementAndObjectType(oldRequirement, PLMObjectType.ITEMREVISION.toString());
        if (requirementDeliverables.size() > 0) {
            for (RequirementDeliverable requirementDeliverable : requirementDeliverables) {
                RequirementDeliverable requirementDeliverable1 = (RequirementDeliverable) Utils.cloneObject(requirementDeliverable, RequirementDeliverable.class);
                requirementDeliverable1.setId(null);
                requirementDeliverable1.setRequirement(newRequirement);
                RequirementDeliverable requirementDeliverable2 = requirementDeliverableRepository.save(requirementDeliverable1);
            }
        }
    }

    private void copyAttributes(Requirement oldRequirement, Requirement newRequirement) {
        List<ObjectAttribute> oldRequirementAttributes = objectAttributeRepository.findByObjectId(oldRequirement.getId());
        if (oldRequirementAttributes.size() > 0) {
            for (ObjectAttribute objectAttribute : oldRequirementAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
                if (objectTypeAttribute != null && objectTypeAttribute.getObjectType().equals(ObjectType.valueOf("REQUIREMENTTYPE"))) {
                    RmObjectAttribute rmObjectAttribute1 = (RmObjectAttribute) Utils.cloneObject(objectAttribute, RmObjectAttribute.class);
                    rmObjectAttribute1.setId(new ObjectAttributeId(newRequirement.getId(), objectAttribute.getId().getAttributeDef()));
                    if (rmObjectAttribute1.getAttachmentValues().length > 0) {
                        List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(rmObjectAttribute1.getAttachmentValues()));
                        List<Integer> integers = new ArrayList<>();
                        for (AttributeAttachment attachment : attachments) {
                            AttributeAttachment attachment1 = new AttributeAttachment();
                            attachment1.setObjectId(newRequirement.getId());
                            attachment1.setAttributeDef(attachment.getAttributeDef());
                            attachment1.setExtension(attachment.getExtension());
                            attachment1.setAddedBy(attachment.getAddedBy());
                            attachment1.setObjectType(attachment.getObjectType());
                            attachment1.setAddedOn(new Date());
                            attachment1.setName(attachment.getName());
                            attachment1.setSize(attachment.getSize());
                            attachment1 = attributeAttachmentRepository.save(attachment1);
                            integers.add(attachment1.getId());
                        }
                        rmObjectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                    }
                    rmObjectAttributeRepository.save(rmObjectAttribute1);

                } else {
                    ObjectAttribute objectAttribute1 = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                    objectAttribute1.setId(new ObjectAttributeId(newRequirement.getId(), objectAttribute.getId().getAttributeDef()));
                    if (objectAttribute1.getAttachmentValues().length > 0) {
                        List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute1.getAttachmentValues()));
                        List<Integer> integers = new ArrayList<>();
                        for (AttributeAttachment attachment : attachments) {
                            AttributeAttachment attachment1 = new AttributeAttachment();
                            attachment1.setObjectId(newRequirement.getId());
                            attachment1.setAttributeDef(attachment.getAttributeDef());
                            attachment1.setExtension(attachment.getExtension());
                            attachment1.setAddedBy(attachment.getAddedBy());
                            attachment1.setObjectType(attachment.getObjectType());
                            attachment1.setAddedOn(new Date());
                            attachment1.setName(attachment.getName());
                            attachment1.setSize(attachment.getSize());
                            attachment1 = attributeAttachmentRepository.save(attachment1);
                            integers.add(attachment1.getId());
                        }
                        objectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                    }
                    objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
                }
            }
        }

    }

    @Transactional(readOnly = true)
    public List<Requirement> getRequirementVersions(Integer reqId) {
        Requirement requirement = requirementRepository.findOne(reqId);
        List<Requirement> requirements = requirementRepository.findByObjectNumberOrderByCreatedDateDesc(requirement.getObjectNumber());
        return requirements;
    }

    @Transactional(readOnly = true)
    public Page<Requirement> getType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        RequirementType type = requirementTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return requirementRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, RequirementType type) {
        if (type != null) {
            collector.add(type.getId());
            List<RequirementType> children = requirementTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(type.getId());
            for (RequirementType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public Integer getReqTypeItems(Integer itemTypeId) {
        RmObjectType objectType = rmObjectTypeRepository.findOne(itemTypeId);
        if (objectType.getObjectType().equals(ObjectType.valueOf("REQUIREMENTTYPE"))) {
            return requirementRepository.getRequirementsByType(objectType.getId()).size();
        } else {
            return specificationRepository.getSpecificationsByType(objectType.getId()).size();
        }

    }

    @Transactional
    public RmObjectFile updateFileName(Integer id, String newFileName) {
        checkNotNull(id);
        RmObjectFile reqFile = rmObjectFileRepository.findOne(id);
        if (reqFile == null) {
            throw new com.cassinisys.platform.exceptions.ResourceNotFoundException();
        } else {
            reqFile.setName(newFileName);
            reqFile = rmObjectFileRepository.save(reqFile);
        }
        return reqFile;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getReqItemDeliverables(Integer reqId, Pageable pageable, RequirementDeliverableCriteria criteria) {
        criteria.setRequirement(reqId);
        Predicate predicate = requirementItemDeliverablePredicateBuilder.build(criteria, com.cassinisys.plm.model.plm.QPLMItem.pLMItem);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        return items;
    }

    @Transactional
    public List<RequirementDeliverable> createRequirementDeliverables(Integer reqId, List<PLMItem> items) {
        List<RequirementDeliverable> requirementDeliverables = new ArrayList<>();
        String itemNames = null;
        String message = null;
        Requirement requirement = requirementRepository.findOne(reqId);
        for (PLMItem item : items) {
            RequirementDeliverable requirementDeliverable = new RequirementDeliverable();
            requirementDeliverable.setObjectId(item.getLatestRevision());
            requirementDeliverable.setObjectType(PLMObjectType.ITEMREVISION.toString());
            requirementDeliverable.setRequirement(requirement);
            requirementDeliverables.add(requirementDeliverable);
            if (itemNames == null) {
                itemNames = item.getItemNumber();
            } else {
                itemNames = itemNames + " , " + item.getItemNumber();
            }
        }
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        String mailSubject = requirement.getObjectNumber() + "  " + " Notification";
        message = "[" + itemNames + "]" + "deliverble(s) added by" + person.getFullName() + "" + requirement.getObjectNumber();
        specificationsService.sendReqSubscribeNotification(requirement, message, mailSubject);
        return requirementDeliverableRepository.save(requirementDeliverables);

    }

    @Transactional(readOnly = true)
    public List<PLMItem> getRequirementDeliverablesByReqId(Integer reqId) {
        List<PLMItem> plmItems = new ArrayList<>();
        Requirement requirement = requirementRepository.findOne(reqId);
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByRequirementAndObjectType(requirement, PLMObjectType.ITEMREVISION.toString());
        if (requirementDeliverables.size() > 0) {
            for (RequirementDeliverable requirementDeliverable : requirementDeliverables) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(requirementDeliverable.getObjectId());
                PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                plmItems.add(plmItem);
            }
        }
        return plmItems;
    }

    @Transactional
    public void deleteRequirementDelivarable(Integer reqId, Integer itemId) {
        Requirement requirement = requirementRepository.findOne(reqId);
        RequirementDeliverable requirementDeliverable = requirementDeliverableRepository.findByRequirementAndObjectIdAndObjectType(requirement, itemId, PLMObjectType.ITEMREVISION.toString());
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        String message = null;
        String mailSubject = null;
        if (plmItem != null) {
            mailSubject = requirement.getObjectNumber() + " " + "Notification";
            message = plmItem.getItemNumber() + "delivarable deleted by" + person.getFullName() + "to (" + requirement.getObjectNumber() + ")";
            if (requirementDeliverable != null) {
                requirementDeliverableRepository.delete(requirementDeliverable.getId());
            }
            specificationsService.sendReqSubscribeNotification(requirement, message, mailSubject);

        }
        PLMProject project = projectRepository.findOne(itemId);
        if (project != null) {
            if (requirementDeliverable != null) {
                requirementDeliverableRepository.delete(requirementDeliverable.getId());
            }
        }
        PLMActivity activity = activityRepository.findOne(itemId);
        if (activity != null) {
            if (requirementDeliverable != null) {
                requirementDeliverableRepository.delete(requirementDeliverable.getId());
            }
        }
        PLMTask task = taskRepository.findOne(itemId);
        if (task != null) {
            if (requirementDeliverable != null) {
                requirementDeliverableRepository.delete(requirementDeliverable.getId());
            }
        }
    }

    @Transactional(readOnly = true)
    public List<RmObjectAttribute> getRmObjectUsedAttributes(Integer attributeId) {
        List<RmObjectAttribute> rmObjectAttributes = rmObjectAttributeRepository.findByAttributeId(attributeId);
        return rmObjectAttributes;
    }

    @Transactional(readOnly = true)
    public DetailsCount getReqDetails(Integer reqId) {
        DetailsCount detailsCount = new DetailsCount();
        Requirement requirement = requirementRepository.findOne(reqId);
        List<RmObjectFile> files = rmObjectFileRepository.findByObjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(reqId, "FILE");
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByRequirementAndObjectType(requirement, PLMObjectType.ITEMREVISION.toString());
        detailsCount.setFiles(files.size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(reqId));
        detailsCount.setDeliverables(requirementDeliverables.size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public void generateZipFile(Integer reqId, HttpServletResponse response) throws FileNotFoundException, IOException {
        Requirement requirement = requirementRepository.findOne(reqId);
        List<RmObjectFile> rmObjectFiles = rmObjectFileRepository.findByObjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(reqId);
        ArrayList<String> fileList = new ArrayList<>();
        rmObjectFiles.forEach(rmObjectFile -> {
            File file = specificationsService.getRmObjectFile(reqId, rmObjectFile.getId());
            fileList.add(file.getAbsolutePath());
        });
        String zipName = requirement.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "RM",reqId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional(readOnly = true)
    public Page<Requirement> freeTextSearch(Pageable pageable, RequirementSearchCriteria criteria) {
        Predicate predicate = requirementSearchPredicateBuilder.getDefaultPredicate(criteria, QRequirement.requirement);
        return requirementRepository.findAll(predicate, pageable);
    }

    @Transactional
    public Page<Requirement> getRequirements(Pageable pageable) {
        Page<Requirement> requirements = requirementRepository.findByLatestTrue(pageable);
        return requirements;
    }


    @Transactional
    public PLMWorkflow attachReqWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        Requirement requirement = requirementRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (requirement != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.REQUIREMENT, requirement.getId(), wfDef);
            requirement.setWorkflow(workflow.getId());
            requirementRepository.save(requirement);
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        RequirementType requirementType = requirementTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (requirementType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, requirementType.getParentType(), type);
        }
        return workflowDefinitions;
    }


    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        RequirementType requirementType = requirementTypeRepository.findOne(typeId);
        if (requirementType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository.findByWorkflowType(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                            definitions.add(workflowDefinition);
                        }
                    });
                }
            }
            if (requirementType.getParentType() != null) {
                getWorkflowsFromHierarchy(definitions, requirementType.getParentType(), type);
            }
        }
    }

}