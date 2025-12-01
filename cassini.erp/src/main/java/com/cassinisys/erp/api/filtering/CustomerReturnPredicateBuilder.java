package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.CustomerReturnCriteria;
import com.cassinisys.erp.model.crm.QERPCustomerReturn;
import com.cassinisys.erp.model.crm.ReturnStatus;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/26/15.
 */
@Component
public class CustomerReturnPredicateBuilder implements PredicateBuilder<CustomerReturnCriteria, QERPCustomerReturn>{

    @Override
    public Predicate build(CustomerReturnCriteria criteria, QERPCustomerReturn pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        Predicate p = Criteria.getDateRangePredicate(pathBase.returnDate, criteria.getReturnDate());
        if(p != null) {
            predicates.add(p);
        }
        if(!Criteria.isEmpty(criteria.getCustomer())) {
            predicates.add((pathBase.customer.name.containsIgnoreCase(criteria.getCustomer())));
        }
        if(!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add((pathBase.status.eq(ReturnStatus.valueOf(criteria.getStatus()))));
        }
        if(!Criteria.isEmpty(criteria.getRegion())) {
            predicates.add((pathBase.customer.salesRegion.name.containsIgnoreCase(criteria.getRegion())));
        }
        if(!Criteria.isEmpty(criteria.getDistrict())) {
            predicates.add((pathBase.customer.salesRegion.district.containsIgnoreCase(criteria.getDistrict())));
        }
        if(!Criteria.isEmpty(criteria.getSalesRep())) {
            predicates.add((pathBase.customer.salesRep.firstName.containsIgnoreCase(criteria.getSalesRep())));
        }
        if(!Criteria.isEmpty(criteria.getReason())) {
            predicates.add((pathBase.reason.containsIgnoreCase(criteria.getReason())));
        }

        return ExpressionUtils.allOf(predicates);
    }

}
