package com.cassinisys.erp.api.reporting;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.scripting.ScriptingEngine;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by reddy on 9/15/15.
 */
@RestController
@RequestMapping("/reporting/editor")
public class ReportEditor extends BaseController {

    @Autowired
    private ScriptingEngine scriptingEngine;

    @RequestMapping (method = RequestMethod.POST)
    public Object executeScript(HttpServletRequest request) {
        try {
            String script = IOUtils.toString(request.getInputStream());
            return scriptingEngine.executeScript(new ResourceScriptSource(
                    new ByteArrayResource(script.getBytes())));
        } catch (IOException e) {
            throw new RuntimeException("Error executing script", e);
        }
    }
}
