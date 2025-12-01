package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.MakeOrBuy;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.service.classification.ManufacturerPartTypeService;
import com.cassinisys.plm.service.classification.ManufacturerTypeService;
import com.cassinisys.plm.service.plm.LifeCycleService;
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
import java.util.stream.Stream;

@Service
@Scope("prototype")
public class IndividualMfrAndMfrPartsImporter {
    @Autowired
    private IndividualItemsImporter individualItemsImporter;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;

    @Autowired
    private LifeCycleService lifecycleService;

    @Autowired
    private ManufacturerTypeService manufacturerTypeService;
    @Autowired
    private ManufacturerPartTypeService manufacturerPartTypeService;
    public static ConcurrentMap<String, PLMManufacturerType> rootMfrTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMManufacturerType> mfrTypesMapByPath = new ConcurrentHashMap<>();

    public static ConcurrentMap<String, PLMManufacturerPartType> rootMfrPartTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMManufacturerPartType> mfrPartTypesMapByPath = new ConcurrentHashMap<>();
    private Map<String, PLMItemManufacturerPart> dpItemManufacturersMap = new ConcurrentHashMap();


    private static PLMLifeCycle plmMfrLifeCycle;
    private static PLMLifeCycle plmMfrPartLifeCycle;


    private void getDefaultMfrLifecycle() {
        plmMfrLifeCycle = lifecycleService.findLifecycleByName("Default Manufacturer Lifecycle");
    }

    public void getDefaultMfrPartLifecycle() {
        plmMfrPartLifeCycle = lifecycleService.findLifecycleByName("Default ManufacturerPart Lifecycle");
    }

