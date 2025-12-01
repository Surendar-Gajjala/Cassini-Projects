package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.repo.wf.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class WorkflowsImporter extends AbstractImporter {
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workflowDefinitionRepository;
    @Autowired
    private PLMWorkflowDefinitionStartRepository workflowDefinitionStartRepository;
    @Autowired
    private PLMWorkflowDefinitionFinishRepository workflowDefinitionFinishRepository;
    @Autowired
    private PLMWorkflowDefinitionStatusRepository workflowDefinitionStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionTransitionRepository workflowDefinitionTransitionRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            List<PLMWorkflowDefinition> workflows = getObjectMapper().readValue(bytes, new TypeReference<List<PLMWorkflowDefinition>>() {
            });
            for (PLMWorkflowDefinition wf : workflows) {
                PLMWorkflowDefinition found = workflowDefinitionRepository.findOne(wf.getId());
                if (found == null) {
                    createNew(wf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNew(PLMWorkflowDefinition workflow) {
        Map<Integer, Integer> idMapping = new HashMap<>();
        PLMWorkflowDefinitionStart start = workflow.getStart();
        PLMWorkflowDefinitionFinish finish = workflow.getFinish();
        List<PLMWorkflowDefinitionStatus> statuses = workflow.getStatuses();
        List<PLMWorkflowDefinitionTransition> transitions = workflow.getTransitions();
        workflow.setId(null);
        PLMWorkflowType workflowType = workflow.getWorkflowType();
        workflow.setWorkflowType(workflowTypeRepository.findByName(workflowType.getName()));
        workflow.setStart(null);
        workflow.setFinish(null);
        workflow = workflowDefinitionRepository.save(workflow);
        Integer oldId = start.getId();
        start.setId(null);
        start.setWorkflow(workflow.getId());
        start = workflowDefinitionStartRepository.save(start);
        idMapping.put(oldId, start.getId());
        oldId = finish.getId();
        finish.setId(null);
        finish.setWorkflow(workflow.getId());
        finish = workflowDefinitionFinishRepository.save(finish);
        idMapping.put(oldId, finish.getId());
        workflow.setStart(start);
        workflow.setFinish(finish);
        workflow = workflowDefinitionRepository.save(workflow);
        for (PLMWorkflowDefinitionStatus status : statuses) {
            oldId = status.getId();
            status.setId(null);
            status.setWorkflow(workflow.getId());
            status = workflowDefinitionStatusRepository.save(status);
            idMapping.put(oldId, status.getId());
        }
        for (PLMWorkflowDefinitionTransition transition : transitions) {
            transition.setId(null);
            Integer fromId = transition.getFromStatus();
            Integer toId = transition.getToStatus();
            transition.setFromStatus(idMapping.get(fromId));
            transition.setToStatus(idMapping.get(toId));
            transition.setWorkflow(workflow.getId());
        }
        workflowDefinitionTransitionRepository.save(transitions);
    }
}
