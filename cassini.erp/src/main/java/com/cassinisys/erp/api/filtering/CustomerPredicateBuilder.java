package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.CustomerCriteria;
import com.cassinisys.erp.model.crm.QERPCustomer;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/24/15.
 */
@Component
public class CustomerPredicateBuilder implements PredicateBuilder<CustomerCriteria, QERPCustomer>{

    @Override
    public Predicate build(CustomerCriteria criteria, QERPCustomer pathBase) {
        if(criteria.isFreeTextSearch()) {
            return getFreeTextSearchPredicate(criteria, pathBase);
        }
        else {
            return getDefaultPredicate(criteria, pathBase);
        }
    }

    private Predicate getFreeTextSearchPredicate(CustomerCriteria criteria, QERPCustomer pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(criteria.getSearchQuery() != null) {
            String [] arr = criteria.getSearchQuery().split(" ");
            for(String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s);
                /*
                    or(pathBase.salesRegion.name.containsIgnoreCase(s)).
                    orAllOf(pathBase.salesRep.isNotNull(), pathBase.salesRep.firstName.containsIgnoreCase(s)).
                    orAllOf(pathBase.contactPerson.isNotNull(), pathBase.contactPerson.firstName.containsIgnoreCase(s)).
                    orAllOf(pathBase.contactPerson.isNotNull(), pathBase.contactPerson.phoneMobile.containsIgnoreCase(s)).
                    orAllOf(pathBase.contactPerson.isNotNull(), pathBase.contactPerson.phoneOffice.containsIgnoreCase(s)).
                    orAllOf(pathBase.officePhone.isNotNull(), pathBase.officePhone.containsIgnoreCase(s));
                 */
                predicates.add(predicate);
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    private Predicate getDefaultPredicate(CustomerCriteria criteria, QERPCustomer pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(!Criteria.isEmpty(criteria.getName())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.name, criteria.getName()));
        }

        if(criteria.isBlacklisted()) {
            predicates.add(pathBase.blacklisted.eq(criteria.isBlacklisted()));
        }

        if(!Criteria.isEmpty(criteria.getRegion())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.salesRegion.name, criteria.getRegion()));
        }

        if(!Criteria.isEmpty(criteria.getState())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.salesRegion.state.name, criteria.getState()));
        }

        if(!Criteria.isEmpty(criteria.getSalesRep())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.salesRep.firstName, criteria.getSalesRep()));
        }

        if(!Criteria.isEmpty(criteria.getContactPerson())) {
            predicates.add(Criteria.getMultiplePredicates(pathBase.contactPerson.firstName, criteria.getContactPerson()));
        }

        if(!Criteria.isEmpty(criteria.getContactPhone())) {
            String s = criteria.getContactPhone();
            Predicate predicate = pathBase.contactPerson.phoneMobile.containsIgnoreCase(s).
                    or(pathBase.contactPerson.phoneOffice.containsIgnoreCase(s)).
                    or(pathBase.officePhone.containsIgnoreCase(s));

            predicates.add(predicate);
        }
        if (!Criteria.isEmpty(criteria.getCustomerType())){
            predicates.add(criteria.getMultiplePredicates(pathBase.customerType.name,criteria.getCustomerType()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
