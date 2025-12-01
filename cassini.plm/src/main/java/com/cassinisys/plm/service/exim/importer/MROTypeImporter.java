package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.Measurement;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.plm.model.mro.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.mro.*;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Lenovo on 02-08-2021.
 */
@Service
public class MROTypeImporter {


    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private MROAssetTypeRepository assetTypeRepository;
    @Autowired
    private MROObjectTypeAttributeRepository mroObjectTypeAttributeRepository;
    @Autowired
    private MROMeterTypeRepository meterTypeRepository;
    @Autowired
    private MROSparePartTypeRepository sparePartTypeRepository;
    @Autowired
    private MROWorkRequestTypeRepository workRequestTypeRepository;
    @Autowired
    private MROWorkOrderTypeRepository workOrderTypeRepository;
    @Autowired
    private AssetImporter assetImporter;
    @Autowired
    private MeterImporter meterImporter;
    @Autowired
    private SparePartImporter sparePartImporter;
    @Autowired
    private WorkRequestImporter workRequestImporter;
    @Autowired
    private WorkOrderImporter workOrderImporter;

    private void initAutoWiredValues() {
        List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
        this.autoWireKeyMap = autoNumbers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }


    public void importMROTypeObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        initAutoWiredValues();
        List<Integer> integers = new ArrayList<>();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");
//        String path = paths.get(i);
//        String desc = this.importer.getValueFromMap(i, null, "Type Description", stringListHashMap);
        Integer index = path.lastIndexOf("/");
        if (objectType.toString().equals("ASSETTYPE")) {
            this.assetImporter.loadassetClassificationTree();
            if (typeName != null && typeName != "") {
                MROAssetType assetType = this.assetImporter.rootAssetTypesMap.get(typeName);
                if (assetType != null) {
                    assetType.setName(typeName);
                    assetType.setName(desc != null ? desc : typeName);
                    assetTypeRepository.save(assetType);
                    MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                    importMROObjectTypeAttributes(i, assetType.getObjectType(), objectTypeAttribute, assetType.getId(), stringListHashMap);
                } else {
                    if (path != null && path != "") {
                        MROAssetType assetTypeObj = this.assetImporter.getAssetTypes(path);
                        if (assetTypeObj != null) {
                            MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                            importMROObjectTypeAttributes(i, assetTypeObj.getObjectType(), objectTypeAttribute, assetTypeObj.getId(), stringListHashMap);
                            //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                        }
                    } else {
                        throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                    }
                }
            } else {
                if (path != null && path != "") {
                    MROAssetType assetTypeObj = this.assetImporter.getAssetTypes(path);
                    if (assetTypeObj != null) {
                        MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                        importMROObjectTypeAttributes(i, assetTypeObj.getObjectType(), objectTypeAttribute, assetTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                }
            }

        } else if (objectType.toString().equals("METERTYPE")) {
            this.meterImporter.loadMeterClassificationTree();
            if (typeName != null && typeName != "") {
                MROMeterType meterType = this.meterImporter.rootMeterTypesMap.get(typeName);
                if (meterType != null) {
                    meterType.setName(typeName);
                    meterType.setName(desc != null ? desc : typeName);
                    meterTypeRepository.save(meterType);
                    MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                    importMROObjectTypeAttributes(i, meterType.getObjectType(), objectTypeAttribute, meterType.getId(), stringListHashMap);
                } else {
                    if (path != null && path != "") {
                        MROMeterType meterTypeObj = this.meterImporter.getMeterType(path);
                        if (meterTypeObj != null) {
                            MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                            importMROObjectTypeAttributes(i, meterTypeObj.getObjectType(), objectTypeAttribute, meterTypeObj.getId(), stringListHashMap);
                            //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                        }
                    } else {
                        throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                    }
                }
            } else {
                if (path != null && path != "") {
                    MROMeterType meterTypeObj = this.meterImporter.getMeterType(path);
                    if (meterTypeObj != null) {
                        MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                        importMROObjectTypeAttributes(i, meterTypeObj.getObjectType(), objectTypeAttribute, meterTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");

                }
            }

        } else if (objectType.toString().equals("SPAREPARTTYPE")) {
            this.sparePartImporter.loadSparePartClassificationTree();
            if (typeName != null && typeName != "") {
                MROSparePartType sparePartType = this.sparePartImporter.rootSparePartTypesMap.get(typeName);
                if (sparePartType != null) {
                    sparePartType.setName(typeName);
                    sparePartType.setName(desc != null ? desc : typeName);
                    sparePartTypeRepository.save(sparePartType);
                    MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                    importMROObjectTypeAttributes(i, sparePartType.getObjectType(), objectTypeAttribute, sparePartType.getId(), stringListHashMap);
                } else {
                    if (path != null && path != "") {
                        MROSparePartType sparePartTypeObj = this.sparePartImporter.getSparePartType(path);
                        if (sparePartTypeObj != null) {
                            MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                            importMROObjectTypeAttributes(i, sparePartTypeObj.getObjectType(), objectTypeAttribute, sparePartTypeObj.getId(), stringListHashMap);
                            //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                        }
                    } else {
                        throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                    }
                }
            } else {
                if (path != null && path != "") {
                    MROSparePartType sparePartTypeObj = this.sparePartImporter.getSparePartType(path);
                    if (sparePartTypeObj != null) {
                        MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                        importMROObjectTypeAttributes(i, sparePartTypeObj.getObjectType(), objectTypeAttribute, sparePartTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                }
            }
        } else if (objectType.toString().equals("WORKREQUESTTYPE")) {
            this.workRequestImporter.loadWorkRequestClassificationTree();
            if (typeName != null && typeName != "") {
                MROWorkRequestType workRequestType = this.workRequestImporter.rootWorkRequestTypesMap.get(typeName);
                if (workRequestType != null) {
                    workRequestType.setName(typeName);
                    workRequestType.setName(desc != null ? desc : typeName);
                    workRequestTypeRepository.save(workRequestType);
                    MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                    importMROObjectTypeAttributes(i, workRequestType.getObjectType(), objectTypeAttribute, workRequestType.getId(), stringListHashMap);
                } else {
                    if (path != null && path != "") {
                        MROWorkRequestType workRequestTypeObj = this.workRequestImporter.getWorkRequestType(path);
                        if (workRequestTypeObj != null) {
                            MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                            importMROObjectTypeAttributes(i, workRequestTypeObj.getObjectType(), objectTypeAttribute, workRequestTypeObj.getId(), stringListHashMap);
                            //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                        }
                    } else {
                        throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                    }
                }
            } else {
                if (path != null && path != "") {
                    MROWorkRequestType workRequestTypeObj = this.workRequestImporter.getWorkRequestType(path);
                    if (workRequestTypeObj != null) {
                        MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                        importMROObjectTypeAttributes(i, workRequestTypeObj.getObjectType(), objectTypeAttribute, workRequestTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                }
            }
        } else if (objectType.toString().equals("WORKORDERTYPE")) {

            this.workOrderImporter.loadWorkOrderClassificationTree();
            if (typeName != null && typeName != "") {
                MROWorkOrderType workOrderType = this.workOrderImporter.rootWorkOrderTypesMap.get(typeName);
                if (workOrderType != null) {
                    workOrderType.setName(typeName);
                    workOrderType.setName(desc != null ? desc : typeName);
                    workOrderTypeRepository.save(workOrderType);
                    MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                    importMROObjectTypeAttributes(i, workOrderType.getObjectType(), objectTypeAttribute, workOrderType.getId(), stringListHashMap);
                } else {
                    if (path != null && path != "") {
                        MROWorkOrderType workOrderTypeObj = this.workOrderImporter.getWorkOrderType(path);
                        if (workOrderTypeObj != null) {
                            MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                            importMROObjectTypeAttributes(i, workOrderTypeObj.getObjectType(), objectTypeAttribute, workOrderTypeObj.getId(), stringListHashMap);
                            //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                        }
                    } else {
                        throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                    }
                }
            } else {
                if (path != null && path != "") {
                    MROWorkOrderType workOrderTypeObj = this.workOrderImporter.getWorkOrderType(path);
                    if (workOrderTypeObj != null) {
                        MROObjectTypeAttribute objectTypeAttribute = new MROObjectTypeAttribute();
                        importMROObjectTypeAttributes(i, workOrderTypeObj.getObjectType(), objectTypeAttribute, workOrderTypeObj.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:");
                }
            }
        }
    }

    /*
 * AssetType
 * */
    private MROAssetType setAssetParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MROAssetType assetType = assetTypeRepository.findByName(parent);
            if (assetType == null) {
                MROAssetType parentType = assetTypeRepository.findByName(path3);
                if (parentType != null) {
                    MROAssetType assetType1 = createAssetType(parent, parentType, null, 0, objectType, numberSource);
                    return assetType1;
                } else {
                    parentType = setAssetParent(path3, objectType, numberSource);
                    MROAssetType assetType1 = createAssetType(parent, parentType, null, 0, objectType, numberSource);
                    return assetType1;
                }
            } else {
                return assetType;
            }

        } else {
            MROAssetType parentType = assetTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createAssetParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MROAssetType createAssetType(String name, MROAssetType parent, String desc, int i, Enum objectType, String numberSource) {
        MROAssetType newAssetType = new MROAssetType();
        newAssetType.setName(name);
        newAssetType.setDescription(desc);
        newAssetType.setObjectType(PLMObjectType.ASSETTYPE);
        newAssetType.setAutoNumberSource(this.autoWireKeyMap.get("Default Asset Number Source"));

        if (parent != null)
            newAssetType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_assert_type_for_row_number" + (i + 1),
                    null, "Please provide assert type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MROAssetType assetType = assetTypeRepository.save(newAssetType);
        return assetType;
    }

    private MROAssetType createAssetParentType(String name, Enum objectType) {
        MROAssetType parent = null;
        if (name != null) {
            parent = assetTypeRepository.findByName(name);
        }
        MROAssetType newAssetType = new MROAssetType();
        newAssetType.setName(name);
        newAssetType.setDescription(parent.getDescription());
        newAssetType.setObjectType(objectType);
        newAssetType.setAutoNumberSource(parent.getAutoNumberSource());
        return assetTypeRepository.save(newAssetType);
    }


    /*
* MeterType
* */
    private MROMeterType setMeterParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MROMeterType meterType = meterTypeRepository.findByName(parent);
            if (meterType == null) {
                MROMeterType parentType = meterTypeRepository.findByName(path3);
                if (parentType != null) {
                    MROMeterType meterType1 = createMeterType(parent, parentType, null, 0, objectType, numberSource);
                    return meterType1;
                } else {
                    parentType = setMeterParent(path3, objectType, numberSource);
                    MROMeterType meterType1 = createMeterType(parent, parentType, null, 0, objectType, numberSource);
                    return meterType1;
                }
            } else {
                return meterType;
            }

        } else {
            MROMeterType parentType = meterTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMeterParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MROMeterType createMeterType(String name, MROMeterType parent, String desc, int i, Enum objectType, String numberSource) {
        MROMeterType newMeterType = new MROMeterType();
        newMeterType.setName(name);
        newMeterType.setDescription(desc);
        newMeterType.setObjectType(PLMObjectType.METERTYPE);
        newMeterType.setAutoNumberSource(this.autoWireKeyMap.get("Default Meter Number Source"));

        if (parent != null)
            newMeterType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_meter_type_for_row_number" + (i + 1),
                    null, "Please provide meter type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MROMeterType meterType = meterTypeRepository.save(newMeterType);
        return meterType;
    }

    private MROMeterType createMeterParentType(String name, Enum objectType) {
        MROMeterType parent = null;
        if (name != null) {
            parent = meterTypeRepository.findByName(name);
        }
        MROMeterType newMeterType = new MROMeterType();
        newMeterType.setName(name);
        newMeterType.setDescription(parent.getDescription());
        newMeterType.setObjectType(objectType);
        newMeterType.setAutoNumberSource(parent.getAutoNumberSource());
        return meterTypeRepository.save(newMeterType);
    }


    /*
* SparePartType
* */
    private MROSparePartType setSparePartParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MROSparePartType sparePartType = sparePartTypeRepository.findByName(parent);
            if (sparePartType == null) {
                MROSparePartType parentType = sparePartTypeRepository.findByName(path3);
                if (parentType != null) {
                    MROSparePartType sparePartType1 = createSparePartType(parent, parentType, null, 0, objectType, numberSource);
                    return sparePartType1;
                } else {
                    parentType = setSparePartParent(path3, objectType, numberSource);
                    MROSparePartType sparePartType1 = createSparePartType(parent, parentType, null, 0, objectType, numberSource);
                    return sparePartType1;
                }
            } else {
                return sparePartType;
            }

        } else {
            MROSparePartType parentType = sparePartTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createSparePartParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MROSparePartType createSparePartType(String name, MROSparePartType parent, String desc, int i, Enum objectType, String numberSource) {
        MROSparePartType newSparePartType = new MROSparePartType();
        newSparePartType.setName(name);
        newSparePartType.setDescription(desc);
        newSparePartType.setObjectType(PLMObjectType.SPAREPARTTYPE);
        newSparePartType.setAutoNumberSource(this.autoWireKeyMap.get("Default Spare Part Number Source"));

        if (parent != null)
            newSparePartType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_sparePart_type_for_row_number" + (i + 1),
                    null, "Please provide sparePart type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MROSparePartType sparePartType = sparePartTypeRepository.save(newSparePartType);
        return sparePartType;
    }

    private MROSparePartType createSparePartParentType(String name, Enum objectType) {
        MROSparePartType parent = null;
        if (name != null) {
            parent = sparePartTypeRepository.findByName(name);
        }
        MROSparePartType newSparePartType = new MROSparePartType();
        newSparePartType.setName(name);
        newSparePartType.setDescription(parent.getDescription());
        newSparePartType.setObjectType(objectType);
        newSparePartType.setAutoNumberSource(parent.getAutoNumberSource());
        return sparePartTypeRepository.save(newSparePartType);
    }


    /*
* WorkRequestType
* */
    private MROWorkRequestType setWorkRequestParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MROWorkRequestType workRequestType = workRequestTypeRepository.findByName(parent);
            if (workRequestType == null) {
                MROWorkRequestType parentType = workRequestTypeRepository.findByName(path3);
                if (parentType != null) {
                    MROWorkRequestType workRequestType1 = createWorkRequestType(parent, parentType, null, 0, objectType, numberSource);
                    return workRequestType1;
                } else {
                    parentType = setWorkRequestParent(path3, objectType, numberSource);
                    MROWorkRequestType workRequestType1 = createWorkRequestType(parent, parentType, null, 0, objectType, numberSource);
                    return workRequestType1;
                }
            } else {
                return workRequestType;
            }

        } else {
            MROWorkRequestType parentType = workRequestTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createWorkRequestParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MROWorkRequestType createWorkRequestType(String name, MROWorkRequestType parent, String desc, int i, Enum objectType, String numberSource) {
        MROWorkRequestType newWorkRequestType = new MROWorkRequestType();
        newWorkRequestType.setName(name);
        newWorkRequestType.setDescription(desc);
        newWorkRequestType.setObjectType(PLMObjectType.WORKREQUESTTYPE);
        newWorkRequestType.setAutoNumberSource(this.autoWireKeyMap.get("Default Work Request Number Source"));

        if (parent != null)
            newWorkRequestType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_workRequest_type_for_row_number" + (i + 1),
                    null, "Please provide workRequest type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MROWorkRequestType workRequestType = workRequestTypeRepository.save(newWorkRequestType);
        return workRequestType;
    }

    private MROWorkRequestType createWorkRequestParentType(String name, Enum objectType) {
        MROWorkRequestType parent = null;
        if (name != null) {
            parent = workRequestTypeRepository.findByName(name);
        }
        MROWorkRequestType newWorkRequestType = new MROWorkRequestType();
        newWorkRequestType.setName(name);
        newWorkRequestType.setDescription(parent.getDescription());
        newWorkRequestType.setObjectType(objectType);
        newWorkRequestType.setAutoNumberSource(parent.getAutoNumberSource());
        return workRequestTypeRepository.save(newWorkRequestType);
    }

    /*
* WorkOrderType
* */
    private MROWorkOrderType setWorkOrderParent(String path, Enum objectType, String numberSource) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            MROWorkOrderType workOrderType = workOrderTypeRepository.findByName(parent);
            if (workOrderType == null) {
                MROWorkOrderType parentType = workOrderTypeRepository.findByName(path3);
                if (parentType != null) {
                    MROWorkOrderType workOrderType1 = createWorkOrderType(parent, parentType, null, 0, objectType, numberSource);
                    return workOrderType1;
                } else {
                    parentType = setWorkOrderParent(path3, objectType, numberSource);
                    MROWorkOrderType workOrderType1 = createWorkOrderType(parent, parentType, null, 0, objectType, numberSource);
                    return workOrderType1;
                }
            } else {
                return workOrderType;
            }

        } else {
            MROWorkOrderType parentType = workOrderTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createWorkOrderParentType(path, objectType);
                return parentType;
            }
        }

    }

    private MROWorkOrderType createWorkOrderType(String name, MROWorkOrderType parent, String desc, int i, Enum objectType, String numberSource) {
        MROWorkOrderType newWorkOrderType = new MROWorkOrderType();
        newWorkOrderType.setName(name);
        newWorkOrderType.setDescription(desc);
        newWorkOrderType.setObjectType(objectType);
        newWorkOrderType.setType(parent.getType());
        newWorkOrderType.setAutoNumberSource(this.autoWireKeyMap.get("Default Work Order Number Source"));

        if (parent != null)
            newWorkOrderType.setParentType(parent.getId());
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_workOrder_type_for_row_number" + (i + 1),
                    null, "Please provide workOrder type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        MROWorkOrderType workOrderType = workOrderTypeRepository.save(newWorkOrderType);
        return workOrderType;
    }

    private MROWorkOrderType createWorkOrderParentType(String name, Enum objectType) {
        MROWorkOrderType parent = null;
        if (name != null) {
            parent = workOrderTypeRepository.findByName(name);
        }
        MROWorkOrderType newWorkOrderType = new MROWorkOrderType();
        newWorkOrderType.setName(name);
        newWorkOrderType.setDescription(parent.getDescription());
        newWorkOrderType.setObjectType(objectType);
        newWorkOrderType.setAutoNumberSource(parent.getAutoNumberSource());
        return workOrderTypeRepository.save(newWorkOrderType);
    }


    private Boolean checkMROObjectTypeAttributeName(Enum objectType, Integer Type, String name) {
        Boolean isExist = false;
        if (objectType.toString().equals("ASSETTYPE")) {
            MROAssetType assetType = assetTypeRepository.findOne(Type);
            MROObjectTypeAttribute mroObjectTypeAttribute = null;
            if (assetType != null) {
                mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findByTypeAndName(assetType.getId(), name);
                if (mroObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("METERTYPE")) {
            MROMeterType meterType = meterTypeRepository.findOne(Type);
            MROObjectTypeAttribute mroObjectTypeAttribute = null;
            if (meterType != null) {
                mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findByTypeAndName(meterType.getId(), name);
                if (mroObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("SPAREPARTTYPE")) {
            MROSparePartType sparePartType = sparePartTypeRepository.findOne(Type);
            MROObjectTypeAttribute mroObjectTypeAttribute = null;
            if (sparePartType != null) {
                mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findByTypeAndName(sparePartType.getId(), name);
                if (mroObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("WORKREQUESTTYPE")) {
            MROWorkRequestType workRequestType = workRequestTypeRepository.findOne(Type);
            MROObjectTypeAttribute mroObjectTypeAttribute = null;
            if (workRequestType != null) {
                mroObjectTypeAttribute = mroObjectTypeAttributeRepository.findByTypeAndName(workRequestType.getId(), name);
                if (mroObjectTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        }

        return isExist;
    }


    public void importMROObjectTypeAttributes(Integer value, Enum objectType, MROObjectTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name") && stringListHashMap.containsKey("Data Type")) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            MROObjectTypeAttribute mroObjectTypeAttribute = null;
            if (name != null && name != "") {
                isExist = checkMROObjectTypeAttributeName(objectType, objectId, name);
            }

            if (!isExist) {
                String dataType = stringListHashMap.get("Data Type".trim());
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length".trim());
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern".trim());
                        JSONArray array = this.itemTypeImporter.getTextValidations(min, max, cap, small, mixCase, pattern);
                        attribute.setValidations(array.toString());

                    }
                    if (dataType.equals("LONGTEXT")) {
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getLongTextValidations(max, cap, small, mixCase);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("INTEGER")) {
                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getIntegerValidations(min, max, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("DATE")) {
                        String fromDate = stringListHashMap.get("From date".trim());
                        String toDate = stringListHashMap.get("To date".trim());
                        String format = stringListHashMap.get("Date format".trim());
                        JSONArray array = this.itemTypeImporter.getDateValidations(fromDate, toDate, format);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("LIST")) {
                        String lvName = stringListHashMap.get("List Name".trim());
                        if (lvName != null && lvName != null) {
                            Lov lov = lovRepository.findByName(lvName);
                            if (lov == null) {
                                Lov newObj = new Lov();
                                String commasepLovs = stringListHashMap.get("List of Values (Comma separated)".trim());
                                if (commasepLovs != null && commasepLovs != "") {
                                    List<String> lovs = Arrays.asList(commasepLovs.split(","));
                                    String[] arr = new String[lovs.size()];
                                    arr = lovs.toArray(arr);
                                    newObj.setName(lvName);
                                    newObj.setDescription("");
                                    newObj.setDefaultValue("");
                                    newObj.setValues(arr);
                                    Lov lovObj = lovRepository.save(newObj);
                                    attribute.setLov(lovObj);
                                }

                            } else {
                                attribute.setLov(lov);
                            }

                        }
                        attribute.setListMultiple(this.itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)".trim(), stringListHashMap));

                    }
                    if (dataType.equals("TIME")) {
                        String fromTime = stringListHashMap.get("From Time".trim());
                        String toTime = stringListHashMap.get("To Time".trim());
                        String format = stringListHashMap.get("Time Format".trim());
                        JSONArray array = this.itemTypeImporter.getTimeValidations(fromTime, toTime, format);
                        attribute.setValidations(array.toString());
                    }

                    if (dataType.equals("CURRENCY")) {
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getCurrencyValidations(dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("DOUBLE")) {
                        String qom = stringListHashMap.get("QoM".trim());
                        if (qom != null && qom != null) {
                            Measurement measurement = measurementRepository.findByName(qom);
                            if (measurement != null) {
                                attribute.setMeasurement(measurement);
                            }

                        }

                        String min = stringListHashMap.get("Minimum Value".trim());
                        String max = stringListHashMap.get("Maximum Value".trim());
                        String dis = stringListHashMap.get("No of decimals to display".trim());
                        String enter = stringListHashMap.get("No of decimals to enter".trim());
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)".trim(), stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)".trim(), stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)".trim(), stringListHashMap);
                        JSONArray array = this.itemTypeImporter.getDoubleValidations(min, max, dis, enter, positiv, negative, both);
                        attribute.setValidations(array.toString());
                    }
                    if (dataType.equals("OBJECT")) {
                        String objName = stringListHashMap.get("Object Name".trim());
                        attribute.setRefType(PLMObjectType.valueOf(objName));
                    }
                    if (dataType.equals("FORMULA")) {
                        String formula = stringListHashMap.get("Enter Formula".trim());
                        attribute.setFormula(formula);
                    }


                    String desc = stringListHashMap.get("Attribute Description".trim());
                    String attrGrp = stringListHashMap.get("Attribute Group".trim());
                    String defaultValue = stringListHashMap.get("Default value".trim());
                    attribute.setAttributeGroup(attrGrp);
                    attribute.setName(name);
                    attribute.setType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);

                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    mroObjectTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


}
