package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.BOPCriteria;
import com.cassinisys.plm.filtering.MESObjectCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.MESObjectDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.mes.BOPService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/mes/bops")
@Api(tags = "PLM.MES", description = "MES Related")
public class BOPController extends BaseController {

    @Autowired
    private BOPService bopService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESBOP create(@RequestBody MESBOP bop) {
        return bopService.create(bop);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESBOP update(@PathVariable("id") Integer id,
                         @RequestBody MESBOP bop) {
        return bopService.update(bop);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        bopService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESBOP get(@PathVariable("id") Integer id) {
        return bopService.get(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getBOPCounts(@PathVariable("id") Integer id) {
        return bopService.getBOPCounts(id);
    }

    @RequestMapping(value = "/operations/{operationId}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getBOPPlanCounts(@PathVariable("operationId") Integer operationId) {
        return bopService.getBOPPlanCounts(operationId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESBOP> getAll() {
        return bopService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<BOPDto> getAllBOPS(PageRequest pageRequest, BOPCriteria bopCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bopService.getAllBopsByPageable(pageable, bopCriteria);
    }

    @RequestMapping(value = "/revisions/{id}", method = RequestMethod.GET)
    public MESBOPRevision getBOPRevision(@PathVariable("id") Integer id) {
        return bopService.getBOPRevision(id);
    }

    @RequestMapping(value = "/{id}/revisions/history", method = RequestMethod.GET)
    public List<MESBOPRevision> getBOPRevisionHistory(@PathVariable("id") Integer id) {
        return bopService.getBOPRevisionHistory(id);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachBOPWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return bopService.attachBOPWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/create/attributes/multiple", method = RequestMethod.POST)
    public void saveBopAttributes(@RequestBody List<MESObjectAttribute> attributes) {
        bopService.saveBopAttributes(attributes);
    }

    @RequestMapping(value = "/update/attributes", method = RequestMethod.PUT)
    public MESObjectAttribute updateBopAttribute(@PathVariable("id") Integer id,
                                                 @RequestBody MESObjectAttribute attribute) {
        return bopService.updateBopAttribute(attribute);
    }

    @RequestMapping(value = "/uploadimageattribute/{objectid}/{attributeid}", method = RequestMethod.POST)
    public MESBOP saveImageAttributeValue(@PathVariable("objectid") Integer objectId,
                                          @PathVariable("attributeid") Integer attributeId, MultipartHttpServletRequest request) {
        return bopService.saveImageAttributeValue(objectId, attributeId, request.getFileMap());
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESBOP> getBOPsByType(@PathVariable("typeId") Integer id,
                                      PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bopService.getBOPsByType(id, pageable);
    }


    @RequestMapping(value = "/{id}/{objectType}/workflows", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getHierarchyWorkflows(@PathVariable("id") Integer id, @PathVariable("objectType") String type) {
        return bopService.getHierarchyWorkflows(id, type);
    }

    @RequestMapping(value = "/{id}/routes", method = RequestMethod.POST)
    public MESBOPRouteOperation creteBopRouteItem(@PathVariable("id") Integer id, @RequestBody MESBOPRouteOperation mesBopRouteOperation) {
        return bopService.createBopRouteItem(mesBopRouteOperation);
    }


    @RequestMapping(value = "/{id}/routes/{routeId}", method = RequestMethod.PUT)
    public MESBOPRouteOperation updateBopRouteItem(@PathVariable("id") Integer id, @PathVariable("routeId") Integer routeId,
                                                   @RequestBody MESBOPRouteOperation mesBopRouteOperation) {
        return bopService.updateBopRouteItem(mesBopRouteOperation);
    }

    @RequestMapping(value = "/{id}/routes/{routeId}", method = RequestMethod.DELETE)
    public void deleteBopRouteItem(@PathVariable("id") Integer id, @PathVariable("routeId") Integer routeId) {
        bopService.deleteBopRouteItem(routeId);
    }

    @RequestMapping(value = "/{id}/routes/multiple", method = RequestMethod.POST)
    public List<MESBOPRouteOperation> creteBopRouteItems(@PathVariable("id") Integer id, @RequestBody List<MESBOPRouteOperation> mesBopRouteOperation) {
        return bopService.createBopRouteItems(mesBopRouteOperation);
    }

    @RequestMapping(value = "/{id}/routes/{routeId}", method = RequestMethod.GET)
    public BOPRouteDto getBopRouteItem(@PathVariable("id") Integer id, @PathVariable("routeId") Integer routeId) {
        return bopService.getBopRouteItem(routeId);
    }

    @RequestMapping(value = "/{id}/routes/{routeId}/children", method = RequestMethod.GET)
    public List<BOPRouteDto> getBopRouteItemChildren(@PathVariable("id") Integer id, @PathVariable("routeId") Integer routeId) {
        return bopService.getBopRouteItemChildren(routeId);
    }

    @RequestMapping(value = "/{id}/routes", method = RequestMethod.GET)
    public List<BOPRouteDto> getBopRoutes(@PathVariable("id") Integer id) {
        return bopService.getBopRoutes(id);
    }

    @RequestMapping(value = "/operations/{operationId}/resources", method = RequestMethod.GET)
    public List<ResourceDto> getBopOperationResources(@PathVariable("operationId") Integer operationId) {
        return bopService.getBopOperationResources(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/resources/multiple", method = RequestMethod.POST)
    public List<MESBOPOperationResource> createBopOperationResources(@PathVariable("operationId") Integer operationId,
                                                                     @RequestBody List<MESBOPOperationResource> planResources) {
        return bopService.createBopOperationResources(operationId, planResources);
    }

    @RequestMapping(value = "/operations/{operationId}/resources", method = RequestMethod.POST)
    public MESBOPOperationResource createBopOperationResource(@PathVariable("operationId") Integer operationId,
                                                              @RequestBody MESBOPOperationResource planResource) {
        return bopService.createBopOperationResource(operationId, planResource);
    }

    @RequestMapping(value = "/operations/{operationId}/resources/{resourceId}", method = RequestMethod.PUT)
    public MESBOPOperationResource updateBopOperationResource(@PathVariable("operationId") Integer operationId, @PathVariable("resourceId") Integer resourceId,
                                                              @RequestBody MESBOPOperationResource planResource) {
        return bopService.updateBopOperationResource(resourceId, planResource);
    }

    @RequestMapping(value = "/operations/{operationId}/resources/{resourceId}", method = RequestMethod.DELETE)
    public void deleteBopOperationResource(@PathVariable("operationId") Integer operationId, @PathVariable("resourceId") Integer resourceId) {
        bopService.deleteBopOperationResource(resourceId);
    }

    @RequestMapping(value = "/operations/{operationId}/resources/{resourceId}", method = RequestMethod.GET)
    public BOPOperationResourceDto getBopOperationResource(@PathVariable("operationId") Integer operationId, @PathVariable("resourceId") Integer resourceId) {
        return bopService.getBopOperationResource(resourceId);
    }

    @RequestMapping(value = "/operations/{operationId}/parts/type/{type}", method = RequestMethod.GET)
    public List<BOPOperationPartDto> getBopOperationItemsByType(@PathVariable("operationId") Integer operationId, @PathVariable("type") OperationPartType type) {
        return bopService.getBopOperationItemsByType(operationId, type);
    }

    @RequestMapping(value = "/operations/{operationId}/parts", method = RequestMethod.GET)
    public OperationPartDto getBopOperationItems(@PathVariable("operationId") Integer operationId) {
        return bopService.getBopOperationItems(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/parts/multiple", method = RequestMethod.POST)
    public List<MESBOPOperationPart> createBopOperationItems(@PathVariable("operationId") Integer operationId,
                                                             @RequestBody List<MESBOPOperationPart> planItems) {
        return bopService.createBopOperationItems(operationId, planItems);
    }

    @RequestMapping(value = "/operations/{operationId}/parts", method = RequestMethod.POST)
    public MESBOPOperationPart createBopOperationItem(@PathVariable("operationId") Integer operationId,
                                                      @RequestBody MESBOPOperationPart planItem) {
        return bopService.createBopOperationItem(operationId, planItem);
    }

    @RequestMapping(value = "/operations/{operationId}/parts/{planItemId}", method = RequestMethod.PUT)
    public MESBOPOperationPart updateBopOperationItem(@PathVariable("operationId") Integer operationId, @PathVariable("planItemId") Integer planItemId,
                                                      @RequestBody MESBOPOperationPart planItem) {
        return bopService.updateBopOperationItem(planItemId, planItem);
    }

    @RequestMapping(value = "/operations/{operationId}/parts/{planItemId}", method = RequestMethod.DELETE)
    public void deleteBopOperationItem(@PathVariable("operationId") Integer operationId, @PathVariable("planItemId") Integer planItemId) {
        bopService.deleteBopOperationItem(operationId, planItemId);
    }

    @RequestMapping(value = "/operations/{operationId}/parts/{planItemId}", method = RequestMethod.GET)
    public BOPOperationPartDto getBopOperationItem(@PathVariable("operationId") Integer operationId, @PathVariable("planItemId") Integer planItemId) {
        return bopService.getBopOperationItem(planItemId);
    }

    @RequestMapping(value = "/routes/{routeId}/{operationId}/mboms/{mbomId}/parts", method = RequestMethod.GET)
    public List<MBOMItemDto> getBOPMBOMItems(@PathVariable("routeId") Integer routeId, @PathVariable("operationId") Integer operationId, @PathVariable("mbomId") Integer mbomId,
                                             @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = false;
        }
        return bopService.getBOPMBOMItems(routeId, operationId, mbomId, hierarchy);
    }

    @RequestMapping(value = "/routes/{routeId}/{operationId}/mboms/{mbomId}/parts/type/{type}", method = RequestMethod.GET)
    public List<MBOMItemDto> getBOPMBOMItemsByType(@PathVariable("routeId") Integer routeId, @PathVariable("operationId") Integer operationId, @PathVariable("mbomId") Integer mbomId,
                                                   @PathVariable("type") OperationPartType type, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = false;
        }
        return bopService.getBOPMBOMItemsByType(routeId, operationId, mbomId, type, hierarchy);
    }

    @RequestMapping(value = "/operations/{operationId}/instructions", method = RequestMethod.POST)
    public MESBOPOperationInstructions createBopOperationInstructions(@PathVariable("operationId") Integer operationId,
                                                                      @RequestBody MESBOPOperationInstructions planInstructions) {
        return bopService.createBopOperationInstructions(operationId, planInstructions);
    }

    @RequestMapping(value = "/operations/{operationId}/instructions/{instructionId}", method = RequestMethod.PUT)
    public MESBOPOperationInstructions updateBopOperationInstructions(@PathVariable("operationId") Integer operationId, @PathVariable("instructionId") Integer instructionId,
                                                                      @RequestBody MESBOPOperationInstructions planInstructions) {
        return bopService.updateBopOperationInstructions(instructionId, planInstructions);
    }

    @RequestMapping(value = "/operations/{operationId}/instructions/{instructionId}", method = RequestMethod.DELETE)
    public void deleteBopOperationInstructions(@PathVariable("operationId") Integer operationId, @PathVariable("instructionId") Integer instructionId) {
        bopService.deleteBopOperationInstructions(instructionId);
    }

    @RequestMapping(value = "/operations/{operationId}/instructions", method = RequestMethod.GET)
    public MESBOPOperationInstructions getBopOperationInstructions(@PathVariable("operationId") Integer operationId) {
        return bopService.getBopOperationInstructions(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/resources/search", method = RequestMethod.GET)
    public MESObjectDto searchBopOperationResources(@PathVariable("operationId") Integer operationId, PageRequest pageRequest, MESObjectCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bopService.searchBopOperationResources(operationId, pageable, criteria);
    }

    @RequestMapping(value = "/{id}/routes/validate", method = RequestMethod.GET)
    public List<BOPRouteDto> getBOPPlanValidate(@PathVariable("id") Integer id) {
        return bopService.getBOPPlanValidate(id);
    }

    @RequestMapping(value = "/revisions/{id}/revise", method = RequestMethod.PUT)
    public MESBOPRevision reviseBOPRevision(@PathVariable Integer id, @RequestBody MESBOPRevision mesbopRevision) {
        return bopService.reviseBOPRevision(mesbopRevision, null, null);
    }
}

