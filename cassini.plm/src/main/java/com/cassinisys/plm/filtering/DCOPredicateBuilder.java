package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMDCO;
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
public class DCOPredicateBuilder implements PredicateBuilder<DCOCriteria, QPLMDCO> {

    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(DCOCriteria criteria, QPLMDCO pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(DCOCriteria criteria, QPLMDCO pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.dcoNumber.containsIgnoreCase(s).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getDcoType())) {
            predicates.add(pathBase.dcoType.eq(criteria.getDcoType()));
        }

        if (!Criteria.isEmpty(criteria.getDcoNumber())) {
            predicates.add(pathBase.dcoNumber.containsIgnoreCase(criteria.getDcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.eq(criteria.getTitle()));
        }
        if (!Criteria.isEmpty(criteria.getChangeAnalyst())) {
            predicates.add(pathBase.changeAnalyst.in(criteria.getChangeAnalyst()));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.CHANGE);
            predicates.add(pathBase.id.in(ids));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
