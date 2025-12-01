package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.PMListDto;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ProgramTypeService;
import com.cassinisys.platform.service.core.ProgramTypeSystem;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.plm.event.ProgramEvents;
import com.cassinisys.plm.filtering.ProgramPredicateBuilder;
import com.cassinisys.plm.filtering.ProjectCriteria;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.WorkflowStatusDTO;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.ProjectTemplateRepository;
import com.cassinisys.plm.repo.plm.SharedObjectRepository;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStatusRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.wf.PLMWorkflowDefinitionService;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by smukka on 02-06-2022.
 */
@Service
public class ProgramService implements CrudService<PLMProgram, Integer>, ProgramTypeSystem {

    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private ProgramProjectRepository programProjectRepository;
    @Autowired
    private ProgramResourceRepository programResourceRepository;
    @Autowired
    private org.springframework.context.MessageSource messageSource;
    @Autowired
    private ProgramPredicateBuilder programPredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PLMWorkflowRepository plmWorkflowRepository;
    @Autowired
    private PLMWorkflowStatusRepository plmWorkflowStatusRepository;
    @Autowired
    private PLMWorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private PLMWorkflowService workflowService;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ProgramTemplateResourceRepository programTemplateResourceRepository;
    @Autowired
    private ProgramTemplateProjectRepository programTemplateProjectRepository;
    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;
    @Autowired
    private PMObjectTypeRepository pmObjectTypeRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private ProgramTemplateFileRepository programTemplateFileRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ProgramTemplateRepository programTemplateRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProgramTypeService programTypeService;


    @PostConstruct
    public void InitProgramObjectService() {
        programTypeService.registerTypeSystem("ProgramServiceObject", new ProgramService());
    }


