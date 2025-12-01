package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.dto.ColumnData;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.platform.service.utils.ExcelService;
import com.cassinisys.platform.util.converter.ImportConverter;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.cassinisys.plm.service.plm.LifeCycleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

@Service
@Scope("prototype")
public class ItemImporter {
    @Autowired
    private ImportConverter importConverter;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private LovService lovService;
    @Autowired
    private BomService bomService;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private LifeCycleService lifecycleService;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private BomRepository bomRepository;

    private ExecutorService executorService;
    private ConcurrentMap<String, PLMItemType> rootItemTypesMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PLMItemType> itemTypesMapByPath = new ConcurrentHashMap<>();
    private ConcurrentMap<String, PLMItem> itemMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, PLMItemRevision> itemRevisionMap = new ConcurrentHashMap<>();


    public List<String> getHeadersFromExcel(Map<String, MultipartFile> fileMap) throws Exception {
        for (MultipartFile mFile : fileMap.values()) {
            ColumnData columnData = excelService.readColumnData(mFile.getInputStream());
            return columnData.getNames();
        }

        return new ArrayList<>();
    }

    @Transactional
    public void importItemDataFromFile(Map<String, MultipartFile> fileMap) throws Exception {
        for (MultipartFile mFile : fileMap.values()) {
            TableData tableData = excelService.readData(mFile.getInputStream());

            loadItemClassificationTree();
            importItemTypes(tableData);
            loadItemsMap(tableData);
            importItems(tableData);

            break;
        }
    }

    public void loadItemsMap(TableData tableData) {
        List<RowData> rows = tableData.getRows();
        List<String> itemNumbers = new ArrayList<>();
        for (RowData row : rows) {
            String itemNumber = row.get("Item Number");
            if (itemNumber != null && itemNumber.trim().length() > 0) {
                itemNumbers.add(itemNumber);
            }
        }

        if (itemNumbers.size() > 0) {
            List<PLMItem> plmItems = itemRepository.findByItemNumberIn(itemNumbers);
            List<Integer> revIds = new ArrayList<>();
            for (PLMItem plmItem : plmItems) {
                itemMap.put(plmItem.getItemNumber(), plmItem);
                revIds.add(plmItem.getLatestRevision());
            }

            if (revIds.size() > 0) {
                List<PLMItemRevision> itemRevs = itemRevisionRepository.findByIdIn(revIds);
                for (PLMItemRevision itemRev : itemRevs) {
                    itemRevisionMap.put(itemRev.getId(), itemRev);
                }
            }
        }
    }

    public void importItemTypes(TableData tableData) {
        List<RowData> rows = tableData.getRows();
        for (RowData row : rows) {
            String typePath = row.get("Type Path");
            PLMItemType itemType = itemTypesMapByPath.get(typePath);
            if (itemType == null) {
                String typeClass = row.get("Type Class");
                ItemClass itemClass = ItemClass.valueOf(typeClass.toUpperCase());
                itemType = getItemTypeByPath(typePath);
                if (itemType == null) {
                    itemType = createItemTypeByPath(null, typePath, itemClass);
                }

                itemTypesMapByPath.put(typePath, itemType);
            }
        }
    }

    public void importItems(TableData tableData) {
        try {
            List<RowData> rows = tableData.getRows();
            List<PLMItem> plmItems = new ArrayList<>();

            for (RowData row : rows) {
                PLMItem plmItem = importItem(row);
                itemMap.put(plmItem.getItemNumber(), plmItem);
                plmItems.add(plmItem);
            }

            plmItems = itemRepository.save(plmItems);

            List<PLMItemRevision> plmItemRevisions = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                RowData rowData = rows.get(i);
                String revision = rowData.get("Revision");
                String lifecycle = rowData.get("Item LifeCycle");

                PLMItem plmItem = plmItems.get(i);
                PLMItemRevision plmItemRevision = null;
                if (plmItem.getLatestRevision() == null) {
                    plmItemRevision = new PLMItemRevision();
                    plmItemRevision.setItemMaster(plmItem.getId());
                    plmItemRevision.setRevision(revision != null ? revision : "-");
                    PLMLifeCyclePhase phase = getPhaseByName(plmItem.getItemType().getLifecycle(), lifecycle != null ? lifecycle : "In Work");
                    if (phase != null) {
                        plmItemRevision.setLifeCyclePhase(phase);
                    }
                } else {
                    plmItemRevision = itemRevisionMap.get(plmItem.getLatestRevision());
                }

                plmItemRevisions.add(plmItemRevision);
            }

            plmItemRevisions = itemRevisionRepository.save(plmItemRevisions);
            for (int i = 0; i < plmItems.size(); i++) {
                PLMItem plmItem = plmItems.get(i);
                plmItem.setLatestRevision(plmItemRevisions.get(i).getId());
            }
            plmItems = itemRepository.save(plmItems);

            List<BomItem> bomItems = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                RowData rowData = rows.get(i);

                PLMItem plmItem = plmItems.get(i);
                PLMItemRevision plmItemRevision = plmItemRevisions.get(i);

                int qty = Integer.parseInt(rowData.get("Quantity"));
                BomItem bomItem = new BomItem();
                bomItem.setPlmItem(plmItem);
                bomItem.setPlmItemRevision(plmItemRevision);
                bomItem.setQuantity(qty);
                bomItem.setRefDes(rowData.get("RefDes"));
                bomItems.add(bomItem);

                if (i > 0) {
                    int level = Integer.parseInt(rowData.get("Level"));
                    int parentIndex = getParentRow(rows, i, level);
                    if (parentIndex != -1) {
                        BomItem parent = bomItems.get(parentIndex);
                        bomItem.setParent(parent);
                        parent.getPlmItemRevision().setHasBom(Boolean.TRUE);
                    }
                }
            }

