package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectDocument;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMSharedObject;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.ProjectTemplateActivity;
import com.cassinisys.plm.model.pm.ProjectTemplateActivityFile;
import com.cassinisys.plm.model.pm.ProjectTemplateTask;
import com.cassinisys.plm.model.pm.ProjectTemplateTaskFile;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.ProjectTemplateActivityRepository;
import com.cassinisys.plm.repo.plm.SharedObjectRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pm.ProjectTemplateActivityFileRepository;
import com.cassinisys.plm.repo.pm.ProjectTemplateTaskFileRepository;
import com.cassinisys.plm.repo.pm.ProjectTemplateTaskRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Service
public class ProjectTemplateActivityService implements CrudService<ProjectTemplateActivity, Integer> {

    @Autowired
    private ProjectTemplateActivityRepository projectTemplateActivityRepository;

    @Autowired
    private ProjectTemplateTaskRepository projectTemplateTaskRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectTemplateActivityFileRepository projectTemplateActivityFileRepository;
    @Autowired
    private ProjectTemplateTaskFileRepository projectTemplateTaskFileRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateActivity,'create')")
    public ProjectTemplateActivity create(ProjectTemplateActivity projectTemplateActivity) {
        projectTemplateActivity = projectTemplateActivityRepository.save(projectTemplateActivity);
        return projectTemplateActivity;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateActivity.id ,'edit')")
    public ProjectTemplateActivity update(ProjectTemplateActivity projectTemplateActivity) {
        ProjectTemplateActivity activity = projectTemplateActivityRepository.findByWbsAndNameEqualsIgnoreCase(projectTemplateActivity.getWbs(), projectTemplateActivity.getName());
        if (activity != null && !activity.getId().equals(projectTemplateActivity.getId())) {
            String message = messageSource.getMessage("activity_name_already_exist_in_wbs", null, "{0} activity name already exist on phase", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", projectTemplateActivity.getName());
            throw new CassiniException(result);
        }
        projectTemplateActivity = projectTemplateActivityRepository.save(projectTemplateActivity);
        return projectTemplateActivity;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        ProjectTemplateActivity projectTemplateActivity = projectTemplateActivityRepository.findOne(id);
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        projectTemplateActivityRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateActivity get(Integer id) {
        return projectTemplateActivityRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplateActivity> getAll() {
        return projectTemplateActivityRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateActivity getActivityByNameAndWbs(String activityName, Integer wbsId) {
        ProjectTemplateActivity templateActivity = projectTemplateActivityRepository.findByWbsAndNameEqualsIgnoreCase(wbsId, activityName);
        return templateActivity;
    }

    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateTask,'create')")
    public ProjectTemplateTask createTask(ProjectTemplateTask projectTemplateTask) {
        projectTemplateTask = projectTemplateTaskRepository.save(projectTemplateTask);
        return projectTemplateTask;
    }

    @Transactional
    @PreAuthorize("hasPermission(#templateTask.id ,'edit')")
    public ProjectTemplateTask updateTask(Integer taskId, ProjectTemplateTask templateTask) {
        ProjectTemplateTask task = projectTemplateTaskRepository.findByActivityAndNameEqualsIgnoreCase(templateTask.getActivity(), templateTask.getName());
        if (task != null && !task.getId().equals(templateTask.getId())) {
            String message = messageSource.getMessage("task_name_already_exist_in_activity", null, "{0} task name already exist on activity.", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", templateTask.getName());
            throw new CassiniException(result);
        }
        templateTask = projectTemplateTaskRepository.save(templateTask);
        return templateTask;
    }

    @Transactional(readOnly = true)
    public ProjectTemplateTask getTask(Integer taskId) {
        ProjectTemplateTask templateTask = projectTemplateTaskRepository.findOne(taskId);
        PLMWorkflow workflow = workflowRepository.findByAttachedTo(templateTask.getId());
        if (workflow != null) {
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    templateTask.setStartWorkflow(true);
                }
            }
            if (workflow.getFinish() != null) {
                PLMWorkflowStatus workflowStatus1 = plmWorkflowStatusRepository.findOne(workflow.getFinish().getId());
                if (workflowStatus1 != null && workflowStatus1.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    templateTask.setFinishWorkflow(true);
                }
            }
        }
        return templateTask;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplateTask> getProjectTemplateTasks(Integer activityId) {
        return projectTemplateTaskRepository.findByActivity(activityId);
    }

    @Transactional
    @PreAuthorize("hasPermission(#taskId,'delete')")
    public void deleteTemplateTasks(Integer taskId) {
        projectTemplateTaskRepository.delete(taskId);
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(taskId));
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @PreFilter("hasPermission(filterObject,'create')")
    public List<ProjectTemplateActivity> createActivities(List<ProjectTemplateActivity> plmActivities) {
        List<ProjectTemplateActivity> plmActivityList = new ArrayList<>();
        plmActivities.forEach(ProjectTemplateActivity -> {
            ProjectTemplateActivity activity = projectTemplateActivityRepository.save(ProjectTemplateActivity);
            activity.setGanttId(ProjectTemplateActivity.getGanttId());
            plmActivityList.add(activity);
        });
        return plmActivityList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @PreFilter("hasPermission(filterObject,'create')")
    public List<ProjectTemplateTask> createActivityTasks(List<ProjectTemplateTask> activityTaskList) {
        List<ProjectTemplateTask> plmTaskList = new ArrayList<>();
        activityTaskList.forEach(activityTask -> {
            ProjectTemplateTask task = projectTemplateTaskRepository.save(activityTask);
            task.setGanttId(activityTask.getGanttId());
            plmTaskList.add(task);
        });
        return plmTaskList;
    }

    public String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMFile projectFile = fileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }

    @Transactional
    public void deleteFolder(Integer projectId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, folderId);
        List<ProjectTemplateActivityFile> templateFiles = projectTemplateActivityFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) templateFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        projectTemplateActivityFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            ProjectTemplateActivityFile parent = projectTemplateActivityFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = projectTemplateActivityFileRepository.save(parent);
        }
    }

    @Transactional
    public ProjectTemplateActivityFile createProjectTemplateActivityFolder(Integer templateId, ProjectTemplateActivityFile plmProjectFile) throws JsonProcessingException {
        plmProjectFile.setId(null);
        String folderNumber = null;
        ProjectTemplateActivityFile existFolderName = null;
        if (plmProjectFile.getParentFile() != null) {
            existFolderName = projectTemplateActivityFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(plmProjectFile.getName(), plmProjectFile.getParentFile(), templateId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmProjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = projectTemplateActivityFileRepository.findByNameEqualsIgnoreCaseAndActivityAndLatestTrue(plmProjectFile.getName(), templateId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmProjectFile.setActivity(templateId);
        plmProjectFile.setFileNo(folderNumber);
        plmProjectFile.setFileType("FOLDER");
        plmProjectFile = projectTemplateActivityFileRepository.save(plmProjectFile);
        if (plmProjectFile.getParentFile() != null) {
            ProjectTemplateActivityFile parent = projectTemplateActivityFileRepository.findOne(plmProjectFile.getParentFile());
            parent.setModifiedDate(plmProjectFile.getModifiedDate());
            parent = projectTemplateActivityFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(templateId, plmProjectFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return plmProjectFile;
    }

    @Transactional
    public void deleteTaskFolder(Integer projectId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, folderId);
        List<ProjectTemplateTaskFile> templateFiles = projectTemplateTaskFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) templateFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        projectTemplateTaskFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            ProjectTemplateTaskFile parent = projectTemplateTaskFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = projectTemplateTaskFileRepository.save(parent);
        }
    }

    @Transactional
    public ProjectTemplateTaskFile createProjectTemplateTaskFolder(Integer templateId, ProjectTemplateTaskFile plmProjectFile) throws JsonProcessingException {
        plmProjectFile.setId(null);
        String folderNumber = null;
        ProjectTemplateTaskFile existFolderName = null;
        if (plmProjectFile.getParentFile() != null) {
            existFolderName = projectTemplateTaskFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndTaskAndLatestTrue(plmProjectFile.getName(), plmProjectFile.getParentFile(), templateId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmProjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = projectTemplateTaskFileRepository.findByNameEqualsIgnoreCaseAndTaskAndLatestTrue(plmProjectFile.getName(), templateId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmProjectFile.setTask(templateId);
        plmProjectFile.setFileNo(folderNumber);
        plmProjectFile.setFileType("FOLDER");
        plmProjectFile = projectTemplateTaskFileRepository.save(plmProjectFile);
        if (plmProjectFile.getParentFile() != null) {
            ProjectTemplateTaskFile parent = projectTemplateTaskFileRepository.findOne(plmProjectFile.getParentFile());
            parent.setModifiedDate(plmProjectFile.getModifiedDate());
            parent = projectTemplateTaskFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(templateId, plmProjectFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return plmProjectFile;
    }

    @Transactional
    public PLMWorkflow attachTaskTemplateWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        ProjectTemplateTask projectTemplateTask = projectTemplateTaskRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = workflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            projectTemplateTask.setWorkflow(null);
            projectTemplateTaskRepository.save(projectTemplateTask);
            workflowService.deleteWorkflow(id);
        }
        if (projectTemplateTask != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.TEMPLATETASK, projectTemplateTask.getId(), wfDef);
            projectTemplateTask.setWorkflow(workflow.getId());
            projectTemplateTask = projectTemplateTaskRepository.save(projectTemplateTask);
        }
        return workflow;
    }


    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<ProjectTemplateTaskFile> files = projectTemplateTaskFileRepository.findByIdIn(fileIds);
        List<ProjectTemplateTaskFile> plmFiles = projectTemplateTaskFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = projectTemplateTaskFileRepository.getFileNosByIds(fileIds);
        List<ProjectTemplateTaskFile> fileNoFiles = projectTemplateTaskFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<ProjectTemplateTaskFile> fileCountList = projectTemplateTaskFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.getDocumentsCountByObjectIdAndFolderIds(object, fileIds);
        Map<Integer, List<PLMFile>> childrenMap = new HashMap<>();
        Map<String, List<PLMFile>> fileNosMap = new HashMap();
        Map<Integer, List<PLMFile>> fileCountsMap = new HashMap();
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountsMap = new HashMap();
        Map<Integer, Person> personsMap = new HashMap();
        Map<Integer, Login> loginsMap = new HashMap();

        personsMap = persons.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        loginsMap = logins.stream().collect(Collectors.toMap(x -> x.getPerson().getId(), x -> x));
        childrenMap = plmFiles.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));
        objectDocumentCountsMap = objectDocuments.stream().collect(Collectors.groupingBy(d -> d.getFolder()));
        fileNosMap = fileNoFiles.stream().collect(Collectors.groupingBy(d -> d.getFileNo()));
        fileCountsMap = fileCountList.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));

        List<PLMSharedObject> shareObjects = sharedObjectRepository.getSharedObjectByObjectIdsInAndPerson(fileIds, sessionWrapper.getSession().getLogin().getPerson().getId());
        Map<Integer, List<PLMSharedObject>> shareObjsCountMap = new HashMap();
        shareObjsCountMap = shareObjects.stream().collect(Collectors.groupingBy(d -> d.getObjectId()));


        Map<Integer, List<PLMFile>> fileChildrenMap = childrenMap;
        Map<String, List<PLMFile>> fileNoMap = fileNosMap;
        Map<Integer, List<PLMFile>> fileCountMap = fileCountsMap;
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountMap = objectDocumentCountsMap;
        Map<Integer, Person> personMap = personsMap;
        Map<Integer, Login> loginMap = loginsMap;
        Map<Integer, List<PLMSharedObject>> finalShareObjectMap = shareObjsCountMap;
        files.forEach(plmFile -> {
            FileDto fileDto = new FileDto();
            fileDto.setId(plmFile.getId());
            fileDto.setName(plmFile.getName());
            fileDto.setObject(object);
            fileDto.setParentObject(objectType);
            fileDto.setDescription(plmFile.getDescription());
            fileDto.setFileNo(plmFile.getFileNo());
            fileDto.setFileType(plmFile.getFileType());
            fileDto.setParentFile(plmFile.getParentFile());
            fileDto.setSize(plmFile.getSize());
            fileDto.setLatest(plmFile.getLatest());
            fileDto.setVersion(plmFile.getVersion());
            fileDto.setLocked(plmFile.getLocked());
            fileDto.setLockedBy(plmFile.getLockedBy());
            fileDto.setLockedDate(plmFile.getLockedDate());
            fileDto.setThumbnail(plmFile.getThumbnail());
            List<PLMSharedObject> existingShareObjects = finalShareObjectMap.containsKey(plmFile.getId()) ? finalShareObjectMap.get(plmFile.getId()) : new ArrayList<>();
            if (existingShareObjects.size() > 0) {
                fileDto.setShared(true);
            }
            fileDto.setChildFileCount(fileChildrenMap.containsKey(plmFile.getId()) ? fileChildrenMap.get(plmFile.getId()).size() : 0);
            if (fileDto.getLockedBy() != null) {
                fileDto.setLockedByName(personRepository.findOne(plmFile.getLockedBy()).getFullName());
            }
            fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
            fileDto.setCreatedDate(plmFile.getCreatedDate());
            fileDto.setModifiedDate(plmFile.getModifiedDate());
            List<PLMFile> initialVersionFiles = fileNoMap.containsKey(plmFile.getFileNo()) ? fileNoMap.get(plmFile.getFileNo()) : new ArrayList<PLMFile>();
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personMap.get(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personMap.get(plmFile.getCreatedBy());
            }
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(person.getId());
            Login login = loginMap.get(person.getId());
            fileDto.setExternal(login.getExternal());
            fileDto.setModifiedByName(personMap.get(plmFile.getModifiedBy()).getFullName());
            fileDto.setReplaceFileName(plmFile.getReplaceFileName());
            fileDto.setUrn(plmFile.getUrn());
            if (fileDto.getFileType().equals("FOLDER")) {
                fileDto.setCount(fileCountMap.containsKey(fileDto.getId()) ? fileCountMap.get(fileDto.getId()).size() : 0);
                fileDto.setCount(fileDto.getCount() + (objectDocumentCountMap.containsKey(fileDto.getId()) ? objectDocumentCountMap.get(fileDto.getId()).size() : 0));
                if (hierarchy) {
                    visitTaskFileChildren(object, objectType, fileDto, hierarchy);
                }
            }
            filesDto.add(fileDto);
        });
        return filesDto;
    }

    @Transactional(readOnly = true)
    public List<FileDto> convertActivityFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<ProjectTemplateActivityFile> files = projectTemplateActivityFileRepository.findByIdIn(fileIds);
        List<ProjectTemplateActivityFile> plmFiles = projectTemplateActivityFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = projectTemplateActivityFileRepository.getFileNosByIds(fileIds);
        List<ProjectTemplateActivityFile> fileNoFiles = projectTemplateActivityFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<ProjectTemplateActivityFile> fileCountList = projectTemplateActivityFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
        List<PLMObjectDocument> objectDocuments = objectDocumentRepository.getDocumentsCountByObjectIdAndFolderIds(object, fileIds);
        Map<Integer, List<PLMFile>> childrenMap = new HashMap<>();
        Map<String, List<PLMFile>> fileNosMap = new HashMap();
        Map<Integer, List<PLMFile>> fileCountsMap = new HashMap();
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountsMap = new HashMap();
        Map<Integer, Person> personsMap = new HashMap();
        Map<Integer, Login> loginsMap = new HashMap();

        personsMap = persons.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        loginsMap = logins.stream().collect(Collectors.toMap(x -> x.getPerson().getId(), x -> x));
        childrenMap = plmFiles.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));
        objectDocumentCountsMap = objectDocuments.stream().collect(Collectors.groupingBy(d -> d.getFolder()));
        fileNosMap = fileNoFiles.stream().collect(Collectors.groupingBy(d -> d.getFileNo()));
        fileCountsMap = fileCountList.stream().collect(Collectors.groupingBy(d -> d.getParentFile()));

        List<PLMSharedObject> shareObjects = sharedObjectRepository.getSharedObjectByObjectIdsInAndPerson(fileIds, sessionWrapper.getSession().getLogin().getPerson().getId());
        Map<Integer, List<PLMSharedObject>> shareObjsCountMap = new HashMap();
        shareObjsCountMap = shareObjects.stream().collect(Collectors.groupingBy(d -> d.getObjectId()));


        Map<Integer, List<PLMFile>> fileChildrenMap = childrenMap;
        Map<String, List<PLMFile>> fileNoMap = fileNosMap;
        Map<Integer, List<PLMFile>> fileCountMap = fileCountsMap;
        Map<Integer, List<PLMObjectDocument>> objectDocumentCountMap = objectDocumentCountsMap;
        Map<Integer, Person> personMap = personsMap;
        Map<Integer, Login> loginMap = loginsMap;
        Map<Integer, List<PLMSharedObject>> finalShareObjectMap = shareObjsCountMap;
        files.forEach(plmFile -> {
            FileDto fileDto = new FileDto();
            fileDto.setId(plmFile.getId());
            fileDto.setName(plmFile.getName());
            fileDto.setObject(object);
            fileDto.setParentObject(objectType);
            fileDto.setDescription(plmFile.getDescription());
            fileDto.setFileNo(plmFile.getFileNo());
            fileDto.setFileType(plmFile.getFileType());
            fileDto.setParentFile(plmFile.getParentFile());
            fileDto.setSize(plmFile.getSize());
            fileDto.setLatest(plmFile.getLatest());
            fileDto.setVersion(plmFile.getVersion());
            fileDto.setLocked(plmFile.getLocked());
            fileDto.setLockedBy(plmFile.getLockedBy());
            fileDto.setLockedDate(plmFile.getLockedDate());
            fileDto.setThumbnail(plmFile.getThumbnail());
            List<PLMSharedObject> existingShareObjects = finalShareObjectMap.containsKey(plmFile.getId()) ? finalShareObjectMap.get(plmFile.getId()) : new ArrayList<>();
            if (existingShareObjects.size() > 0) {
                fileDto.setShared(true);
            }
            fileDto.setChildFileCount(fileChildrenMap.containsKey(plmFile.getId()) ? fileChildrenMap.get(plmFile.getId()).size() : 0);
            if (fileDto.getLockedBy() != null) {
                fileDto.setLockedByName(personRepository.findOne(plmFile.getLockedBy()).getFullName());
            }
            fileDto.setObjectType(PLMObjectType.valueOf(plmFile.getObjectType().toString()));
            fileDto.setCreatedDate(plmFile.getCreatedDate());
            fileDto.setModifiedDate(plmFile.getModifiedDate());
            List<PLMFile> initialVersionFiles = fileNoMap.containsKey(plmFile.getFileNo()) ? fileNoMap.get(plmFile.getFileNo()) : new ArrayList<PLMFile>();
            Person person = null;
            if (initialVersionFiles.size() > 0) {
                person = personMap.get(initialVersionFiles.get(0).getCreatedBy());
            } else {
                person = personMap.get(plmFile.getCreatedBy());
            }
            fileDto.setCreatedByName(person.getFullName());
            fileDto.setCreatedBy(person.getId());
            Login login = loginMap.get(person.getId());
            fileDto.setExternal(login.getExternal());
            fileDto.setModifiedByName(personMap.get(plmFile.getModifiedBy()).getFullName());
            fileDto.setReplaceFileName(plmFile.getReplaceFileName());
            fileDto.setUrn(plmFile.getUrn());
            if (fileDto.getFileType().equals("FOLDER")) {
                fileDto.setCount(fileCountMap.containsKey(fileDto.getId()) ? fileCountMap.get(fileDto.getId()).size() : 0);
                fileDto.setCount(fileDto.getCount() + (objectDocumentCountMap.containsKey(fileDto.getId()) ? objectDocumentCountMap.get(fileDto.getId()).size() : 0));
                if (hierarchy) {
                    visitChildren(object, objectType, fileDto, hierarchy);
                }
            }
            filesDto.add(fileDto);
        });
        return filesDto;
    }


    public FileDto visitChildren(Integer object, PLMObjectType objectType, FileDto fileDto, Boolean hierarchy) {
        List<Integer> foldersList = projectTemplateActivityFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = projectTemplateActivityFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertActivityFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertActivityFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

    public FileDto visitTaskFileChildren(Integer object, PLMObjectType objectType, FileDto fileDto, Boolean hierarchy) {
        List<Integer> foldersList = projectTemplateTaskFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = projectTemplateTaskFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

}
