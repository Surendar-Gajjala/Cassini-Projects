package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import com.cassinisys.plm.model.pgc.PGCSubstanceType;
import com.cassinisys.plm.repo.pgc.PGCSubstanceRepository;
import com.cassinisys.plm.repo.pgc.PGCSubstanceTypeRepository;
import com.cassinisys.plm.service.classification.PGCObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class SubstancesImporter {

    @Autowired
    private Importer importer;
    private MessageSource messageSource;
    @Autowired
    private PGCSubstanceRepository substanceRepository;
    @Autowired
    private PGCSubstanceTypeRepository substanceTypeRepository;
    @Autowired
    private PGCObjectTypeService pgcObjectTypeService;
    @Autowired
    private AutoNumberService autoNumberService;

    private ConcurrentMap<String, PGCSubstanceType> rootSubstanceTypesMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PGCSubstanceType> substanceTypesMapByPath = new ConcurrentHashMap<>();
    private static AutoNumber autoNumber;

    private void getDefaultSubstanceNumberSource() {
        autoNumber = autoNumberService.getByName("Default Substance Number Source");
    }

    public void loadSubstanceClassificationTree() {
        getDefaultSubstanceNumberSource();
        rootSubstanceTypesMap = new ConcurrentHashMap<>();
        substanceTypesMapByPath = new ConcurrentHashMap<>();
        List<PGCSubstanceType> rootTypes = pgcObjectTypeService.getSubstanceTypeTree();
        for (PGCSubstanceType rootType : rootTypes) {
            rootSubstanceTypesMap.put(rootType.getName(), rootType);
            substanceTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public PGCSubstanceType getSubstanceTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PGCSubstanceType substanceType = rootSubstanceTypesMap.get(name);
            if (substanceType != null) {
                substanceType = substanceType.getChildTypeByPath(path.substring(index + 1));
                return substanceType;
            } else {
                return substanceType;
            }

        } else {
            name = path;
            return rootSubstanceTypesMap.get(name);
        }
    }

    public PGCSubstanceType getSubstanceType(String typePath) {
        PGCSubstanceType substanceType = substanceTypesMapByPath.get(typePath);
        if (substanceType == null) {
            substanceType = getSubstanceTypeByPath(typePath);
            if (substanceType == null) {
                substanceType = createSubstanceTypeByPath(null, typePath);
            }

            substanceTypesMapByPath.put(typePath, substanceType);
        }
        return substanceType;
    }


    public PGCSubstanceType createSubstanceTypeByPath(PGCSubstanceType parentType, String path) {
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
            parentType = rootSubstanceTypesMap.get(name);
            if (parentType == null) {
                parentType = new PGCSubstanceType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(autoNumber);
                parentType = pgcObjectTypeService.createSubstanceType(parentType);
                rootSubstanceTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createSubstanceTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PGCSubstanceType childsubstanceType = new PGCSubstanceType();
            childsubstanceType.setParentType(parentType.getId());
            childsubstanceType.setName(name);
            childsubstanceType.setDescription(name);
            childsubstanceType.setAutoNumberSource(parentType.getAutoNumberSource());
            parentType = pgcObjectTypeService.createSubstanceType(childsubstanceType);
            parentType.getChildren().add(childsubstanceType);
            if (restOfPath != null) {
                return parentType = createSubstanceTypeByPath(childsubstanceType, restOfPath);
            } else {
                return childsubstanceType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createSubstanceTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }


    public void importSubstances(TableData tableData) throws ParseException {
        this.loadSubstanceClassificationTree();
        Map<String, PGCSubstance> dbSubstanceMap = new LinkedHashMap();
        Map<String, PGCSubstanceType> dbSubstanceTypesMap = new LinkedHashMap();
        List<PGCSubstance> dbSubstances = substanceRepository.findAll();
        List<PGCSubstanceType> dbSubstanceTypes = substanceTypeRepository.findAll();
        dbSubstanceMap = dbSubstances.stream().collect(Collectors.toMap(r -> r.getNumber(), r -> r));
        dbSubstanceTypesMap = dbSubstanceTypes.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        createSubstances(tableData, dbSubstanceMap, dbSubstanceTypesMap);
    }

    private List<PGCSubstance> createSubstances(TableData tableData, Map<String, PGCSubstance> dbSubstanceMap, Map<String, PGCSubstanceType> dbSubstanceTypesMap) {
        List<PGCSubstance> substances = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Substance Name") && stringListHashMap.containsKey("Substance Number") && stringListHashMap.containsKey("Substance Type") && stringListHashMap.containsKey("Type Path")) {
                String number = stringListHashMap.get("Substance Number");
                String name = stringListHashMap.get("Substance Name");
                if (number != null && !number.trim().equals("")) {
                    String substanceTypeName = stringListHashMap.get("Substance Type");
                    String typePath = stringListHashMap.get("Type Path");
                    if (substanceTypeName != null && substanceTypeName != "") {
                        PGCSubstanceType substanceType = dbSubstanceTypesMap.get(substanceTypeName);
                        if (substanceType != null) {
                            PGCSubstance specification = dbSubstanceMap.get(number);
                            if (specification != null) {
                                substances.add(specification);
                            } else {
                                PGCSubstance pgcSpecification = createSubstance(i, number, substanceType, stringListHashMap);
                                substances.add(pgcSpecification);
                            }

                        } else {
                            if (typePath != null && typePath != "") {

                                PGCSubstanceType pgcSubstanceType = this.getSubstanceType(typePath);
                                if (pgcSubstanceType != null) {
                                    dbSubstanceTypesMap.put(pgcSubstanceType.getName(), pgcSubstanceType);
                                    PGCSubstance substance = dbSubstanceMap.get(name);
                                    if (substance != null) {
                                        substances.add(substance);
                                    } else {
                                        PGCSubstance pgcSubstance = createSubstance(i, number, pgcSubstanceType, stringListHashMap);
                                        substances.add(pgcSubstance);
                                    }
                                }
                            }
                        }
                    } else {

                        if (typePath != null && typePath != "") {
                            PGCSubstanceType pgcSubstanceType = this.getSubstanceType(typePath);
                            if (pgcSubstanceType != null) {
                                dbSubstanceTypesMap.put(pgcSubstanceType.getName(), pgcSubstanceType);
                                PGCSubstance substance = dbSubstanceMap.get(name);
                                if (substance != null) {
                                    substances.add(substance);
                                } else {
                                    PGCSubstance pgcSubstance = createSubstance(i, number, pgcSubstanceType, stringListHashMap);
                                    substances.add(pgcSubstance);
                                }
                            }

                        } else {
                            throw new CassiniException(messageSource.getMessage("please_provide_substance_type_path_for_row_number" + (i),
                                    null, "Please provide Substance Type Path or Substance Type for row number:" + (i), LocaleContextHolder.getLocale()));
                        }
                    }
                } else {
                    substances.add(null);
                }

            } else {
                throw new CassiniException("Please provide Substance Name,Substance Number and Substance Type Column also");
            }

            i++;
        }

        if (substances.size() > 0) {
            substances = substanceRepository.save(substances);
        }

        return substances;
    }

    /*
    * Create Substances
    * */
    private PGCSubstance createSubstance(Integer i, String number, PGCSubstanceType substanceType, RowData stringListHashMap) {
        String name = stringListHashMap.get("Substance Name");
        String desc = stringListHashMap.get("Substance Description");
        String casNumber = stringListHashMap.get("CAS Number");
        PGCSubstance substance = new PGCSubstance();
        substance.setType(substanceType);
        substance.setName(name);
        substance.setDescription(desc);
        substance.setNumber(number);
        substance.setCasNumber(casNumber);
//        substance = substanceRepository.save(substance);
        return substance;
    }


}
