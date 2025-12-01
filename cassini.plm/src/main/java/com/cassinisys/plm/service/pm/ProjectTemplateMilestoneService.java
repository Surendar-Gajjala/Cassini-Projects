package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.model.pm.ProjectTemplateMilestone;
import com.cassinisys.plm.repo.plm.ProjectTemplateMilestoneRepository;
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
public class ProjectTemplateMilestoneService implements CrudService<ProjectTemplateMilestone, Integer> {

    @Autowired
    private ProjectTemplateMilestoneRepository projectTemplateMilestoneRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateMilestone,'create')")
    public ProjectTemplateMilestone create(ProjectTemplateMilestone projectTemplateMilestone) {
        projectTemplateMilestone = projectTemplateMilestoneRepository.save(projectTemplateMilestone);
        return projectTemplateMilestone;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#projectTemplateMilestone.id ,'edit')")
    public ProjectTemplateMilestone update(ProjectTemplateMilestone projectTemplateMilestone) {
        projectTemplateMilestone = projectTemplateMilestoneRepository.save(projectTemplateMilestone);
        return projectTemplateMilestone;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        projectTemplateMilestoneRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateMilestone get(Integer id) {
        return projectTemplateMilestoneRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<ProjectTemplateMilestone> getAll() {
        return projectTemplateMilestoneRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public ProjectTemplateMilestone getMilestoneByNameAndWbs(String activityName, Integer wbsId) {
        ProjectTemplateMilestone templateMilestone = projectTemplateMilestoneRepository.findByWbsAndNameEqualsIgnoreCase(wbsId, activityName);
        return templateMilestone;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @PreFilter("hasPermission(filterObject,'create')")
    public List<ProjectTemplateMilestone> createMilestones(List<ProjectTemplateMilestone> plmMilestones) {
        List<ProjectTemplateMilestone> plmMilestoneList = new ArrayList<>();
        plmMilestones.forEach(milestone -> {
            milestone = projectTemplateMilestoneRepository.save(milestone);
            milestone.setGanttId(milestone.getGanttId());
            plmMilestoneList.add(milestone);
        });
        return plmMilestoneList;
    }
}
