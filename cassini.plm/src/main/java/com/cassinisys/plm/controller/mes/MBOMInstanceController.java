package com.cassinisys.plm.controller.mes;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.MESBOPInstanceOperationInstructions;
import com.cassinisys.plm.model.mes.MESMBOMInstance;
import com.cassinisys.plm.model.mes.dto.BOPInstanceOperationPartDto;
import com.cassinisys.plm.model.mes.dto.BOPInstanceRouteDto;
import com.cassinisys.plm.model.mes.dto.BOPRouteDto;
import com.cassinisys.plm.model.mes.dto.InstanceOperationPartDto;
import com.cassinisys.plm.model.mes.dto.MBOMInstanceItemDto;
import com.cassinisys.plm.model.mes.dto.ResourceDto;
import com.cassinisys.plm.service.mes.MBOMInstanceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mes/mbominstances")
@Api(tags = "PLM.MES", description = "MES Related")
public class MBOMInstanceController extends BaseController {

    @Autowired
    private MBOMInstanceService mbomInstanceService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public MESMBOMInstance create(@RequestBody MESMBOMInstance mesmbomInstance) {
        return mbomInstanceService.create(mesmbomInstance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MESMBOMInstance update(@PathVariable("id") Integer id,
                                  @RequestBody MESMBOMInstance mesmbomInstance) {
        return mbomInstanceService.update(mesmbomInstance);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mbomInstanceService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MESMBOMInstance get(@PathVariable("id") Integer id) {
        return mbomInstanceService.get(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getMBOMInstanceCounts(@PathVariable("id") Integer id) {
        return mbomInstanceService.getMBOMInstanceCounts(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MESMBOMInstance> getAll() {
        return mbomInstanceService.getAll();
    }

    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    public List<MBOMInstanceItemDto> getMBOMInstanceItems(@PathVariable Integer id, @RequestParam("hierarchy") Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = false;
        }
        return mbomInstanceService.getMBOMInstanceItems(id, hierarchy);
    }

    @RequestMapping(value = "/{id}/items/{itemId}/children", method = RequestMethod.GET)
    public List<MBOMInstanceItemDto> getMBOMInstanceItemChildren(@PathVariable("id") Integer id, @PathVariable("itemId") Integer itemId) {
        return mbomInstanceService.getMBOMInstanceItemChildren(id, itemId);
    }

    @RequestMapping(value = "/{id}/operations", method = RequestMethod.GET)
    public List<BOPInstanceRouteDto> getMBOMInstanceOperations(@PathVariable("id") Integer id) {
        return mbomInstanceService.getMBOMInstanceOperations(id);
    }

    @RequestMapping(value = "/operations/{operationId}", method = RequestMethod.GET)
    public BOPInstanceRouteDto getBopRouteItem(@PathVariable("operationId") Integer operationId) {
        return mbomInstanceService.getMbomInstanceOperationItem(operationId);
    }
    @RequestMapping(value = "/operations/{operationId}/parts", method = RequestMethod.GET)
    public InstanceOperationPartDto getBopInstanceOperationItems(@PathVariable("operationId") Integer operationId) {
        return mbomInstanceService.getBopInstanceOperationItems(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/resources", method = RequestMethod.GET)
    public List<ResourceDto> getBopInstanceOperationResources(@PathVariable("operationId") Integer operationId) {
        return mbomInstanceService.getBopInstanceOperationResources(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/instructions", method = RequestMethod.GET)
    public MESBOPInstanceOperationInstructions getBopOperationInstructions(@PathVariable("operationId") Integer operationId) {
        return mbomInstanceService.getBopInstanceOperationInstructions(operationId);
    }

    @RequestMapping(value = "/operations/{operationId}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getBOPInstanceOperationCounts(@PathVariable("operationId") Integer operationId) {
        return mbomInstanceService.getBOPInstanceOperationCounts(operationId);
    }

}

