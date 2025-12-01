package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.MESMachine;
import com.cassinisys.plm.model.mes.MESMachineType;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mes.MESWorkCenter;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.MESMachineRepository;
import com.cassinisys.plm.repo.mes.MESWorkCenterRepository;
import com.cassinisys.plm.repo.mes.MachineTypeRepository;
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
public class MachineImporter {

    @Autowired
    private MESMachineRepository machineRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
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
    @Autowired
    private EquipmentImporter equipmentImporter;

    public static ConcurrentMap<String, MESMachineType> rootMachineTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESMachineType> machineTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private static AutoNumber autoNumber;

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Machine Number Source");
    }

    Map<String, MESWorkCenter> dbWorkCentersMap = new LinkedHashMap();

    public void importMachines(TableData tableData) throws ParseException {
        dbWorkCentersMap = new LinkedHashMap();
        this.loadMachineClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESMachine> dbMachineMap = new LinkedHashMap();
        Map<String, MESMachineType> dbMachineTypeMap = new LinkedHashMap();
        List<MESMachine> machines = machineRepository.findAll();
        List<MESMachineType> machineTypes = machineTypeRepository.findAll();
        dbMachineMap = machines.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        for (MESMachineType assetType : machineTypes) {
            if (!dbMachineTypeMap.containsKey(assetType.getName()))
                dbMachineTypeMap.put(assetType.getName(), assetType);
        }
        dbMachineTypeMap = machineTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));

        List<MESWorkCenter> workCenters = workCenterRepository.findAll();
        dbWorkCentersMap = workCenters.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));

        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();

        for (MROAssetType assetType : assetTypes) {
            if (!dbAssetTypeMap.containsKey(assetType.getName())) dbAssetTypeMap.put(assetType.getName(), assetType);
        }
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESMachine> machines1 = createMachines(tableData, dbMachineMap, dbMachineTypeMap);
        if (machines1.size() > 0) {
            List<MESManufacturerDataDTO> manufacturersDtoList = new ArrayList<>();
            for (MESMachine machine : machines1) {
                MESManufacturerDataDTO manufacturerDto = new MESManufacturerDataDTO();
                manufacturerDto.setId(machine.getId());
                manufacturerDto.setName(machine.getName());
                manufacturerDto.setNumber(machine.getId());
                manufacturersDtoList.add(manufacturerDto);
            }
            equipmentImporter.createMESManufacturer(tableData, manufacturersDtoList);
        }

        if (machines1.size() > 0) {

            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESMachine machine : machines1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(machine.getId());
                resourcesDto.setName(machine.getName());
                resourcesDto.setNumber(machine.getId());
                resourcesDto.setType(MESType.MACHINETYPE);
                resourcesDtoList.add(resourcesDto);
            }

            List<MROAsset> mroAsset = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }

    }


    private List<MESMachine> createMachines(TableData tableData, Map<String, MESMachine> dbMachinesMap, Map<String, MESMachineType> dbMachineTypesMap) throws ParseException {
        List<MESMachine> machines2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Machine Name");
            if (name != null && !name.trim().equals("")) {
                String machineTypeName = stringListHashMap.get("Machine Type".trim());
                String machineTypePath = stringListHashMap.get("Type Path".trim());
                String number = stringListHashMap.get("Machine Number");
                if ((machineTypeName == null || machineTypeName == "")) {
                    MESMachineType machineType = dbMachineTypesMap.get(machineTypeName);
                    if (machineType != null) {
                        MESMachine machine = dbMachinesMap.get(number);
                        if (machine == null) {
                            machines2.add(createMachine(i, number, machineType, stringListHashMap, dbMachinesMap));
                        } else {
                            updateMachine(i, machine, stringListHashMap);
                            machines2.add(machine);
                        }
                    } else {
                        machines2.add(createMachineByPath(i, number, machineTypePath, dbMachinesMap, dbMachineTypesMap, stringListHashMap));
                    }
                } else {
                    machines2.add(createMachineByPath(i, number, machineTypePath, dbMachinesMap, dbMachineTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Machine Name for row :" + i);
            }
        }
        if (machines2.size() > 0) {
            machineRepository.save(machines2);
        }
        return machines2;
    }


    private MESMachine createMachineByPath(int i, String number, String machineTypePath, Map<String, MESMachine> dbMachinesMap, Map<String, MESMachineType> dbMachineTypesMap, RowData stringListHashMap) {
        MESMachine Machine = null;
        if (machineTypePath != null && machineTypePath != "") {
            MESMachineType MESMachineType = this.getMachineTypes(machineTypePath);
            if (MESMachineType != null) {
                dbMachineTypesMap.put(MESMachineType.getName(), MESMachineType);
                Machine = dbMachinesMap.get(number);
                if (Machine == null)
                    Machine = createMachine(i, number, MESMachineType, stringListHashMap, dbMachinesMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Machine Type or Machine Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Machine Type or Machine Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Machine;
    }

    private MESMachine createMachine(Integer i, String name, MESMachineType machineType, RowData stringListHashMap, Map<String, MESMachine> dbMachinesMap) {
        MESMachine machine = new MESMachine();
        String workCenterNumber = stringListHashMap.get("WorkCenter Number".trim());
        String machineNumber = stringListHashMap.get("Machine Number".trim());
        String machineName = stringListHashMap.get("Machine Name".trim());
        String machineDescription = stringListHashMap.get("Machine Description".trim());
        String requiresMaintance = stringListHashMap.get("Requires Maintenance".trim());
        if (machineNumber == null || machineNumber.trim().equals("")) {
            machineNumber = importer.getNextNumberWithoutUpdate(machineType.getAutoNumberSource().getName(), autoNumberMap);
        }
        machine.setType(machineType);
        machine.setNumber(machineNumber);
        machine.setName(machineName);
        machine.setDescription(machineDescription);
        if (workCenterNumber != null) {
            MESWorkCenter workCenter = dbWorkCentersMap.get(workCenterNumber);
            if (workCenter != null) {
                machine.setWorkCenter(workCenter.getId());
            }
        }
        importer.saveNextNumber(machineType.getAutoNumberSource().getName(), machine.getNumber(), autoNumberMap);
        if (requiresMaintance.equals("No")) {
            machine.setRequiresMaintenance(false);
        }
        dbMachinesMap.put(machine.getNumber(), machine);
        return machine;
    }

    public void loadMachineClassificationTree() {
        getDefaultNumberSource();
        machineTypesMapByPath = new ConcurrentHashMap<>();
        List<MESMachineType> rootTypes = mesObjectTypeService.getMachineTypeTree();
        for (MESMachineType rootType : rootTypes) {
            rootMachineTypesMap.put(rootType.getName(), rootType);
            machineTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MESMachineType getMachineTypes(String path) {
        MESMachineType mesMachineType = machineTypesMapByPath.get(path);
        if (mesMachineType == null) {
            mesMachineType = getMachineTypeByPath(path);
            if (mesMachineType == null) {
                mesMachineType = createMachineTypeByPath(null, path);
            }
            machineTypesMapByPath.put(path, mesMachineType);
        }

        return mesMachineType;
    }

    private MESMachineType getMachineTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESMachineType itemType = rootMachineTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootMachineTypesMap.get(name);
        }
    }


    private MESMachineType createMachineTypeByPath(MESMachineType parentType, String path) {
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
            parentType = rootMachineTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESMachineType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = machineTypeRepository.save(parentType);
                rootMachineTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMachineTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESMachineType childItemType = new MESMachineType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = machineTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createMachineTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMachineTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESMachine updateMachine(Integer i, MESMachine mesMachine, RowData stringListHashMap) {
        String machineName = stringListHashMap.get("Machine Name".trim());
        String machineDescription = stringListHashMap.get("Machine Description".trim());

        mesMachine.setName(machineName);
        mesMachine.setDescription(machineDescription);

        return mesMachine;
    }


}
