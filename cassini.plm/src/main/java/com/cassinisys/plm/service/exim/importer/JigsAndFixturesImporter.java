package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.JigsFixtureTypeRepository;
import com.cassinisys.plm.repo.mes.MESJigsFixtureRepository;
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
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class JigsAndFixturesImporter {

    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
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

    public static ConcurrentMap<String, MESJigsFixtureType> rootJigsFixtureTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESJigsFixtureType> jigsFixtureTypesMapByPath = new ConcurrentHashMap<>();
    private static AutoNumber autoNumber;
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Jigs and Fixture Number Source");
    }


    public void importJigsFixtures(TableData tableData) throws ParseException {
        this.loadJigsFixtureClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESJigsFixture> dbJigsFixtureMap = new LinkedHashMap();
        Map<String, MESJigsFixtureType> dbJigsFixtureTypeMap = new LinkedHashMap();
        List<MESJigsFixture> jigsFixtures = jigsFixtureRepository.findAll();
        List<MESJigsFixtureType> jigsFixtureTypes = jigsFixtureTypeRepository.findAll();
        dbJigsFixtureMap = jigsFixtures.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbJigsFixtureTypeMap = jigsFixtureTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));

        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        for (MROAssetType assetType : assetTypes) {
            if (!dbAssetTypeMap.containsKey(assetType.getName())) dbAssetTypeMap.put(assetType.getName(), assetType);
        }
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESJigsFixture> jigsFixtures1 = createJigsFixtures(tableData, dbJigsFixtureMap, dbJigsFixtureTypeMap);
        if (jigsFixtures1.size() > 0) {
            List<MESManufacturerDataDTO> manufacturersDtoList = new ArrayList<>();
            for (MESJigsFixture jigsFixture : jigsFixtures1) {
                MESManufacturerDataDTO manufacturerDto = new MESManufacturerDataDTO();
                manufacturerDto.setId(jigsFixture.getId());
                manufacturerDto.setName(jigsFixture.getName());
                manufacturerDto.setNumber(jigsFixture.getId());
                manufacturersDtoList.add(manufacturerDto);
            }
            equipmentImporter.createMESManufacturer(tableData, manufacturersDtoList);
        }

        if (jigsFixtures1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESJigsFixture jigsFixture : jigsFixtures1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(jigsFixture.getId());
                resourcesDto.setName(jigsFixture.getName());
                resourcesDto.setNumber(jigsFixture.getId());
                resourcesDto.setType(MESType.JIGFIXTURETYPE);
                resourcesDtoList.add(resourcesDto);
            }

            List<MROAsset> mroAsset = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }
    }


    private List<MESJigsFixture> createJigsFixtures(TableData tableData, Map<String, MESJigsFixture> dbJigsFixtureMap, Map<String, MESJigsFixtureType> dbJigsFixtureTypeMap) {
        List<MESJigsFixture> jigsFixtures2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("JigsFixture Name");
            if (name != null && !name.trim().equals("")) {
                String jigsTypeName = stringListHashMap.get("JigsFixture Type");
                String number = stringListHashMap.get("JigsFixture Number");
                String typePath = stringListHashMap.get("Type Path");
                if (jigsTypeName != null && jigsTypeName != "") {
                    MESJigsFixtureType jigsFixtureType = dbJigsFixtureTypeMap.get(jigsTypeName);
                    if (jigsFixtureType != null) {
                        MESJigsFixture jigsFixture = dbJigsFixtureMap.get(number);
                        if (jigsFixture == null) {
                            jigsFixtures2.add(createJigsFixture(i, number, jigsFixtureType, stringListHashMap, dbJigsFixtureMap));
                        } else {
                            updateJigsFixture(i, jigsFixture, stringListHashMap);
                            jigsFixtures2.add(jigsFixture);
                        }

                    } else {
                        jigsFixtures2.add(createJigsFixtureByPath(i, number, typePath, dbJigsFixtureMap, dbJigsFixtureTypeMap, stringListHashMap));
                    }
                } else {
                    jigsFixtures2.add(createJigsFixtureByPath(i, number, typePath, dbJigsFixtureMap, dbJigsFixtureTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide JigsFixture Name for row :" + i);
            }
        }

        if (jigsFixtures2.size() > 0) {
            jigsFixtureRepository.save(jigsFixtures2);
        }

        return jigsFixtures2;
    }

    private MESJigsFixture createJigsFixtureByPath(int i, String number, String typePath, Map<String, MESJigsFixture> dbJigsFixtureMap,
                                     Map<String, MESJigsFixtureType> dbJigsFixtureTypeMap, RowData stringListHashMap) {
        MESJigsFixture jigsFixture = null;
        if (typePath != null && typePath != "") {
            MESJigsFixtureType mesJigsFixtureType = this.getJigsFixtureTypes(typePath);
            if (mesJigsFixtureType != null) {
                dbJigsFixtureTypeMap.put(mesJigsFixtureType.getName(), mesJigsFixtureType);
                jigsFixture = dbJigsFixtureMap.get(number);
                if (jigsFixture == null)
                    jigsFixture = createJigsFixture(i, number, mesJigsFixtureType, stringListHashMap, dbJigsFixtureMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide JigsFixture Type or JigsFixture Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide JigsFixture Type or JigsFixture Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return jigsFixture;
    }


    private MESJigsFixture createJigsFixture(Integer i, String name, MESJigsFixtureType jigsFixtureType,
                                             RowData stringListHashMap, Map<String, MESJigsFixture> dbJigsFixtureMap)  {
        MESJigsFixture jigsFixture = new MESJigsFixture();
        String jigsOrFixture = stringListHashMap.get("Jig or Fixture".trim());
        String jigsFixtureNumber = stringListHashMap.get("JigsFixture Number".trim());
        String jigsFixtureName = stringListHashMap.get("JigsFixture Name".trim());
        String jigsFixtureDescription = stringListHashMap.get("JigsFixtures Description".trim());
        String requiresMaintance = stringListHashMap.get("Requires Maintenance".trim());
        if (jigsFixtureNumber == null || jigsFixtureNumber.trim().equals("")) {
            jigsFixtureNumber = importer.getNextNumberWithoutUpdate(jigsFixtureType.getAutoNumberSource().getName(), autoNumberMap);
        }
        jigsFixture.setType(jigsFixtureType);
        if(jigsOrFixture.equalsIgnoreCase("Jig")){
          jigsFixture.setJigType(JigsFixtureType.JIG);
        }else {
            jigsFixture.setJigType(JigsFixtureType.FIXTURE);
        }
        jigsFixture.setNumber(jigsFixtureNumber);
        jigsFixture.setName(jigsFixtureName);
        jigsFixture.setDescription(jigsFixtureDescription);
        importer.saveNextNumber(jigsFixtureType.getAutoNumberSource().getName(), jigsFixture.getNumber(), autoNumberMap);

        if (requiresMaintance.equals("No")) {
            jigsFixture.setRequiresMaintenance(false);
        }
        // jigsFixture = jigsFixtureRepository.save(jigsFixture);
        dbJigsFixtureMap.put(jigsFixture.getNumber(), jigsFixture);


        return jigsFixture;
    }


    public void loadJigsFixtureClassificationTree() {
        getDefaultNumberSource();
        jigsFixtureTypesMapByPath = new ConcurrentHashMap<>();
        List<MESJigsFixtureType> rootTypes = mesObjectTypeService.getJigsFixTypeTree();
        for (MESJigsFixtureType rootType : rootTypes) {
            rootJigsFixtureTypesMap.put(rootType.getName(), rootType);
            jigsFixtureTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESJigsFixtureType getJigsFixtureTypes(String path) {
        MESJigsFixtureType mesJigsFixtureType = jigsFixtureTypesMapByPath.get(path);
        if (mesJigsFixtureType == null) {
            mesJigsFixtureType = getJigsFixtureTypeByPath(path);
            if (mesJigsFixtureType == null) {
                mesJigsFixtureType = createJigsFixtureTypeByPath(null, path);
            }
            jigsFixtureTypesMapByPath.put(path, mesJigsFixtureType);
        }

        return mesJigsFixtureType;
    }


    private MESJigsFixtureType getJigsFixtureTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESJigsFixtureType itemType = rootJigsFixtureTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootJigsFixtureTypesMap.get(name);
        }
    }


    private MESJigsFixtureType createJigsFixtureTypeByPath(MESJigsFixtureType parentType, String path) {
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
            parentType = rootJigsFixtureTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESJigsFixtureType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = jigsFixtureTypeRepository.save(parentType);
                rootJigsFixtureTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createJigsFixtureTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESJigsFixtureType childItemType = new MESJigsFixtureType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = jigsFixtureTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createJigsFixtureTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createJigsFixtureTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESJigsFixture updateJigsFixture(Integer i, MESJigsFixture mesJigsFixture, RowData stringListHashMap) {
        String jigsFixtureName = stringListHashMap.get("JigsFixture Name".trim());
        String jigsFixtureDescription = stringListHashMap.get("JigsFixtures Description".trim());

        mesJigsFixture.setName(jigsFixtureName);
        mesJigsFixture.setDescription(jigsFixtureDescription);

        return mesJigsFixture;
    }


}
