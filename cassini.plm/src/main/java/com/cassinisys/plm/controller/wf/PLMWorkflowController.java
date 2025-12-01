package com.cassinisys.plm.controller.wf;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.WorkflowCriteria;
import com.cassinisys.plm.filtering.WorkflowDefinitionCriteria;
import com.cassinisys.plm.model.dto.WorkflowDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.dto.WorkflowEventDto;
import com.cassinisys.plm.model.wf.dto.WorkflowRevisionDto;
import com.cassinisys.plm.model.wf.dto.WorkflowStatusAssignmentsDto;
import com.cassinisys.plm.service.wf.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by subramanyamreddy on 019 19-May -17.
 */
@RestController
@RequestMapping("/plm/workflows")
@Api(tags = "PLM.WF", description = "Workflow Related")
public class PLMWorkflowController extends BaseController {

    @Autowired
    private PLMWorkflowService plmWorkflowService;

    @Autowired
    private PLMWorkflowStatusService workflowStatusService;

    @Autowired
    private PLMWorkflowTransitionService workflowTransitionService;

    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;

    @Autowired
    private PLMWorkflowDefinitionStatusService workflowDefinitionStatusService;

    @Autowired
    private PLMWorkflowTypeService plmWorkflowTypeService;

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMWorkflowDefinitionTransitionService workflowDefinitionTransitionService;

