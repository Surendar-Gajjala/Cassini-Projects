package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.model.mes.MESPlantType;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetMeter;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.model.mro.MROMeter;
import com.cassinisys.plm.repo.mes.MESPlantRepository;
import com.cassinisys.plm.repo.mes.PlantTypeRepository;
import com.cassinisys.plm.repo.mro.MROAssetMeterRepository;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.repo.mro.MROAssetTypeRepository;
import com.cassinisys.plm.repo.mro.MROMeterRepository;
import com.cassinisys.plm.service.classification.MESObjectTypeService;
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

@Service
@Scope("prototype")
public class PlantsImporter {

    @Autowired
    private MESPlantRepository plantRepository;
    @Autowired
    private PlantTypeRepository plantTypeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MROMeterRepository meterRepository;
    @Autowired
    private MROAssetMeterRepository assetMeterRepository;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;

    public static ConcurrentMap<String, MESPlantType> rootPlantTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESPlantType> plantTypesMapByPath = new ConcurrentHashMap<>();

    private ConcurrentMap<String, MROAssetType> rootAssetTypesMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, MROAssetType> assetTypesMapByPath = new ConcurrentHashMap<>();

    private static AutoNumber autoNumber;
    private static AutoNumber assetAutoNumber;

    private void getAssetNumberSource() {
        autoNumber = getDefaultPlantNumberSource("Default Asset Number Source");
    }

    private void getDefaultNumberSource() {
        autoNumber = this.getDefaultPlantNumberSource("Default Plant Number Source");
    }

    Map<String, Person> dbPersonMap = new LinkedHashMap();

