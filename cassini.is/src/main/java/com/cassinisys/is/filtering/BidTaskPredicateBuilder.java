package com.cassinisys.is.filtering;
/**
 * The class is for BidTaskPredicateBuilder
 */

import com.cassinisys.is.model.tm.QISBidTask;
import com.cassinisys.is.model.tm.TaskStatus;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BidTaskPredicateBuilder implements PredicateBuilder<BidTaskCriteria, QISBidTask> {
    /**
     * The method used to build Predicate
     */
    @Override
    public Predicate build(BidTaskCriteria criteria, QISBidTask pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(TaskStatus.valueOf(criteria.getStatus())));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.equalsIgnoreCase(criteria.getName()));
        }
        Predicate p = Criteria.getDateRangePredicate(pathBase.plannedStartDate, criteria.getPlannedStartDate());
        if (p != null) {
            predicates.add(p);
        }
        Predicate p1 = Criteria.getDateRangePredicate(pathBase.plannedFinishDate, criteria.getPlannedFinishDate());
        if (p1 != null) {
            predicates.add(p1);
        }
        Predicate p2 = Criteria.getDateRangePredicate(pathBase.actualStartDate, criteria.getActualStartDate());
        if (p2 != null) {
            predicates.add(p2);
        }
        Predicate p3 = Criteria.getDateRangePredicate(pathBase.actualFinishDate, criteria.getActualFinishDate());
        if (p3 != null) {
            predicates.add(p3);
        }
        if (!Criteria.isEmpty(criteria.getBid())) {
            String s = criteria.getBid().trim();
            if (s.startsWith("=")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.bid.eq(d));
            }
            if (s.startsWith(">")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.bid.goe(d));
            }
            if (s.startsWith("<")) {
                Integer d = Integer.parseInt(s.substring(1));
                predicates.add(pathBase.bid.loe(d));
            }
        }
        return ExpressionUtils.allOf(predicates);
    }
}
