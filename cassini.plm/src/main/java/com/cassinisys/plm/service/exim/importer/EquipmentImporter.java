package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.MESEquipment;
import com.cassinisys.plm.model.mes.MESEquipmentType;
import com.cassinisys.plm.model.mes.MESManufacturerData;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.EquipmentTypeRepository;
import com.cassinisys.plm.repo.mes.MESEquipmentRepository;
import com.cassinisys.plm.repo.mes.MESWorkCenterRepository;
import com.cassinisys.plm.repo.mes.ManufacturerDataRepository;
import com.cassinisys.plm.repo.mro.MROAssetMeterRepository;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.repo.mro.MROAssetTypeRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
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
 * Created by Lenovo on 11-11-2021.
 */
@Service
@Scope("prototype")
public class EquipmentImporter {

    @Autowired
    private MESEquipmentRepository equipmentRepository;
    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROAssetMeterRepository assetMeterRepository;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private PlantsImporter plantsImporter;
    private static AutoNumber autoNumber;

    public static ConcurrentMap<String, MESEquipmentType> rootEquipmentTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESEquipmentType> equipmentTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();
    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Equipment Number Source");
    }

    public void importEquipments(TableData tableData) throws ParseException {

        this.loadEquipmentClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESEquipment> dbEquipmentMap = new LinkedHashMap();
        Map<String, MESEquipmentType> dbEquipmentTypeMap = new LinkedHashMap();
        List<MESEquipment> equipments = equipmentRepository.findAll();
        List<MESEquipmentType> equipmentTypes = equipmentTypeRepository.findAll();
        dbEquipmentMap = equipments.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        for (MESEquipmentType assetType : equipmentTypes) {
            if (!dbEquipmentTypeMap.containsKey(assetType.getName())) dbEquipmentTypeMap.put(assetType.getName(), assetType);
        }
        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        for (MROAssetType assetType : assetTypes) {
            if (!dbAssetTypeMap.containsKey(assetType.getName())) dbAssetTypeMap.put(assetType.getName(), assetType);
        }
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESEquipment> equipments1 = createEquipments(tableData, dbEquipmentMap, dbEquipmentTypeMap);

        if (equipments1.size() > 0) {
            List<MESManufacturerDataDTO> manufacturersDtoList = new ArrayList<>();
            for (MESEquipment equipment : equipments1) {
                MESManufacturerDataDTO manufacturerDto = new MESManufacturerDataDTO();
                manufacturerDto.setId(equipment.getId());
                manufacturerDto.setName(equipment.getName());
                manufacturerDto.setNumber(equipment.getId());
                manufacturersDtoList.add(manufacturerDto);
            }
            createMESManufacturer(tableData, manufacturersDtoList);
        }

        if (equipments1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESEquipment equipment : equipments1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(equipment.getId());
                resourcesDto.setName(equipment.getName());
                resourcesDto.setNumber(equipment.getId());
                resourcesDto.setType(MESType.EQUIPMENTTYPE);
                resourcesDtoList.add(resourcesDto);
            }
            List<MROAsset> mroAsset = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }
    }

    public void createMESManufacturer(TableData tableData, List<MESManufacturerDataDTO> mesManufacturerDataDTOs) throws ParseException {
        List<MESManufacturerData> mesManufacturerData = new ArrayList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            String mfrName = stringListHashMap.get("MFR. Name");
            String mfrDescription = stringListHashMap.get("MFR. Description");
            String mfrModelNumber = stringListHashMap.get("MFR. Model Number".trim());
            String mfrPartNumber = stringListHashMap.get("MFR. Part Number".trim());
            String mfrSerialNumber = stringListHashMap.get("MFR. Serial Number".trim());
            String mfrLotNumber = stringListHashMap.get("MFR. Lot Number".trim());
            String mfrDate = stringListHashMap.get("MFR. Date".trim());
            MESManufacturerDataDTO manufacturerObject = mesManufacturerDataDTOs.get(i);
            MESManufacturerData manufacturerData = new MESManufacturerData();
            manufacturerData.setObject(manufacturerObject.getId());
            manufacturerData.setMfrName(mfrName);
            manufacturerData.setMfrDescription(mfrDescription);
            manufacturerData.setMfrModelNumber(mfrModelNumber);
            manufacturerData.setMfrPartNumber(mfrPartNumber);
            manufacturerData.setMfrSerialNumber(mfrSerialNumber);
            manufacturerData.setMfrLotNumber(mfrLotNumber);
            if (mfrDate != null && mfrDate != "") {
                manufacturerData.setMfrDate(importer.parseDate(mfrDate, "dd/MM/yyyy"));
            }
            mesManufacturerData.add(manufacturerData);
            i++;
        }
        if (mesManufacturerData.size() > 0) {
            manufacturerDataRepository.save(mesManufacturerData);
        }
    }



    private List<MESEquipment> createEquipments(TableData tableData, Map<String, MESEquipment> dbEquipmentsMap, Map<String, MESEquipmentType> dbEquipmentTypesMap) {
        List<MESEquipment> equipments2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Equipment Name");
            if (name != null && !name.trim().equals("")) {
                String equipmentTypeName = stringListHashMap.get("Equipment Type");
                String number = stringListHashMap.get("Equipment Number");
                String typePath = stringListHashMap.get("Type Path");
                if (equipmentTypeName != null && equipmentTypeName != "") {
                    MESEquipmentType equipmentType = dbEquipmentTypesMap.get(equipmentTypeName);
                    if (equipmentType != null) {
                        MESEquipment equipment = dbEquipmentsMap.get(number);
                        if (equipment == null) {
                            equipments2.add(createEquipment(i, number, equipmentType, stringListHashMap, dbEquipmentsMap));
                        } else {
                            updateEquipment(i, equipment, stringListHashMap);
                            equipments2.add(equipment);

                        }

                    } else {
                        equipments2.add(createEquipmentByPath(i, number, typePath, dbEquipmentsMap, dbEquipmentTypesMap, stringListHashMap));
                    }
                } else {
                    equipments2.add(createEquipmentByPath(i, number, typePath, dbEquipmentsMap, dbEquipmentTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Equipment Name for row :" + i);
            }
        }

        if (equipments2.size() > 0) {
            equipmentRepository.save(equipments2);
        }

        return equipments2;
    }

    private MESEquipment createEquipmentByPath(int i, String number, String typePath, Map<String, MESEquipment> dbEquipmentsMap,
                                                     Map<String, MESEquipmentType> dbEquipmentTypesMap, RowData stringListHashMap) {
        MESEquipment Equipment = null;
        if (typePath != null && typePath != "") {
            MESEquipmentType MESEquipmentType = this.getEquipmentTypes(typePath);
            if (MESEquipmentType != null) {
                dbEquipmentTypesMap.put(MESEquipmentType.getName(), MESEquipmentType);
                Equipment = dbEquipmentsMap.get(number);
                if (Equipment == null)
                    Equipment = createEquipment(i, number, MESEquipmentType, stringListHashMap, dbEquipmentsMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Equipment Type or Equipment Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Equipment Type or Equipment Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Equipment;
    }



    private MESEquipment createEquipment(Integer i, String number, MESEquipmentType equipmentType, RowData stringListHashMap, Map<String, MESEquipment> dbEquipmentsMap) {
        MESEquipment equipment = new MESEquipment();
        String equipmentNumber = stringListHashMap.get("Equipment Number".trim());
        String equipmentName = stringListHashMap.get("Equipment Name".trim());
        String equipmentDescription = stringListHashMap.get("Equipment Description".trim());
        String requiresMaintance = stringListHashMap.get("Requires Maintenance".trim());
        if (equipmentNumber == null || equipmentNumber.trim().equals("")) {
            equipmentNumber = importer.getNextNumberWithoutUpdate(equipmentType.getAutoNumberSource().getName(), autoNumberMap);
        }
        equipment.setType(equipmentType);
        equipment.setNumber(equipmentNumber);
        equipment.setName(equipmentName);
        equipment.setDescription(equipmentDescription);
        importer.saveNextNumber(equipmentType.getAutoNumberSource().getName(), equipment.getNumber(), autoNumberMap);

        if (requiresMaintance.equals("No")) {
            equipment.setRequiresMaintenance(false);
        }

        dbEquipmentsMap.put(equipment.getNumber(), equipment);

        return equipment;
    }


    public void loadEquipmentClassificationTree() {
        getDefaultNumberSource();
        equipmentTypesMapByPath = new ConcurrentHashMap<>();
        List<MESEquipmentType> rootTypes = mesObjectTypeService.getEquipmentTypeTree();
        for (MESEquipmentType rootType : rootTypes) {
            rootEquipmentTypesMap.put(rootType.getName(), rootType);
            equipmentTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESEquipmentType getEquipmentTypes(String path) {
        MESEquipmentType mesEquipmentType = equipmentTypesMapByPath.get(path);
        if (mesEquipmentType == null) {
            mesEquipmentType = getEquipmentTypeByPath(path);
            if (mesEquipmentType == null) {
                mesEquipmentType = createEquipmentTypeByPath(null, path);
            }
            equipmentTypesMapByPath.put(path, mesEquipmentType);
        }

        return mesEquipmentType;
    }


    private MESEquipmentType getEquipmentTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESEquipmentType itemType = rootEquipmentTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootEquipmentTypesMap.get(name);
        }
    }


    private MESEquipmentType createEquipmentTypeByPath(MESEquipmentType parentType, String path) {
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
            parentType = rootEquipmentTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESEquipmentType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = equipmentTypeRepository.save(parentType);
                rootEquipmentTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createEquipmentTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESEquipmentType childItemType = new MESEquipmentType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = equipmentTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createEquipmentTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createEquipmentTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESEquipment updateEquipment(Integer i, MESEquipment mesEquipment, RowData stringListHashMap) {
        String equipmentName = stringListHashMap.get("Equipment Name".trim());
        String equipmentDescription = stringListHashMap.get("Equipment Description".trim());

        mesEquipment.setName(equipmentName);
        mesEquipment.setDescription(equipmentDescription);

        return mesEquipment;
    }


}
