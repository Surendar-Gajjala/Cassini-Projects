package com.cassinisys.plm.service.classification;

import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.plm.event.ClassificationEvents;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartAttribute;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartTypeAttribute;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.ManufacturerPartAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeAttributeRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartTypeRepository;
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
public class ManufacturerPartTypeService implements CrudService<PLMManufacturerPartType, Integer>,
        TypeSystem, ClassificationTypeService<PLMManufacturerPartType, PLMManufacturerPartTypeAttribute> {
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ManufacturerPartAttributeRepository manufacturerPartAttributeRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;


    @PostConstruct
    public void InitMfrPartTypeService() {
        objectTypeService.registerTypeSystem("manufacturerPartType", new ManufacturerPartTypeService());
    }

    @Override
    @PreAuthorize("hasPermission(#plmManufacturerType,'create')")
    public PLMManufacturerPartType create(PLMManufacturerPartType plmManufacturerType) {
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.save(plmManufacturerType);
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeCreatedEvent(PLMObjectType.MANUFACTURERPARTTYPE, manufacturerPartType));
        return manufacturerPartType;
    }

    @Override
    @PreAuthorize("hasPermission(#plmManufacturerType.id ,'edit')")
    public PLMManufacturerPartType update(PLMManufacturerPartType plmManufacturerType) {
        checkNotNull(plmManufacturerType);
        checkNotNull(plmManufacturerType.getId());
        PLMManufacturerPartType oldmfrPartType = manufacturerPartTypeRepository.findOne(plmManufacturerType.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeUpdatedEvent(PLMObjectType.MANUFACTURERPARTTYPE, oldmfrPartType, plmManufacturerType));
        PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.save(plmManufacturerType);
        return manufacturerPartType;
    }

    @Override
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        PLMManufacturerPartType manufacturerType = manufacturerPartTypeRepository.findOne(id);
        if (manufacturerType.getParentType() != null) {
            applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeDeletedEvent(PLMObjectType.MANUFACTURERPARTTYPE, manufacturerType));
        }
        manufacturerPartTypeRepository.delete(id);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PLMManufacturerPartType get(Integer id) {
        return manufacturerPartTypeRepository.findOne(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPartType> getAll() {
        return manufacturerPartTypeRepository.findAll();
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPartType> getRootTypes() {
        return manufacturerPartTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    public List<PLMManufacturerPartType> getChildren(Integer parent) {
        return manufacturerPartTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PLMManufacturerPartType> getClassificationTree() {
        List<PLMManufacturerPartType> types = getRootTypes();
        for (PLMManufacturerPartType type : types) {
            visitChildren(type);
        }
        return types;
    }

    public List<PLMManufacturerPartType> getAllManufacturerPartTypesWithAttributes() {
        List<PLMManufacturerPartType> types = getAll();
        for (PLMManufacturerPartType type : types) {
            if (type.getParentType() != null) {
                type.setParentTypeReference(manufacturerPartTypeRepository.findOne(type.getParentType()));
            }
            type.setAttributes(manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderByName(type.getId()));
        }
        return types;
    }

    private void visitChildren(PLMManufacturerPartType parent) {
        List<PLMManufacturerPartType> children = getChildren(parent.getId());
        for (PLMManufacturerPartType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    public PLMManufacturerPartTypeAttribute createAttribute(PLMManufacturerPartTypeAttribute attribute) {
        List<PLMManufacturerPartTypeAttribute> partTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartType(attribute.getMfrPartType());
        if (partTypeAttributes.size() >= 0) {
            attribute.setSeq(partTypeAttributes.size() + 1);
        }
        PLMManufacturerPartTypeAttribute plmManufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(attribute);

        PLMManufacturerPartType partType = manufacturerPartTypeRepository.findOne(attribute.getMfrPartType());
        if (attribute.getDataType().toString().equals("FORMULA")) {
            List<PLMManufacturerPart> parts = manufacturerPartRepository.findByMfrPartType(partType);
            for (PLMManufacturerPart part : parts) {
                PLMManufacturerPartAttribute objectAttribute = new PLMManufacturerPartAttribute();
                objectAttribute.setId(new ObjectAttributeId(part.getId(), attribute.getId()));
                manufacturerPartAttributeRepository.save(objectAttribute);
            }
        }

        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeCreatedEvent(PLMObjectType.MANUFACTURERPARTTYPE, plmManufacturerPartTypeAttribute));
        return plmManufacturerPartTypeAttribute;
    }

    public PLMManufacturerPartTypeAttribute updateAttribute(PLMManufacturerPartTypeAttribute attribute) {
        PLMManufacturerPartTypeAttribute plmManufacturerPartTypeAttribute1 = manufacturerPartTypeAttributeRepository.findOne(attribute.getId());
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeUpdatedEvent(PLMObjectType.MANUFACTURERPARTTYPE, plmManufacturerPartTypeAttribute1, attribute));
        PLMManufacturerPartTypeAttribute plmManufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(attribute);
        return plmManufacturerPartTypeAttribute;
    }

    public PLMManufacturerPartTypeAttribute getAttribute(Integer id) {
        return manufacturerPartTypeAttributeRepository.findOne(id);
    }

    public List<PLMManufacturerPartTypeAttribute> getTypeAttributes(Integer id) {
        return manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderByName(id);
    }

    @Override
    public void deleteAttribute(Integer id) {
        PLMManufacturerPartTypeAttribute attribute = manufacturerPartTypeAttributeRepository.findOne(id);
        List<PLMManufacturerPartTypeAttribute> manufacturerPartTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartType(attribute.getMfrPartType());
        if (manufacturerPartTypeAttributes.size() > 0) {
            for (PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute : manufacturerPartTypeAttributes) {
                if (manufacturerPartTypeAttribute.getSeq() > attribute.getSeq()) {
                    manufacturerPartTypeAttribute.setSeq(manufacturerPartTypeAttribute.getSeq() - 1);
                    manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.save(manufacturerPartTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new ClassificationEvents.ClassificationTypeAttributeDeletedEvent(PLMObjectType.MANUFACTURERPARTTYPE, attribute));
        manufacturerPartTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<PLMManufacturerPartTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        List<PLMManufacturerPartTypeAttribute> partTypeAttributes = new ArrayList<>();
        if (!hierarchy) {
            partTypeAttributes = manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderBySeq(typeId);
            partTypeAttributes.forEach(partTypeAttribute -> {
                if (partTypeAttribute.getRefSubType() != null) {
                    partTypeAttribute.setRefSubTypeName(itemTypeService.getRefSubType(partTypeAttribute.getRefType().name(), partTypeAttribute.getRefSubType()));
                }
            });
        } else {
            partTypeAttributes = getAttributesFromHierarchy(typeId);
        }

        return partTypeAttributes;
    }

    public List<PLMManufacturerPartTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<PLMManufacturerPartTypeAttribute> collector = new ArrayList<>();
        List<PLMManufacturerPartTypeAttribute> atts = manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderBySeq(typeId);
        collector.addAll(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<PLMManufacturerPartTypeAttribute> collector, Integer typeId) {
        PLMManufacturerPartType itemType = manufacturerPartTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<PLMManufacturerPartTypeAttribute> atts = manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderByName(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerPartTypeRepository = webApplicationContext.getBean(ManufacturerPartTypeRepository.class);
        PLMManufacturerPartType itemType = (PLMManufacturerPartType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(manufacturerPartTypeRepository.findOne(itemType.getParentType()), s1);
        }
        return false;
    }

    public Boolean checkWithId(PLMManufacturerPartType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerPartTypeRepository = webApplicationContext.getBean(ManufacturerPartTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(manufacturerPartTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(PLMManufacturerPartType plmItemType, String s1) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerPartTypeRepository = webApplicationContext.getBean(ManufacturerPartTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = compareWithParent(manufacturerPartTypeRepository.findOne(plmItemType.getParentType()), s1);
        }
        return flag;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PLMManufacturerPartTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        manufacturerPartTypeAttributeRepository = webApplicationContext.getBean(ManufacturerPartTypeAttributeRepository.class);
        manufacturerPartTypeRepository = webApplicationContext.getBean(ManufacturerPartTypeRepository.class);
        if (!hierarchy) {
            return manufacturerPartTypeAttributeRepository.findByMfrPartTypeOrderBySeq(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }
}
