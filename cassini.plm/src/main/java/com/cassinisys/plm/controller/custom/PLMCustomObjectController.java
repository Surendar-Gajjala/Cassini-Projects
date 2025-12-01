package com.cassinisys.plm.controller.custom;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.service.custom.PLMCustomObjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by GSR on 20-07-2021.
 */
@RestController
@RequestMapping("/plm/customobjects")
@Api(tags = "PLM.MES", description = "MES Related")
public class PLMCustomObjectController extends BaseController {

    @Autowired
    private PLMCustomObjectService customObjectService;


    @RequestMapping(value = "/type/{typeId}/assignedtype/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getCustomObjectWorkflows(@PathVariable("typeId") Integer typeId,
                                                                @PathVariable("type") String type) {
        return customObjectService.getCustomObjectHierarchyWorkflows(typeId, type);
    }

    @RequestMapping(value = "/{id}/attachworkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return customObjectService.attachCustomObjectWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getCustomObjectTabCounts(@PathVariable("id") Integer id) {
        return customObjectService.getCustomObjectTabCounts(id);
    }

    @RequestMapping(value = "/type/{id}/attributes", method = RequestMethod.GET)
    public List<CustomObjectTypeAttribute> getAttributes(@PathVariable Integer id,
                                                         @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = Boolean.FALSE;
        }
        return customObjectService.getAttributes(id, hierarchy);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CustomObject getCustomObjectWorkflowStatus(@PathVariable("id") Integer id) {
        return customObjectService.getCustomObjectWorkflowStatus(id);
    }

}
