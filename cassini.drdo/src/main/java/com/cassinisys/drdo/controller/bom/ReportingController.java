package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.model.dto.MissileSummaryDTO;
import com.cassinisys.drdo.model.dto.SystemSummaryDTO;
import com.cassinisys.drdo.service.ReportingService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("drdo/reporting")
public class ReportingController extends BaseController {
    @Autowired
    private ReportingService reportingService;

    @RequestMapping ("/summary/systems/{id}")
    public SystemSummaryDTO getSystemSummary(@PathVariable Integer id) {
        return reportingService.getSystemSummary(id);
    }

    @RequestMapping ("/summary/missiles/{id}")
    public MissileSummaryDTO getMissileSummary(@PathVariable Integer id) {
        return reportingService.getMissileSummary(id);
    }
}
