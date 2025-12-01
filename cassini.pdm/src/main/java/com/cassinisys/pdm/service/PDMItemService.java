package com.cassinisys.pdm.service;

import com.cassinisys.pdm.filtering.ItemAdvancedCriteria;
import com.cassinisys.pdm.filtering.ItemCriteria;
import com.cassinisys.pdm.filtering.ItemPredicateBuilder;
import com.cassinisys.pdm.filtering.ParameterCriteria;
import com.cassinisys.pdm.model.*;
import com.cassinisys.pdm.model.QPDMItem;
import com.cassinisys.pdm.repo.ItemAttributeRepository;
import com.cassinisys.pdm.repo.ItemRepository;
import com.cassinisys.pdm.repo.ItemTypeRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMItemService implements CrudService<PDMItem, Integer>{

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemAttributeRepository itemAttributeRepository;

    @Autowired
    private ItemPredicateBuilder predicateBuilder;

    @Autowired
    private ItemAdvancedCriteria itemAdvancedCriteria;

    @Override
    public PDMItem create(PDMItem pdmItem) {
        checkNotNull(pdmItem);
        return itemRepository.save(pdmItem);
    }

    public PDMItemAttribute createAttribute(PDMItemAttribute itemAttribute) {
        checkNotNull(itemAttribute);
        return itemAttributeRepository.save(itemAttribute);
    }

    public void saveItemAttributes(List<PDMItemAttribute> attributes) {

        for(PDMItemAttribute attribute : attributes){
            itemAttributeRepository.save(attribute);
        }
    }
    @Override
    public PDMItem update(PDMItem pdmItem) {
        checkNotNull(pdmItem);
        return itemRepository.save(pdmItem);
    }

    public PDMItemAttribute updateAttribute(PDMItemAttribute itemAttribute) {
        checkNotNull(itemAttribute);
        itemAttribute = itemAttributeRepository.save(itemAttribute);
        return itemAttribute;
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PDMItem pdmItem = itemRepository.findOne(id);
        if(pdmItem == null){
            throw new ResourceNotFoundException();
        }
        itemRepository.delete(id);
    }

    @Override
    public PDMItem get(Integer id) {
        checkNotNull(id);
        return itemRepository.findOne(id);
    }

    public Page<PDMItem> getItemsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();

        PDMItemType type = itemTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return itemRepository.getByItemTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, PDMItemType type) {
        if (type != null) {
            collector.add(type.getId());

            List<PDMItemType> children = itemTypeRepository.findByParentTypeOrderByCreatedDateAsc(type.getId());
            for (PDMItemType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Override
    public List<PDMItem> getAll() {
        return itemRepository.findAll();
    }

    public List<PDMItemAttribute> getItemAttributes(Integer itemId) {
        return itemAttributeRepository.findByItemId(itemId);
    }
    public Page<PDMItem> findAll(Pageable pageable){
        return itemRepository.findAll(pageable);
    }

    public Page<PDMItem> freeTextSearch(Pageable pageable, ItemCriteria itemCriteria){
        Predicate predicate = predicateBuilder.build(itemCriteria, QPDMItem.pDMItem);
        return itemRepository.findAll(predicate,pageable);
    }

    public Page<PDMItem> searchItems(Predicate predicate, Pageable pageable) {
        return itemRepository.findAll(predicate, pageable);
    }

    public Page<PDMItem> advancedSearchItem(ParameterCriteria[] parameterCriterias, Pageable pageable) {

        TypedQuery<PDMItem> typedQuery = itemAdvancedCriteria.getItemTypeQuery(parameterCriterias);
        typedQuery.setFirstResult(pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());
        Page<PDMItem> resultlist1 = new PageImpl<PDMItem>(typedQuery.getResultList());
        return resultlist1;
    }
}
