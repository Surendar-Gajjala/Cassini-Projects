package com.cassinisys.plm.filtering;


import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mes.MESWorkCenterOperation;
import com.cassinisys.plm.model.mes.QMESOperation;
import com.cassinisys.plm.repo.mes.BOPRouteOperationRepository;
import com.cassinisys.plm.repo.mes.MESWorkCenterOperationRepo;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 29-10-2020.
 */
@Component
public class OperationPredicateBuilder implements PredicateBuilder<OperationCriteria, QMESOperation> {
    @Autowired
    private BOPRouteOperationRepository bopRouteOperationRepository;
    @Autowired
    private MESWorkCenterOperationRepo workCenterOperationRepo;

    @Override
    public Predicate build(OperationCriteria operationCriteria, QMESOperation pathBase) {
        return getDefaultPredicate(operationCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(OperationCriteria criteria, QMESOperation pathBase) {
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
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getBop())) {
            if (!Criteria.isEmpty(criteria.getBopPlan())) {
                List<Integer> ids = bopRouteOperationRepository.getOperationIdsByParent(criteria.getBopPlan());
                if (ids.size() > 0) {
                    predicates.add(pathBase.id.notIn(ids));
                }
            } else {
                List<Integer> ids = bopRouteOperationRepository.getOperationIdsByBopAndParentIsNull(criteria.getBop());
                if (ids.size() > 0) {
                    predicates.add(pathBase.id.notIn(ids));
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getWorkCenter())) {
            List<MESWorkCenterOperation> workCenterOperations = workCenterOperationRepo.findByWorkCenter(criteria.getWorkCenter());
            for (MESWorkCenterOperation workcentrOperation : workCenterOperations) {
                predicates.add(pathBase.id.ne(workcentrOperation.getOperation()));
            }
        }

        return ExpressionUtils.allOf(predicates);
    }
}
