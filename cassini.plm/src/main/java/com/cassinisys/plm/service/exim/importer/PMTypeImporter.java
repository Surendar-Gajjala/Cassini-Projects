package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.req.PLMRequirementDocumentType;
import com.cassinisys.plm.model.req.PLMRequirementObjectType;
import com.cassinisys.plm.model.req.PLMRequirementObjectTypeAttribute;
import com.cassinisys.plm.model.req.PLMRequirementType;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectTypeAttributeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementObjectTypeRepository;
import com.cassinisys.plm.repo.req.PLMRequirementTypeRepository;
import com.cassinisys.plm.service.plm.LifeCycleService;
import com.cassinisys.plm.service.req.RequirementTypeService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 30-07-2021.
 */
@Service
public class PMTypeImporter {

    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private PLMRequirementObjectTypeAttributeRepository requirementObjectTypeAttributeRepository;
    @Autowired
    private PLMRequirementDocumentTypeRepository requirementDocumentTypeRepository;
    @Autowired
    private PLMRequirementTypeRepository requirementTypeRepository;
    @Autowired
    private PLMRequirementObjectTypeRepository requirementObjectTypeRepository;
    @Autowired
    private RequirementTypeService requirementTypeService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private LovService lovService;
    @Autowired
    private LifeCycleService lifeCycleService;
    private static AutoNumber autoNumber;
    private static PLMLifeCycle plmLifeCycle;
    private static Lov lov;
    private static Lov priorities;


    public static ConcurrentMap<String, PLMRequirementDocumentType> rootReqDocTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMRequirementDocumentType> reqDocTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMRequirementType> rootRequirementTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMRequirementType> requirementTypesMapByPath = new ConcurrentHashMap<>();


    public void getDefaultNumberSource() {
        autoNumber = autoNumberService.getByName("Default Requirements Document Number Source");
    }

    private void getDefaultRevisionSequence() {
        lov = lovService.getLovByName("Default Revision Sequence");
    }

    private void getDefaultReqDocLifecycle() {
        plmLifeCycle = lifeCycleService.findLifecycleByName("Default Requirement Document Lifecycle");
    }


    public void getDefaultReqNumberSource() {
        autoNumber = autoNumberService.getByName("Default Requirement Number Source");
    }

    private void getDefaultReqPrioritiesSequence() {
        lov = lovService.getLovByName("Default Requirement Priority List");
    }

    private PLMLifeCycle getDefaultReqLifecycle() {
        return lifeCycleService.findLifecycleByName("Default Requirement Lifecycle");
    }

    private PLMRequirementObjectTypeAttribute checkAttributeName(Integer type, String name) {
        PLMRequirementObjectType requirementObjectType = requirementObjectTypeRepository.findOne(type);
        PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = null;
        if (requirementObjectType != null) {
            requirementObjectTypeAttribute = requirementObjectTypeAttributeRepository.findByTypeAndName(requirementObjectType.getId(), name);
        }
        return requirementObjectTypeAttribute;
    }


