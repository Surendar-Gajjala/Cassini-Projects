package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.ConsignmentCriteria;
import com.cassinisys.erp.model.crm.ConsignmentStatus;
import com.cassinisys.erp.model.crm.QERPConsignment;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 10/14/15.
 */
@Component
public class ConsignmentPredicateBuilder implements PredicateBuilder<ConsignmentCriteria, QERPConsignment> {
    @Override
    public Predicate build(ConsignmentCriteria criteria, QERPConsignment pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add((pathBase.number.containsIgnoreCase(criteria.getNumber())));
        }
        if(!Criteria.isEmpty(criteria.getConsignee())) {
            predicates.add((pathBase.consignee.containsIgnoreCase(criteria.getConsignee())));
        }
        if(!Criteria.isEmpty(criteria.getShipper())) {
            predicates.add((pathBase.shipper.name.containsIgnoreCase(criteria.getShipper())));
        }
        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(pathBase.status.eq(ConsignmentStatus.valueOf(criteria.getStatus())));
        }
        if(!Criteria.isEmpty(criteria.getOrderNumber())) {
            predicates.add(pathBase.shipments.any().order.orderNumber.containsIgnoreCase(criteria.getOrderNumber()));
        }
        if(!Criteria.isEmpty(criteria.getInvoiceNumber())) {
            predicates.add(pathBase.shipments.any().invoiceNumber.containsIgnoreCase(criteria.getInvoiceNumber()));
        }
        if(!Criteria.isEmpty(criteria.getConfirmationNumber())) {
            predicates.add((pathBase.confirmationNumber.containsIgnoreCase(criteria.getConfirmationNumber())));
        }
        if(!Criteria.isEmpty(criteria.getVehicle())) {
            predicates.add((pathBase.vehicle.number.containsIgnoreCase(criteria.getVehicle())));
        }
        if(!Criteria.isEmpty(criteria.getDriver())) {
            predicates.add((pathBase.driver.firstName.containsIgnoreCase(criteria.getDriver())));
        }
        if(!Criteria.isEmpty(criteria.getPoNumber())) {
            predicates.add(pathBase.shipments.any().order.poNumber.containsIgnoreCase(criteria.getPoNumber()));
        }
        if(!Criteria.isEmpty(criteria.getThrough())) {
            predicates.add((pathBase.through.containsIgnoreCase(criteria.getThrough())));
        }
        if(!Criteria.isEmpty(criteria.getShippedTo())) {
            predicates.add((pathBase.shippingAddress.city.containsIgnoreCase(criteria.getShippedTo())));
        }
        if(!Criteria.isEmpty(criteria.getValue())) {
            String s = criteria.getValue().trim();
            if(s.startsWith("=")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.value.eq(d));
            }
            if(s.startsWith(">")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.value.goe(d));
            }
            if(s.startsWith("<")) {
                Double d = Double.parseDouble(s.substring(1));
                predicates.add(pathBase.value.loe(d));
            }
        }
        Predicate p = Criteria.getDateRangePredicate(pathBase.shippedDate, criteria.getShippedDate());
        if(p != null) {
            predicates.add(p);
        }

        return ExpressionUtils.allOf(predicates);
    }
}
