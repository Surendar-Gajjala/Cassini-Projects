package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.QPLMItem;
import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.RequirementDeliverable;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.rm.RequirementDeliverableRepository;
import com.cassinisys.plm.repo.rm.RequirementRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 20-08-2019.
 */
@Component
public class RequirementItemDeliverablePredicateBuilder implements PredicateBuilder<RequirementDeliverableCriteria, QPLMItem> {

	@Autowired
	private ItemTypeRepository itemTypeRepository;

	@Autowired
	private ItemRevisionRepository itemRevisionRepository;

	@Autowired
	private RequirementRepository requirementRepository;

	@Autowired
	private RequirementDeliverableRepository requirementDeliverableRepository;

	public Predicate build(RequirementDeliverableCriteria criteria, QPLMItem pathBase) {
		return getDefaultPredicate(criteria, pathBase);
	}

	private Predicate getDefaultPredicate(RequirementDeliverableCriteria criteria, QPLMItem pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (!Criteria.isEmpty(criteria.getName())) {
			predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getName()));
		}
		if (!Criteria.isEmpty(criteria.getItemNumber())) {
			predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
		}
		if (!Criteria.isEmpty(criteria.getRequirement())) {
			Requirement requirement = requirementRepository.findOne(criteria.getRequirement());
			List<RequirementDeliverable> itemDeliverables = requirementDeliverableRepository.findByRequirement(requirement);
			if (itemDeliverables.size() > 0) {
				for (RequirementDeliverable deliverable : itemDeliverables) {
					predicates.add(pathBase.latestRevision.ne(deliverable.getObjectId()));
				}
			}
		}
		if (!Criteria.isEmpty(criteria.getItemType())) {
			PLMItemType plmItemType = itemTypeRepository.findOne(criteria.getItemType());
			predicates.add(pathBase.itemType.eq(plmItemType));
		}
		predicates.add(pathBase.configured.eq(false));
		return ExpressionUtils.allOf(predicates);
	}

}
