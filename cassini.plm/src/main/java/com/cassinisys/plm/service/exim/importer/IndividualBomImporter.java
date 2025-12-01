package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.plm.ItemTypeRepository;
import com.cassinisys.plm.service.plm.ItemTypeService;
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
public class IndividualBomImporter {
    @Autowired
    private IndividualItemsImporter individualItemsImporter;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    Map<Integer, PLMItemRevision> dbPlmItemRevisionMap1 = new LinkedHashMap();
    LinkedHashMap<Integer, PLMItem> plmItemLinkedHashMap = new LinkedHashMap<>();
    @Autowired
    private ItemImporter itemImporter;


    public void importBomItems(TableData tableData) throws ParseException {
        itemImporter.loadItemClassificationTree();
        itemImporter.importItemTypes(tableData);
        itemImporter.loadItemsMap(tableData);
        itemImporter.importItems(tableData);

    }


   /* public void importBomItems(TableData tableData) throws ParseException {
        individualItemsImporter.loadItemClassificationTree();
        Map<String, PLMItem> dbItemMap = new LinkedHashMap();
        Map<String, PLMItemRevision> plmItemRevisionMap = new LinkedHashMap();
        Map<String, PLMBom> dbParentItemBomMap = new LinkedHashMap();
        List<PLMItem> dbItems = itemRepository.findAll();
        List<PLMItemRevision> dbItemRevisions = itemRevisionRepository.findAll();
        List<PLMBom> dbBoms = bomRepository.findAll();
        dbItemMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x));
        dbPlmItemRevisionMap1 = dbItemRevisions.stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
        dbParentItemBomMap = dbBoms.stream().collect(Collectors.toMap(b -> b.getParent().getId() + "-" + b.getItem().getId(), b -> b));
        Map<String, PLMItemType> dbItemTypeMap = new ConcurrentHashMap();
        List<PLMItemType> dbItemTypes = itemTypeRepository.findAll();
        dbItemTypeMap = dbItemTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        for (PLMItem item : dbItems) {
            plmItemRevisionMap.put(item.getItemNumber(), dbPlmItemRevisionMap1.get(item.getLatestRevision()));
        }

        List<PLMBom> boms = new LinkedList<>();
        int i = 0;
        List<RowData> rows = tableData.getRows();
        for (RowData stringListHashMap : rows) {
            if (stringListHashMap.containsKey("Item Number") && stringListHashMap.containsKey("Level") && stringListHashMap.containsKey("Quantity")) {

                LinkedHashMap<Integer, Integer> seqMap = new LinkedHashMap<>();
                Integer toplevel = 0;
                String number = stringListHashMap.get("Item Number".trim());
                Integer lev = Integer.parseInt(stringListHashMap.get("Level"));
                if (number != null && number != "") {
                    PLMItem plmItem = dbItemMap.get(number);
                    if (plmItem != null) {
                        if (toplevel == lev && lev == 0) {
                            toplevel = lev;
                            plmItemLinkedHashMap.put(toplevel, plmItem);
                        } else {
                            Integer seq = 1;
                            if ((toplevel > lev) || (toplevel == lev && lev > 0)) {
                                if (seqMap.containsKey(lev - 1)) seq = seqMap.get(lev - 1);
                            }
                            toplevel = lev;
                            PLMBom bom = createBom(stringListHashMap, lev, dbPlmItemRevisionMap1, dbParentItemBomMap, plmItem, seq);
                            boms.add(bom);
                            seqMap.put(lev - 1, seq + 1);
                            plmItemLinkedHashMap.put(toplevel, plmItem);
                        }

                    } else {
                        if (stringListHashMap.containsKey("Item Name".trim()) && (stringListHashMap.containsKey("Type Path".trim()) || stringListHashMap.containsKey("Item Type".trim()))) {
                            String name = stringListHashMap.get("Item Name".trim());
                            String description = stringListHashMap.get("Item Description".trim());
                            String clss = stringListHashMap.get("Type Class".trim());
                            String revision = stringListHashMap.get("Revision".trim());
                            String makeorBuy = stringListHashMap.get("Make/Buy".trim());
                            String unit = stringListHashMap.get("Units".trim());
                            if (name != null && name != "") {
                                PLMItem plmItemNewObject = createItem(i, number, dbItemMap, stringListHashMap, dbPlmItemRevisionMap1, plmItemRevisionMap, dbItemTypeMap, plmItemLinkedHashMap);
                                if (plmItemNewObject != null) {
                                    if (toplevel == lev && lev == 0) {
                                        toplevel = lev;
                                        plmItemLinkedHashMap.put(toplevel, plmItemNewObject);
                                    } else {
                                        Integer seq = 1;
                                        if ((toplevel > lev) || (toplevel == lev && lev > 0)) {
                                            if (seqMap.containsKey(lev - 1)) seq = seqMap.get(lev - 1);
                                        }
                                        toplevel = lev;
                                        PLMBom bom = createBom(stringListHashMap, lev, dbPlmItemRevisionMap1, dbParentItemBomMap, plmItemNewObject, seq);
                                        boms.add(bom);
                                        seqMap.put(lev - 1, seq + 1);
                                        plmItemLinkedHashMap.put(toplevel, plmItemNewObject);
                                    }
                                    dbItemMap.put(plmItemNewObject.getItemNumber(), plmItemNewObject);

                                }

                            } else {

                                throw new CassiniException(messageSource.getMessage("please_provide_proper_item_name_value_for_row_number" + (i),
                                        null, "Please provide valid Item Name for row number:" + (i), LocaleContextHolder.getLocale()));
                            }

                        } else {
                            throw new CassiniException("Please provide Item Number , Level and Quantity Column also");
                        }


                    }

                } else {
                    throw new CassiniException(messageSource.getMessage("please_provide_proper_item_number_value_for_row_number" + (i),
                            null, "Please provide valid Item Number for row number:" + (i), LocaleContextHolder.getLocale()));

                }
                i++;
            } else {
                throw new CassiniException("Please provide Item Number , Level and Quantity Column also");
            }
        }
        if (boms.size() > 0) {
            boms = bomRepository.save(boms);
        }


    }*/

