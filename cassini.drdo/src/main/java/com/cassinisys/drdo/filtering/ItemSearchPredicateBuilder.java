package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 09-10-2018.
 */
@Component
public class ItemSearchPredicateBuilder implements PredicateBuilder<ItemSearchCriteria, QItem> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private BomItemRepository bomItemRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Override
    public Predicate build(ItemSearchCriteria criteria, QItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.itemCode.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.itemType.name.containsIgnoreCase(criteria.getSearchQuery())));
        }

        List<Predicate> predicates1 = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getItem())) {
            ItemRevision itemRevision = itemRevisionRepository.findOne(criteria.getItem());
            predicates1.add(pathBase.id.ne(itemRevision.getItemMaster().getId()));

            /*-------  Removing System Type Items  ------------------------*/

            ItemType itemType = itemTypeRepository.findByName("System");

            if (itemType != null && itemType.getParentNode()) {
                visitItemTypeChildren(itemType, pathBase, predicates1);
                List<Item> items = itemRepository.findByItemType(itemType);
                for (Item item : items) {
                    predicates1.add(pathBase.id.ne(item.getId()));
                }
            }

            /*-------  Removing Selected Item Children  ------------------------*/

            Bom bom = bomRepository.findByItem(itemRevision);
            if (bom != null) {
                List<BomItem> bomItems = bomItemRepository.findByBomOrderByCreatedDateAsc(bom.getId());

                for (BomItem bomItem : bomItems) {
                    if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                        predicates1.add(pathBase.id.ne(bomItem.getItem().getItemMaster().getId()));
                    }
                }
            }

        }

        /*-----------  To Remove selected BomItem Children  ---------------------*/

        if (!Criteria.isEmpty(criteria.getSelectedItem())) {
            List<BomItem> bomItems = bomItemRepository.findByParentOrderByCreatedDateAsc(criteria.getSelectedItem());
            bomItems.forEach(bomItem -> {
                if (bomItem.getBomItemType().equals(BomItemType.PART)) {
                    predicates1.add(pathBase.id.ne(bomItem.getItem().getItemMaster().getId()));
                }
            });
        }


        return ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.allOf(predicates1));
    }

    private void removeItemParents(Bom bom, QItem pathBase, List<Predicate> predicates1) {
        ItemRevision itemRevision = itemRevisionRepository.findOne(bom.getItem().getId());
        List<BomItem> bomItems1 = bomItemRepository.findByItem(itemRevision);

        for (BomItem bomItem : bomItems1) {
            Bom bom1 = bomRepository.findOne(bomItem.getBom());
            if (bom1 != null) {
                predicates1.add(pathBase.id.ne(bomItem.getItem().getItemMaster().getId()));
                removeItemParents(bom1, pathBase, predicates1);
            }
        }
    }

    private void visitItemTypeChildren(ItemType itemType, QItem pathBase, List<Predicate> predicates) {
        List<ItemType> itemTypes = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemType.getId());
        if (itemTypes.size() > 0) {
            for (ItemType itemType1 : itemTypes) {
                predicates.add(pathBase.itemType.id.ne(itemType1.getId()));
                visitItemTypeChildren(itemType1, pathBase, predicates);
            }
        }
    }
}
