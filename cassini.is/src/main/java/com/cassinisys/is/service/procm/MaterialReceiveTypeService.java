package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISMaterialReceiveType;
import com.cassinisys.is.model.procm.ISMaterialReceiveTypeAttribute;
import com.cassinisys.is.repo.procm.MaterialReceiveTypeAttributeRepository;
import com.cassinisys.is.repo.procm.MaterialReceiveTypeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by swapna on 20/06/19.
 */
@Service
@Transactional
public class MaterialReceiveTypeService implements CrudService<ISMaterialReceiveType, Integer>,
        PageableService<ISMaterialReceiveType, Integer>, ClassificationTypeService<ISMaterialReceiveType, ISMaterialReceiveTypeAttribute> {

    @Autowired
    private MaterialReceiveTypeRepository materialReceiveTypeRepository;

    @Autowired
    private MaterialReceiveTypeAttributeRepository materialReceiveTypeAttributeRepository;

    @Transactional(readOnly = false)
    public ISMaterialReceiveType create(ISMaterialReceiveType itemType) {
        checkNotNull(itemType);
        itemType = materialReceiveTypeRepository.save(itemType);
        return itemType;
    }

    @Transactional(readOnly = false)
    public ISMaterialReceiveType update(ISMaterialReceiveType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        itemType = materialReceiveTypeRepository.save(itemType);
        return itemType;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISMaterialReceiveType itemType = materialReceiveTypeRepository.findOne(id);
        materialReceiveTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ISMaterialReceiveType get(Integer id) {
        checkNotNull(id);
        ISMaterialReceiveType itemType = materialReceiveTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveType> findMultiple(List<Integer> ids) {
        return materialReceiveTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveType> getRootTypes() {
        return materialReceiveTypeRepository.findByParentTypeIsNullOrderByName();
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveType> getChildren(Integer parent) {
        return materialReceiveTypeRepository.findByParentTypeOrderByName(parent);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveType> getClassificationTree() {
        List<ISMaterialReceiveType> types = getRootTypes();
        for (ISMaterialReceiveType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(ISMaterialReceiveType parent) {
        List<ISMaterialReceiveType> children = getChildren(parent.getId());
        for (ISMaterialReceiveType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISMaterialReceiveType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveType> getAll() {
        return materialReceiveTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ISMaterialReceiveType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return materialReceiveTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialReceiveTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return materialReceiveTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ISMaterialReceiveTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMaterialReceiveTypeAttribute> collector = new ArrayList<>();
        List<ISMaterialReceiveTypeAttribute> atts = materialReceiveTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<ISMaterialReceiveTypeAttribute> collector, Integer typeId) {
        ISMaterialReceiveType itemType = materialReceiveTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISMaterialReceiveTypeAttribute> atts = materialReceiveTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public ISMaterialReceiveTypeAttribute getAttribute(Integer id) {
        return materialReceiveTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public ISMaterialReceiveTypeAttribute createAttribute(ISMaterialReceiveTypeAttribute attribute) {
        return materialReceiveTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISMaterialReceiveTypeAttribute updateAttribute(ISMaterialReceiveTypeAttribute attribute) {
        return materialReceiveTypeAttributeRepository.save(attribute);

    }

    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        materialReceiveTypeAttributeRepository.delete(id);
    }

}
