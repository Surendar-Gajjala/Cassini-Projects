package com.cassinisys.plm.service.pm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.filtering.BomItemFilterCriteria;
import com.cassinisys.plm.filtering.BomItemFilterPredicateBuilder;
import com.cassinisys.plm.filtering.ProjectItemReferenceBuilder;
import com.cassinisys.plm.filtering.ProjectItemReferenceCriteria;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.service.activitystream.dto.ASNewMemberDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 031 31-Jan -18.
 */
@Service
public class ProjectItemReferenceService implements CrudService<PLMProjectItemReference, Integer> {

    @Autowired
    private ProjectItemReferenceRepository projectItemReferenceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PLMActivityRepository activityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WbsElementRepository wbsElementRepository;

    @Autowired
    private ActivityItemReferenceRepository activityItemReferenceRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BomItemFilterPredicateBuilder bomItemFilterPredicateBuilder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProjectItemReferenceBuilder projectItemReferenceBuilder;

    @Override
    public PLMProjectItemReference create(PLMProjectItemReference projectItemReference) {
        return projectItemReferenceRepository.save(projectItemReference);
    }

    @Override
    public PLMProjectItemReference update(PLMProjectItemReference projectItemReference) {
        return projectItemReferenceRepository.save(projectItemReference);
    }

    @Override
    public void delete(Integer id) {
        projectItemReferenceRepository.delete(id);
    }

    @Override
    public PLMProjectItemReference get(Integer id) {
        return projectItemReferenceRepository.findOne(id);
    }

