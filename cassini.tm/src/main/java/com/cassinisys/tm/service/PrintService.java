package com.cassinisys.tm.service;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.service.config.ConfigService;
import com.cassinisys.tm.model.TMProjectTask;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 10/1/15.
 */
@Service
public class PrintService {

    @Autowired
    private ConfigService configService;

    @Autowired
    private TaskService taskService;

    @AutowiredLogger
    private Logger LOGGER;

    public String printTask(Integer assignedTo, Date assignedDate) {
        String output = "";
        try {
            TemplateEngine engine = new SimpleTemplateEngine();
            File tplFile = configService.getCurrentTenantConfig().getTasksTemplateFile();
            Template template = engine.createTemplate(new InputStreamReader(FileUtils.openInputStream(tplFile)));

           List<TMProjectTask> tmProjectTask = taskService.getTasksByPersonIdAndDate(assignedTo,assignedDate);
            Map<String, Object> map = new HashMap<>();
            map.put("person", assignedTo);
            map.put("date",  assignedDate);
            map.put("tasks", tmProjectTask);
            output = template.make(map).toString();
        } catch (Exception e) {
            LOGGER.error("Error printing task details", e);
        }

        return output;
    }
}
