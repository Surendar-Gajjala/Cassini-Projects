package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.pqm.QPQMSupplierAudit;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierAuditPredicateBuilder implements PredicateBuilder<SupplierAuditCriteria, QPQMSupplierAudit> {
    @Override
    public Predicate build(SupplierAuditCriteria PPAPCriteria, QPQMSupplierAudit pathBase) {
        return getDefaultPredicate(PPAPCriteria, pathBase);
    }

    private Predicate getDefaultPredicate(SupplierAuditCriteria criteria, QPQMSupplierAudit pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s))
                        .or(pathBase.plannedYear.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getPlannedYear())) {
            predicates.add(pathBase.plannedYear.containsIgnoreCase(criteria.getPlannedYear()));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
