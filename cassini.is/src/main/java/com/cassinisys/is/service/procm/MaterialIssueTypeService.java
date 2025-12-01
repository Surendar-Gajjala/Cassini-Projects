package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.procm.ISMaterialIssueType;
import com.cassinisys.is.model.procm.ISMaterialIssueTypeAttribute;
import com.cassinisys.is.repo.procm.IssueTypeAttributeRepository;
import com.cassinisys.is.repo.procm.MaterialIssueTypeRepository;
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
public class MaterialIssueTypeService implements CrudService<ISMaterialIssueType, Integer>,
        PageableService<ISMaterialIssueType, Integer>, ClassificationTypeService<ISMaterialIssueType, ISMaterialIssueTypeAttribute> {

    @Autowired
    private MaterialIssueTypeRepository issueTypeRepository;

    @Autowired
    private IssueTypeAttributeRepository issueTypeAttributeRepository;

    @Transactional(readOnly = false)
    public ISMaterialIssueType create(ISMaterialIssueType itemType) {
        checkNotNull(itemType);
        itemType = issueTypeRepository.save(itemType);
        return itemType;
    }

    @Transactional(readOnly = false)
    public ISMaterialIssueType update(ISMaterialIssueType itemType) {
        checkNotNull(itemType);
        checkNotNull(itemType.getId());
        itemType = issueTypeRepository.save(itemType);
        return itemType;
    }

    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISMaterialIssueType itemType = issueTypeRepository.findOne(id);
        issueTypeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ISMaterialIssueType get(Integer id) {
        checkNotNull(id);
        ISMaterialIssueType itemType = issueTypeRepository.findOne(id);
        if (itemType == null) {
            throw new ResourceNotFoundException();
        }
        return itemType;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueType> findMultiple(List<Integer> ids) {
        return issueTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueType> getRootTypes() {
        return issueTypeRepository.findByParentTypeIsNullOrderByName();
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueType> getChildren(Integer parent) {
        return issueTypeRepository.findByParentTypeOrderByName(parent);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueType> getClassificationTree() {
        List<ISMaterialIssueType> types = getRootTypes();
        for (ISMaterialIssueType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(ISMaterialIssueType parent) {
        List<ISMaterialIssueType> children = getChildren(parent.getId());
        for (ISMaterialIssueType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public List<Integer> getAllSubTypes(Integer parent) {
        List<Integer> subtypes = new ArrayList<>();
        subtypes.add(parent);
        List<ISMaterialIssueType> children = getChildren(parent);
        children.forEach(item -> {
            List<Integer> childSubtypes = getAllSubTypes(item.getId());
            subtypes.addAll(childSubtypes);
        });
        return subtypes;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueType> getAll() {
        return issueTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ISMaterialIssueType> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return issueTypeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialIssueTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return issueTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ISMaterialIssueTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ISMaterialIssueTypeAttribute> collector = new ArrayList<>();
        List<ISMaterialIssueTypeAttribute> atts = issueTypeAttributeRepository.findByItemTypeOrderByIdAsc(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<ISMaterialIssueTypeAttribute> collector, Integer typeId) {
        ISMaterialIssueType itemType = issueTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ISMaterialIssueTypeAttribute> atts = issueTypeAttributeRepository.findByItemTypeOrderByIdAsc(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    public ISMaterialIssueTypeAttribute getAttribute(Integer id) {
        return issueTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = false)
    public ISMaterialIssueTypeAttribute createAttribute(ISMaterialIssueTypeAttribute attribute) {
        return issueTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISMaterialIssueTypeAttribute updateAttribute(ISMaterialIssueTypeAttribute attribute) {
        return issueTypeAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public void deleteAttribute(Integer id) {
        issueTypeAttributeRepository.delete(id);
    }
}
