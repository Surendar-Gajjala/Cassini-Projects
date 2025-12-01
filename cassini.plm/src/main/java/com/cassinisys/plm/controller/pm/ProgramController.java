package com.cassinisys.plm.controller.pm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ProjectCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.pm.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by smukka on 02-06-2022.
 */
@RestController
@RequestMapping("/plm/programs")
public class ProgramController extends BaseController {

    @Autowired
    private ProgramService programService;
    @Autowired
    private PageRequestConverter pageRequestConverter;


    @RequestMapping(method = RequestMethod.POST)
    public PLMProgram create(@RequestBody PLMProgram plmProject) {
        return programService.create(plmProject);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMProgram update(@PathVariable("id") Integer id, @RequestBody PLMProgram plmProject) {
        return programService.update(plmProject);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        programService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMProgram get(@PathVariable("id") Integer id) {
        return programService.get(id);
    }

    @RequestMapping(value = "/{id}/counts", method = RequestMethod.GET)
    public ItemDetailsDto getProgramCounts(@PathVariable("id") Integer id) {
        return programService.getProgramCounts(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMProgram> getAll() {
        return programService.getAll();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<ProgramDto> getAllPrograms(PageRequest pageRequest, ProjectCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return programService.getAllPrograms(pageable, criteria);
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    public List<ProgramProjectDto> getProgramProjects(@PathVariable("id") Integer id) {
        return programService.getProgramProjects(id);
    }

    @RequestMapping(value = "/{id}/projects/folders", method = RequestMethod.GET)
    public List<ProgramProjectFolderDto> getProgramProjectFolders(@PathVariable("id") Integer id) {
        return programService.getProgramProjectFolders(id);
    }

    @RequestMapping(value = "/{id}/projects/drilldown", method = RequestMethod.GET)
    public List<DrillDownProjectDto> getProgramProjectsDrillDown(@PathVariable("id") Integer id) {
        return programService.getProgramProjectsDrillDown(id);
    }

    @RequestMapping(value = "/{id}/projects/{projectId}/plan", method = RequestMethod.GET)
    public List<DrillDownProjectDto> getProgramProjectStructure(@PathVariable("id") Integer id, @PathVariable("projectId") Integer projectId) {
        return programService.getProgramProjectStructure(id, projectId);
    }

    @RequestMapping(value = "/{id}/projects/plan/{assignedTo}/all", method = RequestMethod.GET)
    public List<DrillDownProjectDto> getProgramProjectsStructureByAssignedTo(@PathVariable("id") Integer id, @PathVariable("assignedTo") Integer assignedTo) {
        return programService.getProgramProjectsStructureByAssignedTo(id, assignedTo);
    }

    @RequestMapping(value = "/programManagers", method = RequestMethod.GET)
    public List<Person> getProgramManagers() {
        return programService.getProgramManagers();
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.POST)
    public ProgramProjectDto createProgramProject(@PathVariable("id") Integer id, @RequestBody PLMProgramProject programProject) {
        return programService.createProgramProject(id, programProject);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.PUT)
    public ProgramProjectDto updateProgramProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId,
                                                  @RequestBody PLMProgramProject programProject) {
        return programService.updateProgramProject(programProjectId, programProject);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.DELETE)
    public void deleteProgramProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId) {
        programService.deleteProgramProject(id, programProjectId);
    }

    @RequestMapping(value = "/{id}/projects/{programProjectId}", method = RequestMethod.GET)
    public ProgramProjectDto getProgramProject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId) {
        return programService.getProgramProject(id, programProjectId);
    }


    @RequestMapping(value = "/{id}/projects/object", method = RequestMethod.POST)
    public DrillDownProjectDto createProgramProjectObject(@PathVariable("id") Integer id, @RequestBody DrillDownProjectDto drillDownProjectDto) {
        return programService.createProgramProjectObject(id, drillDownProjectDto);
    }

    @RequestMapping(value = "/{id}/projects/object/{programProjectId}", method = RequestMethod.PUT)
    public DrillDownProjectDto updateProgramProjectObject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId,
                                                          @RequestBody DrillDownProjectDto drillDownProjectDto) {
        return programService.updateProgramProjectObject(programProjectId, drillDownProjectDto);
    }

    @RequestMapping(value = "/{id}/projects/object/{programProjectId}", method = RequestMethod.DELETE)
    public void deleteProgramProjectObject(@PathVariable("id") Integer id, @PathVariable("programProjectId") Integer programProjectId) {
        programService.deleteProgramProjectObject(id, programProjectId);
    }


    @RequestMapping(value = "/{id}/resources/multiple", method = RequestMethod.POST)
    public List<PLMProgramResource> createProgramResources(@PathVariable("id") Integer id, @RequestBody List<PLMProgramResource> programResources) {
        return programService.createProgramResources(programResources);
    }

    @RequestMapping(value = "/{id}/resources", method = RequestMethod.POST)
    public PLMProgramResource createProgramResource(@PathVariable("id") Integer id, @RequestBody PLMProgramResource programResource) {
        return programService.createProgramResource(programResource);
    }

    @RequestMapping(value = "/{id}/resources/{resourceId}", method = RequestMethod.PUT)
    public PLMProgramResource updateProgramResource(@PathVariable("id") Integer id, @PathVariable("resourceId") Integer resourceId,
                                                    @RequestBody PLMProgramResource programResource) {
        return programService.updateProgramResource(programResource);
    }

    @RequestMapping(value = "/{id}/resources/{resourceId}", method = RequestMethod.DELETE)
    public void deleteProgramResource(@PathVariable("id") Integer id, @PathVariable("resourceId") Integer resourceId) {
        programService.deleteProgramResource(id, resourceId);
    }

    @RequestMapping(value = "/{id}/resources/{resourceId}", method = RequestMethod.GET)
    public PLMProgramResource getProgramResource(@PathVariable("id") Integer id, @PathVariable("resourceId") Integer resourceId) {
        return programService.getProgramResource(id, resourceId);
    }

    @RequestMapping(value = "/{id}/resources", method = RequestMethod.GET)
    public List<PLMProgramResource> getProgramResources(@PathVariable("id") Integer id) {
        return programService.getProgramResources(id);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}", method = RequestMethod.GET)
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return programService.attachProgramWorkflow(id, wfDefId, null);
    }

    @RequestMapping(value = "/{id}/type/{type}/tasks/notifications", method = RequestMethod.GET)
    public void sentProgramTaskNotifications(@PathVariable("id") Integer id, @PathVariable("type") String type) {
        programService.sentProgramTaskNotifications(id, type);
    }

    @RequestMapping(value = "/{id}/projectManagers", method = RequestMethod.GET)
    public List<Person> getProgramProjectManagers(@PathVariable("id") Integer id) {
        return programService.getProgramProjectManagers(id);
    }

    @RequestMapping(value = "/{id}/projects/assignedTos", method = RequestMethod.GET)
    public List<Person> getProgramProjectAssignedTos(@PathVariable("id") Integer id) {
        return programService.getProgramProjectAssignedTos(id);
    }

}
