package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.ShipmentCriteria;
import com.cassinisys.erp.model.crm.QERPCustomerOrderShipment;
import com.cassinisys.erp.model.crm.ShipmentStatus;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 10/14/15.
 */
@Component
public class ShipmentPredicateBuilder implements PredicateBuilder<ShipmentCriteria, QERPCustomerOrderShipment> {

    @Override
    public Predicate build(ShipmentCriteria criteria, QERPCustomerOrderShipment pathBase) {
        if (criteria.isFreeTextSearch()) {
            Predicate searchPredicate = getFreeTextSearchPredicate(criteria, pathBase);
            Predicate predicate = searchPredicate;

            if (criteria.hasAnyFilters()) {
                predicate = ExpressionUtils.allOf(searchPredicate, getDefaultPredicate(criteria, pathBase));
            }

            return predicate;
        } else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(ShipmentCriteria criteria, QERPCustomerOrderShipment pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.order.orderNumber.containsIgnoreCase(s).
                        or(pathBase.order.customer.name.containsIgnoreCase(s)).
                        or(pathBase.order.shipTo.containsIgnoreCase(s)).
                        or(pathBase.invoiceNumber.containsIgnoreCase(s)).
                        or(pathBase.order.poNumber.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }


    public Predicate getDefaultPredicate(ShipmentCriteria criteria, QERPCustomerOrderShipment pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getInvoiceNumber())) {
            predicates.add((pathBase.invoiceNumber.containsIgnoreCase(criteria.getInvoiceNumber())));
        }
        Predicate p = Criteria.getDateRangePredicate(pathBase.createdDate, criteria.getDate());
        if(p != null) {
            predicates.add(p);
        }
        if(!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(ShipmentStatus.valueOf(criteria.getStatus())));
        }
        if(!Criteria.isEmpty(criteria.getOrderNumber())) {
            predicates.add((pathBase.order.orderNumber.containsIgnoreCase(criteria.getOrderNumber())));
        }
        if(!Criteria.isEmpty(criteria.getPoNumber())) {
            predicates.add((pathBase.order.poNumber.containsIgnoreCase(criteria.getPoNumber())));
        }
        if(!Criteria.isEmpty(criteria.getCustomer())) {
            predicates.add((pathBase.order.customer.name.containsIgnoreCase(criteria.getCustomer())));
        }
        if(!Criteria.isEmpty(criteria.getShipTo())) {
            predicates.add((pathBase.order.shipTo.containsIgnoreCase(criteria.getShipTo())));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
