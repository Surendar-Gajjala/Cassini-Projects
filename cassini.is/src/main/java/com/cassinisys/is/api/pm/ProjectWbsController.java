package com.cassinisys.is.api.pm;
/**
 * The Class is for ProjectWbsController
 **/

import com.cassinisys.is.model.pm.GanttLinkDTO;
import com.cassinisys.is.model.pm.ISLinks;
import com.cassinisys.is.model.pm.ISProjectWbs;
import com.cassinisys.is.model.tm.ISTask;
import com.cassinisys.is.service.pm.ProjectWbsService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Api(name = "Project Wbs",
        description = "Project Wbs endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/wbs")
public class ProjectWbsController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectWbsService projectWbsService;

    /**
     * The method used for creating the ISProjectWbs
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectWbs create(@PathVariable("projectId") Integer projectId,
                               @RequestBody ISProjectWbs projectWbs) {
        return projectWbsService.create(projectWbs);
    }

    @RequestMapping(value = "createGantt", method = RequestMethod.POST)
    public  List<GanttLinkDTO> createGanttChart(@PathVariable("projectId") Integer projectId,
                                 @RequestBody String gantt) throws IOException {
        return projectWbsService.createGanttChart(projectId, gantt);
    }

    @RequestMapping(value = "createLinks", method = RequestMethod.POST)
    public void createLinks(@PathVariable("projectId") Integer projectId,
                            @RequestBody String links) throws IOException {
        projectWbsService.createLinks(projectId, links);
    }

    @RequestMapping(value = "getLinks", method = RequestMethod.GET)
    public ISLinks getLinks(@PathVariable("projectId") Integer projectId) {
        return projectWbsService.getLinks(projectId);
    }

    /**
     * The method used for updating the ISProjectwbs
     **/
    @RequestMapping(value = "/{wbsId}", method = RequestMethod.PUT)
    public ISProjectWbs update(@PathVariable("projectId") Integer projectId,
                               @PathVariable("wbsId") Integer wbsId,
                               @RequestBody ISProjectWbs projectWbs) {
        projectWbs.setId(wbsId);
        return projectWbsService.update(projectWbs);
    }

    /**
     * The method used for deleting the ISProjectwbs
     **/
    @RequestMapping(value = "/{wbsId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("wbsId") Integer wbsId) {
        projectWbsService.delete(wbsId);
    }

    /**
     * The method used get the value of  ISProjectwbs
     **/
    @RequestMapping(value = "/{wbsId}", method = RequestMethod.GET)
    public ISProjectWbs get(@PathVariable("projectId") Integer projectId,
                            @PathVariable("wbsId") Integer wbsId) {
        return projectWbsService.get(wbsId);
    }

    @RequestMapping(value = "/tasksByWbs/{wbsId}", method = RequestMethod.GET)
    public List<ISTask> getTasksByWbsItem(@PathVariable("projectId") Integer projectId,
                                          @PathVariable("wbsId") Integer wbsId) {
        return projectWbsService.getAllTasksByWbsItem(wbsId);
    }

    /**
     * The method used getChildren of  ISProjectwbs
     **/
    @RequestMapping(value = "/{wbsId}/children", method = RequestMethod.GET)
    public List<ISProjectWbs> getChildren(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("wbsId") Integer wbsId) {
        return projectWbsService.getChildren(wbsId);
    }

    /**
     * The method used to getmultiples of  ISProjectwbs
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectWbs> getMultiple(@PathVariable Integer[] ids) {
        return projectWbsService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/parent/{parentId}/wbsName", method = RequestMethod.GET)
    public ISProjectWbs getChildWbsName(@PathVariable("projectId") Integer projectId, @PathVariable("parentId") Integer parentId,
                                        @RequestParam("wbsName") String wbsName) {
        return projectWbsService.getProjectWbsName(projectId, wbsName, parentId);
    }

    @RequestMapping(value = "/parent/{parentName}", method = RequestMethod.GET)
    public ISProjectWbs getParentWbsByName(@PathVariable("projectId") Integer projectId, @PathVariable("parentName") String parentName) {
        return projectWbsService.getParentWbsByName(projectId, parentName);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ISProjectWbs> getAllWbs(@PathVariable("projectId") Integer projectId) {
        List<ISProjectWbs> isProjectWbsList = projectWbsService.getAllWbs(projectId);
        isProjectWbsList.forEach(wbs -> wbs.setCassiniId(wbs.getId()));
        return isProjectWbsList;
    }

    @RequestMapping(value = "/finish/{wbs}", method = RequestMethod.POST)
    public List<ISProjectWbs> finishWbs(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("wbs") Integer wbs) {
        return projectWbsService.finishWbs(wbs);
    }

}