    // Create Methods
    @RequestMapping(value = "/instances", method = RequestMethod.POST)
    public PLMWorkflow create(@RequestBody @Valid PLMWorkflow plmWorkflow) {
        return plmWorkflowService.create(plmWorkflow);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses", method = RequestMethod.POST)
    public PLMWorkflowStatus createStatus(@PathVariable("workflowId") Integer workflowId,
                                          @RequestBody @Valid PLMWorkflowStatus workflowStatus) {
        workflowStatus.setWorkflow(workflowId);
        return workflowStatusService.create(workflowStatus);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}/transitions", method = RequestMethod.POST)
    public PLMWorkflowTransition createTransition(@PathVariable("workflowId") Integer workflowId,
                                                  @PathVariable("statusId") Integer statusId,
                                                  @RequestBody @Valid PLMWorkflowTransition workflowTransition) {
        workflowTransition.setFromStatus(statusId);
        workflowTransition.setToStatus(statusId);
        return workflowTransitionService.create(workflowTransition);
    }

    @RequestMapping(value = "/definitions/{workflowId}/transitions/delete", method = RequestMethod.POST)
    public void deleteTransitions(@PathVariable("workflowId") Integer workflowId,
                                  @RequestBody @Valid List<PLMWorkflowTransition> transitions) {
        workflowTransitionService.deleteTransitions(transitions);
    }

    @RequestMapping(value = "/definitions/{workflowId}/activities/delete", method = RequestMethod.POST)
    public void deleteActivities(@PathVariable("workflowId") Integer workflowId,
                                 @RequestBody @Valid List<PLMWorkflowStatus> activities) {
        workflowStatusService.deleteStatuses(activities);
    }


    @RequestMapping(value = "/definitions/{workflowId}/transition/{transitionId}/delete", method = RequestMethod.DELETE)
    public void deleteWfDefTransition(@PathVariable("workflowId") Integer workflowId,
                                      @PathVariable("transitionId") Integer transitionId) {
        workflowDefinitionTransitionService.deleteWfDefTransition(workflowId, transitionId);
    }

    @RequestMapping(value = "/definitions/{workflowId}/status/{statusId}/delete", method = RequestMethod.DELETE)
    public void deleteWfDefStatus(@PathVariable("workflowId") Integer workflowId,
                                  @PathVariable("statusId") Integer statusId) {
        workflowDefinitionStatusService.deleteWfDefStatus(workflowId, statusId);
    }

    @RequestMapping(value = "/definitions", method = RequestMethod.POST)
    public PLMWorkflowDefinition create(@RequestBody @Valid PLMWorkflowDefinition workflowDefinition) {
        return workflowDefinitionService.create(workflowDefinition);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses", method = RequestMethod.POST)
    public PLMWorkflowDefinitionStatus create(@RequestBody @Valid PLMWorkflowDefinitionStatus definitionStatus) {
        return workflowDefinitionStatusService.create(definitionStatus);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses/{statusId}/transitions", method = RequestMethod.POST)
    public PLMWorkflowDefinitionTransition create(@RequestBody @Valid PLMWorkflowDefinitionTransition definitionTransition) {
        return workflowDefinitionTransitionService.create(definitionTransition);
    }

    // Update Methods
    @RequestMapping(value = "/instances/{workflowId}", method = RequestMethod.PUT)
    public PLMWorkflow update(@PathVariable("workflowId") Integer workflowId,
                              @RequestBody PLMWorkflow plmWorkflow) {
        plmWorkflow.setId(workflowId);
        return plmWorkflowService.update(plmWorkflow);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}", method = RequestMethod.PUT)
    public PLMWorkflowStatus update(@PathVariable("workflowId") Integer workflowId, @PathVariable("statusId") Integer statusId,
                                    @RequestBody PLMWorkflowStatus workflowStatus) {
        workflowStatus.setId(statusId);
        return workflowStatusService.update(workflowStatus);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}/transitions/{transitionId}", method = RequestMethod.PUT)
    public PLMWorkflowTransition update(@PathVariable("workflowId") Integer workflowId, @PathVariable("statusId") Integer statusId,
                                        @PathVariable("transitionId") Integer transitionId,
                                        @RequestBody PLMWorkflowTransition workflowTransition) {
        workflowTransition.setId(transitionId);
        return workflowTransitionService.update(workflowTransition);
    }

    @RequestMapping(value = "/definitions/{definitionId}", method = RequestMethod.PUT)
    public PLMWorkflowDefinition update(@PathVariable("definitionId") Integer definitionId,
                                        @RequestBody PLMWorkflowDefinition workflowDefinition) {
        workflowDefinition.setId(definitionId);
        return workflowDefinitionService.update(workflowDefinition);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses/{statusId}", method = RequestMethod.PUT)
    public PLMWorkflowDefinitionStatus update(@PathVariable("definitionId") Integer definitionId,
                                              @PathVariable("statusId") Integer statusId,
                                              @RequestBody PLMWorkflowDefinitionStatus definitionStatus) {
        definitionStatus.setId(statusId);
        return workflowDefinitionStatusService.update(definitionStatus);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses/{statusId}/transitions/{transitionId}", method = RequestMethod.PUT)
    public PLMWorkflowDefinitionTransition update(@PathVariable("definitionId") Integer definitionId,
                                                  @PathVariable("statusId") Integer statusId,
                                                  @PathVariable("transitionId") Integer transitionId,
                                                  @RequestBody PLMWorkflowDefinitionTransition definitionTransition) {
        definitionTransition.setId(transitionId);
        return workflowDefinitionTransitionService.update(definitionTransition);
    }
    //Delete Methods

    @RequestMapping(value = "/instances/{workflowId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("workflowId") Integer workflowId) {
        plmWorkflowService.delete(workflowId);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("workflowId") Integer workflowId, @PathVariable("statusId") Integer statusId) {
        workflowStatusService.delete(statusId);
    }

    //Get Methods
    @RequestMapping(value = "/instances/{workflowId}", method = RequestMethod.GET)
    public PLMWorkflow get(@PathVariable("workflowId") Integer workflowId) {
        return plmWorkflowService.get(workflowId);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}", method = RequestMethod.GET)
    public PLMWorkflowStatus get(@PathVariable("workflowId") Integer workflowId, @PathVariable("statusId") Integer statusId) {
        return workflowStatusService.get(statusId);
    }

    @RequestMapping(value = "/instances/{workflowId}/statuses/{statusId}/transitions/{transitionId}", method = RequestMethod.GET)
    public PLMWorkflowTransition get(@PathVariable("workflowId") Integer workflowId, @PathVariable("statusId") Integer statusId,
                                     @PathVariable("transitionId") Integer transitionId) {
        return workflowTransitionService.get(transitionId);
    }

    @RequestMapping(value = "/definitions/{definitionId}", method = RequestMethod.GET)
    public PLMWorkflowDefinition getDefinition(@PathVariable("definitionId") Integer definitionId) {
        return workflowDefinitionService.get(definitionId);
    }

    @RequestMapping(value = "/definitions/workflowName/{workflowName}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getDefinitionByName(@PathVariable("workflowName") String workflowName) {
        return workflowDefinitionService.getByName(workflowName);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses/{statusId}", method = RequestMethod.GET)
    public PLMWorkflowDefinitionStatus getDefinitionStatus(@PathVariable("definitionId") Integer definitionId,
                                                           @PathVariable("statusId") Integer statusId) {
        return workflowDefinitionStatusService.get(statusId);
    }

    @RequestMapping(value = "/definitions/{definitionId}/statuses/{statusId}/transitions/{transitionId}", method = RequestMethod.GET)
    public PLMWorkflowDefinitionTransition getTransition(@PathVariable("definitionId") Integer definitionId,
                                                         @PathVariable("statusId") Integer statusId,
                                                         @PathVariable("transitionId") Integer transitionId) {
        return workflowDefinitionTransitionService.get(transitionId);
    }

    @RequestMapping(value = "/instances", method = RequestMethod.GET)
    public List<PLMWorkflow> getAllWorkflows() {
        return plmWorkflowService.getAll();
    }

    @RequestMapping(value = "/definitions", method = RequestMethod.GET)
    public Page<PLMWorkflowDefinition> getAllWorkflowDefinitions(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getAll(pageable);
    }

    @RequestMapping(value = "/definitions/all", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getAllWorkflowDefinitions() {
        return workflowDefinitionService.getAll();
    }

    @RequestMapping(value = "/definitions/{id}", method = RequestMethod.DELETE)
    public void deleteWorkflowDef(@PathVariable Integer id) {
        workflowDefinitionService.delete(id);
    }

    @RequestMapping(value = {"/instances/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<PLMWorkflow> getMultipleWorkflows(@PathVariable Integer[] ids) {
        return plmWorkflowService.getMultipleWorkflows(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/instances/{workflowId}/statuses/multiple"}, method = {RequestMethod.GET})
    public List<PLMWorkflowStatus> getStatusesByWorkFlow(@PathVariable("workflowId") Integer workflowId) {
        return workflowStatusService.getStatusesByWorkflow(workflowId);
    }

    @RequestMapping(value = {"/instances/{workflowId}/statuses/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<PLMWorkflowStatus> getMultipleStatusesByWorkFlow(@PathVariable("workflowId") Integer workflowId, @PathVariable Integer[] ids) {
        return workflowStatusService.getMultipleStatuses(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/instances/{workflowId}/transitions/multiple/[{ids}]"}, method = {RequestMethod.GET})
    public List<PLMWorkflowTransition> getMultipleTransitionsByWorkFlow(@PathVariable("workflowId") Integer workflowId, @PathVariable Integer[] ids) {
        return workflowTransitionService.getMultipleTransitions(Arrays.asList(ids));
    }

    @RequestMapping(value = {"/instances/{workflowId}/transitions/multiple"}, method = {RequestMethod.GET})
    public List<PLMWorkflowTransition> getTransitionsByWorkFlow(@PathVariable("workflowId") Integer workflowId) {
        return workflowTransitionService.getByWorkflow(workflowId);
    }

    @RequestMapping(value = "/definitions/freetextsearch", method = RequestMethod.GET)
    public Page<PLMWorkflowDefinition> freeTextSearch(PageRequest pageRequest, WorkflowDefinitionCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMWorkflowDefinition> workflowDefinitions = workflowDefinitionService.freeTextSearch(pageable, criteria);
        return workflowDefinitions;
    }

    /*--------   Attributes  -------------------*/

    @RequestMapping(value = "/definitions/{id}/attributes", method = RequestMethod.POST)
    public PLMWorkflowAttribute createItemAttribute(@PathVariable("id") Integer id,
                                                    @RequestBody PLMWorkflowAttribute attribute) {
        return workflowDefinitionService.createWorkflowAttribute(attribute);
    }

    @RequestMapping(value = "/definitions/{id}/attributes", method = RequestMethod.PUT)
    public PLMWorkflowAttribute updateItemAttribute(@PathVariable("id") Integer id,
                                                    @RequestBody PLMWorkflowAttribute attribute) {
        return workflowDefinitionService.updateWorkflowAttribute(attribute);
    }

    @RequestMapping(value = "/workflowTypes/{type}/attributes", method = RequestMethod.GET)
    public List<PLMWorkflowTypeAttribute> getWorkflowAttributes(@PathVariable("type") Integer type,
                                                                @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return plmWorkflowTypeService.getAttributes(type, hierarchy);
    }

    @RequestMapping(value = "/definitions/{definitionId}/attributes/multiple", method = RequestMethod.POST)
    public void saveAttributes(@PathVariable("definitionId") Integer definitionId,
                               @RequestBody List<PLMWorkflowAttribute> attributes) {
        workflowDefinitionService.saveAttributes(attributes);
    }

    @RequestMapping(value = "/definitions/{id}/attributes", method = RequestMethod.GET)
    public List<PLMWorkflowAttribute> getWorkflowAttributes(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getWorkflowAttributes(id);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PLMWorkflowDefinition> getWorkflowsByType(@PathVariable("typeId") Integer id,
                                                          PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getWorkflowsByType(id, pageable);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMWorkflowAttribute> getWorkflowUsedAttributes(@PathVariable("attributeId") Integer attributeId) {
        return plmWorkflowService.getWorkflowUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/updateImageValue/{objectId}/{attributeId}", method = RequestMethod.POST)
    public PLMWorkflowDefinition saveImageAttributeValue(@PathVariable("objectId") Integer objectId,
                                                         @PathVariable("attributeId") Integer attributeId, MultipartHttpServletRequest request) {
        return workflowDefinitionService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/definitions/workflow/{id}", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("id") Integer id) {
        workflowDefinitionService.deleteWorkflow(id);
    }

    @RequestMapping(value = "/definitions/assigned/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getAssignedWorkflows(@PathVariable("type") String type) {
        return workflowDefinitionService.getAssignedWorkflows(type);
    }

    @RequestMapping(value = "/definitions/assignedType/{type}", method = RequestMethod.GET)
    public PLMWorkflowType getAssignedType(@PathVariable("type") String type) {
        return workflowDefinitionService.getAssignedType(type);
    }

    @RequestMapping(value = "/definitions/{id}/workflows", method = RequestMethod.GET)
    public List<WorkflowDto> getWorkflowsByWfDef(@PathVariable("id") Integer id) {
        return plmWorkflowService.getWorkflowsByWfDef(id);
    }

    @RequestMapping(value = "/{id}/assigments", method = RequestMethod.GET)
    public List<PLMWorkflowStatusHistory> getWorkflowAssigments(@PathVariable("id") Integer id) {
        return plmWorkflowService.getStatusWorkflowHistory(id);
    }

    @RequestMapping(value = "/workflowType/{id}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getUsedWorkflows(@PathVariable("id") Integer id) {
        return plmWorkflowService.getAllWorkflowsByType(id);
    }

 /*   @RequestMapping(value = "/status/type/report", method = RequestMethod.GET)
    public Page<PLMWorkflow> getWorkflowByType(PageRequest pageRequest, WorkflowCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plmWorkflowService.getWorkflowByType(pageable, criteria);
    }*/

    @RequestMapping(value = "/definitions/master/{id}", method = RequestMethod.GET)
    public List<WorkflowDto> getWorkflowByType(@PathVariable("id") Integer id) {
        return plmWorkflowService.getWorkflowsByMaster(id);
    }

    @RequestMapping(value = "/definitions/{id}/statuses", method = RequestMethod.GET)
    private List<PLMWorkflowDefinitionStatus> getNormalWorkflowStatuses(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getNormalWorkflowStatuses(id);
    }

    @RequestMapping(value = "/definitions/{id}/statuses/all", method = RequestMethod.GET)
    private List<PLMWorkflowDefinitionStatus> getWorkflowDefinitionStatuses(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getWorkflowDefinitionStatuses(id);
    }

    @RequestMapping(value = "/{id}/statuses", method = RequestMethod.GET)
    private List<PLMWorkflowStatus> getWorkflowStatuses(@PathVariable("id") Integer id) {
        return plmWorkflowService.getWorkflowStatuses(id);
    }

    @RequestMapping(value = "/definitions/all/instances", method = RequestMethod.GET)
    private Page<WorkflowDto> getAllWorkflowInstances(PageRequest pageRequest, WorkflowCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plmWorkflowService.getAllWorkflowInstances(pageable, criteria);
    }


    @RequestMapping(value = "/filter/{object}", method = RequestMethod.GET)
    private Page<WorkflowDto> getFilterWorkflowInstances(@PathVariable("object") String object, PageRequest pageRequest, WorkflowCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plmWorkflowService.getFilterWorkflowInstancess(object, pageable, criteria);
    }


    @RequestMapping(value = "/statuses/{statusId}/approvers")
    public List<PLMWorkFlowStatusApprover> getApprovers(@PathVariable("statusId") Integer statusId) {
        return plmWorkflowService.getApprovers(statusId);
    }

    @RequestMapping(value = "/statuses/{statusId}/approvers", method = RequestMethod.POST)
    public List<PLMWorkFlowStatusApprover> addApprovers(@PathVariable("statusId") Integer statusId,
                                                        @RequestBody List<PLMWorkFlowStatusApprover> approvers) {
        return plmWorkflowService.addApprovers(statusId, approvers);
    }

    @RequestMapping(value = "/statuses/{statusId}/approvers/{assignmentId}", method = RequestMethod.PUT)
    public PLMWorkFlowStatusApprover updateApprover(@PathVariable("statusId") Integer statusId,
                                                    @PathVariable("assignmentId") Integer assignmentId,
                                                    @RequestBody PLMWorkFlowStatusApprover approver) {
        return plmWorkflowService.updateApprover(approver);
    }

    @RequestMapping(value = "/statuses/{statusId}/assignments/{assignmentId}", method = RequestMethod.DELETE)
    public void deleteAssignment(@PathVariable("statusId") Integer statusId,
                                 @PathVariable("assignmentId") Integer assignmentId) {
        plmWorkflowService.removeWorkflowAssignment(assignmentId);
    }

    @RequestMapping(value = "/statuses/{statusId}/observers")
    public List<PLMWorkFlowStatusObserver> getObservers(@PathVariable("statusId") Integer statusId) {
        return plmWorkflowService.getObservers(statusId);
    }

    @RequestMapping(value = "/statuses/{statusId}/observers", method = RequestMethod.POST)
    public List<PLMWorkFlowStatusObserver> addObservers(@PathVariable("statusId") Integer statusId,
                                                        @RequestBody List<PLMWorkFlowStatusObserver> observers) {
        return plmWorkflowService.addObservers(statusId, observers);
    }

    @RequestMapping(value = "/statuses/{statusId}/observers/{assignmentId}", method = RequestMethod.PUT)
    public PLMWorkFlowStatusObserver updateObserver(@PathVariable("statusId") Integer statusId,
                                                    @PathVariable("assignmentId") Integer assignmentId,
                                                    @RequestBody PLMWorkFlowStatusObserver observer) {
        return plmWorkflowService.updateObserver(observer);
    }


    @RequestMapping(value = "/statuses/{statusId}/acknowledgers/{assignmentId}", method = RequestMethod.PUT)
    public PLMWorkFlowStatusAcknowledger updateObserver(@PathVariable("statusId") Integer statusId,
                                                        @PathVariable("assignmentId") Integer assignmentId,
                                                        @RequestBody PLMWorkFlowStatusAcknowledger acknowledger) {
        return plmWorkflowService.updateAcknowledger(acknowledger);
    }

    @RequestMapping(value = "/statuses/{statusId}/acknowledgers")
    public List<PLMWorkFlowStatusAcknowledger> getAcknowledgers(@PathVariable("statusId") Integer statusId) {
        return plmWorkflowService.getAcknowledgers(statusId);
    }

    @RequestMapping(value = "/statuses/{statusId}/acknowledgers", method = RequestMethod.POST)
    public List<PLMWorkFlowStatusAcknowledger> addAcknowledgers(@PathVariable("statusId") Integer statusId,
                                                                @RequestBody List<PLMWorkFlowStatusAcknowledger> acknowledgers) {
        return plmWorkflowService.addAcknowledgers(statusId, acknowledgers);
    }

    @RequestMapping(value = "/{id}/assignments", method = RequestMethod.GET)
    public Map<Integer, WorkflowStatusAssignmentsDto> getWorkflowAssignments(@PathVariable("id") Integer id) {
        return plmWorkflowService.getWorkflowAssignments(id);
    }

    @RequestMapping(value = "/{wfId}/start")
    public PLMWorkflow startWorkflow(@PathVariable("wfId") Integer wfId) {
        return plmWorkflowService.startWorkflow(wfId);
    }

    @RequestMapping(value = "/{wfId}/promote")
    public PLMWorkflowStatus promoteWorkflow(@PathVariable("wfId") Integer wfId,
                                             @RequestParam("fromStatus") Integer fromStatus,
                                             @RequestParam("toStatus") Integer toStatus) {
        return plmWorkflowService.promoteWorkflow(fromStatus, toStatus);
    }

    @RequestMapping(value = "/{wfId}/demote")
    public PLMWorkflowStatus promoteWorkflow(@PathVariable("wfId") Integer wfId) {
        return plmWorkflowService.demoteWorkflow(wfId);
    }

    @RequestMapping(value = "/{wfId}/onhold")
    public Boolean putWorkflowOnHold(@PathVariable("wfId") Integer wfId,
                                     @RequestParam("currentStatus") Integer currentStatus,
                                     @RequestParam("notes") String notes) {
        return plmWorkflowService.putWorkflowOnHold(currentStatus, notes);
    }

    @RequestMapping(value = "/{wfId}/removeonhold")
    public Boolean removeWorkflowOnHold(@PathVariable("wfId") Integer wfId,
                                        @RequestParam("currentStatus") Integer currentStatus,
                                        @RequestParam("notes") String notes) {
        return plmWorkflowService.removeWorkflowOnHold(currentStatus, notes);
    }

    @RequestMapping(value = "/{wfId}/finish")
    public PLMWorkflow finishWorkflow(@PathVariable("wfId") Integer wfId) {
        return plmWorkflowService.finishWorkflow(wfId);
    }

    @RequestMapping(value = "/{wfId}/history")
    public List<PLMWorkflowStatusHistory> getWorkflowHistory(@PathVariable("wfId") Integer wfId) {
        return plmWorkflowService.getWorkflowHistory(wfId);
    }


    /* ------------------------- New Workflow Revision Methods --------------------------- */

    @RequestMapping(value = "/definitions/allRevisions", method = RequestMethod.GET)
    public Page<WorkflowRevisionDto> getAllWorkflowDefinitions(PageRequest pageRequest, WorkflowDefinitionCriteria definitionCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getAllWorkflows(pageable, definitionCriteria);
    }

    @RequestMapping(value = "/definitions/{id}/promote", method = RequestMethod.PUT)
    public PLMWorkflowDefinition promoteDefinition(@PathVariable("id") Integer id) {
        return workflowDefinitionService.promoteDefinition(id);
    }

    @RequestMapping(value = "/definitions/{id}/demote", method = RequestMethod.PUT)
    public PLMWorkflowDefinition demoteDefinition(@PathVariable("id") Integer id) {
        return workflowDefinitionService.demoteDefinition(id);
    }

    @RequestMapping(value = "/definitions/{id}/revise", method = RequestMethod.POST)
    public PLMWorkflowDefinition reviseWorkflowDefinition(@PathVariable("id") Integer id,
                                                          @RequestBody PLMWorkflowDefinition workflowDefinition) {
        return workflowDefinitionService.reviseDefinition(id, workflowDefinition);
    }

    @RequestMapping(value = "/revision/history/{id}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflowRevisionHistory(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getWorkflowRevisionHistory(id);
    }

    @RequestMapping(value = "/status/{id}/persons", method = RequestMethod.GET)
    public List<Person> getWorkflowAssignmentPersons(@PathVariable("id") Integer id,
                                                     @RequestParam("type") String type) {
        return plmWorkflowService.getWorkflowAssignmentPersons(id, type);
    }

    @RequestMapping(value = "/definitions/type", method = RequestMethod.GET)
    public Page<PLMWorkflowDefinition> getWorkflowDefsByType(PageRequest pageRequest, WorkflowDefinitionCriteria workflowDefinition) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getWorkflowDefsByType(pageable, workflowDefinition);
    }

    @RequestMapping(value = "/master/defs", method = RequestMethod.GET)
    public Page<PLMWorkflowDefinition> getAllWorkflowDefs(PageRequest pageRequest, WorkflowDefinitionCriteria definitionCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workflowDefinitionService.getAllWorkflowDefs(pageable, definitionCriteria);
    }

    @RequestMapping(value = "/type/tree", method = RequestMethod.GET)
    public List<PLMWorkflowType> getWorkflowTypeTree() {
        return plmWorkflowTypeService.getClassificationTree();
    }

    @RequestMapping(value = "/{id}/events", method = RequestMethod.GET)
    public List<WorkflowEventDto> getWorkflowEvents(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getWorkflowEvents(id);
    }

    @RequestMapping(value = "/{id}/events", method = RequestMethod.POST)
    public PLMWorkflowEvent createWorkflowEvent(@PathVariable("id") Integer id, @RequestBody PLMWorkflowEvent workflowEvent) {
        return workflowDefinitionService.createWorkflowEvent(id, workflowEvent);
    }

    @RequestMapping(value = "/{id}/events/{eventId}", method = RequestMethod.PUT)
    public PLMWorkflowEvent updateWorkflowEvent(@PathVariable("id") Integer id, @PathVariable("eventId") Integer eventId, @RequestBody PLMWorkflowEvent workflowEvent) {
        return workflowDefinitionService.updateWorkflowEvent(eventId, workflowEvent);
    }

    @RequestMapping(value = "/{id}/events/{eventId}", method = RequestMethod.DELETE)
    public void deleteWorkflowEvent(@PathVariable("id") Integer id, @PathVariable("eventId") Integer eventId) {
        workflowDefinitionService.deleteWorkflowEvent(id, eventId);
    }

    @RequestMapping(value = "/definitions/{id}/events", method = RequestMethod.GET)
    public List<WorkflowEventDto> getWorkflowDefinitionEvents(@PathVariable("id") Integer id) {
        return workflowDefinitionService.getWorkflowDefinitionEvents(id);
    }

    @RequestMapping(value = "/definitions/{id}/events", method = RequestMethod.POST)
    public PLMWorkflowDefinitionEvent createWorkflowDefinitionEvent(@PathVariable("id") Integer id, @RequestBody PLMWorkflowDefinitionEvent workflowEvent) {
        return workflowDefinitionService.createWorkflowDefinitionEvent(id, workflowEvent);
    }

    @RequestMapping(value = "/definitions/{id}/events/copy", method = RequestMethod.POST)
    public PLMWorkflowDefinition copyWorkflowDefinitionEvents(@PathVariable("id") Integer id, @RequestBody PLMWorkflowDefinition workflowDefinition) {
        return workflowDefinitionService.copyWorkflowDefinitionEvents(id, workflowDefinition);
    }

    @RequestMapping(value = "/definitions/{id}/events/{eventId}", method = RequestMethod.PUT)
    public PLMWorkflowDefinitionEvent updateWorkflowDefinitionEvent(@PathVariable("id") Integer id, @PathVariable("eventId") Integer eventId, @RequestBody PLMWorkflowDefinitionEvent workflowEvent) {
        return workflowDefinitionService.updateWorkflowDefinitionEvent(eventId, workflowEvent);
    }

    @RequestMapping(value = "/definitions/{id}/events/{eventId}", method = RequestMethod.DELETE)
    public void deleteWorkflowDefinitionEvent(@PathVariable("id") Integer id, @PathVariable("eventId") Integer eventId) {
        workflowDefinitionService.deleteWorkflowDefinitionEvent(id, eventId);
    }

    @RequestMapping(value = "/definitions/{id}/assignabletype", method = RequestMethod.GET)
    public CassiniObject getWorkflowAssignableTypeObjectType(@PathVariable("id") Integer id) {
        return plmWorkflowTypeService.getWorkflowAssignableTypeObjectType(id);
    }

    @RequestMapping(value = "/status/assigment/persons", method = RequestMethod.GET)
    public Page<Person> getWorkflowStatusAssignmentPersons(PageRequest pageRequest, WorkflowCriteria workflowCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return plmWorkflowService.getWorkflowStatusAssignmentPersons(pageable, workflowCriteria);
    }

    @RequestMapping(value = "/definitions/status/{id}/attributes", method = RequestMethod.GET)
    public List<PLMWorkflowActivityFormFields> getWorkflowStatusAttributes(@PathVariable("id") Integer id) {
        return plmWorkflowTypeService.getWorkflowStatusAttributes(id);
    }

    @RequestMapping(value = "/definitions/status/{id}/attributes", method = RequestMethod.POST)
    public PLMWorkflowActivityFormFields createWorkflowStatusAttribute(@PathVariable("id") Integer id,
                                                                       @RequestBody PLMWorkflowActivityFormFields attribute) {
        return plmWorkflowTypeService.createWorkflowStatusAttribute(attribute);
    }

    @RequestMapping(value = "/definitions/status/{id}/attributes/{attributeId}", method = RequestMethod.GET)
    public PLMWorkflowActivityFormFields getWorkflowStatusAttribute(@PathVariable("id") Integer id,
                                                                    @PathVariable("attributeId") Integer attributeId) {
        return plmWorkflowTypeService.getWorkflowStatusAttribute(attributeId);
    }

    @RequestMapping(value = "/definitions/status/{id}/attributes/{attributeId}", method = RequestMethod.PUT)
    public PLMWorkflowActivityFormFields updateWorkflowStatusAttribute(@PathVariable("id") Integer id,
                                                                       @PathVariable("attributeId") Integer attributeId,
                                                                       @RequestBody PLMWorkflowActivityFormFields attribute) {
        return plmWorkflowTypeService.updateWorkflowStatusAttribute(attribute);
    }

    @RequestMapping(value = "/definitions/status/{id}/attributes/{attributeId}", method = RequestMethod.DELETE)
    public void deleteWorkflowStatusAttribute(@PathVariable("id") Integer id,
                                              @PathVariable("attributeId") Integer attributeId) {
        plmWorkflowTypeService.deleteWorkflowStatusAttribute(attributeId);
    }

    @RequestMapping(value = "/definitions/status/{id}/name/{name}", method = RequestMethod.GET)
    public PLMWorkflowActivityFormFields getWorkflowStatusAttributeByName(@PathVariable("id") Integer id, @PathVariable("name") String name) {
        return plmWorkflowTypeService.getWorkflowStatusAttributeByName(id, name);
    }


    @RequestMapping(value = "/status/{id}/attributes", method = RequestMethod.POST)
    public PLMWorkflowActivityFormData createWorkflowStatusFormData(@PathVariable("id") Integer id,
                                                                    @RequestBody PLMWorkflowActivityFormData attribute) {
        return plmWorkflowTypeService.createWorkflowStatusFormData(attribute);
    }

    @RequestMapping(value = "/status/{id}/attributes", method = RequestMethod.PUT)
    public PLMWorkflowActivityFormData updateWorkflowStatusFormData(@PathVariable("id") Integer id,
                                                                    @RequestBody PLMWorkflowActivityFormData attribute) {
        return plmWorkflowTypeService.updateWorkflowStatusFormData(attribute);
    }

    @RequestMapping(value = "/object/status/{objectType}", method = RequestMethod.GET)
    public List<String> getObjectWorkflowStatus(@PathVariable("objectType") PLMObjectType objectType) {
        return plmWorkflowService.getObjectWorkflowStatus(objectType);
    }
}
