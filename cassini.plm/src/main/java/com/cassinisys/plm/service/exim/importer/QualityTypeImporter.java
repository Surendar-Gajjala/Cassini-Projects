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
import com.cassinisys.plm.model.plm.ItemClass;
import com.cassinisys.plm.model.plm.PLMItemType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.service.classification.QualityTypeService;
import com.cassinisys.plm.service.plm.LifeCycleService;
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
public class QualityTypeImporter {
    @Autowired
    private ProductInspectionPlanTypeRepository productInspectionPlanTypeRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private LovRepository lovRepository;
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemTypeImporter itemTypeImporter;
    @Autowired
    private QualityTypeAttributeRepository qualityTypeAttributeRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private MaterialInspectionPlanRepository materialInspectionPlanRepository;
    @Autowired
    private ProblemReportTypeRepository problemReportTypeRepository;
    @Autowired
    private NCRTypeRepository ncrTypeRepository;
    @Autowired
    private QCRTypeRepository qcrTypeRepository;
    @Autowired
    private MaterialInspectionPlanTypeRepository materialInspectionPlanTypeRepository;
    @Autowired
    private QualityTypeRepository qualityTypeRepository;

    @Autowired
    private QualityTypeService qualityTypeService;

    private PLMLifeCycle plmLifeCycle;
    @Autowired
    private LifeCycleService lifeCycleService;
    private static PLMItemType  itemType;
    private static Lov lov;
    private static Lov failureTypes;
    private static Lov dispositions;
    private static Lov severities;


    public static ConcurrentMap<String, PQMProductInspectionPlanType> rootPipTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMProductInspectionPlanType> piPTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMMaterialInspectionPlanType> rootMipTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMMaterialInspectionPlanType> mipTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMProblemReportType> rootPrTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMProblemReportType> prTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMNCRType> rootNcrTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMNCRType> ncrTypesMapByPath = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMQCRType> rootQcrTypeTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PQMQCRType> qcrTypesMapByPath = new ConcurrentHashMap<>();

    private void getDefaultPipLifecycle() {
        plmLifeCycle = lifeCycleService.findLifecycleByName("Default Inspection Plan Lifecycle");
    }


    private void getDefaultProductType() {
        itemType = itemTypeRepository.findByParentTypeIsNullAndItemClass(ItemClass.PRODUCT);
    }

    private void getDefaultRev() {
        lov = lovRepository.findByName("Default Revision Sequence");
    }


    private void getDefaultFailureTypes() {
        failureTypes = lovRepository.findByName("Default Problem Report Defect Types");
    }

    private void getDefaultSeveritiesTypes() {
        failureTypes = lovRepository.findByName("Default Problem Report Severities");
    }

    private void getDefaultDispositionsTypes() {
        failureTypes = lovRepository.findByName("Default Problem Report Dispositions");
    }


