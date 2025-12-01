package com.cassinisys.plm.controller.integration;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.integration.reqif.model.ReqIFMapping;
import com.cassinisys.plm.service.integration.ReqIFService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration/reqif")
@Api(tags = "PLM.INTEGRATION",description = "Integration Related")
public class ReqIFController extends BaseController {
    @Autowired
    private ReqIFService reqIFService;

    @RequestMapping(value = "/mapping", method = RequestMethod.GET)
    public ReqIFMapping getReqIFMapping() {
        return reqIFService.getReqIFMapping();
    }
}
