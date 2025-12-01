package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.SupplierAuditEvents;
import com.cassinisys.plm.filtering.SupplierAuditCriteria;
import com.cassinisys.plm.filtering.SupplierAuditPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.tm.UserTaskEvents;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SupplierAuditService
 */
@Service
public class SupplierAuditService {
    public static Logger logger = LoggerFactory.getLogger(SupplierAuditService.class);
    @Autowired
    private SupplierAuditRepository supplierAuditRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MessageSource messageSource;
    @Autowired(required = true)
    private SupplierAuditPredicateBuilder supplierAuditPredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;
    @Autowired
    private PQMSupplierAuditReviewerRepository supplierAuditReviewerRepository;
    @Autowired
    private PQMSupplierAuditAttributeRepository supplierAuditAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierAuditTypeRepository supplierAuditTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private Environment environment;

    @Transactional
    @PreAuthorize("hasPermission(#supplierAudit,'create')")
    public PQMSupplierAudit create(PQMSupplierAudit supplierAudit) {
        Integer workflowId = supplierAudit.getWorkflow();
        PQMSupplierAudit existPpapNumber = supplierAuditRepository.findByNumber(supplierAudit.getNumber());
        if (existPpapNumber != null) {
            throw new CassiniException(
                    messageSource.getMessage(supplierAudit.getNumber() + " : " + "supplier_audit_number_already_exists",
                            null, "PPAP Number already exist", LocaleContextHolder.getLocale()));
        }
        autoNumberService.saveNextNumber(supplierAudit.getType().getNumberSource().getId(), supplierAudit.getNumber());
        if (supplierAudit.getType().getLifecycle() != null) {
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getType().getLifecycle().getId(), LifeCyclePhaseType.PRELIMINARY);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(0));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        }
        supplierAudit = supplierAuditRepository.save(supplierAudit);
        if (workflowId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(workflowId);
            if (wfDef != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.SUPPLIERAUDIT,
                        supplierAudit.getId(), wfDef);
                supplierAudit.setWorkflow(workflow.getId());
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        }

        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditCreatedEvent(supplierAudit));

        return supplierAudit;
    }

    @Transactional
    @PreAuthorize("hasPermission(#supplierAudit.id,'edit')")
    public PQMSupplierAudit update(PQMSupplierAudit supplierAudit) {
        PQMSupplierAudit oldPpap = JsonUtils.cloneEntity(supplierAuditRepository.findOne(supplierAudit.getId()),
                PQMSupplierAudit.class);
        applicationEventPublisher
                .publishEvent(new SupplierAuditEvents.SupplierAuditBasicInfoUpdatedEvent(oldPpap, supplierAudit));
        return supplierAuditRepository.save(supplierAudit);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        supplierAuditRepository.delete(id);
    }

    @Transactional
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMSupplierAudit> getAll() {
        return supplierAuditRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<PQMSupplierAudit> getAllSupplierAuditsByPageable(Pageable pageable, SupplierAuditCriteria criteria) {
        Predicate predicate = supplierAuditPredicateBuilder.build(criteria, QPQMSupplierAudit.pQMSupplierAudit);
        Page<PQMSupplierAudit> supplierAudits = supplierAuditRepository.findAll(predicate, pageable);
        supplierAudits.forEach(pqmSupplierAudit -> {
            if (pqmSupplierAudit.getAssignedTo() != null) {
                pqmSupplierAudit
                        .setAssignedToName(personRepository.findOne(pqmSupplierAudit.getAssignedTo()).getFullName());
            }
        });
        return supplierAudits;
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMSupplierAudit getById(Integer id) {
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(supplierAudit.getId());
        supplierAudit.setStartWorkflow(setWorkflowStart(workflow));
        return supplierAudit;
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
    public DetailsCount getSupplierAuditTabCounts(Integer id) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setPlanCount(supplierAuditPlanRepository.getPlanCountByAudit(id));
        detailsCount
                .setFiles(supplierAuditFileRepository.getFilesCountBySupplierAuditAndFileTypeAndLatestTrue(id, "FILE"));
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsCount;
    }

    // -----------------------------------------PQMSupplierAuditAttribute-----------------------------------------

    @Transactional
    public PQMSupplierAuditAttribute createSupplierAuditAttribute(PQMSupplierAuditAttribute attribute) {
        return supplierAuditAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMSupplierAuditAttribute updateSupplierAuditAttribute(PQMSupplierAuditAttribute attribute) {
        PQMSupplierAuditAttribute oldValue = supplierAuditAttributeRepository
                .findBySupplierAuditAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMSupplierAuditAttribute.class);
        attribute = supplierAuditAttributeRepository.save(attribute);
        PQMSupplierAudit pqmSupplierAudit = supplierAuditRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplieAuditAttributesUpdatedEvent(pqmSupplierAudit, oldValue, attribute));
        return attribute;
    }

    // --------------------------Supplier Audit plan------------------------------------------------------

    @Transactional
    public PQMSupplierAuditPlan createSupplierAuditPlan(PQMSupplierAuditPlan supplierAuditPlan) {
        supplierAuditPlan = supplierAuditPlanRepository.save(supplierAuditPlan);
        return supplierAuditPlan;
    }

    @Transactional
    public List<PQMSupplierAuditPlan> createMultipleSupplierAuditPlans(List<PQMSupplierAuditPlan> supplierAuditPlans) {
        List<PQMSupplierAuditPlan> auditPlans = new ArrayList<>();
        for (PQMSupplierAuditPlan pqmSupplierAuditPlan : supplierAuditPlans) {
            PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findBySupplierAuditAndSupplier(
                    pqmSupplierAuditPlan.getSupplierAudit(), pqmSupplierAuditPlan.getSupplier());
            if (supplierAuditPlan == null) {
                auditPlans.add(pqmSupplierAuditPlan);
            }
        }
        auditPlans = supplierAuditPlanRepository.save(auditPlans);
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditPlansAddedEvent(auditPlans.get(0).getSupplierAudit(), auditPlans));
        return auditPlans;
    }

    @Transactional
    public PQMSupplierAuditPlan updateSupplierAuditPlan(PQMSupplierAuditPlan supplierAuditPlan) {
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
        PQMSupplierAuditPlan auditPlan = JsonUtils.cloneEntity(
                supplierAuditPlanRepository.findOne(supplierAuditPlan.getId()), PQMSupplierAuditPlan.class);
        if (supplierAuditPlan.getPlannedStartDate() != null && supplierAuditPlan.getFinishedDate() != null) {
            Integer approverCount = supplierAuditReviewerRepository.getApproverCountByPlan(supplierAuditPlan.getId());
            if (approverCount == 0) {
                PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
                String message = messageSource.getMessage("add_atleast_approver_to_plan", null,
                        "Add atleast one approver to {0} plan", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", supplier.getName());
                throw new CassiniException(result);
            }
            supplierAuditPlan.setStatus(AuditPlanStatus.COMPLETED);
        } else if (supplierAuditPlan.getPlannedStartDate() != null || supplierAuditPlan.getFinishedDate() != null) {
            supplierAuditPlan.setStatus(AuditPlanStatus.PLANNED);
        } else {
            supplierAuditPlan.setStatus(AuditPlanStatus.NONE);
        }
        supplierAuditPlan = supplierAuditPlanRepository.save(supplierAuditPlan);
        if (supplierAuditPlan.getStatus().equals(AuditPlanStatus.PLANNED)) {
            sendPlanedNotificaton(supplierAuditPlan);
        }
        setSupplierAuditStatus(supplierAuditPlan, supplierAudit);
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditPlanUpdatedEvent(
                supplierAuditPlan.getSupplierAudit(), auditPlan, supplierAuditPlan));
        return supplierAuditPlan;
    }

    private void sendPlanedNotificaton(PQMSupplierAuditPlan supplierAuditPlan) {
        List<Person> persons = getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
        persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
        Date plannedDate = supplierAuditPlan.getPlannedStartDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(plannedDate);
        String supplierPlannedDate = df.format(plannedDate);
        String email = "";
        if (persons.size() > 0) {
            String[] recipientAddress = new String[persons.size() + 1];
            for (int i = 0; i < persons.size(); i++) {
                Person person = persons.get(i);
                if (email.equals("")) {
                    email = person.getEmail();
                } else {
                    email = email + "," + person.getEmail();
                }
            }
            email = email + "," + supplier.getEmail();
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            Map<String, Object> model = new HashMap<>();

            model.put("host", "");
            if (persons.size() == 1) {
                model.put("personName", persons.get(0).getFullName());
            } else {
                model.put("personName", "All");
            }
            model.put("supplierName", supplier.getName());
            model.put("auditNumber", supplierAudit.getNumber());
            model.put("plannedDate", supplierPlannedDate);
            Mail mail = new Mail();
            mail.setMailSubject("Supplier Audit Plan Notification");
            mail.setTemplatePath("email/auditPlanNotifyMail.html");
            mail.setModel(model);
            mail.setMailToList(recipientAddress);
            new Thread(() -> {
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Transactional
    public void deleteSupplierAuditPlan(Integer id) {
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(id);
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
        supplierAuditPlanRepository.delete(id);
        setSupplierAuditStatus(supplierAuditPlan, supplierAudit);
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditPlanDeletedEvent(
                supplierAuditPlan.getSupplierAudit(), supplierAuditPlan));
    }

    private void setSupplierAuditStatus(PQMSupplierAuditPlan supplierAuditPlan, PQMSupplierAudit supplierAudit) {
        Integer planCount = supplierAuditPlanRepository.getPlanCountByAudit(supplierAuditPlan.getSupplierAudit());
        Integer approvedPlanCount = supplierAuditPlanRepository
                .getAuditPlanCountByAuditAndStatus(supplierAuditPlan.getSupplierAudit(), AuditPlanStatus.APPROVED);
        Integer noPlannedDatesCount = supplierAuditPlanRepository
                .getNoPlannedDatesCountByAudit(supplierAuditPlan.getSupplierAudit());
        Integer plannedDatesCount = supplierAuditPlanRepository
                .getPlannedDatesCountByAudit(supplierAuditPlan.getSupplierAudit());
        Integer noFinishedDatesCount = supplierAuditPlanRepository
                .getNoFinishedDatesCountByAudit(supplierAuditPlan.getSupplierAudit());
        if (planCount > 0 && plannedDatesCount > 0 && noFinishedDatesCount > 0) {
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getStatus().getLifeCycle(), LifeCyclePhaseType.REVIEW);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(0));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        } else if (planCount > 0 && noPlannedDatesCount == 0 && noFinishedDatesCount == 0) {
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getStatus().getLifeCycle(), LifeCyclePhaseType.REVIEW);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        } else if (planCount > 0 && planCount.equals(noPlannedDatesCount)) {
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getStatus().getLifeCycle(), LifeCyclePhaseType.PRELIMINARY);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(0));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        }
        if (planCount.equals(approvedPlanCount) && planCount > 0) {
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getStatus().getLifeCycle(), LifeCyclePhaseType.RELEASED);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        }
    }

    @Transactional
    public List<PQMSupplierAuditPlan> getAllSupplierAuditPlans(Integer id) {
        List<PQMSupplierAuditPlan> auditPlans = supplierAuditPlanRepository
                .findBySupplierAuditOrderByModifiedDateDesc(id);
        auditPlans.forEach(pqmSupplierAuditPlan -> {
            PLMSupplier supplier = supplierRepository.findOne(pqmSupplierAuditPlan.getSupplier());
            pqmSupplierAuditPlan.setName(supplier.getName());
            pqmSupplierAuditPlan.setNumber(supplier.getNumber());
            pqmSupplierAuditPlan.setCity(supplier.getCity());
            pqmSupplierAuditPlan.setApprovedCount(
                    supplierAuditReviewerRepository.getApprovedCountByPlan(pqmSupplierAuditPlan.getId()));
            pqmSupplierAuditPlan.setReviewerCount(supplierAuditReviewerRepository.getApproverCountByPlan(pqmSupplierAuditPlan.getId()));       

        });
        return auditPlans;
    }

    @Transactional
    public PQMSupplierAuditPlan getSupplierAuditPlanById(Integer id) {
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(id);
        PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
        supplierAuditPlan.setName(supplier.getName());
        supplierAuditPlan.setCity(supplier.getCity());
        supplierAuditPlan
                .setApprovedCount(supplierAuditReviewerRepository.getApprovedCountByPlan(supplierAuditPlan.getId()));
        return supplierAuditPlan;
    }

    // --------------------------PQMSupplierAuditReviewer-----------------------------------------------------

    @Transactional
    public PQMSupplierAuditReviewer createSupplierAuditReviewer(PQMSupplierAuditReviewer supplierAuditReviewer) {
        supplierAuditReviewer = supplierAuditReviewerRepository.save(supplierAuditReviewer);
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(supplierAuditReviewer.getPlan());
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditPlanReviewerAddedEvent(supplierAuditPlan, supplierAuditReviewer));
        return supplierAuditReviewer;
    }

    @Transactional
    public PQMSupplierAuditReviewer updateSupplierAuditReviewer(PQMSupplierAuditReviewer supplierAuditReviewer) {
        supplierAuditReviewer = supplierAuditReviewerRepository.save(supplierAuditReviewer);
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(supplierAuditReviewer.getPlan());
        applicationEventPublisher.publishEvent(new UserTaskEvents.SupplierAuditPlanAssignedEvent(
                supplierAuditReviewer.getPlan(), supplierAuditReviewer));
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditPlanReviewerUpdateEvent(supplierAuditPlan, supplierAuditReviewer));
        return supplierAuditReviewer;
    }

    @Transactional
    public void deleteSupplierAuditReviewer(Integer id) {
        PQMSupplierAuditReviewer supplierAuditReviewer = supplierAuditReviewerRepository.findOne(id);
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(supplierAuditReviewer.getPlan());
        applicationEventPublisher.publishEvent(new UserTaskEvents.SupplierAuditPlanDeletedEvent(
                supplierAuditReviewer.getPlan(), supplierAuditReviewer));
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditPlanReviewerDeletedEvent(
                supplierAuditPlan, supplierAuditReviewer));
        supplierAuditReviewerRepository.delete(id);
    }

    @Transactional
    public List<PQMSupplierAuditReviewer> getAllSupplierAuditReviewers(Integer planId) {
        return supplierAuditReviewerRepository.findByPlan(planId);
    }

    @Transactional
    public PQMSupplierAuditReviewer getSupplierAuditReviewerById(Integer id) {
        PQMSupplierAuditReviewer supplierAuditReviewer = supplierAuditReviewerRepository.findOne(id);
        return supplierAuditReviewer;
    }

    @Transactional
    public PLMWorkflow attachNewSupplierAuditWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (supplierAudit != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.SUPPLIERAUDIT, supplierAudit.getId(), wfDef);
            supplierAudit.setWorkflow(workflow.getId());
            supplierAuditRepository.save(supplierAudit);
            applicationEventPublisher.publishEvent(
                    new SupplierAuditEvents.SupplierAuditWorkflowChangeEvent(supplierAudit, null, workflow));
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(Integer typeId, String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PQMSupplierAuditType supplierAuditType = supplierAuditTypeRepository.findOne(typeId);
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
        List<PLMWorkflowDefinition> workflowDefinition1 = workFlowDefinitionRepository
                .findByWorkflowTypeAndReleasedTrue(workflowType);
        if (workflowDefinition1.size() > 0) {
            workflowDefinition1.forEach(workflowDefinition -> {
                if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                    workflowDefinitions.add(workflowDefinition);
                }
            });
        }
        if (supplierAuditType.getParentType() != null) {
            getWorkflowsFromHierarchy(workflowDefinitions, supplierAuditType.getParentType(), type);
        }
        return workflowDefinitions;
    }

    private void getWorkflowsFromHierarchy(List<PLMWorkflowDefinition> definitions, Integer typeId, String type) {
        PQMSupplierAuditType manufacturerPartType = supplierAuditTypeRepository.findOne(typeId);
        if (manufacturerPartType != null) {
            PLMWorkflowType workflowType = workflowTypeRepository.findByAssignableAndAssignedType(type, typeId);
            if (workflowType != null) {
                List<PLMWorkflowDefinition> workflowDefinition2 = workFlowDefinitionRepository
                        .findByWorkflowTypeAndReleasedTrue(workflowType);
                if (workflowDefinition2.size() > 0) {
                    workflowDefinition2.forEach(workflowDefinition -> {
                        if (workflowDefinition.getMaster().getLatestReleasedRevision()
                                .equals(workflowDefinition.getId())) {
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

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void mfrWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditWorkflowStartedEvent(supplierAudit, event.getPlmWorkflow()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void supplierAuditWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            Integer nonApprovedCount = supplierAuditPlanRepository.getNonApprovedAuditPlanCount(supplierAudit.getId());
            if (nonApprovedCount > 0) {
                String message = messageSource.getMessage("audit_plans_not_approved", null,
                        "Audit plans are not approved to promote the workflow", LocaleContextHolder.getLocale());
                throw new CassiniException(message);
            }
        }
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditWorkflowPromotedEvent(supplierAudit,
                plmWorkflow, fromStatus, toStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void supplierAuditWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditWorkflowDemotedEvent(supplierAudit,
                event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void supplierAuditWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        if (fromStatus.getType().equals(WorkflowStatusType.RELEASED)) {
            Integer nonApprovedCount = supplierAuditPlanRepository.getNonApprovedAuditPlanCount(supplierAudit.getId());
            if (nonApprovedCount > 0) {
                String message = messageSource.getMessage("audit_plans_not_approved", null,
                        "Audit plans are not approved to promote the workflow", LocaleContextHolder.getLocale());
                throw new CassiniException(message);
            }
        }
        applicationEventPublisher
                .publishEvent(new SupplierAuditEvents.SupplierAuditWorkflowFinishedEvent(supplierAudit, plmWorkflow));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void supplierAuditWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditWorkflowHoldEvent(supplierAudit, plmWorkflow, fromStatus));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'SUPPLIERAUDIT'")
    public void supplierAuditWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PQMSupplierAudit supplierAudit = (PQMSupplierAudit) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(
                new SupplierAuditEvents.SupplierAuditWorkflowUnholdEvent(supplierAudit, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = false)
    public PQMSupplierAuditReviewer submitReview(Integer planId, PQMSupplierAuditReviewer supplierAuditReviewer) {
        PQMSupplierAuditPlan supplierAuditPlan = supplierAuditPlanRepository.findOne(planId);
        supplierAuditReviewer.setVoteTimestamp(new Date());
        supplierAuditReviewer = supplierAuditReviewerRepository.save(supplierAuditReviewer);
        Integer nonApprovedCount = supplierAuditReviewerRepository.getNotApprovedCountByPlan(supplierAuditPlan.getId());
        if (nonApprovedCount == 0) {
            supplierAuditPlan.setStatus(AuditPlanStatus.APPROVED);
            supplierAuditPlan = supplierAuditPlanRepository.save(supplierAuditPlan);
        }

        Integer planCount = supplierAuditPlanRepository.getPlanCountByAudit(supplierAuditPlan.getSupplierAudit());
        Integer approvedCount = supplierAuditPlanRepository
                .getAuditPlanCountByAuditAndStatus(supplierAuditPlan.getSupplierAudit(), AuditPlanStatus.APPROVED);

        if (planCount.equals(approvedCount) && planCount > 0) {
            PQMSupplierAudit supplierAudit = supplierAuditRepository.findOne(supplierAuditPlan.getSupplierAudit());
            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(
                    supplierAudit.getStatus().getLifeCycle(), LifeCyclePhaseType.RELEASED);
            if (lifeCyclePhases.size() > 0) {
                supplierAudit.setStatus(lifeCyclePhases.get(lifeCyclePhases.size() - 1));
                supplierAudit = supplierAuditRepository.save(supplierAudit);
            }
        }
        applicationEventPublisher.publishEvent(new UserTaskEvents.SupplierAuditPlanSubmittedEvent(
                supplierAuditReviewer.getPlan(), supplierAuditReviewer));
        applicationEventPublisher.publishEvent(new SupplierAuditEvents.SupplierAuditPlanReviewerSubmittedEvent(
                supplierAuditPlan, supplierAuditReviewer));
        return supplierAuditReviewer;
    }

    @Scheduled(cron = "0 10 8 * * ?")
    public void sendSupplierAuditPlannedNotification() {
        new Thread(() -> {
            String val1 = environment.getProperty("cassini.tenants");
            String[] tenants = val1.split(",");
            for (int s = 0; s < tenants.length; s++) {
                String schema = tenants[s];
                TenantManager.get().setTenantId(schema);
                Preference auditPlanReminderPreference = null;
                try {
                    auditPlanReminderPreference = preferenceRepository.findByPreferenceKey("SUPPLIER_AUDIT_EMAIL_REMINDER");
                } catch (Exception e) {

                }
                if (auditPlanReminderPreference != null && auditPlanReminderPreference.getBooleanValue() != null && auditPlanReminderPreference.getBooleanValue()) {
                    List<PQMSupplierAuditPlan> supplierAuditPlans = supplierAuditPlanRepository
                            .getSupplierAuditPlansByStatus(AuditPlanStatus.PLANNED);
                    if (supplierAuditPlans.size() > 0) {
                        List<Person> persons = getQARolePerson("DEFAULT_QUALITY_ADMINISTRATOR_ROLE");
                        persons.addAll(getQARolePerson("DEFAULT_QUALITY_ANALYST_ROLE"));

                        if (persons.size() > 0) {
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                            for (PQMSupplierAuditPlan supplierAuditPlan : supplierAuditPlans) {
                                PLMSupplier supplier = supplierRepository.findOne(supplierAuditPlan.getSupplier());
                                PQMSupplierAudit supplierAudit = supplierAuditRepository
                                        .findOne(supplierAuditPlan.getSupplierAudit());
                                Date plannedDate = supplierAuditPlan.getPlannedStartDate();
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(plannedDate);
                                cal.add(Calendar.DAY_OF_MONTH, -10);

                                Date tenDaysBeforeDate = cal.getTime();

                                Date currentDate = new Date();
                                String currentDateStr = df.format(currentDate);
                                String tenDaysBeforeDateStr = df.format(tenDaysBeforeDate);
                                String supplierPlannedDate = df.format(plannedDate);
                                Date parsedCurrentDate = null;
                                Date parsedTenDaysBeforeDate = null;
                                Date parsedPlannedDate = null;

                                try {
                                    parsedCurrentDate = df.parse(currentDateStr);
                                    parsedTenDaysBeforeDate = df.parse(tenDaysBeforeDateStr);
                                    parsedPlannedDate = df.parse(supplierPlannedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (parsedCurrentDate != null && parsedPlannedDate != null && parsedTenDaysBeforeDate != null
                                        && (parsedCurrentDate.equals(parsedTenDaysBeforeDate)
                                        || parsedCurrentDate.equals(parsedPlannedDate))) {
                                    String email = "";
                                    String[] recipientAddress = new String[persons.size() + 1];
                                    for (int i = 0; i < persons.size(); i++) {
                                        Person person = persons.get(i);
                                        if (email.equals("")) {
                                            email = person.getEmail();
                                        } else {
                                            email = email + "," + person.getEmail();
                                        }
                                    }
                                    email = email + "," + supplier.getEmail();
                                    String[] recipientList = email.split(",");
                                    int counter = 0;
                                    for (String recipient : recipientList) {
                                        recipientAddress[counter] = recipient;
                                        counter++;
                                    }
                                    Map<String, Object> model = new HashMap<>();
                                    model.put("host", "");
                                    if (persons.size() == 1) {
                                        model.put("personName", persons.get(0).getFullName());
                                    } else {
                                        model.put("personName", "All");
                                    }
                                    model.put("supplierName", supplier.getName());
                                    model.put("auditNumber", supplierAudit.getNumber());
                                    model.put("plannedDate", supplierPlannedDate);
                                    Mail mail = new Mail();
                                    mail.setMailSubject("Supplier Audit Plan Reminder Notification");
                                    mail.setTemplatePath("email/auditPlanNotifyMail.html");
                                    mail.setModel(model);
                                    mail.setMailToList(recipientAddress);
                                    mailService.sendEmail(mail);
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public List<Person> getQARolePerson(String name) {
        List<Person> persons = new ArrayList<>();
        Preference pref = preferenceRepository.findByPreferenceKey(name);
        PersonGroup personGroup = null;
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer id = jsonNode.get("typeId").asInt();
                    if (id != null) {
                        personGroup = personGroupRepository.findOne(id);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (personGroup != null) {
            List<GroupMember> groupMembers = groupMemberRepository.findByPersonGroup(personGroup);
            groupMembers.forEach(groupMember -> {
                Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());
                if (login != null && login.getIsActive()) {
                    persons.add(groupMember.getPerson());
                }
            });
        }
        return persons;
    }
}