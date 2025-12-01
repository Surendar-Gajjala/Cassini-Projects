package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.repo.procm.*;
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

/**
 * The class is for ItemTypeService
 */
@Service
@Transactional
public class ItemTypeService implements CrudService<ISItemType, Integer>,
        PageableService<ISItemType, Integer> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;

    /**
     * The method used to create ISItemType
     **/
    @Override
    @Transactional(readOnly = false)
    public ISItemType create(ISItemType itemType) {
        checkNotNull(itemType);
        itemType = itemTypeRepository.save(itemType);
        return itemType;
    }

    /**
     * The method used to update ISItemType
     **/
    @Override
    @Transactional(readOnly = false)
    public ISItemType update(ISItemType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        ResourceType resourceType = itemType.getResourceType();
        if (resourceType == null) {
            ResourceType resourceType1 = itemTypeRepository.findOne(itemType.getParentType()).getResourceType();
            itemType.setResourceType(resourceType1);

        }
        itemType = itemTypeRepository.save(itemType);
        return itemType;
    }

    /**
     * The method used to delete itemType  ISItemType
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISItemType itemType = itemTypeRepository.findOne(id);
        itemTypeRepository.delete(id);

    }

    /**
     * The method used to get ISItemType
     **/
    @Transactional(readOnly = true)
    @Override
    public ISItemType get(Integer id) {
        checkNotNull(id);
        ISItemType itemType = itemTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    /**
     * The method used to findMultiple for the list of ISItemType
     **/
    @Transactional(readOnly = true)
    public List<ISItemType> findMultiple(List<Integer> ids) {
        return itemTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialType> getMultipleMaterialTypes(List<Integer> ids) {
        return materialTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMachineType> getMultipleMachineTypes(List<Integer> ids) {
        return machineTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> getMultipleManpowerTypes(List<Integer> ids) {
        return manpowerTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISItemType> getResourceType(ResourceType resourceType) {
        List<ISItemType> itemType = itemTypeRepository.findByResourceType(resourceType);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    /**
     * The method used to getRootTypes for the list of ISItemType
     **/
    @Transactional(readOnly = true)
    public List<ISItemType> getRootTypes() {
        return itemTypeRepository.findByParentTypeIsNullOrderByCreatedDateAsc();
    }

    /**
     * The method used to getChildren for the list of ISItemType
     **/
    @Transactional(readOnly = true)
    public List<ISItemType> getChildren(Integer parent) {
        return itemTypeRepository.findByParentTypeOrderByCreatedDateAsc(parent);
    }

    /**
     * The method used to getClassificationTree for the list of ISItemType
     **/
    @Transactional(readOnly = true)
    public List<ISItemType> getClassificationTree() {
        List<ISItemType> types = getRootTypes();
        for (ISItemType type : types) {
            visitChildren(type);
        }
        return types;
    }

    /**
     * The method used to visitChildren
     **/
    private void visitChildren(ISItemType parent) {
        List<ISItemType> children = getChildren(parent.getId());
        for (ISItemType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    /**
     * The method used to getAllSubTypes for the list of type Integer
     **/
    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISItemType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    /**
     * The method used to getAll for the list of ISItemType
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISItemType> getAll() {
        return itemTypeRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISItemType
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<ISItemType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return itemTypeRepository.findAll(pageable);
    }

    /**
     * The method used to getAttributes for the list of ISItemTypeAttribute
     **/
    @Transactional(readOnly = true)
    public List<ISItemTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return itemTypeAttributeRepository.findByItemType(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    /**
     * The method used to getAttributesFromHierarchy for the list of ISItemTypeAttribute
     **/
    private List<ISItemTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISItemTypeAttribute> collector = new ArrayList<>();
        List<ISItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemType(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarch(collector, typeId);
        return collector;
    }

    /**
     * The method used to collectAttributesFromHierarch
     **/
    private void collectAttributesFromHierarch(List<ISItemTypeAttribute> collector, Integer typeId) {
        ISItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemType(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarch(collector, parentType);
            }
        }
    }

    /**
     * The method used to getAttribute for  ISItemTypeAttribute
     **/
    @Transactional(readOnly = true)
    public ISItemTypeAttribute getAttribute(Integer id) {
        return itemTypeAttributeRepository.findOne(id);
    }

    /**
     * The method used to createAttribute for ISItemTypeAttribute
     **/
    @Transactional(readOnly = false)
    public ISItemTypeAttribute createAttribute(ISItemTypeAttribute attribute) {
        return itemTypeAttributeRepository.save(attribute);
    }

    /**
     * The method used to updateAttribute for ISItemTypeAttribute
     **/
    @Transactional(readOnly = false)
    public ISItemTypeAttribute updateAttribute(ISItemTypeAttribute attribute) {
        return itemTypeAttributeRepository.save(attribute);
    }

    /**
     * The method used to deleteAttribute
     **/
    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        itemTypeAttributeRepository.delete(id);
    }

}
