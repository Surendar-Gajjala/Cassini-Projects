package com.cassinisys.plm.service.wf;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.filtering.WorkflowDefinitionCriteria;
import com.cassinisys.plm.filtering.WorkflowDefinitionPredicateBuilder;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.WorkflowEventDto;
import com.cassinisys.plm.model.wf.dto.WorkflowRevisionDto;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.plm.ItemServiceException;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 19-05-2017.
 */
@Service
public class PLMWorkflowDefinitionService implements CrudService<PLMWorkflowDefinition, Integer> {

    @Autowired
    private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;

    @Autowired
    private PLMWorkflowDefinitionStatusService plmWorkflowDefinitionStatusService;

    @Autowired
    private PLMWorkflowDefinitionTransitionService plmWorkflowDefinitionTransitionService;

    @Autowired
    private PLMWorkflowDefinitionTransitionRepository plmWorkflowDefinitionTransitionRepository;

    @Autowired
    private PLMWorkflowDefinitionStartRepository plmWorkflowDefinitionStartRepository;

    @Autowired
    private PLMWorkflowDefinitionFinishRepository plmWorkflowDefinitionFinishRepository;

    @Autowired
    private PLMWorkflowAttributeRepository workflowAttributeRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private WorkflowDefinitionPredicateBuilder predicateBuilder;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository plmWorkflowDefinitionStatusRepository;

