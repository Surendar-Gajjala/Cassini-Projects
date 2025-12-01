package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMECR;
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
public class ECRPredicateBuilder implements PredicateBuilder<DCOCriteria, QPLMECR> {
    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(DCOCriteria criteria, QPLMECR pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(DCOCriteria criteria, QPLMECR pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.crNumber.containsIgnoreCase(s).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.changeReasonType.containsIgnoreCase(s)).
                        or(pathBase.descriptionOfChange.containsIgnoreCase(s)).
                        or(pathBase.reasonForChange.containsIgnoreCase(s))
                        .or(pathBase.proposedChanges.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getDcoNumber())) {
            predicates.add(pathBase.crNumber.containsIgnoreCase(criteria.getDcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.descriptionOfChange.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getCrType())) {
            predicates.add(pathBase.crType.eq(criteria.getCrType()));
        }

        if (!Criteria.isEmpty(criteria.getChangeAnalyst())) {
            predicates.add(pathBase.changeAnalyst.in(criteria.getChangeAnalyst()));
        }

        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.CHANGE);
            predicates.add(pathBase.id.in(ids));
//            predicates.add(pathBase.status.containsIgnoreCase(criteria.getStatus()));
        }

        if (!Criteria.isEmpty(criteria.getUrgency())) {
            predicates.add(pathBase.urgency.containsIgnoreCase(criteria.getUrgency()));
        }

        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.eq(criteria.getTitle()));
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
        return ExpressionUtils.allOf(predicates);
    }
}
