package com.cassinisys.platform.service.core;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AttributesDetailsDTO;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by GSR on 03-07-2017.
 */
@Service
public class ObjectTypeAttributeService implements CrudService<ObjectTypeAttribute, Integer>, PageableService<ObjectTypeAttribute, Integer> {

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Override
    @Transactional
    public ObjectTypeAttribute create(ObjectTypeAttribute objectTypeAttribute) {
        checkNotNull(objectTypeAttribute);
        return objectTypeAttributeRepository.save(objectTypeAttribute);
    }

    @Override
    @Transactional
    public ObjectTypeAttribute update(ObjectTypeAttribute objectTypeAttribute) {
        return objectTypeAttributeRepository.save(objectTypeAttribute);
        /*if (objectAttributeRepository.findByAttributeDef(objectTypeAttribute.getId()).size() == 0) {
            return objectTypeAttributeRepository.save(objectTypeAttribute);
        } else {
            throw new RuntimeException("This Property is used by other items, cannot modify this Property");
        }*/
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (objectAttributeRepository.findByAttributeDef(id).size() == 0) {
            objectTypeAttributeRepository.delete(id);
        } else {
            throw new CassiniException(messageSource.getMessage("attribute_already_used", null, "This Attribute is used by other items, cannot Delete this Attribute", LocaleContextHolder.getLocale()));

        }
    }

    @Override
    @Transactional(readOnly = true)
    public ObjectTypeAttribute get(Integer id) {
        return objectTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public AttributesDetailsDTO getObjectTypeAttributesByObjectType(String type, Integer objectId) {
        AttributesDetailsDTO attributesDetailsDTO = new AttributesDetailsDTO();
        Map<Integer, ObjectAttribute> attributeMap = new HashMap<>();
        List<ObjectTypeAttribute> objectTypeAttributes = new ArrayList<>();
        ObjectAttribute attribute = new ObjectAttribute();
        objectTypeAttributes = getByObjectTypeAttribute(type);
        if (objectTypeAttributes.size() > 0) {
            attributesDetailsDTO.setObjectTypeAttributes(objectTypeAttributes);
            for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
                attribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, objectTypeAttribute.getId());
                if (attribute == null) {
                    attribute = new ObjectAttribute();
                }
                attributeMap.put(objectTypeAttribute.getId(), attribute);
            }
            attributesDetailsDTO.setAttributeMap(attributeMap);
        }
        return attributesDetailsDTO;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getByObjectTypeAttribute(String type) {
        List<ObjectTypeAttribute> typeAttributes = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(type));
        typeAttributes.forEach(objectTypeAttribute -> {
            if (objectTypeAttribute.getMeasurement() != null) {
                objectTypeAttribute.getMeasurement().setMeasurementUnits(measurementUnitRepository.findByMeasurementOrderByIdAsc(objectTypeAttribute.getMeasurement().getId()));
            }
        });
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> findAllObjectTypeAttributesByIds(List<Integer> ids) {
        return objectTypeAttributeRepository.findByIdIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAll() {
        return objectTypeAttributeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAllByObjectTypeAttribute() {
        return objectTypeAttributeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ObjectTypeAttribute> findAll(Pageable pageable) {
        return objectTypeAttributeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<String> getAttributesGroups() {
        return objectTypeAttributeRepository.getUniqueAttributeGroups();
    }

    @Transactional(readOnly = true)
    public List<String> getAttributesGroupsByName(String name) {
        return objectTypeAttributeRepository.getAttributeGroupsByName(name);
    }

    @Transactional
    public ObjectTypeAttribute updateAttributeGroupName(Integer attributeId, String groupName) {
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(attributeId);

        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByAttributeGroup(objectTypeAttribute.getAttributeGroup());
        objectTypeAttribute.setAttributeGroup(groupName);
        objectTypeAttribute = objectTypeAttributeRepository.save(objectTypeAttribute);
        if (objectTypeAttributes.size() > 0) {
            for (ObjectTypeAttribute typeAttribute : objectTypeAttributes) {
                typeAttribute.setAttributeGroup(groupName);
                typeAttribute = objectTypeAttributeRepository.save(typeAttribute);
            }
        }

        return objectTypeAttribute;
    }
}
