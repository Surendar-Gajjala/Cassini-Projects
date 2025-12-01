package com.cassinisys.plm.controller.pqm;

import java.util.List;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.wfm.WorkFlowService;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.SupplierAuditCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.pqm.PQMSupplierAudit;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditAttribute;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditPlan;
import com.cassinisys.plm.model.pqm.PQMSupplierAuditReviewer;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.pqm.SupplierAuditService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/pqm/supplieraudits")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class SupplierAuditController extends BaseController {

    @Autowired
    private SupplierAuditService supplierAuditService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PLMWorkflowService workflowService;


    @RequestMapping(method = RequestMethod.POST)
    private PQMSupplierAudit create(@RequestBody PQMSupplierAudit supplierAudit) {
        return supplierAuditService.create(supplierAudit);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    private PQMSupplierAudit update(@PathVariable("id") Integer id, @RequestBody PQMSupplierAudit supplierAudit) {
        return supplierAuditService.update(supplierAudit);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    private void delete(@PathVariable("id") Integer id) {
        supplierAuditService.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    private List<PQMSupplierAudit> getAll() {
        return supplierAuditService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PQMSupplierAudit> getAllSupplierAudits(PageRequest pageRequest, SupplierAuditCriteria supplierAuditCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return supplierAuditService.getAllSupplierAuditsByPageable(pageable, supplierAuditCriteria);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public DetailsCount getSupplierAuditTabCounts(@PathVariable Integer id) {
        return supplierAuditService.getSupplierAuditTabCounts(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private PQMSupplierAudit get(@PathVariable("id") Integer id) {
        return supplierAuditService.getById(id);
    }

    // --------------------------PQMSupplierAuditAttribute------------------------------------------------------


    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMSupplierAuditAttribute createSupplierAuditAttribute(@PathVariable("id") Integer id,
                                                                  @RequestBody PQMSupplierAuditAttribute attribute) {
        return supplierAuditService.createSupplierAuditAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMSupplierAuditAttribute updateSupplierAuditAttribute(@PathVariable("id") Integer id,
                                                                  @RequestBody PQMSupplierAuditAttribute attribute) {
        return supplierAuditService.updateSupplierAuditAttribute(attribute);
    }


    // --------------------------Supplier Audit plan------------------------------------------------------


    @RequestMapping(value = "/{id}/plan", method = RequestMethod.POST)
    private PQMSupplierAuditPlan createSupplierAuditPlan(@PathVariable Integer id, @RequestBody PQMSupplierAuditPlan supplierAuditPlan) {
        return supplierAuditService.createSupplierAuditPlan(supplierAuditPlan);
    }

    @RequestMapping(value = "/{id}/plan/multiple", method = RequestMethod.POST)
    private List<PQMSupplierAuditPlan> createMultipleSupplierAuditPlan(@PathVariable Integer id, @RequestBody List<PQMSupplierAuditPlan> supplierAuditPlans) {
        return supplierAuditService.createMultipleSupplierAuditPlans(supplierAuditPlans);
    }

    @RequestMapping(value = "/{id}/plan/{planId}", method = RequestMethod.PUT)
    private PQMSupplierAuditPlan updateSupplierAuditPlan(@PathVariable("id") Integer id, @PathVariable Integer planId, @RequestBody PQMSupplierAuditPlan supplierAuditPlan) {
        return supplierAuditService.updateSupplierAuditPlan(supplierAuditPlan);
    }

    @RequestMapping(value = "/{id}/plan/{planId}", method = RequestMethod.DELETE)
    private void deleteSupplierAuditPlan(@PathVariable("id") Integer id, @PathVariable Integer planId) {
        supplierAuditService.deleteSupplierAuditPlan(planId);
    }

    @RequestMapping(value = "{id}/plan", method = RequestMethod.GET)
    private List<PQMSupplierAuditPlan> getAllSupplierAuditPlans(@PathVariable Integer id) {
        return supplierAuditService.getAllSupplierAuditPlans(id);
    }

    @RequestMapping(value = "/{id}/plan/{planId}", method = RequestMethod.GET)
    private PQMSupplierAuditPlan getSupplierAuditPlan(@PathVariable("id") Integer id, @PathVariable Integer planId) {
        return supplierAuditService.getSupplierAuditPlanById(planId);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers", method = RequestMethod.POST)
    private PQMSupplierAuditReviewer createSupplierAuditReviewer(@PathVariable Integer planId, @RequestBody PQMSupplierAuditReviewer supplierAuditReviewer) {
        return supplierAuditService.createSupplierAuditReviewer(supplierAuditReviewer);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers/{reviewerId}", method = RequestMethod.PUT)
    private PQMSupplierAuditReviewer updateSupplierAuditReviewer(@PathVariable("planId") Integer planId, @PathVariable Integer reviewerId,
                                                                 @RequestBody PQMSupplierAuditReviewer supplierAuditReviewer) {
        return supplierAuditService.updateSupplierAuditReviewer(supplierAuditReviewer);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers/{reviewerId}", method = RequestMethod.DELETE)
    private void deleteSupplierAuditReviewer(@PathVariable("planId") Integer planId, @PathVariable("reviewerId") Integer reviewerId) {
        supplierAuditService.deleteSupplierAuditReviewer(reviewerId);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers", method = RequestMethod.GET)
    private List<PQMSupplierAuditReviewer> getAllSupplierAuditReviewers(@PathVariable Integer planId) {
        return supplierAuditService.getAllSupplierAuditReviewers(planId);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers/{reviewerId}", method = RequestMethod.GET)
    private PQMSupplierAuditReviewer getSupplierAuditReviewer(@PathVariable("planId") Integer planId, @PathVariable Integer reviewerId) {
        return supplierAuditService.getSupplierAuditReviewerById(reviewerId);
    }

     /*  supplier audit workflow methods   */


    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return supplierAuditService.attachNewSupplierAuditWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{auditId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("auditId") Integer mfrId) {
        workflowService.deleteWorkflow(mfrId);
    }

    @RequestMapping(value = "/workflow/{typeId}/supplieraudittype/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("typeId") Integer typeId,
                                                    @PathVariable("type") String type) {
        return supplierAuditService.getHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/plan/{planId}/reviewers/submit", method = RequestMethod.PUT)
    public PQMSupplierAuditReviewer submitReview(@PathVariable("planId") Integer planId, @RequestBody PQMSupplierAuditReviewer supplierAuditReviewer) {
        return supplierAuditService.submitReview(planId, supplierAuditReviewer);
    }

}