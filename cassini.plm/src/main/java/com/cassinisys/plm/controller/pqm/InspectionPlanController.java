package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.InspectionPlanCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.InspectionPlansDto;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.InspectionPlanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@RestController
@RequestMapping("/pqm/inspectionplans")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class InspectionPlanController extends BaseController {

    @Autowired
    private InspectionPlanService inspectionPlanService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PQMInspectionPlan create(@RequestBody PQMInspectionPlan inspectionPlan) {
        return inspectionPlanService.create(inspectionPlan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PQMInspectionPlan update(@PathVariable("id") Integer id,
                                    @RequestBody PQMInspectionPlan inspectionPlan) {
        return inspectionPlanService.update(inspectionPlan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        inspectionPlanService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PQMInspectionPlan get(@PathVariable("id") Integer id) {
        return inspectionPlanService.get(id);
    }

    @RequestMapping(value = "/{id}/mobile", method = RequestMethod.GET)
    public InspectionPlansDto getPlanDetails(@PathVariable("id") Integer id) {
        return inspectionPlanService.getPlanDetails(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PQMInspectionPlan> getAll() {
        return inspectionPlanService.getAll();
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PQMInspectionPlan> getMultiple(@PathVariable Integer[] ids) {
        return inspectionPlanService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public PQMProductInspectionPlan updateProductInspectionPlan(@PathVariable("id") Integer id,
                                                                @RequestBody PQMProductInspectionPlan inspectionPlan) {
        return inspectionPlanService.updateProductInspectionPlan(inspectionPlan);
    }

    @RequestMapping(value = "/revise/{id}", method = RequestMethod.GET)
    public PQMInspectionPlanRevision reviseInspectionPlan(@PathVariable("id") Integer id) {
        return inspectionPlanService.revisePlan(id);
    }

    @RequestMapping(value = "/revision/history/{id}", method = RequestMethod.GET)
    public List<PQMInspectionPlanRevision> getPlanRevisionHistory(@PathVariable("id") Integer id) {
        return inspectionPlanService.getPlanRevisionHistory(id);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public void deleteProductInspectionPlan(@PathVariable("id") Integer id) {
        inspectionPlanService.deleteProductInspectionPlan(id);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public PQMProductInspectionPlan getProductInspectionPlan(@PathVariable("id") Integer id) {
        return inspectionPlanService.getProductInspectionPlan(id);
    }

    @RequestMapping(value = "/products/all", method = RequestMethod.GET)
    public Page<InspectionPlansDto> getAllProductInspectionPlans(PageRequest pageRequest, InspectionPlanCriteria planCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inspectionPlanService.getAllProductInspectionPlans(pageable, planCriteria);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.PUT)
    public PQMMaterialInspectionPlan updateMaterialInspectionPlan(@PathVariable("id") Integer id,
                                                                  @RequestBody PQMMaterialInspectionPlan inspectionPlan) {
        return inspectionPlanService.updateMaterialInspectionPlan(inspectionPlan);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.DELETE)
    public void deleteMaterialInspectionPlan(@PathVariable("id") Integer id) {
        inspectionPlanService.deleteMaterialInspectionPlan(id);
    }

    @RequestMapping(value = "/materials/{id}", method = RequestMethod.GET)
    public PQMMaterialInspectionPlan getMaterialInspectionPlan(@PathVariable("id") Integer id) {
        return inspectionPlanService.getMaterialInspectionPlan(id);
    }

    @RequestMapping(value = "/materials/all", method = RequestMethod.GET)
    public Page<InspectionPlansDto> getAllMaterialInspectionPlans(PageRequest pageRequest, InspectionPlanCriteria planCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return inspectionPlanService.getAllMaterialInspectionPlans(pageable, planCriteria);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<PQMInspectionPlan> getInspectionPlans() {
        return inspectionPlanService.getInspectionPlans();
    }

    @RequestMapping(value = "/products/{product}/released", method = RequestMethod.GET)
    public List<PQMProductInspectionPlan> getInspectionPlansByProduct(@PathVariable("product") Integer product) {
        return inspectionPlanService.getProductInspectionPlansByProduct(product);
    }

    @RequestMapping(value = "/materials/{product}/released", method = RequestMethod.GET)
    public List<PQMMaterialInspectionPlan> getMaterialInspectionPlansByProduct(@PathVariable("product") Integer product) {
        return inspectionPlanService.getMaterialInspectionPlansByProduct(product);
    }

    @RequestMapping(value = "/list/released", method = RequestMethod.GET)
    public List<PQMInspectionPlan> getReleasedInspectionPlans() {
        return inspectionPlanService.getReleasedInspectionPlans();
    }

    @RequestMapping(value = "/revisions/{id}", method = RequestMethod.GET)
    public PQMInspectionPlanRevision getInspectionPlanRevision(@PathVariable("id") Integer id) {
        return inspectionPlanService.getInspectionPlanRevision(id);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.POST)
    public PQMInspectionPlanAttribute createPlanAttribute(@PathVariable("id") Integer id,
                                                          @RequestBody PQMInspectionPlanAttribute attribute) {
        return inspectionPlanService.createPlanAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/revisionAttributes", method = RequestMethod.POST)
    public PQMInspectionPlanRevisionAttribute createPlanRevisionAttribute(@PathVariable("id") Integer id,
                                                                          @RequestBody PQMInspectionPlanRevisionAttribute attribute) {
        return inspectionPlanService.createPlanRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/attributes", method = RequestMethod.PUT)
    public PQMInspectionPlanAttribute updatePlanAttribute(@PathVariable("id") Integer id,
                                                          @RequestBody PQMInspectionPlanAttribute attribute) {
        return inspectionPlanService.updatePlanAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/revisionAttributes", method = RequestMethod.PUT)
    public PQMInspectionPlanRevisionAttribute updatePlanRevisionAttribute(@PathVariable("id") Integer id,
                                                                          @RequestBody PQMInspectionPlanRevisionAttribute attribute) {
        return inspectionPlanService.updatePlanRevisionAttribute(attribute);
    }

    @RequestMapping(value = "/{id}/checklists", method = RequestMethod.POST)
    public PQMInspectionPlanChecklist createInspectionPlanChecklist(@PathVariable("id") Integer id, @RequestBody PQMInspectionPlanChecklist planChecklist) {
        return inspectionPlanService.createInspectionPlanChecklist(id, planChecklist);
    }

    @RequestMapping(value = "/{id}/checklists", method = RequestMethod.PUT)
    public PQMInspectionPlanChecklist updateInspectionPlanChecklist(@PathVariable("id") Integer id, @RequestBody PQMInspectionPlanChecklist planChecklist) {
        return inspectionPlanService.updateInspectionPlanChecklist(id, planChecklist);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}", method = RequestMethod.DELETE)
    public void deleteInspectionPlanChecklist(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId) {
        inspectionPlanService.deleteInspectionPlanChecklist(id, checklistId);
    }

    @RequestMapping(value = "/{id}/checklists", method = RequestMethod.GET)
    public List<PQMInspectionPlanChecklist> getInspectionPlanChecklists(@PathVariable("id") Integer id) {
        return inspectionPlanService.getInspectionPlanChecklists(id);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/children", method = RequestMethod.GET)
    public List<PQMInspectionPlanChecklist> getInspectionPlanChecklistChildren(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId) {
        return inspectionPlanService.getInspectionPlanChecklistChildren(id, checklistId);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params", method = RequestMethod.GET)
    public List<PQMInspectionPlanChecklistParameter> getChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId) {
        return inspectionPlanService.getChecklistParams(id, checklistId);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params", method = RequestMethod.POST)
    public PQMInspectionPlanChecklistParameter createChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId,
                                                                     @RequestBody PQMInspectionPlanChecklistParameter parameter) {
        return inspectionPlanService.createChecklistParams(id, checklistId, parameter);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params", method = RequestMethod.PUT)
    public PQMInspectionPlanChecklistParameter updateChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId,
                                                                     @RequestBody PQMInspectionPlanChecklistParameter parameter) {
        return inspectionPlanService.updateChecklistParams(id, checklistId, parameter);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/params/{paramId}", method = RequestMethod.DELETE)
    public void deleteChecklistParams(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId, @PathVariable("paramId") Integer paramId) {
        inspectionPlanService.deleteChecklistParams(id, checklistId, paramId);
    }

    @RequestMapping(value = "/{id}/checklists/{checklistId}/attachments", method = RequestMethod.POST)
    public ObjectFileDto uploadAttributeAttachments(@PathVariable("id") Integer id, @PathVariable("checklistId") Integer checklistId,
                                                     MultipartHttpServletRequest request) throws Exception {
        return inspectionPlanService.uploadAttributeAttachments(id, checklistId, request);
    }

    @RequestMapping(value = "{id}/details/count", method = RequestMethod.GET)
    public ItemDetailsDto getPlanDetailsCount(@PathVariable("id") Integer id) {
        return inspectionPlanService.getPlanDetailsCount(id);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
    public Page<PQMInspection> getInspectionPlanTasks(@PathVariable("id") Integer id, PageRequest pageRequest) {
        return inspectionPlanService.getInspectionPlanTasks(id, pageRequest);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public List<String> getStatus() {
        return inspectionPlanService.getStatus();
    }

    @RequestMapping(value = "/types/lifecycles", method = RequestMethod.GET)
    public List<PLMLifeCycle> getInspectionTypeLifecycles() {
        return inspectionPlanService.getInspectionTypeLifecycles();
    }
}
