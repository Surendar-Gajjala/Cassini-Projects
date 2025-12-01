package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mro.MROAssetMeter;
import com.cassinisys.plm.model.mro.QMROMeter;
import com.cassinisys.plm.repo.mro.MROAssetMeterRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 18-11-2020.
 */
@Component
public class MeterPredicateBuilder implements PredicateBuilder<MeterCriteria, QMROMeter> {

    @Autowired
    private MROAssetMeterRepository assetMeterRepository;

    @Override
    public Predicate build(MeterCriteria meterCriteria, QMROMeter pathBase) {
        return getDefaultPredicate(meterCriteria, pathBase);
    }

    private Predicate getDefaultPredicate(MeterCriteria criteria, QMROMeter pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.name.containsIgnoreCase(s).
                        or(pathBase.description.containsIgnoreCase(s).or(pathBase.number.containsIgnoreCase(s)));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getAsset())) {
            List<Integer> itemSpecs = assetMeterRepository.getMeterIdsByAsset(criteria.asset);
            for (Integer itemSpecification : itemSpecs) {
                MROAssetMeter astMeter = assetMeterRepository.findOne(itemSpecification);
                predicates.add(pathBase.id.ne(astMeter.getMeter()));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }

}
