package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mes.MESOperation;
import com.cassinisys.plm.model.mes.MESOperationType;
import com.cassinisys.plm.repo.mes.MESOperationRepository;
import com.cassinisys.plm.repo.mes.OperationTypeRepository;
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
public class OperationImporter {
    public static ConcurrentMap<String, MESOperationType> rootOperationTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESOperationType> operationTypesMapByPath = new ConcurrentHashMap<>();
    private static AutoNumber autoNumber;
    Map<String, AutoNumber> autoNumberMap = new HashMap<>();
    @Autowired
    private MESOperationRepository operationReposotory;
    @Autowired
    private OperationTypeRepository operationTypeRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;

    private void getDefaultNumberSource() {
        autoNumber = plantsImporter.getDefaultPlantNumberSource("Default Operation Number Source");
    }

    public void importOperations(TableData tableData) throws ParseException {

        this.loadOperationClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESOperation> dbOperationMap = new LinkedHashMap();
        Map<String, MESOperationType> dbOperationTypeMap = new LinkedHashMap();
        List<MESOperation> operations = operationReposotory.findAll();
        List<MESOperationType> operationTypes = operationTypeRepository.findAll();
        dbOperationMap = operations.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbOperationTypeMap = operationTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESOperation> operations1 = createOperations(tableData, dbOperationMap, dbOperationTypeMap);
    }

    private List<MESOperation> createOperations(TableData tableData, Map<String, MESOperation> dbOperationsMap, Map<String, MESOperationType> dbOperationTypesMap) throws ParseException {
        List<MESOperation> operations2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Operation Name");
            if (name != null && !name.trim().equals("")) {
                String operationTypeName = stringListHashMap.get("Operation Type".trim());
                String operationTypePath = stringListHashMap.get("Type Path".trim());
                String number = stringListHashMap.get("Operation Number");
                if ((operationTypeName == null || operationTypeName == "")) {
                    MESOperationType operationType = dbOperationTypesMap.get(operationTypeName);
                    if (operationType != null) {
                        MESOperation operation = dbOperationsMap.get(number);
                        if (operation == null) {
                            operations2.add(createOperation(i, number, operationType, stringListHashMap, dbOperationsMap));
                        } else {
                            updateOperation(i, operation, stringListHashMap);
                            operations2.add(operation);
                        }
                    } else {
                        operations2.add(createOperationByPath(i, number, operationTypePath, dbOperationsMap, dbOperationTypesMap, stringListHashMap));
                    }
                } else {
                    operations2.add(createOperationByPath(i, number, operationTypePath, dbOperationsMap, dbOperationTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Operation Name for row :" + i);
            }
        }
        if (operations2.size() > 0) {
            operationReposotory.save(operations2);
        }
        return operations2;
    }


    private MESOperation createOperationByPath(int i, String number, String operationTypePath, Map<String, MESOperation> dbOperationsMap, Map<String, MESOperationType> dbOperationTypesMap, RowData stringListHashMap) {
        MESOperation Operation = null;
        if (operationTypePath != null && operationTypePath != "") {
            MESOperationType MESOperationType = this.getOperationTypes(operationTypePath);
            if (MESOperationType != null) {
                dbOperationTypesMap.put(MESOperationType.getName(), MESOperationType);
                Operation = dbOperationsMap.get(number);
                if (Operation == null)
                    Operation = createOperation(i, number, MESOperationType, stringListHashMap, dbOperationsMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Operation Type or Operation Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Operation Type or Operation Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Operation;
    }
    
    
    
    private MESOperation createOperation(Integer i, String name, MESOperationType operationType, RowData stringListHashMap, Map<String, MESOperation> dbOperationsMap) {
        MESOperation operation = new MESOperation();
        String operationNumber = stringListHashMap.get("Operation Number".trim());
        String operationName = stringListHashMap.get("Operation Name".trim());
        String operationDescription = stringListHashMap.get("Operation Description".trim());
        if (operationNumber == null || operationNumber.trim().equals("")) {
            operationNumber = importer.getNextNumberWithoutUpdate(operationType.getAutoNumberSource().getName(), autoNumberMap);
        }
        operation.setType(operationType);
        operation.setNumber(operationNumber);
        operation.setName(operationName);
        operation.setDescription(operationDescription);
        importer.saveNextNumber(operationType.getAutoNumberSource().getName(), operation.getNumber(), autoNumberMap);

//        operation = operationReposotory.save(operation);
        dbOperationsMap.put(operation.getNumber(), operation);

        return operation;
    }


    public void loadOperationClassificationTree() {
        operationTypesMapByPath = new ConcurrentHashMap<>();
        List<MESOperationType> rootTypes = mesObjectTypeService.getOperationTypeTree();
        for (MESOperationType rootType : rootTypes) {
            rootOperationTypesMap.put(rootType.getName(), rootType);
            operationTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public MESOperationType getOperationTypes(String path) {
        MESOperationType mesOperationType = operationTypesMapByPath.get(path);
        if (mesOperationType == null) {
            mesOperationType = getOperationTypeByPath(path);
            if (mesOperationType == null) {
                mesOperationType = createOperationTypeByPath(null, path);
            }
            operationTypesMapByPath.put(path, mesOperationType);
        }

        return mesOperationType;
    }

    private MESOperationType getOperationTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESOperationType itemType = rootOperationTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootOperationTypesMap.get(name);
        }
    }


    private MESOperationType createOperationTypeByPath(MESOperationType parentType, String path) {
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
            parentType = rootOperationTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESOperationType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = operationTypeRepository.save(parentType);
                rootOperationTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createOperationTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESOperationType childItemType = new MESOperationType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = operationTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createOperationTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createOperationTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private MESOperation updateOperation(Integer i, MESOperation mesOperation, RowData stringListHashMap) {
        String operationName = stringListHashMap.get("Operation Name".trim());
        String operationDescription = stringListHashMap.get("Operation Description".trim());

        mesOperation.setName(operationName);
        mesOperation.setDescription(operationDescription);

        return mesOperation;
    }


}