    @Autowired
    private WorkflowDefinitionMasterRepository workflowDefinitionMasterRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private WorkflowRevisionHistoryRepository workflowRevisionHistoryRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PLMWorkflowDefinitionEventRepository plmWorkflowDefinitionEventRepository;
    @Autowired
    private PLMWorkflowEventRepository plmWorkflowEventRepository;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private PLMWorkflowActivityFormFieldsRepository workflowActivityFormFieldsRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmWorkflowDefinition,'create')")
    public PLMWorkflowDefinition create(PLMWorkflowDefinition plmWorkflowDefinition) {
        PLMWorkflowDefinitionMaster plmWorkflowDefinitionMaster = new PLMWorkflowDefinitionMaster();
        if (!plmWorkflowDefinition.getNextRevision()) {
            if (plmWorkflowDefinition.getNumber() != null &&
                    workflowDefinitionMasterRepository.findByNumber(plmWorkflowDefinition.getNumber()) != null) {
                throw new CassiniException(plmWorkflowDefinition.getNumber() + " : " +
                        messageSource.getMessage("workflow_number_already_exists", null, "Workflow number already exists", LocaleContextHolder.getLocale()));
            }

            checkNotNull(plmWorkflowDefinition);
            if (plmWorkflowDefinition.getId() == null &&
                    plmWorkflowDefinitionRepository.findByName(plmWorkflowDefinition.getName()).size() > 0) {
                throw new CassiniException(plmWorkflowDefinition.getName() + " : " +
                        messageSource.getMessage("workflow_already_exists", null, "Workflow already exists", LocaleContextHolder.getLocale()));
            }

            plmWorkflowDefinitionMaster.setNumber(plmWorkflowDefinition.getNumber());
            autoNumberService.saveNextNumber(plmWorkflowDefinition.getWorkflowType().getNumberSource().getId(), plmWorkflowDefinition.getNumber());
            plmWorkflowDefinitionMaster = workflowDefinitionMasterRepository.save(plmWorkflowDefinitionMaster);
        } else {
            plmWorkflowDefinitionMaster = workflowDefinitionMasterRepository.findOne(plmWorkflowDefinition.getMaster().getId());

        }

        plmWorkflowDefinition.setId(null);

        PLMWorkflowDefinitionStart start = plmWorkflowDefinition.getStart();
        start.setId(null);

        PLMWorkflowDefinitionFinish finish = plmWorkflowDefinition.getFinish();
        finish.setId(null);

        plmWorkflowDefinition.setStart(null);
        plmWorkflowDefinition.setFinish(null);
        plmWorkflowDefinition.setReleased(false);
        plmWorkflowDefinition.setReleasedDate(null);
        plmWorkflowDefinition.setMaster(plmWorkflowDefinitionMaster);

        PLMWorkflowType workflowType = workflowTypeRepository.findOne(plmWorkflowDefinition.getWorkflowType().getId());
        Lov lov = workflowType.getRevisionSequence();
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(workflowType.getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        plmWorkflowDefinition.setLifeCyclePhase(lifeCyclePhase);
        if (!plmWorkflowDefinition.getNextRevision()) {
            plmWorkflowDefinition.setRevision("-");
        }
        autoNumberService.saveNextNumber(plmWorkflowDefinition.getWorkflowType().getNumberSource().getId(), plmWorkflowDefinition.getNumber());
        PLMWorkflowDefinition plmWorkflowDef = plmWorkflowDefinitionRepository.save(plmWorkflowDefinition);
        plmWorkflowDefinitionMaster.setLatestRevision(plmWorkflowDef.getId());
        workflowDefinitionMasterRepository.save(plmWorkflowDefinitionMaster);
        start.setWorkflow(plmWorkflowDef.getId());
        start = plmWorkflowDefinitionStartRepository.save(start);
        plmWorkflowDef.setStart(start);

        finish.setWorkflow(plmWorkflowDef.getId());
        finish = plmWorkflowDefinitionFinishRepository.save(finish);
        plmWorkflowDef.setFinish(finish);

        Map<String, PLMWorkflowDefinitionStatus> map = new HashMap<>();
        map.put(start.getDiagramId(), start);
        map.put(finish.getDiagramId(), finish);

        List<PLMWorkflowDefinitionStatus> savedStatuses = new ArrayList<>();
        List<PLMWorkflowDefinitionStatus> statuses = plmWorkflowDefinition.getStatuses();
        statuses.forEach(s -> {
            if (s.getType() != WorkflowStatusType.START && s.getType() != WorkflowStatusType.FINISH) {
                s.setId(null);
                s.setWorkflow(plmWorkflowDef.getId());
                s = plmWorkflowDefinitionStatusService.create(s);
                savedStatuses.add(s);
                map.put(s.getDiagramId(), s);
            }
        });

        List<PLMWorkflowDefinitionTransition> savedTransitions = new ArrayList<>();
        List<PLMWorkflowDefinitionTransition> transitions = plmWorkflowDefinition.getTransitions();
        transitions.forEach(t -> {
            t.setId(null);
            t.setWorkflow(plmWorkflowDef.getId());
            t = createTransition(map, t);
            if (t != null) {
                savedTransitions.add(t);
            }
        });
        plmWorkflowDef.setStatuses(savedStatuses);
        plmWorkflowDef.setTransitions(savedTransitions);

        PLMWorkflowRevisionHistory workflowRevisionHistory = new PLMWorkflowRevisionHistory();
        workflowRevisionHistory.setWorkflow(plmWorkflowDef.getId());
        workflowRevisionHistory.setOldStatus(plmWorkflowDef.getLifeCyclePhase());
        workflowRevisionHistory.setNewStatus(plmWorkflowDef.getLifeCyclePhase());
        workflowRevisionHistory.setTimestamp(new Date());
        workflowRevisionHistory.setUpdatedBy(plmWorkflowDef.getCreatedBy());
        workflowRevisionHistory = workflowRevisionHistoryRepository.save(workflowRevisionHistory);

        return plmWorkflowDefinitionRepository.save(plmWorkflowDef);
    }

    private PLMWorkflowDefinitionTransition createTransition(Map<String, PLMWorkflowDefinitionStatus> map,
                                                             PLMWorkflowDefinitionTransition t) {
        String fromDiagramId = t.getFromStatusDiagramId();
        String toDiagramId = t.getToStatusDiagramId();
        if (fromDiagramId != null && toDiagramId != null) {
            PLMWorkflowDefinitionStatus fromStatus = map.get(fromDiagramId);
            PLMWorkflowDefinitionStatus toStatus = map.get(toDiagramId);
            if (fromStatus != null && toStatus != null) {
                t.setFromStatus(fromStatus.getId());
                t.setToStatus(toStatus.getId());
                return plmWorkflowDefinitionTransitionService.create(t);
            }
        }
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmWorkflowDefinition.id ,'edit')")
    public PLMWorkflowDefinition update(PLMWorkflowDefinition plmWorkflowDefinition) {
        checkNotNull(plmWorkflowDefinition);
        checkNotNull(plmWorkflowDefinition.getId());
        //return create(plmWorkflowDefinition);

        Map<String, PLMWorkflowDefinitionStatus> map = new HashMap<>();
        map.put(plmWorkflowDefinition.getStart().getDiagramId(), plmWorkflowDefinition.getStart());
        map.put(plmWorkflowDefinition.getFinish().getDiagramId(), plmWorkflowDefinition.getFinish());

        List<PLMWorkflowDefinitionStatus> savedStatuses = new ArrayList<>();
        List<PLMWorkflowDefinitionStatus> statuses = plmWorkflowDefinition.getStatuses();
        statuses.forEach(s -> {
            if (s.getType() != WorkflowStatusType.START && s.getType() != WorkflowStatusType.FINISH) {
                s.setWorkflow(plmWorkflowDefinition.getId());
                s = plmWorkflowDefinitionStatusService.create(s);
                savedStatuses.add(s);
                map.put(s.getDiagramId(), s);
            }
        });

        List<PLMWorkflowDefinitionTransition> savedTransitions = new ArrayList<>();
        List<PLMWorkflowDefinitionTransition> transitions = plmWorkflowDefinition.getTransitions();
        transitions.forEach(t -> {
            t.setWorkflow(plmWorkflowDefinition.getId());
            t = createTransition(map, t);
            if (t != null) {
                savedTransitions.add(t);
            }
        });
        plmWorkflowDefinition.setStatuses(savedStatuses);
        plmWorkflowDefinition.setTransitions(savedTransitions);

        return plmWorkflowDefinitionRepository.save(plmWorkflowDefinition);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        checkNotNull(id);
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(id);
        if (recentlyVisiteds.size() > 0) {
            for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
                recentlyVisitedRepository.delete(recentlyVisited.getId());
            }
        }
        plmWorkflowDefinitionRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMWorkflowDefinition get(Integer id) {
        checkNotNull(id);
        PLMWorkflowDefinition plmWorkflowDefinition = plmWorkflowDefinitionRepository.findOne(id);
        if (plmWorkflowDefinition == null) {
            throw new ResourceNotFoundException();
        }
        List<PLMWorkflowDefinitionStatus> statuses = plmWorkflowDefinitionStatusService.getByWorkflow(id);
        List<PLMWorkflowDefinitionStatus> filtered = new ArrayList<>();
        statuses.forEach(s -> {
            if (s.getType() != WorkflowStatusType.UNDEFINED) {
                filtered.add(s);
            }
        });
        plmWorkflowDefinition.setStatuses(filtered);
        plmWorkflowDefinition.setTransitions(plmWorkflowDefinitionTransitionService.getByWorkflow(id));
        return plmWorkflowDefinition;
    }

    public List<PLMWorkflowDefinition> getByName(String workflowName) {
        return plmWorkflowDefinitionRepository.findByName(workflowName);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWorkflowDefinition> getAll() {
        List<PLMWorkflowDefinition> list = new ArrayList<>();
        String[] ecoWorkflows = null;
        List<PLMWorkflowDefinition> workflows = plmWorkflowDefinitionRepository.findAll();
        for (PLMWorkflowDefinition workflowDefinition : workflows) {
            if (workflowDefinition.getAssignableTo() != null) {
                ecoWorkflows = workflowDefinition.getAssignableTo();
                for (String ecoWorkflow : ecoWorkflows) {
                    if (ecoWorkflow.contains("ECO")) {
                        list.add(workflowDefinition);
                    }
                }
            }
        }
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignable("ECOS");
        List<PLMWorkflowDefinition> workflowDefinitions = workFlowDefinitionRepository.findByWorkflowType(workflowType);
        if (workflowDefinitions.size() > 0) {
            list.addAll(workflowDefinitions);
        }
        return list;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMWorkflowDefinition> getAll(Pageable pageable) {
        List<PLMWorkflowDefinition> list = new ArrayList<>();
        Page<PLMWorkflowDefinition> workflows = plmWorkflowDefinitionRepository.findAll(pageable);
       /* workflows.forEach(w -> list.add(get(w.getId())));

        Page<PLMWorkflowDefinition> workflowDefinitions = new PageImpl<PLMWorkflowDefinition>(list, pageable, list.size());

*/
        for (PLMWorkflowDefinition workflowDefinition : workflows.getContent()) {
            workflowDefinition = get(workflowDefinition.getId());
        }
        return workflows;
    }

    @Transactional
    public PLMWorkflowAttribute createWorkflowAttribute(PLMWorkflowAttribute attribute) {
        return workflowAttributeRepository.save(attribute);
    }

    @Transactional
    public PLMWorkflowAttribute updateWorkflowAttribute(PLMWorkflowAttribute attribute) {
        PLMWorkflowAttribute oldValue = workflowAttributeRepository.findByWorkflowAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        attribute = workflowAttributeRepository.save(attribute);
        return attribute;
    }

    @Transactional
    public void saveAttributes(List<PLMWorkflowAttribute> attributes) {
        for (PLMWorkflowAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null ||
                    attribute.getStringValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                workflowAttributeRepository.save(attribute);
            }
        }
    }

    public List<PLMWorkflowAttribute> getWorkflowAttributes(Integer workflowId) {
        return workflowAttributeRepository.findByWorkflowIdIn(workflowId);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMWorkflowDefinition> freeTextSearch(Pageable pageable, WorkflowDefinitionCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMWorkflowDefinition.pLMWorkflowDefinition);
        return plmWorkflowDefinitionRepository.findAll(predicate, pageable);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMWorkflowDefinition> getWorkflowsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        PLMWorkflowType type = workflowTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        Page<PLMWorkflowDefinition> plmecos = null;
        if (ids.size() > 0) {
            return plmecos = plmWorkflowDefinitionRepository.getByWorkflowTypeIds(ids, pageable);
        } else {
            return new PageImpl<PLMWorkflowDefinition>(new ArrayList<>(), pageable, 0);
        }

    }

    private void collectHierarchyTypeIds(List<Integer> collector, PLMWorkflowType type) {
        if (type != null) {
            collector.add(type.getId());
            List<PLMWorkflowType> children = workflowTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (PLMWorkflowType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional
    public PLMWorkflowDefinition saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        PLMWorkflowDefinition workflowDefinition = plmWorkflowDefinitionRepository.findOne(objectId);
        if (workflowDefinition != null) {
            PLMWorkflowAttribute workflowAttribute = new PLMWorkflowAttribute();
            workflowAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, workflowAttribute);

        }
        return workflowDefinition;
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteWorkflow(Integer id) {
        PLMWorkflowDefinitionMaster workflowDefinitionMaster = workflowDefinitionMasterRepository.findOne(id);
        List<PLMWorkflowDefinition> plmWorkflowDefinitions = plmWorkflowDefinitionRepository.findByMaster(workflowDefinitionMaster);
        for (PLMWorkflowDefinition plmWorkflowDefinition : plmWorkflowDefinitions) {
            List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(plmWorkflowDefinition.getId());
            if (recentlyVisiteds.size() > 0) {
                for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
                    recentlyVisitedRepository.delete(recentlyVisited.getId());
                }
            }
        }

        workflowDefinitionMasterRepository.delete(workflowDefinitionMaster.getId());
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWorkflowDefinition> getAssignedWorkflows(String type) {
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignable(type);
        List<PLMWorkflowDefinition> workflowDefinitions = plmWorkflowDefinitionRepository.findByWorkflowType(workflowType);
        return workflowDefinitions;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public PLMWorkflowType getAssignedType(String type) {
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignable(type);
        return workflowType;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinitionStatus> getNormalWorkflowStatuses(Integer id) {
        List<PLMWorkflowDefinitionStatus> statuses = plmWorkflowDefinitionStatusRepository.getNormalWorkflowStatuses(id);

        return statuses;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinitionStatus> getWorkflowDefinitionStatuses(Integer id) {
        List<PLMWorkflowDefinitionStatus> statuses = plmWorkflowDefinitionStatusRepository.getWorkflowStatuses(id);

        return statuses;
    }

    /* ---------------------------New Workflow Revision Methods ----------------*/

    public Page<WorkflowRevisionDto> getAllWorkflows(Pageable pageable, WorkflowDefinitionCriteria dcrCriteria) {
        Predicate predicate = predicateBuilder.build(dcrCriteria, QPLMWorkflowDefinition.pLMWorkflowDefinition);
        Page<PLMWorkflowDefinition> definations = workFlowDefinitionRepository.findAll(predicate, pageable);
        List<WorkflowRevisionDto> workflowRevisionDtos = new LinkedList<>();
        for (PLMWorkflowDefinition workflowDefinition : definations) {
            WorkflowRevisionDto workflowRevisionDto = new WorkflowRevisionDto();
            workflowRevisionDto.setNumber(workflowDefinition.getMaster().getNumber());
            workflowRevisionDto.setName(workflowDefinition.getName());
            workflowRevisionDto.setDescription(workflowDefinition.getDescription());
            workflowRevisionDto.setReleasedDate(workflowDefinition.getReleasedDate());
            workflowRevisionDto.setCreatedBy(personRepository.findOne(workflowDefinition.getCreatedBy()).getFullName());
            workflowRevisionDto.setModifiedBy(personRepository.findOne(workflowDefinition.getModifiedBy()).getFullName());
            workflowRevisionDto.setCreatedDate(workflowDefinition.getCreatedDate());
            workflowRevisionDto.setModifiedDate(workflowDefinition.getModifiedDate());
            workflowRevisionDto.setLatestRevision(workflowDefinition.getId());
            workflowRevisionDto.setLifeCyclePhase(workflowDefinition.getLifeCyclePhase());
            workflowRevisionDto.setRevision(workflowDefinition.getRevision());
            workflowRevisionDto.setInstances(workflowDefinition.getMaster().getInstances());
            workflowRevisionDto.setType(workflowDefinition.getWorkflowType().getName());
            workflowRevisionDto.setId(workflowDefinition.getMaster().getId());
            workflowRevisionDto.setObjectType(workflowDefinition.getObjectType().toString());
            workflowRevisionDtos.add(workflowRevisionDto);
        }

        return new PageImpl<WorkflowRevisionDto>(workflowRevisionDtos, pageable, definations.getTotalElements());
    }


    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','plmworkflowdefinition')")
    public PLMWorkflowDefinition promoteDefinition(Integer id) {
        PLMWorkflowDefinition definition = workFlowDefinitionRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(definition.getWorkflowType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = definition.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        String message = null;
        String mailSubject = null;
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = definition.getLifeCyclePhase();
            index++;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                definition.setLifeCyclePhase(lifeCyclePhase);
                if (lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    definition.setReleased(true);
                    definition.setReleasedDate(new Date());
                    if (definition.getRevision().trim().equalsIgnoreCase("-")) {
                        definition.setRevision(getNextRevisionSequence(definition));
                    }
                    PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(definition.getMaster().getId());
                    definitionMaster.setLatestReleasedRevision(definition.getId());
                    workflowDefinitionMasterRepository.save(definitionMaster);
                }
                definition = workFlowDefinitionRepository.save(definition);

                PLMWorkflowRevisionHistory workflowRevisionHistory = new PLMWorkflowRevisionHistory();
                workflowRevisionHistory.setWorkflow(definition.getId());
                workflowRevisionHistory.setOldStatus(oldStatus);
                workflowRevisionHistory.setNewStatus(definition.getLifeCyclePhase());
                workflowRevisionHistory.setTimestamp(new Date());
                workflowRevisionHistory.setUpdatedBy(definition.getCreatedBy());
                workflowRevisionHistory = workflowRevisionHistoryRepository.save(workflowRevisionHistory);

            }
        }
        return definition;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','plmworkflowdefinition')")
    public PLMWorkflowDefinition demoteDefinition(Integer id) {
        PLMWorkflowDefinition definition = workFlowDefinitionRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(definition.getWorkflowType().getLifecycle().getId());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = definition.getLifeCyclePhase().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = lifeCyclePhases.stream().
                filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).
                findFirst().get();
        Integer index = lifeCyclePhases.indexOf(lifeCyclePhase2);
        String message = null;
        String mailSubject = null;
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = definition.getLifeCyclePhase();
            index--;
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                definition.setLifeCyclePhase(lifeCyclePhase);
                if (!lifeCyclePhase.getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    definition.setReleased(false);
                    definition.setReleasedDate(null);
                }
                definition = workFlowDefinitionRepository.save(definition);

                PLMWorkflowRevisionHistory workflowRevisionHistory = new PLMWorkflowRevisionHistory();
                workflowRevisionHistory.setWorkflow(definition.getId());
                workflowRevisionHistory.setOldStatus(oldStatus);
                workflowRevisionHistory.setNewStatus(definition.getLifeCyclePhase());
                workflowRevisionHistory.setTimestamp(new Date());
                workflowRevisionHistory.setUpdatedBy(definition.getCreatedBy());
                workflowRevisionHistory = workflowRevisionHistoryRepository.save(workflowRevisionHistory);

            }
        }
        return definition;
    }


    @Transactional
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMWorkflowDefinition reviseDefinition(Integer id, PLMWorkflowDefinition workflowDefinition) {
        PLMWorkflowDefinition revision = workFlowDefinitionRepository.findOne(id);
        if (revision != null) {
            return reviseWorkflowDefinition(revision, null, workflowDefinition);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PLMWorkflowDefinition reviseWorkflowDefinition(PLMWorkflowDefinition revision, String nextRev, PLMWorkflowDefinition workflowDefinition) {
        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(revision.getMaster().getId());
        if (nextRev == null) {
            nextRev = getNextRevisionSequence(revision);
        }
        if (nextRev != null) {
            PLMWorkflowDefinition copy = createNextRev(revision, nextRev, workflowDefinition);
            return copy;
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    public String getNextRevisionSequence(PLMWorkflowDefinition workflowDefinition) {
        return getNextRevFromLov(workflowDefinition, workflowDefinition.getRevision());
    }

    private String getNextRevFromLov(PLMWorkflowDefinition workflowDefinition, String currRev) {
        String nextRev = null;
        Lov lov = workflowDefinition.getWorkflowType().getRevisionSequence();
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(currRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex != values.length - 1) {
            nextRev = values[lastIndex + 1];
        }

        PLMWorkflowDefinition found = workFlowDefinitionRepository.findByMasterAndRevision(workflowDefinition.getMaster(), nextRev);
        if (found != null) {
            return getNextRevFromLov(workflowDefinition, nextRev);
        }

        return nextRev;

    }

    @PostAuthorize("hasPermission(returnObject,'view')")
    private PLMWorkflowDefinition createNextRev(PLMWorkflowDefinition definition, String nextRev, PLMWorkflowDefinition workflowDefinition) {
        workflowDefinition.setReleasedDate(null);
        workflowDefinition.setReleased(false);
        workflowDefinition.setRevision(nextRev);
        workflowDefinition.setNextRevision(true);
        workflowDefinition.setInstances(0);
        workflowDefinition = create(workflowDefinition);

        return workflowDefinition;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMWorkflowDefinition> getWorkflowRevisionHistory(Integer id) {
        PLMWorkflowDefinitionMaster definitionMaster = workflowDefinitionMasterRepository.findOne(id);
        List<PLMWorkflowDefinition> definitions = workFlowDefinitionRepository.getByMasterOrderByCreatedDateDesc(definitionMaster);
        definitions.forEach(revision -> {
            List<PLMWorkflowRevisionHistory> history = workflowRevisionHistoryRepository.findByWorkflowOrderByTimestampDesc(revision.getId());
            revision.setStatusHistory(history);
        });
        return definitions;
    }

    @Transactional(readOnly = true)
    public Page<PLMWorkflowDefinition> getWorkflowDefsByType(Pageable pageable, WorkflowDefinitionCriteria workflowDefinition) {
        Predicate predicate = predicateBuilder.getPredicates(workflowDefinition, QPLMWorkflowDefinition.pLMWorkflowDefinition);
        return workFlowDefinitionRepository.findAll(predicate, pageable);
    }

    public Page<PLMWorkflowDefinition> getAllWorkflowDefs(Pageable pageable, WorkflowDefinitionCriteria dcrCriteria) {
        Predicate predicate = predicateBuilder.build(dcrCriteria, QPLMWorkflowDefinition.pLMWorkflowDefinition);
        return workFlowDefinitionRepository.findAll(predicate, pageable);
    }


    @Transactional(readOnly = true)
    public List<WorkflowEventDto> getWorkflowEvents(Integer id) {
        List<WorkflowEventDto> eventsDto = new LinkedList<>();
        List<PLMWorkflowEvent> workflowEvents = plmWorkflowEventRepository.findByWorkflowOrderByIdAsc(id);
        HashMap<String, WorkflowEventDto> hashMap = new LinkedHashMap<>();
        workflowEvents.forEach(plmWorkflowEvent -> {
            WorkflowEventDto workflowEventDto = hashMap.containsKey(plmWorkflowEvent.getEventType()) ? hashMap.get(plmWorkflowEvent.getEventType()) : new WorkflowEventDto();
            workflowEventDto.getWorkflowEvents().add(plmWorkflowEvent);
            workflowEventDto.setEventType(plmWorkflowEvent.getEventType());
            hashMap.put(plmWorkflowEvent.getEventType(), workflowEventDto);
        });

        if (workflowEvents.size() > 0) {
            eventsDto = hashMap.values().stream().collect(Collectors.toCollection(ArrayList::new));
        }

        return eventsDto;
    }

    @Transactional
    public PLMWorkflowEvent createWorkflowEvent(Integer id, PLMWorkflowEvent workflowEvent) {
        PLMWorkflowEvent existEvent = null;
        if (workflowEvent.getActivity() != null) {
            existEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getActivity(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null) {
                throw new CassiniException("[ " + workflowEvent.getEventType() + " - " + workflowEvent.getActivity().getName() + " - " + workflowEvent.getActionType() + " ] event already exist");
            }
        } else {
            existEvent = plmWorkflowEventRepository.findByWorkflowAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null) {
                throw new CassiniException("[ " + workflowEvent.getEventType() + " - " + workflowEvent.getActionType() + " ] event already exist");
            }
        }
        return plmWorkflowEventRepository.save(workflowEvent);
    }

    @Transactional
    public PLMWorkflowEvent updateWorkflowEvent(Integer id, PLMWorkflowEvent workflowEvent) {
        PLMWorkflowEvent existEvent = null;
        if (workflowEvent.getActivity() != null) {
            existEvent = plmWorkflowEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getActivity(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null && !existEvent.getId().equals(workflowEvent.getId())) {
                throw new CassiniException(workflowEvent.getEventType() + " - " + workflowEvent.getActivity().getName() + " - " + workflowEvent.getActionType() + " event already exist");
            }
        } else {
            existEvent = plmWorkflowEventRepository.findByWorkflowAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null && !existEvent.getId().equals(workflowEvent.getId())) {
                throw new CassiniException(workflowEvent.getEventType() + " - " + workflowEvent.getActionType() + " event already exist");
            }
        }
        return plmWorkflowEventRepository.save(workflowEvent);
    }

    @Transactional
    public void deleteWorkflowEvent(Integer id, Integer eventId) {
        plmWorkflowEventRepository.delete(eventId);
    }

    @Transactional(readOnly = true)
    public List<WorkflowEventDto> getWorkflowDefinitionEvents(Integer id) {
        List<WorkflowEventDto> eventsDto = new LinkedList<>();
        List<PLMWorkflowDefinitionEvent> workflowEvents = plmWorkflowDefinitionEventRepository.findByWorkflowOrderByIdAsc(id);
        HashMap<String, WorkflowEventDto> hashMap = new LinkedHashMap<>();
        workflowEvents.forEach(plmWorkflowEvent -> {
            WorkflowEventDto workflowEventDto = hashMap.containsKey(plmWorkflowEvent.getEventType()) ? hashMap.get(plmWorkflowEvent.getEventType()) : new WorkflowEventDto();
            workflowEventDto.getWorkflowDefinitionEvents().add(plmWorkflowEvent);
            workflowEventDto.setEventType(plmWorkflowEvent.getEventType());
            hashMap.put(plmWorkflowEvent.getEventType(), workflowEventDto);
        });

        if (workflowEvents.size() > 0) {
            eventsDto = hashMap.values().stream().collect(Collectors.toCollection(ArrayList::new));
        }

        return eventsDto;
    }

    @Transactional
    public PLMWorkflowDefinitionEvent createWorkflowDefinitionEvent(Integer id, PLMWorkflowDefinitionEvent workflowEvent) {
        PLMWorkflowDefinitionEvent existEvent = null;
        if (workflowEvent.getActivity() != null) {
            existEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getActivity(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null) {
                throw new CassiniException("[ " + workflowEvent.getEventType() + " - " + workflowEvent.getActivity().getName() + " - " + workflowEvent.getActionType() + " ] event already exist");
            }
        } else {
            existEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null) {
                throw new CassiniException("[ " + workflowEvent.getEventType() + " - " + workflowEvent.getActionType() + " ] event already exist");
            }
        }
        return plmWorkflowDefinitionEventRepository.save(workflowEvent);
    }

    @Transactional
    public PLMWorkflowDefinitionEvent updateWorkflowDefinitionEvent(Integer id, PLMWorkflowDefinitionEvent workflowEvent) {
        PLMWorkflowDefinitionEvent existEvent = null;
        if (workflowEvent.getActivity() != null) {
            existEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndActivityAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getActivity(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null && !existEvent.getId().equals(workflowEvent.getId())) {
                throw new CassiniException(workflowEvent.getEventType() + " - " + workflowEvent.getActivity().getName() + " - " + workflowEvent.getActionType() + " event already exist");
            }
        } else {
            existEvent = plmWorkflowDefinitionEventRepository.findByWorkflowAndEventTypeAndActionType(workflowEvent.getWorkflow(), workflowEvent.getEventType(), workflowEvent.getActionType());
            if (existEvent != null && !existEvent.getId().equals(workflowEvent.getId())) {
                throw new CassiniException(workflowEvent.getEventType() + " - " + workflowEvent.getActionType() + " event already exist");
            }
        }
        return plmWorkflowDefinitionEventRepository.save(workflowEvent);
    }

    @Transactional
    public void deleteWorkflowDefinitionEvent(Integer id, Integer eventId) {
        plmWorkflowDefinitionEventRepository.delete(eventId);
    }


    @Transactional
    public PLMWorkflowDefinition copyWorkflowDefinitionEvents(Integer id, PLMWorkflowDefinition workflowDefinition) {
        CassiniObject cassiniObject = objectRepository.findById(workflowDefinition.getWorkflowType().getAssignedType());
        PLMWorkflowDefinition definition = plmWorkflowDefinitionRepository.findOne(id);
        CassiniObject definitionTypeObject = objectRepository.findById(definition.getWorkflowType().getAssignedType());
        List<PLMWorkflowDefinitionEvent> workflowDefinitionEvents = plmWorkflowDefinitionEventRepository.findByWorkflowOrderByIdAsc(id);
        List<PLMWorkflowDefinitionStatus> workflowDefinitionStatuses = plmWorkflowDefinitionStatusRepository.findByWorkflow(id);

        for (PLMWorkflowDefinitionEvent workflowDefinitionEvent : workflowDefinitionEvents) {
            if (workflowDefinitionEvent.getActionType().equals("SetLifecyclePhase")) {
                if (cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.ECOTYPE.toString()))
                        || cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.DCOTYPE.toString()))
                        || cassiniObject.getObjectType().equals(ObjectType.valueOf(PLMObjectType.MCOTYPE.toString()))) {
                    PLMWorkflowDefinitionEvent workflowEvent = new PLMWorkflowDefinitionEvent();
                    workflowEvent.setWorkflow(workflowDefinition.getId());
                    workflowEvent.setEventType(workflowDefinitionEvent.getEventType());
                    workflowEvent.setActionType(workflowDefinitionEvent.getActionType());
                    if (workflowDefinitionEvent.getActivity() != null) {
                        PLMWorkflowDefinitionStatus workflowStatus = plmWorkflowDefinitionStatusRepository.getWorkflowDefinitionStatusByWorkflowAndNameAndType(workflowDefinition.getId(), workflowDefinitionEvent.getActivity().getName(), workflowDefinitionEvent.getActivity().getType());
                        workflowEvent.setActivity(workflowStatus);
                    }
                    if (cassiniObject.getObjectType().equals(definitionTypeObject.getObjectType())) {
                        workflowEvent.setActionData(workflowDefinitionEvent.getActionData());
                    }
                    workflowEvent = plmWorkflowDefinitionEventRepository.save(workflowEvent);
                }
            } else {
                PLMWorkflowDefinitionEvent workflowEvent = new PLMWorkflowDefinitionEvent();
                workflowEvent.setWorkflow(workflowDefinition.getId());
                workflowEvent.setEventType(workflowDefinitionEvent.getEventType());
                workflowEvent.setActionType(workflowDefinitionEvent.getActionType());
                if (workflowDefinitionEvent.getActivity() != null) {
                    PLMWorkflowDefinitionStatus workflowStatus = plmWorkflowDefinitionStatusRepository.getWorkflowDefinitionStatusByWorkflowAndNameAndType(workflowDefinition.getId(), workflowDefinitionEvent.getActivity().getName(), workflowDefinitionEvent.getActivity().getType());
                    workflowEvent.setActivity(workflowStatus);
                }
                workflowEvent.setActionData(workflowDefinitionEvent.getActionData());
                workflowEvent = plmWorkflowDefinitionEventRepository.save(workflowEvent);
            }
        }

        for (PLMWorkflowDefinitionStatus workflowDefinitionStatus : workflowDefinitionStatuses) {
            PLMWorkflowDefinitionStatus workflowStatus = plmWorkflowDefinitionStatusRepository.getWorkflowDefinitionStatusByWorkflowAndNameAndType(workflowDefinition.getId(), workflowDefinitionStatus.getName(), workflowDefinitionStatus.getType());
            List<PLMWorkflowActivityFormFields> activityFormFields = workflowActivityFormFieldsRepository.findByWorkflowActivityOrderByName(workflowDefinitionStatus.getId());
            List<PLMWorkflowActivityFormFields> statusFormFields = new ArrayList<PLMWorkflowActivityFormFields>();
            for (PLMWorkflowActivityFormFields activityFormField : activityFormFields) {
                PLMWorkflowActivityFormFields formFields = JsonUtils.cloneEntity(activityFormField, PLMWorkflowActivityFormFields.class);
                formFields.setId(null);
                formFields.setWorkflowActivity(workflowStatus.getId());
                formFields.setObjectType(workflowStatus.getObjectType());
                statusFormFields.add(formFields);
            }
            if (statusFormFields.size() > 0) {
                statusFormFields = workflowActivityFormFieldsRepository.save(statusFormFields);
            }
        }
        return workflowDefinition;
    }
}
