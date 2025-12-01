package com.cassinisys.is.filtering;
/**
 * The class is for TaskPredicateBuilder
 */

import com.cassinisys.is.model.tm.InspectionResult;
import com.cassinisys.is.model.tm.QISProjectTask;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TaskPredicateBuilder implements PredicateBuilder<TaskCriteria, QISProjectTask> {

    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(TaskCriteria criteria, QISProjectTask pathBase) {
        if (criteria.getFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(TaskCriteria criteria, QISProjectTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getFreeTextSearch() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (criteria.getProject() != null) {
            predicates.add(pathBase.project.eq(criteria.getProject()));
        }
        if (criteria.getPerson() != null) {
            predicates.add(pathBase.person.eq(criteria.getPerson()));
        }
        return ExpressionUtils.allOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(TaskCriteria criteria, QISProjectTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Predicate> p = new ArrayList<>();
            String[] arr = criteria.getStatus().split(",");
            for (String s : arr) {
                Predicate predicate = pathBase.status.eq(TaskStatus.valueOf(s));
                p.add(predicate);
            }
            Predicate p1 = ExpressionUtils.anyOf(p);
            predicates.add(p1);
        }
        if (!Criteria.isEmpty(criteria.getInspectionResult())) {
            List<Predicate> p = new ArrayList<>();
            String[] arr = criteria.getInspectionResult().split(",");
            for (String s : arr) {
                Predicate predicate = pathBase.inspectionResult.eq(InspectionResult.valueOf(s));
                p.add(predicate);
            }
            Predicate p1 = ExpressionUtils.anyOf(p);
            predicates.add(p1);
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getSite())) {
            predicates.add(pathBase.site.eq(Integer.parseInt(criteria.getSite())));
        }
        if (!Criteria.isEmpty(criteria.getWbsItem())) {
            predicates.add(pathBase.wbsItem.eq(Integer.parseInt(criteria.getWbsItem())));
        }

       /* if (!Criteria.isEmpty(criteria.getPercentComplete())) {
            predicates.add(pathBase.percentComplete.eq(Integer.parseInt(criteria.getPercentComplete())));
        }*/
        if (!Criteria.isEmpty(criteria.getPlannedStartDate())) {
            Predicate p1 = Criteria.getDatePredicate(pathBase.plannedStartDate, criteria.getPlannedStartDate());
            if (p1 != null) {
                predicates.add(p1);
            }
        }
        if (!Criteria.isEmpty(criteria.getPlannedFinishDate())) {
            Predicate p = Criteria.getDatePredicate(pathBase.plannedFinishDate, criteria.getPlannedFinishDate());
            if (p != null) {
                predicates.add(p);
            }
        }
        if (!Criteria.isEmpty(criteria.getActualStartDate())) {
            Predicate p2 = Criteria.getDatePredicate(pathBase.actualStartDate, criteria.getActualStartDate());
            if (p2 != null) {
                predicates.add(p2);
            }
        }
        if (!Criteria.isEmpty(criteria.getActualFinishDate())) {
            Predicate p3 = Criteria.getDatePredicate(pathBase.actualFinishDate, criteria.getActualFinishDate());
            if (p3 != null) {
                predicates.add(p3);
            }
        }
        if (!Criteria.isEmpty(criteria.getInspectedOn())) {
            Predicate p1 = Criteria.getDatePredicate(pathBase.inspectedOn, criteria.getInspectedOn());
            if (p1 != null) {
                predicates.add(p1);
            }
        }
        if (!Criteria.isEmpty(criteria.getProject())) {
            predicates.add(pathBase.project.eq(criteria.getProject()));
        }
        if (!Criteria.isEmpty(criteria.getPerson())) {
            predicates.add(pathBase.person.eq(criteria.getPerson()));
        }
        if (!Criteria.isEmpty(criteria.getInspectedBy())) {
            predicates.add(pathBase.inspectedBy.eq(criteria.getInspectedBy()));
        }
        if (!Criteria.isEmpty(criteria.getSubContract())) {
            predicates.add(pathBase.subContract.eq(Boolean.parseBoolean(criteria.getSubContract())));
        }
        if (criteria.getDelayTask()) {
            Date todayDate = new Date();
            predicates.add(pathBase.plannedFinishDate.before(todayDate));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
