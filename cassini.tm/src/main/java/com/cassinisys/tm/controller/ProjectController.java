package com.cassinisys.tm.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.tm.filtering.ProjectCriteria;
import com.cassinisys.tm.filtering.ProjectTaskCriteria;
import com.cassinisys.tm.model.TMProject;
import com.cassinisys.tm.model.TMProjectTask;
import com.cassinisys.tm.service.ProjectService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by CassiniSystems on 07-07-2016.
 */
@Api(name = "Project",
        description = "Project endpoint")
@RestController
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST)
    public TMProject create(@RequestBody TMProject project) {

        return projectService.create(project);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.PUT)
    public TMProject update(@PathVariable("projectId") Integer projectId,
                            @RequestBody TMProject project) {
        project.setId(projectId);
        return projectService.update(project);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId) {
        projectService.delete(projectId);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public TMProject get(@PathVariable("projectId") Integer projectId) {
        return projectService.get(projectId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<TMProject> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.findAll(pageable);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<TMProject> freeTextSearch(ProjectCriteria criteria,
                                              PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<TMProject> getMultiple(@PathVariable Integer[] ids) {
        return projectService.findMultipleProjects(Arrays.asList(ids));
    }
}
