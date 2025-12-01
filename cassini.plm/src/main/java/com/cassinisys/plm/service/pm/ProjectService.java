package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.activitystream.ASPropertyChangeDTO;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SecurityPermissionService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ProgramEvents;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.filtering.*;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.dto.SubscribeMailDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.model.wf.*;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.req.PLMProjectRequirementDocumentRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.*;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.activitystream.ProjectActivityStream;
import com.cassinisys.plm.service.activitystream.dto.ASNewFileDTO;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import com.cassinisys.plm.service.activitystream.dto.ASReqDocoumentDto;
import com.cassinisys.plm.service.activitystream.dto.ASVersionedFileDTO;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.cassinisys.plm.service.wf.PLMWorkflowStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by swapna on 1/2/18.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ProjectDeliveravbleRepository projectDeliveravbleRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private ProjectPredicateBuilder predicateBuilder;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectTemplateWbsRepository projectTemplateWbsRepository;
    @Autowired
    private ProjectTemplateActivityRepository projectTemplateActivityRepository;
    @Autowired
    private ProjectTemplateMilestoneRepository projectTemplateMilestoneRepository;
    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProjectTemplateService projectTemplateService;
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private GlossaryRepository glossaryRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private GlossaryDeliverableRepository glossaryDeliverableRepository;

    @Autowired
    private ProjectItemDeliverableBuilder projectItemDeliverableBuilder;

    @Autowired
    private ProjectGlossaryDeliverableBuilder projectGlossaryDeliverableBuilder;

    @Autowired
    private ActivityItemReferenceRepository activityItemReferenceRepository;

    @Autowired
    private ActivityDeliverableRepository activityDeliverableRepository;

    @Autowired
    private ActivityFileRepository activityFileRepository;

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private TaskDeliverableRepository taskDeliverableRepository;

    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;

    @Autowired
    private ProjectEmailSettingsRepository projectEmailSettingsRepository;

    @Autowired
    private SpecificationDeliverableRepository specificationDeliverableRepository;

    @Autowired
    private RequirementDeliverableRepository requirementDeliverableRepository;

    @Autowired
    private ProjectTemplateTaskRepository projectTemplateTaskRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ProjectTemplateTaskFileRepository projectTemplateTaskFileRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private ProjectItemReferenceRepository projectItemReferenceRepository;

    @Autowired
    private FileHelpers fileHelpers;

    @Autowired
    private ItemFileService itemFileService;

    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private LinksRepository linksRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ProjectActivityStream projectActivityStream;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStartRepository;

    @Autowired
    private PLMWorkflowStatusService plmWorkflowStatusService;

    @Autowired
    private PLMWorkflowTransitionRepository workflowTransitionRepository;

    @Autowired
    private PLMWorkflowFinishRepository plmWorkflowFinishRepository;

    @Autowired
    private PLMWorkflowStatusHistoryRepository plmWorkflowStatusHistoryRepository;

    @Autowired
    private SecurityPermissionService securityPermissionService;

    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;

    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;

    @Autowired
    private ObjectFileService qualityFileService;

    @Autowired
    private ProjectTemplateMemberRepository projectTemplateMemberRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMProjectRequirementDocumentRepository projectRequirementDocumentRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private PMObjectTypeAttributeRepository pmObjectTypeAttributeRepository;
    @Autowired
    private PMObjectTypeService pmObjectTypeService;
    @Autowired
    private ProjectTemplateFileRepository projectTemplateFileRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private ProjectTemplateActivityFileRepository projectTemplateActivityFileRepository;
    /*    CRUD operations for Project    */

    @Transactional
    @PreAuthorize("hasPermission(#plmProject,'create')")
    public PLMProject createProject(PLMProject plmProject) {
        Integer workflowDef = null;
        Integer program = plmProject.getProgram();
        PLMProject existProject = null;
        if (program == null) {
            existProject = projectRepository.findByNameEqualsIgnoreCaseAndProgramIsNull(plmProject.getName());
        } else {
            existProject = projectRepository.findByNameEqualsIgnoreCaseAndProgram(plmProject.getName(), program);
        }
        if (existProject != null) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existProject.getName());
            throw new CassiniException(result);
        }
        Integer programProject = plmProject.getProgramProject();
        if (plmProject.getWorkflowDefId() != null) {
            workflowDef = plmProject.getWorkflowDefId();
        }
        PLMProject project = projectRepository.save(plmProject);
        if (workflowDef != null) {
            attachProjectWorkflow(project.getId(), workflowDef, null);
        }
        if (program != null && programProject != null) {
            PLMProgramProject plmProgramProject = new PLMProgramProject();
            plmProgramProject.setProgram(program);
            plmProgramProject.setParent(programProject);
            plmProgramProject.setProject(project.getId());
            plmProgramProject.setType(ProgramProjectType.PROJECT);
            plmProgramProject = programProjectRepository.save(plmProgramProject);
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectAddedEvent(plmProgramProject.getProgram(), plmProgramProject));
        } else {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectCreatedEvent(project));
        }
        return project;

    }

    @Transactional
    public PLMProject createTemplateProject(PLMProject plmProject, Integer template) {
        Integer program = plmProject.getProgram();
        ProjectTemplate template1 = projectTemplateRepository.findOne(template);
        Integer programProject = plmProject.getProgramProject();
        PMObjectType activityType = null;
        List<PMObjectType> objectTypes = pmObjectTypeRepository.findByTypeAndParentIsNullOrderByIdAsc(PMType.PROJECTACTIVITY);
        if (objectTypes.size() > 0) {
            activityType = objectTypes.get(0);
        }

        Integer workflowDef = null;
        if (plmProject.getWorkflowDefId() != null) {
            workflowDef = plmProject.getWorkflowDefId();
        }
        PLMProject existProject = null;
        if (program == null) {
            existProject = projectRepository.findByNameEqualsIgnoreCaseAndProgramIsNull(plmProject.getName());
        } else {
            existProject = projectRepository.findByNameEqualsIgnoreCaseAndProgram(plmProject.getName(), program);
        }
        if (existProject != null) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existProject.getName());
            throw new CassiniException(result);
        }
        PLMProject project = projectRepository.save(plmProject);

        List<ProjectTemplateWbs> wbsList = projectTemplateWbsRepository.findByTemplate(template);
        if (plmProject.getTeam()) {
            List<ProjectTemplateMember> projectTemplateMembers = projectTemplateMemberRepository.findByTemplate(template);
            List<PLMProjectMember> projectMembers = new LinkedList<>();
            for (ProjectTemplateMember templateMember : projectTemplateMembers) {
                PLMProjectMember projectMember = new PLMProjectMember();
                projectMember.setProject(project.getId());
                projectMember.setPerson(templateMember.getPerson());
                projectMember.setRole(templateMember.getRole());
                projectMembers.add(projectMember);
                //projectMember = projectMemberRepository.save(projectMember);
            }
            if (projectMembers.size() > 0) {
                projectMemberRepository.save(projectMembers);
            }
        }
        Integer count = 0;
        if (wbsList.size() != 0) {
            for (ProjectTemplateWbs templateWbs : wbsList) {
                PLMWbsElement wbsElement = new PLMWbsElement();
                wbsElement.setSequenceNumber(count + 1);
                wbsElement.setName(templateWbs.getName());
                wbsElement.setDescription(templateWbs.getDescription());
                wbsElement.setProject(project);
                wbsElement = wbsElementRepository.save(wbsElement);
                count++;
                List<ProjectTemplateActivity> templateActivities = projectTemplateActivityRepository.findByWbsOrderByCreatedDateAsc(templateWbs.getId());
                if (templateActivities.size() != 0) {
                    Integer activityCount = 0;
                    for (ProjectTemplateActivity templateActivity : templateActivities) {
                        PLMActivity existActivity = activityRepository.findByWbsAndNameEqualsIgnoreCase(wbsElement.getId(), templateActivity.getName());
                        if (existActivity == null) {
                            PLMActivity activity = new PLMActivity();
                            activity.setName(templateActivity.getName());
                            activity.setSequenceNumber(activityCount + 1);
                            activity.setDescription(templateActivity.getDescription());
                            activity.setWbs(wbsElement.getId());
                            activity.setType(activityType);
                            activity.setStatus(ProjectActivityStatus.PENDING);
                            if (plmProject.getAssignedTo()) {
                                activity.setAssignedTo(templateActivity.getAssignedTo());
                            }
                            activity = activityRepository.save(activity);
                            copyTemplateFoldersToActivity(plmProject, templateActivity.getId(), activity.getId());
                            if (templateActivity.getWorkflow() != null && plmProject.getCopyWorkflows() && (plmProject.getAllWorkflows() || plmProject.getActivityWorkflows())) {
                                attachProjectActivityWorkflow(activity.getId(), null, templateActivity.getWorkflow());
                            }
                            activityCount++;
                            createActivityTasks(templateActivity, activity, plmProject);
                        }
                    }
                }
                List<ProjectTemplateMilestone> templateMilestones = projectTemplateMilestoneRepository.findByWbsOrderByCreatedDateAsc(templateWbs.getId());
                if (templateMilestones.size() != 0) {
                    Integer milestoneCount = 0;
                    List<PLMMilestone> plmMilestones = new LinkedList<>();
                    for (ProjectTemplateMilestone templateMilestone : templateMilestones) {
                        PLMMilestone existMilestone = milestoneRepository.findByWbsAndNameEqualsIgnoreCase(wbsElement.getId(), templateMilestone.getName());
                        if (existMilestone == null) {
                            PLMMilestone plmMilestone = new PLMMilestone();
                            plmMilestone.setName(templateMilestone.getName());
                            plmMilestone.setSequenceNumber(milestoneCount + 1);
                            plmMilestone.setDescription(templateMilestone.getDescription());
                            plmMilestone.setWbs(wbsElement.getId());
                            plmMilestone.setStatus(ProjectActivityStatus.PENDING);
                            if (plmProject.getAssignedTo()) {
                                plmMilestone.setAssignedTo(templateMilestone.getAssignedTo());
                            }
                            plmMilestones.add(plmMilestone);
                            milestoneCount++;
                            //plmMilestone = milestoneRepository.save(plmMilestone);
                        }
                    }
                    if (plmMilestones.size() > 0) {
                        milestoneRepository.save(plmMilestones);
                    }
                }

            }
        }
        copyTemplateFoldersToProject(plmProject, template, project.getId());

        if (program != null && programProject != null) {
            PLMProgramProject plmProgramProject = new PLMProgramProject();
            plmProgramProject.setProgram(program);
            plmProgramProject.setParent(programProject);
            plmProgramProject.setProject(project.getId());
            plmProgramProject.setType(ProgramProjectType.PROJECT);
            plmProgramProject = programProjectRepository.save(plmProgramProject);
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectAddedEvent(plmProgramProject.getProgram(), plmProgramProject));
        } else {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectCreatedEvent(project));
        }
        if (workflowDef != null) {
            attachProjectWorkflow(project.getId(), workflowDef, null);
        } else if (template1.getWorkflow() != null && (plmProject.getCopyWorkflows() && (plmProject.getAllWorkflows() || plmProject.getProjectWorkflows()))) {
            attachProjectWorkflow(project.getId(), null, template1.getWorkflow());
        }
        return project;
    }

    private void copyTemplateFoldersToProject(PLMProject plmProject, Integer template, Integer project) {
        if (plmProject.getCopyFolders() && (plmProject.getAllFolders() || plmProject.getProjectFolders())) {
            List<ProjectTemplateFile> projectTemplateFiles = projectTemplateFileRepository.findByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(template, "FOLDER");
            for (ProjectTemplateFile projectTemplateFile : projectTemplateFiles) {
                PLMProjectFile projectFile = new PLMProjectFile();
                projectFile.setName(projectTemplateFile.getName());
                projectFile.setDescription(projectTemplateFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectFile.setVersion(1);
                projectFile.setSize(0L);
                projectFile.setProject(project);
                projectFile.setFileNo(folderNumber);
                projectFile.setFileType("FOLDER");
                projectFile = projectFileRepository.save(projectFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + project;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + projectFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                copyProjectFileChildrenFolders(projectTemplateFile.getId(), project, projectFile.getId());
            }
        }
    }

    private void copyProjectFileChildrenFolders(Integer templateFile, Integer project, Integer projectFileId) {
        List<ProjectTemplateFile> foldersList = projectTemplateFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(templateFile, "FOLDER");
        for (ProjectTemplateFile plmFile : foldersList) {
            PLMProjectFile projectFile = new PLMProjectFile();
            projectFile.setName(plmFile.getName());
            projectFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            projectFile.setVersion(1);
            projectFile.setSize(0L);
            projectFile.setParentFile(projectFileId);
            projectFile.setProject(project);
            projectFile.setFileNo(folderNumber);
            projectFile.setFileType("FOLDER");
            projectFile = projectFileRepository.save(projectFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + getParentFileSystemPath(project, projectFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectFileChildrenFolders(plmFile.getId(), project, projectFile.getId());
        }
    }

    private void copyTemplateFoldersToActivity(PLMProject plmProject, Integer template, Integer activity) {
        if (plmProject.getCopyFolders() && (plmProject.getAllFolders() || plmProject.getActivityFolders())) {
            List<ProjectTemplateActivityFile> activityTemplateFiles = projectTemplateActivityFileRepository.findByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(template, "FOLDER");
            for (ProjectTemplateActivityFile projectTemplateActivityFile : activityTemplateFiles) {
                PLMActivityFile activityFile = new PLMActivityFile();
                activityFile.setName(projectTemplateActivityFile.getName());
                activityFile.setDescription(projectTemplateActivityFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                activityFile.setVersion(1);
                activityFile.setSize(0L);
                activityFile.setActivity(activity);
                activityFile.setFileNo(folderNumber);
                activityFile.setFileType("FOLDER");
                activityFile = activityFileRepository.save(activityFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + activity;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + activityFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                copyActivityFileChildrenFolders(projectTemplateActivityFile.getId(), activity, activityFile.getId());
            }
        }
    }

    private void copyActivityFileChildrenFolders(Integer templateFile, Integer activity, Integer activityFileId) {
        List<ProjectTemplateActivityFile> foldersList = projectTemplateActivityFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(templateFile, "FOLDER");
        for (ProjectTemplateActivityFile plmFile : foldersList) {
            PLMActivityFile activityFile = new PLMActivityFile();
            activityFile.setName(plmFile.getName());
            activityFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            activityFile.setVersion(1);
            activityFile.setSize(0L);
            activityFile.setParentFile(activityFileId);
            activityFile.setActivity(activity);
            activityFile.setFileNo(folderNumber);
            activityFile.setFileType("FOLDER");
            activityFile = activityFileRepository.save(activityFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentActivityFileSystemPath(activity, activityFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyActivityFileChildrenFolders(plmFile.getId(), activity, activityFile.getId());
        }
    }


    @Transactional
    public PLMProject addTemplateProject(PLMProject plmProject, Integer template) throws ParseException {
        List<ProjectTemplateWbs> wbsList = projectTemplateWbsRepository.findByTemplate(template);
        if (plmProject.getTeam()) {
            List<ProjectTemplateMember> projectTemplateMembers = projectTemplateMemberRepository.findByTemplate(template);
            for (ProjectTemplateMember templateMember : projectTemplateMembers) {
                PLMProjectMember projectMember = projectMemberRepository.findByProjectAndPerson(plmProject.getId(), templateMember.getPerson());
                if (projectMember == null) {
                    PLMProjectMember plmProjectMember = new PLMProjectMember();
                    plmProjectMember.setProject(plmProject.getId());
                    plmProjectMember.setPerson(templateMember.getPerson());
                    projectMember = projectMemberRepository.save(plmProjectMember);
                }
            }
        }
        Integer count = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date todayDate = dateFormat.parse(dateFormat.format(new Date()));
        Date tommorowDate = dateFormat.parse(dateFormat.format(todayDate.getTime() + (1000 * 60 * 60 * 24)));
        if (wbsList.size() != 0) {
            for (ProjectTemplateWbs templateWbs : wbsList) {
                PLMWbsElement wbsElement = new PLMWbsElement();
                wbsElement.setSequenceNumber(count + 1);
                wbsElement.setName(templateWbs.getName());
                wbsElement.setDescription(templateWbs.getDescription());
                wbsElement.setProject(plmProject);
                wbsElement = wbsElementRepository.save(wbsElement);
                count++;
                List<ProjectTemplateActivity> templateActivities = projectTemplateActivityRepository.findByWbsOrderByCreatedDateAsc(templateWbs.getId());
                if (templateActivities.size() != 0) {
                    Integer activityCount = 0;
                    for (ProjectTemplateActivity templateActivity : templateActivities) {
                        PLMActivity existActivity = activityRepository.findByWbsAndNameEqualsIgnoreCase(wbsElement.getId(), templateActivity.getName());
                        if (existActivity == null) {
                            PLMActivity activity = new PLMActivity();
                            activity.setName(templateActivity.getName());
                            activity.setPlannedStartDate(todayDate);
                            activity.setPlannedFinishDate(tommorowDate);
                            activity.setSequenceNumber(activityCount + 1);
                            activity.setDescription(templateActivity.getDescription());
                            activity.setWbs(wbsElement.getId());
                            activity.setStatus(ProjectActivityStatus.PENDING);
                            if (plmProject.getAssignedTo()) {
                                activity.setAssignedTo(templateActivity.getAssignedTo());
                            }
                            activity = activityRepository.save(activity);
                            activityCount++;
                            createActivityTasks(templateActivity, activity, plmProject);
                        }
                    }
                }
                List<ProjectTemplateMilestone> templateMilestones = projectTemplateMilestoneRepository.findByWbsOrderByCreatedDateAsc(templateWbs.getId());
                if (templateMilestones.size() != 0) {
                    Integer milestoneCount = 0;
                    for (ProjectTemplateMilestone templateMilestone : templateMilestones) {
                        PLMMilestone existMilestone = milestoneRepository.findByWbsAndNameEqualsIgnoreCase(wbsElement.getId(), templateMilestone.getName());
                        if (existMilestone == null) {
                            PLMMilestone plmMilestone = new PLMMilestone();
                            plmMilestone.setName(templateMilestone.getName());
                            plmMilestone.setSequenceNumber(milestoneCount + 1);
                            plmMilestone.setPlannedFinishDate(tommorowDate);
                            plmMilestone.setDescription(templateMilestone.getDescription());
                            plmMilestone.setWbs(wbsElement.getId());
                            plmMilestone.setStatus(ProjectActivityStatus.PENDING);
                            if (plmProject.getAssignedTo()) {
                                plmMilestone.setAssignedTo(templateMilestone.getAssignedTo());
                            }
                            milestoneCount++;
                            plmMilestone = milestoneRepository.save(plmMilestone);
                        }
                    }
                }

            }
        }
        copyTemplateFoldersToProject(plmProject, template, plmProject.getId());
        return plmProject;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMProject> getProjects(Pageable pageable) {
        Page<PLMProject> projects = projectRepository.findAll(pageable);
        for (PLMProject project : projects.getContent()) {
            project.setPercentComplete(getProjectPercentComplete(project.getId()).getPercentComplete());
        }
        return projects;
    }

    @Transactional(readOnly = true)
    public List<ProgramProjectFolderDto> getProgramNullProjects() {
        List<ProgramProjectFolderDto> dtos = new LinkedList<>();
        List<PLMProject> projects = projectRepository.getProjectsByProgramIsNull();
        projects.forEach(project -> {
            ProgramProjectFolderDto projectDto = new ProgramProjectFolderDto();
            projectDto.setId(project.getId());
            projectDto.setProject(project.getId());
            projectDto.setName(project.getName());
            projectDto.setObjectType("PROJECT");

            List<PLMProjectFile> projectFiles = projectFileRepository.getFilesByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByNameAsc(project.getId(), "FOLDER");
            projectFiles.forEach(plmProjectFile -> {
                ProgramProjectFolderDto fileDto = new ProgramProjectFolderDto();
                fileDto.setId(plmProjectFile.getId());
                fileDto.setName(plmProjectFile.getName());
                fileDto.setProject(plmProjectFile.getProject());
                fileDto.setObjectType(PLMObjectType.FILE.name());
                fileDto = visitFolders(fileDto);
                projectDto.getChildren().add(fileDto);
            });
            dtos.add(projectDto);
        });

        return dtos;
    }

    private ProgramProjectFolderDto visitFolders(ProgramProjectFolderDto fileDto) {
        List<PLMProjectFile> projectFiles = projectFileRepository.getFilesByParentAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        projectFiles.forEach(plmProjectFile -> {
            ProgramProjectFolderDto dto = new ProgramProjectFolderDto();
            dto.setId(plmProjectFile.getId());
            dto.setName(plmProjectFile.getName());
            dto.setProject(plmProjectFile.getProject());
            dto.setObjectType(PLMObjectType.FILE.name());
            dto = visitFolders(dto);
            fileDto.getChildren().add(dto);
        });
        return fileDto;
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ProjectDto> getFilteredProjects(Pageable pageable, ProjectCriteria projectCriteria) {
        Predicate predicate = predicateBuilder.build(projectCriteria, QPLMProject.pLMProject);
        List<ProjectDto> projectDtoList = new LinkedList<>();
        Page<PLMProject> projects = projectRepository.findAll(predicate, pageable);
        for (PLMProject project : projects.getContent()) {
            ProjectDto projectDto = new ProjectDto();
            PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(project.getId());
            if (workflow != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
                projectDto.setStatus(workflowStatus.getName());
            }

            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            // projectDto.setType(pmObjectTypeRepository.findOne(project.getType()).getName());
            if (project.getType() != null) {
                projectDto.setType(project.getType().getName());
            }
            projectDto.setDescription(project.getDescription());
            projectDto.setPercentComplete(getProjectPercentComplete(project.getId()).getPercentComplete());
            projectDto.setPlannedStartDate(project.getPlannedStartDate());
            projectDto.setPlannedFinishDate(project.getPlannedFinishDate());
            projectDto.setActualStartDate(project.getActualStartDate());
            projectDto.setActualFinishDate(project.getActualFinishDate());
            projectDto.setMakeConversationPrivate(project.getMakeConversationPrivate());
            if (project.getProjectManager() != null) {
                Person person = personRepository.findOne(project.getProjectManager());
                projectDto.setManagerFirstName(person.getFirstName());
                projectDto.setManagerLastName(person.getLastName());
                projectDto.setManagerFullName(person.getFullName());
                projectDto.setProjectManager(project.getProjectManager());
                projectDto.setHasManagerImage(person.isHasImage());
            }
            Integer loginPersonId = null;
            if (sessionWrapper != null && sessionWrapper.getSession() != null) {
                loginPersonId = sessionWrapper.getSession().getLogin().getPerson().getId();
                PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(sessionWrapper.getSession().getLogin().getPerson(), project.getId());
                if (subscribe != null) projectDto.setIsSubscribed(subscribe.getSubscribe());
                else projectDto.setIsSubscribed(false);
            }
            Boolean personExistInTeam = false;
            List<Integer> projectMemberIds = projectMemberRepository.getProjectMemberIds(project.getId());
            if (projectMemberIds.size() > 0) {
                List<Person> persons = personRepository.findByIdIn(projectMemberIds);
                for (Person person : persons) {
                    if (project.getMakeConversationPrivate()) {
                        if (loginPersonId != null && loginPersonId.equals(person.getId())) {
                            personExistInTeam = true;
                        }
                    }
                    ProjectMemberDto projectMemberDto = new ProjectMemberDto();
                    projectMemberDto.setId(person.getId());
                    projectMemberDto.setFirstName(person.getFirstName());
                    projectMemberDto.setLastName(person.getLastName());
                    projectMemberDto.setFullName(person.getFullName());
                    projectMemberDto.setHasImage(person.isHasImage());
                    projectDto.getProjectMembers().add(projectMemberDto);
                }
            }
            if (!project.getMakeConversationPrivate()) {
                projectDto.setShowConversationCount(true);
            } else if (personExistInTeam || (loginPersonId != null && project.getProjectManager().equals(loginPersonId))) {
                projectDto.setShowConversationCount(true);
            }

            projectDto.setTasks(taskRepository.getProjectTaskCount(project.getId()));
            projectDto.setComments(commentRepository.findAllByObjectId(project.getId()).size());
            List<Integer> deliverableItems = projectDeliveravbleRepository.getProjectDeliverableItem(project.getId());
            deliverableItems.addAll(activityDeliverableRepository.getActivityDeliverableItems(project.getId()));
            deliverableItems.addAll(taskDeliverableRepository.getTaskDeliverableItems(project.getId()));

            List<Integer> items = new ArrayList<Integer>(new HashSet<Integer>(deliverableItems));
            projectDto.setDeliverables(items.size());
            projectDto.setTagsCount(project.getTags().size());
            projectDtoList.add(projectDto);
        }
        return new PageImpl<ProjectDto>(projectDtoList, pageable, projects.getTotalElements());
    }

    @Transactional
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMProject getProject(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        getMinAndMaxProjectDates(project);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(project.getId());
        PLMProjectMember member = projectMemberRepository.findByProjectAndPerson(project.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
        if (member != null) {
            project.setTeamMember(Boolean.TRUE);
        } else if (project.getProjectManager().equals(sessionWrapper.getSession().getLogin().getPerson().getId())) {
            project.setTeamMember(Boolean.TRUE);
        }
        //Adding workflow relavent settings
        WorkflowStatusDTO workFlowStatusDto = workflowService.setWorkflowStatusSettings(project.getId());
        project.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
        project.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
        project.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());
        return project;
    }

    @Transactional
    @PreAuthorize("hasPermission(#plmProject.id ,'edit')")
    public PLMProject updateProject(PLMProject plmProject) throws ParseException, JsonProcessingException {
        PLMProject project = null;
        if (plmProject.getProgram() != null) {
            project = projectRepository.findByNameEqualsIgnoreCaseAndProgram(plmProject.getName(), plmProject.getProgram());
        } else {
            project = projectRepository.findByNameEqualsIgnoreCaseAndProgramIsNull(plmProject.getName());
        }

        if (plmProject.getPlannedStartDate() != null && plmProject.getPlannedFinishDate() != null) {
            String message = null;
            Integer count = taskRepository.getProjectTaskCountByProjectStartFinishDate(plmProject.getId(), plmProject.getPlannedStartDate(), plmProject.getPlannedFinishDate());
            if (count > 0) {
                message = messageSource.getMessage("task_dates_have_preceding_exceeding_dates", null, "Some of the tasks are preceding/exceeded dates", LocaleContextHolder.getLocale());
            } else {
                count = activityRepository.getProjectActivityCountByProjectStartFinishDate(plmProject.getId(), plmProject.getPlannedStartDate(), plmProject.getPlannedFinishDate());
                if (count > 0) {
                    message = messageSource.getMessage("activity_dates_have_preceding_exceeding_dates", null, "Some of the activities are preceding/exceeded dates", LocaleContextHolder.getLocale());
                } else {
                    count = milestoneRepository.getProjectMilestoneCountByProjectStartFinishDate(plmProject.getId(), plmProject.getPlannedStartDate(), plmProject.getPlannedFinishDate());
                    if (count > 0) {
                        message = messageSource.getMessage("milestone_dates_have_preceding_exceeding_dates", null, "Some of the milestones are preceding/exceeded dates", LocaleContextHolder.getLocale());
                    }
                }
            }

            if (count > 0) {
                String result = MessageFormat.format(message + ".", count);
                throw new CassiniException(result);
            }
        } else if (plmProject.getPlannedStartDate() != null) {
            String message = null;
            Integer count = taskRepository.getProjectTaskCountByProjectStartDate(plmProject.getId(), plmProject.getPlannedStartDate());
            if (count > 0) {
                message = messageSource.getMessage("task_dates_have_preceding_dates", null, "Some of the tasks are preceding dates", LocaleContextHolder.getLocale());
            } else {
                count = activityRepository.getProjectActivityCountByProjectStartDate(plmProject.getId(), plmProject.getPlannedStartDate());
                if (count > 0) {
                    message = messageSource.getMessage("activity_dates_have_preceding_dates", null, "Some of the activities are preceding dates", LocaleContextHolder.getLocale());
                } else {
                    count = milestoneRepository.getProjectMilestoneCountByProjectStartDate(plmProject.getId(), plmProject.getPlannedStartDate());
                    if (count > 0) {
                        message = messageSource.getMessage("milestone_dates_have_preceding_dates", null, "Some of the milestones are preceding dates", LocaleContextHolder.getLocale());
                    }
                }
            }

            if (count > 0) {
                String result = MessageFormat.format(message + ".", count);
                throw new CassiniException(result);
            }
        } else if (plmProject.getPlannedFinishDate() != null) {
            String message = null;
            Integer count = taskRepository.getProjectTaskCountByProjectFinishDate(plmProject.getId(), plmProject.getPlannedFinishDate());
            if (count > 0) {
                message = messageSource.getMessage("task_dates_have_exceeding_dates", null, "Some of the tasks are exceeded dates", LocaleContextHolder.getLocale());
            } else {
                count = activityRepository.getProjectActivityCountByProjectFinishDate(plmProject.getId(), plmProject.getPlannedFinishDate());
                if (count > 0) {
                    message = messageSource.getMessage("task_dates_have_exceeding_dates", null, "Some of the activities are exceeded dates", LocaleContextHolder.getLocale());
                } else {
                    count = milestoneRepository.getProjectMilestoneCountByProjectFinishDate(plmProject.getId(), plmProject.getPlannedFinishDate());
                    if (count > 0) {
                        message = messageSource.getMessage("task_dates_have_exceeding_dates", null, "Some of the milestones are exceeded dates", LocaleContextHolder.getLocale());
                    }
                }
            }

            if (count > 0) {
                String result = MessageFormat.format(message + ".", count);
                throw new CassiniException(result);
            }
        }

        PLMProject existProject = projectRepository.findOne(plmProject.getId());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectBasicInfoUpdatedEvent(existProject, plmProject));
        if (project != null && !project.getId().equals(plmProject.getId())) {
            throw new CassiniException(plmProject.getName() + " : " + messageSource.getMessage("project_name_already_exists", null, "Project name already exist", LocaleContextHolder.getLocale()));
        }
        plmProject = projectRepository.save(plmProject);
        sendProjectSubscribeNotification(plmProject, projectActivityStream.getProjectBasicInfoUpdatedJson(existProject, plmProject), "basic");
        return plmProject;
    }

    @Transactional
    @PreAuthorize("hasPermission(#plmProjectId,'delete')")
    public void deleteProject(Integer plmProjectId) {
        PLMProject project = projectRepository.findOne(plmProjectId);
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(plmProjectId);
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        applicationEventPublisher.publishEvent(new WorkflowEvents.ProjectActivityTaskWorkflowDeletedEvent(plmProjectId));
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(plmProjectId));
        projectRepository.delete(plmProjectId);
    }

    /*  CRUD operations for projectMember   */
    @Transactional
    public PLMProjectMember createProjectMember(Integer projectId, PLMProjectMember projectMember) {
        projectMember.setProject(projectId);
        return projectMemberRepository.save(projectMember);
    }

    @Transactional
    public List<PLMProjectMember> createProjectMembers(Integer projectId, List<Person> persons) throws JsonProcessingException {
        PLMProject project = projectRepository.findOne(projectId);
        String personName = null;
        List<PLMProjectMember> projectMembers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Person person : persons) {
            if (personName == null) {
                personName = person.getFullName();
            } else {
                personName = personName + " , " + person.getFullName();
            }
            PLMProjectMember projectMember = new PLMProjectMember();
            projectMember.setPerson(person.getId());
            projectMember.setProject(projectId);
            projectMembers.add(projectMember);
            names.add(personName);
            securityPermissionService.addLoginSecurityPermission(person.getId(), project, "view");
        }
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMembersAddedEvent(project, persons));
        sendProjectSubscribeNotification(project, projectActivityStream.getProjectMembersAddedJson(persons), "teamAdded");
        return projectMemberRepository.save(projectMembers);
    }

    @Transactional(readOnly = true)
    public PLMProjectMember getProjectMember(Integer project, Integer person) {
        return projectMemberRepository.findByProjectAndPerson(project, person);
    }

    @Transactional
    public PLMProjectMember updateProjectMember(PLMProjectMember projectMember) {
        return projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void deleteProjectMember(Integer project, Integer projectMemberId) throws JsonProcessingException {
        PLMProject plmProject = projectRepository.findOne(project);
        PLMProjectMember projectMember = projectMemberRepository.findByProjectAndPerson(project, projectMemberId);
        projectMemberRepository.delete(projectMember);
        Person person = personRepository.findOne(projectMemberId);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMemberDeletedEvent(plmProject, person));
        sendProjectSubscribeNotification(plmProject, person.getFullName(), "teamDeleted");
        SetAssignToNull(plmProject,projectMemberId);
    }

    // set Assign to null to all the activities,task,milestones when person is delete from the project team.
    public void SetAssignToNull(PLMProject plmProject,Integer projectMemberId){
        List<WBSDto> plans = getProjectPlanStructure(plmProject);
        plans.forEach(plan -> {
            if(plan.getObjectType().equals("PROJECTPHASEELEMENT")) {
                List<WBSDto> activities = plan.getChildren();
                activities.forEach(activity -> {
                    if (activity.getObjectType().equals("PROJECTACTIVITY")) {
                        if (activity.getPerson() != null) {
                            if (activity.getPerson().getId().equals(projectMemberId)) {
                                PLMActivity activity1 = activityRepository.findByIdAndAssignedTo(activity.getId(), projectMemberId);
                                activity1.setAssignedTo(null);
                                activityRepository.save(activity1);
                            }
                        }
                        //----tasks--------
                        activity.getActivityTasks().forEach(task -> {
                            if (task.getPerson() != null) {
                            if (task.getPerson().getId().equals(projectMemberId)) {
                                PLMTask plmTask = taskRepository.findByIdAndAssignedTo(task.getId(), projectMemberId);
                                plmTask.setAssignedTo(null);
                                taskRepository.save(plmTask);
                            }
                        }
                        });
                    }
                    if (activity.getObjectType().equals("PROJECTMILESTONE")) {
                        if (activity.getPerson() != null) {
                            if (activity.getPerson().getId().equals(projectMemberId)) {
                                PLMMilestone milestone = milestoneRepository.findByIdAndAssignedTo(activity.getId(), projectMemberId);
                                milestone.setAssignedTo(null);
                                milestoneRepository.save(milestone);
                            }
                        }
                    }
                });
            }
        });
    }


    @Transactional

    public void deleteProjectMember(PLMProjectMember projectMember) {
        projectMemberRepository.delete(projectMember);
    }

    @Transactional(readOnly = true)
    public Page<PLMProjectMember> getProjectMembers(Integer projectId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        Page<PLMProjectMember> projectMembers = projectMemberRepository.findAllByProject(projectId, pageable);
        return projectMembers;

    }

    @Transactional
    public PLMWbsElement createWBSElement(PLMWbsElement wbsElement) {
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(wbsElement.getProject());
        wbsElement.setSequenceNumber(wbsElements.size() + 1);
        wbsElement = wbsElementRepository.save(wbsElement);
        wbsElement.setGanttId(wbsElement.getGanttId());
        PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
        if (project.getActualFinishDate() != null) {
            project.setActualFinishDate(null);
            project = projectRepository.save(project);
        }
        return wbsElement;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<PLMWbsElement> createWBSElements(Integer projectId, List<PLMWbsElement> wbsElementList) throws JsonProcessingException {
        List<PLMWbsElement> plmWbsElements = new ArrayList<>();
        List<PLMWbsElement> newWbsElements = new ArrayList<>();
        wbsElementList.forEach(wbsElement -> {
            if (wbsElement.getId() == null) newWbsElements.add(wbsElement);
            List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(wbsElement.getProject());
            wbsElement = wbsElementRepository.save(wbsElement);
            wbsElement.setGanttId(wbsElement.getGanttId());
            PLMProject project = projectRepository.findOne(wbsElement.getProject().getId());
            if (project.getActualFinishDate() != null) {
                project.setActualFinishDate(null);
                project = projectRepository.save(project);
            }
            plmWbsElements.add(wbsElement);
        });
        PLMProject plmProject = projectRepository.findOne(projectId);
        if (newWbsElements.size() > 0) {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectPhasesAddedEvent(plmProject, newWbsElements));
            List<String> phases = new ArrayList<>();
            newWbsElements.forEach(f -> phases.add(f.getName()));
            sendProjectSubscribeNotification(plmProject, phases.toString(), "phaseAdded");
        }
        return plmWbsElements;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void createLinks(Integer projectId, String links) {
        PLMLinks isLinks = linksRepository.findByProject(projectId);
        if (isLinks != null) {
            isLinks.setId(isLinks.getId());
            isLinks.setProject(projectId);
            isLinks.setDependency(links);
            linksRepository.save(isLinks);
        } else {
            PLMLinks links1 = new PLMLinks();
            links1.setProject(projectId);
            links1.setDependency(links);
            linksRepository.save(links1);
        }
    }

    @Transactional
    public PLMLinks getLinks(Integer projectId) {
        return linksRepository.findByProject(projectId);
    }

    @Transactional(readOnly = true)
    public List<PLMWbsElement> getWBSElements(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        return wbsElementRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    public PLMWbsElement getWBSElement(Integer wbsId) {
        return wbsElementRepository.findOne(wbsId);
    }

    @Transactional
    public PLMWbsElement updateWBSElement(Integer projectId, PLMWbsElement wbsElement) {
        PLMProject project = projectRepository.findOne(projectId);
        PLMWbsElement wbsExist = wbsElementRepository.findOne(wbsElement.getId());
        PLMWbsElement wbsElement1 = wbsElementRepository.findByProjectAndNameEqualsIgnoreCase(project, wbsElement.getName());
        if (wbsElement1 != null && !wbsElement1.getId().equals(wbsElement.getId())) {
            throw new CassiniException(wbsElement.getName() + " : " + messageSource.getMessage("wbs_name_already_exist", null, "WBS name already exist", LocaleContextHolder.getLocale()));
        }
        return wbsElementRepository.save(wbsElement);
    }

    @Transactional
    public void deleteWBSElement(Integer wbsElement) throws JsonProcessingException {
        PLMWbsElement wbsElement1 = wbsElementRepository.findOne(wbsElement);
        PLMProject project = projectRepository.findOne(wbsElement1.getProject().getId());
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProjectOrderBySequenceNumberAsc(project);
        for (PLMWbsElement element : wbsElements) {
            if (element.getSequenceNumber() > wbsElement1.getSequenceNumber()) {
                element.setSequenceNumber(element.getSequenceNumber() - 1);
                element = wbsElementRepository.save(element);
            }
        }
        wbsElementRepository.delete(wbsElement);
        sendProjectSubscribeNotification(project, wbsElement1.getName(), "phaseDeleted");
    }

    @Transactional
    public void deleteWBSElement(PLMWbsElement wbsElement) {
        wbsElementRepository.delete(wbsElement);
    }

    @Transactional(readOnly = true)
    public List<PLMWbsElement> getRootWbsElements(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        return wbsElementRepository.findByProjectAndParentIsNull(project);
    }

    @Transactional(readOnly = true)
    public List<PLMWbsElement> getWBSElementChildren(Integer wbsId) {
        return wbsElementRepository.findByParent(wbsId);
    }

    public List<PLMActivity> getWBSActivities(Integer wbsId) {
        return activityRepository.findByWbsOrderByCreatedDateAsc(wbsId);
    }

    @Transactional(readOnly = true)
    public List<PLMMilestone> getWBSMilestones(Integer wbsId) {
        return milestoneRepository.findByWbs(wbsId);
    }

    @Transactional(readOnly = true)
    public List<PLMProjectFile> getProjectFiles(Integer projectId) {
        List<PLMProjectFile> projectFiles = projectFileRepository.findByProjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(projectId);
        projectFiles.forEach(projectFile -> {
            projectFile.setParentObject(PLMObjectType.PROJECT);
            if (projectFile.getFileType().equals("FOLDER")) {
                projectFile.setCount(projectFileRepository.getChildrenCountByParentFileAndLatestTrue(projectFile.getId()));
                projectFile.setCount(projectFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(projectFile.getProject(), projectFile.getId()));
            }
        });
        return projectFiles;
    }

    @Transactional(readOnly = true)
    public PLMProjectFile getProjectFile(Integer fileId) {
        return projectFileRepository.findOne(fileId);
    }

    @Transactional
    public List<PLMProjectFile> uploadProjectFiles(Integer projectId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMProjectFile> uploadedFiles = new ArrayList<>();
        List<PLMProjectFile> newFiles = new ArrayList<>();
        List<PLMProjectFile> versionedFiles = new ArrayList<>();
        String fNames = null;
        PLMProject plmProject = projectRepository.findOne(projectId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        boolean itemFileNotExist = false;
        List<PLMProjectFile> plmItemFiles = new ArrayList<>();
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
                    PLMProjectFile projectFile = null;
                    if (folderId == 0) {
                        projectFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(projectId, name);
                    } else {
                        projectFile = projectFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    Integer oldFile = null;
                    String autoNumber1 = null;
                    if (projectFile != null) {
                        Integer mewVersion = projectFile.getVersion() + 1;
                        projectFile.setLatest(false);
                        Integer oldVersion = projectFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = projectFile.getFileNo();
                        oldFile = projectFile.getId();
                        projectFileRepository.save(projectFile);
                        versioned = true;
                    }
                    if (projectFile == null) {
                        itemFileNotExist = true;
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    projectFile = new PLMProjectFile();
                    projectFile.setName(name);
                    projectFile.setFileNo(autoNumber1);
                    projectFile.setCreatedBy(login.getPerson().getId());
                    projectFile.setModifiedBy(login.getPerson().getId());
                    projectFile.setVersion(version);
                    projectFile.setProject(projectId);
                    projectFile.setSize(file.getSize());
                    projectFile.setFileType("FILE");
                    if (fNames == null) {
                        fNames = projectFile.getName();
                    } else {
                        fNames = fNames + " , " + projectFile.getName();
                    }
                    if (folderId != 0) {
                        projectFile.setParentFile(folderId);
                    }
                    projectFile = projectFileRepository.save(projectFile);
                    if (projectFile.getParentFile() != null) {
                        PLMProjectFile parent = projectFileRepository.findOne(projectFile.getParentFile());
                        parent.setModifiedDate(projectFile.getModifiedDate());
                        parent = projectFileRepository.save(parent);
                    }
                    if (projectFile.getVersion() > 1) {
                        qualityFileService.copyFileAttributes(oldFile, projectFile.getId());
                    }
                    if (itemFileNotExist) {
                        plmItemFiles.add(projectFile);
                    }
                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + projectId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(projectId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fNames = projectFile.getName();
                    String path = dir + File.separator + projectFile.getId();
                    saveDocumentToDisk(file, path);
                    String ext2 = FilenameUtils.getExtension(file.getOriginalFilename());
                    /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                    if (map != null) {
                        projectFile.setUrn(map.get("urn"));
                        projectFile.setThumbnail(map.get("thumbnail"));
                        projectFile = projectFileRepository.save(projectFile);
                    }*/
                    uploadedFiles.add(projectFile);
                    if (versioned) {
                        versionedFiles.add(projectFile);
                    } else {
                        newFiles.add(projectFile);
                    }
                }

            }

			 /* App Events */
            if (newFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFilesAddedEvent(plmProject, newFiles));
                sendProjectSubscribeNotification(plmProject, projectActivityStream.getProjectFilesAddedJson(newFiles), "fileAdded");
            }
            if (versionedFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFilesVersionedEvent(plmProject, versionedFiles));
                sendProjectSubscribeNotification(plmProject, projectActivityStream.getProjectFilesVersionedJson(versionedFiles), "fileVersioned");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadedFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMProjectFile> getFileVersions(Integer projectId, Integer fileId) {
        PLMProjectFile projectFile = projectFileRepository.findByProjectAndIdAndLatestTrue(projectId, fileId);
        List<PLMProjectFile> projectFiles = projectFileRepository.findByProjectAndName(projectId, projectFile.getName());
        return projectFiles;
    }

    @Transactional(readOnly = true)
    public PLMProjectFile getLatestUploadedProjectFile(Integer taskId, Integer fileId) {
        PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
        PLMProjectFile projectFile = projectFileRepository.findByProjectAndFileNoAndLatestTrue(plmProjectFile.getProject(), plmProjectFile.getFileNo());
        return projectFile;
    }

    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }

    }

    @Transactional
    public void deleteProjectFile(Integer projectId, Integer fileId) throws JsonProcessingException {
        PLMProjectFile projectFile = projectFileRepository.findOne(fileId);
        List<PLMProjectFile> projectFiles = projectFileRepository.findByProjectAndFileNo(projectFile.getProject(), projectFile.getFileNo());
        for (PLMProjectFile file : projectFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(file.getProject(), file.getId());
            fileSystemService.deleteDocumentFromDiskFolder(file.getId(), dir);
            projectFileRepository.delete(file.getId());
        }
        if (projectFile.getParentFile() != null) {
            PLMProjectFile parent = projectFileRepository.findOne(projectFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = projectFileRepository.save(parent);
        }
        PLMProject project = projectRepository.findOne(projectId);
        /* App events */
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileDeletedEvent(project, projectFile));
        sendProjectSubscribeNotification(project, projectFile.getName(), "fileDeleted");
        projectFileRepository.delete(projectFile);
    }

    @Transactional(readOnly = true)
    public File getProjectFile(Integer projectId, Integer fileId, String type) {
        checkNotNull(projectId);
        checkNotNull(fileId);
        PLMProjectFile projectFile = null;
        if (type.equals("COMMON")) {
            projectFile = projectFileRepository.findByProjectAndId(projectId, fileId);
        } else {
            projectFile = projectFileRepository.findByProjectAndIdAndLatestTrue(projectId, fileId);
        }
        if (projectFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public void generateZipFile(Integer projectId, HttpServletResponse response) throws IOException {
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMProjectFile> projectFiles = projectFileRepository.findByProjectAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(projectId);
        ArrayList<String> fileList = new ArrayList<>();
        projectFiles.forEach(plmProjectFile -> {
            File file = getProjectFile(projectId, plmProjectFile.getId(), "LATEST");
            fileList.add(file.getAbsolutePath());
        });
        String zipName = project.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "PROJECT",projectId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }


    /*      CRUD operations for Project Deliverables        */
    @Transactional
    public PLMProjectDeliverable createProjectDeliverable(PLMProjectDeliverable projectDeliverable) {
        return projectDeliveravbleRepository.save(projectDeliverable);
    }

    @Transactional
    public List<PLMProjectDeliverable> createProjectDeliverables(Integer projectId, List<PLMItem> items) throws JsonProcessingException {
        List<PLMProjectDeliverable> projectDeliverables = new ArrayList<>();
        String addItems = null;
        for (PLMItem item : items) {
            PLMProjectDeliverable deliverable = projectDeliveravbleRepository.findByProjectAndItemRevision(projectId, item.getLatestRevision());
            if (deliverable == null) {
                PLMProjectDeliverable projectDeliverable = new PLMProjectDeliverable();
                projectDeliverable.setProject(projectId);
                projectDeliverable.setItemRevision(item.getLatestRevision());
                projectDeliverable.setDeliverableStatus(DeliverableStatus.PENDING);
                projectDeliverables.add(projectDeliverable);
                if (addItems == null) {
                    addItems = item.getItemNumber();
                } else {
                    addItems = addItems + " , " + item.getItemNumber();
                }
            }

        }
        if (addItems != null) {
            projectDeliveravbleRepository.save(projectDeliverables);
        }
        PLMProject plmProject = projectRepository.findOne(projectId);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectDeliverablesAddedEvent(plmProject, items));
        sendProjectSubscribeNotification(plmProject, projectActivityStream.getProjectDeliverablesAddedJson(items), "deliverableAdded");
        return projectDeliverables;
    }

    @Transactional
    public List<PLMGlossaryDeliverable> createProjectGlossaryDeliverables(Integer projectId, List<PLMGlossary> glossaries) {
        String addGlossaries = null;
        List<PLMGlossaryDeliverable> deliverables = new ArrayList<>();
        for (PLMGlossary glossary : glossaries) {
            PLMGlossaryDeliverable glossaryDeliverable = glossaryDeliverableRepository.findByObjectIdAndGlossary(projectId, glossary);
            if (glossaryDeliverable == null) {
                PLMGlossaryDeliverable glossaryDeliverable1 = new PLMGlossaryDeliverable();
                glossaryDeliverable1.setObjectId(projectId);
                glossaryDeliverable1.setObjectType(PLMObjectType.PROJECT.toString());
                glossaryDeliverable1.setGlossary(glossary);
                deliverables.add(glossaryDeliverable1);
                if (addGlossaries == null) {
                    addGlossaries = glossary.getName();
                } else {
                    addGlossaries = addGlossaries + " , " + glossary.getName();
                }
            }
        }
        if (addGlossaries != null) {
            glossaryDeliverableRepository.save(deliverables);
        }
        return deliverables;
    }

    @Transactional(readOnly = true)
    public PLMProjectDeliverable getProjectDeliverableById(Integer projectDeliverableId) {
        return projectDeliveravbleRepository.findOne(projectDeliverableId);
    }

    @Transactional
    public PLMProjectDeliverable updateProjectDeliverable(PLMProjectDeliverable projectDeliverable) {
        return projectDeliveravbleRepository.save(projectDeliverable);
    }

    @Transactional(readOnly = true)
    public List<PLMProjectDeliverable> getAllProjectDeliverablesByProjectId(Integer projectId) {
        List<PLMProjectDeliverable> projectDeliverables = new ArrayList<>();
        projectDeliverables = projectDeliveravbleRepository.findByProject(projectId);
        return projectDeliverables;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getProjectDeliverablesByProjectId(Integer projectId) {
        List<PLMItem> plmItems = new ArrayList<>();
        List<PLMProjectDeliverable> projects = projectDeliveravbleRepository.findByProject(projectId);
        List<PLMItem> items = itemRepository.findAll();
        if (projects.size() != 0) {
            for (PLMItem item : items) {
                Boolean exist = false;
                for (PLMProjectDeliverable project : projects) {
                    if (project.getItemRevision().equals(item.getLatestRevision())) {
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
        return plmItems;
    }

    @Transactional(readOnly = true)
    public List<PLMGlossary> getProjectGlossarysDeliverablesByProjectId(Integer projectId) {
        List<PLMGlossary> glossaries = new ArrayList<>();
        List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(projectId);
        List<PLMGlossary> glossarys = glossaryRepository.findAll();
        if (glossaryDeliverables.size() != 0) {
            for (PLMGlossary glossary : glossarys) {
                Boolean exist = false;
                for (PLMGlossaryDeliverable glossaryDeliverable : glossaryDeliverables) {
                    if (glossaryDeliverable.getGlossary().equals(glossary.getId())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    glossaries.add(glossary);
                }
            }
        } else {
            glossaries.addAll(glossarys);
        }
        return glossaries;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PLMProject> freeTextSearch(Pageable pageable, ProjectCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMProject.pLMProject);
        Page<PLMProject> projects = projectRepository.findAll(predicate, pageable);
        for (PLMProject project : projects.getContent()) {
            project = getProjectPercentComplete(project.getId());
        }
        return projects;
    }

    @Transactional(readOnly = true)
    public List<PMObjectTypeAttribute> getAllTypeAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return pmObjectTypeAttributeRepository.findByTypeOrderBySequence(typeId);
        } else {
            return pmObjectTypeService.getAttributesFromHierarchy(typeId);
        }
    }

    @Transactional
    public List<PLMWbsElement> getProjectWbsStructure(PLMProject project) {
        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project);
        if (rootWbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : rootWbsElements) {
                wbsElement.setLevel(0);
                List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (wbsActivities.size() != 0) {
                    for (PLMActivity activity : wbsActivities) {
                        activity.setLevel(wbsElement.getLevel() + 1);
                        if (activity.getAssignedTo() != null) {
                            Person person = personRepository.findOne(activity.getAssignedTo());
                            activity.setPerson(person);
                        }
                        List<PLMTask> activityTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
                        if (activityTasks.size() != 0) {
                            Double activityPercentComplete = 0.0;
                            for (PLMTask task : activityTasks) {
                                activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                            }
                            activity.setActivityTasks(activityTasks);
                            activity.setPercentComplete((activityPercentComplete) / activityTasks.size());
                        } else {
                            activity.setActivityTasks(activityTasks);
                        }
                        activity.getActivityDeliverables().addAll(activityDeliverableRepository.findByActivity(activity.getId()));
                        activity.getActivityFiles().addAll(activityFileRepository.findByActivity(activity.getId()));
                        activity.getActivityItemReferences().addAll(activityItemReferenceRepository.findByActivity(activity.getId()));
                    }
                    wbsElement.setHasBom(true);
                    wbsElement.setExpanded(true);
                    wbsElement.setActivities(wbsActivities);
                }
                List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (wbsMilestones.size() != 0) {
                    for (PLMMilestone milestone : wbsMilestones) {
                        milestone.setLevel(wbsElement.getLevel() + 1);
                        if (milestone.getAssignedTo() != null) {
                            Person person1 = personRepository.findOne(milestone.getAssignedTo());
                            milestone.setPerson(person1);
                        }
                        List<PLMActivity> finishedActivities = new ArrayList<>();
                        for (PLMActivity activity : wbsActivities) {
                            if (activity.getStatus().equals(ProjectActivityStatus.FINISHED)) {
                                finishedActivities.add(activity);
                            }
                        }
                        if (wbsActivities.size() == finishedActivities.size()) {
                            milestone.setFinishMilestone(true);
                        } else {
                            milestone.setFinishMilestone(false);
                        }
                    }
                    wbsElement.setHasBom(true);
                    wbsElement.setExpanded(true);
                    wbsElement.setMilestones(wbsMilestones);
                }
            }
        }
        return rootWbsElements;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#project, 'view')")
    public List<WBSDto> getProjectPlanStructure(PLMProject project) {
        List<WBSDto> wbsDtoList = new ArrayList<>();
        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderBySequenceNumberAsc(project);
        if (rootWbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : rootWbsElements) {
                WBSDto wbsDto = new WBSDto();
                wbsDto.setId(wbsElement.getId());
                wbsDto.setName(wbsElement.getName());
                wbsDto.setDescription(wbsElement.getDescription());
                wbsDto.setLevel(0);
                wbsDto.setCreatedBy(wbsElement.getCreatedBy());
                wbsDto.setModifiedBy(wbsElement.getModifiedBy());
                wbsDto.setCreatedDate(wbsElement.getCreatedDate());
                wbsDto.setSequenceNumber(wbsElement.getSequenceNumber());
                wbsDto.setObjectType(wbsElement.getObjectType().toString());
                wbsDto.setProject(wbsElement.getProject());
                wbsDto.setPlannedStartDate(wbsElement.getPlannedStartDate());
                wbsDto.setPlannedFinishDate(wbsElement.getPlannedFinishDate());
                wbsDto.setParentId(wbsElement.getProject().getId());
                List<WBSDto> children = new ArrayList<>();
                List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                //List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (wbsActivities.size() > 0) {
                    for (PLMActivity activity : wbsActivities) {
                        WBSDto activityDto = new WBSDto();
                        activityDto.setId(activity.getId());
                        activityDto.setName(activity.getName());
                        activityDto.setDescription(activity.getDescription());
                        activityDto.setDuration(activity.getDuration());
                        activityDto.setLevel(wbsDto.getLevel() + 1);
                        activityDto.setActualFinishDate(activity.getActualFinishDate());
                        activityDto.setActualStartDate(activity.getActualStartDate());
                        activityDto.setPlannedStartDate(activity.getPlannedStartDate());
                        activityDto.setPlannedFinishDate(activity.getPlannedFinishDate());
                        activityDto.setSequenceNumber(activity.getSequenceNumber());
                        activityDto.setCreatedBy(activity.getCreatedBy());
                        activityDto.setModifiedBy(activity.getModifiedBy());
                        activityDto.setCreatedDate(activity.getCreatedDate());
                        activityDto.setObjectType(activity.getObjectType().toString());
                        activityDto.setStatus(activity.getStatus().toString());
                        activityDto.setParent(wbsDto.getId());
                        String[] tabs = activity.getType().getTabs();
                        if (tabs != null) {
                            for (String tab : tabs) {
                                if (tab.equals("files")) {
                                    activityDto.setFileTab(true);
                                }
                            }
                        }
                        activityDto.setIsShared(sharedObjectRepository.getSharedCountByObjectId(activity.getId()) > 0);
                        if (activityFileRepository.findByActivityAndFileTypeAndLatestTrueOrderByModifiedDateDesc(activity.getId(), "FILE").size() > 0)
                            activityDto.setHasFiles(true);
                        activityDto.setParentId(wbsDto.getId());
                        activityDto.setWorkflow(activity.getWorkflow());
                        if (activity.getAssignedTo() != null) {
                            activityDto.setPerson(personRepository.findOne(activity.getAssignedTo()));
                        }
                        List<PLMTask> activityTasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activity.getId());
                        if (activityTasks.size() > 0) {
                            Double activityPercentComplete = 0.0;
                            for (PLMTask task : activityTasks) {
                                if (task.getAssignedTo() != null) {
                                    task.setPerson(personRepository.findOne(task.getAssignedTo()));
                                }
                                if (task.getWorkflow() != null)
                                    task.setFinishedWorkflow(plmWorkflowRepository.findOne(task.getWorkflow()).getFinished());
                                task.setIsShared(sharedObjectRepository.getSharedCountByObjectId(task.getId()) > 0);
                                activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                                if (taskFileRepository.findByTaskAndFileTypeAndLatestTrueOrderByModifiedDateDesc(task.getId(), "FILE").size() > 0)
                                    task.setHasFiles(true);
                                String[] taskTabs = task.getType().getTabs();
                                if (taskTabs != null) {
                                    for (String tab : taskTabs) {
                                        if (tab.equals("files")) {
                                            task.setFileTab(true);
                                        }
                                    }
                                }
                            }
                            activityDto.setActivityTasks(activityTasks);
                            activityDto.setCount(activityTasks.size());
                            activityDto.setPercentComplete((activityPercentComplete) / activityTasks.size());
                        } else {
                            activityDto.setPercentComplete(activity.getPercentComplete());
                        }
                        children.add(activityDto);
                    }
                }
                List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                //List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (wbsMilestones.size() > 0) {
                    for (PLMMilestone milestone : wbsMilestones) {
                        WBSDto milestoneDto = new WBSDto();
                        milestoneDto.setId(milestone.getId());
                        milestoneDto.setName(milestone.getName());
                        milestoneDto.setDescription(milestone.getDescription());
                        milestoneDto.setLevel(wbsDto.getLevel() + 1);
                        milestoneDto.setSequenceNumber(milestone.getSequenceNumber());
                        milestoneDto.setPlannedFinishDate(milestone.getPlannedFinishDate());
                        milestoneDto.setActualFinishDate(milestone.getActualFinishDate());
                        milestoneDto.setCreatedBy(milestone.getCreatedBy());
                        milestoneDto.setModifiedBy(milestone.getModifiedBy());
                        milestoneDto.setStatus(milestone.getStatus().toString());
                        milestoneDto.setObjectType(milestone.getObjectType().toString());
                        milestoneDto.setParent(wbsDto.getId());
                        milestoneDto.setParentId(wbsDto.getId());
                        milestoneDto.setCreatedDate(milestone.getCreatedDate());
                        if (milestone.getAssignedTo() != null) {
                            milestoneDto.setPerson(personRepository.findOne(milestone.getAssignedTo()));
                        }
                        List<PLMActivity> finishedActivities = new ArrayList<>();
                        for (PLMActivity activity : wbsActivities) {
                            if (activity.getStatus().equals(ProjectActivityStatus.FINISHED)) {
                                finishedActivities.add(activity);
                            }
                        }
                        if (wbsActivities.size() == finishedActivities.size()) {
                            milestoneDto.setFinishMilestone(true);
                        } else {
                            milestoneDto.setFinishMilestone(false);
                        }
                        children.add(milestoneDto);
                    }
                }
                if (children.size() > 0) {
                    Collections.sort(children, new Comparator<WBSDto>() {
                        public int compare(final WBSDto object1, final WBSDto object2) {
                            return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                        }
                    });
                    wbsDto.setHasBom(true);
                    wbsDto.setExpanded(true);
                    wbsDto.setChildren(children);
                }
                wbsDtoList.add(wbsDto);
            }
            Collections.sort(wbsDtoList, new Comparator<WBSDto>() {
                public int compare(final WBSDto object1, final WBSDto object2) {
                    return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                }
            });
        }
        return wbsDtoList;
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#project, 'view')")
    public List<WBSCountDto> getDrillDownProjectPlanStructure(PLMProject project) {
        List<WBSCountDto> wbsDtoList = new ArrayList<>();
        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderBySequenceNumberAsc(project);
        if (rootWbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : rootWbsElements) {
                WBSCountDto wbsDto = new WBSCountDto();
                wbsDto.setId(wbsElement.getId());
                wbsDto.setName(wbsElement.getName());
                List<WBSCountDto> children = new ArrayList<>();
                List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                if (wbsActivities.size() > 0) {
                    for (PLMActivity activity : wbsActivities) {
                        WBSCountDto activityDto = new WBSCountDto();
                        activityDto.setId(activity.getId());
                        activityDto.setName(activity.getName());
                        List<PLMTask> activityTasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activity.getId());
                        if (activityTasks.size() > 0) {
                            activityDto.setActivityTasks(activityTasks);
                        }
                        children.add(activityDto);
                    }
                }
                wbsDto.setChildren(children);
                wbsDtoList.add(wbsDto);
            }
        }
        return wbsDtoList;
    }

    @Transactional
    public void updateSequenceNumberForProjectPlan(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProjectAndSequenceNumberIsNullOrderByCreatedDateAsc(project);
        Integer wbsCount = 1;
        for (PLMWbsElement wbsElement : wbsElements) {
            wbsElement.setSequenceNumber(wbsCount);
            wbsElement = wbsElementRepository.save(wbsElement);
            List<WBSDto> children = new ArrayList<>();
            List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsActivities.size() > 0) {
                for (PLMActivity activity : wbsActivities) {
                    WBSDto activityDto = new WBSDto();
                    activityDto.setId(activity.getId());
                    activityDto.setName(activity.getName());
                    activityDto.setCreatedDate(activity.getCreatedDate());
                    activityDto.setObjectType(activity.getObjectType().toString());
                    children.add(activityDto);
                }
            }
            List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsMilestones.size() > 0) {
                for (PLMMilestone milestone : wbsMilestones) {
                    WBSDto milestoneDto = new WBSDto();
                    milestoneDto.setId(milestone.getId());
                    milestoneDto.setName(milestone.getName());
                    milestoneDto.setCreatedDate(milestone.getCreatedDate());
                    milestoneDto.setObjectType(milestone.getObjectType().toString());
                    children.add(milestoneDto);
                }
            }
            Collections.sort(children, new Comparator<WBSDto>() {
                public int compare(final WBSDto object1, final WBSDto object2) {
                    return object1.getCreatedDate().compareTo(object2.getCreatedDate());
                }
            });
            Integer childCount = 1;
            for (WBSDto child : children) {
                if (child.getObjectType().equals("PROJECTACTIVITY")) {
                    PLMActivity activity = activityRepository.findOne(child.getId());
                    activity.setSequenceNumber(childCount);
                    activity = activityRepository.save(activity);
                    List<PLMTask> plmTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
                    Integer taskCount = 1;
                    for (PLMTask plmTask : plmTasks) {
                        plmTask.setSequenceNumber(taskCount);
                        plmTask = taskRepository.save(plmTask);
                        taskCount++;
                    }

                } else if (child.getObjectType().equals("PROJECTMILESTONE")) {
                    PLMMilestone milestone = milestoneRepository.findOne(child.getId());
                    milestone.setSequenceNumber(childCount);
                    milestone = milestoneRepository.save(milestone);
                }
                childCount++;
            }
            wbsCount++;
        }
    }

    @Transactional(readOnly = true)
    public List<WBSDto> getWbsChildrenBySequence(Integer projectId, Integer wbsId) {
        List<WBSDto> wbsDtoList = new ArrayList<>();
        List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsId);
        if (wbsActivities.size() > 0) {
            for (PLMActivity activity : wbsActivities) {
                WBSDto activityDto = new WBSDto();
                activityDto.setId(activity.getId());
                activityDto.setName(activity.getName());
                activityDto.setDescription(activity.getDescription());
                activityDto.setLevel(1);
                activityDto.setActualFinishDate(activity.getActualFinishDate());
                activityDto.setActualStartDate(activity.getActualStartDate());
                activityDto.setPlannedStartDate(activity.getPlannedStartDate());
                activityDto.setPlannedFinishDate(activity.getPlannedFinishDate());
                activityDto.setSequenceNumber(activity.getSequenceNumber());
                activityDto.setCreatedBy(activity.getCreatedBy());
                activityDto.setModifiedBy(activity.getModifiedBy());
                activityDto.setObjectType(activity.getObjectType().toString());
                activityDto.setStatus(activity.getStatus().toString());
                activityDto.setParent(wbsId);
                activityDto.setParentId(wbsId);
                if (activity.getAssignedTo() != null) {
                    activityDto.setPerson(personRepository.findOne(activity.getAssignedTo()));
                }
                List<PLMTask> activityTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
                if (activityTasks.size() > 0) {
                    Double activityPercentComplete = 0.0;
                    for (PLMTask task : activityTasks) {
                        activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                    }
                    activityDto.setActivityTasks(activityTasks);
                    activityDto.setCount(activityTasks.size());
                    activityDto.setPercentComplete((activityPercentComplete) / activityTasks.size());
                }
                wbsDtoList.add(activityDto);
            }
        }
        List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsId);
        if (wbsMilestones.size() > 0) {
            for (PLMMilestone milestone : wbsMilestones) {
                WBSDto milestoneDto = new WBSDto();
                milestoneDto.setId(milestone.getId());
                milestoneDto.setName(milestone.getName());
                milestoneDto.setDescription(milestone.getDescription());
                milestoneDto.setLevel(1);
                milestoneDto.setSequenceNumber(milestone.getSequenceNumber());
                milestoneDto.setPlannedFinishDate(milestone.getPlannedFinishDate());
                milestoneDto.setActualFinishDate(milestone.getActualFinishDate());
                milestoneDto.setCreatedBy(milestone.getCreatedBy());
                milestoneDto.setModifiedBy(milestone.getModifiedBy());
                milestoneDto.setStatus(milestone.getStatus().toString());
                milestoneDto.setObjectType(milestone.getObjectType().toString());
                milestoneDto.setParent(wbsId);
                milestoneDto.setParentId(wbsId);
                if (milestone.getAssignedTo() != null) {
                    milestoneDto.setPerson(personRepository.findOne(milestone.getAssignedTo()));
                }
                List<PLMActivity> finishedActivities = new ArrayList<>();
                for (PLMActivity activity : wbsActivities) {
                    if (activity.getStatus().equals(ProjectActivityStatus.FINISHED)) {
                        finishedActivities.add(activity);
                    }
                }
                if (wbsActivities.size() == finishedActivities.size()) {
                    milestoneDto.setFinishMilestone(true);
                } else {
                    milestoneDto.setFinishMilestone(false);
                }
                wbsDtoList.add(milestoneDto);
            }
        }
        if (wbsDtoList.size() > 0) {
            Collections.sort(wbsDtoList, new Comparator<WBSDto>() {
                public int compare(final WBSDto object1, final WBSDto object2) {
                    return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                }
            });
        }
        return wbsDtoList;
    }

    @Transactional(readOnly = true)
    public List<PLMWbsElement> getProjectWbsTree(PLMProject project) {
        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectAndParentIsNull(project);
        for (PLMWbsElement wbsElement : rootWbsElements) {
            List<PLMWbsElement> rootWbsElementChildren = wbsElementRepository.findByParent(wbsElement.getId());
            if (rootWbsElements.size() > 0) {
                wbsElement.setLevel(0);
                if (rootWbsElementChildren.size() > 0) {
                    wbsElement.setHasBom(true);
                    for (PLMWbsElement imBom3 : rootWbsElementChildren) {
                        imBom3.setParent(wbsElement.getId());
                        imBom3.setLevel(wbsElement.getLevel() + 1);
                    }
//                    wbsElement.setChildren(rootWbsElementChildren);
                    wbsElement.setExpanded(Boolean.TRUE);
                    loadLevels(rootWbsElementChildren, wbsElement.getLevel() + 1);
                } else {
                    wbsElement.setExpanded(Boolean.FALSE);
                }
            }
        }
        return rootWbsElements;
    }

    private void loadLevels(List<PLMWbsElement> boms, Integer level) {
        for (PLMWbsElement imBom : boms) {
            List<PLMWbsElement> imBoms1 = wbsElementRepository.findByParent(imBom.getId());
            if (imBoms1 != null) {
                if (imBoms1.size() > 0) {
                    imBom.setLevel(level);
                    for (PLMWbsElement imBom3 : imBoms1) {
                        imBom3.setParent(imBom.getId());
                        imBom3.setLevel(imBom.getLevel() + 1);
                    }
                    imBom.setHasBom(true);
//                    imBom.setChildren(imBoms1);
                    imBom.setExpanded(Boolean.TRUE);
                } else {
                    imBom.setExpanded(Boolean.FALSE);
                }
            }
            loadLevels(imBoms1, level + 1);
        }
    }

    @Transactional(readOnly = true)
    private void loadChildrenLevels(List<PLMWbsElement> boms, Integer level) {
        for (PLMWbsElement imBom : boms) {
            List<PLMWbsElement> imBoms1 = wbsElementRepository.findByParent(imBom.getId());
            if (imBoms1 != null) {
                if (imBoms1.size() > 0) {
                    imBom.setLevel(level);
                    for (PLMWbsElement imBom3 : imBoms1) {
                        imBom3.setParent(imBom.getId());
                        imBom3.setLevel(imBom.getLevel() + 1);
                    }
                    imBom.setHasBom(true);
//                    imBom.setChildren(imBoms1);
                }
            }
            loadChildrenLevels(imBoms1, level + 1);
        }
    }

    @Transactional(readOnly = true)
    public PLMWbsElement getProjectWbsName(Integer projectId, String wbsName, Integer parentId) {
        PLMProject project = projectRepository.findOne(projectId);
        PLMWbsElement projectWbs = wbsElementRepository.findByProjectAndNameEqualsIgnoreCaseAndParent(project, wbsName, parentId);
        return projectWbs;
    }

    @Transactional(readOnly = true)
    public PLMWbsElement getParentWbsByName(Integer projectId, String parentName) {
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMWbsElement> plmWbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project);
        for (PLMWbsElement wbsElement : plmWbsElements) {
            if (wbsElement.getName().equalsIgnoreCase(parentName)) {
                return wbsElement;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    public PLMWbsElement getWbsChildren(Integer projectId, Integer wbsId) {
        PLMWbsElement wbsElement = wbsElementRepository.findOne(wbsId);
        if (wbsElement != null) {
            List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsActivities.size() != 0) {
                wbsElement.setHasBom(true);
                wbsElement.setLevel(0);
                for (PLMActivity activity : wbsActivities) {
                    activity.setLevel(wbsElement.getLevel() + 1);
                    if (activity.getAssignedTo() != null) {
                        Person person = personRepository.findOne(activity.getAssignedTo());
                        activity.setPerson(person);
                    }
                    List<PLMTask> activityTasks = taskRepository.findByActivityOrderByCreatedDateAsc(activity.getId());
                    if (activityTasks.size() != 0) {
                        Double activityPercentComplete = 0.0;
                        for (PLMTask task : activityTasks) {
                            activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                        }
                        activity.setPercentComplete((activityPercentComplete) / activityTasks.size());
                    } else {
                        activity.setActivityTasks(activityTasks);
                    }
                    activity.getActivityDeliverables().addAll(activityDeliverableRepository.findByActivity(activity.getId()));
                    activity.getActivityFiles().addAll(activityFileRepository.findByActivity(activity.getId()));
                    activity.getActivityItemReferences().addAll(activityItemReferenceRepository.findByActivity(activity.getId()));
                }
                wbsElement.setActivities(wbsActivities);
            }
            List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsMilestones.size() != 0) {
                wbsElement.setHasBom(true);
                wbsElement.setLevel(0);
                for (PLMMilestone milestone : wbsMilestones) {
                    milestone.setLevel(wbsElement.getLevel() + 1);
                    if (milestone.getAssignedTo() != null) {
                        Person person = personRepository.findOne(milestone.getAssignedTo());
                        milestone.setPerson(person);
                    }
                }
                wbsElement.setMilestones(wbsMilestones);
            }

        }
        return wbsElement;
    }

    @Transactional(readOnly = true)
    public PLMProject getProjectPercentComplete(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        Double projectPercentage = 0.0;
        Double wbsPercentage = 0.0;
        Double activityPercentage = 0.0;
        Double tasksPercent = 0.0;
        project.setPercentComplete(0.0);
        List<PLMWbsElement> projectWbsElements = wbsElementRepository.findByProject(project);
        if (projectWbsElements.size() != 0) {
            projectPercentage = 0.0;
            for (PLMWbsElement wbsElement : projectWbsElements) {
                List<PLMActivity> wbsActivities = activityRepository.findByWbs(wbsElement.getId());
                if (wbsActivities.size() != 0) {
                    activityPercentage = 0.0;
                    for (PLMActivity activity : wbsActivities) {
                        List<PLMTask> activityTasks = taskRepository.findByActivity(activity.getId());
                        if (activityTasks.size() != 0) {
                            tasksPercent = 0.0;
                            for (PLMTask task : activityTasks) {
                                tasksPercent = tasksPercent + task.getPercentComplete();
                            }
                            activity.setPercentComplete((tasksPercent) / (activityTasks.size()));
                        }
                        activityPercentage = activityPercentage + activity.getPercentComplete();
                    }
                    wbsElement.setPercentComplete((activityPercentage) / (wbsActivities.size()));
                }
                projectPercentage = projectPercentage + wbsElement.getPercentComplete();
            }
        }
        if (projectWbsElements.size() != 0) {
            project.setPercentComplete((projectPercentage) / projectWbsElements.size());
        }
        return project;
    }

    @Transactional
    public ProjectTemplate saveAsTemplate(Integer projectId, ProjectTemplate projectTemplate) {
        ProjectTemplate template = projectTemplateRepository.save(projectTemplate);
        PLMProject project = projectRepository.findOne(projectId);
        if (project.getWorkflow() != null && projectTemplate.getCopyWorkflows() && (projectTemplate.getAllWorkflows() || projectTemplate.getProjectWorkflows())) {
            attachProjectTemplateWorkflow(template.getId(), project.getWorkflow());
        }
        if (project != null) {
            List<PLMWbsElement> wbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project);
            if (projectTemplate.getTeam()) {
                List<ProjectTemplateMember> projectTemplateMembers = new LinkedList<>();
                List<PLMProjectMember> projectMembers = projectMemberRepository.findByProject(project.getId());
                for (PLMProjectMember projectMember : projectMembers) {
                    ProjectTemplateMember projectTemplateMember = new ProjectTemplateMember();
                    projectTemplateMember.setTemplate(template.getId());
                    projectTemplateMember.setPerson(projectMember.getPerson());
                    projectTemplateMember.setRole(projectMember.getRole());
                    projectTemplateMembers.add(projectTemplateMember);
                    // projectTemplateMember = projectTemplateMemberRepository.save(projectTemplateMember);
                }
                if (projectTemplateMembers.size() > 0) {
                    projectTemplateMemberRepository.save(projectTemplateMembers);
                }
            }
            if (wbsElements.size() != 0) {
                for (PLMWbsElement wbsElement : wbsElements) {
                    ProjectTemplateWbs templateWbs = new ProjectTemplateWbs();
                    templateWbs.setName(wbsElement.getName());
                    templateWbs.setSequenceNumber(wbsElement.getSequenceNumber());
                    templateWbs.setDescription(wbsElement.getDescription());
                    templateWbs.setTemplate(template.getId());
                    templateWbs = projectTemplateWbsRepository.save(templateWbs);
                    List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                    if (activities.size() != 0) {
                        for (PLMActivity activity : activities) {
                            ProjectTemplateActivity templateActivity = new ProjectTemplateActivity();
                            templateActivity.setName(activity.getName());
                            templateActivity.setDescription(activity.getDescription());
                            templateActivity.setSequenceNumber(activity.getSequenceNumber());
                            templateActivity.setWbs(templateWbs.getId());
                            if (projectTemplate.getAssignedTo()) {
                                templateActivity.setAssignedTo(activity.getAssignedTo());
                            }
                            templateActivity = projectTemplateActivityRepository.save(templateActivity);
                            copyActivityFoldersToTemplateActivity(projectTemplate, activity.getId(), templateActivity.getId());
                            if (activity.getWorkflow() != null && projectTemplate.getCopyWorkflows() && (projectTemplate.getAllWorkflows() || projectTemplate.getActivityWorkflows())) {
                                attachProjectActivityTemplateWorkflow(templateActivity.getId(), activity.getWorkflow());
                            }
                            createProjectActivityTasks(activity, templateActivity, projectTemplate);
                        }
                    }
                    List<PLMMilestone> plmMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                    if (plmMilestones.size() != 0) {
                        List<ProjectTemplateMilestone> templateMilestones = new LinkedList<>();
                        for (PLMMilestone plmMilestone : plmMilestones) {
                            ProjectTemplateMilestone templateMilestone = new ProjectTemplateMilestone();
                            templateMilestone.setName(plmMilestone.getName());
                            templateMilestone.setSequenceNumber(plmMilestone.getSequenceNumber());
                            templateMilestone.setDescription(plmMilestone.getDescription());
                            templateMilestone.setWbs(templateWbs.getId());
                            if (projectTemplate.getAssignedTo()) {
                                templateMilestone.setAssignedTo(plmMilestone.getAssignedTo());
                            }
                            templateMilestones.add(templateMilestone);
                            //templateMilestone = projectTemplateMilestoneRepository.save(templateMilestone);
                        }
                        if (templateMilestones.size() > 0) {
                            projectTemplateMilestoneRepository.save(templateMilestones);
                        }
                    }
                }
            }

            if (projectTemplate.getCopyFolders() && (projectTemplate.getAllFolders() || projectTemplate.getProjectFolders())) {
                List<PLMProjectFile> projectFiles = projectFileRepository.findByProjectAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(projectId, "FOLDER");
                for (PLMProjectFile projectFile : projectFiles) {
                    ProjectTemplateFile projectTemplateFile = new ProjectTemplateFile();
                    projectTemplateFile.setName(projectFile.getName());
                    projectTemplateFile.setDescription(projectFile.getDescription());
                    String folderNumber = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                    if (autoNumber != null) {
                        folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    projectTemplateFile.setVersion(1);
                    projectTemplateFile.setSize(0L);
                    projectTemplateFile.setTemplate(template.getId());
                    projectTemplateFile.setFileNo(folderNumber);
                    projectTemplateFile.setFileType("FOLDER");
                    projectTemplateFile = projectTemplateFileRepository.save(projectTemplateFile);

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + template.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + projectTemplateFile.getId();
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }

                    copyProjectTemplateFileChildrenFolders(projectFile.getId(), template.getId(), projectTemplateFile.getId());
                }
            }
        }
        return template;
    }

    private void copyActivityFoldersToTemplateActivity(ProjectTemplate projectTemplate, Integer activity, Integer template) {
        if (projectTemplate.getCopyFolders() && (projectTemplate.getAllFolders() || projectTemplate.getActivityFolders())) {
            List<PLMActivityFile> activityFiles = activityFileRepository.findByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(activity, "FOLDER");
            for (PLMActivityFile activityFile : activityFiles) {
                ProjectTemplateActivityFile projectTemplateActivityFile = new ProjectTemplateActivityFile();
                projectTemplateActivityFile.setName(activityFile.getName());
                projectTemplateActivityFile.setDescription(activityFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectTemplateActivityFile.setVersion(1);
                projectTemplateActivityFile.setSize(0L);
                projectTemplateActivityFile.setActivity(template);
                projectTemplateActivityFile.setFileNo(folderNumber);
                projectTemplateActivityFile.setFileType("FOLDER");
                projectTemplateActivityFile = projectTemplateActivityFileRepository.save(projectTemplateActivityFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + template;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + projectTemplateActivityFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                copyActivityTemplateFileChildrenFolders(activityFile.getId(), template, projectTemplateActivityFile.getId());
            }
        }
    }

    private void copyActivityTemplateFileChildrenFolders(Integer activityFile, Integer templateId, Integer templateFileId) {
        List<PLMActivityFile> foldersList = activityFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(activityFile, "FOLDER");
        for (PLMActivityFile plmFile : foldersList) {
            ProjectTemplateActivityFile projectTemplateActivityFile = new ProjectTemplateActivityFile();
            projectTemplateActivityFile.setName(plmFile.getName());
            projectTemplateActivityFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            projectTemplateActivityFile.setVersion(1);
            projectTemplateActivityFile.setSize(0L);
            projectTemplateActivityFile.setParentFile(templateFileId);
            projectTemplateActivityFile.setActivity(templateId);
            projectTemplateActivityFile.setFileNo(folderNumber);
            projectTemplateActivityFile.setFileType("FOLDER");
            projectTemplateActivityFile = projectTemplateActivityFileRepository.save(projectTemplateActivityFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentActivityTemplateFileSystemPath(templateId, projectTemplateActivityFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyActivityTemplateFileChildrenFolders(plmFile.getId(), templateId, projectTemplateActivityFile.getId());
        }
    }


    private void copyProjectTemplateFileChildrenFolders(Integer programFile, Integer templateId, Integer templateFileId) {
        List<PLMProjectFile> foldersList = projectFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(programFile, "FOLDER");
        for (PLMProjectFile plmFile : foldersList) {
            ProjectTemplateFile programTemplateFile = new ProjectTemplateFile();
            programTemplateFile.setName(plmFile.getName());
            programTemplateFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            programTemplateFile.setVersion(1);
            programTemplateFile.setSize(0L);
            programTemplateFile.setParentFile(templateFileId);
            programTemplateFile.setTemplate(templateId);
            programTemplateFile.setFileNo(folderNumber);
            programTemplateFile.setFileType("FOLDER");
            programTemplateFile = projectTemplateFileRepository.save(programTemplateFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentFileSystemPath(templateId, programTemplateFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectTemplateFileChildrenFolders(plmFile.getId(), templateId, programTemplateFile.getId());
        }
    }


    @Transactional
    public void createProjectActivityTasks(PLMActivity activity, ProjectTemplateActivity projectTemplateActivity, ProjectTemplate template) {
        List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
        if (tasks != null) {
            for (PLMTask task : tasks) {
                PLMTask exitTask = taskRepository.findByActivityAndNameEqualsIgnoreCase(activity.getId(), task.getName());
                if (exitTask != null) {
                    ProjectTemplateTask templateTask1 = new ProjectTemplateTask();
                    templateTask1.setName(task.getName());
                    templateTask1.setDescription(task.getDescription());
                    templateTask1.setActivity(projectTemplateActivity.getId());
                    if (template.getAssignedTo()) {
                        templateTask1.setAssignedTo(task.getAssignedTo());
                    }
                    ProjectTemplateTask templateTask2 = projectTemplateTaskRepository.save(templateTask1);
                    if (task.getWorkflow() != null && template.getCopyWorkflows() && (template.getAllWorkflows() || template.getTaskWorkflows())) {
                        attachProjectTaskTemplateWorkflow(templateTask2.getId(), task.getWorkflow());
                    }
                    if (template.getCopyFolders() && (template.getAllFolders() || template.getTaskFolders())) {
                        List<PLMTaskFile> taskFiles = taskFileRepository.findByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(task.getId(), "FOLDER");
                        for (PLMTaskFile taskFile : taskFiles) {
                            ProjectTemplateTaskFile templateTaskFile = new ProjectTemplateTaskFile();
                            templateTaskFile.setName(taskFile.getName());
                            templateTaskFile.setDescription(taskFile.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            templateTaskFile.setVersion(1);
                            templateTaskFile.setSize(0L);
                            templateTaskFile.setTask(templateTask2.getId());
                            templateTaskFile.setFileNo(folderNumber);
                            templateTaskFile.setFileType("FOLDER");
                            templateTaskFile = projectTemplateTaskFileRepository.save(templateTaskFile);

                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + templateTask2.getId();
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = dir + File.separator + templateTaskFile.getId();
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            copyProjectTemplateTaskFileChildrenFolders(taskFile.getId(), templateTask2.getId(), templateTaskFile.getId());
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public void attachProjectTemplateWorkflow(Integer id, Integer wfDefId) {
        ProjectTemplate project = projectTemplateRepository.findOne(id);
        PLMWorkflow workflow1 = workflowRepository.findOne(wfDefId);
        if (workflow1 != null) {
            PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
            PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
            if (wfDef1 != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.TEMPLATE, project.getId(), wfDef1);
                project.setWorkflow(workflow.getId());
                projectTemplateRepository.save(project);
            }
        }

    }

    @Transactional
    public void attachProjectTaskTemplateWorkflow(Integer id, Integer wfDefId) {
        ProjectTemplateTask templateTask = projectTemplateTaskRepository.findOne(id);
        PLMWorkflow workflow1 = workflowRepository.findOne(wfDefId);
        if (workflow1 != null) {
            PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
            PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
            if (wfDef1 != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.TEMPLATETASK, templateTask.getId(), wfDef1);
                templateTask.setWorkflow(workflow.getId());
                projectTemplateTaskRepository.save(templateTask);
            }
        }
    }

    @Transactional
    public void attachProjectActivityTemplateWorkflow(Integer id, Integer wfDefId) {
        ProjectTemplateActivity templateActivity = projectTemplateActivityRepository.findOne(id);
        PLMWorkflow workflow1 = workflowRepository.findOne(wfDefId);
        if (workflow1 != null) {
            PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
            PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
            if (wfDef1 != null) {
                PLMWorkflow workflow = workflowService.attachWorkflow(PLMObjectType.TEMPLATEACTIVITY, templateActivity.getId(), wfDef1);
                templateActivity.setWorkflow(workflow.getId());
                projectTemplateActivityRepository.save(templateActivity);
            }
        }
    }

    @Transactional
    public PLMFileDownloadHistory fileDownloadHistory(Integer projectId, Integer fileId) throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMProject project = projectRepository.findOne(projectId);
        PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);

		/* App events */
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileDownloadedEvent(project, plmProjectFile));
        sendProjectSubscribeNotification(project, plmProjectFile.getName(), "fileDownloaded");

        return plmFileDownloadHistory;
    }

    @Transactional(readOnly = true)
    public List<PLMProjectFile> getProjectFileVersionAndCommentsAndDownloads(Integer itemId, Integer fileId, ObjectType objectType) {
        List<PLMProjectFile> projectFiles = new ArrayList<>();
        PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmProjectFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmProjectFile.getId());
        if (comments.size() > 0) {
            plmProjectFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmProjectFile.setDownloadHistories(fileDownloadHistories);
        }
        projectFiles.add(plmProjectFile);
        List<PLMProjectFile> files = projectFileRepository.findByProjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmProjectFile.getProject(), plmProjectFile.getFileNo());
        if (files.size() > 0) {
            for (PLMProjectFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                projectFiles.add(file);
            }
        }
        return projectFiles;
    }

    @Transactional(readOnly = true)
    public List<PLMGlossary> getProjectGlossariesDeliverables(Integer projectId) {
        List<PLMGlossary> glossarys = new ArrayList<>();
        List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(projectId);
        if (glossaryDeliverables.size() > 0) {
            for (PLMGlossaryDeliverable glossaryDeliverable : glossaryDeliverables) {
                PLMGlossary glossary = glossaryRepository.findOne(glossaryDeliverable.getGlossary().getId());
                glossarys.add(glossary);
            }

        }
        return glossarys;
    }

    @Transactional
    public void deleteProjectDeliverable(Integer projectId, Integer itemId) throws JsonProcessingException {
        PLMProject project = projectRepository.findOne(projectId);
        PLMDeliverable deliverable = deliverableRepository.findOne(itemId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectDeliverableDeletedEvent(project, plmItem));
        sendProjectSubscribeNotification(project, plmItem.getItemNumber(), "deliverableDeleted");
        projectDeliveravbleRepository.delete(itemId);
    }

    @Transactional
    public void deleteGlossaryDeliverable(Integer projectId, Integer glossaryId) {
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        PLMGlossaryDeliverable glossaryDeliverable = glossaryDeliverableRepository.findByObjectIdAndGlossary(projectId, glossary);

        PLMProject project = projectRepository.findOne(projectId);
        if (project != null) {
            glossaryDeliverableRepository.delete(glossaryDeliverable);
        }
        PLMActivity activity = activityRepository.findOne(projectId);
        if (activity != null) {
            PLMWbsElement wbsElement1 = wbsElementRepository.findOne(activity.getWbs());
            glossaryDeliverableRepository.delete(glossaryDeliverable);
        }
        PLMTask task = taskRepository.findOne(projectId);
        if (task != null) {
            glossaryDeliverableRepository.delete(glossaryDeliverable);
        }
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getProjectItemDeliverable(Integer project, Pageable pageable, ProjectDeliverableCriteria criteria) {
        criteria.setProject(project);
        Predicate predicate = projectItemDeliverableBuilder.build(criteria, com.cassinisys.plm.model.plm.QPLMItem.pLMItem);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        return items;
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossary> getProjectGlossaryDeliverables(Integer project, Pageable pageable, ProjectDeliverableCriteria criteria) {
        criteria.setProject(project);
        Predicate predicate = projectGlossaryDeliverableBuilder.build(criteria, com.cassinisys.plm.model.rm.QPLMGlossary.pLMGlossary);
        Page<PLMGlossary> glossaries = glossaryRepository.findAll(predicate, pageable);
        return glossaries;
    }

    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getProjectDeliverableAndGlossaryDeliverble(Integer projectId) {
        PLMProjectDeliverableDto plmProjectDeliverableDtos = new PLMProjectDeliverableDto();
        List<PLMProjectDeliverable> deliverableList = projectDeliveravbleRepository.findByProject(projectId);
        List<PLMDeliverable> deliverables = new ArrayList<>();
        if (deliverableList.size() > 0) {
            for (PLMProjectDeliverable projectDeliverable : deliverableList) {
                PLMDeliverable deliverable = deliverableRepository.findOne(projectDeliverable.getId());
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                PLMProject plmProject = projectRepository.findOne(projectDeliverable.getProject());
                Person person = personRepository.findOne(plmProject.getProjectManager());
                deliverable.setRevision(itemRevision);
                PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                deliverable.setItem(item);
                deliverable.setOwner(person.getFullName());
                deliverable.setOwnerId(person.getId());
                deliverable.setContextName(plmProject.getName());
                deliverable.setObjectId(projectDeliverable.getProject());
                deliverable.setObjectType(PLMObjectType.PROJECT.toString());
                deliverable.setStatus(projectDeliverable.getDeliverableStatus().name());
                deliverables.add(deliverable);
            }
        }
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
        if (wbsElements.size() > 0) {
            for (PLMWbsElement wbsElement : wbsElements) {
                List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                for (PLMActivity activity : activities) {
                    List<PLMActivityDeliverable> activityDeliverables = activityDeliverableRepository.findByActivity(activity.getId());
                    for (PLMActivityDeliverable activityDeliverable : activityDeliverables) {
                        Boolean deliverableExist = false;
                        for (PLMDeliverable deliverable : deliverables) {
                            if (activityDeliverable.getItemRevision().equals(deliverable.getItemRevision())) {
                                deliverableExist = true;
                            }
                        }
                        if (!deliverableExist) {
                            PLMDeliverable deliverable = deliverableRepository.findOne(activityDeliverable.getId());
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(deliverable.getItemRevision());
                            deliverable.setRevision(itemRevision);
                            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
                            PLMActivity plmActivity = activityRepository.findOne(activityDeliverable.getActivity());
                            if (plmActivity.getAssignedTo() != null) {
                                Person person = personRepository.findOne(plmActivity.getAssignedTo());
                                deliverable.setOwner(person.getFullName());
                                deliverable.setOwnerId(person.getId());
                            }
                            deliverable.setContextName(plmActivity.getName());
                            deliverable.setItem(item);
                            deliverable.setObjectId(activityDeliverable.getActivity());
                            deliverable.setObjectType(PLMObjectType.PROJECTACTIVITY.toString());
                            deliverable.setStatus(activityDeliverable.getDeliverableStatus().name());
                            deliverables.add(deliverable);
                        }
                    }
                    List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
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
                                deliverable.setStatus(taskDeliverable.getDeliverableStatus().name());
                                deliverables.add(deliverable);
                            }
                        }
                    }
                }
            }
        }
        plmProjectDeliverableDtos.getItemDeliverables().addAll(deliverables);
        List<PLMGlossaryDeliverable> glossaryDeliverables = glossaryDeliverableRepository.findByObjectId(projectId);
        plmProjectDeliverableDtos.getGlossaryDeliverables().addAll(glossaryDeliverables);
        for (PLMWbsElement wbsElement : wbsElements) {
            List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            for (PLMActivity activity : activities) {
                List<PLMGlossaryDeliverable> activityDeliverables = glossaryDeliverableRepository.findByObjectId(activity.getId());
                for (PLMGlossaryDeliverable activityDeliverable : activityDeliverables) {
                    Boolean deliverableExist = false;
                    for (PLMGlossaryDeliverable deliverable : glossaryDeliverables) {
                        if (activityDeliverable.getGlossary().getId().equals(deliverable.getGlossary().getId())) {
                            deliverableExist = true;
                        }
                    }
                    if (!deliverableExist) {
                        plmProjectDeliverableDtos.getGlossaryDeliverables().add(activityDeliverable);
                    }
                }
                List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
                for (PLMTask task : tasks) {
                    List<PLMGlossaryDeliverable> taskDeliverables = glossaryDeliverableRepository.findByObjectId(task.getId());
                    for (PLMGlossaryDeliverable taskDeliverable : taskDeliverables) {
                        Boolean taskDeliverableExist = false;
                        for (PLMGlossaryDeliverable deliverable : plmProjectDeliverableDtos.getGlossaryDeliverables()) {
                            if (taskDeliverable.getGlossary().getId().equals(deliverable.getGlossary().getId())) {
                                taskDeliverableExist = true;
                            }
                        }
                        if (!taskDeliverableExist) {
                            plmProjectDeliverableDtos.getGlossaryDeliverables().add(taskDeliverable);
                        }
                    }
                }
            }
        }
        return plmProjectDeliverableDtos;
    }

    /*--------------- Getting Specifications deliverables ---------------------*/
    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getSpecificationDeliverables(Integer projectId) {
        PLMProjectDeliverableDto plmProjectDeliverableDtos = new PLMProjectDeliverableDto();
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
        List<SpecificationDeliverable> specificationDeliverables = specificationDeliverableRepository.findByObjectId(projectId);
        plmProjectDeliverableDtos.getSpecificationDeliverables().addAll(specificationDeliverables);
        for (PLMWbsElement wbsElement : wbsElements) {
            List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            for (PLMActivity activity : activities) {
                List<SpecificationDeliverable> activityDeliverables = specificationDeliverableRepository.findByObjectId(activity.getId());
                for (SpecificationDeliverable activityDeliverable : activityDeliverables) {
                    Boolean deliverableExist = false;
                    for (SpecificationDeliverable deliverable : specificationDeliverables) {
                        if (activityDeliverable.getSpecification().getId().equals(deliverable.getSpecification().getId())) {
                            deliverableExist = true;
                        }
                    }
                    if (!deliverableExist) {
                        plmProjectDeliverableDtos.getSpecificationDeliverables().add(activityDeliverable);
                    }
                }
                List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
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
            }
        }
        return plmProjectDeliverableDtos;
    }

    /*------------------- Get Requirement Deliverables ----------------*/

    @Transactional(readOnly = true)
    public PLMProjectDeliverableDto getRequirementDeliverables(Integer projectId) {
        PLMProjectDeliverableDto plmProjectDeliverableDtos = new PLMProjectDeliverableDto();
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
        List<RequirementDeliverable> specificationDeliverables = requirementDeliverableRepository.findByObjectId(projectId);
        plmProjectDeliverableDtos.getRequirementDeliverables().addAll(specificationDeliverables);
        for (PLMWbsElement wbsElement : wbsElements) {
            List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            for (PLMActivity activity : activities) {
                List<RequirementDeliverable> activityDeliverables = requirementDeliverableRepository.findByObjectId(activity.getId());
                for (RequirementDeliverable activityDeliverable : activityDeliverables) {
                    Boolean deliverableExist = false;
                    for (RequirementDeliverable deliverable : specificationDeliverables) {
                        if (activityDeliverable.getRequirement().getId().equals(deliverable.getRequirement().getId())) {
                            deliverableExist = true;
                        }
                    }
                    if (!deliverableExist) {
                        plmProjectDeliverableDtos.getRequirementDeliverables().add(activityDeliverable);
                    }
                }
                List<PLMTask> tasks = taskRepository.findByActivity(activity.getId());
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
            }
        }
        return plmProjectDeliverableDtos;
    }

    @Transactional
    public PLMWbsElement copyWbsStructure(Integer projectId, Integer wbs) {
        PLMWbsElement wbsElement = wbsElementRepository.findOne(wbs);
        PLMProject project = projectRepository.findOne(projectId);
        PLMWbsElement plmWbsElement = new PLMWbsElement();
        PLMWbsElement copyWbsExist = wbsElementRepository.findByProjectAndNameEqualsIgnoreCase(project, "Copy - " + wbsElement.getName());
        if (copyWbsExist == null) {
            plmWbsElement.setName("Copy - " + wbsElement.getName());
        } else {
            plmWbsElement.setName("Copy(1) " + wbsElement.getName());
        }
        plmWbsElement.setDescription(wbsElement.getDescription());
        plmWbsElement.setProject(project);
        plmWbsElement = wbsElementRepository.save(plmWbsElement);
        List<PLMActivity> wbsActivities = activityRepository.findByWbs(wbsElement.getId());
        for (PLMActivity activity : wbsActivities) {
            PLMActivity copyActivity = new PLMActivity();
            copyActivity.setName(activity.getName());
            copyActivity.setDescription(activity.getDescription());
            copyActivity.setWbs(plmWbsElement.getId());
            copyActivity.setStatus(ProjectActivityStatus.PENDING);
            copyActivity = activityRepository.save(copyActivity);
        }
        List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
        for (PLMMilestone milestone : wbsMilestones) {
            PLMMilestone copyMilestone = new PLMMilestone();
            copyMilestone.setName(milestone.getName());
            copyMilestone.setDescription(milestone.getDescription());
            copyMilestone.setStatus(ProjectActivityStatus.PENDING);
            copyMilestone.setWbs(plmWbsElement.getId());
            copyMilestone = milestoneRepository.save(copyMilestone);
        }
        return wbsElement;
    }

    @Transactional
    public PLMProjectFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMProjectFile file1 = projectFileRepository.findOne(id);
        file1.setLatest(false);
        PLMProjectFile plmProjectFile = projectFileRepository.save(file1);
        PLMProjectFile projectFile = (PLMProjectFile) Utils.cloneObject(plmProjectFile, PLMProjectFile.class);
        PLMProjectFile oldFile = (PLMProjectFile) Utils.cloneObject(plmProjectFile, PLMProjectFile.class);
        if (projectFile != null) {
            projectFile.setId(null);
            projectFile.setName(newFileName);
            projectFile.setVersion(file1.getVersion() + 1);
            projectFile.setLatest(true);
            projectFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            projectFile = projectFileRepository.save(projectFile);
            if (projectFile.getParentFile() != null) {
                PLMProjectFile parent = projectFileRepository.findOne(projectFile.getParentFile());
                parent.setModifiedDate(projectFile.getModifiedDate());
                parent = projectFileRepository.save(parent);
            }
            if (plmProjectFile != null) {
                qualityFileService.copyFileAttributes(plmProjectFile.getId(), projectFile.getId());
            }
            String dir = "";
            dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + projectFile.getProject();
            dir = dir + File.separator + projectFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + projectFile.getProject() + File.separator + file1.getId();
            FileInputStream instream = null;
            FileOutputStream outstream = null;

            Utils.copyDataUsingStream(oldFileDir, instream, outstream, dir);
            PLMProject project = projectRepository.findOne(plmProjectFile.getProject());
             /* App Events */
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileRenamedEvent(project, "Rename", oldFile, projectFile));
            sendProjectSubscribeNotification(project, oldFile.getName() + "-" + projectFile.getName(), "fileRename");
        }
        return projectFile;
    }

    @Transactional
    public PLMFile updateProjectFile(Integer id, PLMProjectFile plmProjectFile) throws JsonProcessingException {
        PLMFile file = fileRepository.findOne(id);
        PLMProjectFile projectFile = projectFileRepository.findOne(file.getId());
        PLMProject project = projectRepository.findOne(projectFile.getProject());
        if (file != null) {
                /* App events */
            if (!projectFile.getLocked()) {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileLockedEvent(project, projectFile));
            } else {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileUnlockedEvent(project, projectFile));
            }
            file.setDescription(plmProjectFile.getDescription());
            file.setLocked(plmProjectFile.getLocked());
            file.setLockedBy(plmProjectFile.getLockedBy());
            file.setLockedDate(plmProjectFile.getLockedDate());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public PLMFile updateProjectTaskFile(Integer id, PLMTaskFile plmItemFile) {
        PLMFile file = fileRepository.findOne(id);
        PLMTaskFile projectFile = taskFileRepository.findOne(file.getId());
        PLMTask task = taskRepository.findOne(projectFile.getTask());
        PLMActivity activity = activityRepository.findOne(task.getActivity());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());

        if (file != null) {
            file.setDescription(plmItemFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }


    /* Email settings methods */
    @Transactional
    public PLMProjectEmailSetting createEmailSettings(PLMProjectEmailSetting emailSetting) {
        return projectEmailSettingsRepository.save(emailSetting);
    }

    @Transactional(readOnly = true)
    public PLMProjectEmailSetting getEmailSettings(Integer project) {
        return projectEmailSettingsRepository.findByProject(project);
    }

    @Transactional
    public PLMProjectEmailSetting updateEmailSettings(PLMProjectEmailSetting emailSetting) {
        return projectEmailSettingsRepository.save(emailSetting);
    }

    @Transactional
    public void deleteEmailSettings(Integer project) {
        projectEmailSettingsRepository.delete(project);
    }

    @Transactional
    public void createActivityTasks(ProjectTemplateActivity templateActivity, PLMActivity activity, PLMProject project) {
        PMObjectType taskType = null;
        List<PMObjectType> objectTypes = pmObjectTypeRepository.findByTypeAndParentIsNullOrderByIdAsc(PMType.PROJECTTASK);
        if (objectTypes.size() > 0) {
            taskType = objectTypes.get(0);
        }
        List<ProjectTemplateTask> templateTasks = projectTemplateTaskRepository.findByActivity(templateActivity.getId());
        if (templateTasks != null) {
            for (ProjectTemplateTask projectTemplateTask : templateTasks) {
                ProjectTemplateTask exitTask = projectTemplateTaskRepository.findByActivityAndNameEqualsIgnoreCase(templateActivity.getId(), projectTemplateTask.getName());
                if (exitTask != null) {
                    PLMTask task = new PLMTask();
                    task.setName(projectTemplateTask.getName());
                    task.setDescription(projectTemplateTask.getDescription());
                    task.setActivity(activity.getId());
                    task.setType(taskType);
                    if (project.getAssignedTo()) {
                        task.setAssignedTo(projectTemplateTask.getAssignedTo());
                    }
                    PLMTask plmTask = taskRepository.save(task);
                    if (projectTemplateTask.getWorkflow() != null && project.getCopyWorkflows() && (project.getAllWorkflows() || project.getTaskWorkflows())) {
                        attachProjectTaskWorkflow(plmTask.getId(), null, projectTemplateTask.getWorkflow());
                    }
                    if (project.getCopyFolders() && (project.getAllFolders() || project.getTaskFolders())) {
                        List<ProjectTemplateTaskFile> templateTaskFiles = projectTemplateTaskFileRepository.findByTaskAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(projectTemplateTask.getId(), "FOLDER");
                        for (ProjectTemplateTaskFile templateTaskFile : templateTaskFiles) {
                            PLMTaskFile taskFile = new PLMTaskFile();
                            taskFile.setName(templateTaskFile.getName());
                            taskFile.setDescription(templateTaskFile.getDescription());
                            String folderNumber = null;
                            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                            if (autoNumber != null) {
                                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                            }
                            taskFile.setVersion(1);
                            taskFile.setSize(0L);
                            taskFile.setTask(plmTask.getId());
                            taskFile.setFileNo(folderNumber);
                            taskFile.setFileType("FOLDER");
                            taskFile = taskFileRepository.save(taskFile);

                            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                    "filesystem" + File.separator + plmTask.getId();
                            File fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            dir = dir + File.separator + taskFile.getId();
                            fDir = new File(dir);
                            if (!fDir.exists()) {
                                fDir.mkdirs();
                            }
                            copyProjectTaskFileChildrenFolders(templateTaskFile.getId(), plmTask.getId(), taskFile.getId());
                        }
                    }
                }
            }
        }
    }

    @Transactional
    public PLMWorkflow attachProjectTaskWorkflow(Integer id, Integer wfDefId, Integer workflowId) {
        PLMWorkflow workflow = null;
        PLMTask task = taskRepository.findOne(id);
        if (wfDefId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (task != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTTASK, task.getId(), wfDef);
                task.setWorkflow(workflow.getId());
                taskRepository.save(task);
            }
        } else if (workflowId != null) {
            PLMWorkflow workflow1 = workflowRepository.findOne(workflowId);
            if (workflow1 != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef1 != null) {
                    workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTTASK, task.getId(), wfDef1);
                    task.setWorkflow(workflow.getId());
                    taskRepository.save(task);
                }
            }
        }
        return workflow;
    }


    @Transactional
    public PLMWorkflow attachProjectActivityWorkflow(Integer id, Integer wfDefId, Integer workflowId) {
        PLMWorkflow workflow = null;
        PLMActivity activity = activityRepository.findOne(id);
        if (wfDefId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (activity != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTACTIVITY, activity.getId(), wfDef);
                activity.setWorkflow(workflow.getId());
                activityRepository.save(activity);
            }
        } else if (workflowId != null) {
            PLMWorkflow workflow1 = workflowRepository.findOne(workflowId);
            if (workflow1 != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef1 != null) {
                    workflow = workflowService.attachWorkflow(PLMObjectType.PROJECTACTIVITY, activity.getId(), wfDef1);
                    activity.setWorkflow(workflow.getId());
                    activityRepository.save(activity);
                }
            }
        }
        return workflow;
    }


    private void copyProjectTaskFileChildrenFolders(Integer templateTaskFile, Integer taskId, Integer taskFileId) {
        List<ProjectTemplateTaskFile> foldersList = projectTemplateTaskFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(templateTaskFile, "FOLDER");
        for (ProjectTemplateTaskFile plmFile : foldersList) {
            PLMTaskFile taskFile = new PLMTaskFile();
            taskFile.setName(plmFile.getName());
            taskFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            taskFile.setVersion(1);
            taskFile.setSize(0L);
            taskFile.setParentFile(taskFileId);
            taskFile.setTask(taskId);
            taskFile.setFileNo(folderNumber);
            taskFile.setFileType("FOLDER");
            taskFile = taskFileRepository.save(taskFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentTaskFileSystemPath(taskId, taskFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectTaskFileChildrenFolders(plmFile.getId(), taskId, taskFile.getId());
        }
    }


    private void copyProjectTemplateTaskFileChildrenFolders(Integer templateTaskFile, Integer taskId, Integer taskFileId) {
        List<PLMTaskFile> foldersList = taskFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(templateTaskFile, "FOLDER");
        for (PLMTaskFile plmFile : foldersList) {
            ProjectTemplateTaskFile projectTemplateTaskFile = new ProjectTemplateTaskFile();
            projectTemplateTaskFile.setName(plmFile.getName());
            projectTemplateTaskFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            projectTemplateTaskFile.setVersion(1);
            projectTemplateTaskFile.setSize(0L);
            projectTemplateTaskFile.setParentFile(taskFileId);
            projectTemplateTaskFile.setTask(taskId);
            projectTemplateTaskFile.setFileNo(folderNumber);
            projectTemplateTaskFile.setFileType("FOLDER");
            projectTemplateTaskFile = projectTemplateTaskFileRepository.save(projectTemplateTaskFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentTaskTemplateFileSystemPath(taskId, projectTemplateTaskFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectTemplateTaskFileChildrenFolders(plmFile.getId(), taskId, projectTemplateTaskFile.getId());
        }
    }


    @Transactional(readOnly = true)
    public List<PLMProject> findMultiple(Iterable<Integer> ids) {
        return projectRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMProject> getPersonProjects(Integer personId) {
        List<PLMProject> projects = projectRepository.findByProjectManager(personId);
        List<PLMProject> projects1 = new ArrayList<>();
        for (PLMProject project : projects) {
            project = getProjectPercentComplete(project.getId());
            projects1.add(project);
        }
        return projects1;
    }

    @Transactional(readOnly = true)
    public PLMProject getWbsProject(Integer wbs) {
        PLMWbsElement wbsElement = wbsElementRepository.findOne(wbs);
        return projectRepository.findOne(wbsElement.getProject().getId());
    }

    @Transactional
    public List<PLMProjectFile> replaceProjectFiles(Integer projectId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMProjectFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        String htmlTable = null;
        boolean itemFileNotExist = false;
        String action = null;
        PLMProject project = projectRepository.findOne(projectId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        String name = null;
        PLMProjectFile plmProjectFile = null;
        PLMProjectFile oldFile = null;
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
                    PLMProjectFile projectFile = null;
                    plmProjectFile = projectFileRepository.findOne(fileId);
                    if (plmProjectFile != null && plmProjectFile.getParentFile() != null) {
                        projectFile = projectFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        projectFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(projectId, name);
                    }

                    if (plmProjectFile != null) {
                        plmProjectFile.setLatest(false);
                        plmProjectFile = projectFileRepository.save(plmProjectFile);
                        oldFile = JsonUtils.cloneEntity(plmProjectFile, PLMProjectFile.class);
                    }
                    projectFile = new PLMProjectFile();
                    projectFile.setName(name);
                    if (plmProjectFile != null && plmProjectFile.getParentFile() != null) {
                        projectFile.setParentFile(plmProjectFile.getParentFile());
                    }
                    if (plmProjectFile != null) {
                        projectFile.setFileNo(plmProjectFile.getFileNo());
                        projectFile.setVersion(plmProjectFile.getVersion() + 1);
                        projectFile.setReplaceFileName(plmProjectFile.getName() + " Replaced to " + name);
                    }
                    projectFile.setCreatedBy(login.getPerson().getId());
                    projectFile.setModifiedBy(login.getPerson().getId());
                    projectFile.setProject(projectId);
                    projectFile.setSize(file.getSize());
                    projectFile.setFileType("FILE");
                    projectFile = projectFileRepository.save(projectFile);
                    if (projectFile.getParentFile() != null) {
                        PLMProjectFile parent = projectFileRepository.findOne(projectFile.getParentFile());
                        parent.setModifiedDate(projectFile.getModifiedDate());
                        parent = projectFileRepository.save(parent);
                    }
                    if (plmProjectFile != null) {
                        qualityFileService.copyFileAttributes(plmProjectFile.getId(), projectFile.getId());
                    }
                    String dir = "";
                    if (plmProjectFile != null && plmProjectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(projectId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + projectId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + projectFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(projectFile);

					/* App Events */
                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFileRenamedEvent(project, "Replace", oldFile, projectFile));
                    sendProjectSubscribeNotification(project, oldFile.getName() + "-" + projectFile.getName(), "fileReplace");
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer projectId, Integer fileId) {
        String path = "";
        PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
        if (plmProjectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(projectId, plmProjectFile.getParentFile(), path);
        } else {
            path = File.separator + projectId;
        }
        return path;
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMProjectFile projectFile = projectFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }

    @Transactional
    public PLMProjectFile createProjectFolder(Integer projectId, PLMProjectFile plmProjectFile) throws JsonProcessingException {
        plmProjectFile.setId(null);
        String folderNumber = null;
        PLMProject project = projectRepository.findOne(projectId);
        PLMProjectFile existFolderName = null;
        if (plmProjectFile.getParentFile() != null) {
            existFolderName = projectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProjectAndLatestTrue(plmProjectFile.getName(), plmProjectFile.getParentFile(), projectId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmProjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = projectFileRepository.findByNameEqualsIgnoreCaseAndProjectAndLatestTrue(plmProjectFile.getName(), projectId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmProjectFile.setProject(projectId);
        plmProjectFile.setFileNo(folderNumber);
        plmProjectFile.setFileType("FOLDER");
        plmProjectFile = projectFileRepository.save(plmProjectFile);
        if (plmProjectFile.getParentFile() != null) {
            PLMProjectFile parent = projectFileRepository.findOne(plmProjectFile.getParentFile());
            parent.setModifiedDate(plmProjectFile.getModifiedDate());
            parent = projectFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, plmProjectFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFoldersAddedEvent(project, plmProjectFile));
        sendProjectSubscribeNotification(project, plmProjectFile.getName(), "folderAdded");
        return plmProjectFile;
    }

    @Transactional
    public List<PLMProjectFile> uploadProjectFolderFiles(Integer projectId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException, JsonProcessingException {
        List<PLMProjectFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<PLMProjectFile> newFiles = new ArrayList<>();
        List<PLMProjectFile> versionedFiles = new ArrayList<>();
        PLMProject project = projectRepository.findOne(projectId);
        Login login = sessionWrapper.getSession().getLogin();
        String fileNames = null;
        PLMProjectFile projectFile = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                projectFile = projectFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                if (projectFile != null) {
                    comments = commentRepository.findAllByObjectId(projectFile.getId());
                }
                boolean versioned = false;
                Integer version = 1;
                String autoNumber1 = null;
                if (projectFile != null) {
                    projectFile.setLatest(false);
                    Integer oldVersion = projectFile.getVersion();
                    version = oldVersion + 1;
                    autoNumber1 = projectFile.getFileNo();
                    projectFileRepository.save(projectFile);
                    versioned = true;
                }
                if (projectFile == null) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                }
                projectFile = new PLMProjectFile();
                projectFile.setName(name);
                projectFile.setFileNo(autoNumber1);
                projectFile.setFileType("FILE");
                projectFile.setParentFile(fileId);
                /*itemFile.setReplaceFileName(name);*/
                projectFile.setCreatedBy(login.getPerson().getId());
                projectFile.setModifiedBy(login.getPerson().getId());
                projectFile.setProject(project.getId());
                projectFile.setVersion(version);
                projectFile.setSize(file.getSize());
                projectFile.setFileType("FILE");
                projectFile = projectFileRepository.save(projectFile);
                if (fileNames == null) {
                    fNames = projectFile.getName();
                    fileNames = projectFile.getName() + " - Version : " + projectFile.getVersion();
                } else {
                    fNames = fNames + " , " + projectFile.getName();
                    fileNames = fileNames + " , " + projectFile.getName() + " - Version : " + projectFile.getVersion();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(projectId, fileId);
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
                    projectFile = projectFileRepository.save(projectFile);
                }*/
                uploaded.add(projectFile);
                if (versioned) {
                    versionedFiles.add(projectFile);
                } else {
                    newFiles.add(projectFile);
                }
            }
             /* App Events */
            if (newFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFilesAddedEvent(project, newFiles));
                sendProjectSubscribeNotification(project, projectActivityStream.getProjectFilesAddedJson(newFiles), "fileAdded");
            }
            if (versionedFiles.size() > 0) {
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFilesVersionedEvent(project, versionedFiles));
                sendProjectSubscribeNotification(project, projectActivityStream.getProjectFilesVersionedJson(versionedFiles), "fileVersioned");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional
    public PLMFile moveProjectFileToFolder(Integer id, PLMProjectFile plmProjectFile) throws Exception {
        PLMProjectFile file = projectFileRepository.findOne(plmProjectFile.getId());
        PLMProjectFile existFile = (PLMProjectFile) Utils.cloneObject(file, PLMProjectFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getProject(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getProject() + File.separator + existFile.getId();
        }
        if (plmProjectFile.getParentFile() != null) {
            PLMProjectFile existItemFile = projectFileRepository.findByParentFileAndNameAndLatestTrue(plmProjectFile.getParentFile(), plmProjectFile.getName());
            PLMProjectFile folder = projectFileRepository.findOne(plmProjectFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmProjectFile = projectFileRepository.save(plmProjectFile);
            }
        } else {
            PLMProjectFile existItemFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(plmProjectFile.getProject(), plmProjectFile.getName());
            PLMProject project = projectRepository.findOne(plmProjectFile.getProject());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmProjectFile = projectFileRepository.save(plmProjectFile);
            }
        }
        if (plmProjectFile != null) {
            String dir = "";
            if (plmProjectFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmProjectFile.getProject(), plmProjectFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmProjectFile.getProject() + File.separator + plmProjectFile.getId();
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
            List<PLMProjectFile> oldVersionFiles = projectFileRepository.findByProjectAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getProject(), existFile.getFileNo());
            for (PLMProjectFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getProject(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getProject() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmProjectFile.getProject(), plmProjectFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmProjectFile.getParentFile());
                oldVersionFile = projectFileRepository.save(oldVersionFile);
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
        return plmProjectFile;
    }

    @Transactional(readOnly = true)
    public List<PLMProjectFile> getProjectFolderChidren(Integer folderId) {
        List<PLMProjectFile> projectFiles = projectFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        projectFiles.forEach(projectFile -> {
            projectFile.setParentObject(PLMObjectType.PROJECT);
            if (projectFile.getFileType().equals("FOLDER")) {
                projectFile.setCount(projectFileRepository.getChildrenCountByParentFileAndLatestTrue(projectFile.getId()));
                projectFile.setCount(projectFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(projectFile.getProject(), projectFile.getId()));
            }
        });
        return projectFiles;
    }

    @Transactional
    public void deleteFolder(Integer projectId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, folderId);
        List<PLMProjectFile> projectFiles = projectFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) projectFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        PLMProject project = projectRepository.findOne(projectId);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectFoldersDeletedEvent(project, file));
        sendProjectSubscribeNotification(project, file.getName(), "folderDeleted");
        projectFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            PLMProjectFile parent = projectFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = projectFileRepository.save(parent);
        }
    }

    /**
     *
     * */
    @Transactional(readOnly = true)
    public List<Person> getAllProjectMembers(Integer project) {
        List<Person> persons = new ArrayList<>();
        List<PLMProjectMember> projectMembers = projectMemberRepository.findByProject(project);
        for (PLMProjectMember projectMember : projectMembers) {
            Person person = personRepository.findOne(projectMember.getPerson());
            if ((loginRepository.findByPersonId(person.getId()) != null)) {
                person.setActive(loginRepository.findByPersonId(person.getId()).getIsActive());
            }
            persons.add(person);
        }
        return persons;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMProject> getAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DetailsCount getProjectDetails(Integer projectId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(projectFileRepository.findByProjectAndFileTypeAndLatestTrueOrderByModifiedDateDesc(projectId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(projectId));
        detailsCount.setTeam(projectMemberRepository.findByProject(projectId).size());
        PLMProjectDeliverableDto projectDeliverableDto = getProjectDeliverableAndGlossaryDeliverble(projectId);
        PLMProjectDeliverableDto projectDeliverableDto1 = getSpecificationDeliverables(projectId);
        PLMProjectDeliverableDto projectDeliverableDto2 = getRequirementDeliverables(projectId);
        PLMProject project = projectRepository.findOne(projectId);
        /*detailsCount.setDeliverables(projectDeliverableDto.getItemDeliverables().size());*/
        detailsCount.setDeliverables(projectDeliverableDto.getItemDeliverables().size() + projectDeliverableDto.getGlossaryDeliverables().size() + projectDeliverableDto1.getSpecificationDeliverables().size() + projectDeliverableDto2.getRequirementDeliverables().size());
        detailsCount.setReferenceItems(projectItemReferenceRepository.findByProject(project).size());
        detailsCount.setRequirementDocuments(projectRequirementDocumentRepository.getReqDocumentsByProjectCount(projectId));
        detailsCount.setTasks(taskRepository.getPendingTaskIdsByProjectId(projectId));
        detailsCount.setTasks(detailsCount.getTasks() + activityRepository.getPendingActivityIdsByProjectId(projectId));
        detailsCount.setFinishedTasks(taskRepository.getFinishedTaskIdsByProjectId(projectId));
        detailsCount.setFinishedTasks(detailsCount.getFinishedTasks() + activityRepository.getFinishedActivityIdsByProjectId(projectId));
        return detailsCount;
    }

    @Transactional
    public PLMWbsElement updateWbsItemSeq(Integer actualId, Integer targetId) {
        PLMWbsElement actualRow = wbsElementRepository.findOne(actualId);
        PLMWbsElement targetRow = wbsElementRepository.findOne(targetId);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(actualRow.getProject());
        if ((actualRow.getSequenceNumber() > targetRow.getSequenceNumber())) {
            for (PLMWbsElement wbsElement : wbsElements) {
                if (targetRow.getId().equals(wbsElement.getId()) || actualRow.getId().equals(wbsElement.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() < wbsElement.getSequenceNumber()) && (actualRow.getSequenceNumber() > wbsElement.getSequenceNumber())) {
                        wbsElement.setSequenceNumber(wbsElement.getSequenceNumber() + 1);
                        wbsElement = wbsElementRepository.save(wbsElement);
                    }
                }
            }
            actualRow.setSequenceNumber(targetRow.getSequenceNumber());
            actualRow = wbsElementRepository.save(actualRow);
            targetRow.setSequenceNumber(targetRow.getSequenceNumber() + 1);
            targetRow = wbsElementRepository.save(targetRow);
        } else {
            for (PLMWbsElement wbsElement : wbsElements) {
                if (targetRow.getId().equals(wbsElement.getId()) || actualRow.getId().equals(wbsElement.getId())) {
                } else {
                    if ((targetRow.getSequenceNumber() > wbsElement.getSequenceNumber()) && (actualRow.getSequenceNumber() < wbsElement.getSequenceNumber())) {
                        wbsElement.setSequenceNumber(wbsElement.getSequenceNumber() - 1);
                        wbsElement = wbsElementRepository.save(wbsElement);
                    }
                }
            }
            actualRow.setSequenceNumber(targetRow.getSequenceNumber());
            actualRow = wbsElementRepository.save(actualRow);
            targetRow.setSequenceNumber(targetRow.getSequenceNumber() - 1);
            targetRow = wbsElementRepository.save(targetRow);
        }
        return actualRow;
    }

    @Transactional
    public List<PLMProjectFile> pasteFromClipboard(Integer projectId, Integer fileId, List<PLMFile> files) {
        PLMProject plmeco = projectRepository.findOne(projectId);
        List<PLMProjectFile> fileList = new ArrayList<>();
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
                PLMProjectFile projectFile = new PLMProjectFile();
                PLMProjectFile existFile = null;
                if (fileId != 0) {
                    projectFile.setParentFile(fileId);
                    existFile = projectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProjectAndLatestTrue(file.getName(), fileId, projectId);
                } else {
                    existFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(projectId, file.getName());
                }
                if (existFile == null) {
                    projectFile.setName(file.getName());
                    projectFile.setDescription(file.getDescription());
                    projectFile.setProject(projectId);
                    projectFile.setVersion(1);
                    projectFile.setSize(file.getSize());
                    projectFile.setLatest(file.getLatest());
                    String autoNumber1 = null;
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                    projectFile.setFileNo(autoNumber1);
                    projectFile.setFileType("FILE");
                    projectFile = projectFileRepository.save(projectFile);
                    projectFile.setParentObject(PLMObjectType.PROJECT);
                    fileList.add(projectFile);
                    String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + itemFileService.getCopyFilePath(file);
                    String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + projectId;
                    File fDirectory = new File(directory);
                    if (!fDirectory.exists()) {
                        fDirectory.mkdirs();
                    }
                    String dir = "";
                    if (projectFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getParentFileSystemPath(projectId, projectFile.getId());
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + projectId + File.separator + projectFile.getId();
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
                PLMProjectFile projectFile = new PLMProjectFile();
                PLMProjectFile existFile = null;
                if (fileId != 0) {
                    projectFile.setParentFile(fileId);
                    existFile = projectFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndProjectAndLatestTrue(file.getName(), fileId, projectId);
                } else {
                    existFile = projectFileRepository.findByProjectAndNameAndParentFileIsNullAndLatestTrue(projectId, file.getName());
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
                    projectFile.setProject(projectId);
                    projectFile.setFileNo(folderNumber);
                    projectFile.setFileType("FOLDER");
                    projectFile = projectFileRepository.save(projectFile);
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + projectId;
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(projectId, projectFile.getId());
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    fileList.add(projectFile);
                    copyFolderFiles(projectId, file.getParentObject(), file.getId(), projectFile.getId());
                }
            }
        }
        return fileList;
    }

    private void copyFolderFiles(Integer project, PLMObjectType objectType, Integer file, Integer parent) {
        List<PLMFile> projectFiles = fileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(file);
        projectFiles.forEach(plmProjectFile -> {
            PLMProjectFile projectFile = new PLMProjectFile();
            projectFile.setParentFile(parent);
            projectFile.setName(plmProjectFile.getName());
            projectFile.setDescription(plmProjectFile.getDescription());
            projectFile.setProject(project);
            String folderNumber = null;
            if (plmProjectFile.getFileType().equals("FILE")) {
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                projectFile.setVersion(1);
                projectFile.setFileNo(folderNumber);
                projectFile.setSize(plmProjectFile.getSize());
                projectFile.setFileType("FILE");
                projectFile = projectFileRepository.save(projectFile);
                projectFile.setParentObject(PLMObjectType.PROJECT);
                plmProjectFile.setParentObject(objectType);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(plmProjectFile);

                String dir = "";
                if (projectFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(project, projectFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + project + File.separator + projectFile.getId();
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
                projectFile.setVersion(1);
                projectFile.setSize(0L);
                projectFile.setFileNo(folderNumber);
                projectFile.setFileType("FOLDER");
                projectFile = projectFileRepository.save(projectFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + project;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(project, projectFile.getId());
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyFolderFiles(project, objectType, plmProjectFile.getId(), projectFile.getId());
            }
        });
    }

    @Transactional
    public PLMProjectDeliverableDto pasteProjectDeliverables(Integer projectId, PLMProjectDeliverableDto deliverableDto) {
        List<Integer> itemIds = new ArrayList<>();
        List<Integer> glossaryIds = new ArrayList<>();
        List<Integer> specIds = new ArrayList<>();
        List<Integer> reqIds = new ArrayList<>();
        deliverableDto.getItemIds().forEach(itemId -> {
            PLMProjectDeliverable deliverable = projectDeliveravbleRepository.findByProjectAndItemRevision(projectId, itemId);
            if (deliverable == null) {
                deliverable = new PLMProjectDeliverable();
                deliverable.setProject(projectId);
                deliverable.setDeliverableStatus(DeliverableStatus.PENDING);
                deliverable.setItemRevision(itemId);
                deliverable = projectDeliveravbleRepository.save(deliverable);
                itemIds.add(deliverable.getId());
            }
        });
        deliverableDto.getGlossaryIds().forEach(glossaryId -> {
            PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
            PLMGlossaryDeliverable deliverable = glossaryDeliverableRepository.findByObjectIdAndGlossary(projectId, glossary);
            if (deliverable == null) {
                deliverable = new PLMGlossaryDeliverable();
                deliverable.setObjectId(projectId);
                deliverable.setObjectType("PROJECT");
                deliverable.setGlossary(glossary);
                deliverable = glossaryDeliverableRepository.save(deliverable);
                glossaryIds.add(deliverable.getId());
            }
        });
        deliverableDto.getRequirementIds().forEach(requirementId -> {
            Requirement requirement = requirementRepository.findOne(requirementId);
            RequirementDeliverable deliverable = requirementDeliverableRepository.findByObjectIdAndRequirement(projectId, requirement);
            if (deliverable == null) {
                deliverable = new RequirementDeliverable();
                deliverable.setObjectId(projectId);
                deliverable.setObjectType("PROJECT");
                deliverable.setRequirement(requirement);
                deliverable = requirementDeliverableRepository.save(deliverable);
                reqIds.add(deliverable.getId());
            }
        });
        deliverableDto.getSpecIds().forEach(specificationId -> {
            Specification specification = specificationRepository.findOne(specificationId);
            SpecificationDeliverable deliverable = specificationDeliverableRepository.findByObjectIdAndSpecification(projectId, specification);
            if (deliverable == null) {
                deliverable = new SpecificationDeliverable();
                deliverable.setObjectId(projectId);
                deliverable.setObjectType("PROJECT");
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
    public void undoProjectDeliverables(Integer projectId, PLMProjectDeliverableDto deliverableDto) {
        for (Integer itemId : deliverableDto.getItemIds()) {
            projectDeliveravbleRepository.delete(itemId);
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
    public void undoCopiedProjectFiles(Integer projectId, List<PLMProjectFile> projectFiles) {
        projectFiles.forEach(plmProjectFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(projectId, plmProjectFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(plmProjectFile.getId(), dir);
            projectFileRepository.delete(plmProjectFile.getId());
        });
    }


    @Transactional
    public PLMWorkflow attachProjectWorkflow(Integer id, Integer wfDefId, Integer workflowId) {
        PLMWorkflow workflow = null;
        PLMProject project = projectRepository.findOne(id);
        if (wfDefId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (project != null && wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.PROJECT, project.getId(), wfDef);
                project.setWorkflow(workflow.getId());
                projectRepository.save(project);
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowChangeEvent(project, null, workflow));
            }
        } else if (workflowId != null) {
            PLMWorkflow workflow1 = workflowRepository.findOne(workflowId);
            if (workflow1 != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef1 != null) {
                    workflow = workflowService.attachWorkflow(PLMObjectType.PROJECT, project.getId(), wfDef1);
                    project.setWorkflow(workflow.getId());
                    projectRepository.save(project);
                    applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowChangeEvent(project, null, workflow));
                }
            }
        }
        return workflow;
    }

    @Transactional(readOnly = true)
    public List<PLMWorkflowDefinition> getProjectWorkflows(String type) {
        List<PLMWorkflowDefinition> workflowDefinitions = new ArrayList<>();
        PLMWorkflowType workflowType = workflowTypeRepository.findByAssignable(type);
        if (workflowType != null) {
            List<PLMWorkflowDefinition> definitions = workFlowDefinitionRepository.findByWorkflowTypeAndReleasedTrue(workflowType);
            if (definitions.size() > 0) {
                definitions.forEach(workflowDefinition -> {
                    if (workflowDefinition.getMaster().getLatestReleasedRevision().equals(workflowDefinition.getId())) {
                        workflowDefinitions.add(workflowDefinition);
                    }
                });
            }
        }
        return workflowDefinitions;
    }

    @Transactional
    public PLMDeliverable finishProjectDeliverable(PLMProjectDeliverable plmProjectDeliverable) throws JsonProcessingException {
        PLMDeliverable plmDeliverable1 = deliverableRepository.findOne(plmProjectDeliverable.getId());
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmProjectDeliverable.getItemRevision());
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
        if (plmDeliverable1.getCriteria() == null) {
            plmDeliverable1.setDeliverableStatus(DeliverableStatus.FINISHED);
            plmDeliverable1 = deliverableRepository.save(plmDeliverable1);
            PLMProject project = projectRepository.findOne(plmProjectDeliverable.getProject());
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectDeliverableFinishedEvent(project, plmItem));
            sendProjectSubscribeNotification(project, plmItem.getItemNumber(), "deliverableFinished");
        } else {
            throw new CassiniException(messageSource.getMessage("cannot_finish_deliverable", null, "You cannot finish this deliverable", LocaleContextHolder.getLocale()));
        }
        return plmDeliverable1;
    }

    @Transactional(readOnly = true)
    public List<PLMProjectFile> getProjectFilesByName(Integer eco, String name) {
        return projectFileRepository.findByProjectAndNameContainingIgnoreCaseAndLatestTrue(eco, name);
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) throws JsonProcessingException {
        PLMProject project = (PLMProject) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowStartedEvent(project, event.getPlmWorkflow()));
        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowStarted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) throws JsonProcessingException {
        PLMProject project = (PLMProject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowPromotedEvent(project, plmWorkflow, fromStatus, toStatus));
        sendProjectSubscribeNotification(project, plmWorkflow.getName() + "-" + fromStatus.getName() + "-" + toStatus.getName(), "workflowPromoted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) throws JsonProcessingException {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMProject project = (PLMProject) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowDemotedEvent(project, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName() + "-" + fromStatus.getName() + "-" + event.getToStatus().getName(), "workflowDemoted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) throws JsonProcessingException {
        PLMProject project = (PLMProject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowFinishedEvent(project, plmWorkflow));
        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowFinished");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) throws JsonProcessingException {
        PLMProject project = (PLMProject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowHoldEvent(project, plmWorkflow, fromStatus));
        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowHold");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROJECT'")
    public void projectWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) throws JsonProcessingException {
        PLMProject project = (PLMProject) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectWorkflowUnholdEvent(project, plmWorkflow, fromStatus));
        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowUnHold");
    }

    public void getMinAndMaxProjectDates(PLMProject project) {
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProject(project);
        Date minDate = null;
        Date maxDate = null;
        for (PLMWbsElement wbs : wbsElements) {
            if (minDate == null) minDate = wbs.getPlannedStartDate();
            else minDate = minDate.after(wbs.getPlannedStartDate()) ? wbs.getPlannedStartDate() : minDate;
            if (maxDate == null) maxDate = wbs.getPlannedFinishDate();
            else maxDate = maxDate.before(wbs.getPlannedFinishDate()) ? wbs.getPlannedFinishDate() : maxDate;
        }
        if (minDate != null) project.setMinDate(minDate);
        if (maxDate != null) project.setMaxDate(maxDate);
    }


    @Transactional
    public List<PLMProjectRequirementDocument> createProjectReqDocuments(List<PLMProjectRequirementDocument> requirementDocuments) throws JsonProcessingException {
        requirementDocuments = projectRequirementDocumentRepository.save(requirementDocuments);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectReqDocumentsAddedEvent(requirementDocuments.get(0).getProject(), requirementDocuments));
        sendProjectSubscribeNotification(requirementDocuments.get(0).getProject(), projectActivityStream.getProjectReqDocumentsAddedJson(requirementDocuments), "requirementAdded");
        return requirementDocuments;
    }

    @Transactional
    public PLMProjectRequirementDocument createProjectReqDocument(PLMProjectRequirementDocument requirementDocument) throws JsonProcessingException {
        requirementDocument = projectRequirementDocumentRepository.save(requirementDocument);
        List<PLMProjectRequirementDocument> requirementDocuments = new ArrayList<>();
        requirementDocuments.add(requirementDocument);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectReqDocumentsAddedEvent(requirementDocument.getProject(), requirementDocuments));
        sendProjectSubscribeNotification(requirementDocuments.get(0).getProject(), projectActivityStream.getProjectReqDocumentsAddedJson(requirementDocuments), "requirementAdded");
        return requirementDocument;
    }

    @Transactional
    public PLMProjectRequirementDocument updateProjectReqDocument(Integer projectReqDocId, PLMProjectRequirementDocument requirementDocument) {
        return projectRequirementDocumentRepository.save(requirementDocument);
    }

    @Transactional(readOnly = true)
    public List<PLMProjectRequirementDocument> getProjectReqDocuments(Integer projectId) {
        return projectRequirementDocumentRepository.getReqDocumentsByProject(projectId);
    }

    @Transactional
    public void deleteProjectReqDocument(Integer projectId, Integer projectReqDocId) throws JsonProcessingException {
        PLMProjectRequirementDocument requirementDocument = projectRequirementDocumentRepository.findOne(projectReqDocId);
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectReqDocumentsDeletedEvent(requirementDocument.getProject(), requirementDocument));
        sendProjectSubscribeNotification(requirementDocument.getProject(), requirementDocument.getReqDocument().getName(), "requirementDeleted");
        projectRequirementDocumentRepository.delete(projectReqDocId);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PMObjectType> getProjectTypeTree(PMType objType) {
        List<PMObjectType> types = pmObjectTypeRepository.findByTypeAndParentIsNullOrderByIdAsc(objType);
        for (PMObjectType type : types) {
            visitProjectTypeChildren(type, objType);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PMObjectType> getProjectTypeChildren(Integer id, PMType objType) {
        return pmObjectTypeRepository.findByTypeAndParentOrderByIdAsc(objType, id);

    }

    private void visitProjectTypeChildren(PMObjectType parent, PMType objType) {
        List<PMObjectType> childrens = getProjectTypeChildren(parent.getId(), objType);
        for (PMObjectType child : childrens) {
            visitProjectTypeChildren(child, objType);
        }
        parent.setChildren(childrens);
    }

    @Transactional
    public PLMSubscribe subscribeProject(Integer itemId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMSubscribe subscribe = subscribeRepository.findByPersonAndObjectId(person, itemId);
        PLMProject project = projectRepository.findOne(itemId);
        if (subscribe == null) {
            subscribe = new PLMSubscribe();
            subscribe.setPerson(person);
            subscribe.setObjectId(itemId);
            subscribe.setObjectType(project.getObjectType().name());
            subscribe.setSubscribe(true);
            subscribe = subscribeRepository.save(subscribe);
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectSubscribeEvent(project));
        } else {
            if (subscribe.getSubscribe()) {
                subscribe.setSubscribe(false);
                subscribe = subscribeRepository.save(subscribe);
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectUnSubscribeEvent(project));
            } else {
                subscribe.setSubscribe(true);
                subscribe = subscribeRepository.save(subscribe);
                applicationEventPublisher.publishEvent(new ProjectEvents.ProjectSubscribeEvent(project));
            }
        }
        return subscribe;
    }

    public void sendProjectSubscribeNotification(PLMProject project, String messageJson, String type) throws JsonProcessingException {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(project.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") email = subscribe.getPerson().getEmail();
                else email = email + "," + subscribe.getPerson().getEmail();
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            SubscribeMailDto subscribeMailDto = getMessageAndSubject(project, messageJson, type);
            final String messageContent = subscribeMailDto.getMessage();
            final String finalMailSubject = subscribeMailDto.getMailSubject();
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(finalMailSubject);
                mail.setTemplatePath("email/subscribeNotification.html");
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Transactional
    public PLMProjectMember createProjectMemeber(Integer id, PLMProjectMember projectMember) {
        PLMProjectMember projectMember1 = projectMemberRepository.save(projectMember);
        PLMProject project = projectRepository.findOne(id);
        Person person = personRepository.findOne(projectMember1.getPerson());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMemberAddedEvent(project, person));
        return projectMember1;

    }

    @Transactional
    public PLMProjectMember updateProjectMemeber(Integer id, PLMProjectMember projectMember) {
        PLMProjectMember oldProjectMember = projectMemberRepository.findOne(projectMember.getId());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMemberUpdatedEvent(oldProjectMember, projectMember));
        return projectMemberRepository.save(projectMember);
    }

    @Transactional
    public List<PLMProjectMember> createProjectMemebers(Integer id, List<PLMProjectMember> projectMembers) {
        List<PLMProjectMember> projectMembers1 = projectMemberRepository.save(projectMembers);
        PLMProject project = projectRepository.findOne(id);
        List<Person> persons = new ArrayList<>();
        for (PLMProjectMember plmProjectMember : projectMembers) {
            Person person = personRepository.findOne(plmProjectMember.getPerson());
            persons.add(person);
        }
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMembersAddedEvent(project, persons));
        return projectMembers1;

    }

    public List<Person> getProjectManagers() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = projectRepository.getProjectManagerIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    public SubscribeMailDto getMessageAndSubject(PLMProject project, String messageJson, String type) throws JsonProcessingException {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        SubscribeMailDto subscribeMailDto = new SubscribeMailDto();
        List<String> names = new ArrayList<>();
        String message;
        String mailSubject;
        String arrayNames[];
        switch (type) {
            case "basic":
                messageJson = messageJson.replaceAll("\\[|\\]", "");
                ASPropertyChangeDTO changeDTO = objectMapper.readValue(messageJson, ASPropertyChangeDTO.class);
                message = person.getFullName().trim() + " has updated property " + changeDTO.getProperty() + " from " + changeDTO.getOldValue() + " to " + changeDTO.getNewValue() + " of project " + project.getName();
                mailSubject = project.getName() + " - Basic Information updated : Notification";
                break;
            case "teamAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewMemberDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added member(s) " + names + " of project " + project.getName();
                mailSubject = project.getName() + " - Team Member added : Notification";
                break;
            case "teamDeleted":
                message = person.getFullName() + "has deleted member " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - Team Member deleted : Notification";
                break;
            case "requirementAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASReqDocoumentDto>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added requirement document(s) " + names + " of project " + project.getName();
                mailSubject = project.getName() + " - Requirement Document added : Notification";
                break;
            case "requirementDeleted":
                message = person.getFullName() + "has deleted requirement document " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - Requirement Document deleted : Notification";
                break;
            case "deliverableAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewMemberDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added deliverable(s) " + names + " of project " + project.getName();
                mailSubject = project.getName() + " - Deliverable added : Notification";
                break;
            case "deliverableFinished":
                message = person.getFullName() + "has finished deliverable " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - Deliverable finished : Notification";
                break;
            case "deliverableDeleted":
                message = person.getFullName() + "has deleted deliverable " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - Deliverable deleted : Notification";
                break;
            case "referenceitemAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewMemberDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added reference item(s) " + names + " of project " + project.getName();
                mailSubject = project.getName() + " - Reference Item added : Notification";
                break;
            case "referenceitemDeleted":
                message = person.getFullName() + "has deleted reference item " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - Reference Item deleted : Notification";
                break;
            case "fileAdded":
                objectMapper.readValue(messageJson, new TypeReference<List<ASNewFileDTO>>() {
                }).forEach(f -> names.add(f.getName()));
                message = person.getFullName().trim() + " has added new file(s) " + names + " of project " + project.getName();
                mailSubject = project.getName() + " - File added : Notification";
                break;
            case "fileDeleted":
                message = person.getFullName() + "has deleted file " + messageJson + " of project : " + project.getName();
                mailSubject = project.getName() + " - File deleted : Notification";
                break;
            case "fileVersioned":
                message = person.getFullName() + "has updated file(s) " + messageJson + " of project : " + project.getName();
                final String[] s = {""};
                objectMapper.readValue(messageJson, new TypeReference<List<ASVersionedFileDTO>>() {
                }).forEach(f -> {
                    s[0] = s[0] + f.getName() + "from version " + f.getOldVersion() + " to " + f.getNewVersion();
                });
                message = message + s[0];
                mailSubject = project.getName() + " - File updated : Notification";
                break;
            case "fileRename":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has renamed from " + arrayNames[0] + " to " + arrayNames[1] + " of project " + project.getName();
                mailSubject = project.getName() + " - File renamed : Notification";
                break;
            case "fileReplace":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has replace from " + arrayNames[0] + " to " + arrayNames[1] + " of project " + project.getName();
                mailSubject = project.getName() + " - File replace : Notification";
                break;
            case "fileLocked":
                message = person.getFullName().trim() + " has locked file " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - File locked : Notification";
                break;
            case "fileUnLocked":
                message = person.getFullName().trim() + " has unlocked file " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - File unlocked : Notification";
                break;
            case "fileDownloaded":
                message = person.getFullName().trim() + " has downloaded file " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - File downloaded : Notification";
                break;
            case "folderAdded":
                message = person.getFullName().trim() + " has added folder " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Folder added : Notification";
                break;
            case "folderDeleted":
                message = person.getFullName().trim() + " has deleted folder " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Folder deleted : Notification";
                break;
            case "workflowStarted":
                message = person.getFullName().trim() + " has stated workflow " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow started : Notification";
                break;
            case "workflowPromoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has promoted workflow " + arrayNames[0] + " from " + arrayNames[1] + " to " + arrayNames[2] + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow promoted : Notification";
                break;
            case "workflowDemoted":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has demoted workflow " + arrayNames[0] + " from " + arrayNames[1] + " to " + arrayNames[2] + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow demoted : Notification";
                break;
            case "workflowFinished":
                message = person.getFullName().trim() + " has finished workflow " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow finished : Notification";
                break;
            case "workflowHold":
                message = person.getFullName().trim() + " has hold workflow " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow hold : Notification";
                break;
            case "workflowUnHold":
                message = person.getFullName().trim() + " has unhold workflow " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Workflow unhold : Notification";
                break;
            case "phaseAdded":
                message = person.getFullName().trim() + " has added phase(s) " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Phase added : Notification";
                break;
            case "phaseDeleted":
                message = person.getFullName().trim() + " has deleted phase " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Phase deleted : Notification";
                break;
            case "activityAdded":
                message = person.getFullName().trim() + " has added activity(s) " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Activity added : Notification";
                break;
            case "taskAdded":
                message = person.getFullName().trim() + " has added task(s) " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Task added : Notification";
                break;
            case "taskDeleted":
                message = person.getFullName().trim() + " has deleted task " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Task deleted : Notification";
                break;
            case "milestoneAdded":
                message = person.getFullName().trim() + " has added milestone(s) " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Milestone added : Notification";
                break;
            case "milestoneDeleted":
                message = person.getFullName().trim() + " has deleted milestone " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Milestone deleted : Notification";
                break;
            case "taskFinished":
                message = person.getFullName().trim() + " has finished task " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Task finished : Notification";
                break;
            case "activityFinished":
                message = person.getFullName().trim() + " has finished activity " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Activity finished : Notification";
                break;
            case "activityDeleted":
                message = person.getFullName().trim() + " has deleted activity " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Activity deleted : Notification";
                break;
            case "milestoneFinished":
                message = person.getFullName().trim() + " has finished milestone " + messageJson + " of project " + project.getName();
                mailSubject = project.getName() + " - Milestone added : Notification";
                break;
            case "taskPercentUpdated":
                arrayNames = messageJson.split("-");
                message = person.getFullName().trim() + " has updated task " + arrayNames[0] + " to " + arrayNames[1] + "% percent of project " + project.getName();
                mailSubject = project.getName() + " - Task updated : Notification";
                break;
            default:
                message = "";
                mailSubject = "";
        }
        subscribeMailDto.setMailSubject(mailSubject);
        subscribeMailDto.setMessage(message);
        return subscribeMailDto;
    }


    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<PLMProjectFile> files = projectFileRepository.findByIdIn(fileIds);
        List<PLMProjectFile> plmFiles = projectFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = projectFileRepository.getFileNosByIds(fileIds);
        List<PLMProjectFile> fileNoFiles = projectFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<PLMProjectFile> fileCountList = projectFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
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
        List<Integer> foldersList = projectFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = projectFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }


    @Transactional(readOnly = true)
    public Integer getProjectTasksAssignedToCount(Integer projectId, Integer personId) {
        Integer finishedCount = taskRepository.getTaskFinishedCountByProjectIdAndAssignedTo(projectId, personId);
        String message = null;
        if (finishedCount > 0) {
            message = messageSource.getMessage("project_finish_tasks_dates_validate", null, "{0} has {1} finished tasks. You cannot remove this project member", LocaleContextHolder.getLocale());
        } else {
            finishedCount = activityRepository.getActivityFinishedCountByProjectIdAndAssignedTo(projectId, personId);
            if (finishedCount > 0) {
                message = messageSource.getMessage("project_finish_activities_dates_validate", null, "{0} has {1} finished activities. You cannot remove this project member", LocaleContextHolder.getLocale());
            } else {
                finishedCount = milestoneRepository.getMilestoneFinishedCountByProjectIdAndAssignedTo(projectId, personId);
                if (finishedCount > 0) {
                    message = messageSource.getMessage("project_finish_milestones_dates_validate", null, "{0} has {1} finished milestones. You cannot remove this project member", LocaleContextHolder.getLocale());
                }
            }
        }
        if (finishedCount > 0) {
            Person person = personRepository.findOne(personId);
            String result = MessageFormat.format(message + ".", person.getFullName(), finishedCount);
            throw new CassiniException(result);
        }
        Integer taskCount = taskRepository.getTaskCountByProjectIdAndAssignedTo(projectId, personId);
        taskCount = taskCount + activityRepository.getActivityCountByProjectIdAndAssignedTo(projectId, personId);
        taskCount = taskCount + milestoneRepository.getMilestoneCountByProjectIdAndAssignedTo(projectId, personId);
        return taskCount;
    }

}
