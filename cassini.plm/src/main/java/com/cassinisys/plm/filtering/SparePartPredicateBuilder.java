package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.mro.MROWorkOrder;
import com.cassinisys.plm.model.mro.QMROSparePart;
import com.cassinisys.plm.repo.mro.MROAssetSparePartRepository;
import com.cassinisys.plm.repo.mro.MROWorkOrderPartRepository;
import com.cassinisys.plm.repo.mro.MROWorkOrderRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suresh Cassini on 18-11-2020.
 */
@Component
public class SparePartPredicateBuilder implements PredicateBuilder<SparePartCriteria, QMROSparePart> {

    @Autowired
    private MROWorkOrderPartRepository mroWorkOrderPartRepository;
    @Autowired
    private MROAssetSparePartRepository assetSparePartRepository;
    @Autowired
    private MROWorkOrderRepository mroWorkOrderRepository;

    @Override
    public Predicate build(SparePartCriteria sparePartCriteria, QMROSparePart pathBase) {
        return getDefaultPredicate(sparePartCriteria, pathBase);

    }

    private Predicate getDefaultPredicate(SparePartCriteria criteria, QMROSparePart pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                Predicate predicate = pathBase.type.name.containsIgnoreCase(s).
                        or(pathBase.number.containsIgnoreCase(s)).
                        or(pathBase.name.containsIgnoreCase(s)).
                        or(pathBase.description.containsIgnoreCase(s));
                predicates.add(predicate);
            }
        }

        if (!Criteria.isEmpty(criteria.getNumber())) {
            predicates.add(pathBase.number.containsIgnoreCase(criteria.getNumber()));
        }
        if (!Criteria.isEmpty(criteria.getPartType())) {
            predicates.add(pathBase.type.id.eq(criteria.getPartType()));
        }
        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }

        if (!Criteria.isEmpty(criteria.getWorkOrder())) {
            MROWorkOrder workOrder = mroWorkOrderRepository.findOne(criteria.getWorkOrder());
            List<Integer> sparePartIds = assetSparePartRepository.getSparePartIdsByAsset(workOrder.getAsset());
            predicates.add(pathBase.id.in(sparePartIds));
            sparePartIds = mroWorkOrderPartRepository.getSparePartIdsByWorkOrder(criteria.getWorkOrder());
            for (Integer sparePartId : sparePartIds) {
                predicates.add(pathBase.id.ne(sparePartId));
            }
        }
        if (!Criteria.isEmpty(criteria.getAsset())) {
            List<Integer> sparePartIds = assetSparePartRepository.getSparePartIdsByAsset(criteria.getAsset());
            for (Integer sparePartId : sparePartIds) {
                predicates.add(pathBase.id.ne(sparePartId));
            }
        }


        return ExpressionUtils.allOf(predicates);
    }
}