            plmItemRevisions = itemRevisionRepository.save(plmItemRevisions);

            List<PLMBom> plmBomItems = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                RowData rowData = rows.get(i);
                BomItem bomItem = bomItems.get(i);
                int level = Integer.parseInt(rowData.get("Level"));
                if (level != 0) {
                    BomItem parent = bomItem.getParent();
                    if (parent != null) {
                        PLMBom plmBom = new PLMBom();
                        plmBom.setParent(parent.getPlmItemRevision());
                        plmBom.setItem(bomItem.getPlmItem());
                        plmBom.setQuantity(bomItem.getQuantity());
                        plmBom.setRefdes(bomItem.getRefDes());
                        plmBomItems.add(plmBom);
                    }
                }
            }

            plmBomItems = bomRepository.save(plmBomItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getParentRow(List<RowData> rows, int currIndex, int level) {
        for (int i = currIndex - 1; i >= 0; i--) {
            RowData rd = rows.get(i);
            int l = Integer.parseInt(rd.get("Level"));
            if (l == level - 1) {
                return i;
            }

            if (l == 0) {
                break;
            }
        }

        return -1;
    }

    private PLMItem importItem(RowData rowData) {
        String type = rowData.get("Type Path");
        PLMItemType itemType = itemTypesMapByPath.get(type);

        String itemNumber = rowData.get("Item Number");
        if (itemNumber == null || itemNumber.trim().length() == 0) {
            itemNumber = autoNumberService.getNextNumber(itemType.getItemNumberSource().getId());
        }

        PLMItem plmItem = itemMap.get(itemNumber.trim());
        if (plmItem == null) {
            String itemName = rowData.get("Item Name");
            String itemDesc = rowData.get("Item Description");

            plmItem = new PLMItem();
            plmItem.setItemType(itemType);
            plmItem.setItemNumber(itemNumber);
            plmItem.setItemName(itemName);
            plmItem.setDescription(itemDesc);
        }
        return plmItem;
    }

    private void importBom(TableData tableData) {

    }

    private PLMItemType getItemTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMItemType itemType = rootItemTypesMap.get(name);
            return itemType.getChildTypeByPath(path.substring(index + 1));
        } else {
            name = path;
            return rootItemTypesMap.get(name);
        }
    }

    private PLMItemType createItemTypeByPath(PLMItemType parentType, String path, ItemClass itemClass) {
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
                parentType.setItemNumberSource(getDefaultItemNumberSource());
                parentType.setLifecycle(getDefaultItemLifecycle());
                parentType.setRevisionSequence(getDefaultRevisionSequence());
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
            childItemType.setRevisionSequence(getDefaultRevisionSequence());
            childItemType = itemTypeService.create(childItemType);
            //rootItemTypesMap.put(name, parentType);
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

    public void loadItemClassificationTree() {
        rootItemTypesMap = new ConcurrentHashMap<>();
        itemTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMItemType> rootTypes = itemTypeService.getClassificationTree();
        for (PLMItemType rootType : rootTypes) {
            rootItemTypesMap.put(rootType.getName(), rootType);
            itemTypesMapByPath.put(rootType.getName(), rootType);
        }
    }


    private AutoNumber getDefaultItemNumberSource() {
        return autoNumberService.getByName("Default Item Number Source");
    }

    private Lov getDefaultRevisionSequence() {
        return lovService.getLovByName("Default Revision Sequence");
    }

    private PLMLifeCycle getDefaultItemLifecycle() {
        return lifecycleService.findLifecycleByName("Default Lifecycle");
    }

    private PLMLifeCyclePhase getPhaseByName(PLMLifeCycle lifeCycle, String name) {
        List<PLMLifeCyclePhase> phases = lifeCycle.getPhases();
        for (PLMLifeCyclePhase phase : phases) {
            if (phase.getPhase().equalsIgnoreCase(name)) {
                return phase;
            }
        }
        return null;
    }

    @Data
    class BomItem {
        int quantity = 1;
        private BomItem parent;
        private PLMItem plmItem;
        private PLMItemRevision plmItemRevision;
        private String refDes;
    }
}
