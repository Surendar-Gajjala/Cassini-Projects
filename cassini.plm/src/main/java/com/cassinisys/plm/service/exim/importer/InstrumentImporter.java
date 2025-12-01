package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.MESEquipmentType;
import com.cassinisys.plm.model.mes.MESInstrument;
import com.cassinisys.plm.model.mes.MESInstrumentType;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.InstrumentTypeRepository;
import com.cassinisys.plm.repo.mes.MESInstrumentRepository;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.repo.mro.MROAssetTypeRepository;
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
public class InstrumentImporter {
    @Autowired
    private MESInstrumentRepository instrumentRepository;
    @Autowired
    private InstrumentTypeRepository instrumentTypeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private EquipmentImporter equipmentImporter;
    @Autowired
    private Importer importer;
    
    private static AutoNumber autoNumber;

    public static ConcurrentMap<String, MESInstrumentType> rootInstrumentTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESInstrumentType> instrumentTypesMapByPath = new ConcurrentHashMap<>();

    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Instrument Number Source");
    }

    public void importInstruments(TableData tableData) throws ParseException {
        this.loadInstrumentClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESInstrument> dbInstrumentMap = new LinkedHashMap();
        Map<String, MESInstrumentType> dbInstrumentTypeMap = new LinkedHashMap();
        List<MESInstrument> instruments = instrumentRepository.findAll();
        List<MESInstrumentType> instrumentTypes = instrumentTypeRepository.findAll();
        dbInstrumentMap = instruments.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        for (MESInstrumentType instrumentType : instrumentTypes) {
            if (!dbInstrumentTypeMap.containsKey(instrumentType.getName())) dbInstrumentTypeMap.put(instrumentType.getName(), instrumentType);
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
        List<MESInstrument> instruments1 = createInstruments(tableData, dbInstrumentMap, dbInstrumentTypeMap);

        if (instruments1.size() > 0) {
            List<MESManufacturerDataDTO> manufacturersDtoList = new ArrayList<>();
            for (MESInstrument instrument : instruments1) {
                MESManufacturerDataDTO manufacturerDto = new MESManufacturerDataDTO();
                manufacturerDto.setId(instrument.getId());
                manufacturerDto.setName(instrument.getName());
                manufacturerDto.setNumber(instrument.getId());
                manufacturersDtoList.add(manufacturerDto);
            }
            equipmentImporter.createMESManufacturer(tableData, manufacturersDtoList);
        }

        if (instruments1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESInstrument instrument : instruments1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(instrument.getId());
                resourcesDto.setName(instrument.getName());
                resourcesDto.setNumber(instrument.getId());
                resourcesDto.setType(MESType.INSTRUMENTTYPE);
                resourcesDtoList.add(resourcesDto);
            }
            List<MROAsset> mroAsset = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }
    }

    private List<MESInstrument> createInstruments(TableData tableData, Map<String, MESInstrument> dbInstrumentsMap, Map<String, MESInstrumentType> dbInstrumentTypesMap) throws ParseException {
        List<MESInstrument> instruments2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Instrument Name");
            if (name != null && !name.trim().equals("")) {
                String instrumentTypeName = stringListHashMap.get("Instrument Type".trim());
                String instrumentTypePath = stringListHashMap.get("Type Path".trim());
                String number = stringListHashMap.get("Instrument Number");
                if ((instrumentTypeName == null || instrumentTypeName == "")) {
                    MESInstrumentType instrumentType = dbInstrumentTypesMap.get(instrumentTypeName);
                    if (instrumentType != null) {
                        MESInstrument instrument = dbInstrumentsMap.get(number);
                        if (instrument == null) {
                            instruments2.add(createInstrument(i, number, instrumentType, stringListHashMap, dbInstrumentsMap));
                        } else {
                            updateInstrument(i, instrument, stringListHashMap);
                            instruments2.add(instrument);
                        }
                    } else {
                        instruments2.add(createInstrumentByPath(i, number, instrumentTypePath, dbInstrumentsMap, dbInstrumentTypesMap, stringListHashMap));
                    }
                } else {
                    instruments2.add(createInstrumentByPath(i, number, instrumentTypePath, dbInstrumentsMap, dbInstrumentTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Instrument Name for row :" + i);
            }
        }
        if (instruments2.size() > 0) {
            instrumentRepository.save(instruments2);
        }
        return instruments2;
    }


    private MESInstrument createInstrumentByPath(int i, String number, String instrumentTypePath, Map<String, MESInstrument> dbInstrumentsMap, Map<String, MESInstrumentType> dbInstrumentTypesMap, RowData stringListHashMap) {
        MESInstrument Instrument = null;
        if (instrumentTypePath != null && instrumentTypePath != "") {
            MESInstrumentType MESInstrumentType = this.getInstrumentTypes(instrumentTypePath);
            if (MESInstrumentType != null) {
                dbInstrumentTypesMap.put(MESInstrumentType.getName(), MESInstrumentType);
                Instrument = dbInstrumentsMap.get(number);
                if (Instrument == null)
                    Instrument = createInstrument(i, number, MESInstrumentType, stringListHashMap, dbInstrumentsMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Instrument Type or Instrument Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Instrument Type or Instrument Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Instrument;
    }



    private MESInstrument createInstrument(Integer i, String name, MESInstrumentType instrumentType, RowData stringListHashMap, Map<String, MESInstrument> dbInstrumentsMap) {
        MESInstrument instrument = new MESInstrument();
        String instrumentNumber = stringListHashMap.get("Instrument Number".trim());
        String instrumentName = stringListHashMap.get("Instrument Name".trim());
        String instrumentDescription = stringListHashMap.get("Instrument Description".trim());
        String requiresMaintance = stringListHashMap.get("Requires Maintenance".trim());
        if (instrumentNumber == null || instrumentNumber.trim().equals("")) {
            instrumentNumber = importer.getNextNumberWithoutUpdate(instrumentType.getAutoNumberSource().getName(), autoNumberMap);
        }
        instrument.setType(instrumentType);
        instrument.setNumber(instrumentNumber);
        instrument.setName(instrumentName);
        instrument.setDescription(instrumentDescription);

        importer.saveNextNumber(instrumentType.getAutoNumberSource().getName(), instrument.getNumber(), autoNumberMap);

        if (requiresMaintance.equals("No")) {
            instrument.setRequiresMaintenance(false);
        }

        dbInstrumentsMap.put(instrument.getNumber(), instrument);

        return instrument;
    }


    public void loadInstrumentClassificationTree() {
        getDefaultNumberSource();
        instrumentTypesMapByPath = new ConcurrentHashMap<>();
        List<MESInstrumentType> rootTypes = mesObjectTypeService.getInstrumentTypeTree();
        for (MESInstrumentType rootType : rootTypes) {
            rootInstrumentTypesMap.put(rootType.getName(), rootType);
            instrumentTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESInstrumentType getInstrumentTypes(String path) {
        MESInstrumentType mesInstrumentType = instrumentTypesMapByPath.get(path);
        if (mesInstrumentType == null) {
            mesInstrumentType = getInstrumentTypeByPath(path);
            if (mesInstrumentType == null) {
                mesInstrumentType = createInstrumentTypeByPath(null, path);
            }
            instrumentTypesMapByPath.put(path, mesInstrumentType);
        }

        return mesInstrumentType;
    }


    private MESInstrumentType getInstrumentTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESInstrumentType itemType = rootInstrumentTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootInstrumentTypesMap.get(name);
        }
    }


    private MESInstrumentType createInstrumentTypeByPath(MESInstrumentType parentType, String path) {
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
            parentType = rootInstrumentTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESInstrumentType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = instrumentTypeRepository.save(parentType);
                rootInstrumentTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createInstrumentTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESInstrumentType childItemType = new MESInstrumentType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = instrumentTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createInstrumentTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createInstrumentTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESInstrument updateInstrument(Integer i, MESInstrument mesInstrument, RowData stringListHashMap) {
        String instrumentName = stringListHashMap.get("Instrument Name".trim());
        String instrumentDescription = stringListHashMap.get("Instrument Description".trim());

        mesInstrument.setName(instrumentName);
        mesInstrument.setDescription(instrumentDescription);

        return mesInstrument;
    }


}
