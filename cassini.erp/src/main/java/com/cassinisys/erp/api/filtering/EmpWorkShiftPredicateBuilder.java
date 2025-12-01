package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.api.criteria.EmpWorkShiftCriteria;
import com.cassinisys.erp.model.crm.QERPCustomer;
import com.cassinisys.erp.model.production.QERPWorkShiftEmployee;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakshmi on 2/10/2016.
 */
@Component
public class EmpWorkShiftPredicateBuilder  implements PredicateBuilder<EmpWorkShiftCriteria, QERPWorkShiftEmployee>{

    @Override
        public Predicate build(EmpWorkShiftCriteria criteria, QERPWorkShiftEmployee pathBase) {
                return getDefaultPredicate(criteria, pathBase);

        }

         private Predicate getDefaultPredicate(EmpWorkShiftCriteria criteria, QERPWorkShiftEmployee pathBase) {
            List<Predicate> predicates = new ArrayList<>();

            if(!Criteria.isEmpty(criteria.getShift())) {
                predicates.add((pathBase.shiftId.eq(criteria.getShift())));
            }

            if(!Criteria.isEmpty(criteria.getEmployee())) {
                predicates.add(Criteria.getMultiplePredicates(pathBase.employee.firstName, criteria.getEmployee()));
            }

            return ExpressionUtils.allOf(predicates);
        }
}

