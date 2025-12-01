package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mfr.QPLMSupplier;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.repo.pqm.PQMSupplierAuditPlanRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Component
public class PLMSupplierPredicateBuilder implements PredicateBuilder<PLMSupplierCriteria, QPLMSupplier> {

    @Autowired
    private PQMSupplierAuditPlanRepository supplierAuditPlanRepository;

    @Override
    public Predicate build(PLMSupplierCriteria supplierCriteria, QPLMSupplier pathBase) {
        return getDefaultPredicate(supplierCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(PLMSupplierCriteria criteria, QPLMSupplier pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.supplierType.name.containsIgnoreCase(s).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s)).
                        or(pathBase.address.containsIgnoreCase(s)).
                        or(pathBase.city.containsIgnoreCase(s)).
                        or(pathBase.country.containsIgnoreCase(s)).
                        or(pathBase.postalCode.containsIgnoreCase(s)).
                        or(pathBase.phoneNumber.containsIgnoreCase(s)).
                        or(pathBase.mobileNumber.containsIgnoreCase(s)).
                        or(pathBase.faxNumber.containsIgnoreCase(s)).
                        or(pathBase.email.containsIgnoreCase(s)).
                        or(pathBase.webSite.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getSupplierType())) {
            predicates.add(pathBase.supplierType.id.eq(criteria.getSupplierType()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getAddress())) {
            predicates.add(pathBase.address.containsIgnoreCase(criteria.getAddress()));
        }
        if (!Criteria.isEmpty(criteria.getCity())) {
            predicates.add(pathBase.city.containsIgnoreCase(criteria.getCity()));
        }
        if (!Criteria.isEmpty(criteria.getCountry())) {
            predicates.add(pathBase.country.containsIgnoreCase(criteria.getCountry()));
        }
        if (!Criteria.isEmpty(criteria.getPostalCode())) {
            predicates.add(pathBase.postalCode.containsIgnoreCase(criteria.getPostalCode()));
        }
        if (!Criteria.isEmpty(criteria.getPhoneNumber())) {
            predicates.add(pathBase.phoneNumber.containsIgnoreCase(criteria.getPhoneNumber()));
        }
        if (!Criteria.isEmpty(criteria.getMobileNumber())) {
            predicates.add(pathBase.mobileNumber.containsIgnoreCase(criteria.getMobileNumber()));
        }
        if (!Criteria.isEmpty(criteria.getFaxAddress())) {
            predicates.add(pathBase.faxNumber.containsIgnoreCase(criteria.getFaxAddress()));
        }

        if (!Criteria.isEmpty(criteria.getEmail())) {
            predicates.add(pathBase.email.containsIgnoreCase(criteria.getEmail()));
        }
        if (!Criteria.isEmpty(criteria.getWebSite())) {
            predicates.add(pathBase.webSite.containsIgnoreCase(criteria.getWebSite()));
        }

        if (!Criteria.isEmpty(criteria.getType())) {
            predicates.add(pathBase.supplierType.id.eq(criteria.getType()));
        }

        if (!Criteria.isEmpty(criteria.getAudit())) {
            predicates.add(pathBase.lifeCyclePhase.phaseType.eq(LifeCyclePhaseType.RELEASED));
            List<Integer> supplierIds = supplierAuditPlanRepository.getSupplierIdsByAudit(criteria.getAudit());
            if (supplierIds.size() > 0) {
                predicates.add(pathBase.id.notIn(supplierIds));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }
}