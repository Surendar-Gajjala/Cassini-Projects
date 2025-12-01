package com.cassinisys.plm.controller.req;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.service.wfm.WorkFlowService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.RequirementCriteria;
import com.cassinisys.plm.filtering.RequirementDocumentCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.RequirementDocumentsDto;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.req.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.req.ReqDocumentService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 27-10-2020.
 */
@RestController
@RequestMapping("/req/documents")
@Api(tags = "PLM.PGC", description = "PGC Related")
public class ReqDocumentController extends BaseController {


    @Autowired
    private ReqDocumentService reqDocumentService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMRequirementDocument create(@RequestBody PLMRequirementDocument substance) {
        return reqDocumentService.create(substance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMRequirementDocument update(@PathVariable("id") Integer id,
                                         @RequestBody PLMRequirementDocument substance) {
        return reqDocumentService.update(substance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        reqDocumentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMRequirementDocument get(@PathVariable("id") Integer id) {
        return reqDocumentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMRequirementDocument> getAll() {
        return reqDocumentService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMRequirementDocument> getMultiple(@PathVariable Integer[] ids) {
        return reqDocumentService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<RequirementDocumentsDto> getAllReqDocuments(PageRequest pageRequest, RequirementDocumentCriteria requirementDocumentCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return reqDocumentService.getAllRequirementDocuments(pageable, requirementDocumentCriteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<PLMRequirementDocument> getObjectsByType(@PathVariable("typeId") Integer id,
                                                         PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return reqDocumentService.getReqDocObjectsByType(id, pageable);
    }

    @RequestMapping(value = {"/revisions/multiple/[{docRevisionIds}]"}, method = {RequestMethod.GET})
    public List<PLMRequirementDocumentRevision> getDocumentRevisionsByIds(@PathVariable Integer[] docRevisionIds) {
        return reqDocumentService.getDocumentRevisionsByIds(Arrays.asList(docRevisionIds));
    }

    @RequestMapping(value = "/revision/{revisionId}", method = RequestMethod.GET)
    public PLMRequirementDocumentRevision getReqDocumentRevision(@PathVariable("revisionId") Integer revisionId) {
        return reqDocumentService.getReqDocumentRevision(revisionId);
    }

    @RequestMapping(value = "/revision/{id}", method = RequestMethod.PUT)
    public PLMRequirementDocumentRevision updateDocumentRevision(@PathVariable("id") Integer id,
                                                                 @RequestBody PLMRequirementDocumentRevision revision) {
        return reqDocumentService.updateDocumentRevision(revision);
    }

    @RequestMapping(value = "/requirements", method = RequestMethod.POST)
    public PLMRequirementDocumentChildren createRequirement(@RequestBody PLMRequirementVersion requirementVersion) {
        return reqDocumentService.createRequirement(requirementVersion);
    }

    @RequestMapping(value = "/requirements/{id}", method = RequestMethod.PUT)
    public PLMRequirement updateRequirement(@PathVariable("id") Integer id,
                                            @RequestBody PLMRequirement requirement) {
        return reqDocumentService.updateRequirement(requirement);
    }

    @RequestMapping(value = "/requirements/versions/{id}", method = RequestMethod.PUT)
    public PLMRequirementVersion updateRequirementVersion(@PathVariable("id") Integer id,
                                                          @RequestBody PLMRequirementVersion requirementVersion) {
        return reqDocumentService.updateRequirementVersion(id, requirementVersion);
    }

    @RequestMapping(value = "/requirements/{id}", method = RequestMethod.DELETE)
    public void deleteRequirement(@PathVariable("id") Integer id) {
        reqDocumentService.deleteRequirement(id);
    }

    @RequestMapping(value = "/requirements/{id}", method = RequestMethod.GET)
    public PLMRequirement getRequirement(@PathVariable("id") Integer id) {
        return reqDocumentService.getRequirement(id);
    }

    @RequestMapping(value = "/requirements/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMRequirement> findMultipleRequirement(@PathVariable Integer[] ids) {
        return reqDocumentService.findMultipleRequirement(Arrays.asList(ids));
    }

    @RequestMapping(value = "/revision/{revisionId}/requirements/tree", method = RequestMethod.GET)
    public List<PLMRequirementVersion> getRequirementTree(@PathVariable("revisionId") Integer revisionId) {
        return reqDocumentService.getRequirementTree(revisionId);
    }

    @RequestMapping(value = "/requirements/version/{versionId}", method = RequestMethod.GET)
    public PLMRequirementVersion getRequirementVersion(@PathVariable("versionId") Integer versionId) {
        return reqDocumentService.getRequirementVersion(versionId);
    }

    @RequestMapping(value = "/requirements/version/{versionId}/mobile", method = RequestMethod.GET)
    public RequirementDto getRequirementVersionDetails(@PathVariable("versionId") Integer versionId) {
        return reqDocumentService.getRequirementVersionDetails(versionId);
    }

    @RequestMapping(value = "/requirements/search", method = RequestMethod.GET)
    public List<PLMRequirementVersion> getAllRequirements(RequirementCriteria requirementCriteria) {
        return reqDocumentService.getAllRequirements(requirementCriteria);
    }

    @RequestMapping(value = "/requirements/{id}/reviewers", method = RequestMethod.POST)
    public PLMRequirementReviewer addReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementReviewer reviewer) {
        return reqDocumentService.addReviewer(id, reviewer);
    }

    @RequestMapping(value = "/requirements/{id}/reviewers/{reviewerId}", method = RequestMethod.PUT)
    public PLMRequirementReviewer updateReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementReviewer reviewer) {
        return reqDocumentService.updateReviewer(id, reviewer);
    }

    @RequestMapping(value = "/requirements/{id}/reviewers/{reviewerId}", method = RequestMethod.DELETE)
    public void deleteReviewer(@PathVariable("id") Integer id, @PathVariable("reviewerId") Integer reviewerId) {
        reqDocumentService.deleteReviewer(id, reviewerId);
    }

    @RequestMapping(value = "/{id}/submit", method = RequestMethod.GET)
    public PLMRequirementDocumentRevision submitReqDocument(@PathVariable("id") Integer id) {
        return reqDocumentService.submitReqDocument(id);
    }

    @RequestMapping(value = "/{id}/release", method = RequestMethod.GET)
    public PLMRequirementDocumentRevision releaseReqDocument(@PathVariable("id") Integer id) {
        return reqDocumentService.releaseReqDocument(id);
    }

    @RequestMapping(value = "/requirements/{id}/approve", method = RequestMethod.PUT)
    public PLMRequirementReviewer approveRequirement(@PathVariable("id") Integer id,
                                                     @RequestBody PLMRequirementReviewer reviewer) {
        return reqDocumentService.approveRequirement(id, reviewer, false);
    }

    @RequestMapping(value = "/requirements/{id}/reject", method = RequestMethod.PUT)
    public PLMRequirementReviewer rejectRequirement(@PathVariable("id") Integer id,
                                                    @RequestBody PLMRequirementReviewer reviewer) {
        return reqDocumentService.rejectRequirement(id, reviewer);
    }

    @RequestMapping(value = "/requirements/{id}/review", method = RequestMethod.PUT)
    public PLMRequirementReviewer reviewRequirement(@PathVariable("id") Integer id,
                                                    @RequestBody PLMRequirementReviewer reviewer) {
        return reqDocumentService.reviewRequirement(id, reviewer);
    }

    @RequestMapping(value = "/requirements/{id}/reviewers", method = RequestMethod.GET)
    public List<PLMRequirementReviewer> getReviewers(@PathVariable("id") Integer id) {
        return reqDocumentService.getReviewers(id);
    }

    @RequestMapping(value = "/requirements/all", method = RequestMethod.GET)
    public Page<PLMRequirement> getRequirements(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return reqDocumentService.getRequirements(pageable);
    }

    @RequestMapping(value = "/requirements/freeTextSearch", method = RequestMethod.GET)
    public Page<PLMRequirement> freeTextSearch(PageRequest pageRequest, RequirementCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMRequirement> requirements = reqDocumentService.freeTextSearch(pageable, criteria);
        return requirements;
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getReqDocumentTabCounts(@PathVariable("id") Integer id) {
        return reqDocumentService.getReqDocumentTabCounts(id);
    }

    @RequestMapping(value = "/requirements/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getRequirementTabCounts(@PathVariable("id") Integer id) {
        return reqDocumentService.getRequirementTabCounts(id);
    }

    @RequestMapping(value = "/reqtype/{typeId}", method = RequestMethod.GET)
    public Page<PLMRequirement> getReqObjectsByType(@PathVariable("typeId") Integer id,
                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return reqDocumentService.getReqObjectsByType(id, pageable);
    }

    @RequestMapping(value = "/{id}/reviewers", method = RequestMethod.POST)
    public PLMRequirementDocumentReviewer addRequirementDocumentReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementDocumentReviewer reviewer) {
        return reqDocumentService.addRequirementDocumentReviewer(id, reviewer);
    }

    @RequestMapping(value = "/{id}/reviewers/{reviewerId}", method = RequestMethod.PUT)
    public PLMRequirementDocumentReviewer updateRequirementDocumentReviewer(@PathVariable("id") Integer id, @RequestBody PLMRequirementDocumentReviewer reviewer) {
        return reqDocumentService.updateRequirementDocumentReviewer(id, reviewer);
    }

    @RequestMapping(value = "/{id}/reviewers/{reviewerId}", method = RequestMethod.DELETE)
    public void deleteRequirementDocumentReviewer(@PathVariable("id") Integer id, @PathVariable("reviewerId") Integer reviewerId) {
        reqDocumentService.deleteRequirementDocumentReviewer(id, reviewerId);
    }

    @RequestMapping(value = "/{id}/reviewers", method = RequestMethod.GET)
    public List<PLMRequirementDocumentReviewer> getRequirementDocumentReviewers(@PathVariable("id") Integer id) {
        return reqDocumentService.getRequirementDocumentReviewers(id);
    }

    @RequestMapping(value = "/{id}/approve", method = RequestMethod.PUT)
    public PLMRequirementDocumentReviewer approveRequirementDocument(@PathVariable("id") Integer id,
                                                                     @RequestBody PLMRequirementDocumentReviewer reviewer) {
        return reqDocumentService.approveRequirementDocument(id, reviewer);
    }

    @RequestMapping(value = "/{id}/reject", method = RequestMethod.PUT)
    public PLMRequirementDocumentReviewer rejectRequirementDocument(@PathVariable("id") Integer id,
                                                                    @RequestBody PLMRequirementDocumentReviewer reviewer) {
        return reqDocumentService.rejectRequirementDocument(id, reviewer);
    }

    @RequestMapping(value = "/{id}/review", method = RequestMethod.PUT)
    public PLMRequirementDocumentReviewer reviewRequirementDocument(@PathVariable("id") Integer id,
                                                                    @RequestBody PLMRequirementDocumentReviewer reviewer) {
        return reqDocumentService.reviewRequirementDocument(id, reviewer);
    }

    @RequestMapping(value = "/{id}/requirements/all/approve", method = RequestMethod.PUT)
    public PLMRequirementDocumentRevision approveAllRequirement(@PathVariable("id") Integer id) {
        return reqDocumentService.approveAllRequirement(id);
    }

    @RequestMapping(value = "/{reqDocId}/template", method = RequestMethod.POST)
    public PLMRequirementDocumentTemplate saveAsReqDocumentTemplate(@PathVariable("reqDocId") Integer reqDocId, @RequestBody PLMRequirementDocumentTemplate requirementDocumentTemplate) {
        return reqDocumentService.saveAsReqDocumentTemplate(reqDocId, requirementDocumentTemplate);
    }

    @RequestMapping(value = "/requirements/{reqId}/items/multiple", method = RequestMethod.POST)
    public List<PLMRequirementItem> createRequirementItems(@PathVariable("reqId") Integer reqId, @RequestBody List<PLMRequirementItem> requirementItems) {
        return reqDocumentService.createRequirementItems(reqId,requirementItems);
    }

    @RequestMapping(value = "/requirements/{reqId}/items", method = RequestMethod.POST)
    public PLMRequirementItem createRequirementItem(@PathVariable("reqId") Integer reqId, @RequestBody PLMRequirementItem requirementItem) {
        return reqDocumentService.createRequirementItem(reqId, requirementItem);
    }

    @RequestMapping(value = "/requirements/{reqId}/items", method = RequestMethod.GET)
    public List<PLMRequirementItem> getRequirementItems(@PathVariable("reqId") Integer reqId) {
        return reqDocumentService.getRequirementItems(reqId);
    }

    @RequestMapping(value = "/requirements/{reqId}/items/{reqItemId}", method = RequestMethod.DELETE)
    public void deleteRequirementItem(@PathVariable("reqId") Integer reqId, @PathVariable("reqItemId") Integer reqItemId) {
        reqDocumentService.deleteRequirementItem(reqId, reqItemId);
    }

    @RequestMapping(value = "/requirements/{reqId}/items/{reqItemId}", method = RequestMethod.PUT)
    public PLMRequirementItem updateRequirementItem(@PathVariable("reqId") Integer reqId, @PathVariable("reqItemId") Integer reqItemId,
                                                    @RequestBody PLMRequirementItem requirementItem) {
        return reqDocumentService.updateRequirementItem(reqItemId, requirementItem);
    }

    @RequestMapping(value = "/item/{itemId}/requirements", method = RequestMethod.GET)
    public List<PLMRequirementItem> getItemRequirements(@PathVariable("itemId") Integer itemId) {
        return reqDocumentService.getItemRequirements(itemId);
    }

    @RequestMapping(value = "/{dcoId}/revisions/history", method = RequestMethod.GET)
    public List<PLMRequirementDocumentRevision> getReqDocumentRevisionStatusHistory(@PathVariable("dcoId") Integer dcoId) {
        return reqDocumentService.getReqDocumentRevisionStatusHistory(dcoId);
    }

    @RequestMapping(value = "/{dcoId}/revise", method = RequestMethod.POST)
    public PLMRequirementDocumentRevision reviseReqDocument(@RequestBody PLMRequirementDocumentRevision revision, @PathVariable("dcoId") Integer dcoId) {
        return reqDocumentService.reviseReqDocument(revision, dcoId);
    }

    @RequestMapping(value = "/requirement/{reqId}/revise", method = RequestMethod.POST)
    public PLMRequirementDocumentChildren reviseRequirement(@RequestBody PLMRequirementDocumentChildren version, @PathVariable("reqId") Integer reqId) {
        return reqDocumentService.reviseRequirement(version, reqId);
    }

    @RequestMapping(value = "/requirement/{reqId}/versions/history", method = RequestMethod.GET)
    public List<PLMRequirementVersion> getReqVersionStatusHistory(@PathVariable("reqId") Integer reqId) {
        return reqDocumentService.getReqVersionStatusHistory(reqId);
    }

    @RequestMapping(value = "/revision/{revisionId}/requirements/children/tree", method = RequestMethod.GET)
    public List<PLMRequirementDocumentChildren> getRequirementVersionsTree(@PathVariable("revisionId") Integer revisionId) {
        return reqDocumentService.getRequirementVersionsTree(revisionId);
    }

    @RequestMapping(value = "/requirements/children/version/{versionId}", method = RequestMethod.GET)
    public PLMRequirementDocumentChildren getRequirementChildrenVersion(@PathVariable("versionId") Integer versionId) {
        return reqDocumentService.getRequirementChildrenVersion(versionId);
    }

    @RequestMapping(value = "/requirement/objects/search", method = RequestMethod.GET)
    public List<PLMRequirementDocumentChildren> getAllReqDocChildrens(RequirementCriteria requirementCriteria) {
        return reqDocumentService.getAllReqDocChildrens(requirementCriteria);
    }

    @RequestMapping(value = "/requirements/children/{id}", method = RequestMethod.DELETE)
    public void deleteRequirementDocChildren(@PathVariable("id") Integer id) {
        reqDocumentService.deleteRequirementDocChildren(id);
    }

    @RequestMapping(value = "/requirement/{id}/submit", method = RequestMethod.GET)
    public PLMRequirementVersion submitReqVersion(@PathVariable("id") Integer id) {
        return reqDocumentService.submitReqVersion(id);
    }

    @RequestMapping(value = "/template/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getReqDocumentTemplateTabCounts(@PathVariable("id") Integer id) {
        return reqDocumentService.getReqDocumentTemplateTabCounts(id);
    }

    @RequestMapping(value = "/template/requirement/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getRequirementTemplateTabCounts(@PathVariable("id") Integer id) {
        return reqDocumentService.getRequirementTemplateTabCounts(id);
    }
    @RequestMapping(value = "/type/{typeId}/workflow/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getReqDocWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return reqDocumentService.getReqDocHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/requirement/type/{typeId}/workflow/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getRequirementWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return reqDocumentService.getRequirementHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachReqDocWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return reqDocumentService.attachReqDocWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/requirements/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachReqWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return reqDocumentService.attachReqWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{id}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("id") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "/attributes/{attributeId}", method = RequestMethod.GET)
    public List<PLMRequirementObjectAttribute> getAttributeUsedItems(@PathVariable("attributeId") Integer attributeId) {
        return reqDocumentService.getReqDocumentUsedAttributes(attributeId);
    }

    @RequestMapping(value = "/{reqDocId}/revisions/ids", method = RequestMethod.GET)
    public List<Integer> getReqDocRevisionIds(@PathVariable("reqDocId") Integer reqDocId) {
        return reqDocumentService.getReqDocRevisionIds(reqDocId);
    }
    @RequestMapping(value = "/requirements/{reqId}/versions/ids", method = RequestMethod.GET)
    public List<Integer> getReqVersionIds(@PathVariable("reqId") Integer reqId) {
        return reqDocumentService.getReqVersionIds(reqId);
    }

    @RequestMapping(value = "/types/lifecycles", method = RequestMethod.GET)
    public List<PLMLifeCycle> getItemTypeLifecycles() {
        return reqDocumentService.getReqDocTypeLifecycles();
    }

    @RequestMapping(value = "/owners", method = RequestMethod.GET)
    public List<Person> getReqDocOwner() {
        return reqDocumentService.getReqDocOwners();
    }

    @RequestMapping(value = "/assignedTo/{reqDocId}", method = RequestMethod.GET)
    public List<Person> getAssignedTo(@PathVariable("reqDocId") Integer reqDocId) {
        return reqDocumentService.getAssignedTo(reqDocId);
    }

    @RequestMapping(value = "/priority/{reqDocId}", method = RequestMethod.GET)
    public List<String> getpriority(@PathVariable("reqDocId") Integer reqDocId) {
        return reqDocumentService.getReqPriorities(reqDocId);
    }

    @RequestMapping(value = "/lifecyclePhases/{reqDocId}", method = RequestMethod.GET)
    public List<PLMLifeCyclePhase> getLifecyclePhases(@PathVariable("reqDocId") Integer reqDocId) {
        return reqDocumentService.getReqLifecycles(reqDocId);
    }

}


