package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.mes.MESManpower;
import com.cassinisys.plm.model.mes.MESManpowerType;
import com.cassinisys.plm.repo.mes.MESManpowerRepository;
import com.cassinisys.plm.repo.mes.ManpowerTypeRepository;
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

@Service
@Scope("prototype")
public class ManpowersImporter {

    @Autowired
    private MESManpowerRepository manpowerRepository;
    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private MESObjectTypeService mesObjectTypeService;

    public static ConcurrentMap<String, MESManpowerType> rootManpowerTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, MESManpowerType> manpowerTypesMapByPath = new ConcurrentHashMap<>();

    Map<String, AutoNumber> autoNumberMap = new HashMap<>();


    public void importManpowers(TableData tableData) throws ParseException {
        this.loadManpowerClassificationTree();
        autoNumberMap = new HashMap<>();
        Map<String, MESManpower> dbManpowerMap = new LinkedHashMap();
        Map<String, MESManpowerType> dbManpowerTypeMap = new LinkedHashMap();
        List<MESManpower> manpowers = manpowerRepository.findAll();
        List<MESManpowerType> mesManpowerTypes = manpowerTypeRepository.findAll();
        dbManpowerMap = manpowers.stream().collect(Collectors.toMap(x -> x.getNumber(), x -> x));
        dbManpowerTypeMap = mesManpowerTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        autoNumberMap = importer.getCommonAutoNumbers();
        List<MESManpower> manpowers1 = createManpowers(tableData, dbManpowerMap, dbManpowerTypeMap);

    }


