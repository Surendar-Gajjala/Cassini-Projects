package com.cassinisys.is.service.tm;

import com.cassinisys.is.filtering.*;
import com.cassinisys.is.model.col.ISProjectUserInbox;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.*;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.tm.*;
import com.cassinisys.is.model.workflow.*;
import com.cassinisys.is.repo.col.ProjectUserInboxRepository;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.pm.*;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.tm.*;
import com.cassinisys.is.repo.workflow.ISWorkflowRepository;
import com.cassinisys.is.repo.workflow.ISWorkflowStartRepository;
import com.cassinisys.is.service.pm.ProjectService;
import com.cassinisys.is.service.pm.WbsService;
import com.cassinisys.is.service.workflow.ISWorkflowDefinitionService;
import com.cassinisys.is.service.workflow.ISWorkflowService;
import com.cassinisys.is.service.workflow.ISWorkflowStatusService;
import com.cassinisys.is.service.workflow.ISWorkflowTransitionService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class is for ProjectTaskService
 **/
@Service
@Transactional
public class ProjectTaskService extends TaskService {

    @Autowired
    WbsService wbsService;
    @Autowired
    WbsRepository wbsRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    IssueRepository issueRepository;

    List<TaskReportDTO> reportDTOs = new ArrayList();
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskStatusHistoryRepository taskStatusHistoryRepository;
    @Autowired
    private ProjectUserInboxRepository projectUserInboxRepository;
    @Autowired
    private TaskAssignedToRepository assignedToRepository;
    @Autowired
    private TaskPredicateBuilder taskPredicateBuilder;
    @Autowired
    private TaskFilesRepository filesRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ISTaskCompletionHistoryRepository taskCompletionHistoryRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ExportService exportService;
    @Autowired
    private ProjectSiteRepository projectSiteRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AssignedTaskPredicateBuilder assignedTaskPredicateBuilder;
    @Autowired
    private WorkOrderItemRepository workOrderItemRepository;
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private ForgeService forgeService;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private ISWorkflowDefinitionService workflowDefinitionService;

    @Autowired
    private ISWorkflowService workflowService;

    @Autowired
    private ISWorkflowRepository isWorkflowRepository;

    @Autowired
    private ISWorkflowTransitionService isWorkflowTransitionService;

    @Autowired
    private ISWorkflowStatusService isWorkflowStatusService;

    @Autowired
    private ISWorkflowStartRepository isWorkflowStartRepository;

    @Autowired
    private ISWorkflowService isWorkflowService;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private BoqItemRepository boqItemRepository;

    @Autowired
    private MachineItemRepository machineItemRepository;

    @Autowired
    private MaterialItemRepository materialItemRepository;

    @Autowired
    private ManpowerItemRepository manpowerItemRepository;

    @Autowired
    private ISProjectRoleRepository projectRoleRepository;

    @Autowired
    private TaskCompletionResourceRepository taskCompletionResourceRepository;

    @Autowired
    private ProjectWbsRepository projectWbsRepository;

    @Transactional(readOnly = true)
    public List<ISTaskFile> findByTask(Integer task) {
        return taskFileRepository.findByTask(task);
    }

    @Transactional(readOnly = true)
    public List<ISTaskFile> findByTaskLatest(Integer task) {
        List<ISTaskFile> taskFiles = taskFileRepository.findByTaskAndLatestTrueOrderByCreatedDateDesc(task);
        taskFiles.forEach(isTaskFile -> {
            isTaskFile.setUploadedBy(personRepository.findOne(isTaskFile.getCreatedBy()).getFullName());
        });
        return taskFiles;
    }

    @Transactional(readOnly = false)
    public List<ISTaskFile> uploadFiles(Integer taskId, Map<String, MultipartFile> fileMap) {
        List<ISTaskFile> uploadedFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        for (MultipartFile file : fileMap.values()) {
            String name = file.getOriginalFilename();
            ISTaskFile taskFile = taskFileRepository.findByTaskAndNameAndLatestTrue(taskId, name);
            Integer version = 1;
            if (taskFile != null) {
                if (!taskFile.getLocked()) {
                    taskFile.setLatest(false);
                    Integer oldVersion = taskFile.getVersion();
                    version = oldVersion + 1;
                    taskFileRepository.save(taskFile);
                } else {
                    throw new CassiniException("Locked Files cannot be updated");
                }

            }
            taskFile = new ISTaskFile();
            taskFile.setName(file.getOriginalFilename());
            taskFile.setCreatedBy(login.getPerson().getId());
            taskFile.setModifiedBy(login.getPerson().getId());
            taskFile.setVersion(version);
            taskFile.setTask(taskId);
            taskFile.setSize(file.getSize());
            taskFile = taskFileRepository.save(taskFile);
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + taskId;
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            String path = dir + File.separator + taskFile.getId();
            fileSystemService.saveDocumentToDisk(file, path);
            Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
            if (map != null) {
                taskFile.setUrn(map.get("urn"));
                taskFile.setThumbnail(map.get("thumbnail"));
                taskFile = taskFileRepository.save(taskFile);
            }
            uploadedFiles.add(taskFile);
        }
        return uploadedFiles;
    }

