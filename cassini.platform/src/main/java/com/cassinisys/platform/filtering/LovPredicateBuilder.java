package com.cassinisys.platform.filtering;

import com.cassinisys.platform.model.core.QLov;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author reddy
 */
@Component
public class LovPredicateBuilder implements
		PredicateBuilder<LovCriteria, QLov> {

	@Override
	public Predicate build(LovCriteria criteria, QLov pathBase) {
				
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!Criteria.isEmpty(criteria.getType())) {
			predicates.add(pathBase.type.equalsIgnoreCase(criteria.getType()));
		}
		if (!Criteria.isEmpty(criteria.getName())) {
			predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
		}
		return ExpressionUtils.allOf(predicates);
	}

}
