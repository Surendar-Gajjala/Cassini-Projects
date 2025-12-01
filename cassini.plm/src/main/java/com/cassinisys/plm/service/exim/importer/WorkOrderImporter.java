package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mro.MROWorkOrderType;
import com.cassinisys.plm.model.mro.WorkOrderType;
import com.cassinisys.plm.repo.mro.MROWorkOrderTypeRepository;
import com.cassinisys.plm.service.classification.MROObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Lenovo on 24-11-2021.
 */
@Service
@Scope("prototype")
public class WorkOrderImporter {

    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    private AutoNumber autoNumber;
    private AutoNumberService autoNumberService;

    public static ConcurrentMap<String, MROWorkOrderType> rootWorkOrderTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MROWorkOrderType> workOrderTypesMapByPath = new ConcurrentHashMap<>();

    private void getDefaultNumberSource() {
        autoNumber = autoNumberService.getByName("Default Work Order Number Source");
    }

    public void loadWorkOrderClassificationTree() {
        getDefaultNumberSource();
        workOrderTypesMapByPath = new ConcurrentHashMap<>();
        List<MROWorkOrderType> rootTypes = mroObjectTypeService.getWorkOrderTypeTree();
        for (MROWorkOrderType rootType : rootTypes) {
            rootWorkOrderTypesMap.put(rootType.getName(), rootType);
            workOrderTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MROWorkOrderType getWorkOrderType(String path) {
        MROWorkOrderType mesWorkOrderType = workOrderTypesMapByPath.get(path);
        if (mesWorkOrderType == null) {
            mesWorkOrderType = getWorkOrderTypeByPath(path);
            if (mesWorkOrderType == null) {
                mesWorkOrderType = createWorkOrderTypeByPath(null, path);
            }
            workOrderTypesMapByPath.put(path, mesWorkOrderType);
        }

        return mesWorkOrderType;
    }

    private MROWorkOrderType getWorkOrderTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROWorkOrderType itemType = rootWorkOrderTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootWorkOrderTypesMap.get(name);
        }
    }

    private MROWorkOrderType createWorkOrderTypeByPath(MROWorkOrderType parentType, String path) {
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
            parentType = rootWorkOrderTypesMap.get(name);
            if (parentType == null) {
                parentType = new MROWorkOrderType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = workOrderTypeRepository.save(parentType);
                rootWorkOrderTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createWorkOrderTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROWorkOrderType childItemType = new MROWorkOrderType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setType(parentType.getType());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = workOrderTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createWorkOrderTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createWorkOrderTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


}