    @Transactional(readOnly = true)
    public File getTaskFile(Integer taskId, Integer fileId) {
        checkNotNull(taskId);
        checkNotNull(fileId);
        ISProjectTask isProjectTask = projectTaskRepository.findOne(taskId);
        if (taskId == null) {
            throw new ResourceNotFoundException();
        }
        ISTaskFile isTaskFile1 = taskFileRepository.findOne(fileId);
        if (isTaskFile1 == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + taskId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = false)
    public ISTaskFile createTaskFile(ISTaskFile taskFile) {
        checkNotNull(taskFile);
        taskFile.setId(null);
        taskFile = taskFileRepository.save(taskFile);
        ISProjectTask isProjectTask = projectTaskRepository.findOne(taskFile.getTask());
        return taskFile;
    }

    /**
     * The Method is used to createTaskFiles for the List of ISTaskFiles
     **/
    @Transactional(readOnly = false)
    public List<ISTaskFiles> createTaskFiles(List<ISTaskFiles> taskFiles) {
        return filesRepository.save(taskFiles);
    }

    /**
     * The Method is used to getTaskFiles for the List of ISTaskFiles
     **/
    @Transactional(readOnly = true)
    public List<ISTaskFiles> getTaskFiles(Integer taskId, AttachmentType type) {
        return filesRepository.findByTaskAndType(taskId, type);
    }

    @Transactional(readOnly = false)
    public ISTaskFile updateTaskFile(ISTaskFile taskFile) {
        checkNotNull(taskFile);
        checkNotNull(taskFile.getId());
        taskFile = taskFileRepository.save(taskFile);
        ISProjectTask isProjectTask = projectTaskRepository.findOne(taskFile.getTask());
        return taskFile;
    }

    @Transactional(readOnly = false)
    public void deleteTaskFile(Integer taskId, Integer id) {
        checkNotNull(id);
        ISTaskFile taskFile = taskFileRepository.findOne(id);
        if (taskFile == null) {
            throw new ResourceNotFoundException();
        }
        List<ISTaskFile> taskFiles = taskFileRepository.findAllByTaskAndNameOrderByCreatedDateDesc(taskId, taskFile.getName());
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + taskId;
        for (ISTaskFile taskFile1 : taskFiles) {
            fileSystemService.deleteDocumentFromDisk(taskFile1.getId(), dir);
            taskFileRepository.delete(taskFile1);
        }

    }

    @Transactional(readOnly = false)
    public void deleteTaskAttachment(Integer attachmentId) {
        checkNotNull(attachmentId);
        ISTaskFiles taskFiles = filesRepository.findOne(attachmentId);
        if (taskFiles == null) {
            throw new ResourceNotFoundException();
        }
        filesRepository.delete(attachmentId);
    }

    @Transactional(readOnly = true)
    public ISTaskFile getFile(Integer id) {
        checkNotNull(id);
        ISTaskFile taskFile = taskFileRepository.findOne(id);
        if (taskFile == null) {
            throw new ResourceNotFoundException();
        }
        return taskFile;
    }

    @Transactional(readOnly = true)
    public List<ISTaskFile> getAllTaskFiles() {
        return taskFileRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISTaskFile> getAllFileVersions(Integer taskId, String name) {
        return taskFileRepository.findAllByTaskAndNameOrderByCreatedDateDesc(taskId, name);
    }

    @Transactional(readOnly = true)
    public Page<ISTaskFile> findAllTaskFiles(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null)
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(new Sort.Order("id")));
        return taskFileRepository.findAll(pageable);
    }

    @Transactional(readOnly = false)
    public ISProjectTask submitFormData(ISProjectTask projectTask) {
        return projectTask;
    }

    @Transactional
    public void wbsPercentage(ISProjectWbs taskWbs) {
        double weightage = 0.0;
        double percent = 0;
        List<ISProjectTask> projectTasks = projectTaskRepository.findByWbsItem(taskWbs.getId());
        for (ISProjectTask task : projectTasks) {
            weightage += taskWbs.getWeightage();
            percent += (task.getPercentComplete() * taskWbs.getWeightage()) / 100;
        }
        if (percent > 0) {
            percent = (percent * 100) / weightage;
        }
        percent = Math.round(percent);
        taskWbs.setPercentageComplete(percent);
        wbsRepository.save(taskWbs);
        if (taskWbs.getParent() != null) {
            visitWbsParent(taskWbs.getParent());
        }
    }

    /**
     * The Method is used to create ISProjectTask
     **/
    //@Override
    private void visitWbsParent(Integer wbsId) {
        double weightage = 0;
        double percent = 0;
        ISProjectWbs projectWbs = projectWbsRepository.findOne(wbsId);
        List<ISProjectWbs> childWbs = projectWbsRepository.findByParentOrderByCreatedDateAsc(projectWbs.getId());
        for (ISProjectWbs child : childWbs) {
            weightage += child.getWeightage();
            percent += (child.getPercentageComplete() * child.getWeightage()) / 100;
        }
        percent = Math.round(percent);
        projectWbs.setPercentageComplete(percent);
        projectWbs = projectWbsRepository.save(projectWbs);
        if (projectWbs.getParent() != null) {
            visitWbsParent(projectWbs.getParent());
        }
    }

    @Transactional(readOnly = false)
    public ISProjectTask create(ISProjectTask projectTask) {
        checkNotNull(projectTask);
        projectTask.setId(null);
        ISProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectTask = projectTaskRepository.save(projectTask);
        ISProjectWbs projectWbs = projectWbsRepository.findOne(projectTask.getWbsItem());
        wbsPercentage(projectWbs);
        addStatusHistoryRecord(projectTask.getId(), TaskStatus.NEW, TaskStatus.NEW);
        //TODO Add project user inbox message
        List<Integer> list = assignedToRepository.findByTaskId(projectTask.getId());
        if (list != null) {
            for (Integer assignedTo : list) {
                ISProjectUserInbox projectInbox = new ISProjectUserInbox();
                projectInbox.setProject(projectTask.getProject());
                projectInbox.setUserId(assignedTo);
                projectInbox.setMessageType("PROJECTTASK");
                projectInbox.setObjectType(ISObjectType.PROJECTTASK);
                projectInbox.setTimeStamp(new Date());
                projectInbox.setObjectId(projectTask.getId());
                String message = "Task {0} of project {1} has been assigned to you";
                message = MessageFormat.format(message, projectTask.getName(), project.getName());
                projectInbox.setMessage(message);
                projectUserInboxRepository.save(projectInbox);
            }
        }
        return projectTask;
    }