    public void loadMfrClassificationTree() {
        getDefaultMfrLifecycle();
        rootMfrTypesMap = new ConcurrentHashMap<>();
        mfrTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMManufacturerType> rootTypes = manufacturerTypeService.getClassificationTree();
        for (PLMManufacturerType rootType : rootTypes) {
            rootMfrTypesMap.put(rootType.getName(), rootType);
            mfrTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public PLMManufacturerType getMfrTypeByPath(String path) {
        PLMManufacturerType plmMfrType = null;
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMManufacturerType mfrType = rootMfrTypesMap.get(name);
            if (mfrType != null) {
                return mfrType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return mfrType;
            }


        } else {
            name = path;
            return rootMfrTypesMap.get(name);
        }
    }

    public PLMManufacturerType getMfrType(String typePath) {
        PLMManufacturerType mfrType = mfrTypesMapByPath.get(typePath);
        if (mfrType == null) {
            mfrType = getMfrTypeByPath(typePath);
            if (mfrType == null) {
                mfrType = createMfrTypeByPath(null, typePath);
            }

            mfrTypesMapByPath.put(typePath, mfrType);
        }
        return mfrType;
    }

    public PLMManufacturerType createMfrTypeByPath(PLMManufacturerType parentType, String path) {
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
            parentType = rootMfrTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMManufacturerType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setLifecycle(plmMfrLifeCycle);
                parentType = manufacturerTypeService.create(parentType);
                rootMfrTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMfrTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMManufacturerType childmfrType = new PLMManufacturerType();
            childmfrType.setParentType(parentType.getId());
            childmfrType.setName(name);
            childmfrType.setDescription(name);
            childmfrType.setLifecycle(parentType.getLifecycle());
            childmfrType = manufacturerTypeService.create(childmfrType);
            parentType.getChildren().add(childmfrType);
            if (restOfPath != null) {
                return parentType = createMfrTypeByPath(childmfrType, restOfPath);
            } else {
                return childmfrType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMfrTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }

    /*
* Manufacturer Type Creation from Path
* */

    public void loadMfrPartClassificationTree() {
        getDefaultMfrPartLifecycle();
        rootMfrPartTypesMap = new ConcurrentHashMap<>();
        mfrPartTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMManufacturerPartType> rootTypes = manufacturerPartTypeService.getClassificationTree();
        for (PLMManufacturerPartType rootType : rootTypes) {
            rootMfrPartTypesMap.put(rootType.getName(), rootType);
            mfrPartTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    public PLMManufacturerPartType getMfrPartTypeByPath(String path) {
        PLMManufacturerPartType plmMfrPartType = null;
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMManufacturerPartType mfrType = rootMfrPartTypesMap.get(name);
            if (mfrType != null) {
                return mfrType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return mfrType;
            }

        } else {
            name = path;
            return rootMfrPartTypesMap.get(name);
        }
    }

    public PLMManufacturerPartType getMfrPartType(String typePath) {
        PLMManufacturerPartType mfrType = mfrPartTypesMapByPath.get(typePath);
        if (mfrType == null) {
            mfrType = getMfrPartTypeByPath(typePath);
            if (mfrType == null) {
                mfrType = createMfrPartTypeByPath(null, typePath);
            }

            mfrPartTypesMapByPath.put(typePath, mfrType);
        }
        return mfrType;
    }

    public PLMManufacturerPartType createMfrPartTypeByPath(PLMManufacturerPartType parentType, String path) {
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
            parentType = rootMfrPartTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMManufacturerPartType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setLifecycle(plmMfrPartLifeCycle);
                parentType = manufacturerPartTypeService.create(parentType);
                rootMfrPartTypesMap.put(name, parentType);
            }
            if (restOfPath != null) {
                parentType = createMfrPartTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMManufacturerPartType childmfrType = new PLMManufacturerPartType();
            childmfrType.setParentType(parentType.getId());
            childmfrType.setName(name);
            childmfrType.setDescription(name);
            childmfrType.setLifecycle(parentType.getLifecycle());
            childmfrType = manufacturerPartTypeService.create(childmfrType);
            parentType.getChildren().add(childmfrType);
            if (restOfPath != null) {
                parentType = createMfrPartTypeByPath(childmfrType, restOfPath);
            } else {
                return childmfrType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            parentType = parentType.getChildTypeByPath(name);
            if (restOfPath != null) {
                parentType = createMfrPartTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }

        return parentType;
    }


    public void importMfrAndMfrParts(TableData tableData) throws ParseException {
        this.loadMfrClassificationTree();
        this.loadMfrPartClassificationTree();
        Map<String, PLMManufacturerType> dbManufacturerTypesMap = new ConcurrentHashMap();
        Map<String, PLMManufacturer> dbManufacturersMap = new ConcurrentHashMap();
        Map<String, PLMManufacturerPart> dbManufacturerPartsMap = new ConcurrentHashMap();
        Map<String, PLMManufacturerPartType> dbManufacturerPartTypesMap = new ConcurrentHashMap();
        dpItemManufacturersMap = new ConcurrentHashMap();
        Map<String, PLMItem> dbItemMap = new ConcurrentHashMap();
        List<PLMItem> dbItems = itemRepository.findAll();
        List<PLMManufacturer> dbManufacturers = manufacturerRepository.findAll();
        List<PLMManufacturerPart> dbManufacturerParts = manufacturerPartRepository.findAll();
        List<PLMManufacturerType> dbManufacturerTypes = manufacturerTypeRepository.findAll();
        List<PLMManufacturerPartType> dbManufacturerPartTypes = manufacturerPartTypeRepository.findAll();
        List<PLMItemManufacturerPart> dbItemManufacturers = itemManufacturerPartRepository.findAll();
        dbManufacturerTypesMap = dbManufacturerTypes.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
        dbManufacturerPartTypesMap = dbManufacturerPartTypes.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        dbItemMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x));
        dbManufacturersMap = dbManufacturers.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        dbManufacturerPartsMap = dbManufacturerParts.stream().collect(Collectors.toMap(r -> r.getPartNumber(), r -> r));
        dpItemManufacturersMap = dbItemManufacturers.stream().collect(Collectors.toMap(r -> r.getItem().toString() + r.getManufacturerPart().getId().toString(), r -> r));
        List<PLMManufacturer> mfrs = createMfrs(tableData, dbManufacturersMap, dbManufacturerTypesMap);
        if (mfrs.size() > 0) {
            List<PLMManufacturerPart> mfrParts = createMfrParts(tableData, mfrs, dbManufacturerPartTypesMap, dbManufacturerPartsMap);
            if (mfrParts.size() > 0) {
                addMfrPartsToItems(dbItemMap, mfrParts, tableData);
            }
        }
    }

    private PLMManufacturer createMfrByPath(int i, String name, String typePath, Map<String, PLMManufacturer> dbManufacturersMap, Map<String, PLMManufacturerType> dbManufacturerTypesMap, RowData stringListHashMap) {
        PLMManufacturer mfr = null;
        if (typePath != null && typePath != "") {
            PLMManufacturerType plmMfrType = this.getMfrType(typePath);
            if (plmMfrType != null) {
                dbManufacturerTypesMap.put(plmMfrType.getName(), plmMfrType);
                mfr = dbManufacturersMap.get(name);
                if (mfr == null)
                    mfr = createMfr(i, name, plmMfrType, stringListHashMap, dbManufacturersMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_type_for_row_number" + (i),
                        null, "Please provide Manufacturer Type or Manufacturer Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_type_for_row_number" + (i),
                    null, "Please provide Manufacturer Type or Manufacturer Type Path for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return mfr;
    }

    private List<PLMManufacturer> createMfrs(TableData tableData, Map<String, PLMManufacturer> dbManufacturersMap, Map<String, PLMManufacturerType> dbManufacturerTypesMap) {
        List<PLMManufacturer> mfrs2 = new LinkedList<>();
        int size = tableData.getRows().size();
        for (int i = 0; i < size; i++) {
            RowData stringListHashMap = tableData.getRows().get(i);
            String name = stringListHashMap.get("Manufacturer");
            if (name != null && !name.trim().equals("")) {
                String manufacturerTypeName = stringListHashMap.get("Manufacturer Type");
                String typePath = stringListHashMap.get("Manufacturer Type Path");
                if (manufacturerTypeName != null && manufacturerTypeName != "") {
                    PLMManufacturerType manufacturerType = dbManufacturerTypesMap.get(manufacturerTypeName);
                    if (manufacturerType != null) {
                        PLMManufacturer manufacturer = dbManufacturersMap.get(name);
                        if (manufacturer == null) {
                            mfrs2.add(createMfr(i, name, manufacturerType, stringListHashMap, dbManufacturersMap));
                        } else {
                            mfrs2.add(manufacturer);

                        }

                    } else {
                        mfrs2.add(createMfrByPath(i, name, typePath, dbManufacturersMap, dbManufacturerTypesMap, stringListHashMap));
                    }
                } else {
                    mfrs2.add(createMfrByPath(i, name, typePath, dbManufacturersMap, dbManufacturerTypesMap, stringListHashMap));
                }
            } else {
                throw new CassiniException("Please provide Manufacturer for row :" + i);
            }
        }

        if (mfrs2.size() > 0) {
            manufacturerRepository.save(mfrs2);
        }

        return mfrs2;
    }


    private PLMManufacturer createMfr(Integer i, String name, PLMManufacturerType plmItemType, RowData stringListHashMap, Map<String, PLMManufacturer> dbManufacturersMap) {
        String description = stringListHashMap.get("Manufacturer Description");
        String phoneNo = stringListHashMap.get("Manufacturer PhoneNumber");
        String contactPerson = stringListHashMap.get("Manufacturer Contact Person");
        String mfrLifeCycle = stringListHashMap.get("Manufacturer Lifecycle".trim());
        PLMManufacturer mfr = new PLMManufacturer();
        mfr.setMfrType(plmItemType);
        mfr.setName(name);
        mfr.setPhoneNumber(phoneNo);
        mfr.setContactPerson(contactPerson);
        mfr.setDescription(description);
        PLMLifeCyclePhase phase = this.importer.getPhaseByName(plmItemType.getLifecycle(), mfrLifeCycle != null ? mfrLifeCycle : "Unqualified");
        if (phase != null) {
            mfr.setLifeCyclePhase(phase);
        }
        dbManufacturersMap.put(mfr.getName(), mfr);

        return mfr;
    }


    private PLMManufacturerPart createMfrPartByPath(int i, String name, String typePath, Integer manufacturer, Map<String, PLMManufacturerPart> dbManufacturersPartMap, Map<String, PLMManufacturerPartType> dbManufacturerTypesMap, RowData stringListHashMap) {
        PLMManufacturerPart mfrPart = null;
        if (typePath != null && typePath != "") {
            PLMManufacturerPartType plmMfrPartType = this.getMfrPartType(typePath);
            if (plmMfrPartType != null) {
                dbManufacturerTypesMap.put(plmMfrPartType.getName(), plmMfrPartType);
                mfrPart = dbManufacturersPartMap.get(name);
                if (mfrPart == null)
                    mfrPart = createMfrPart(i, name, plmMfrPartType, stringListHashMap, manufacturer, dbManufacturersPartMap);
            } else {
                throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_part_type_for_row_number" + (i),
                        null, "Please provide Manufacturer Part Type Path or Manufacturer Part Type for row number:" + (i), LocaleContextHolder.getLocale()));
            }
        } else {
            throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_part_type_for_row_number" + (i),
                    null, "Please provide Manufacturer Part Type Path or Manufacturer Part Type for row number:" + (i), LocaleContextHolder.getLocale()));
        }
        return mfrPart;
    }

    private List<PLMManufacturerPart> createMfrParts(TableData tableData, List<PLMManufacturer> manufacturers, Map<String, PLMManufacturerPartType> dbManufacturerPartTypesMap, Map<String, PLMManufacturerPart> dbManufacturerPartsMap) {
        List<PLMManufacturerPart> mfrs2 = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Manufacturer Part Number") && (stringListHashMap.containsKey("Manufacturer Part Type") || stringListHashMap.containsKey("Manufacturer Part Type Path"))) {
                String number = stringListHashMap.get("Manufacturer Part Number");
                if (number != null && !number.trim().equals("")) {
                    String manufacturerPartTypeName = stringListHashMap.get("Manufacturer Part Type");
                    String typePath = stringListHashMap.get("Manufacturer Part Type Path");
                    PLMManufacturer mfrObject = manufacturers.get(i);
                    if (manufacturerPartTypeName != null && manufacturerPartTypeName != "") {
                        PLMManufacturerPartType manufacturerPartType = dbManufacturerPartTypesMap.get(manufacturerPartTypeName);
                        if (mfrObject != null) {
                            Integer manufacturer = mfrObject.getId();
                            if (manufacturerPartType != null) {
                                PLMManufacturerPart manufacturerPart = dbManufacturerPartsMap.get(number);
                                if (manufacturerPart == null) {
                                    mfrs2.add(createMfrPart(i, number, manufacturerPartType, stringListHashMap, manufacturer, dbManufacturerPartsMap));
                                } else {
                                    mfrs2.add(manufacturerPart);
                                }

                            } else {
                                if (typePath != null && typePath != "") {
                                    mfrs2.add(createMfrPartByPath(i, number, typePath, manufacturer, dbManufacturerPartsMap, dbManufacturerPartTypesMap, stringListHashMap));

                                } else {
                                    throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_part_type_for_row_number" + (i),
                                            null, "Please provide Manufacturer Part Type Path or Manufacturer Part Type for row number:" + (i), LocaleContextHolder.getLocale()));
                                }

                            }

                        }
                    } else {
                        if (typePath != null && typePath != "") {
                            if (mfrObject != null) {
                                if (typePath != null && typePath != "") {
                                    Integer manufacturer = mfrObject.getId();
                                    mfrs2.add(createMfrPartByPath(i, number, typePath, manufacturer, dbManufacturerPartsMap, dbManufacturerPartTypesMap, stringListHashMap));

                                } else {
                                    throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_part_type_for_row_number" + (i),
                                            null, "Please provide Manufacturer Part Type Path or Manufacturer Part Type for row number:" + (i), LocaleContextHolder.getLocale()));
                                }
                            }
                        } else {
                            throw new CassiniException(messageSource.getMessage("please_provide_manufacturer_part_type_for_row_number" + (i),
                                    null, "Please provide Manufacturer Part Type Path or Manufacturer Part Type for row number:" + (i), LocaleContextHolder.getLocale()));
                        }
                    }


                } else {

                    throw new CassiniException(messageSource.getMessage("please_provide_mfr_part_number_for_row" + (i),
                            null, "Please provide Mfr part Number for row :" + (i), LocaleContextHolder.getLocale()));
                }


            }
            i++;
        }


        if (mfrs2.size() > 0) {
            manufacturerPartRepository.save(mfrs2);
        }
        return mfrs2;
    }

    private PLMManufacturerPart createMfrPart(Integer i, String number, PLMManufacturerPartType plmItemType, RowData stringListHashMap, Integer mfrId, Map<String, PLMManufacturerPart> dbManufacturerPartsMap) {
        String name = stringListHashMap.get("Manufacturer Part Name");
        String desc = stringListHashMap.get("Manufacturer Part Description");
        String status = stringListHashMap.get("Manufacturer Part Status");
        String mfrPartLifeCycle = stringListHashMap.get("Mfr Part Lifecycle".trim());
        PLMManufacturerPart mfrPart = new PLMManufacturerPart();
        mfrPart.setMfrPartType(plmItemType);
        mfrPart.setPartNumber(number);
        mfrPart.setPartName(name != null ? name : number);
        mfrPart.setDescription(desc);
        mfrPart.setStatus(ManufacturerPartStatus.valueOf(status.toUpperCase()));
        mfrPart.setManufacturer(mfrId);
        PLMLifeCyclePhase phase = this.importer.getPhaseByName(plmItemType.getLifecycle(), mfrPartLifeCycle != null ? mfrPartLifeCycle : "Unqualified");
        if (phase != null) {
            mfrPart.setLifeCyclePhase(phase);
        }
        dbManufacturerPartsMap.put(mfrPart.getPartNumber(), mfrPart);

        return mfrPart;
    }

    private String getMfrPartItemStatus(RowData headerValueMap) {
        String ss = "ALTERNATE";
        if (headerValueMap.containsKey("Manufacturer Part Item Status")) {
            String s2 = headerValueMap.get("Manufacturer Part Item Status");
            ss = s2;
            ss = this.importer.getRightStatus(ss, Stream.of(ManufacturerPartStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        }
        if (ss == null || ss.trim().equals("")) ss = "ALTERNATE";
        return ss;
    }

    private void addMfrPartsToItems(Map<String, PLMItem> dbItemMap, List<PLMManufacturerPart> mfrParts, TableData tableData) {
        int i = 0;
        List<PLMItem> items = new LinkedList<>();
        List<PLMItemManufacturerPart> itemMfrParts = new LinkedList<>();
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Item Number")) {
                String number = stringListHashMap.get("Item Number");
                if (number != null && !number.trim().equals("")) {
                    PLMItem item = dbItemMap.get(number);
                    if (item != null) {
                        PLMManufacturerPart part = mfrParts.get(i);
                        if (part != null) {
                            item.setMakeOrBuy(MakeOrBuy.BUY);
                            items.add(item);
                            String status = getMfrPartItemStatus(stringListHashMap);
                            PLMItemManufacturerPart itemPart = new PLMItemManufacturerPart();
                            itemPart.setItem(item.getLatestRevision());
                            itemPart.setManufacturerPart(part);
                            itemPart.setStatus(ManufacturerPartStatus.valueOf(status.toUpperCase()));
                            PLMItemManufacturerPart itemPart2 = dpItemManufacturersMap.get(item.getLatestRevision().toString() + part.getId());
                            if (itemPart2 == null) {
                                itemMfrParts.add(itemPart);
                                dpItemManufacturersMap.put(item.getLatestRevision().toString() + part.getId(), itemPart);
                            }
                        }
                    } else {
                        throw new CassiniException("Item does not exist");
                    }

                } else {
                    throw new CassiniException(messageSource.getMessage("please_provide_item_number_for_row_number" + (i),
                            null, "Please provide Item Number for row number:" + (i), LocaleContextHolder.getLocale()));

                }


            } else {
                throw new CassiniException("Please provide Item Number Column also");
            }

            i++;
        }
        if (items.size() > 0) {
            itemRepository.save(items);
        }
        if (itemMfrParts.size() > 0) {
            itemManufacturerPartRepository.save(itemMfrParts);
        }


    }

}
