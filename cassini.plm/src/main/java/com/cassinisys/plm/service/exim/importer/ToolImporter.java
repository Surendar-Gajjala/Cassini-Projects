package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.MESTool;
import com.cassinisys.plm.model.mes.MESToolType;
import com.cassinisys.plm.model.mes.MESType;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetType;
import com.cassinisys.plm.repo.mes.MESToolRepository;
import com.cassinisys.plm.repo.mes.ToolTypeRepository;
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
 * Created by Lenovo on 12-11-2021.
 */
@Service
@Scope("prototype")
public class ToolImporter {

    @Autowired
    private MESToolRepository toolRepository;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private Importer importer;
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

    public static ConcurrentMap<String, MESToolType> rootToolTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESToolType> toolTypesMapByPath = new ConcurrentHashMap<>();
    private static AutoNumber toolAutoNumber;
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    private void getDefaultToolNumberSource() {
        toolAutoNumber = plantsImporter.getDefaultPlantNumberSource("Default Tool Number Source");
    }


    public void importTools(TableData tableData) throws ParseException {

        this.loadToolClassificationTree();
        plantsImporter.loadAssetClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESTool> dbToolMap = new ConcurrentHashMap<>();
        Map<String, MESToolType> dbToolTypeMap = new ConcurrentHashMap<>();
        List<MESTool> tools = toolRepository.findAll();
        List<MESToolType> toolTypes = toolTypeRepository.findAll();
        dbToolMap = tools.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        for (MESToolType toolType : toolTypes) {
            if (!dbToolTypeMap.containsKey(toolType.getName())) dbToolTypeMap.put(toolType.getName(), toolType);
        }
        Map<String, MROAssetType> dbAssetTypeMap = new ConcurrentHashMap<>();
        List<MROAssetType> assetTypes = assetTypeRepository.findAll();
        for (MROAssetType assetType : assetTypes) {
            if (!dbAssetTypeMap.containsKey(assetType.getName())) dbAssetTypeMap.put(assetType.getName(), assetType);
        }
        Map<String, MROAsset> dbAssetMap = new ConcurrentHashMap<>();
        List<MROAsset> assets = assetRepository.findAll();
        dbAssetMap = assets.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESTool> tools1 = createTools(tableData, dbToolMap, dbToolTypeMap);

        if (tools1.size() > 0) {
            List<AssetResourcesDto> resourcesDtoList = new ArrayList<>();
            for (MESTool tool : tools1) {
                AssetResourcesDto resourcesDto = new AssetResourcesDto();
                resourcesDto.setId(tool.getId());
                resourcesDto.setName(tool.getName());
                resourcesDto.setNumber(tool.getId());
                resourcesDto.setType(MESType.TOOLTYPE);
                resourcesDtoList.add(resourcesDto);
            }
            List<MROAsset> mroAsset = plantsImporter.createAssets(tableData, dbAssetMap, dbAssetTypeMap, resourcesDtoList);
            plantsImporter.createMeters(tableData, dbAssetMap);
        }
    }

    private List<MESTool> createTools(TableData tableData, Map<String, MESTool> dbToolsMap, Map<String, MESToolType> dbToolTypesMap) {
        List<MESTool> tools2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Tool Name");
            if (name != null && !name.trim().equals("")) {
                String toolTypeName = stringListHashMap.get("Tool Type");
                String typePath = stringListHashMap.get("Type Path");
                String number = stringListHashMap.get("Tool Number");
                if (toolTypeName != null && toolTypeName != "") {
                    MESToolType toolType = dbToolTypesMap.get(toolTypeName);
                    if (toolType != null) {
                        MESTool tool = dbToolsMap.get(number);
                        if (tool == null) {
                            tools2.add(createTool(i, number, toolType, stringListHashMap, dbToolsMap));
                        } else {
                            updateTool(i, tool, stringListHashMap);
                            tools2.add(tool);
                        }

                    } else {
                        tools2.add(createToolByPath(i, number, typePath, dbToolsMap, dbToolTypesMap, stringListHashMap));
                    }
                } else {
                    tools2.add(createToolByPath(i, number, typePath, dbToolsMap, dbToolTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Tool NAme for row :" + i);
            }
        }

        if (tools2.size() > 0) {
            toolRepository.save(tools2);
        }

        return tools2;
    }

    private MESTool createToolByPath(int i, String number, String typePath, Map<String, MESTool> dbToolsMap,
                                                     Map<String, MESToolType> dbToolTypesMap, RowData stringListHashMap) {
        MESTool tool = null;
        if (typePath != null && typePath != "") {
            MESToolType mesToolType = this.getToolTypes(typePath);
            if (mesToolType != null) {
                dbToolTypesMap.put(mesToolType.getName(), mesToolType);
                tool = dbToolsMap.get(number);
                if (tool == null)
                    tool = createTool(i, number, mesToolType, stringListHashMap, dbToolsMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide Tool Type or Tool Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide Tool Type or Tool Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return tool;
    }


    private MESTool createTool(Integer i, String name, MESToolType toolType, RowData stringListHashMap, Map<String, MESTool> dbToolsMap) {
        MESTool tool = new MESTool();
        String toolNumber = stringListHashMap.get("Tool Number".trim());
        String toolName = stringListHashMap.get("Tool Name".trim());
        String toolDescription = stringListHashMap.get("Tool Description".trim());
        String requiresMaintance = stringListHashMap.get("Requires Maintenance".trim());
        if (toolNumber == null || toolNumber.trim().equals("")) {
            toolNumber = importer.getNextNumberWithoutUpdate(toolType.getAutoNumberSource().getName(), autoNumberMap);
        }
        tool.setType(toolType);
        tool.setNumber(toolNumber);
        tool.setName(toolName);
        tool.setDescription(toolDescription);
        importer.saveNextNumber(toolType.getAutoNumberSource().getName(), tool.getNumber(), autoNumberMap);
        if (requiresMaintance.equals("No")) {
            tool.setRequiresMaintenance(false);
        }
        dbToolsMap.put(tool.getNumber(), tool);
        return tool;
    }


    public void loadToolClassificationTree() {
        getDefaultToolNumberSource();
        toolTypesMapByPath = new ConcurrentHashMap<>();
        List<MESToolType> rootTypes = mesObjectTypeService.getToolTypeTree();
        for (MESToolType rootType : rootTypes) {
            rootToolTypesMap.put(rootType.getName(), rootType);
            toolTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESToolType getToolTypes(String path) {
        MESToolType mesToolType = toolTypesMapByPath.get(path);
        if (mesToolType == null) {
            mesToolType = getToolTypeByPath(path);
            if (mesToolType == null) {
                mesToolType = createToolTypeByPath(null, path);
            }
            toolTypesMapByPath.put(path, mesToolType);
        }

        return mesToolType;
    }


    private MESToolType getToolTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESToolType itemType = rootToolTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootToolTypesMap.get(name);
        }
    }


    private MESToolType createToolTypeByPath(MESToolType parentType, String path) {
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
            parentType = rootToolTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESToolType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(toolAutoNumber);
                parentType = toolTypeRepository.save(parentType);
                rootToolTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createToolTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESToolType childItemType = new MESToolType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = toolTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createToolTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createToolTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESTool updateTool(Integer i, MESTool mesTool, RowData stringListHashMap) {
        String toolName = stringListHashMap.get("Tool Name".trim());
        String toolDescription = stringListHashMap.get("Tool Description".trim());

        mesTool.setName(toolName);
        mesTool.setDescription(toolDescription);

        return mesTool;
    }


}
