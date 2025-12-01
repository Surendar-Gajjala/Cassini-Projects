package com.cassinisys.is.filtering;

import com.cassinisys.is.model.tm.QISProjectTask;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 24-09-2018.
 */
@Component
public class AssignedTaskPredicateBuilder implements PredicateBuilder<AssignedTaskCriteria, QISProjectTask> {

    @Override
    public Predicate build(AssignedTaskCriteria criteria, QISProjectTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery())));
        }
        if (!Criteria.isEmpty(criteria.getAssignedType())) {
            if (criteria.getAssignedType().equals("ASSIGNEDTO")) {
                predicates.add(pathBase.person.eq(criteria.getPerson()));
                predicates.add(pathBase.status.ne(TaskStatus.FINISHED));
            } else {
                predicates.add(pathBase.createdBy.eq(criteria.getPerson()));
                predicates.add(pathBase.status.ne(TaskStatus.FINISHED));
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

}
