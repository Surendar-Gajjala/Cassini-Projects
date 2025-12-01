package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mes.MESAssemblyLine;
import com.cassinisys.plm.model.mes.MESAssemblyLineType;
import com.cassinisys.plm.model.mes.MESPlant;
import com.cassinisys.plm.repo.mes.AssemblyLineTypeRepository;
import com.cassinisys.plm.repo.mes.MESAssemblyLineRepository;
import com.cassinisys.plm.repo.mes.MESPlantRepository;
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
public class AssemblyLinesImporter {

    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;
    @Autowired
    private MESPlantRepository plantRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;
    private static AutoNumber autoNumber;

    private void getDefaultAssemblyLineNumberSource() {
        autoNumber = autoNumberService.getByName("Default AssemblyLine Number Source");
    }

    public static ConcurrentMap<String, MESAssemblyLineType> rootAssemblyLineTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESAssemblyLineType> assemblyLineTypesMapByPath = new ConcurrentHashMap<>();
    Map<String, MESPlant> dbPlantsMap = new LinkedHashMap();
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();

    public void importAssemblyLines(TableData tableData) throws ParseException {
        getDefaultAssemblyLineNumberSource();
        dbPlantsMap = new LinkedHashMap();
        autoNumberMap = new HashMap<>();
        this.loadAssemblyLineClassificationTree();
        List<MESPlant> plants = plantRepository.findAll();
        dbPlantsMap = plants.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        Map<String, MESAssemblyLine> dbAssemblyLineMap = new LinkedHashMap();
        Map<String, MESAssemblyLineType> dbAssemblyLineTypeMap = new LinkedHashMap();
        List<MESAssemblyLine> assemblyLines = assemblyLineRepository.findAll();
        List<MESAssemblyLineType> assemblyLineTypes = assemblyLineTypeRepository.findAll();
        dbAssemblyLineMap = assemblyLines.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbAssemblyLineTypeMap = assemblyLineTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESAssemblyLine> assemblyLines1 = createAssemblyLines(tableData, dbAssemblyLineMap, dbAssemblyLineTypeMap);

    }


