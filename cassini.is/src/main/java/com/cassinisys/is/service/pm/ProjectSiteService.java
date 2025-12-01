package com.cassinisys.is.service.pm;

import com.cassinisys.is.filtering.SiteCriteria;
import com.cassinisys.is.filtering.SitePredicateBuilder;
import com.cassinisys.is.model.pm.*;
import com.cassinisys.is.model.procm.ISBoqItem;
import com.cassinisys.is.model.tm.DetailsCountDto;
import com.cassinisys.is.model.tm.ISProjectTask;
import com.cassinisys.is.model.tm.ProjectTaskDto;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.is.repo.pm.ProjectSiteRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.BoqItemRepository;
import com.cassinisys.is.repo.tm.ProjectTaskRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ProjectSiteService
 */
@Service
public class ProjectSiteService
        implements CrudService<ISProjectSite, Integer> {

    @Autowired
    private ProjectSiteRepository projectSiteRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private SitePredicateBuilder sitePredicateBuilder;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BoqItemRepository boqItemRepository;

    /**
     * The method used to create ISProjectSite
     **/
    @Transactional(readOnly = false)
    @Override
    public ISProjectSite create(ISProjectSite site) {
        checkNotNull(site);
        ISProjectSite projectSite = projectSiteRepository.findByProjectAndNameEqualsIgnoreCase(site.getProject(), site.getName());
        if (projectSite == null) {
            site = projectSiteRepository.save(site);
        } else {
            throw new CassiniException(projectSite.getName() + " already exist");
        }
        return site;
    }

    /**
     * The method used to update  ISProjectSite
     **/
    @Transactional(readOnly = false)
    @Override
    public ISProjectSite update(ISProjectSite site) {
        checkNotNull(site);
        checkNotNull(site.getId());
        site = projectSiteRepository.save(site);
        return site;
    }

    /**
     * The method used to findMultipleSitesByIds  with list of  ISProjectTask
     **/
    @Transactional(readOnly = true)
    public List<ISProjectTask> findMultipleSitesByIds(List<Integer> ids) {
        return projectSiteRepository.findByIdIn(ids);
    }
    /**
     * The method used to delete site of ISProjectSite
     **/

    /**
     * The method used to get ISProjectSite
     **/
    @Transactional(readOnly = true)
    public ISProjectSite get(Integer id) {
        checkNotNull(id);
        ISProjectSite site = projectSiteRepository.findOne(id);
        if (site == null) {
            throw new ResourceNotFoundException();
        }
        List<Integer> taskIds = new ArrayList<>();
        List<ISProjectTask> projectTasks = projectTaskRepository.findBySite(id);
        projectTasks.forEach(isProjectTask -> {
            taskIds.add(isProjectTask.getId());
        });
        site.setTotalTasks(projectTasks.size());
        site.setFinishedTasks(projectTaskRepository.findCountByStatusAndSite(TaskStatus.FINISHED, id));
        List<ISProjectResource> projectResources = resourceRepository.findByProjectAndTaskIn(site.getProject(), taskIds);
        site.setResources(projectResources.size());
        return site;
    }

    /**
     * The method used to getAll  with list of  ISProjectSite
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectSite> getAll() {
        return projectSiteRepository.findAll();
    }

    /**
     * The method used to findAll  with page of  ISProjectSite
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectSite> findAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectSiteRepository.findAll(pageable);
    }

    /**
     * The method used to findByProject  with page of  ISProjectSite
     **/
    @Transactional(readOnly = true)
    public Page<ISProjectSite> findByProject(Integer projectId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new org.springframework.data.domain.PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        Page<ISProjectSite> projectSites = projectSiteRepository.findAllByProject(projectId, pageable);
        for (ISProjectSite projectSite : projectSites.getContent()) {
            List<ISProjectTask> projectTasks = projectTaskRepository.findBySite(projectSite.getId());
            projectSite.setTotalTasks(projectTasks.size());
        }
        return projectSites;
    }

    /**
     * The method used to deleteSiteStore
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer siteId) {
        checkNotNull(siteId);
        projectSiteRepository.delete(siteId);
    }

    /**
     * The method used for the freeTextSearch for the page of  ISProjectSite
     **/
    public Page<ISProjectSite> freeTextSearch(Pageable pageable, SiteCriteria siteCriteria) {
        Predicate predicate = sitePredicateBuilder.build(siteCriteria, QISProjectSite.iSProjectSite);
        return projectSiteRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public ISProjectSite getSiteByName(Integer projectId, String siteName) {
        return projectSiteRepository.findByProjectAndNameEqualsIgnoreCase(projectId, siteName);
    }

    @Transactional(readOnly = true)
    public DetailsCountDto getSiteDetailsCount(Integer projectId, Integer siteId) {
        DetailsCountDto detailsCountDto = new DetailsCountDto();
        List<ISProjectTask> projectTasks = projectTaskRepository.findBySite(siteId);
        List<Integer> taskIds = new ArrayList<>();
        for (ISProjectTask projectTask : projectTasks) {
            taskIds.add(projectTask.getId());
        }
        detailsCountDto.setResource(resourceRepository.findByProjectAndTaskIn(projectId, taskIds).size());
        detailsCountDto.setTasks(projectTasks.size());
        return detailsCountDto;
    }

    @Transactional(readOnly = true)
    public List<ProjectTaskDto> getSiteTasks(Integer id) {
        List<ProjectTaskDto> projectTaskDtos = new ArrayList<>();
        List<ISProjectTask> siteTasks = projectTaskRepository.findBySite(id);
        siteTasks.forEach(isProjectTask -> {
            ProjectTaskDto taskDto = new ProjectTaskDto();
            taskDto.setTask(isProjectTask);
            Person assignedTo = personRepository.findOne(isProjectTask.getPerson());
            taskDto.setAssignedTo(assignedTo.getFullName());
            taskDto.setPhoneNumber(assignedTo.getPhoneMobile());
            if (isProjectTask.getInspectedBy() != null) {
                Person inspectedBy = personRepository.findOne(isProjectTask.getInspectedBy());
                taskDto.setInspectedBy(inspectedBy.getFullName());
                taskDto.setInspectedByPhone(inspectedBy.getPhoneMobile());
            }
            projectTaskDtos.add(taskDto);
        });
        return projectTaskDtos;
    }

    @Transactional(readOnly = true)
    public List<SiteResourceDto> getSiteResources(Integer id) {
        List<SiteResourceDto> siteResourceDtos = new ArrayList<>();
        List<ISProjectTask> siteTasks = projectTaskRepository.findBySite(id);
        for (ISProjectTask siteTask : siteTasks) {
            List<ISProjectResource> projectResources = resourceRepository.findByProjectAndTask(siteTask.getProject(), siteTask.getId());
            for (ISProjectResource projectResource : projectResources) {
                SiteResourceDto resourceDto = new SiteResourceDto();
                resourceDto.setTask(siteTask);
                resourceDto.setProjectResource(projectResource);
                if (projectResource.getResourceType().equals(ResourceType.MACHINETYPE) || projectResource.getResourceType().equals(ResourceType.MATERIALTYPE)) {
                    ISBoqItem boqItem = boqItemRepository.findOne(projectResource.getReferenceId());
                    resourceDto.setResource(boqItem.getItemName());
                } else {
                    Person person = personRepository.findOne(projectResource.getReferenceId());
                    resourceDto.setResource(person.getFullName());
                }
                siteResourceDtos.add(resourceDto);
            }
        }
        return siteResourceDtos;
    }

    @Transactional(readOnly = true)
    public List<ProjectSiteDto> getSitesByProject(Integer projectId) {
        List<ProjectSiteDto> siteDtos = new ArrayList<>();
        List<ISProjectSite> projectSites = projectSiteRepository.findByProject(projectId);
        projectSites.forEach(isProjectSite -> {
            ProjectSiteDto siteDto = new ProjectSiteDto();
            siteDto.setId(isProjectSite.getId());
            siteDto.setName(isProjectSite.getName());
            siteDtos.add(siteDto);
        });
        return siteDtos;
    }
}
