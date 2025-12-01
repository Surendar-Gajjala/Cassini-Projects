package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.mfr.PLMManufacturerTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.ManufacturerAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerTypeRepository;
import com.cassinisys.plm.service.plm.ItemTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 6/13/17.
 */
@Service
public class ManufacturerTypeService implements CrudService<PLMManufacturerType, Integer>,
        TypeSystem, ClassificationTypeService<PLMManufacturerType, PLMManufacturerTypeAttribute> {
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ManufacturerAttributeRepository manufacturerAttributeRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @PostConstruct
    public void InitMfrTypeService() {
        objectTypeService.registerTypeSystem("manufacturerType", new ManufacturerTypeService());
    }


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmManufacturerType,'create')")
    public PLMManufacturerType create(PLMManufacturerType plmManufacturerType) {
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.save(plmManufacturerType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MANUFACTURERTYPE, manufacturerType));
        return manufacturerType;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#plmManufacturerType.id ,'edit')")
    public PLMManufacturerType update(PLMManufacturerType plmManufacturerType) {
        checkNotNull(plmManufacturerType);
        checkNotNull(plmManufacturerType.getId());
        PLMManufacturerType oldManufacturerType = manufacturerTypeRepository.findOne(plmManufacturerType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MANUFACTURERTYPE, oldManufacturerType, plmManufacturerType));
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.save(plmManufacturerType);
        return manufacturerType;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.findOne(id);
        if (manufacturerType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.MANUFACTURERTYPE, manufacturerType));
        }
        manufacturerTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerType get(Integer id) {
        return manufacturerTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerType> getAll() {
        return manufacturerTypeRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerType> getRootTypes() {
        return manufacturerTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    public List<PLMManufacturerType> getChildren(Integer parent) {
        return manufacturerTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerType> getClassificationTree() {
        List<PLMManufacturerType> types = getRootTypes();
        for (PLMManufacturerType type : types) {
            visitChildren(type);
        }
        return types;
    }

    public List<PLMManufacturerType> getAllManufacturerTypesWithAttributes() {
        List<PLMManufacturerType> types = getAll();
        for (PLMManufacturerType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(manufacturerTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(manufacturerTypeAttributeRepository.findByMfrTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(PLMManufacturerType parent) {
        List<PLMManufacturerType> children = getChildren(parent.getId());
        for (PLMManufacturerType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional
    public PLMManufacturerTypeAttribute createAttribute(PLMManufacturerTypeAttribute attribute) {
        List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrType(attribute.getMfrType());
        if (manufacturerTypeAttributes.size() >= 0) {
            attribute.setSeq(manufacturerTypeAttributes.size() + 1);
        }
        PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(attribute);
        PLMManufacturerType manufacturerType = manufacturerTypeRepository.findOne(attribute.getMfrType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            List<PLMManufacturer> manufacturers = manufacturerRepository.findByMfrType(manufacturerType);
            for (PLMManufacturer manufacturer : manufacturers) {
                PLMManufacturerAttribute objectAttribute = new PLMManufacturerAttribute();
                objectAttribute.setId(new ObjectAttributeId(manufacturer.getId(), attribute.getId()));
                manufacturerAttributeRepository.save(objectAttribute);
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.MANUFACTURERTYPE, plmManufacturerTypeAttribute));
        return plmManufacturerTypeAttribute;
    }

    @Transactional
    public PLMManufacturerTypeAttribute updateAttribute(PLMManufacturerTypeAttribute attribute) {
        PLMManufacturerTypeAttribute plmManufacturerTypeAttribute1 = manufacturerTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.MANUFACTURERTYPE, plmManufacturerTypeAttribute1, attribute));
        PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(attribute);
        return plmManufacturerTypeAttribute;
    }

    @Override
    @Transactional
    public void deleteAttribute(Integer id) {
        PLMManufacturerTypeAttribute attribute = manufacturerTypeAttributeRepository.findOne(id);
        List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrType(attribute.getMfrType());
        if (manufacturerTypeAttributes.size() > 0) {
            for (PLMManufacturerTypeAttribute manufacturerTypeAttribute : manufacturerTypeAttributes) {
                if (manufacturerTypeAttribute.getSeq() > attribute.getSeq()) {
                    manufacturerTypeAttribute.setSeq(manufacturerTypeAttribute.getSeq() - 1);
                    manufacturerTypeAttribute = manufacturerTypeAttributeRepository.save(manufacturerTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.MANUFACTURERTYPE, attribute));
        manufacturerTypeAttributeRepository.delete(id);
    }

    public PLMManufacturerTypeAttribute getAttribute(Integer id) {
        return manufacturerTypeAttributeRepository.findOne(id);
    }

    public List<PLMManufacturerTypeAttribute> getTypeAttributes(Integer id) {
        return manufacturerTypeAttributeRepository.findByMfrTypeOrderByName(id);
    }

    public List<PLMManufacturerTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        List<PLMManufacturerTypeAttribute> manufacturerTypeAttributes = new ArrayList<>();
        if (!hierarchy) {
            manufacturerTypeAttributes = manufacturerTypeAttributeRepository.findByMfrTypeOrderBySeq(typeId);
            manufacturerTypeAttributes.forEach(manufacturerTypeAttribute -> {
                if (manufacturerTypeAttribute.getRefSubType() != null) {
                    manufacturerTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(manufacturerTypeAttribute.getRefType().name(), manufacturerTypeAttribute.getRefSubType()));
                }
            });
        } else {
            manufacturerTypeAttributes = getAttributesFromHierarchy(typeId);
        }
        return manufacturerTypeAttributes;
    }

    public List<PLMManufacturerTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMManufacturerTypeAttribute> collector = new ArrayList<>();
        List<PLMManufacturerTypeAttribute> atts = manufacturerTypeAttributeRepository.findByMfrTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMManufacturerTypeAttribute> collector, Integer typeId) {
        PLMManufacturerType itemType = manufacturerTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMManufacturerTypeAttribute> atts = manufacturerTypeAttributeRepository.findByMfrTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerTypeRepository = webApplicationContext.getBean(ManufacturerTypeRepository.class);
        PLMManufacturerType itemType = (PLMManufacturerType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(manufacturerTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMManufacturerType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerTypeRepository = webApplicationContext.getBean(ManufacturerTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(manufacturerTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMManufacturerType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerTypeRepository = webApplicationContext.getBean(ManufacturerTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(manufacturerTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PLMManufacturerTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerTypeAttributeRepository = webApplicationContext.getBean(ManufacturerTypeAttributeRepository.class);
        manufacturerTypeRepository = webApplicationContext.getBean(ManufacturerTypeRepository.class);
        if (!hierarchy) {
            return manufacturerTypeAttributeRepository.findByMfrTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }
}