    @Override
    @Transactional
    public PLMProgram create(PLMProgram plmProgram) {
        PLMProgram existProgram = programRepository.findByNameEqualsIgnoreCase(plmProgram.getName());
        Integer template = plmProgram.getProgramTemplate();
        ProgramTemplate programTemplate = null;
        if (template != null) {
            programTemplate = programTemplateRepository.findOne(template);
        }
        Integer workflowDef = null;
        if (plmProgram.getWorkflowDefId() != null) {
            workflowDef = plmProgram.getWorkflowDefId();
        }
        if (existProgram != null) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", plmProgram.getName());
            throw new CassiniException(result);
        }
        PLMProgram program = programRepository.save(plmProgram);
        if (workflowDef != null) {
            attachProgramWorkflow(program.getId(), workflowDef, null);
        } else if ((programTemplate != null && programTemplate.getWorkflow() != null) && (plmProgram.getCopyWorkflows() && (plmProgram.getAllWorkflows() || plmProgram.getProgramWorkflows()))) {
            attachProgramWorkflow(program.getId(), null, programTemplate.getWorkflow());
        }
        if (template != null) {
            copyTemplateData(program.getId(), template, plmProgram);
        }
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramCreatedEvent(program));
        return program;
    }

    private void copyTemplateData(Integer programId, Integer programTemplateId, PLMProgram plmProgram) {
        if (plmProgram.getResources()) {
            List<ProgramTemplateResource> templateResources = programTemplateResourceRepository.findByTemplate(programTemplateId);
            List<PLMProgramResource> programResources = new LinkedList<>();
            templateResources.forEach(programTemplateResource -> {
                PLMProgramResource programResource = new PLMProgramResource();
                programResource.setPerson(programTemplateResource.getPerson());
                programResource.setProgram(programId);
                programResource.setType(ProgramResourceType.PERSON);
                programResource.setRole(programTemplateResource.getRole());
                programResources.add(programResource);
            });

            if (programResources.size() > 0) {
                programResourceRepository.save(programResources);
            }
        }
        PMObjectType projectType = pmObjectTypeRepository.findByNameAndParentIsNullAndType("Project", PMType.PROJECT);
        if (projectType == null) {
            List<PMObjectType> objectTypes = pmObjectTypeRepository.findByTypeAndParentIsNullOrderByIdAsc(PMType.PROJECT);
            if (objectTypes.size() > 0) {
                projectType = objectTypes.get(0);
            }
        }
        List<ProgramTemplateProject> programTemplateProjects = programTemplateProjectRepository.findByTemplateAndParentIsNullOrderByIdAsc(programTemplateId);
        for (ProgramTemplateProject programTemplateProject : programTemplateProjects) {
            PLMProgramProject programProject = new PLMProgramProject();
            programProject.setName(programTemplateProject.getName());
            programProject.setDescription(programTemplateProject.getDescription());
            programProject.setProgram(programId);
            programProject.setType(programTemplateProject.getType());
            if (programTemplateProject.getType().equals(ProgramProjectType.PROJECT)) {
                ProjectTemplate projectTemplate = projectTemplateRepository.findOne(programTemplateProject.getProjectTemplate());
                PLMProject project = new PLMProject();
                project.setName(projectTemplate.getName());
                project.setDescription(projectTemplate.getDescription());
                project.setProgram(programId);
                project.setTeam(plmProgram.getTeam());
                if (projectType != null) {
                    project.setType(projectType);
                }
                project.setAssignedTo(plmProgram.getAssignedTo());
                project.setCopyFolders(plmProgram.getCopyFolders());
                project.setAllFolders(plmProgram.getAllFolders());
                project.setProjectFolders(plmProgram.getProjectFolders());
                project.setTaskFolders(plmProgram.getTaskFolders());
                project.setCopyWorkflows(plmProgram.getCopyWorkflows());
                project.setAllWorkflows(plmProgram.getAllWorkflows());
                project.setProjectWorkflows(plmProgram.getProjectWorkflows());
                project.setTaskWorkflows(plmProgram.getTaskWorkflows());
                if (projectTemplate.getManager() != null) {
                    project.setProjectManager(projectTemplate.getManager());
                }
                project = projectService.createTemplateProject(project, projectTemplate.getId());
                programProject.setProject(project.getId());
            }
            programProject = programProjectRepository.save(programProject);

            List<ProgramTemplateProject> children = programTemplateProjectRepository.findByParentOrderByIdAsc(programTemplateProject.getId());
            for (ProgramTemplateProject child : children) {
                PLMProgramProject plmProgramProject = new PLMProgramProject();
                plmProgramProject.setName(child.getName());
                plmProgramProject.setDescription(child.getDescription());
                plmProgramProject.setProgram(programId);
                plmProgramProject.setType(child.getType());
                if (child.getType().equals(ProgramProjectType.PROJECT)) {
                    ProjectTemplate projectTemplate = projectTemplateRepository.findOne(child.getProjectTemplate());
                    PLMProject project = new PLMProject();
                    project.setName(projectTemplate.getName());
                    project.setDescription(projectTemplate.getDescription());
                    project.setProgram(programId);
                    project.setTeam(plmProgram.getTeam());
                    project.setAssignedTo(plmProgram.getAssignedTo());
                    project.setCopyFolders(plmProgram.getCopyFolders());
                    project.setAllFolders(plmProgram.getAllFolders());
                    project.setProjectFolders(plmProgram.getProjectFolders());
                    project.setTaskFolders(plmProgram.getTaskFolders());
                    project.setCopyWorkflows(plmProgram.getCopyWorkflows());
                    project.setAllWorkflows(plmProgram.getAllWorkflows());
                    project.setProjectWorkflows(plmProgram.getProjectWorkflows());
                    project.setTaskWorkflows(plmProgram.getTaskWorkflows());
                    if (projectType != null) {
                        project.setType(projectType);
                    }
                    if (projectTemplate.getManager() != null) {
                        project.setProjectManager(projectTemplate.getManager());
                    }
                    project = projectService.createTemplateProject(project, projectTemplate.getId());
                    plmProgramProject.setProject(project.getId());
                }
                plmProgramProject.setParent(programProject.getId());
                plmProgramProject = programProjectRepository.save(plmProgramProject);
            }
        }

        if (plmProgram.getCopyFolders() && (plmProgram.getAllFolders() || plmProgram.getProgramFolders())) {
            List<ProgramTemplateFile> programTemplateFiles = programTemplateFileRepository.findByTemplateAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(programTemplateId, "FOLDER");
            for (ProgramTemplateFile templateFile : programTemplateFiles) {
                PLMProgramFile programFile = new PLMProgramFile();
                programFile.setName(templateFile.getName());
                programFile.setDescription(templateFile.getDescription());
                String folderNumber = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                if (autoNumber != null) {
                    folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                }
                programFile.setVersion(1);
                programFile.setSize(0L);
                programFile.setProgram(programId);
                programFile.setFileNo(folderNumber);
                programFile.setFileType("FOLDER");
                programFile = programFileRepository.save(programFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + programId;
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + programFile.getId();
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                copyProgramFileChildrenFolders(templateFile.getId(), programId, programFile.getId());
            }
        }
    }


    private void copyProgramFileChildrenFolders(Integer templateFile, Integer program, Integer programFileId) {
        List<ProgramTemplateFile> foldersList = programTemplateFileRepository.findByParentFileAndLatestTrueAndFileTypeOrderByCreatedDateDesc(templateFile, "FOLDER");
        for (ProgramTemplateFile plmFile : foldersList) {
            PLMProgramFile programFile = new PLMProgramFile();
            programFile.setName(plmFile.getName());
            programFile.setDescription(plmFile.getDescription());
            String folderNumber = null;
            AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
            if (autoNumber != null) {
                folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
            }
            programFile.setVersion(1);
            programFile.setSize(0L);
            programFile.setParentFile(programFileId);
            programFile.setProgram(program);
            programFile.setFileNo(folderNumber);
            programFile.setFileType("FOLDER");
            programFile = programFileRepository.save(programFile);

            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem";
            dir = dir + getParentFileSystemPath(program, programFile.getId());
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }
            copyProgramFileChildrenFolders(plmFile.getId(), program, programFile.getId());
        }
    }


    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMProgramFile templateFile = programFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (templateFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, templateFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + templateFile.getId();
        }
        return path;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmProgram.id ,'edit')")
    public PLMProgram update(PLMProgram plmProgram) {
        PLMProgram existProgram = programRepository.findByNameEqualsIgnoreCase(plmProgram.getName());
        PLMProgram oldProgram = JsonUtils.cloneEntity(programRepository.findOne(plmProgram.getId()), PLMProgram.class);
        if (existProgram != null && !existProgram.getId().equals(plmProgram.getId())) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", plmProgram.getName());
            throw new CassiniException(result);
        }
        plmProgram = programRepository.save(plmProgram);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramBasicInfoUpdatedEvent(oldProgram, plmProgram));
        return plmProgram;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        applicationEventPublisher.publishEvent(new WorkflowEvents.ProgramProjectActivityTaskWorkflowDeletedEvent(id));
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        programRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMProgram get(Integer id) {
        PLMProgram program = programRepository.findOne(id);
        PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(program.getId());
         //Adding workflow relavent settings
         WorkflowStatusDTO workFlowStatusDto=workflowService.setWorkflowStatusSettings(program.getId());
         program.setStartWorkflow(workFlowStatusDto.getStartWorkflow());
         program.setWorkflowSettingStatus(workFlowStatusDto.getWorkflowSettingStatus());
         program.setWorkflowSettingStatusType(workFlowStatusDto.getWorkflowSettingStatusType());

        Double completedCount = 0.0;
        List<Integer> projectIds = programProjectRepository.getProgramProjectIds(program.getId());
        for (Integer projectId : projectIds) {
            Double percent = projectService.getProjectPercentComplete(projectId).getPercentComplete();
            if (percent == 100) {
                completedCount += 1;
            }
        }
        if (projectIds.size() > 0) {
            program.setPercent(completedCount.intValue() + "/" + projectIds.size());
            Double percent = completedCount / projectIds.size();
            program.setPercentComplete(percent * 100);
        }

        PLMProgramResource resource = programResourceRepository.findByProgramAndPerson(id, sessionWrapper.getSession().getLogin().getPerson().getId());
        if (resource != null) {
            program.setProgramResource(true);
        }
        return program;

    }

    @Override
    public PMListDto getPMToList(Integer integer) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        projectRepository = webApplicationContext.getBean(ProjectRepository.class);
        programResourceRepository = webApplicationContext.getBean(ProgramResourceRepository.class);
        projectMemberRepository = webApplicationContext.getBean(ProjectMemberRepository.class);
        programRepository = webApplicationContext.getBean(ProgramRepository.class);
        taskRepository = webApplicationContext.getBean(TaskRepository.class);
        activityRepository = webApplicationContext.getBean(PLMActivityRepository.class);
        wbsElementRepository = webApplicationContext.getBean(WbsElementRepository.class);
        projectFileRepository = webApplicationContext.getBean(ProjectFileRepository.class);
        programProjectRepository = webApplicationContext.getBean(ProgramProjectRepository.class);

        List<Object> projectTasks = new ArrayList<>(taskRepository.getAssignToTasks(integer));
        List<Object> projectActivities = new ArrayList<>(activityRepository.getAssignToActivities(integer));

        List<Object> plmProjects = new ArrayList<>(projectRepository.findByProjectManager(integer));
        List<Object> projectFiles = new ArrayList<>();

        plmProjects.forEach(project -> {
            PLMProject plmProject = (PLMProject)project;
            List<PLMProjectFile> proFiles = projectFileRepository.findByProject(plmProject.getId());
            proFiles.forEach(proFile -> {
                projectFiles.add(proFile);
            });

            List<PLMWbsElement> wbs = wbsElementRepository.findByProject(plmProject);
            wbs.forEach(w ->{
                List<PLMActivity> activities =  activityRepository.findByWbs(w.getId());
                activities.forEach(activity ->{
                    List<PLMTask> plmTasks= taskRepository.findByActivity(activity.getId());
                    if(plmTasks.size() > 0) {
                        plmTasks.forEach(task -> projectTasks.add(task));
                    }
                });
                if(activities.size() > 0) {
                    activities.forEach(activity -> projectActivities.add(activity));
                }
            });

        });
        List<Object> programResource = new ArrayList<>(programResourceRepository.findByPerson(integer));
        List<Object> plmProjectMembers = new ArrayList<>(projectMemberRepository.findByPerson(integer));

        // ---------------------- program -------------------------------------------------------
        List<Object> programs = new ArrayList<>(programRepository.findByProgramManager(integer));
        programs.forEach(program ->{
            PLMProgram program1 = (PLMProgram)program;
            List<PLMProgramProject> programProjects= programProjectRepository.findByProgramAndProjectIsNotNull(program1.getId());
            programProjects.forEach(pp ->{
                PLMProject project =  projectRepository.findOne(pp.getProject());
                plmProjects.add(project);

                List<PLMWbsElement> wbs = wbsElementRepository.findByProject(project);
                wbs.forEach(w ->{
                    List<PLMActivity> activities =  activityRepository.findByWbs(w.getId());
                    activities.forEach(activity ->{
                        List<PLMTask> plmTasks= taskRepository.findByActivity(activity.getId());
                        if(plmTasks.size() > 0) {
                            plmTasks.forEach(task -> projectTasks.add(task));
                        }
                    });
                    if(activities.size() > 0) {
                        activities.forEach(activity -> projectActivities.add(activity));
                    }
                });

            });
        });


        PMListDto pmdto = new PMListDto();
        pmdto.setProgramList(programs);
        pmdto.setProgramResourceList(programResource);
        pmdto.setProjectList(plmProjects);
        pmdto.setProjectMembersList(plmProjectMembers);
        pmdto.setProjectTaskList(projectTasks);
        pmdto.setProjectActivityList(projectActivities);
        pmdto.setFileList(projectFiles);
        return pmdto;
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getProgramCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setResourcesCount(programResourceRepository.getProgramResourceCount(id));
        detailsDto.setFiles(programFileRepository.getFilesCountByProgramAndFileTypeAndLatestTrue(id, "FILE"));
        detailsDto.setProjectCount(programProjectRepository.getProgramProjectsCount(id));
        List<Integer> ids = programProjectRepository.getProgramProjectIds(id);
        if (ids.size() > 0) {
            detailsDto.setTasks(taskRepository.getPendingTaskIdsByProjectIds(ids));
            detailsDto.setTasks(detailsDto.getTasks() + activityRepository.getPendingActivityIdsByProjectIds(ids));
            detailsDto.setFinishedTasks(taskRepository.getFinishedTaskIdsByProjectIds(ids));
            detailsDto.setFinishedTasks(detailsDto.getFinishedTasks() + activityRepository.getFinishedActivityIdsByProjectIds(ids));
        }
        return detailsDto;
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public List<PLMProgram> getAll() {
        return programRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<ProgramDto> getAllPrograms(Pageable pageable, ProjectCriteria criteria) {
        List<ProgramDto> programDtos = new LinkedList<>();
        Predicate predicate = programPredicateBuilder.build(criteria, QPLMProgram.pLMProgram);
        Page<PLMProgram> programs = programRepository.findAll(predicate, pageable);
        for (PLMProgram program : programs.getContent()) {
            ProgramDto programDto = new ProgramDto();
            PLMWorkflow workflow = plmWorkflowRepository.findByAttachedTo(program.getId());
            if (workflow != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
                programDto.setStatus(workflowStatus.getName());
            }

            programDto.setId(program.getId());
            programDto.setName(program.getName());
            if (program.getType() != null) {
                programDto.setType(program.getType().getName());
            }
            programDto.setDescription(program.getDescription());
            getProgramPercentage(programDto);
            if (program.getProgramManager() != null) {
                Person person = personRepository.findOne(program.getProgramManager());
                programDto.setManagerFirstName(person.getFirstName());
                programDto.setManagerLastName(person.getLastName());
                programDto.setManagerFullName(person.getFullName());
                programDto.setProgramManager(program.getProgramManager());
                programDto.setHasManagerImage(person.isHasImage());
            }

            List<Integer> projectMemberIds = programResourceRepository.getProjectResourceIds(program.getId());
            if (projectMemberIds.size() > 0) {
                List<Person> persons = personRepository.findByIdIn(projectMemberIds);
                for (Person person : persons) {
                    ProjectMemberDto projectMemberDto = new ProjectMemberDto();
                    projectMemberDto.setId(person.getId());
                    projectMemberDto.setFirstName(person.getFirstName());
                    projectMemberDto.setLastName(person.getLastName());
                    projectMemberDto.setFullName(person.getFullName());
                    projectMemberDto.setHasImage(person.isHasImage());
                    programDto.getProjectMembers().add(projectMemberDto);
                }
            }
            programDto.setModifiedDate(program.getModifiedDate());
            programDto.setModifiedByName(personRepository.findOne(program.getModifiedBy()).getFullName());
            programDto.setProjects(programProjectRepository.getProgramProjectsCount(program.getId()));
            programDto.setComments(commentRepository.findAllByObjectId(program.getId()).size());
            programDto.setTagsCount(program.getTags().size());
            programDtos.add(programDto);
        }
        return new PageImpl<ProgramDto>(programDtos, pageable, programs.getTotalElements());
    }

    @Transactional(readOnly = true)
    private ProgramDto getProgramPercentage(ProgramDto program) {
        Double completedCount = 0.0;
        List<Integer> projectIds = programProjectRepository.getProgramProjectIds(program.getId());
        for (Integer projectId : projectIds) {
            Double percent = projectService.getProjectPercentComplete(projectId).getPercentComplete();
            if (percent == 100) {
                completedCount += 1;
            } else if (percent > 0) {
                program.setStarted(true);
            }
        }
        if (projectIds.size() > 0) {
            program.setPercent(completedCount.intValue() + "/" + projectIds.size());
            Double percent = completedCount / projectIds.size();
            program.setPercentComplete(percent * 100);
        }
        return program;
    }

    @Transactional(readOnly = true)
    public List<PLMProgram> getProgramsByIds(List<Integer> ids) {
        return programRepository.findByIdIn(ids);
    }

    public List<Person> getProgramManagers() {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = programRepository.getProgramManagerIds();
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<ProgramProjectFolderDto> getProgramProjectFolders(Integer programId) {
        List<ProgramProjectFolderDto> programProjectDtos = new LinkedList<>();
        List<PLMProgramProject> programProjects = programProjectRepository.findByProgramAndParentIsNullOrderByIdAsc(programId);
        programProjects.forEach(programProject -> {
            ProgramProjectFolderDto groupDto = new ProgramProjectFolderDto();
            groupDto.setId(programProject.getId());
            groupDto.setProject(programProject.getProject());
            groupDto.setName(programProject.getName());
            groupDto.setProgram(programProject.getProgram());
            groupDto.setParent(programProject.getParent());
            groupDto.setObjectType("GROUP");

            List<PLMProgramProject> projects = programProjectRepository.findByParentOrderByIdAsc(programProject.getId());
            projects.forEach(plmProgramProject -> {
                ProgramProjectFolderDto projectDto = new ProgramProjectFolderDto();
                PLMProject project = projectRepository.findOne(plmProgramProject.getProject());
                projectDto.setId(plmProgramProject.getId());
                projectDto.setProject(plmProgramProject.getProject());
                projectDto.setProgram(plmProgramProject.getProgram());
                projectDto.setParent(programProject.getParent());
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
                groupDto.getChildren().add(projectDto);
            });
            programProjectDtos.add(groupDto);
        });

        return programProjectDtos;
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

    @Transactional
    public List<ProgramProjectDto> getProgramProjects(Integer programId) {
        List<ProgramProjectDto> programProjectDtos = new LinkedList<>();
        List<PLMProgramProject> programProjects = programProjectRepository.findByProgramAndParentIsNullOrderByIdAsc(programId);
        programProjects.forEach(programProject -> {
            programProjectDtos.add(convertProgramProjectToDto(programProject));
        });

        return programProjectDtos;
    }

    private ProgramProjectDto convertProgramProjectToDto(PLMProgramProject programProject) {
        ProgramProjectDto programProjectDto = new ProgramProjectDto();
        programProjectDto.setId(programProject.getId());
        programProjectDto.setProject(programProject.getProject());
        programProjectDto.setProgram(programProject.getProgram());
        programProjectDto.setParent(programProject.getParent());
        if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
            PLMProject project = projectRepository.findOne(programProject.getProject());
            Integer totalCount = taskRepository.getProjectTaskCount(programProject.getProject());
            Integer completedCount = taskRepository.getProjectTaskCompletedCount(programProject.getProject());

            List<Integer> activityIds = taskRepository.getProjectTaskActivityIdsCount(programProject.getProject());
            if (activityIds.size() > 0) {
                totalCount = totalCount + activityRepository.getProjectActivityCountWithoutIds(programProject.getProject(), activityIds);
                completedCount = completedCount + activityRepository.getProjectActivityCompletedCountWithoutIds(programProject.getProject(), activityIds);
            } else {
                totalCount = activityRepository.getProjectActivityCount(programProject.getProject());
                completedCount = activityRepository.getProjectActivityCompletedCount(programProject.getProject());
            }

            if (totalCount > 0) {
                programProjectDto.setPercentComplete((completedCount.doubleValue() / totalCount.doubleValue()) * 100);
                programProjectDto.setPercent(completedCount + "/" + totalCount);
            }
            programProjectDto.setName(project.getName());
            programProjectDto.setDescription(project.getDescription());
            programProjectDto.setPlannedStartDate(project.getPlannedStartDate());
            programProjectDto.setPlannedFinishDate(project.getPlannedFinishDate());
            programProjectDto.setActualStartDate(project.getActualStartDate());
            programProjectDto.setActualFinishDate(project.getActualFinishDate());
            programProjectDto.setMakeConversationPrivate(project.getMakeConversationPrivate());
            if (project.getProjectManager() != null) {
                Person person = personRepository.findOne(project.getProjectManager());
                programProjectDto.setManagerFirstName(person.getFirstName());
                programProjectDto.setManagerLastName(person.getLastName());
                programProjectDto.setManagerFullName(person.getFullName());
                programProjectDto.setProjectManager(project.getProjectManager());
            }
        } else {
            programProjectDto.setName(programProject.getName());
            programProjectDto.setDescription(programProject.getDescription());
        }
        List<PLMProgramProject> programProjects = programProjectRepository.findByParentOrderByIdAsc(programProject.getId());
        programProjects.forEach(child -> {
            programProjectDto.getChildren().add(convertProgramProjectToDto(child));
        });
        programProjectDto.setType(programProject.getType());
        return programProjectDto;
    }


    @Transactional
    public List<DrillDownProjectDto> getProgramProjectsDrillDown(Integer programId) {
        List<DrillDownProjectDto> drillDownProjectDtos = new LinkedList<>();
        List<PLMProgramProject> programProjects = programProjectRepository.findByProgramAndParentIsNullOrderByIdAsc(programId);
        programProjects.forEach(programProject -> {
            drillDownProjectDtos.add(convertProgramProjectToDrillDownDto(programProject));
        });

        return drillDownProjectDtos;
    }


    private DrillDownProjectDto convertProgramProjectToDrillDownDto(PLMProgramProject programProject) {
        DrillDownProjectDto programProjectDto = new DrillDownProjectDto();
        programProjectDto.setId(programProject.getId());
        programProjectDto.setProject(programProject.getProject());
        programProjectDto.setProgram(programProject.getProgram());
        programProjectDto.setParent(programProject.getParent());
        if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
            PLMProject project = projectRepository.findOne(programProject.getProject());
            Integer totalCount = taskRepository.getProjectTaskCount(programProject.getProject());
            Integer completedCount = taskRepository.getProjectTaskCompletedCount(programProject.getProject());
            List<Integer> activityIds = taskRepository.getProjectTaskActivityIdsCount(programProject.getProject());
            if (activityIds.size() > 0) {
                totalCount = totalCount + activityRepository.getProjectActivityCountWithoutIds(programProject.getProject(), activityIds);
                completedCount = completedCount + activityRepository.getProjectActivityCompletedCountWithoutIds(programProject.getProject(), activityIds);
            } else {
                totalCount = activityRepository.getProjectActivityCount(programProject.getProject());
                completedCount = activityRepository.getProjectActivityCompletedCount(programProject.getProject());
            }
            if (totalCount > 0) {
                programProjectDto.setPercentComplete((completedCount.doubleValue() / totalCount.doubleValue()) * 100);
                programProjectDto.setPercent(completedCount + "/" + totalCount);
            }
            programProjectDto.setTypeName("Project");
            programProjectDto.setName(project.getName());
            programProjectDto.setDescription(project.getDescription());
            programProjectDto.setPlannedStartDate(project.getPlannedStartDate());
            programProjectDto.setPlannedFinishDate(project.getPlannedFinishDate());
            programProjectDto.setActualStartDate(project.getActualStartDate());
            programProjectDto.setActualFinishDate(project.getActualFinishDate());
            if (project.getProjectManager() != null) {
                Person person = personRepository.findOne(project.getProjectManager());
                programProjectDto.setAssignedToName(person.getFullName());
                programProjectDto.setProjectManager(project.getProjectManager());
            }
            programProjectDto.setChildCount(wbsElementRepository.getWbsCountByProject(project.getId()));
        } else {
            programProjectDto.setTypeName("Group");
            programProjectDto.setName(programProject.getName());
            programProjectDto.setDescription(programProject.getDescription());
            programProjectDto.setChildCount(programProjectRepository.getChildCountByParent(programProject.getId()));
        }
        List<PLMProgramProject> programProjects = programProjectRepository.findByParentOrderByIdAsc(programProject.getId());
        programProjects.forEach(child -> {
            programProjectDto.getChildren().add(convertProgramProjectToDrillDownDto(child));
        });
        programProjectDto.setProjectType(programProject.getType());
        programProjectDto.setObjectType(programProject.getType().name());
        return programProjectDto;
    }

    @Transactional(readOnly = true)
    public List<DrillDownProjectDto> getProgramProjectStructure(Integer id, Integer projectId) {
        List<DrillDownProjectDto> wbsDtoList = new ArrayList<>();
        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectIdAndParentIsNullOrderBySequenceNumberAsc(projectId);
        if (rootWbsElements.size() != 0) {
            for (PLMWbsElement wbsElement : rootWbsElements) {
                DrillDownProjectDto wbsDto = new DrillDownProjectDto();
                wbsDto.setId(wbsElement.getId());
                wbsDto.setName(wbsElement.getName());
                wbsDto.setDescription(wbsElement.getDescription());
                wbsDto.setSequenceNumber(wbsElement.getSequenceNumber());
                wbsDto.setObjectType(wbsElement.getObjectType().name());
                wbsDto.setProject(wbsElement.getProject().getId());
                wbsDto.setPlannedStartDate(wbsElement.getPlannedStartDate());
                wbsDto.setPlannedFinishDate(wbsElement.getPlannedFinishDate());
                wbsDto.setTypeName("Phase");
                List<PLMActivity> wbsActivities = activityRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                if (wbsActivities.size() > 0) {
                    for (PLMActivity activity : wbsActivities) {
                        DrillDownProjectDto activityDto = new DrillDownProjectDto();
                        activityDto.setId(activity.getId());
                        activityDto.setName(activity.getName());
                        activityDto.setDescription(activity.getDescription());
                        activityDto.setDuration(activity.getDuration());
                        activityDto.setActualFinishDate(activity.getActualFinishDate());
                        activityDto.setActualStartDate(activity.getActualStartDate());
                        activityDto.setPlannedStartDate(activity.getPlannedStartDate());
                        activityDto.setPlannedFinishDate(activity.getPlannedFinishDate());
                        activityDto.setSequenceNumber(activity.getSequenceNumber());
                        activityDto.setObjectType(activity.getObjectType().name());
                        activityDto.setStatus(activity.getStatus().name());
                        activityDto.setParent(wbsDto.getId());
                        activityDto.setWorkflow(activity.getWorkflow());
                        activityDto.setAssignedTo(activity.getAssignedTo());
                        activityDto.setTypeName("Activity");
                        activityDto.setIsShared(sharedObjectRepository.getSharedCountByObjectId(activityDto.getId()) > 0);
                        activityDto.setHasFiles(activityFileRepository.getActivityFileCount(activity.getId(), "FILE") > 0);
                        if (activity.getAssignedTo() != null) {
                            activityDto.setAssignedToName(personRepository.findOne(activity.getAssignedTo()).getFullName());
                            activityDto.setValidUser(loginRepository.findByPersonId(activityDto.getAssignedTo()).getIsActive());
                        }
                        List<PLMTask> activityTasks = taskRepository.findByActivityOrderBySequenceNumberAsc(activity.getId());
                        if (activityTasks.size() > 0) {
                            Double activityPercentComplete = 0.0;
                            for (PLMTask task : activityTasks) {
                                activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                                DrillDownProjectDto taskDto = new DrillDownProjectDto();
                                taskDto.setId(task.getId());
                                taskDto.setName(task.getName());
                                taskDto.setDescription(task.getDescription());
                                taskDto.setDuration(task.getDuration());
                                taskDto.setActivity(task.getActivity());
                                taskDto.setActualFinishDate(task.getActualFinishDate());
                                taskDto.setActualStartDate(task.getActualStartDate());
                                taskDto.setPlannedStartDate(task.getPlannedStartDate());
                                taskDto.setPlannedFinishDate(task.getPlannedFinishDate());
                                taskDto.setSequenceNumber(task.getSequenceNumber());
                                taskDto.setObjectType(task.getObjectType().name());
                                taskDto.setStatus(task.getStatus().name());
                                taskDto.setParent(wbsDto.getId());
                                taskDto.setWorkflow(task.getWorkflow());
                                taskDto.setAssignedTo(task.getAssignedTo());
                                taskDto.setTypeName("Task");
                                taskDto.setProject(wbsElement.getProject().getId());
                                taskDto.setIsShared(sharedObjectRepository.getSharedCountByObjectId(task.getId()) > 0);
                                taskDto.setHasFiles(taskFileRepository.getTaskFileCount(task.getId(), "FILE") > 0);
                                if (task.getAssignedTo() != null) {
                                    taskDto.setAssignedToName(personRepository.findOne(task.getAssignedTo()).getFullName());
                                    taskDto.setValidUser(loginRepository.findByPersonId(task.getAssignedTo()).getIsActive());
                                }
                                if (task.getWorkflow() != null) {
                                    taskDto.setFinishedWorkflow(plmWorkflowRepository.findOne(task.getWorkflow()).getFinished());
                                }
                                taskDto.setPercentComplete(task.getPercentComplete());
                                activityDto.getChildren().add(taskDto);
                            }
                            activityDto.setChildCount(activityTasks.size());
                            activityDto.setPercentComplete((activityPercentComplete) / activityTasks.size());
                        }
                        wbsDto.getChildren().add(activityDto);
                    }
                }
                List<PLMMilestone> wbsMilestones = milestoneRepository.findByWbsOrderBySequenceNumberAsc(wbsElement.getId());
                if (wbsMilestones.size() > 0) {
                    for (PLMMilestone milestone : wbsMilestones) {
                        DrillDownProjectDto milestoneDto = new DrillDownProjectDto();
                        milestoneDto.setId(milestone.getId());
                        milestoneDto.setName(milestone.getName());
                        milestoneDto.setDescription(milestone.getDescription());
                        milestoneDto.setSequenceNumber(milestone.getSequenceNumber());
                        milestoneDto.setPlannedFinishDate(milestone.getPlannedFinishDate());
                        milestoneDto.setActualFinishDate(milestone.getActualFinishDate());
                        milestoneDto.setStatus(milestone.getStatus().name());
                        milestoneDto.setObjectType(milestone.getObjectType().name());
                        milestoneDto.setParent(wbsDto.getId());
                        milestoneDto.setAssignedTo(milestone.getAssignedTo());
                        milestoneDto.setTypeName("Milestone");
                        if (milestone.getAssignedTo() != null) {
                            milestoneDto.setAssignedToName(personRepository.findOne(milestone.getAssignedTo()).getFullName());
                            milestoneDto.setValidUser(loginRepository.findByPersonId(milestoneDto.getAssignedTo()).getIsActive());
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
                        wbsDto.getChildren().add(milestoneDto);
                    }
                }
                if (wbsDto.getChildren().size() > 0) {
                    Collections.sort(wbsDto.getChildren(), new Comparator<DrillDownProjectDto>() {
                        public int compare(final DrillDownProjectDto object1, final DrillDownProjectDto object2) {
                            return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                        }
                    });
                }
                wbsDtoList.add(wbsDto);
            }
            Collections.sort(wbsDtoList, new Comparator<DrillDownProjectDto>() {
                public int compare(final DrillDownProjectDto object1, final DrillDownProjectDto object2) {
                    return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                }
            });
        }
        return wbsDtoList;
    }


    @Transactional(readOnly = true)
    public List<DrillDownProjectDto> getProgramProjectsStructureByAssignedTo(Integer id, Integer assignedTo) {
        List<DrillDownProjectDto> drillDownProjectDtos = new LinkedList<>();

        List<Integer> projectIds = programProjectRepository.getProgramProjectIds(id);
        Map<Integer, List<PLMWbsElement>> projectWbsMap = new HashMap<>();
        Map<Integer, List<PLMActivity>> wbsActivityMap = new HashMap();
        Map<Integer, List<PLMMilestone>> wbsMilestoneMap = new HashMap();
        Map<Integer, List<PLMTask>> activityTaskMap = new HashMap();

        List<PLMWbsElement> rootWbsElements = wbsElementRepository.findByProjectIdInAndParentIsNullOrderBySequenceNumberAsc(projectIds);
        List<PLMActivity> activities = activityRepository.getActivityByProjectIds(projectIds);
        List<PLMMilestone> milestones = milestoneRepository.getMilestoneByProjectIdsAndAssignedTo(projectIds, assignedTo);
        List<PLMTask> tasks = taskRepository.getTaskByProjectIdsAndAssignedTo(projectIds, assignedTo);
        projectWbsMap = rootWbsElements.stream().collect(Collectors.groupingBy(d -> d.getProject().getId()));
        wbsActivityMap = activities.stream().collect(Collectors.groupingBy(d -> d.getWbs()));
        wbsMilestoneMap = milestones.stream().collect(Collectors.groupingBy(d -> d.getWbs()));
        activityTaskMap = tasks.stream().collect(Collectors.groupingBy(d -> d.getActivity()));
        List<PLMProgramProject> programProjects = programProjectRepository.findByProgramAndParentIsNullOrderByIdAsc(id);

        Map<Integer, List<PLMWbsElement>> wbsMap = projectWbsMap;
        Map<Integer, List<PLMActivity>> activityMap = wbsActivityMap;
        Map<Integer, List<PLMMilestone>> milestoneMap = wbsMilestoneMap;
        Map<Integer, List<PLMTask>> taskMap = activityTaskMap;
        programProjects.forEach(programProject -> {
            DrillDownProjectDto programProjectDto = new DrillDownProjectDto();
            programProjectDto.setId(programProject.getId());
            programProjectDto.setProject(programProject.getProject());
            programProjectDto.setProgram(programProject.getProgram());
            programProjectDto.setParent(programProject.getParent());
            if (programProject.getType().equals(ProgramProjectType.PROJECT)) {
                PLMProject project = projectRepository.findOne(programProject.getProject());
                Integer totalCount = taskRepository.getProjectTaskCount(programProject.getProject());
                Integer completedCount = taskRepository.getProjectTaskCompletedCount(programProject.getProject());
                List<Integer> activityIds = taskRepository.getProjectTaskActivityIdsCount(programProject.getProject());
                if (activityIds.size() > 0) {
                    totalCount = totalCount + activityRepository.getProjectActivityCountWithoutIds(programProject.getProject(), activityIds);
                    completedCount = completedCount + activityRepository.getProjectActivityCompletedCountWithoutIds(programProject.getProject(), activityIds);
                } else {
                    totalCount = activityRepository.getProjectActivityCount(programProject.getProject());
                    completedCount = activityRepository.getProjectActivityCompletedCount(programProject.getProject());
                }
                if (totalCount > 0) {
                    programProjectDto.setPercentComplete((completedCount.doubleValue() / totalCount.doubleValue()) * 100);
                    programProjectDto.setPercent(completedCount + "/" + totalCount);
                }
                programProjectDto.setTypeName("Project");
                programProjectDto.setName(project.getName());
                programProjectDto.setDescription(project.getDescription());
                programProjectDto.setPlannedStartDate(project.getPlannedStartDate());
                programProjectDto.setPlannedFinishDate(project.getPlannedFinishDate());
                programProjectDto.setActualStartDate(project.getActualStartDate());
                programProjectDto.setActualFinishDate(project.getActualFinishDate());
                if (project.getProjectManager() != null) {
                    Person person = personRepository.findOne(project.getProjectManager());
                    programProjectDto.setAssignedToName(person.getFullName());
                    programProjectDto.setProjectManager(project.getProjectManager());
                }
                programProjectDto.setChildCount(wbsElementRepository.getWbsCountByProject(project.getId()));
            } else {
                programProjectDto.setTypeName("Group");
                programProjectDto.setName(programProject.getName());
                programProjectDto.setDescription(programProject.getDescription());
                programProjectDto.setChildCount(programProjectRepository.getChildCountByParent(programProject.getId()));
            }
            programProjectDto.setProjectType(programProject.getType());
            programProjectDto.setObjectType(programProject.getType().name());
            List<PLMProgramProject> childProjects = programProjectRepository.findByParentOrderByIdAsc(programProject.getId());
            childProjects.forEach(child -> {
                DrillDownProjectDto downProjectDto = convertProgramProjectToDrillDownDto(child);
                List<PLMWbsElement> wbsElements = wbsMap.containsKey(child.getProject()) ? wbsMap.get(child.getProject()) : new ArrayList<PLMWbsElement>();
                for (PLMWbsElement wbsElement : wbsElements) {
                    DrillDownProjectDto wbsDto = new DrillDownProjectDto();
                    wbsDto.setId(wbsElement.getId());
                    wbsDto.setName(wbsElement.getName());
                    wbsDto.setDescription(wbsElement.getDescription());
                    wbsDto.setSequenceNumber(wbsElement.getSequenceNumber());
                    wbsDto.setObjectType(wbsElement.getObjectType().name());
                    wbsDto.setProject(wbsElement.getProject().getId());
                    wbsDto.setPlannedStartDate(wbsElement.getPlannedStartDate());
                    wbsDto.setPlannedFinishDate(wbsElement.getPlannedFinishDate());
                    wbsDto.setTypeName("Phase");
                    List<PLMActivity> wbsActivities = activityMap.containsKey(wbsElement.getId()) ? activityMap.get(wbsElement.getId()) : new ArrayList<PLMActivity>();
                    for (PLMActivity activity : wbsActivities) {
                        DrillDownProjectDto activityDto = new DrillDownProjectDto();
                        activityDto.setId(activity.getId());
                        activityDto.setName(activity.getName());
                        activityDto.setDescription(activity.getDescription());
                        activityDto.setDuration(activity.getDuration());
                        activityDto.setActualFinishDate(activity.getActualFinishDate());
                        activityDto.setActualStartDate(activity.getActualStartDate());
                        activityDto.setPlannedStartDate(activity.getPlannedStartDate());
                        activityDto.setPlannedFinishDate(activity.getPlannedFinishDate());
                        activityDto.setSequenceNumber(activity.getSequenceNumber());
                        activityDto.setObjectType(activity.getObjectType().name());
                        activityDto.setStatus(activity.getStatus().name());
                        activityDto.setParent(wbsDto.getId());
                        activityDto.setWorkflow(activity.getWorkflow());
                        activityDto.setAssignedTo(activity.getAssignedTo());
                        activityDto.setTypeName("Activity");
                        activityDto.setIsShared(sharedObjectRepository.getSharedCountByObjectId(activityDto.getId()) > 0);
                        activityDto.setHasFiles(activityFileRepository.getActivityFileCount(activity.getId(), "FILE") > 0);
                        if (activity.getAssignedTo() != null) {
                            activityDto.setAssignedToName(personRepository.findOne(activity.getAssignedTo()).getFullName());
                            activityDto.setValidUser(loginRepository.findByPersonId(activityDto.getAssignedTo()).getIsActive());
                        }
                        List<PLMTask> activityTasks = taskMap.containsKey(activity.getId()) ? taskMap.get(activity.getId()) : new ArrayList<PLMTask>();
                        if (activityTasks.size() > 0) {
                            Double activityPercentComplete = 0.0;
                            for (PLMTask task : activityTasks) {
                                activityPercentComplete = activityPercentComplete + task.getPercentComplete();
                                DrillDownProjectDto taskDto = new DrillDownProjectDto();
                                taskDto.setId(task.getId());
                                taskDto.setName(task.getName());
                                taskDto.setDescription(task.getDescription());
                                taskDto.setDuration(task.getDuration());
                                taskDto.setActivity(task.getActivity());
                                taskDto.setActualFinishDate(task.getActualFinishDate());
                                taskDto.setActualStartDate(task.getActualStartDate());
                                taskDto.setPlannedStartDate(task.getPlannedStartDate());
                                taskDto.setPlannedFinishDate(task.getPlannedFinishDate());
                                taskDto.setSequenceNumber(task.getSequenceNumber());
                                taskDto.setObjectType(task.getObjectType().name());
                                taskDto.setStatus(task.getStatus().name());
                                taskDto.setParent(wbsDto.getId());
                                taskDto.setWorkflow(task.getWorkflow());
                                taskDto.setAssignedTo(task.getAssignedTo());
                                taskDto.setTypeName("Task");
                                taskDto.setProject(wbsElement.getProject().getId());
                                taskDto.setIsShared(sharedObjectRepository.getSharedCountByObjectId(task.getId()) > 0);
                                taskDto.setHasFiles(taskFileRepository.getTaskFileCount(task.getId(), "FILE") > 0);
                                if (task.getAssignedTo() != null) {
                                    taskDto.setAssignedToName(personRepository.findOne(task.getAssignedTo()).getFullName());
                                    taskDto.setValidUser(loginRepository.findByPersonId(task.getAssignedTo()).getIsActive());
                                }
                                if (task.getWorkflow() != null) {
                                    taskDto.setFinishedWorkflow(plmWorkflowRepository.findOne(task.getWorkflow()).getFinished());
                                }
                                taskDto.setPercentComplete(task.getPercentComplete());
                                activityDto.getChildren().add(taskDto);
                            }
                            activityDto.setChildCount(activityTasks.size());
                            activityDto.setPercentComplete((activityPercentComplete) / activityTasks.size());
                        }
                        if (activityDto.getChildren().size() > 0) {
                            wbsDto.getChildren().add(activityDto);
                        } else if (activityDto.getAssignedTo() != null && activityDto.getAssignedTo().equals(assignedTo)) {
                            wbsDto.getChildren().add(activityDto);
                        }
                    }

                    List<PLMMilestone> wbsMilestones = milestoneMap.containsKey(wbsElement.getId()) ? milestoneMap.get(wbsElement.getId()) : new ArrayList<PLMMilestone>();
                    for (PLMMilestone milestone : wbsMilestones) {
                        DrillDownProjectDto milestoneDto = new DrillDownProjectDto();
                        milestoneDto.setId(milestone.getId());
                        milestoneDto.setName(milestone.getName());
                        milestoneDto.setDescription(milestone.getDescription());
                        milestoneDto.setSequenceNumber(milestone.getSequenceNumber());
                        milestoneDto.setPlannedFinishDate(milestone.getPlannedFinishDate());
                        milestoneDto.setActualFinishDate(milestone.getActualFinishDate());
                        milestoneDto.setStatus(milestone.getStatus().name());
                        milestoneDto.setObjectType(milestone.getObjectType().name());
                        milestoneDto.setParent(wbsDto.getId());
                        milestoneDto.setAssignedTo(milestone.getAssignedTo());
                        milestoneDto.setTypeName("Milestone");
                        if (milestone.getAssignedTo() != null) {
                            milestoneDto.setAssignedToName(personRepository.findOne(milestone.getAssignedTo()).getFullName());
                            milestoneDto.setValidUser(loginRepository.findByPersonId(milestoneDto.getAssignedTo()).getIsActive());
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
                        wbsDto.getChildren().add(milestoneDto);
                    }

                    if (wbsDto.getChildren().size() > 0) {
                        Collections.sort(wbsDto.getChildren(), new Comparator<DrillDownProjectDto>() {
                            public int compare(final DrillDownProjectDto object1, final DrillDownProjectDto object2) {
                                return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                            }
                        });
                    }
                    if (wbsDto.getChildren().size() > 0) {
                        downProjectDto.getChildren().add(wbsDto);
                    }
                }
                Collections.sort(downProjectDto.getChildren(), new Comparator<DrillDownProjectDto>() {
                    public int compare(final DrillDownProjectDto object1, final DrillDownProjectDto object2) {
                        return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                    }
                });
                if (downProjectDto.getChildren().size() > 0) {
                    programProjectDto.getChildren().add(downProjectDto);
                } else if (downProjectDto.getProjectManager() != null && downProjectDto.getProjectManager().equals(assignedTo)) {
                    programProjectDto.getChildren().add(downProjectDto);
                }
            });
            if (programProjectDto.getChildren().size() > 0) {
                drillDownProjectDtos.add(programProjectDto);
            }
        });

        return drillDownProjectDtos;
    }


    @Transactional
    public ProgramProjectDto createProgramProject(Integer id, PLMProgramProject programProject) {
        PLMProgramProject existProgram = null;
        if (programProject.getType().equals(ProgramProjectType.GROUP)) {
            existProgram = programProjectRepository.findByProgramAndTypeAndNameEqualsIgnoreCase(programProject.getProgram(), programProject.getType(), programProject.getName());
            if (existProgram != null) {
                String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programProject.getName());
                throw new CassiniException(result);
            }
        } else {
            if (programProject.getParent() != null) {
                existProgram = programProjectRepository.findByParentAndProject(programProject.getParent(), programProject.getProject());
                if (existProgram != null) {
                    PLMProject project = projectRepository.findOne(programProject.getProject());
                    String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", project.getName());
                    throw new CassiniException(result);
                }
            }
        }
        programProject = programProjectRepository.save(programProject);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectAddedEvent(programProject.getProgram(), programProject));
        return convertProgramProjectToDto(programProject);
    }

    @Transactional
    public ProgramProjectDto updateProgramProject(Integer id, PLMProgramProject programProject) {
        PLMProgramProject existProgram = null;
        PLMProgramProject existProgram1 = programProjectRepository.findOne(programProject.getId());
        if (programProject.getType().equals(ProgramProjectType.GROUP)) {
            existProgram = programProjectRepository.findByProgramAndTypeAndNameEqualsIgnoreCase(programProject.getProgram(), programProject.getType(), programProject.getName());
            if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", programProject.getName());
                throw new CassiniException(result);
            }
        } else {
            if (programProject.getParent() != null) {
                existProgram = programProjectRepository.findByParentAndProject(programProject.getParent(), programProject.getProject());
                if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                    PLMProject project = projectRepository.findOne(programProject.getProject());
                    String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", project.getName());
                    throw new CassiniException(result);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectUpdateEvent(programProject, existProgram1));
        programProject = programProjectRepository.save(programProject);
        return convertProgramProjectToDto(programProject);
    }

    @Transactional
    public void deleteProgramProject(Integer id, Integer programProject) {
        if (programProject != null) {
            PLMProgramProject plmProgramProject = programProjectRepository.findOne(programProject);
            if (plmProgramProject != null) {
                applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectDeletedEvent(plmProgramProject.getProgram(), plmProgramProject));
                if (plmProgramProject.getType().equals(ProgramProjectType.PROJECT)) {
                    PLMProject plmProject = projectRepository.findOne(plmProgramProject.getProject());
                    Integer taskFinishedCount = taskRepository.getProjectTaskCompletedCount(plmProgramProject.getProject());
                    List<Integer> activityIds = taskRepository.getProjectTaskActivityIdsCount(plmProgramProject.getProject());
                    if (activityIds.size() > 0) {
                        taskFinishedCount = taskFinishedCount + activityRepository.getProjectActivityCompletedCountWithoutIds(plmProgramProject.getProject(), activityIds);
                    } else {
                        taskFinishedCount = activityRepository.getProjectActivityCompletedCount(plmProgramProject.getProject());
                    }
                    if (taskFinishedCount == 0) {
                        applicationEventPublisher.publishEvent(new WorkflowEvents.ProjectActivityTaskWorkflowDeletedEvent(plmProgramProject.getProject()));
                        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(plmProgramProject.getProject()));
                        projectRepository.delete(plmProgramProject.getProject());
                    } else {
                        String message = messageSource.getMessage("project_already_finished", null, "{0}: project already finished we cannot delete", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", plmProject.getName());
                        throw new CassiniException(result);
                    }
                } else {
                    List<PLMProgramProject> programProjects = programProjectRepository.findByParentOrderByIdAsc(plmProgramProject.getId());
                    for (PLMProgramProject programProject1 : programProjects) {
                        PLMProject plmProject = projectRepository.findOne(programProject1.getProject());
                        if (plmProject.getActualFinishDate() == null) {
                            projectRepository.delete(programProject1.getProject());
                        } else {
                            String message = messageSource.getMessage("program_project_already_finished", null, "{0}: project already finished we cannot delete the group : {1}", LocaleContextHolder.getLocale());
                            String result = MessageFormat.format(message + ".", plmProject.getName(), plmProgramProject.getName());
                            throw new CassiniException(result);
                        }
                    }
                    programProjectRepository.delete(programProject);
                }
            }

        }

    }

    @Transactional
    public ProgramProjectDto getProgramProject(Integer id, Integer programProjectId) {
        PLMProgramProject programProject = programProjectRepository.findOne(programProjectId);
        return convertProgramProjectToDto(programProject);
    }


    @Transactional
    public DrillDownProjectDto createProgramProjectObject(Integer id, DrillDownProjectDto drillDownProjectDto) {
        PLMProgramProject existProgram = null;
        if (drillDownProjectDto.getObjectType().equals("GROUP") || drillDownProjectDto.getObjectType().equals("PROJECT")) {
            PLMProgramProject programProject = new PLMProgramProject();
            programProject.setName(drillDownProjectDto.getName());
            programProject.setType(ProgramProjectType.valueOf(drillDownProjectDto.getObjectType()));
            programProject.setDescription(drillDownProjectDto.getDescription());
            programProject.setProgram(id);
            if (programProject.getType().equals(ProgramProjectType.GROUP)) {
                existProgram = programProjectRepository.findByProgramAndTypeAndNameEqualsIgnoreCase(programProject.getProgram(), programProject.getType(), programProject.getName());
                if (existProgram != null) {
                    String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", programProject.getName());
                    throw new CassiniException(result);
                }
            } else {
                if (programProject.getParent() != null) {
                    existProgram = programProjectRepository.findByParentAndProject(programProject.getParent(), programProject.getProject());
                    if (existProgram != null) {
                        PLMProject project = projectRepository.findOne(programProject.getProject());
                        String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", project.getName());
                        throw new CassiniException(result);
                    }
                }
            }
            programProject = programProjectRepository.save(programProject);
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectAddedEvent(programProject.getProgram(), programProject));
            return convertProgramProjectToDrillDownDto(programProject);
        } else {
            if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTPHASEELEMENT.name())) {
                PLMWbsElement wbsElement = new PLMWbsElement();
                wbsElement.setName(drillDownProjectDto.getName());
                wbsElement.setDescription(drillDownProjectDto.getDescription());
                wbsElement.setProject(projectRepository.findOne(drillDownProjectDto.getProject()));
                wbsElement.setSequenceNumber(drillDownProjectDto.getSequenceNumber());
                wbsElement.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                wbsElement.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                wbsElement = wbsElementRepository.save(wbsElement);
                drillDownProjectDto.setId(wbsElement.getId());
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.name())) {
                PLMActivity activity = new PLMActivity();
                activity.setName(drillDownProjectDto.getName());
                activity.setDescription(drillDownProjectDto.getDescription());
                activity.setWbs(drillDownProjectDto.getWbs());
                activity.setSequenceNumber(drillDownProjectDto.getSequenceNumber());
                activity.setAssignedTo(drillDownProjectDto.getAssignedTo());
                activity.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                activity.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                activity.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTACTIVITY));
                activity = activityRepository.save(activity);
                drillDownProjectDto.setId(activity.getId());
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTMILESTONE.name())) {
                PLMMilestone milestone = new PLMMilestone();
                milestone.setName(drillDownProjectDto.getName());
                milestone.setDescription(drillDownProjectDto.getDescription());
                milestone.setAssignedTo(drillDownProjectDto.getAssignedTo());
                milestone.setSequenceNumber(drillDownProjectDto.getSequenceNumber());
                milestone.setWbs(drillDownProjectDto.getWbs());
                milestone.setStatus(ProjectActivityStatus.PENDING);
                milestone.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                milestone = milestoneRepository.save(milestone);
                drillDownProjectDto.setId(milestone.getId());
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTTASK.name())) {
                PLMTask task = new PLMTask();
                task.setName(drillDownProjectDto.getName());
                task.setDescription(drillDownProjectDto.getDescription());
                task.setActivity(drillDownProjectDto.getActivity());
                task.setDuration(drillDownProjectDto.getDuration());
                task.setSequenceNumber(drillDownProjectDto.getSequenceNumber());
                task.setAssignedTo(drillDownProjectDto.getAssignedTo());
                task.setStatus(ProjectTaskStatus.PENDING);
                task.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                task.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                task.setType(pmObjectTypeRepository.findByTypeAndParentIsNullOrderByCreatedDateAsc(PMType.PROJECTTASK));
                task = taskRepository.save(task);
                drillDownProjectDto.setId(task.getId());
            }
            return drillDownProjectDto;
        }
    }

    @Transactional
    public DrillDownProjectDto updateProgramProjectObject(Integer id, DrillDownProjectDto drillDownProjectDto) {
        PLMProgramProject existProgram = null;
        if (drillDownProjectDto.getObjectType().equals("GROUP") || drillDownProjectDto.getObjectType().equals("PROJECT")) {
            PLMProgramProject existProgram1 = JsonUtils.cloneEntity(programProjectRepository.findOne(drillDownProjectDto.getId()), PLMProgramProject.class);
            PLMProgramProject programProject = programProjectRepository.findOne(drillDownProjectDto.getId());
            programProject.setName(drillDownProjectDto.getName());
            programProject.setDescription(drillDownProjectDto.getDescription());
            if (programProject.getType().equals(ProgramProjectType.GROUP)) {
                existProgram = programProjectRepository.findByProgramAndTypeAndNameEqualsIgnoreCase(programProject.getProgram(), programProject.getType(), programProject.getName());
                if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                    String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", programProject.getName());
                    throw new CassiniException(result);
                }
            } else {
                if (programProject.getParent() != null) {
                    existProgram = programProjectRepository.findByParentAndProject(programProject.getParent(), programProject.getProject());
                    if (existProgram != null && !existProgram.getId().equals(programProject.getId())) {
                        PLMProject project = projectRepository.findOne(programProject.getProject());
                        String message = messageSource.getMessage("program_project_already_exist", null, "{0}: project already exist", LocaleContextHolder.getLocale());
                        String result = MessageFormat.format(message + ".", project.getName());
                        throw new CassiniException(result);
                    }
                }
            }
            applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectUpdateEvent(programProject, existProgram1));
            programProject = programProjectRepository.save(programProject);
            return convertProgramProjectToDrillDownDto(programProject);
        } else {
            if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTPHASEELEMENT.name())) {
                PLMWbsElement wbsElement = wbsElementRepository.findOne(drillDownProjectDto.getId());
                wbsElement.setName(drillDownProjectDto.getName());
                wbsElement.setDescription(drillDownProjectDto.getDescription());
                wbsElement.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                wbsElement.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                wbsElement = wbsElementRepository.save(wbsElement);
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.name())) {
                PLMActivity activity = activityRepository.findOne(drillDownProjectDto.getId());
                activity.setName(drillDownProjectDto.getName());
                activity.setDescription(drillDownProjectDto.getDescription());
                activity.setAssignedTo(drillDownProjectDto.getAssignedTo());
                activity.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                activity.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                activity = activityRepository.save(activity);
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTMILESTONE.name())) {
                PLMMilestone milestone = milestoneRepository.findOne(drillDownProjectDto.getId());
                milestone.setName(drillDownProjectDto.getName());
                milestone.setDescription(drillDownProjectDto.getDescription());
                milestone.setAssignedTo(drillDownProjectDto.getAssignedTo());
                milestone.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                milestone = milestoneRepository.save(milestone);
            } else if (drillDownProjectDto.getObjectType().equals(PLMObjectType.PROJECTTASK.name())) {
                PLMTask task = taskRepository.findOne(drillDownProjectDto.getId());
                task.setName(drillDownProjectDto.getName());
                task.setDescription(drillDownProjectDto.getDescription());
                task.setDuration(drillDownProjectDto.getDuration());
                task.setAssignedTo(drillDownProjectDto.getAssignedTo());
                task.setPlannedStartDate(drillDownProjectDto.getPlannedStartDate());
                task.setPlannedFinishDate(drillDownProjectDto.getPlannedFinishDate());
                task = taskRepository.save(task);
            }
            if (drillDownProjectDto.getAssignedTo() != null) {
                drillDownProjectDto.setAssignedToName(personRepository.findOne(drillDownProjectDto.getAssignedTo()).getFullName());
                drillDownProjectDto.setValidUser(loginRepository.findByPersonId(drillDownProjectDto.getAssignedTo()).getIsActive());
            }
            return drillDownProjectDto;
        }
    }

    @Transactional
    public void deleteProgramProjectObject(Integer id, Integer programProject) {
        PLMProgramProject plmProgramProject = programProjectRepository.findOne(programProject);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramProjectDeletedEvent(plmProgramProject.getProgram(), plmProgramProject));
        if (plmProgramProject.getType().equals(ProgramProjectType.PROJECT)) {
            PLMProject plmProject = projectRepository.findOne(plmProgramProject.getProject());
            if (plmProject.getActualFinishDate() == null) {
                projectRepository.delete(plmProgramProject.getProject());
            } else {
                String message = messageSource.getMessage("project_already_finished", null, "{0}: project already finished we cannot delete", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProject.getName());
                throw new CassiniException(result);
            }
        } else {
            List<PLMProgramProject> programProjects = programProjectRepository.findByParentOrderByIdAsc(plmProgramProject.getId());
            for (PLMProgramProject programProject1 : programProjects) {
                PLMProject plmProject = projectRepository.findOne(programProject1.getProject());
                if (plmProject.getActualFinishDate() == null) {
                    projectRepository.delete(programProject1.getProject());
                } else {
                    String message = messageSource.getMessage("program_project_already_finished", null, "{0}: project already finished we cannot delete the group : {1}", LocaleContextHolder.getLocale());
                    String result = MessageFormat.format(message + ".", plmProject.getName(), plmProgramProject.getName());
                    throw new CassiniException(result);
                }
            }
            programProjectRepository.delete(programProject);
        }
    }


    @Transactional
    public List<PLMProgramResource> createProgramResources(List<PLMProgramResource> programResources) {
        programResources = programResourceRepository.save(programResources);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramMembersAddedEvent(programResources.get(0).getProgram(), programResources));
        return programResources;
    }

    @Transactional
    public PLMProgramResource createProgramResource(PLMProgramResource programProject) {
        List<PLMProgramResource> list = new ArrayList<>();
        programProject = programResourceRepository.save(programProject);
        list.add(programProject);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramMembersAddedEvent(programProject.getProgram(), list));
        return programProject;
    }

    @Transactional
    public PLMProgramResource updateProgramResource(PLMProgramResource programProject) {
        PLMProgramResource oldProgramResource = programResourceRepository.findOne(programProject.getId());
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramMemberUpdatedEvent(oldProgramResource, programProject));
        return programResourceRepository.save(programProject);
    }

    @Transactional
    public void deleteProgramResource(Integer programId, Integer resourceId) {
        PLMProgramResource programResource = programResourceRepository.findOne(resourceId);
        programResourceRepository.delete(resourceId);
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramMemberDeletedEvent(programResource.getProgram(), programResource));
    }

    @Transactional
    public PLMProgramResource getProgramResource(Integer programId, Integer resourceId) {
        return programResourceRepository.findOne(resourceId);
    }

    @Transactional
    public List<PLMProgramResource> getProgramResources(Integer programId) {
        return programResourceRepository.findByProgramOrderByIdAsc(programId);
    }

    @Transactional
    public PLMWorkflow attachProgramWorkflow(Integer id, Integer wfDefId, Integer workflowId) {
        PLMWorkflow workflow = null;
        PLMProgram program = programRepository.findOne(id);
        if (wfDefId != null) {
            PLMWorkflowDefinition wfDef = workflowDefinitionService.get(wfDefId);
            if (wfDef != null) {
                workflow = workflowService.attachWorkflow(PLMObjectType.PROGRAM, program.getId(), wfDef);
                program.setWorkflow(workflow.getId());
                programRepository.save(program);
            }
        } else if (workflowId != null) {
            PLMWorkflow workflow1 = workflowRepository.findOne(workflowId);
            if (workflow1 != null) {
                PLMWorkflowDefinition workflowDefinition = workFlowDefinitionRepository.findOne(workflow1.getWorkflowRevision());
                PLMWorkflowDefinition wfDef1 = workflowDefinitionService.get(workflowDefinition.getId());
                if (wfDef1 != null) {
                    workflow = workflowService.attachWorkflow(PLMObjectType.PROGRAM, program.getId(), wfDef1);
                    program.setWorkflow(workflow.getId());
                    programRepository.save(program);
                }
            }
        }
        return workflow;
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowStarted(WorkflowEvents.WorkflowStartedEvent event) throws JsonProcessingException {
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowStartedEvent(program, event.getPlmWorkflow()));
//        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowStarted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowPromoted(WorkflowEvents.WorkflowPromotedEvent event) throws JsonProcessingException {
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMWorkflowStatus toStatus = event.getToStatus();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowPromotedEvent(program, plmWorkflow, fromStatus, toStatus));
//        sendProjectSubscribeNotification(project, plmWorkflow.getName() + "-" + fromStatus.getName() + "-" + toStatus.getName(), "workflowPromoted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowDemoted(WorkflowEvents.WorkflowDemotedEvent event) throws JsonProcessingException {
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowDemotedEvent(program, event.getPlmWorkflow(), event.getFromStatus(), event.getToStatus()));
//        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName() + "-" + fromStatus.getName() + "-" + event.getToStatus().getName(), "workflowDemoted");
    }

    @org.springframework.context.event.EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowFinished(WorkflowEvents.WorkflowFinishedEvent event) throws JsonProcessingException {
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getFromStatus();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowFinishedEvent(program, plmWorkflow));
//        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowFinished");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowHold(WorkflowEvents.WorkflowHoldEvent event) throws JsonProcessingException {
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowHoldEvent(program, plmWorkflow, fromStatus));
//        sendProjectSubscribeNotification(project, event.getPlmWorkflow().getName(), "workflowHold");
    }

    @EventListener(condition = "#event.attachedToType.name() == 'PROGRAM'")
    public void programWorkflowUnHold(WorkflowEvents.WorkflowUnHoldEvent event) throws JsonProcessingException {
        PLMProgram program = (PLMProgram) event.getAttachedToObject();
        PLMWorkflow plmWorkflow = event.getPlmWorkflow();
        PLMWorkflowStatus fromStatus = event.getCurrentStatus();
        applicationEventPublisher.publishEvent(new ProgramEvents.ProgramWorkflowUnholdEvent(program, plmWorkflow, fromStatus));
//        sendProjectSubscribeNotification(program, event.getPlmWorkflow().getName(), "workflowUnHold");
    }

    public void sentProgramTaskNotifications(Integer id, String type) {
        List<Integer> ids = new ArrayList<>();
        List<Integer> activityIds = new ArrayList<>();
        List<Integer> wbsIds = new ArrayList<>();
        Map<Integer, PLMActivity> activityMap = new LinkedHashMap();
        Map<Integer, PLMWbsElement> wbsElementMap = new LinkedHashMap();
        PLMProgram program = programRepository.findOne(id);
        PLMProject project = projectRepository.findOne(id);
        String programName = "";
        String projectName = "";
        boolean programType = false;
        boolean projectType = false;
        List<Integer> assignedToIds = new ArrayList<>();
        if (type.equals("PROGRAM")) {
            programType = true;
            programName = program.getName();
            ids = programProjectRepository.getProgramProjectIds(id);
            if (ids.size() > 0) {
                assignedToIds = taskRepository.getUniquePendingAssignedToByProjectIds(ids);
                List<Integer> activityAssignedToIds = activityRepository.getUniquePendingAssignedToByProjectIds(ids);
                if (activityAssignedToIds.size() > 0) {
                    for (Integer activityAssignedToId : activityAssignedToIds) {
                        if (assignedToIds.indexOf(activityAssignedToId) == -1) {
                            assignedToIds.add(activityAssignedToId);
                        }
                    }
                }
                activityIds = taskRepository.getUniqueActivityByProjectIds(ids);
                List<Integer> wbsIdsList = taskRepository.getUniqueWbsByProjectIds(ids);
                wbsIdsList.addAll(activityRepository.getUniqueWbsByProjectIds(ids));
                wbsIds = wbsIdsList.stream().distinct().collect(Collectors.toList());
            }
        } else if (type.equals("PROJECT")) {
            projectName = project.getName();
            projectType = true;
            assignedToIds = taskRepository.getUniqueAssignedToByProjectId(id);
            List<Integer> activityAssignedToIds = activityRepository.getUniqueAssignedToByProjectId(id);
            if (activityAssignedToIds.size() > 0) {
                for (Integer activityAssignedToId : activityAssignedToIds) {
                    if (assignedToIds.indexOf(activityAssignedToId) == -1) {
                        assignedToIds.add(activityAssignedToId);
                    }
                }
            }
            activityIds = taskRepository.getUniqueActivityByProjectId(id);
            List<Integer> wbsIdsList = taskRepository.getUniqueWbsByProjectId(id);
            wbsIdsList.addAll(activityRepository.getUniqueWbsByProjectId(id));
            wbsIds = wbsIdsList.stream().distinct().collect(Collectors.toList());
        }
        List<PLMActivity> activities = activityRepository.findByIdIn(activityIds);
        List<PLMWbsElement> wbsElements = wbsElementRepository.findByIdIn(wbsIds);
        activityMap = activities.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        wbsElementMap = wbsElements.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        if (assignedToIds != null && assignedToIds.size() > 0) {
            String tenantId = sessionWrapper.getSession().getTenantId();
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            List<PLMTask> taskList = new ArrayList<>();
            List<PLMActivity> activityList = new ArrayList<>();
            List<Person> persons = personRepository.findByIdIn(assignedToIds);
            for (Person person : persons) {
                if (person.getEmail() != null) {
                    if (type.equals("PROGRAM")) {
                        taskList = taskRepository.getPendingTaskByProjectIdsAndAssignedTo(ids, person.getId());
                        activityList = activityRepository.getPendingActivityByProjectIdsAndAssignedTo(ids, person.getId());
                    } else {
                        taskList = taskRepository.getTaskByProjectIdAndAssignedTo(id, person.getId());
                        activityList = activityRepository.getActivityByProjectIdAndAssignedTo(id, person.getId());
                    }
                    List<String> taskNames = new ArrayList<>();
                    List<String> activityNames = new ArrayList<>();
                    for (PLMTask plmTask : taskList) {
                        PLMActivity activity = activityMap.get(plmTask.getActivity());
                        PLMWbsElement wbsElement = wbsElementMap.get(activity.getWbs());
                        if (type.equals("PROGRAM")) {
                            taskNames.add(wbsElement.getProject().getName() + " / " + wbsElement.getName() + " / " + activity.getName() + " / " + plmTask.getName());
                        } else {
                            taskNames.add(wbsElement.getName() + " / " + activity.getName() + " / " + plmTask.getName());
                        }
                    }
                    for (PLMActivity activity : activityList) {
                        PLMWbsElement wbsElement = wbsElementMap.get(activity.getWbs());
                        if (type.equals("PROGRAM")) {
                            activityNames.add(wbsElement.getProject().getName() + " / " + wbsElement.getName() + " / " + activity.getName());
                        } else {
                            activityNames.add(wbsElement.getName() + " / " + activity.getName());
                        }
                    }

                    Collections.sort(taskNames, new Comparator<String>() {
                        public int compare(final String object1, final String object2) {
                            return object1.compareTo(object2);
                        }
                    });
                    Collections.sort(activityNames, new Comparator<String>() {
                        public int compare(final String object1, final String object2) {
                            return object1.compareTo(object2);
                        }
                    });
                    final String programNameFinal = programName;
                    final String projectNameFinal = projectName;
                    final Boolean projectTypeFinal = projectType;
                    final Boolean programTypeFinal = programType;
                    final List<String> taskNamesFinal = taskNames;
                    final List<String> activityNamesFinal = activityNames;
                    new Thread(() -> {
                        Map<String, Object> model = new HashMap<>();
                        model.put("host", host);
                        model.put("personName", person.getFullName());
                        model.put("programName", programNameFinal);
                        model.put("projectName", projectNameFinal);
                        model.put("tenantId", tenantId);
                        model.put("programTyp", programTypeFinal);
                        model.put("projectTyp", projectTypeFinal);
                        model.put("taskNames", taskNamesFinal);
                        model.put("activityNames", activityNamesFinal);
                        Mail mail = new Mail();
                        mail.setMailSubject("Activity / Task Notification");
                        mail.setTemplatePath("email/pendingTasksNotification.html");
                        mail.setModel(model);
                        mail.setMailTo(person.getEmail());
                        mailService.sendEmail(mail);
                    }).start();
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Person> getProgramProjectManagers(Integer program) {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = projectRepository.getProjectManagerIdsByProgram(program);
        if (integers.size() > 0) {
            list = personRepository.findByIdIn(integers);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<Person> getProgramProjectAssignedTos(Integer program) {
        List<Person> list = new ArrayList<>();
        List<Integer> integers = projectRepository.getProjectManagerIdsByProgram(program);
        List<Integer> projectIds = programProjectRepository.getProgramProjectIds(program);

        if (projectIds.size() > 0) {
            integers.addAll(activityRepository.getUniqueAssignedToByProjectIds(projectIds));
            integers.addAll(taskRepository.getUniqueAssignedToByProjectIds(projectIds));
            integers.addAll(milestoneRepository.getUniqueAssignedToByProjectIds(projectIds));
        }

        if (integers.size() > 0) {
            List<Integer> uniqueIds = integers.stream().distinct().collect(Collectors.toList());
            list = personRepository.findByIdIn(uniqueIds);
        }
        return list;
    }
}
