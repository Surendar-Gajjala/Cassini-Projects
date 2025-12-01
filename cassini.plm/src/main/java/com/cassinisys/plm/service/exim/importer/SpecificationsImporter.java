package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.filtering.PGCSpecificationPredicateBuilder;
import com.cassinisys.plm.model.pgc.PGCSpecification;
import com.cassinisys.plm.model.pgc.PGCSpecificationSubstance;
import com.cassinisys.plm.model.pgc.PGCSpecificationType;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.repo.pgc.PGCSpecificationRepository;
import com.cassinisys.plm.repo.pgc.PGCSpecificationSubstanceRepository;
import com.cassinisys.plm.repo.pgc.PGCSpecificationTypeRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceRepository;
import com.cassinisys.plm.service.classification.PGCObjectTypeService;
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
public class SpecificationsImporter {

    @Autowired
    private Importer importer;
    private MessageSource messageSource;
    @Autowired
    private PGCSpecificationRepository specificationRepository;
    @Autowired
    private PGCSpecificationTypeRepository specificationTypeRepository;
    @Autowired
    private PGCSpecificationPredicateBuilder specificationPredicateBuilder;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private PGCSpecificationSubstanceRepository specificationSubstanceRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;

    private static AutoNumber autoNumber;
    private ConcurrentMap<String, PGCSpecificationType> rootSpecificationTypesMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PGCSpecificationType> specificationTypesMapByPath = new ConcurrentHashMap<>();
    Map<Integer, MeasurementUnit> dbMeasurementBaseUnitsMap = new LinkedHashMap();
    Map<String, PGCSpecificationSubstance> dbSpecificationSubstancesMap = new LinkedHashMap();

    private void getDefaultSpecificationNumberSource() {
        autoNumber = autoNumberService.getByName("Default Specification Number Source");
    }

    public void loadSpecificationClassificationTree() {
        getDefaultSpecificationNumberSource();
        rootSpecificationTypesMap = new ConcurrentHashMap<>();
        specificationTypesMapByPath = new ConcurrentHashMap<>();
        List<PGCSpecificationType> rootTypes = pgcObjectTypeService.getSpecificationTypeTree();
        for (PGCSpecificationType rootType : rootTypes) {
            rootSpecificationTypesMap.put(rootType.getName(), rootType);
            specificationTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public PGCSpecificationType getSpecificationTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PGCSpecificationType substanceType = rootSpecificationTypesMap.get(name);
            if (substanceType != null) {
                substanceType = substanceType.getChildTypeByPath(path.substring(index + 1));
                return substanceType;
            } else {
                return substanceType;
            }

        } else {
            name = path;
            return rootSpecificationTypesMap.get(name);
        }
    }

    public PGCSpecificationType getSpecificationType(String typePath) {
        PGCSpecificationType substanceType = specificationTypesMapByPath.get(typePath);
        if (substanceType == null) {
            substanceType = getSpecificationTypeByPath(typePath);
            if (substanceType == null) {
                substanceType = createSpecificationTypeByPath(null, typePath);
            }

            specificationTypesMapByPath.put(typePath, substanceType);
        }
        return substanceType;
    }

