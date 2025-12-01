package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISMachineType;
import com.cassinisys.is.model.procm.ISMachineTypeAttribute;
import com.cassinisys.is.repo.procm.MachineTypeAttributeRepository;
import com.cassinisys.is.repo.procm.MachineTypeRepository;
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
public class MachineTypeService implements CrudService<ISMachineType, Integer>,
        PageableService<ISMachineType, Integer>, ClassificationTypeService<ISMachineType, ISMachineTypeAttribute> {

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private MachineTypeAttributeRepository machineTypeAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Override
    @Transactional(readOnly = false)
    public ISMachineType create(ISMachineType itemType) {
        checkNotNull(itemType);
        itemType = machineTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public ISMachineType update(ISMachineType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        itemType = machineTypeRepository.save(itemType);
        return itemType;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISMachineType itemType = machineTypeRepository.findOne(id);
        machineTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ISMachineType get(Integer id) {
        checkNotNull(id);
        ISMachineType itemType = machineTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @Transactional(readOnly = true)
    public List<ISMachineType> findMultiple(List<Integer> ids) {
        return machineTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMachineType> getRootTypes() {
        return machineTypeRepository.findByParentTypeIsNullOrderByName();
    }

    @Transactional(readOnly = true)
    public List<ISMachineType> getChildren(Integer parent) {
        return machineTypeRepository.findByParentTypeOrderByName(parent);
    }

    @Transactional(readOnly = true)
    public List<ISMachineType> getClassificationTree() {
        List<ISMachineType> types = getRootTypes();
        for (ISMachineType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(ISMachineType parent) {
        List<ISMachineType> children = getChildren(parent.getId());
        for (ISMachineType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISMachineType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ISMachineType> getAll() {
        return machineTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISMachineType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("id")));
        }
        return machineTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISMachineTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return machineTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ISMachineTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMachineTypeAttribute> collector = new ArrayList<>();
        List<ISMachineTypeAttribute> atts = machineTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<ISMachineTypeAttribute> collector, Integer typeId) {
        ISMachineType itemType = machineTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISMachineTypeAttribute> atts = machineTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public ISMachineTypeAttribute getAttribute(Integer id) {
        return machineTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public ISMachineTypeAttribute createAttribute(ISMachineTypeAttribute attribute) {
        return machineTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISMachineTypeAttribute updateAttribute(ISMachineTypeAttribute attribute) {
        return machineTypeAttributeRepository.save(attribute);

    }

    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        ISMachineTypeAttribute attribute = machineTypeAttributeRepository.findOne(id);
        machineTypeAttributeRepository.delete(id);
    }

    public List<ObjectTypeAttribute> getAllItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }
}