    private List<MESManpower> createManpowers(TableData tableData, Map<String, MESManpower> dbManpowersMap, Map<String, MESManpowerType> dbManpowerTypesMap) throws ParseException {
        List<MESManpower> manpowers2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Manpower Name");
            if (name != null && !name.trim().equals("")) {
                String manpowerTypeName = stringListHashMap.get("Manpower Type".trim());
                String manpowerTypePath = stringListHashMap.get("Type Path".trim());
                String number = stringListHashMap.get("Manpower Number");
                if ((manpowerTypeName == null || manpowerTypeName == "")) {
                    MESManpowerType manpowerType = dbManpowerTypesMap.get(manpowerTypeName);
                    if (manpowerType != null) {
                        MESManpower manpower = dbManpowersMap.get(number);
                        if (manpower == null) {
                            manpowers2.add(createManpower(i, number, manpowerType, stringListHashMap, dbManpowersMap));
                        } else {
                            updateManpower(i, manpower, stringListHashMap);
                            manpowers2.add(manpower);
                        }
                    } else {
                        manpowers2.add(createManpowerByPath(i, number, manpowerTypePath, dbManpowersMap, dbManpowerTypesMap, stringListHashMap));
                    }
                } else {
                    manpowers2.add(createManpowerByPath(i, number, manpowerTypePath, dbManpowersMap, dbManpowerTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Manpower Name for row :" + i);
            }
        }
        if (manpowers2.size() > 0) {
            manpowerRepository.save(manpowers2);
        }
        return manpowers2;
    }


    private MESManpower createManpowerByPath(int i, String number, String manpowerTypePath, Map<String, MESManpower> dbManpowersMap, Map<String, MESManpowerType> dbManpowerTypesMap, RowData stringListHashMap) {
        MESManpower Manpower = null;
        if (manpowerTypePath != null && manpowerTypePath != "") {
            MESManpowerType MESManpowerType = this.getManpowerTypes(manpowerTypePath);
            if (MESManpowerType != null) {
                dbManpowerTypesMap.put(MESManpowerType.getName(), MESManpowerType);
                Manpower = dbManpowersMap.get(number);
                if (Manpower == null)
                    Manpower = createManpower(i, number, MESManpowerType, stringListHashMap, dbManpowersMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                        null, "Please provide Manpower Type or Manpower Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_equipment_line_type_for_row_number" + (i),
                    null, "Please provide Manpower Type or Manpower Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return Manpower;
    }
    
    
    
    private MESManpower createManpower(Integer i, String name, MESManpowerType manpowerType, RowData stringListHashMap, Map<String, MESManpower> dbManpowersMap) {
        MESManpower manpower = new MESManpower();
        String manpowerNumber = stringListHashMap.get("Manpower Number".trim());
        String manpowerName = stringListHashMap.get("Manpower Name".trim());
        String manpowerDescription = stringListHashMap.get("Manpower Description".trim());
        // String newPersom = stringListHashMap.get("New Person".trim());
        // String firstName = stringListHashMap.get("First Name".trim());
        // String lastName = stringListHashMap.get("Last Name".trim());
        // String phoneNumber = stringListHashMap.get("Phone Number".trim());
        // String email = stringListHashMap.get("E-mail".trim());
        if (manpowerNumber == null || manpowerNumber.trim().equals("")) {
            manpowerNumber = importer.getNextNumberWithoutUpdate(manpowerType.getAutoNumberSource().getName(), autoNumberMap);
        }
        manpower.setType(manpowerType);
        manpower.setNumber(manpowerNumber);
        manpower.setName(manpowerName);
        manpower.setDescription(manpowerDescription);
        importer.saveNextNumber(manpowerType.getAutoNumberSource().getName(), manpower.getNumber(), autoNumberMap);
        // Person person = new Person();
        // if (newPersom.equals("Yes")) {
        //     person.setFirstName(firstName);
        //     person.setLastName(lastName);
        //     person.setPhoneMobile(phoneNumber);
        //     person.setEmail(email);
        //     person.setPersonType(1);
        //     person = personRepository.save(person);
        //     manpower.setPerson(person.getId());
        // } else {
        //     manpower.setPerson(personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).getId());
        // }
//        manpower = manpowerRepository.save(manpower);
        dbManpowersMap.put(manpower.getNumber(),manpower);
        return manpower;
    }



    public void loadManpowerClassificationTree() {
        manpowerTypesMapByPath = new ConcurrentHashMap<>();
        List<MESManpowerType> rootTypes = mesObjectTypeService.getManpowerTypeTree();
        for (MESManpowerType rootType : rootTypes) {
            rootManpowerTypesMap.put(rootType.getName(), rootType);
            manpowerTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public MESManpowerType getManpowerTypes(String path) {
        MESManpowerType mesManpowerType = manpowerTypesMapByPath.get(path);
        if (mesManpowerType == null) {
            mesManpowerType = getManpowerTypeByPath(path);
            if (mesManpowerType == null) {
                mesManpowerType = createManpowerTypeByPath(null, path);
            }
            manpowerTypesMapByPath.put(path, mesManpowerType);
        }

        return mesManpowerType;
    }

    private MESManpowerType getManpowerTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            MESManpowerType itemType = rootManpowerTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }
        } else {
            name = path;
            return rootManpowerTypesMap.get(name);
        }
    }



    private MESManpowerType createManpowerTypeByPath(MESManpowerType parentType, String path) {
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
            parentType = rootManpowerTypesMap.get(name);
            if (parentType == null) {
                parentType = new MESManpowerType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setAutoNumberSource(plantsImporter.getDefaultPlantNumberSource("Default Manpower Number Source"));
                parentType = manpowerTypeRepository.save(parentType);
                rootManpowerTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createManpowerTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            MESManpowerType childItemType = new MESManpowerType();
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setAutoNumberSource(parentType.getAutoNumberSource());
            childItemType = manpowerTypeRepository.save(childItemType);
            parentType.getChildrens().add(childItemType);
            if (restOfPath != null) {
                parentType = createManpowerTypeByPath(childItemType, restOfPath);
            } else {
                return childItemType;
            }

        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createManpowerTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }





    private MESManpower updateManpower(Integer i, MESManpower mesManpower, RowData stringListHashMap) {
        String manpowerName = stringListHashMap.get("Manpower Name".trim());
        String manpowerDescription = stringListHashMap.get("Manpower Description".trim());

        mesManpower.setName(manpowerName);
        mesManpower.setDescription(manpowerDescription);

        return mesManpower;
    }



}
