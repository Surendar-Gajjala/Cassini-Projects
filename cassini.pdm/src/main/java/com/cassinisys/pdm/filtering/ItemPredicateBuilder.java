package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.PDMItemType;
import com.cassinisys.pdm.model.QPDMItem;
import com.cassinisys.pdm.repo.ItemTypeRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 27-01-2017.
 */
@Component
public class ItemPredicateBuilder implements PredicateBuilder<ItemCriteria, QPDMItem> {

	@Autowired
	private ItemTypeRepository itemTypeRepository;

	@Override
	public Predicate build(ItemCriteria criteria, QPDMItem pathBase) {
		if (criteria.getSearchQuery() != null) {
			Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
			return predicate;
		} else {
			return getDefaultPredicate(criteria, pathBase);
		}
	}

	private Predicate getFreeTextSearchPredicate(ItemCriteria criteria, QPDMItem pathBase) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (criteria.getSearchQuery() != null) {
			String[] arr = criteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.itemType.name.containsIgnoreCase(s).
						or(pathBase.itemNumber.containsIgnoreCase(s)).
						or(pathBase.description.containsIgnoreCase(s)).
						or(pathBase.status.containsIgnoreCase(s));
				predicates.add(predicate);

			}
		}
		return ExpressionUtils.allOf(predicates);
	}

	private Predicate getDefaultPredicate(ItemCriteria criteria, QPDMItem pathBase) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!Criteria.isEmpty(criteria.getItemType())) {
			PDMItemType pdmItemType = itemTypeRepository.findOne(Integer.parseInt(criteria.getItemType()));
			predicates.add(pathBase.itemType.eq(pdmItemType));
		}
		if (!Criteria.isEmpty(criteria.getItemNumber())) {
			predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
		}

		if (!Criteria.isEmpty(criteria.getDescription())) {
			predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
		}

		if (!Criteria.isEmpty(criteria.getStatus())) {
			predicates.add(pathBase.status.containsIgnoreCase(criteria.getStatus()));
		}
		if (!Criteria.isEmpty(criteria.getRevision())) {
			predicates.add(pathBase.revision.containsIgnoreCase(criteria.getRevision()));
		}
		return ExpressionUtils.allOf(predicates);
	}
}
