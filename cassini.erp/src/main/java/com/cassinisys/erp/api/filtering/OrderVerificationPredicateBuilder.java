package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.OrderVerificationCriteria;
import com.cassinisys.erp.model.crm.CustomerOrderStatus;
import com.cassinisys.erp.model.crm.OrderVerificationStatus;
import com.cassinisys.erp.model.crm.QERPOrderVerification;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 18/03/16.
 */
@Component
public class OrderVerificationPredicateBuilder implements PredicateBuilder<OrderVerificationCriteria, QERPOrderVerification> {

    @Override
    public Predicate build(OrderVerificationCriteria criteria, QERPOrderVerification pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getOrderNumber())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.order.orderNumber, criteria.getOrderNumber()));
        }

        if(!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.order.status.eq(CustomerOrderStatus.valueOf(criteria.getStatus())));
        }

        if(!Criteria.isEmpty(criteria.getPoNumber())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.order.poNumber, criteria.getPoNumber()));
        }

        if(!Criteria.isEmpty(criteria.getCustomer())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.order.customer.name, criteria.getCustomer()));
        }

        if(!Criteria.isEmpty(criteria.getAssignedTo())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.verifiedBy.firstName, criteria.getAssignedTo()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
