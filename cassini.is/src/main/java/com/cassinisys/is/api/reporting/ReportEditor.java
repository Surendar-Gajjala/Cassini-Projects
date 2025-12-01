package com.cassinisys.is.api.reporting;

import com.cassinisys.is.scripting.ScriptingEngine;
import com.cassinisys.platform.api.core.BaseController;
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
 * Created by swapna on 12/18/17.
 */
@RestController
@RequestMapping("/reporting/editor")
public class ReportEditor extends BaseController {

    @Autowired
    private ScriptingEngine scriptingEngine;

    @RequestMapping(method = RequestMethod.POST)
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
