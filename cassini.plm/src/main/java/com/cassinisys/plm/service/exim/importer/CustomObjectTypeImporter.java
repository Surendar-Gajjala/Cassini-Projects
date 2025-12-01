package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.custom.CustomObjectType;
import com.cassinisys.platform.model.custom.CustomObjectTypeAttribute;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.custom.CustomObjectTypeRepository;
import com.cassinisys.platform.service.custom.CustomObjectTypeService;
import com.cassinisys.plm.model.plm.PLMObjectType;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 06-08-2021.
 */
@Service
public class CustomObjectTypeImporter {


    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private CustomObjectTypeRepository customObjectTypeRepository;
    @Autowired
    private CustomObjectTypeAttributeRepository customObjectTypeAttributeRepository;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private CustomObjectTypeService customObjectTypeService;
    @Autowired
    private PlantsImporter plantsImporter;

    public static ConcurrentMap<String, CustomObjectType> rootCustomObjectTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, CustomObjectType> customObjectTypesMapByPath = new ConcurrentHashMap<>();

    private CustomObjectTypeAttribute checkAttributeName(Integer customObjectType, String name) {
        CustomObjectType customObjectType1 = customObjectTypeRepository.findOne(customObjectType);
        CustomObjectTypeAttribute customObjectTypeAttribute = null;
        if (customObjectType1.getParentType() != null) {
            customObjectTypeAttribute = customObjectTypeAttributeRepository.findByCustomObjectTypeAndName(customObjectType1.getId(), name);
        }
        return customObjectTypeAttribute;
    }


