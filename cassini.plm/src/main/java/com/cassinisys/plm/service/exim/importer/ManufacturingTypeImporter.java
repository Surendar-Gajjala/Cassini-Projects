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
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mes.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManufacturingTypeImporter {
    Map<String, MESPlantType> dbPlantTypeMap = new LinkedHashMap();
    Map<String, MESAssemblyLineType> dbALTypeMap = new LinkedHashMap();
    Map<String, MESWorkCenterType> dbWCTypeMap = new LinkedHashMap();
    Map<String, MESMachineType> dbMCTypeMap = new LinkedHashMap();
    Map<String, MESEquipmentType> dbEQTypeMap = new LinkedHashMap();
    Map<String, MESInstrumentType> dbIMTypeMap = new LinkedHashMap();
    Map<String, MESToolType> dbToolTypeMap = new LinkedHashMap();
    Map<String, MESJigsFixtureType> dbJFTypeMap = new LinkedHashMap();
    Map<String, MESMaterialType> dbMTTypeMap = new LinkedHashMap();
    Map<String, MESManpowerType> dbMPTypeMap = new LinkedHashMap();
    Map<String, MESOperationType> dbOPTypeMap = new LinkedHashMap();
    Map<String, MESProductionOrderType> dbPOTypeMap = new LinkedHashMap();
    Map<String, MESServiceOrderType> dbSOTypeMap = new LinkedHashMap();
    @Autowired
    private MESObjectTypeRepository objectTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private ProductionOrderTypeRepository productionOrderTypeRepository;
    @Autowired
    private ServiceOrderTypeRepository serviceOrderTypeRepository;
    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private InstrumentTypeRepository instrumentTypeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MESPlantRepository plantRepository;
    @Autowired
    private MESObjectRepository mesObjectRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private MESToolRepository toolRepository;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MESManpowerRepository manpowerRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;
    @Autowired
    private MESOperationRepository operationRepository;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MachineImporter machineImporter;
    @Autowired
    private AssemblyLinesImporter assemblyLinesImporter;
    @Autowired
    private MaterialImporter materialImporter;
    @Autowired
    private ToolImporter toolImporter;
    @Autowired
    private WorkCentersImporter workCentersImporter;
    @Autowired
    private JigsAndFixturesImporter jigsAndFixturesImporter;
    @Autowired
    private OperationImporter operationImporter;
    @Autowired
    private ManpowersImporter manpowersImporter;
    @Autowired
    private EquipmentImporter equipmentImporter;
    @Autowired
    private InstrumentImporter instrumentImporter;

    private void getMESDBTypes() {
        List<MESPlantType> plantTypes = plantTypeRepository.findAll();
        this.dbPlantTypeMap = plantTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESAssemblyLineType> assemblyLineTypes = assemblyLineTypeRepository.findAll();
        this.dbALTypeMap = assemblyLineTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESWorkCenterType> workCenterTypes = workCenterTypeRepository.findAll();
        this.dbWCTypeMap = workCenterTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESMachineType> machineTypes = machineTypeRepository.findAll();
        this.dbMCTypeMap = machineTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESEquipmentType> equipmentTypes = equipmentTypeRepository.findAll();
        this.dbEQTypeMap = equipmentTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESInstrumentType> instrumentTypes = instrumentTypeRepository.findAll();
        this.dbIMTypeMap = instrumentTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESToolType> toolTypes = toolTypeRepository.findAll();
        this.dbToolTypeMap = toolTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESJigsFixtureType> jigsFixtureTypes = jigsFixtureTypeRepository.findAll();
        this.dbJFTypeMap = jigsFixtureTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESMaterialType> materialTypes = materialTypeRepository.findAll();
        this.dbMTTypeMap = materialTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESManpowerType> manpowerTypes = manpowerTypeRepository.findAll();
        this.dbMPTypeMap = manpowerTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESOperationType> operationTypes = operationTypeRepository.findAll();
        this.dbOPTypeMap = operationTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESProductionOrderType> productionOrderTypes = productionOrderTypeRepository.findAll();
        this.dbPOTypeMap = productionOrderTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<MESServiceOrderType> serviceOrderTypes = serviceOrderTypeRepository.findAll();
        this.dbSOTypeMap = serviceOrderTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
    }

    private void initAutoWiredValues() {
        List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
        this.autoWireKeyMap = autoNumbers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }


    public void importManufacturingObjects(Integer i, MESType type, Enum objectType, RowData stringListHashMap, String plant) {

        if (objectType.toString().equals("PLANTTYPE")) {
            createPlantTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("MACHINETYPE")) {
            createMachineTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("ASSEMBLYLINETYPE")) {
            createAssemblyLineTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("MATERIALTYPE")) {
            createMaterialTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("TOOLTYPE")) {
            createToolTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("WORKCENTERTYPE")) {
            createWorkCenterTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("JIGFIXTURETYPE")) {
            createJigsAndFixtureTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("OPERATIONTYPE")) {
            createOperationTypeObjects(stringListHashMap);

        } else if (objectType.toString().equals("PRODUCTIONORDERTYPE")) {
           // createProductOrderObjects(objectType, index, path, desc, i, numberSource, stringListHashMap);
        } else if (objectType.toString().equals("SERVICEORDERTYPE")) {
           // createServiceOrderObjects(objectType, index, path, desc, i, numberSource, stringListHashMap);

        } else if (objectType.toString().equals("MANPOWERTYPE")) {
            createManpowerObjects(stringListHashMap);

        } else if (objectType.toString().equals("EQUIPMENTTYPE")) {
            createEquipmentObjects(stringListHashMap);

        } else if (objectType.toString().equals("INSTRUMENTTYPE")) {

            createInstrumentObjects(stringListHashMap);


        }


    }

    public void createPlantTypeObjects(RowData stringListHashMap) {
        this.plantsImporter.loadPlantClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESPlantType plantType = this.plantsImporter.rootPlantTypesMap.get(typeName);
            if (plantType != null) {
                plantType.setName(typeName);
                plantType.setName(desc != null ? desc : typeName);
                plantTypeRepository.save(plantType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(plantType.getObjectType(), mesObjectTypeAttribute, plantType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESPlantType plantTypeObj = this.plantsImporter.getPlantTypes(path);
                    if (plantTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(plantTypeObj.getObjectType(), mesObjectTypeAttribute, plantTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }


        } else {
            if (path != null && path != "") {
                MESPlantType plantTypeObj = this.plantsImporter.getPlantTypes(path);
                if (plantTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(plantTypeObj.getObjectType(), mesObjectTypeAttribute, plantTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }

        }

    }


    private void createMachineTypeObjects(RowData stringListHashMap) {
        this.machineImporter.loadMachineClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESMachineType machineType = this.machineImporter.rootMachineTypesMap.get(typeName);
            if (machineType != null) {
                machineType.setName(typeName);
                machineType.setName(desc != null ? desc : typeName);
                machineTypeRepository.save(machineType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(machineType.getObjectType(), mesObjectTypeAttribute, machineType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESMachineType machineTypeObj = this.machineImporter.getMachineTypes(path);
                    if (machineTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(machineTypeObj.getObjectType(), mesObjectTypeAttribute, machineTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }


        } else {
            if (path != null && path != "") {
                MESMachineType machineTypeObj = this.machineImporter.getMachineTypes(path);
                if (machineTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(machineTypeObj.getObjectType(), mesObjectTypeAttribute, machineTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }

        }
    }


    private void createAssemblyLineTypeObjects(RowData stringListHashMap) {
        this.assemblyLinesImporter.loadAssemblyLineClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESAssemblyLineType assemblyLineType = this.assemblyLinesImporter.rootAssemblyLineTypesMap.get(typeName);
            if (assemblyLineType != null) {
                assemblyLineType.setName(typeName);
                assemblyLineType.setName(desc != null ? desc : typeName);
                assemblyLineTypeRepository.save(assemblyLineType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(assemblyLineType.getObjectType(), mesObjectTypeAttribute, assemblyLineType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESAssemblyLineType assemblyLineTypeObj = this.assemblyLinesImporter.getAssemblyLineTypes(path);
                    if (assemblyLineTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(assemblyLineTypeObj.getObjectType(), mesObjectTypeAttribute, assemblyLineTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESAssemblyLineType assemblyLineTypeObj = this.assemblyLinesImporter.getAssemblyLineTypes(path);
                if (assemblyLineTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(assemblyLineTypeObj.getObjectType(), mesObjectTypeAttribute, assemblyLineTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }


    private void createMaterialTypeObjects(RowData stringListHashMap) {
        this.materialImporter.loadMaterialClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESMaterialType materialType = this.materialImporter.rootMaterialTypesMap.get(typeName);
            if (materialType != null) {
                materialType.setName(typeName);
                materialType.setName(desc != null ? desc : typeName);
                materialTypeRepository.save(materialType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(materialType.getObjectType(), mesObjectTypeAttribute, materialType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESMaterialType materialTypeObj = this.materialImporter.getMaterialTypes(path);
                    if (materialTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(materialTypeObj.getObjectType(), mesObjectTypeAttribute, materialTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESMaterialType materialTypeObj = this.materialImporter.getMaterialTypes(path);
                if (materialTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(materialTypeObj.getObjectType(), mesObjectTypeAttribute, materialTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }


    private void createToolTypeObjects(RowData stringListHashMap) {
        this.toolImporter.loadToolClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESToolType toolType = this.toolImporter.rootToolTypesMap.get(typeName);
            if (toolType != null) {
                toolType.setName(typeName);
                toolType.setName(desc != null ? desc : typeName);
                toolTypeRepository.save(toolType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(toolType.getObjectType(), mesObjectTypeAttribute, toolType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESToolType toolTypeObj = this.toolImporter.getToolTypes(path);
                    if (toolTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(toolTypeObj.getObjectType(), mesObjectTypeAttribute, toolTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESToolType toolTypeObj = this.toolImporter.getToolTypes(path);
                if (toolTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(toolTypeObj.getObjectType(), mesObjectTypeAttribute, toolTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }

    }


    private void createWorkCenterTypeObjects(RowData stringListHashMap) {
        this.workCentersImporter.loadWorkCenterClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESWorkCenterType workCenterType = this.workCentersImporter.rootWorkCenterTypesMap.get(typeName);
            if (workCenterType != null) {
                workCenterType.setName(typeName);
                workCenterType.setName(desc != null ? desc : typeName);
                workCenterTypeRepository.save(workCenterType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(workCenterType.getObjectType(), mesObjectTypeAttribute, workCenterType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESWorkCenterType workCenterTypeObj = this.workCentersImporter.getWorkCenterTypes(path);
                    if (workCenterTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(workCenterTypeObj.getObjectType(), mesObjectTypeAttribute, workCenterTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESWorkCenterType workCenterTypeObj = this.workCentersImporter.getWorkCenterTypes(path);
                if (workCenterTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(workCenterTypeObj.getObjectType(), mesObjectTypeAttribute, workCenterTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }

    }


    private void createJigsAndFixtureTypeObjects(RowData stringListHashMap) {
        this.jigsAndFixturesImporter.loadJigsFixtureClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESJigsFixtureType jigsFixtureType = this.jigsAndFixturesImporter.rootJigsFixtureTypesMap.get(typeName);
            if (jigsFixtureType != null) {
                jigsFixtureType.setName(typeName);
                jigsFixtureType.setName(desc != null ? desc : typeName);
                jigsFixtureTypeRepository.save(jigsFixtureType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(jigsFixtureType.getObjectType(), mesObjectTypeAttribute, jigsFixtureType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESJigsFixtureType jigsFixtureTypeObj = this.jigsAndFixturesImporter.getJigsFixtureTypes(path);
                    if (jigsFixtureTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(jigsFixtureTypeObj.getObjectType(), mesObjectTypeAttribute, jigsFixtureTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESJigsFixtureType jigsFixtureTypeObj = this.jigsAndFixturesImporter.getJigsFixtureTypes(path);
                if (jigsFixtureTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(jigsFixtureTypeObj.getObjectType(), mesObjectTypeAttribute, jigsFixtureTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }


    private void createOperationTypeObjects(RowData stringListHashMap) {
        this.operationImporter.loadOperationClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESOperationType operationType = this.operationImporter.rootOperationTypesMap.get(typeName);
            if (operationType != null) {
                operationType.setName(typeName);
                operationType.setName(desc != null ? desc : typeName);
                operationTypeRepository.save(operationType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(operationType.getObjectType(), mesObjectTypeAttribute, operationType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESOperationType operationTypeObj = this.operationImporter.getOperationTypes(path);
                    if (operationTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(operationTypeObj.getObjectType(), mesObjectTypeAttribute, operationTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESOperationType operationTypeObj = this.operationImporter.getOperationTypes(path);
                if (operationTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(operationTypeObj.getObjectType(), mesObjectTypeAttribute, operationTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }


    private List<Integer> createProductOrderObjects(Enum objectType, Integer index, String path, String desc, Integer i, String numberSource, RowData stringListHashMap) {
        List<Integer> integers = new ArrayList<>();
//        if (index != -1) {
//            String name = path.substring(index + 1, path.length());
//            String path3 = path.substring(0, index);
//            MESProductionOrderType plantType = this.dbPOTypeMap.get(path3);
//            if (plantType != null && (name != null & name != "")) {
//                List<MESProductionOrderType> plantTypes = productionOrderTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(plantType.getId(), name);
//                if (plantTypes.size() == 0) {
//                    Integer index1 = path3.lastIndexOf("/");
//                    if (index1 != -1) {
//                        MESProductionOrderType parentType = setPoParent(path3, objectType, numberSource);
//                        MESProductionOrderType mesPlantType = createPoType(name, parentType, desc, i, objectType, numberSource);
//                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                        importMesTypeAttributes(i, mesPlantType.getObjectType(), mesObjectTypeAttribute, mesPlantType.getId(), stringListHashMap);
//                        integers.add(mesPlantType.getId());
//                    } else {
//                        MESProductionOrderType parentType = this.dbPOTypeMap.get(path3);
//                        if (parentType != null) {
//                            MESProductionOrderType plantType1 = createPoType(name, parentType, desc, i, objectType, numberSource);
//                            MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                            importMesTypeAttributes(i, plantType1.getObjectType(), mesObjectTypeAttribute, plantType1.getId(), stringListHashMap);
//                            integers.add(plantType1.getId());
//                        } else {
//                            parentType = createPoParentType(path3, objectType);
//                            MESProductionOrderType plmType = this.dbPOTypeMap.get(name);
//                            if (plmType == null) {
//                                plmType = createPoType(name, parentType, desc, i, objectType, numberSource);
//                                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                                importMesTypeAttributes(i, plmType.getObjectType(), mesObjectTypeAttribute, plmType.getId(), stringListHashMap);
//                            }
//                            integers.add(plmType.getId());
//                        }
//                    }
//                } else {
//
//                    MESProductionOrderType plantType1 = plantTypes.get(0);
//                    if (plantType1 != null) {
//                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                        importMesTypeAttributes(i, plantType1.getObjectType(), mesObjectTypeAttribute, plantType1.getId(), stringListHashMap);
//                        integers.add(plantType1.getId());
//                    }
//                }
//
//            }
//
//        }

        return integers;
    }

    private List<Integer> createServiceOrderObjects(Enum objectType, Integer index, String path, String desc, Integer i, String numberSource, RowData stringListHashMap) {
        List<Integer> integers = new ArrayList<>();
//        if (index != -1) {
//            String name = path.substring(index + 1, path.length());
//            String path3 = path.substring(0, index);
//            MESServiceOrderType plantType = this.dbSOTypeMap.get(path3);
//            if (plantType != null && (name != null & name != "")) {
//                List<MESServiceOrderType> plantTypes = serviceOrderTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(plantType.getId(), name);
//                if (plantTypes.size() == 0) {
//                    Integer index1 = path3.lastIndexOf("/");
//                    if (index1 != -1) {
//                        MESServiceOrderType parentType = setSoParent(path3, objectType, numberSource);
//                        MESServiceOrderType mesPlantType = createSoType(name, parentType, desc, i, objectType, numberSource);
//                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                        importMesTypeAttributes(i, mesPlantType.getObjectType(), mesObjectTypeAttribute, mesPlantType.getId(), stringListHashMap);
//                        integers.add(mesPlantType.getId());
//                    } else {
//                        MESServiceOrderType parentType = this.dbSOTypeMap.get(path3);
//                        if (parentType != null) {
//                            MESServiceOrderType plantType1 = createSoType(name, parentType, desc, i, objectType, numberSource);
//                            MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                            importMesTypeAttributes(i, plantType1.getObjectType(), mesObjectTypeAttribute, plantType1.getId(), stringListHashMap);
//                            integers.add(plantType1.getId());
//                        } else {
//                            parentType = createSoParentType(path3, objectType);
//                            MESServiceOrderType plmType = this.dbSOTypeMap.get(name);
//                            if (plmType == null) {
//                                plmType = createSoType(name, parentType, desc, i, objectType, numberSource);
//                                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                                importMesTypeAttributes(i, plmType.getObjectType(), mesObjectTypeAttribute, plmType.getId(), stringListHashMap);
//                            }
//                            integers.add(plmType.getId());
//                        }
//                    }
//                } else {
//
//                    MESServiceOrderType plantType1 = plantTypes.get(0);
//                    if (plantType1 != null) {
//                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
//                        importMesTypeAttributes(i, plantType1.getObjectType(), mesObjectTypeAttribute, plantType1.getId(), stringListHashMap);
//                        integers.add(plantType1.getId());
//                    }
//                }
//
//            }
//
//        }

        return integers;
    }


    private void createManpowerObjects(RowData stringListHashMap) {
        this.manpowersImporter.loadManpowerClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESManpowerType manpowerType = this.manpowersImporter.rootManpowerTypesMap.get(typeName);
            if (manpowerType != null) {
                manpowerType.setName(typeName);
                manpowerType.setName(desc != null ? desc : typeName);
                manpowerTypeRepository.save(manpowerType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(manpowerType.getObjectType(), mesObjectTypeAttribute, manpowerType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESManpowerType manpowerTypeObj = this.manpowersImporter.getManpowerTypes(path);
                    if (manpowerTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(manpowerTypeObj.getObjectType(), mesObjectTypeAttribute, manpowerTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESManpowerType manpowerTypeObj = this.manpowersImporter.getManpowerTypes(path);
                if (manpowerTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(manpowerTypeObj.getObjectType(), mesObjectTypeAttribute, manpowerTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }

    }


    private void createEquipmentObjects(RowData stringListHashMap) {
        this.equipmentImporter.loadEquipmentClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESEquipmentType equipmentType = this.equipmentImporter.rootEquipmentTypesMap.get(typeName);
            if (equipmentType != null) {
                equipmentType.setName(typeName);
                equipmentType.setName(desc != null ? desc : typeName);
                equipmentTypeRepository.save(equipmentType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(equipmentType.getObjectType(), mesObjectTypeAttribute, equipmentType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESEquipmentType equipmentTypeObj = this.equipmentImporter.getEquipmentTypes(path);
                    if (equipmentTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(equipmentTypeObj.getObjectType(), mesObjectTypeAttribute, equipmentTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESEquipmentType equipmentTypeObj = this.equipmentImporter.getEquipmentTypes(path);
                if (equipmentTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(equipmentTypeObj.getObjectType(), mesObjectTypeAttribute, equipmentTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }

    private void createInstrumentObjects(RowData stringListHashMap) {
        this.instrumentImporter.loadInstrumentClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
        if (typeName != null && typeName != "") {
            MESInstrumentType instrumentType = this.instrumentImporter.rootInstrumentTypesMap.get(typeName);
            if (instrumentType != null) {
                instrumentType.setName(typeName);
                instrumentType.setName(desc != null ? desc : typeName);
                instrumentTypeRepository.save(instrumentType);
                MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                importMesTypeAttributes(instrumentType.getObjectType(), mesObjectTypeAttribute, instrumentType.getId(), stringListHashMap);

            } else {

                if (path != null && path != "") {
                    MESInstrumentType instrumentTypeObj = this.instrumentImporter.getInstrumentTypes(path);
                    if (instrumentTypeObj != null) {
                        MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                        importMesTypeAttributes(instrumentTypeObj.getObjectType(), mesObjectTypeAttribute, instrumentTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else {
            if (path != null && path != "") {
                MESInstrumentType instrumentTypeObj = this.instrumentImporter.getInstrumentTypes(path);
                if (instrumentTypeObj != null) {
                    MESObjectTypeAttribute mesObjectTypeAttribute = new MESObjectTypeAttribute();
                    importMesTypeAttributes(instrumentTypeObj.getObjectType(), mesObjectTypeAttribute, instrumentTypeObj.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

            }
        }
    }

    /*
    * Plant
    * */
    private MESPlantType setPlantParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESPlantType manufacturingType = this.dbPlantTypeMap.get(parent);
            if (manufacturingType == null) {
                MESPlantType parentType = this.dbPlantTypeMap.get(path3);
                if (parentType != null) {
                    MESPlantType plmPlantType = createPlantType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setPlantParent(path3, objectType, numberSource);
                    MESPlantType plantType = createPlantType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESPlantType parentType = this.dbPlantTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createPlantParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESPlantType createPlantType(String name, MESPlantType parent, String desc, int i, Enum objectType, String numberSource) {
        MESPlantType mesPlantType = new MESPlantType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESPlantType plantType = plantTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESPlantType createPlantParentType(String name, Enum objectType) {
        MESPlantType parent = null;
        if (name != null) {
            parent = this.dbPlantTypeMap.get(name);
        }

        MESPlantType mesPlantType = new MESPlantType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = plantTypeRepository.save(mesPlantType);
            this.dbPlantTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }

    /*
   * Machine Type
   * */
    private MESMachineType setMachineParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESMachineType manufacturingType = this.dbMCTypeMap.get(parent);
            if (manufacturingType == null) {
                MESMachineType parentType = this.dbMCTypeMap.get(path3);
                if (parentType != null) {
                    MESMachineType plmPlantType = createMachineType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setMachineParent(path3, objectType, numberSource);
                    MESMachineType plantType = createMachineType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESMachineType parentType = this.dbMCTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMachineParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESMachineType createMachineType(String name, MESMachineType parent, String desc, int i, Enum objectType, String numberSource) {
        MESMachineType mesPlantType = new MESMachineType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESMachineType plantType = machineTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESMachineType createMachineParentType(String name, Enum objectType) {
        MESMachineType parent = null;
        if (name != null) {
            parent = this.dbMCTypeMap.get(name);
        }

        MESMachineType mesPlantType = new MESMachineType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = machineTypeRepository.save(mesPlantType);
            this.dbMCTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


    /*
 * Assembly Line Type
 * */
    private MESAssemblyLineType setALParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESAssemblyLineType manufacturingType = this.dbALTypeMap.get(parent);
            if (manufacturingType == null) {
                MESAssemblyLineType parentType = this.dbALTypeMap.get(path3);
                if (parentType != null) {
                    MESAssemblyLineType plmPlantType = createALType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setALParent(path3, objectType, numberSource);
                    MESAssemblyLineType plantType = createALType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESAssemblyLineType parentType = this.dbALTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createALParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESAssemblyLineType createALType(String name, MESAssemblyLineType parent, String desc, int i, Enum objectType, String numberSource) {
        MESAssemblyLineType mesPlantType = new MESAssemblyLineType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESAssemblyLineType plantType = assemblyLineTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESAssemblyLineType createALParentType(String name, Enum objectType) {
        MESAssemblyLineType parent = null;
        if (name != null) {
            parent = this.dbALTypeMap.get(name);
        }

        MESAssemblyLineType mesPlantType = new MESAssemblyLineType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = assemblyLineTypeRepository.save(mesPlantType);
            this.dbALTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


    /*
* Material Type
* */
    private MESMaterialType setMaterialParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESMaterialType manufacturingType = this.dbMTTypeMap.get(parent);
            if (manufacturingType == null) {
                MESMaterialType parentType = this.dbMTTypeMap.get(path3);
                if (parentType != null) {
                    MESMaterialType plmPlantType = createMaterialType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setMaterialParent(path3, objectType, numberSource);
                    MESMaterialType plantType = createMaterialType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESMaterialType parentType = this.dbMTTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMaterialParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESMaterialType createMaterialType(String name, MESMaterialType parent, String desc, int i, Enum objectType, String numberSource) {
        MESMaterialType mesPlantType = new MESMaterialType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESMaterialType plantType = materialTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESMaterialType createMaterialParentType(String name, Enum objectType) {
        MESMaterialType parent = null;
        if (name != null) {
            parent = this.dbMTTypeMap.get(name);
        }

        MESMaterialType mesPlantType = new MESMaterialType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = materialTypeRepository.save(mesPlantType);
            this.dbMTTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }

    /*
    * Tool Type
    * */

    private MESToolType setToolParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESToolType manufacturingType = this.dbToolTypeMap.get(parent);
            if (manufacturingType == null) {
                MESToolType parentType = this.dbToolTypeMap.get(path3);
                if (parentType != null) {
                    MESToolType plmPlantType = createToolType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setToolParent(path3, objectType, numberSource);
                    MESToolType plantType = createToolType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESToolType parentType = this.dbToolTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createToolParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESToolType createToolType(String name, MESToolType parent, String desc, int i, Enum objectType, String numberSource) {
        MESToolType mesPlantType = new MESToolType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESToolType plantType = toolTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESToolType createToolParentType(String name, Enum objectType) {
        MESToolType parent = null;
        if (name != null) {
            parent = this.dbToolTypeMap.get(name);
        }

        MESToolType mesPlantType = new MESToolType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            this.dbToolTypeMap.put(mesPlantType.getName(), mesPlantType);
            mesPlantType = toolTypeRepository.save(mesPlantType);
        }
        return mesPlantType;

    }


    /*
    * Workcenter Type
    * */

    private MESWorkCenterType setWorkCenterParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESWorkCenterType manufacturingType = this.dbWCTypeMap.get(parent);
            if (manufacturingType == null) {
                MESWorkCenterType parentType = this.dbWCTypeMap.get(path3);
                if (parentType != null) {
                    MESWorkCenterType plmPlantType = createWorkCenterType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setWorkCenterParent(path3, objectType, numberSource);
                    MESWorkCenterType plantType = createWorkCenterType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESWorkCenterType parentType = this.dbWCTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createWorkCenterParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESWorkCenterType createWorkCenterType(String name, MESWorkCenterType parent, String desc, int i, Enum objectType, String numberSource) {
        MESWorkCenterType mesPlantType = new MESWorkCenterType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESWorkCenterType plantType = workCenterTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESWorkCenterType createWorkCenterParentType(String name, Enum objectType) {
        MESWorkCenterType parent = null;
        if (name != null) {
            parent = this.dbWCTypeMap.get(name);
        }

        MESWorkCenterType mesPlantType = new MESWorkCenterType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = workCenterTypeRepository.save(mesPlantType);
            this.dbWCTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }

     /*
    * JigsFixture Type
    * */

    private MESJigsFixtureType setJigsParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESJigsFixtureType manufacturingType = this.dbJFTypeMap.get(parent);
            if (manufacturingType == null) {
                MESJigsFixtureType parentType = this.dbJFTypeMap.get(path3);
                if (parentType != null) {
                    MESJigsFixtureType plmPlantType = createJigsType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setJigsParent(path3, objectType, numberSource);
                    MESJigsFixtureType plantType = createJigsType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESJigsFixtureType parentType = this.dbJFTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createJigsParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESJigsFixtureType createJigsType(String name, MESJigsFixtureType parent, String desc, int i, Enum objectType, String numberSource) {
        MESJigsFixtureType mesPlantType = new MESJigsFixtureType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESJigsFixtureType plantType = jigsFixtureTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESJigsFixtureType createJigsParentType(String name, Enum objectType) {
        MESJigsFixtureType parent = null;
        if (name != null) {
            parent = this.dbJFTypeMap.get(name);
        }

        MESJigsFixtureType mesPlantType = new MESJigsFixtureType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = jigsFixtureTypeRepository.save(mesPlantType);
            this.dbJFTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


     /*
    * Operation Type
    * */

    private MESOperationType setOpParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESOperationType manufacturingType = this.dbOPTypeMap.get(parent);
            if (manufacturingType == null) {
                MESOperationType parentType = this.dbOPTypeMap.get(path3);
                if (parentType != null) {
                    MESOperationType plmPlantType = createOpType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setOpParent(path3, objectType, numberSource);
                    MESOperationType plantType = createOpType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESOperationType parentType = this.dbOPTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createOpParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESOperationType createOpType(String name, MESOperationType parent, String desc, int i, Enum objectType, String numberSource) {
        MESOperationType mesPlantType = new MESOperationType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESOperationType plantType = operationTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESOperationType createOpParentType(String name, Enum objectType) {
        MESOperationType parent = null;
        if (name != null) {
            parent = this.dbOPTypeMap.get(name);
        }

        MESOperationType mesPlantType = new MESOperationType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = operationTypeRepository.save(mesPlantType);
            this.dbOPTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


     /*
    * Production Order Type
    * */

    private MESProductionOrderType setPoParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESProductionOrderType manufacturingType = this.dbPOTypeMap.get(parent);
            if (manufacturingType == null) {
                MESProductionOrderType parentType = this.dbPOTypeMap.get(path3);
                if (parentType != null) {
                    MESProductionOrderType plmPlantType = createPoType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setPoParent(path3, objectType, numberSource);
                    MESProductionOrderType plantType = createPoType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESProductionOrderType parentType = this.dbPOTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createPoParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESProductionOrderType createPoType(String name, MESProductionOrderType parent, String desc, int i, Enum objectType, String numberSource) {
        MESProductionOrderType mesPlantType = new MESProductionOrderType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESProductionOrderType plantType = productionOrderTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESProductionOrderType createPoParentType(String name, Enum objectType) {
        MESProductionOrderType parent = null;
        if (name != null) {
            parent = this.dbPOTypeMap.get(name);
        }

        MESProductionOrderType mesPlantType = new MESProductionOrderType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = productionOrderTypeRepository.save(mesPlantType);
            this.dbPOTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }



     /*
    * Service Order Type
    * */

    private MESServiceOrderType setSoParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESServiceOrderType manufacturingType = this.dbSOTypeMap.get(parent);
            if (manufacturingType == null) {
                MESServiceOrderType parentType = this.dbSOTypeMap.get(path3);
                if (parentType != null) {
                    MESServiceOrderType plmPlantType = createSoType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setSoParent(path3, objectType, numberSource);
                    MESServiceOrderType plantType = createSoType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESServiceOrderType parentType = this.dbSOTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createSoParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESServiceOrderType createSoType(String name, MESServiceOrderType parent, String desc, int i, Enum objectType, String numberSource) {
        MESServiceOrderType mesPlantType = new MESServiceOrderType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESServiceOrderType plantType = serviceOrderTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESServiceOrderType createSoParentType(String name, Enum objectType) {
        MESServiceOrderType parent = null;
        if (name != null) {
            parent = this.dbSOTypeMap.get(name);
        }

        MESServiceOrderType mesPlantType = new MESServiceOrderType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = serviceOrderTypeRepository.save(mesPlantType);
            this.dbSOTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }

     /*
    * Service Order Type
    * */

    private MESManpowerType setMpParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESManpowerType manufacturingType = this.dbMPTypeMap.get(parent);
            if (manufacturingType == null) {
                MESManpowerType parentType = this.dbMPTypeMap.get(path3);
                if (parentType != null) {
                    MESManpowerType plmPlantType = createMpType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setMpParent(path3, objectType, numberSource);
                    MESManpowerType plantType = createMpType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESManpowerType parentType = this.dbMPTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMpParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESManpowerType createMpType(String name, MESManpowerType parent, String desc, int i, Enum objectType, String numberSource) {
        MESManpowerType mesPlantType = new MESManpowerType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESManpowerType plantType = manpowerTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESManpowerType createMpParentType(String name, Enum objectType) {
        MESManpowerType parent = null;
        if (name != null) {
            parent = this.dbMPTypeMap.get(name);
        }
        MESManpowerType mesPlantType = new MESManpowerType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = manpowerTypeRepository.save(mesPlantType);
            this.dbMPTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


      /*
    * Equipment Type
    * */

    private MESEquipmentType setEquipmentParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESEquipmentType manufacturingType = this.dbEQTypeMap.get(parent);
            if (manufacturingType == null) {
                MESEquipmentType parentType = this.dbEQTypeMap.get(path3);
                if (parentType != null) {
                    MESEquipmentType plmPlantType = createEquipmentType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setEquipmentParent(path3, objectType, numberSource);
                    MESEquipmentType plantType = createEquipmentType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESEquipmentType parentType = this.dbEQTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createEquipmentParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESEquipmentType createEquipmentType(String name, MESEquipmentType parent, String desc, int i, Enum objectType, String numberSource) {
        MESEquipmentType mesPlantType = new MESEquipmentType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESEquipmentType plantType = equipmentTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESEquipmentType createEquipmentParentType(String name, Enum objectType) {
        MESEquipmentType parent = null;
        if (name != null) {
            parent = this.dbEQTypeMap.get(name);
        }
        MESEquipmentType mesPlantType = new MESEquipmentType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = equipmentTypeRepository.save(mesPlantType);
            this.dbEQTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


     /*
    * Instrument Type
    * */

    private MESInstrumentType setInstrumentParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MESInstrumentType manufacturingType = this.dbIMTypeMap.get(parent);
            if (manufacturingType == null) {
                MESInstrumentType parentType = this.dbIMTypeMap.get(path3);
                if (parentType != null) {
                    MESInstrumentType plmPlantType = createInstrumentType(parent, parentType, null, 0, objectType, numberSource);
                    return plmPlantType;
                } else {
                    parentType = setInstrumentParent(path3, objectType, numberSource);
                    MESInstrumentType plantType = createInstrumentType(parent, parentType, null, 0, objectType, numberSource);
                    return plantType;
                }
            } else {
                return manufacturingType;
            }

        } else {
            MESInstrumentType parentType = this.dbIMTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createInstrumentParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MESInstrumentType createInstrumentType(String name, MESInstrumentType parent, String desc, int i, Enum objectType, String numberSource) {
        MESInstrumentType mesPlantType = new MESInstrumentType();
        mesPlantType.setName(name);
        mesPlantType.setDescription(desc);
        mesPlantType.setObjectType(objectType);
        mesPlantType.setAutoNumberSource(this.autoWireKeyMap.get("Default " + numberSource + " Number Source"));
        if (parent != null)
            mesPlantType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturing_type_for_row_number" + (i + 1),
                    null, "Please provide Manufacturing type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MESInstrumentType plantType = instrumentTypeRepository.save(mesPlantType);
        return plantType;
    }

    private MESInstrumentType createInstrumentParentType(String name, Enum objectType) {
        MESInstrumentType parent = null;
        if (name != null) {
            parent = this.dbIMTypeMap.get(name);
        }
        MESInstrumentType mesPlantType = new MESInstrumentType();
        mesPlantType.setName(name);
        mesPlantType.setObjectType(parent.getObjectType());
        mesPlantType.setAutoNumberSource(parent.getAutoNumberSource());
        mesPlantType.setParentType(parent.getId());
        if (mesPlantType.getId() == null) {
            mesPlantType = instrumentTypeRepository.save(mesPlantType);
            this.dbIMTypeMap.put(mesPlantType.getName(), mesPlantType);
        }
        return mesPlantType;

    }


    private Boolean checkMesTypeAttributeName(Enum objectType, Integer qualityType, String name) {
        Boolean isExist = false;
        if (objectType.toString().equals("PLANTTYPE")) {
            MESPlantType mesPlantType = plantTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (mesPlantType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(mesPlantType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("MACHINETYPE")) {
            MESMachineType machineType = machineTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("ASSEMBLYLINETYPE")) {
            MESAssemblyLineType machineType = assemblyLineTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("MATERIALTYPE")) {
            MESMaterialType machineType = materialTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("TOOLTYPE")) {
            MESToolType machineType = toolTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("WORKCENTERTYPE")) {
            MESWorkCenterType machineType = workCenterTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("JIGFIXTURETYPE")) {
            MESJigsFixtureType machineType = jigsFixtureTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("OPERATIONTYPE")) {
            MESOperationType machineType = operationTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("PRODUCTIONORDERTYPE")) {
            MESProductionOrderType machineType = productionOrderTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("SERVICEORDERTYPE")) {
            MESServiceOrderType machineType = serviceOrderTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("MANPOWERTYPE")) {
            MESManpowerType machineType = manpowerTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("EQUIPMENTTYPE")) {
            MESEquipmentType machineType = equipmentTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("INSTRUMENTTYPE")) {
            MESInstrumentType machineType = instrumentTypeRepository.findOne(qualityType);
            MESObjectTypeAttribute mesObjectTypeAttribute = null;
            if (machineType != null) {
                mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findByTypeAndName(machineType.getId(), name);
                if (mesObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        }


        return isExist;
    }


    public void importMesTypeAttributes(Enum objectType, MESObjectTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            if (name != null && name != "") {
                isExist = checkMesTypeAttributeName(objectType, objectId, name);
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
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
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
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    mesObjectTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


}