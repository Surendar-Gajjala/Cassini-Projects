package com.cassinisys.is.service.procm;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.repo.procm.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reddy on 6/13/17.
 */
@Service
@Transactional
public class ClassificationService {
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MaterialTypeService materialTypeService;
    @Autowired
    private ManpowerTypeService manpowerTypeService;
    @Autowired
    private MaterialReceiveTypeService materialReceiveTypeService;
    @Autowired
    private MaterialIssueTypeService materialIssueTypeService;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private MachineTypeRepository machineTypeRepository;
    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;
    @Autowired
    private MaterialReceiveTypeRepository materialReceiveTypeRepository;
    @Autowired
    private MaterialIssueTypeRepository materialIssueTypeRepository;

    private Map<ISObjectType, ClassificationTypeService> mapServices = new HashMap<>();
    private Map<ISObjectType, ClassificationObjectConverter> mapConverters = new HashMap<>();

    private ClassificationObjectConverter<ISMaterialType, ISMaterialTypeAttribute> materialTypeConvertor = new ClassificationObjectConverter<>(ISMaterialType.class, ISMaterialTypeAttribute.class);
    private ClassificationObjectConverter<ISMachineType, ISMachineTypeAttribute> machineTypeConvertor = new ClassificationObjectConverter<>(ISMachineType.class, ISMachineTypeAttribute.class);
    private ClassificationObjectConverter<ISManpowerType, ISManpowerTypeAttribute> manpowerTypeConvertor = new ClassificationObjectConverter<>(ISManpowerType.class, ISManpowerTypeAttribute.class);
    private ClassificationObjectConverter<ISMaterialReceiveType, ISMaterialReceiveTypeAttribute> receiveTypeConvertor = new ClassificationObjectConverter<>(ISMaterialReceiveType.class, ISMaterialReceiveTypeAttribute.class);
    private ClassificationObjectConverter<ISMaterialIssueType, ISMaterialIssueTypeAttribute> issueTypeConvertor = new ClassificationObjectConverter<>(ISMaterialIssueType.class, ISMaterialIssueTypeAttribute.class);

    @PostConstruct
    public void initMap() {
        mapServices.put(ISObjectType.MACHINETYPE, machineTypeService);
        mapServices.put(ISObjectType.MATERIALTYPE, materialTypeService);
        mapServices.put(ISObjectType.MANPOWERTYPE, manpowerTypeService);
        mapServices.put(ISObjectType.MATERIALRECEIVETYPE, materialReceiveTypeService);
        mapServices.put(ISObjectType.MATERIALISSUETYPE, materialIssueTypeService);
        mapConverters.put(ISObjectType.MACHINETYPE, machineTypeConvertor);
        mapConverters.put(ISObjectType.MATERIALTYPE, materialTypeConvertor);
        mapConverters.put(ISObjectType.MANPOWERTYPE, manpowerTypeConvertor);
        mapConverters.put(ISObjectType.MATERIALRECEIVETYPE, receiveTypeConvertor);
        mapConverters.put(ISObjectType.MATERIALISSUETYPE, issueTypeConvertor);

    }

    @Transactional(readOnly = false)
    public Object createType(ISObjectType type, ObjectNode json) {
        return validate(type, json, "create");
    }

    @Transactional(readOnly = false)
    public Object updateType(ISObjectType type, ObjectNode json) {
        return validate(type, json, "update");
    }

