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
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.repo.cm.*;
import com.cassinisys.plm.service.classification.ChangeTypeService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class ChangeTypeImporter {
    @Autowired
    private ChangeTypeRepository changeTypeRepository;

    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    private Lov defaultLOV;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private ChangeTypeAttributeRepository changeTypeAttributeRepository;
    @Autowired
    private ECOTypeRepository ecoTypeRepository;
    @Autowired
    private ECRTypeRepository ecrTypeRepository;
    @Autowired
    private DCOTypeRepository dcoTypeRepository;
    @Autowired
    private DCRTypeRepository dcrTypeRepository;
    @Autowired
    private DeviationTypeRepository deviationTypeRepository;
    @Autowired
    private WaiverTypeRepository waiverTypeRepository;
    @Autowired
    private MCOTypeRepository mcoTypeRepository;

    @Autowired
    private ChangeTypeService changeTypeService;
    @Autowired
    private AutoNumberService autoNumberService;

    public static ConcurrentMap<String, PLMECOType> rootEcoTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMECOType> ecoTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMECRType> rootEcrTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMECRType> ecrTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDCOType> rootDcoTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDCOType> dcoTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDCRType> rootDcrTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDCRType> dcrTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDeviationType> rootDevTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMDeviationType> devTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMWaiverType> rootWavTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMWaiverType> wavTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMMCOType> rootMcoTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMMCOType> mcoTypesMapByPath = new ConcurrentHashMap<>();

    public void loadEcoClassificationTree() {
        rootEcoTypeTypesMap = new ConcurrentHashMap<>();
        ecoTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMECOType> rootTypes = changeTypeService.getEcoTypeTree();
        for (PLMECOType rootType : rootTypes) {
            rootEcoTypeTypesMap.put(rootType.getName(), rootType);
            ecoTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadEcrClassificationTree() {
        rootEcrTypeTypesMap = new ConcurrentHashMap<>();
        ecrTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMECRType> rootTypes = changeTypeService.getEcrTypeTree();
        for (PLMECRType rootType : rootTypes) {
            rootEcrTypeTypesMap.put(rootType.getName(), rootType);
            ecrTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadDcoClassificationTree() {
        rootDcoTypeTypesMap = new ConcurrentHashMap<>();
        dcoTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMDCOType> rootTypes = changeTypeService.getDcoTypeTree();
        for (PLMDCOType rootType : rootTypes) {
            rootDcoTypeTypesMap.put(rootType.getName(), rootType);
            dcoTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadDcrClassificationTree() {
        rootDcrTypeTypesMap = new ConcurrentHashMap<>();
        dcrTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMDCRType> rootTypes = changeTypeService.getDcrTypeTree();
        for (PLMDCRType rootType : rootTypes) {
            rootDcrTypeTypesMap.put(rootType.getName(), rootType);
            dcrTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadDevClassificationTree() {
        rootDevTypeTypesMap = new ConcurrentHashMap<>();
        devTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMDeviationType> rootTypes = changeTypeService.getDevitionTypeTree();
        for (PLMDeviationType rootType : rootTypes) {
            rootDevTypeTypesMap.put(rootType.getName(), rootType);
            devTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadWavClassificationTree() {
        rootWavTypeTypesMap = new ConcurrentHashMap<>();
        wavTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMWaiverType> rootTypes = changeTypeService.getWaiverTypeTree();
        for (PLMWaiverType rootType : rootTypes) {
            rootWavTypeTypesMap.put(rootType.getName(), rootType);
            wavTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadMcoClassificationTree() {
        rootMcoTypeTypesMap = new ConcurrentHashMap<>();
        mcoTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMMCOType> rootTypes = changeTypeService.getMcoTypeTree();
        for (PLMMCOType rootType : rootTypes) {
            rootMcoTypeTypesMap.put(rootType.getName(), rootType);
            mcoTypesMapByPath.put(rootType.getName(), rootType);
        }
    }
/*
* ECO Type
*
* */

    public PLMECOType getEcoTypes(String path, String numberSourece) {
        PLMECOType plmItemType = ecoTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getEcoTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createEcoTypeByPath(null, path, numberSourece);
            }
            ecoTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMECOType getEcoTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMECOType itemType = rootEcoTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootEcoTypeTypesMap.get(name);
        }
    }

    public PLMECOType createEcoTypeByPath(PLMECOType parentType, String path, String numberSource) {
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
            parentType = rootEcoTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMECOType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType = ecoTypeRepository.save(parentType);
                rootEcoTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createEcoTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMECOType childItemType = new PLMECOType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = ecoTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createEcoTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createEcoTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    /*
    * ECR Type
    *
    * */


    public PLMECRType getEcrTypes(String path, String numberSourece) {
        PLMECRType plmItemType = ecrTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getEcrTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createEcrTypeByPath(null, path, numberSourece);
            }
            ecrTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMECRType getEcrTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMECRType itemType = rootEcrTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootEcrTypeTypesMap.get(name);
        }
    }

    public PLMECRType createEcrTypeByPath(PLMECRType parentType, String path, String numberSource) {
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
            parentType = rootEcrTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMECRType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType.setChangeReasonTypes(lovRepository.findByName("Default Change Request Reasons"));
                parentType = ecrTypeRepository.save(parentType);
                rootEcrTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createEcrTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMECRType childItemType = new PLMECRType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setChangeReasonTypes(parentType.getChangeReasonTypes());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = ecrTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createEcrTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createEcrTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }




    /*
    * DCO Type
    *
    * */


    public PLMDCOType getDcoTypes(String path, String numberSourece) {
        PLMDCOType plmItemType = dcoTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getDcoTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createDcoTypeByPath(null, path, numberSourece);
            }
            dcoTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMDCOType getDcoTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMDCOType itemType = rootDcoTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootDcoTypeTypesMap.get(name);
        }
    }

    public PLMDCOType createDcoTypeByPath(PLMDCOType parentType, String path, String numberSource) {
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
            parentType = rootDcoTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMDCOType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType = dcoTypeRepository.save(parentType);
                rootDcoTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createDcoTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMDCOType childItemType = new PLMDCOType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = dcoTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createDcoTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createDcoTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }



      /*
    * DCO Type
    *
    * */


    public PLMDCRType getDcrTypes(String path, String numberSourece) {
        PLMDCRType plmItemType = dcrTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getDcrTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createDcrTypeByPath(null, path, numberSourece);
            }
            dcrTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMDCRType getDcrTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMDCRType itemType = rootDcrTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootDcrTypeTypesMap.get(name);
        }
    }

    public PLMDCRType createDcrTypeByPath(PLMDCRType parentType, String path, String numberSource) {
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
            parentType = rootDcrTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMDCRType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType.setChangeReasonTypes(lovRepository.findByName("Default Change Request Reasons"));
                parentType = dcrTypeRepository.save(parentType);
                rootDcrTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createDcrTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMDCRType childItemType = new PLMDCRType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setChangeReasonTypes(parentType.getChangeReasonTypes());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = dcrTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createDcrTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createDcrTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }




       /*
    * Deviation Type
    *
    * */


    public PLMDeviationType getDevTypes(String path, String numberSourece) {
        PLMDeviationType plmItemType = devTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getDevTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createDevTypeByPath(null, path, numberSourece);
            }
            devTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMDeviationType getDevTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMDeviationType itemType = rootDevTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootDevTypeTypesMap.get(name);
        }
    }

    public PLMDeviationType createDevTypeByPath(PLMDeviationType parentType, String path, String numberSource) {
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
            parentType = rootDevTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMDeviationType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType = deviationTypeRepository.save(parentType);
                rootDevTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createDevTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMDeviationType childItemType = new PLMDeviationType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setChangeReasonTypes(parentType.getChangeReasonTypes());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = deviationTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createDevTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createDevTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }




       /*
    * Waiver Type
    *
    * */


    public PLMWaiverType getWavTypes(String path, String numberSourece) {
        PLMWaiverType plmItemType = wavTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getWavTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createWavTypeByPath(null, path, numberSourece);
            }
            wavTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMWaiverType getWavTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMWaiverType itemType = rootWavTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootWavTypeTypesMap.get(name);
        }
    }

    public PLMWaiverType createWavTypeByPath(PLMWaiverType parentType, String path, String numberSource) {
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
            parentType = rootWavTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMWaiverType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType = waiverTypeRepository.save(parentType);
                rootWavTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createWavTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMWaiverType childItemType = new PLMWaiverType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setChangeReasonTypes(parentType.getChangeReasonTypes());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = waiverTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createWavTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createWavTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }



       /*
    * MCO Type
    *
    * */


    public PLMMCOType getMcoTypes(String path, String numberSourece) {
        PLMMCOType plmItemType = mcoTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getMcoTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createMcoTypeByPath(null, path, numberSourece);
            }
            mcoTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }

    private PLMMCOType getMcoTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMMCOType itemType = rootMcoTypeTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootMcoTypeTypesMap.get(name);
        }
    }

    public PLMMCOType createMcoTypeByPath(PLMMCOType parentType, String path, String numberSource) {
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
            parentType = rootMcoTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMMCOType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(getDefaultItemNumberSource(numberSource));
                parentType = mcoTypeRepository.save(parentType);
                rootMcoTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMcoTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMMCOType childItemType = new PLMMCOType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setChangeReasonTypes(parentType.getChangeReasonTypes());
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = mcoTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createMcoTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMcoTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    public void importEcoTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadEcoClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMECOType plmecoType = this.rootEcoTypeTypesMap.get(typeName);
            if (plmecoType != null) {
                plmecoType.setName(typeName);
                plmecoType.setName(desc != null ? desc : typeName);
                ecoTypeRepository.save(plmecoType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, plmecoType.getObjectType(), ChangeTypeAttribute, plmecoType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMECOType itemTypeObject = this.getEcoTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, itemTypeObject.getObjectType(), ChangeTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMECOType plmecoType = this.getEcoTypes(path, numberSource);
                if (plmecoType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmecoType.getObjectType(), ChangeTypeAttribute, plmecoType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importEcrTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadEcrClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMECRType plmecoType = this.rootEcrTypeTypesMap.get(typeName);
            if (plmecoType != null) {
                plmecoType.setName(typeName);
                plmecoType.setName(desc != null ? desc : typeName);
                ecrTypeRepository.save(plmecoType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, plmecoType.getObjectType(), ChangeTypeAttribute, plmecoType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMECRType itemTypeObject = this.getEcrTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, itemTypeObject.getObjectType(), ChangeTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMECRType plmecoType = this.getEcrTypes(path, numberSource);
                if (plmecoType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmecoType.getObjectType(), ChangeTypeAttribute, plmecoType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importDcoTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadDcoClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMDCOType changeType = this.rootDcoTypeTypesMap.get(typeName);
            if (changeType != null) {
                changeType.setName(typeName);
                changeType.setName(desc != null ? desc : typeName);
                dcoTypeRepository.save(changeType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, changeType.getObjectType(), ChangeTypeAttribute, changeType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMDCOType changeTypeObject = this.getDcoTypes(path, numberSource);
                    if (changeTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, changeTypeObject.getObjectType(), ChangeTypeAttribute, changeTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMDCOType plmChangeType = this.getDcoTypes(path, numberSource);
                if (plmChangeType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmChangeType.getObjectType(), ChangeTypeAttribute, plmChangeType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void importDcrTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadDcrClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMDCRType changeType = this.rootDcrTypeTypesMap.get(typeName);
            if (changeType != null) {
                changeType.setName(typeName);
                changeType.setName(desc != null ? desc : typeName);
                dcrTypeRepository.save(changeType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, changeType.getObjectType(), ChangeTypeAttribute, changeType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMDCRType changeTypeObject = this.getDcrTypes(path, numberSource);
                    if (changeTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, changeTypeObject.getObjectType(), ChangeTypeAttribute, changeTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMDCRType plmChangeType = this.getDcrTypes(path, numberSource);
                if (plmChangeType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmChangeType.getObjectType(), ChangeTypeAttribute, plmChangeType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void importDevTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadDevClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMDeviationType changeType = this.rootDevTypeTypesMap.get(typeName);
            if (changeType != null) {
                changeType.setName(typeName);
                changeType.setName(desc != null ? desc : typeName);
                deviationTypeRepository.save(changeType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, changeType.getObjectType(), ChangeTypeAttribute, changeType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMDeviationType changeTypeObject = this.getDevTypes(path, numberSource);
                    if (changeTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, changeTypeObject.getObjectType(), ChangeTypeAttribute, changeTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMDeviationType plmChangeType = this.getDevTypes(path, numberSource);
                if (plmChangeType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmChangeType.getObjectType(), ChangeTypeAttribute, plmChangeType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void importWavTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadWavClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMWaiverType changeType = this.rootWavTypeTypesMap.get(typeName);
            if (changeType != null) {
                changeType.setName(typeName);
                changeType.setName(desc != null ? desc : typeName);
                waiverTypeRepository.save(changeType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, changeType.getObjectType(), ChangeTypeAttribute, changeType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMWaiverType changeTypeObject = this.getWavTypes(path, numberSource);
                    if (changeTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, changeTypeObject.getObjectType(), ChangeTypeAttribute, changeTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMWaiverType plmChangeType = this.getWavTypes(path, numberSource);
                if (plmChangeType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmChangeType.getObjectType(), ChangeTypeAttribute, plmChangeType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }

    public void importMcoTypeObjects(Integer i, String type, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.loadMcoClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PLMMCOType changeType = this.rootMcoTypeTypesMap.get(typeName);
            if (changeType != null) {
                changeType.setName(typeName);
                changeType.setName(desc != null ? desc : typeName);
                mcoTypeRepository.save(changeType);
                PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                importChangeTypeAttributes(i, changeType.getObjectType(), ChangeTypeAttribute, changeType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PLMMCOType changeTypeObject = this.getMcoTypes(path, numberSource);
                    if (changeTypeObject != null) {
                        PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                        importChangeTypeAttributes(i, changeTypeObject.getObjectType(), ChangeTypeAttribute, changeTypeObject.getId(), stringListHashMap);
                        //dbItemTypeMap.put(itemTypeObject.getName(), itemTypeObject);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PLMMCOType plmChangeType = this.getMcoTypes(path, numberSource);
                if (plmChangeType != null) {
                    PLMChangeTypeAttribute ChangeTypeAttribute = new PLMChangeTypeAttribute();
                    importChangeTypeAttributes(i, plmChangeType.getObjectType(), ChangeTypeAttribute, plmChangeType.getId(), stringListHashMap);
                    //dbItemTypeMap.put(itemType.getName(), itemType);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    private AutoNumber getDefaultItemNumberSource(String numberSource) {
        return autoNumberService.getByName("Default " + numberSource + " Source");
    }

    private PLMChangeTypeAttribute checkAttributeName(Integer ChangeType, String name) {
        PLMChangeType plmChangeType = changeTypeRepository.findOne(ChangeType);
        PLMChangeTypeAttribute ChangeTypeAttribute = null;
        if (plmChangeType != null) {
            ChangeTypeAttribute = changeTypeAttributeRepository.findByChangeTypeAndName(plmChangeType.getId(), name);
        }
        return ChangeTypeAttribute;
    }


    public void importChangeTypeAttributes(Integer value, Enum objectType, PLMChangeTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name".trim()) && stringListHashMap.containsKey("Data Type".trim())) {

            String name = stringListHashMap.get("Attribute Name".trim());
            PLMChangeTypeAttribute ChangeTypeAttribute = null;
            if (name != null && name != "") {
                ChangeTypeAttribute = checkAttributeName(objectId, name);
            }

            if (ChangeTypeAttribute == null) {
                String dataType = stringListHashMap.get("Data Type".trim());
                if (dataType != null && dataType != "") {
                    dataType = dataType.toUpperCase();
                    if (dataType.equals("TEXT")) {
                        String min = stringListHashMap.get("Minimum length".trim());
                        String max = stringListHashMap.get("Maximum length".trim());
                        Boolean cap = this.itemTypeImporter.getBoolPropertyValue("All uppercase (Yes or No)".trim(), stringListHashMap);
                        Boolean small = this.itemTypeImporter.getBoolPropertyValue("All lowercase (Yes or No)".trim(), stringListHashMap);
                        Boolean mixCase = this.itemTypeImporter.getBoolPropertyValue("Allow mixed case (Yes or No)".trim(), stringListHashMap);
                        String pattern = stringListHashMap.get("Pattern");
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
                        String enter = stringListHashMap.get("No of decimals to enter");
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
                    attribute.setChangeType(objectId);
                    attribute.setObjectType(objectType);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    changeTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


}