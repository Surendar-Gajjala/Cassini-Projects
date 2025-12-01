package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetMeter;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.MROAssetMeterRepository;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.repo.mro.MROAssetTypeRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.service.classification.MROObjectTypeService;
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
public class AssetImporter {

    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROAssetMeterRepository assetMeterRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESPlantRepository mesPlantRepository;
    @Autowired
    private MESWorkCenterRepository mesWorkCenterRepository;
    @Autowired
    private MESMachineRepository mesMachineRepository;
    @Autowired
    private MESEquipmentRepository mesEquipmentRepository;
    @Autowired
    private MESInstrumentRepository mesInstrumentRepository;
    @Autowired
    private MESToolRepository mesToolRepository;
    @Autowired
    private MESJigsFixtureRepository mesJigsFixtureRepository;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PlantsImporter plantsImporter;


    public static ConcurrentMap<String, MROAssetType> rootAssetTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MROAssetType> assetTypesMapByPath = new ConcurrentHashMap<>();

    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private static AutoNumber assetAutoNumber;

    private void getDefaultAssetNumberSource() {
        assetAutoNumber = plantsImporter.getDefaultPlantNumberSource("Default Tool Number Source");
    }

    public void loadassetClassificationTree() {
        getDefaultAssetNumberSource();
        rootAssetTypesMap = new ConcurrentHashMap<>();
        assetTypesMapByPath = new ConcurrentHashMap<>();
        List<MROAssetType> rootTypes = mroObjectTypeService.getAssetTypeTree();
        for (MROAssetType rootType : rootTypes) {
            rootAssetTypesMap.put(rootType.getName(), rootType);
            assetTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    private MROAssetType getAssetTypeByPath(String path) {
        MROAssetType mroAssetType = null;
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROAssetType assetType = rootAssetTypesMap.get(name);
            if (assetType != null) {
                assetType = assetType.getChildTypeByPath(path.substring(index + 1));
                return mroAssetType;
            } else {
                return assetType;
            }

        } else {
            name = path;
            return rootAssetTypesMap.get(name);
        }
    }


    private MROAssetType createAssetTypeByPath(MROAssetType parentType, String path) {
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
            parentType = rootAssetTypesMap.get(name);
            if (parentType == null) {
                parentType = new MROAssetType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(assetAutoNumber);
                parentType = mroObjectTypeService.createAssetType(parentType);
                rootAssetTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createAssetTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROAssetType childAssetType = new MROAssetType();
            childAssetType.setParentType(parentType.getId());
            childAssetType.setName(name);
            childAssetType.setDescription(name);
            childAssetType.setAutoNumberSource(parentType.getAutoNumberSource());
            parentType = mroObjectTypeService.createAssetType(childAssetType);
            parentType.getChildren().add(childAssetType);
            if (restOfPath != null) {
                return parentType = createAssetTypeByPath(childAssetType, restOfPath);
            } else {
                return childAssetType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createAssetTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }


    public void importAssets(TableData tableData) throws ParseException {
        this.loadassetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        Map<String, MROMeter> dbMetersMap = new LinkedHashMap();
        List<MROAsset> asset = assetRepository.findAll();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        List<MROMeter> dbMeters = meterRepository.findAll();
        dbAssetMap = asset.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbAssetTypeMap = assetTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        dbMetersMap = dbMeters.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MROAsset> assets1 = createAssets(tableData, dbAssetMap, dbAssetTypeMap, dbMetersMap);

        plantsImporter.createMeters(tableData, dbAssetMap);
    }

    private List<MROAsset> createAssets(TableData tableData, Map<String, MROAsset> dbAssetsMap, Map<String, MROAssetType> dbAssetTypeMap, Map<String, MROMeter> dbMetersMap) {
        List<MROAsset> assets2 = new LinkedList<>();
//        int i = 0;
//        for (RowData stringListHashMap : tableData.getRows()) {
//            if (stringListHashMap.containsKey("Asset Number") && (stringListHashMap.containsKey("Asset Type") || stringListHashMap.containsKey("Asset Type Path"))) {
//                String number = stringListHashMap.get("Asset Number".trim());
//                if (number != null && !number.trim().equals("")) {
//                    String assetTypeName = stringListHashMap.get("Asset Type".trim());
//                    String assetTypePath = stringListHashMap.get("Asset Type Path".trim());
//                    MROAssetType mroAssetType = null;
//                    if ((assetTypeName == null || assetTypeName == "")) {
//                        if (assetTypePath != null) {
//                            MROAssetType assetType1 = assetTypesMapByPath.get(assetTypePath);
//                            if (assetType1 == null) {
//                                mroAssetType = this.getAssetTypes(assetTypePath);
//                                String plantNumber = stringListHashMap.get("Asset Number".trim());
//                                MROAsset asset = dbAssetsMap.get(plantNumber);
//                                if (asset != null) {
//                                    updateAsset(i, asset, stringListHashMap);
//                                    assets2.add(asset);
//                                } else {
//                                    MROAsset asset1 = createAsset(i, number, mroAssetType, stringListHashMap, dbAssetsMap);
//                                    assets2.add(asset1);
//                                }
//                            }
//                        } else {
//                            throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
//                                    null, "Please provide valid Asset Type or Type Path an for row number:" + (i), LocaleContextHolder.getLocale()));
//                        }
//                    } else {
//                        String typeName = stringListHashMap.get("Asset Type".trim());
//                        MROAssetType assetType = dbAssetTypeMap.get(typeName);
//                        if (assetType == null) {
//                            if (assetTypePath != null) {
//                                mroAssetType = this.getAssetTypes(assetTypePath);
//                                String plantNumber = stringListHashMap.get("Asset Number".trim());
//                                MROAsset asset = dbAssetsMap.get(plantNumber);
//                                if (asset != null) {
//                                    MROAsset asset1 = updateAsset(i, asset, stringListHashMap);
//                                    assets2.add(asset1);
//                                } else {
//                                    MROAsset asset1 = createAsset(i, number, mroAssetType, stringListHashMap, dbAssetsMap);
//                                    assets2.add(asset1);
//                                }
//                            } else {
//                                throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
//                                        null, "Please provide valid Asset Type or Type Path an for row number:" + (i), LocaleContextHolder.getLocale()));
//                            }
//                        } else {
//                            String plantNumber = stringListHashMap.get("Asset Number".trim());
//                            MROAsset asset = dbAssetsMap.get(plantNumber);
//                            if (asset != null) {
//                                MROAsset asset1 = updateAsset(i, asset, stringListHashMap);
//                                assets2.add(asset1);
//                            } else {
//                                MROAsset asset1 = createAsset(i, number, assetType, stringListHashMap, dbAssetsMap);
//                                assets2.add(asset1);
//                            }
//                        }
//
//                    }
//
//                }
//
//            }
//            i++;
//        }
//
//        if (assets2.size() > 0) {
//            assets2 = assetRepository.save(assets2);
//        }
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Asset Name");
            if (name != null && !name.trim().equals("")) {
                String assetTypeName = stringListHashMap.get("Asset Type");
                String typePath = stringListHashMap.get("Asset Type Path");
                String number = stringListHashMap.get("Asset Number");
                if (assetTypeName != null && assetTypeName != "") {
                    MROAssetType assetType = dbAssetTypeMap.get(assetTypeName);
                    if (assetType != null) {
                        MROAsset asset = dbAssetsMap.get(number);
                        if (asset == null) {
                            assets2.add(createAsset(i, number, assetType, stringListHashMap, dbAssetsMap));
                        } else {
                            updateAsset(i, asset, stringListHashMap);
                            assets2.add(asset);
                        }

                    } else {
                        assets2.add(createAssetByPath(i, number, typePath, dbAssetsMap, dbAssetTypeMap, stringListHashMap));
                    }
                } else {
                    assets2.add(createAssetByPath(i, number, typePath, dbAssetsMap, dbAssetTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Asset Name for row :" + i);
            }
        }

        if (assets2.size() > 0) {
            assetRepository.save(assets2);
        }

        return assets2;
    }


    private MROAsset createAssetByPath(int i, String number, String typePath, Map<String, MROAsset> dbAssetMap,
                                       Map<String, MROAssetType> dbAssetTypeMap, RowData stringListHashMap) {
        MROAsset meter = null;
        if (typePath != null && typePath != "") {
            MROAssetType mesAssetType = this.getAssetTypes(typePath);
            if (mesAssetType != null) {
                dbAssetTypeMap.put(mesAssetType.getName(), mesAssetType);
                meter = dbAssetMap.get(number);
                if (meter == null)
                    meter = createAsset(i, number, mesAssetType, stringListHashMap, dbAssetMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide Asset Type or Asset Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide Asset Type or Asset Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return meter;
    }



    private MROAsset createAsset(Integer i, String name, MROAssetType assetType, RowData stringListHashMap, Map<String, MROAsset> dbAssetsMap) {
        MROAsset asset = new MROAsset();
        String resourceType = stringListHashMap.get("Resource Type".trim());
        String resourceNumber = stringListHashMap.get("Resource Number".trim());
        String assetNumber = stringListHashMap.get("Asset Number".trim());
        String assetName = stringListHashMap.get("Asset Name".trim());
        String assetDescription = stringListHashMap.get("Asset Description".trim());
        String metered = stringListHashMap.get("Metered".trim());
        String meters = stringListHashMap.get("Meters".trim());

        if (assetNumber == null || assetNumber.trim().equals("")) {
            assetNumber = importer.getNextNumberWithoutUpdate(assetType.getAutoNumberSource().getName(), autoNumberMap);
        }
        MESType resourceType1 = selectResourceType(resourceType);
        asset.setResourceType(resourceType1);

        Integer resourceNumber1 = selectResource(resourceType, resourceNumber);
        asset.setResource(resourceNumber1);

        asset.setType(assetType);
        asset.setNumber(assetNumber);
        asset.setName(assetName);
        asset.setDescription(assetDescription);

        importer.saveNextNumber(assetType.getAutoNumberSource().getName(), asset.getNumber(), autoNumberMap);

        if (metered.equalsIgnoreCase("Yes")) {
            asset.setMetered(true);
        }
        dbAssetsMap.put(asset.getNumber(), asset);
//        asset = assetRepository.save(asset);

//        if (metered.equalsIgnoreCase("Yes")) {
//            MROAssetMeter mroAssetMeter = new MROAssetMeter();
//            MROMeter mroMeter = meterRepository.findByName(meters);
//            mroAssetMeter.setAsset(asset.getId());
//            mroAssetMeter.setMeter(mroMeter.getId());
//            assetMeterRepository.save(mroAssetMeter);
//        }


        return asset;
    }

    private MESType selectResourceType(String objectType) {
        MESType resourceType = null;
        switch (objectType) {
            case "Plant":
                resourceType = MESType.PLANTTYPE;
                break;
            case "Work Center":
                resourceType = MESType.WORKCENTERTYPE;
                break;
            case "Machine":
                resourceType = MESType.MACHINETYPE;
                break;
            case "Equipment":
                resourceType = MESType.EQUIPMENTTYPE;
                break;
            case "Instrument":
                resourceType = MESType.INSTRUMENTTYPE;
                break;
            case "Tool":
                resourceType = MESType.TOOLTYPE;
                break;
            case "Jigs & Fixtures":
                resourceType = MESType.JIGFIXTURETYPE;
                break;
            default:
                resourceType = null;
        }
        return resourceType;
    }

    private Integer selectResource(String resourceType1, String resourceNumber) {
        Integer resource = null;
        switch (resourceType1) {
            case "Plant":
                resource = mesPlantRepository.findByNumber(resourceNumber).getId();
                break;
            case "Work Center":
                resource = mesWorkCenterRepository.findByNumber(resourceNumber).getId();
                break;
            case "Machine":
                resource = mesMachineRepository.findByNumber(resourceNumber).getId();
                break;
            case "Equipment":
                resource = mesEquipmentRepository.findByNumber(resourceNumber).getId();
                break;
            case "Instrument":
                resource = mesInstrumentRepository.findByNumber(resourceNumber).getId();
                break;
            case "Tool":
                resource = mesToolRepository.findByNumber(resourceNumber).getId();
                break;
            case "Jigs & Fixtures":
                resource = mesJigsFixtureRepository.findByNumber(resourceNumber).getId();
                break;
            default:
                resource = null;
        }
        return resource;
    }


    public MROAssetType getAssetTypes(String path) {
        MROAssetType mesPlantType = assetTypesMapByPath.get(path);
        if (mesPlantType == null) {
            mesPlantType = getAssetTypeByPath(path);
            if (mesPlantType == null) {
                mesPlantType = createAssetTypeByPath(null, path);
            }
            assetTypesMapByPath.put(path, mesPlantType);
        }

        return mesPlantType;
    }

    private MROAsset updateAsset(Integer i, MROAsset asset, RowData stringListHashMap) {
        String assetName = stringListHashMap.get("Asset Name".trim());
        String assetDescription = stringListHashMap.get("Asset Description".trim());

        asset.setName(assetName);
        asset.setDescription(assetDescription);

        return asset;
    }
}