   /* public PLMItem createItem(Integer i, String number, Map<String, PLMItem> dbItemMap, RowData stringListHashMap,
                              Map<Integer, PLMItemRevision> dbPlmItemRevisionMap1,
                              Map<String, PLMItemRevision> plmItemRevisionMap, Map<String, PLMItemType> dbItemTypeMap, LinkedHashMap<Integer, PLMItem> plmItemLinkedHashMap) {
        String path = stringListHashMap.get("Type Path".trim());
        String typeName = stringListHashMap.get("Item Type".trim());
        String clss = stringListHashMap.get("Type Class".trim());
        ItemClass itemClass = ItemClass.valueOf(clss.toUpperCase());
        PLMItemType plmItemTypeObject = null;
        if (typeName != null && typeName != "") {
            PLMItemType plmItemType = dbItemTypeMap.get(typeName);
            if (plmItemType != null) {
                plmItemTypeObject = plmItemType;
            } else {
                if (path != null && path != "") {
                    PLMItemType plmType = individualItemsImporter.getItemTypes(path, itemClass);
                    if (plmType != null) {
                        plmItemTypeObject = plmType;
                    } else {
                        throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
                                null, "Please provide valid Item Type or Type Path for row number:" + (i), LocaleContextHolder.getLocale()));

                    }

                }
            }

        } else {

            if (path != null && path != "") {
                PLMItemType plmType = individualItemsImporter.getItemTypes(path, itemClass);
                if (plmType != null) {
                    plmItemTypeObject = plmType;
                } else {
                    throw new CassiniException(messageSource.getMessage("please_provide_proper_attribute_value_for_row_number" + (i),
                            null, "Please provide valid Item Type or Type Path for row number:" + (i), LocaleContextHolder.getLocale()));

                }

            }
        }
        PLMItem plmItemNewObject = new PLMItem();
        PLMItemRevision itemRevision = new PLMItemRevision();
        if (plmItemTypeObject != null) {
            PLMLifeCyclePhase lifeCycle = this.importer.getLifeCycle(plmItemTypeObject, stringListHashMap);
            String name = stringListHashMap.get("Item Name".trim());
            PLMItem item = dbItemMap.get(name);
            if (item == null) {
                String description = stringListHashMap.get("Item Description".trim());
                String revision = stringListHashMap.get("Revision".trim());
                String makeorBuy = stringListHashMap.get("Make/Buy".trim());
                if (makeorBuy == null || makeorBuy == "") {
                    makeorBuy = "MAKE";
                }
                String unit = stringListHashMap.get("Units".trim());
                plmItemNewObject.setItemType(plmItemTypeObject);
                plmItemNewObject.setItemNumber(number);
                plmItemNewObject.setItemName(name);
                plmItemNewObject.setDescription(description);
                plmItemNewObject.setMakeOrBuy(MakeOrBuy.valueOf(makeorBuy.toUpperCase()));
                plmItemNewObject.setUnits("EA");
                plmItemNewObject = itemRepository.save(plmItemNewObject);
                itemRevision.setItemMaster(plmItemNewObject.getId());
                itemRevision.setRevision("-");
                itemRevision.setLifeCyclePhase(lifeCycle);
                itemRevision.setHasBom(false);
                itemRevision.setHasFiles(false);
                itemRevision = itemRevisionRepository.save(itemRevision);
                dbPlmItemRevisionMap1.put(itemRevision.getId(), itemRevision);
                plmItemRevisionMap.put(plmItemNewObject.getItemNumber(), itemRevision);
                plmItemNewObject.setLatestRevision(itemRevision.getId());
                plmItemNewObject = itemRepository.save(plmItemNewObject);
                //plmItemLinkedHashMap.put(level, plmItemNewObject);
                dbItemMap.put(plmItemNewObject.getItemNumber(), plmItemNewObject);
                dbItemTypeMap.put(plmItemTypeObject.getName(), plmItemTypeObject);
            }

        }

        return plmItemNewObject;
    }


    public PLMBom createBom(RowData stringListHashMap, Integer lev, Map<Integer, PLMItemRevision> dbPlmItemRevisionMap1,
                            Map<String, PLMBom> dbParentItemBomMap, PLMItem plmItem, Integer seq) throws ParseException {
        PLMBom bom = new PLMBom();
        PLMItemRevision revision = dbPlmItemRevisionMap1.get(plmItemLinkedHashMap.get(lev - 1).getLatestRevision());
        revision.setHasBom(true);
        revision = itemRevisionRepository.save(revision);
        Integer qty = Integer.parseInt(stringListHashMap.get("Quantity".trim()));
        String refDes = stringListHashMap.get("RefDes".trim());
        String notes = stringListHashMap.get("Bom Notes".trim());
        String fromDate = stringListHashMap.get("Effective From".trim());
        String toDate = stringListHashMap.get("Effective To");
        if (fromDate != null && fromDate != "") {
            bom.setEffectiveFrom(individualItemsImporter.parseDate(fromDate, "dd/MM/yyyy"));

        }
        if (toDate != null && toDate != "") {
            bom.setEffectiveTo(individualItemsImporter.parseDate(toDate, "dd/MM/yyyy"));
        }

        bom.setParent(revision);
        bom.setItem(plmItem);
        bom.setQuantity(qty);
        bom.setRefdes(refDes);
        bom.setNotes(notes);
        bom.setSequence(seq);
        bom = createBomIfNotExists(bom, dbParentItemBomMap);
        return bom;
    }

    public PLMBom createBomIfNotExists(PLMBom bom, Map<String, PLMBom> dbParentItemBomMap) {
//        PLMBom bom1 = bomRepository.findByParentAndItem(bom.getParent(), bom.getItem());
        PLMBom bom1 = dbParentItemBomMap.get(bom.getParent().getId() + "-" + bom.getItem().getId());
        if (bom1 == null) return bom;
        else {
            bom1.setQuantity(bom.getQuantity());
            bom1.setNotes(bom.getNotes());
            bom1.setRefdes(bom.getRefdes());
            bom1.setEffectiveFrom(bom.getEffectiveFrom());
            bom1.setEffectiveTo(bom.getEffectiveTo());
            return bom1;
        }
    }*/
}
