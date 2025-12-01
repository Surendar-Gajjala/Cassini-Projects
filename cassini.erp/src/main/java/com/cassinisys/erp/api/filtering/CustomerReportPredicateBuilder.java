package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.CustomerReportCriteria;
import com.cassinisys.erp.model.crm.QERPSalesRepFieldReport;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/14/15.
 */
@Component
public class CustomerReportPredicateBuilder implements PredicateBuilder<CustomerReportCriteria, QERPSalesRepFieldReport> {

    @Override
    public Predicate build(CustomerReportCriteria criteria, QERPSalesRepFieldReport pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getSalesRep())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.salesRep.firstName, criteria.getSalesRep()));
        }
        if(!Criteria.isEmpty(criteria.getCustomer())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.customer.name, criteria.getCustomer()));
        }
        if(!Criteria.isEmpty(criteria.getNotes())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.notes, criteria.getNotes()));
        }
        Predicate p = Criteria.getDateRangePredicate(pathBase.timestamp, criteria.getTimestamp());
        if(p != null) {
            predicates.add(p);
        }

        return ExpressionUtils.allOf(predicates);
    }
}
