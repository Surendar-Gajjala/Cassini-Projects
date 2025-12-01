package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.mfr.PLMSupplierAttribute;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.mfr.PLMSupplierTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.SupplierAttributeRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeRepository;
import com.cassinisys.plm.repo.mro.MROObjectAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by GSR on 03-06-2020.
 */
@Service
public class PLMSupplierTypeService implements CrudService<PLMSupplierType, Integer>,
        TypeSystem, ClassificationTypeService<PLMSupplierType, PLMSupplierTypeAttribute> {

    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MROObjectAttributeRepository mroObjectAttributeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierAttributeRepository supplierAttributeRepository;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void InitSupplierTypeService() {
        objectTypeService.registerTypeSystem("supplierType", new PLMSupplierTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#supplierType,'create')")
    public PLMSupplierType create(PLMSupplierType supplierType) {
        PLMSupplierType resourceType = supplierTypeRepository.save(supplierType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.SUPPLIERTYPE, resourceType));
        return resourceType;
    }

    @Override
    @PreAuthorize("hasPermission(#resourceType.id ,'edit')")
    public PLMSupplierType update(PLMSupplierType resourceType) {
        PLMSupplierType oldSupplierType = supplierTypeRepository.findOne(resourceType.getId());
        PLMSupplierType existingMfrType;
        if (resourceType.getParentType() == null) {
            existingMfrType = supplierTypeRepository.findByNameEqualsIgnoreCaseAndParentTypeIsNull(resourceType.getName());
        } else {
            existingMfrType = supplierTypeRepository.findByNameEqualsIgnoreCaseAndParentType(resourceType.getName(), resourceType.getParentType());
        }
        if ( existingMfrType != null && !resourceType.getId().equals(existingMfrType.getId())) {
            String message = messageSource.getMessage("name_already_exists", null, "{0} name already exist",LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingMfrType.getName());
            throw new CassiniException(result);    
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.SUPPLIERTYPE, oldSupplierType, resourceType));
        return supplierTypeRepository.save(resourceType);
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        supplierTypeRepository.delete(id);
    }

    @Override
    public PLMSupplierType get(Integer id) {
        return supplierTypeRepository.findOne(id);
    }

    @Override
    public List<PLMSupplierType> getAll() {
        return supplierTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierType> findMultipleTypes(List<Integer> ids) {
        return supplierTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierType> getTypeTree() {
        List<PLMSupplierType> types = supplierTypeRepository.findByParentTypeIsNullOrderByIdAsc();
        for (PLMSupplierType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Override
    public List<PLMSupplierType> getRootTypes() {
        return supplierTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @Override
    public List<PLMSupplierType> getChildren(Integer parent) {
        return supplierTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMSupplierType> getClassificationTree() {
        List<PLMSupplierType> types = getRootTypes();
        for (PLMSupplierType type : types) {
            visitResourceTypeChildren(type);
        }
        return types;
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierType> getTypeChildren(Integer id) {
        return supplierTypeRepository.findByParentTypeOrderByIdAsc(id);
    }

    private void visitResourceTypeChildren(PLMSupplierType parent) {
        List<PLMSupplierType> childrens = getTypeChildren(parent.getId());
        for (PLMSupplierType child : childrens) {
            visitResourceTypeChildren(child);
        }
        parent.setChildren(childrens);
    }

    @Override
    public PLMSupplierTypeAttribute createAttribute(PLMSupplierTypeAttribute attribute) {
        List<PLMSupplierTypeAttribute> qualityTypeAttributes = supplierTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (qualityTypeAttributes.size() >= 0) {
            attribute.setSeq(qualityTypeAttributes.size() + 1);
        }
        attribute = supplierTypeAttributeRepository.save(attribute);
        PLMSupplierType supplierType = supplierTypeRepository.findOne(attribute.getType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            List<PLMSupplier> suppliers = supplierRepository.findBySupplierType(supplierType);
            for (PLMSupplier supplier : suppliers) {
                PLMSupplierAttribute objectAttribute = new PLMSupplierAttribute();
                objectAttribute.setId(new ObjectAttributeId(supplier.getId(), attribute.getId()));
                supplierAttributeRepository.save(objectAttribute);
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.SUPPLIERTYPE, attribute));
        return attribute;

    }

    @Override
    public PLMSupplierTypeAttribute updateAttribute(PLMSupplierTypeAttribute attribute) {
        PLMSupplierTypeAttribute substanceGroupTypeAttribute = supplierTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.SUPPLIERTYPE, substanceGroupTypeAttribute, attribute));
        return supplierTypeAttributeRepository.save(attribute);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PLMSupplierTypeAttribute attribute = supplierTypeAttributeRepository.findOne(id);
        List<PLMSupplierTypeAttribute> substanceGroupTypeAttributes = supplierTypeAttributeRepository.findByTypeOrderBySeq(attribute.getType());
        if (substanceGroupTypeAttributes.size() > 0) {
            for (PLMSupplierTypeAttribute substanceGroupTypeAttribute : substanceGroupTypeAttributes) {
                if (substanceGroupTypeAttribute.getSeq() > attribute.getSeq()) {
                    substanceGroupTypeAttribute.setSeq(substanceGroupTypeAttribute.getSeq() - 1);
                    substanceGroupTypeAttribute = supplierTypeAttributeRepository.save(substanceGroupTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.SUPPLIERTYPE, attribute));
        supplierTypeAttributeRepository.delete(id);
    }

    @Override
    public PLMSupplierTypeAttribute getAttribute(Integer id) {
        return supplierTypeAttributeRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PLMSupplierTypeAttribute> getAttributes(Integer typeId, Boolean hier) {
        List<PLMSupplierTypeAttribute> supplierTypeAttributes = new ArrayList<>();
        if (!hier) {
            supplierTypeAttributes = supplierTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            supplierTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return supplierTypeAttributes;
    }

    @Transactional(readOnly = true)
    public List<PLMSupplierTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMSupplierTypeAttribute> collector = new ArrayList<>();
        List<PLMSupplierTypeAttribute> atts = supplierTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMSupplierTypeAttribute> collector, Integer typeId) {
        PLMSupplierType objectType = supplierTypeRepository.findOne(typeId);
        if (objectType != null) {
            Integer parentType = objectType.getParentType();
            if (parentType != null) {
                List<PLMSupplierTypeAttribute> atts = supplierTypeAttributeRepository.findByTypeOrderBySeq(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        supplierTypeRepository = webApplicationContext.getBean(SupplierTypeRepository.class);
        PLMSupplierType itemType = (PLMSupplierType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(supplierTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMSupplierType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        supplierTypeRepository = webApplicationContext.getBean(SupplierTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(supplierTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }


    private Boolean compareWithParent(PLMSupplierType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        supplierTypeRepository = webApplicationContext.getBean(SupplierTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(supplierTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PLMSupplierTypeAttribute> getTypeAttributes(Integer typeId, Boolean hier) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        supplierTypeAttributeRepository = webApplicationContext.getBean(SupplierTypeAttributeRepository.class);
        supplierTypeRepository = webApplicationContext.getBean(SupplierTypeRepository.class);
        List<PLMSupplierTypeAttribute> supplierTypeAttributes = new ArrayList<>();
        if (!hier) {
            supplierTypeAttributes = supplierTypeAttributeRepository.findByTypeOrderBySeq(typeId);
        } else {
            supplierTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return supplierTypeAttributes;
    }

    @Transactional(readOnly = true)
    public Integer getObjectsByType(Integer id) {
        Integer count = 0;
        PLMSupplierType supplierType = supplierTypeRepository.findOne(id);
        count = supplierRepository.findBySupplierType(supplierType).size();
        return count;
    }

}

