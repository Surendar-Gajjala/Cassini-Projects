package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.QBomGroup;
import com.cassinisys.drdo.repo.bom.BomItemRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 08-11-2018.
 */
@Component
public class BomGroupPredicateBuilder implements PredicateBuilder<BomGroupCriteria, QBomGroup> {

    @Autowired
    private BomItemRepository bomItemRepository;

    @Override
    public Predicate build(BomGroupCriteria criteria, QBomGroup pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getName())) {
            predicates.add(pathBase.name.containsIgnoreCase(criteria.getName())
                    .or(pathBase.code.containsIgnoreCase(criteria.getName())));
        }

        if (!Criteria.isEmpty(criteria.getType())) {
            if (criteria.getType().equals(BomItemType.SECTION.toString())) {

                List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(criteria.getBom());

                bomItems.forEach(bomItem -> {
                    if (!bomItem.getBomItemType().equals(BomItemType.PART)) {
                        predicates.add(pathBase.code.ne(bomItem.getTypeRef().getCode()));
                    }
                });

                predicates.add(pathBase.type.eq(BomItemType.valueOf(criteria.getType())));

            } else if (criteria.getType().equals(BomItemType.SUBSYSTEM.toString())) {
                List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(criteria.getBom());

                bomItems.forEach(bomItem -> {
                    if (!bomItem.getBomItemType().equals(BomItemType.PART)) {
                        predicates.add(pathBase.code.ne(bomItem.getTypeRef().getCode()));
                    }
                });

                predicates.add(pathBase.type.eq(BomItemType.valueOf(criteria.getType())));
            } else if (criteria.getType().equals(BomItemType.UNIT.toString())) {
                List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(criteria.getBom());

                bomItems.forEach(bomItem -> {
                    if (!bomItem.getBomItemType().equals(BomItemType.PART)) {
                        predicates.add(pathBase.code.ne(bomItem.getTypeRef().getCode()));
                    }
                });

                predicates.add(pathBase.type.eq(BomItemType.valueOf(criteria.getType())));
            }
        }

        return ExpressionUtils.allOf(predicates);
    }
}
