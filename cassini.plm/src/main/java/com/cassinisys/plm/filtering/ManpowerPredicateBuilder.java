package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mes.MESBOPRouteOperation;
import com.cassinisys.plm.model.mes.QMESManpower;
import com.cassinisys.plm.repo.mes.BOPOperationResourceRepository;
import com.cassinisys.plm.repo.mes.BOPRouteOperationRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manipal CassiniPLM on 27-10-2020.
 */
@Component
public class ManpowerPredicateBuilder implements PredicateBuilder<ManpowerCriteria, QMESManpower> {
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private BOPOperationResourceRepository bopOperationResourceRepository;

    @Override
    public Predicate build(ManpowerCriteria manpowerCriteria, QMESManpower pathBase) {
        return getDefaultPredicate(manpowerCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(ManpowerCriteria criteria, QMESManpower pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.type.id.eq(criteria.getType()));
            if (!Criteria.isEmpty(criteria.getBopRoute())) {
                MESBOPRouteOperation plan = bopRouteOperationRepository.findOne(criteria.getBopRoute());
                List<Integer> ids = bopOperationResourceRepository.getResourceIdsByPlanAndOperationAndResourceType(criteria.getBopRoute(), plan.getOperation(), criteria.getType());
                if (ids.size() > 0) {
                    predicates.add(pathBase.id.notIn(ids));
                }
            }
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}