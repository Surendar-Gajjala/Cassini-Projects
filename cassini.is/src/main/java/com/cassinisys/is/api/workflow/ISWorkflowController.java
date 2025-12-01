package com.cassinisys.is.api.workflow;

import com.cassinisys.is.filtering.WorkflowDefinitionCriteria;
import com.cassinisys.is.model.workflow.ISWorkflow;
import com.cassinisys.is.model.workflow.ISWorkflowDefinition;
import com.cassinisys.is.service.workflow.ISWorkflowDefinitionService;
import com.cassinisys.is.service.workflow.ISWorkflowDefinitionStatusService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@RestController
@RequestMapping("is/workflows")
public class ISWorkflowController extends BaseController {

    @Autowired
    private ISWorkflowDefinitionService workflowDefinitionService;

    @Autowired
    private ISWorkflowDefinitionStatusService workflowDefinitionStatusService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/definitions", method = RequestMethod.POST)
    public ISWorkflowDefinition create(@RequestBody @Valid ISWorkflowDefinition workflowDefinition) {
        return workflowDefinitionService.create(workflowDefinition);
    }

    @RequestMapping(value = "/definitions/{definitionId}", method = RequestMethod.PUT)
    public ISWorkflowDefinition update(@PathVariable("definitionId") Integer definitionId,
                                       @RequestBody ISWorkflowDefinition workflowDefinition) {
        workflowDefinition.setId(definitionId);
        return workflowDefinitionService.update(workflowDefinition);
    }

    @RequestMapping(value = "/definitions/{definitionId}", method = RequestMethod.GET)
    public ISWorkflowDefinition getDefinition(@PathVariable("definitionId") Integer definitionId) {
        return workflowDefinitionService.get(definitionId);
    }

    @RequestMapping(value = "/definitions", method = RequestMethod.GET)
    public Page<ISWorkflowDefinition> getAllWorkflowDefinitions(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getAll(pageable);
    }

    @RequestMapping(value = "/definitions/all", method = RequestMethod.GET)
    public List<ISWorkflowDefinition> getAllWorkflowDefinitions() {
        return workflowDefinitionService.getAll();
    }

    @RequestMapping(value = "/instances/{taskId}", method = RequestMethod.GET)
    public ISWorkflow get(@PathVariable("taskId") Integer taskId) {
        return workflowDefinitionService.getWorkflowInstances(taskId);
    }

    @RequestMapping(value = "/definitions/workflow/{id}", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("id") Integer id) {
        workflowDefinitionService.delete(id);
    }

    @RequestMapping(value = "/definitions/freetextsearch", method = RequestMethod.GET)
    public Page<ISWorkflowDefinition> freeTextSearch(PageRequest pageRequest, WorkflowDefinitionCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISWorkflowDefinition> workflowDefinitions = workflowDefinitionService.freeTextSearch(pageable, criteria);
        return workflowDefinitions;
    }
}
