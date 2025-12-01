package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ProjectTemplateCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.pm.ProjectTemplate;
import com.cassinisys.plm.model.pm.ProjectTemplateMember;
import com.cassinisys.plm.model.pm.ProjectTemplateWbs;
import com.cassinisys.plm.model.pm.WBSDto;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.pm.ProgramTemplateService;
import com.cassinisys.plm.service.pm.ProjectTemplateService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@RestController
@RequestMapping("/plm/templates")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class ProjectTemplateController extends BaseController {

    @Autowired
    private ProjectTemplateService projectTemplateService;
    @Autowired
    private ProgramTemplateService programTemplateService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ProjectTemplate create(@RequestBody ProjectTemplate projectTemplate) {
        return projectTemplateService.create(projectTemplate);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.PUT)
    public ProjectTemplate update(@PathVariable("templateId") Integer templateId, @RequestBody ProjectTemplate projectTemplate) {
        return projectTemplateService.update(projectTemplate);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("templateId") Integer templateId) {
        projectTemplateService.delete(templateId);
    }

    @RequestMapping(value = "/{templateId}", method = RequestMethod.GET)
    public ProjectTemplate getProjectTemplate(@PathVariable("templateId") Integer templateId) {
        return projectTemplateService.get(templateId);
    }

    @RequestMapping(value = "/byName", method = RequestMethod.GET)
    public ProjectTemplate getProjectTemplateByName(@RequestParam("templateName") String templateName) {
        return projectTemplateService.getProjectTemplateByName(templateName);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ProjectTemplate> getAll() {
        return projectTemplateService.getAll();
    }

    @RequestMapping(value = "/programNull", method = RequestMethod.GET)
    public List<ProjectTemplate> getAllTemplatesProgramNull() {
        return projectTemplateService.getAllTemplatesProgramNull();
    }

    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public Page<ProjectTemplate> getAllProjectTemplates(PageRequest pageRequest, ProjectTemplateCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTemplateService.getProjectTemplates(pageable, criteria);
    }

    @RequestMapping(value = "/{templateId}/wbs", method = RequestMethod.GET)
    public List<WBSDto> getProjectTemplateWbs(@PathVariable("templateId") Integer templateId) {
        return projectTemplateService.getProjectTemplateWbs(templateId);
    }

    @RequestMapping(value = "{templateId}/wbs/children/{wbsId}", method = RequestMethod.GET)
    public ProjectTemplateWbs getTemplateWbsChildren(@PathVariable("templateId") Integer templateId, @PathVariable("wbsId") Integer wbsId) {
        return projectTemplateService.getTemplateWbsChildren(templateId, wbsId);
    }

    @RequestMapping(value = "/{templateId}/members", method = RequestMethod.GET)
    public Page<ProjectTemplateMember> getProjectTemplateMembers(@PathVariable("templateId") Integer templateId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTemplateService.getProjectTemplateMembers(templateId, pageable);
    }

    @RequestMapping(value = "/{templateId}/members/{memberId}", method = RequestMethod.DELETE)
    public void deleteProjectTemplateMember(@PathVariable("templateId") Integer templateId, @PathVariable("memberId") Integer memberId) {
        projectTemplateService.deleteProjectTemplateMember(templateId, memberId);
    }

    @RequestMapping(value = "/{templateId}/team/multiple", method = RequestMethod.POST)
    public List<ProjectTemplateMember> createProjectTemplateMembers(@PathVariable("templateId") Integer templateId,
                                                                    @RequestBody List<ProjectTemplateMember> persons) {
        return projectTemplateService.createProjectTemplateMembers(templateId, persons);
    }

    @RequestMapping(value = "/{templateId}/team", method = RequestMethod.POST)
    public ProjectTemplateMember createProjectTemplateMember(@PathVariable("templateId") Integer templateId,
                                                             @RequestBody ProjectTemplateMember templateMember) {
        return projectTemplateService.createProjectTemplateMember(templateId, templateMember);
    }

    @RequestMapping(value = "/{templateId}/team/{id}", method = RequestMethod.PUT)
    public ProjectTemplateMember updateProjectTemplateMember(@PathVariable("templateId") Integer templateId, @PathVariable("id") Integer id,
                                                             @RequestBody ProjectTemplateMember templateMember) {
        return projectTemplateService.updateProjectTemplateMember(templateId, templateMember);
    }

    @RequestMapping(value = "/team/members/{templateId}", method = RequestMethod.GET)
    public List<Person> getAllProjectTemplateMembers(@PathVariable("templateId") Integer templateId) {
        return projectTemplateService.getAllProjectTemplateMembers(templateId);
    }

    @RequestMapping(value = "/{templateId}/details", method = RequestMethod.GET)
    public DetailsCount getProjectTemplateDetails(@PathVariable("templateId") Integer templateId) {
        return projectTemplateService.getProjectTemplateDetails(templateId);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return programTemplateService.attachWorkflow(id, null, "PROJECTTEMPLATE", wfDefId);
    }
}