    public void importCustomObjectTypeAttributes(Integer value, Enum objectType, CustomObjectTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {

            String name = stringListHashMap.get("Attribute Name");
            CustomObjectTypeAttribute customObjectTypeAttribute = null;
            if (name != null && name != "") {
                customObjectTypeAttribute = checkAttributeName(objectId, name);
            }

            if (customObjectTypeAttribute == null) {
                String dataType = stringListHashMap.get("Data Type");
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length");
                        String max = stringListHashMap.get("Maximum length");
                        Boolean cap = itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)", stringListHashMap);
                        Boolean small = itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)", stringListHashMap);
                        Boolean mixCase = itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)", stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
                        JSONArray array = itemTypeImporter.getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length");
                        Boolean cap = itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)", stringListHashMap);
                        Boolean small = itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)", stringListHashMap);
                        Boolean mixCase = itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)", stringListHashMap);
                        JSONArray array = itemTypeImporter.getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value");
                        String max = stringListHashMap.get("Maximum Value");
                        Boolean positiv = itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = itemTypeImporter.getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date");
                        String toDate = stringListHashMap.get("To date");
                        String format = stringListHashMap.get("Date format");
                        JSONArray array = itemTypeImporter.getDateValidations(fromDate, toDate, format);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("LIST")) {
                        String lvName = stringListHashMap.get("List Name");
                        if (lvName != null && lvName != null) {
                            Lov lov = lovRepository.findByName(lvName);
                            if (lov == null) {
                                Lov newObj = new Lov();
                                String commasepLovs = stringListHashMap.get("List of Values (Comma separated)");
                                if (commasepLovs != null && commasepLovs != "") {
                                    List<String> lovs = Arrays.asList(commasepLovs.split(","));
                                    String[] arr = new String[lovs.size()];
                                    arr = lovs.toArray(arr);
                                    newObj.setName(lvName);
                                    newObj.setDescription("");
                                    newObj.setDefaultValue("");
                                    newObj.setValues(arr);
                                    Lov lovObj = lovRepository.save(newObj);
                                    attribute.setLov(lovObj);
                                }

                            } else {
                                attribute.setLov(lov);
                            }

                        }
                        attribute.setListMultiple(itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)", stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time");
                        String toTime = stringListHashMap.get("To Time");
                        String format = stringListHashMap.get("Time Format");
                        JSONArray array = itemTypeImporter.getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display");
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = itemTypeImporter.getCurrencyValidations(dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("DOUBLE")) {
                        String qom = stringListHashMap.get("QoM");
                        if (qom != null && qom != null) {
                            Measurement measurement = measurementRepository.findByName(qom);
                            if (measurement != null) {
                                attribute.setMeasurement(measurement);
                            }

                        }

                        String min = stringListHashMap.get("Minimum Value");
                        String max = stringListHashMap.get("Maximum Value");
                        String dis = stringListHashMap.get("No of decimals to display");
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = itemTypeImporter.getDoubleValidations(min, max, dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("OBJECT")) {
                        String objName = stringListHashMap.get("Object Name");
                        attribute.setRefType(PLMObjectType.valueOf(objName));
                    }
                    if (dataType.equals("FORMULA")) {
                        String formula = stringListHashMap.get("Enter Formula");
                        attribute.setFormula(formula);
                    }


                    String desc = stringListHashMap.get("Attribute Description");
                    String attrGrp = stringListHashMap.get("Attribute Group");
                    String defaultValue = stringListHashMap.get("Default value");
                    attribute.setAttributeGroup(attrGrp);
                    attribute.setName(name);
                    attribute.setCustomObjectType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setSeqNo(1);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(itemTypeImporter.getBoolPropertyValue("Is Required", stringListHashMap));
                    attribute.setRevisionSpecific(itemTypeImporter.getBoolPropertyValue("Revision Specific (Yes or No)", stringListHashMap));
                    customObjectTypeAttributeRepository.save(attribute);

                }


            }

        }
    }

    public void importCustomObjectTypes(Integer i, RowData stringListHashMap) {
        loadCustomObjectClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            CustomObjectType customObjectType = rootCustomObjectTypesMap.get(typeName);
            if (customObjectType != null) {
                customObjectType.setName(typeName);
                customObjectType.setName(desc != null ? desc : typeName);
                customObjectTypeRepository.save(customObjectType);
                CustomObjectTypeAttribute customObjectTypeAttribute = new CustomObjectTypeAttribute();
                importCustomObjectTypeAttributes(i, customObjectType.getObjectType(), customObjectTypeAttribute, customObjectType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    CustomObjectType customObjectTypeObject = getCustomObjectTypes(path);
                    if (customObjectTypeObject != null) {
                        CustomObjectTypeAttribute customObjectTypeAttribute = new CustomObjectTypeAttribute();
                        importCustomObjectTypeAttributes(i, customObjectTypeObject.getObjectType(), customObjectTypeAttribute, customObjectTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                CustomObjectType customObjectType = getCustomObjectTypes(path);
                if (customObjectType != null) {
                    CustomObjectTypeAttribute customObjectTypeAttribute = new CustomObjectTypeAttribute();
                    importCustomObjectTypeAttributes(i, customObjectType.getObjectType(), customObjectTypeAttribute, customObjectType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void loadCustomObjectClassificationTree() {
        rootCustomObjectTypesMap = new ConcurrentHashMap<>();
        customObjectTypesMapByPath = new ConcurrentHashMap<>();
        List<CustomObjectType> rootTypes = customObjectTypeService.getClassificationTree();
        for (CustomObjectType rootType : rootTypes) {
            rootCustomObjectTypesMap.put(rootType.getName(), rootType);
            customObjectTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private CustomObjectType getCustomObjectTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            CustomObjectType itemType = rootCustomObjectTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootCustomObjectTypesMap.get(name);
        }
    }

    private CustomObjectType createCustomObjectTypeByPath(CustomObjectType parentType, String path) {
        String name;
        int index = path.indexOf('/');
        String restOfPath = null;
        if (index != -1) {
            name = path.substring(0, index);
            restOfPath = path.substring(index + 1);
        } else {
            name = path;
        }
        if (parentType == null) {
            parentType = rootCustomObjectTypesMap.get(name);
            if (parentType == null) {
                parentType = new CustomObjectType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(plantsImporter.getDefaultPlantNumberSource("Default Part Number Source"));
                parentType = customObjectTypeRepository.save(parentType);
                rootCustomObjectTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createCustomObjectTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            CustomObjectType childItemType = new CustomObjectType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType = customObjectTypeRepository.save(childItemType);
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                parentType = createCustomObjectTypeByPath(parentType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createCustomObjectTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    private CustomObjectType getCustomObjectTypes(String path) {
        CustomObjectType mesCustomObjectType = customObjectTypesMapByPath.get(path);
        if (mesCustomObjectType == null) {
            mesCustomObjectType = getCustomObjectTypeByPath(path);
            if (mesCustomObjectType == null) {
                mesCustomObjectType = createCustomObjectTypeByPath(null, path);
            }
            customObjectTypesMapByPath.put(path, mesCustomObjectType);
        }

        return mesCustomObjectType;
    }

}
