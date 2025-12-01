package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.mro.MROWorkRequestType;
import com.cassinisys.plm.repo.mro.MROWorkRequestTypeRepository;
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
public class WorkRequestImporter {

    @Autowired
    private MROObjectTypeService mroObjectTypeService;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    public static ConcurrentMap<String, MROWorkRequestType> rootWorkRequestTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MROWorkRequestType> workRequestTypesMapByPath = new ConcurrentHashMap<>();

    private AutoNumber autoNumber;

    private void getDefaultNumberSource() {
        autoNumber = autoNumberService.getByName("Default Work Request Number Source");
    }

    public void loadWorkRequestClassificationTree() {
        getDefaultNumberSource();
        workRequestTypesMapByPath = new ConcurrentHashMap<>();
        List<MROWorkRequestType> rootTypes = mroObjectTypeService.getWorkRequestTypeTree();
        for (MROWorkRequestType rootType : rootTypes) {
            rootWorkRequestTypesMap.put(rootType.getName(), rootType);
            workRequestTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MROWorkRequestType getWorkRequestType(String path) {
        MROWorkRequestType mesWorkRequestType = workRequestTypesMapByPath.get(path);
        if (mesWorkRequestType == null) {
            mesWorkRequestType = getWorkRequestTypeByPath(path);
            if (mesWorkRequestType == null) {
                mesWorkRequestType = createWorkRequestTypeByPath(null, path);
            }
            workRequestTypesMapByPath.put(path, mesWorkRequestType);
        }

        return mesWorkRequestType;
    }

    private MROWorkRequestType getWorkRequestTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MROWorkRequestType itemType = rootWorkRequestTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootWorkRequestTypesMap.get(name);
        }
    }

    private MROWorkRequestType createWorkRequestTypeByPath(MROWorkRequestType parentType, String path) {
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
            parentType = rootWorkRequestTypesMap.get(name);
            if (parentType == null) {
                parentType = new MROWorkRequestType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = workRequestTypeRepository.save(parentType);
                rootWorkRequestTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createWorkRequestTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MROWorkRequestType childItemType = new MROWorkRequestType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = workRequestTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createWorkRequestTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createWorkRequestTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

}
