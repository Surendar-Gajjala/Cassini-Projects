package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.pm.ProjectTemplateWbs;
import com.cassinisys.plm.repo.plm.ProjectTemplateWbsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 14-03-2018.
 */
@Service
public class ProjectTemplateWbsService implements CrudService<ProjectTemplateWbs, Integer> {

    @Autowired
    private ProjectTemplateWbsRepository projectTemplateWbsRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateWbs,'create')")
    public ProjectTemplateWbs create(ProjectTemplateWbs projectTemplateWbs) {
        projectTemplateWbs = projectTemplateWbsRepository.save(projectTemplateWbs);
        return projectTemplateWbs;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateWbs.id ,'edit')")
    public ProjectTemplateWbs update(ProjectTemplateWbs projectTemplateWbs) {
        projectTemplateWbs = projectTemplateWbsRepository.save(projectTemplateWbs);
        return projectTemplateWbs;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        projectTemplateWbsRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateWbs get(Integer id) {
        return projectTemplateWbsRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplateWbs> getAll() {
        return projectTemplateWbsRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateWbs getTemplateWbsByName(Integer templateId, String name) {
        ProjectTemplateWbs templateWbs = projectTemplateWbsRepository.findByTemplateAndNameEqualsIgnoreCase(templateId, name);
        return templateWbs;
    }

    @PreFilter("hasPermission(filterObject,'create')")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<ProjectTemplateWbs> createWBSElements(List<ProjectTemplateWbs> wbsElementList) {
        List<ProjectTemplateWbs> plmWbsElements = new ArrayList<>();
        wbsElementList.forEach(wbsElement -> {
            List<ProjectTemplateWbs> wbsElements = projectTemplateWbsRepository.findByTemplate(wbsElement.getTemplate());
            wbsElement = projectTemplateWbsRepository.save(wbsElement);
            wbsElement.setGanttId(wbsElement.getGanttId());
            plmWbsElements.add(wbsElement);
        });
        return plmWbsElements;
    }
}
