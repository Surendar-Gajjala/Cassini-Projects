package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.plm.LifeCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class IndividualItemsImporter {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    ItemTypeService itemTypeService;
    @Autowired
    private LovService lovService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private LifeCycleService lifecycleService;

    private static AutoNumber autoNumber;
    private static Lov revSeq;
    private PLMLifeCycle lifeCycle;


    public static ConcurrentMap<String, PLMItemType> rootItemTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMItemType> itemTypesMapByPath = new ConcurrentHashMap<>();


    private void getDefaultItemNumberSource() {
        autoNumber = autoNumberService.getByName("Default Item Number Source");
    }

    private void getDefaultRevisionSequence() {
        revSeq = lovService.getLovByName("Default Revision Sequence");
    }

    private void getDefaultItemLifecycle() {
        lifeCycle = lifecycleService.findLifecycleByName("Default Lifecycle");
    }

    @Transactional
    public void importItems(TableData headerValueMap) throws ParseException {
        loadItemClassificationTree();
        Map<String, PLMItem> itemMap = new ConcurrentHashMap();
        Map<String, PLMItem> dbItemMap = new LinkedHashMap();
        Map<String, PLMItemRevision> plmItemRevisionMap = new ConcurrentHashMap();
        Map<Integer, PLMItemRevision> dbPlmItemRevisionMap1 = new ConcurrentHashMap();
        Map<String, Integer> itemNumberIdMap = new ConcurrentHashMap();
        Map<String, AutoNumber> autoNumberMap = new ConcurrentHashMap();
        List<PLMItem> dbItems = itemRepository.findAll();
        List<PLMItemRevision> dbItemRevisions = itemRevisionRepository.findAll();
        List<AutoNumber> dbAutoNumbers = autoNumberRepository.findAll();
        dbItemMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x));
        dbPlmItemRevisionMap1 = dbItemRevisions.stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
        autoNumberMap = dbAutoNumbers.stream().collect(Collectors.toMap(a -> a.getName(), a -> a));
        itemNumberIdMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x.getId()));
        for (PLMItem item : dbItems) {
            plmItemRevisionMap.put(item.getItemNumber(), dbPlmItemRevisionMap1.get(item.getLatestRevision()));
        }
        this.createItems(headerValueMap, itemMap, plmItemRevisionMap, itemNumberIdMap, dbItemMap,
                dbPlmItemRevisionMap1, autoNumberMap);

    }

    private List<PLMItem> createItems(TableData tableData, Map<String, PLMItem> itemMap, Map<String, PLMItemRevision> plmItemRevisionMap, Map<String, Integer> itemNumberIdMap,
                                      Map<String, PLMItem> dbItemMap, Map<Integer, PLMItemRevision> plmItemRevisionMap1, Map<String, AutoNumber> autoNumberMap) throws ParseException {
        List<RowData> rows = tableData.getRows();
        Map<String, PLMItemType> dbItemTypeMap = new ConcurrentHashMap();
        List<PLMItemType> dbItemTypes = itemTypeRepository.findAll();
        dbItemTypeMap = dbItemTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        List<PLMItem> items2 = new LinkedList<PLMItem>();
        int i = 0;
        List<PLMItemAttribute> itemAttributes = new LinkedList<>();
        List<String> attributes = new LinkedList<>();
        for (RowData stringListHashMap : rows) {
            for (Object val : stringListHashMap.keySet()) {
                if (val != null && val != "") {
                    if (val.toString().toLowerCase().startsWith("attribute.")) {
                        attributes.add(val.toString());
                    }

                }

            }
            if (stringListHashMap.containsKey("Item Number") && (stringListHashMap.containsKey("Item Type") || stringListHashMap.containsKey("Type Path"))) {
                String itemName = stringListHashMap.get("Item Name");
                if (itemName == null || itemName == "") {
                    throw new CassiniException("Please provide Item Name  for row number:" + (i));
                }
                String number = stringListHashMap.get("Item Number");
                String path = stringListHashMap.get("Type Path");
                String typeClass = stringListHashMap.get("Type Class");
                String typeName = stringListHashMap.get("Item Type");
                ItemClass itemClass = ItemClass.valueOf(typeClass.toUpperCase());
                PLMItemType plmItemType = null;
                if (typeName != null && typeName != "") {
                    PLMItemType itemType = dbItemTypeMap.get(typeName);
                    if (itemType != null) {
                        plmItemType = itemType;
                    } else {
                        if (path != null && path != "") {
                            PLMItemType itemTypeByPath = this.getItemTypes(path, itemClass);
                            if (itemTypeByPath != null) {
                                plmItemType = itemTypeByPath;
                                dbItemTypeMap.put(plmItemType.getName(), plmItemType);
                            }
                        } else {
                            throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
                                    null, "Please provide valid Item Type or Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
                        }
                    }
                } else if (path != null && path != "") {
                    if (path != null && path != "") {
                        PLMItemType itemTypeByPath = this.getItemTypes(path, itemClass);
                        if (itemTypeByPath != null) {
                            plmItemType = itemTypeByPath;
                            dbItemTypeMap.put(plmItemType.getName(), plmItemType);
                        }
                    } else {
                        throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
                                null, "Please provide valid Item Type or Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
                    }
                }

                if (plmItemType != null) {
                    if (number == null || number.trim().equals("")) {
                        number = importer.getNextNumberWithoutUpdate(plmItemType.getItemNumberSource().getName(), autoNumberMap);
                    }
                    PLMItem item = dbItemMap.get(number);
                    if (item == null) item = itemMap.get(number);
                    if (item == null) {
                        this.addItem(i, number, plmItemType, stringListHashMap, itemMap, plmItemRevisionMap, autoNumberMap, dbItemMap);
                    }


                }


            } else if (stringListHashMap.containsKey("Type Class")) {
                String number = stringListHashMap.get("Item Number");
                String clss = stringListHashMap.get("Type Class");
                if (clss != null && clss != "") {
                    PLMItemType plmItemType = dbItemTypeMap.get(clss);
                    if (plmItemType != null) {
                        if (number == null || number.trim().equals("")) {
                            number = importer.getNextNumberWithoutUpdate(plmItemType.getItemNumberSource().getName(), autoNumberMap);
                        }
                        PLMItem item = dbItemMap.get(number);
                        if (item == null) item = itemMap.get(number);
                        if (item != null) {
                            itemMap.put(item.getItemNumber(), item);
                        } else {
                            this.addItem(i, number, plmItemType, stringListHashMap, itemMap, plmItemRevisionMap, autoNumberMap, dbItemMap);
                        }

                    }

                }


            } else {
                throw new CassiniException("Please provide Item Number and Type Path Column also");
            }
            i++;
        }


        items2 = new LinkedList<>(itemMap.values());
        items2 = itemRepository.save(items2);
        for (PLMItem item : items2) itemNumberIdMap.put(item.getItemNumber(), item.getId());
        List<PLMItemRevision> itemRevisions = new LinkedList<>();
        for (String str : itemNumberIdMap.keySet()) {
            PLMItemRevision itemRevision = plmItemRevisionMap.get(str);
            Integer itemId = itemNumberIdMap.get(str);
            itemRevision.setItemMaster(itemId);
            itemRevisions.add(itemRevision);
        }

        itemRevisions = itemRevisionRepository.save(itemRevisions);
        importer.sleep(1000);
        List<PLMItem> plmItems = new LinkedList();
        for (PLMItem item : items2) {
            PLMItemRevision itemRevision = itemRevisions.stream().filter(revision -> item.getId().equals(revision.getItemMaster())).findFirst().orElse(null);
            item.setLatestRevision(itemRevision.getId());
            plmItems.add(item);
            plmItemRevisionMap1.put(itemRevision.getId(), itemRevision);
        }
        plmItems = itemRepository.save(plmItems);
        createItemAttributes(plmItems, rows, attributes);
        autoNumberRepository.save(autoNumberMap.values());
        importer.sleep(1000);
        return plmItems;
    }

    public Date parseDate(String stringDate, String format) throws ParseException {
        Date date = new SimpleDateFormat(format).parse(stringDate);
        return date;
    }

    public void addItem(Integer i, String number, PLMItemType plmItemType, RowData stringListHashMap,
                        Map<String, PLMItem> itemMap, Map<String, PLMItemRevision> plmItemRevisionMap, Map<String, AutoNumber> autoNumberMap, Map<String, PLMItem> dbItemMap) throws ParseException {

        String name = stringListHashMap.get("Item Name".trim());
        String description = stringListHashMap.get("Item Description".trim());
        String revision = stringListHashMap.get("Revision".trim());
        String lifeCycle = stringListHashMap.get("Item LifeCycle".trim());
        String unit = stringListHashMap.get("Units");
        String makeorBuy = stringListHashMap.get("Make/Buy");
        String fromDate = stringListHashMap.get("Effective From");
        String toDate = stringListHashMap.get("Effective To");
        Boolean conf = false;
        PLMItem item = new PLMItem();
        PLMItemRevision itemRevision = new PLMItemRevision();
        String configuration = stringListHashMap.get("Configuration");
        if (configuration != null && configuration != "") {
            if (configuration.toLowerCase().equals("normal")) {
                conf = false;
            } else if (configuration.toLowerCase().equals("configured")) {
                if (stringListHashMap.containsKey("Instance Of".trim())) {
                    String instanceOf = stringListHashMap.get("Instance Of".trim());
                    if (instanceOf != null && instanceOf != "") {
                        PLMItem existedItem = dbItemMap.get(instanceOf);
                        if (existedItem != null) {
                            item.setInstance(existedItem.getId());
                            itemRevision.setInstance(existedItem.getLatestRevision());
                            conf = true;
                        } else {
                            throw new CassiniException("Instance does not exist for row: " + i);
                        }
                    } else {
                        throw new CassiniException("Please provide Instance Of for row: " + i);
                    }
                } else {
                    throw new CassiniException("Please provide Instance Of Column also");
                }
            } else if (configuration.toLowerCase().equals("configurable")) {
                conf = true;
            }
        } else {
            conf = false;
        }

        if (fromDate != null && fromDate != "") {
            itemRevision.setEffectiveFrom(parseDate(fromDate, "dd/MM/yyyy"));

        }
        if (toDate != null && toDate != "") {
            itemRevision.setEffectiveTo(parseDate(toDate, "dd/MM/yyyy"));
        }
        item.setItemType(plmItemType);
        item.setItemNumber(number);
        item.setItemName(name);
        item.setDescription(description != null ? description : name);
        item.setUnits(unit != null ? unit : "EA");
        item.setConfigurable(conf);
        item.setMakeOrBuy(MakeOrBuy.valueOf(makeorBuy.toUpperCase()));
        saveNextNumber(item.getItemType().getItemNumberSource().getName(), item.getItemNumber(), autoNumberMap);
        itemMap.put(item.getItemNumber(), item);
        itemRevision.setRevision(revision != null ? revision : "-");
        PLMLifeCyclePhase phase = this.importer.getPhaseByName(plmItemType.getLifecycle(), lifeCycle != null ? lifeCycle : "In Work");
        itemRevision.setLifeCyclePhase(phase);
        itemRevision.setHasBom(false);
        itemRevision.setHasFiles(false);
        plmItemRevisionMap.put(item.getItemNumber(), itemRevision);
        item.setLatestRevision(itemRevision.getId());
    }

    public void saveNextNumber(String autoNumber, String number, Map<String, AutoNumber> autoNumberMap) {
        AutoNumber auto = autoNumberMap.get(autoNumber);
        String[] strs = number.split("-");
        if (strs.length >= 2 && auto.getPrefix().equalsIgnoreCase(strs[0] + "-")) {
            strs[1] = strs[1].replaceAll("[^0-9]", "");
            String strs11 = "^0+(?!$)";
            Integer strPattern1 = Integer.valueOf(Integer.parseInt(strs[1].replaceAll(strs11, "")));
            if (auto.getNextNumber().intValue() < strPattern1.intValue()) {
                auto.setNextNumber(Integer.valueOf(strPattern1.intValue() + 1));
            } else if (auto.getNextNumber().intValue() == strPattern1.intValue()) {
                auto.next();
            }
        } else {
            String[] strs1 = number.split("(?<=\\D)(?=\\d)");
            if (strs1.length >= 2) {
                strs1[1] = strs1[1].replaceAll("[^0-9]", "");
                String strPattern = "^0+(?!$)";
                Integer val1 = Integer.valueOf(Integer.parseInt(strs1[1].replaceAll(strPattern, "")));
                if (auto.getNextNumber().intValue() < val1.intValue()) {
                    auto.setNextNumber(Integer.valueOf(val1.intValue() + 1));
                } else if (auto.getNextNumber().intValue() == val1.intValue()) {
                    auto.next();
                }
            }
        }

//        this.autoNumberRepository.save(auto);
        autoNumberMap.put(autoNumber, auto);
    }

    private String getDataType(String val) {
        String dataType = null;
        // checking for Integer
        if (val.matches("\\d+")) {
            dataType = "Integer";
        }
        // checking for floating point numbers
        else if (val.matches("\\d*[.]\\d+")) {
            dataType = "Double";
        }

        // checking for date format dd/mm/yyyy
        else if (val.matches(
                "\\d{2}[/]\\d{2}[/]\\d{4}")) {
            dataType = "Date";
        }
        // checking for time format HH:MM
        else if (val.matches(
                "([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            dataType = "Time";
        }
        // checking for timestamp format HH:MM
        else if (val.matches(
                "[0-9]{1,4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}")) {
            dataType = "TimeStamp";
        }
        // checking for Boolean
        else if (val.matches("^(true|false)$")) {
            dataType = "Boolean";
        }
        // checking for Currency
        else if (val.matches("^(?:(?![,0-9]{14})\\d{1,3}(?:,\\d{3})*(?:\\.\\d{1,2})?|(?![.0-9]{14})\\d{1,3}(?:\\.\\d{3})*(?:\\,\\d{1,2})?)$")) {
            dataType = "Currency";
        }

        // checking for String
        else {
            dataType = "String";
        }
        return dataType;
    }

    public void createItemAttributes(List<PLMItem> items, List<RowData> rows, List<String> attributes) throws ParseException {
        List<PLMItemAttribute> itemAttributes = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : rows) {
            List<PLMItemAttribute> newAttributes = new LinkedList<>();
            if (attributes.size() > 0) {
                for (int j = 0; j < attributes.size(); j++) {
                    PLMItemAttribute itemAttribute = new PLMItemAttribute();
                    String attributeColName = attributes.get(j);
                    String attributeName = attributeColName.split("\\.")[1];
                    if (attributeName != null && attributeName != "") {
                        String attributeValue = stringListHashMap.get(attributeColName);
                        if (attributeValue != null && attributeValue != "") {
                            PLMItem plmItem = items.get(i);
                            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(plmItem.getItemType().getId(), attributeName);
                            String dataType = getDataType(attributeValue);
                            if (itemTypeAttribute != null) {
                                ObjectAttributeId objAttr = new ObjectAttributeId();
                                objAttr.setObjectId(plmItem.getId());
                                objAttr.setAttributeDef(itemTypeAttribute.getId());
                                if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
                                    if (dataType.equals("Integer")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setIntegerValue(Integer.parseInt(attributeValue));
                                    } else {
                                        returnCommonException(i);
                                    }


                                } else if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
                                    if (dataType.equals("Double")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setDoubleValue(new Double(attributeValue));
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
                                    if (dataType.equals("Date")) {
                                        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                        Date date = format.parse(attributeValue);
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setDateValue(date);
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("TIME")) {

                                    if (dataType.equals("Time")) {
                                        itemAttribute.setId(objAttr);
                                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                        java.sql.Time timeValue = new java.sql.Time(format.parse(attributeValue).getTime());
                                        itemAttribute.setTimeValue(timeValue);
                                    } else {
                                        returnCommonException(i);
                                    }
                                } else if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {

                                    if (dataType.equals("TimeStamp")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setTimestampValue(Timestamp.valueOf(attributeValue));
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
                                    if (dataType.equals("Boolean")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setBooleanValue(Boolean.valueOf(attributeValue));
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
                                    if (dataType.equals("Currency")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setCurrencyValue(new Double(attributeValue));
                                    } else {
                                        returnCommonException(i);
                                    }


                                } else if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
                                    if (dataType.equals("String")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setStringValue(attributeValue);
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
                                    if (dataType.equals("String")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setStringValue(attributeValue);
                                    } else {
                                        returnCommonException(i);
                                    }

                                } else if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {

                                    if (dataType.equals("String")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setStringValue(attributeValue);
                                    } else {
                                        returnCommonException(i);
                                    }
                                } else if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
                                    if (dataType.equals("String")) {
                                        itemAttribute.setId(objAttr);
                                        itemAttribute.setStringValue(attributeValue);
                                    } else {
                                        returnCommonException(i);
                                    }
                                }
                                newAttributes.add(itemAttribute);
                            }
                        }

                    }
                    //newAttributes.add(itemAttribute);
                }

            }
            if (newAttributes.size() > 0) {
                itemAttributes.addAll(newAttributes);
            }
            i++;

        }
        itemAttributeRepository.save(itemAttributes);

    }

    private void returnCommonException(Integer i) {
        throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
                null, "Please provide proper Attribute value based on Data type for row number:" + (i), LocaleContextHolder.getLocale()));

    }

    public void loadItemClassificationTree() {
        getDefaultItemLifecycle();
        getDefaultItemNumberSource();
        getDefaultRevisionSequence();
        rootItemTypesMap = new ConcurrentHashMap<>();
        itemTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMItemType> rootTypes = itemTypeService.getClassificationTree();
        for (PLMItemType rootType : rootTypes) {
            rootItemTypesMap.put(rootType.getName(), rootType);
            itemTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public PLMItemType getItemTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMItemType itemType = rootItemTypesMap.get(name);
            if (itemType != null) {
                return itemType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return itemType;
            }

        } else {
            name = path;
            return rootItemTypesMap.get(name);
        }
    }

    public PLMItemType createItemTypeByPath(PLMItemType parentType, String path, ItemClass itemClass) {
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
            parentType = rootItemTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMItemType();
                parentType.setItemClass(itemClass != null ? itemClass : ItemClass.OTHER);
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setItemNumberSource(autoNumber);
                parentType.setLifecycle(lifeCycle);
                parentType.setRevisionSequence(revSeq);
                parentType = itemTypeService.create(parentType);
                rootItemTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createItemTypeByPath(parentType, restOfPath, parentType.getItemClass());
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMItemType childItemType = new PLMItemType();
            childItemType.setItemClass(parentType.getItemClass());
            childItemType.setParentType(parentType.getId());
            childItemType.setName(name);
            childItemType.setDescription(name);
            childItemType.setItemNumberSource(parentType.getItemNumberSource());
            childItemType.setLifecycle(parentType.getLifecycle());
            childItemType.setRevisionSequence(parentType.getRevisionSequence());
            childItemType = itemTypeService.create(childItemType);
            parentType.getChildren().add(childItemType);
            if (restOfPath != null) {
                return parentType = createItemTypeByPath(childItemType, restOfPath, childItemType.getItemClass());
            } else {
                return childItemType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createItemTypeByPath(parentType, restOfPath, parentType.getItemClass());
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }

    public PLMItemType getItemTypes(String path, ItemClass itemClass) {
        PLMItemType plmItemType = itemTypesMapByPath.get(path);
        if (plmItemType == null) {
            plmItemType = getItemTypeByPath(path);
            if (plmItemType == null) {
                plmItemType = createItemTypeByPath(null, path, itemClass);
            }
            itemTypesMapByPath.put(path, plmItemType);
        }

        return plmItemType;
    }


}