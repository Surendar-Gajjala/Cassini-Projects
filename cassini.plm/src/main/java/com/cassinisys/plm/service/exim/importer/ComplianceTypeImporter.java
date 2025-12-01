package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.service.classification.PGCObjectTypeService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ComplianceTypeImporter {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private PGCObjectTypeRepository pgcObjectTypeRepository;
    @Autowired
    private PGCObjectTypeAttributeRepository pgcObjectTypeAttributeRepository;
    @Autowired
    private PGCDeclarationTypeRepository declarationTypeRepository;
    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;
    @Autowired
    private PGCSpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private SubstancesImporter substancesImporter;
    @Autowired
    private SpecificationsImporter specificationsImporter;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;
    private static AutoNumber autoNumber;

    public static ConcurrentMap<String, PGCSubstanceType> rootSubstanceTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PGCSpecificationType> rootSpecificationTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PGCDeclarationType> rootDeclarationTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PGCDeclarationType> declarationTypesMapByPath = new ConcurrentHashMap<>();

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Part Number Source");
    }

    private PGCObjectTypeAttribute checkAttributeName(Integer type, String name) {
        PGCObjectType pgcObjectType = pgcObjectTypeRepository.findOne(type);
        PGCObjectTypeAttribute pgcObjectTypeAttribute = null;
        if (pgcObjectType != null) {
            pgcObjectTypeAttribute = pgcObjectTypeAttributeRepository.findByTypeAndName(pgcObjectType.getId(), name);
        }
        return pgcObjectTypeAttribute;
    }


    public void importPGCObjectTypeAttributes(Integer value, Enum objectType, PGCObjectTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {

            String name = stringListHashMap.get("Attribute Name");
            PGCObjectTypeAttribute pgcObjectTypeAttribute = null;
            if (name != null && name != "") {
                pgcObjectTypeAttribute = checkAttributeName(objectId, name);
            }

            if (pgcObjectTypeAttribute == null) {
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
                    attribute.setVisible(true);
                    attribute.setRequired(itemTypeImporter.getBoolPropertyValue("Is Required", stringListHashMap));
                    pgcObjectTypeAttributeRepository.save(attribute);

                }


            }

        }
    }

    public void importSubstanceTypes(Integer i, RowData stringListHashMap) {
        substancesImporter.loadSubstanceClassificationTree();
        getDefaultNumberSource();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PGCSubstanceType substanceType = rootSubstanceTypesMap.get(typeName);
            if (substanceType != null) {
                substanceType.setName(typeName);
                substanceType.setName(desc != null ? desc : typeName);
                substanceTypeRepository.save(substanceType);
                PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                importPGCObjectTypeAttributes(i, substanceType.getObjectType(), pgcObjectTypeAttribute, substanceType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PGCSubstanceType substanceType1 = substancesImporter.getSubstanceType(path);
                    if (substanceType1 != null) {
                        PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                        importPGCObjectTypeAttributes(i, substanceType1.getObjectType(), pgcObjectTypeAttribute, substanceType1.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PGCSubstanceType substanceType = substancesImporter.getSubstanceType(path);
                if (substanceType != null) {
                    PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                    importPGCObjectTypeAttributes(i, substanceType.getObjectType(), pgcObjectTypeAttribute, substanceType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importSpecificationTypes(Integer i, RowData stringListHashMap) {
        specificationsImporter.loadSpecificationClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PGCSpecificationType specificationType = rootSpecificationTypesMap.get(typeName);
            if (specificationType != null) {
                specificationType.setName(typeName);
                specificationType.setName(desc != null ? desc : typeName);
                specificationTypeRepository.save(specificationType);
                PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                importPGCObjectTypeAttributes(i, specificationType.getObjectType(), pgcObjectTypeAttribute, specificationType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PGCSpecificationType specificationType1 = specificationsImporter.getSpecificationType(path);
                    if (specificationType1 != null) {
                        PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                        importPGCObjectTypeAttributes(i, specificationType1.getObjectType(), pgcObjectTypeAttribute, specificationType1.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PGCSpecificationType specificationType = specificationsImporter.getSpecificationType(path);
                if (specificationType != null) {
                    PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                    importPGCObjectTypeAttributes(i, specificationType.getObjectType(), pgcObjectTypeAttribute, specificationType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void importDeclarationTypes(Integer i, RowData stringListHashMap) {
        loadDeclarationClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String cls = stringListHashMap.get("Type Class");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            PGCDeclarationType declarationType = rootDeclarationTypesMap.get(typeName);
            if (declarationType != null) {
                declarationType.setName(typeName);
                declarationType.setName(desc != null ? desc : typeName);
                declarationTypeRepository.save(declarationType);
                PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                importPGCObjectTypeAttributes(i, declarationType.getObjectType(), pgcObjectTypeAttribute, declarationType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PGCDeclarationType declarationType1 = getDeclarationTypes(path);
                    if (declarationType1 != null) {
                        PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                        importPGCObjectTypeAttributes(i, declarationType1.getObjectType(), pgcObjectTypeAttribute, declarationType1.getId(), stringListHashMap);
                        //dbItemTypeMap.put(customObjectTypeObject.getName(), customObjectTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PGCDeclarationType declarationType = getDeclarationTypes(path);
                if (declarationType != null) {
                    PGCObjectTypeAttribute pgcObjectTypeAttribute = new PGCObjectTypeAttribute();
                    importPGCObjectTypeAttributes(i, declarationType.getObjectType(), pgcObjectTypeAttribute, declarationType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(customObjectType.getName(), customObjectType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void loadDeclarationClassificationTree() {
        rootDeclarationTypesMap = new ConcurrentHashMap<>();
        declarationTypesMapByPath = new ConcurrentHashMap<>();
        List<PGCDeclarationType> rootTypes = pgcObjectTypeService.getDeclarationTypeTree();
        for (PGCDeclarationType rootType : rootTypes) {
            rootDeclarationTypesMap.put(rootType.getName(), rootType);
            declarationTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private PGCDeclarationType getSubstanceTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PGCDeclarationType itemType = rootDeclarationTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootDeclarationTypesMap.get(name);
        }
    }

    private PGCDeclarationType createDeclarationTypeByPath(PGCDeclarationType parentType, String path) {
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
            parentType = rootDeclarationTypesMap.get(name);
            if (parentType == null) {
                parentType = new PGCDeclarationType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = declarationTypeRepository.save(parentType);
                rootDeclarationTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createDeclarationTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PGCDeclarationType childItemType = new PGCDeclarationType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = declarationTypeRepository.save(childItemType);
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                parentType = createDeclarationTypeByPath(parentType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createDeclarationTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    private PGCDeclarationType getDeclarationTypes(String path) {
        PGCDeclarationType declarationType = declarationTypesMapByPath.get(path);
        if (declarationType == null) {
            declarationType = getSubstanceTypeByPath(path);
            if (declarationType == null) {
                declarationType = createDeclarationTypeByPath(null, path);
            }
            declarationTypesMapByPath.put(path, declarationType);
        }

        return declarationType;
    }


}