    private List<MESAssemblyLine> createAssemblyLines(TableData tableData, Map<String, MESAssemblyLine> dbAssemblyLinesMap, Map<String, MESAssemblyLineType> dbAssemblyLineTypesMap) {
        List<MESAssemblyLine> assemblyLines2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Assembly Line Name");
            if (name != null && !name.trim().equals("")) {
                String assemblyTypeName = stringListHashMap.get("Assembly Line Type");
                String number = stringListHashMap.get("Assembly Line Number");
                String typePath = stringListHashMap.get("Type Path");
                if (assemblyTypeName != null && assemblyTypeName != "") {
                    MESAssemblyLineType assemblyLineType = dbAssemblyLineTypesMap.get(assemblyTypeName);
                    if (assemblyLineType != null) {
                        MESAssemblyLine assemblyLine = dbAssemblyLinesMap.get(number);
                        if (assemblyLine == null) {
                            assemblyLines2.add(createAssemblyLine(i, number, assemblyLineType, stringListHashMap, dbAssemblyLinesMap));
                        } else {
                            updateAssemblyLine(i, assemblyLine, stringListHashMap);
                            assemblyLines2.add(assemblyLine);

                        }

                    } else {
                        assemblyLines2.add(createAssemblyLineByPath(i, number, typePath, dbAssemblyLinesMap, dbAssemblyLineTypesMap, stringListHashMap));
                    }
                } else {
                    assemblyLines2.add(createAssemblyLineByPath(i, number, typePath, dbAssemblyLinesMap, dbAssemblyLineTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Assembly Line Name for row :" + i);
            }
        }

        if (assemblyLines2.size() > 0) {
            assemblyLineRepository.save(assemblyLines2);
        }

        return assemblyLines2;
    }

    private MESAssemblyLine createAssemblyLineByPath(int i, String number, String typePath, Map<String, MESAssemblyLine> dbAssemblyLinesMap,
                                                     Map<String, MESAssemblyLineType> dbAssemblyLineTypesMap, RowData stringListHashMap) {
        MESAssemblyLine assemblyLine = null;
        if (typePath != null && typePath != "") {
            MESAssemblyLineType mesAssemblyLineType = this.getAssemblyLineTypes(typePath);
            if (mesAssemblyLineType != null) {
                dbAssemblyLineTypesMap.put(mesAssemblyLineType.getName(), mesAssemblyLineType);
                assemblyLine = dbAssemblyLinesMap.get(number);
                if (assemblyLine == null)
                    assemblyLine = createAssemblyLine(i, number, mesAssemblyLineType, stringListHashMap, dbAssemblyLinesMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide Assembly Line Type or Assembly Line Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide Assembly Line Type or Assembly Line Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return assemblyLine;
    }

    private MESAssemblyLine createAssemblyLine(Integer i, String name, MESAssemblyLineType assemblyLineType, RowData stringListHashMap, Map<String, MESAssemblyLine> dbAssemblyLinesMap) {
        MESAssemblyLine assemblyLine = new MESAssemblyLine();
        String assemblyLineNumber = stringListHashMap.get("Assembly Line Number");
        String assemblyLineName = stringListHashMap.get("Assembly Line Name");
        String assemblyLineDescription = stringListHashMap.get("Assembly Line Description");
        String plantNumber = stringListHashMap.get("Plant Number");
        if (assemblyLineNumber == null || assemblyLineNumber.trim().equals("")) {
            assemblyLineNumber = importer.getNextNumberWithoutUpdate(assemblyLineType.getAutoNumberSource().getName(), autoNumberMap);
        }
        assemblyLine.setType(assemblyLineType);
        assemblyLine.setNumber(assemblyLineNumber);
        assemblyLine.setName(assemblyLineName);
        assemblyLine.setDescription(assemblyLineDescription);
        importer.saveNextNumber(assemblyLineType.getAutoNumberSource().getName(), assemblyLine.getNumber(), autoNumberMap);
        if (plantNumber != null) {
            MESPlant plant = dbPlantsMap.get(plantNumber);
            if (plant != null) {
                assemblyLine.setPlant(plant.getId());
            }
        }
        dbAssemblyLinesMap.put(assemblyLine.getNumber(), assemblyLine);
        return assemblyLine;
    }


    public void loadAssemblyLineClassificationTree() {
        assemblyLineTypesMapByPath = new ConcurrentHashMap<>();
        List<MESAssemblyLineType> rootTypes = mesObjectTypeService.getAssemblyLineTypeTree();
        for (MESAssemblyLineType rootType : rootTypes) {
            rootAssemblyLineTypesMap.put(rootType.getName(), rootType);
            assemblyLineTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MESAssemblyLineType getAssemblyLineTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESAssemblyLineType assemblyLineType = rootAssemblyLineTypesMap.get(name);
            if (assemblyLineType != null) {
                return assemblyLineType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return assemblyLineType;
            }
        } else {
            name = path;
            return rootAssemblyLineTypesMap.get(name);
        }
    }

    private MESAssemblyLineType createAssemblyLineTypeByPath(MESAssemblyLineType parentType, String path) {
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
            parentType = rootAssemblyLineTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESAssemblyLineType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = mesObjectTypeService.createAssemblyLineType(parentType);
                rootAssemblyLineTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createAssemblyLineTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESAssemblyLineType childItemType = new MESAssemblyLineType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            parentType = mesObjectTypeService.createAssemblyLineType(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createAssemblyLineTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createAssemblyLineTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    public MESAssemblyLineType getAssemblyLineTypes(String path) {
        MESAssemblyLineType mesAssemblyLineType = assemblyLineTypesMapByPath.get(path);
        if (mesAssemblyLineType == null) {
            mesAssemblyLineType = getAssemblyLineTypeByPath(path);
            if (mesAssemblyLineType == null) {
                mesAssemblyLineType = createAssemblyLineTypeByPath(null, path);
            }
            assemblyLineTypesMapByPath.put(path, mesAssemblyLineType);
        }

        return mesAssemblyLineType;
    }


    private MESAssemblyLine updateAssemblyLine(Integer i, MESAssemblyLine assemblyLine, RowData stringListHashMap) {
        String assemblyLineName = stringListHashMap.get("Assembly Line Name");
        String assemblyLineDescription = stringListHashMap.get("Assembly Line Description");
        assemblyLine.setName(assemblyLineName);
        assemblyLine.setDescription(assemblyLineDescription);
        return assemblyLine;
    }

}
