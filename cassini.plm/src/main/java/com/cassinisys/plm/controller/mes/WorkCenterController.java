package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.WorkCenterCriteria;
import com.cassinisys.plm.model.mes.MESOperation;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.mes.MESWorkCenterOperation;
import com.cassinisys.plm.service.mes.WorkCenterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by subramanyam on 27-10-2020.
 */
@RestController
@RequestMapping("/mes/workcenters")
@Api(tags = "PLM.MES", description = "MES Related")
public class WorkCenterController extends BaseController {

    @Autowired
    private WorkCenterService workCenterService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESWorkCenter create(@RequestBody MESWorkCenter workCenter) {
        return workCenterService.create(workCenter);
    }

    @RequestMapping(value = "/{workCenterId}", method = RequestMethod.PUT)
    public MESWorkCenter update(@PathVariable("workCenterId") Integer workCenterId, @RequestBody MESWorkCenter workCenter) {
        return workCenterService.update(workCenter);
    }

    @RequestMapping(value = "/{workCenterId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("workCenterId") Integer workCenterId) {
        workCenterService.delete(workCenterId);
    }

    @RequestMapping(value = "/{workCenterId}", method = RequestMethod.GET)
    public MESWorkCenter get(@PathVariable("workCenterId") Integer workCenterId) {
        return workCenterService.get(workCenterId);
    }

    @RequestMapping(value = "/multiple/[{workCenterIds}]", method = RequestMethod.GET)
    public List<MESWorkCenter> getMultiple(@PathVariable Integer[] workCenterIds) {
        return workCenterService.findMultiple(Arrays.asList(workCenterIds));
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<MESWorkCenter> getAll() {
        return workCenterService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<MESWorkCenter> getPageableWorkCenters(PageRequest pageRequest, WorkCenterCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workCenterService.getPageableWorkCenters(pageable, criteria);
    }

    @RequestMapping(value = "/type/{typeId}", method = RequestMethod.GET)
    public Page<MESWorkCenter> getWorkCentersByType(@PathVariable("typeId") Integer id,
                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return workCenterService.getWorkCentersByType(id, pageable);
    }

    @RequestMapping(value = "/multiple/{assemblyLineId}", method = RequestMethod.PUT)
    public List<MESWorkCenter> updateMultipleAssemblyLineWorkCenters(@PathVariable("assemblyLineId") Integer assemblyLineId,
                                                                     @RequestBody List<MESWorkCenter> workCenters) {
        return workCenterService.updateMultipleAssemblyLineWorkCenters(assemblyLineId, workCenters);
    }

    @RequestMapping(value = "/{workCenterId}/operations/multiple", method = RequestMethod.POST)
    public List<MESWorkCenterOperation> createWorkCenterOperations(@PathVariable("workCenterId") Integer workCenterId,
                                                                     @RequestBody List<MESOperation> operations) {
        return workCenterService.createWorkCenterOperations(workCenterId, operations);
    }

    @RequestMapping(value = "/{workCenterId}/operations", method = RequestMethod.GET)
    public List<MESWorkCenterOperation> getAllWorkCenterOperations(@PathVariable("workCenterId") Integer workCenterId) {
        return workCenterService.getAllWorkCenterOperations(workCenterId);
    }

    @RequestMapping(value = "/operations/{id}", method = RequestMethod.DELETE)
    public void deleteWorkCenterOperations(@PathVariable("id") Integer id) {
         workCenterService.deleteWorkCenterOperations(id);
    }
}
