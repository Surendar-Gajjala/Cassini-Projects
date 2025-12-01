package com.cassinisys.platform.service.core;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by GSR on 03-07-2017.
 */
@Service
public class ObjectAttributeService {

    ExpressionParser parser = new SpelExpressionParser();
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Transactional
    public ObjectAttribute createAttribute(ObjectAttribute objectAttribute) {
        checkNotNull(objectAttribute);
        return objectAttributeRepository.save(objectAttribute);
    }

    @Transactional
    public ObjectAttribute updateAttribute(ObjectAttribute objectAttribute) {
        checkNotNull(objectAttribute);
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
        if (objectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(objectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(objectAttribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(objectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
            }
        } else {
            objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
        }
        return objectAttributeRepository.save(objectAttribute);
    }

    @Transactional
    public void deleteAttribute(Integer objectId, Integer id) {
        checkNotNull(id);
        ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, id);
        objectAttributeRepository.delete(objectAttribute);

    }

    @Transactional(readOnly = true)
    public ObjectAttribute getAttribute(Integer id) {
        checkNotNull(id);
        return objectAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<ObjectAttribute> getAttributes(Integer objectId) {
        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(objectId);

        List<Integer> attributeDefIds = new ArrayList();
        for (ObjectAttribute attribute : objectAttributes) {
            attributeDefIds.add(attribute.getId().getAttributeDef());
        }
        List<ObjectTypeAttribute> plmItemTypeAttributes = objectTypeAttributeRepository.findByIdIn(attributeDefIds);
        Map<Integer, ObjectTypeAttribute> objectTypeAttributeMap = plmItemTypeAttributes.stream().collect(Collectors.toMap(ObjectTypeAttribute::getId, typeAttribute -> typeAttribute));

        Map<String, Double> stringDoubleMap = new HashMap();

        for (ObjectAttribute objectAttribute : objectAttributes) {
            ObjectTypeAttribute typeAttribute4 = objectTypeAttributeMap.get(objectAttribute.getId().getAttributeDef());
            if (typeAttribute4 != null) {
                if (typeAttribute4.getDataType().toString().equals( "INTEGER") && objectAttribute.getIntegerValue() != null) {
                    stringDoubleMap.put(typeAttribute4.getName(), objectAttribute.getIntegerValue().doubleValue());
                } else if (typeAttribute4.getDataType().toString().equals("DOUBLE")) {
                    stringDoubleMap.put(typeAttribute4.getName(), objectAttribute.getDoubleValue());
                }
            }
        }

        for (ObjectAttribute objectAttribute : objectAttributes) {
            ObjectTypeAttribute typeAttribute4 = objectTypeAttributeMap.get(objectAttribute.getId().getAttributeDef());
            if (typeAttribute4 != null && typeAttribute4.getDataType().toString().equals("FORMULA")) {
                if (stringDoubleMap.size() > 0) {
                    try {
                        objectAttribute.setFormulaValue(parser.parseExpression(typeAttribute4.getFormula()).getValue(stringDoubleMap, String.class));
                    } catch (Exception e) {
                        objectAttribute.setFormulaValue(null);
                    }
                }
            }
        }

        return objectAttributes;
    }

    @Transactional(readOnly = true)
    public ObjectAttribute findByObjectIdsInAndAttributeDefIdsIn(Integer objectId, Integer attId) {
        return objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attId);
    }

    @Transactional
    public List<ObjectAttribute> saveObjectAttributes(List<ObjectAttribute> objectAttributes) {
        for (ObjectAttribute objectAttribute : objectAttributes) {

            ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());

            if (objectTypeAttribute != null) {
                if (objectTypeAttribute.getDataType().equals(DataType.BOOLEAN)) {
                    objectAttribute = objectAttributeRepository.save(objectAttribute);
                }
                else {
                    if (objectAttribute.getTimeValue() != null || objectAttribute.getTimestampValue() != null || objectAttribute.getRefValue() != null || objectAttribute.getListValue() != null ||
                            objectAttribute.getImageValue() != null || objectAttribute.getAttachmentValues().length > 0 || (objectAttribute.getCurrencyType() != null &&
                            objectAttribute.getCurrencyValue() != null) || objectAttribute.getDateValue() != null || objectAttribute.getHyperLinkValue() != null ||
                            objectAttribute.getIntegerValue() != null || objectAttribute.getStringValue() != null || objectAttribute.getLongTextValue() != null
                            || objectAttribute.getRichTextValue() != null || objectAttribute.getMListValue().length > 0 || objectAttribute.getBooleanValue() != null) {

                        if (objectTypeAttribute.getMeasurement() != null) {
                            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(objectTypeAttribute.getMeasurement().getId());
                            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(objectAttribute.getMeasurementUnit().getId());
                            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(objectTypeAttribute.getMeasurement().getId());

                            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
                            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

                            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue() / measurementUnit.getConversionFactor());
                            } else {
                                objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
                            }
                            objectAttribute.setMeasurementUnit(objectAttribute.getMeasurementUnit());
                        } else {
                            objectAttribute.setDoubleValue(objectAttribute.getDoubleValue());
                        }

                        objectAttribute = objectAttributeRepository.save(objectAttribute);
                    }
                }
            }


        }

        return objectAttributes;
    }

    @Transactional(readOnly = true)
    public void getImageAttribute(Integer objectId, Integer attributeId, HttpServletResponse response) {
        ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attributeId);
        if (objectAttribute != null && objectAttribute.getImageValue() != null) {
            InputStream is = new ByteArrayInputStream(objectAttribute.getImageValue());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = true)
    public ObjectAttribute getObjectAttributeByIdAndDef(Integer objectId, Integer attributeDef) {
        return objectAttributeRepository.findByObjectIdAndAttributeDefId(objectId, attributeDef);
    }

    @Transactional(readOnly = true)
    public List<ObjectAttribute> getUsedAttributes(Integer attributeId) {
        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByAttributeDef(attributeId);
        return objectAttributes;
    }

}
