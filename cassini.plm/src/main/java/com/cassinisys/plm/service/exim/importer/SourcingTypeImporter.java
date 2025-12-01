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
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 30-07-2021.
 */
@Service
public class SourcingTypeImporter {

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private LovRepository lovRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();

    @Autowired
    private Importer importer;

    @Autowired
    private ItemTypeImporter itemTypeImporter;

    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerTypeAttributeRepository manufacturerTypeAttributeRepository;

    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private ManufacturerPartTypeAttributeRepository manufacturerPartTypeAttributeRepository;

    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private SupplierTypeAttributeRepository supplierTypeAttributeRepository;
    @Autowired
    private IndividualMfrAndMfrPartsImporter individualMfrAndMfrPartsImporter;
    @Autowired
    private SuppliersImporter suppliersImporter;


    public void importMfrTypeObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.individualMfrAndMfrPartsImporter.loadMfrClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMManufacturerType sourcingType = this.individualMfrAndMfrPartsImporter.rootMfrTypesMap.get(typeName);
            if (sourcingType != null) {
                sourcingType.setName(typeName);
                sourcingType.setName(desc != null ? desc : typeName);
                manufacturerTypeRepository.save(sourcingType);
                PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = new PLMManufacturerTypeAttribute();
                importManufacturerTypeAttributes(i, sourcingType.getObjectType(), plmManufacturerTypeAttribute, sourcingType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    PLMManufacturerType sourcingTypeObject = this.individualMfrAndMfrPartsImporter.getMfrType(path);
                    if (sourcingTypeObject != null) {
                        PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = new PLMManufacturerTypeAttribute();
                        importManufacturerTypeAttributes(i, sourcingTypeObject.getObjectType(), plmManufacturerTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMManufacturerType sourcingTypeObject = this.individualMfrAndMfrPartsImporter.getMfrType(path);
                if (sourcingTypeObject != null) {
                    PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = new PLMManufacturerTypeAttribute();
                    importManufacturerTypeAttributes(i, sourcingTypeObject.getObjectType(), plmManufacturerTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }


    }


    public void importMfrPartTypeObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {

        this.individualMfrAndMfrPartsImporter.loadMfrPartClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMManufacturerPartType sourcingType = this.individualMfrAndMfrPartsImporter.rootMfrPartTypesMap.get(typeName);
            if (sourcingType != null) {
                sourcingType.setName(typeName);
                sourcingType.setName(desc != null ? desc : typeName);
                manufacturerPartTypeRepository.save(sourcingType);
                PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute = new PLMManufacturerPartTypeAttribute();
                importManufacturerPartTypeAttributes(i, sourcingType.getObjectType(), manufacturerPartTypeAttribute, sourcingType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    PLMManufacturerPartType sourcingTypeObject = this.individualMfrAndMfrPartsImporter.getMfrPartType(path);
                    if (sourcingTypeObject != null) {
                        PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute = new PLMManufacturerPartTypeAttribute();
                        importManufacturerPartTypeAttributes(i, sourcingTypeObject.getObjectType(), manufacturerPartTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMManufacturerPartType sourcingTypeObject = this.individualMfrAndMfrPartsImporter.getMfrPartType(path);
                if (sourcingTypeObject != null) {
                    PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute = new PLMManufacturerPartTypeAttribute();
                    importManufacturerPartTypeAttributes(i, sourcingTypeObject.getObjectType(), manufacturerPartTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }


    }


    public void importSupplierTypeObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {

        this.suppliersImporter.loadSupplierClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMSupplierType sourcingType = this.suppliersImporter.rootSupplierTypesMap.get(typeName);
            if (sourcingType != null) {
                sourcingType.setName(typeName);
                sourcingType.setName(desc != null ? desc : typeName);
                supplierTypeRepository.save(sourcingType);
                PLMSupplierTypeAttribute plmSupplierTypeAttribute = new PLMSupplierTypeAttribute();
                importSupplierTypeAttributes(i, sourcingType.getObjectType(), plmSupplierTypeAttribute, sourcingType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    PLMSupplierType sourcingTypeObject = this.suppliersImporter.getSupplierType(path);
                    if (sourcingTypeObject != null) {
                        PLMSupplierTypeAttribute plmSupplierTypeAttribute = new PLMSupplierTypeAttribute();
                        importSupplierTypeAttributes(i, sourcingTypeObject.getObjectType(), plmSupplierTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMSupplierType sourcingTypeObject = this.suppliersImporter.getSupplierType(path);
                if (sourcingTypeObject != null) {
                    PLMSupplierTypeAttribute plmSupplierTypeAttribute = new PLMSupplierTypeAttribute();
                    importSupplierTypeAttributes(i, sourcingTypeObject.getObjectType(), plmSupplierTypeAttribute, sourcingTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }


    }


    private Boolean checkSourcingObjectTypeAttributeName(Enum objectType, Integer Type, String name) {
        Boolean isExist = false;
        if (objectType.toString().equals("MANUFACTURERTYPE")) {
            PLMManufacturerType plmManufacturerType = manufacturerTypeRepository.findOne(Type);
            PLMManufacturerTypeAttribute plmManufacturerTypeAttribute = null;
            if (plmManufacturerType != null) {
                plmManufacturerTypeAttribute = manufacturerTypeAttributeRepository.findByMfrTypeAndName(plmManufacturerType.getId(), name);
                if (plmManufacturerTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("MANUFACTURERPARTTYPE")) {
            PLMManufacturerPartType manufacturerPartType = manufacturerPartTypeRepository.findOne(Type);
            PLMManufacturerPartTypeAttribute manufacturerPartTypeAttribute = null;
            if (manufacturerPartType != null) {
                manufacturerPartTypeAttribute = manufacturerPartTypeAttributeRepository.findByMfrPartTypeAndName(manufacturerPartType.getId(), name);
                if (manufacturerPartTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("SUPPLIERTYPE")) {
            PLMSupplierType plmSupplierType = supplierTypeRepository.findOne(Type);
            PLMSupplierTypeAttribute plmSupplierTypeAttribute = null;
            if (plmSupplierType != null) {
                plmSupplierTypeAttribute = supplierTypeAttributeRepository.findByTypeAndName(plmSupplierType.getId(), name);
                if (plmSupplierTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        }

        return isExist;
    }


    public void importManufacturerTypeAttributes(Integer value, Enum objectType, PLMManufacturerTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name".trim()) && stringListHashMap.containsKey("Data Type".trim())) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            PLMManufacturerAttribute manufacturerAttribute = null;
            if (name != null && name != "") {
                isExist = checkSourcingObjectTypeAttributeName(objectType, objectId, name);
            }

            if (!isExist) {
                String dataType = stringListHashMap.get("Data Type".trim());
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length".trim());
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
                        JSONArray array = this.itemTypeImporter.getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date".trim());
                        String toDate = stringListHashMap.get("To date".trim());
                        String format = stringListHashMap.get("Date format".trim());
                        JSONArray array = this.itemTypeImporter.getDateValidations(fromDate, toDate, format);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("LIST")) {
                        String lvName = stringListHashMap.get("List Name".trim());
                        if (lvName != null && lvName != null) {
                            Lov lov = lovRepository.findByName(lvName);
                            if (lov == null) {
                                Lov newObj = new Lov();
                                String commasepLovs = stringListHashMap.get("List of Values (Comma separated)".trim());
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
                        attribute.setListMultiple(this.itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)".trim(), stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time".trim());
                        String toTime = stringListHashMap.get("To Time".trim());
                        String format = stringListHashMap.get("Time Format".trim());
                        JSONArray array = this.itemTypeImporter.getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getCurrencyValidations(dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("DOUBLE")) {
                        String qom = stringListHashMap.get("QoM".trim());
                        if (qom != null && qom != null) {
                            Measurement measurement = measurementRepository.findByName(qom);
                            if (measurement != null) {
                                attribute.setMeasurement(measurement);
                            }

                        }

                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getDoubleValidations(min, max, dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("OBJECT")) {
                        String objName = stringListHashMap.get("Object Name".trim());
                        attribute.setRefType(PLMObjectType.valueOf(objName));
                    }
                    if (dataType.equals("FORMULA")) {
                        String formula = stringListHashMap.get("Enter Formula".trim());
                        attribute.setFormula(formula);
                    }


                    String desc = stringListHashMap.get("Attribute Description".trim());
                    String attrGrp = stringListHashMap.get("Attribute Group".trim());
                    String defaultValue = stringListHashMap.get("Default value".trim());
                    attribute.setAttributeGroup(attrGrp);
                    attribute.setName(name);
                    attribute.setMfrType(objectId);
                    attribute.setObjectType(PLMObjectType.MANUFACTURERTYPE);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);

                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    manufacturerTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


    public void importManufacturerPartTypeAttributes(Integer value, Enum objectType, PLMManufacturerPartTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name".trim()) && stringListHashMap.containsKey("Data Type".trim())) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            if (name != null && name != "") {
                isExist = checkSourcingObjectTypeAttributeName(objectType, objectId, name);
            }

            if (!isExist) {
                String dataType = stringListHashMap.get("Data Type".trim());
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length".trim());
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
                        JSONArray array = this.itemTypeImporter.getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date".trim());
                        String toDate = stringListHashMap.get("To date".trim());
                        String format = stringListHashMap.get("Date format".trim());
                        JSONArray array = this.itemTypeImporter.getDateValidations(fromDate, toDate, format);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("LIST")) {
                        String lvName = stringListHashMap.get("List Name".trim());
                        if (lvName != null && lvName != null) {
                            Lov lov = lovRepository.findByName(lvName);
                            if (lov == null) {
                                Lov newObj = new Lov();
                                String commasepLovs = stringListHashMap.get("List of Values (Comma separated)".trim());
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
                        attribute.setListMultiple(this.itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)".trim(), stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time".trim());
                        String toTime = stringListHashMap.get("To Time".trim());
                        String format = stringListHashMap.get("Time Format".trim());
                        JSONArray array = this.itemTypeImporter.getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getCurrencyValidations(dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("DOUBLE")) {
                        String qom = stringListHashMap.get("QoM".trim());
                        if (qom != null && qom != null) {
                            Measurement measurement = measurementRepository.findByName(qom);
                            if (measurement != null) {
                                attribute.setMeasurement(measurement);
                            }

                        }

                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getDoubleValidations(min, max, dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("OBJECT")) {
                        String objName = stringListHashMap.get("Object Name".trim());
                        attribute.setRefType(PLMObjectType.valueOf(objName));
                    }
                    if (dataType.equals("FORMULA")) {
                        String formula = stringListHashMap.get("Enter Formula".trim());
                        attribute.setFormula(formula);
                    }


                    String desc = stringListHashMap.get("Attribute Description".trim());
                    String attrGrp = stringListHashMap.get("Attribute Group".trim());
                    String defaultValue = stringListHashMap.get("Default value".trim());
                    attribute.setAttributeGroup(attrGrp);
                    attribute.setName(name);
                    attribute.setMfrPartType(objectId);
                    attribute.setObjectType(PLMObjectType.MANUFACTURERPARTTYPE);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);

                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    manufacturerPartTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


    public void importSupplierTypeAttributes(Integer value, Enum objectType, PLMSupplierTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name".trim()) && stringListHashMap.containsKey("Data Type".trim())) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            if (name != null && name != "") {
                isExist = checkSourcingObjectTypeAttributeName(objectType, objectId, name);
            }

            if (!isExist) {
                String dataType = stringListHashMap.get("Data Type".trim());
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length".trim());
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
                        JSONArray array = this.itemTypeImporter.getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date".trim());
                        String toDate = stringListHashMap.get("To date".trim());
                        String format = stringListHashMap.get("Date format".trim());
                        JSONArray array = this.itemTypeImporter.getDateValidations(fromDate, toDate, format);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("LIST")) {
                        String lvName = stringListHashMap.get("List Name".trim());
                        if (lvName != null && lvName != null) {
                            Lov lov = lovRepository.findByName(lvName);
                            if (lov == null) {
                                Lov newObj = new Lov();
                                String commasepLovs = stringListHashMap.get("List of Values (Comma separated)".trim());
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
                        attribute.setListMultiple(this.itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)".trim(), stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time".trim());
                        String toTime = stringListHashMap.get("To Time".trim());
                        String format = stringListHashMap.get("Time Format".trim());
                        JSONArray array = this.itemTypeImporter.getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter");
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getCurrencyValidations(dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("DOUBLE")) {
                        String qom = stringListHashMap.get("QoM".trim());
                        if (qom != null && qom != null) {
                            Measurement measurement = measurementRepository.findByName(qom);
                            if (measurement != null) {
                                attribute.setMeasurement(measurement);
                            }

                        }

                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getDoubleValidations(min, max, dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("OBJECT")) {
                        String objName = stringListHashMap.get("Object Name".trim());
                        attribute.setRefType(PLMObjectType.valueOf(objName));
                    }
                    if (dataType.equals("FORMULA")) {
                        String formula = stringListHashMap.get("Enter Formula".trim());
                        attribute.setFormula(formula);
                    }


                    String desc = stringListHashMap.get("Attribute Description".trim());
                    String attrGrp = stringListHashMap.get("Attribute Group".trim());
                    String defaultValue = stringListHashMap.get("Default value".trim());
                    attribute.setAttributeGroup(attrGrp);
                    attribute.setName(name);
                    attribute.setType(objectId);
                    attribute.setObjectType(PLMObjectType.SUPPLIERTYPE);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    supplierTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


}