    private PGCSpecificationType createSpecificationTypeByPath(PGCSpecificationType parentType, String path) {
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
            parentType = rootSpecificationTypesMap.get(name);
            if (parentType == null) {
                parentType = new PGCSpecificationType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = pgcObjectTypeService.createSpecificationType(parentType);
                rootSpecificationTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createSpecificationTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PGCSpecificationType childSpecificationType = new PGCSpecificationType();
            childSpecificationType.setParentType(parentType.getId());
            childSpecificationType.setName(name);
            childSpecificationType.setDescription(name);
            childSpecificationType.setAutoNumberSource(parentType.getAutoNumberSource());
            childSpecificationType = pgcObjectTypeService.createSpecificationType(childSpecificationType);
            parentType.getChildren().add(childSpecificationType);
            if (restOfPath != null) {
                return parentType = createSpecificationTypeByPath(childSpecificationType, restOfPath);
            } else {
                return childSpecificationType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createSpecificationTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }


    public void importSpecificationsAndSubstances(TableData tableData) throws ParseException {
        this.loadSpecificationClassificationTree();
        Map<String, PGCSpecification> dbSpecificationMap = new LinkedHashMap();
        dbSpecificationSubstancesMap = new LinkedHashMap();
        Map<String, PGCSpecificationType> dbSpecificationTypesMap = new LinkedHashMap();
        Map<String, PGCSubstance> dbSubstancesMap = new LinkedHashMap();
        Map<String, MeasurementUnit> dbMeasurementUnitsMap = new LinkedHashMap();
        dbMeasurementBaseUnitsMap = new LinkedHashMap();

        List<PGCSpecification> dbSpecifications = specificationRepository.findAll();
        List<PGCSpecificationType> dbSpecificationTypes = specificationTypeRepository.findAll();
        List<PGCSubstance> dbSubstances = substanceRepository.findAll();
        List<MeasurementUnit> dbMeasurementUnits = measurementUnitRepository.findAll();
        List<PGCSpecificationSubstance> dbSpecSubstances = specificationSubstanceRepository.findAll();
        dbSpecificationMap = dbSpecifications.stream().collect(Collectors.toMap(r -> r.getNumber(), r -> r));
//        dbSpecificationTypesMap = dbSpecificationTypes.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        for (PGCSpecificationType pgcSpecificationType : dbSpecificationTypes) {
            if (!dbSpecificationTypesMap.containsKey(pgcSpecificationType.getName()))
                dbSpecificationTypesMap.put(pgcSpecificationType.getName(), pgcSpecificationType);
        }


        dbSubstancesMap = dbSubstances.stream().collect(Collectors.toMap(r -> r.getNumber(), r -> r));
        dbMeasurementUnitsMap = dbMeasurementUnits.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        List<MeasurementUnit> dbMeasurementBaseUnits = measurementUnitRepository.findByBaseUnitTrue();
        dbMeasurementBaseUnitsMap = dbMeasurementBaseUnits.stream().collect(Collectors.toMap(r -> r.getMeasurement(), r -> r));
        dbSpecificationSubstancesMap = dbSpecSubstances.stream().collect(Collectors.toMap(r -> r.getSpecification().toString() + r.getSubstance().getId().toString(), r -> r));
        List<PGCSpecification> specifications = createSpecifications(tableData, dbSpecificationMap, dbSpecificationTypesMap);
        createSpecSubstances(tableData, specifications, dbSubstancesMap, dbMeasurementUnitsMap);
    }

    private List<PGCSpecification> createSpecifications(TableData tableData, Map<String, PGCSpecification> dbSpecifiactionsMap, Map<String, PGCSpecificationType> dbSpecificationTypesMap) {
        List<PGCSpecification> specifications = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Specification Name") && stringListHashMap.containsKey("Specification Number") && stringListHashMap.containsKey("Specification Type") && stringListHashMap.containsKey("Type Path")) {
                String number = stringListHashMap.get("Specification Number");
                String name = stringListHashMap.get("Specification Name");
                ;
                if (number != null && !number.trim().equals("")) {
                    String specTypeName = stringListHashMap.get("Specification Type");
                    String typePath = stringListHashMap.get("Type Path");
                    if (specTypeName != null && specTypeName != "") {
                        PGCSpecificationType specType = dbSpecificationTypesMap.get(specTypeName);
                        if (specType != null) {
                            PGCSpecification specification = dbSpecifiactionsMap.get(number);
                            if (specification != null) {
                                specifications.add(specification);
                            } else {
                                PGCSpecification pgcSpecification = createSpecification(i, number, specType, stringListHashMap, dbSpecifiactionsMap);
                                specifications.add(pgcSpecification);
                            }
                        } else {
                            if (typePath != null && typePath != "") {

                                PGCSpecificationType pgcSpecificationType = this.getSpecificationType(typePath);
                                if (pgcSpecificationType != null) {
                                    dbSpecificationTypesMap.put(pgcSpecificationType.getName(), pgcSpecificationType);
                                    PGCSpecification specification = dbSpecifiactionsMap.get(name);
                                    if (specification != null) {
                                        specifications.add(specification);
                                    } else {
                                        PGCSpecification manufacturer1 = createSpecification(i, number, pgcSpecificationType, stringListHashMap, dbSpecifiactionsMap);
                                        specifications.add(manufacturer1);
                                    }
                                }
                            }
                        }
                    } else {

                        if (typePath != null && typePath != "") {
                            PGCSpecificationType pgcSpecificationType = this.getSpecificationType(typePath);
                            if (pgcSpecificationType != null) {
                                dbSpecificationTypesMap.put(pgcSpecificationType.getName(), pgcSpecificationType);
                                PGCSpecification specification = dbSpecifiactionsMap.get(name);
                                if (specification != null) {
                                    specifications.add(specification);
                                } else {
                                    PGCSpecification manufacturer1 = createSpecification(i, number, pgcSpecificationType, stringListHashMap, dbSpecifiactionsMap);
                                    specifications.add(manufacturer1);
                                }
                            }


                        } else {
                            throw new CassiniException(messageSource.getMessage("please_provide_specification_type_for_row_number" + (i),
                                    null, "Please provide Specification Type for row number:" + (i), LocaleContextHolder.getLocale()));
                        }
                    }
                } else {
                    specifications.add(null);
                }

            } else {
                throw new CassiniException("Please provide Specification Name,Specification Number and Specification Type Column also");
            }

            i++;
        }

