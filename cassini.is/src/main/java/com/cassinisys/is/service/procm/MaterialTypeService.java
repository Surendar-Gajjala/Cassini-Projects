package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISMaterialType;
import com.cassinisys.is.model.procm.ISMaterialTypeAttribute;
import com.cassinisys.is.repo.procm.MaterialTypeAttributeRepository;
import com.cassinisys.is.repo.procm.MaterialTypeRepository;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class MaterialTypeService implements CrudService<ISMaterialType, Integer>,
        PageableService<ISMaterialType, Integer>, ClassificationTypeService<ISMaterialType, ISMaterialTypeAttribute> {

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private MaterialTypeAttributeRepository materialTypeAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Override
    @Transactional(readOnly = false)
    public ISMaterialType create(ISMaterialType itemType) {
        checkNotNull(itemType);
        itemType = materialTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public ISMaterialType update(ISMaterialType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        itemType = materialTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISMaterialType itemType = materialTypeRepository.findOne(id);
        materialTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ISMaterialType get(Integer id) {
        checkNotNull(id);
        ISMaterialType itemType = materialTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialType> findMultiple(List<Integer> ids) {
        return materialTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialType> getRootTypes() {
        return materialTypeRepository.findByParentTypeIsNullOrderByName();
    }

    @Transactional(readOnly = true)
    public List<ISMaterialType> getChildren(Integer parent) {
        return materialTypeRepository.findByParentTypeOrderByName(parent);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialType> getClassificationTree() {
        List<ISMaterialType> types = getRootTypes();
        for (ISMaterialType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(ISMaterialType parent) {
        List<ISMaterialType> children = getChildren(parent.getId());
        for (ISMaterialType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISMaterialType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ISMaterialType> getAll() {
        return materialTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISMaterialType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return materialTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return materialTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ISMaterialTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMaterialTypeAttribute> collector = new ArrayList<>();
        List<ISMaterialTypeAttribute> atts = materialTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<ISMaterialTypeAttribute> collector, Integer typeId) {
        ISMaterialType itemType = materialTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISMaterialTypeAttribute> atts = materialTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public ISMaterialTypeAttribute getAttribute(Integer id) {
        return materialTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public ISMaterialTypeAttribute createAttribute(ISMaterialTypeAttribute attribute) {
        return materialTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISMaterialTypeAttribute updateAttribute(ISMaterialTypeAttribute attribute) {
        return materialTypeAttributeRepository.save(attribute);

    }

    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        ISMaterialTypeAttribute attribute = materialTypeAttributeRepository.findOne(id);
        materialTypeAttributeRepository.delete(id);
    }

    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getObjectTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getMaterialTypeAttributesRequiredFalse(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectTypeAndRequiredFalse(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getMaterialTypeAttributesRequiredTrue(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return typeAttributes;
    }
}
