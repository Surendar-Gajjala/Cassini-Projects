package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.plm.model.pm.ProjectTemplateWbs;
import com.cassinisys.plm.service.pm.ProjectTemplateWbsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam reddy on 15-03-2018.
 */
@RestController
@RequestMapping("/plm/templates/wbs")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class ProjectTemplateWbsController extends BaseController {

    @Autowired
    private ProjectTemplateWbsService projectTemplateWbsService;

    @RequestMapping(method = RequestMethod.POST)
    public ProjectTemplateWbs create(@RequestBody ProjectTemplateWbs projectTemplateWbs) {
        return projectTemplateWbsService.create(projectTemplateWbs);
    }

    @RequestMapping(value = "/{templateId}/wbsList", method = RequestMethod.POST)
    public List<ProjectTemplateWbs> createWBSElements(@PathVariable("templateId") Integer templateId, @RequestBody List<ProjectTemplateWbs> wbsElementList) {
        return projectTemplateWbsService.createWBSElements(wbsElementList);
    }

    @RequestMapping(value = "/{wbsId}", method = RequestMethod.PUT)
    public ProjectTemplateWbs update(@PathVariable("wbsId") Integer wbsId, @RequestBody ProjectTemplateWbs projectTemplateWbs) {
        return projectTemplateWbsService.update(projectTemplateWbs);
    }

    @RequestMapping(value = "/{wbsId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("wbsId") Integer wbsId) {
        projectTemplateWbsService.delete(wbsId);
    }

    @RequestMapping(value = "/{wbsId}", method = RequestMethod.GET)
    public ProjectTemplateWbs getProjectTemplate(@PathVariable("wbsId") Integer wbsId) {
        return projectTemplateWbsService.get(wbsId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProjectTemplateWbs> getAll() {
        return projectTemplateWbsService.getAll();
    }

    @RequestMapping(value = "/{templateId}/byName", method = RequestMethod.GET)
    public ProjectTemplateWbs getTemplateWbsByName(@PathVariable("templateId") Integer templateId, @RequestParam("templateWbsName") String templateWbsName) {
        return projectTemplateWbsService.getTemplateWbsByName(templateId, templateWbsName);
    }
}
