package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.QPDMFile;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyamreddy on 27-Jan-17.
 */
@Component
public class FilePredicateBuilder implements PredicateBuilder<FileCriteria, QPDMFile> {


	@Override
	public Predicate build(FileCriteria criteria, QPDMFile pathBase) {
		if (criteria.getSearchQuery() != null) {
			Predicate predicate = getFreeTextSearchPredicate(criteria, pathBase);
			return predicate;
		} else {
			return getDefaultPredicate(criteria, pathBase);
		}
	}

	private Predicate getFreeTextSearchPredicate(FileCriteria criteria, QPDMFile pathBase) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (criteria.getSearchQuery() != null) {
			String[] arr = criteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.name.containsIgnoreCase(s).
						and(pathBase.latest.eq(true));
				predicates.add(predicate);

			}
		}
		return ExpressionUtils.allOf(predicates);
	}

	private Predicate getDefaultPredicate(FileCriteria criteria, QPDMFile pathBase) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!Criteria.isEmpty(criteria.getName())) {
			predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
		}

		if (!Criteria.isEmpty(criteria.getDescription())) {
			predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
		}

		return ExpressionUtils.allOf(predicates);
	}

	public Predicate getFreeTextSearchPredicateForAll(FileCriteria criteria, QPDMFile pathBase) {
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (criteria.getSearchQuery() != null) {
			String[] arr = criteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.name.containsIgnoreCase(s).
						and(pathBase.latest.eq(true));
				predicates.add(predicate);

			}
		}
		return ExpressionUtils.allOf(predicates);
	}

}
