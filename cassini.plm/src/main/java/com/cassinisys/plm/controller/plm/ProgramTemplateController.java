package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ProjectTemplateCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pm.ProgramTemplate;
import com.cassinisys.plm.model.pm.ProgramTemplateProject;
import com.cassinisys.plm.model.pm.ProgramTemplateProjectDto;
import com.cassinisys.plm.model.pm.ProgramTemplateResource;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.pm.ProgramTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by smukka on 18-06-2022.
 */
@RestController
@RequestMapping("/plm/programtemplates")
public class ProgramTemplateController extends BaseController {

    @Autowired
    private ProgramTemplateService programTemplateService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ProgramTemplate create(@RequestBody ProgramTemplate programTemplate) {
        return programTemplateService.create(programTemplate);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.PUT)
    public ProgramTemplate update(@PathVariable("templateId") Integer templateId, @RequestBody ProgramTemplate programTemplate) {
        return programTemplateService.update(programTemplate);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("templateId") Integer templateId) {
        programTemplateService.delete(templateId);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.GET)
    public ProgramTemplate getProjectTemplate(@PathVariable("templateId") Integer templateId) {
        return programTemplateService.get(templateId);
    }

    @RequestMapping(value = "/byName", method = RequestMethod.GET)
    public ProgramTemplate getProgramTemplateByName(@RequestParam("templateName") String templateName) {
        return programTemplateService.getProgramTemplateByName(templateName);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProgramTemplate> getAll() {
        return programTemplateService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ProgramTemplate> getAllProjectTemplates(PageRequest pageRequest, ProjectTemplateCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return programTemplateService.getProgramTemplates(pageable, criteria);
    }

    @RequestMapping(value = "/{templateId}/resources", method = RequestMethod.GET)
    public List<ProgramTemplateResource> getProgramTemplateResources(@PathVariable("templateId") Integer templateId) {
        return programTemplateService.getProgramTemplateResources(templateId);
    }

    @RequestMapping(value = "/{templateId}/resources/{resourceId}", method = RequestMethod.DELETE)
    public void deleteProjectTemplateResource(@PathVariable("templateId") Integer templateId, @PathVariable("resourceId") Integer resourceId) {
        programTemplateService.deleteProjectTemplateResource(templateId, resourceId);
    }

    @RequestMapping(value = "/{templateId}/resources/multiple", method = RequestMethod.POST)
    public List<ProgramTemplateResource> createProjectTemplateResources(@PathVariable("templateId") Integer templateId,
                                                                        @RequestBody List<ProgramTemplateResource> templateResources) {
        return programTemplateService.createProjectTemplateResources(templateId, templateResources);
    }

    @RequestMapping(value = "/{templateId}/resources", method = RequestMethod.POST)
    public ProgramTemplateResource createProjectTemplateResource(@PathVariable("templateId") Integer templateId,
                                                                 @RequestBody ProgramTemplateResource templateResource) {
        return programTemplateService.createProjectTemplateResource(templateId, templateResource);
    }


    @RequestMapping(value = "/{templateId}/resources/{id}", method = RequestMethod.PUT)
    public ProgramTemplateResource updateProjectTemplateResource(@PathVariable("templateId") Integer templateId,
                                                                 @RequestBody ProgramTemplateResource templateResource) {
        return programTemplateService.updateProjectTemplateResource(templateId, templateResource);
    }

    @RequestMapping(value = "/{templateId}/details", method = RequestMethod.GET)
    public ItemDetailsDto getProgramTemplateDetails(@PathVariable("templateId") Integer templateId) {
        return programTemplateService.getProgramTemplateDetails(templateId);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    public List<ProgramTemplateProjectDto> getProgramTemplateProjects(@PathVariable("id") Integer id) {
        return programTemplateService.getProgramTemplateProjects(id);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.POST)
    public ProgramTemplateProjectDto createProgramTemplateProject(@PathVariable("id") Integer id, @RequestBody ProgramTemplateProject programProject) {
        return programTemplateService.createProgramTemplateProject(id, programProject);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.PUT)
    public ProgramTemplateProjectDto updateProgramTemplateProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId,
                                                                  @RequestBody ProgramTemplateProject programProject) {
        return programTemplateService.updateProgramTemplateProject(programProjectId, programProject);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.DELETE)
    public void deleteProgramTemplateProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId) {
        programTemplateService.deleteProgramTemplateProject(id, programProjectId);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.GET)
    public ProgramTemplateProjectDto getProgramTemplateProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId) {
        return programTemplateService.getProgramTemplateProject(id, programProjectId);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return programTemplateService.attachWorkflow(id, null, "PROGRAMTEMPLATE", wfDefId);
    }
}
