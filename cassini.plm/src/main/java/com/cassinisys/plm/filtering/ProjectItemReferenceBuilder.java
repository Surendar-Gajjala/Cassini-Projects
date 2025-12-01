package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pm.*;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pm.*;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectItemReferenceBuilder implements PredicateBuilder<ProjectItemReferenceCriteria, QPLMItem> {

    @Autowired
    private ProjectItemReferenceRepository projectItemReferenceRepository;

    @Autowired
    private ActivityItemReferenceRepository activityItemReferenceRepository;
    @Autowired
    private TaskItemReferenceRepository taskItemReferenceRepository;

    @Autowired
    private PLMActivityRepository activityRepository;

    @Autowired
    private WbsElementRepository wbsElementRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Override
    public Predicate build(ProjectItemReferenceCriteria criteria, QPLMItem pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ProjectItemReferenceCriteria criteria, QPLMItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getItemName())) {
            predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getItemName()));
        }

        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(criteria.getItemType())) {
            PLMItemType plmItemType = itemTypeRepository.findOne(criteria.getItemType());
            predicates.add(pathBase.itemType.eq(plmItemType));
        }

		/*----------------  For Project,Activity and Task Deliverables ----------------*/
        if (!Criteria.isEmpty(criteria.getObjectType()) && !Criteria.isEmpty(criteria.getId())) {
            switch (criteria.getObjectType()) {
                case "PROJECT":
                    List<PLMProjectItemReference> projectItemReferences = projectItemReferenceRepository.findByProject(projectRepository.findOne(criteria.getId()));
                    if (projectItemReferences.size() > 0)
                        predicates.addAll(projectItemReferences.stream().map(projectDeliverable -> pathBase.latestRevision.ne(projectDeliverable.getItem().getId())).collect(Collectors.toList()));
                    break;
                case "ACTIVITY":
                    PLMActivity activity = activityRepository.findOne(criteria.getId());
                    PLMWbsElement wbsElement = wbsElementRepository.findOne(activity.getWbs());
                    List<PLMProjectItemReference> activityItems = projectItemReferenceRepository.findByProject(wbsElement.getProject());
                    List<Integer> ids = new ArrayList<>();
                    activityItems.forEach(activityItem -> {
                        ids.add(activityItem.getItem().getId());
                    });
                    List<Integer> itemIds = activityItemReferenceRepository.getItemIdsByActivity(activity.getId());
                    if (itemIds.size() > 0) {
                        predicates.add(pathBase.latestRevision.notIn(itemIds));
                    }
                    predicates.add(pathBase.latestRevision.in(ids));
                    break;
                case "TASK":
                    PLMTask task = taskRepository.findOne(criteria.getId());
                    List<PLMActivityItemReference> activityItemReferences = activityItemReferenceRepository.findByActivity(task.getActivity());
                    ids = new ArrayList<>();
                    activityItemReferences.forEach(activityItem -> {
                        ids.add(activityItem.getItem().getId());
                    });
                    itemIds = taskItemReferenceRepository.getItemIdsByTask(task.getId());
                    if (itemIds.size() > 0) {
                        predicates.add(pathBase.latestRevision.notIn(itemIds));
                    }
                    predicates.add(pathBase.latestRevision.in(ids));
                    break;
            }
        }
        predicates.add(pathBase.configured.eq(false));
        return ExpressionUtils.allOf(predicates);
    }

}