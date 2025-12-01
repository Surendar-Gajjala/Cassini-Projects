package com.cassinisys.erp.api.filtering;

import com.cassinisys.erp.model.api.criteria.SalesRegionCriteria;
import com.cassinisys.erp.model.crm.QERPSalesRegion;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/22/15.
 */
@Component
public class SalesRegionPredicateBuilder implements PredicateBuilder<SalesRegionCriteria, QERPSalesRegion>{

    @Override
    public Predicate build(SalesRegionCriteria criteria, QERPSalesRegion pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if(criteria.getName() != null) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if(criteria.getDistrict() != null) {
            predicates.add(pathBase.district.containsIgnoreCase(criteria.getDistrict()));
        }
        if(criteria.getState() != null) {
            predicates.add(pathBase.state.name.containsIgnoreCase(criteria.getState()));
        }
        if(criteria.getCountry() != null) {
            predicates.add(pathBase.country.name.containsIgnoreCase(criteria.getCountry()));
        }

        return ExpressionUtils.allOf(predicates);
    }
}
