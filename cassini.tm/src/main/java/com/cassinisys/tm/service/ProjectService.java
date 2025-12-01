package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tm.filtering.ProjectCriteria;
import com.cassinisys.tm.filtering.ProjectPredicateBuilder;
import com.cassinisys.tm.model.QTMProject;
import com.cassinisys.tm.model.TMProject;
import com.cassinisys.tm.repo.ProjectRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by CassiniSystems on 07-07-2016.
 */
@Transactional
@Service
public class ProjectService implements
        CrudService<TMProject, Integer>,
        PageableService<TMProject, Integer> {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectPredicateBuilder predicateBuilder;

    @Override
    public TMProject create(TMProject project) {
        checkNotNull(project);
        project = projectRepository.save(project);

        return project;
    }

    @Override
    public TMProject update(TMProject project) {
        checkNotNull(project);
        checkNotNull(project.getId());
        project = projectRepository.save(project);

        return project;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        TMProject project = projectRepository.findOne(id);
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        projectRepository.delete(project);
    }

    @Override
    public TMProject get(Integer id) {
        checkNotNull(id);
        TMProject project = projectRepository.findOne(id);
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        return project;
    }

    @Override
    public List<TMProject> getAll() {
        return projectRepository.findAll();
    }

    @Override
    public Page<TMProject> findAll(Pageable pageable) {
        checkNotNull(pageable);
        return projectRepository.findAll(pageable);
    }

    public Page<TMProject> findAll(ProjectCriteria criteria, Pageable pageable) {
        checkNotNull(pageable);
        Predicate predicate = predicateBuilder.build(criteria, QTMProject.tMProject);
        return projectRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<TMProject> findMultipleProjects(List<Integer> ids) {
        return projectRepository.findByIdIn(ids);
    }


}