    public void importPLMRequirementObjectTypeAttributes(Integer value, Enum objectType, PLMRequirementObjectTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {

            String name = stringListHashMap.get("Attribute Name");
            PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = null;
            if (name != null && name != "") {
                requirementObjectTypeAttribute = checkAttributeName(objectId, name);
            }

            if (requirementObjectTypeAttribute == null) {
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
                    attribute.setType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setSequence(1);
                    attribute.setVisible(true);
                    attribute.setRequired(itemTypeImporter.getBoolPropertyValue("Is Required", stringListHashMap));
                    requirementObjectTypeAttributeRepository.save(attribute);

                }


            }

        }
    }

    public void importRequirementDocumentTypes(Integer i, RowData stringListHashMap) {
        loadReqDocClassificationTree();
        getDefaultReqDocLifecycle();
        getDefaultNumberSource();
        getDefaultRevisionSequence();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PLMRequirementDocumentType requirementDocumentType = rootReqDocTypesMap.get(typeName);
            if (requirementDocumentType != null) {
                requirementDocumentType.setName(typeName);
                requirementDocumentType.setName(desc != null ? desc : typeName);
                requirementDocumentTypeRepository.save(requirementDocumentType);
                PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                importPLMRequirementObjectTypeAttributes(i, requirementDocumentType.getObjectType(), requirementObjectTypeAttribute, requirementDocumentType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMRequirementDocumentType reqDocTypes = getReqDocTypes(path);
                    if (reqDocTypes != null) {
                        PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                        importPLMRequirementObjectTypeAttributes(i, reqDocTypes.getObjectType(), requirementObjectTypeAttribute, reqDocTypes.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMRequirementDocumentType documentType = getReqDocTypes(path);
                if (documentType != null) {
                    PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                    importPLMRequirementObjectTypeAttributes(i, documentType.getObjectType(), requirementObjectTypeAttribute, documentType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importRequirementTypes(Integer i, RowData stringListHashMap) {
        loadRequirementClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PLMRequirementType requirementType = rootRequirementTypesMap.get(typeName);
            if (requirementType != null) {
                requirementType.setName(typeName);
                requirementType.setName(desc != null ? desc : typeName);
                requirementTypeRepository.save(requirementType);
                PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                importPLMRequirementObjectTypeAttributes(i, requirementType.getObjectType(), requirementObjectTypeAttribute, requirementType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMRequirementType requirementType1 = getRequirementTypes(path);
                    if (requirementType1 != null) {
                        PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                        importPLMRequirementObjectTypeAttributes(i, requirementType1.getObjectType(), requirementObjectTypeAttribute, requirementType1.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMRequirementType requirementType = getRequirementTypes(path);
                if (requirementType != null) {
                    PLMRequirementObjectTypeAttribute requirementObjectTypeAttribute = new PLMRequirementObjectTypeAttribute();
                    importPLMRequirementObjectTypeAttributes(i, requirementType.getObjectType(), requirementObjectTypeAttribute, requirementType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void loadReqDocClassificationTree() {
        rootReqDocTypesMap = new ConcurrentHashMap<>();
        reqDocTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMRequirementDocumentType> rootTypes = requirementTypeService.getReqDocumentTypeTree();
        for (PLMRequirementDocumentType rootType : rootTypes) {
            rootReqDocTypesMap.put(rootType.getName(), rootType);
            reqDocTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private PLMRequirementDocumentType getReqDocTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMRequirementDocumentType recDocumentType = rootReqDocTypesMap.get(name);
            if (recDocumentType != null) {
                return recDocumentType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return recDocumentType;
            }

        } else {
            name = path;
            return rootReqDocTypesMap.get(name);
        }
    }

    private PLMRequirementDocumentType createReqDocTypeByPath(PLMRequirementDocumentType parentType, String path) {
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
            parentType = rootReqDocTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMRequirementDocumentType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType.setLifecycle(plmLifeCycle);
                parentType.setRevisionSequence(lov);
                parentType = requirementDocumentTypeRepository.save(parentType);
                rootReqDocTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createReqDocTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMRequirementDocumentType childItemType = new PLMRequirementDocumentType();
            childItemType.setParent(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType.setLifecycle(parentType.getLifecycle());
            childItemType.setRevisionSequence(parentType.getRevisionSequence());
            childItemType = requirementDocumentTypeRepository.save(childItemType);
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                parentType = createReqDocTypeByPath(parentType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createReqDocTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    private PLMRequirementDocumentType getReqDocTypes(String path) {
        PLMRequirementDocumentType documentType = reqDocTypesMapByPath.get(path);
        if (documentType == null) {
            documentType = getReqDocTypeByPath(path);
            if (documentType == null) {
                documentType = createReqDocTypeByPath(null, path);
            }
            reqDocTypesMapByPath.put(path, documentType);
        }

        return documentType;
    }


    public void loadRequirementClassificationTree() {
        getDefaultReqLifecycle();
        getDefaultReqNumberSource();
        getDefaultReqPrioritiesSequence();
        rootRequirementTypesMap = new ConcurrentHashMap<>();
        requirementTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMRequirementType> rootTypes = requirementTypeService.getRequirementTypeTree();
        for (PLMRequirementType rootType : rootTypes) {
            rootRequirementTypesMap.put(rootType.getName(), rootType);
            requirementTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private PLMRequirementType getRequirementTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMRequirementType itemType = rootRequirementTypesMap.get(name);
            return itemType.getChildTypeByPath(path.substring(index + 1));
        } else {
            name = path;
            return rootRequirementTypesMap.get(name);
        }
    }

    private PLMRequirementType createRequirementTypeByPath(PLMRequirementType parentType, String path) {
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
            parentType = rootRequirementTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMRequirementType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType.setLifecycle(plmLifeCycle);
                parentType.setPriorityList(priorities);
                parentType = requirementTypeRepository.save(parentType);
                rootRequirementTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createRequirementTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMRequirementType childItemType = new PLMRequirementType();
            childItemType.setParent(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType.setLifecycle(parentType.getLifecycle());
            childItemType.setPriorityList(parentType.getPriorityList());
            childItemType = requirementTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createRequirementTypeByPath(parentType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createRequirementTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    private PLMRequirementType getRequirementTypes(String path) {
        PLMRequirementType requirementType = requirementTypesMapByPath.get(path);
        if (requirementType == null) {
            requirementType = getRequirementTypeByPath(path);
            if (requirementType == null) {
                requirementType = createRequirementTypeByPath(null, path);
            }
            requirementTypesMapByPath.put(path, requirementType);
        }

        return requirementType;
    }


}
