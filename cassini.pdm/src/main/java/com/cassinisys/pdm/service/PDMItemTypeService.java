package com.cassinisys.pdm.service;

import com.cassinisys.pdm.model.PDMItemType;
import com.cassinisys.pdm.model.PDMItemTypeAttribute;
import com.cassinisys.pdm.repo.ItemTypeAttributeRepository;
import com.cassinisys.pdm.repo.ItemTypeRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMItemTypeService implements CrudService<PDMItemType, Integer> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;

    @Override
    public PDMItemType create(PDMItemType itemType) {
        checkNotNull(itemType);
        itemType.setId(null);
        return itemTypeRepository.save(itemType);
    }

    public PDMItemTypeAttribute createType(PDMItemTypeAttribute typeAttribute) {
        checkNotNull(typeAttribute);
        return itemTypeAttributeRepository.save(typeAttribute);
    }

    @Override
    public PDMItemType update(PDMItemType itemType) {
        checkNotNull(itemType);
        return itemTypeRepository.save(itemType);
    }

    public PDMItemTypeAttribute updateType(PDMItemTypeAttribute typeAttribute) {
        checkNotNull(typeAttribute);
        return itemTypeAttributeRepository.save(typeAttribute);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        PDMItemType itemType = itemTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        itemTypeRepository.delete(id);
    }

    public void deleteType(Integer id) {
        checkNotNull(id);
        PDMItemTypeAttribute typeAttribute = itemTypeAttributeRepository.findOne(id);
        if (typeAttribute == null) {
            throw new ResourceNotFoundException();
        }
        itemTypeAttributeRepository.delete(id);
    }

    @Override
    public PDMItemType get(Integer id) {
        checkNotNull(id);
        return itemTypeRepository.findOne(id);
    }

    @Override
    public List<PDMItemType> getAll() {
        return itemTypeRepository.findAll();
    }

    public Page<PDMItemType> findAll(Pageable pageable) {
        return itemTypeRepository.findAll(pageable);
    }

    public List<PDMItemTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return itemTypeAttributeRepository.findByItemType(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<PDMItemTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PDMItemTypeAttribute> collector = new ArrayList<>();

        List<PDMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemType(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);

        return collector;
    }

    private void collectAttributesFromHierarchy(List<PDMItemTypeAttribute> collector, Integer typeId) {
        PDMItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PDMItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemType(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }
    public List<PDMItemType> getClassificationTree() {
        List<PDMItemType> types = getRootTypes();

        for (PDMItemType type : types) {
            visitChildren(type);
        }

        return types;
    }

    public List<PDMItemType> getRootTypes() {
        return itemTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    private void visitChildren(PDMItemType parent) {
        List<PDMItemType> children = getChildren(parent.getId());

        for (PDMItemType child : children) {
            visitChildren(child);
        }

        parent.setChildren(children);
    }

    public List<PDMItemType> getChildren(Integer parent) {
        return itemTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }
}
