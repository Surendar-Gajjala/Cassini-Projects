package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.cm.QualityWorkflowService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by subramanyam on 16-06-2020.
 */
@RestController
@RequestMapping(value = "pqm/qualities/{id}/workflows")
@Api(tags = "PLM.CM",description = "Changes Related")
public class QualityWorkflowController extends BaseController {

    @Autowired
    private QualityWorkflowService qualityWorkflowService;

    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(value = "/{wfDefId}/attachWorkflow/{objectType}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                      @PathVariable("wfDefId") Integer wfDefId) {
        return qualityWorkflowService.attachQualityWorkflow(id,objectType,wfDefId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("id") Integer id) {
        workflowService.deleteWorkflow(id);
    }

}
