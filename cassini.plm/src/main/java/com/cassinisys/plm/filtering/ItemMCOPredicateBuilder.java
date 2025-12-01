package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.cm.QPLMItemMCO;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.MCORepository;
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
public class ItemMCOPredicateBuilder implements PredicateBuilder<ItemMCOCriteria, QPLMItemMCO> {

    @Autowired
    private PLMWorkflowService workflowService;

    @Override
    public Predicate build(ItemMCOCriteria criteria, QPLMItemMCO pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ItemMCOCriteria criteria, QPLMItemMCO pathBase) {
        List<Predicate> predicates = new ArrayList<>();


        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.mcoNumber.containsIgnoreCase(s).
                        or(pathBase.title.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.mcoType.name.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getMcoNumber())) {
            predicates.add(pathBase.mcoNumber.containsIgnoreCase(criteria.getMcoNumber()));
        }
        if (!Criteria.isEmpty(criteria.getMcoType())) {
            predicates.add(pathBase.mcoType.id.eq(criteria.getMcoType()));
        }
        if (!Criteria.isEmpty(criteria.getChangeAnalyst())) {
            predicates.add(pathBase.changeAnalyst.in(criteria.getChangeAnalyst()));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            List<Integer> ids = workflowService.getWorkflowAttachedToIdsByStatusAndType(criteria.getStatus(), PLMObjectType.ITEMMCO);
            predicates.add(pathBase.id.in(ids));
        }
        return ExpressionUtils.allOf(predicates);
    }
}
