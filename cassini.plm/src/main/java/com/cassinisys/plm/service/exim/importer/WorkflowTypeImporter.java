package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflowType;
import com.cassinisys.plm.model.wf.PLMWorkflowTypeAttribute;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeAttributeRepository;
import com.cassinisys.plm.repo.wf.WorkflowTypeRepository;
import com.cassinisys.plm.service.plm.LifeCycleService;
import com.cassinisys.plm.service.wf.PLMWorkflowTypeService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 03-08-2021.
 */
@Service
public class WorkflowTypeImporter {

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
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private LifeCycleService lifeCycleService;
    @Autowired
    private WorkflowTypeRepository workflowTypeRepository;
    @Autowired
    private WorkflowTypeAttributeRepository workflowTypeAttributeRepository;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private PLMWorkflowTypeService workflowTypeService;
    private static AutoNumber autoNumber;
    private static PLMLifeCycle plmLifeCycle;
    @Autowired
    private AutoNumberService autoNumberService;


    public static ConcurrentMap<String, PLMWorkflowType> rootWorkflowTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMWorkflowType> workflowTypesMapByPath = new ConcurrentHashMap<>();

    private void getDefaultWorkflowLifecycle() {
        plmLifeCycle = lifeCycleService.findLifecycleByName("Default Part Number Source");
    }

    private void getDefaultNumberSource() {
        autoNumber = autoNumberService.getByName("Default Part Number Source");
    }

    private PLMWorkflowTypeAttribute checkAttributeName(Integer type, String name) {
        PLMWorkflowType workflowType = workflowTypeRepository.findOne(type);
        PLMWorkflowTypeAttribute workflowTypeAttribute = null;
        if (workflowType != null) {
            workflowTypeAttribute = workflowTypeAttributeRepository.findByWorkflowTypeAndName(workflowType.getId(), name);
        }
        return workflowTypeAttribute;
    }


    public void importPLMWorkflowTypeAttributes(Integer value, Enum objectType, PLMWorkflowTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {

            String name = stringListHashMap.get("Attribute Name");
            PLMWorkflowTypeAttribute workflowTypeAttribute = null;
            if (name != null && name != "") {
                workflowTypeAttribute = checkAttributeName(objectId, name);
            }

            if (workflowTypeAttribute == null) {
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
                    attribute.setWorkflowType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(itemTypeImporter.getBoolPropertyValue("Is Required", stringListHashMap));
                    workflowTypeAttributeRepository.save(attribute);

                }


            }

        }
    }

    public void importWorkflowTypes(Integer i, RowData stringListHashMap) {
        loadWorkflowTypeClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PLMWorkflowType workflowType = rootWorkflowTypesMap.get(typeName);
            if (workflowType != null) {
                workflowType.setName(typeName);
                workflowType.setName(desc != null ? desc : typeName);
                workflowTypeRepository.save(workflowType);
                PLMWorkflowTypeAttribute workflowTypeAttribute = new PLMWorkflowTypeAttribute();
                importPLMWorkflowTypeAttributes(i, workflowType.getObjectType(), workflowTypeAttribute, workflowType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMWorkflowType workflowTypeObject = getWorkflowTypes(path);
                    if (workflowTypeObject != null) {
                        PLMWorkflowTypeAttribute workflowTypeAttribute = new PLMWorkflowTypeAttribute();
                        importPLMWorkflowTypeAttributes(i, workflowTypeObject.getObjectType(), workflowTypeAttribute, workflowTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMWorkflowType workflowType = getWorkflowTypes(path);
                if (workflowType != null) {
                    PLMWorkflowTypeAttribute workflowTypeAttribute = new PLMWorkflowTypeAttribute();
                    importPLMWorkflowTypeAttributes(i, workflowType.getObjectType(), workflowTypeAttribute, workflowType.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void loadWorkflowTypeClassificationTree() {
        getDefaultWorkflowLifecycle();
        getDefaultNumberSource();
        rootWorkflowTypesMap = new ConcurrentHashMap<>();
        workflowTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMWorkflowType> rootTypes = workflowTypeService.getClassificationTree();
        for (PLMWorkflowType rootType : rootTypes) {
            rootWorkflowTypesMap.put(rootType.getName(), rootType);
            workflowTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private PLMWorkflowType getWorkflowTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMWorkflowType itemType = rootWorkflowTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootWorkflowTypesMap.get(name);
        }
    }

    private PLMWorkflowType createWorkflowTypeByPath(PLMWorkflowType parentType, String path) {
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
            parentType = rootWorkflowTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMWorkflowType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(autoNumber);
                parentType.setLifecycle(plmLifeCycle);
                parentType = workflowTypeRepository.save(parentType);
                rootWorkflowTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createWorkflowTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMWorkflowType childItemType = new PLMWorkflowType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType = workflowTypeRepository.save(childItemType);
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                parentType = createWorkflowTypeByPath(parentType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createWorkflowTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    private PLMWorkflowType getWorkflowTypes(String path) {
        PLMWorkflowType mesPLMWorkflowType = workflowTypesMapByPath.get(path);
        if (mesPLMWorkflowType == null) {
            mesPLMWorkflowType = getWorkflowTypeByPath(path);
            if (mesPLMWorkflowType == null) {
                mesPLMWorkflowType = createWorkflowTypeByPath(null, path);
            }
            workflowTypesMapByPath.put(path, mesPLMWorkflowType);
        }

        return mesPLMWorkflowType;
    }


}
