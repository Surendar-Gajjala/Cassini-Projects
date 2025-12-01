package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mro.MROSparePart;
import com.cassinisys.plm.model.mro.MROSparePartType;
import com.cassinisys.plm.repo.mro.MROSparePartRepository;
import com.cassinisys.plm.repo.mro.MROSparePartTypeRepository;
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
public class SparePartImporter {

    @Autowired
    private MROSparePartRepository sparePartRepository;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;

    public static ConcurrentMap<String, MROSparePartType> rootSparePartTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MROSparePartType> sparePartTypesMapByPath = new ConcurrentHashMap<>();

    Map<String, AutoNumber> autoNumberMap = new HashMap<>();
    
    public void loadSparePartClassificationTree() {
        rootSparePartTypesMap = new ConcurrentHashMap<>();
        sparePartTypesMapByPath = new ConcurrentHashMap<>();
        List<MROSparePartType> rootTypes = mroObjectTypeService.getSparePartTypeTree();
        for (MROSparePartType rootType : rootTypes) {
            rootSparePartTypesMap.put(rootType.getName(), rootType);
            sparePartTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    private MROSparePartType getSparePartTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROSparePartType sparePartType = rootSparePartTypesMap.get(name);
            if (sparePartType != null) {
                sparePartType = sparePartType.getChildTypeByPath(path.substring(index + 1));
                return sparePartType;
            } else {
                return sparePartType;
            }

        } else {
            name = path;
            return rootSparePartTypesMap.get(name);
        }
    }

    public MROSparePartType getSparePartType(String typePath) {
        MROSparePartType sparePartType = sparePartTypesMapByPath.get(typePath);
        if (sparePartType == null) {
            sparePartType = getSparePartTypeByPath(typePath);
            if (sparePartType == null) {
                sparePartType = createSparePartTypeByPath(null, typePath);
            }

            sparePartTypesMapByPath.put(typePath, sparePartType);
        }
        return sparePartType;
    }

    private AutoNumber getDefaultSparePartNumberSource() {
        return autoNumberService.getByName("Default Spare Part Number Source");
    }


    private MROSparePartType createSparePartTypeByPath(MROSparePartType parentType, String path) {
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
            parentType = rootSparePartTypesMap.get(name);
            if (parentType == null) {
                parentType = new MROSparePartType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultSparePartNumberSource());
                parentType = mroObjectTypeService.createSparePartType(parentType);
                rootSparePartTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createSparePartTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROSparePartType childSparePartType = new MROSparePartType();
            childSparePartType.setParentType(parentType.getId());
            childSparePartType.setName(name);
            childSparePartType.setDescription(name);
            childSparePartType.setAutoNumberSource(getDefaultSparePartNumberSource());
            parentType = mroObjectTypeService.createSparePartType(childSparePartType);
            parentType.getChildren().add(childSparePartType);
            if (restOfPath != null) {
                return parentType = createSparePartTypeByPath(childSparePartType, restOfPath);
            } else {
                return childSparePartType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createSparePartTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    public void importSpareParts(TableData tableData) throws ParseException {
        this.loadSparePartClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MROSparePart> dbSparePartMap = new LinkedHashMap();
        Map<String, MROSparePartType> dbSparePartTypeMap = new LinkedHashMap();
        List<MROSparePart> sparePart = sparePartRepository.findAll();
        List<MROSparePartType> sparePartTypes = sparePartTypeRepository.findAll();
        dbSparePartMap = sparePart.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbSparePartTypeMap = sparePartTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MROSparePart> sparePart1 = createSpareParts(tableData, dbSparePartMap, dbSparePartTypeMap);

    }

    private List<MROSparePart> createSpareParts(TableData tableData, Map<String, MROSparePart> dbSparePartMap, Map<String, MROSparePartType> dbSparePartTypeMap) {
        List<MROSparePart> spareParts2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Spare Part Name");
            if (name != null && !name.trim().equals("")) {
                String jigsTypeName = stringListHashMap.get("Spare Part Type");
                String typePath = stringListHashMap.get("Type Path");
                String number = stringListHashMap.get("Spare Part Number");
                if (jigsTypeName != null && jigsTypeName != "") {
                    MROSparePartType sparePartType = dbSparePartTypeMap.get(jigsTypeName);
                    if (sparePartType != null) {
                        MROSparePart sparePart = dbSparePartMap.get(number);
                        if (sparePart == null) {
                            spareParts2.add(createSparePart(i, number, sparePartType, stringListHashMap, dbSparePartMap));
                        } else {
                            updateSparePart(i, sparePart, stringListHashMap);
                            spareParts2.add(sparePart);
                        }

                    } else {
                        spareParts2.add(createSparePartByPath(i, number, typePath, dbSparePartMap, dbSparePartTypeMap, stringListHashMap));
                    }
                } else {
                    spareParts2.add(createSparePartByPath(i, number, typePath, dbSparePartMap, dbSparePartTypeMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide SparePart Name for row :" + i);
            }
        }

        if (spareParts2.size() > 0) {
            sparePartRepository.save(spareParts2);
        }

        return spareParts2;
    }

    private MROSparePart createSparePartByPath(int i, String number, String typePath, Map<String, MROSparePart> dbSparePartMap,
                                       Map<String, MROSparePartType> dbSparePartTypeMap, RowData stringListHashMap) {
        MROSparePart sparePart = null;
        if (typePath != null && typePath != "") {
            MROSparePartType mesSparePartType = this.getSparePartType(typePath);
            if (mesSparePartType != null) {
                dbSparePartTypeMap.put(mesSparePartType.getName(), mesSparePartType);
                sparePart = dbSparePartMap.get(number);
                if (sparePart == null)
                    sparePart = createSparePart(i, number, mesSparePartType, stringListHashMap, dbSparePartMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                        null, "Please provide SparePart Type or SparePart Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_assembly_line_type_for_row_number" + (i),
                    null, "Please provide SparePart Type or SparePart Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return sparePart;
    }



    private MROSparePart createSparePart(Integer i, String name, MROSparePartType sparePartType, RowData stringListHashMap, Map<String, MROSparePart> dbSparePartsMap) {
        MROSparePart sparePart = new MROSparePart();
        String sparePartNumber = stringListHashMap.get("Spare Part Number".trim());
        String sparePartName = stringListHashMap.get("Spare Part Name".trim());
        String sparePartDescription = stringListHashMap.get("Spare Part Description".trim());
        if (sparePartNumber == null || sparePartNumber.trim().equals("")) {
            sparePartNumber = importer.getNextNumberWithoutUpdate(sparePartType.getAutoNumberSource().getName(), autoNumberMap);
        }
        sparePart.setType(sparePartType);
        sparePart.setNumber(sparePartNumber);
        sparePart.setName(sparePartName);
        sparePart.setDescription(sparePartDescription);
        importer.saveNextNumber(sparePartType.getAutoNumberSource().getName(), sparePart.getNumber(), autoNumberMap);
//      sparePart = sparePartRepository.save(sparePart);
        dbSparePartsMap.put(sparePart.getNumber(), sparePart);

        return sparePart;
    }


    private MROSparePart updateSparePart(Integer i, MROSparePart mesSparePart, RowData stringListHashMap) {
        String jigsFixtureName = stringListHashMap.get("Spare Part Name".trim());
        String jigsFixtureDescription = stringListHashMap.get("Spare Part Description".trim());

        mesSparePart.setName(jigsFixtureName);
        mesSparePart.setDescription(jigsFixtureDescription);

        return mesSparePart;
    }
    
}
