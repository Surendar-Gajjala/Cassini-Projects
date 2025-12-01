package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.pm.MilestoneRepository;
import com.cassinisys.plm.repo.pm.PLMActivityRepository;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.pm.WbsElementRepository;
import com.cassinisys.plm.service.activitystream.dto.ASNewActivityAndMilestoneDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyamreddy on 005 5-Feb -18.
 */
@Service
public class MilestoneService implements CrudService<PLMMilestone, Integer> {

    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private PLMActivityRepository activityRepository;

    @Autowired
    private WbsElementRepository wbsElementRepository;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ProjectRepository projectRepository;
    @Override
    @Transactional
    @PreAuthorize("hasPermission(#item,'create')")
    public PLMMilestone create(PLMMilestone milestone) {
        PLMMilestone plmMilestone = milestoneRepository.findByWbsAndNameEqualsIgnoreCase(milestone.getWbs(), milestone.getName());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(milestone.getWbs());
        /*if (plmMilestone != null) {
            throw new CassiniException(plmMilestone.getName() + " : Milestone name already exist on Wbs.");
		}*/
        List<PLMActivity> activities = activityRepository.findByWbs(milestone.getWbs());
        List<PLMMilestone> milestones = milestoneRepository.findByWbs(milestone.getWbs());
        milestone.setSequenceNumber((activities.size() + milestones.size()) + 1);
        milestone = milestoneRepository.save(milestone);
        milestone.setGanttId(milestone.getGanttId());
        return milestone;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<PLMMilestone> createMilestones(Integer projectId, List<PLMMilestone> plmMilestones) throws JsonProcessingException {
        List<PLMMilestone> plmMilestoneList = new ArrayList<>();
        List<ASNewActivityAndMilestoneDTO> asNewActivityAndMilestoneDTOs = new ArrayList<>();
        plmMilestones.forEach(milestone -> {
            PLMMilestone plmMilestone = milestoneRepository.findByWbsAndNameEqualsIgnoreCase(milestone.getWbs(), milestone.getName());
            PLMWbsElement wbsElement = wbsElementRepository.findOne(milestone.getWbs());
            PLMProject project = wbsElement.getProject();
            if(milestone.getId() == null){
                ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), milestone.getName());
                asNewActivityAndMilestoneDTOs.add(asNewActivityAndMilestoneDTO);
            }
            List<PLMActivity> activities = activityRepository.findByWbs(milestone.getWbs());
            List<PLMMilestone> milestones = milestoneRepository.findByWbs(milestone.getWbs());
            milestone = milestoneRepository.save(milestone);
            milestone.setGanttId(milestone.getGanttId());
            plmMilestoneList.add(milestone);
        });
        PLMProject plmProject = projectRepository.findOne(projectId);
        if(asNewActivityAndMilestoneDTOs.size() > 0) {
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMilestonesAddedEvent(plmProject, asNewActivityAndMilestoneDTOs));
            List<String> tasks = new ArrayList<>();
            asNewActivityAndMilestoneDTOs.forEach(f -> tasks.add(f.getName()));
            projectService.sendProjectSubscribeNotification(plmProject, tasks.toString(), "milestoneAdded");
        }
        return plmMilestoneList;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#itemRevision.id ,'edit')")
    public PLMMilestone update(PLMMilestone milestone) {
        PLMMilestone plmMilestone = milestoneRepository.findByWbsAndNameEqualsIgnoreCase(milestone.getWbs(), milestone.getName());
        PLMMilestone existMileStone = milestoneRepository.findOne(milestone.getId());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(milestone.getWbs());

        if (plmMilestone != null && !plmMilestone.getId().equals(milestone.getId())) {
            String message = messageSource.getMessage("milestone_already_exist_on_wbs", null, "{0} Milestone name already exist on Wbs", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", plmMilestone.getName());
            throw new CassiniException(result);
        }

        milestone = milestoneRepository.save(milestone);
        return milestone;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMMilestone milestone = milestoneRepository.findOne(id);
        PLMWbsElement wbsElement = wbsElementRepository.findOne(milestone.getWbs());
        List<WBSDto> wbsDtoList = projectService.getWbsChildrenBySequence(0, milestone.getWbs());
        for (WBSDto element : wbsDtoList) {
            if (element.getSequenceNumber() > milestone.getSequenceNumber()) {
                if (element.getObjectType().equals("PROJECTACTIVITY")) {
                    PLMActivity activity1 = activityRepository.findOne(element.getId());
                    activity1.setSequenceNumber(element.getSequenceNumber() - 1);
                    activity1 = activityRepository.save(activity1);
                } else {
                    PLMMilestone milestone1 = milestoneRepository.findOne(element.getId());
                    milestone1.setSequenceNumber(element.getSequenceNumber() - 1);
                    milestone1 = milestoneRepository.save(milestone1);
                }
            }
        }
        milestoneRepository.delete(id);
        try {
            projectService.sendProjectSubscribeNotification(wbsElement.getProject(), milestone.getName(), "milestoneDeleted");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMMilestone get(Integer id) {
        return milestoneRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMMilestone> getAll() {
        return milestoneRepository.findAll();
    }

    @Transactional
    public PLMMilestone finishMilestone(PLMMilestone milestone) throws JsonProcessingException {
        PLMMilestone milestone1 = milestoneRepository.findOne(milestone.getId());
        PLMWbsElement wbsElement = wbsElementRepository.findOne(milestone.getWbs());
        PLMProject project = wbsElement.getProject();
        milestone1.setStatus(ProjectActivityStatus.FINISHED);
        milestone1.setActualFinishDate(new Date());
        ASNewActivityAndMilestoneDTO asNewActivityAndMilestoneDTO = new ASNewActivityAndMilestoneDTO(wbsElement.getName(), milestone.getName());
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMilestoneFinishedEvent(project, asNewActivityAndMilestoneDTO));
        projectService.sendProjectSubscribeNotification(project, asNewActivityAndMilestoneDTO.getName(), "milestoneFinished");
        return milestoneRepository.save(milestone1);
    }
}
