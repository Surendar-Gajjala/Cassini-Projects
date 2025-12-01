package com.cassinisys.pdm.filtering;

import com.cassinisys.pdm.model.QPDMFolder;
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
public class FolderPredicateBuilder implements PredicateBuilder<FolderCriteria, QPDMFolder> {

	@Override
	public Predicate build(FolderCriteria folderCriteria, QPDMFolder pathBase) {

		if (folderCriteria.isFreeTextSearch()) {
			return getFreeTextSearchPredicate(folderCriteria, pathBase);
		} else {
			return getDefaultPredicate(folderCriteria, pathBase);
		}
	}

	private Predicate getFreeTextSearchPredicate(FolderCriteria folderCriteria, QPDMFolder pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (folderCriteria.getSearchQuery() != null) {
			String[] arr = folderCriteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.name.containsIgnoreCase(s).and(pathBase.description.containsIgnoreCase(s));
				predicates.add(predicate);
			}
		}
		return ExpressionUtils.allOf(predicates);
	}

	private Predicate getDefaultPredicate(FolderCriteria folderCriteria, QPDMFolder pathBase) {
		List<Predicate> predicates = new ArrayList<>();

		if (!Criteria.isEmpty(folderCriteria.getName())) {
			predicates.add(pathBase.name.equalsIgnoreCase(folderCriteria.getName()));
		}
		if (!Criteria.isEmpty(folderCriteria.getName())) {
			predicates.add(pathBase.description.equalsIgnoreCase(folderCriteria.getName()));
		}

		return ExpressionUtils.allOf(predicates);
	}

	public Predicate getFreeTextSearchPredicateForAll(FolderCriteria folderCriteria, QPDMFolder pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (folderCriteria.getSearchQuery() != null) {
			String[] arr = folderCriteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.name.containsIgnoreCase(s).and(pathBase.description.containsIgnoreCase(s));
				predicates.add(predicate);
			}
		}
		return ExpressionUtils.allOf(predicates);
	}
}