    @Override
    public List<PLMProjectItemReference> getAll() {
        return projectItemReferenceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMProjectItemReference> getItemsByProject(Integer projectId) {
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMProjectItemReference> projectItemReferences = projectItemReferenceRepository.findByProject(project);
        for (PLMProjectItemReference projectItemReference : projectItemReferences) {
            PLMItem item = itemRepository.findOne(projectItemReference.getItem().getItemMaster());
            projectItemReference.setPlmItem(item);
        }
        return projectItemReferences;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getProjectItems(Integer projectId, Pageable pageable) {
        BomItemFilterCriteria bomItemFilterCriteria=new BomItemFilterCriteria();
        bomItemFilterCriteria.setProject(projectId);
        Predicate predicate=bomItemFilterPredicateBuilder.build(bomItemFilterCriteria, QPLMItem.pLMItem);
        Page<PLMItem> plmItems1 = itemRepository.findAll(predicate,pageable);
        return plmItems1;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> searchItems(Integer projectId, Predicate predicate, Pageable pageable) {
        List<PLMItem> plmItems = new ArrayList<>();
        PLMProject project = projectRepository.findOne(projectId);
        List<PLMProjectItemReference> projectItemReferences = projectItemReferenceRepository.findByProject(project);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        if (projectItemReferences.size() != 0) {
            for (PLMItem item : items.getContent()) {
                Boolean exist = false;
                for (PLMProjectItemReference projectItemReference : projectItemReferences) {
                    if (projectItemReference.getItem().getId().equals(item.getLatestRevision())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    plmItems.add(item);
                }
            }
        } else {
            plmItems.addAll(items.getContent());
        }
        Page<PLMItem> plmItems1 = new PageImpl<PLMItem>(plmItems, pageable, plmItems.size());
        return plmItems1;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> searchReferenceItems(Integer projectId, ProjectItemReferenceCriteria criteria, Pageable pageable, String type) {
        List<PLMItem> plmItems1 = new ArrayList<>();
        criteria.setId(projectId);
        criteria.setObjectType(type);
        Page<PLMItem> plmItems = null;
        switch (type) {
            case "PROJECT":
                Predicate predicate = projectItemReferenceBuilder.build(criteria, QPLMItem.pLMItem);
                plmItems = itemRepository.findAll(predicate, pageable);
                break;
            case "ACTIVITY":
                PLMActivity activity = activityRepository.findOne(criteria.getId());
                PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                List<PLMProjectItemReference> activityItems = projectItemReferenceRepository.findByProject(wbsElement.getProject());
                if (activityItems.size() > 0) {
                    predicate = projectItemReferenceBuilder.build(criteria, QPLMItem.pLMItem);
                    plmItems = itemRepository.findAll(predicate, pageable);
                } else plmItems = new PageImpl<>(plmItems1, pageable, plmItems1.size());
                break;
            case "TASK":
                PLMTask task = taskRepository.findOne(criteria.getId());
                List<PLMActivityItemReference> activityItemReferences = activityItemReferenceRepository.findByActivity(task.getActivity());
                if (activityItemReferences.size() > 0) {
                    predicate = projectItemReferenceBuilder.build(criteria, QPLMItem.pLMItem);
                    plmItems = itemRepository.findAll(predicate, pageable);
                } else plmItems = new PageImpl<>(plmItems1, pageable, plmItems1.size());
                break;
        }
        return plmItems;
    }

    @Transactional
    public List<PLMProjectItemReference> createItemReferences(List<PLMProjectItemReference> plmProjectItemReferences) throws JsonProcessingException {
        List<PLMProjectItemReference> projectItemReferences = new ArrayList<>();
        List<ASNewMemberDTO> AsNewMemberDtos = new ArrayList<>();
        String itemNames = null;
        PLMProject project = null;
        for (PLMProjectItemReference projectItemReference : plmProjectItemReferences) {
            PLMProjectItemReference itemReference = new PLMProjectItemReference();
            PLMProjectItemReference existItem = projectItemReferenceRepository.findByProjectAndItem(projectItemReference.getProject(), projectItemReference.getItem());
            if (existItem == null) {
                itemReference.setItem(projectItemReference.getItem());
                itemReference.setProject(projectItemReference.getProject());
                projectItemReferences.add(projectItemReferenceRepository.save(itemReference));
            }
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemReference.getItem().getId());
            PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
            ASNewMemberDTO asNewMemberDTO = new ASNewMemberDTO(projectItemReference.getId(), plmItem.getItemName());
            AsNewMemberDtos.add(asNewMemberDTO);
            if (itemNames == null) {
                itemNames = plmItem.getItemNumber() + " - " + plmItem.getItemName();
            } else {
                itemNames = itemNames + " , " + plmItem.getItemNumber() + " - " + plmItem.getItemName();
            }
            project = itemReference.getProject();

        }
        applicationEventPublisher.publishEvent(new ProjectEvents.ProjectReferenceItemsAddedEvent(project, AsNewMemberDtos));
        projectService.sendProjectSubscribeNotification(project, objectMapper.writeValueAsString(AsNewMemberDtos), "referenceitemAdded");
        return projectItemReferences;
    }

    @Transactional
    public void deleteItem(Integer project, Integer item) throws JsonProcessingException {
        PLMProject project1 = projectRepository.findOne(project);
        PLMProjectItemReference projectItemReference = projectItemReferenceRepository.findOne(item);
        PLMItemRevision itemRevision = projectItemReference.getItem();
        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMActivityItemReference> activityItemReferences = new ArrayList<>();
        List<PLMWbsElement> wbsElement = wbsElementRepository.findByProject(project1);
        for (PLMWbsElement wbsElement1 : wbsElement) {
            List<PLMActivity> activities = activityRepository.findByWbs(wbsElement1.getId());
            for (PLMActivity activitie : activities) {
                PLMActivityItemReference activityItemReference = activityItemReferenceRepository.findByActivityAndItem(activitie.getId(), projectItemReference.getItem());
                if (activityItemReference != null) {
                    activityItemReferences.add(activityItemReference);
                    throw new CassiniException(messageSource.getMessage("reference_item_does_not_exist_in_activity",
                            null, "Reference Item exit in activitys does not delete this item", LocaleContextHolder.getLocale()));
                }
            }
        }
        if (activityItemReferences.size() == 0) {
            delete(item);
            applicationEventPublisher.publishEvent(new ProjectEvents.ProjectReferenceItemDeletedEvent(project1, plmItem));
            projectService.sendProjectSubscribeNotification(project1, plmItem.getItemNumber(), "referenceitemDeleted");
        }
    }
}