    public Object validate(ISObjectType type, ObjectNode json, String cretaionType) {
        List<ISMaterialType> isMaterialTypes = new ArrayList<>();
        List<ISMachineType> isMachineTypes = new ArrayList<>();
        List<ISManpowerType> isManpowerTypes = new ArrayList<>();
        List<ISMaterialReceiveType> receiveTypes = new ArrayList<>();
        List<ISMaterialIssueType> isIssueTypes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        if (type == ISObjectType.MATERIALTYPE) {
            ISMaterialType itemType = mapper.convertValue(json, ISMaterialType.class);
            if (itemType.getParentType() == null) {
                isMaterialTypes = materialTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(itemType.getName());
            } else {
                isMaterialTypes = materialTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(itemType.getParentType(), itemType.getName());
            }
            if (isMaterialTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((isMaterialTypes.size() == 1 && isMaterialTypes.get(0).getId().equals(itemType.getId())) || isMaterialTypes.size() == 0) {
                if (cretaionType.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        if (type == ISObjectType.MACHINETYPE) {
            ISMachineType machineType = mapper.convertValue(json, ISMachineType.class);
            if (machineType.getParentType() == null) {
                isMachineTypes = machineTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(machineType.getName());
            } else {
                isMachineTypes = machineTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(machineType.getParentType(), machineType.getName());
            }
            if (isMachineTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((isMachineTypes.size() == 1 && isMachineTypes.get(0).getId().equals(machineType.getId())) || isMachineTypes.size() == 0) {
                if (cretaionType.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        if (type == ISObjectType.MANPOWERTYPE) {
            ISManpowerType manpowerType = mapper.convertValue(json, ISManpowerType.class);
            if (manpowerType.getParentType() == null) {
                isManpowerTypes = manpowerTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(manpowerType.getName());
            } else {
                isManpowerTypes = manpowerTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(manpowerType.getParentType(), manpowerType.getName());
            }
            if (isManpowerTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((isManpowerTypes.size() == 1 && isManpowerTypes.get(0).getId().equals(manpowerType.getId())) || isManpowerTypes.size() == 0) {
                if (cretaionType.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        if (type == ISObjectType.MATERIALRECEIVETYPE) {
            ISMaterialReceiveType itemType = mapper.convertValue(json, ISMaterialReceiveType.class);
            if (itemType.getParentType() == null) {
                receiveTypes = materialReceiveTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(itemType.getName());
            } else {
                receiveTypes = materialReceiveTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(itemType.getParentType(), itemType.getName());
            }
            if (receiveTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((receiveTypes.size() == 1 && receiveTypes.get(0).getId().equals(itemType.getId())) || receiveTypes.size() == 0) {
                if (cretaionType.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        if (type == ISObjectType.MATERIALISSUETYPE) {
            ISMaterialIssueType itemType = mapper.convertValue(json, ISMaterialIssueType.class);
            if (itemType.getParentType() == null) {
                isIssueTypes = materialIssueTypeRepository.findByParentTypeIsNullAndNameEqualsIgnoreCase(itemType.getName());
            } else {
                isIssueTypes = materialIssueTypeRepository.findByParentTypeAndNameEqualsIgnoreCase(itemType.getParentType(), itemType.getName());
            }
            if (isIssueTypes.size() > 1) {
                throw new RuntimeException("Duplicate name");
            } else if ((isIssueTypes.size() == 1 && isIssueTypes.get(0).getId().equals(itemType.getId())) || isIssueTypes.size() == 0) {
                if (cretaionType.equalsIgnoreCase("create")) {
                    return mapServices.get(type).create(mapConverters.get(type).convertToType(json));
                } else {
                    return mapServices.get(type).update(mapConverters.get(type).convertToType(json));
                }
            } else {
                throw new RuntimeException("Duplicate name");
            }
        }
        return null;
    }

    @Transactional(readOnly = false)
    public void deleteType(ISObjectType type, Integer id) {
        mapServices.get(type).delete(id);
    }

    @Transactional(readOnly = true)
    public Object getType(ISObjectType type, Integer id) {
        return mapServices.get(type).get(id);
    }

    @Transactional(readOnly = true)
    public List<Object> getAllTypes(ISObjectType type) {
        return mapServices.get(type).getAll();
    }

    @Transactional(readOnly = true)
    public List<Object> getChildren(ISObjectType type, Integer id) {
        return mapServices.get(type).getChildren(id);
    }

    @Transactional(readOnly = true)
    public List<Object> getClassificationTree(ISObjectType objectType) {
        List<Object> classification = new ArrayList<>();
        ClassificationTypeService service = mapServices.get(objectType);
        if (service != null) {
            classification.addAll(service.getClassificationTree());
        }
        return classification;
    }

    public Object createTypeAttribute(ISObjectType type, ObjectNode json) {
        return mapServices.get(type).createAttribute(mapConverters.get(type).convertToTypeAttribute(json));
    }

    public Object updateTypeAttribute(ISObjectType type, ObjectNode json) {
        return mapServices.get(type).updateAttribute(mapConverters.get(type).convertToTypeAttribute(json));
    }

    public void deleteTypeAttribute(ISObjectType type, Integer id) {
        mapServices.get(type).deleteAttribute(id);
    }

    @Transactional(readOnly = true)
    public Object getTypeAttribute(ISObjectType type, Integer id) {
        return mapServices.get(type).getAttribute(id);
    }

    @Transactional(readOnly = true)
    public List<Object> getTypeAttributes(ISObjectType objectType, Integer typeId, Boolean hier) {
        List<Object> attributes = new ArrayList<>();
        ClassificationTypeService service = mapServices.get(objectType);
        if (service != null) {
            attributes.addAll(service.getAttributes(typeId, hier));
        }
        return attributes;
    }

}
