package com.cassinisys.is.service.pm;

import com.cassinisys.is.filtering.ProjectCriteria;
import com.cassinisys.is.filtering.ProjectPersonCriteria;
import com.cassinisys.is.filtering.ProjectPersonPredicateBuilder;
import com.cassinisys.is.filtering.ProjectPredicateBuilder;
import com.cassinisys.is.model.col.ISProjectMeeting;
import com.cassinisys.is.model.col.ISProjectMessage;
import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.dm.DocType;
import com.cassinisys.is.model.dm.ISProjectDocument;
import com.cassinisys.is.model.dm.ISProjectFolder;
import com.cassinisys.is.model.im.ISIssue;
import com.cassinisys.is.model.pm.*;
import com.cassinisys.is.model.pm.QISProject;
import com.cassinisys.is.model.pm.QISProjectPerson;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.model.tm.*;
import com.cassinisys.is.repo.col.ProjectMeetingRepository;
import com.cassinisys.is.repo.col.ProjectMessageRepository;
import com.cassinisys.is.repo.dm.ProjectDocumentRepository;
import com.cassinisys.is.repo.dm.ProjectFolderRepository;
import com.cassinisys.is.repo.im.IssueRepository;
import com.cassinisys.is.repo.pm.*;
import com.cassinisys.is.repo.procm.BoqItemRepository;
import com.cassinisys.is.repo.procm.MaterialItemRepository;
import com.cassinisys.is.repo.procm.ProjectBoqRepository;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.repo.tm.ProjectTaskRepository;
import com.cassinisys.is.repo.tm.ProjectTeamRepository;
import com.cassinisys.is.service.login.LoginService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.col.MediaType;
import com.cassinisys.platform.model.col.MeetingAttendee;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import com.cassinisys.platform.repo.col.MeetingAttendeeRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ProjectService
 */
