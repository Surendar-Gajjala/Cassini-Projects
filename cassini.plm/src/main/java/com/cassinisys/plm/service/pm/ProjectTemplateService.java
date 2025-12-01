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
import com.cassinisys.plm.filtering.ProjectTemplateCriteria;
import com.cassinisys.plm.filtering.ProjectTemplatePredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMObjectDocument;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.PLMSharedObject;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.model.rm.RecentlyVisited;
import com.cassinisys.plm.model.wf.PLMWorkflow;
import com.cassinisys.plm.model.wf.PLMWorkflowStatus;
import com.cassinisys.plm.model.wf.WorkflowStatusFlag;
import com.cassinisys.plm.model.wf.events.WorkflowEvents;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.rm.RecentlyVisitedRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowStartRepository;
import com.cassinisys.plm.service.UtilService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * Created by subramanyam reddy on 14-03-2018.
 */
@Service
public class ProjectTemplateService implements CrudService<ProjectTemplate, Integer> {

    @Autowired
    private ProjectTemplateRepository projectTemplateRepository;

    @Autowired
    private ProjectTemplateWbsRepository projectTemplateWbsRepository;

    @Autowired
    private ProjectTemplateActivityRepository templateActivityRepository;

    @Autowired
    private ProjectTemplateMilestoneRepository templateMilestoneRepository;