        if (specifications.size() > 0) {
            specifications = specificationRepository.save(specifications);
        }

        return specifications;
    }

    /*
    * Create Specifications
    * */

    private PGCSpecification createSpecification(Integer i, String number, PGCSpecificationType specificationType, RowData stringListHashMap, Map<String, PGCSpecification> dbSpecifiactionsMap) {
        String name = stringListHashMap.get("Specification Name");
        String desc = stringListHashMap.get("Specification Description");
        PGCSpecification specification = new PGCSpecification();
        specification.setType(specificationType);
        specification.setName(name);
        specification.setDescription(desc);
        specification.setNumber(number);
        specification.setActive(true);
        dbSpecifiactionsMap.put(specification.getNumber(), specification);
        // specification = specificationRepository.save(specification);
        return specification;
    }

    private List<PGCSpecificationSubstance> createSpecSubstances(TableData tableData, List<PGCSpecification> specifications, Map<String, PGCSubstance> dbSubstanceMap, Map<String, MeasurementUnit> dbMeasurementUnitsMap) {
        List<PGCSpecificationSubstance> specSubstances = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {

            if (stringListHashMap.containsKey("Substance Number") && stringListHashMap.containsKey("Threshold Mass") && stringListHashMap.containsKey("Units")) {

                String name = stringListHashMap.get("Substance Number");
                if (name != null && !name.trim().equals("")) {
                    PGCSpecification specification = specifications.get(i);
                    PGCSubstance substance = dbSubstanceMap.get(name);
                    if (specification != null && substance != null) {
                        PGCSpecificationSubstance existSubstance = dbSpecificationSubstancesMap.get(specification.getId().toString() + substance.getId().toString());
                        if (existSubstance == null) {
                            String units = stringListHashMap.get("Units");
                            if (units != null && units != "") {
                                MeasurementUnit measurementUnit = dbMeasurementUnitsMap.get(units);
                                PGCSpecificationSubstance pgcSpecificationSubstance = specSubstance(i, name, stringListHashMap, specification.getId(), substance, measurementUnit);
                                specSubstances.add(pgcSpecificationSubstance);
                                dbSpecificationSubstancesMap.put(pgcSpecificationSubstance.getSpecification().toString() + pgcSpecificationSubstance.getSubstance().getId().toString(), pgcSpecificationSubstance);
                            }
                        } else {
                            specSubstances.add(existSubstance);
                        }
                    } else {
                        throw new CassiniException("Substance does not exist for row number:" + (i));
                    }
                } else {
                    specSubstances.add(null);
                }


            } else {
                throw new CassiniException("Please provide Substance Number,Threshold Mass and Units Column also");
            }

            i++;
        }
        if (specSubstances.size() > 0) {
            specSubstances = specificationSubstanceRepository.save(specSubstances);
        }

        return specSubstances;
    }

    /*
    * Create Specification Substances
    * */
    private PGCSpecificationSubstance specSubstance(Integer i, String number, RowData stringListHashMap, Integer supplierId, PGCSubstance manufacturerPart, MeasurementUnit measurementUnit) {
        PGCSpecificationSubstance pgcSpecificationSubstance = new PGCSpecificationSubstance();
        String thresholdMass = stringListHashMap.get("Threshold Mass");
        if (measurementUnit != null && (thresholdMass != null && thresholdMass != "")) {
            Double threshold = new Double(thresholdMass);
            //MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurementUnit.getMeasurement());
            MeasurementUnit baseUnit = dbMeasurementBaseUnitsMap.get(measurementUnit.getMeasurement());
            if (!measurementUnit.getId().equals(baseUnit.getId())) {
                pgcSpecificationSubstance.setThresholdMass(threshold / measurementUnit.getConversionFactor());
                pgcSpecificationSubstance.setUom(measurementUnit.getId());
            } else {
                pgcSpecificationSubstance.setThresholdMass(threshold);
            }
        } else {

        }

        if (pgcSpecificationSubstance.getThresholdMass() == null) {
            pgcSpecificationSubstance.setThresholdMass(0.0);
        }
        pgcSpecificationSubstance.setSpecification(supplierId);
        pgcSpecificationSubstance.setSubstance(manufacturerPart);
        pgcSpecificationSubstance.setThresholdPpm(0);
        // pgcSpecificationSubstance = specificationSubstanceRepository.save(pgcSpecificationSubstance);
        return pgcSpecificationSubstance;
    }
}
