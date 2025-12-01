package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.security.LoginSecurityPermission;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.repo.security.LoginSecurityPermissionRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SecurityPermissionService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ActivityEvents;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.event.TaskEvents;
import com.cassinisys.plm.filtering.ProjectDeliverableCriteria;
import com.cassinisys.plm.filtering.ProjectGlossaryDeliverableBuilder;
import com.cassinisys.plm.filtering.ProjectItemDeliverableBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.LinkDto;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.activitystream.dto.ASBomItem;
import com.cassinisys.plm.service.activitystream.dto.ASNewActivityAndMilestoneDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewTaskDTO;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.tm.UserTaskEvents;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by swapna on 1/3/18.
 */
@Service
public class ActivityService {

    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;
    @Autowired
    private ActivityItemReferenceRepository activityItemReferenceRepository;
    @Autowired
    private TaskItemReferenceRepository taskItemReferenceRepository;
    @Autowired
    private ProjectItemReferenceRepository projectItemReferenceRepository;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private DeliverableRepository deliverableRepository;
    @Autowired
    private ProjectItemDeliverableBuilder projectItemDeliverableBuilder;
    @Autowired
    private ProjectGlossaryDeliverableBuilder projectGlossaryDeliverableBuilder;
    @Autowired
    private GlossaryRepository glossaryRepository;
    @Autowired
    private GlossaryDeliverableRepository glossaryDeliverableRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SpecificationDeliverableRepository specificationDeliverableRepository;
    @Autowired
    private RequirementDeliverableRepository requirementDeliverableRepository;
    @Autowired
    private FileHelpers fileHelpers;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private LinksRepository linksRepository;
    @Autowired
    private ObjectFileService qualityFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private SecurityPermissionService securityPermissionService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private LoginSecurityPermissionRepository loginSecurityPermissionRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private LoginRepository loginRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','projectactivity') || @customePrivilegeFilter.filterPrivilage(authentication,'create','project')")
    public List<PLMActivity> createActivities(Integer projectId, List<PLMActivity> plmActivities) throws JsonProcessingException {
        List<PLMActivity> plmActivityList = new ArrayList<>();
        List<ASNewActivityAndMilestoneDTO> asNewActivityAndMilestoneDTOs = new ArrayList<>();
        plmActivities.forEach(plmActivity -> {
            PLMWbsElement wbsElement = wbsElementRepository.findOne(plmActivity.getWbs());
            PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
            if (plmActivity.getId() == null) {
                ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), plmActivity.getName());
                asNewActivityAndMilestoneDTOs.add(asNewActivityAndMilestoneDTO);
            }
            plmActivity.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTACTIVITY));
            PLMActivity activity = activityRepository.save(plmActivity);
            activity.setGanttId(plmActivity.getGanttId());
            if (project.getActualFinishDate() != null) {
                project.setActualFinishDate(null);
                project = projectRepository.save(project);
            }
            plmActivityList.add(activity);

            if (plmActivity.getAssignedTo() != null) {
                applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityAssignedEvent(project, activity));
                String criteria = "object.id == " + activity.getId();
                List<LoginSecurityPermission> loginSecurityPermissions = loginSecurityPermissionRepository.findByObjectTypeAndPrivilegeAndCriteria("projectactivity", "all", criteria);
                if (loginSecurityPermissions.size() > 0) {
                    for (LoginSecurityPermission loginSecurityPermission : loginSecurityPermissions) {
                        loginSecurityPermission.setPerson(personRepository.findOne(plmActivity.getAssignedTo()));
                        loginSecurityPermissionRepository.save(loginSecurityPermission);
                    }
                } else {
                    securityPermissionService.addLoginSecurityPermission(plmActivity.getAssignedTo(), activity, "all");
                }
            }
        });
        PLMProject plmProject = projectRepository.findOne(projectId);
        if (asNewActivityAndMilestoneDTOs.size() > 0) {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivitiesAddedEvent(plmProject, asNewActivityAndMilestoneDTOs));
            List<String> activities = new ArrayList<>();
            asNewActivityAndMilestoneDTOs.forEach(f -> activities.add(f.getName()));
            projectService.sendProjectSubscribeNotification(plmProject, activities.toString(), "activityAdded");
        }
        return plmActivityList;
    }

    @Transactional
    @PreAuthorize("hasPermission(#plmActivity,'create')")
    public PLMActivity createActivity(PLMActivity plmActivity) {
        Integer workflowDef = null;
        if (plmActivity.getWorkflowDefId() != null) {
            workflowDef = plmActivity.getWorkflowDefId();
        }
        List<PLMActivity> activities = activityRepository.findByWbs(plmActivity.getWbs());
        List<PLMMilestone> milestones = milestoneRepository.findByWbs(plmActivity.getWbs());
        plmActivity.setSequenceNumber((activities.size() + milestones.size()) + 1);
        PLMActivity activity = activityRepository.save(plmActivity);
        activity.setGanttId(plmActivity.getGanttId());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        if (project.getActualFinishDate() != null) {
            project.setActualFinishDate(null);
            project = projectRepository.save(project);
        }

        if (workflowDef != null) {
            attachActivityWorkflow(activity.getId(), workflowDef);
        }

        if (plmActivity.getAssignedTo() != null) {
            applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityAssignedEvent(project, activity));
            securityPermissionService.addLoginSecurityPermission(plmActivity.getAssignedTo(), activity, "all");
        }
        return activity;
    }

    @Transactional
    @PreAuthorize("hasPermission(#plmActivity.id ,'edit')")
    public PLMActivity updateActivity(PLMActivity plmActivity) {
        PLMActivity activity = activityRepository.findByWbsAndNameEqualsIgnoreCase(plmActivity.getWbs(), plmActivity.getName());
        PLMActivity existActivity = activityRepository.findOne(plmActivity.getId());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(existActivity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        if (activity != null && !activity.getId().equals(plmActivity.getId())) {
            String message = messageSource.getMessage("activity_name_already_exist_in_wbs", null, "{0} activity name already exist on phase.", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", plmActivity.getName());
            throw new CassiniException(result);
        }
        if (plmActivity.getAssignedTo() != null) {
            applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityAssignedEvent(project, plmActivity));
        }
        if (plmActivity.getType() == null && existActivity.getType() == null) {
            plmActivity.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTACTIVITY));
        } else if (existActivity.getType() != null) {
            plmActivity.setType(existActivity.getType());
        }
        if (!existActivity.getStatus().equals(ProjectActivityStatus.FINISHED) && plmActivity.getStatus().equals(ProjectActivityStatus.FINISHED)) {
            plmActivity.setActualStartDate(new Date());
            plmActivity.setActualFinishDate(new Date());
        }
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityBasicInfoUpdatedEvent(existActivity, plmActivity));
        plmActivity = activityRepository.save(plmActivity);
        return plmActivity;
    }

    @Transactional
    @PreAuthorize("hasPermission(#plmActivity,'delete')")
    public void deleteActivity(Integer plmActivity) throws JsonProcessingException {
        PLMActivity activity = activityRepository.findOne(plmActivity);
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        List<WBSDto> wbsDtoList = projectService.getWbsChildrenBySequence(project.getId(), wbsElement.getId());
        for (WBSDto element : wbsDtoList) {
            if (element.getSequenceNumber() > activity.getSequenceNumber()) {
                if (element.getObjectType().equals("PROJECTACTIVITY")) {
                    PLMActivity activity1 = activityRepository.findOne(element.getId());
                    activity1.setSequenceNumber(element.getSequenceNumber() - 1);
                    activity1 = activityRepository.save(activity1);
                } else {
                    PLMMilestone milestone = milestoneRepository.findOne(element.getId());
                    milestone.setSequenceNumber(element.getSequenceNumber() - 1);
                    milestone = milestoneRepository.save(milestone);
                }
            }
        }
        applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityDeletedEvent(project, activity));
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(plmActivity));
        activityRepository.delete(plmActivity);
        projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityDeleted");
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMActivity getActivity(Integer plmActivity) {
        PLMActivity activity = activityRepository.findOne(plmActivity);
        if (activity != null) {
            if (activity.getAssignedTo() != null) {
                Person person = personRepository.findOne(activity.getAssignedTo());
                activity.setPerson(person);
            }
            PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(activity.getId());
            if (workflow != null) {
                PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
                if (status != null) {
                    activity.setWorkflowStatus(status.getName());
                }
                if (workflow.getStart() != null) {
                    PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                    if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                        activity.setStartWorkflow(true);
                    }
                }
            }
        }
        return activity;
    }

    @Transactional(readOnly = true)
    public List<PLMActivityFile> getActivityFiles(Integer activityId) {
        List<PLMActivityFile> activityFiles = activityFileRepository.findByActivityAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(activityId);
        activityFiles.forEach(activityFile -> {
            activityFile.setParentObject(PLMObjectType.PROJECTACTIVITY);
            if (activityFile.getFileType().equals("FOLDER")) {
                activityFile.setCount(activityFileRepository.getChildrenCountByParentFileAndLatestTrue(activityFile.getId()));
                activityFile.setCount(activityFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(activityFile.getActivity(), activityFile.getId()));
            }
        });
        return activityFiles;
    }

    @Transactional
    public List<PLMActivityFile> uploadActivityFiles(Integer activityId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMActivityFile> uploadedFiles = new ArrayList<>();
        List<PLMActivityFile> newFiles = new ArrayList<>();
        List<PLMActivityFile> versionedFiles = new ArrayList<>();
        String fNames = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        boolean itemFileNotExist = false;
        List<PLMActivityFile> plmItemFiles = new ArrayList<>();
        PLMActivity activity1 = activityRepository.findOne(activityId);
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity1.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    boolean versioned = false;
                    PLMActivityFile activityFile = null;
                    if (folderId == 0) {
                        activityFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(activityId, name);
                    } else {
                        activityFile = activityFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (activityFile != null) {
                        activityFile.setLatest(false);
                        Integer oldVersion = activityFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = activityFile.getFileNo();
                        oldFile = activityFile.getId();
                        activityFileRepository.save(activityFile);
                        versioned = true;
                    }
                    if (activityFile == null) {
                        itemFileNotExist = true;
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    activityFile = new PLMActivityFile();
                    activityFile.setName(name);
                    activityFile.setFileNo(autoNumber1);
                    activityFile.setCreatedBy(login.getPerson().getId());
                    activityFile.setModifiedBy(login.getPerson().getId());
                    activityFile.setVersion(version);
                    activityFile.setActivity(activityId);
                    activityFile.setSize(file.getSize());
                    activityFile.setFileType("FILE");
                    if (folderId != 0) {
                        activityFile.setParentFile(folderId);
                    }
                    activityFile = activityFileRepository.save(activityFile);
                    if (activityFile.getParentFile() != null) {
                        PLMActivityFile parent = activityFileRepository.findOne(activityFile.getParentFile());
                        parent.setModifiedDate(activityFile.getModifiedDate());
                        parent = activityFileRepository.save(parent);
                    }
                    if (activityFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, activityFile.getId());
                    }
                    if (itemFileNotExist) {
                        plmItemFiles.add(activityFile);
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + activityId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(activityId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fNames = activityFile.getName();
                    String path = dir + File.separator + activityFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(activityFile);
                    if (versioned) {
                        versionedFiles.add(activityFile);
                    } else {
                        newFiles.add(activityFile);
                    }
                }
            }
            if (newFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFilesAddedEvent(activity1, newFiles));
            }
            if (versionedFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFilesVersionedEvent(activity1, versionedFiles));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadedFiles;
    }

    @Transactional
    public void deleteActivityFile(Integer activityId, Integer fileId) {
        PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(activityId, fileId);
        fileSystemService.deleteDocumentFromDiskFolder(fileId, dir);
        if (activityFile.getParentFile() != null) {
            PLMActivityFile parent = activityFileRepository.findOne(activityFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = activityFileRepository.save(parent);
        }
        PLMActivity activity = activityRepository.findOne(activityId);
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileDeletedEvent(activity, activityFile));
        activityFileRepository.delete(fileId);
    }

    @Transactional
    public PLMActivityDeliverable createActivityDeliverable(PLMActivityDeliverable activityDeliverable) {
        return activityDeliverableRepository.save(activityDeliverable);
    }

    @Transactional(readOnly = true)
    public PLMActivityDeliverable getActivityDeliverable(Integer deliverableId) {
        return activityDeliverableRepository.findOne(deliverableId);
    }

    @Transactional
    public PLMActivityDeliverable updateActivityDeliverable(PLMActivityDeliverable activityDeliverable) {
        return activityDeliverableRepository.save(activityDeliverable);
    }

    @Transactional
    public void deleteActivityDeliverable(Integer deliverableId) {
        PLMActivityDeliverable activityDeliverable = activityDeliverableRepository.findOne(deliverableId);
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(activityDeliverable.getItemRevision());
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        PLMActivity activity = activityRepository.findOne(activityDeliverable.getActivity());
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityDeliverableDeletedEvent(activity, item));
        activityDeliverableRepository.delete(deliverableId);
    }

    @Transactional(readOnly = true)
    public List<PLMDeliverable> getActivtiyDeliverables(Integer activityId) {
        List<PLMDeliverable> activityDeliverables = new ArrayList<>();
        List<PLMActivityDeliverable> deliverables = activityDeliverableRepository.findByActivity(activityId);
        if (deliverables.size() != 0) {
            for (PLMActivityDeliverable activityDeliverable : deliverables) {
                PLMDeliverable deliverable = deliverableRepository.findOne(activityDeliverable.getId());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                deliverable.setRevision(itemRevision);
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                deliverable.setItem(item);
                deliverable.setObjectId(activityDeliverable.getActivity());
                deliverable.setObjectType(PLMObjectType.PROJECTACTIVITY.toString());
                activityDeliverables.add(deliverable);
            }
        }
        List<PLMTask> tasks = taskRepository.findByActivity(activityId);
        for (PLMTask task : tasks) {
            List<PLMTaskDeliverable> taskDeliverables = taskDeliverableRepository.findByTask(task.getId());
            for (PLMTaskDeliverable taskDeliverable : taskDeliverables) {
                Boolean taskDeliverableExist = false;
                for (PLMDeliverable deliverable : deliverables) {
                    if (taskDeliverable.getItemRevision().equals(deliverable.getItemRevision())) {
                        taskDeliverableExist = true;
                    }
                }
                if (!taskDeliverableExist) {
                    PLMDeliverable deliverable = deliverableRepository.findOne(taskDeliverable.getId());
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                    deliverable.setRevision(itemRevision);
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    deliverable.setItem(item);
                    deliverable.setObjectId(taskDeliverable.getTask());
                    deliverable.setObjectType(PLMObjectType.PROJECTTASK.toString());
                    activityDeliverables.add(deliverable);
                }
            }
        }
        return activityDeliverables;
    }

    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getAllActivtiyDeliverables(Integer activityId) {
        PLMProjectDeliverableDto activityDeliverableDto = new PLMProjectDeliverableDto();
        List<PLMActivityDeliverable> deliverables = activityDeliverableRepository.findByActivity(activityId);
        if (deliverables.size() != 0) {
            for (PLMActivityDeliverable activityDeliverable : deliverables) {
                PLMDeliverable deliverable = deliverableRepository.findOne(activityDeliverable.getId());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                PLMActivity plmActivity = activityRepository.findOne(activityDeliverable.getActivity());
                if (plmActivity.getAssignedTo() != null) {
                    Person person = personRepository.findOne(plmActivity.getAssignedTo());
                    deliverable.setOwner(person.getFullName());
                    deliverable.setOwnerId(person.getId());
                }
                deliverable.setContextName(plmActivity.getName());
                deliverable.setRevision(itemRevision);
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                deliverable.setItem(item);
                deliverable.setObjectId(activityDeliverable.getActivity());
                deliverable.setObjectType(PLMObjectType.PROJECTACTIVITY.toString());
                activityDeliverableDto.getItemDeliverables().add(deliverable);
            }
        }
        List<PLMTask> tasks = taskRepository.findByActivity(activityId);
        for (PLMTask task : tasks) {
            List<PLMTaskDeliverable> taskDeliverables = taskDeliverableRepository.findByTask(task.getId());
            for (PLMTaskDeliverable taskDeliverable : taskDeliverables) {
                Boolean taskDeliverableExist = false;
                for (PLMDeliverable deliverable : deliverables) {
                    if (taskDeliverable.getItemRevision().equals(deliverable.getItemRevision())) {
                        taskDeliverableExist = true;
                    }
                }
                if (!taskDeliverableExist) {
                    PLMDeliverable deliverable = deliverableRepository.findOne(taskDeliverable.getId());
                    PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                    deliverable.setRevision(itemRevision);
                    PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                    PLMTask plmTask = taskRepository.findOne(taskDeliverable.getTask());
                    if (plmTask.getAssignedTo() != null) {
                        Person person = personRepository.findOne(plmTask.getAssignedTo());
                        deliverable.setOwner(person.getFullName());
                        deliverable.setOwnerId(person.getId());
                    }
                    deliverable.setContextName(plmTask.getName());
                    deliverable.setItem(item);
                    deliverable.setObjectId(taskDeliverable.getTask());
                    deliverable.setObjectType(PLMObjectType.PROJECTTASK.toString());
                    activityDeliverableDto.getItemDeliverables().add(deliverable);
                }
            }
        }
        List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(activityId);
        activityDeliverableDto.getGlossaryDeliverables().addAll(glossaryDeliverables);
        List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(activityId);
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByObjectId(activityId);
        activityDeliverableDto.getSpecificationDeliverables().addAll(specificationDeliverables);
        activityDeliverableDto.getRequirementDeliverables().addAll(requirementDeliverables);
        for (PLMTask task : tasks) {
            List<PLMGlossaryDeliverable> taskDeliverables = glossaryDeliverableRepository.findByObjectId(task.getId());
            for (PLMGlossaryDeliverable taskDeliverable : taskDeliverables) {
                Boolean taskDeliverableExist = false;
                for (PLMGlossaryDeliverable deliverable : glossaryDeliverables) {
                    if (taskDeliverable.getGlossary().getId().equals(deliverable.getGlossary().getId())) {
                        taskDeliverableExist = true;
                    }
                }
                if (!taskDeliverableExist) {
                    activityDeliverableDto.getGlossaryDeliverables().add(taskDeliverable);
                }
            }
        }
        return activityDeliverableDto;
    }

    /*--------------- Getting Specifications deliverables ---------------------*/
    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getActivitySpecDeliverables(Integer activity) {
        PLMProjectDeliverableDto plmProjectDeliverableDtos = new PLMProjectDeliverableDto();
        List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(activity);
        plmProjectDeliverableDtos.getSpecificationDeliverables().addAll(specificationDeliverables);
        List<PLMTask> tasks = taskRepository.findByActivity(activity);
        for (PLMTask task : tasks) {
            List<SpecificationDeliverable> taskDeliverables = specificationDeliverableRepository.findByObjectId(task.getId());
            for (SpecificationDeliverable taskDeliverable : taskDeliverables) {
                Boolean taskDeliverableExist = false;
                for (SpecificationDeliverable deliverable : plmProjectDeliverableDtos.getSpecificationDeliverables()) {
                    if (taskDeliverable.getSpecification().getId().equals(deliverable.getSpecification().getId())) {
                        taskDeliverableExist = true;
                    }
                }
                if (!taskDeliverableExist) {
                    plmProjectDeliverableDtos.getSpecificationDeliverables().add(taskDeliverable);
                }
            }
        }
        return plmProjectDeliverableDtos;
    }


    /*------------------- Get Requirement Deliverables ----------------*/
    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getActivityReqDeliverables(Integer activity) {
        PLMProjectDeliverableDto plmProjectDeliverableDtos = new PLMProjectDeliverableDto();
        List<RequirementDeliverable> specificationDeliverables = requirementDeliverableRepository.findByObjectId(activity);
        plmProjectDeliverableDtos.getRequirementDeliverables().addAll(specificationDeliverables);

        List<PLMTask> tasks = taskRepository.findByActivity(activity);
        for (PLMTask task : tasks) {
            List<RequirementDeliverable> taskDeliverables = requirementDeliverableRepository.findByObjectId(task.getId());
            for (RequirementDeliverable taskDeliverable : taskDeliverables) {
                Boolean taskDeliverableExist = false;
                for (RequirementDeliverable deliverable : plmProjectDeliverableDtos.getRequirementDeliverables()) {
                    if (taskDeliverable.getRequirement().getId().equals(deliverable.getRequirement().getId())) {
                        taskDeliverableExist = true;
                    }
                }
                if (!taskDeliverableExist) {
                    plmProjectDeliverableDtos.getRequirementDeliverables().add(taskDeliverable);
                }
            }
        }

        return plmProjectDeliverableDtos;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getFilteredItems(Pageable pageable, ProjectDeliverableCriteria deliverableCriteria) {
        Predicate predicate = projectItemDeliverableBuilder.build(deliverableCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems = itemRepository.findAll(predicate, pageable);
        return plmItems;
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossary> getFilteredGlossaries(Pageable pageable, ProjectDeliverableCriteria deliverableCriteria) {
        Predicate predicate = projectGlossaryDeliverableBuilder.build(deliverableCriteria, QPLMGlossary.pLMGlossary);
        Page<PLMGlossary> glossaries = glossaryRepository.findAll(predicate, pageable);
        return glossaries;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getDeliveablesByActivity(Integer activityId, PageRequest pageRequest) {
        List<PLMActivityDeliverable> plmActivityDeliverables = activityDeliverableRepository.findByActivity(activityId);
        List<PLMItem> plmItems = new ArrayList<>();
        pageRequest.setSize(pageRequest.getSize() + plmActivityDeliverables.size());
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> items = itemRepository.findAll(pageable);
        if (plmActivityDeliverables.size() != 0) {
            for (PLMItem item : items.getContent()) {
                Boolean exist = false;
                for (PLMActivityDeliverable activityDeliverable : plmActivityDeliverables) {
                    if (activityDeliverable.getItemRevision().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items.getContent());
        }
        Page<PLMItem> itemsPage = new PageImpl<PLMItem>(plmItems, pageable, plmItems.size());
        return itemsPage;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getDeliveablesByTask(Integer taskId, PageRequest pageRequest) {
        List<PLMTaskDeliverable> taskDeliverableList = taskDeliverableRepository.findByTask(taskId);
        pageRequest.setSize(pageRequest.getSize() + taskDeliverableList.size());
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        List<PLMItem> plmItems = new ArrayList<>();
        Page<PLMItem> items = itemRepository.findAll(pageable);
        if (taskDeliverableList.size() != 0) {
            for (PLMItem item : items.getContent()) {
                Boolean exist = false;
                for (PLMTaskDeliverable taskDeliverable : taskDeliverableList) {
                    if (taskDeliverable.getItemRevision().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items.getContent());
        }
        Page<PLMItem> itemsPage = new PageImpl<PLMItem>(plmItems, pageable, plmItems.size());
        return itemsPage;
    }

    @Transactional
    public List<PLMActivityDeliverable> createActivityDeliverables(Integer id, List<PLMActivityDeliverable> activityDeliverables) {
        String items = null;
        PLMActivity activity1 = activityRepository.findOne(id);
        List<PLMItem> items1 = new ArrayList<>();
        for (PLMActivityDeliverable plmActivityDeliverable : activityDeliverables) {
            PLMActivityDeliverable activityDeliverable = activityDeliverableRepository.findByActivityAndItemRevision(plmActivityDeliverable.getActivity(), plmActivityDeliverable.getItemRevision());
            if (activityDeliverable == null) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmActivityDeliverable.getItemRevision());
                PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                items1.add(plmItem);
                if (items == null) {
                    items = plmItem.getItemNumber();
                } else {
                    items = items + " ," + plmItem.getItemNumber();
                }
            }
        }
        if (items != null) {
            activityDeliverableRepository.save(activityDeliverables);
        }
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityDeliverablesAddedEvent(activity1, items1));
        return activityDeliverables;
    }

    @Transactional
    public List<PLMGlossaryDeliverable> createActivityGlossaryDeliverables(Integer activityId, List<PLMGlossaryDeliverable> activityDeliverables) {
        String items = null;
        PLMWbsElement wbsElement = null;
        PLMActivity activity1 = activityRepository.findOne(activityId);
        if (activity1 != null) {
            wbsElement = wbsElementRepository.findOne(activity1.getWbs());
        }
        PLMTask task = taskRepository.findOne(activityId);
        if (task != null) {
            PLMActivity activity = activityRepository.findOne(task.getActivity());
            wbsElement = wbsElementRepository.findOne(activity.getWbs());
        }
        for (PLMGlossaryDeliverable glossaryDeliverable : activityDeliverables) {
            PLMGlossaryDeliverable glossaryDeliverable1 = glossaryDeliverableRepository.findByObjectIdAndGlossary(glossaryDeliverable.getObjectId(), glossaryDeliverable.getGlossary());
            PLMGlossary glossary = glossaryRepository.findOne(glossaryDeliverable.getGlossary().getId());
            if (glossaryDeliverable1 == null) {
                if (items == null) {
                    items = glossary.getName();
                } else {
                    items = items + " ," + glossary.getName();
                }
                glossaryDeliverable = glossaryDeliverableRepository.save(glossaryDeliverable);
            }
        }
        return activityDeliverables;
    }

    @Transactional
    public PLMTask createActivityTask(PLMTask activityTask) {
        List<PLMTask> plmTasks = taskRepository.findByActivity(activityTask.getActivity());
        activityTask.setSequenceNumber(plmTasks.size() + 1);
        PLMTask task = taskRepository.save(activityTask);
        task.setGanttId(activityTask.getGanttId());
        PLMActivity activity = activityRepository.findOne(task.getActivity());
        if (activity.getActualFinishDate() != null) {
            activity.setStatus(ProjectActivityStatus.INPROGRESS);
            activity.setActualFinishDate(null);
            activity = activityRepository.save(activity);
        }
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        if (project.getActualFinishDate() != null) {
            project.setActualFinishDate(null);
            project = projectRepository.save(project);
        }
        return task;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','projecttask') || @customePrivilegeFilter.filterPrivilage(authentication,'create','project')")
    public List<PLMTask> createActivityTasks(Integer projectId, List<PLMTask> activityTaskList) throws JsonProcessingException {
        List<PLMTask> plmTaskList = new ArrayList<>();
        List<ASNewTaskDTO> asNewTaskDTOs = new ArrayList<>();
        PLMProject plmProject = projectRepository.findOne(projectId);
        List<String> tasks = new ArrayList<>();
        if (activityTaskList.size() > 0) {
            activityTaskList.forEach(f -> {
                if (f.getId() == null) {
                    tasks.add(f.getName() + " and assigned to " + personRepository.findOne(f.getAssignedTo()).getFullName());
                }
            });
        }
        if (tasks.size() > 0) {
            projectService.sendProjectSubscribeNotification(plmProject, tasks.toString(), "taskAdded");
        }
        activityTaskList.forEach(activityTask -> {
            List<PLMTask> plmTasks = taskRepository.findByActivity(activityTask.getActivity());
            PLMActivity activity = activityRepository.findOne(activityTask.getActivity());
            PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
            PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
            if (activityTask.getId() == null) {
                ASNewTaskDTO asNewTaskDTO = new ASNewTaskDTO(wbsElement.getName(), activity.getName(), activityTask.getName());
                asNewTaskDTOs.add(asNewTaskDTO);
            }
            activityTask.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTTASK));
            PLMTask task = taskRepository.save(activityTask);
            task.setGanttId(activityTask.getGanttId());
            if (activity.getActualFinishDate() != null && activityTask.getGanttId() != null) {
                activity.setStatus(ProjectActivityStatus.INPROGRESS);
                activity.setActualFinishDate(null);
                activity = activityRepository.save(activity);
            }

            if (project.getActualFinishDate() != null) {
                project.setActualFinishDate(null);
                project = projectRepository.save(project);
            }
            plmTaskList.add(task);
            if (task.getAssignedTo() != null) {
                applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectTaskAssignedEvent(project, task));
                securityPermissionService.addLoginSecurityPermission(task.getAssignedTo(), activity, "view");
                String criteria = "object.id == " + task.getId();
                List<LoginSecurityPermission> loginSecurityPermissions = loginSecurityPermissionRepository.findByObjectTypeAndPrivilegeAndCriteria("projecttask", "all", criteria);
                if (loginSecurityPermissions.size() > 0) {
                    for (LoginSecurityPermission loginSecurityPermission : loginSecurityPermissions) {
                        loginSecurityPermission.setPerson(personRepository.findOne(task.getAssignedTo()));
                        loginSecurityPermissionRepository.save(loginSecurityPermission);
                    }
                } else {
                    securityPermissionService.addLoginSecurityPermission(task.getAssignedTo(), task, "all");
                }
            }
        });
        if (asNewTaskDTOs.size() > 0) {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectTasksAddedEvent(plmProject, asNewTaskDTOs));
        }
        return plmTaskList;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMTask getActivityTask(Integer taskId, Integer activityId) {
        PLMTask plmTask = taskRepository.findOne(taskId);
        List<Person> persons = new ArrayList<>();
        if (plmTask != null) {
            PLMActivity plmActivity = activityRepository.findOne(activityId);
            if (plmActivity != null) {
                PLMWbsElement projectWbsElement = wbsElementRepository.findOne(plmActivity.getWbs());
                if (projectWbsElement != null) {
                    List<PLMProjectMember> listOfTeamMembers = projectMemberRepository.findByProject(projectWbsElement.getProject().getId());
                    for (PLMProjectMember member : listOfTeamMembers) {
                        Person person = personRepository.findOne(member.getPerson());
                        persons.add(person);
                    }
                    plmTask.setPersons(persons);

                }
            }
            PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(taskId);
            if (workflow != null) {
                PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
                if (status != null) {
                    plmTask.setTaskStatus(status.getName());
                }
                plmTask.setFinishedWorkflow(plmWorkflowRepository.findOne(plmTask.getWorkflow()).getFinished());
                if (workflow.getStart() != null) {
                    PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                    if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                        plmTask.setStartWorkflow(true);
                    }
                }
            }
        }
        return plmTask;
    }

    @Transactional(readOnly = true)
    public PLMTask getActivityTaskByName(Integer activityId, String taskName) {
        PLMTask task = taskRepository.findByActivityAndNameEqualsIgnoreCase(activityId, taskName);
        return task;
    }

    @Transactional
    @PreAuthorize("hasPermission(#task.id ,'edit')")
    public PLMTask updateActivityTask(PLMTask task) throws JsonProcessingException {
        PLMTask plmtask = taskRepository.findByActivityAndNameEqualsIgnoreCase(task.getActivity(), task.getName());
        if (plmtask != null && !plmtask.getId().equals(task.getId())) {
            String message = messageSource.getMessage("Task_name_already_exist_in_Activity", null, "{0} Task name already exist on activity.", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", task.getName());
            throw new CassiniException(result);
        }
        PLMActivity activity = activityRepository.findOne(task.getActivity());
        PLMTask existTask = taskRepository.findOne(task.getId());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = wbsElement.getProject();
        if (checkPredecessors(task, project)) {
            if (task.getAssignedTo() != null) {
                applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectTaskAssignedEvent(project, task));
                Person person = personRepository.findOne(task.getAssignedTo());
            }
            if (task.getPercentComplete() == 100 && !existTask.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                task.setStatus(ProjectTaskStatus.FINISHED);
                task.setActualFinishDate(new Date());
                if (task.getActualStartDate() == null) task.setActualStartDate(new Date());
                ASNewTaskDTO asNewTaskDTO = new ASNewTaskDTO(wbsElement.getName(), activity.getName(), task.getName());
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectTaskFinishedEvent(project, asNewTaskDTO));
                applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectTaskFinishedEvent(project, task));
                projectService.sendProjectSubscribeNotification(project, task.getName(), "taskFinished");
            }
            if (task.getPercentComplete() > 0 && task.getPercentComplete() < 100) {
                if (task.getActualStartDate() == null) task.setActualStartDate(new Date());
                ASBomItem asBomItem = new ASBomItem(wbsElement.getName(), (int) Math.round(task.getPercentComplete()), activity.getName(), task.getName());
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectTaskPercentageUpdatedEvent(project, asBomItem));
                projectService.sendProjectSubscribeNotification(project, task.getName() + "-" + task.getPercentComplete(), "taskPercentUpdated");
            }
            if (task.getType() == null && existTask.getType() == null) {
                task.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTTASK));
            } else if (existTask.getType() != null) {
                task.setType(existTask.getType());
            }
            applicationEventPublisher.publishEvent(new TaskEvents.TaskBasicInfoUpdatedEvent(existTask, task));
            PLMTask plmTask = taskRepository.save(task);

            List<PLMTask> plmTasks = taskRepository.findByActivity(activity.getId());
            if (activity.getActualStartDate() != null) {
                if (task.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                    if (plmTasks.size() == 1) {
                        activity.setStatus(ProjectActivityStatus.FINISHED);
                        activity.setActualFinishDate(new Date());
                        activity = activityRepository.save(activity);
                        ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), activity.getName());
                        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityFinishedEvent(project, asNewActivityAndMilestoneDTO));
                        applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityFinishedEvent(project, activity));
                        projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityFinished");
                    } else if (plmTasks.size() > 1) {
                        List<PLMTask> finishedTasks = new ArrayList<>();
                        for (PLMTask task1 : plmTasks) {
                            if (task1.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                                finishedTasks.add(task1);
                            }
                        }
                        if (plmTasks.size() == finishedTasks.size()) {
                            activity.setStatus(ProjectActivityStatus.FINISHED);
                            activity.setActualFinishDate(new Date());
                            activity = activityRepository.save(activity);
                            ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), activity.getName());
                            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityFinishedEvent(project, asNewActivityAndMilestoneDTO));
                            applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityFinishedEvent(project, activity));
                            projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityFinished");
                        } else {
                            activity.setStatus(ProjectActivityStatus.INPROGRESS);
                            activity.setActualFinishDate(null);
                            activity = activityRepository.save(activity);
                        }
                    }
                } else if (task.getStatus().equals(ProjectTaskStatus.INPROGRESS)) {
                    if (plmTasks.size() == 1) {
                        activity.setStatus(ProjectActivityStatus.INPROGRESS);
                        activity.setActualFinishDate(null);
                        activity = activityRepository.save(activity);
                    } else if (plmTasks.size() > 1) {
                        List<PLMTask> finishedTasks = new ArrayList<>();
                        for (PLMTask task1 : plmTasks) {
                            if (task1.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                                finishedTasks.add(task1);
                            }
                        }
                        if (plmTasks.size() == finishedTasks.size()) {
                            activity.setStatus(ProjectActivityStatus.FINISHED);
                            activity.setActualFinishDate(new Date());
                            activity = activityRepository.save(activity);
                            ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), activity.getName());
                            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityFinishedEvent(project, asNewActivityAndMilestoneDTO));
                            applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityFinishedEvent(project, activity));
                            projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityFinished");
                        } else {
                            activity.setStatus(ProjectActivityStatus.INPROGRESS);
                            activity.setActualFinishDate(null);
                            activity = activityRepository.save(activity);
                        }
                    }
                }
            }
            if (activity.getActualStartDate() == null) {
                if (task.getStatus().equals(ProjectTaskStatus.INPROGRESS)) {
                    activity.setStatus(ProjectActivityStatus.INPROGRESS);
                    activity.setActualStartDate(new Date());
                    activity = activityRepository.save(activity);
                } else if (task.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                    activity.setActualStartDate(new Date());
                    if (plmTasks.size() == 1) {
                        activity.setStatus(ProjectActivityStatus.FINISHED);
                        activity.setActualFinishDate(new Date());
                        activity = activityRepository.save(activity);
                        ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), activity.getName());
                        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityFinishedEvent(project, asNewActivityAndMilestoneDTO));
                        applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityFinishedEvent(project, activity));
                        projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityFinished");
                    } else if (plmTasks.size() > 1) {
                        activity.setStatus(ProjectActivityStatus.INPROGRESS);
                        activity.setActualStartDate(new Date());
                        activity = activityRepository.save(activity);
                    }
                }
            }
            project = projectRepository.findOne(wbsElement.getProject().getId());
            if (activity.getActualStartDate() != null) {
                if (project.getActualStartDate() == null) {
                    project.setActualStartDate(new Date());
                    projectRepository.save(project);
                }
            }
            List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
            if (activity.getActualFinishDate() != null) {
                List<PLMActivity> finishedActivities = new ArrayList<>();
                List<PLMActivity> allProjectActivities = new ArrayList<>();
                for (PLMWbsElement wbsElement1 : wbsElements) {
                    List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement1.getId());
                    if (activities.size() != 0) {
                        for (PLMActivity activity1 : activities) {
                            if (activity1.getStatus().equals(ProjectActivityStatus.FINISHED)) {
                                finishedActivities.add(activity1);
                            }
                            allProjectActivities.add(activity1);
                        }
                    }
                }
                if (allProjectActivities.size() == finishedActivities.size()) {
                    project.setActualFinishDate(new Date());
                } else {
                    project.setActualFinishDate(null);
                }
                projectRepository.save(project);
            } else {
                project.setActualFinishDate(null);
                projectRepository.save(project);
            }
            return plmTask;
        } else {

            return task;
        }
    }

    public boolean checkPredecessors(PLMTask task, PLMProject project) throws JsonProcessingException {
        PLMLinks links = linksRepository.findByProject(project.getId());
        boolean flag = true;
        if (links != null) {
            String linkString = linksRepository.findByProject(project.getId()).getDependency();
            ObjectMapper objectMapper = new ObjectMapper();
            LinkDto[] linkDtos = objectMapper.readValue(linkString, LinkDto[].class);
            for (LinkDto link : linkDtos) {
                if (link.getTarget().equals(task.getId()) && flag) {
                    PLMTask sourceTask = taskRepository.findOne(link.getSource());
                    if (sourceTask.getPercentComplete() != 100) {
                        flag = false;
                        String message = messageSource.getMessage("cannot_update_task", null, "task {0} cannot be updated until task {1} is completed", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", task.getName(), sourceTask.getName());
                        throw new CassiniException(result);
                    }
                }
            }
        } else flag = true;
        return flag;
    }


    @Transactional
    public void deleteActivityTask(Integer taskId) throws JsonProcessingException {
        PLMTask plmTask = taskRepository.findOne(taskId);
        PLMActivity activity = activityRepository.findOne(plmTask.getActivity());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        List<PLMTask> tasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activity.getId());
        /*for (PLMTask task : tasks) {
            if (task.getSequenceNumber() > plmTask.getSequenceNumber()) {
                task.setSequenceNumber(task.getSequenceNumber() - 1);
                task = taskRepository.save(task);
            }
        }*/
        applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectTaskDeletedEvent(wbsElement.getProject(), plmTask));
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(taskId));
        taskRepository.delete(taskId);
        projectService.sendProjectSubscribeNotification(wbsElement.getProject(), plmTask.getName(), "taskDeleted");
    }

    @Transactional(readOnly = true)
    public List<PLMTask> getActivityTasks(Integer activityId) {
        return taskRepository.findByActivityOrderBySequenceNumberAsc(activityId);
    }

    @Transactional(readOnly = true)
    public List<PLMTaskFile> getTaskFiles(Integer taskId) {
        List<PLMTaskFile> taskFiles = taskFileRepository.findByTaskAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(taskId);
        taskFiles.forEach(taskFile -> {
            taskFile.setParentObject(PLMObjectType.PROJECTTASK);
            if (taskFile.getFileType().equals("FOLDER")) {
                taskFile.setCount(taskFileRepository.getChildrenCountByParentFileAndLatestTrue(taskFile.getId()));
                taskFile.setCount(taskFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(taskFile.getTask(), taskFile.getId()));
            }
        });
        return taskFiles;
    }

    @Transactional
    public List<PLMTaskFile> uploadTaskFiles(Integer taskId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMTaskFile> uploadedFiles = new ArrayList<>();
        List<PLMTaskFile> newFiles = new ArrayList<>();
        List<PLMTaskFile> versionedFiles = new ArrayList<>();
        PLMTask task = taskRepository.findOne(taskId);
        boolean itemFileNotExist = false;
        List<PLMTaskFile> plmItemFiles = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    boolean versioned = false;
                    PLMTaskFile taskFile = null;
                    if (folderId == 0) {
                        taskFile = taskFileRepository.findByTaskAndNameAndParentFileIsNullAndLatestTrue(taskId, name);
                    } else {
                        taskFile = taskFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (taskFile != null) {
                        taskFile.setLatest(false);
                        Integer oldVersion = taskFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = taskFile.getFileNo();
                        oldFile = taskFile.getId();
                        taskFileRepository.save(taskFile);
                        versioned = true;

                    }
                    if (taskFile == null) {
                        itemFileNotExist = true;
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    taskFile = new PLMTaskFile();
                    taskFile.setName(name);
                    taskFile.setFileNo(autoNumber1);
                    taskFile.setCreatedBy(login.getPerson().getId());
                    taskFile.setModifiedBy(login.getPerson().getId());
                    taskFile.setVersion(version);
                    taskFile.setTask(taskId);
                    taskFile.setSize(file.getSize());
                    taskFile.setFileType("FILE");
                    if (folderId != 0) {
                        taskFile.setParentFile(folderId);
                    }
                    taskFile = taskFileRepository.save(taskFile);
                    if (taskFile.getParentFile() != null) {
                        PLMTaskFile parent = taskFileRepository.findOne(taskFile.getParentFile());
                        parent.setModifiedDate(taskFile.getModifiedDate());
                        parent = taskFileRepository.save(parent);
                    }
                    if (taskFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, taskFile.getId());
                    }
                    if (itemFileNotExist) {
                        plmItemFiles.add(taskFile);
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + taskId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentTaskFileSystemPath(taskId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + taskFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploadedFiles.add(taskFile);
                    if (versioned) {
                        versionedFiles.add(taskFile);
                    } else {
                        newFiles.add(taskFile);
                    }
                }
            }
            if (newFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new TaskEvents.TaskFilesAddedEvent(task, newFiles));
            }
            if (versionedFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new TaskEvents.TaskFilesVersionedEvent(task, versionedFiles));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadedFiles;
    }

    @Transactional
    public void deleteTaskFile(Integer taskId, Integer fileId) {
        PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentTaskFileSystemPath(taskId, fileId);
        fileSystemService.deleteDocumentFromDiskFolder(fileId, dir);
        if (taskFile.getParentFile() != null) {
            PLMTaskFile parent = taskFileRepository.findOne(taskFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = taskFileRepository.save(parent);
        }
        PLMTask task = taskRepository.findOne(taskId);
        applicationEventPublisher.publishEvent(new TaskEvents.TaskFileDeletedEvent(task, taskFile));
        taskFileRepository.delete(fileId);
    }

    @Transactional(readOnly = true)
    public PLMActivityFile getActivityFile(Integer fileId) {
        return activityFileRepository.findOne(fileId);
    }

    @Transactional(readOnly = true)
    public PLMTaskFile getTaskFile(Integer fileId) {
        return taskFileRepository.findOne(fileId);
    }

    @Transactional(readOnly = true)
    public File getActivityFile(Integer activityId, Integer fileId, String type) {
        checkNotNull(activityId);
        checkNotNull(fileId);
        PLMActivityFile activityFile = null;
        if (type.equals("COMMON")) {
            activityFile = activityFileRepository.findByActivityAndId(activityId, fileId);
        } else {
            activityFile = activityFileRepository.findByActivityAndIdAndLatestTrue(activityId, fileId);
        }
        if (activityFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(activityId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public File getActivityTaskFile(Integer taskId, Integer fileId, String type) {
        checkNotNull(fileId);
        PLMTaskFile taskFile = null;
        if (type.equals("COMMON")) {
            taskFile = taskFileRepository.findByTaskAndId(taskId, fileId);
        } else {
            taskFile = taskFileRepository.findByTaskAndIdAndLatestTrue(taskId, fileId);
        }
        if (taskFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentTaskFileSystemPath(taskId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<PLMActivityFile> getFileVersions(Integer projectId, Integer fileId) {
        PLMActivityFile activityFile = activityFileRepository.findByActivityAndIdAndLatestTrue(projectId, fileId);
        List<PLMActivityFile> activityFiles = activityFileRepository.findByActivityAndName(projectId, activityFile.getName());
        return activityFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMTaskFile> getTaskFileVersions(Integer activityId, Integer taskId, Integer fileId) {
        PLMTaskFile activityFile = taskFileRepository.findByTaskAndIdAndLatestTrue(taskId, fileId);
        List<PLMTaskFile> taskFiles = taskFileRepository.findByTaskAndName(taskId, activityFile.getName());
        return taskFiles;
    }

    @Transactional(readOnly = true)
    public PLMActivity getActivityPercentComplete(Integer plmActivity) {
        Double activityPercentComplete = 0.0;
        PLMActivity activity = activityRepository.findOne(plmActivity);
        List<PLMTask> plmTasks = taskRepository.findByActivity(activity.getId());
        if (plmTasks.size() != 0) {
            activityPercentComplete = 0.0;
            for (PLMTask task : plmTasks) {
                activityPercentComplete = activityPercentComplete + task.getPercentComplete();
            }
            activity.setPercentComplete((activityPercentComplete) / plmTasks.size());
        }
        return activity;
    }

    @Transactional(readOnly = true)
    public Page<PLMActivity> getActivityStructure(Integer personId, Pageable pageable) {
        Page<PLMActivity> activities = activityRepository.findByAssignedToAndNotStatus(personId, ProjectActivityStatus.FINISHED, pageable);
        if (activities.getContent().size() != 0) {
            for (PLMActivity activity : activities) {
                PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                List<PLMTask> taskList = new ArrayList<>();
                Person person = personRepository.findOne(activity.getAssignedTo());
                activity.setPerson(person);
                activity.setProject(wbsElement.getProject());
                List<PLMTask> activityTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
                if (activityTasks.size() != 0) {
                    activity.setLevel(0);
                    Double activityPercentComplete = 0.0;
                    for (PLMTask task : activityTasks) {
                        task.setLevel(activity.getLevel() + 1);
                        activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                        if (!task.getStatus().equals(ProjectTaskStatus.FINISHED)) {
                            taskList.add(task);
                        }
                    }
                    activity.setPercentComplete((activityPercentComplete) / activityTasks.size());
                    activity.setActivityTasks(taskList);
                }
            }
        }
        return activities;
    }

    @Transactional
    public PLMFileDownloadHistory fileDownloadHistory(Integer activityId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
        PLMActivity activity = activityRepository.findOne(activityFile.getActivity());
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileDownloadedEvent(activity, activityFile));
        return plmFileDownloadHistory;
    }

    @Transactional(readOnly = true)
    public PLMTaskFile getLatestUploadedTaskFile(Integer taskId, Integer fileId) {
        PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
        PLMTaskFile taskFile = taskFileRepository.findByTaskAndFileNoAndLatestTrue(plmTaskFile.getTask(), plmTaskFile.getFileNo());
        return taskFile;
    }

    @Transactional(readOnly = true)
    public PLMActivityFile getLatestUploadedFile(Integer taskId, Integer fileId) {
        PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
        PLMActivityFile activityFile = activityFileRepository.findByActivityAndFileNoAndLatestTrue(plmActivityFile.getActivity(), plmActivityFile.getFileNo());
        return activityFile;
    }

    @Transactional
    public PLMFileDownloadHistory taskFileDownloadHistory(Integer activityId, Integer taskId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
        PLMTask task = taskRepository.findOne(taskFile.getTask());
        applicationEventPublisher.publishEvent(new TaskEvents.TaskFileDownloadedEvent(task, taskFile));
        return plmFileDownloadHistory;
    }

    @Transactional
    public List<PLMTaskDeliverable> createTaskDeliverables(Integer activityId, Integer taskId, List<PLMTaskDeliverable> taskDeliverables) {
        String items = null;
        PLMActivity activity1 = null;
        PLMTask task = taskRepository.findOne(taskId);
        List<PLMItem> items1 = new ArrayList<>();
        for (PLMTaskDeliverable plmTaskDeliverable : taskDeliverables) {
            activity1 = activityRepository.findOne(task.getActivity());
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmTaskDeliverable.getItemRevision());
            PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
            items1.add(plmItem);
            if (items == null) {
                items = plmItem.getItemNumber();
            } else {
                items = items + " ," + plmItem.getItemNumber();
            }
        }
        applicationEventPublisher.publishEvent(new TaskEvents.TaskDeliverablesAddedEvent(task, items1));
        return taskDeliverableRepository.save(taskDeliverables);
    }

    @Transactional(readOnly = true)
    public List<PLMTaskDeliverable> getTaskDeliverables(Integer activityId, Integer taskId) {
        List<PLMTaskDeliverable> taskDeliverables = new ArrayList<>();
        List<PLMTaskDeliverable> deliverables = taskDeliverableRepository.findByTask(taskId);
        if (deliverables.size() != 0) {
            for (PLMTaskDeliverable taskDeliverable : deliverables) {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(taskDeliverable.getItemRevision());
                taskDeliverable.setRevision(itemRevision);
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                taskDeliverable.setItem(item);
                taskDeliverables.add(taskDeliverable);
            }
        }
        return taskDeliverables;
    }

    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getAllTaskDeliverables(Integer activityId, Integer taskId) {
        PLMProjectDeliverableDto taskDeliverableDto = new PLMProjectDeliverableDto();
        List<PLMTaskDeliverable> deliverables = taskDeliverableRepository.findByTask(taskId);
        if (deliverables.size() != 0) {
            for (PLMTaskDeliverable taskDeliverable : deliverables) {
                PLMDeliverable deliverable = deliverableRepository.findOne(taskDeliverable.getId());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                deliverable.setRevision(itemRevision);
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                PLMTask plmTask = taskRepository.findOne(taskDeliverable.getTask());
                if (plmTask.getAssignedTo() != null) {
                    Person person = personRepository.findOne(plmTask.getAssignedTo());
                    deliverable.setOwner(person.getFullName());
                    deliverable.setOwnerId(person.getId());
                }
                deliverable.setItem(item);
                deliverable.setObjectId(taskDeliverable.getTask());
                deliverable.setObjectType(PLMObjectType.PROJECTTASK.toString());
                taskDeliverableDto.getItemDeliverables().add(deliverable);
            }
        }
        List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(taskId);
        List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(taskId);
        List<RequirementDeliverable> requirementDeliverables = requirementDeliverableRepository.findByObjectId(taskId);
        taskDeliverableDto.getSpecificationDeliverables().addAll(specificationDeliverables);
        taskDeliverableDto.getRequirementDeliverables().addAll(requirementDeliverables);
        taskDeliverableDto.getGlossaryDeliverables().addAll(glossaryDeliverables);
        return taskDeliverableDto;
    }

    @Transactional(readOnly = true)
    public List<PLMActivityItemReference> getItemsByActivity(Integer activity) {
        List<PLMActivityItemReference> activityItemReferences = activityItemReferenceRepository.findByActivity(activity);
        for (PLMActivityItemReference activityItemReference : activityItemReferences) {
            PLMItem item = itemRepository.findOne(activityItemReference.getItem().getItemMaster());
            activityItemReference.setPlmItem(item);
        }
        return activityItemReferences;
    }

    @Transactional
    public void deleteActivityReferenceItem(Integer referenceItem) {
        List<PLMTaskItemReference> taskItemReferences = new ArrayList<>();
        PLMActivityItemReference reference = activityItemReferenceRepository.findOne(referenceItem);
        PLMActivity activity1 = activityRepository.findOne(reference.getActivity());
        PLMWbsElement activity = wbsElementRepository.findOne(activity1.getWbs());
        List<PLMTask> task = taskRepository.findByActivity(activity.getId());
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(reference.getItem().getId());
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        for (PLMTask task1 : task) {
            PLMTaskItemReference taskItemReference = taskItemReferenceRepository.findByTaskAndItem(task1.getId(), reference.getItem());
            if (taskItemReference != null) {
                taskItemReferences.add(taskItemReference);
                throw new CassiniException(messageSource.getMessage("reference_item_does_not_exist_in_tasks",
                        null, "Reference Item exit in tasks does not delete this item", LocaleContextHolder.getLocale()));
            }
        }
        if (taskItemReferences.size() == 0) {
            applicationEventPublisher.publishEvent(new ActivityEvents.ActivityReferenceItemDeletedEvent(activity1, item));
            activityItemReferenceRepository.delete(referenceItem);
        }
    }

    @Transactional
    public void deleteTaskDeliverable(Integer taskId, Integer deliverableId) {
        PLMTask task = taskRepository.findOne(taskId);
        PLMTaskDeliverable deliverable = taskDeliverableRepository.findOne(deliverableId);
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        applicationEventPublisher.publishEvent(new TaskEvents.TaskDeliverableDeletedEvent(task, item));
        taskDeliverableRepository.delete(deliverableId);
    }

    @Transactional
    public List<PLMActivityItemReference> createActivityItemReferences(Integer id, List<PLMActivityItemReference> plmActivityItemReferences) {
        List<PLMActivityItemReference> activityItemReferences = new ArrayList<>();
        String itemNumber = null;
        PLMActivity activity1 = activityRepository.findOne(id);
        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        for (PLMActivityItemReference activityItemReference : plmActivityItemReferences) {
            PLMActivityItemReference itemReference = new PLMActivityItemReference();
            PLMActivityItemReference existItem = activityItemReferenceRepository.findByActivityAndItem(activityItemReference.getActivity(), activityItemReference.getItem());
            if (existItem == null) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(activityItemReference.getItem().getId());
                PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                itemReference.setItem(activityItemReference.getItem());
                itemReference.setActivity(activityItemReference.getActivity());
                activityItemReferences.add(activityItemReferenceRepository.save(itemReference));
                ASNewMemberDTO asNewMemberDTO = new ASNewMemberDTO(itemReference.getId(), plmItem.getItemName());
                AsNewMemberDtos.add(asNewMemberDTO);
                if (itemNumber == null) {
                    itemNumber = plmItem.getItemNumber() + " - " + plmItem.getItemName();
                } else {
                    itemNumber = itemNumber + " , " + plmItem.getItemNumber() + " - " + plmItem.getItemName();
                }
            }
        }
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityReferenceItemsAddedEvent(activity1, AsNewMemberDtos));
        return activityItemReferences;
    }

    @Transactional
    public List<PLMTaskItemReference> createTaskItemReferences(List<PLMTaskItemReference> itemReferences) {
        List<PLMTaskItemReference> taskItemReferences = new ArrayList<>();
        String itemNumber = null;
        PLMTask task = null;
        PLMActivity activity1 = null;
        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        for (PLMTaskItemReference taskItemReference : itemReferences) {
            PLMTaskItemReference itemReference = new PLMTaskItemReference();
            PLMTaskItemReference existItem = taskItemReferenceRepository.findByTaskAndItem(taskItemReference.getTask(), taskItemReference.getItem());
            if (existItem == null) {
                task = taskRepository.findOne(taskItemReference.getTask());
                activity1 = activityRepository.findOne(task.getActivity());
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(taskItemReference.getItem().getId());
                PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                itemReference.setItem(taskItemReference.getItem());
                itemReference.setTask(taskItemReference.getTask());
                taskItemReferences.add(taskItemReferenceRepository.save(itemReference));
                ASNewMemberDTO asNewMemberDTO = new ASNewMemberDTO(taskItemReference.getId(), plmItem.getItemName());
                AsNewMemberDtos.add(asNewMemberDTO);
                if (itemNumber == null) {
                    itemNumber = plmItem.getItemNumber() + " - " + plmItem.getItemName();
                } else {
                    itemNumber = itemNumber + " , " + plmItem.getItemNumber() + " - " + plmItem.getItemName();
                }
            }
        }
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityReferenceItemsAddedEvent(activity1, AsNewMemberDtos));
        return taskItemReferences;
    }

    @Transactional(readOnly = true)
    public List<PLMTaskItemReference> getItemsByTask(Integer activity, Integer taskId) {
        List<PLMTaskItemReference> taskItemReferences = taskItemReferenceRepository.findByTask(taskId);
        for (PLMTaskItemReference taskItemReference : taskItemReferences) {
            PLMItem item = itemRepository.findOne(taskItemReference.getItem().getItemMaster());
            taskItemReference.setPlmItem(item);
        }
        return taskItemReferences;
    }

    @Transactional
    public void deleteTaskReferenceItem(Integer referenceItem) {
        PLMTaskItemReference reference = taskItemReferenceRepository.findOne(referenceItem);
        PLMTask task = taskRepository.findOne(reference.getTask());
        PLMActivity activity1 = activityRepository.findOne(task.getActivity());
        PLMWbsElement activity = wbsElementRepository.findOne(activity1.getWbs());
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(reference.getItem().getId());
        PLMItem item = itemRepository.findOne(plmItemRevision.getItemMaster());
        applicationEventPublisher.publishEvent(new TaskEvents.TaskReferenceItemDeletedEvent(task, item));
        taskItemReferenceRepository.delete(referenceItem);
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getActivityItems(Integer activityId, Pageable pageable) {
        List<PLMItem> plmItems = new ArrayList<>();
        PLMActivity activity = activityRepository.findOne(activityId);
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        List<PLMProjectItemReference> projectItemReferences = projectItemReferenceRepository.findByProject(project);
        List<Integer> itemIds = new ArrayList<>();
        if (projectItemReferences.size() > 0) {
            for (PLMProjectItemReference projectItemReference : projectItemReferences) {
                itemIds.add(projectItemReference.getItem().getItemMaster());
            }
        }
        List<PLMItem> items = new ArrayList<>();
        if (itemIds.size() > 0) {
            items = itemRepository.findByIdIn(itemIds);
        }
        List<PLMActivityItemReference> activityItemReferences = activityItemReferenceRepository.findByActivity(activityId);
        if (activityItemReferences.size() != 0) {
            for (PLMItem item : items) {
                Boolean exist = false;
                for (PLMActivityItemReference activityItemReference : activityItemReferences) {
                    if (activityItemReference.getItem().getId().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items);
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > plmItems.size() ? plmItems.size() : (start + pageable.getPageSize());
        Page<PLMItem> pages = new PageImpl<PLMItem>(plmItems.subList(start, end), pageable, plmItems.size());
        return pages;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getTaskItems(Integer taskId, Pageable pageable) {
        List<PLMItem> plmItems = new ArrayList<>();
        PLMTask task = taskRepository.findOne(taskId);
        List<PLMActivityItemReference> activityItemReferences = activityItemReferenceRepository.findByActivity(task.getActivity());
        List<Integer> itemIds = new ArrayList<>();
        if (activityItemReferences.size() > 0) {
            for (PLMActivityItemReference activityItemReference : activityItemReferences) {
                itemIds.add(activityItemReference.getItem().getItemMaster());
            }
        }
        List<PLMItem> items = new ArrayList<>();
        if (itemIds.size() > 0) {
            items = itemRepository.findByIdIn(itemIds);
        }
        List<PLMTaskItemReference> taskItemReferences = taskItemReferenceRepository.findByTask(taskId);
        if (taskItemReferences.size() != 0) {
            for (PLMItem item : items) {
                Boolean exist = false;
                for (PLMTaskItemReference taskItemReference : taskItemReferences) {
                    if (taskItemReference.getItem().getId().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items);
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > plmItems.size() ? plmItems.size() : (start + pageable.getPageSize());
        Page<PLMItem> pages = new PageImpl<PLMItem>(plmItems.subList(start, end), pageable, plmItems.size());
        return pages;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> searchActivityDeliverables(Integer activityId, Predicate predicate, PageRequest pageRequest) {
        List<PLMActivityDeliverable> plmActivityDeliverables = activityDeliverableRepository.findByActivity(activityId);
        List<PLMItem> plmItems = new ArrayList<>();
        pageRequest.setSize(pageRequest.getSize() + plmActivityDeliverables.size());
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        if (plmActivityDeliverables.size() != 0) {
            for (PLMItem item : items.getContent()) {
                Boolean exist = false;
                for (PLMActivityDeliverable activityDeliverable : plmActivityDeliverables) {
                    if (activityDeliverable.getItemRevision().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items.getContent());
        }
        Page<PLMItem> plmItems1 = new PageImpl<PLMItem>(plmItems, pageable, plmItems.size());
        return plmItems1;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> searchTaskDeliverables(Integer taskId, Predicate predicate, PageRequest pageRequest) {
        List<PLMTaskDeliverable> taskDeliverables = taskDeliverableRepository.findByTask(taskId);
        List<PLMItem> plmItems = new ArrayList<>();
        pageRequest.setSize(pageRequest.getSize() + taskDeliverables.size());
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        if (taskDeliverables.size() != 0) {
            for (PLMItem item : items.getContent()) {
                Boolean exist = false;
                for (PLMTaskDeliverable taskDeliverable : taskDeliverables) {
                    if (taskDeliverable.getItemRevision().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items.getContent());
        }
        Page<PLMItem> plmItems1 = new PageImpl<PLMItem>(plmItems, pageable, plmItems.size());
        return plmItems1;
    }

    @Transactional(readOnly = true)
    public List<PLMActivityFile> getActivityFileVersionAndCommentsAndDownloads(Integer itemId, Integer fileId, ObjectType objectType) {
        List<PLMActivityFile> activityFiles = new ArrayList<>();
        PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmActivityFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmActivityFile.getId());
        if (comments.size() > 0) {
            plmActivityFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmActivityFile.setDownloadHistories(fileDownloadHistories);
        }
        activityFiles.add(plmActivityFile);
        List<PLMActivityFile> files = activityFileRepository.findByActivityAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmActivityFile.getActivity(), plmActivityFile.getFileNo());
        if (files.size() > 0) {
            for (PLMActivityFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                activityFiles.add(file);
            }
        }
        return activityFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMTaskFile> getTaskFileVersionAndCommentsAndDownloads(Integer activityId, Integer taskId, Integer fileId, ObjectType objectType) {
        List<PLMTaskFile> taskFiles = new ArrayList<>();
        PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmTaskFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmTaskFile.getId());
        if (comments.size() > 0) {
            plmTaskFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmTaskFile.setDownloadHistories(fileDownloadHistories);
        }
        taskFiles.add(plmTaskFile);
        List<PLMTaskFile> files = taskFileRepository.findByTaskAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmTaskFile.getTask(), plmTaskFile.getFileNo());
        if (files.size() > 0) {
            for (PLMTaskFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                taskFiles.add(file);
            }
        }
        return taskFiles;
    }

    @Transactional
    public PLMActivityFile renameActivityFile(Integer id, Integer fileId, String newFileName) throws IOException {
        String oldFileDir = "";
        String dir = "";
        PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
        activityFile.setLatest(false);
        activityFile = activityFileRepository.save(activityFile);
        PLMActivityFile plmActivityFile = (PLMActivityFile) Utils.cloneObject(activityFile, PLMActivityFile.class);
        PLMActivityFile oldFile = (PLMActivityFile) Utils.cloneObject(activityFile, PLMActivityFile.class);
        if (plmActivityFile != null) {
            plmActivityFile.setId(null);
            plmActivityFile.setName(newFileName);
            plmActivityFile.setVersion(activityFile.getVersion() + 1);
            plmActivityFile.setLatest(true);
            plmActivityFile.setReplaceFileName(activityFile.getName() + " ReName to " + newFileName);
            plmActivityFile = activityFileRepository.save(plmActivityFile);
            if (plmActivityFile.getParentFile() != null) {
                PLMActivityFile parent = activityFileRepository.findOne(plmActivityFile.getParentFile());
                parent.setModifiedDate(plmActivityFile.getModifiedDate());
                parent = activityFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(activityFile.getId(), plmActivityFile.getId());
            if (activityFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(id, fileId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            dir = dir + File.separator + plmActivityFile.getId();
            if (plmActivityFile.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(id, fileId);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id + File.separator + fileId;
            }
        }
        if (!dir.equals("")) {
            File fDir = new File(dir);
            if (!fDir.exists()) {
                try {
                    fDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMActivity activity = activityRepository.findOne(plmActivityFile.getActivity());
            applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileRenamedEvent(activity, "Rename", oldFile, plmActivityFile));
        }

        return plmActivityFile;

    }

    @Transactional
    public PLMTaskFile renameTaskFile(Integer id, Integer fileId, String newFileName) throws IOException {
        String oldFileDir = "";
        String dir = "";
        PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
        taskFile.setLatest(false);
        taskFile = taskFileRepository.save(taskFile);
        PLMTaskFile plmTaskFile = (PLMTaskFile) Utils.cloneObject(taskFile, PLMTaskFile.class);
        PLMTaskFile oldFile = (PLMTaskFile) Utils.cloneObject(taskFile, PLMTaskFile.class);
        if (plmTaskFile != null) {
            plmTaskFile.setId(null);
            plmTaskFile.setName(newFileName);
            plmTaskFile.setVersion(taskFile.getVersion() + 1);
            plmTaskFile.setLatest(true);
            plmTaskFile.setReplaceFileName(taskFile.getName() + " ReName to " + newFileName);
            plmTaskFile = taskFileRepository.save(plmTaskFile);
            if (plmTaskFile.getParentFile() != null) {
                PLMTaskFile parent = taskFileRepository.findOne(plmTaskFile.getParentFile());
                parent.setModifiedDate(plmTaskFile.getModifiedDate());
                parent = taskFileRepository.save(parent);
            }
            qualityFileService.copyFileAttributes(taskFile.getId(), plmTaskFile.getId());
            dir = "";
            if (taskFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceTaskFileSystemPath(id, fileId);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            dir = dir + File.separator + plmTaskFile.getId();
            oldFileDir = "";
            if (plmTaskFile.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentTaskFileSystemPath(id, fileId);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id + File.separator + fileId;
            }
        }

        if (!dir.equals("")) {
            File fDir = new File(dir);
            if (!fDir.exists()) {
                try {
                    fDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMTask task = taskRepository.findOne(plmTaskFile.getTask());
            applicationEventPublisher.publishEvent(new TaskEvents.TaskFileRenamedEvent(task, "Rename", oldFile, plmTaskFile));
        }

        return plmTaskFile;
    }

    @Transactional(readOnly = true)
    public Page<PLMActivity> getPersonActivitys(Integer personId, Pageable pageable) {
        Page<PLMActivity> activities = activityRepository.findByAssignedTo(personId, pageable);
        for (PLMActivity activity : activities) {
            List<PLMTask> activityTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
            if (activityTasks.size() != 0) {
                Double activityPercentComplete = 0.0;
                for (PLMTask task : activityTasks) {
                    activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                }
                activity.setActivityTasks(activityTasks);
                activity.setPercentComplete((activityPercentComplete) / activityTasks.size());
            }
        }
        return activities;
    }

    @Transactional
    public Page<PLMTask> getPersonActivityTasks(Integer personId, Pageable pageable) {
        Page<PLMTask> tasks = taskRepository.findByAssignedTo(personId, pageable);
        return tasks;
    }

    @Transactional
    public List<PLMActivityFile> replaceActivityFiles(Integer activityId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMActivityFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        String htmlTable = null;
        boolean itemFileNotExist = false;
        String action = null;
        PLMActivity activity1 = activityRepository.findOne(activityId);
        PLMWbsElement activity = wbsElementRepository.findOne(activity1.getWbs());
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String name = null;
        PLMActivityFile plmActivityFile = null;
        PLMActivityFile oldFile = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    PLMActivityFile activityFile = null;
                    plmActivityFile = activityFileRepository.findOne(fileId);
                    if (plmActivityFile != null && plmActivityFile.getParentFile() != null) {
                        activityFile = activityFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        activityFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(activityId, name);
                    }

                    if (plmActivityFile != null) {
                        plmActivityFile.setLatest(false);
                        plmActivityFile = activityFileRepository.save(plmActivityFile);
                        oldFile = JsonUtils.cloneEntity(plmActivityFile, PLMActivityFile.class);
                    }

                    activityFile = new PLMActivityFile();
                    activityFile.setName(name);
                    if (plmActivityFile != null && plmActivityFile.getParentFile() != null) {
                        activityFile.setParentFile(plmActivityFile.getParentFile());
                    }
                    if (plmActivityFile != null) {
                        activityFile.setFileNo(plmActivityFile.getFileNo());
                        activityFile.setVersion(plmActivityFile.getVersion() + 1);
                        activityFile.setReplaceFileName(plmActivityFile.getName() + " Replaced to " + name);
                    }
                    activityFile.setCreatedBy(login.getPerson().getId());
                    activityFile.setModifiedBy(login.getPerson().getId());
                    activityFile.setActivity(activityId);
                    activityFile.setSize(file.getSize());
                    activityFile.setFileType("FILE");
                    activityFile = activityFileRepository.save(activityFile);
                    if (activityFile.getParentFile() != null) {
                        PLMActivityFile parent = activityFileRepository.findOne(activityFile.getParentFile());
                        parent.setModifiedDate(activityFile.getModifiedDate());
                        parent = activityFileRepository.save(parent);
                    }
                    if (plmActivityFile != null) {
                        qualityFileService.copyFileAttributes(plmActivityFile.getId(), activityFile.getId());
                    }

                    String dir = "";
                    if (plmActivityFile != null && plmActivityFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(activityId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + activityId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + activityFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(activityFile);
                    /* App Events */
                    applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFileRenamedEvent(activity1, "Replace", oldFile, activityFile));
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional
    public List<PLMTaskFile> replaceTaskFiles(Integer taskId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMTaskFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMTask task = taskRepository.findOne(taskId);
        Login login = sessionWrapper.getSession().getLogin();
        String htmlTable = null;
        boolean itemFileNotExist = false;
        String action = null;
        PLMActivity activity1 = activityRepository.findOne(task.getActivity());
        PLMWbsElement activity = wbsElementRepository.findOne(activity1.getWbs());
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String name = null;
        PLMTaskFile plmTaskFile = null;
        PLMTaskFile oldFile = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    PLMTaskFile taskFile = null;
                    plmTaskFile = taskFileRepository.findOne(fileId);
                    if (plmTaskFile != null && plmTaskFile.getParentFile() != null) {
                        taskFile = taskFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        taskFile = taskFileRepository.findByTaskAndNameAndParentFileIsNullAndLatestTrue(taskId, name);
                    }

                    if (plmTaskFile != null) {
                        plmTaskFile.setLatest(false);
                        plmTaskFile = taskFileRepository.save(plmTaskFile);
                        oldFile = JsonUtils.cloneEntity(plmTaskFile, PLMTaskFile.class);
                    }

                    taskFile = new PLMTaskFile();
                    taskFile.setName(name);
                    if (plmTaskFile != null && plmTaskFile.getParentFile() != null) {
                        taskFile.setParentFile(plmTaskFile.getParentFile());
                    }
                    if (plmTaskFile != null) {
                        taskFile.setFileNo(plmTaskFile.getFileNo());
                        taskFile.setVersion(plmTaskFile.getVersion() + 1);
                        taskFile.setReplaceFileName(plmTaskFile.getName() + " Replaced to " + name);
                    }
                    taskFile.setCreatedBy(login.getPerson().getId());
                    taskFile.setModifiedBy(login.getPerson().getId());
                    taskFile.setTask(taskId);
                    taskFile.setSize(file.getSize());
                    taskFile.setFileType("FILE");
                    taskFile = taskFileRepository.save(taskFile);
                    if (taskFile.getParentFile() != null) {
                        PLMTaskFile parent = taskFileRepository.findOne(taskFile.getParentFile());
                        parent.setModifiedDate(taskFile.getModifiedDate());
                        parent = taskFileRepository.save(parent);
                    }
                    if (plmTaskFile != null) {
                        qualityFileService.copyFileAttributes(plmTaskFile.getId(), taskFile.getId());
                    }
                    String dir = "";
                    if (plmTaskFile != null && plmTaskFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceTaskFileSystemPath(taskId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + taskId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + taskFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(taskFile);
                    applicationEventPublisher.publishEvent(new TaskEvents.TaskFileRenamedEvent(task, "Replace", oldFile, taskFile));
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional
    public PLMFile updateFile(Integer id, PLMActivityFile plmActivityFile) {
        PLMFile file = fileRepository.findOne(id);
        saveFile(plmActivityFile, file);
        return file;
    }

    @Transactional
    public PLMFile updateTaskFile(Integer id, PLMTaskFile plmTaskFile) {
        PLMFile file = fileRepository.findOne(id);
        saveFile(plmTaskFile, file);
        return file;
    }

    private void saveFile(PLMFile plmActivityFile, PLMFile file) {
        if (file != null) {
            file.setDescription(plmActivityFile.getDescription());
            file.setLocked(plmActivityFile.getLocked());
            file.setLockedBy(plmActivityFile.getLockedBy());
            file.setLockedDate(plmActivityFile.getLockedDate());
            file = fileRepository.save(file);
        }
    }

    private String getReplaceFileSystemPath(Integer activityId, Integer fileId) {
        String path = "";
        PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
        if (plmActivityFile.getParentFile() != null) {
            path = utilService.visitParentFolder(activityId, plmActivityFile.getParentFile(), path);
        } else {
            path = File.separator + activityId;
        }
        return path;
    }


    private String getReplaceTaskFileSystemPath(Integer taskId, Integer fileId) {
        String path = "";
        PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
        if (plmTaskFile.getParentFile() != null) {
            path = visitParentTaskFolder(taskId, plmTaskFile.getParentFile(), path);
        } else {
            path = File.separator + taskId;
        }
        return path;
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMActivityFile projectFile = activityFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }

    private String getParentTaskFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (taskFile.getParentFile() != null) {
            path = visitParentTaskFolder(itemId, taskFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + taskFile.getId();
        }
        return path;
    }

    private String visitParentTaskFolder(Integer itemId, Integer fileId, String path) {
        PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
        if (taskFile.getParentFile() != null) {
            path = File.separator + taskFile.getId() + path;
            path = visitParentTaskFolder(itemId, taskFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + taskFile.getId() + path;
            return path;
        }
        return path;
    }

    @Transactional
    public PLMActivityFile createActivityFolder(Integer activityId, PLMActivityFile plmActivityFile) {
        plmActivityFile.setId(null);
        String folderNumber = null;
        PLMActivity activity = activityRepository.findOne(activityId);
        PLMActivityFile existFolderName = null;
        if (plmActivityFile.getParentFile() != null) {
            existFolderName = activityFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(plmActivityFile.getName(), plmActivityFile.getParentFile(), activityId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmActivityFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmActivityFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = activityFileRepository.findByNameEqualsIgnoreCaseAndActivityAndLatestTrue(plmActivityFile.getName(), activityId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmActivityFile.getName(), existFolderName.getName());
                throw new CassiniException(result);
            }
        }

        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);

        plmActivityFile.setActivity(activityId);
        plmActivityFile.setFileNo(folderNumber);
        plmActivityFile.setFileType("FOLDER");
        plmActivityFile = activityFileRepository.save(plmActivityFile);
        if (plmActivityFile.getParentFile() != null) {
            PLMActivityFile parent = activityFileRepository.findOne(plmActivityFile.getParentFile());
            parent.setModifiedDate(plmActivityFile.getModifiedDate());
            parent = activityFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(activityId, plmActivityFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFoldersAddedEvent(activity, plmActivityFile));
        return plmActivityFile;
    }

    @Transactional
    public List<PLMActivityFile> uploadActivityFolderFiles(Integer activityId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMActivityFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMActivity activity = activityRepository.findOne(activityId);
        Login login = sessionWrapper.getSession().getLogin();
        String fileNames = null;
        PLMActivityFile projectFile = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                projectFile = activityFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                if (projectFile != null) {
                    comments = commentRepository.findAllByObjectId(projectFile.getId());
                }
                Integer version = 1;
                String autoNumber1 = null;
                if (projectFile != null) {
                    projectFile.setLatest(false);
                    Integer oldVersion = projectFile.getVersion();
                    version = oldVersion + 1;
                    autoNumber1 = projectFile.getFileNo();
                    activityFileRepository.save(projectFile);
                }
                if (projectFile == null) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                }
                projectFile = new PLMActivityFile();
                projectFile.setName(name);
                projectFile.setFileNo(autoNumber1);
                projectFile.setFileType("FILE");
                projectFile.setParentFile(fileId);
                projectFile.setCreatedBy(login.getPerson().getId());
                projectFile.setModifiedBy(login.getPerson().getId());
                projectFile.setActivity(activity.getId());
                projectFile.setVersion(version);
                projectFile.setSize(file.getSize());
                projectFile.setFileType("FILE");
                projectFile = activityFileRepository.save(projectFile);
                if (fileNames == null) {
                    fNames = projectFile.getName();
                    fileNames = projectFile.getName() + " - Version : " + projectFile.getVersion();
                } else {
                    fNames = fNames + " , " + projectFile.getName();
                    fileNames = fileNames + " , " + projectFile.getName() + " - Version : " + projectFile.getVersion();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(activityId, fileId);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + projectFile.getId();
                fileSystemService.saveDocumentToDisk(file, path);
                /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                if (map != null) {
                    projectFile.setUrn(map.get("urn"));
                    projectFile.setThumbnail(map.get("thumbnail"));
                    projectFile = activityFileRepository.save(projectFile);
                }*/
                uploaded.add(projectFile);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uploaded;
    }

    @Transactional
    public PLMFile moveActivityFileToFolder(Integer id, PLMActivityFile plmActivityFile) throws Exception {
        PLMActivityFile file = activityFileRepository.findOne(plmActivityFile.getId());
        PLMActivityFile existFile = (PLMActivityFile) Utils.cloneObject(file, PLMActivityFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getActivity(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getActivity() + File.separator + existFile.getId();
        }
        if (plmActivityFile.getParentFile() != null) {
            PLMActivityFile existItemFile = activityFileRepository.findByParentFileAndNameAndLatestTrue(plmActivityFile.getParentFile(), plmActivityFile.getName());
            PLMActivityFile folder = activityFileRepository.findOne(plmActivityFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmActivityFile = activityFileRepository.save(plmActivityFile);
            }
        } else {
            PLMActivityFile existItemFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(plmActivityFile.getActivity(), plmActivityFile.getName());
            PLMActivity plmActivity = activityRepository.findOne(plmActivityFile.getActivity());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_activity", null, "{0} file already exist in {1} activity", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), plmActivity.getName());
                throw new CassiniException(result);
            } else {
                plmActivityFile = activityFileRepository.save(plmActivityFile);
            }
        }
        if (plmActivityFile != null) {
            String dir = "";
            if (plmActivityFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmActivityFile.getActivity(), plmActivityFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmActivityFile.getActivity() + File.separator + plmActivityFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<PLMActivityFile> oldVersionFiles = activityFileRepository.findByActivityAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getActivity(), existFile.getFileNo());
            for (PLMActivityFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getActivity(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getActivity() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmActivityFile.getActivity(), plmActivityFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmActivityFile.getParentFile());
                oldVersionFile = activityFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return plmActivityFile;
    }

    @Transactional
    public PLMFile moveActivityTaskFileToFolder(Integer id, PLMTaskFile plmTaskFile) throws Exception {
        PLMTaskFile file = taskFileRepository.findOne(plmTaskFile.getId());
        PLMTaskFile existFile = (PLMTaskFile) Utils.cloneObject(file, PLMTaskFile.class);
        PLMTask task = taskRepository.findOne(plmTaskFile.getTask());
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentTaskFileSystemPath(existFile.getTask(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getTask() + File.separator + existFile.getId();
        }
        if (plmTaskFile.getParentFile() != null) {
            PLMTaskFile existItemFile = taskFileRepository.findByParentFileAndNameAndLatestTrue(plmTaskFile.getParentFile(), plmTaskFile.getName());
            PLMTaskFile folder = taskFileRepository.findOne(plmTaskFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmTaskFile = taskFileRepository.save(plmTaskFile);
            }
        } else {
            PLMTaskFile existItemFile = taskFileRepository.findByTaskAndNameAndParentFileIsNullAndLatestTrue(plmTaskFile.getId(), plmTaskFile.getName());
            PLMTask plmTask = taskRepository.findOne(plmTaskFile.getTask());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_task", null, "{0} file already exist in {1} task", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), plmTask.getName());
                throw new CassiniException(result);
            } else {
                plmTaskFile = taskFileRepository.save(plmTaskFile);
            }
        }
        if (plmTaskFile != null) {
            String dir = "";
            if (plmTaskFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentTaskFileSystemPath(plmTaskFile.getTask(), plmTaskFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmTaskFile.getTask() + File.separator + plmTaskFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<PLMTaskFile> oldVersionFiles = taskFileRepository.findByTaskAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getTask(), existFile.getFileNo());
            for (PLMTaskFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentTaskFileSystemPath(oldVersionFile.getTask(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getTask() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceTaskFileSystemPath(plmTaskFile.getTask(), plmTaskFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmTaskFile.getParentFile());
                oldVersionFile = taskFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }

                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return plmTaskFile;
    }

    @Transactional(readOnly = true)
    public List<PLMActivityFile> getActivityFolderChildren(Integer folderId) {
        List<PLMActivityFile> projectFiles = activityFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        projectFiles.forEach(changeFile -> {
            changeFile.setParentObject(PLMObjectType.PROJECTACTIVITY);
            if (changeFile.getFileType().equals("FOLDER")) {
                changeFile.setCount(activityFileRepository.getChildrenCountByParentFileAndLatestTrue(changeFile.getId()));
                changeFile.setCount(changeFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(changeFile.getActivity(), changeFile.getId()));
            }
        });
        return projectFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMTaskFile> getActivityTaskFolderChildren(Integer folderId) {
        List<PLMTaskFile> taskFiles = taskFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        taskFiles.forEach(taskFile -> {
            taskFile.setParentObject(PLMObjectType.PROJECTTASK);
            if (taskFile.getFileType().equals("FOLDER")) {
                taskFile.setCount(taskFileRepository.getChildrenCountByParentFileAndLatestTrue(taskFile.getId()));
                taskFile.setCount(taskFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(taskFile.getTask(), taskFile.getId()));
            }
        });
        return taskFiles;
    }

    @Transactional
    public void deleteFolder(Integer activityId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(activityId, folderId);
        List<PLMActivityFile> projectFiles = activityFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) projectFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        PLMActivity activity = activityRepository.findOne(activityId);
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityFoldersDeletedEvent(activity, file));
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        activityFileRepository.delete(folderId);
    }

    @Transactional
    public void deleteActivityTaskFolder(Integer taskId, Integer folderId) {
        PLMTask task = taskRepository.findOne(taskId);
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + getParentTaskFileSystemPath(taskId, folderId);
        List<PLMTaskFile> taskFiles = taskFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) taskFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        applicationEventPublisher.publishEvent(new TaskEvents.TaskFoldersDeletedEvent(task, file));
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        taskFileRepository.delete(folderId);
    }

    @Transactional
    public PLMTaskFile createActivityTaskFolder(Integer taskId, PLMTaskFile plmTaskFile) {
        plmTaskFile.setId(null);
        String folderNumber = null;
        PLMTask task = taskRepository.findOne(taskId);
        PLMTaskFile existFolderName = null;
        if (plmTaskFile.getParentFile() != null) {
            existFolderName = taskFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndTaskAndLatestTrue(plmTaskFile.getName(), plmTaskFile.getParentFile(), taskId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmTaskFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmTaskFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = taskFileRepository.findByNameEqualsIgnoreCaseAndTaskAndLatestTrue(plmTaskFile.getName(), taskId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmTaskFile.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmTaskFile.setTask(taskId);
        plmTaskFile.setFileNo(folderNumber);
        plmTaskFile.setFileType("FOLDER");
        plmTaskFile.setSize(0L);
        plmTaskFile = taskFileRepository.save(plmTaskFile);
        if (plmTaskFile.getParentFile() != null) {
            PLMTaskFile parent = taskFileRepository.findOne(plmTaskFile.getParentFile());
            parent.setModifiedDate(plmTaskFile.getModifiedDate());
            parent = taskFileRepository.save(parent);
        }
        PLMActivity activity = activityRepository.findOne(task.getActivity());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentTaskFileSystemPath(taskId, plmTaskFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new TaskEvents.TaskFoldersAddedEvent(task, plmTaskFile));
        return plmTaskFile;
    }

    @Transactional
    public List<PLMTaskFile> uploadActivityTaskFolderFiles(Integer activityId, Integer taskId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMTaskFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMTask task = taskRepository.findOne(taskId);
        Login login = sessionWrapper.getSession().getLogin();
        String fileNames = null;
        PLMTaskFile taskFile = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                taskFile = taskFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                if (taskFile != null) {
                    comments = commentRepository.findAllByObjectId(taskFile.getId());
                }
                Integer version = 1;
                String autoNumber1 = null;
                if (taskFile != null) {
                    taskFile.setLatest(false);
                    Integer oldVersion = taskFile.getVersion();
                    version = oldVersion + 1;
                    autoNumber1 = taskFile.getFileNo();
                    taskFile = taskFileRepository.save(taskFile);
                }
                if (taskFile == null) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                }
                taskFile = new PLMTaskFile();
                taskFile.setName(name);
                taskFile.setFileNo(autoNumber1);
                taskFile.setFileType("FILE");
                taskFile.setParentFile(fileId);
                taskFile.setCreatedBy(login.getPerson().getId());
                taskFile.setModifiedBy(login.getPerson().getId());
                taskFile.setTask(task.getId());
                taskFile.setVersion(version);
                taskFile.setSize(file.getSize());
                taskFile = taskFileRepository.save(taskFile);
                if (fileNames == null) {
                    fNames = taskFile.getName();
                    fileNames = taskFile.getName() + " - Version : " + taskFile.getVersion();
                } else {
                    fNames = fNames + " , " + taskFile.getName();
                    fileNames = fileNames + " , " + taskFile.getName() + " - Version : " + taskFile.getVersion();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentTaskFileSystemPath(taskId, fileId);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + taskFile.getId();
                fileSystemService.saveDocumentToDisk(file, path);
                /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                if (map != null) {
                    taskFile.setUrn(map.get("urn"));
                    taskFile.setThumbnail(map.get("thumbnail"));
                    taskFile = taskFileRepository.save(taskFile);
                }*/
                uploaded.add(taskFile);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uploaded;
    }

    @Transactional(readOnly = true)
    public DetailsCount getActivityDetailsCount(Integer activityId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(activityFileRepository.findByActivityAndFileTypeAndLatestTrueOrderByModifiedDateDesc(activityId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(activityId));
        PLMProjectDeliverableDto projectDeliverableDto = getAllActivtiyDeliverables(activityId);
        PLMProjectDeliverableDto projectDeliverableDto1 = getActivitySpecDeliverables(activityId);
        PLMProjectDeliverableDto projectDeliverableDto2 = getActivityReqDeliverables(activityId);
        detailsCount.setDeliverables(projectDeliverableDto.getItemDeliverables().size() + projectDeliverableDto.getGlossaryDeliverables().size() + projectDeliverableDto1.getSpecificationDeliverables().size() + projectDeliverableDto2.getRequirementDeliverables().size());
        detailsCount.setReferenceItems(activityItemReferenceRepository.findByActivity(activityId).size());
        detailsCount.setTasks(taskRepository.findByActivity(activityId).size());
        return detailsCount;
    }

    @Transactional(readOnly = true)
    public DetailsCount getTaskDetailsCount(Integer taskId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(taskFileRepository.findByTaskAndFileTypeAndLatestTrueOrderByModifiedDateDesc(taskId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(taskId));
        PLMProjectDeliverableDto projectDeliverableDto = getAllTaskDeliverables(0, taskId);
        detailsCount.setDeliverables(projectDeliverableDto.getItemDeliverables().size() + projectDeliverableDto.getGlossaryDeliverables().size() + projectDeliverableDto.getSpecificationDeliverables().size() + projectDeliverableDto.getRequirementDeliverables().size());
        detailsCount.setReferenceItems(taskItemReferenceRepository.findByTask(taskId).size());
        return detailsCount;
    }

    @Transactional
    public Page<PLMTask> getAssignedTasks(Integer personId, Pageable pageable) {
        Page<PLMTask> tasks = taskRepository.getActivityAndNotStatus(personId, ProjectTaskStatus.FINISHED, pageable);
        for (PLMTask task : tasks) {
            PLMActivity activity = activityRepository.findOne(task.getActivity());
            PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
            task.setActivityName(activity.getName());
            task.setProject(wbsElement.getProject().getName());
        }
        return tasks;
    }

    @Transactional
    public void generateZipFile(Integer Id, HttpServletResponse response, String type) throws IOException {
        if (type.equals("ACTIVITY")) {
            PLMActivity plmActivity = activityRepository.findOne(Id);
            List<PLMActivityFile> plmActivityFiles = activityFileRepository.findByActivityAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Id);
            ArrayList<String> fileList = new ArrayList<>();
            plmActivityFiles.forEach(plmActivityFile -> {
                File file = getActivityFile(Id, plmActivityFile.getId(), "LATEST");
                fileList.add(file.getAbsolutePath());
            });
            String zipName = plmActivity.getName() + "_Files.zip";
            File zipBox = new File(zipName);
            if (zipBox.exists())
                zipBox.delete();
            fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "ACTIVITY",Id);
            InputStream inputStream = new FileInputStream(zipBox.getPath());
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
            response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
            response.getOutputStream().flush();
            inputStream.close();
        }
        if (type.equals("TASKS")) {
            PLMTask plmTask = taskRepository.findOne(Id);
            List<PLMTaskFile> plmTaskFiles = taskFileRepository.findByTaskAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Id);
            ArrayList<String> fileList = new ArrayList<>();
            plmTaskFiles.forEach(plmTaskFile -> {
                File file = getActivityTaskFile(Id, plmTaskFile.getId(), "LATEST");
                fileList.add(file.getAbsolutePath());
            });
            String zipName = plmTask.getName() + "_Files.zip";
            File zipBox = new File(zipName);
            if (zipBox.exists())
                zipBox.delete();
            fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "TASKS",Id);
            InputStream inputStream = new FileInputStream(zipBox.getPath());
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
            response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
            response.getOutputStream().flush();
            inputStream.close();
        }
    }

    @Transactional
    public WBSDto updateWbsChildSeq(Integer actualId, Integer targetId) {
        PLMActivity actualRowObject = activityRepository.findOne(actualId);
        PLMActivity targetRowObject = activityRepository.findOne(targetId);

        WBSDto actualRow = new WBSDto();
        WBSDto targetRow = new WBSDto();
        Integer wbsId = null;
        if (actualRowObject != null && actualRowObject.getObjectType().equals(ObjectType.valueOf("PROJECTACTIVITY"))) {
            PLMActivity activity = activityRepository.findOne(actualId);
            actualRow.setId(activity.getId());
            actualRow.setSequenceNumber(activity.getSequenceNumber());
            actualRow.setObjectType(activity.getObjectType().toString());
            wbsId = activity.getWbs();
        } else {
            PLMMilestone milestone = milestoneRepository.findOne(actualId);
            actualRow.setId(milestone.getId());
            actualRow.setSequenceNumber(milestone.getSequenceNumber());
            actualRow.setObjectType(milestone.getObjectType().toString());
            wbsId = milestone.getWbs();
        }
        if (targetRowObject != null && targetRowObject.getObjectType().equals(ObjectType.valueOf("PROJECTACTIVITY"))) {
            PLMActivity activity = activityRepository.findOne(targetId);
            targetRow.setId(activity.getId());
            targetRow.setSequenceNumber(activity.getSequenceNumber());
            targetRow.setObjectType(activity.getObjectType().toString());
        } else {
            PLMMilestone milestone = milestoneRepository.findOne(targetId);
            targetRow.setId(milestone.getId());
            targetRow.setSequenceNumber(milestone.getSequenceNumber());
            targetRow.setObjectType(milestone.getObjectType().toString());
        }
        List<WBSDto> wbsDtoList = getWbsSequenceList(wbsId);
        if ((actualRow.getSequenceNumber() > targetRow.getSequenceNumber())) {
            for (WBSDto wbsElement : wbsDtoList) {
                if (targetRow.getId().equals(wbsElement.getId()) || actualRow.getId().equals(wbsElement.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() < wbsElement.getSequenceNumber()) && (actualRow.getSequenceNumber() > wbsElement.getSequenceNumber())) {
                        if (wbsElement.getObjectType().equals("PROJECTACTIVITY")) {
                            PLMActivity activity = activityRepository.findOne(wbsElement.getId());
                            activity.setSequenceNumber(wbsElement.getSequenceNumber() + 1);
                            activity = activityRepository.save(activity);
                        } else {
                            PLMMilestone milestone = milestoneRepository.findOne(wbsElement.getId());
                            milestone.setSequenceNumber(wbsElement.getSequenceNumber() + 1);
                            milestone = milestoneRepository.save(milestone);
                        }
                    }
                }
            }
            if (actualRow.getObjectType().equals("PROJECTACTIVITY")) {
                PLMActivity activity = activityRepository.findOne(actualRow.getId());
                activity.setSequenceNumber(targetRow.getSequenceNumber());
                activity = activityRepository.save(activity);
            } else {
                PLMMilestone milestone = milestoneRepository.findOne(actualRow.getId());
                milestone.setSequenceNumber(targetRow.getSequenceNumber());
                milestone = milestoneRepository.save(milestone);
            }
            if (targetRow.getObjectType().equals("PROJECTACTIVITY")) {
                PLMActivity activity = activityRepository.findOne(targetRow.getId());
                activity.setSequenceNumber(targetRow.getSequenceNumber() + 1);
                activity = activityRepository.save(activity);
            } else {
                PLMMilestone milestone = milestoneRepository.findOne(targetRow.getId());
                milestone.setSequenceNumber(targetRow.getSequenceNumber() + 1);
                milestone = milestoneRepository.save(milestone);
            }

        } else {
            for (WBSDto wbsElement : wbsDtoList) {
                if (targetRow.getId().equals(wbsElement.getId()) || actualRow.getId().equals(wbsElement.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() > wbsElement.getSequenceNumber()) && (actualRow.getSequenceNumber() < wbsElement.getSequenceNumber())) {
                        if (wbsElement.getObjectType().equals("PROJECTACTIVITY")) {
                            PLMActivity activity = activityRepository.findOne(wbsElement.getId());
                            activity.setSequenceNumber(wbsElement.getSequenceNumber() - 1);
                            activity = activityRepository.save(activity);
                        } else {
                            PLMMilestone milestone = milestoneRepository.findOne(wbsElement.getId());
                            milestone.setSequenceNumber(wbsElement.getSequenceNumber() - 1);
                            milestone = milestoneRepository.save(milestone);
                        }
                    }
                }
            }
            if (actualRow.getObjectType().equals("PROJECTACTIVITY")) {
                PLMActivity activity = activityRepository.findOne(actualRow.getId());
                activity.setSequenceNumber(targetRow.getSequenceNumber());
                activity = activityRepository.save(activity);
            } else {
                PLMMilestone milestone = milestoneRepository.findOne(actualRow.getId());
                milestone.setSequenceNumber(targetRow.getSequenceNumber());
                milestone = milestoneRepository.save(milestone);
            }
            if (targetRow.getObjectType().equals("PROJECTACTIVITY")) {
                PLMActivity activity = activityRepository.findOne(targetRow.getId());
                activity.setSequenceNumber(targetRow.getSequenceNumber() - 1);
                activity = activityRepository.save(activity);
            } else {
                PLMMilestone milestone = milestoneRepository.findOne(targetRow.getId());
                milestone.setSequenceNumber(targetRow.getSequenceNumber() - 1);
                milestone = milestoneRepository.save(milestone);
            }
        }
        return actualRow;
    }

    private List<WBSDto> getWbsSequenceList(Integer wbsId) {
        List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsId);
        List<PLMMilestone> milestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsId);
        List<WBSDto> wbsDtoList = new ArrayList<>();
        activities.forEach(plmActivity -> {
            setValuesWBSDto(plmActivity.getId(), plmActivity.getSequenceNumber(), plmActivity.getObjectType().toString(), wbsDtoList);
        });
        milestones.forEach(plmMilestone -> {
            setValuesWBSDto(plmMilestone.getId(), plmMilestone.getSequenceNumber(), plmMilestone.getObjectType().toString(), wbsDtoList);
        });
        Collections.sort(wbsDtoList, new Comparator<WBSDto>() {
            public int compare(final WBSDto object1, final WBSDto object2) {
                return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
            }
        });
        return wbsDtoList;
    }

    private void setValuesWBSDto(Integer id, Integer seqNum, String str, List<WBSDto> wbsDtoList) {
        WBSDto wbsDto = new WBSDto();
        wbsDto.setId(id);
        wbsDto.setSequenceNumber(seqNum);
        wbsDto.setObjectType(str);
        wbsDtoList.add(wbsDto);
    }

    @Transactional
    public PLMTask updateActivityChildSeq(Integer activityId, Integer actualId, Integer targetId) {
        PLMTask actualRow = taskRepository.findOne(actualId);
        PLMTask targetRow = taskRepository.findOne(targetId);
        List<PLMTask> tasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activityId);
        if ((actualRow.getSequenceNumber() > targetRow.getSequenceNumber())) {
            for (PLMTask task : tasks) {
                if (targetRow.getId().equals(task.getId()) || actualRow.getId().equals(task.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() < task.getSequenceNumber()) && (actualRow.getSequenceNumber() > task.getSequenceNumber())) {
                        task.setSequenceNumber(task.getSequenceNumber() + 1);
                        task = taskRepository.save(task);
                    }
                }
            }
            actualRow.setSequenceNumber(targetRow.getSequenceNumber());
            actualRow = taskRepository.save(actualRow);
            targetRow.setSequenceNumber(targetRow.getSequenceNumber() + 1);
            targetRow = taskRepository.save(targetRow);
        } else {
            for (PLMTask task : tasks) {
                if (targetRow.getId().equals(task.getId()) || actualRow.getId().equals(task.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() > task.getSequenceNumber()) && (actualRow.getSequenceNumber() < task.getSequenceNumber())) {
                        task.setSequenceNumber(task.getSequenceNumber() - 1);
                        task = taskRepository.save(task);
                    }
                }
            }
            actualRow.setSequenceNumber(targetRow.getSequenceNumber());
            actualRow = taskRepository.save(actualRow);
            targetRow.setSequenceNumber(targetRow.getSequenceNumber() - 1);
            targetRow = taskRepository.save(targetRow);
        }
        return actualRow;
    }

    @Transactional
    public List<PLMActivityFile> pasteFromClipboardToActivity(Integer activityId, Integer fileId, List<PLMFile> files) {
        List<PLMActivityFile> fileList = new ArrayList<>();
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PLMActivityFile activityFile = new PLMActivityFile();
                PLMActivityFile existFile = null;
                if (fileId != 0) {
                    activityFile.setParentFile(fileId);
                    existFile = activityFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(file.getName(), fileId, activityId);
                } else {
                    existFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(activityId, file.getName());
                }
                if (existFile == null) {
                    activityFile.setName(file.getName());
                    activityFile.setDescription(file.getDescription());
                    activityFile.setActivity(activityId);
                    activityFile.setVersion(1);
                    activityFile.setSize(file.getSize());
                    activityFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    activityFile.setFileNo(autoNumber1);
                    activityFile.setFileType("FILE");
                    activityFile = activityFileRepository.save(activityFile);
                    activityFile.setParentObject(PLMObjectType.PROJECTACTIVITY);
                    fileList.add(activityFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + activityId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (activityFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(activityId, activityFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + activityId + File.separator + activityFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                }
            } else if (file.getFileType().equals("FOLDER") && canCreate) {
                PLMActivityFile projectFile = new PLMActivityFile();
                PLMActivityFile existFile = null;
                if (fileId != 0) {
                    projectFile.setParentFile(fileId);
                    existFile = activityFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(file.getName(), fileId, activityId);
                } else {
                    existFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(activityId, file.getName());
                }
                if (existFile == null) {
                    projectFile.setName(file.getName());
                    projectFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    projectFile.setVersion(1);
                    projectFile.setSize(0L);
                    projectFile.setActivity(activityId);
                    projectFile.setFileNo(folderNumber);
                    projectFile.setFileType("FOLDER");
                    projectFile = activityFileRepository.save(projectFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + activityId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(activityId, projectFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(projectFile);
                    copyFolderFiles(activityId, file.getParentObject(), file.getId(), projectFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer project, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> projectFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMActivityFile activityFile = new PLMActivityFile();
            activityFile.setParentFile(parent);
            activityFile.setName(plmProjectFile.getName());
            activityFile.setDescription(plmProjectFile.getDescription());
            activityFile.setActivity(project);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                activityFile.setVersion(1);
                activityFile.setFileNo(folderNumber);
                activityFile.setSize(plmProjectFile.getSize());
                activityFile.setFileType("FILE");
                activityFile = activityFileRepository.save(activityFile);
                activityFile.setParentObject(PLMObjectType.PROJECTACTIVITY);
                plmProjectFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmProjectFile);

                String dir = "";
                if (activityFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(project, activityFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + project + File.separator + activityFile.getId();
                }
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    try {
                        fDir.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            } else {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                activityFile.setVersion(1);
                activityFile.setSize(0L);
                activityFile.setFileNo(folderNumber);
                activityFile.setFileType("FOLDER");
                activityFile = activityFileRepository.save(activityFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + project;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(project, activityFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(project, objectType, plmProjectFile.getId(), activityFile.getId());
            }
        });
    }

    private void copyTaskFolderFiles(Integer project, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> projectFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMTaskFile taskFile = new PLMTaskFile();
            taskFile.setParentFile(parent);
            taskFile.setName(plmProjectFile.getName());
            taskFile.setDescription(plmProjectFile.getDescription());
            taskFile.setTask(project);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                taskFile.setVersion(1);
                taskFile.setFileNo(folderNumber);
                taskFile.setSize(plmProjectFile.getSize());
                taskFile.setFileType("FILE");
                taskFile = taskFileRepository.save(taskFile);
                taskFile.setParentObject(PLMObjectType.PROJECTTASK);
                plmProjectFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmProjectFile);

                String dir = "";
                if (taskFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentTaskFileSystemPath(project, taskFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + project + File.separator + taskFile.getId();
                }
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    try {
                        fDir.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileInputStream instream = null;
                FileOutputStream outstream = null;
                Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            } else {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                taskFile.setVersion(1);
                taskFile.setSize(0L);
                taskFile.setFileNo(folderNumber);
                taskFile.setFileType("FOLDER");
                taskFile = taskFileRepository.save(taskFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + project;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentTaskFileSystemPath(project, taskFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyTaskFolderFiles(project, objectType, plmProjectFile.getId(), taskFile.getId());
            }
        });
    }


    @Transactional
    public List<PLMTaskFile> pasteFromClipboardToTask(Integer taskId, Integer fileId, List<PLMFile> files) {
        List<PLMTaskFile> fileList = new ArrayList<>();
        List<Integer> folderIds = new ArrayList<>();
        files.forEach(file -> {
            if (file.getFileType().equals("FOLDER")) {
                folderIds.add(file.getId());
            }
        });
        for (PLMFile file : files) {
            Boolean canCreate = true;
            if (file.getParentFile() != null && folderIds.indexOf(file.getParentFile()) != -1) {
                canCreate = false;
            }
            if (file.getFileType().equals("FILE") && canCreate) {
                PLMTaskFile taskFile = new PLMTaskFile();
                PLMTaskFile existFile = null;
                if (fileId != 0) {
                    taskFile.setParentFile(fileId);
                    existFile = taskFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndTaskAndLatestTrue(file.getName(), fileId, taskId);
                } else {
                    existFile = taskFileRepository.findByTaskAndNameAndParentFileIsNullAndLatestTrue(taskId, file.getName());
                }
                if (existFile == null) {
                    taskFile.setName(file.getName());
                    taskFile.setDescription(file.getDescription());
                    taskFile.setTask(taskId);
                    taskFile.setVersion(1);
                    taskFile.setSize(file.getSize());
                    taskFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    taskFile.setFileNo(autoNumber1);
                    taskFile.setFileType("FILE");
                    taskFile = taskFileRepository.save(taskFile);
                    taskFile.setParentObject(PLMObjectType.PROJECTTASK);
                    fileList.add(taskFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + taskId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (taskFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentTaskFileSystemPath(taskId, taskFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + taskId + File.separator + taskFile.getId();
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        try {
                            fDir.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
                }
            } else if (file.getFileType().equals("FOLDER") && canCreate) {
                PLMTaskFile taskFile = new PLMTaskFile();
                PLMActivityFile existFile = null;
                if (fileId != 0) {
                    taskFile.setParentFile(fileId);
                    existFile = activityFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndActivityAndLatestTrue(file.getName(), fileId, taskId);
                } else {
                    existFile = activityFileRepository.findByActivityAndNameAndParentFileIsNullAndLatestTrue(taskId, file.getName());
                }
                if (existFile == null) {
                    taskFile.setName(file.getName());
                    taskFile.setDescription(file.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    taskFile.setVersion(1);
                    taskFile.setSize(0L);
                    taskFile.setTask(taskId);
                    taskFile.setFileNo(folderNumber);
                    taskFile.setFileType("FOLDER");
                    taskFile = taskFileRepository.save(taskFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + taskId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentTaskFileSystemPath(taskId, taskFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(taskFile);
                    copyTaskFolderFiles(taskId, file.getParentObject(), file.getId(), taskFile.getId());
                }
            }
        }
        return fileList;
    }

    @Transactional
    public PLMProjectDeliverableDto pasteActivityDeliverables(Integer activityId, PLMProjectDeliverableDto deliverableDto) {
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> glossaryIds = new ArrayList<>();
        List<Integer> specIds = new ArrayList<>();
        List<Integer> reqIds = new ArrayList<>();
        deliverableDto.getItemIds().forEach(itemId -> {
            PLMActivityDeliverable deliverable = activityDeliverableRepository.findByActivityAndItemRevision(activityId, itemId);
            if (deliverable == null) {
                deliverable = new PLMActivityDeliverable();
                deliverable.setActivity(activityId);
                deliverable.setDeliverableStatus(DeliverableStatus.PENDING);
                deliverable.setItemRevision(itemId);
                deliverable = activityDeliverableRepository.save(deliverable);
                itemIds.add(deliverable.getId());
            }
        });
        deliverableDto.getGlossaryIds().forEach(glossaryId -> {
            PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
            PLMGlossaryDeliverable deliverable = glossaryDeliverableRepository.findByObjectIdAndGlossary(activityId, glossary);
            if (deliverable == null) {
                deliverable = new PLMGlossaryDeliverable();
                deliverable.setObjectId(activityId);
                deliverable.setObjectType("PROJECTACTIVITY");
                deliverable.setGlossary(glossary);
                deliverable = glossaryDeliverableRepository.save(deliverable);
                glossaryIds.add(deliverable.getId());
            }
        });
        deliverableDto.getRequirementIds().forEach(requirementId -> {
            Requirement requirement = requirementRepository.findOne(requirementId);
            RequirementDeliverable deliverable = requirementDeliverableRepository.findByObjectIdAndRequirement(activityId, requirement);
            if (deliverable == null) {
                deliverable = new RequirementDeliverable();
                deliverable.setObjectId(activityId);
                deliverable.setObjectType("PROJECTACTIVITY");
                deliverable.setRequirement(requirement);
                deliverable = requirementDeliverableRepository.save(deliverable);
                reqIds.add(deliverable.getId());
            }
        });
        deliverableDto.getSpecIds().forEach(specificationId -> {
            Specification specification = specificationRepository.findOne(specificationId);
            SpecificationDeliverable deliverable = specificationDeliverableRepository.findByObjectIdAndSpecification(activityId, specification);
            if (deliverable == null) {
                deliverable = new SpecificationDeliverable();
                deliverable.setObjectId(activityId);
                deliverable.setObjectType("PROJECTACTIVITY");
                deliverable.setSpecification(specification);
                deliverable = specificationDeliverableRepository.save(deliverable);
                specIds.add(deliverable.getId());
            }
        });
        deliverableDto.setItemIds(itemIds);
        deliverableDto.setGlossaryIds(glossaryIds);
        deliverableDto.setSpecIds(specIds);
        deliverableDto.setRequirementIds(reqIds);
        return deliverableDto;
    }

    @Transactional
    public void undoActivityDeliverables(Integer activityId, PLMProjectDeliverableDto deliverableDto) {
        for (Integer itemId : deliverableDto.getItemIds()) {
            activityDeliverableRepository.delete(itemId);
        }
        for (Integer glossaryId : deliverableDto.getGlossaryIds()) {
            glossaryDeliverableRepository.delete(glossaryId);
        }
        for (Integer requirementId : deliverableDto.getRequirementIds()) {
            requirementDeliverableRepository.delete(requirementId);
        }
        for (Integer specificationId : deliverableDto.getSpecIds()) {
            specificationDeliverableRepository.delete(specificationId);
        }
    }

    @Transactional
    public PLMProjectDeliverableDto pasteTaskDeliverables(Integer taskId, PLMProjectDeliverableDto deliverableDto) {
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> glossaryIds = new ArrayList<>();
        List<Integer> specIds = new ArrayList<>();
        List<Integer> reqIds = new ArrayList<>();
        deliverableDto.getItemIds().forEach(itemId -> {
            PLMTaskDeliverable deliverable = taskDeliverableRepository.findByTaskAndItemRevision(taskId, itemId);
            if (deliverable == null) {
                deliverable = new PLMTaskDeliverable();
                deliverable.setTask(taskId);
                deliverable.setDeliverableStatus(DeliverableStatus.PENDING);
                deliverable.setItemRevision(itemId);
                deliverable = taskDeliverableRepository.save(deliverable);
                itemIds.add(deliverable.getId());
            }
        });
        deliverableDto.getGlossaryIds().forEach(glossaryId -> {
            PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
            PLMGlossaryDeliverable deliverable = glossaryDeliverableRepository.findByObjectIdAndGlossary(taskId, glossary);
            if (deliverable == null) {
                deliverable = new PLMGlossaryDeliverable();
                deliverable.setObjectId(taskId);
                deliverable.setObjectType("PROJECTTASK");
                deliverable.setGlossary(glossary);
                deliverable = glossaryDeliverableRepository.save(deliverable);
                glossaryIds.add(deliverable.getId());
            }
        });
        deliverableDto.getRequirementIds().forEach(requirementId -> {
            Requirement requirement = requirementRepository.findOne(requirementId);
            RequirementDeliverable deliverable = requirementDeliverableRepository.findByObjectIdAndRequirement(taskId, requirement);
            if (deliverable == null) {
                deliverable = new RequirementDeliverable();
                deliverable.setObjectId(taskId);
                deliverable.setObjectType("PROJECTTASK");
                deliverable.setRequirement(requirement);
                deliverable = requirementDeliverableRepository.save(deliverable);
                reqIds.add(deliverable.getId());
            }
        });
        deliverableDto.getSpecIds().forEach(specificationId -> {
            Specification specification = specificationRepository.findOne(specificationId);
            SpecificationDeliverable deliverable = specificationDeliverableRepository.findByObjectIdAndSpecification(taskId, specification);
            if (deliverable == null) {
                deliverable = new SpecificationDeliverable();
                deliverable.setObjectId(taskId);
                deliverable.setObjectType("PROJECTTASK");
                deliverable.setSpecification(specification);
                deliverable = specificationDeliverableRepository.save(deliverable);
                specIds.add(deliverable.getId());
            }
        });
        deliverableDto.setItemIds(itemIds);
        deliverableDto.setGlossaryIds(glossaryIds);
        deliverableDto.setSpecIds(specIds);
        deliverableDto.setRequirementIds(reqIds);
        return deliverableDto;
    }

    @Transactional
    public void undoTaskDeliverables(Integer taskId, PLMProjectDeliverableDto deliverableDto) {
        for (Integer itemId : deliverableDto.getItemIds()) {
            taskDeliverableRepository.delete(itemId);
        }
        for (Integer glossaryId : deliverableDto.getGlossaryIds()) {
            glossaryDeliverableRepository.delete(glossaryId);
        }
        for (Integer requirementId : deliverableDto.getRequirementIds()) {
            requirementDeliverableRepository.delete(requirementId);
        }
        for (Integer specificationId : deliverableDto.getSpecIds()) {
            specificationDeliverableRepository.delete(specificationId);
        }
    }

    @Transactional
    public void undoCopiedActivityFiles(Integer activityId, List<PLMActivityFile> activityFiles) {
        activityFiles.forEach(plmActivityFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(activityId, plmActivityFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(plmActivityFile.getId(), dir);
            activityFileRepository.delete(plmActivityFile.getId());
        });
    }

    @Transactional
    public void undoCopiedTaskFiles(Integer taskId, List<PLMTaskFile> taskFiles) {
        taskFiles.forEach(taskFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentTaskFileSystemPath(taskId, taskFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(taskFile.getId(), dir);
            taskFileRepository.delete(taskFile.getId());
        });
    }

    @Transactional
    public PLMActivity finishActivity(Integer activityId) throws JsonProcessingException {
        PLMActivity activity = activityRepository.findOne(activityId);
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
        PLMProject project = wbsElement.getProject();
        List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
        if (tasks.size() > 0) {
            for (PLMTask task : tasks) {
                task.setPercentComplete(100.0);
                task.setStatus(ProjectTaskStatus.FINISHED);
                task = taskRepository.save(task);
                ASNewTaskDTO asNewTaskDTO = new ASNewTaskDTO(wbsElement.getName(), activity.getName(), task.getName());
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectTaskFinishedEvent(wbsElement.getProject(), asNewTaskDTO));
                applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectTaskFinishedEvent(wbsElement.getProject(), task));
                projectService.sendProjectSubscribeNotification(project, task.getName(), "taskFinished");
            }
        }
        activity.setPercentComplete(100.0);
        activity.setStatus(ProjectActivityStatus.FINISHED);
        activityRepository.save(activity);
        ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), activity.getName());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityFinishedEvent(wbsElement.getProject(), asNewActivityAndMilestoneDTO));
        applicationEventPublisher.publishEvent(new UserTaskEvents.ProjectActivityFinishedEvent(wbsElement.getProject(), activity));
        projectService.sendProjectSubscribeNotification(project, activity.getName(), "activityFinished");
        return activity;
    }

    @Transactional
    public PLMWorkflow attachActivityWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMActivity activity = activityRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        if (activity != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTACTIVITY, activity.getId(), wfDef);
            activity.setWorkflow(workflow.getId());
            activityRepository.save(activity);
        }
        return workflow;
    }

    @Transactional
    public PLMWorkflow attachNewActivityWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMActivity activity = activityRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (activity != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTACTIVITY, activity.getId(), wfDef);
            activity.setWorkflow(workflow.getId());
            activityRepository.save(activity);
            PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
            PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
            //applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityWorkflowChangeEvent(project, activity, null, workflow));
            if (workflow1 != null) {
                applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowChangeEvent(activity, workflow1, workflow));
            } else {
                applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowChangeEvent(activity, null, workflow));
            }
        }
        return workflow;
    }

    @Transactional
    public PLMDeliverable finishActivityAndTaskDeliverable(String type, PLMDeliverable plmDeliverable) {
        PLMDeliverable plmDeliverable1 = deliverableRepository.findOne(plmDeliverable.getId());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmDeliverable.getItemRevision());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
        if (plmDeliverable1.getCriteria() == null) {
            plmDeliverable1.setDeliverableStatus(DeliverableStatus.FINISHED);
            plmDeliverable = deliverableRepository.save(plmDeliverable1);
            if (type.equals("ACTIVITY")) {
                PLMActivityDeliverable deliverable = activityDeliverableRepository.findOne(plmDeliverable.getId());
                PLMActivity activity = activityRepository.findOne(deliverable.getActivity());
                applicationEventPublisher.publishEvent(new ActivityEvents.ActivityDeliverableFinishedEvent(activity, plmItem));
            } else {
                PLMTaskDeliverable deliverable = taskDeliverableRepository.findOne(plmDeliverable.getId());
                PLMTask task = taskRepository.findOne(deliverable.getTask());
                applicationEventPublisher.publishEvent(new TaskEvents.TaskDeliverableFinishedEvent(task, plmItem));
            }

        } else {
            throw new CassiniException(messageSource.getMessage("cannot_finish_deliverable",
                    null, "You cannot finish this deliverable", LocaleContextHolder.getLocale()));
        }
        return plmDeliverable;
    }

    @Transactional(readOnly = true)
    public List<PLMActivityFile> getActivityFilesByName(Integer id, String name) {
        return activityFileRepository.findByActivityAndNameContainingIgnoreCaseAndLatestTrue(id, name);
    }

    @Transactional(readOnly = true)
    public List<PLMTaskFile> getTaskFilesByName(Integer id, String name) {
        return taskFileRepository.findByTaskAndNameContainingIgnoreCaseAndLatestTrue(id, name);
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void activityWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowStartedEvent(activity, event.getPlmWorkflow()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void activityWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowPromotedEvent(activity, plmWorkflow, fromStatus, toStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void activityWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowDemotedEvent(activity, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void activityWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowFinishedEvent(activity, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void itemWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowHoldEvent(activity, plmWorkflow, fromStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTACTIVITY'")
    public void activityWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMActivity activity = (PLMActivity) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ActivityEvents.ActivityWorkflowUnholdEvent(activity, plmWorkflow, fromStatus));
    }


    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) {
        PLMTask task = (PLMTask) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowStartedEvent(task, event.getPlmWorkflow()));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) {
        PLMTask task = (PLMTask) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowPromotedEvent(task, plmWorkflow, fromStatus, toStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMTask task = (PLMTask) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowDemotedEvent(task, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) {
        PLMTask task = (PLMTask) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowFinishedEvent(task, plmWorkflow));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) {
        PLMTask task = (PLMTask) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowHoldEvent(task, plmWorkflow, fromStatus));
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECTTASK'")
    public void taskWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) {
        PLMTask task = (PLMTask) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowUnholdEvent(task, plmWorkflow, fromStatus));
    }

    @Transactional(readOnly = true)
    public TaskDto getTaskDetails(Integer taskId) {
        TaskDto taskDto = new TaskDto();

        PLMTask task = taskRepository.findOne(taskId);

        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setPercentComplete(task.getPercentComplete());
        taskDto.setAssignedTo(task.getAssignedTo());
        taskDto.setSequenceNumber(task.getSequenceNumber());
        taskDto.setRequired(task.getRequired());
        if (task.getAssignedTo() != null) {
            taskDto.setAssignedToPerson(personRepository.findOne(task.getAssignedTo()).getFullName());
        }
        taskDto.setCreatedDate(task.getCreatedDate());
        taskDto.setModifiedDate(task.getModifiedDate());
        taskDto.setCreatedByPerson(personRepository.findOne(task.getCreatedBy()).getFullName());
        taskDto.setModifiedByPerson(personRepository.findOne(task.getModifiedBy()).getFullName());
        taskDto.setActivityName(activityRepository.findOne(task.getActivity()).getName());
        taskDto.setActivity(task.getActivity());
        taskDto.setCreatedBy(task.getCreatedBy());
        taskDto.setModifiedBy(task.getModifiedBy());
        taskDto.setObjectType(task.getObjectType());
        taskDto.setPlannedStartDate(task.getPlannedStartDate());
        taskDto.setPlannedFinishDate(task.getPlannedFinishDate());
        taskDto.setActualStartDate(task.getActualStartDate());
        taskDto.setActualFinishDate(task.getActualFinishDate());
        return taskDto;
    }

    @Transactional(readOnly = true)
    public ActivityDto getActivityDetails(Integer activityId) {
        ActivityDto activityDto = new ActivityDto();

        PLMActivity activity = activityRepository.findOne(activityId);

        activityDto.setId(activity.getId());
        activityDto.setName(activity.getName());
        activityDto.setDescription(activity.getDescription());
        activityDto.setStatus(activity.getStatus());
        if (activity.getAssignedTo() != null) {
            activityDto.setAssignedToPerson(personRepository.findOne(activity.getAssignedTo()).getFullName());
        }
        activityDto.setCreatedDate(activity.getCreatedDate());
        activityDto.setModifiedDate(activity.getModifiedDate());
        activityDto.setCreatedByPerson(personRepository.findOne(activity.getCreatedBy()).getFullName());
        activityDto.setModifiedByPerson(personRepository.findOne(activity.getModifiedBy()).getFullName());
        activityDto.setPlannedStartDate(activity.getPlannedStartDate());
        activityDto.setPlannedFinishDate(activity.getPlannedFinishDate());
        activityDto.setActualStartDate(activity.getActualStartDate());
        activityDto.setActualFinishDate(activity.getActualFinishDate());
        return activityDto;
    }

    @Transactional
    public PLMWorkflow attachTaskWorkflow(Integer id, Integer wfDefId) {
        PLMWorkflow workflow = null;
        PLMTask task = taskRepository.findOne(id);
        PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
        PLMWorkflow workflow1 = plmWorkflowRepository.findByAttachedTo(id);
        if (workflow1 != null) {
            workflowService.deleteWorkflow(id);
        }
        if (task != null && wfDef != null) {
            workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTTASK, task.getId(), wfDef);
            task.setWorkflow(workflow.getId());
            taskRepository.save(task);
            //PLMActivity activity = activityRepository.findOne(task.getActivity());
          /*  PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
            PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());*/
            //applicationEventPublisher.publishEvent(new ProjectEvents.ProjectActivityWorkflowChangeEvent(project, task, null, workflow));
            if (workflow1 != null) {
                applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowChangeEvent(task, workflow1, workflow));
            } else {
                applicationEventPublisher.publishEvent(new TaskEvents.TaskWorkflowChangeEvent(task, null, workflow));
            }
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<FileDto> convertActivityFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<PLMActivityFile> files = activityFileRepository.findByIdIn(fileIds);
        List<PLMActivityFile> plmFiles = activityFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = activityFileRepository.getFileNosByIds(fileIds);
        List<PLMActivityFile> fileNoFiles = activityFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<PLMActivityFile> fileCountList = activityFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
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
        List<Integer> foldersList = activityFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = activityFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertActivityFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertActivityFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }


    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<PLMTaskFile> files = taskFileRepository.findByIdIn(fileIds);
        List<PLMTaskFile> plmFiles = taskFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = taskFileRepository.getFileNosByIds(fileIds);
        List<PLMTaskFile> fileNoFiles = taskFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<PLMTaskFile> fileCountList = taskFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
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

    public FileDto visitTaskFileChildren(Integer object, PLMObjectType objectType, FileDto fileDto, Boolean hierarchy) {
        List<Integer> foldersList = taskFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = taskFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

}
