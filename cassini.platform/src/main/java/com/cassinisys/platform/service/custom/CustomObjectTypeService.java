package com.cassinisys.platform.service.custom;

import com.cassinisys.platform.events.CustomObjectEvents;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectAttribute;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.model.dto.CustomObjectDto;
import com.cassinisys.platform.model.dto.CustomObjectTypeAttributeDto;
import com.cassinisys.platform.repo.custom.CustomObjectAttributeRepository;
import com.cassinisys.platform.repo.custom.CustomObjectRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.cassinisys.platform.service.core.ObjectTypeService;
import com.cassinisys.platform.service.core.TypeSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
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

@Service
public class CustomObjectTypeService implements TypeSystem {
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;
    @Autowired
    private CustomObjectTypeAttributeRepository customObjectTypeAttributeRepository;
    @Autowired
    private CustomObjectAttributeRepository customObjectAttributeRepository;
    @Autowired
    private ObjectTypeService objectTypeService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private CustomObjectRepository customObjectRepository;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void InitCustomeTypeService() {
        objectTypeService.registerTypeSystem("customeType", new CustomObjectTypeService());
    }

    @Transactional
    @PreAuthorize("hasPermission(#customObjectType,'create')")
    public CustomObjectType create(CustomObjectType customObjectType) {
        CustomObjectType objectType = customObjectTypeRepository.save(customObjectType);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectTypeCreatedEvent(ObjectType.CUSTOMOBJECTTYPE, objectType));
        return objectType;
    }

    @Transactional
    @PreAuthorize("hasPermission(#customObjectType.id ,'edit')")
    public CustomObjectType update(Integer id, CustomObjectType customObjectType) {
        CustomObjectType oldCustomObjectType = customObjectTypeRepository.findOne(id);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectTypeUpdatedEvent(ObjectType.CUSTOMOBJECTTYPE, oldCustomObjectType, customObjectType));
        return customObjectTypeRepository.save(customObjectType);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<CustomObject> customObjects = customObjectRepository.findByTypeId(id);
        CustomObjectType customObjectType = customObjectTypeRepository.findOne(id);
        if (customObjects.size() > 0) {
            String message = messageSource.getMessage("custom_object_type_already_use", null, "{0} already in use", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", customObjectType.getName());
            throw new CassiniException(result);
        } else {
            customObjectTypeRepository.delete(id);
        }
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public CustomObjectType get(Integer id) {
        return customObjectTypeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getAll() {
        return customObjectTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getMultiple(List<Integer> ids) {
        return customObjectTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getChildren(Integer parent) {
        return customObjectTypeRepository.findByParentTypeOrderByIdAsc(parent);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getRootTypes() {
        return customObjectTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getClassificationTree() {
        List<CustomObjectType> types = getRootTypes();
        for (CustomObjectType type : types) {
            visitChildren(type);
        }
        return types;
    }

    private void visitChildren(CustomObjectType parent) {
        List<CustomObjectType> children = getChildren(parent.getId());
        for (CustomObjectType child : children) {
            visitChildren(child);
        }
        parent.setChildren(children);
    }

    @Transactional(readOnly = true)
    public CustomObjectType getParentCustomType(CustomObjectType parent) {
        CustomObjectType customObjectType = parent;
        if (parent.getParentType() != null) {
            customObjectType = visitParent(parent.getParentType());
        }
        return customObjectType;
    }

    private CustomObjectType visitParent(Integer parent) {
        CustomObjectType objectType = customObjectTypeRepository.findOne(parent);
        if (objectType.getParentType() != null) {
            objectType = visitParent(objectType.getParentType());
        }
        return objectType;
    }

    @Transactional(readOnly = true)
    public List<Integer> getIdsByType(Integer parent) {
        List<Integer> typeIds = new ArrayList<>();
        List<Integer> types = customObjectTypeRepository.getTypeIdsByParentTypeOrderByIdAsc(parent);
        typeIds.addAll(types);
        types.forEach(type -> {
            visitTypeChildren(type, typeIds);
        });
        return typeIds;
    }

    public void visitTypeChildren(Integer parent, List<Integer> typeIds) {
        List<Integer> types = customObjectTypeRepository.getTypeIdsByParentTypeOrderByIdAsc(parent);
        typeIds.addAll(types);
        types.forEach(type -> {
            visitTypeChildren(type, typeIds);
        });
    }

    @Transactional(readOnly = true)
    public CustomObjectType getCustomObjectTypeTree(Integer id) {
        CustomObjectType customObjectType = customObjectTypeRepository.findOne(id);
        visitChildren(customObjectType);
        return customObjectType;
    }

    @Transactional
    public CustomObjectTypeAttribute createAttribute(CustomObjectTypeAttribute customObjectTypeAttribute) {
        List<CustomObjectTypeAttribute> customObjectTypeAttributes = customObjectTypeAttributeRepository.findByCustomObjectType(customObjectTypeAttribute.getCustomObjectType());
        if (customObjectTypeAttributes.size() >= 0) {
            customObjectTypeAttribute.setSeqNo(customObjectTypeAttributes.size() + 1);
        }
        customObjectTypeAttribute = customObjectTypeAttributeRepository.save(customObjectTypeAttribute);
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectTypeAttributeCreatedEvent(ObjectType.CUSTOMOBJECTTYPE, customObjectTypeAttribute));
        return customObjectTypeAttribute;
    }

    @Transactional
    public CustomObjectTypeAttribute updateAttribute(CustomObjectTypeAttribute customObjectTypeAttribute) {
        CustomObjectTypeAttribute customObjectTypeAttribute1 = customObjectTypeAttributeRepository.findOne(customObjectTypeAttribute.getId());
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectTypeAttributeUpdatedEvent(ObjectType.CUSTOMOBJECTTYPE, customObjectTypeAttribute1, customObjectTypeAttribute));
        return customObjectTypeAttributeRepository.save(customObjectTypeAttribute);

    }

    @Transactional
    public void deleteAttribute(Integer id) {
        CustomObjectTypeAttribute attribute = customObjectTypeAttributeRepository.findOne(id);
        List<CustomObjectTypeAttribute> customObjectTypeAttributes = customObjectTypeAttributeRepository.findByCustomObjectType(attribute.getCustomObjectType());
        if (customObjectTypeAttributes.size() > 0) {
            for (CustomObjectTypeAttribute customObjectTypeAttribute : customObjectTypeAttributes) {
                if (customObjectTypeAttribute.getSeqNo() > attribute.getSeqNo()) {
                    customObjectTypeAttribute.setSeqNo(customObjectTypeAttribute.getSeqNo() - 1);
                    customObjectTypeAttribute = customObjectTypeAttributeRepository.save(customObjectTypeAttribute);
                }
            }
        }
        applicationEventPublisher.publishEvent(new CustomObjectEvents.CustomObjectTypeAttributeDeletedEvent(ObjectType.CUSTOMOBJECTTYPE, attribute));
        customObjectTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public CustomObjectTypeAttribute getAttribute(Integer id) {
        return customObjectTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<CustomObjectTypeAttribute> getMultipleAttributes(List<Integer> ids) {
        return customObjectTypeAttributeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<CustomObjectTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<CustomObjectTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<CustomObjectTypeAttribute> atts = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
        List<CustomObjectTypeAttribute> collector = new ArrayList<>(atts);
        collectAttributesFromHierarchy(collector, typeId);
        return collector;
    }

    private void collectAttributesFromHierarchy(List<CustomObjectTypeAttribute> collector, Integer typeId) {
        CustomObjectType itemType = customObjectTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<CustomObjectTypeAttribute> atts = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(parentType);
                collector.addAll(atts);
                collectAttributesFromHierarchy(collector, parentType);
            }
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getAllObjectTypes() {
        return customObjectTypeRepository.findByParentTypeIsNullOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public CustomObjectTypeAttributeDto getCustomObjectTypeAttributes(ObjectType objectType, Integer typeId, Boolean hierarchy) {

        CustomObjectTypeAttributeDto customObjectTypeAttributeDto = new CustomObjectTypeAttributeDto();

        if (!hierarchy) {
            if (objectType.equals(ObjectType.CUSTOMOBJECTTYPE)) {
                List<CustomObjectTypeAttribute> customObjectTypeAttributes = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
                customObjectTypeAttributeDto.getCustomObjectTypeAttributes().addAll(customObjectTypeAttributes);
            } else {
                List<CustomObjectTypeAttribute> customObjectTypeAttributes = customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
                customObjectTypeAttributeDto.getCustomObjectTypeAttributes().addAll(customObjectTypeAttributes);
            }
        } else {
            if (objectType.equals(ObjectType.CUSTOMOBJECTTYPE)) {
                List<CustomObjectTypeAttribute> customObjectTypeAttributes = getAttributesFromHierarchy(typeId);
                customObjectTypeAttributeDto.getCustomObjectTypeAttributes().addAll(customObjectTypeAttributes);
            } else {
                List<CustomObjectTypeAttribute> customObjectTypeAttributes = getAttributesFromHierarchy(typeId);
                customObjectTypeAttributeDto.getCustomObjectTypeAttributes().addAll(customObjectTypeAttributes);
            }
        }
        return customObjectTypeAttributeDto;
    }

    @Transactional(readOnly = true)
    public CustomObjectTypeAttributeDto getCustomObjectTypeAttributeValues(ObjectType objectType, Integer typeId, Integer objectId) {

        CustomObjectTypeAttributeDto customObjectTypeAttributeDto = new CustomObjectTypeAttributeDto();

        if (objectType.equals(ObjectType.CUSTOMOBJECTTYPE)) {
            List<CustomObjectAttribute> customObjectAttributes = customObjectAttributeRepository.findByIdIn(objectId);
            customObjectTypeAttributeDto.getCustomObjectAttributes().addAll(customObjectAttributes);
        }
        return customObjectTypeAttributeDto;
    }

    @Override
    public Boolean isSubtypeOf(String s1, Object s2, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        customObjectTypeRepository = webApplicationContext.getBean(CustomObjectTypeRepository.class);
        CustomObjectType itemType = (CustomObjectType) s2;
        if (subTypeId != null && checkWithId(itemType, subTypeId)) {
            return true;
        }
        if (itemType.getName().equalsIgnoreCase(s1)) {
            return true;
        } else {
            if (itemType.getParentType() != null)
                return compareWithParent(customObjectTypeRepository.findOne(itemType.getParentType()), s1, subTypeId);
        }
        return false;
    }

    public Boolean checkWithId(CustomObjectType plmItemType, Integer typeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        customObjectTypeRepository = webApplicationContext.getBean(CustomObjectTypeRepository.class);
        Boolean flag = false;
        if (Objects.equals(plmItemType.getId(), typeId)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                flag = checkWithId(customObjectTypeRepository.findOne(plmItemType.getParentType()), typeId);
        }
        return flag;
    }

    private Boolean compareWithParent(CustomObjectType plmItemType, String s1, Integer subTypeId) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        customObjectTypeRepository = webApplicationContext.getBean(CustomObjectTypeRepository.class);
        Boolean flag = false;
        if (plmItemType.getName().equalsIgnoreCase(s1)) {
            flag = true;
        } else {
            if (plmItemType.getParentType() != null)
                compareWithParent(customObjectTypeRepository.findOne(plmItemType.getParentType()), s1, subTypeId);
        }
        return flag;
    }

    @Override
    public List<CustomObjectTypeAttribute> getTypeAttributes(Integer typeId, Boolean hierarchy) {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        customObjectTypeAttributeRepository = webApplicationContext.getBean(CustomObjectTypeAttributeRepository.class);
        if (!hierarchy) {
            return customObjectTypeAttributeRepository.findByCustomObjectTypeOrderBySeqNo(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<CustomObjectType> getNavigationCustomTypes() {
        return customObjectTypeRepository.findByShowInNavigationTrueOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public CustomObjectDto getCustomObjectTypeByName(String name) {
        CustomObjectDto dto = new CustomObjectDto();
        Integer typeId = customObjectTypeRepository.getTypeIdByName(name);
        if (typeId != null) {
            dto.setCustomObjectTypeId(typeId);
            dto.setCustomObjectType(customObjectTypeRepository.findOne(typeId));
        }
        return dto;
    }


}
