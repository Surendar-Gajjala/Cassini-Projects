package com.cassinisys.plm.controller.pm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.service.pm.ProjectItemReferenceService;
import com.cassinisys.plm.service.pm.ProjectService;
import com.cassinisys.plm.service.rm.RequirementsService;
import com.cassinisys.plm.service.rm.SpecificationsService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by swapna on 1/3/18.
 */

@RestController
@RequestMapping("/plm/projects")
@Api(tags = "PLM.PM", description = "Project Related")
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private ProjectItemReferenceService projectItemReferenceService;
    @Autowired
    private ItemPredicateBuilder predicateBuilder;

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private RequirementsService requirementsService;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private ItemRepository itemRepository;

    @RequestMapping(method = RequestMethod.POST)
    public PLMProject createProject(@RequestBody PLMProject plmProject) {
        return projectService.createProject(plmProject);
    }

    @RequestMapping(value = "/template", method = RequestMethod.POST)
    public PLMProject createTemplateProject(@RequestParam("template") Integer template, @RequestBody PLMProject plmProject) {
        return projectService.createTemplateProject(plmProject, template);
    }

    @RequestMapping(value = "/template/add", method = RequestMethod.POST)
    public PLMProject addTemplateProject(@RequestParam("template") Integer template, @RequestBody PLMProject plmProject) throws ParseException {
        return projectService.addTemplateProject(plmProject, template);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PLMProject> getProjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getProjects(pageable);
    }

    @RequestMapping(value = "/programnull", method = RequestMethod.GET)
    public List<ProgramProjectFolderDto> getProgramNullProjects() {
        return projectService.getProgramNullProjects();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<ProjectDto> getFilteredProjects(PageRequest pageRequest, ProjectCriteria projectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getFilteredProjects(pageable, projectCriteria);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
    public PLMProject getProject(@PathVariable("projectId") Integer projectId) {
        return projectService.getProject(projectId);
    }

    @RequestMapping(value = "/{projectId}/percentComplete", method = RequestMethod.GET)
    public PLMProject getProjectPercentComplete(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectPercentComplete(projectId);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.PUT)
    public PLMProject updateProject(@RequestBody PLMProject plmProject) throws ParseException, JsonProcessingException {
        return projectService.updateProject(plmProject);
    }

    @RequestMapping(value = "/{projectId}", method = RequestMethod.DELETE)
    public void deleteProject(@PathVariable("projectId") Integer projectId) {
        projectService.deleteProject(projectId);
    }

    @RequestMapping(value = "/{projectId}/memebers", method = RequestMethod.POST)
    public PLMProjectMember createProjectMember(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectMember projectMember) {
        return projectService.createProjectMember(projectId, projectMember);
    }

    @RequestMapping(value = "/projectManagers", method = RequestMethod.GET)
    public List<Person> getProjectManagers() {
        return projectService.getProjectManagers();
    }

    @RequestMapping(value = "/{projectId}/team", method = RequestMethod.POST)
    public List<PLMProjectMember> createProjectMembers(@PathVariable("projectId") Integer projectId,
                                                       @RequestBody List<Person> persons) throws JsonProcessingException {
        return projectService.createProjectMembers(projectId, persons);
    }

    @RequestMapping(value = "/{projectId}/members/{personId}", method = RequestMethod.GET)
    public PLMProjectMember getProjectMember(@PathVariable("projectId") Integer projectId, @PathVariable("personId") Integer personId) {
        return projectService.getProjectMember(projectId, personId);
    }

    @RequestMapping(value = "/{projectId}/members/{memberId}", method = RequestMethod.PUT)
    public PLMProjectMember updateProjectMember(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectMember projectMember) {
        return projectService.updateProjectMember(projectMember);
    }

    @RequestMapping(value = "/{projectId}/members/{projectMemberId}", method = RequestMethod.DELETE)
    public void deleteProjectMember(@PathVariable("projectId") Integer projectId, @PathVariable("projectMemberId") Integer projectMemberId) throws JsonProcessingException {
        projectService.deleteProjectMember(projectId, projectMemberId);
    }

    @RequestMapping(value = "/{projectId}/members", method = RequestMethod.GET)
    public Page<PLMProjectMember> getProjectMembers(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getProjectMembers(projectId, pageable);
    }

    @RequestMapping(value = "/{projectId}/wbs", method = RequestMethod.POST)
    public PLMWbsElement createWBSElement(@PathVariable("projectId") Integer projectId, @RequestBody PLMWbsElement wbsElement) {
        return projectService.createWBSElement(wbsElement);
    }

    @RequestMapping(value = "/{projectId}/wbsList", method = RequestMethod.POST)
    public List<PLMWbsElement> createWBSElements(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMWbsElement> wbsElementList) throws JsonProcessingException {
        return projectService.createWBSElements(projectId, wbsElementList);
    }

    @RequestMapping(value = "/{projectId}/createLinks", method = RequestMethod.POST)
    public void createLinks(@PathVariable("projectId") Integer projectId,
                            @RequestBody String links) throws IOException {
        projectService.createLinks(projectId, links);
    }

    @RequestMapping(value = "/{projectId}/getLinks", method = RequestMethod.GET)
    public PLMLinks getLinks(@PathVariable("projectId") Integer projectId) {
        return projectService.getLinks(projectId);
    }

    @RequestMapping(value = "/{projectId}/wbs", method = RequestMethod.GET)
    public List<PLMWbsElement> getWBSElements(@PathVariable("projectId") Integer projectId) {
        return projectService.getWBSElements(projectId);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}", method = RequestMethod.GET)
    public PLMWbsElement getWBSElement(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) {
        return projectService.getWBSElement(wbsId);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}", method = RequestMethod.PUT)
    public PLMWbsElement updateWBSElement(@PathVariable("projectId") Integer projectId, @RequestBody PLMWbsElement wbsElement) {
        return projectService.updateWBSElement(projectId, wbsElement);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}", method = RequestMethod.DELETE)
    public void deleteWBSElement(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) throws JsonProcessingException {
        projectService.deleteWBSElement(wbsId);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}/activities", method = RequestMethod.GET)
    public List<PLMActivity> getWBSActivities(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) {
        return projectService.getWBSActivities(wbsId);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}/milestones", method = RequestMethod.GET)
    public List<PLMMilestone> getWBSMilestones(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) {
        return projectService.getWBSMilestones(wbsId);
    }

    @RequestMapping(value = "/{projectId}/wbs/roots", method = RequestMethod.GET)
    public List<PLMWbsElement> getRootWbsElements(@PathVariable("projectId") Integer projectId) {
        return projectService.getRootWbsElements(projectId);
    }

    @RequestMapping(value = "/{projectId}/wbs/{wbsId}/children", method = RequestMethod.GET)
    public List<PLMWbsElement> getWBSElementChildren(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) {
        return projectService.getWBSElementChildren(wbsId);
    }

    @RequestMapping(value = "/{projectId}/files", method = RequestMethod.GET)
    public List<PLMProjectFile> getProjectFiles(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectFiles(projectId);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}", method = RequestMethod.GET)
    public PLMProjectFile getProjectFile(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) {
        return projectService.getProjectFile(fileId);
    }

    @RequestMapping(value = "/{projectId}/files", method = RequestMethod.POST)
    public List<PLMProjectFile> uploadProjectFiles(@PathVariable("projectId") Integer projectId, @RequestParam("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException, JsonProcessingException {
        return projectService.uploadProjectFiles(projectId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMProjectFile getLatestUploadedProjectFile(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) {
        return projectService.getLatestUploadedProjectFile(projectId, fileId);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMProjectFile> getFileVersions(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) {
        return projectService.getFileVersions(projectId, fileId);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    public List<PLMProjectFile> getProjectFileVersionAndCommentsAndDownloads(@PathVariable("projectId") Integer projectId,
                                                                             @PathVariable("fileId") Integer fileId,
                                                                             @PathVariable("objectType") ObjectType objectType) {
        return projectService.getProjectFileVersionAndCommentsAndDownloads(projectId, fileId, objectType);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteProjectFile(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        projectService.deleteProjectFile(projectId, fileId);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/download/{type}", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                                 HttpServletResponse response) {
        PLMProjectFile projectFile = projectService.getProjectFile(fileId);
        File file = projectService.getProjectFile(projectId, fileId, type);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, projectFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/preview/{type}", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("projectId") Integer projectId,
                                @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                                HttpServletResponse response) throws IOException {
        PLMProjectFile projectFile = projectService.getProjectFile(fileId);
        String fileName = projectFile.getName();
        File file = projectService.getProjectFile(projectId, fileId, type);
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

    @RequestMapping(value = "{projectId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("projectId") Integer projectId, HttpServletResponse response) throws IOException {
        projectService.generateZipFile(projectId, response);
    }

    @RequestMapping(value = "/{projectId}/deliverables", method = RequestMethod.POST)
    public PLMProjectDeliverable createProjectDeliverable(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectDeliverable projectDeliverable) {
        return projectService.createProjectDeliverable(projectDeliverable);
    }

    @RequestMapping(value = "/{projectId}/deliverables/multiple", method = RequestMethod.POST)
    public List<PLMProjectDeliverable> createProjectDeliverables(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMItem> items) throws JsonProcessingException {
        return projectService.createProjectDeliverables(projectId, items);
    }

    @RequestMapping(value = "/{projectId}/glossaryDeliverables/multiple", method = RequestMethod.POST)
    public List<PLMGlossaryDeliverable> createProjectGlossaryDeliverables(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMGlossary> glossaries) {
        return projectService.createProjectGlossaryDeliverables(projectId, glossaries);
    }

    @RequestMapping(value = "/{projectId}/deliverables/{deliverableId}", method = RequestMethod.GET)
    public PLMProjectDeliverable getProjectDeliverableById(@PathVariable("projectId") Integer projectId, @PathVariable("projectDeliverableId") Integer projectDeliverableId) {
        return projectService.getProjectDeliverableById(projectDeliverableId);
    }

    @RequestMapping(value = "/{projectId}/deliverables/{deliverableId}", method = RequestMethod.PUT)
    public PLMProjectDeliverable updateProjectDeliverable(@RequestBody PLMProjectDeliverable projectDeliverable) {
        return projectService.updateProjectDeliverable(projectDeliverable);
    }

    @RequestMapping(value = "/{projectId}/deliverables/{deliverableId}", method = RequestMethod.DELETE)
    public void deleteProjectDeliverable(@PathVariable("projectId") Integer projectId, @PathVariable("deliverableId") Integer deliverableId) throws JsonProcessingException {
        projectService.deleteProjectDeliverable(projectId, deliverableId);
    }

/*    @RequestMapping(value = "/{projectId}/deliverables", method = RequestMethod.GET)
    public List<PLMProjectDeliverable> getProjectDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectDeliverablesByProjectId(projectId);
    }*/

    @RequestMapping(value = "/{projectId}/projectdeliverables", method = RequestMethod.GET)
    public List<PLMItem> getProjectDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectDeliverablesByProjectId(projectId);
    }

    @RequestMapping(value = "/{projectId}/projectGlossarydeliverables", method = RequestMethod.GET)
    public List<PLMGlossary> getProjectGlossarysDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectGlossarysDeliverablesByProjectId(projectId);
    }

    @RequestMapping(value = "/{projectId}/projectSpecificationDeliverables", method = RequestMethod.GET)
    public List<Specification> getProjectSpecificationDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return specificationsService.getProjectSpecificationDeliverablesByProjectId(projectId);
    }

    @RequestMapping(value = "/{projectId}/projectRequirementDeliverables", method = RequestMethod.GET)
    public List<Requirement> getProjectRequirementDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return requirementsService.getProjectRequirementDeliverablesByProjectId(projectId);
    }

    @RequestMapping(value = "/{projectId}/deliverables", method = RequestMethod.GET)
    public List<PLMProjectDeliverable> getAllProjectDeliverablesByProjectId(@PathVariable("projectId") Integer projectId) {
        return projectService.getAllProjectDeliverablesByProjectId(projectId);
    }

    @RequestMapping(value = "/freetextsearch", method = RequestMethod.GET)
    public Page<PLMProject> freeTextSearch(PageRequest pageRequest, ProjectCriteria projectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMProject> projects = projectService.freeTextSearch(pageable, projectCriteria);
        return projects;
    }

    @RequestMapping(value = "/attributes/{typeId}", method = RequestMethod.GET)
    public List<PMObjectTypeAttribute> getAllTypeAttributes(@PathVariable("typeId") Integer typeId, @RequestParam("hierarchy") Boolean hierarchy) {
        if (hierarchy == null) {
            hierarchy = true;
        }
        return projectService.getAllTypeAttributes(typeId, hierarchy);
    }

    @RequestMapping(value = "{projectId}/itemReferences", method = RequestMethod.POST)
    public PLMProjectItemReference createProjectItemReference(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectItemReference projectItemReference) {
        return projectItemReferenceService.create(projectItemReference);
    }

    @RequestMapping(value = "{projectId}/itemReferences/{itemId}", method = RequestMethod.PUT)
    public PLMProjectItemReference updateProjectItemReference(@PathVariable("projectId") Integer projectId, @PathVariable("itemId") Integer itemId,
                                                              @RequestBody PLMProjectItemReference projectItemReference) {
        return projectItemReferenceService.update(projectItemReference);
    }

    @RequestMapping(value = "/{projectId}/itemReferences/{itemId}", method = RequestMethod.DELETE)
    public void deleteProjectItem(@PathVariable("projectId") Integer projectId, @PathVariable("itemId") Integer itemId) throws JsonProcessingException {
        projectItemReferenceService.deleteItem(projectId, itemId);
    }

    @RequestMapping(value = "/{projectId}/itemReferences/{itemId}", method = RequestMethod.GET)
    public PLMProjectItemReference getProjectItemReference(@PathVariable("projectId") Integer projectId, @PathVariable("itemId") Integer itemId) {
        return projectItemReferenceService.get(itemId);
    }

    @RequestMapping(value = "/{projectId}/itemReferences", method = RequestMethod.GET)
    public List<PLMProjectItemReference> getItemsByProject(@PathVariable("projectId") Integer projectId) {
        return projectItemReferenceService.getItemsByProject(projectId);
    }

    @RequestMapping(value = "{projectId}/items/search", method = RequestMethod.GET)
    public Page<PLMItem> search(@PathVariable("projectId") Integer projectId, ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria,
                QPLMItem.pLMItem);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> pdmItems = projectItemReferenceService.searchItems(projectId, predicate, pageable);
        return pdmItems;
    }

    @RequestMapping(value = "{projectId}/items/type/{objectType}/search", method = RequestMethod.GET)
    public Page<PLMItem> searchReferenaceItems(@PathVariable("projectId") Integer projectId, @PathVariable("objectType") String type, ProjectItemReferenceCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectItemReferenceService.searchReferenceItems(projectId, criteria, pageable, type);
    }

    @RequestMapping(value = "/items/{projectId}", method = RequestMethod.GET)
    public Page<PLMItem> getProjectItems(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectItemReferenceService.getProjectItems(projectId, pageable);
    }

    @RequestMapping(value = "{projectId}/itemReferences/multiple", method = RequestMethod.POST)
    public List<PLMProjectItemReference> createItemReferences(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMProjectItemReference> projectItemReferences) throws JsonProcessingException {
        return projectItemReferenceService.createItemReferences(projectItemReferences);
    }

    @RequestMapping(value = "{projectId}/wbsStructure", method = RequestMethod.GET)
    public List<WBSDto> getProjectWbs(@PathVariable("projectId") Integer projectId) {
        List<WBSDto> wbsElementList = new ArrayList<>();
        PLMProject project = projectService.getProject(projectId);
        if (project != null) {
            wbsElementList = projectService.getProjectPlanStructure(project);
        }
        return wbsElementList;
    }

    @RequestMapping(value = "{projectId}/wbsTree", method = RequestMethod.GET)
    public List<PLMWbsElement> getProjectWbsTree(@PathVariable("projectId") Integer projectId) {
        List<PLMWbsElement> wbsElementList = null;
        PLMProject project = projectService.getProject(projectId);
        if (project != null) {
            wbsElementList = projectService.getProjectWbsTree(project);
        }
        return wbsElementList;
    }

    @RequestMapping(value = "{projectId}/parent/{parentId}/wbsName", method = RequestMethod.GET)
    public PLMWbsElement getChildWbsName(@PathVariable("projectId") Integer projectId, @PathVariable("parentId") Integer parentId,
                                         @RequestParam("wbsName") String wbsName) {
        return projectService.getProjectWbsName(projectId, wbsName, parentId);
    }

    @RequestMapping(value = "{projectId}/parent/{parentName}", method = RequestMethod.GET)
    public PLMWbsElement getParentWbsByName(@PathVariable("projectId") Integer projectId, @PathVariable("parentName") String parentName) {
        return projectService.getParentWbsByName(projectId, parentName);
    }

    @RequestMapping(value = "{projectId}/wbs/children/{wbsId}", method = RequestMethod.GET)
    public List<WBSDto> getWbsChildren(@PathVariable("projectId") Integer projectId, @PathVariable("wbsId") Integer wbsId) {
        return projectService.getWbsChildrenBySequence(projectId, wbsId);
    }

    @RequestMapping(value = "/{projectId}/template", method = RequestMethod.POST)
    public ProjectTemplate saveAsTemplate(@PathVariable("projectId") Integer projectId, @RequestBody ProjectTemplate projectTemplate) {
        return projectService.saveAsTemplate(projectId, projectTemplate);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        return projectService.fileDownloadHistory(projectId, fileId);
    }

    @RequestMapping(value = "{projectId}/ProjectGlossariesDeliverables", method = RequestMethod.GET)
    public List<PLMGlossary> ProjectGlossariesDeliverables(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectGlossariesDeliverables(projectId);
    }

    @RequestMapping(value = "/{projectId}/glossaryDeliverables/{glossaryId}", method = RequestMethod.DELETE)
    public void deleteGlossaryDeliverable(@PathVariable("projectId") Integer projectId, @PathVariable("glossaryId") Integer glossaryId) {
        projectService.deleteGlossaryDeliverable(projectId, glossaryId);
    }

    @RequestMapping(value = "/{projectId}/itemDeliverable", method = RequestMethod.GET)
    public Page<PLMItem> getProjectItemDeliverables(@PathVariable("projectId") Integer projectId, PageRequest pageRequest, ProjectDeliverableCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getProjectItemDeliverable(projectId, pageable, criteria);
    }

    @RequestMapping(value = "/{projectId}/glossaryDeliverable", method = RequestMethod.GET)
    public Page<PLMGlossary> getProjectGlossaryDeliverables(@PathVariable("projectId") Integer projectId, PageRequest pageRequest, ProjectDeliverableCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectService.getProjectGlossaryDeliverables(projectId, pageable, criteria);
    }

    /*---------------  Specification Deliverables --------------*/
    @RequestMapping(value = "/{projectId}/specificationDeliverable", method = RequestMethod.GET)
    public Page<Specification> getProjectSpecificationDeliverables(@PathVariable("projectId") Integer projectId, PageRequest pageRequest, SpecificationBuilderCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return specificationsService.getProjectSpecificationDeliverables(projectId, pageable, criteria);
    }

    @RequestMapping(value = "/{projectId}/{objectType}/specificationDeliverables/multiple", method = RequestMethod.POST)
    public List<SpecificationDeliverable> createProjectSpecificationDeliverables(@PathVariable("projectId") Integer projectId, @PathVariable("objectType") String objectType, @RequestBody List<Specification> specifications) {
        return specificationsService.createProjectSpecificationDeliverables(projectId, objectType, specifications);
    }

    @RequestMapping(value = "/{projectId}/specDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getSpecificationDeliverables(@PathVariable("projectId") Integer projectId) {
        return projectService.getSpecificationDeliverables(projectId);
    }

    @RequestMapping(value = "/{projectId}/requirementDeliverable", method = RequestMethod.GET)
    public Page<Requirement> getProjectRequirementDeliverables(@PathVariable("projectId") Integer projectId, PageRequest pageRequest, RequirementBuildCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return requirementsService.getProjectRequirementDeliverables(projectId, pageable, criteria);
    }

    @RequestMapping(value = "/{projectId}/{objectType}/requirementDeliverables/multiple", method = RequestMethod.POST)
    public List<RequirementDeliverable> createProjectRequirementDeliverables(@PathVariable("projectId") Integer projectId, @PathVariable("objectType") String objectType, @RequestBody List<Requirement> requirements) {
        return requirementsService.createProjectRequirementDeliverables(projectId, objectType, requirements);
    }

    @RequestMapping(value = "/{projectId}/reqDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getRequirementDeliverables(@PathVariable("projectId") Integer projectId) {
        return projectService.getRequirementDeliverables(projectId);
    }

    @RequestMapping(value = "/{projectId}/specificationDeliverables/{specId}", method = RequestMethod.DELETE)
    public void deleteSpecificationDeliverable(@PathVariable("projectId") Integer projectId, @PathVariable("specId") Integer specId) {
        specificationsService.deleteSpecificationDeliverable(projectId, specId);
    }

    @RequestMapping(value = "/{projectId}/requirementDeliverables/{reqId}", method = RequestMethod.DELETE)
    public void deleteRequirementDeliverable(@PathVariable("projectId") Integer projectId, @PathVariable("reqId") Integer reqId) {
        requirementsService.deleteRequirementDeliverable(projectId, reqId);
    }

    @RequestMapping(value = "/{projectId}/projectDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getProjectDeliverableAndGlossaryDeliverble(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectDeliverableAndGlossaryDeliverble(projectId);
    }

    @RequestMapping(value = "/{projectId}/copy/wbs/{wbs}", method = RequestMethod.POST)
    public PLMWbsElement copyWbsStructure(@PathVariable("projectId") Integer projectId, @PathVariable("wbs") Integer wbs) {
        return projectService.copyWbsStructure(projectId, wbs);
    }

    @RequestMapping(value = "/{projectId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMProjectFile renameFile(@PathVariable("projectId") Integer id,
                                     @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return projectService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PLMProject> getMultiple(@PathVariable Integer[] ids) {
        return projectService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
    public List<PLMProject> getPersonProjects(@PathVariable("personId") Integer personId) {
        return projectService.getPersonProjects(personId);
    }

    @RequestMapping(value = "/wbs/{wbs}", method = RequestMethod.GET)
    public PLMProject getWbsProject(@PathVariable("wbs") Integer wbs) {
        return projectService.getWbsProject(wbs);
    }

    @RequestMapping(value = "{projectId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMProjectFile> replaceProjectFiles(@PathVariable("projectId") Integer projectId,
                                                    @PathVariable("fileId") Integer fileId,
                                                    MultipartHttpServletRequest request) throws JsonProcessingException {
        return projectService.replaceProjectFiles(projectId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/{projectId}/folder", method = RequestMethod.POST)
    public PLMProjectFile createProjectFolder(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectFile plmProjectFile) throws JsonProcessingException {
        return projectService.createProjectFolder(projectId, plmProjectFile);
    }

    @RequestMapping(value = "/{projectId}/folder/{folderId}/upload", method = RequestMethod.POST)
    public List<PLMProjectFile> uploadProjectFolderFiles(@PathVariable("projectId") Integer projectId, @PathVariable("folderId") Integer folderId,
                                                         MultipartHttpServletRequest request) throws JsonProcessingException {
        return projectService.uploadProjectFolderFiles(projectId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{folderId}/move", method = RequestMethod.PUT)
    public PLMFile moveProjectFileToFolder(@PathVariable("folderId") Integer folderId,
                                           @RequestBody PLMProjectFile file) throws Exception {
        return projectService.moveProjectFileToFolder(folderId, file);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PLMProjectFile> getProjectFolderChildren(@PathVariable("folderId") Integer folderId) {
        return projectService.getProjectFolderChidren(folderId);
    }

    @RequestMapping(value = "/{projectId}/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("projectId") Integer projectId, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        projectService.deleteFolder(projectId, fileId);
    }

    @RequestMapping(value = "/team/members/{projectId}", method = RequestMethod.GET)
    public List<Person> getAllProjectMembers(@PathVariable("projectId") Integer projectId) {
        return projectService.getAllProjectMembers(projectId);
    }

    @RequestMapping(value = "/{projectId}/details", method = RequestMethod.GET)
    public DetailsCount getProjectDetails(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectDetails(projectId);
    }

    @RequestMapping(value = "/project/file/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateProjectFile(@PathVariable("fileId") Integer fileId,
                                     @RequestBody PLMProjectFile file) throws JsonProcessingException {
        return projectService.updateProjectFile(fileId, file);
    }

    @RequestMapping(value = "/project/task/file/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateTaskFile(@PathVariable("fileId") Integer fileId,
                                  @RequestBody PLMTaskFile file) {
        return projectService.updateProjectTaskFile(fileId, file);
    }

    @RequestMapping(value = "{actualId}/{targetId}/updateSequence", method = RequestMethod.GET)
    public PLMWbsElement updateWbsItemSeq(@PathVariable("actualId") Integer actualId, @PathVariable("targetId") Integer targetId) {
        return projectService.updateWbsItemSeq(actualId, targetId);
    }

    @RequestMapping(value = "{projectId}/files/paste", method = RequestMethod.PUT)
    public List<PLMProjectFile> pasteFromClipboard(@PathVariable("projectId") Integer projectId, @RequestParam("fileId") Integer fileId,
                                                   @RequestBody List<PLMFile> files) {
        return projectService.pasteFromClipboard(projectId, fileId, files);
    }

    @RequestMapping(value = "/{projectId}/deliverables/paste", method = RequestMethod.PUT)
    public PLMProjectDeliverableDto pasteProjectDeliverables(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        return projectService.pasteProjectDeliverables(projectId, deliverableDto);
    }

    @RequestMapping(value = "/{projectId}/deliverables/undo", method = RequestMethod.PUT)
    public void undoProjectDeliverables(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        projectService.undoProjectDeliverables(projectId, deliverableDto);
    }

    @RequestMapping(value = "{projectId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedProjectFiles(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMProjectFile> projectFiles) {
        projectService.undoCopiedProjectFiles(projectId, projectFiles);
    }

    @RequestMapping(value = "/{projectId}/update/sequence", method = RequestMethod.GET)
    public void updateProjectPlanSequenceNumbers(@PathVariable("projectId") Integer projectId) {
        projectService.updateSequenceNumberForProjectPlan(projectId);
    }

    @RequestMapping(value = "/workflow/{type}", method = RequestMethod.GET)
    public List<PLMWorkflowDefinition> getWorkflows(@PathVariable("type") String type) {
        return projectService.getProjectWorkflows(type);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}", method = RequestMethod.GET)
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return projectService.attachProjectWorkflow(id, wfDefId, null);
    }

    @RequestMapping(value = "/{specId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("specId") Integer specId) {
        workflowService.deleteWorkflow(specId);
    }

    @RequestMapping(value = "/deliverable/finish", method = RequestMethod.POST)
    public PLMDeliverable finishProjectDeliverable(@RequestBody PLMProjectDeliverable plmDeliverable) throws JsonProcessingException {
        return projectService.finishProjectDeliverable(plmDeliverable);
    }

    @RequestMapping(value = "/{projectId}/files/byName/{name}", method = RequestMethod.GET)
    public List<PLMProjectFile> getProjectFilesByName(@PathVariable("projectId") Integer projectId,
                                                      @PathVariable("name") String name) {
        return projectService.getProjectFilesByName(projectId, name);
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateActivityFile(@PathVariable("activityId") Integer activityId,
                                      @PathVariable("fileId") Integer fileId,
                                      @RequestBody PLMProjectFile file) throws JsonProcessingException {
        return projectService.updateProjectFile(fileId, file);
    }


    @RequestMapping(value = "/{projectId}/reqdocuments/multiple", method = RequestMethod.POST)
    public List<PLMProjectRequirementDocument> createProjectReqDocuments(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMProjectRequirementDocument> documentProjects) throws JsonProcessingException {
        return projectService.createProjectReqDocuments(documentProjects);
    }

    @RequestMapping(value = "/{projectId}/reqdocuments", method = RequestMethod.POST)
    public PLMProjectRequirementDocument createProjectReqDocument(@PathVariable("projectId") Integer projectId, @RequestBody PLMProjectRequirementDocument documentProject) throws JsonProcessingException {
        return projectService.createProjectReqDocument(documentProject);
    }

    @RequestMapping(value = "/{projectId}/reqdocuments/{projectReqDocId}", method = RequestMethod.PUT)
    public PLMProjectRequirementDocument updateProjectReqDocument(@PathVariable("projectId") Integer projectId, @PathVariable("projectReqDocId") Integer projectReqDocId,
                                                                  @RequestBody PLMProjectRequirementDocument documentProject) {
        return projectService.updateProjectReqDocument(projectReqDocId, documentProject);
    }

    @RequestMapping(value = "/{projectId}/reqdocuments", method = RequestMethod.GET)
    public List<PLMProjectRequirementDocument> getProjectReqDocuments(@PathVariable("projectId") Integer projectId) {
        return projectService.getProjectReqDocuments(projectId);
    }

    @RequestMapping(value = "/{projectId}/reqdocuments/{projectReqDocId}", method = RequestMethod.DELETE)
    public void deleteProjectReqDocument(@PathVariable("projectId") Integer projectId, @PathVariable("projectReqDocId") Integer projectReqDocId) throws JsonProcessingException {
        projectService.deleteProjectReqDocument(projectId, projectReqDocId);
    }

    @RequestMapping(value = "/subscribe/{projectId}", method = RequestMethod.POST)
    public PLMSubscribe subscribeItem(@PathVariable("projectId") Integer itemId) {
        return projectService.subscribeProject(itemId);
    }

    @RequestMapping(value = "/projecttype/{type}/tree", method = RequestMethod.GET)
    public List<PMObjectType> getProjectTypeTree(@PathVariable("type") PMType type) {
        return projectService.getProjectTypeTree(type);
    }

    @RequestMapping(value = "/{projectId}/member", method = RequestMethod.POST)
    public PLMProjectMember createProjectMemeber(@PathVariable("projectId") Integer id, @RequestBody PLMProjectMember projectMemeber) {
        return projectService.createProjectMemeber(id, projectMemeber);
    }

    @RequestMapping(value = "/{projectId}/member/{id}", method = RequestMethod.PUT)
    public PLMProjectMember updateProjectMemeber(@PathVariable("id") Integer id,
                                                 @RequestBody PLMProjectMember projectMemeber) {
        return projectService.updateProjectMemeber(id, projectMemeber);
    }

    @RequestMapping(value = "/{projectId}/member/multiple", method = RequestMethod.POST)
    public List<PLMProjectMember> createProjectMemebers(@PathVariable("projectId") Integer Id,
                                                        @RequestBody List<PLMProjectMember> projectMemebers) {
        return projectService.createProjectMemebers(Id, projectMemebers);
    }

    @RequestMapping(value = "/{projectId}/person/{personId}/assignedTo/count", method = RequestMethod.GET)
    public Integer getProjectTasksAssignedToCount(@PathVariable("projectId") Integer projectId, @PathVariable("personId") Integer personId) {
        return projectService.getProjectTasksAssignedToCount(projectId, personId);
    }

}
