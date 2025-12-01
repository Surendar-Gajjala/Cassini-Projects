package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.filtering.ProgramTemplatePredicateBuilder;
import com.cassinisys.plm.filtering.ProjectTemplateCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectDocument;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMSharedObject;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStartRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by smukka on 18-06-2022.
 */
@Service
public class ProgramTemplateService implements CrudService<ProgramTemplate, Integer> {
    @Autowired
    private ProgramTemplateRepository programTemplateRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired(required = true)
    private ProgramTemplatePredicateBuilder projectTemplatePredicateBuilder;
    @Autowired
    private ProgramTemplateResourceRepository programTemplateResourceRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ProgramResourceRepository programResourceRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private ProgramTemplateProjectRepository programTemplateProjectRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProjectTemplateMemberRepository projectTemplateMemberRepository;
    @Autowired
    private ProjectTemplateWbsRepository projectTemplateWbsRepository;
    @Autowired
    private ProjectTemplateActivityRepository projectTemplateActivityRepository;
    @Autowired
    private ProjectTemplateTaskRepository projectTemplateTaskRepository;
    @Autowired
    private ProjectTemplateMilestoneRepository projectTemplateMilestoneRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private ProgramTemplateFileRepository programTemplateFileRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProjectTemplateFileRepository projectTemplateFileRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ProjectTemplateService projectTemplateService;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ProjectTemplateTaskFileRepository projectTemplateTaskFileRepository;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStatusRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private ProjectTemplateActivityFileRepository projectTemplateActivityFileRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#programTemplate,'create')")
    public ProgramTemplate create(ProgramTemplate programTemplate) {
        Integer def = null;
        def = programTemplate.getWorkflow();
        programTemplate.setWorkflow(null);
        ProgramTemplate existTemplate = programTemplateRepository.findByNameEqualsIgnoreCase(programTemplate.getName());
        if (existTemplate != null) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", programTemplate.getName());
            throw new CassiniException(result);
        }
        ProgramTemplate template = programTemplateRepository.save(programTemplate);
        if (def != null) {
            attachWorkflow(template.getId(), null, "PROGRAMTEMPLATE", def);
        }
        if (programTemplate.getProgram() != null) {
            PLMProgram program = programRepository.findOne(programTemplate.getProgram());
            if (program.getWorkflow() != null && (programTemplate.getCopyWorkflows() && (programTemplate.getAllWorkflows() || programTemplate.getProgramWorkflows()))) {
                attachWorkflow(template.getId(), program.getWorkflow(), "PROGRAMTEMPLATE", null);
            }
            copyTemplateDetails(template.getId(), programTemplate);
        }
        return template;
    }

    private void copyTemplateDetails(Integer templateId, ProgramTemplate programTemplate) {
        if (programTemplate.getResources()) {
            List<PLMProgramResource> programResources = programResourceRepository.findByProgramOrderByIdAsc(programTemplate.getProgram());
            List<ProgramTemplateResource> templateResources = new LinkedList<>();
            for (PLMProgramResource programResource : programResources) {
                ProgramTemplateResource templateResource = new ProgramTemplateResource();
                templateResource.setTemplate(templateId);
                templateResource.setPerson(programResource.getPerson());
                templateResource.setRole(programResource.getRole());
                templateResource.setType(programResource.getType());
                templateResources.add(templateResource);
            }
            if (templateResources.size() > 0) {
                programTemplateResourceRepository.save(templateResources);
            }
        }

        if (programTemplate.getProjects() && programTemplate.getSelectedProjectIds().size() > 0) {

            List<Integer> programProjectIds = programTemplate.getSelectedProjectIds();
            List<PLMProgramProject> programProjects = programProjectRepository.findByIdIn(programProjectIds);
            Map<Integer, List<PLMProgramProject>> groupMap = new LinkedHashMap<>();
            for (PLMProgramProject programProject : programProjects) {
                List<PLMProgramProject> projects = groupMap.containsKey(programProject.getParent()) ? groupMap.get(programProject.getParent()) : new ArrayList<PLMProgramProject>();
                if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
                    projects.add(programProject);
                    groupMap.put(programProject.getParent(), projects);
                } else {
                    groupMap.put(programProject.getId(), projects);
                }
            }

            for (Integer group : groupMap.keySet()) {
                PLMProgramProject programProject = programProjectRepository.findOne(group);
                ProgramTemplateProject programTemplateProject = new ProgramTemplateProject();
                programTemplateProject.setTemplate(templateId);
                programTemplateProject.setName(programProject.getName());
                programTemplateProject.setType(programProject.getType());
                programTemplateProject.setDescription(programProject.getDescription());
                programTemplateProject = programTemplateProjectRepository.save(programTemplateProject);

                List<PLMProgramProject> programProjectList = groupMap.get(group);
                List<ProgramTemplateProject> programTemplateProjects = new LinkedList<>();
                for (PLMProgramProject child : programProjectList) {
                    ProgramTemplateProject templateProject = new ProgramTemplateProject();
                    templateProject.setTemplate(templateId);
                    if (child.getProject() != null) {
                        ProjectTemplate template = createProjectTemplate(templateId, child.getProject(), programTemplate);
                        templateProject.setProjectTemplate(template.getId());
                    }
                    templateProject.setName(child.getName());
                    templateProject.setType(child.getType());
                    templateProject.setDescription(child.getDescription());
                    templateProject.setParent(programTemplateProject.getId());
                    programTemplateProjects.add(templateProject);
                    //templateProject = programTemplateProjectRepository.save(templateProject);
                }
                if (programTemplateProjects.size() > 0) {
                    programTemplateProjectRepository.save(programTemplateProjects);
                }
            }

            /*List<PLMProgramProject> programProjects = programProjectRepository.findByProgramAndParentIsNullOrderByIdAsc(programTemplate.getProgram());
            for (PLMProgramProject programProject : programProjects) {
                ProgramTemplateProject programTemplateProject = new ProgramTemplateProject();
                programTemplateProject.setTemplate(templateId);
                if (programProject.getProject() != null) {
                    ProjectTemplate template = createProjectTemplate(templateId, programProject.getProject(), programTemplate);
                    programTemplateProject.setProjectTemplate(template.getId());
                }
                programTemplateProject.setName(programProject.getName());
                programTemplateProject.setType(programProject.getType());
                programTemplateProject.setDescription(programProject.getDescription());
                programTemplateProject.setParent(programProject.getParent());
                programTemplateProject = programTemplateProjectRepository.save(programTemplateProject);

                List<PLMProgramProject> programProjectList = programProjectRepository.findByParentOrderByIdAsc(programProject.getId());
                for (PLMProgramProject child : programProjectList) {
                    ProgramTemplateProject templateProject = new ProgramTemplateProject();
                    templateProject.setTemplate(templateId);
                    if (child.getProject() != null) {
                        ProjectTemplate template = createProjectTemplate(templateId, child.getProject(), programTemplate);
                        templateProject.setProjectTemplate(template.getId());
                    }
                    templateProject.setName(child.getName());
                    templateProject.setType(child.getType());
                    templateProject.setDescription(child.getDescription());
                    templateProject.setParent(programTemplateProject.getId());
                    templateProject = programTemplateProjectRepository.save(templateProject);
                }
            }*/
        }

        if (programTemplate.getCopyFolders() && (programTemplate.getAllFolders() || programTemplate.getProgramFolders())) {
            List<PLMProgramFile> programFiles = programFileRepository.findByProgramAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(programTemplate.getProgram(), "FOLDER");
            for (PLMProgramFile programFile : programFiles) {
                ProgramTemplateFile programTemplateFile = new ProgramTemplateFile();
                programTemplateFile.setName(programFile.getName());
                programTemplateFile.setDescription(programFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                programTemplateFile.setVersion(1);
                programTemplateFile.setSize(0L);
                programTemplateFile.setTemplate(templateId);
                programTemplateFile.setFileNo(folderNumber);
                programTemplateFile.setFileType("FOLDER");
                programTemplateFile = programTemplateFileRepository.save(programTemplateFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + templateId;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + programTemplateFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                copyProgramFileChildrenFolders(programFile.getId(), templateId, programTemplateFile.getId());
            }
        }
    }

    private void copyProgramFileChildrenFolders(Integer programFile, Integer templateId, Integer templateFileId) {
        List<PLMProgramFile> foldersList = programFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(programFile, "FOLDER");
        for (PLMProgramFile plmFile : foldersList) {
            ProgramTemplateFile programTemplateFile = new ProgramTemplateFile();
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
            programTemplateFile = programTemplateFileRepository.save(programTemplateFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + getParentFileSystemPath(templateId, programTemplateFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProgramFileChildrenFolders(plmFile.getId(), templateId, programTemplateFile.getId());
        }
    }

    private void copyProjectFileChildrenFolders(Integer programFile, Integer templateId, Integer templateFileId) {
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
            copyProjectFileChildrenFolders(plmFile.getId(), templateId, programTemplateFile.getId());
        }
    }

    private void copyProjectTaskFileChildrenFolders(Integer taskFile, Integer templateTaskId, Integer templateTaskFileId) {
        List<PLMTaskFile> foldersList = taskFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(taskFile, "FOLDER");
        for (PLMTaskFile plmFile : foldersList) {
            ProjectTemplateTaskFile templateTaskFile = new ProjectTemplateTaskFile();
            templateTaskFile.setName(plmFile.getName());
            templateTaskFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            templateTaskFile.setVersion(1);
            templateTaskFile.setSize(0L);
            templateTaskFile.setParentFile(templateTaskFileId);
            templateTaskFile.setTask(templateTaskId);
            templateTaskFile.setFileNo(folderNumber);
            templateTaskFile.setFileType("FOLDER");
            templateTaskFile = projectTemplateTaskFileRepository.save(templateTaskFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentTaskTemplateFileSystemPath(templateTaskId, templateTaskFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectTaskFileChildrenFolders(plmFile.getId(), templateTaskId, templateTaskFile.getId());
        }
    }

    private ProjectTemplate createProjectTemplate(Integer program, Integer projectId, ProgramTemplate programTemplate) {
        PLMProject project = projectRepository.findOne(projectId);
        ProjectTemplate projectTemplate = new ProjectTemplate();
        projectTemplate.setName(project.getName());
        projectTemplate.setDescription(project.getDescription());
        projectTemplate.setProgramTemplate(program);
        projectTemplate.setManager(project.getProjectManager());
        projectTemplate = projectTemplateRepository.save(projectTemplate);
        if (project.getWorkflow() != null && (programTemplate.getCopyWorkflows() && (programTemplate.getAllWorkflows() || programTemplate.getProjectWorkflows()))) {
            attachWorkflow(projectTemplate.getId(), project.getWorkflow(), "PROJECTTEMPLATE", null);
        }
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project);
        if (programTemplate.getTeam()) {
            List<PLMProjectMember> projectMembers = projectMemberRepository.findByProject(project.getId());
            List<ProjectTemplateMember> templateMember = new LinkedList<>();
            for (PLMProjectMember projectMember : projectMembers) {
                ProjectTemplateMember projectTemplateMember = new ProjectTemplateMember();
                projectTemplateMember.setTemplate(projectTemplate.getId());
                projectTemplateMember.setPerson(projectMember.getPerson());
                projectTemplateMember.setRole(projectMember.getRole());
                templateMember.add(projectTemplateMember);
                //projectTemplateMember = projectTemplateMemberRepository.save(projectTemplateMember);
            }
            if (templateMember.size() > 0) {
                projectTemplateMemberRepository.save(templateMember);
            }
        }
        if (wbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : wbsElements) {
                ProjectTemplateWbs templateWbs = new ProjectTemplateWbs();
                templateWbs.setName(wbsElement.getName());
                templateWbs.setSequenceNumber(wbsElement.getSequenceNumber());
                templateWbs.setDescription(wbsElement.getDescription());
                templateWbs.setTemplate(projectTemplate.getId());
                templateWbs = projectTemplateWbsRepository.save(templateWbs);
                List<PLMActivity> activities = activityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (activities.size() != 0) {
                    for (PLMActivity activity : activities) {
                        ProjectTemplateActivity templateActivity = new ProjectTemplateActivity();
                        templateActivity.setName(activity.getName());
                        templateActivity.setDescription(activity.getDescription());
                        templateActivity.setSequenceNumber(activity.getSequenceNumber());
                        templateActivity.setWbs(templateWbs.getId());
                        if (programTemplate.getAssignedTo()) {
                            templateActivity.setAssignedTo(activity.getAssignedTo());
                        }
                        templateActivity = projectTemplateActivityRepository.save(templateActivity);
                        projectTemplateActivityFiles(activity, templateActivity, programTemplate);
                        if (activity.getWorkflow() != null && (programTemplate.getCopyWorkflows() && (programTemplate.getAllWorkflows() || programTemplate.getActivityWorkflows()))) {
                            attachWorkflow(templateActivity.getId(), activity.getWorkflow(), "PROJECTACTIVITYTEMPLATE", null);
                        }
                        createProjectActivityTasks(activity, templateActivity, programTemplate);
                    }
                }
                List<PLMMilestone> plmMilestones = milestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
                if (plmMilestones.size() != 0) {
                    List<ProjectTemplateMilestone> projectTemplateMilestones = new LinkedList<>();
                    for (PLMMilestone plmMilestone : plmMilestones) {
                        ProjectTemplateMilestone templateMilestone = new ProjectTemplateMilestone();
                        templateMilestone.setName(plmMilestone.getName());
                        templateMilestone.setSequenceNumber(plmMilestone.getSequenceNumber());
                        templateMilestone.setDescription(plmMilestone.getDescription());
                        templateMilestone.setWbs(templateWbs.getId());
                        if (programTemplate.getAssignedTo()) {
                            templateMilestone.setAssignedTo(plmMilestone.getAssignedTo());
                        }
                        projectTemplateMilestones.add(templateMilestone);
                        //templateMilestone = projectTemplateMilestoneRepository.save(templateMilestone);
                    }
                    if (projectTemplateMilestones.size() > 0) {
                        projectTemplateMilestoneRepository.save(projectTemplateMilestones);
                    }
                }
            }
        }

        if (programTemplate.getCopyFolders() && (programTemplate.getAllFolders() || programTemplate.getProjectFolders())) {
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
                projectTemplateFile.setTemplate(projectTemplate.getId());
                projectTemplateFile.setFileNo(folderNumber);
                projectTemplateFile.setFileType("FOLDER");
                projectTemplateFile = projectTemplateFileRepository.save(projectTemplateFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + projectTemplate.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + projectTemplateFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyProjectFileChildrenFolders(projectFile.getId(), projectTemplate.getId(), projectTemplateFile.getId());
            }
        }

        return projectTemplate;
    }

    private void projectTemplateActivityFiles(PLMActivity activity, ProjectTemplateActivity projectTemplateActivity, ProgramTemplate template) {
        if (template.getCopyFolders() && (template.getAllFolders() || template.getActivityFolders())) {
            List<PLMActivityFile> activityFiles = activityFileRepository.findByActivityAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(activity.getId(), "FOLDER");
            for (PLMActivityFile activityFile : activityFiles) {
                ProjectTemplateActivityFile templateActivityFile = new ProjectTemplateActivityFile();
                templateActivityFile.setName(activityFile.getName());
                templateActivityFile.setDescription(activityFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                templateActivityFile.setVersion(1);
                templateActivityFile.setSize(0L);
                templateActivityFile.setActivity(projectTemplateActivity.getId());
                templateActivityFile.setFileNo(folderNumber);
                templateActivityFile.setFileType("FOLDER");
                templateActivityFile = projectTemplateActivityFileRepository.save(templateActivityFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + template.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + templateActivityFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                copyProjectActivityFileChildrenFolders(activityFile.getId(), projectTemplateActivity.getId(), templateActivityFile.getId());
            }
        }
    }


    private void copyProjectActivityFileChildrenFolders(Integer activityFile, Integer templateActivityId, Integer templateActivityFileId) {
        List<PLMActivityFile> foldersList = activityFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(activityFile, "FOLDER");
        for (PLMActivityFile plmFile : foldersList) {
            ProjectTemplateActivityFile templateActivityFile = new ProjectTemplateActivityFile();
            templateActivityFile.setName(plmFile.getName());
            templateActivityFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            templateActivityFile.setVersion(1);
            templateActivityFile.setSize(0L);
            templateActivityFile.setParentFile(templateActivityFileId);
            templateActivityFile.setActivity(templateActivityId);
            templateActivityFile.setFileNo(folderNumber);
            templateActivityFile.setFileType("FOLDER");
            templateActivityFile = projectTemplateActivityFileRepository.save(templateActivityFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + projectTemplateService.getParentActivityTemplateFileSystemPath(templateActivityId, templateActivityFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProjectActivityFileChildrenFolders(plmFile.getId(), templateActivityId, templateActivityFile.getId());
        }
    }


    @Transactional
    public void createProjectActivityTasks(PLMActivity activity, ProjectTemplateActivity projectTemplateActivity, ProgramTemplate template) {
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
                    if (task.getWorkflow() != null && (template.getCopyWorkflows() && (template.getAllWorkflows() || template.getTaskWorkflows()))) {
                        attachWorkflow(templateTask2.getId(), task.getWorkflow(), "PROJECTTASKTEMPLATE", null);
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
                            copyProjectTaskFileChildrenFolders(taskFile.getId(), templateTask2.getId(), templateTaskFile.getId());
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#programTemplate.id ,'edit')")
    public ProgramTemplate update(ProgramTemplate programTemplate) {
        ProgramTemplate existTemplate = programTemplateRepository.findByNameEqualsIgnoreCase(programTemplate.getName());
        if (existTemplate != null && !existTemplate.getId().equals(programTemplate.getId())) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", programTemplate.getName());
            throw new CassiniException(result);
        }
        programTemplate = programTemplateRepository.save(programTemplate);
        return programTemplate;

    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        programTemplateRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProgramTemplate get(Integer id) {
        ProgramTemplate programTemplate = programTemplateRepository.findOne(id);
        PLMWorkflow workflow = workflowRepository.findByAttachedTo(programTemplate.getId());
        if (workflow != null) {
            PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
            if (status != null) {
                programTemplate.setWorkflowStatus(status.getName());
            }
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    programTemplate.setStartWorkflow(true);
                }
            }
            if (workflow.getFinish() != null) {
                PLMWorkflowStatus workflowStatus1 = plmWorkflowStatusRepository.findOne(workflow.getFinish().getId());
                if (workflowStatus1 != null && workflowStatus1.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    programTemplate.setFinishWorkflow(true);
                }
            }
        }
        return programTemplate;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProgramTemplate getProgramTemplateByName(String templateName) {
        ProgramTemplate programTemplate = programTemplateRepository.findByNameEqualsIgnoreCase(templateName);
        return programTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProgramTemplate> getAll() {
        return programTemplateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ProgramTemplate> getProgramTemplates(Pageable pageable, ProjectTemplateCriteria criteria) {
        Predicate predicate = projectTemplatePredicateBuilder.build(criteria, QProgramTemplate.programTemplate);
        Page<ProgramTemplate> projectTemplates = programTemplateRepository.findAll(predicate, pageable);
        return projectTemplates;
    }

    @Transactional(readOnly = true)
    public List<ProgramTemplateResource> getProgramTemplateResources(Integer templateId) {
        List<ProgramTemplateResource> projectTemplateMembers = programTemplateResourceRepository.findByTemplate(templateId);
        return projectTemplateMembers;

    }

    @Transactional
    public void deleteProjectTemplateResource(Integer template, Integer resourceId) {
        programTemplateResourceRepository.delete(resourceId);
    }

    @Transactional
    public List<ProgramTemplateResource> createProjectTemplateResources(Integer templateId, List<ProgramTemplateResource> templateResources) {
        return programTemplateResourceRepository.save(templateResources);
    }

    @Transactional
    public ProgramTemplateResource createProjectTemplateResource(Integer templateId, ProgramTemplateResource templateResource) {
        return programTemplateResourceRepository.save(templateResource);
    }

    @Transactional
    public ProgramTemplateResource updateProjectTemplateResource(Integer templateId, ProgramTemplateResource templateResource) {
        return programTemplateResourceRepository.save(templateResource);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getProgramTemplateDetails(Integer templateId) {
        ItemDetailsDto detailsCount = new ItemDetailsDto();
        detailsCount.setResourcesCount(programTemplateResourceRepository.getResourceCountByTemplate(templateId));
        detailsCount.setProjectCount(programTemplateProjectRepository.getTemplateProjectsCount(templateId));
        return detailsCount;
    }

    @Transactional
    public List<ProgramTemplateProjectDto> getProgramTemplateProjects(Integer programId) {
        List<ProgramTemplateProjectDto> programProjectDtos = new LinkedList<>();
        List<ProgramTemplateProject> programProjects = programTemplateProjectRepository.findByTemplateAndParentIsNullOrderByIdAsc(programId);
        programProjects.forEach(programProject -> {
            programProjectDtos.add(convertProgramProjectToDto(programProject));
        });

        return programProjectDtos;
    }

    private ProgramTemplateProjectDto convertProgramProjectToDto(ProgramTemplateProject programProject) {
        ProgramTemplateProjectDto programProjectDto = new ProgramTemplateProjectDto();
        programProjectDto.setId(programProject.getId());
        programProjectDto.setProjectTemplate(programProject.getProjectTemplate());
        programProjectDto.setTemplate(programProject.getTemplate());
        programProjectDto.setParent(programProject.getParent());
        if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
            ProjectTemplate project = projectTemplateRepository.findOne(programProject.getProjectTemplate());
            programProjectDto.setName(project.getName());
            programProjectDto.setDescription(project.getDescription());
            if (project.getManager() != null) {
                programProjectDto.setManagerName(personRepository.findOne(project.getManager()).getFullName());
            }
            programProjectDto.setCreatedByName(personRepository.findOne(project.getCreatedBy()).getFullName());
            programProjectDto.setModifiedByName(personRepository.findOne(project.getModifiedBy()).getFullName());
            programProjectDto.setCreatedDate(project.getCreatedDate());
            programProjectDto.setModifiedDate(project.getModifiedDate());
        } else {
            programProjectDto.setName(programProject.getName());
            programProjectDto.setDescription(programProject.getDescription());
        }
        List<ProgramTemplateProject> programProjects = programTemplateProjectRepository.findByParentOrderByIdAsc(programProject.getId());
        programProjects.forEach(child -> {
            programProjectDto.getChildren().add(convertProgramProjectToDto(child));
        });
        programProjectDto.setType(programProject.getType());
        return programProjectDto;
    }


    @Transactional
    public ProgramTemplateProjectDto createProgramTemplateProject(Integer id, ProgramTemplateProject programProject) {
        ProgramTemplateProject existProgram = null;
        if (programProject.getType().equals(ProgramProjectType.GROUP)) {
            existProgram = programTemplateProjectRepository.findByTemplateAndTypeAndNameEqualsIgnoreCase(programProject.getTemplate(), programProject.getType(), programProject.getName());
            if (existProgram != null) {
                String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programProject.getName());
                throw new CassiniException(result);
            }
        } else {
            if (programProject.getParent() != null) {
                existProgram = programTemplateProjectRepository.findByParentAndProjectTemplate(programProject.getParent(), programProject.getProjectTemplate());
                if (existProgram != null) {
                    ProjectTemplate project = projectTemplateRepository.findOne(programProject.getProjectTemplate());
                    String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", project.getName());
                    throw new CassiniException(result);
                }
            }
        }
        programProject = programTemplateProjectRepository.save(programProject);
        return convertProgramProjectToDto(programProject);
    }

    @Transactional
    public ProgramTemplateProjectDto updateProgramTemplateProject(Integer id, ProgramTemplateProject programProject) {
        ProgramTemplateProject existProgram = null;
        if (programProject.getType().equals(ProgramProjectType.GROUP)) {
            existProgram = programTemplateProjectRepository.findByTemplateAndTypeAndNameEqualsIgnoreCase(programProject.getTemplate(), programProject.getType(), programProject.getName());
            if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programProject.getName());
                throw new CassiniException(result);
            }
        } else {
            if (programProject.getParent() != null) {
                existProgram = programTemplateProjectRepository.findByParentAndProjectTemplate(programProject.getParent(), programProject.getProjectTemplate());
                if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                    ProjectTemplate project = projectTemplateRepository.findOne(programProject.getProjectTemplate());
                    String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", project.getName());
                    throw new CassiniException(result);
                }
            }
        }
        programProject = programTemplateProjectRepository.save(programProject);
        return convertProgramProjectToDto(programProject);
    }

    @Transactional
    public void deleteProgramTemplateProject(Integer id, Integer programProject) {
        ProgramTemplateProject plmProgramProject = programTemplateProjectRepository.findOne(programProject);
        if (plmProgramProject.getType().equals(ProgramProjectType.PROJECT)) {
            programTemplateProjectRepository.delete(programProject);
            projectTemplateRepository.delete(plmProgramProject.getProjectTemplate());
        } else {
            List<ProgramTemplateProject> programProjects = programTemplateProjectRepository.findByParentOrderByIdAsc(plmProgramProject.getId());
            programTemplateProjectRepository.delete(programProject);
            for (ProgramTemplateProject programProject1 : programProjects) {
                projectTemplateRepository.delete(programProject1.getProjectTemplate());
            }
        }
    }

    @Transactional
    public ProgramTemplateProjectDto getProgramTemplateProject(Integer id, Integer programProjectId) {
        ProgramTemplateProject programProject = programTemplateProjectRepository.findOne(programProjectId);
        return convertProgramProjectToDto(programProject);
    }


    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        ProgramTemplateFile templateFile = programTemplateFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (templateFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, templateFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + templateFile.getId();
        }
        return path;
    }

    @Transactional
    public void deleteFolder(Integer projectId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, folderId);
        List<ProgramTemplateFile> templateFiles = programTemplateFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) templateFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        programTemplateFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            ProgramTemplateFile parent = programTemplateFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = programTemplateFileRepository.save(parent);
        }
    }

    @Transactional
    public ProgramTemplateFile createProgramTemplateFolder(Integer templateId, ProgramTemplateFile plmProjectFile) throws JsonProcessingException {
        plmProjectFile.setId(null);
        String folderNumber = null;
        ProgramTemplateFile existFolderName = null;
        if (plmProjectFile.getParentFile() != null) {
            existFolderName = programTemplateFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndTemplateAndLatestTrue(plmProjectFile.getName(), plmProjectFile.getParentFile(), templateId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmProjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = programTemplateFileRepository.findByNameEqualsIgnoreCaseAndTemplateAndLatestTrueAndParentFileIsNull(plmProjectFile.getName(), templateId);
            if (existFolderName != null) {
                String message = messageSource.getMessage("folder_already_exist", null, "{0} folder already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName());
                throw new CassiniException(result);
            }
        }
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmProjectFile.setTemplate(templateId);
        plmProjectFile.setFileNo(folderNumber);
        plmProjectFile.setFileType("FOLDER");
        plmProjectFile = programTemplateFileRepository.save(plmProjectFile);
        if (plmProjectFile.getParentFile() != null) {
            ProgramTemplateFile parent = programTemplateFileRepository.findOne(plmProjectFile.getParentFile());
            parent.setModifiedDate(plmProjectFile.getModifiedDate());
            parent = programTemplateFileRepository.save(parent);
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
    public PLMWorkflow attachWorkflow(Integer id, Integer workflowId, String type, Integer workflowDef) {
        PLMWorkflowDefinition wfDef = null;
        if (workflowId != null) {
            PLMWorkflow workflow = workflowRepository.findOne(workflowId);
            if (workflow != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow.getWorkflowRevision());
                wfDef = workflowDefinitionService.get(workflowDefinition.getId());
            }
        }
        if (workflowDef != null) {
            wfDef = workflowDefinitionService.get(workflowDef);
        }

        if (type.equals("PROGRAMTEMPLATE")) {
            ProgramTemplate programTemplate = programTemplateRepository.findOne(id);
            if (programTemplate.getWorkflow() != null) {
                PLMWorkflow workflow1 = workflowRepository.findByAttachedTo(programTemplate.getId());
                if (workflow1 != null) {
                    programTemplate.setWorkflow(null);
                    programTemplateRepository.save(programTemplate);
                    workflowService.deleteWorkflow(id);
                }
            }
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(PLMObjectType.PROGRAMTEMPLATE, programTemplate.getId(), wfDef);
                programTemplate.setWorkflow(workflow1.getId());
                programTemplateRepository.save(programTemplate);
            }
        } else if (type.equals("PROJECTTEMPLATE")) {
            ProjectTemplate projectTemplate = projectTemplateRepository.findOne(id);
            if (projectTemplate.getWorkflow() != null) {
                PLMWorkflow workflow1 = workflowRepository.findByAttachedTo(projectTemplate.getId());
                if (workflow1 != null) {
                    projectTemplate.setWorkflow(null);
                    projectTemplateRepository.save(projectTemplate);
                    workflowService.deleteWorkflow(id);
                }
            }
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(PLMObjectType.TEMPLATE, projectTemplate.getId(), wfDef);
                projectTemplate.setWorkflow(workflow1.getId());
                projectTemplateRepository.save(projectTemplate);
            }
        } else if (type.equals("PROJECTACTIVITYTEMPLATE")) {
            ProjectTemplateActivity activity = projectTemplateActivityRepository.findOne(id);
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(PLMObjectType.TEMPLATEACTIVITY, activity.getId(), wfDef);
                activity.setWorkflow(workflow1.getId());
                projectTemplateActivityRepository.save(activity);
            }
        } else if (type.equals("PROJECTTASKTEMPLATE")) {
            ProjectTemplateTask templateTask = projectTemplateTaskRepository.findOne(id);
            if (wfDef != null) {
                PLMWorkflow workflow1 = workflowService.attachWorkflow(PLMObjectType.TEMPLATETASK, templateTask.getId(), wfDef);
                templateTask.setWorkflow(workflow1.getId());
                projectTemplateTaskRepository.save(templateTask);
            }
        }
        return null;
    }


    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<ProgramTemplateFile> files = programTemplateFileRepository.findByIdIn(fileIds);
        List<ProgramTemplateFile> plmFiles = programTemplateFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = programTemplateFileRepository.getFileNosByIds(fileIds);
        List<ProgramTemplateFile> fileNoFiles = programTemplateFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<ProgramTemplateFile> fileCountList = programTemplateFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
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
        List<Integer> foldersList = programTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = programTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

}
