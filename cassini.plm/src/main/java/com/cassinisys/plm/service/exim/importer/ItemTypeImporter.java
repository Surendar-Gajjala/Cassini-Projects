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
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.ItemTypeAttributeRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class ItemTypeImporter {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    private Lov defaultLOV;
    private Map<String, PLMLifeCycle> lifeCycleKeyMap = new HashMap<>();
    private String[] tabs = new String[]{"bom", "whereUsed", "changes", "files", "mfrParts", "relatedItems", "projects", "requirements"};
    private String[] classes = new String[]{"Product", "Part", "Assembly", "Document"};
    private Map<String, String> headersMap = null;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private IndividualItemsImporter individualItemsImporter;


    public JSONArray getTextValidations(String min, String max, Boolean cap, Boolean small, Boolean mixCase, String pattern) {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("key", "MIN_LENGTH_OF_CHARACTERS");
        json.put("value", min);
        json.put("type", "number");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "MAX_LENGTH_OF_CHARACTERS");
        json1.put("value", max);
        json1.put("type", "number");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "ALL_CAPITAL");
        json2.put("value", cap);
        json2.put("type", "checkbox");
        array.put(json2);
        JSONObject json3 = new JSONObject();
        json3.put("key", "ALL_SMALL");
        json3.put("value", small);
        json3.put("type", "checkbox");
        array.put(json3);
        JSONObject json4 = new JSONObject();
        json4.put("key", "CAPITAL_AND_SMALL");
        json4.put("value", mixCase);
        json4.put("type", "checkbox");
        array.put(json4);
        JSONObject json5 = new JSONObject();
        json5.put("key", "PATTERN");
        json5.put("value", pattern);
        json5.put("type", "text");
        array.put(json5);
        return array;
    }

    public JSONArray getLongTextValidations(String max, Boolean cap, Boolean small, Boolean mixCase) {
        JSONArray array = new JSONArray();

        JSONObject json = new JSONObject();
        json.put("key", "MAX_LENGTH_OF_CHARACTERS");
        json.put("value", max);
        json.put("type", "number");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "ALL_CAPITAL");
        json1.put("value", cap);
        json1.put("type", "checkbox");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "ALL_SMALL");
        json2.put("value", small);
        json2.put("type", "checkbox");
        array.put(json2);
        JSONObject json3 = new JSONObject();
        json3.put("key", "CAPITAL_AND_SMALL");
        json3.put("value", mixCase);
        json3.put("type", "checkbox");
        array.put(json3);
        return array;
    }

    public JSONArray getIntegerValidations(String min, String max, Boolean cap, Boolean small, Boolean mixCase) {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("key", "MIN_VALUE");
        json.put("value", min);
        json.put("type", "number");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "MAX_VALUE");
        json1.put("value", max);
        json1.put("type", "number");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "ONLY_POSITIVE_VALUES");
        json2.put("value", cap);
        json2.put("type", "checkbox");
        array.put(json2);
        JSONObject json3 = new JSONObject();
        json3.put("key", "ONLY_NEGATIVE_VALUES");
        json3.put("value", small);
        json3.put("type", "checkbox");
        array.put(json3);
        JSONObject json4 = new JSONObject();
        json4.put("key", "POSITIVE_AND_NEGATIVE");
        json4.put("value", mixCase);
        json4.put("type", "checkbox");
        array.put(json4);
        return array;
    }

    public JSONArray getDateValidations(String fromDate, String toDate, String format) {
        String[] formatArray = {"dd/mm/yyyy", "yyyy/mm/dd", "yyyy/dd/mm", "dd/MM/yyyy"};
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("key", "FROM_DATE");
        json.put("value", fromDate);
        json.put("type", "date");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "TO_DATE");
        json1.put("value", toDate);
        json1.put("type", "date");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "DATE_FORMAT");
        json2.put("value", format);
        json2.put("values", formatArray);
        json2.put("type", "select");
        array.put(json2);
        return array;
    }

    public JSONArray getTimeValidations(String fromTime, String toTime, String format) {
        String[] formatArray = {"hh:mm:ss", "hh:mm", "hh"};
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("key", "FROM_TIME");
        json.put("value", fromTime);
        json.put("type", "time");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "TO_TIME");
        json1.put("value", toTime);
        json1.put("type", "time");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "TIME_FORMAT");
        json2.put("value", format);
        json2.put("values", formatArray);
        json2.put("type", "select");
        array.put(json2);
        return array;
    }


    public JSONArray getCurrencyValidations(String min, String max, Boolean cap, Boolean small, Boolean mixCase) {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("key", "NO_OF_DECIMALS_TO_DISPLAY");
        json.put("value", min);
        json.put("type", "number");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "NO_OF_DECIMALS_TO_ENTER");
        json1.put("value", max);
        json1.put("type", "number");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "ONLY_POSITIVE_VALUES");
        json2.put("value", cap);
        json2.put("type", "checkbox");
        array.put(json2);
        JSONObject json3 = new JSONObject();
        json3.put("key", "ONLY_NEGATIVE_VALUES");
        json3.put("value", small);
        json3.put("type", "checkbox");
        array.put(json3);
        JSONObject json4 = new JSONObject();
        json4.put("key", "POSITIVE_AND_NEGATIVE");
        json4.put("value", mixCase);
        json4.put("type", "checkbox");
        array.put(json4);
        return array;
    }

    public JSONArray getDoubleValidations(String min, String max, String display, String enter, Boolean cap, Boolean small, Boolean mixCase) {
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();

        JSONObject json5 = new JSONObject();
        json5.put("key", "MIN_VALUE");
        json5.put("value", min);
        json5.put("type", "number");
        array.put(json5);
        JSONObject json6 = new JSONObject();
        json6.put("key", "MAX_VALUE");
        json6.put("value", max);
        json6.put("type", "number");
        array.put(json6);

        json.put("key", "NO_OF_DECIMALS_TO_DISPLAY");
        json.put("value", display);
        json.put("type", "number");
        array.put(json);
        JSONObject json1 = new JSONObject();
        json1.put("key", "NO_OF_DECIMALS_TO_ENTER");
        json1.put("value", enter);
        json1.put("type", "number");
        array.put(json1);
        JSONObject json2 = new JSONObject();
        json2.put("key", "ONLY_POSITIVE_VALUES");
        json2.put("value", cap);
        json2.put("type", "checkbox");
        array.put(json2);
        JSONObject json3 = new JSONObject();
        json3.put("key", "ONLY_NEGATIVE_VALUES");
        json3.put("value", small);
        json3.put("type", "checkbox");
        array.put(json3);
        JSONObject json4 = new JSONObject();
        json4.put("key", "POSITIVE_AND_NEGATIVE");
        json4.put("value", mixCase);
        json4.put("type", "checkbox");
        array.put(json4);
        return array;
    }

    public Boolean getBoolValue(Integer value, String columnName, HashMap<String, List<String>> stringListHashMap) {
        Boolean val = false;
        /*String multiListCheck = stringListHashMap.get(columnName, stringListHashMap);
        if (multiListCheck != null && multiListCheck != "") {
            if (multiListCheck.equals("Yes")) {
                val = true;
            } else {
                val = false;
            }
        }*/
        return val;
    }

    public Boolean getBoolPropertyValue(String columnName, RowData stringListHashMap) {
        Boolean val = false;
        String multiListCheck = stringListHashMap.get(columnName);
        if (multiListCheck != null && multiListCheck != "") {
            if (multiListCheck.equals("Yes")) {
                val = true;
            } else {
                val = false;
            }
        }
        return val;
    }


    private PLMItemTypeAttribute checkAttributeName(Integer itemType, String name) {
        PLMItemType itemType1 = itemTypeRepository.findOne(itemType);
        PLMItemTypeAttribute itemTypeAttribute = null;
        if (itemType1.getParentType() != null) {
            itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(itemType1.getId(), name);
        }
        return itemTypeAttribute;
    }



    public void importItemTypeAttributes(Integer value, Enum objectType, PLMItemTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {

            String name = stringListHashMap.get("Attribute Name");
            PLMItemTypeAttribute itemTypeAttribute = null;
            if (name != null && name != "") {
                itemTypeAttribute = checkAttributeName(objectId, name);
            }

            if (itemTypeAttribute == null) {
                String dataType = stringListHashMap.get("Data Type");
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length");
                        String max = stringListHashMap.get("Maximum length");
                        Boolean cap = getBoolPropertyValue("All uppercase (Yes or No)", stringListHashMap);
                        Boolean small = getBoolPropertyValue("All lowercase (Yes or No)", stringListHashMap);
                        Boolean mixCase = getBoolPropertyValue("Allow mixed case (Yes or No)", stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
                        JSONArray array = getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length");
                        Boolean cap = getBoolPropertyValue("All uppercase (Yes or No)", stringListHashMap);
                        Boolean small = getBoolPropertyValue("All lowercase (Yes or No)", stringListHashMap);
                        Boolean mixCase = getBoolPropertyValue("Allow mixed case (Yes or No)", stringListHashMap);
                        JSONArray array = getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value");
                        String max = stringListHashMap.get("Maximum Value");
                        Boolean positiv = getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date");
                        String toDate = stringListHashMap.get("To date");
                        String format = stringListHashMap.get("Date format");
                        JSONArray array = getDateValidations(fromDate, toDate, format);
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
                        attribute.setListMultiple(getBoolPropertyValue("Multi List (Yes or No)", stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time");
                        String toTime = stringListHashMap.get("To Time");
                        String format = stringListHashMap.get("Time Format");
                        JSONArray array = getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display");
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = getCurrencyValidations(dis, enter, positiv, negative, both);
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
                        Boolean positiv = getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
                        JSONArray array = getDoubleValidations(min, max, dis, enter, positiv, negative, both);
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
                    attribute.setItemType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(getBoolPropertyValue("Is Required", stringListHashMap));
                    attribute.setConfigurable(getBoolPropertyValue("Configurable (Yes or No)", stringListHashMap));
                    attribute.setRevisionSpecific(getBoolPropertyValue("Revision Specific (Yes or No)", stringListHashMap));
                    attribute.setChangeControlled(getBoolPropertyValue("Change Controlled (Yes or No)", stringListHashMap));
                    attribute.setAllowEditAfterRelease(getBoolPropertyValue("Allow Edit After Release (Yes or No)", stringListHashMap));
                    itemTypeAttributeRepository.save(attribute);

                }


            }

        }
    }



    public void importItemTypeObjects(Integer i, RowData stringListHashMap) {
        individualItemsImporter.loadItemClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        ItemClass itemClass = ItemClass.valueOf(cls.toUpperCase());
        if (typeName != null && typeName != "") {
            PLMItemType itemType = this.individualItemsImporter.rootItemTypesMap.get(typeName);
            if (itemType != null) {
                itemType.setName(typeName);
                itemType.setName(desc != null ? desc : typeName);
                itemTypeRepository.save(itemType);
                PLMItemTypeAttribute itemTypeAttribute = new PLMItemTypeAttribute();
                importItemTypeAttributes(i, itemType.getObjectType(), itemTypeAttribute, itemType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMItemType itemTypeObject = this.individualItemsImporter.getItemTypes(path, itemClass != null ? itemClass : ItemClass.OTHER);
                    if (itemTypeObject != null) {
                        PLMItemTypeAttribute itemTypeAttribute = new PLMItemTypeAttribute();
                        importItemTypeAttributes(i, itemTypeObject.getObjectType(), itemTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMItemType itemType = individualItemsImporter.getItemTypes(path, itemClass != null ? itemClass : ItemClass.OTHER);
                if (itemType != null) {
                    PLMItemTypeAttribute itemTypeAttribute = new PLMItemTypeAttribute();
                    importItemTypeAttributes(i, itemType.getObjectType(), itemTypeAttribute, itemType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

}