    /**
     * The Method is used to update ISProjectTask
     **/
    @Transactional(readOnly = false)
    public ISProjectTask update(Integer projectId, ISProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        ISProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        TaskStatus oldStatus = prev.getStatus();
        ISProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        if (prev.getStatus() == TaskStatus.NEW) {
            projectTask.setStatus(TaskStatus.ASSIGNED);
        }
        if (projectTask.getStatus() == TaskStatus.FINISHED) {
            int count = 0;
            projectTask.setPercentComplete(100.00);
            projectTask.setActualFinishDate(new Date());
            List<ISProjectTask> projectTasks = projectTaskRepository.findByWbsItem(projectTask.getWbsItem());
            for (ISProjectTask projectTask1 : projectTasks) {
                if (projectTask1.getStatus() != TaskStatus.FINISHED) {
                    count++;
                    break;
                }
            }
            if (count == 0) {
                ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
                wbs.setActualFinishDate(new Date());
                wbsRepository.save(wbs);
            }
        }
        if (projectTask.getActualStartDate() != null) {
            ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
            if (wbs.getActualStartDate() == null) {
                wbs.setActualStartDate(projectTask.getActualStartDate());
                wbsRepository.save(wbs);
                updateWbs(projectId, projectTask, wbs);
            }
        }
        updateWorkOrderStatus(projectTask);
        projectTask = projectTaskRepository.save(projectTask);
        Login login = sessionWrapper.getSession().getLogin();
        if (!oldStatus.equals(projectTask.getStatus())) {
            ISTaskStatusHistory history = new ISTaskStatusHistory();
            history.setModifiedBy(login.getPerson().getId());
            history.setModifiedDate(new Date());
            history.setNewStatus(projectTask.getStatus().name());
            history.setOldStatus(oldStatus.name());
            history.setTask(projectTask.getId());
            taskStatusHistoryRepository.save(history);

        }
        projectService.calculateWbsPercentage(projectTask.getWbsItem());
        return projectTask;
    }

    public void updateWorkOrderStatus(ISTask projectTask) {
        if (projectTask.getStatus() == TaskStatus.FINISHED && projectTask.getSubContract() == Boolean.TRUE) {
            List<ISWorkOrderItem> workOrderItems = workOrderItemRepository.findByTask(projectTask.getId());
            if (workOrderItems.size() > 0) {
                int workOrderId = workOrderItems.get(0).getWorkOrder();
                int count = 0;
                List<ISWorkOrderItem> workOrderItemList = workOrderItemRepository.findByWorkOrder(workOrderId);
                List<Integer> taskIds = workOrderItemList.stream().map(ISWorkOrderItem::getTask).collect(Collectors.toList());
                List<ISTask> tasks = taskRepository.findByIdIn(taskIds);
                for (ISTask task : tasks) {
                    if (task.getStatus() != TaskStatus.FINISHED) {
                        count++;
                        break;
                    }
                }
                if (count == 0) {
                    ISWorkOrder workOrder = workOrderRepository.findOne(workOrderId);
                    workOrder.setStatus(WorkOrderStatus.FINISHED);
                    workOrderRepository.save(workOrder);
                }
            }
        }

    }

    @Transactional(readOnly = false)
    public void updateWbs(Integer projectId, ISProjectTask projectTask, ISWbs wbs) {
        Date date = new Date();
        Date fdate = projectTask.getActualStartDate();
        int count = 0;
        List<ISWbs> wbsList = wbsRepository.findByProjectAndParentOrderByCreatedDateAsc(projectId, wbs.getParent());
        for (ISWbs wbs1 : wbsList) {
            if (wbs1.getActualStartDate() != null && date.after(wbs1.getActualStartDate())) {
                date = wbs1.getActualStartDate();
            }
            if (wbs1.getActualFinishDate() == null) {
                count++;
            }
            if (wbs1.getActualFinishDate() != null && fdate.before(wbs1.getActualFinishDate())) {
                fdate = wbs1.getActualFinishDate();
            }
        }
        if (wbs.getParent() != null) {
            ISWbs wbs1 = wbsRepository.findOne(wbs.getParent());
            wbs1.setActualStartDate(date);
            if (count == 0) {
                wbs1.setActualFinishDate(fdate);
            }
            wbsRepository.save(wbs1);
            if (wbs1.getParent() != null) {
                updateWbs(projectId, projectTask, wbsRepository.findOne(wbs.getParent()));
            }
        }
    }

