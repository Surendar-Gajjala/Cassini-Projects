package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.bom.BomInstanceItem;
import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.QBomInstanceItem;
import com.cassinisys.drdo.repo.bom.BomInstanceItemRepository;
import com.cassinisys.drdo.repo.bom.BomInstanceRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Component
public class BomInstanceItemSearchPredicateBuilder implements PredicateBuilder<BomSearchCriteria, QBomInstanceItem> {

    @Autowired
    private BomInstanceRepository bomInstanceRepository;

    @Autowired
    private BomInstanceItemRepository bomInstanceItemRepository;

    @Override
    public Predicate build(BomSearchCriteria criteria, QBomInstanceItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] strs = criteria.getSearchQuery().split(" ");
            for (String str : strs) {
                predicates.add(pathBase.item.itemMaster.itemName.containsIgnoreCase(str)
                        .or(pathBase.item.itemMaster.description.containsIgnoreCase(str))
                        .or(pathBase.item.itemMaster.itemCode.containsIgnoreCase(str))
                        .and(pathBase.bomItemType.eq(BomItemType.PART)));
            }
        }
        List<Predicate> predicates1 = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getBom())) {
            BomInstance bomInstance = bomInstanceRepository.findOne(criteria.getBom());

            if (!Criteria.isEmpty(criteria.getUnit())) {
                List<Integer> ints = new ArrayList<>();
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(criteria.getUnit());
                ints = visitChildren(bomInstanceItem, ints);
                predicates1.add(pathBase.id.in(ints));
            } else if (!Criteria.isEmpty(criteria.getSubsystem())) {
                List<Integer> ints = new ArrayList<>();
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(criteria.getSubsystem());
                ints = visitChildren(bomInstanceItem, ints);
                predicates1.add(pathBase.id.in(ints));
            } else if (!Criteria.isEmpty(criteria.getSection())) {
                List<Integer> ints = new ArrayList<>();
                BomInstanceItem bomInstanceItem = bomInstanceItemRepository.findOne(criteria.getSection());
                ints = visitChildren(bomInstanceItem, ints);
                predicates1.add(pathBase.id.in(ints));

            } else {
                List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByBomOrderByCreatedDateAsc(bomInstance.getId());
                List<Integer> ints = new ArrayList<>();
                for (BomInstanceItem bomInstanceItem : bomInstanceItems) {
                    if (bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {
                        ints.add(bomInstanceItem.getId());
                    }
                    ints = visitChildren(bomInstanceItem, ints);
                }
                predicates1.add(pathBase.id.in(ints));
            }
        }
        if (predicates1.size() == 0) {
            predicates = new ArrayList<>();
        }

        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.anyOf(predicates1));
    }

    private List<Integer> visitChildren(BomInstanceItem bomInstanceItem, List<Integer> ints) {

        if (!bomInstanceItem.getBomItemType().equals(BomItemType.PART)) {

            List<BomInstanceItem> bomInstanceItems = bomInstanceItemRepository.findByParentOrderByCreatedDateAsc(bomInstanceItem.getId());
            for (BomInstanceItem instanceItem : bomInstanceItems) {
                if (instanceItem.getBomItemType().equals(BomItemType.PART)) {
                    ints.add(instanceItem.getId());
                }
                ints = visitChildren(instanceItem, ints);
            }
        }

        return ints;
    }
}
