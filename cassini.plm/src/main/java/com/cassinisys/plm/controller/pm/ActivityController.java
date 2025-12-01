package com.cassinisys.plm.controller.pm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.wfm.ActivityRepository;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.ItemCriteria;
import com.cassinisys.plm.filtering.ItemPredicateBuilder;
import com.cassinisys.plm.filtering.ProjectDeliverableCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.rm.PLMGlossary;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.service.pm.ActivityService;
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
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by swapna on 1/3/18.
 */
@RestController
@RequestMapping("/plm/projects/wbs/activities")
@Api(tags = "PLM.PM", description = "Project Related")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ItemPredicateBuilder itemPredicateBuilder;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PLMWorkflowService workflowService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMActivity createActivity(@RequestBody PLMActivity plmActivity) {
        return activityService.createActivity(plmActivity);
    }

    @RequestMapping(value = "/activityList/{projectId}", method = RequestMethod.POST)
    public List<PLMActivity> createActivities(@PathVariable("projectId") Integer projectId, @RequestBody List<PLMActivity> plmActivityList) throws JsonProcessingException {
        return activityService.createActivities(projectId, plmActivityList);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.GET)
    public PLMActivity getActivity(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivity(activityId);
    }

    @RequestMapping(value = "/{activityId}/percentComplete", method = RequestMethod.GET)
    public PLMActivity getActivityPercentComplete(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivityPercentComplete(activityId);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.PUT)
    public PLMActivity updateActivity(@PathVariable("activityId") Integer activityId, @RequestBody PLMActivity plmActivity) {
        return activityService.updateActivity(plmActivity);
    }

    @RequestMapping(value = "/{activityId}", method = RequestMethod.DELETE)
    public void deleteActivity(@PathVariable("activityId") Integer activityId) throws JsonProcessingException {
        activityService.deleteActivity(activityId);
    }

    @RequestMapping(value = "/{activityId}/files", method = RequestMethod.GET)
    public List<PLMActivityFile> getActivityFiles(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivityFiles(activityId);
    }

    @RequestMapping(value = "/{activityId}/files", method = RequestMethod.POST)
    public List<PLMActivityFile> uploadActivityFiles(@PathVariable("activityId") Integer activityId, @PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException {
        return activityService.uploadActivityFiles(activityId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateTaskFile(@PathVariable("activityId") Integer activityId,
                                  @PathVariable("taskId") Integer taskId,
                                  @PathVariable("fileId") Integer fileId,
                                  @RequestBody PLMTaskFile file) {
        return activityService.updateTaskFile(fileId, file);
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateActivityFile(@PathVariable("activityId") Integer activityId,
                                      @PathVariable("fileId") Integer fileId,
                                      @RequestBody PLMActivityFile file) {
        return activityService.updateFile(fileId, file);
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteActivityFile(@PathVariable("activityId") Integer activityId, @PathVariable("fileId") Integer fileId) {
        activityService.deleteActivityFile(activityId, fileId);
    }

    @RequestMapping(value = "/{activityId}/deliverables", method = RequestMethod.POST)
    public PLMActivityDeliverable createActivityDeliverable(@PathVariable("activityId") Integer activityId, @RequestBody PLMActivityDeliverable activityDeliverable) {
        return activityService.createActivityDeliverable(activityDeliverable);
    }

    @RequestMapping(value = "/{activityId}/deliverables/{deliverableId}", method = RequestMethod.GET)
    public PLMActivityDeliverable getActivityDeliverable(@PathVariable("activityId") Integer activityId, @PathVariable("deliverableId") Integer deliverableId) {
        return activityService.getActivityDeliverable(deliverableId);
    }

    @RequestMapping(value = "/{activityId}/deliverables/{deliverableId}", method = RequestMethod.PUT)
    public PLMActivityDeliverable updateActivityDeliverable(@PathVariable("activityId") Integer activityId, PLMActivityDeliverable activityDeliverable) {
        return activityService.updateActivityDeliverable(activityDeliverable);
    }

    @RequestMapping(value = "/{activityId}/deliverables/{deliverableId}", method = RequestMethod.DELETE)
    public void deleteActivityDeliverable(@PathVariable("activityId") Integer activityId, @PathVariable("deliverableId") Integer deliverableId) {
        activityService.deleteActivityDeliverable(deliverableId);
    }

    @RequestMapping(value = "/{activityId}/deliverables", method = RequestMethod.GET)
    public List<PLMDeliverable> getActivtiyDeliverables(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivtiyDeliverables(activityId);
    }

    @RequestMapping(value = "/{activityId}/allDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getAllActivtiyDeliverables(@PathVariable("activityId") Integer activityId) {
        return activityService.getAllActivtiyDeliverables(activityId);
    }

    @RequestMapping(value = "/{activityId}/specDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getActivitySpecDeliverables(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivitySpecDeliverables(activityId);
    }

    @RequestMapping(value = "/{activityId}/reqDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getActivityReqDeliverables(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivityReqDeliverables(activityId);
    }

    @RequestMapping(value = "/items/filter", method = RequestMethod.GET)
    Page<PLMItem> getFilteredItems(PageRequest pageRequest, ProjectDeliverableCriteria deliverableCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getFilteredItems(pageable, deliverableCriteria);
    }

    @RequestMapping(value = "/glossary/filter", method = RequestMethod.GET)
    Page<PLMGlossary> getFilteredGlossaries(PageRequest pageRequest, ProjectDeliverableCriteria deliverableCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getFilteredGlossaries(pageable, deliverableCriteria);
    }

    @RequestMapping(value = "/deliverables/activity/{activityId}", method = RequestMethod.GET)
    public Page<PLMItem> getDeliveablesByActivity(@PathVariable("activityId") Integer activityId, PageRequest pageRequest) {
        return activityService.getDeliveablesByActivity(activityId, pageRequest);
    }

    @RequestMapping(value = "/deliverables/task/{taskId}", method = RequestMethod.GET)
    public Page<PLMItem> getDeliveablesByTask(@PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        return activityService.getDeliveablesByTask(taskId, pageRequest);
    }

    @RequestMapping(value = "/{activityId}/deliverables/multiple", method = RequestMethod.POST)
    public List<PLMActivityDeliverable> createActivityDeliverables(@PathVariable("activityId") Integer activityId,
                                                                   @RequestBody List<PLMActivityDeliverable> activityDeliverables) {
        return activityService.createActivityDeliverables(activityId, activityDeliverables);
    }

    @RequestMapping(value = "/{activityId}/glossaryDeliverables/multiple", method = RequestMethod.POST)
    public List<PLMGlossaryDeliverable> createActivityGlossaryDeliverables(@PathVariable("activityId") Integer activityId,
                                                                           @RequestBody List<PLMGlossaryDeliverable> activityDeliverables) {
        return activityService.createActivityGlossaryDeliverables(activityId, activityDeliverables);
    }

    @RequestMapping(value = "/{activityId}/tasks", method = RequestMethod.POST)
    public PLMTask createActivityTask(@PathVariable("activityId") Integer activityId, @RequestBody PLMTask activityTask) {
        return activityService.createActivityTask(activityTask);
    }

    @RequestMapping(value = "/{activityId}/taskList/{projectId}", method = RequestMethod.POST)
    public List<PLMTask> createActivityTasks(@PathVariable("projectId") Integer projectId, @PathVariable("activityId") Integer activityId, @RequestBody List<PLMTask> activityTaskList) throws JsonProcessingException {
        return activityService.createActivityTasks(projectId, activityTaskList);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}", method = RequestMethod.GET)
    public PLMTask getActivityTaskById(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId) {
        return activityService.getActivityTask(taskId, activityId);
    }

    @RequestMapping(value = "/{activityId}/tasks/byName", method = RequestMethod.GET)
    public PLMTask getActivityTaskByName(@PathVariable("activityId") Integer activityId, @RequestParam("taskName") String taskName) {
        return activityService.getActivityTaskByName(activityId, taskName);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}", method = RequestMethod.PUT)
    public PLMTask updateActivityTask(@PathVariable("activityId") Integer activityId, @RequestBody PLMTask task) throws JsonProcessingException {
        return activityService.updateActivityTask(task);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}", method = RequestMethod.DELETE)
    public void deleteActivityTask(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId) throws JsonProcessingException {
        activityService.deleteActivityTask(taskId);
    }

    @RequestMapping(value = "/{activityId}/tasks", method = RequestMethod.GET)
    public List<PLMTask> getActivityTasks(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivityTasks(activityId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files", method = RequestMethod.GET)
    public List<PLMTaskFile> getTaskFiles(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId) {
        return activityService.getTaskFiles(taskId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files", method = RequestMethod.POST)
    public List<PLMTaskFile> uploadTaskFiles(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId,
                                             @RequestParam("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException {
        return activityService.uploadTaskFiles(taskId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteTaskFile(@PathVariable("taskId") Integer taskId, @PathVariable("activityId") Integer activityId, @PathVariable("fileId") Integer fileId) {
        activityService.deleteTaskFile(taskId, fileId);
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}/download/{type}", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("activityId") Integer activityId,
                                 @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                                 HttpServletResponse response) {
        PLMActivityFile activityFile = activityService.getActivityFile(fileId);
        File file = activityService.getActivityFile(activityId, fileId, type);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, activityFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}/preview/{type}", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("activityId") Integer activityId,
                                @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                                HttpServletResponse response) throws Exception {
        PLMActivityFile activityFile = activityService.getActivityFile(fileId);
        String fileName = activityFile.getName();
        File file = activityService.getActivityFile(activityId, fileId, type);
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

    @RequestMapping(value = "/tasks/{taskId}/files/{fileId}/download/{type}", method = RequestMethod.GET)
    public void downloadTaskFile(@PathVariable("taskId") Integer taskId,
                                 @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                                 HttpServletResponse response) {
        PLMTaskFile taskFile = activityService.getTaskFile(fileId);
        File file = activityService.getActivityTaskFile(taskId, fileId, type);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, taskFile.getName(), file);
        }
    }

    @RequestMapping(value = "/tasks/{taskId}/files/{fileId}/preview/{type}", method = RequestMethod.GET)
    public void previewFile(@PathVariable("taskId") Integer taskId,
                            @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                            HttpServletResponse response) throws Exception {
        PLMTaskFile taskFile = activityService.getTaskFile(fileId);
        String fileName = taskFile.getName();
        File file = activityService.getActivityTaskFile(taskId, fileId, type);
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

    @RequestMapping(value = "/{activityId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMActivityFile> getFileVersions(@PathVariable("activityId") Integer activityId,
                                                 @PathVariable("fileId") Integer fileId) {
        return activityService.getFileVersions(activityId, fileId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMTaskFile> getTaskFileVersions(@PathVariable("activityId") Integer activityId,
                                                 @PathVariable("taskId") Integer taskId,
                                                 @PathVariable("fileId") Integer fileId) {
        return activityService.getTaskFileVersions(activityId, taskId, fileId);
    }

    @RequestMapping(value = "/activityStructure/{personId}", method = RequestMethod.GET)
    public Page<PLMActivity> getActivityStructure(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Page<PLMActivity> plmActivityList = null;
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        if (personId != null) {
            plmActivityList = activityService.getActivityStructure(personId, pageable);
        }
        return plmActivityList;
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("activityId") Integer activityId,
                                                      @PathVariable("fileId") Integer fileId) {
        return activityService.fileDownloadHistory(activityId, fileId);
    }

    @RequestMapping(value = "/tasks/{taskId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMTaskFile getLatestUploadedTaskFile(@PathVariable("taskId") Integer taskId, @PathVariable("fileId") Integer fileId) {
        return activityService.getLatestUploadedTaskFile(taskId, fileId);
    }

    @RequestMapping(value = "/{taskId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMActivityFile getLatestUploadedFile(@PathVariable("taskId") Integer taskId, @PathVariable("fileId") Integer fileId) {
        return activityService.getLatestUploadedFile(taskId, fileId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory taskFileDownloadHistory(@PathVariable("activityId") Integer activityId,
                                                          @PathVariable("taskId") Integer taskId,
                                                          @PathVariable("fileId") Integer fileId) {
        return activityService.taskFileDownloadHistory(activityId, taskId, fileId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/deliverables/multiple", method = RequestMethod.POST)
    public List<PLMTaskDeliverable> createTaskDeliverables(@PathVariable("activityId") Integer activityId,
                                                           @PathVariable("taskId") Integer taskId,
                                                           @RequestBody List<PLMTaskDeliverable> taskDeliverables) {
        return activityService.createTaskDeliverables(activityId, taskId, taskDeliverables);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/deliverables", method = RequestMethod.GET)
    public List<PLMTaskDeliverable> getTaskDeliverables(@PathVariable("activityId") Integer activityId,
                                                        @PathVariable("taskId") Integer taskId) {
        return activityService.getTaskDeliverables(activityId, taskId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/allDeliverables", method = RequestMethod.GET)
    public PLMProjectDeliverableDto getAllTaskDeliverables(@PathVariable("activityId") Integer activityId,
                                                           @PathVariable("taskId") Integer taskId) {
        return activityService.getAllTaskDeliverables(activityId, taskId);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/deliverables/{deliverableId}", method = RequestMethod.DELETE)
    public void deleteTaskDeliverable(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId, @PathVariable("deliverableId") Integer deliverableId) {
        activityService.deleteTaskDeliverable(taskId, deliverableId);
    }

    @RequestMapping(value = "/{activityId}/referenceItems", method = RequestMethod.GET)
    public List<PLMActivityItemReference> getItemsByProject(@PathVariable("activityId") Integer activityId) {
        return activityService.getItemsByActivity(activityId);
    }

    @RequestMapping(value = "/{activityId}/referenceItems/{referenceItem}", method = RequestMethod.DELETE)
    public void deleteActivityReferenceItem(@PathVariable("activityId") Integer activityId, @PathVariable("referenceItem") Integer referenceItem) {
        activityService.deleteActivityReferenceItem(referenceItem);
    }

    @RequestMapping(value = "/{activityId}/referenceItems/multiple", method = RequestMethod.POST)
    public List<PLMActivityItemReference> createActivityItemReferences(@PathVariable("activityId") Integer activityId,
                                                                       @RequestBody List<PLMActivityItemReference> activityItemReferences) {
        return activityService.createActivityItemReferences(activityId, activityItemReferences);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/referenceItems/multiple", method = RequestMethod.POST)
    public List<PLMTaskItemReference> createTaskItemReferences(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId,
                                                               @RequestBody List<PLMTaskItemReference> taskItemReferences) {
        return activityService.createTaskItemReferences(taskItemReferences);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/referenceItems/{referenceItem}", method = RequestMethod.DELETE)
    public void deleteTaskReferenceItem(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId,
                                        @PathVariable("referenceItem") Integer referenceItem) {
        activityService.deleteTaskReferenceItem(referenceItem);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/referenceItems", method = RequestMethod.GET)
    public List<PLMTaskItemReference> getItemsByTask(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId) {
        return activityService.getItemsByTask(activityId, taskId);
    }

    @RequestMapping(value = "/items/{activityId}", method = RequestMethod.GET)
    public Page<PLMItem> getActivityItems(@PathVariable("activityId") Integer activityId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getActivityItems(activityId, pageable);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/items", method = RequestMethod.GET)
    public Page<PLMItem> getTaskItems(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getTaskItems(taskId, pageable);
    }

    @RequestMapping(value = "/deliverables/byActivity/{activityId}", method = RequestMethod.GET)
    public Page<PLMItem> searchActivityDeliverables(@PathVariable("activityId") Integer activityId, ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = itemPredicateBuilder.build(criteria,
                QPLMItem.pLMItem);
        Page<PLMItem> plmItems = activityService.searchActivityDeliverables(activityId, predicate, pageRequest);
        return plmItems;
    }

    @RequestMapping(value = "/deliverables/byTask/{taskId}", method = RequestMethod.GET)
    public Page<PLMItem> searchTaskDeliverables(@PathVariable("taskId") Integer taskId, ItemCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = itemPredicateBuilder.build(criteria,
                QPLMItem.pLMItem);
        Page<PLMItem> plmItems = activityService.searchTaskDeliverables(taskId, predicate, pageRequest);
        return plmItems;
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    public List<PLMActivityFile> getActivityFileVersionAndCommentsAndDownloads(@PathVariable("activityId") Integer activityId,
                                                                               @PathVariable("fileId") Integer fileId, @PathVariable("objectType") ObjectType objectType) {
        return activityService.getActivityFileVersionAndCommentsAndDownloads(activityId, fileId, objectType);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    public List<PLMTaskFile> getTaskFileVersionAndCommentsAndDownloads(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId,
                                                                       @PathVariable("fileId") Integer fileId, @PathVariable("objectType") ObjectType objectType) {
        return activityService.getTaskFileVersionAndCommentsAndDownloads(activityId, taskId, fileId, objectType);
    }

    @RequestMapping(value = "/{activityId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMActivityFile renameActivityFile(@PathVariable("activityId") Integer id,
                                              @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return activityService.renameActivityFile(id, fileId, newFileName);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMTaskFile renameTaskFile(@PathVariable("activityId") Integer id,
                                      @PathVariable("taskId") Integer taskId,
                                      @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return activityService.renameTaskFile(taskId, fileId, newFileName);
    }

    @RequestMapping(value = "{activityId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMActivityFile> replaceActivityFiles(@PathVariable("activityId") Integer activityId,
                                                      @PathVariable("fileId") Integer fileId,
                                                      MultipartHttpServletRequest request) {
        return activityService.replaceActivityFiles(activityId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMTaskFile> replaceTaskFiles(@PathVariable("taskId") Integer taskId,
                                              @PathVariable("fileId") Integer fileId,
                                              MultipartHttpServletRequest request) {
        return activityService.replaceTaskFiles(taskId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/personActivity/{personId}", method = RequestMethod.GET)
    public Page<PLMActivity> getPersonActivitys(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getPersonActivitys(personId, pageable);
    }

    @RequestMapping(value = "/personActivityTask/{personId}", method = RequestMethod.GET)
    public Page<PLMTask> getPersonActivityTasks(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getPersonActivityTasks(personId, pageable);
    }

    @RequestMapping(value = "file/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateItemFile(@PathVariable("fileId") Integer fileId,
                                  @RequestBody PLMActivityFile file) {
        return activityService.updateFile(fileId, file);
    }

    @RequestMapping(value = "/{activityId}/folder", method = RequestMethod.POST)
    public PLMActivityFile createActivityFolder(@PathVariable("activityId") Integer activityId, @RequestBody PLMActivityFile plmActivityFile) {
        return activityService.createActivityFolder(activityId, plmActivityFile);
    }

    @RequestMapping(value = "/{activityId}/folder/{folderId}/upload", method = RequestMethod.POST)
    public List<PLMActivityFile> uploadActivityFolderFiles(@PathVariable("activityId") Integer activityId, @PathVariable("folderId") Integer folderId,
                                                           MultipartHttpServletRequest request) {
        return activityService.uploadActivityFolderFiles(activityId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{folderId}/move", method = RequestMethod.PUT)
    public PLMFile moveActivityFileToFolder(@PathVariable("folderId") Integer folderId,
                                            @RequestBody PLMActivityFile file) throws Exception {
        return activityService.moveActivityFileToFolder(folderId, file);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PLMActivityFile> getActivityFolderChildren(@PathVariable("folderId") Integer folderId) {
        return activityService.getActivityFolderChildren(folderId);
    }

    @RequestMapping(value = "/{activityId}/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("activityId") Integer activityId, @PathVariable("fileId") Integer fileId) {
        activityService.deleteFolder(activityId, fileId);
    }

    /*------------------------------------------  TASK ----------------------------------------------*/

    @RequestMapping(value = "/tasks/{taskId}/folder/{folderId}/delete", method = RequestMethod.DELETE)
    public void deleteActivityTaskFolder(@PathVariable("taskId") Integer taskId, @PathVariable("folderId") Integer folderId) {
        activityService.deleteActivityTaskFolder(taskId, folderId);
    }

    @RequestMapping(value = "/tasks/{taskId}/folder", method = RequestMethod.POST)
    public PLMTaskFile createActivityTaskFolder(@PathVariable("taskId") Integer taskId, @RequestBody PLMTaskFile folder) {
        return activityService.createActivityTaskFolder(taskId, folder);
    }

    @RequestMapping(value = "/{activityId}/tasks/{taskId}/folder/{folderId}/upload", method = RequestMethod.POST)
    public List<PLMTaskFile> uploadActivityTaskFolderFiles(@PathVariable("activityId") Integer activityId, @PathVariable("taskId") Integer taskId,
                                                           @PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) {
        return activityService.uploadActivityTaskFolderFiles(activityId, taskId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/tasks/{folderId}/move", method = RequestMethod.PUT)
    public PLMFile moveActivityTaskFileToFolder(@PathVariable("folderId") Integer folderId,
                                                @RequestBody PLMTaskFile file) throws Exception {
        return activityService.moveActivityTaskFileToFolder(folderId, file);
    }

    @RequestMapping(value = "/tasks/{folderId}/children", method = RequestMethod.GET)
    public List<PLMTaskFile> getActivityTaskFolderChildren(@PathVariable("folderId") Integer folderId) {
        return activityService.getActivityTaskFolderChildren(folderId);
    }

    @RequestMapping(value = "/{activityId}/details", method = RequestMethod.GET)
    public DetailsCount getActivityDetailsCount(@PathVariable("activityId") Integer projectId) {
        return activityService.getActivityDetailsCount(projectId);
    }

    @RequestMapping(value = "/tasks/{taskId}/details", method = RequestMethod.GET)
    public DetailsCount getTaskDetailsCount(@PathVariable("taskId") Integer taskId) {
        return activityService.getTaskDetailsCount(taskId);
    }

    @RequestMapping(value = "/tasks/{personId}", method = RequestMethod.GET)
    public Page<PLMTask> getAssignedTasks(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return activityService.getAssignedTasks(personId, pageable);
    }

    @RequestMapping(value = "{activityId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("activityId") Integer activityId, HttpServletResponse response) throws IOException {
        activityService.generateZipFile(activityId, response, "ACTIVITY");
    }

    @RequestMapping(value = "/tasks/{taskId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateTaskZip(@PathVariable("taskId") Integer taskId, HttpServletResponse response) throws IOException {
        activityService.generateZipFile(taskId, response, "TASKS");
    }

    @RequestMapping(value = "{actualId}/{targetId}/updateSequence", method = RequestMethod.GET)
    public WBSDto updateWbsChildSeq(@PathVariable("actualId") Integer actualId, @PathVariable("targetId") Integer targetId) {
        return activityService.updateWbsChildSeq(actualId, targetId);
    }

    @RequestMapping(value = "/{activity}/tasks/{actualId}/{targetId}/updateSequence", method = RequestMethod.GET)
    public PLMTask updateActivityChildSeq(@PathVariable("activity") Integer activity, @PathVariable("actualId") Integer actualId, @PathVariable("targetId") Integer targetId) {
        return activityService.updateActivityChildSeq(activity, actualId, targetId);
    }

    @RequestMapping(value = "{activity}/files/paste", method = RequestMethod.PUT)
    public List<PLMActivityFile> pasteActivityFilesFromClipboard(@PathVariable("activity") Integer activity, @RequestParam("fileId") Integer fileId,
                                                                 @RequestBody List<PLMFile> files) {
        return activityService.pasteFromClipboardToActivity(activity, fileId, files);
    }

    @RequestMapping(value = "/tasks/{taskId}/files/paste", method = RequestMethod.PUT)
    public List<PLMTaskFile> pasteFromClipboard(@PathVariable("taskId") Integer taskId, @RequestParam("fileId") Integer fileId,
                                                @RequestBody List<PLMFile> files) {
        return activityService.pasteFromClipboardToTask(taskId, fileId, files);
    }

    @RequestMapping(value = "/{activityId}/deliverables/paste", method = RequestMethod.PUT)
    public PLMProjectDeliverableDto pasteActivityDeliverables(@PathVariable("activityId") Integer activityId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        return activityService.pasteActivityDeliverables(activityId, deliverableDto);
    }

    @RequestMapping(value = "/{activityId}/deliverables/undo", method = RequestMethod.PUT)
    public void undoActivityDeliverables(@PathVariable("activityId") Integer activityId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        activityService.undoActivityDeliverables(activityId, deliverableDto);
    }

    @RequestMapping(value = "/tasks/{taskId}/deliverables/paste", method = RequestMethod.PUT)
    public PLMProjectDeliverableDto pasteTaskDeliverables(@PathVariable("taskId") Integer taskId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        return activityService.pasteTaskDeliverables(taskId, deliverableDto);
    }

    @RequestMapping(value = "/tasks/{taskId}/deliverables/undo", method = RequestMethod.PUT)
    public void undoTaskDeliverables(@PathVariable("taskId") Integer taskId, @RequestBody PLMProjectDeliverableDto deliverableDto) {
        activityService.undoTaskDeliverables(taskId, deliverableDto);
    }

    @RequestMapping(value = "{activityId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedActivityFiles(@PathVariable("activityId") Integer activityId, @RequestBody List<PLMActivityFile> activityFiles) {
        activityService.undoCopiedActivityFiles(activityId, activityFiles);
    }

    @RequestMapping(value = "tasks/{taskId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedTaskFiles(@PathVariable("taskId") Integer taskId, @RequestBody List<PLMTaskFile> taskFiles) {
        activityService.undoCopiedTaskFiles(taskId, taskFiles);
    }

    @RequestMapping(value = "/finish/{activityId}", method = RequestMethod.POST)
    public PLMActivity finishActivity(@PathVariable("activityId") Integer activityId) throws JsonProcessingException {
        return activityService.finishActivity(activityId);
    }

    @RequestMapping(value = "/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return activityService.attachNewActivityWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/{specId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteWorkflow(@PathVariable("specId") Integer specId) {
        workflowService.deleteWorkflow(specId);
    }

    @RequestMapping(value = "/deliverable/{type}/finish", method = RequestMethod.POST)
    public PLMDeliverable finishActivityAndTaskDeliverable(@PathVariable("type") String type, @RequestBody PLMDeliverable plmDeliverable) {
        return activityService.finishActivityAndTaskDeliverable(type, plmDeliverable);
    }


    @RequestMapping(value = "/{id}/files/byName/{name}", method = RequestMethod.GET)
    public List<PLMActivityFile> getActivityFilesByName(@PathVariable("id") Integer id,
                                                        @PathVariable("name") String name) {
        return activityService.getActivityFilesByName(id, name);
    }

    @RequestMapping(value = "/task/{id}/files/byName/{name}", method = RequestMethod.GET)
    public List<PLMTaskFile> getProjectFilesByName(@PathVariable("id") Integer id,
                                                   @PathVariable("name") String name) {
        return activityService.getTaskFilesByName(id, name);
    }

    @RequestMapping(value = "/tasks/{taskId}/mobile", method = RequestMethod.GET)
    public TaskDto getTaskDetails(@PathVariable("taskId") Integer taskId) {
        return activityService.getTaskDetails(taskId);
    }

    @RequestMapping(value = "/{activityId}/mobile", method = RequestMethod.GET)
    public ActivityDto getActivityDetails(@PathVariable("activityId") Integer activityId) {
        return activityService.getActivityDetails(activityId);
    }


    @RequestMapping(value = "/task/{id}/attachWorkflow/{wfDefId}")
    public PLMWorkflow attachTaskWorkflow(@PathVariable("id") Integer id, @PathVariable("wfDefId") Integer wfDefId) {
        return activityService.attachTaskWorkflow(id, wfDefId);
    }

    @RequestMapping(value = "/task/{specId}/workflow/delete", method = RequestMethod.DELETE)
    public void deleteTaskWorkflow(@PathVariable("specId") Integer specId) {
        workflowService.deleteWorkflow(specId);
    }

}

