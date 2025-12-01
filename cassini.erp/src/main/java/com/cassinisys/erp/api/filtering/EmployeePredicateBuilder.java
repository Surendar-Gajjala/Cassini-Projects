package com.cassinisys.erp.api.filtering;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cassinisys.erp.model.api.criteria.EmployeeCriteria;
import com.cassinisys.erp.model.hrm.QERPEmployee;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Component
public class EmployeePredicateBuilder implements
		PredicateBuilder<EmployeeCriteria, QERPEmployee> {

	@Override
	public Predicate build(EmployeeCriteria criteria, QERPEmployee pathBase) {
		List<Predicate> predicates = new ArrayList<>();

		if (!Criteria.isEmpty(criteria.getFirstName())) {
			predicates.add(Criteria.getMultiplePredicates(pathBase.firstName,
					criteria.getFirstName()));
		}

		if (!Criteria.isEmpty(criteria.getLastName())) {
			predicates.add(Criteria.getMultiplePredicates(pathBase.lastName,
					criteria.getLastName()));
		}
		if (!Criteria.isEmpty(criteria.getJobTitle())) {
			predicates.add(Criteria.getMultiplePredicates(pathBase.jobTitle,
					criteria.getJobTitle()));
		}

		if (!Criteria.isEmpty(criteria.getDepartment())) {
			predicates.add(Criteria.getMultiplePredicates(
					pathBase.department.name, criteria.getDepartment()));
		}
		return ExpressionUtils.allOf(predicates);
	}
}