    public void importPlants(TableData tableData) throws ParseException {
        this.loadPlantClassificationTree();
        this.loadAssetClassificationTree();
        Map<String, MESPlant> dbPlantMap = new LinkedHashMap();
        Map<String, MESPlantType> dbPlantTypeMap = new LinkedHashMap();
        List<MESPlant> plants = plantRepository.findAll();
        List<MESPlantType> plantTypes = plantTypeRepository.findAll();
        dbPlantMap = plants.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbPlantTypeMap = plantTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        dbAssetTypeMap = assetTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        List<Person> persons = personRepository.findAll();
        for (Person person : persons) {
            dbPersonMap.put(person.getFirstName(), person);
        }
        List<MESPlant> plants1 = createPlants(tableData, dbPlantMap, dbPlantTypeMap);
        if (plants1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESPlant plant : plants1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(plant.getId());
                resourcesDto.setName(plant.getName());
                resourcesDto.setNumber(plant.getId());
                resourcesDto.setType(MESType.PLANTTYPE);
                resourcesDtoList.add(resourcesDto);
            }
            List<MROAsset> mroAssets = createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            createMeters(tableData, dbAssetMap);
        }

    }

    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private void getAutoNumbers() {
        autoNumberMap = new HashMap<>();
        autoNumberMap = importer.getCommonAutoNumbers();
    }

    private List<MESPlant> createPlants(TableData tableData, Map<String, MESPlant> dbPlantMap, Map<String, MESPlantType> dbPlantTypeMap) {
        getAutoNumbers();
        List<MESPlant> plants2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Plant Name");
            if (name != null && !name.trim().equals("")) {
                String number = stringListHashMap.get("Plant Number");
                String plantTypeName = stringListHashMap.get("Plant Type");
                String typePath = stringListHashMap.get("Type Path");
                if (plantTypeName != null && plantTypeName != "") {
                    MESPlantType plantType = dbPlantTypeMap.get(plantTypeName);
                    if (plantType != null) {
                        MESPlant plant = dbPlantMap.get(number);
                        if (plant == null) {
                            plants2.add(createPlant(i, number, plantType, stringListHashMap, dbPlantMap));
                        } else {
                            updatePlant(i, plant, stringListHashMap);
                            plants2.add(plant);

                        }

                    } else {
                        plants2.add(createPlantByPath(i, number, typePath, dbPlantMap, dbPlantTypeMap, stringListHashMap));
                    }
                } else {
                    plants2.add(createPlantByPath(i, number, typePath, dbPlantMap, dbPlantTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Name for row :" + i);
            }
        }

        if (plants2.size() > 0) {
            plantRepository.save(plants2);
        }

        return plants2;
    }

    private MESPlant createPlantByPath(int i, String number, String typePath, Map<String, MESPlant> dbPlantMap, Map<String, MESPlantType> dbPlantTypeMap, RowData stringListHashMap) {
        MESPlant plant = null;
        if (typePath != null && typePath != "") {
            MESPlantType mesPlantType = this.getPlantTypes(typePath);
            if (mesPlantType != null) {
                dbPlantTypeMap.put(mesPlantType.getName(), mesPlantType);
                plant = dbPlantMap.get(number);
                if (plant == null)
                    plant = createPlant(i, number, mesPlantType, stringListHashMap, dbPlantMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_plant_type_for_row_number" + (i),
                        null, "Please provide plant Type or plant Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_plant_type_for_row_number" + (i),
                    null, "Please provide plant Type or plant Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return plant;
    }


    private MESPlant createPlant(Integer i, String number, MESPlantType plantType, RowData stringListHashMap, Map<String, MESPlant> dbPlantsMap) {
        MESPlant plant = new MESPlant();
        String plantNumber = stringListHashMap.get("Plant Number");
        String plantName = stringListHashMap.get("Plant Name");
        String plantDescription = stringListHashMap.get("Description");
        String plantManager = stringListHashMap.get("Plant Manager");
        String requiresMaintance = stringListHashMap.get("Requires Maintenance");
        String address = stringListHashMap.get("Address");
        String city = stringListHashMap.get("City");
        String country = stringListHashMap.get("Country");
        String postalCode = stringListHashMap.get("Postal Code");
        String phoneNumber = stringListHashMap.get("Phone Number");
        String mobileNumber = stringListHashMap.get("Mobile Number");
        String faxNumber = stringListHashMap.get("Fax Number");
        String email = stringListHashMap.get("E-Mail");
        String notes = stringListHashMap.get("Notes");
        if (plantNumber == null || plantNumber.trim().equals("")) {
            plantNumber = importer.getNextNumberWithoutUpdate(plantType.getAutoNumberSource().getName(), autoNumberMap);
        }
        plant.setType(plantType);
        plant.setNumber(plantNumber);
        plant.setName(plantName);
        plant.setDescription(plantDescription);
        if (plantManager != null) {
            Person person = dbPersonMap.get(plantManager);
            if (person != null) {
                plant.setPlantPerson(person.getId());
            } else {

            }
        }
        plant.setAddress(address);
        plant.setCity(city);
        plant.setCountry(country);
        plant.setPostalCode(postalCode);
        plant.setPhoneNumber(phoneNumber);
        plant.setMobileNumber(mobileNumber);
        plant.setAddress(faxNumber);
        plant.setEmail(email);
        plant.setNotes(notes);
        importer.saveNextNumber(plantType.getAutoNumberSource().getName(), plant.getNumber(), autoNumberMap);
        if (requiresMaintance.equals("No")) {
            plant.setRequiresMaintenance(false);
        }
        dbPlantsMap.put(plant.getNumber(), plant);
        return plant;
    }

    public List<MROAsset> createAssets(TableData tableData, Map<String, MROAsset> dbAssetMap, Map<String, MROAssetType> dbAssetTypeMap, List<AssetResourcesDto> resourcesDtos) {
        getAutoNumbers();
        List<MROAsset> assets2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Asset Name");
            String requiresMaintenance = stringListHashMap.get("Requires Maintenance");
            if (requiresMaintenance.equalsIgnoreCase("Yes")) {
                if (name != null && !name.trim().equals("")) {
                    String number = stringListHashMap.get("Asset Number");
                    String assetTypeName = stringListHashMap.get("Asset Type");
                    String typePath = stringListHashMap.get("Type Path");
                    AssetResourcesDto resourcesDto = resourcesDtos.get(i);
                    if (assetTypeName != null && assetTypeName != "") {
                        MROAssetType assetType = dbAssetTypeMap.get(assetTypeName);
                        if (assetType != null) {
                            MROAsset asset = dbAssetMap.get(number);
                            if (asset == null) {
                                assets2.add(createAsset(i, number, assetType, stringListHashMap, dbAssetMap, resourcesDto));
                            } else {
                                updateAsset(i, asset, stringListHashMap);
                                assets2.add(asset);

                            }

                        } else {
                            assets2.add(createAssetByPath(i, number, typePath, dbAssetMap, dbAssetTypeMap, stringListHashMap, resourcesDto));
                        }
                    } else {
                        assets2.add(createAssetByPath(i, number, typePath, dbAssetMap, dbAssetTypeMap, stringListHashMap, resourcesDto));
                    }
                } else {
                    throw new CassiniException("Please provide Asset Number for row :" + i);
                }
            }
        }

        if (assets2.size() > 0) {
            assetRepository.save(assets2);
        }

        return assets2;
    }

    private MROAsset createAssetByPath(int i, String number, String typePath, Map<String, MROAsset> dbAssetMap, Map<String, MROAssetType> dbAssetTypeMap,
                                       RowData stringListHashMap, AssetResourcesDto resourcesDto) {
        MROAsset asset = null;
        if (typePath != null && typePath != "") {
            MROAssetType mroAssetType = this.getAssetTypes(typePath);
            if (mroAssetType != null) {
                dbAssetTypeMap.put(mroAssetType.getName(), mroAssetType);
                asset = dbAssetMap.get(number);
                if (asset == null)
                    asset = createAsset(i, number, mroAssetType, stringListHashMap, dbAssetMap, resourcesDto);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_asset_type_for_row_number" + (i),
                        null, "Please provide asset Type or asset Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_plant_type_for_row_number" + (i),
                    null, "Please provide asset Type or asset Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return asset;
    }


    public void loadPlantClassificationTree() {
        getDefaultNumberSource();
        plantTypesMapByPath = new ConcurrentHashMap<>();
        List<MESPlantType> rootTypes = mesObjectTypeService.getPlantTypeTree();
        for (MESPlantType rootType : rootTypes) {
            rootPlantTypesMap.put(rootType.getName(), rootType);
            plantTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MESPlantType getPlantTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESPlantType plantType = rootPlantTypesMap.get(name);
            if (plantType != null) {
                return plantType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return plantType;
            }
        } else {
            name = path;
            return rootPlantTypesMap.get(name);
        }
    }

    public MESPlantType createPlantTypeByPath(MESPlantType parentType, String path) {
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
            parentType = rootPlantTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESPlantType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultPlantNumberSource("Default Plant Number Source"));
                parentType = plantTypeRepository.save(parentType);
                rootPlantTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createPlantTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESPlantType childItemType = new MESPlantType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = plantTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createPlantTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createPlantTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    public MESPlantType getPlantTypes(String path) {
        MESPlantType mesPlantType = plantTypesMapByPath.get(path);
        if (mesPlantType == null) {
            mesPlantType = getPlantTypeByPath(path);
            if (mesPlantType == null) {
                mesPlantType = createPlantTypeByPath(null, path);
            }
            plantTypesMapByPath.put(path, mesPlantType);
        }

        return mesPlantType;
    }

    public MESPlant updatePlant(Integer i, MESPlant plant, RowData stringListHashMap) {
        String plantName = stringListHashMap.get("Plant Name");
        String plantDescription = stringListHashMap.get("Description");
        String address = stringListHashMap.get("Address");
        String city = stringListHashMap.get("City");
        String country = stringListHashMap.get("Country");
        String postalCode = stringListHashMap.get("Postal Code");
        String phoneNumber = stringListHashMap.get("Phone Number");
        String mobileNumber = stringListHashMap.get("Mobile Number");
        String faxNumber = stringListHashMap.get("Fax Number");
        String email = stringListHashMap.get("E-Mail");
        String notes = stringListHashMap.get("Notes");

        plant.setName(plantName);
        plant.setDescription(plantDescription);
        plant.setAddress(address);
        plant.setCity(city);
        plant.setCountry(country);
        plant.setPostalCode(postalCode);
        plant.setPhoneNumber(phoneNumber);
        plant.setMobileNumber(mobileNumber);
        plant.setAddress(faxNumber);
        plant.setEmail(email);
        plant.setNotes(notes);
        return plant;
    }


    private MROAsset createAsset(Integer i, String number, MROAssetType assetType, RowData stringListHashMap, Map<String, MROAsset> dbAssetsMap, AssetResourcesDto plant) {
        MROAsset asset = new MROAsset();
        String assetNumber = stringListHashMap.get("Asset Number");
        String assetName = stringListHashMap.get("Asset Name");
        String assetDescription = stringListHashMap.get("Description");
        String metered = stringListHashMap.get("Metered");
        if (assetNumber == null || assetNumber.trim().equals("")) {
            assetNumber = importer.getNextNumberWithoutUpdate(assetType.getAutoNumberSource().getName(), autoNumberMap);
        }
        asset.setType(assetType);
        asset.setNumber(assetNumber);
        asset.setName(assetName);
        asset.setDescription(assetDescription);
        asset.setResource(plant.getId());
        asset.setResourceType(plant.getType());
        importer.saveNextNumber(assetType.getAutoNumberSource().getName(), asset.getNumber(), autoNumberMap);
        if (metered.equalsIgnoreCase("Yes")) {
            asset.setMetered(true);
        }
        dbAssetsMap.put(asset.getNumber(), asset);
        return asset;
    }


    public void loadAssetClassificationTree() {
        getAssetNumberSource();
        assetTypesMapByPath = new ConcurrentHashMap<>();
        List<MROAssetType> rootTypes = mroObjectTypeService.getAssetTypeTree();
        for (MROAssetType rootType : rootTypes) {
            rootAssetTypesMap.put(rootType.getName(), rootType);
            assetTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private MROAssetType getAssetTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROAssetType assetType = rootAssetTypesMap.get(name);
            if (assetType != null) {
                return assetType.getChildTypeByPath(path.substring(index + 1));
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
                parentType.setAutoNumberSource(getDefaultPlantNumberSource("Default Asset Number Source"));
                parentType = assetTypeRepository.save(parentType);
                rootAssetTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createAssetTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROAssetType childItemType = new MROAssetType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = assetTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                return parentType = createAssetTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
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
        String assetName = stringListHashMap.get("Asset Name");
        String assetDescription = stringListHashMap.get("Description");

        asset.setName(assetName);
        asset.setDescription(assetDescription);

        return asset;
    }

    public AutoNumber getDefaultPlantNumberSource(String type) {
        return autoNumberService.getByName(type);
    }

    public void createMeters(TableData tableData, Map<String, MROAsset> dbAssetsMap) {
        List<MROAssetMeter> mroAssetMeters = new ArrayList<>();
        List<MROMeter> mroMeters = meterRepository.findAll();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Metered") && stringListHashMap.containsKey("Meters")) {
                String metered = stringListHashMap.get("Metered");
                String meters = stringListHashMap.get("Meters");
                String assetNumber = stringListHashMap.get("Asset Number");
                MROAsset asset = null;
                if (metered.equalsIgnoreCase("Yes")) {
                    asset = dbAssetsMap.get(assetNumber);
                    if (asset != null) {
                        Map<String, MROMeter> dbMetersMap = new ConcurrentHashMap<>();
                        dbMetersMap = mroMeters.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
                        MROMeter mroMeter = dbMetersMap.get(meters);
                        MROAssetMeter mroAssetMeter = new MROAssetMeter();
                        mroAssetMeter.setAsset(asset.getId());
                        if (mroMeter != null) {
                            mroAssetMeter.setMeter(mroMeter.getId());
                            mroAssetMeters.add(mroAssetMeter);
                        }
                    }
                }
            }
            i++;
        }
        if (mroAssetMeters.size() > 0) {
            assetMeterRepository.save(mroAssetMeters);
        }
    }
}

