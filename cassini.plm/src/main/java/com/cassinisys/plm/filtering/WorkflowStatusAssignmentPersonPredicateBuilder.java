package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.core.QLogin;
import com.cassinisys.plm.model.wf.PLMWorkFlowStatusAssignment;
import com.cassinisys.plm.repo.wf.PLMWorkFlowStatusAssignmentRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR.
 */
@Component
public class WorkflowStatusAssignmentPersonPredicateBuilder implements PredicateBuilder<WorkflowCriteria, QLogin> {

    @Autowired
    private PLMWorkFlowStatusAssignmentRepository workFlowStatusAssignmentRepository;

    @Override
    public Predicate build(WorkflowCriteria criteria, QLogin pathBase) {
        return getDefaultPredicate(criteria, pathBase);

    }

    private Predicate getDefaultPredicate(WorkflowCriteria criteria, QLogin pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.person.firstName.containsIgnoreCase(s).
                        or(pathBase.person.lastName.containsIgnoreCase(s)).
                        or(pathBase.person.email.containsIgnoreCase(s)).
                        or(pathBase.person.phoneMobile.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (!Criteria.isEmpty(criteria.getTypeId())) {
            List<Integer> ids = new ArrayList<>();
            List<PLMWorkFlowStatusAssignment> assignments = workFlowStatusAssignmentRepository.findByStatus(criteria.getTypeId());
            for (PLMWorkFlowStatusAssignment assignment : assignments) {
                ids.add(assignment.getPerson());
            }
            predicates.add(pathBase.person.id.notIn(ids));
            predicates.add(pathBase.isActive.eq(true));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
