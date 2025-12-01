package com.cassinisys.plm.controller.mro;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.MaintenancePlanCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mro.MROMaintenanceOperation;
import com.cassinisys.plm.model.mro.MROMaintenancePlan;
import com.cassinisys.plm.model.mro.dto.MaintenancePlanDto;
import com.cassinisys.plm.service.mro.MaintenancePlanService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@RestController
@RequestMapping("/mro/maintenanceplans")
@Api(tags = "PLM.MRO", description = "MRO Related")
public class MaintenancePlanController extends BaseController {
    @Autowired
    private MaintenancePlanService maintenancePlanService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MROMaintenancePlan create(@RequestBody MROMaintenancePlan maintenancePlan) {
        return maintenancePlanService.create(maintenancePlan);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MROMaintenancePlan update(@PathVariable("id") Integer id,
                                     @RequestBody MROMaintenancePlan maintenancePlan) {
        return maintenancePlanService.update(maintenancePlan);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        maintenancePlanService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MROMaintenancePlan get(@PathVariable("id") Integer id) {
        return maintenancePlanService.get(id);
    }

    @RequestMapping(value = "/asset/{assetId}", method = RequestMethod.GET)
    public List<MROMaintenancePlan> getMaintenancePlansByAsset(@PathVariable("assetId") Integer assetId) {
        return maintenancePlanService.getMaintenancePlansByAsset(assetId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MROMaintenancePlan> getAll() {
        return maintenancePlanService.getAll();
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MaintenancePlanDto> getAllMaintenancePlans(PageRequest pageRequest, MaintenancePlanCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return maintenancePlanService.getAllMaintenancePlans(pageable, criteria);
    }

    @RequestMapping(value = "/{id}/count", method = RequestMethod.GET)
    public ItemDetailsDto getTabCounts(@PathVariable("id") Integer id) {
        return maintenancePlanService.getTabCounts(id);
    }

    @RequestMapping(value = "/{planId}/operations", method = RequestMethod.GET)
    public List<MROMaintenanceOperation> getMaintenancePlanOperations(@PathVariable("planId") Integer planId) {
        return maintenancePlanService.getMaintenancePlanOperations(planId);
    }

    @RequestMapping(value = "/{planId}/operations", method = RequestMethod.POST)
    public MROMaintenanceOperation createMaintenancePlanOperation(@PathVariable("planId") Integer planId,
                                                                  @RequestBody MROMaintenanceOperation maintenanceOperation) {
        return maintenancePlanService.createMaintenancePlanOperation(maintenanceOperation);
    }

    @RequestMapping(value = "/{planId}/operations/multiple", method = RequestMethod.POST)
    public List<MROMaintenanceOperation> createMultipleMaintenancePlanOperations(@PathVariable("planId") Integer planId,
                                                                                 @RequestBody List<MROMaintenanceOperation> maintenanceOperations) {
        return maintenancePlanService.createMultipleMaintenancePlanOperations(planId, maintenanceOperations);
    }

    @RequestMapping(value = "/{planId}/operations/{partId}", method = RequestMethod.PUT)
    public MROMaintenanceOperation updateMaintenancePlanOperation(@PathVariable("planId") Integer planId, @PathVariable("partId") Integer partId,
                                                                  @RequestBody MROMaintenanceOperation workOrderPart) {
        return maintenancePlanService.updateMaintenancePlanOperation(workOrderPart);
    }

    @RequestMapping(value = "/{planId}/operations/{partId}", method = RequestMethod.DELETE)
    public void deleteMaintenancePlanSparePart(@PathVariable("planId") Integer planId, @PathVariable("partId") Integer partId) {
        maintenancePlanService.deleteMaintenancePlanOperation(partId);
    }

}