    @Autowired
    private ProjectTemplateTaskRepository templateTaskRepository;

    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired(required = true)
    private ProjectTemplatePredicateBuilder projectTemplatePredicateBuilder;
    @Autowired
    private ProjectTemplateMemberRepository projectTemplateMemberRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProgramTemplateProjectRepository programTemplateProjectRepository;
    @Autowired
    private ProjectTemplateFileRepository projectTemplateFileRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private SharedObjectRepository sharedObjectRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ProjectTemplateTaskFileRepository projectTemplateTaskFileRepository;
    @Autowired
    private ProjectTemplateActivityFileRepository projectTemplateActivityFileRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ProgramTemplateService programTemplateService;
    @Autowired
    private PLMWorkflowRepository workflowRepository;
    @Autowired
    private PLMWorkflowStartRepository plmWorkflowStatusRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ActivityFileRepository activityFileRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplate,'create')")
    public ProjectTemplate create(ProjectTemplate projectTemplate) {
        Integer programTemplateProject = projectTemplate.getProgramTemplateProject();
        ProjectTemplate existTemplate = null;
        Integer wfId = null;
        wfId = projectTemplate.getWorkflow();
        projectTemplate.setWorkflow(null);
        if (projectTemplate.getProgramTemplate() != null) {
            existTemplate = projectTemplateRepository.findByNameEqualsIgnoreCaseAndProgramTemplate(projectTemplate.getName(), projectTemplate.getProgramTemplate());
        } else {
            existTemplate = projectTemplateRepository.findByNameEqualsIgnoreCaseAndProgramTemplateIsNull(projectTemplate.getName());
        }
        if (existTemplate != null) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existTemplate.getName());
            throw new CassiniException(result);
        }
        projectTemplate = projectTemplateRepository.save(projectTemplate);
        if (wfId != null) {
            programTemplateService.attachWorkflow(projectTemplate.getId(), null, "PROJECTTEMPLATE", wfId);
        }
        if (programTemplateProject != null && projectTemplate.getProgramTemplate() != null) {
            ProgramTemplateProject templateProject = new ProgramTemplateProject();
            templateProject.setProjectTemplate(projectTemplate.getId());
            templateProject.setTemplate(projectTemplate.getProgramTemplate());
            templateProject.setParent(programTemplateProject);
            templateProject.setType(ProgramProjectType.PROJECT);
            templateProject = programTemplateProjectRepository.save(templateProject);
        }
        return projectTemplate;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplate.id ,'edit')")
    public ProjectTemplate update(ProjectTemplate projectTemplate) {
        ProjectTemplate existTemplate = null;
        if (projectTemplate.getProgramTemplate() != null) {
            existTemplate = projectTemplateRepository.findByNameEqualsIgnoreCaseAndProgramTemplate(projectTemplate.getName(), projectTemplate.getProgramTemplate());
        } else {
            existTemplate = projectTemplateRepository.findByNameEqualsIgnoreCaseAndProgramTemplateIsNull(projectTemplate.getName());
        }
        if (existTemplate != null && !existTemplate.getId().equals(projectTemplate.getId())) {
            String message = messageSource.getMessage("program_name_already_exist", null, "{0}: name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existTemplate.getName());
            throw new CassiniException(result);
        }
        projectTemplate = projectTemplateRepository.save(projectTemplate);
        return projectTemplate;

    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(id);
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        applicationEventPublisher.publishEvent(new WorkflowEvents.AttachedToObjectDeletedEvent(id));
        projectTemplateRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplate get(Integer id) {
        ProjectTemplate projectTemplate = projectTemplateRepository.findOne(id);
        PLMWorkflow workflow = workflowRepository.findByAttachedTo(projectTemplate.getId());
        if (workflow != null) {
            PLMWorkflowStatus status = plmWorkflowStatusRepository.findOne(workflow.getCurrentStatus());
            if (status != null) {
                projectTemplate.setWorkflowStatus(status.getName());
            }
            if (workflow.getStart() != null) {
                PLMWorkflowStatus workflowStatus = plmWorkflowStatusRepository.findOne(workflow.getStart().getId());
                if (workflowStatus != null && workflowStatus.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    projectTemplate.setStartWorkflow(true);
                }
            }
            if (workflow.getFinish() != null) {
                PLMWorkflowStatus workflowStatus1 = plmWorkflowStatusRepository.findOne(workflow.getFinish().getId());
                if (workflowStatus1 != null && workflowStatus1.getFlag().equals(WorkflowStatusFlag.COMPLETED)) {
                    projectTemplate.setFinishWorkflow(true);
                }
            }
        }
        return projectTemplate;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplate getProjectTemplateByName(String templateName) {
        ProjectTemplate projectTemplate = projectTemplateRepository.findByNameEqualsIgnoreCase(templateName);
        return projectTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplate> getAll() {
        return projectTemplateRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplate> getAllTemplatesProgramNull() {
        return projectTemplateRepository.findByProgramTemplateIsNull();
    }

    public Page<ProjectTemplate> getProjectTemplates(Pageable pageable, ProjectTemplateCriteria criteria) {
        Predicate predicate = projectTemplatePredicateBuilder.build(criteria, QProjectTemplate.projectTemplate);
        Page<ProjectTemplate> projectTemplates = projectTemplateRepository.findAll(predicate, pageable);
        return projectTemplates;
    }

    @Transactional(readOnly = true)
    public List<WBSDto> getProjectTemplateWbs(Integer templateId) {
        List<WBSDto> wbsDtoList = new ArrayList<>();
        List<ProjectTemplateWbs> templateWbsList = new ArrayList<>();
        List<ProjectTemplateWbs> wbsList = projectTemplateWbsRepository.findByTemplateOrderBySequenceNumberAsc(templateId);
        if (wbsList.size() != 0) {
            for (ProjectTemplateWbs templateWbs : wbsList) {
                WBSDto wbsDto = new WBSDto();
                wbsDto.setId(templateWbs.getId());
                wbsDto.setName(templateWbs.getName());
                wbsDto.setDescription(templateWbs.getDescription());
                wbsDto.setLevel(0);
                wbsDto.setCreatedBy(templateWbs.getCreatedBy());
                wbsDto.setModifiedBy(templateWbs.getModifiedBy());
                wbsDto.setCreatedDate(templateWbs.getCreatedDate());
                wbsDto.setSequenceNumber(templateWbs.getSequenceNumber());
                wbsDto.setObjectType(templateWbs.getObjectType().toString());
                List<WBSDto> children = new ArrayList<>();
                List<ProjectTemplateActivity> activities = templateActivityRepository.findByWbsOrderBySequenceNumberAsc(templateWbs.getId());
                if (activities.size() != 0) {
                    for (ProjectTemplateActivity templateActivity : activities) {
                        WBSDto activityDto = new WBSDto();
                        activityDto.setId(templateActivity.getId());
                        activityDto.setName(templateActivity.getName());
                        activityDto.setDescription(templateActivity.getDescription());
                        activityDto.setLevel(wbsDto.getLevel() + 1);
                        activityDto.setSequenceNumber(templateActivity.getSequenceNumber());
                        activityDto.setCreatedBy(templateActivity.getCreatedBy());
                        activityDto.setModifiedBy(templateActivity.getModifiedBy());
                        activityDto.setCreatedDate(templateActivity.getCreatedDate());
                        activityDto.setObjectType(templateActivity.getObjectType().toString());
                        activityDto.setParent(wbsDto.getId());
                        if (templateActivity.getAssignedTo() != null) {
                            activityDto.setPerson(personRepository.findOne(templateActivity.getAssignedTo()));
                        }
                        templateActivity.setLevel(templateWbs.getLevel() + 1);
                        List<ProjectTemplateTask> tasks = templateTaskRepository.findByActivity(templateActivity.getId());
                        templateActivity.setTemplateTaskList(tasks);
                        activityDto.setTemplateActivityTasks(tasks);
                        children.add(activityDto);
                    }
                    templateWbs.setHasBom(true);
                    templateWbs.setExpanded(true);
                    templateWbs.setTemplateActivities(activities);
                }
                List<ProjectTemplateMilestone> milestones = templateMilestoneRepository.findByWbsOrderBySequenceNumberAsc(templateWbs.getId());
                if (milestones.size() != 0) {
                    for (ProjectTemplateMilestone templateMilestone : milestones) {
                        WBSDto milestoneDto = new WBSDto();
                        milestoneDto.setId(templateMilestone.getId());
                        milestoneDto.setName(templateMilestone.getName());
                        milestoneDto.setDescription(templateMilestone.getDescription());
                        milestoneDto.setLevel(wbsDto.getLevel() + 1);
                        milestoneDto.setSequenceNumber(templateMilestone.getSequenceNumber());
                        milestoneDto.setCreatedBy(templateMilestone.getCreatedBy());
                        milestoneDto.setModifiedBy(templateMilestone.getModifiedBy());
                        milestoneDto.setObjectType(templateMilestone.getObjectType().toString());
                        milestoneDto.setParent(wbsDto.getId());
                        if (templateMilestone.getAssignedTo() != null) {
                            milestoneDto.setPerson(personRepository.findOne(templateMilestone.getAssignedTo()));
                        }
                        milestoneDto.setParentId(wbsDto.getId());
                        milestoneDto.setCreatedDate(templateMilestone.getCreatedDate());
                        templateMilestone.setLevel(templateWbs.getLevel() + 1);
                        children.add(milestoneDto);
                    }
                    templateWbs.setHasBom(true);
                    templateWbs.setExpanded(true);
                    templateWbs.setTemplateMilestones(milestones);
                }
                if (children.size() > 0) {
                    Collections.sort(children, new Comparator<WBSDto>() {
                        public int compare(final WBSDto object1, final WBSDto object2) {
                            if (object1.getSequenceNumber() != null && object2.getSequenceNumber() != null)
                                return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                            else return 0;
                        }
                    });
                    wbsDto.setHasBom(true);
                    wbsDto.setExpanded(true);
                    wbsDto.setChildren(children);
                }
                wbsDtoList.add(wbsDto);
                templateWbsList.add(templateWbs);
            }
            Collections.sort(wbsDtoList, new Comparator<WBSDto>() {
                public int compare(final WBSDto object1, final WBSDto object2) {
                    if (object1.getSequenceNumber() != null && object2.getSequenceNumber() != null)
                        return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                    else return 0;
                }
            });
        }
        return wbsDtoList;
    }

    @Transactional(readOnly = true)
    public ProjectTemplateWbs getTemplateWbsChildren(Integer templateId, Integer wbsId) {
        ProjectTemplateWbs wbsElement = projectTemplateWbsRepository.findOne(wbsId);
        if (wbsElement != null) {
            List<ProjectTemplateActivity> wbsActivities = templateActivityRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsActivities.size() != 0) {
                wbsElement.setHasBom(true);
                wbsElement.setLevel(0);
                for (ProjectTemplateActivity activity : wbsActivities) {
                    activity.setLevel(wbsElement.getLevel() + 1);
                }
                wbsElement.setTemplateActivities(wbsActivities);
            }
            List<ProjectTemplateMilestone> wbsMilestones = templateMilestoneRepository.findByWbsOrderByCreatedDateAsc(wbsElement.getId());
            if (wbsMilestones.size() != 0) {
                wbsElement.setHasBom(true);
                wbsElement.setLevel(0);
                for (ProjectTemplateMilestone milestone : wbsMilestones) {
                    milestone.setLevel(wbsElement.getLevel() + 1);
                }
                wbsElement.setTemplateMilestones(wbsMilestones);
            }

        }
        return wbsElement;
    }

    @Transactional(readOnly = true)
    public Page<ProjectTemplateMember> getProjectTemplateMembers(Integer templateId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        Page<ProjectTemplateMember> projectTemplateMembers = projectTemplateMemberRepository.findAllByTemplate(templateId, pageable);
        return projectTemplateMembers;

    }

    @Transactional
    public void deleteProjectTemplateMember(Integer template, Integer projectMemberId) {
        ProjectTemplateMember templateMember = projectTemplateMemberRepository.findByTemplateAndPerson(template, projectMemberId);
        projectTemplateMemberRepository.delete(templateMember);
    }

    @Transactional
    public List<ProjectTemplateMember> createProjectTemplateMembers(Integer templateId, List<ProjectTemplateMember> templateMembers) {
        return projectTemplateMemberRepository.save(templateMembers);
    }

    @Transactional
    public ProjectTemplateMember createProjectTemplateMember(Integer templateId, ProjectTemplateMember templateMember) {
        return projectTemplateMemberRepository.save(templateMember);
    }

    @Transactional
    public ProjectTemplateMember updateProjectTemplateMember(Integer templateId, ProjectTemplateMember templateMember) {
        return projectTemplateMemberRepository.save(templateMember);
    }

    @Transactional(readOnly = true)
    public List<Person> getAllProjectTemplateMembers(Integer project) {
        List<Person> persons = new ArrayList<>();
        List<ProjectTemplateMember> projectMembers = projectTemplateMemberRepository.findByTemplate(project);
        for (ProjectTemplateMember projectMember : projectMembers) {
            Person person = personRepository.findOne(projectMember.getPerson());
            if ((loginRepository.findByPersonId(person.getId()) != null)) {
                person.setActive(loginRepository.findByPersonId(person.getId()).getIsActive());
            }
            persons.add(person);
        }
        return persons;
    }

    @Transactional(readOnly = true)
    public DetailsCount getProjectTemplateDetails(Integer projectId) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setTeam(projectTemplateMemberRepository.findByTemplate(projectId).size());
        return detailsCount;
    }


    @Transactional
    public void deleteFolder(Integer projectId, Integer folderId) throws JsonProcessingException {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(projectId, folderId);
        List<ProjectTemplateFile> templateFiles = projectTemplateFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) templateFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        projectTemplateFileRepository.delete(folderId);
        if (file.getParentFile() != null) {
            ProjectTemplateFile parent = projectTemplateFileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = projectTemplateFileRepository.save(parent);
        }
    }


    public String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        ProjectTemplateFile projectFile = projectTemplateFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }


    public String getParentTaskFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMTaskFile projectFile = taskFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }


    public String getParentTaskTemplateFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        ProjectTemplateTaskFile projectFile = projectTemplateTaskFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (projectFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, projectFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + projectFile.getId();
        }
        return path;
    }


    public String getParentActivityFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (activityFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, activityFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + activityFile.getId();
        }
        return path;
    }


    public String getParentActivityTemplateFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        ProjectTemplateActivityFile templateActivityFile = projectTemplateActivityFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (templateActivityFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, templateActivityFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + templateActivityFile.getId();
        }
        return path;
    }


    @Transactional
    public ProjectTemplateFile createProjectTemplateFolder(Integer templateId, ProjectTemplateFile plmProjectFile) throws JsonProcessingException {
        plmProjectFile.setId(null);
        String folderNumber = null;
        ProjectTemplateFile existFolderName = null;
        if (plmProjectFile.getParentFile() != null) {
            existFolderName = projectTemplateFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndTemplateAndLatestTrue(plmProjectFile.getName(), plmProjectFile.getParentFile(), templateId);
            if (existFolderName != null) {
                PLMFile file = fileRepository.findOne(plmProjectFile.getParentFile());
                String message = messageSource.getMessage("folder_already_exist_folder", null, "{0} folder already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", plmProjectFile.getName(), file.getName());
                throw new CassiniException(result);
            }
        } else {
            existFolderName = projectTemplateFileRepository.findByNameEqualsIgnoreCaseAndTemplateAndLatestTrue(plmProjectFile.getName(), templateId);
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
        plmProjectFile = projectTemplateFileRepository.save(plmProjectFile);
        if (plmProjectFile.getParentFile() != null) {
            ProjectTemplateFile parent = projectTemplateFileRepository.findOne(plmProjectFile.getParentFile());
            parent.setModifiedDate(plmProjectFile.getModifiedDate());
            parent = projectTemplateFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(templateId, plmProjectFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return plmProjectFile;
    }


    @Transactional(readOnly = true)
    public List<FileDto> convertFilesIdsToDtoList(Integer object, PLMObjectType objectType, List<Integer> fileIds, Boolean hierarchy) {
        List<FileDto> filesDto = new ArrayList<>();
        List<ProjectTemplateFile> files = projectTemplateFileRepository.findByIdIn(fileIds);
        List<ProjectTemplateFile> plmFiles = projectTemplateFileRepository.getParentChildrenByFileType(fileIds, "FILE");
        List<String> fileNos = projectTemplateFileRepository.getFileNosByIds(fileIds);
        List<ProjectTemplateFile> fileNoFiles = projectTemplateFileRepository.findByFileNosOrderByIdAsc(fileNos);
        List<Integer> personIds = fileRepository.getCreatedByIds(fileIds);
        personIds.addAll(fileRepository.getModifiedByIds(fileIds));
        fileNoFiles.forEach(plmFile1 -> {
            personIds.add(plmFile1.getCreatedBy());
        });
        List<Integer> filterIds = personIds.stream().distinct().collect(Collectors.toList());
        List<Person> persons = personRepository.findByIdIn(filterIds);
        List<Login> logins = loginRepository.getLoginsByPersonIds(filterIds);
        List<ProjectTemplateFile> fileCountList = projectTemplateFileRepository.getChildrenCountByParentFileAndLatestTrueByIds(fileIds);
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
        List<Integer> foldersList = projectTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FOLDER");
        List<Integer> filesList = projectTemplateFileRepository.getByParentFileAndLatestTrueAndFileType(fileDto.getId(), "FILE");
        if (foldersList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, foldersList, hierarchy));
        }
        if (filesList.size() > 0) {
            fileDto.getChildren().addAll(convertFilesIdsToDtoList(object, objectType, filesList, hierarchy));
        }
        return fileDto;
    }

}
