package com.cassinisys.is.api.tm;
/**
 * The Class is for ProjectTaskController
 **/

import com.cassinisys.is.filtering.ReportCriteria;
import com.cassinisys.is.filtering.TaskCriteria;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.TaskProblemDto;
import com.cassinisys.is.model.tm.*;
import com.cassinisys.is.model.workflow.ISWorkFlowStatusApprover;
import com.cassinisys.is.model.workflow.ISWorkflow;
import com.cassinisys.is.model.workflow.ISWorkflowStatus;
import com.cassinisys.is.model.workflow.ISWorkflowStatusHistory;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.tm.TaskRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowRepository;
import com.cassinisys.is.service.pm.ProjectService;
import com.cassinisys.is.service.tm.ProjectTaskService;
import com.cassinisys.is.service.workflow.ISWorkflowService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

@Api(name = "Project tasks",
        description = "Project tasks endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class ProjectTaskController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectTaskService projectTaskService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private ISWorkflowService isWorkflowService;

    @Autowired
    private ISWorkflowRepository isWorkflowRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private IssueRepository issueRepository;

    @RequestMapping(value = "/{objectId}/formData", method = RequestMethod.POST)
    public ISProjectTask submitFormData(@PathVariable("objectId") Integer objectId, MultipartHttpServletRequest req) {
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterator = upload.getItemIterator(req);
            while (iterator.hasNext()) {
                FileItemStream item = iterator.next();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    if (item.getFieldName().equals("vFormName")) {
                        byte[] str = new byte[stream.available()];
                        stream.read(str);
                        String full = new String(str, "UTF8");
                    }
                } else {
                    byte[] data = new byte[stream.available()];
                    stream.read(data);
                    String base64 = Base64Utils.encodeToString(data);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ISProjectTask projectTask = new ISProjectTask();
        return projectTaskService.submitFormData(projectTask);
    }

    /**
     * The method used for creating the ISBidRfq
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectTask create(@PathVariable("projectId") Integer projectId,
                                @RequestBody ISProjectTask projectTask) {
        return projectTaskService.create(projectTask);
    }

    /**
     * The method used for updating the ISProjectTask
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.PUT)
    public ISProjectTask update(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @RequestBody ISProjectTask projectTask) {
        projectTask.setId(taskId);
        return projectTaskService.update(projectId, projectTask);
    }

    /**
     * The method used for deleting the ISBidRfq
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("taskId") Integer taskId) {
        projectTaskService.delete(taskId);
    }

    /**
     * The method used to get the values of ISProjectTask
     **/
    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public ISProjectTask get(@PathVariable("projectId") Integer projectId,
                             @PathVariable("taskId") Integer taskId) {
        return projectTaskService.getTask(projectId, taskId);
    }

    /**
     * The method used to getAllTasksByFilters from the list of ISProjectTask
     **/
    @RequestMapping(value = "/filters", method = RequestMethod.GET)
    public Page<ISProjectTask> getAllTasksByFilters(@PathVariable("projectId") Integer projectId, TaskCriteria criteria, PageRequest pageRequest) {
        criteria.setProject(projectId);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.find(criteria, pageable);
    }

    /**
     * The method used to getPageableTasksForProject from the page of ISProjectTask
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISProjectTask> getPageableTasksForProject(
            @PathVariable("projectId") Integer projectId,
            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getPageableTasksForProject(projectId, pageable);
    }

    /**
     * The method used to getAllProjectTasks from the page of ISProjectTask
     **/
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<ISProjectTask> getAllProjectTasks(TaskCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.findAll(criteria, pageable);
    }

    /**
     * The method used to getTasks from the list of ISProjectTask
     **/
    @RequestMapping(value = "/projecttasks", method = RequestMethod.GET)
    public List<ISProjectTask> getTasks(@PathVariable("projectId") Integer projectId, TaskCriteria criteria) {
        criteria.setProject(projectId);
        return projectTaskService.findTasks(criteria);
    }

    /**
     * The method used to getAllProjectTasks from the list of ISProjectTask
     **/
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ISProjectTask> getAllProjectTasks(
            @PathVariable("projectId") Integer projectId) {
        return projectTaskService.getAllByProject(projectId);
    }

    /**
     * The method used to getTasksBySite from the list of ISProjectTask
     **/
    @RequestMapping(value = "/bySite/{siteId}", method = RequestMethod.GET)
    public List<ISProjectTask> getTasksBySite(
            @PathVariable("siteId") Integer siteId) {
        return projectTaskService.getTasksBySite(siteId);
    }

    /**
     * The method used to getIssues from the page of ISStockIssue
     **/
    @RequestMapping(value = "/{taskId}/issues", method = RequestMethod.GET)
    public Page<ISIssue> getIssues(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getIssues(taskId, pageable);
    }

    /**
     * The method used to getAssignedTo from the list of person
     **/
    @RequestMapping(value = "/{taskId}/assignedTo", method = RequestMethod.GET)
    public List<Person> getAssignedTo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId) {
        return projectTaskService.getAssignedTo(taskId);
    }

    /**
     * The method used to addAssignedTo to ISTaskAssignedTo
     **/
    @RequestMapping(value = "/{taskId}/assignedTo/{personId}",
            method = RequestMethod.PUT)
    public ISTaskAssignedTo addAssignedTo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId,
            @PathVariable("personId") Integer personId) {
        return projectTaskService.addAssignedTo(taskId, personId);
    }

    /**
     * The method used to deleteAssignedTo
     **/
    @RequestMapping(value = "/{taskId}/assignedTo/{personId}",
            method = RequestMethod.DELETE)
    public void deleteAssignedTo(@PathVariable("projectId") Integer projectId,
                                 @PathVariable("taskId") Integer taskId,
                                 @PathVariable("personId") Integer personId) {
        projectTaskService.deleteAssignedTo(taskId, personId);
    }

    /**
     * The method used to getObservers from Person
     **/
    @RequestMapping(value = "/{taskId}/observers", method = RequestMethod.GET)
    public Page<Person> getObservers(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getObservers(taskId, pageable);
    }

    /**
     * The method used to addObservers to  ISTaskObserver
     **/
    @RequestMapping(value = "/{taskId}/observers/{personId}",
            method = RequestMethod.PUT)
    public ISTaskObserver addObservers(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId,
            @PathVariable("personId") Integer personId) {
        return projectTaskService.addObservers(taskId, personId);
    }

    /**
     * The method used to deleteObservers
     **/
    @RequestMapping(value = "/{taskId}/observers/{personId}",
            method = RequestMethod.DELETE)
    public void deleteObservers(@PathVariable("projectId") Integer projectId,
                                @PathVariable("taskId") Integer taskId,
                                @PathVariable("personId") Integer personId) {
        projectTaskService.deleteObservers(taskId, personId);
    }

    /**
     * The method used to getStatusHistory from ISTaskStatusHistory
     **/
    @RequestMapping(value = "/{taskId}/statushistory",
            method = RequestMethod.GET)
    public Page<ISTaskStatusHistory> getStatusHistory(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getStatusHistory(taskId, pageable);
    }

    /**
     * The method used to getMultiplesResources from List of ISProjectTask
     **/
    @RequestMapping(value = "/multiple/[{ids}]/resources", method = RequestMethod.GET)
    public List<ISProjectTask> getMultipleResourcesByTask(@PathVariable("projectId") Integer projectId,
                                                          @PathVariable Integer[] ids) {
        return projectTaskService.findMultipleResources(Arrays.asList(ids));
    }

    /**
     * The method used for freeTextSearch of ISProjectTask
     **/
    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<ISProjectTask> freeTextSearch(@PathVariable("projectId") Integer projectId, PageRequest pageRequest, TaskCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISProjectTask> projectTasks = projectTaskService.freeTextSearch(pageable, criteria);
        return projectTasks;
    }

    /**
     * The method used for Creating  of ISTaskFiles
     **/
    @RequestMapping(value = "/{taskId}/files", method = RequestMethod.POST)
    public List<ISTaskFiles> createTaskFiles(@PathVariable("projectId") Integer projectId,
                                             @PathVariable("taskId") Integer taskId,
                                             @RequestBody List<ISTaskFiles> taskFiles) {
        return projectTaskService.createTaskFiles(taskFiles);
    }

    /**
     * The method used for get all ISTaskFiles based on taskId
     **/

    @RequestMapping(value = "/{taskId}/{attachmentType}/files", method = RequestMethod.GET)
    public List<ISTaskFiles> getTaskFiles(@PathVariable("projectId") Integer projectId,
                                          @PathVariable("taskId") Integer taskId,
                                          @PathVariable("attachmentType") AttachmentType type) {
        return projectTaskService.getTaskFiles(taskId, type);
    }

    /**
     * The method used for getTaskFiles
     **/
    @RequestMapping(value = "/{taskId}/files", method = RequestMethod.GET)
    public List<ISTaskFile> getTaskFiles(@PathVariable("projectId") Integer projectId,
                                         @PathVariable("taskId") Integer taskId) {
        return projectTaskService.findByTask(taskId);
    }

    /**
     * The method used for getLatestTaskFiles
     **/
    @RequestMapping(value = "/{taskId}/latestFiles", method = RequestMethod.GET)
    public List<ISTaskFile> getTaskLatestFiles(@PathVariable("projectId") Integer projectId,
                                               @PathVariable("taskId") Integer taskId) {
        return projectTaskService.findByTaskLatest(taskId);
    }

    @RequestMapping(value = "/{taskId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<ISTaskFile> getAllFileVersions(@PathVariable("taskId") Integer taskId,
                                               @PathVariable("fileId") Integer fileId) {
        ISTaskFile file = projectTaskService.getFile(fileId);
        return projectTaskService.getAllFileVersions(taskId, file.getName());
    }

    /**
     * The method used for upload/Create TaskFiles
     **/
    @RequestMapping(value = "/{taskId}/upload/files", method = RequestMethod.POST)
    public List<ISTaskFile> uploadTaskFiles(@PathVariable("taskId") Integer taskId,
                                            MultipartHttpServletRequest request) {
        return projectTaskService.uploadFiles(taskId, request.getFileMap());
    }

    /**
     * The method used for uploadate TaskFiles
     **/

    @RequestMapping(value = "/{taskId}/files/{fileId}", method = RequestMethod.PUT)
    public ISTaskFile updateTaskFile(@PathVariable("taskId") Integer taskId,
                                     @PathVariable("fileId") Integer fileId,
                                     @RequestBody ISTaskFile taskFile) {
        taskFile.setId(fileId);
        return projectTaskService.updateTaskFile(taskFile);
    }

    /**
     * The method used for delete TaskFile
     **/
    @RequestMapping(value = "/{taskId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteTaskFile(@PathVariable("taskId") Integer taskId,
                               @PathVariable("fileId") Integer fileId) {
        projectTaskService.deleteTaskFile(taskId, fileId);
    }

    /**
     * The method used for delete TaskAttachment (Documents and Drawings)
     **/
    @RequestMapping(value = "/attachment/{attachmentId}", method = RequestMethod.DELETE)
    public void deleteTaskAttachment(@PathVariable("attachmentId") Integer attachmentId) {
        projectTaskService.deleteTaskAttachment(attachmentId);
    }

    /**
     * The method used for getTaskFile
     **/
    @RequestMapping(value = "/{taskId}/files/{fileId}", method = RequestMethod.GET)
    public ISTaskFile getTaskFile(@PathVariable("taskId") Integer taskId,
                                  @PathVariable("fileId") Integer fileId) {
        return projectTaskService.getFile(fileId);
    }

    /**
     * The method used for download tTaskFiles
     **/
    @RequestMapping(value = "/{taskId}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadTaskFile(@PathVariable("taskId") Integer taskId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        ISTaskFile taskFile = projectTaskService.getFile(fileId);
        File file = projectTaskService.getTaskFile(taskId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, taskFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{taskId}/taskHistory", method = RequestMethod.POST)
    public ISTaskCompletionHistory createTaskHistory(@PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId, @RequestBody ISTaskCompletionHistory taskCompletionHistory) {
        return projectTaskService.createTaskHistory(projectId, taskId, taskCompletionHistory);
    }

    @RequestMapping(value = "/{taskId}/taskHistory", method = RequestMethod.GET)
    public List<ISTaskCompletionHistory> getTaskHistory(@PathVariable("taskId") Integer taskId) {
        return projectTaskService.getTaskHistory(taskId);
    }

    @RequestMapping(value = "/requiredTaskAttributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getRequiredTaskAttributes(@PathVariable("objectType") String objectType) {
        return projectTaskService.getRequiredTaskAttributes(objectType);
    }

    /**
     * The method used to searchAllTasks from the list of ISProjectTask
     **/
    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public List<ISTask> searchTasks(@PathVariable("projectId") String projectId, TaskCriteria criteria) {
        if (!projectId.equals("null")) {
            criteria.setProject(Integer.parseInt(projectId));
        }
        return projectTaskService.searchTasks(criteria);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public List<TaskReportDTO> getTaskReportRecords(ReportCriteria criteria) {
        return projectTaskService.getReportByDates(criteria.getId(), criteria.getFromDate(), criteria.getToDate());
    }

    @RequestMapping(value = "/{fileType}/report", method = RequestMethod.GET,
            produces = "text/html")
    public String getTaskReport(@PathVariable("projectId") Integer projectId, @PathVariable("fileType") String fileType, ReportCriteria criteria,
                                HttpServletResponse response) {
        return projectTaskService.exportTaskReport(projectId, fileType, response, criteria);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISTask> getTasksByIds(@PathVariable Integer[] ids) {
        return projectTaskService.getTasksByIds(Arrays.asList(ids));
    }

    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public List<ISProjectTask> getContractTasks(@PathVariable("projectId") Integer projectId) {
        return projectTaskService.getContractTasks(projectId);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public TaskStatusCountDTO getTasksCount(@PathVariable("projectId") Integer projectId) {
        return projectTaskService.getTasksCount(projectId);
    }

    @RequestMapping(value = "/{taskId}/addManpower/pageable", method = RequestMethod.GET)
    public void getPersonsToAddToTask(@PathVariable("projectId") Integer projectId,
                                      @PathVariable("taskId") Integer taskId,
                                      PageRequest pageRequest) {
    }

    @RequestMapping(value = "/{taskId}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("taskId") Integer taskId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        ISTaskFile taskFile = projectTaskService.getFile(fileId);
        String fileName = taskFile.getName();
        File file = projectTaskService.getTaskFile(taskId, fileId);
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

    @RequestMapping(value = "{taskId}/count", method = RequestMethod.GET)
    public DetailsCountDto getTaskDetailsCount(@PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId) {
        return projectTaskService.getTaskDetailsCount(projectId, taskId);
    }

    @RequestMapping(value = "/{taskId}/attachWorkflow/{wfDefId}")
    public ISWorkflow attachWorkflow(@PathVariable("taskId") Integer taskId, @PathVariable("wfDefId") Integer wfDefId) {
        return projectTaskService.attachWorkflow(taskId, wfDefId);
    }

    @RequestMapping(value = "/{taskId}/workflow/start")
    public ISWorkflow startWorkflow(@PathVariable("taskId") Integer taskId) {
        ISTask isTask = projectTaskService.get(taskId);
        return projectTaskService.startWorkflow(isTask.getWorkflow());
    }

    @RequestMapping(value = "/{taskId}/workflow/promote")
    public Boolean promoteWorkflow(@PathVariable("taskId") Integer taskId,
                                   @RequestParam("fromStatus") Integer fromStatus,
                                   @RequestParam("toStatus") Integer toStatus) {
        ISTask isTask = projectTaskService.get(taskId);
        ISWorkflow workflow = isWorkflowRepository.findOne(isTask.getWorkflow());
        /*ISWorkflow workflow = isWorkflowRepository.findByAttachedTo(taskId);*/
        ISWorkflowStatus status = isWorkflowService.getWorkflowStatus(workflow.getCurrentStatus());
        isTask.setWfStatus(status.getName());
        isTask = taskRepository.save(isTask);
        return isWorkflowService.promoteWorkflow(fromStatus, toStatus);
    }

    @RequestMapping(value = "/{taskId}/workflow/finish")
    public ISWorkflow finishWorkflow(@PathVariable("taskId") Integer taskId) {
        ISTask isTask = projectTaskService.get(taskId);
        ISWorkflow workflow = isWorkflowRepository.findOne(isTask.getWorkflow());
        /*ISWorkflow workflow = isWorkflowRepository.findByAttachedTo(taskId);*/
        ISWorkflowStatus status = isWorkflowService.getWorkflowStatus(workflow.getCurrentStatus());
        workflow = isWorkflowService.finishWorkflow(workflow.getId());
        isTask.setWfStatus(status.getName());
        isTask = taskRepository.save(isTask);
        return workflow;
    }

    @RequestMapping(value = "/{taskId}/workflow/statuses/{statusId}/approvers", method = RequestMethod.POST)
    public List<ISWorkFlowStatusApprover> addApprovers(@PathVariable("statusId") Integer statusId,
                                                       @RequestBody List<ISWorkFlowStatusApprover> approvers) {
        return isWorkflowService.addApprovers(statusId, approvers);
    }

    @RequestMapping(value = "/{taskId}/workflow/statuses/{statusId}/approvers", method = RequestMethod.GET)
    public List<ISWorkFlowStatusApprover> getApprovers(@PathVariable("statusId") Integer statusId) {
        return isWorkflowService.getApprovers(statusId);
    }

    @RequestMapping(value = "/{taskId}/workflow/history")
    public List<ISWorkflowStatusHistory> getWorkflowHistory(@PathVariable("taskId") Integer taskId) {
        ISTask isTask = projectTaskService.get(taskId);
        return isWorkflowService.getWorkflowHistory(isTask.getWorkflow());
    }

    @RequestMapping(value = "/{taskId}/resources")
    public ProjectTaskResourcesDto getProjectResources(@PathVariable("taskId") Integer taskId) {
        return projectTaskService.getProjectResources(taskId);
    }

    @RequestMapping(value = "/{task}/resources/{taskHistory}/object", method = RequestMethod.POST)
    public List<ISTaskCompletionResource> createResource(@PathVariable("task") Integer task,
                                                         @PathVariable("taskHistory") Integer taskHistory,
                                                         @RequestBody List<ISTaskCompletionResource> resources) {
        return projectTaskService.createTaskResources(taskHistory, resources);
    }

    @RequestMapping(value = "/{taskId}/taskResources/{historyId}")
    public List<ISTaskCompletionResource> getProjectTaskResources(@PathVariable("taskId") Integer taskId,
                                                                  @PathVariable("historyId") Integer historyId) {
        return projectTaskService.getProjectTaskResources(historyId);
    }

    @RequestMapping(value = "/{taskId}/problems")
    public List<TaskProblemDto> getTaskProblems(@PathVariable("taskId") Integer taskId) {
        return projectTaskService.getTaskProblems(taskId);
    }
}
