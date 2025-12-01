package com.cassinisys.erp.api.filtering;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cassinisys.erp.model.api.criteria.EmployeeTimeOffCriteria;
import com.cassinisys.erp.model.hrm.QERPEmployeeTimeOff;
import com.cassinisys.erp.model.hrm.TimeOffStatus;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

@Component
public class EmployeeTimeOffPredicateBuilder implements
		PredicateBuilder<EmployeeTimeOffCriteria, QERPEmployeeTimeOff> {

	@Override
	public Predicate build(EmployeeTimeOffCriteria criteria,
			QERPEmployeeTimeOff pathBase) {
		List<Predicate> predicates = new ArrayList<>();

		if (!Criteria.isEmpty(criteria.getReason())) {
			predicates.add((pathBase.reason.containsIgnoreCase(criteria
					.getReason())));
		}

		Predicate from = Criteria.getDateRangePredicate(pathBase.startDate,
				criteria.getStartDate());
		if (from != null) {
			predicates.add(from);
		}
		
		Predicate to = Criteria.getDateRangePredicate(pathBase.endDate,
				criteria.getEndDate());
		if (to != null) {
			predicates.add(to);
		}


		if (!Criteria.isEmpty(criteria.getTimeOffType())) {
			String s = criteria.getTimeOffType().trim();
			if (s.startsWith("=")) {
				Integer d = Integer.parseInt(s.substring(1));
				predicates.add(pathBase.timeOffType.eq(d));
			}
			if (s.startsWith(">")) {
				Integer d = Integer.parseInt(s.substring(1));
				predicates.add(pathBase.timeOffType.goe(d));
			}
			if (s.startsWith("<")) {
				Integer d = Integer.parseInt(s.substring(1));
				predicates.add(pathBase.timeOffType.loe(d));
			}
		}

		if (!Criteria.isEmpty(criteria.getEmployeeId())) {
			String s = criteria.getEmployeeId().trim();
			if (s.startsWith("=")) {
				Integer d = Integer.parseInt(s.substring(1));
				predicates.add(pathBase.employeeId.eq(d));
			}
			if (s.startsWith(">")) {
				Double d = Double.parseDouble(s.substring(1));
				predicates.add(pathBase.employeeId.goe(d));
			}
			if (s.startsWith("<")) {
				Double d = Double.parseDouble(s.substring(1));
				predicates.add(pathBase.employeeId.loe(d));
			}
		}

		if (!Criteria.isEmpty(criteria.getStatus())) {
			String s = criteria.getStatus().trim();
			String arr[] = s.split(",");
			List<Predicate> statuses = new ArrayList<>();

			for (String status : arr) {
				statuses.add(pathBase.status.eq(TimeOffStatus.valueOf(status
						.trim())));
			}
			predicates.add(ExpressionUtils.anyOf(statuses));
		}

		return ExpressionUtils.allOf(predicates);
	}

}
