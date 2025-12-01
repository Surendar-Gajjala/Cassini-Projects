package com.cassinisys.erp.api.reporting;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.scripting.ScriptingEngine;
import com.cassinisys.erp.scripting.dto.Report;
import com.cassinisys.erp.scripting.dto.ReportGroup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by reddy on 9/14/15.
 */
@RestController
@RequestMapping("reporting")
public class ReportController extends BaseController {
    @Autowired
    private ScriptingEngine scriptingEngine;

    @Value("classpath:reports/reports.json")
    private Resource reportsResource;

    @RequestMapping (method = RequestMethod.GET)
    public List<ReportGroup> getAvailableReports() {
        try {
            return new ObjectMapper().readValue(reportsResource.getInputStream(),
                    new TypeReference<List<ReportGroup>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error loading reports file", e);
        }
    }

    @RequestMapping (value = "/execute", method = RequestMethod.POST)
    public Object executeReport(@RequestParam(value = "dateRange", required = false) String dateRange,
                                @RequestBody Report report) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(report.getReportFile());
        if(dateRange != null && !dateRange.trim().isEmpty()) {
            scriptingEngine.bindVariable("_dateRange", dateRange.trim());
        }
        else {
            scriptingEngine.bindVariable("_dateRange", null);
        }
        return scriptingEngine.executeScript(new ResourceScriptSource(resource));
    }
}
