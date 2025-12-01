package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISManpowerType;
import com.cassinisys.is.model.procm.ISManpowerTypeAttribute;
import com.cassinisys.is.repo.procm.ManpowerTypeAttributeRepository;
import com.cassinisys.is.repo.procm.ManpowerTypeRepository;
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
public class ManpowerTypeService implements CrudService<ISManpowerType, Integer>,
        PageableService<ISManpowerType, Integer>, ClassificationTypeService<ISManpowerType, ISManpowerTypeAttribute> {

    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;

    @Autowired
    private ManpowerTypeAttributeRepository manpowerTypeAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Override
    @Transactional(readOnly = false)
    public ISManpowerType create(ISManpowerType itemType) {
        checkNotNull(itemType);
        itemType = manpowerTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public ISManpowerType update(ISManpowerType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        itemType = manpowerTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISManpowerType itemType = manpowerTypeRepository.findOne(id);
        manpowerTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ISManpowerType get(Integer id) {
        checkNotNull(id);
        ISManpowerType itemType = manpowerTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> findMultiple(List<Integer> ids) {
        return manpowerTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> getRootTypes() {
        return manpowerTypeRepository.findByParentTypeIsNullOrderByName();
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> getChildren(Integer parent) {
        return manpowerTypeRepository.findByParentTypeOrderByName(parent);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> getClassificationTree() {
        List<ISManpowerType> types = getRootTypes();
        for (ISManpowerType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(ISManpowerType parent) {
        List<ISManpowerType> children = getChildren(parent.getId());
        for (ISManpowerType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISManpowerType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ISManpowerType> getAll() {
        return manpowerTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISManpowerType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return manpowerTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return manpowerTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ISManpowerTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISManpowerTypeAttribute> collector = new ArrayList<>();
        List<ISManpowerTypeAttribute> atts = manpowerTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<ISManpowerTypeAttribute> collector, Integer typeId) {
        ISManpowerType itemType = manpowerTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISManpowerTypeAttribute> atts = manpowerTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public ISManpowerTypeAttribute getAttribute(Integer id) {
        return manpowerTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public ISManpowerTypeAttribute createAttribute(ISManpowerTypeAttribute attribute) {
        return manpowerTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISManpowerTypeAttribute updateAttribute(ISManpowerTypeAttribute attribute) {
        return manpowerTypeAttributeRepository.save(attribute);

    }

    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        ISManpowerTypeAttribute attribute = manpowerTypeAttributeRepository.findOne(id);
        manpowerTypeAttributeRepository.delete(id);
    }

    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }
}
