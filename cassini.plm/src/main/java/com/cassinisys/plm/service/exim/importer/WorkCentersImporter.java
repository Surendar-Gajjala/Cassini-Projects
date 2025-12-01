package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.MESAssemblyLineRepository;
import com.cassinisys.plm.repo.mes.MESPlantRepository;
import com.cassinisys.plm.repo.mes.MESWorkCenterRepository;
import com.cassinisys.plm.repo.mes.WorkCenterTypeRepository;
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

@Service
@Scope("prototype")
public class WorkCentersImporter {

    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private WorkCenterTypeRepository workCenterTypeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESPlantRepository plantRepository;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROAssetRepository assetRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    private static AutoNumber autoNumber;
    public static ConcurrentMap<String, MESWorkCenterType> rootWorkCenterTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESWorkCenterType> workCenterTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, MESPlant> dbPlantsMap = new LinkedHashMap();
    Map<String, MESAssemblyLine> dbAssemblyLineMap = new LinkedHashMap();

    private void getDefaultNumberSource() {
        autoNumber = autoNumberService.getByName("Default Work Center Number Source");
    }
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();
    public void importWorkCenters(TableData tableData) throws ParseException {
        dbPlantsMap = new LinkedHashMap();
        dbAssemblyLineMap = new LinkedHashMap<>();
        autoNumberMap = new HashMap<>();
        this.loadWorkCenterClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        Map<String, MESWorkCenter> dbWorkCentersMap = new LinkedHashMap();
        Map<String, MESWorkCenterType> dbWorkCenterTypeMap = new LinkedHashMap();
        List<MESWorkCenter> workCenters = workCenterRepository.findAll();
        List<MESWorkCenterType> workCenterTypes = workCenterTypeRepository.findAll();
        dbWorkCentersMap = workCenters.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbWorkCenterTypeMap = workCenterTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        Map<String, MROAssetType> dbAssetTypeMap = new LinkedHashMap();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        dbAssetTypeMap = assetTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        Map<String, MROAsset> dbAssetMap = new LinkedHashMap();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));

        List<MESPlant> plants = plantRepository.findAll();
        dbPlantsMap = plants.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        List<MESAssemblyLine> assemblyLines = assemblyLineRepository.findAll();
        dbAssemblyLineMap = assemblyLines.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESWorkCenter> workCenters1 = createWorkCenters(tableData, dbWorkCentersMap, dbWorkCenterTypeMap);
        if (workCenters1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESWorkCenter workCenter : workCenters1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(workCenter.getId());
                resourcesDto.setName(workCenter.getName());
                resourcesDto.setNumber(workCenter.getId());
                resourcesDto.setType(MESType.WORKCENTERTYPE);
                resourcesDtoList.add(resourcesDto);
            }
            List<MROAsset> mroAssets = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }

    }

    private List<MESWorkCenter> createWorkCenters(TableData tableData, Map<String, MESWorkCenter> dbWorkCentersMap, Map<String, MESWorkCenterType> dbWorkCenterTypeMap) {
        List<MESWorkCenter> workCenters2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("WorkCenter Name");
            if (name != null && !name.trim().equals("")) {
                String wcTypeName = stringListHashMap.get("WorkCenter Type");
                String typePath = stringListHashMap.get("Type Path");
                String number = stringListHashMap.get("WorkCenter Number");
                if (wcTypeName != null && wcTypeName != "") {
                    MESWorkCenterType workCenterType = dbWorkCenterTypeMap.get(wcTypeName);
                    if (workCenterType != null) {
                        MESWorkCenter workCenter = dbWorkCentersMap.get(number);
                        if (workCenter == null) {
                            workCenters2.add(createWorkcenter(i, number, workCenterType, stringListHashMap, dbWorkCentersMap));
                        } else {
                            updateWorkCenter(i, workCenter, stringListHashMap);
                            workCenters2.add(workCenter);

                        }

                    } else {
                        workCenters2.add(createWorkCenterByPath(i, number, typePath, dbWorkCentersMap, dbWorkCenterTypeMap, stringListHashMap));
                    }
                } else {
                    workCenters2.add(createWorkCenterByPath(i, number, typePath, dbWorkCentersMap, dbWorkCenterTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Work Center Name for row :" + i);
            }
        }

        if (workCenters2.size() > 0) {
            workCenterRepository.save(workCenters2);
        }

        return workCenters2;
    }

    private MESWorkCenter createWorkCenterByPath(int i, String number, String typePath, Map<String, MESWorkCenter> dbWorkCentersMap,
                                                     Map<String, MESWorkCenterType> dbWorkCenterTypeMap, RowData stringListHashMap) {
        MESWorkCenter workCenter = null;
        if (typePath != null && typePath != "") {
            MESWorkCenterType workCenterType = this.getWorkCenterTypes(typePath);
            if (workCenterType != null) {
                dbWorkCenterTypeMap.put(workCenterType.getName(), workCenterType);
                workCenter = dbWorkCentersMap.get(number);
                if (workCenter == null)
                    workCenter = createWorkcenter(i, number, workCenterType, stringListHashMap, dbWorkCentersMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_work_center_type_for_row_number" + (i),
                        null, "Please provide Work Center Type or Work Center Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_work_center_type_for_row_number" + (i),
                    null, "Please provide Work Center Type or Work Center Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return workCenter;
    }

    private MESWorkCenter createWorkcenter(Integer i, String name, MESWorkCenterType workCenterType, RowData stringListHashMap, Map<String, MESWorkCenter> dbWorkCentersMap) {
        MESWorkCenter workCenter = new MESWorkCenter();
        String workCenterNumber = stringListHashMap.get("WorkCenter Number");
        String workCenterName = stringListHashMap.get("WorkCenter Name");
        String workCenterDescription = stringListHashMap.get("Description");
        String workCenterLocation = stringListHashMap.get("Location");
        String requiresMaintance = stringListHashMap.get("Requires Maintenance");
        String plantNumber = stringListHashMap.get("Plant Number");
        String assemblyLineNo = stringListHashMap.get("Assembly Line Number");

        if (workCenterNumber == null || workCenterNumber.trim().equals("")) {
            workCenterNumber = importer.getNextNumberWithoutUpdate(workCenterType.getAutoNumberSource().getName(), autoNumberMap);
        }

        workCenter.setType(workCenterType);
        workCenter.setNumber(workCenterNumber);
        workCenter.setName(workCenterName);
        workCenter.setDescription(workCenterDescription);
        workCenter.setLocation(workCenterLocation);
        importer.saveNextNumber(workCenterType.getAutoNumberSource().getName(), workCenter.getNumber(), autoNumberMap);
        if (requiresMaintance.equals("No")) {
            workCenter.setRequiresMaintenance(false);
        }
        if (plantNumber != null) {
            workCenter.setPlant(dbPlantsMap.get(plantNumber).getId());
        }
        if (assemblyLineNo != null) {
            workCenter.setAssemblyLine(dbAssemblyLineMap.get(assemblyLineNo).getId());
        }
        dbWorkCentersMap.put(workCenter.getNumber(), workCenter);
        return workCenter;
    }


    public void loadWorkCenterClassificationTree() {
        getDefaultNumberSource();
        workCenterTypesMapByPath = new ConcurrentHashMap<>();
        List<MESWorkCenterType> rootTypes = mesObjectTypeService.getWorkCenterTypeTree();
        for (MESWorkCenterType rootType : rootTypes) {
            rootWorkCenterTypesMap.put(rootType.getName(), rootType);
            workCenterTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    private MESWorkCenterType getWorkCenterTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESWorkCenterType itemType = rootWorkCenterTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootWorkCenterTypesMap.get(name);
        }
    }

    private MESWorkCenterType createWorkCenterTypeByPath(MESWorkCenterType parentType, String path) {
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
            parentType = rootWorkCenterTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESWorkCenterType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = mesObjectTypeService.createWorkCenter(parentType);
                rootWorkCenterTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createWorkCenterTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESWorkCenterType childItemType = new MESWorkCenterType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            parentType = mesObjectTypeService.createWorkCenter(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createWorkCenterTypeByPath(childItemType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createWorkCenterTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    public MESWorkCenterType getWorkCenterTypes(String path) {
        MESWorkCenterType mesWorkCenterType = workCenterTypesMapByPath.get(path);
        if (mesWorkCenterType == null) {
            mesWorkCenterType = getWorkCenterTypeByPath(path);
            if (mesWorkCenterType == null) {
                mesWorkCenterType = createWorkCenterTypeByPath(null, path);
            }
            workCenterTypesMapByPath.put(path, mesWorkCenterType);
        }

        return mesWorkCenterType;
    }


    private MESWorkCenter updateWorkCenter(Integer i, MESWorkCenter workCenter, RowData stringListHashMap) {
        String assemblyLineName = stringListHashMap.get("WorkCenter Name");
        String assemblyLineDescription = stringListHashMap.get("WorkCenter Description");
        workCenter.setName(assemblyLineName);
        workCenter.setDescription(assemblyLineDescription);
        return workCenter;
    }

}