@Service
@Transactional
public class ProjectService implements CrudService<ISProject, Integer>,
        PageableService<ISProject, Integer> {

    private volatile List<ProjectMaterialReportDTO> reportDTOs = new ArrayList();
    @Autowired
    private ISProjectPersonRepository projectPersonRepository;
    @Autowired
    private MaterialItemRepository materialItemRepository;
    @Autowired
    private ProjectPersonPredicateBuilder projectPersonPredicateBuilder;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private ProjectMessageRepository projectMessageRepository;
    @Autowired
    private MeetingAttendeeRepository meetingAttendeeRepository;
    @Autowired
    private ProjectFolderRepository projectFolderRepository;
    @Autowired
    private ProjectDocumentRepository projectDocumentRepository;
    @Autowired
    private ProjectMeetingRepository projectMeetingRepository;
    @Autowired
    private ProjectWbsRepository projectWbsRepository;
    @Autowired
    private ISProjectPersonRepository isProjectPersonRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ISProjectRoleRepository isProjectRoleRepository;
    @Autowired
    private ISPersonRoleRepository isPersonRoleRepository;
    @Autowired
    private ProjectTeamRepository projectTeamRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ProjectBoqRepository projectBoqRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private BoqItemRepository itemRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private WbsRepository wbsRepository;
    @Autowired
    private BoqItemRepository boqItemRepository;
    @Autowired
    private ProjectPredicateBuilder projectPredicateBuilder;
    @Autowired
    private ExportService exportService;
    @Autowired
    private ISTStockIssueItemRepository istStockIssueItemRepository;
    @Autowired
    private ISStockReceiveItemRepository isStockReceiveItemRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private ISStockReceiveRepository stockReceiveRepository;
    @Autowired
    private ISStockIssueRepository stockIssueRepository;
    @Autowired
    private ISStockReturnItemRepository stockReturnItemRepository;
    @Autowired
    private ISStockReturnRepository stockReturnRepository;
    @Autowired
    private ISTopInventoryRepository topInventoryRepository;

    @Autowired
    private ProjectPersonOrganizationRepository projectPersonOrganizationRepository;

    @Autowired
    private ProjectPersonRoleRepository projectPersonRoleRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ProjectSiteRepository projectSiteRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * The method used to create ISProject
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProject create(ISProject project) {
        checkNotNull(project);
        project.setId(null);
        project = projectRepository.save(project);
        return project;
    }

    /**
     * The method used to createResources for the list of ISProject
     **/
    @Transactional(readOnly = false)
    public List<ISProjectResource> createResources(List<ISProjectResource> resources) {
        return resourceRepository.save(resources);
    }

    /**
     * The method used to updateResources for the list of ISProjectResource
     **/
    @Transactional(readOnly = false)
    public ISProjectResource updateResources(ISProjectResource resource) {
        return resourceRepository.save(resource);
    }

    /**
     * The method used to getResources for the list of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<ISProjectResource> getResources(Integer projectId, Integer taskId) {
        List<ISProjectResource> projectResources = new ArrayList<>();
        projectResources = resourceRepository.findByProjectAndTaskAndResourceType(projectId, taskId, ResourceType.MATERIALTYPE);
        List<ISProjectResource> projectResources1 = resourceRepository.findByProjectAndTaskAndResourceType(projectId, taskId, ResourceType.MACHINETYPE);
        for (ISProjectResource projectResource : projectResources1) {
            projectResources.add(projectResource);
        }
        return projectResources;
    }

    /**
     * The method used to deleteResource
     **/
    @Transactional(readOnly = false)
    public void deleteResource(Integer resourceId) {
        checkNotNull(resourceId);
        ISProjectResource resource = resourceRepository.findOne(resourceId);
        if (resource == null) {
            throw new ResourceNotFoundException();
        }
        resourceRepository.delete(resource);
    }

    /**
     * The method used to getMutlipleByIds for the list within the list of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<List<ISProjectResource>> getMultipleByIds(Integer projectId, List<Integer> taskIds, ResourceType resourceType) {
        List<List<ISProjectResource>> isProjectResources = new ArrayList<List<ISProjectResource>>();
        for (Integer taskId : taskIds) {
            List<ISProjectResource> isProjectResource1 = resourceRepository.findByProjectAndTaskAndResourceType(projectId, taskId, resourceType);
            isProjectResources.add(isProjectResource1);
        }
        return isProjectResources;
    }

    /**
     * The method used to getResourcesByTasks for the list  of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<ISProjectResource> getMultipleResourcesByTasks(Integer projectId, List<Integer> taskIds) {
        return resourceRepository.findByProjectAndTaskIn(projectId, taskIds);
    }

    @Transactional(readOnly = true)
    public List<ISProjectResource> getResourcesByProject(Integer projectId) {
        return resourceRepository.findByProject(projectId);
    }

    /**
     * The method used to getResourcesByType for the list  of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<ISProjectResource> getResourcesByType(Integer projectId, Integer taskId, ResourceType resourceType) {
        return resourceRepository.findByProjectAndTaskAndResourceType(projectId, taskId, resourceType);
    }

    @Transactional(readOnly = true)
    public List<ISProjectResource> getProjectResourcesByTypes(Integer projectId, Integer taskId, List<ResourceType> resourceTypes) {
        List<ISProjectResource> isProjectResources = new ArrayList<>();
        resourceTypes.forEach(resourceType -> {
            isProjectResources.addAll(resourceRepository.findByProjectAndTaskAndResourceType(projectId, taskId, resourceType));
        });
        return isProjectResources;
    }

    /**
     * The method used to getResources of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public List<ISProjectResource> getResourcesByIds(List<Integer> resourceIds) {
        return resourceRepository.findByIdIn(resourceIds);
    }

    /**
     * The method used to getItemAvailableQuantities of ISProjectResource
     **/
    @Transactional(readOnly = true)
    public Map<Integer, Double> getItemAvailableQuantities(Integer projectId, List<Integer> refIds) {
        Map<Integer, Double> map = new HashMap<>();
        List<ISBoqItem> boqItems = boqItemRepository.findByIdIn(refIds);
        for (ISBoqItem boqItem : boqItems) {
            List<ISProjectResource> resources = resourceRepository.findByProjectAndReferenceIdAndResourceType(projectId, boqItem.getId(), boqItem.getItemType());
            for (ISProjectResource resource : resources) {
                Integer refId = resource.getReferenceId();
                Double qty = map.get(refId);
                if (qty == null) {
                    map.put(refId, resource.getQuantity());
                } else {
                    map.put(refId, qty + resource.getQuantity());
                }
            }
        }
        return map;
    }

    @Transactional(readOnly = false)
    public ISProjectPerson updateProjectPerson(ISProjectPerson projectPerson) {
        checkNotNull(projectPerson);
        projectPerson = projectPersonRepository.save(projectPerson);
        return projectPerson;
    }

    /**
     * The method used to update  ISProject
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProject update(ISProject project) {
        checkNotNull(project);
        checkNotNull(project.getId());
        ISProject previous = projectRepository.findOne(project.getId());
        if (previous == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project1 = projectRepository.findByName(project.getName());
        if (project1 != null && project1.getId() != project.getId()) {
            throw new RuntimeException("Name already exists");
        }
        project = projectRepository.save(project);
        return project;
    }

    /**
     * The method used to delete project of ISProject
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProject project = projectRepository.findOne(id);
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectRepository.delete(project);

    }

    /**
     * The method used to get ISProject
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProject get(Integer id) {
        checkNotNull(id);
        ISProject project = projectRepository.findOne(id);
        List<ISProjectTask> projectTask = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        if (login.getIsSuperUser()) {
            projectTask = projectTaskRepository.findByProject(id);
        } else {
            projectTask = projectTaskRepository.getByPerson(login.getPerson().getId());
        }
        if (projectTask.size() > 0) {
            Integer pendingTasks = 0;
            Integer finishedTasks = 0;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = formatter.format(date);
            try {
                date = formatter.parse(strDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            project.setTotalTasks(projectTask.size());
            for (ISProjectTask projectTask1 : projectTask) {
                if (projectTask1.getPlannedFinishDate().before(date)) {
                    pendingTasks++;

                }
                if (projectTask1.getStatus().equals(TaskStatus.FINISHED)) {
                    finishedTasks++;
                }
                project.setFinishedTasks(finishedTasks);
                project.setPendingTasks(pendingTasks);
            }
            if (project == null) {
                throw new ResourceNotFoundException();
            }
        }
        return project;
    }

    @Transactional(readOnly = true)
    public List<ISProject> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ISProject> getProjectsByIds(List<Integer> projectIds) {
        return projectRepository.findByIdIn(projectIds);
    }

    /**
     * The method used to getAll for the list of ISProject
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProject> getAll() {
        return projectRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISProject
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISProject> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));

        }
        List<ISProject> projects = projectRepository.findAll();
        for (ISProject project : projects) {
            calculateProjectPercent(project);
        }
        Page<ISProject> projectPageable = new PageImpl<ISProject>(projects, pageable, projects.size());
        return projectPageable;
    }

    public void calculateProjectPercent(ISProject project) {
        double percentComplete = 0.0;
        double weightage = 0.0;
        List<ISWbs> wbses = wbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project.getId());
        for (ISWbs wbs : wbses) {
            weightage = weightage + wbs.getWeightage();
            double var = wbs.getWeightage() * wbs.getPercentageComplete();
            percentComplete = percentComplete + (var / 100);
        }
        if (wbses.size() > 0 && weightage > 0) {
            percentComplete = (percentComplete * 100) / weightage;
            percentComplete = Math.round(percentComplete);
        }
        project.setPercentComplete(percentComplete);
    }

    @Transactional(readOnly = true)
    public List<ISProject> getUserProjects(Integer userId) {
        List<ISProjectPerson> projectPersons = isProjectPersonRepository.findByPerson(userId);
        List<Integer> projects = new ArrayList<>();
        for (ISProjectPerson projectPerson : projectPersons) {
            projects.add(projectPerson.getProject());
        }
        return projectRepository.findByIdIn(projects);
    }

    @Transactional(readOnly = true)
    public ISProject findProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<ISProject> findProjectByPortfolio(Integer portfolio) {
        List<ISProject> projects = projectRepository.findByPortfolio(portfolio);
        for (ISProject project : projects) {
            calculateProjectPercent(project);
        }
        return projects;
    }

    /**
     * The method used to getTasks for the page of ISProjectTask
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectTask> getTasks(Integer projectId, Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        if (projectRepository.findOne(projectId) == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "plannedStartDate")));

        }
        return projectTaskRepository.findByProject(projectId, pageable);
    }

    /**
     * The method used to getMessages for the page of ISProjectMessage
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectMessage> getMessages(Integer projectId,
                                              Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        if (projectRepository.findOne(projectId) == null) {
            throw new ResourceNotFoundException();
        }
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "sentDate")));
        }
        return projectMessageRepository.findByProject(projectId, pageable);
    }

    /**
     * The method used to getRootFolders for the list of ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public List<ISProjectFolder> getRootFolders(Integer projectId, DocType folderType) {
        checkNotNull(projectId);
        checkNotNull(folderType);
        if (projectRepository.findOne(projectId) == null) {
            throw new ResourceNotFoundException();
        }
        return projectFolderRepository.findRootFolders(projectId, folderType);
    }

    /**
     * The method used to getAllProjectFolders for the list of ISProjectFolder
     **/
    @Transactional(readOnly = true)
    public List<ISProjectFolder> getAllProjectFolders(Integer projectId, DocType folderType) {
        checkNotNull(projectId);
        checkNotNull(folderType);
        if (projectRepository.findOne(projectId) == null) {
            throw new ResourceNotFoundException();
        }
        List<ISProjectFolder> parents = projectFolderRepository.findRootFolders(projectId, folderType);
        visitFoldersTree(parents);
        return parents;
    }

    /**
     * The method used to visitFoldersTree
     **/
    private void visitFoldersTree(List<ISProjectFolder> parents) {
        for (ISProjectFolder folder : parents) {
            List<ISProjectFolder> children = projectFolderRepository.findByParentOrderByCreatedDateAsc(folder.getId());
            visitFoldersTree(children);
            for (ISProjectFolder child : children) {
                folder.getChildren().add(child);
            }
        }
    }

    /**
     * The method used to getDocuments for the list of ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public List<ISProjectDocument> getDocuments(Integer projectId) {
        checkNotNull(projectId);
        return projectDocumentRepository.findByProjectAndLatestTrueOrderByModifiedDateDesc(projectId);
    }

    /**
     * The method used to getDocument for  ISProjectDocument
     **/
    @Transactional(readOnly = true)
    public ISProjectDocument getDocument(Integer documentId) {
        checkNotNull(documentId);
        return projectDocumentRepository.findOne(documentId);
    }

    /**
     * The method used to getMeetings for the page of  ISProjectMeeting
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectMeeting> getMeetings(Integer projectId,
                                              Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "createdDate")));
        }
        Page<ISProjectMeeting> projectMeetings = projectMeetingRepository.findByProject(projectId, pageable);
        for (ISProjectMeeting projectMeeting : projectMeetings.getContent()) {
            List<MeetingAttendee> attendees = meetingAttendeeRepository.findByMeetingId(projectMeeting.getId());
            List<Person> persons = new ArrayList<>();
            attendees.forEach(a -> {
                Person p = personService.get(a.getId().getPersonId());
                if (p != null) {
                    persons.add(p);
                }
            });
            projectMeeting.setAttendees(persons);
        }
        return projectMeetings;
    }

    /**
     * The method used to getRootWbs for the list of ISProjectWbs
     **/
    @Transactional(readOnly = true)
    public List<ISProjectWbs> getRootWbs(Integer projectId) {
        checkNotNull(projectId);
        return projectWbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(projectId);
    }

    /**
     * The method used to getAllWbs for the list of ISProjectWbs
     **/
    @Transactional(readOnly = false)
    public List<ISProjectWbs> getAllWbs(Integer projectId) {
        checkNotNull(projectId);
        List<ISProjectWbs> roots = projectWbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(projectId);
        visitWbsTree(roots);
        return roots;
    }

    public void calculateWbsPercentage(Integer wbsId) {
        ISProjectWbs wbs = projectWbsRepository.findOne(wbsId);
        if (wbs.getParent() == null) {
            calculatePercentComplete(wbs);
        } else {
            ISProjectWbs wbs1 = projectWbsRepository.findOne(getRootParent(wbs));
            calculatePercentComplete(wbs1);
        }
    }

    public Integer getRootParent(ISProjectWbs wbs) {
        Integer parentId = 0;
        if (wbs.getParent() != null) {
            parentId = wbs.getParent();
            getRootParent(projectWbsRepository.findOne(parentId));
        } else {
            parentId = wbs.getId();
        }
        return parentId;
    }

    /**
     * The method used to calculatePercentComplete ISProjectWbs
     **/
    private void calculatePercentComplete(ISProjectWbs wbsParent) {
        double percent = 0;
        double weightage = 0.0;
        List<ISProjectWbs> children = projectWbsRepository.findByParentOrderByCreatedDateAsc(wbsParent.getId());
        if (children.size() == 0) {
            List<ISProjectTask> projectTasks = projectTaskRepository.findByWbsItem(wbsParent.getId());
            if (projectTasks.size() > 0) {
                calculateWbsPercent(wbsParent);
            }

        } else {
            for (ISProjectWbs child : children) {
                calculateWbsPercent(child);
                weightage += child.getWeightage();
                percent += (child.getPercentageComplete() * child.getWeightage()) / 100;
            }
        }
        if (weightage > 0) {
            percent = percent / weightage;
            percent = (percent * 100);
            percent = Math.round(percent);
            wbsParent.setPercentageComplete(percent);
            if (wbsParent.getParent() != null) {
                ISWbs wbs = wbsRepository.findOne(wbsParent.getParent());
                if (wbs != null) {
                    wbs.setPercentageComplete(wbs.getPercentageComplete() + wbsParent.getPercentageComplete());
                }
            }
        }

    }

    public double calculateWbsPercent(ISProjectWbs wbs) {
        double percent = 0;
        double weightage = 0.0;
        List<ISProjectTask> projectTasks = projectTaskRepository.findByWbsItem(wbs.getId());
        for (ISProjectTask task : projectTasks) {
            if (task.getInspectionResult() == null || task.getInspectionResult().equals(InspectionResult.ACCEPTED)) {
                weightage += wbs.getWeightage();
                percent += (task.getPercentComplete() * wbs.getWeightage()) / 100;
            }
        }
        if (percent > 0) {
            percent = (percent * 100) / weightage;
        }
        percent = Math.round(percent);
        wbs.setPercentageComplete(percent);
        wbsRepository.save(wbs);
        return percent;
    }

    /**
     * The method used to visitWbsTree
     **/
    private void visitWbsTree(List<ISProjectWbs> parents) {
        for (ISProjectWbs wbs : parents) {
            List<ISProjectWbs> children = projectWbsRepository.findByParentOrderByCreatedDateAsc(wbs.getId());
            Integer parent = wbs.getParent();
            visitWbsTree(children);
            wbs.getChildren().addAll(children);
        }
    }

    /**
     * The method used to findMultiple for the list of ISProject
     **/
    @Transactional(readOnly = true)
    public List<ISProject> findMultiple(List<Integer> ids) {
        return projectRepository.findByIdIn(ids);
    }

    /**
     * The method used to getIssues for the page of ISIssue
     **/
    @Transactional(readOnly = true)
    public Page<ISIssue> getIssues(Integer projectId, Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("modifiedDate")));
        }
        return issueRepository.findByTargetObjectIdAndTargetObjectType(projectId,
                ObjectType.valueOf(ISObjectType.PROJECT.name()), pageable);
    }

    /**
     * The method used to getBoqs for the list of ISProjectBoq
     **/
    @Transactional(readOnly = true)
    public List<ISProjectBoq> getBoqs(Integer id) {
        checkNotNull(id);
        return projectBoqRepository.findByProject(id);
    }

    /**
     * The method used to getRfqs for the page of ISProjectRfq
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectRfq> getRfqs(Integer id, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    /**
     * The method used to getTeam for the page of ISProjectTeam
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectTeam> getTeam(Integer projectId, Pageable pageable) {
        checkNotNull(projectId);
        checkNotNull(pageable);
        return projectTeamRepository.findByProjectId(projectId, pageable);
    }

    /**
     * The method used to createProjectPersons for the list of  ISProjectPerson
     **/
    @Transactional(readOnly = false)
    public List<ISProjectPerson> createProjectPersons(List<ISProjectPerson> projectPersons) {
        return isProjectPersonRepository.save(projectPersons);
    }

    /**
     * The method used to getProjectPersons for the list of  ISProjectPerson
     **/
    @Transactional(readOnly = true)
    public List<ISProjectPerson> getProjectPersons(Integer projectId) {
        List<ISProjectPerson> team = isProjectPersonRepository.findByProject(projectId);
        List<ISProjectPerson> projectPersons = new ArrayList<>();
        for (ISProjectPerson person : team) {
            Person person1 = personRepository.findOne(person.getPerson());
            if (person1.getObjectType() == ObjectType.valueOf("PERSON")) {
                Login login = loginService.getByPersonId(person1.getId());
                if (login != null && login.getIsActive() == true) {
                    projectPersons.add(person);
                } else {
                    projectPersons.add(person);
                }
            }
        }
        return projectPersons;
    }

    /**
     * The method used to getPagedPersons for the page of ISProjectPerson
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectPerson> getPagedPersons(Integer projectId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("rowId")));
        }
        return isProjectPersonRepository.findByProject(projectId, pageable);
    }

    /**
     * The method used to deleteProjectPerson
     **/
    @Transactional(readOnly = false)
    public void deleteProjectPerson(Integer projectId, Integer personId) {
        checkNotNull(personId);
        ISProjectPerson person = isProjectPersonRepository.findByProjectAndPerson(projectId, personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        } else {
            List<ISPersonRole> personRoles = isPersonRoleRepository.findByProjectAndPerson(projectId, personId);
            if (personRoles.size() > 0) {
                isPersonRoleRepository.delete(personRoles);
            }
            isProjectPersonRepository.delete(person);
        }
    }

    /**
     * The method used to createPersonRoles for the list of ISPersonRole
     **/
    @Transactional(readOnly = false)
    public List<ISPersonRole> createPersonRoles(List<ISPersonRole> personRoles) {
        return isPersonRoleRepository.save(personRoles);
    }

    /**
     * The method used to updatePersonRole for the list of ISPersonRole
     **/
    @Transactional(readOnly = false)
    public ISPersonRole updatePersonRole(ISPersonRole personRole) {
        checkNotNull(personRole.getPerson());
        personRole.setPerson(personRole.getPerson());
        return isPersonRoleRepository.save(personRole);
    }

    /**
     * The method used to deletePersonRole
     **/
    @Transactional(readOnly = false)
    public void deletePersonRole(Integer rowId) {
        checkNotNull(rowId);
        ISPersonRole personRole = isPersonRoleRepository.findByRowId(rowId);
        if (personRole == null) {
            throw new ResourceNotFoundException();
        }
        isPersonRoleRepository.delete(personRole);
    }

    /**
     * The method used to findRolesByPerson for the list of ISPersonRole
     **/
    @Transactional(readOnly = true)
    public List<ISPersonRole> findRolesByPerson(Integer projectId, Integer personId) {
        return isPersonRoleRepository.findByProjectAndPerson(projectId, personId);
    }

    @Transactional(readOnly = true)
    public List<ISPersonRole> findPersonsByRole(Integer projectId, Integer roleId) {
        return isPersonRoleRepository.findByProjectAndRole(projectId, roleId);
    }

    /**
     * The method used to findPersonsRoles for the list of ISPersonRole
     **/
    @Transactional(readOnly = true)
    public Map<Integer, List<ISPersonRole>> findPersonsRoles(Integer projectId, Integer[] ids) {
        Map<Integer, List<ISPersonRole>> iMap = new HashMap<>();
        for (Integer id : ids) {
            List<ISPersonRole> personRoles = isPersonRoleRepository.findByProjectAndPerson(projectId, id);
            iMap.put(id, personRoles);

        }
        return iMap;
    }

    /**
     * The method used to createRole for the list of ISProjectRole
     **/
    @Transactional(readOnly = false)
    public ISProjectRole createRole(ISProjectRole projectRole) {
        ISProjectRole isProjectRole = isProjectRoleRepository.findByProjectAndRoleIgnoreCase(projectRole.getProject(), projectRole.getRole());
        ISProjectRole isProjectRole1 = new ISProjectRole();
        if (isProjectRole != null) {
            throw new CassiniException("Role already exist's please check");
        } else {
            isProjectRole1 = isProjectRoleRepository.save(projectRole);
        }
        return isProjectRole1;
    }

    /**
     * The method used to getRoles for the list of ISProjectRole
     **/
    @Transactional(readOnly = true)
    public List<ISProjectRole> getRoles(Integer projectId) {
        return isProjectRoleRepository.findByProject(projectId);
    }

    /**
     * The method used to getRole for ISProjectRole
     **/
    @Transactional(readOnly = true)
    public ISProjectRole getRole(Integer id) {
        return isProjectRoleRepository.findById(id);
    }

    /**
     * The method used to deleteProjectRole
     **/
    @Transactional(readOnly = false)
    public void deleteProjectRole(Integer id) {
        checkNotNull(id);
        ISProjectRole projectRole = isProjectRoleRepository.findOne(id);
        if (projectRole == null) {
            throw new ResourceNotFoundException();
        }
        isProjectRoleRepository.delete(projectRole);
    }

    /**
     * The method used to getPagedRoles for the page of ISProjectRole
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectRole> getPagedRoles(Integer project, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return isProjectRoleRepository.findAllByProject(project, pageable);
    }

    /**
     * The method used to findMultipleRoles for the list of ISProjectRole
     **/
    @Transactional(readOnly = true)
    public List<ISProjectRole> findMultipleRoles(List<Integer> ids) {
        return isProjectRoleRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public ISBoqItem getBoqItem(Integer boqItemId) {
        return itemRepository.findOne(boqItemId);
    }

    public List<ObjectTypeAttribute> getProjectAttributesRequiredFalse(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectTypeAndRequiredFalse(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getProjectAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        for (ObjectTypeAttribute attribute : typeAttributes) {
            String attrName = attribute.getName().replace(" ", "");
            if ("planhead".equalsIgnoreCase(attrName)) {
                attribute.setLov(lovRepository.findByName("Project Type"));
            }
        }
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByProjectIdsAndAttributeIds(Integer[] projectIds, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();
        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(projectIds, objectAttributeIds);
        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }
            objectAttributes.add(attribute);
        }
        return objectAttributesMap;

    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getRequiredProjectAttributes(String objectType) {
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return objectTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ISProjectPerson> getProjectPerson(Integer personId) {
        return isProjectPersonRepository.findByPerson(personId);
    }

    @Transactional(readOnly = true)
    public List<ISBoqItem> getBoqItemByItemNumber(String boqId) {
        return boqItemRepository.findByItemNumber(boqId);
    }

    @Transactional(readOnly = false)
    public ISProject getProjectByBoqId(Integer boqId) {
        ISBoqItem boqItem = boqItemRepository.findOne(boqId);
        ISProject project = projectRepository.findOne(boqItem.getProject());
        return project;
    }

    public List<ISProject> searchProjects(ProjectCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Predicate predicate = null;
        if (!criteria.getSearchQuery().equals(null))
            predicate = projectPredicateBuilder.build(criteria, QISProject.iSProject);
        //converting Iterator to List
        List<ISProject> projects = Lists.newArrayList(projectRepository.findAll(predicate));
        return projects;

    }

    public Page<ISProject> freeTextSearch(Pageable pageable, ProjectCriteria criteria) {
        Predicate predicate = projectPredicateBuilder.build(criteria, QISProject.iSProject);
        return projectRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    private Object getAttributeValueByNameAndType(Integer id, String name, ObjectType anEnum) {
        ObjectTypeAttribute challanNumberAttribute = objectTypeAttributeRepository.findByNameAndObjectType(name, anEnum);
        ObjectAttribute value = new ObjectAttribute();
        if (challanNumberAttribute == null) {
            value.setStringValue("");
        } else {
            value = objectAttributeRepository.findByObjectIdAndAttributeDefId(id, challanNumberAttribute.getId());
            if (value == null) {
                return "";
            }
        }
        return value.getStringValue();
    }

    /**
     * The method used to get project material report results
     **/
    @Transactional(readOnly = true)
    public List<ProjectMaterialReportDTO> getMaterialReportByProjectRecords(Integer project) {
        reportDTOs.clear();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ISBoqItem> boqItems = boqItemRepository.findByProjectAndItemType(project, ResourceType.MATERIALTYPE);
        Map<String, List<ISBoqItem>> isBoqItemMap = boqItems.stream()
                .collect(Collectors.groupingBy(ISBoqItem::getItemNumber));
        Set<String> itemNumbers = boqItems.stream().map(ISBoqItem::getItemNumber).collect(Collectors.toSet());
        List<ISMaterialItem> materialItems = materialItemRepository.findByItemNumberIn(new ArrayList(itemNumbers));
        Integer serialNum = 1;
        for (int i = 0; i < materialItems.size(); i++) {
            ISMaterialItem materialItem = materialItems.get(i);
            List<ISTStockIssueItem> issMovements = istStockIssueItemRepository.findByMovementTypeAndItem(MovementType.ISSUED, materialItem.getId());
            List<ISStockReceiveItem> recMovements = isStockReceiveItemRepository.findByMovementTypeAndItem(MovementType.RECEIVED, materialItem.getId());
            List<ISStockReturnItem> retMovements = stockReturnItemRepository.findByItemAndMovementType(materialItem.getId(), MovementType.RETURNED);
            ISTStockIssueItem issMovement = null;
            ISStockReceiveItem recMovement = null;
            ISStockReturnItem retMovement = null;
            Double balanceQty = 0.0;
            Double consumeQty = 0.0;
            Double totalReturnQty = 0.0;
            Double totalSuppliedQty = 0.0;
            Boolean hasRow = false;
            Integer noOfRows = 0;
            noOfRows = recMovements.size() > issMovements.size() ? (recMovements.size() > retMovements.size() ? recMovements.size() : retMovements.size()) : (issMovements.size() > retMovements.size() ? issMovements.size() : retMovements.size());
            Double boqQty = isBoqItemMap.get(materialItem.getItemNumber()).stream().mapToDouble(k -> k.getQuantity()).sum();
            List<ProjectMaterialReportDTO> reportDTOs2 = new ArrayList();
            if (issMovements.size() > 0 || recMovements.size() > 0 || retMovements.size() > 0) {
                reportDTOs2.add(new ProjectMaterialReportDTO(materialItem.getItemNumber(), materialItem.getItemName(), materialItem.getUnits(),
                        boqQty.toString(), "", "", "", "", "", "", "", "", "", "", Boolean.FALSE, noOfRows));
                serialNum++;
            }
            for (int j = 0; (j < issMovements.size() || j < recMovements.size() || j < retMovements.size()); j++) {
                String issueChallan = "";
                String receiveChallan = "";
                String returnChallan = "";
                Double returnQty = 0.0;
                hasRow = true;
                if (issMovements.size() > j && recMovements.size() > j && retMovements.size() > j) {
                    issMovement = issMovements.get(j);
                    recMovement = recMovements.get(j);
                    totalSuppliedQty += recMovement.getQuantity();
                    retMovement = retMovements.get(j);
                    returnQty = retMovement.getQuantity();
                    totalReturnQty += retMovement.getQuantity();
                    consumeQty += issMovement.getQuantity().doubleValue();
                    ISStockReceiveItem receiveItem = isStockReceiveItemRepository.findOne(recMovement.getId());
                    if (receiveItem != null) {
                        ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                        receiveChallan = receive.getReceiveNumberSource();
                    }
                    ISTStockIssueItem stockIssueItem = istStockIssueItemRepository.findOne(issMovement.getId());
                    if (stockIssueItem != null) {
                        ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                        issueChallan = issue.getIssueNumberSource();
                    }
                    ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(retMovement.getId());
                    if (stockReturnItem != null) {
                        ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                        returnChallan = stockReturn.getReturnNumberSource();
                    }
                    reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", recMovement.getQuantity().toString(), receiveChallan, formatter.format(recMovement.getTimeStamp()),
                            issMovement.getQuantity().toString(), issueChallan, formatter.format(issMovement.getTimeStamp()), returnQty.toString(),
                            returnChallan, formatter.format(retMovement.getTimeStamp()), "", Boolean.TRUE, 0));
                } else {
                    if (recMovements.size() > j && issMovements.size() > j) {
                        recMovement = recMovements.get(j);
                        issMovement = issMovements.get(j);
                        ISStockReceiveItem receiveItem = isStockReceiveItemRepository.findOne(recMovement.getId());
                        if (receiveItem != null) {
                            ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                            receiveChallan = receive.getReceiveNumberSource();
                            totalSuppliedQty += receiveItem.getQuantity().doubleValue();
                        }
                        ISTStockIssueItem stockIssueItem = istStockIssueItemRepository.findOne(issMovement.getId());
                        if (stockIssueItem != null) {
                            ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                            issueChallan = issue.getIssueNumberSource();
                        }
                        consumeQty += issMovement.getQuantity().doubleValue();
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", recMovement.getQuantity().toString(), receiveChallan, formatter.format(recMovement.getTimeStamp()),
                                issMovement.getQuantity().toString(), issueChallan, formatter.format(issMovement.getTimeStamp()), "", "", "", "", Boolean.TRUE, 0));
                    } else if (retMovements.size() > j && issMovements.size() > j) {
                        retMovement = retMovements.get(j);
                        ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(retMovement.getId());
                        if (stockReturnItem != null) {
                            ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                            returnChallan = stockReturn.getReturnNumberSource();
                        }
                        issMovement = issMovements.get(j);
                        ISTStockIssueItem stockIssueItem = istStockIssueItemRepository.findOne(issMovement.getId());
                        if (stockIssueItem != null) {
                            ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                            issueChallan = issue.getIssueNumberSource();
                        }
                        consumeQty += issMovement.getQuantity().doubleValue();
                        returnQty = retMovement.getQuantity();
                        totalReturnQty += returnQty;
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", "", "", "",
                                issMovement.getQuantity().toString(), issueChallan, formatter.format(issMovement.getTimeStamp()), returnQty.toString(),
                                returnChallan, formatter.format(retMovement.getTimeStamp()), "", Boolean.TRUE, 0));
                    } else if (retMovements.size() > j && recMovements.size() > j) {
                        retMovement = retMovements.get(j);
                        ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(retMovement.getId());
                        if (stockReturnItem != null) {
                            ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                            returnChallan = stockReturn.getReturnNumberSource();
                        }
                        recMovement = recMovements.get(j);
                        ISStockReceiveItem receiveItem = isStockReceiveItemRepository.findOne(recMovement.getId());
                        if (receiveItem != null) {
                            ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                            receiveChallan = receive.getReceiveNumberSource();
                        }
                        returnQty = retMovement.getQuantity();
                        totalReturnQty += returnQty;
                        totalSuppliedQty += recMovement.getQuantity();
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", recMovement.getQuantity().toString(), receiveChallan, formatter.format(recMovement.getTimeStamp()),
                                "", "", "", returnQty.toString(), returnChallan, formatter.format(retMovement.getTimeStamp()), "", Boolean.TRUE, 0));
                    } else if (issMovements.size() > j) {
                        issMovement = issMovements.get(j);
                        ISTStockIssueItem stockIssueItem = istStockIssueItemRepository.findOne(issMovement.getId());
                        if (stockIssueItem != null) {
                            ISStockIssue issue = stockIssueRepository.findOne(stockIssueItem.getIssue());
                            issueChallan = issue.getIssueNumberSource();
                        }
                        consumeQty += issMovement.getQuantity().doubleValue();
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", "", "", "",
                                issMovement.getQuantity().toString(), issueChallan, formatter.format(issMovement.getTimeStamp()), "", "", "", "", Boolean.TRUE, 0));
                    } else if (retMovements.size() > j) {
                        retMovement = retMovements.get(j);
                        ISStockReturnItem stockReturnItem = stockReturnItemRepository.findOne(retMovement.getId());
                        if (stockReturnItem != null) {
                            ISStockReturn stockReturn = stockReturnRepository.findOne(stockReturnItem.getStockReturn());
                            returnChallan = stockReturn.getReturnNumberSource();
                        }
                        returnQty = retMovement.getQuantity();
                        totalReturnQty += returnQty;
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", "", "", "", "", "", "", returnQty.toString(), returnChallan, formatter.format(retMovement.getTimeStamp()), "", Boolean.TRUE, 0));
                    } else if (recMovements.size() > j) {
                        recMovement = recMovements.get(j);
                        totalSuppliedQty += recMovement.getQuantity();
                        ISStockReceiveItem receiveItem = isStockReceiveItemRepository.findOne(recMovement.getId());
                        if (receiveItem != null) {
                            ISStockReceive receive = stockReceiveRepository.findOne(receiveItem.getReceive());
                            receiveChallan = receive.getReceiveNumberSource();
                        }
                        reportDTOs2.add(new ProjectMaterialReportDTO("", "", "", "", recMovement.getQuantity().toString(), receiveChallan, formatter.format(recMovement.getTimeStamp()), "", "", "", "", "", "", "", Boolean.TRUE, 0));
                    }

                }
            }
            List<ISTopInventory> topInventoryList = topInventoryRepository.findByItemAndProject(materialItem.getId(), project);
            for (ISTopInventory topInventory : topInventoryList) {
                balanceQty += topInventory.getStoreOnHand();
            }
            if (hasRow) {
                ProjectMaterialReportDTO reportDTO = new ProjectMaterialReportDTO(materialItem.getItemNumber(), materialItem.getItemName(), materialItem.getUnits(),
                        boqQty.toString(), totalSuppliedQty.toString(), "", "", consumeQty.toString(), "", "", totalReturnQty.toString(), "", "", balanceQty.toString(), Boolean.FALSE, noOfRows);
                reportDTOs2.set(0, reportDTO);
            }
            reportDTOs.addAll(reportDTOs2);
        }
        return reportDTOs;
    }

    /**
     * The method used to Export the project material details
     **/
    @Transactional(readOnly = true)
    public String getMaterialReportByProject(String fileType, HttpServletResponse response, Integer project) {
        List<String> columns = new ArrayList<String>(Arrays.asList("Item No", "Item", "Unit", "BOQ Qty", "Supplied Qty", "Supplied Challan", "Supplied Date",
                "Isu Qty", "Isu Challan", "Isu Date", "Return Qty", "Return Challan", "Return Date", "Balance Qty"));
        List<ProjectMaterialReportDTO> reportDTOs = getMaterialReportByProjectRecords(project);
        Export export = new Export();
        export.setFileName("Project-Report");
        export.setHeaders(columns);
        exportService.createExportObject(reportDTOs, columns, export);
        return exportService.exportFile(fileType, export, response);
    }

    @Transactional(readOnly = true)
    public Page<ISProjectPerson> getFilterdProjectPersons(Integer ProjectId, Pageable pageable,
                                                          ProjectPersonCriteria projectPersonCriteria) {
        projectPersonCriteria.setProject(ProjectId);
        List<ISProjectPerson> projectPersons = new ArrayList<>();
        Predicate predicate = projectPersonPredicateBuilder.build(projectPersonCriteria, QISProjectPerson.iSProjectPerson);
        if (predicate != null) {
            Page<ISProjectPerson> persons = isProjectPersonRepository.findAll(predicate, pageable);
            return persons;
        } else {
            return null;
        }

    }

    public List<Media> getAllMediaInProject(Integer projectId, Pageable pageable) {
        List<Media> mediaList = new ArrayList<>();
        mediaList = mediaRepository.findByObjectIdOrderByCreatedDateDesc(projectId);
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(projectId);
        for (ISProjectTask projectTask : projectTasks) {
            List<Media> taskMediaList = mediaRepository.findByObjectIdOrderByCreatedDateDesc(projectTask.getId());
            for (Media taskMedia : taskMediaList) {
                mediaList.add(taskMedia);
            }
        }
        List<ISIssue> projectProblems = issueRepository.findByObjectIdAndObjectType(projectId, ObjectType.valueOf("PROJECT"));
        for (ISIssue problem : projectProblems) {
            List<Media> projectProblemMedia = mediaRepository.findByObjectIdOrderByCreatedDateDesc(problem.getId());
            for (Media problemMedia : projectProblemMedia) {
                mediaList.add(problemMedia);
            }
        }
        mediaList.sort(Comparator.comparing(o -> o.getCreatedDate()));
        return mediaList;
    }

    public ISProjectRole updateProjectRole(ISProjectRole isProjectRole) {
        checkNotNull(isProjectRole);
        return isProjectRoleRepository.save(isProjectRole);
    }

    @Transactional(readOnly = false)
    public ISProject saveAsProject(Integer projectId, ISProject project) {
        ISProject existProject = projectRepository.findByName(project.getName());
        if (existProject == null) {
            ISProject isProject = projectRepository.save(project);
            List<ISProjectWbs> wbses = projectWbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(projectId);
            if (wbses.size() > 0) {
                for (ISProjectWbs isWbs : wbses) {
                    ISProjectWbs projectWbs = (ISProjectWbs) Utils.cloneObject(isWbs, ISProjectWbs.class);
                    projectWbs.setProject(isProject.getId());
                    projectWbs.setId(null);
                    projectWbs.setPercentageComplete(0.0);
                    ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                    createWbsChildren(isWbs, projectWbs1, isProject);
                    createWbsTasks(isWbs, projectWbs1, isProject);
                }
            }
            List<ISProjectBoq> projectBoqs = projectBoqRepository.findByProject(projectId);
            if (projectBoqs.size() > 0) {
                for (ISProjectBoq isProjectBoq : projectBoqs) {
                    ISProjectBoq isProjectBoq1 = (ISProjectBoq) Utils.cloneObject(isProjectBoq, ISProjectBoq.class);
                    isProjectBoq1.setProject(isProject.getId());
                    isProjectBoq1.setId(null);
                    ISProjectBoq isProjectBoq2 = projectBoqRepository.save(isProjectBoq1);
                    createBoqItem(isProjectBoq, isProjectBoq2, isProject);
                }

            }
            return isProject;

        } else {
            throw new CassiniException("Project Name already exist");
        }

    }

    private void createBoqItem(ISProjectBoq isProjectBoq, ISProjectBoq isProjectBoq1, ISProject isProject) {
        List<ISBoqItem> boqItems = boqItemRepository.findByBoq(isProjectBoq.getId());
        if (boqItems.size() > 0) {
            for (ISBoqItem isBoqItem : boqItems) {
                ISBoqItem isBoqItem1 = (ISBoqItem) Utils.cloneObject(isBoqItem, ISBoqItem.class);
                isBoqItem1.setId(null);
                isBoqItem1.setProject(isProject.getId());
                isBoqItem1.setBoq(isProjectBoq1.getId());
                ISBoqItem boqItem = boqItemRepository.save(isBoqItem1);
            }
        }

    }

    private void createWbsTasks(ISProjectWbs wbs, ISProjectWbs projectWbs, ISProject project) {
        List<ISProjectTask> wbsTasks = projectTaskRepository.findByWbsItem(wbs.getId());
        for (ISProjectTask wbsTask : wbsTasks) {
            ISProjectTask projectTask = (ISProjectTask) Utils.cloneObject(wbsTask, ISProjectTask.class);
            projectTask.setId(null);
            projectTask.setWbsItem(projectWbs.getId());
            projectTask.setProject(project.getId());
            projectTask.setPercentComplete(0.0);
            projectTask.setPerson(null);
            projectTask.setStatus(TaskStatus.NEW);
            projectTask.setActualStartDate(null);
            projectTask.setActualFinishDate(null);
            projectTask = projectTaskRepository.save(projectTask);
        }

    }

    private void createWbsChildren(ISWbs isWbs, ISProjectWbs newWbs, ISProject project) {
        List<ISProjectWbs> children = projectWbsRepository.findByParentOrderByCreatedDateAsc(isWbs.getId());
        if (children.size() > 0) {
            for (ISProjectWbs child : children) {
                ISProjectWbs projectWbs = (ISProjectWbs) Utils.cloneObject(child, ISProjectWbs.class);
                projectWbs.setProject(project.getId());
                projectWbs.setId(null);
                projectWbs.setParent(newWbs.getId());
                projectWbs.setPercentageComplete(0.0);
                ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                createWbsChildren(child, projectWbs1, project);
                createWbsTasks(child, projectWbs1, project);
            }
        }

    }

    public MediaCountDTO getProjectMediaCount(Integer projectId) {
        Integer count = 0;
        MediaCountDTO mediaCount = new MediaCountDTO();
        //project media
        count = mediaRepository.findCountByObjectIdAndExtension(projectId, MediaType.IMAGE);
        if (count == null) {
            count = 0;
        }
        mediaCount.setImages(count);
        count = mediaRepository.findCountByObjectIdAndExtension(projectId, MediaType.VIDEO);
        if (count == null) {
            count = 0;
        }
        mediaCount.setVideos(count);
        //task media
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(projectId);
        for (ISProjectTask projectTask : projectTasks) {
            List<Media> taskMediaList = mediaRepository.findByObjectIdOrderByCreatedDateDesc(projectTask.getId());
            for (Media taskMedia : taskMediaList) {
                if (taskMedia.getMediaType().equals(MediaType.IMAGE)) {
                    mediaCount.setImages(mediaCount.getImages() + 1);
                } else if (taskMedia.getMediaType().equals(MediaType.VIDEO)) {
                    mediaCount.setImages(mediaCount.getVideos() + 1);
                }
            }
        }
        //problem media
        List<ISIssue> projectProblems = issueRepository.findByObjectIdAndObjectType(projectId, ObjectType.valueOf("PROJECT"));
        for (ISIssue problem : projectProblems) {
            List<Media> projectProblemMedia = mediaRepository.findByObjectIdOrderByCreatedDateDesc(problem.getId());
            for (Media problemMedia : projectProblemMedia) {
                if (problemMedia.getMediaType().equals(MediaType.IMAGE)) {
                    mediaCount.setImages(mediaCount.getImages() + 1);
                } else if (problemMedia.getMediaType().equals(MediaType.VIDEO)) {
                    mediaCount.setImages(mediaCount.getVideos() + 1);
                }
            }
        }
        //files count
        count = projectDocumentRepository.findCountByProject(projectId);
        if (count == null) {
            count = 0;
        }
        mediaCount.setFiles(count);
        return mediaCount;
    }

    @Transactional(readOnly = false)
    public ISProjectPersonOrganization saveProjectPerson(Integer projectId, ISProjectPersonOrganization projectPersonOrganization) {
        if (projectPersonOrganization.getPerson() != null) {
            ISProjectPersonOrganization isProjectPerson = projectPersonOrganizationRepository.findByProjectAndPersonAndRole(projectId, projectPersonOrganization.getPerson(), projectPersonOrganization.getProjectRole().getId());
            if (isProjectPerson == null) {
                projectPersonOrganization.setProject(projectId);
                ISProjectPersonOrganization projectPerson1 = projectPersonOrganizationRepository.save(projectPersonOrganization);
                if (projectPerson1.getPerson() != null) {
                    ISProjectPersonRole personRole = new ISProjectPersonRole();
                    if (projectPersonOrganization.getRole() != null) {
                        ISProjectPersonRole role = projectPersonRoleRepository.findByProjectAndPersonAndRole(projectId, projectPersonOrganization.getProjectPersonId(), projectPersonOrganization.getRole());
                        if (role != null) {
                            personRole.setRowId(role.getRowId());
                        }
                    }
                    personRole.setProject(projectId);
                    personRole.setPerson(projectPersonOrganization.getPerson());
                    personRole.setRole(projectPersonOrganization.getProjectRole().getId());
                    ISProjectPersonRole personRole1 = projectPersonRoleRepository.save(personRole);
                }
                if (projectPerson1.getPerson() != null) {
                    projectPerson1.setRole(projectPersonOrganization.getProjectRole().getId());
                    ISProjectPersonOrganization projectPerson2 = projectPersonOrganizationRepository.save(projectPerson1);
                }

            } else {
                throw new CassiniException("Person and role already exit in this project");
            }
        } else {
            projectPersonOrganization.setProject(projectId);
            ISProjectPersonOrganization projectPerson1 = projectPersonOrganizationRepository.save(projectPersonOrganization);
        }
        return projectPersonOrganization;
    }

    @Transactional(readOnly = true)
    public List<ISProjectPersonOrganization> getProjectPersonObjects(Integer projectId) {
        List<ISProjectPersonOrganization> projectPersonList = projectPersonOrganizationRepository.findByProject(projectId);
        for (ISProjectPersonOrganization personOrganization : projectPersonList) {
            if (personOrganization.getPerson() != null) {
                Person person = personRepository.findOne(personOrganization.getPerson());
                ISProjectPersonRole personRole = projectPersonRoleRepository.findByProjectAndPersonAndRole(personOrganization.getProject(), personOrganization.getPerson(), personOrganization.getRole());
                ISProjectRole role = isProjectRoleRepository.findOne(personRole.getRole());
                personOrganization.setProjectPerson(person);
                personOrganization.setProjectRole(role);
            }
        }
        return projectPersonList;
    }

    @Transactional(readOnly = false)
    public void deleteProjectNode(Integer projectId, Integer nodeId) {
        ISProjectPersonOrganization projectPerson = projectPersonOrganizationRepository.findByProjectAndNode(projectId, nodeId);
        ISProjectPersonRole personRole = projectPersonRoleRepository.findByProjectAndPersonAndRole(projectPerson.getProject(), projectPerson.getPerson(), projectPerson.getRole());
        if (personRole != null) {
            projectPersonRoleRepository.delete(personRole.getRowId());
        }
        if (projectPerson != null) {
            projectPersonOrganizationRepository.delete(projectPerson.getRowId());
        }

    }

    public List<ISProjectWbs> calculatePercentCompleteParent(Integer wbsId) {
        double weightage = 0;
        double percent = 0;
        List<ISProjectWbs> projectWbses = new ArrayList<>();
        ISProjectWbs wbs = projectWbsRepository.findOne(wbsId);
        List<ISProjectTask> projectTasks = projectTaskRepository.findByWbsItem(wbs.getId());
        if (projectTasks.size() > 0) {
            for (ISProjectTask task : projectTasks) {
                if (task.getInspectionResult() == null || task.getInspectionResult().equals(InspectionResult.ACCEPTED)) {
                    weightage += wbs.getWeightage();
                    percent += (task.getPercentComplete() * wbs.getWeightage()) / 100;
                }
            }
            if (percent > 0) {
                percent = (percent * 100) / weightage;
            }
            percent = Math.round(percent);
            wbs.setPercentageComplete(percent);
            wbsRepository.save(wbs);
        }
        projectWbses.add(wbs);
        if (wbs.getParent() != null) {
            ISProjectWbs projectWbs = visitParent(wbs.getParent(), projectWbses);
        }
        return projectWbses;

    }

    public ISProjectWbs visitParent(Integer wbs, List<ISProjectWbs> isProjectWbses) {
        double weightage = 0;
        double percent = 0;
        ISProjectWbs isProjectWbs = projectWbsRepository.findOne(wbs);
        List<ISProjectWbs> childrens = projectWbsRepository.findByParentOrderByCreatedDateAsc(wbs);
        if (childrens.size() > 0) {
            for (ISProjectWbs projectWbs : childrens) {
                weightage += projectWbs.getWeightage();
                percent += (projectWbs.getPercentageComplete() * projectWbs.getWeightage()) / 100;
            }
            percent = Math.round(percent);
            isProjectWbs.setPercentageComplete(percent);
            ISProjectWbs projectWbs = projectWbsRepository.save(isProjectWbs);
            isProjectWbses.add(projectWbs);
            if (projectWbs.getParent() != null) {
                visitParent(projectWbs.getParent(), isProjectWbses);
            }
        }
        return isProjectWbs;
    }

    public List<ISProject> getProjectsByPerson(Integer personId) {
        List<ISProject> projects = projectRepository.findByProjectOwner(personId);
        for (ISProject project : projects) {
            List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(project.getId());
            if (projectTasks.size() > 0) {
                Integer delayedTasks = 0;
                Integer pendingTasks = 0;
                Integer finishedTasks = 0;
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = formatter.format(date);
                try {
                    date = formatter.parse(strDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                project.setTotalTasks(projectTasks.size());
                for (ISProjectTask projectTask1 : projectTasks) {
                    if (!projectTask1.getStatus().equals(TaskStatus.FINISHED) && projectTask1.getPlannedFinishDate().before(date)) {
                        delayedTasks++;
                        pendingTasks++;
                    } else if (!projectTask1.getStatus().equals(TaskStatus.FINISHED)) {
                        pendingTasks++;
                    }
                    if (projectTask1.getStatus().equals(TaskStatus.FINISHED)) {
                        finishedTasks++;
                    }
                    project.setFinishedTasks(finishedTasks);
                    project.setPendingTasks(pendingTasks);
                    project.setDelayedTasks(delayedTasks);
                    calculateProjectPercent(project);
                }
            }
        }
        return projects;
    }

    public ProjectDto getProjectDetails(Integer projectId) {
        ProjectDto projectDto = new ProjectDto();
        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));
        ISProject project = projectRepository.findOne(projectId);
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(project.getId());
        calculateProjectPercent(project);
        Person person = personRepository.findOne(project.getProjectOwner());
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setAssignedTo(person.getFullName());
        projectDto.setPhoneNumber(person.getPhoneMobile());
        projectDto.setPlannedStartDate(project.getPlannedStartDate());
        projectDto.setPlannedFinishDate(project.getPlannedFinishDate());
        projectDto.setActualStartDate(project.getActualStartDate());
        projectDto.setActualFinishDate(project.getActualFinishDate());
        projectDto.setPercentComplete(project.getPercentComplete());
        projectDto.setTotalTasks(projectTasks.size());
        projectDto.setFiles(projectDocumentRepository.findCountByProject(projectId));
        projectDto.setSites(projectSiteRepository.findByProject(projectId).size());
        projectDto.setProblems(issueRepository.findByTargetObjectIdAndTargetObjectType(projectId, ObjectType.PROJECT).size());
        projectDto.setMedia(getAllMediaInProject(projectId, pageable).size());
        projectDto.setComments(commentRepository.findByObjectIdAndObjectType(projectId, ObjectType.PROJECT).size());
        return projectDto;
    }

    public List<ProjectTaskDto> getProjectTasks(Integer projectId) {
        List<ProjectTaskDto> tasks = new ArrayList<>();
        List<ISProjectTask> projectTasks = projectTaskRepository.findByProject(projectId);
        projectTasks.forEach(isProjectTask -> {
            ProjectTaskDto taskDto = new ProjectTaskDto();
            taskDto.setTask(isProjectTask);
            Person assignedTo = personRepository.findOne(isProjectTask.getPerson());
            Person inspectedBy = personRepository.findOne(isProjectTask.getPerson());
            taskDto.setAssignedTo(assignedTo.getFullName());
            taskDto.setPhoneNumber(assignedTo.getPhoneMobile());
            taskDto.setInspectedBy(inspectedBy.getFullName());
            taskDto.setInspectedByPhone(inspectedBy.getPhoneMobile());
            tasks.add(taskDto);
        });
        return tasks;
    }

    public List<ProjectProblemDto> getProjectProblems(Integer projectId) {
        List<ProjectProblemDto> problems = new ArrayList<>();
        List<ISIssue> issues = issueRepository.findByTargetObjectIdAndTargetObjectType(projectId, ObjectType.PROJECT);
        issues.forEach(issue -> {
            ProjectProblemDto problemDto = new ProjectProblemDto();
            problemDto.setTitle(issue.getTitle());
            problemDto.setId(issue.getId());
            problemDto.setPriority(issue.getPriority());
            problemDto.setAssignedTo(personRepository.findOne(issue.getAssignedTo()).getFullName());
            problemDto.setCreatedBy(personRepository.findOne(issue.getCreatedBy()).getFullName());
            problems.add(problemDto);
        });
        return problems;
    }

    @Transactional
    public List<ProjectPersonDto> getProjectTeam(Integer projectId) {
        List<ProjectPersonDto> personDtos = new ArrayList<>();
        List<ISProjectPerson> team = isProjectPersonRepository.findByProject(projectId);
        for (ISProjectPerson person : team) {
            ProjectPersonDto personDto = new ProjectPersonDto();
            Person person1 = personRepository.findOne(person.getPerson());
            if (person1.getObjectType() == ObjectType.valueOf("PERSON")) {
                Login login = loginService.getByPersonId(person1.getId());
                if (login != null && login.getIsActive()) {
                    personDto.setId(person1.getId());
                    personDto.setFullName(person1.getFullName());
                    personDtos.add(personDto);
                }
            }
        }
        return personDtos;
    }

    @Transactional(readOnly = true)
    public List<ISProjectWbs> getProjectWbsList(Integer projectId) {
        List<ISProjectWbs> projectWbsList = new ArrayList<>();
        List<ISProjectWbs> projectWbs = projectWbsRepository.findByProject(projectId);
        for (ISProjectWbs projectWb : projectWbs) {
            List<ISProjectWbs> child = projectWbsRepository.findByParentOrderByCreatedDateAsc(projectWb.getId());
            if (child.size() == 0) {
                projectWbsList.add(projectWb);
            }
        }
        return projectWbsList;
    }

    @Transactional(readOnly = false)
    public ISProject copyTasks(Integer projectId, ISProject project) {
        ISProject isProject = projectRepository.findOne(projectId);
        List<ISProjectWbs> wbses = projectWbsRepository.findByProjectAndParentIsNullOrderByCreatedDateAsc(project.getId());
        if (wbses.size() > 0) {
            for (ISProjectWbs isWbs : wbses) {
                ISProjectWbs projectWbs = (ISProjectWbs) Utils.cloneObject(isWbs, ISProjectWbs.class);
                projectWbs.setProject(isProject.getId());
                projectWbs.setId(null);
                projectWbs.setPercentageComplete(0.0);
                ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                copyWbsChildren(isWbs, projectWbs1, isProject);
                copyWbsTasks(isWbs, projectWbs1, isProject);
            }
        }
        return isProject;
    }

    private void copyWbsTasks(ISProjectWbs wbs, ISProjectWbs projectWbs, ISProject project) {
        List<ISProjectTask> wbsTasks = projectTaskRepository.findByWbsItem(wbs.getId());
        for (ISProjectTask wbsTask : wbsTasks) {
            ISProjectTask projectTask = (ISProjectTask) Utils.cloneObject(wbsTask, ISProjectTask.class);
            projectTask.setId(null);
            projectTask.setWbsItem(projectWbs.getId());
            projectTask.setProject(project.getId());
            projectTask.setPercentComplete(0.0);
            projectTask.setPerson(null);
            projectTask.setStatus(TaskStatus.NEW);
            projectTask.setActualStartDate(null);
            projectTask.setActualFinishDate(null);
            projectTask = projectTaskRepository.save(projectTask);
        }

    }

    private void copyWbsChildren(ISWbs isWbs, ISProjectWbs newWbs, ISProject project) {
        List<ISProjectWbs> children = projectWbsRepository.findByParentOrderByCreatedDateAsc(isWbs.getId());
        if (children.size() > 0) {
            for (ISProjectWbs child : children) {
                ISProjectWbs projectWbs = (ISProjectWbs) Utils.cloneObject(child, ISProjectWbs.class);
                projectWbs.setProject(project.getId());
                projectWbs.setId(null);
                projectWbs.setParent(newWbs.getId());
                projectWbs.setPercentageComplete(0.0);
                ISProjectWbs projectWbs1 = projectWbsRepository.save(projectWbs);
                createWbsChildren(child, projectWbs1, project);
                createWbsTasks(child, projectWbs1, project);
            }
        }

    }
}