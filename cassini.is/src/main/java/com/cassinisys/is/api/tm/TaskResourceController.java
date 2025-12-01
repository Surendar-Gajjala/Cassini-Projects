package com.cassinisys.is.api.tm;
/**
 * The Class is for TaskResourceController
 **/

import com.cassinisys.is.filtering.AssignedTaskCriteria;
import com.cassinisys.is.model.dm.ISProjectDocument;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.ISWbs;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.model.tm.*;
import com.cassinisys.is.repo.dm.ProjectDocumentRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.pm.WbsRepository;
import com.cassinisys.is.repo.procm.BoqItemRepository;
import com.cassinisys.is.repo.tm.ProjectTaskImageRepository;
import com.cassinisys.is.repo.tm.TaskFileRepository;
import com.cassinisys.is.repo.tm.TaskFilesRepository;
import com.cassinisys.is.repo.tm.TaskRepository;
import com.cassinisys.is.service.tm.ProjectTaskService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Api(name = "Task Resources", description = "Task resources management")
@RestController
@RequestMapping("/tasks")
public class TaskResourceController extends BaseController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private BoqItemRepository boqItemRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private TaskFilesRepository filesRepository;
    @Autowired
    private ProjectDocumentRepository documentRepository;
    @Autowired
    private WbsRepository wbsRepository;
    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private ProjectTaskImageRepository projectTaskImageRepository;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ISTask> getMyTasks(@PathVariable("userId") Integer userId) {
        return taskRepository.findByPerson(userId);
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public ISTask getTaskDetails(@PathVariable("taskId") Integer taskId) {
        return taskRepository.findOne(taskId);
    }

    @RequestMapping(value = "/{taskId}/resources/{resourceType}", method = RequestMethod.GET)
    public List<ISProjectResource> getResourcesByResourceType(@PathVariable("taskId") Integer taskId, @PathVariable("resourceType") ResourceType resourceType) {
        return resourceRepository.findByTaskAndResourceType(taskId, resourceType);
    }

    // For task material tab
    @RequestMapping(value = "/boq/multiple/items/[{ids}]", method = RequestMethod.GET)
    public List<ISBoqItem> getMultipleItems(@PathVariable Integer[] ids) {
        return boqItemRepository.findByIdIn(Arrays.asList(ids));
    }

    // For task material tab
    @RequestMapping(value = "/[{referenceIds}]/available", method = RequestMethod.GET)
    public Map<Integer, Double> getItemAvailableQuantities(@PathVariable("referenceIds") List<Integer> referenceIds) {
        Map<Integer, Double> map = new HashMap<>();
        List<ISProjectResource> resources = resourceRepository.findByReferenceIdIn(referenceIds);
        for (ISProjectResource resource : resources) {
            Integer refId = resource.getReferenceId();
            Double qty = map.get(refId);
            if (qty == null) {
                map.put(refId, resource.getQuantity());
            } else {
                map.put(refId, qty + resource.getQuantity());
            }
        }
        return map;
    }

    //For task file tab
    @RequestMapping(value = "/{taskId}/latestFiles", method = RequestMethod.GET)
    public List<ISTaskFile> getTaskLatestFiles(@PathVariable("taskId") Integer taskId) {
        return taskFileRepository.findByTaskAndLatestTrueOrderByModifiedDateDesc(taskId);
    }

    @RequestMapping(value = "/{taskId}/{attachmentType}/files", method = RequestMethod.GET)
    public List<ISTaskFiles> getTaskFiles(@PathVariable("taskId") Integer taskId,
                                          @PathVariable("attachmentType") AttachmentType type) {
        return filesRepository.findByTaskAndType(taskId, type);
    }

    @RequestMapping(value = "/multiple/documents/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectDocument> getMultipleDocuments(@PathVariable Integer[] ids) {
        return documentRepository.findByIdIn(Arrays.asList(ids));
    }

    @RequestMapping(value = "/appWbs/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISWbs> getMultiple(@PathVariable Integer[] ids) {
        return wbsRepository.findByIdIn(Arrays.asList(ids));
    }

    @RequestMapping(value = "/update/{taskId}", method = RequestMethod.PUT)
    public ISProjectTask update(@PathVariable("taskId") Integer taskId,
                                @RequestBody ISProjectTask projectTask) {
        projectTask.setId(taskId);
        return projectTaskService.updateTask(projectTask);
    }

    /*----------------------------------  For Mobile App --------------------------*/

    @RequestMapping(value = "/details/{taskId}", method = RequestMethod.GET)
    public AssignedToTaskDto getSelectedTaskDetails(@PathVariable("taskId") Integer taskId) {
        return projectTaskService.getTaskDetails(taskId);
    }

    @RequestMapping(value = "/assignedToTasksByPerson", method = RequestMethod.GET)
    public Page<AssignedToTaskDto> getAssignedToTasksByPerson(PageRequest pageRequest, AssignedTaskCriteria assignedTaskCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getAssignedToTasksByPerson(pageable, assignedTaskCriteria);
    }

    @RequestMapping(value = "/assignedByTasksByPerson/{personId}", method = RequestMethod.GET)
    public Page<AssignedToTaskDto> getAssignedByTasksByPerson(@PathVariable("personId") Integer personId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectTaskService.getAssignedByTasksByPerson(personId, pageable);
    }

    @Transactional(readOnly = false)
    @RequestMapping(value = "/taskPhotos/{taskId}", method = RequestMethod.POST)
    public List<ISProjectTaskImage> uploadPicture(@PathVariable("taskId") Integer taskId, MultipartHttpServletRequest request) {
        Map<String, MultipartFile> map = request.getFileMap();
        List<ISProjectTaskImage> photos = new ArrayList();
        List<MultipartFile> files = new ArrayList(map.values());
        if (files.size() > 0) {
            for (MultipartFile file : files) {
                try {
                    String fileName = file.getOriginalFilename();
                    if (projectTaskImageRepository.findByTaskAndImageName(taskId, fileName) == null) {
                        ISProjectTaskImage photo = new ISProjectTaskImage();
                        photo.setImage(file.getBytes());
                        photo.setImageName(file.getOriginalFilename());
                        photo.setTask(taskId);
                        photo.setImageSize(file.getSize());
                        photo.setUploadDate(new Date());
                        photo = projectTaskImageRepository.save(photo);
                        photos.add(photo);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return photos;
    }

    @RequestMapping(value = "/{taskId}/taskPhotos", method = RequestMethod.GET)
    public List<ISProjectTaskImage> getPhotosByAlbum(@PathVariable("taskId") Integer taskId) {
        return projectTaskImageRepository.findByTask(taskId);
    }

    @RequestMapping(path = "/{taskId}/photo/{photoId}/download", method = RequestMethod.GET)
    public void downloadPicture(@PathVariable("taskId") Integer taskId,
                                @PathVariable("photoId") Integer photoId,
                                HttpServletResponse response) {
        ISProjectTaskImage photo = projectTaskImageRepository.findOne(photoId);
        if (photo != null) {
            String file = photo.getImageName();
            String s = file.trim().toLowerCase();
            String picType = null;
            if (s.endsWith(".jpg") || s.endsWith(".jpeg")) {
                picType = MediaType.IMAGE_JPEG_VALUE;
            } else if (s.endsWith(".gif")) {
                picType = MediaType.IMAGE_GIF_VALUE;
            } else {
                picType = MediaType.IMAGE_PNG_VALUE;
            }
            response.setContentType(picType);
            response.setContentLength(photo.getImageSize().intValue());
            InputStream is = new ByteArrayInputStream(photo.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
