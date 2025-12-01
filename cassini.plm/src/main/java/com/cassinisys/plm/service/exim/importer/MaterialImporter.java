package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.plm.model.mes.MESMaterial;
import com.cassinisys.plm.model.mes.MESMaterialType;
import com.cassinisys.plm.repo.mes.MESMaterialRepository;
import com.cassinisys.plm.repo.mes.MaterialTypeRepository;
import com.cassinisys.plm.service.classification.MESObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class MaterialImporter {

    @Autowired
    private MESMaterialRepository materialRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;

    public static ConcurrentMap<String, MESMaterialType> rootMaterialTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESMaterialType> materialTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private static AutoNumber autoNumber;

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Material Number Source");
    }


    public void importMaterials(TableData tableData) throws ParseException {

        this.loadMaterialClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESMaterial> dbMaterialMap = new LinkedHashMap();
        Map<String, MESMaterialType> dbMaterialTypeMap = new LinkedHashMap();
        List<MESMaterial> materials = materialRepository.findAll();
        List<MESMaterialType> materialTypes = materialTypeRepository.findAll();

        Map<String, Measurement> dbMeasurementMap = new LinkedHashMap();
        Map<String, MeasurementUnit> dbMeasurementUnits = new LinkedHashMap();
        List<Measurement> measurements = measurementRepository.findAll();
        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findAll();
        dbMeasurementMap = measurements.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        // measurementUnits = measurementUnits.stream().collect(Collectors.toMap(x -> x.getMeasurement(), x -> x));

        dbMaterialMap = materials.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbMaterialTypeMap = materialTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESMaterial> materials1 = createMaterials(tableData, dbMaterialMap, dbMaterialTypeMap, dbMeasurementMap);
    }


    private List<MESMaterial> createMaterials(TableData tableData, Map<String, MESMaterial> dbMaterialsMap, Map<String, MESMaterialType> dbMaterialTypesMap,Map<String, Measurement> dbMeasurementMap) throws ParseException {
        List<MESMaterial> materials2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Material Name");
            if (name != null && !name.trim().equals("")) {
                String materialTypeName = stringListHashMap.get("Material Type".trim());
                String materialTypePath = stringListHashMap.get("Type Path".trim());
                String number = stringListHashMap.get("Material Number");
                if ((materialTypeName == null || materialTypeName == "")) {
                    MESMaterialType materialType = dbMaterialTypesMap.get(materialTypeName);
                    if (materialType != null) {
                        MESMaterial material = dbMaterialsMap.get(number);
                        if (material == null) {
                            materials2.add(createMaterial(i, number, materialType, stringListHashMap, dbMaterialsMap, dbMeasurementMap));
                        } else {
                            updateMaterial(i, material, stringListHashMap);
                            materials2.add(material);
                        }
                    } else {
                        materials2.add(createMaterialByPath(i, number, materialTypePath, dbMaterialsMap, dbMaterialTypesMap, stringListHashMap,dbMeasurementMap));
                    }
                } else {
                    materials2.add(createMaterialByPath(i, number, materialTypePath, dbMaterialsMap, dbMaterialTypesMap, stringListHashMap,dbMeasurementMap));
                }
            } else {
                throw new CassiniException("Please provide Material Name for row :" + i);
            }
        }
        if (materials2.size() > 0) {
            materialRepository.save(materials2);
        }
        return materials2;
    }


    private MESMaterial createMaterialByPath(int i, String number, String materialTypePath, Map<String, MESMaterial> dbMaterialsMap, Map<String, MESMaterialType> dbMaterialTypesMap, RowData stringListHashMap,Map<String, Measurement> dbMeasurementMap) {
        MESMaterial Material = null;
        if (materialTypePath != null && materialTypePath != "") {
            MESMaterialType materialType = this.getMaterialTypes(materialTypePath);
            if (materialType != null) {
                dbMaterialTypesMap.put(materialType.getName(), materialType);
                Material = dbMaterialsMap.get(number);
                if (Material == null)
                    Material = createMaterial(i, number, materialType, stringListHashMap, dbMaterialsMap, dbMeasurementMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Material Type or Material Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Material Type or Material Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Material;
    }



    private MESMaterial createMaterial(Integer i, String name, MESMaterialType materialType, RowData stringListHashMap, Map<String, MESMaterial> dbMaterialsMap, Map<String, Measurement> dbMeasurementMap) {
        MESMaterial material = new MESMaterial();
        String materialNumber = stringListHashMap.get("Material Number".trim());
        String materialName = stringListHashMap.get("Material Name".trim());
        String materialDescription = stringListHashMap.get("Material Description".trim());
        String qom = stringListHashMap.get("QOM".trim());
        String uom = stringListHashMap.get("UOM".trim());
        if (materialNumber == null || materialNumber.trim().equals("")) {
            materialNumber = importer.getNextNumberWithoutUpdate(materialType.getAutoNumberSource().getName(), autoNumberMap);
        }
        material.setType(materialType);
        material.setNumber(materialNumber);
        material.setName(materialName);
        material.setDescription(materialDescription);
        importer.saveNextNumber(materialType.getAutoNumberSource().getName(), material.getNumber(), autoNumberMap);

        Measurement measurement = dbMeasurementMap.get(qom);
        MeasurementUnit measurementUnit = measurementUnitRepository.findByMeasurementAndName(measurement.getId(), uom);

        material.setQom(measurement.getId());
        material.setUom(measurementUnit.getId());

//        material = materialRepository.save(material);

        dbMaterialsMap.put(material.getNumber(), material);

        return material;
    }


    public void loadMaterialClassificationTree() {
        getDefaultNumberSource();
        materialTypesMapByPath = new ConcurrentHashMap<>();
        List<MESMaterialType> rootTypes = mesObjectTypeService.getMaterialTypeTree();
        for (MESMaterialType rootType : rootTypes) {
            rootMaterialTypesMap.put(rootType.getName(), rootType);
            materialTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESMaterialType getMaterialTypes(String path) {
        MESMaterialType mesMaterialType = materialTypesMapByPath.get(path);
        if (mesMaterialType == null) {
            mesMaterialType = getMaterialTypeByPath(path);
            if (mesMaterialType == null) {
                mesMaterialType = createMaterialTypeByPath(null, path);
            }
            materialTypesMapByPath.put(path, mesMaterialType);
        }

        return mesMaterialType;
    }

    private MESMaterialType getMaterialTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESMaterialType itemType = rootMaterialTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootMaterialTypesMap.get(name);
        }
    }


    private MESMaterialType createMaterialTypeByPath(MESMaterialType parentType, String path) {
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
            parentType = rootMaterialTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESMaterialType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = materialTypeRepository.save(parentType);
                rootMaterialTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMaterialTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESMaterialType childItemType = new MESMaterialType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = materialTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createMaterialTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMaterialTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESMaterial updateMaterial(Integer i, MESMaterial mesMaterial, RowData stringListHashMap) {
        String materialName = stringListHashMap.get("Material Name".trim());
        String materialDescription = stringListHashMap.get("Material Description".trim());

        mesMaterial.setName(materialName);
        mesMaterial.setDescription(materialDescription);

        return mesMaterial;
    }


}
