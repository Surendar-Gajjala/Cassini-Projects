package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.filtering.ItemCriteria;
import com.cassinisys.plm.filtering.ItemPredicateBuilder;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.*;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by subramanyamreddy on 011 11-Jan -18.
 */
@Service
@Transactional
public class RelationshipService implements CrudService<PLMRelationship, Integer> {

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelatedItemRepository relatedItemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemRevisionRepository itemRevisionRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemPredicateBuilder itemPredicateBuilder;

    @Override
    public PLMRelationship create(PLMRelationship relationship) {
        return relationshipRepository.save(relationship);
    }

    public PLMRelationship getRelationshipByName(PLMRelationship relationship) {
        return relationshipRepository.findByNameEqualsIgnoreCase(relationship.getName());
    }

    @Override
    public PLMRelationship update(PLMRelationship relationship) {
        return relationshipRepository.save(relationship);
    }

    @Override
    public void delete(Integer id) {
        relationshipRepository.delete(id);
    }

    @Override
    public PLMRelationship get(Integer id) {
        return relationshipRepository.findOne(id);
    }

    @Override
    public List<PLMRelationship> getAll() {
        return relationshipRepository.findAll();
    }

    public Page<PLMRelationship> getAllRelationship(Pageable pageable) {
        return relationshipRepository.findAll(pageable);
    }

    public List<PLMRelationship> getRelationshipsByFromType(Integer typeId) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        List<PLMRelationship> relationshipLIst = relationshipRepository.findByFromType(itemType);
        HashMap<Integer, PLMRelationship> relationshipMap = new HashMap<>();
        relationshipLIst.forEach(plmRelationship -> {
            relationshipMap.put(plmRelationship.getId(), plmRelationship);
        });

        List<PLMRelationship> relationships = relationshipRepository.findByToType(itemType);
        relationships.forEach(plmRelationship -> {
            PLMRelationship relationship = relationshipMap.get(plmRelationship.getId());
            if (relationship == null) {
                relationshipLIst.add(plmRelationship);
            }
        });
        return relationshipLIst;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getRelationshipsByFromTypeAndFromItem(Integer typeId, Integer revisionId) {
        PLMItemType itemType = itemTypeRepository.findOne(typeId);
        List<PLMItem> relatedItemList = new ArrayList<>();
        List<PLMItem> items = itemRepository.findByItemType(itemType);
        List<PLMRelatedItem> relatedItems = relatedItemRepository.findByFromItem(revisionId);
        for (PLMItem item : items) {
            Boolean exist = false;
            for (PLMRelatedItem relatedItem : relatedItems) {
                if (item.getId().equals(relatedItem.getToItem().getItemMaster()) || item.getLatestRevision().equals(revisionId)) {
                    exist = true;
                }
            }
            if (!exist && !item.getConfigured()) {
                relatedItemList.add(item);
            }
        }
        return relatedItemList;
    }

    @Transactional(readOnly = true)
    public Page<PLMItem> getItemByRelationshipAndFromItem(Integer relationshipId, Integer revisionId, Pageable pageable, ItemCriteria itemCriteria) {
        itemCriteria.setRelationShipRevision(revisionId);
        itemCriteria.setRelationShip(relationshipId);
        Predicate predicate = itemPredicateBuilder.build(itemCriteria, QPLMItem.pLMItem);
        Page<PLMItem> items = itemRepository.findAll(predicate, pageable);
        return items;
    }

    @Transactional(readOnly = true)
    public List<Integer> getItemTypeChildrens(PLMItemType parent, List<Integer> itemTypeIds) {
        List<PLMItemType> childrens = itemTypeRepository.findByParentTypeOrderByIdAsc(parent.getId());
        for (PLMItemType child : childrens) {
            getItemTypeChildrens(child, itemTypeIds);
            itemTypeIds.add(child.getId());
        }
        return itemTypeIds;
    }

    @Transactional(readOnly = true)
    public PLMRelationship getRelationshipsByFromTypeAndFromType(Integer fromTypeId, Integer toTypeId) {
        PLMItemType fromType = itemTypeRepository.findOne(fromTypeId);
        PLMItemType toType = itemTypeRepository.findOne(toTypeId);
        PLMRelationship relationship = relationshipRepository.findByFromTypeAndToType(fromType, toType);
        if (relationship == null) {
            relationship = relationshipRepository.findByToTypeAndFromType(fromType, toType);
        }
        return relationship;
    }
}
