package com.cassinisys.plm.service.cm;

import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.cassinisys.plm.service.wf.PLMWorkflowStatusService;
import com.cassinisys.plm.service.wf.PLMWorkflowTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by subramanyam on 16-06-2020.
 */
@Service
public class QualityWorkflowService {

    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkFlowStatusApproverRepository plmWorkFlowStatusApproverRepository;
    @Autowired
    private PLMWorkFlowStatusObserverRepository plmWorkFlowStatusObserverRepository;
    @Autowired
    private PLMWorkFlowStatusAcknowledgerRepository plmWorkFlowStatusAcknowledgerRepository;
    @Autowired
    private PLMWorkflowTransitionService plmWorkflowTransitionService;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStartRepository;
    @Autowired
    private PLMWorkflowStatusService plmWorkflowStatusService;
    @Autowired
    private PLMWorkflowTransitionRepository workflowTransitionRepository;
    @Autowired
    private PLMWorkflowFinishRepository plmWorkflowFinishRepository;
    @Autowired
    private PLMWorkflowStatusHistoryRepository plmWorkflowStatusHistoryRepository;
    @Autowired
    private PLMWorkFlowStatusAssignmentRepository workFlowStatusAssignmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private InspectionPlanRepository inspectionPlanRepository;
    @Autowired
    private ProductInspectionPlanRepository productInspectionPlanRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private InspectionRepository inspectionRepository;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private NCRRepository ncrRepository;
    @Autowired
    private QCRRepository qcrRepository;
    @Autowired
    private PLMWorkflowStatusActionHistoryRepository plmWorkflowStatusActionHistoryRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Transactional
    public PLMWorkflow attachQualityWorkflow(Integer id, PLMObjectType objectType, Integer wfDefId) {
        PLMWorkflow workflow = null;
        if (objectType.equals(PLMObjectType.PROBLEMREPORT)) {
            PQMProblemReport problemReport = problemReportRepository.findOne(id);
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (problemReport != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.PROBLEMREPORT, problemReport.getId(), wfDef);
                problemReport.setWorkflow(workflow.getId());
                problemReportRepository.save(problemReport);
                applicationEventPublisher.publishEvent(new ProblemReportEvents.ProblemReportWorkflowChangeEvent(problemReport, null, workflow));
            }
        } else if (objectType.equals(PLMObjectType.NCR)) {
            PQMNCR pqmncr = ncrRepository.findOne(id);
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (pqmncr != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.NCR, pqmncr.getId(), wfDef);
                pqmncr.setWorkflow(workflow.getId());
                ncrRepository.save(pqmncr);
                applicationEventPublisher.publishEvent(new NCREvents.NCRWorkflowChangeEvent(pqmncr, null, workflow));
            }

        } else if (objectType.equals(PLMObjectType.QCR)) {
            PQMQCR pqmqcr = qcrRepository.findOne(id);
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (pqmqcr != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.QCR, pqmqcr.getId(), wfDef);
                pqmqcr.setWorkflow(workflow.getId());
                qcrRepository.save(pqmqcr);
                applicationEventPublisher.publishEvent(new QCREvents.QCRWorkflowChangeEvent(pqmqcr, null, workflow));
            }

        } else if (objectType.equals(PLMObjectType.ITEMINSPECTION) || objectType.equals(PLMObjectType.MATERIALINSPECTION)) {
            PQMInspection inspection = inspectionRepository.findOne(id);
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (inspection != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(inspection.getObjectType(), inspection.getId(), wfDef);
                inspection.setWorkflow(workflow.getId());
                inspectionRepository.save(inspection);
                applicationEventPublisher.publishEvent(new InspectionEvents.InspectionWorkflowChangeEvent(inspection, null, workflow));
            }

        } else if (objectType.equals(PLMObjectType.INSPECTIONPLANREVISION)) {
            PQMInspectionPlanRevision inspectionPlan = inspectionPlanRevisionRepository.findOne(id);
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (inspectionPlan != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(inspectionPlan.getObjectType(), inspectionPlan.getId(), wfDef);
                inspectionPlan.setWorkflow(workflow.getId());
                inspectionPlanRevisionRepository.save(inspectionPlan);
                applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanWorkflowChangeEvent(inspectionPlan, null, workflow));
            }
        }
        return workflow;
    }
}
