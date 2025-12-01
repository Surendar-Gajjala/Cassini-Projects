package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMDCR;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Component
public class DCRPredicateBuilder implements PredicateBuilder<DCRCriteria,QPLMDCR> {

	@Autowired
	private PLMWorkflowService workflowService;

	@Override
	public Predicate build(DCRCriteria criteria, QPLMDCR pathBase) {
		return getDefaultPredicate(criteria, pathBase);
	}

	private Predicate getDefaultPredicate(DCRCriteria criteria, QPLMDCR pathBase) {
		List<Predicate> predicates = new ArrayList<>();

		if (!Criteria.isEmpty(criteria.getSearchQuery())) {
			String[] arr = criteria.getSearchQuery().split(" ");
			for (String s : arr) {
				Predicate predicate = pathBase.crNumber.containsIgnoreCase(s).
						or(pathBase.title.containsIgnoreCase(s)).
						or(pathBase.descriptionOfChange.containsIgnoreCase(s));
				predicates.add(predicate);
			}
		}

		if (!Criteria.isEmpty(criteria.getCrNumber())) {
			predicates.add(pathBase.crNumber.containsIgnoreCase(criteria.getCrNumber()));
		}
		if (!Criteria.isEmpty(criteria.getDescriptionofChange())) {
			predicates.add(pathBase.descriptionOfChange.containsIgnoreCase(criteria.getDescriptionofChange()));
		}

		if (!Criteria.isEmpty(criteria.getTitle())) {
			predicates.add(pathBase.title.eq(criteria.getTitle()));
		}
		if (!Criteria.isEmpty(criteria.getStatus())) {
			List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.CHANGE);
			predicates.add(pathBase.id.in(ids));
        }
		if (!Criteria.isEmpty(criteria.getUrgency())) {
            predicates.add(pathBase.urgency.containsIgnoreCase(criteria.getUrgency()));
        }
		if (!Criteria.isEmpty(criteria.getChangeAnalyst())) {
            predicates.add(pathBase.changeAnalyst.in(criteria.getChangeAnalyst()));
        }
		if (!Criteria.isEmpty(criteria.getOriginator())) {
            predicates.add(pathBase.originator.in(criteria.getOriginator()));
        }
		
		if (!Criteria.isEmpty(criteria.getRequestedBy())) {
            predicates.add(pathBase.requestedBy.in(criteria.getRequestedBy()));
        }
		if (!Criteria.isEmpty(criteria.getChangeReasonType())) {
            predicates.add(pathBase.changeReasonType.containsIgnoreCase(criteria.getChangeReasonType()));
        }
		if (!Criteria.isEmpty(criteria.getCrType())) {
            predicates.add(pathBase.crType.eq(criteria.getCrType()));
        }
		return ExpressionUtils.allOf(predicates);
	}
}
