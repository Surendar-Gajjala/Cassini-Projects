package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.plm.RelationshipService;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Component
public class ItemPredicateBuilder implements PredicateBuilder<ItemCriteria, QPLMItem> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private RelatedItemRepository relatedItemRepository;

    private Predicate getPredicate(ItemCriteria criteria, QPLMItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (!Criteria.isEmpty(criteria.getItemClass())) {
                    Predicate predicate = pathBase.itemNumber.containsIgnoreCase(s).
                            or(pathBase.itemType.name.containsIgnoreCase(s)).
                            or(pathBase.itemName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s))
                            .and(pathBase.itemType.itemClass.eq(criteria.getItemClass()));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.itemNumber.containsIgnoreCase(s).
                            or(pathBase.itemType.name.containsIgnoreCase(s)).
                            or(pathBase.itemName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }

        return ExpressionUtils.allOf(predicates);
    }

    @Override
    public Predicate build(ItemCriteria criteria, QPLMItem pathBase) {
        return getDefaultPredicate(criteria, pathBase);
    }

    private Predicate getDefaultPredicate(ItemCriteria criteria, QPLMItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            String[] arr = criteria.getSearchQuery().split(" ");
            for (String s : arr) {
                if (!Criteria.isEmpty(criteria.getItemClass())) {
                    Predicate predicate = pathBase.itemNumber.containsIgnoreCase(s).
                            or(pathBase.itemType.name.containsIgnoreCase(s)).
                            or(pathBase.itemName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s))
                            .and(pathBase.itemType.itemClass.eq(criteria.getItemClass()));
                    predicates.add(predicate);
                } else {
                    Predicate predicate = pathBase.itemNumber.containsIgnoreCase(s).
                            or(pathBase.itemType.name.containsIgnoreCase(s)).
                            or(pathBase.itemName.containsIgnoreCase(s)).
                            or(pathBase.description.containsIgnoreCase(s));
                    predicates.add(predicate);
                }
            }
        }

        if (!Criteria.isEmpty(criteria.getItemNumber())) {
            predicates.add(pathBase.itemNumber.containsIgnoreCase(criteria.getItemNumber()));
        }
        if (!Criteria.isEmpty(criteria.getDescription())) {
            predicates.add(pathBase.description.containsIgnoreCase(criteria.getDescription()));
        }
        if (!Criteria.isEmpty(criteria.getItemName())) {
            predicates.add(pathBase.itemName.containsIgnoreCase(criteria.getItemName()));
        }
        if (!Criteria.isEmpty(criteria.getItemType())) {
            PLMItemType plmItemType = itemTypeRepository.findOne(criteria.getItemType());
            predicates.add(pathBase.itemType.eq(plmItemType));
        }

        if (!Criteria.isEmpty(criteria.getLatestRevision())) {
            predicates.add(pathBase.latestRevision.eq(parseInt(criteria.getLatestRevision())));
        }

        if (!Criteria.isEmpty(criteria.getItemClass())) {
            predicates.add(pathBase.itemType.itemClass.eq(criteria.getItemClass()));
        }

        if (criteria.getType() != null) {
            predicates.add(pathBase.itemType.id.in(itemTypeService.getAllSubTypeIds(criteria.getType())));
        }

        if (!Criteria.isEmpty(criteria.getPhase())) {
            List<Integer> ids = itemRevisionRepository.getRevisionIdsByPhase(criteria.getPhase());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getRevision())) {
            List<Integer> ids = itemRevisionRepository.getRevisionIdsByRevision(criteria.getRevision());
            predicates.add(pathBase.latestRevision.in(ids));
        }

        if (!Criteria.isEmpty(criteria.getRelationShip()) && !Criteria.isEmpty(criteria.getRelationShipRevision())) {
            PLMRelationship relationship = relationshipRepository.findOne(criteria.getRelationShip());
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(criteria.getRelationShipRevision());
            PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
            PLMItemType fromType = itemTypeRepository.findOne(plmItem.getItemType().getId());

            List<Integer> itemTypeIds = new ArrayList<>();
            itemTypeIds.add(relationship.getToType().getId());
            itemTypeIds = relationshipService.getItemTypeChildrens(relationship.getToType(), itemTypeIds);
            if (fromType.getId().equals(relationship.getFromType().getId())) {
                predicates.add(pathBase.itemType.id.in(itemTypeIds));
            } else {
                predicates.add(pathBase.itemType.id.eq(relationship.getFromType().getId()));
            }
            List<Integer> ids = new ArrayList<>();
            ids.add(plmItem.getId());
            List<PLMRelatedItem> relatedItems = relatedItemRepository.findByFromItem(criteria.getRelationShipRevision());
            List<PLMRelatedItem> fromRelatedItems = relatedItemRepository.findByToItem(plmItemRevision);
            relatedItems.forEach(relatedItem -> {
                ids.add(relatedItem.getToItem().getItemMaster());
            });

            fromRelatedItems.forEach(relatedItem -> {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(relatedItem.getFromItem());
                ids.add(itemRevision.getItemMaster());
            });

            if (ids.size() > 0) {
                predicates.add(pathBase.id.notIn(ids));
            }
        }

        return ExpressionUtils.allOf(predicates);
    }


    public Predicate getPredicates(ItemCriteria criteria, QPLMItem pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            Predicate predicate = pathBase.itemNumber.containsIgnoreCase(criteria.getSearchQuery()).
                    or(pathBase.itemType.name.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.itemName.containsIgnoreCase(criteria.getSearchQuery())).
                    or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery()))
                    .and(pathBase.itemType.id.in(itemTypeService.getAllSubTypeIds(criteria.getType())));
            predicates.add(predicate);
        } else {
            predicates.add(pathBase.itemType.id.eq(criteria.getType()));
        }
        return ExpressionUtils.allOf(predicates);
    }

}
