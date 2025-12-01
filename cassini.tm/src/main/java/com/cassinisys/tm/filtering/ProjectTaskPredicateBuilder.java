package com.cassinisys.tm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.tm.model.QTMProjectTask;
import com.cassinisys.tm.model.TaskStatus;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 07-07-2016.
 */
@Component
public class ProjectTaskPredicateBuilder implements PredicateBuilder<ProjectTaskCriteria, QTMProjectTask> {


    @Override
    public Predicate build(ProjectTaskCriteria criteria, QTMProjectTask pathBase) {
        if (criteria.isFreeTextSearch()) {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearchQuery() != null) {
                Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
                predicates.add(predicate1);
            }
            if (criteria.getProject() != null) {
                Predicate predicate2 = getDefaultPredicate(criteria, pathBase);
                predicates.add(predicate2);
            }
            return ExpressionUtils.allOf(predicates);

        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ProjectTaskCriteria criteria, QTMProjectTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ProjectTaskCriteria criteria, QTMProjectTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(TaskStatus.valueOf(criteria.getStatus())));
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            predicates.add(pathBase.project.eq(Integer.parseInt(criteria.getProject())));
        }
        if (!Criteria.isEmpty(criteria.getApprovedBy())) {
            predicates.add(pathBase.approvedBy.eq(Integer.parseInt(criteria.getApprovedBy())));
        }
        if (!Criteria.isEmpty(criteria.getAssignedTo())) {
            predicates.add(pathBase.assignedTo.eq(Integer.parseInt(criteria.getAssignedTo())));
        }
        if (!Criteria.isEmpty(criteria.getVerifiedBy())) {
            predicates.add(pathBase.verifiedBy.eq(Integer.parseInt(criteria.getVerifiedBy())));
        }
        if (!Criteria.isEmpty(criteria.getShift())) {
            predicates.add(pathBase.shift.eq(Integer.parseInt(criteria.getShift())));
        }
        if (!Criteria.isEmpty(criteria.getWorkLocation())) {
            predicates.add(pathBase.location.containsIgnoreCase(criteria.getWorkLocation()));
        }
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate1 = getFreeTextSearchPredicate(criteria, pathBase);
            predicates.add(predicate1);
        }
        if (!Criteria.isEmpty(criteria.getAssignedDate())) {
            Predicate p = Criteria.getDatePredicate(pathBase.assignedDate, criteria.getAssignedDate());
            if (p != null) {
                predicates.add(p);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }
}