    public void loadPipClassificationTree() {
        getDefaultPipLifecycle();
        getDefaultProductType();
        getDefaultRev();
        rootPipTypeTypesMap = new ConcurrentHashMap<>();
        piPTypesMapByPath = new ConcurrentHashMap<>();
        List<PQMProductInspectionPlanType> rootTypes = qualityTypeService.getProductInspectionPlanTypeTree();
        for (PQMProductInspectionPlanType rootType : rootTypes) {
            rootPipTypeTypesMap.put(rootType.getName(), rootType);
            piPTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public void loadMipClassificationTree() {
        getDefaultPipLifecycle();
        getDefaultRev();
        rootMipTypeTypesMap = new ConcurrentHashMap<>();
        mipTypesMapByPath = new ConcurrentHashMap<>();
        List<PQMMaterialInspectionPlanType> rootTypes = qualityTypeService.getMaterialInspectionPlanTypeTree();
        for (PQMMaterialInspectionPlanType rootType : rootTypes) {
            rootMipTypeTypesMap.put(rootType.getName(), rootType);
            mipTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public void loadPRClassificationTree() {
        getDefaultDispositionsTypes();
        getDefaultFailureTypes();
        getDefaultSeveritiesTypes();
        getDefaultRev();
        rootPrTypeTypesMap = new ConcurrentHashMap<>();
        prTypesMapByPath = new ConcurrentHashMap<>();
        List<PQMProblemReportType> rootTypes = qualityTypeService.getPrTypeTree();
        for (PQMProblemReportType rootType : rootTypes) {
            rootPrTypeTypesMap.put(rootType.getName(), rootType);
            prTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadNCRClassificationTree() {
        getDefaultDispositionsTypes();
        getDefaultFailureTypes();
        getDefaultSeveritiesTypes();
        rootNcrTypeTypesMap = new ConcurrentHashMap<>();
        ncrTypesMapByPath = new ConcurrentHashMap<>();
        List<PQMNCRType> rootTypes = qualityTypeService.getNcrTypeTree();
        for (PQMNCRType rootType : rootTypes) {
            rootNcrTypeTypesMap.put(rootType.getName(), rootType);
            ncrTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public void loadQCRClassificationTree() {
        rootQcrTypeTypesMap = new ConcurrentHashMap<>();
        qcrTypesMapByPath = new ConcurrentHashMap<>();
        List<PQMQCRType> rootTypes = qualityTypeService.getQcrTypeTree();
        for (PQMQCRType rootType : rootTypes) {
            rootQcrTypeTypesMap.put(rootType.getName(), rootType);
            qcrTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    /*
* PIP Type
*
* */

    public PQMProductInspectionPlanType getPipTypes(String path, String numberSourece) {
        PQMProductInspectionPlanType qualityType = piPTypesMapByPath.get(path);
        if (qualityType == null) {
            qualityType = getPipTypeByPath(path);
            if (qualityType == null) {
                qualityType = createPipTypeByPath(null, path, numberSourece);
            }
            piPTypesMapByPath.put(path, qualityType);
        }

        return qualityType;
    }

    private PQMProductInspectionPlanType getPipTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMProductInspectionPlanType qualityType = rootPipTypeTypesMap.get(name);
            if (qualityType != null) {
                return qualityType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return qualityType;
            }
        } else {
            name = path;
            return rootPipTypeTypesMap.get(name);
        }
    }


    public PQMProductInspectionPlanType createPipTypeByPath(PQMProductInspectionPlanType parentType, String path, String numberSource) {
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
            parentType = rootPipTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PQMProductInspectionPlanType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setProductType(itemType);
                parentType.setNumberSource(this.autoWireKeyMap.get("Default Inspection Plan Number Source"));
                parentType.setInspectionNumberSource(this.autoWireKeyMap.get("Default Inspection Number Source"));
                parentType.setRevisionSequence(lov);
                parentType.setLifecycle(plmLifeCycle);
                parentType = productInspectionPlanTypeRepository.save(parentType);
                rootPipTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createPipTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PQMProductInspectionPlanType childItemType = new PQMProductInspectionPlanType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType.setInspectionNumberSource(parentType.getInspectionNumberSource());
            childItemType.setRevisionSequence(parentType.getRevisionSequence());
            childItemType.setLifecycle(parentType.getLifecycle());
            childItemType = productInspectionPlanTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createPipTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createPipTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }



       /*
* MIP Type
*
* */

    public PQMMaterialInspectionPlanType getMipTypes(String path, String numberSourece) {
        PQMMaterialInspectionPlanType qualityType = mipTypesMapByPath.get(path);
        if (qualityType == null) {
            qualityType = getMipTypeByPath(path);
            if (qualityType == null) {
                qualityType = createMipTypeByPath(null, path, numberSourece);
            }
            mipTypesMapByPath.put(path, qualityType);
        }

        return qualityType;
    }

    private PQMMaterialInspectionPlanType getMipTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMMaterialInspectionPlanType qualityType = rootMipTypeTypesMap.get(name);
            if (qualityType != null) {
                return qualityType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return qualityType;
            }
        } else {
            name = path;
            return rootMipTypeTypesMap.get(name);
        }
    }

    public PQMMaterialInspectionPlanType createMipTypeByPath(PQMMaterialInspectionPlanType parentType, String path, String numberSource) {
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
            parentType = rootMipTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PQMMaterialInspectionPlanType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(this.autoWireKeyMap.get("Default Inspection Plan Number Source"));
                parentType.setInspectionNumberSource(this.autoWireKeyMap.get("Default Inspection Number Source"));
                parentType.setRevisionSequence(lov);
                parentType.setLifecycle(plmLifeCycle);
                parentType = materialInspectionPlanTypeRepository.save(parentType);
                rootMipTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMipTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PQMMaterialInspectionPlanType childItemType = new PQMMaterialInspectionPlanType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType.setInspectionNumberSource(parentType.getInspectionNumberSource());
            childItemType.setRevisionSequence(parentType.getRevisionSequence());
            childItemType.setLifecycle(parentType.getLifecycle());
            childItemType = materialInspectionPlanTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createMipTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMipTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    /*
* PR Type
*
* */

    public PQMProblemReportType getPrTypes(String path, String numberSourece) {
        PQMProblemReportType qualityType = prTypesMapByPath.get(path);
        if (qualityType == null) {
            qualityType = getPrTypeByPath(path);
            if (qualityType == null) {
                qualityType = createPrTypeByPath(null, path, numberSourece);
            }
            prTypesMapByPath.put(path, qualityType);
        }

        return qualityType;
    }

    private PQMProblemReportType getPrTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMProblemReportType qualityType = rootPrTypeTypesMap.get(name);
            if (qualityType != null) {
                return qualityType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return qualityType;
            }
        } else {
            name = path;
            return rootPrTypeTypesMap.get(name);
        }
    }

    public PQMProblemReportType createPrTypeByPath(PQMProblemReportType parentType, String path, String numberSource) {
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
            parentType = rootPrTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PQMProblemReportType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(this.autoWireKeyMap.get("Default PR Number Source"));
                parentType.setFailureTypes(failureTypes);
                parentType.setSeverities(severities);
                parentType.setDispositions(dispositions);
                parentType = problemReportTypeRepository.save(parentType);
                rootPrTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createPrTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PQMProblemReportType childItemType = new PQMProblemReportType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType.setFailureTypes(parentType.getFailureTypes());
            childItemType.setSeverities(parentType.getSeverities());
            childItemType.setDispositions(parentType.getDispositions());
            childItemType = problemReportTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createPrTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createPrTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }




      /*
* NCR Type
*
* */

    public PQMNCRType getNcrTypes(String path, String numberSourece) {
        PQMNCRType qualityType = ncrTypesMapByPath.get(path);
        if (qualityType == null) {
            qualityType = getNcrTypeByPath(path);
            if (qualityType == null) {
                qualityType = createNcrTypeByPath(null, path, numberSourece);
            }
            ncrTypesMapByPath.put(path, qualityType);
        }

        return qualityType;
    }

    private PQMNCRType getNcrTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMNCRType qualityType = rootNcrTypeTypesMap.get(name);
            if (qualityType != null) {
                return qualityType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return qualityType;
            }
        } else {
            name = path;
            return rootNcrTypeTypesMap.get(name);
        }
    }

    public PQMNCRType createNcrTypeByPath(PQMNCRType parentType, String path, String numberSource) {
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
            parentType = rootNcrTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PQMNCRType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(this.autoWireKeyMap.get("Default NCR Number Source"));
                parentType.setFailureTypes(failureTypes);
                parentType.setSeverities(severities);
                parentType.setDispositions(dispositions);
                parentType = ncrTypeRepository.save(parentType);
                rootNcrTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createNcrTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PQMNCRType childItemType = new PQMNCRType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType.setFailureTypes(parentType.getFailureTypes());
            childItemType.setSeverities(parentType.getSeverities());
            childItemType.setDispositions(parentType.getDispositions());
            childItemType = ncrTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createNcrTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createNcrTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }



        /*
* QCR Type
*
* */

    public PQMQCRType getQcrTypes(String path, String numberSourece) {
        PQMQCRType qualityType = qcrTypesMapByPath.get(path);
        if (qualityType == null) {
            qualityType = getQcrTypeByPath(path);
            if (qualityType == null) {
                qualityType = createQcrTypeByPath(null, path, numberSourece);
            }
            qcrTypesMapByPath.put(path, qualityType);
        }

        return qualityType;
    }

    private PQMQCRType getQcrTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PQMQCRType qualityType = rootQcrTypeTypesMap.get(name);
            if (qualityType != null) {
                return qualityType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return qualityType;
            }
        } else {
            name = path;
            return rootQcrTypeTypesMap.get(name);
        }
    }

    public PQMQCRType createQcrTypeByPath(PQMQCRType parentType, String path, String numberSource) {
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
            parentType = rootQcrTypeTypesMap.get(name);
            if (parentType == null) {
                parentType = new PQMQCRType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setNumberSource(this.autoWireKeyMap.get("Default QCR Number Source"));
                parentType = qcrTypeRepository.save(parentType);
                rootQcrTypeTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createQcrTypeByPath(parentType, restOfPath, numberSource);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PQMQCRType childItemType = new PQMQCRType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setNumberSource(parentType.getNumberSource());
            childItemType = qcrTypeRepository.save(childItemType);
            ;
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createQcrTypeByPath(childItemType, restOfPath, numberSource);
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createQcrTypeByPath(parentType, restOfPath, numberSource);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    private void initAutoWiredValues() {
        List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
        this.autoWireKeyMap = autoNumbers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }

    /*
    * Create Quality Type Object Types
    * 
    * */

    public void importPIPObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.initAutoWiredValues();
        this.loadPipClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PQMProductInspectionPlanType plmQualityType = this.rootPipTypeTypesMap.get(typeName);
            if (plmQualityType != null) {
                plmQualityType.setName(typeName);
                plmQualityType.setName(desc != null ? desc : typeName);
                productInspectionPlanTypeRepository.save(plmQualityType);
                PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                importQualityTypeAttributes(i, objectType, qualityTypeAttribute, plmQualityType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PQMProductInspectionPlanType itemTypeObject = this.getPipTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                        importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PQMProductInspectionPlanType itemTypeObject = this.getPipTypes(path, numberSource);
                if (itemTypeObject != null) {
                    PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                    importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importMIPObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.initAutoWiredValues();
        this.loadMipClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PQMMaterialInspectionPlanType plmQualityType = this.rootMipTypeTypesMap.get(typeName);
            if (plmQualityType != null) {
                plmQualityType.setName(typeName);
                plmQualityType.setName(desc != null ? desc : typeName);
                materialInspectionPlanTypeRepository.save(plmQualityType);
                PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                importQualityTypeAttributes(i, objectType, qualityTypeAttribute, plmQualityType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PQMMaterialInspectionPlanType itemTypeObject = this.getMipTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                        importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PQMMaterialInspectionPlanType itemTypeObject = this.getMipTypes(path, numberSource);
                if (itemTypeObject != null) {
                    PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                    importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importPrObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.initAutoWiredValues();
        this.loadPRClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PQMProblemReportType plmQualityType = this.rootPrTypeTypesMap.get(typeName);
            if (plmQualityType != null) {
                plmQualityType.setName(typeName);
                plmQualityType.setName(desc != null ? desc : typeName);
                problemReportTypeRepository.save(plmQualityType);
                PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                importQualityTypeAttributes(i, objectType, qualityTypeAttribute, plmQualityType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PQMProblemReportType itemTypeObject = this.getPrTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                        importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PQMProblemReportType itemTypeObject = this.getPrTypes(path, numberSource);
                if (itemTypeObject != null) {
                    PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                    importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importNcrObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.initAutoWiredValues();
        this.loadNCRClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PQMNCRType plmQualityType = this.rootNcrTypeTypesMap.get(typeName);
            if (plmQualityType != null) {
                plmQualityType.setName(typeName);
                plmQualityType.setName(desc != null ? desc : typeName);
                ncrTypeRepository.save(plmQualityType);
                PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                importQualityTypeAttributes(i, objectType, qualityTypeAttribute, plmQualityType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PQMNCRType itemTypeObject = this.getNcrTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                        importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PQMNCRType itemTypeObject = this.getNcrTypes(path, numberSource);
                if (itemTypeObject != null) {
                    PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                    importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    public void importQcrObjects(Integer i, Enum objectType, RowData stringListHashMap, String numberSource) {
        this.initAutoWiredValues();
        this.loadQCRClassificationTree();
        String path = stringListHashMap.get("Type Path");
        String typeName = stringListHashMap.get("Type Name");
        String desc = stringListHashMap.get("Type Description");

        if (typeName != null && typeName != "") {
            PQMQCRType plmQualityType = this.rootQcrTypeTypesMap.get(typeName);
            if (plmQualityType != null) {
                plmQualityType.setName(typeName);
                plmQualityType.setName(desc != null ? desc : typeName);
                qcrTypeRepository.save(plmQualityType);
                PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                importQualityTypeAttributes(i, objectType, qualityTypeAttribute, plmQualityType.getId(), stringListHashMap);
            } else {

                if (path != null && path != "") {
                    PQMQCRType itemTypeObject = this.getQcrTypes(path, numberSource);
                    if (itemTypeObject != null) {
                        PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                        importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                    }
                } else {
                    throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

                }
            }


        } else {
            if (path != null && path != "") {
                PQMQCRType itemTypeObject = this.getQcrTypes(path, numberSource);
                if (itemTypeObject != null) {
                    PQMQualityTypeAttribute qualityTypeAttribute = new PQMQualityTypeAttribute();
                    importQualityTypeAttributes(i, objectType, qualityTypeAttribute, itemTypeObject.getId(), stringListHashMap);
                }
            } else {
                throw new CassiniException("Please provide valid Type Name or Type Path for row number:" + (i));

            }

        }

    }


    private Boolean checkQualityTypeAttributeName(Enum objectType, Integer qualityType, String name) {
        Boolean isExist = false;
        if (objectType.toString().equals("PRODUCTINSPECTIONPLANTYPE")) {
            PQMProductInspectionPlanType productInspectionPlanType = productInspectionPlanTypeRepository.findOne(qualityType);
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (productInspectionPlanType != null) {
                qualityTypeAttribute = qualityTypeAttributeRepository.findByTypeAndName(productInspectionPlanType.getId(), name);
                if (qualityTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("MATERIALINSPECTIONPLANTYPE")) {
            PQMMaterialInspectionPlanType materialInspectionPlanType = materialInspectionPlanTypeRepository.findOne(qualityType);
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (materialInspectionPlanType != null) {
                qualityTypeAttribute = qualityTypeAttributeRepository.findByTypeAndName(materialInspectionPlanType.getId(), name);
                if (qualityTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("PRTYPE")) {
            PQMProblemReportType problemReportType = problemReportTypeRepository.findOne(qualityType);
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (problemReportType != null) {
                qualityTypeAttribute = qualityTypeAttributeRepository.findByTypeAndName(problemReportType.getId(), name);
                if (qualityTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("NCRTYPE")) {
            PQMNCRType pqmncrType = ncrTypeRepository.findOne(qualityType);
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (pqmncrType != null) {
                qualityTypeAttribute = qualityTypeAttributeRepository.findByTypeAndName(pqmncrType.getId(), name);
                if (qualityTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        } else if (objectType.toString().equals("QCRTYPE")) {
            PQMQCRType pqmqcrType = qcrTypeRepository.findOne(qualityType);
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (pqmqcrType != null) {
                qualityTypeAttribute = qualityTypeAttributeRepository.findByTypeAndName(pqmqcrType.getId(), name);
                if (qualityTypeAttribute != null) {
                    isExist = true;
                } else {
                    isExist = false;
                }
            }
        }


        return isExist;
    }

    public void importQualityTypeAttributes(Integer value, Enum objectType, PQMQualityTypeAttribute attribute, Integer objectId, RowData stringListHashMap) {
        if (stringListHashMap.containsKey("Attribute Name".trim()) && stringListHashMap.containsKey("Data Type".trim())) {
            Boolean isExist = false;
            String name = stringListHashMap.get("Attribute Name".trim());
            PQMQualityTypeAttribute qualityTypeAttribute = null;
            if (name != null && name != "") {
                isExist = checkQualityTypeAttributeName(objectType, objectId, name);
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
                        attribute.setListMultiple(this.itemTypeImporter.getBoolPropertyValue("Multi List (Yes or No)", stringListHashMap));

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
                        Boolean positiv = this.itemTypeImporter.getBoolPropertyValue("Only positive values (Yes or No)", stringListHashMap);
                        Boolean negative = this.itemTypeImporter.getBoolPropertyValue("Only negative values (Yes or No)", stringListHashMap);
                        Boolean both = this.itemTypeImporter.getBoolPropertyValue("Allow positive and negative values (Yes or No)", stringListHashMap);
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
                    attribute.setObjectType(PLMObjectType.QUALITY_TYPE);
                    attribute.setDescription(desc);
                    attribute.setDataType(DataType.valueOf(dataType));
                    attribute.setDefaultTextValue(defaultValue);
                    attribute.setVisible(true);
                    attribute.setRequired(this.itemTypeImporter.getBoolPropertyValue("Is Required".trim(), stringListHashMap));
                    attribute.setRevisionSpecific(this.itemTypeImporter.getBoolPropertyValue("Revision Specific (Yes or No)".trim(), stringListHashMap));
                    qualityTypeAttributeRepository.save(attribute);

                }


            }

        }
    }


}