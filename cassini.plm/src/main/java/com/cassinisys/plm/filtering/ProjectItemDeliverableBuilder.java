package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.pm.PLMActivityDeliverable;
import com.cassinisys.plm.model.pm.PLMProjectDeliverable;
import com.cassinisys.plm.model.pm.PLMTaskDeliverable;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.pm.ActivityDeliverableRepository;
import com.cassinisys.plm.repo.pm.ProjectDeliveravbleRepository;
import com.cassinisys.plm.repo.pm.TaskDeliverableRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 12-09-2018.
 */
@Component
public class ProjectItemDeliverableBuilder implements PredicateBuilder<ProjectDeliverableCriteria, QPLMItem> {

	@Autowired
	private ProjectDeliveravbleRepository projectDeliveravbleRepository;

	@Autowired
	private ActivityDeliverableRepository activityDeliverableRepository;

	@Autowired
	private TaskDeliverableRepository taskDeliverableRepository;

	@Autowired
	private ItemTypeRepository itemTypeRepository;

	@Autowired
	private ItemRevisionRepository itemRevisionRepository;

	@Override
	public Predicate build(ProjectDeliverableCriteria criteria, QPLMItem pathBase) {
		return getDefaultPredicate(criteria, pathBase);
	}

	private Predicate getDefaultPredicate(ProjectDeliverableCriteria criteria, QPLMItem pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (!Criteria.isEmpty(criteria.getName())) {
			predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getName()));
		}
		if (!Criteria.isEmpty(criteria.getItemNumber())) {
			predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
		}
		if (!Criteria.isEmpty(criteria.getProject())) {
			List<PLMProjectDeliverable> itemDeliverables = projectDeliveravbleRepository.findByProject(criteria.getProject());
			if (itemDeliverables.size() > 0) {
				for (PLMProjectDeliverable projectDeliverable : itemDeliverables) {
					predicates.add(pathBase.latestRevision.ne(projectDeliverable.getItemRevision()));
				}
			}
		}
		if (!Criteria.isEmpty(criteria.getItemType())) {
			PLMItemType plmItemType = itemTypeRepository.findOne(criteria.getItemType());
			predicates.add(pathBase.itemType.eq(plmItemType));
		}

		/*----------------  For Project,Activity and Task Deliverables ----------------*/
		if (!Criteria.isEmpty(criteria.getObjectType()) && !Criteria.isEmpty(criteria.getObjectId())) {
			if (criteria.getObjectType().equals(PLMObjectType.PROJECT.toString())) {
				List<PLMProjectDeliverable> itemDeliverables = projectDeliveravbleRepository.findByProject(criteria.getObjectId());
				if (itemDeliverables.size() > 0) {
					for (PLMProjectDeliverable projectDeliverable : itemDeliverables) {
						predicates.add(pathBase.latestRevision.ne(projectDeliverable.getItemRevision()));
					}
				}
			} else if (criteria.getObjectType().equals(PLMObjectType.PROJECTACTIVITY.toString())) {
				List<PLMActivityDeliverable> itemDeliverables = activityDeliverableRepository.findByActivity(criteria.getObjectId());
				if (itemDeliverables.size() > 0) {
					for (PLMActivityDeliverable activityDeliverable : itemDeliverables) {
						predicates.add(pathBase.latestRevision.ne(activityDeliverable.getItemRevision()));
					}
				}
			} else if (criteria.getObjectType().equals(PLMObjectType.PROJECTTASK.toString())) {
				List<PLMTaskDeliverable> itemDeliverables = taskDeliverableRepository.findByTask(criteria.getObjectId());
				if (itemDeliverables.size() > 0) {
					for (PLMTaskDeliverable taskDeliverable : itemDeliverables) {
						predicates.add(pathBase.latestRevision.ne(taskDeliverable.getItemRevision()));
					}
				}
			}
		}
		predicates.add(pathBase.configured.eq(false));
		return ExpressionUtils.allOf(predicates);
	}

}