    @Transactional(readOnly = false)
    public ISProjectTask updateTask(ISProjectTask projectTask) {
        checkNotNull(projectTask);
        checkNotNull(projectTask.getId());
        ISProjectTask prev = projectTaskRepository.findOne(projectTask.getId());
        if (prev == null) {
            throw new ResourceNotFoundException();
        }
        TaskStatus oldStatus = prev.getStatus();
        ISProject project = projectRepository.findOne(projectTask.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        if (prev.getStatus() == TaskStatus.NEW) {
            projectTask.setStatus(TaskStatus.ASSIGNED);
        }
        if (prev.getStatus() == TaskStatus.ASSIGNED) {
            projectTask.setStatus(TaskStatus.ASSIGNED);
        }
        if (projectTask.getStatus() == TaskStatus.FINISHED) {
            projectTask.setPercentComplete(100.00);
            projectTask.setActualFinishDate(new Date());
            ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
            wbs.setActualFinishDate(new Date());
            wbsRepository.save(wbs);
        }
        if (projectTask.getActualStartDate() != null) {
            ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
            wbs.setActualStartDate(projectTask.getActualStartDate());
            wbsRepository.save(wbs);
            updateTaskWbs(projectTask, wbs);
        }
        updateWorkOrderStatus(projectTask);
        projectTask = projectTaskRepository.save(projectTask);
        return projectTask;
    }

    @Transactional(readOnly = false)
    public void updateTaskWbs(ISProjectTask projectTask, ISWbs wbs) {
        Date date = new Date();
        Date fdate = projectTask.getActualStartDate();
        int count = 0;
        List<ISWbs> wbsList = wbsRepository.findByParentOrderByCreatedDateAsc(wbs.getParent());
        for (ISWbs wbs1 : wbsList) {
            if (wbs1.getActualStartDate() != null && date.after(wbs1.getActualStartDate())) {
                date = wbs1.getActualStartDate();
            }
            if (wbs1.getActualFinishDate() == null) {
                count++;
            }
            if (wbs1.getActualFinishDate() != null && fdate.before(wbs1.getActualFinishDate())) {
                fdate = wbs1.getActualFinishDate();
            }
        }
        if (wbs.getParent() != null) {
            ISWbs wbs1 = wbsRepository.findOne(wbs.getParent());
            wbs1.setActualStartDate(date);
            if (count == 0) {
                wbs1.setActualFinishDate(fdate);
            }
            wbsRepository.save(wbs1);
            if (wbs1.getParent() != null) {
                updateTaskWbs(projectTask, wbsRepository.findOne(wbs.getParent()));
            }
        }

    }

    /**
     * The Method is used to delete projectTask
     **/
    //@Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectTask projectTask = projectTaskRepository.findOne(id);
        if (projectTask == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(projectTask.getProject());
        projectTaskRepository.delete(projectTask);
    }

    /**
     * The Method is used to get projectTask
     **/
    @Transactional(readOnly = true)
    //@Override
    public ISProjectTask get(Integer id) {
        checkNotNull(id);
        ISProjectTask projectTask = projectTaskRepository.findOne(id);
        if (projectTask == null) {
            throw new ResourceNotFoundException();
        }
        return projectTask;
    }

    @Transactional(readOnly = true)
    public List<ISTask> getTasksByWbsItem(Integer wbsItem) {
        return taskRepository.findByWbsItemOrderByActualStartDateAsc(wbsItem);
    }

    /**
     * The Method is used to getAll the list of ISProjectTask
     **/
    @Transactional(readOnly = true)
    //@Override
    public List<ISProjectTask> getAll() {
        return projectTaskRepository.findAll();
    }

    /**
     * The Method is used to findAll the page of ISProjectTask to sort the "plannedStartDate".
     **/
    @Transactional(readOnly = true)
    //@Override
    public Page<ISProjectTask> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));
        }
        return projectTaskRepository.findAll(pageable);
    }

    /**
     * The Method is used to findAll the page of ISProjectTask to sort the "name".
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectTask> findAll(TaskCriteria criteria, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    new Sort(new Sort.Order(Sort.Direction.DESC, "name")));
        }
        Predicate predicate = taskPredicateBuilder.build(criteria, QISProjectTask.iSProjectTask);
        Page<ISProjectTask> tasks = projectTaskRepository.findAll(predicate, pageable);
        return tasks;
    }

    /**
     * The Method is used to find the page of ISProjectTask
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectTask> find(TaskCriteria criteria, Pageable pageable) {
        Predicate predicate = taskPredicateBuilder.build(criteria, QISProjectTask.iSProjectTask);
        Page<ISProjectTask> projectTasks = projectTaskRepository.findAll(predicate, pageable);
        for (ISProjectTask projectTask : projectTasks.getContent()) {
            projectTask.setWbsStructure(getWbsItem(projectTask));
            projectTask.setTotalUnitsCompleted(0.0);
            List<ISTaskCompletionHistory> taskCompletionHistory = getTaskHistory(projectTask.getId());
            if (taskCompletionHistory.size() > 0) {
                for (ISTaskCompletionHistory completionHistory : taskCompletionHistory) {
                    projectTask.setTotalUnitsCompleted(projectTask.getTotalUnitsCompleted() + completionHistory.getUnitsCompleted());
                }
            }
        }
        return projectTasks;
    }

    private String getWbsItem(ISProjectTask projectTask) {
        String name = "";
        ISWbs isWbs = wbsRepository.findOne(projectTask.getWbsItem());
        name = isWbs.getName();
        if (isWbs.getParent() != null) {
            name = visitParentWbs(isWbs, name);
        }
        return name;
    }

    private String visitParentWbs(ISWbs isWbs, String name) {
        ISWbs wbs = wbsRepository.findOne(isWbs.getParent());
        name = wbs.getName() + " / " + name;
        if (wbs.getParent() != null) {
            name = visitParentWbs(wbs, name);
        }
        return name;
    }

    /**
     * The Method is used to getPageableTasksForProject for the page of ISProjectTask
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectTask> getPageableTasksForProject(Integer projectId, Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));
        }
        return projectTaskRepository.findByProject(projectId, pageable);
    }

    @Transactional(readOnly = true)
    public List<ISProjectTask> getAllByProject(Integer projectId) {
        return projectTaskRepository.findByProject(projectId);
    }

    /**
     * The Method is used to getAllTasksByProject for the list of ISProjectTask
     **/
    @Transactional(readOnly = true)
    public List<ISProjectTask> getAllTasksByProject(Integer projectId) {
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProjectOrderByActualStartDateAsc(projectId);
        List<ISProjectTask> DescprojectTasks = projectTaskRepository.findByProjectOrderByActualStartDateDesc(projectId);
        ISProject isProject = projectRepository.findOne(projectId);
        isProject.setActualStartDate(projectTasks.get(0).getActualStartDate());
        Integer length = DescprojectTasks.size();
      /*  isProject.setActualFinishDate(DescprojectTasks.get(length - 1).getActualFinishDate());*/
        projectRepository.save(isProject);
        for (ISProjectTask projectTask : projectTasks) {
            ISWbs isWbs1 = wbsService.get(projectTask.getWbsItem());
            isWbs1.setActualStartDate(projectTask.getActualStartDate());
            isWbs1.setActualFinishDate(projectTask.getActualFinishDate());
            List<ISTask> isTasks = getTasksByWbsItem(projectTask.getWbsItem());
            for (ISTask isTask : isTasks) {
                if (isTask.getWbsItem() == isWbs1.getId()) {
                    isWbs1.setPercentageComplete(isTask.getPercentComplete());
                    wbsRepository.save(isWbs1);
                }
            }
            wbsRepository.save(isWbs1);
        }
        List<ISWbs> wbsParents = new ArrayList<ISWbs>();
        List<ISWbs> isWbses = wbsService.allWbsByProject(projectId);
        for (ISWbs isWbse : isWbses) {
            if (isWbse.getParent() == null) {
                wbsParents.add(isWbse);
            }
        }
        for (ISWbs wbsParent : wbsParents) {
            List<ISWbs> isWbs = wbsService.findByProjectAndParentOrderByActualStartDateAsc(projectId, wbsParent.getId());
            if (isWbs.size() == 0) {
                List<ISTask> isTasks = getTasksByWbsItem(wbsParent.getId());
                if (isTasks.size() > 0) {
                    wbsParent.setActualStartDate(isTasks.get(0).getActualStartDate());
                    wbsRepository.save(wbsParent);
                }
            } else {
                if (wbsParent.getActualStartDate() == null) {
                    wbsParent.setActualStartDate(isWbs.get(0).getActualStartDate());
                }
                List<ISWbs> isWbs1 = wbsService.findByProjectAndParentOrderByActualFinishDateDesc(projectId, wbsParent.getId());
                wbsParent.setActualFinishDate(isWbs1.get(0).getActualFinishDate());
                wbsRepository.save(wbsParent);
            }
        }
        projectRepository.save(isProject);
        return projectTasks;
    }

    /**
     * The Method is used to getTasksBySite for the list of ISProjectTask
     **/
    @Transactional(readOnly = true)
    public List<ISProjectTask> getTasksBySite(Integer siteId) {
        List<ISProjectTask> isProjectTasks = projectTaskRepository.findBySite(siteId);
        if (isProjectTasks.size() > 0) {
            for (ISProjectTask projectTask : isProjectTasks) {
                projectTask.setWbsStructure(getWbsItem(projectTask));
            }
        }
        return isProjectTasks;
    }

    /**
     * The Method is used to findMultipleResources for the list of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<ISProjectTask> findMultipleResources(List<Integer> ids) {
        List<ISProjectTask> projectTasks = projectTaskRepository.findByIdIn(ids);
        for (ISProjectTask task : projectTasks) {
            ISProjectSite site = projectSiteRepository.findOne(task.getSite());
            task.setProjectSite(site);
        }
        return projectTasks;
    }

    /**
     * The Method is used for the freeTextSearch for the Page of ISProjectTask
     **/
    public Page<ISProjectTask> freeTextSearch(Pageable pageable, TaskCriteria taskCriteria) {
        Predicate predicate = taskPredicateBuilder.build(taskCriteria, QISProjectTask.iSProjectTask);
        return projectTaskRepository.findAll(predicate, pageable);
    }

    /**
     * The Method is used to findTasks for the List of ISProjectTask
     **/

    @Transactional(readOnly = true)
    public List<ISProjectTask> findTasks(TaskCriteria projectTaskCriteria) {
        Predicate predicate = taskPredicateBuilder.build(projectTaskCriteria, QISProjectTask.iSProjectTask);
        Iterable<ISProjectTask> tasks = projectTaskRepository.findAll(predicate);
        List<ISProjectTask> listTasks = new ArrayList<>();
        for (ISProjectTask tmProjectTask : tasks) {
            listTasks.add(tmProjectTask);
        }
        return listTasks;
    }

    @Transactional(readOnly = false)
    public ISTaskCompletionHistory createTaskHistory(Integer projectId, Integer taskId, ISTaskCompletionHistory taskCompletionHistory) {
        if (taskCompletionHistory.getTimeStamp() == null) {
            taskCompletionHistory.setTimeStamp(new Date());
        }
        ISTaskCompletionHistory completionHistory = taskCompletionHistoryRepository.save(taskCompletionHistory);
        ISTask task = taskRepository.findOne(taskCompletionHistory.getTask());
        Double taskCompleted = 0.0;
        List<ISTaskCompletionHistory> taskCompletionHistoryList = taskCompletionHistoryRepository.findByTask(taskCompletionHistory.getTask());
        for (ISTaskCompletionHistory taskCompletionHistory1 : taskCompletionHistoryList) {
            taskCompleted += taskCompletionHistory1.getUnitsCompleted();
        }
        if (taskCompletionHistoryList.size() == 1 && task.getStatus() == TaskStatus.ASSIGNED) {
            task.setStatus(TaskStatus.INPROGRESS);
            task.setActualStartDate(new Date());
        }
        if (taskCompleted.equals(task.getTotalUnits())) {
            task.setStatus(TaskStatus.FINISHED);
            task.setActualFinishDate(new Date());
        }
        Double percent = (taskCompleted / task.getTotalUnits()) * 100;
        percent = new Double(Math.round(percent));
        task.setPercentComplete(percent);
        updateWorkOrderStatus(task);
        taskRepository.save(task);
        /*projectService.calculateWbsPercentage(task.getWbsItem());*/
        projectService.calculatePercentCompleteParent(task.getWbsItem());
        return completionHistory;
    }

    @Transactional(readOnly = true)
    public List<ISTaskCompletionHistory> getTaskHistory(Integer taskId) {
        List<ISTaskCompletionHistory> taskCompletionHistories = taskCompletionHistoryRepository.findByTask(taskId);
        if (taskCompletionHistories.size() > 0) {
            for (ISTaskCompletionHistory taskCompletionHistory : taskCompletionHistories) {
                List<ISTaskCompletionResource> taskCompletionResources = taskCompletionResourceRepository.findByTaskHistory(taskCompletionHistory.getId());
                if (taskCompletionResources.size() > 0) {
                    taskCompletionHistory.setResources(true);
                }
            }
        }
        return taskCompletionHistories;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getRequiredTaskAttributes(String objectType) {
        List<ObjectTypeAttribute> taskAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return taskAttributes;
    }

    private Date stringToDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<TaskReportDTO> getReportByDates(Integer project, String startDate1, String endDate1) {
        reportDTOs.clear();
        List<ISProjectTask> tasks = projectTaskRepository.findByProject(project);
        List<Integer> taskIds = tasks.stream().map(ISProjectTask::getId).collect(Collectors.toList());
        if (taskIds.size() > 0) {
            reportDTOs = getTaskReportByTaskIds(tasks, taskIds, startDate1, endDate1);
        }
        return reportDTOs;
    }

    @Transactional(readOnly = true)
    private List<TaskReportDTO> getTaskReportByTaskIds(List<ISProjectTask> tasks, List<Integer> taskIds, String startDate1, String endDate1) {
        Date startDate2 = null;
        if (startDate1.equals("undefined") || startDate1.equals("null") || startDate1.trim().equals("")) {
            startDate2 = taskCompletionHistoryRepository.getMinimumDate(taskIds);
            if (startDate2 == null) {
                startDate2 = new Date();
            }
        } else {
            startDate2 = stringToDate(startDate1);
        }
        Date startDate = new Date(startDate2.getTime() - (1000 * 60));
        Date endDate = new Date(stringToDate(endDate1).getTime() + (1000 * 60 * 60 * 24));
        List<ISTaskCompletionHistory> taskCompletionHistories =
                taskCompletionHistoryRepository.findByTaskInAndTimeStampAfterAndTimeStampBefore(taskIds, startDate, endDate);
        if (taskCompletionHistories.size() > 0) {
            Map<Integer, List<ISTaskCompletionHistory>> map = taskCompletionHistories.stream()
                    .collect(Collectors.groupingBy(ISTaskCompletionHistory::getTask));
            for (int i = 0; i < tasks.size(); i++) {
                ISProjectTask projectTask = tasks.get(i);
                List<ISTaskCompletionHistory> taskCompletionHistories1 = map.get(projectTask.getId());
                Double completed = 0.0;
                if (taskCompletionHistories1 != null && taskCompletionHistories1.size() > 0) {
                    for (ISTaskCompletionHistory taskCompletionHistory : taskCompletionHistories1) {
                        completed += taskCompletionHistory.getUnitsCompleted();
                    }
                    Double percentage = (completed / projectTask.getTotalUnits()) * 100;
                    reportDTOs.add(new TaskReportDTO(i + 1, projectTask.getName(), projectTask.getUnitOfWork(), projectTask.getTotalUnits(),
                            completed, projectTask.getTotalUnits() - completed, percentage));
                }

            }
        }
        return reportDTOs;
    }

    @Transactional(readOnly = true)
    public AssignedToTaskDto getTaskDetails(Integer taskId) {
        AssignedToTaskDto assignedToTaskDto = new AssignedToTaskDto();
        ISProjectTask projectTask = projectTaskRepository.findOne(taskId);
        assignedToTaskDto.setTask(projectTask);
        Person assignedBy = personRepository.findOne(projectTask.getCreatedBy());
        assignedToTaskDto.setAssignedBy(assignedBy);
        if (projectTask.getPerson() != null) {
            Person assignedTo = personRepository.findOne(projectTask.getPerson());
            assignedToTaskDto.setAssignedTo(assignedTo);
        }
        if (projectTask.getProject() != null) {
            ISProject project = projectRepository.findOne(projectTask.getProject());
            assignedToTaskDto.setProject(project);
        }
        if (projectTask.getSite() != null) {
            ISProjectSite projectSite = projectSiteRepository.findOne(projectTask.getSite());
            assignedToTaskDto.setSite(projectSite);
        }
        if (projectTask.getWbsItem() != null) {
            ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
            assignedToTaskDto.setWbs(wbs);
        }
        List<ISTaskCompletionHistory> histories = taskCompletionHistoryRepository.findByTask(projectTask.getId());
        for (ISTaskCompletionHistory completionHistory : histories) {
            assignedToTaskDto.setUnitsCompleted(assignedToTaskDto.getUnitsCompleted() + completionHistory.getUnitsCompleted());
            completionHistory.setCompletedPerson(personRepository.findOne(completionHistory.getCompletedBy()));
        }
        assignedToTaskDto.getTaskCompletionHistories().addAll(histories);
        return assignedToTaskDto;
    }

    @Transactional(readOnly = true)
    public Page<AssignedToTaskDto> getAssignedToTasksByPerson(Pageable pageable, AssignedTaskCriteria assignedTaskCriteria) {
        List<AssignedToTaskDto> assignedToTasks = new ArrayList<>();
        Predicate predicate = assignedTaskPredicateBuilder.build(assignedTaskCriteria, QISProjectTask.iSProjectTask);
        Page<ISProjectTask> projectTasks = projectTaskRepository.findAll(predicate, pageable);
        if (projectTasks.getContent().size() > 0) {
            for (ISProjectTask projectTask : projectTasks.getContent()) {
                AssignedToTaskDto assignedToTaskDto = new AssignedToTaskDto();
                assignedToTaskDto.setTask(projectTask);
                Person assignedBy = personRepository.findOne(projectTask.getCreatedBy());
                assignedToTaskDto.setAssignedBy(assignedBy);
                if (projectTask.getPerson() != null) {
                    Person assignedTo = personRepository.findOne(projectTask.getPerson());
                    assignedToTaskDto.setAssignedTo(assignedTo);
                }
                if (projectTask.getProject() != null) {
                    ISProject project = projectRepository.findOne(projectTask.getProject());
                    assignedToTaskDto.setProject(project);
                }
                if (projectTask.getSite() != null) {
                    ISProjectSite projectSite = projectSiteRepository.findOne(projectTask.getSite());
                    assignedToTaskDto.setSite(projectSite);
                }
                if (projectTask.getWbsItem() != null) {
                    ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
                    assignedToTaskDto.setWbs(wbs);
                }
                List<ISTaskCompletionHistory> histories = taskCompletionHistoryRepository.findByTask(projectTask.getId());
                for (ISTaskCompletionHistory completionHistory : histories) {
                    assignedToTaskDto.setUnitsCompleted(assignedToTaskDto.getUnitsCompleted() + completionHistory.getUnitsCompleted());
                    completionHistory.setCompletedPerson(personRepository.findOne(completionHistory.getCompletedBy()));
                }
                assignedToTaskDto.getTaskCompletionHistories().addAll(histories);
                assignedToTasks.add(assignedToTaskDto);
            }
        }
        Page<AssignedToTaskDto> assignedToTaskDtos = new PageImpl<AssignedToTaskDto>(assignedToTasks, pageable, assignedToTasks.size());
        return assignedToTaskDtos;
    }

    @Transactional(readOnly = true)
    public Page<AssignedToTaskDto> getAssignedByTasksByPerson(Integer personId, Pageable pageable) {
        List<AssignedToTaskDto> assignedToTasks = new ArrayList<>();
        Page<ISProjectTask> projectTasks = projectTaskRepository.findByCreatedBy(personId, pageable);
        if (projectTasks.getContent().size() > 0) {
            AssignedToTaskDto assignedToTaskDto = new AssignedToTaskDto();
            for (ISProjectTask projectTask : projectTasks) {
                assignedToTaskDto.setTask(projectTask);
                Person assignedTo = personRepository.findOne(projectTask.getPerson());
                assignedToTaskDto.setAssignedTo(assignedTo);
                ISProject project = projectRepository.findOne(projectTask.getProject());
                assignedToTaskDto.setProject(project);
                ISProjectSite projectSite = projectSiteRepository.findOne(projectTask.getSite());
                assignedToTaskDto.setSite(projectSite);
                ISWbs wbs = wbsRepository.findOne(projectTask.getWbsItem());
                assignedToTaskDto.setWbs(wbs);
                assignedToTasks.add(assignedToTaskDto);
            }
        }
        Page<AssignedToTaskDto> assignedToTaskDtos = new PageImpl<AssignedToTaskDto>(assignedToTasks, pageable, assignedToTasks.size());
        return assignedToTaskDtos;
    }

    public String exportTaskReport(Integer projectId, String fileType, HttpServletResponse response, ReportCriteria criteria) {
        List<String> columns = new ArrayList<String>(Arrays.asList("SerialNo", "Task Name", "Unit of work", "Total Units", "Units Completed", "Balance Work Units", "Percentage Completed", "Remarks"));
        List<TaskReportDTO> reportDTOs = getReportByDates(projectId, criteria.getFromDate(), criteria.getToDate());
        Export export = new Export();
        export.setFileName("Task-Report");
        export.setHeaders(columns);
        exportService.createExportObject(reportDTOs, columns, export);
        return exportService.exportFile(fileType, export, response);
    }

    public List<ISTask> getTasksByIds(List<Integer> taskIds) {
        return taskRepository.findByIdIn(taskIds);
    }

    public List<ISProjectTask> getContractTasks(Integer projectId) {
        return projectTaskRepository.findByProjectAndSubContract(projectId, Boolean.TRUE);
    }

    public TaskStatusCountDTO getTasksCount(Integer projectId) {
        TaskStatusCountDTO taskStatusCountDTO = new TaskStatusCountDTO();
        taskStatusCountDTO.setNewTasks(projectTaskRepository.findCountBystatusAndProject(TaskStatus.NEW, projectId));
        taskStatusCountDTO.setAssignedTasks(projectTaskRepository.findCountBystatusAndProject(TaskStatus.ASSIGNED, projectId));
        taskStatusCountDTO.setInProgressTasks(projectTaskRepository.findCountBystatusAndProject(TaskStatus.INPROGRESS, projectId));
        taskStatusCountDTO.setFinishedTasks(projectTaskRepository.findCountBystatusAndProject(TaskStatus.FINISHED, projectId));
        return taskStatusCountDTO;
    }

    public void getPersonsToAddToTask(Integer projectId, Integer taskId, Pageable pageable) {
    }

    @Transactional(readOnly = true)
    public DetailsCountDto getTaskDetailsCount(Integer projectId, Integer taskId) {
        DetailsCountDto detailsCountDto = new DetailsCountDto();
        detailsCountDto.setFiles(taskFileRepository.findByTaskAndLatestTrueOrderByCreatedDateDesc(taskId).size());
        detailsCountDto.setProblems(issueRepository.findByTargetObjectIdAndTargetObjectType(taskId, ObjectType.TASK).size());
        detailsCountDto.setManpower(projectService.getResourcesByType(projectId, taskId, ResourceType.MANPOWERTYPE).size());
        detailsCountDto.setManpower(detailsCountDto.getManpower() + projectService.getResourcesByType(projectId, taskId, ResourceType.ROLE).size());
        detailsCountDto.setMachine(projectService.getResourcesByType(projectId, taskId, ResourceType.MACHINETYPE).size());
        detailsCountDto.setMaterial(projectService.getResourcesByType(projectId, taskId, ResourceType.MATERIALTYPE).size());
        detailsCountDto.setMedia(mediaRepository.findByObjectIdOrderByCreatedDateDesc(taskId).size());
        return detailsCountDto;
    }

    @Transactional(readOnly = false)
    public ISWorkflow attachWorkflow(Integer taskId, Integer wfDefId) {
        ISWorkflow workflow = null;
        ISTask isTask = taskRepository.findOne(taskId);
        ISWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (isTask != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(taskId, wfDef);
            isTask.setWorkflow(workflow.getId());
            isTask.setWfStatus("-");
            taskRepository.save(isTask);
        }
        return workflow;
    }

    @Transactional
    public ISWorkflow startWorkflow(Integer wfId) {
        /*ISWorkflow workflow = isWorkflowRepository.findByAttachedTo(wfId);*/
        ISWorkflow workflow = isWorkflowRepository.findOne(wfId);
        if (workflow != null) {
            List<ISWorkflowTransition> list = isWorkflowTransitionService.getByFromStatus(workflow.getStart().getId());
            if (list.size() == 1) {
                Integer loginId = sessionWrapper.getSession().getLogin().getPerson().getId();
                workflow.setStarted(Boolean.TRUE);
                workflow.setStartedOn(new Date());
                workflow.getStart().setFlag(WorkflowStatusFlag.COMPLETED);
                workflow.getStart().setCreatedBy(loginId);
                workflow.getStart().setModifiedBy(loginId);
                isWorkflowStartRepository.save(workflow.getStart());
                ISWorkflowStatus status = isWorkflowStatusService.get(list.get(0).getToStatus());
                status.setFlag(WorkflowStatusFlag.INPROCESS);
                status.setCreatedBy(loginId);
                status.setModifiedBy(loginId);
                isWorkflowStatusService.update(status);
                workflow.setCurrentStatus(status.getId());
                isWorkflowService.notifyUsers(workflow, status);
            }
            workflow = isWorkflowRepository.save(workflow);
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public ProjectTaskResourcesDto getProjectResources(Integer taskId) {
        ProjectTaskResourcesDto projectTaskResourcesDto = new ProjectTaskResourcesDto();
        ISProjectTask projectTask = projectTaskRepository.findOne(taskId);
        List<ISProjectResource> isProjectResources = resourceRepository.findByProjectAndTask(projectTask.getProject(), taskId);
        if (isProjectResources.size() > 0) {
            for (ISProjectResource isProjectResource : isProjectResources) {
                if (isProjectResource.getResourceType().equals(ResourceType.MACHINETYPE)) {
                    ISBoqItem boqItem = boqItemRepository.findOne(isProjectResource.getReferenceId());
                    ISMachineItem isMachineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                    projectTaskResourcesDto.getMachineItems().add(isMachineItem);
                }
                if (isProjectResource.getResourceType().equals(ResourceType.MATERIALTYPE)) {
                    ISBoqItem boqItem = boqItemRepository.findOne(isProjectResource.getReferenceId());
                    ISMaterialItem isMaterialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                    projectTaskResourcesDto.getMaterialItems().add(isMaterialItem);
                }
                if (isProjectResource.getResourceType().equals(ResourceType.ROLE)) {
                    ISProjectRole projectRole = projectRoleRepository.findOne(isProjectResource.getReferenceId());
                    projectTaskResourcesDto.getProjectRoles().add(projectRole);
                }
                if (isProjectResource.getResourceType().equals(ResourceType.MANPOWERTYPE)) {
                    Person person = personRepository.findOne(isProjectResource.getReferenceId());
                    projectTaskResourcesDto.getPersons().add(person);

                }
            }

        }
        return projectTaskResourcesDto;
    }

    @Transactional(readOnly = false)
    public List<ISTaskCompletionResource> createTaskResources(Integer id, List<ISTaskCompletionResource> resources) {
        for (ISTaskCompletionResource taskCompletionResource : resources) {
            taskCompletionResource.setTaskHistory(id);
        }
        return taskCompletionResourceRepository.save(resources);
    }

    @Transactional(readOnly = false)
    public List<ISTaskCompletionResource> getProjectTaskResources(Integer id) {
        List<ISTaskCompletionResource> taskCompletionResources = new ArrayList<>();
        ISTaskCompletionHistory taskCompletionHistory = taskCompletionHistoryRepository.findOne(id);
        if (taskCompletionHistory != null) {
            taskCompletionResources = taskCompletionResourceRepository.findByTaskHistory(taskCompletionHistory.getId());
        }
        return taskCompletionResources;
    }

    public List<TaskProblemDto> getTaskProblems(Integer taskId) {
        List<TaskProblemDto> problems = new ArrayList<>();
        List<ISIssue> issues = issueRepository.findByTargetObjectIdAndTargetObjectType(taskId, ObjectType.TASK);
        issues.forEach(issue -> {
            TaskProblemDto problemDto = new TaskProblemDto();
            problemDto.setId(issue.getId());
            problemDto.setTitle(issue.getTitle());
            problemDto.setPriority(issue.getPriority());
            problemDto.setStatus(issue.getStatus());
            problemDto.setAssignedTo(personRepository.findOne(issue.getAssignedTo()).getFullName());
            problemDto.setReportedBy(personRepository.findOne(issue.getCreatedBy()).getFullName());
            problems.add(problemDto);
        });
        return problems;
    }

    public ISProjectTask getTask(Integer projectId, Integer taskId) {
        ISProjectTask projectTask = get(taskId);
//        projectTask.setProjectTasks(getAllByProject(projectId));
        projectTask.setTaskCompletionHistories(getTaskHistory(taskId));
        projectTask.setProjectPersons(projectService.getProjectPersons(projectId));
        projectTask.setFiles(findByTaskLatest(taskId).size());
        projectTask.setMedia(mediaRepository.findByObjectIdOrderByCreatedDateDesc(taskId).size());
        projectTask.setProblems(issueRepository.findByTargetObjectIdAndTargetObjectType(taskId, ObjectType.TASK).size());
        projectTask.setSiteName(projectSiteRepository.findOne(projectTask.getSite()).getName());
        Person person = personRepository.findOne(projectTask.getPerson());
        projectTask.setAssignedTo(person.getFullName());
        projectTask.setAssignedToNumber(person.getPhoneMobile());
        Double completed = 0.0;
        for (ISTaskCompletionHistory completionHistory : projectTask.getTaskCompletionHistories()) {
            completed = completed + completionHistory.getUnitsCompleted();
        }
        projectTask.setTotalUnitsCompleted(completed);
        return projectTask;
    }

}
