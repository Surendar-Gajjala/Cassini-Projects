package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.CustomerOrderCriteria;
import com.cassinisys.erp.model.crm.CustomerOrderStatus;
import com.cassinisys.erp.model.crm.OrderType;
import com.cassinisys.erp.model.crm.QERPCustomerOrder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/26/15.
 */
@Component
public class CustomerOrderPredicateBuilder implements PredicateBuilder<CustomerOrderCriteria, QERPCustomerOrder> {

    @Override
    public Predicate build(CustomerOrderCriteria criteria, QERPCustomerOrder pathBase) {
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

    private Predicate getFreeTextSearchPredicate(CustomerOrderCriteria criteria, QERPCustomerOrder pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getSearchQuery() != null) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.orderNumber.containsIgnoreCase(s).
                        or(pathBase.customer.name.containsIgnoreCase(s)).
                        or(pathBase.shipTo.containsIgnoreCase(s)).
                        or(pathBase.customer.salesRep.firstName.containsIgnoreCase(s)).
                        or(pathBase.customer.salesRegion.name.containsIgnoreCase(s)).
                        or(pathBase.shipments.any().invoiceNumber.containsIgnoreCase(s)).
                        or(pathBase.shipments.any().consignment.number.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(CustomerOrderCriteria criteria, QERPCustomerOrder pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getCustomerId())) {
            try {
                Integer customerId = Integer.parseInt(criteria.getCustomerId().trim());
                predicates.add(pathBase.customer.id.eq(customerId));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (!Criteria.isEmpty(criteria.getPoNumber())) {
            predicates.add((pathBase.poNumber.containsIgnoreCase(criteria.getPoNumber())));
        }

        if (!Criteria.isEmpty(criteria.getOrderNumber())) {
            predicates.add((pathBase.orderNumber.containsIgnoreCase(criteria.getOrderNumber())));
        }

        Predicate p = Criteria.getDatePredicate(pathBase.orderedDate, criteria.getOrderedDate());
        if (p != null) {
            predicates.add(p);
        }

        p = Criteria.getDatePredicate(pathBase.modifiedDate, criteria.getModifiedDate());
        if (p != null) {
            predicates.add(p);

        }
        p = Criteria.getDatePredicate(pathBase.deliveryDate, criteria.getDeliveryDate());
        if (p != null) {
            predicates.add(p);
        }
        if (!Criteria.isEmpty(criteria.getCustomer())) {
            predicates.add((pathBase.customer.name.containsIgnoreCase(criteria.getCustomer())));
        }
        if (!Criteria.isEmpty(criteria.getShipTo())) {
            predicates.add((pathBase.shipTo.containsIgnoreCase(criteria.getShipTo())));
        }
        if (!Criteria.isEmpty(criteria.getRegion())) {
            predicates.add((pathBase.customer.salesRegion.name.containsIgnoreCase(criteria.getRegion())));
        }
        if (!Criteria.isEmpty(criteria.getSalesRep())) {
            predicates.add((pathBase.customer.salesRep.firstName.containsIgnoreCase(criteria.getSalesRep())));
        }
        if (!Criteria.isEmpty(criteria.getOrderTotal())) {
            String s = criteria.getOrderTotal().trim();
            if (s.startsWith("=")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.orderTotal.eq(d));
            }
            if (s.startsWith(">")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.orderTotal.goe(d));
            }
            if (s.startsWith("<")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.orderTotal.loe(d));
            }
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            String s = criteria.getStatus().trim();
            String arr[] = s.split(",");
            List<Predicate> statuses = new ArrayList<>();

            for (String status : arr) {
                statuses.add(pathBase.status.eq(CustomerOrderStatus.valueOf(status.trim())));
            }
            predicates.add(ExpressionUtils.anyOf(statuses));
        }
        if (!Criteria.isEmpty(criteria.getOrderType())) {
            predicates.add(pathBase.orderType.eq(OrderType.valueOf(criteria.getOrderType().trim())));
        }
        if (!Criteria.isEmpty(criteria.getInvoiceNumber())) {
            predicates.add(pathBase.shipments.any().invoiceNumber.containsIgnoreCase(criteria.getInvoiceNumber()));
        }
        if (!Criteria.isEmpty(criteria.getTrackingNumber())) {
            predicates.add(pathBase.shipments.any().consignment.number.containsIgnoreCase(criteria.getTrackingNumber()));
        }

        return ExpressionUtils.allOf(predicates);
    }

}
