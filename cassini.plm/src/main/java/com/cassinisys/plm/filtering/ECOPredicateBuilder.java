package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMECO;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.service.wf.PLMWorkflowService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nageshreddy on 09-06-2016.
 */

@Component
public class ECOPredicateBuilder implements PredicateBuilder<ECOCriteria, QPLMECO> {

    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(ECOCriteria criteria, QPLMECO pathBase) {
        if (criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ECOCriteria criteria, QPLMECO pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.ecoNumber.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.title.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(ECOCriteria criteria, QPLMECO pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getEcoNumber())) {
            predicates.add(pathBase.ecoNumber.containsIgnoreCase(criteria.getEcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.containsIgnoreCase(criteria.getTitle()));
        }
        if (!Criteria.isEmpty(criteria.getEcoType())) {
            predicates.add(pathBase.ecoType.eq(Integer.parseInt(criteria.getEcoType())));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getEcoOwner())) {
            predicates.add(pathBase.ecoOwner.eq(Integer.parseInt(criteria.getEcoOwner())));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.CHANGE);
            predicates.add(pathBase.id.in(ids));
        }
        if (!Criteria.isEmpty(criteria.getStatusType())) {
            predicates.add(pathBase.statusType.eq(WorkflowStatusType.valueOf(criteria.getStatusType())));
        }
        return ExpressionUtils.allOf(predicates);
    }


    public Predicate getDefaultPredicates(ECOCriteria criteria, QPLMECO pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.ecoNumber.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.title.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }
        if (!Criteria.isEmpty(criteria.getEcoNumber())) {
            predicates.add(pathBase.ecoNumber.containsIgnoreCase(criteria.getEcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getTitle())) {
            predicates.add(pathBase.title.eq(criteria.getTitle()));
        }

        if (!Criteria.isEmpty(criteria.getStatusType())) {
            predicates.add(pathBase.statusType.eq(WorkflowStatusType.valueOf(criteria.getStatusType())));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
