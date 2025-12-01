package com.cassinisys.is.api.pm;
/**
 * The Class is for ProjectController
 **/

import com.cassinisys.is.filtering.ProjectCriteria;
import com.cassinisys.is.filtering.ProjectPersonCriteria;
import com.cassinisys.is.model.col.ISProjectMeeting;
import com.cassinisys.is.model.col.ISProjectMessage;
import com.cassinisys.is.model.dm.DocType;
import com.cassinisys.is.model.dm.ISProjectDocument;
import com.cassinisys.is.model.dm.ISProjectFolder;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.*;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.model.procm.ISProjectBoq;
import com.cassinisys.is.model.procm.ISProjectRfq;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.tm.ISProjectTeam;
import com.cassinisys.is.model.tm.ISTask;
import com.cassinisys.is.model.tm.ProjectTaskDto;
import com.cassinisys.is.service.pm.ProjectService;
import com.cassinisys.is.service.tm.ProjectTaskService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*import com.cassinisys.platform.model.col.MediaType;*/

@Api(name = "Projects", description = "Projects endpoint", group = "Project")
@RestController
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTaskService taskService;
    /**
     * The method used for getResources of ISSiteStore by storeId
     **/

    /**
     * The method used for creating the ISProject
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProject create(@RequestBody ISProject project) {
        return projectService.create(project);
    }

    /**
     * The method used for creating the ISProjectResource
     **/
    @RequestMapping(value = "/{projectId}/resources", method = RequestMethod.POST)
    public List<ISProjectResource> createResource(@PathVariable("projectId") Integer projectId,
                                                  @RequestBody List<ISProjectResource> resources) {
        return projectService.createResources(resources);
    }

    /**
     * The method used for updating the ISProjectResource
     **/
    @RequestMapping(value = "/{projectId}/resources/update", method = RequestMethod.POST)
    public ISProjectResource updateResource(@PathVariable("projectId") Integer projectId,
                                            @RequestBody ISProjectResource resource) {
        return projectService.updateResources(resource);
    }

    /**
     * The method used for getResources of ISProjectResource by projectId, taskId
     **/
    @RequestMapping(value = "/{projectId}/{taskId}/resources", method = RequestMethod.GET)
    public List<ISProjectResource> getResources(@PathVariable("projectId") Integer projectId,
                                                @PathVariable("taskId") Integer taskId) {
        return projectService.getResources(projectId, taskId);
    }

    /**
     * The method used to deleteProjectResources
     **/
    @RequestMapping(value = "/{projectId}/resources/{resourceId}",
            method = RequestMethod.DELETE)
    public void deleteResource(@PathVariable("projectId") Integer projectId,
                               @PathVariable("resourceId") Integer resourceId) {
        projectService.deleteResource(resourceId);
    }

    /**
     * The method used for get multiple of ISProjectResource by projectId, taskId's and resourceType
     **/
    @RequestMapping(value = "/{projectId}/[{taskIds}]/resources/{resourceType}", method = RequestMethod.GET)
    public List<List<ISProjectResource>> getMultipleResources(@PathVariable("projectId") Integer projectId, @PathVariable("resourceType") ResourceType resourceType,
                                                              @PathVariable("taskIds") Integer[] taskIds) {
        return projectService.getMultipleByIds(projectId, Arrays.asList(taskIds), resourceType);
    }

    /**
     * The method used for get multiple of ISProjectResource by projectId, taskId's and resourceType
     **/
    @RequestMapping(value = "/{projectId}/[{taskIds}]/resources", method = RequestMethod.GET)
    public List<ISProjectResource> getResourcesByTasks(@PathVariable("projectId") Integer projectId, @PathVariable("taskIds") Integer[] taskIds) {
        return projectService.getMultipleResourcesByTasks(projectId, Arrays.asList(taskIds));
    }

    @RequestMapping(value = "/resources/project/{projectId}", method = RequestMethod.GET)
    public List<ISProjectResource> getResourcesByProject(@PathVariable("projectId") Integer projectId) {
        return projectService.getResourcesByProject(projectId);
    }

    /**
     * The method used for getResources of ISProjectResource by projectId, taskId and resourceType
     **/
    @RequestMapping(value = "/{projectId}/{taskId}/resources/{resourceType}", method = RequestMethod.GET)
    public List<ISProjectResource> getResourcesByResourceType(@PathVariable("projectId") Integer projectId,
                                                              @PathVariable("taskId") Integer taskId, @PathVariable("resourceType") ResourceType resourceType) {
        return projectService.getResourcesByType(projectId, taskId, resourceType);
    }

    @RequestMapping(value = "/{projectId}/{taskId}/resourcesType/[{resourceType}]", method = RequestMethod.GET)
    public List<ISProjectResource> getProjectResourcesByResourceTypes(@PathVariable("projectId") Integer projectId,
                                                                      @PathVariable("taskId") Integer taskId, @PathVariable("resourceType") List<ResourceType> resourceType) {
        return projectService.getProjectResourcesByTypes(projectId, taskId, resourceType);
    }

    /**
     * The method used for getResources of ISProjectResource by resourceIds
     **/
    @RequestMapping(value = "/{projectId}/[{resourceIds}]/resources/", method = RequestMethod.GET)
    public List<ISProjectResource> getResourcesByIds(@PathVariable("projectId") Integer projectId,
                                                     @PathVariable("resourceIds") Integer resourceIds) {
        return projectService.getResourcesByIds(Arrays.asList(resourceIds));
    }

    /**
     * The method used for getItemAvailableQuantities of ISProjectResource by referenceIds
     **/
    @RequestMapping(value = "/{projectId}/[{referenceIds}]/available", method = RequestMethod.GET)
    public Map<Integer, Double> getItemAvailableQuantities(@PathVariable("projectId") Integer projectId,
                                                           @PathVariable("referenceIds") List<Integer> referenceIds) {
        return projectService.getItemAvailableQuantities(projectId, referenceIds);
    }

    /**
     * The method used for updating the ISProject
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISProject update(@PathVariable("id") Integer id,
                            @RequestBody ISProject project) {
        project.setId(id);
        return projectService.update(project);
    }

    /**
     * The method used for deleting the ISProject
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        projectService.delete(id);
    }

    /**
     * The method used get the value of  the ISProject
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISProject get(@PathVariable("id") Integer id) {
        return projectService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISProject> getAllProjects() {
        return projectService.getAllProjects();
    }

    @RequestMapping(value = "/multiple/{ids}", method = RequestMethod.GET)
    public List<ISProject> getProjectsByIds(@PathVariable("ids") List<Integer> projectIds) {
        return projectService.getProjectsByIds(projectIds);
    }

    /**
     * The method used getall the values of  the ISProject
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISProject> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.findAll(pageable);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ISProject> getUserProjects(@PathVariable("userId") Integer userId) {
        return projectService.getUserProjects(userId);
    }

    @RequestMapping(value = "/findByName/{name}", method = RequestMethod.GET)
    public ISProject findProjectByName(@PathVariable("name") String name) {
        return projectService.findProjectByName(name);
    }

    @RequestMapping(value = "/portfolio/{id}", method = RequestMethod.GET)
    public List<ISProject> findProjectByPortfolio(@PathVariable("id") Integer portfolio) {
        return projectService.findProjectByPortfolio(portfolio);
    }

    /**
     * The method used get multiples of  the ISProject
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProject> getMultiple(@PathVariable Integer[] ids) {
        return projectService.findMultiple(Arrays.asList(ids));
    }

    /**
     * The method used getTasks of  the ISProjectTask
     **/
    @RequestMapping(value = "/{id}/tasks", method = RequestMethod.GET)
    public Page<ISProjectTask> getTasks(@PathVariable("id") Integer id,
                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getTasks(id, pageable);
    }

    /**
     * The method used getWbs for  the ISProjectWbs
     **/
    @RequestMapping(value = "/{id}/wbs", method = RequestMethod.GET)
    public List<ISProjectWbs> getWbs(@PathVariable("id") Integer id) {
        return projectService.getRootWbs(id);
    }

    /**
     * The method used getallWbs for  the ISProjectWbs
     **/
    @RequestMapping(value = "/{id}/wbs/root/all", method = RequestMethod.GET)
    public List<ISProjectWbs> getAllWbs(@PathVariable("id") Integer id) {
        return projectService.getAllWbs(id);
    }

    /**
     * The method used getTasksForWbs for  the ISTask
     **/
    @RequestMapping(value = "/{id}/wbs/{wbsItem}/tasks", method = RequestMethod.GET)
    public Page<ISTask> getTasksForWbs(@PathVariable("id") Integer id,
                                       @PathVariable("wbsItem") Integer wbsItem,
                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return taskService.getAllTasksForWbsItem(wbsItem, pageable);
    }

    /**
     * The method used getfolders for  the ISProjectFolder
     **/
    @RequestMapping(value = "/{id}/folders", method = RequestMethod.GET)
    public List<ISProjectFolder> getFolders(@PathVariable("id") Integer id,
                                            @RequestParam("folderType") DocType folderType) {
        if (folderType == null) {
            folderType = DocType.DEFAULT;
        }
        return projectService.getRootFolders(id, folderType);
    }

    /**
     * The method used getallFolders for  the ISProjectFolder
     **/
    @RequestMapping(value = "/{id}/folders/{folderType}/all", method = RequestMethod.GET)
    public List<ISProjectFolder> getAllFolders(@PathVariable("id") Integer id,
                                               @PathVariable("folderType") DocType folderType) {
        if (folderType == null) {
            folderType = DocType.DEFAULT;
        }
        return projectService.getAllProjectFolders(id, folderType);
    }

    /**
     * The method used getIssues for  the ISStockIssue
     **/
    @RequestMapping(value = "/{id}/issues", method = RequestMethod.GET)
    public Page<ISIssue> getIssues(@PathVariable("id") Integer id,
                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getIssues(id, pageable);
    }

    /**
     * The method used to getMessages for  the ISProjectMessage
     **/
    @RequestMapping(value = "/{id}/messages", method = RequestMethod.GET)
    public Page<ISProjectMessage> getMessages(@PathVariable("id") Integer id,
                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getMessages(id, pageable);
    }

    /**
     * The method used getMeetings for  the ISProjectMeeting
     **/
    @RequestMapping(value = "/{id}/meetings", method = RequestMethod.GET)
    public Page<ISProjectMeeting> getMeetings(@PathVariable("id") Integer id,
                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getMeetings(id, pageable);
    }

    /**
     * The method used getDocuments for  the ISProjectDocument
     **/
    @RequestMapping(value = "/{projectId}/documents/all", method = RequestMethod.GET)
    public List<ISProjectDocument> getDocuments(@PathVariable("projectId") Integer projectId) {
        return projectService.getDocuments(projectId);
    }

    /**
     * The method used getDocument for  the ISProjectDocument
     **/
    @RequestMapping(value = "/{projectId}/documents/{documentId}", method = RequestMethod.GET)
    public ISProjectDocument getDocument(@PathVariable("projectId") Integer projectId,
                                         @PathVariable("documentId") Integer documentId) {
        return projectService.getDocument(documentId);
    }

    /**
     * The method used to getBoqs for  the ISProjectBoq
     **/
    @RequestMapping(value = "/{id}/boq", method = RequestMethod.GET)
    public List<ISProjectBoq> getBoqs(@PathVariable("id") Integer id) {
        return projectService.getBoqs(id);
    }

    /**
     * The method used to getRfqs for  the ISProjectRfq
     **/
    @RequestMapping(value = "/{id}/rfqs", method = RequestMethod.GET)
    public Page<ISProjectRfq> getRfqs(@PathVariable("id") Integer id,
                                      PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getRfqs(id, pageable);
    }

    /**
     * The method used to getTeam for  the ISProjectTeam
     **/
    @RequestMapping(value = "/{id}/team", method = RequestMethod.GET)
    public Page<ISProjectTeam> getTeam(@PathVariable("id") Integer id,
                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getTeam(id, pageable);
    }

    /**
     * The method used to createTeamMember for  the ISProjectPerson
     **/
    @RequestMapping(value = "/{projectId}/team", method = RequestMethod.POST)
    public List<ISProjectPerson> createTeamMember(@PathVariable("projectId") Integer projectId,
                                                  @RequestBody List<ISProjectPerson> projectPersons) {
        return projectService.createProjectPersons(projectPersons);
    }

    /**
     * The method used to getTeamPersons for  the ISProjectPerson
     **/
    @RequestMapping(value = "/{projectId}/teamPersons", method = RequestMethod.GET)
    public List<ISProjectPerson> getTeamPersons(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectPersons(projectId);
    }

    /**
     * The method used to getProjectPagedPersons for ISProjectPerson
     **/
    @RequestMapping(value = "/{projectId}/teamPersons/pageable", method = RequestMethod.GET)
    public Page<ISProjectPerson> getProjectPagedPersons(@PathVariable("projectId") Integer projectId,
                                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getPagedPersons(projectId, pageable);
    }

    /**
     * The method used to deleteProjectPersons
     **/
    @RequestMapping(value = "/{projectId}/team/{personId}",
            method = RequestMethod.DELETE)
    public void deleteProjectPersons(@PathVariable("projectId") Integer projectId,
                                     @PathVariable("personId") Integer personId) {
        projectService.deleteProjectPerson(projectId, personId);
    }

    /**
     * The method used to updateProjectPerson for ISProjectPerson
     **/

    @RequestMapping(value = "/{id}/team", method = RequestMethod.PUT)
    public ISProjectPerson updateProjectPerson(@PathVariable("id") Integer id,
                                               @RequestBody ISProjectPerson projectPerson) {
        return projectService.updateProjectPerson(projectPerson);
    }

    /**
     * The method used to createPersonRole for ISPersonRole
     **/
    @RequestMapping(value = "/{projectId}/person/{personId}/roles", method = RequestMethod.POST)
    public List<ISPersonRole> createPersonRoles(@PathVariable("projectId") Integer projectId,
                                                @PathVariable("personId") Integer personId,
                                                @RequestBody List<ISPersonRole> personRoles) {
        return projectService.createPersonRoles(personRoles);
    }

    /**
     * The method used to updatePersonRole for ISPersonRole
     **/

    @RequestMapping(value = "/projectRole/{projectId}", method = RequestMethod.PUT)
    public ISProjectRole updateProjectRole(@PathVariable("projectId") Integer projectId,
                                           @RequestBody ISProjectRole projectRole) {
        return projectService.updateProjectRole(projectRole);
    }

    /**
     * The method used to deletePersonRole for ISPersonRole
     **/
    @RequestMapping(value = "/{projectId}/{personId}/{rowId}", method = RequestMethod.DELETE)
    public void deletePersonRole(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("personId") Integer personId,
                                 @PathVariable("rowId") Integer rowId) {
        projectService.deletePersonRole(rowId);

    }

    /**
     * The method used to getRolesByPersonId for ISPersonRole
     **/
    @RequestMapping(value = "/{projectId}/{personId}/roles", method = RequestMethod.GET)
    public List<ISPersonRole> getRolesByPersonId(@PathVariable("projectId") Integer projectId,
                                                 @PathVariable("personId") Integer personId) {
        return projectService.findRolesByPerson(projectId, personId);
    }

    @RequestMapping(value = "/{projectId}/{roleId}/persons", method = RequestMethod.GET)
    public List<ISPersonRole> getPersonsByRoleId(@PathVariable("projectId") Integer projectId,
                                                 @PathVariable("roleId") Integer roleId) {
        return projectService.findPersonsByRole(projectId, roleId);
    }

    /**
     * The method used to getPersonsRoles for ISPersonRole
     **/
    @RequestMapping(value = "/{projectId}/multiple/persons/[{ids}]", method = RequestMethod.GET)
    public Map<Integer, List<ISPersonRole>> getPersonsRoles(@PathVariable("projectId") Integer projectId,
                                                            @PathVariable Integer[] ids) {
        return projectService.findPersonsRoles(projectId, ids);
    }

    /**
     * The method used to createProjectRole for ISProjectRole
     **/
    @RequestMapping(value = "/{projectId}/roles", method = RequestMethod.POST)
    public ISProjectRole createProjectRole(@PathVariable("projectId") Integer projectId,
                                           @RequestBody ISProjectRole role) {
        return projectService.createRole(role);
    }

    /**
     * The method used to getProjectPagedRoles for ISProjectRole
     **/
    @RequestMapping(value = "/{projectId}/roles/pageable", method = RequestMethod.GET)
    public Page<ISProjectRole> getProjectPagedRoles(@PathVariable("projectId") Integer projectId,
                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getPagedRoles(projectId, pageable);
    }

    /**
     * The method used to getProjectRoles for ISProjectRole
     **/
    @RequestMapping(value = "/{projectId}/roles", method = RequestMethod.GET)
    public List<ISProjectRole> getProjectRoles(@PathVariable("projectId") Integer projectId) {
        return projectService.getRoles(projectId);
    }

    @RequestMapping(value = "/{projectId}/roles/{id}", method = RequestMethod.GET)
    public ISProjectRole getProjectRole(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("id") Integer id) {
        return projectService.getRole(id);
    }

    /**
     * The method used to getMultipleRoles for ISProjectRole
     **/
    @RequestMapping(value = "/{projectId}/multiple/roles/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectRole> getMultipleRoles(@PathVariable("projectId") Integer projectId,
                                                @PathVariable Integer[] ids) {
        return projectService.findMultipleRoles(Arrays.asList(ids));
    }

    /**
     * The method used to deleteProjectRole
     **/
    @RequestMapping(value = "/{projectId}/roles/{id}", method = RequestMethod.DELETE)
    public void deleteProjectRole(@PathVariable("projectId") Integer projectId,
                                  @PathVariable("id") Integer id) {
        projectService.deleteProjectRole(id);

    }

    @RequestMapping(value = "/{projectId}/items/{id}", method = RequestMethod.GET)
    public ISBoqItem getBoqItem(@PathVariable("projectId") Integer projectId,
                                @PathVariable("id") Integer id) {
        return projectService.getBoqItem(id);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getProjectAttributesRequiredFalse(@PathVariable("objectType") String objectType) {
        return projectService.getProjectAttributes(objectType);
    }

    @RequestMapping(value = "/requiredFalseAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getProjectAttributes(@PathVariable("objectType") String objectType) {
        return projectService.getProjectAttributesRequiredFalse(objectType);
    }

    @RequestMapping(value = "/objectAttributes/project", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByProjectAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] projectIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return projectService.getObjectAttributesByProjectIdsAndAttributeIds(projectIds, attIds);
    }

    @RequestMapping(value = "/requiredProjectAttributes", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getRequiredProjectAttributes(@RequestParam("objectType") String objectType) {
        return projectService.getRequiredProjectAttributes(objectType);
    }

    @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
    public List<ISProjectPerson> getProjectPerson(@PathVariable("personId") Integer personId) {
        return projectService.getProjectPerson(personId);
    }

    @RequestMapping(value = "/boq/boqItem/{boqId}", method = RequestMethod.GET)
    public List<ISBoqItem> getBoqItemByItemNumber(@PathVariable("boqId") String boqId) {
        return projectService.getBoqItemByItemNumber(boqId);
    }

    @RequestMapping(value = "/boqItem/{boqId}", method = RequestMethod.GET)
    public ISProject getProjectByBoqId(@PathVariable("boqId") Integer boqId) {
        return projectService.getProjectByBoqId(boqId);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<ISProject> searchProjects(ProjectCriteria criteria) {
        return projectService.searchProjects(criteria);
    }

    @RequestMapping(value = "/freeSearch", method = RequestMethod.GET)
    public Page<ISProject> freeTextSearch(PageRequest pageRequest, ProjectCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISProject> projects = projectService.freeTextSearch(pageable, criteria);
        return projects;
    }

    @RequestMapping(value = "/report/{projectId}", method = RequestMethod.GET)
    public List<ProjectMaterialReportDTO> getProjectMaterialReportRecords(@PathVariable("projectId") Integer projectId) {
        return projectService.getMaterialReportByProjectRecords(projectId);
    }

    @RequestMapping(value = "/{projectId}/report/export/{fileType}", method = RequestMethod.GET,
            produces = "text/html")
    public String getProjectMaterialReport(@PathVariable("projectId") Integer projectId, @PathVariable("fileType") String fileType, HttpServletResponse response) {
        return projectService.getMaterialReportByProject(fileType, response, projectId);
    }

    @RequestMapping(value = "/{projectId}/projectPersons/filters", method = RequestMethod.GET)
    public Page<ISProjectPerson> getFilterProjectPersons(@PathVariable("projectId") Integer projectId,
                                                         PageRequest pageRequest,
                                                         ProjectPersonCriteria projectPersonCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISProjectPerson> projectPersons = projectService.getFilterdProjectPersons(projectId, pageable, projectPersonCriteria);
        return projectPersons;
    }

    @RequestMapping(value = "{projectId}/all/media", method = RequestMethod.GET)
    public List<Media> getAllMediaInProject(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getAllMediaInProject(projectId, pageable);
    }

    @RequestMapping(value = "{projectId}/cloneProject", method = RequestMethod.POST)
    public ISProject saveAsProject(@PathVariable("projectId") Integer projectId, @RequestBody ISProject cloneProject) {
        return projectService.saveAsProject(projectId, cloneProject);
    }

    @RequestMapping(value = "{projectId}/media/count", method = RequestMethod.GET)
    public MediaCountDTO getProjectMediaCount(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectMediaCount(projectId);
    }

    @RequestMapping(value = "{projectId}/projectPerson", method = RequestMethod.POST)
    public ISProjectPersonOrganization saveProject(@PathVariable("projectId") Integer projectId, @RequestBody ISProjectPersonOrganization projectPersonOrganization) {
        return projectService.saveProjectPerson(projectId, projectPersonOrganization);
    }

    @RequestMapping(value = "{projectId}/projectPersonObjects", method = RequestMethod.GET)
    public List<ISProjectPersonOrganization> getProjectPersonObjects(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectPersonObjects(projectId);
    }

    @RequestMapping(value = "/{projectId}/deleteNode/{nodeId}",
            method = RequestMethod.DELETE)
    public void deleteProjectNode(@PathVariable("projectId") Integer projectId,
                                  @PathVariable("nodeId") Integer nodeId) {
        projectService.deleteProjectNode(projectId, nodeId);
    }

    /*----  For Mobile App ---*/
    @RequestMapping(value = "/persons/{id}", method = RequestMethod.GET)
    public List<ISProject> getProjectsByPerson(@PathVariable("id") Integer personId) {
        return projectService.getProjectsByPerson(personId);
    }

    @RequestMapping(value = "/{id}/details", method = RequestMethod.GET)
    public ProjectDto getProjectDetails(@PathVariable("id") Integer personId) {
        return projectService.getProjectDetails(personId);
    }

    @RequestMapping(value = "/{id}/tasks/total", method = RequestMethod.GET)
    public List<ProjectTaskDto> getProjectTasks(@PathVariable("id") Integer projectId) {
        return projectService.getProjectTasks(projectId);
    }

    @RequestMapping(value = "/{id}/problems", method = RequestMethod.GET)
    public List<ProjectProblemDto> getProjectProblems(@PathVariable("id") Integer projectId) {
        return projectService.getProjectProblems(projectId);
    }

    @RequestMapping(value = "/{id}/persons", method = RequestMethod.GET)
    public List<ProjectPersonDto> getProjectTeam(@PathVariable("id") Integer projectId) {
        return projectService.getProjectTeam(projectId);
    }

    @RequestMapping(value = "/{id}/wbsList", method = RequestMethod.GET)
    public List<ISProjectWbs> getProjectWbsList(@PathVariable("id") Integer projectId) {
        return projectService.getProjectWbsList(projectId);
    }

    @RequestMapping(value = "{projectId}/copytasks", method = RequestMethod.POST)
    public ISProject copyTasks(@PathVariable("projectId") Integer projectId, @RequestBody ISProject cloneProject) {
        return projectService.copyTasks(projectId, cloneProject);
    }
}
