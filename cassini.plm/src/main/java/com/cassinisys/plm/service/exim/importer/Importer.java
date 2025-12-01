package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.service.common.PreferenceService;
import com.cassinisys.platform.util.converter.ImportConverter;
import com.cassinisys.plm.model.cm.*;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mfr.*;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pqm.QualityType;
import com.cassinisys.plm.repo.mfr.*;
import com.cassinisys.plm.repo.plm.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class Importer {
    private Map<String, AutoNumber> autoWireKeyMap = new HashMap<>();
    private Lov defaultLOV;
    private Map<String, PLMLifeCycle> lifeCycleKeyMap = new HashMap<>();
    private String[] tabs = new String[]{"bom", "whereUsed", "changes", "files", "mfrParts", "relatedItems", "projects", "requirements"};
    private String[] classes = new String[]{"Product", "Part", "Assembly", "Document"};
    private Map<String, String> headersMap = null;

    @Autowired
    private AutonumbersImporter autonumbersImporter;
    @Autowired
    private LovsImporter lovsImporter;
    @Autowired
    private LifecyclesImporter lifecyclesImporter;
    @Autowired
    private WorkflowsImporter workflowsImporter;
    @Autowired
    private ClassificationImporter classificationImporter;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ManufacturerTypeRepository manufacturerTypeRepository;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private ManufacturerPartTypeRepository manufacturerPartTypeRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private ImportConverter importConverter;
    @Autowired
    private ItemTypeImporter itemTypeImporter;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CustomObjectTypeImporter customObjectTypeImporter;
    @Autowired
    private IndividualItemsImporter individualItemsImporter;
    @Autowired
    private IndividualBomImporter individualBomImporter;
    @Autowired
    private IndividualMfrAndMfrPartsImporter individualMfrAndMfrPartsImporter;
    @Autowired
    private CustomersImporter customersImporter;
    @Autowired
    private PlantsImporter plantsImporter;
    @Autowired
    private AssemblyLinesImporter assemblyLinesImporter;
    @Autowired
    private MachineImporter machineImporter;
    @Autowired
    private EquipmentImporter equipmentImporter;
    @Autowired
    private InstrumentImporter instrumentImporter;
    @Autowired
    private ToolImporter toolImporter;
    @Autowired
    private JigsAndFixturesImporter jigsAndFixturesImporter;
    @Autowired
    private MaterialImporter materialImporter;
    @Autowired
    private ShiftImporter shiftImporter;
    @Autowired
    private OperationImporter operationImporter;
    @Autowired
    private SpecificationsImporter specificationsImporter;
    @Autowired
    private ManpowersImporter manpowersImporter;
    @Autowired
    private WorkCentersImporter workCentersImporter;
    @Autowired
    private SubstancesImporter substancesImporter;
    @Autowired
    private AssetImporter assertImporter;
    @Autowired
    private MeterImporter meterImporter;
    @Autowired
    private SparePartImporter sparePartImporter;
    @Autowired
    private SuppliersImporter suppliersImporter;
    @Autowired
    private PreferenceService preferenceService;
    @Autowired
    private ChangeTypeImporter changeTypeImporter;
    @Autowired
    private ComplianceTypeImporter complianceTypeImporter;
    @Autowired
    private PMTypeImporter pmTypeImporter;
    @Autowired
    private WorkflowTypeImporter workflowTypeImporter;
    @Autowired
    private QualityTypeImporter qualityTypeImporter;
    @Autowired
    private ManufacturingTypeImporter manufacturingTypeImporter;
    @Autowired
    private MROTypeImporter mroTypeImporter;
    @Autowired
    private SourcingTypeImporter sourcingTypeImporter;
    @Autowired
    private ProgramImporter programImporter;


    public Map<String, AutoNumber> getCommonAutoNumbers() {
        List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
        return autoNumbers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }

    public void importData(ZipInputStream zis) {
        try {
            LinkedHashMap<String, byte[]> map = new LinkedHashMap<>();
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                map.put(name, IOUtils.toByteArray(zis));
            }
            importData(map, new LinkedList<>(), map.keySet().stream().toArray(String[]::new));

        } catch (IOException e) {
            throw new CassiniException(messageSource.getMessage("error_import_reason" + e.getMessage(),
                    null, "Error importing data. REASON" + e.getMessage(), LocaleContextHolder.getLocale()));
        }
    }

    private void importData(LinkedHashMap<String, byte[]> map, List<String> processed, String... keys) {
        for (String key : keys) {
            if (!processed.contains("autonumbers.json") && key.equalsIgnoreCase("autonumbers.json")) {
                autonumbersImporter.importData(map.get(key));
                processed.add("autonumbers.json");
            } else if (!processed.contains("lovs.json") && key.equalsIgnoreCase("lovs.json")) {
                lovsImporter.importData(map.get(key));
                processed.add("lovs.json");
            } else if (!processed.contains("lifecycles.json") && key.equalsIgnoreCase("lifecycles.json")) {
                lifecyclesImporter.importData(map.get(key));
                processed.add("lifecycles.json");
            } else if (!processed.contains("workflows.json") && key.equalsIgnoreCase("workflows.json")) {
                importData(map, processed, "classification");
                workflowsImporter.importData(map.get(key));
                processed.add("workflows.json");
            } else if (!processed.contains("classification.json") && key.equalsIgnoreCase("classification.json")) {
                importData(map, processed, "autonumbers", "lovs", "lifecycles");
                classificationImporter.importData(map.get(key));
                processed.add("classification.json");
            }
        }
    }


    public HashMap<String, List<String>> readExcel(File file) {

        List<LinkedHashMap<String, String>> result = new LinkedList<>();
        HashMap<String, List<String>> result1 = new LinkedHashMap<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook;
            workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            XSSFRow row;

            int c = 0;
            LinkedList<String> columns = new LinkedList<>();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (c == 0) {
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getStringCellValue() != null && !cell.getStringCellValue().trim().equals("")) {
                            columns.add(cell.getStringCellValue().trim());
                        }
                    }
                } else {
                    for (int i = 0; i < columns.size(); i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            if (map.get(columns.get(i)) == null) {
                                map.put(columns.get(i), cell.getStringCellValue());
                            }
                        } else {
                            if (map.get(columns.get(i)) == null) {
                                map.put(columns.get(i), "");
                            }
                        }
                    }
                }
                result.add(map);
                c++;
            }
            inputStream.close();

            for (HashMap<String, String> stringMap : result) {
                for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                    List<String> strings = getPropertyList(result, entry.getKey());
                    result1.put(this.headersMap.get(entry.getKey()), strings);
                }
            }
            return result1;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getPropertyList(List<LinkedHashMap<String, String>> obts, String key) {
        return obts.stream()
                .map(m -> m.get(key))
                .collect(Collectors.toList());
    }

    private PLMItemType setItemTypeParent(String path, String cls, Map<String, PLMItemType> dbItemTypeMap) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

//            PLMItemType itemType = itemTypeRepository.findByName(parent);
            PLMItemType itemType = dbItemTypeMap.get(parent);
            if (itemType == null) {
//                PLMItemType parentType = itemTypeRepository.findByName(path3);
                PLMItemType parentType = dbItemTypeMap.get(path3);
                if (parentType != null) {
                    PLMItemType plmItemType = createItemType(parent, parentType, cls, null, 0, dbItemTypeMap);
                    return plmItemType;
                } else {
                    parentType = setItemTypeParent(path3, cls, dbItemTypeMap);
                    PLMItemType itemType2 = createItemType(parent, parentType, cls, null, 0, dbItemTypeMap);
                    return itemType2;
                }
            } else {
                return itemType;
            }

        } else {
            PLMItemType parentType = dbItemTypeMap.get(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createParentType(path, cls, dbItemTypeMap);
                return parentType;
            }
        }

    }

    private String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    private void setTypeClassValues(PLMItemType itemType1, String cls2, PLMItemType parent) {
        String cls1 = convertToTitleCaseIteratingChars(cls2);
        itemType1.setItemNumberSource(this.autoWireKeyMap.get("Default " + cls1 + " Number Source"));
        if (cls1.equals("Document")) itemType1.setLifecycle(this.lifeCycleKeyMap.get("Document"));
        else itemType1.setLifecycle(this.lifeCycleKeyMap.get("Default"));
        if (itemType1.getItemNumberSource() == null) {
            if (parent != null && parent.getItemNumberSource() != null) {
                itemType1.setItemNumberSource(parent.getItemNumberSource());
            } else {
                itemType1.setItemNumberSource(this.autoWireKeyMap.get("Default Part Number Source"));
            }

        }

        itemType1.setRevisionSequence(this.defaultLOV);
        itemType1.setTabs(this.tabs);
        itemType1.setItemClass(ItemClass.valueOf(cls1.toUpperCase()));
    }

    private String getTypeClass(PLMItemType parent) {
        if (this.autoWireKeyMap.containsKey(parent.getName())) {
            return parent.getName();
        } else {
            PLMItemType plmItemType = itemTypeRepository.findOne(parent.getId());
            if (plmItemType == null) return "Part";
            return getTypeClass(plmItemType);
        }
    }

    private PLMItemType createItemType(String name, PLMItemType parent, String cls, String desc, int i, Map<String, PLMItemType> dbItemTypeMap) {

        if (parent == null && (cls == null || cls.trim().equals(""))) {
            parent = dbItemTypeMap.get("Part");
            cls = "Part";
        }

        if (cls == null || cls.trim().equals("")) {
            cls = getTypeClass(parent);
        }

        PLMItemType itemType2 = new PLMItemType();

        itemType2.setName(name);
        itemType2.setDescription(desc);
        if (parent != null)
            itemType2.setParentType(parent.getId());
        setTypeClassValues(itemType2, cls, parent);
        if ((name == null || name.trim().equals("")) && i != 0) {
            throw new CassiniException(messageSource.getMessage("please_provide_item_type_for_row_number" + (i + 1),
                    null, "Please provide Item type for row number:" + (i + 1), LocaleContextHolder.getLocale()));
        }
        if (itemType2.getId() == null) {
            itemType2 = itemTypeRepository.save(itemType2);
            dbItemTypeMap.put(itemType2.getName(), itemType2);
        }

        return itemType2;
    }

    private String getClsName(String path) {
        Integer firIndex = path.indexOf("/");
        if (firIndex != -1) {
            String cls1 = path.substring(0, firIndex);
            if (Arrays.stream(this.classes).anyMatch(cls1::equalsIgnoreCase)) return cls1;
            return "Part";
        } else {
            return "Part";
        }
    }

    private List<Integer> createClassification(HashMap<String, List<String>> stringListHashMap, Map<String, PLMItemType> dbItemTypeMap) {
        if (stringListHashMap.containsKey("Type Class") && stringListHashMap.containsKey("Type Path")) {
            initOtherColumnValues();
            List<Integer> integers = new LinkedList<>();
            List<String> paths = stringListHashMap.get("Type Path");
            List<String> classes = stringListHashMap.get("Type Class");

            for (int i = 1; i < paths.size(); i++) {
                String cls = classes.get(i);
                String path = paths.get(i);
                String desc = getValueFromMap(i, null, "Type Description", stringListHashMap);
                Integer index = path.lastIndexOf("/");
                if (index != -1) {
                    String name = path.substring(index + 1, path.length());
                    String path3 = path.substring(0, index);

                    PLMItemType itemType = dbItemTypeMap.get(name);
                    if (itemType == null) {
                        Integer index1 = path3.lastIndexOf("/");
                        if (index1 != -1) {
                            PLMItemType parentType = setItemTypeParent(path3, cls, dbItemTypeMap);
                            PLMItemType itemType2 = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                            integers.add(itemType2.getId());
                        } else {
                            PLMItemType parentType = dbItemTypeMap.get(path3);
                            if (parentType != null) {
                                PLMItemType plmItemType = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                                integers.add(plmItemType.getId());
                            } else {
                                parentType = createParentType(path3, cls, dbItemTypeMap);
                                PLMItemType plmType = dbItemTypeMap.get(name);
                                if (plmType == null) {
                                    plmType = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                                }
                                integers.add(plmType.getId());
                            }
                        }
                    } else {
                        integers.add(itemType.getId());
                    }
                } else {
                    PLMItemType itemType11 = dbItemTypeMap.get(path);
                    if (itemType11 == null) {
                        PLMItemType plmItemType1 = dbItemTypeMap.get(cls);
                        itemType11 = createItemType(path, plmItemType1, cls, desc, i, dbItemTypeMap);
                        integers.add(itemType11.getId());
                    } else {
                        integers.add(itemType11.getId());
                    }
                }
            }
            return integers;
        } else if (stringListHashMap.containsKey("Type Path")) {
            initOtherColumnValues();
            List<Integer> integers = new LinkedList<>();
            List<String> paths = stringListHashMap.get("Type Path");
            List<String> descrs = new LinkedList<>();
            if (stringListHashMap.containsKey("Type Description")) {
                descrs = stringListHashMap.get("Type Description");
            }

            for (int i = 1; i < paths.size(); i++) {
                String path = paths.get(i);
                String desc = null;
                if (descrs.size() > 0) {
                    desc = descrs.get(i);
                }
                String cls = getClsName(path);
                Integer index = path.lastIndexOf("/");
                if (index != -1) {
                    String name = path.substring(index + 1, path.length());
                    String path3 = path.substring(0, index);

//                    PLMItemType itemType = itemTypeRepository.findByName(name);
                    PLMItemType itemType = dbItemTypeMap.get(name);
                    if (itemType == null) {
                        Integer index1 = path3.lastIndexOf("/");
                        if (index1 != -1) {
                            PLMItemType parentType = setItemTypeParent(path3, cls, dbItemTypeMap);
                            PLMItemType itemType2 = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                            integers.add(itemType2.getId());

                        } else {
//                            PLMItemType parentType = itemTypeRepository.findByName(path3);
                            PLMItemType parentType = dbItemTypeMap.get(path3);
                            if (parentType != null) {
                                PLMItemType plmItemType = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                                integers.add(plmItemType.getId());
                            } else {
                                parentType = createParentType(path3, cls, dbItemTypeMap);
//                                PLMItemType plmType = itemTypeRepository.findByName(name);
                                PLMItemType plmType = dbItemTypeMap.get(name);
                                if (plmType == null) {
                                    plmType = createItemType(name, parentType, cls, desc, i, dbItemTypeMap);
                                }
                                integers.add(plmType.getId());
                            }
                        }
                    } else {
                        integers.add(itemType.getId());
                    }
                } else {
//                    PLMItemType itemType11 = itemTypeRepository.findByName(path);
                    PLMItemType itemType11 = dbItemTypeMap.get(path);
                    if (itemType11 == null) {
//                        PLMItemType plmItemType1 = itemTypeRepository.findByName(cls);
                        PLMItemType plmItemType1 = dbItemTypeMap.get(cls);
                        itemType11 = createItemType(path, plmItemType1, cls, desc, i, dbItemTypeMap);
                        integers.add(itemType11.getId());
                    } else {
                        integers.add(itemType11.getId());
                    }
                }
            }
            return integers;
        } else {
            return null;
        }

    }

    private PLMItemType createParentType(String name, String cls, Map<String, PLMItemType> dbItemTypeMap) {
        PLMItemType parent;
        if (cls != null) {
            parent = dbItemTypeMap.get(cls);
        } else {
            parent = dbItemTypeMap.get(name);
            cls = parent.getName();
        }

        PLMItemType itemType2 = new PLMItemType();
        itemType2.setName(name);
        itemType2.setItemNumberSource(parent.getItemNumberSource());
        itemType2.setParentType(parent.getId());
        setTypeClassValues(itemType2, cls, parent);
        if (itemType2.getId() == null) {
            itemType2 = itemTypeRepository.save(itemType2);
            dbItemTypeMap.put(itemType2.getName(), itemType2);
        }

        return itemType2;
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


    public void sleep(long d) {
        try {
            Thread.sleep(d);
        } catch (Exception e) {

        }
    }

    public String getNextNumberWithoutUpdate(String name, Map<String, AutoNumber> autoNumberMap) {
        AutoNumber number = autoNumberMap.get(name);
        if (number != null) {
            String nextNumber = number.readOnlyNext();
            return nextNumber;
        } else {
            return null;
        }
    }

    private List<PLMItem> createItems(HashMap<String, List<String>> stringListHashMap, Map<String, PLMItem> itemMap, Map<String, PLMItemRevision> plmItemRevisionMap, Map<String, Integer> itemNumberIdMap,
                                      Map<String, PLMItem> dbItemMap, Map<Integer, PLMItemRevision> plmItemRevisionMap1, Map<String, AutoNumber> autoNumberMap) {
        List<PLMItem> items2 = new LinkedList<PLMItem>();
        if (stringListHashMap.containsKey("Item Number") && stringListHashMap.containsKey("Type Id")) {
            List<String> numbers = stringListHashMap.get("Item Number");
            for (int i = 1; i < numbers.size(); i++) {
                String number = numbers.get(i);
                String typeId = stringListHashMap.get("Type Id").get(i - 1);
                PLMItemType plmItemType = null;
                if (typeId != null) {
                    plmItemType = itemTypeRepository.findOne(Integer.parseInt(typeId));
                }
                if (number == null || number.trim().equals("")) {
                    number = getNextNumberWithoutUpdate(plmItemType.getItemNumberSource().getName(), autoNumberMap);
                }
                PLMItem item = dbItemMap.get(number);
                if (item == null) item = itemMap.get(number);
                if (item != null) {
                    itemMap.put(item.getItemNumber(), item);
                } else {
                }
            }
        } else if (stringListHashMap.containsKey("Item Number")) {
            List<String> numbers = stringListHashMap.get("Item Number");
            for (int i = 1; i < numbers.size(); i++) {
                String number = numbers.get(i);

                PLMItem item = dbItemMap.get(number);
                if (item == null) item = itemMap.get(number);
                if (item != null) {
                    itemMap.put(item.getItemNumber(), item);
                } else {
                    throw new CassiniException("Please provide Item Type Column also");
                }
            }
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
        sleep(1000);
        List<PLMItem> plmItems = new LinkedList();
        for (PLMItem item : items2) {
            PLMItemRevision itemRevision = itemRevisions.stream().filter(revision -> item.getId().equals(revision.getItemMaster())).findFirst().orElse(null);
            item.setLatestRevision(itemRevision.getId());
            plmItems.add(item);
            plmItemRevisionMap1.put(itemRevision.getId(), itemRevision);
        }

        plmItems = itemRepository.save(plmItems);
        autoNumberRepository.save(autoNumberMap.values());
        sleep(1000);
        return plmItems;
    }


    public String validateImportedFile(HashMap<String, List<String>> headerValueMap) {
        String ss = "";
        List<String> mandaryFields = new LinkedList<>(Arrays.asList("Item Number", "Item Name", "Item Description", "Type Path", "Item Type"));
        for (String column : this.headersMap.values()) {
            if (mandaryFields.contains(column)) {
                if (column.equals("Item Name") || column.equals("Item Description")) {
                    mandaryFields.remove("Item Name");
                    mandaryFields.remove("Item Description");
                } else if (column.equals("Type Path") || column.equals("Item Type") || column.equals("ItemType")) {
                    mandaryFields.remove("Item Type");
                    mandaryFields.remove("Type Path");
                } else mandaryFields.remove(column);
            }
        }
        if (mandaryFields.size() > 0) {
            for (String fi : mandaryFields) {
                ss += " " + fi;
            }
        }
        if (headerValueMap.containsKey("Level")) {
            if (!headerValueMap.containsKey("Quantity")) {
                ss += " " + "Quantity";
            }
        }
        return ss;
    }

    public PLMBom createBomIfNotExists(PLMBom bom, Map<String, PLMBom> dbParentItemBomMap) {
//        PLMBom bom1 = bomRepository.findByParentAndItem(bom.getParent(), bom.getItem());
        PLMBom bom1 = dbParentItemBomMap.get(bom.getParent().getId() + "-" + bom.getItem().getId());
        if (bom1 == null) return bom;
        else {
            bom1.setQuantity(bom.getQuantity());
            bom1.setNotes(bom.getNotes());
            bom1.setRefdes(bom.getRefdes());
            return bom1;
        }
    }

    private List<PLMBom> createBomFromMap(HashMap<String, List<String>> stringListHashMap, List<PLMItem> items,
                                          Map<Integer, PLMItemRevision> plmItemRevisionMap1, Map<String, PLMBom> dbParentItemBomMap) {
        List<PLMBom> boms = new LinkedList<>();
        if (stringListHashMap.containsKey("Level") && stringListHashMap.containsKey("Quantity")) {
            List<String> levels = stringListHashMap.get("Level");

            PLMItem parentItem = null;
            LinkedHashMap<Integer, PLMItem> plmItemLinkedHashMap = new LinkedHashMap<>();
            LinkedHashMap<Integer, Integer> seqMap = new LinkedHashMap<>();
            Integer toplevel = 0;
            for (int i = 1; i < levels.size(); i++) {
                Integer lev = Integer.parseInt(levels.get(i));
                Integer qty = Integer.parseInt(getValueFromMap(i, "1", "Quantity", stringListHashMap));
                String refDes = getValueFromMap(i, "", "RefDes", stringListHashMap);
                String notes = getValueFromMap(i, "", "Bom Notes", stringListHashMap);
                if (items.size() > (i - 1)) {
                    PLMItem item = items.get(i - 1);
                    if (toplevel == lev && lev == 0) {
                        toplevel = lev;
                        plmItemLinkedHashMap.put(toplevel, item);
                    } else {
                        Integer seq = 1;
                        if ((toplevel > lev) || (toplevel == lev && lev > 0)) {
                            if (seqMap.containsKey(lev - 1)) seq = seqMap.get(lev - 1);
                        }
                        toplevel = lev;
                        PLMBom bom = new PLMBom();
//                    PLMItemRevision revision = itemRevisionRepository.findOne(plmItemLinkedHashMap.get(lev - 1).getLatestRevision());
                        PLMItemRevision revision = plmItemRevisionMap1.get(plmItemLinkedHashMap.get(lev - 1).getLatestRevision());
                        revision.setHasBom(true);
                        revision = itemRevisionRepository.save(revision);

                        bom.setParent(revision);
                        bom.setItem(item);
                        bom.setQuantity(qty);
                        bom.setRefdes(refDes);
                        bom.setNotes(notes);
                        bom.setSequence(seq);
                        bom = createBomIfNotExists(bom, dbParentItemBomMap);
                        boms.add(bom);
                        seqMap.put(lev - 1, seq + 1);
                        plmItemLinkedHashMap.put(toplevel, item);
                    }
                }
            }

            if (boms.size() > 0) {
                boms = bomRepository.save(boms);
                sleep(1000);
            }
        }
        return boms;
    }

    private void initAutoWiredValues() {
        List<AutoNumber> autoNumbers = autoNumberRepository.findAll();
        this.autoWireKeyMap = autoNumbers.stream().collect(Collectors.toMap(x -> x.getName(), x -> x));
    }

    private void initRevisionValue() {
        this.defaultLOV = lovRepository.findByName("Default Revision Sequence");
    }

    private void initLifeCycleValue() {
        List<PLMLifeCycle> cycles = lifeCycleRepository.findAll();
        for (PLMLifeCycle cycle : cycles) {
            if (cycle != null && cycle.getName().equals("Default Lifecycle")) {
                this.lifeCycleKeyMap.put("Default", cycle);
            } else if (cycle != null && cycle.getName().equals("Default Document Lifecycle")) {
                this.lifeCycleKeyMap.put("Document", cycle);
            } else if (cycle != null && cycle.getName().equals("Default Manufacturer Lifecycle")) {
                this.lifeCycleKeyMap.put("Manufacturer", cycle);
            } else if (cycle != null && cycle.getName().equals("Default ManufacturerPart Lifecycle")) {
                this.lifeCycleKeyMap.put("Manufacturer Part", cycle);
            }
        }
    }

    private void initOtherColumnValues() {
        initAutoWiredValues();
        initRevisionValue();
        initLifeCycleValue();
    }

    private PLMManufacturerType createMfrParentType(String name) {
        PLMManufacturerType itemType2 = new PLMManufacturerType();
        itemType2.setName(name);
        itemType2.setLifecycle(this.lifeCycleKeyMap.get("Manufacturer"));
        return manufacturerTypeRepository.save(itemType2);
    }

    private PLMManufacturerType setMfrTypeParent(String path) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            PLMManufacturerType itemType = manufacturerTypeRepository.findByName(parent);
            if (itemType == null) {
                PLMManufacturerType parentType = manufacturerTypeRepository.findByName(path3);
                if (parentType != null) {
                    PLMManufacturerType plmItemType = createMfrType(parent, parentType, null);
                    return plmItemType;
                } else {
                    parentType = setMfrTypeParent(path3);
                    PLMManufacturerType itemType2 = createMfrType(parent, parentType, null);
                    return itemType2;
                }
            } else {
                return itemType;
            }

        } else {
            PLMManufacturerType parentType = manufacturerTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMfrParentType(path);
                return parentType;
            }
        }

    }

    private List<Integer> createMfrTypes(HashMap<String, List<String>> headerValueMap) {
        List<Integer> typeIds = new LinkedList<>();
        if (headerValueMap.containsKey("Manufacturer Type")) {
            initLifeCycleValue();
            List<String> paths = headerValueMap.get("Manufacturer Type");

            for (int i = 1; i < paths.size(); i++) {
                String path = paths.get(i);

                String desc = getValueFromMap(i, null, "Manufacturer Type Description", headerValueMap);

                if (path == null || path.trim().equals("")) path = "OEM";

                Integer index = path.lastIndexOf("/");
                if (index != -1) {
                    String name = path.substring(index + 1, path.length());
                    String path3 = path.substring(0, index);

                    PLMManufacturerType itemType = manufacturerTypeRepository.findByName(name);
                    if (itemType == null) {
                        Integer index1 = path3.lastIndexOf("/");
                        if (index1 != -1) {
                            PLMManufacturerType parentType = setMfrTypeParent(path3);
                            PLMManufacturerType itemType2 = createMfrType(name, parentType, desc);
                            typeIds.add(itemType2.getId());

                        } else {
                            PLMManufacturerType parentType = manufacturerTypeRepository.findByName(path3);
                            if (parentType != null) {
                                PLMManufacturerType plmItemType = createMfrType(name, parentType, desc);
                                typeIds.add(plmItemType.getId());
                            } else {
                                parentType = createMfrParentType(path3);
                                PLMManufacturerType plmType = manufacturerTypeRepository.findByName(name);
                                if (plmType == null) {
                                    plmType = createMfrType(name, parentType, desc);
                                }
                                typeIds.add(plmType.getId());
                            }
                        }
                    } else {
                        typeIds.add(itemType.getId());
                    }
                } else {
                    PLMManufacturerType itemType11 = manufacturerTypeRepository.findByName(path);
                    if (itemType11 == null) {
                        itemType11 = createMfrType(path, null, desc);
                        typeIds.add(itemType11.getId());
                    } else {
                        typeIds.add(itemType11.getId());
                    }
                }
            }
            return typeIds;
        }
        return typeIds;
    }

    private PLMManufacturerType createMfrType(String name, PLMManufacturerType parent, String desc) {

        PLMManufacturerType itemType2 = new PLMManufacturerType();
        itemType2.setName(name);
        itemType2.setDescription(desc);
        if (parent != null) itemType2.setParentType(parent.getId());

        itemType2.setLifecycle(this.lifeCycleKeyMap.get("Manufacturer"));

        return manufacturerTypeRepository.save(itemType2);
    }


    private PLMManufacturerPartType createMfrPartType(String name, PLMManufacturerPartType parent, String desc) {

        PLMManufacturerPartType itemType2 = new PLMManufacturerPartType();
        itemType2.setName(name);
        itemType2.setDescription(desc);
        if (parent != null) itemType2.setParentType(parent.getId());

        itemType2.setLifecycle(this.lifeCycleKeyMap.get("Manufacturer Part"));

        return manufacturerPartTypeRepository.save(itemType2);
    }


    private PLMManufacturerPartType setMfrPartTypeParent(String path) {

        Integer index = path.lastIndexOf("/");
        if (index != -1) {
            String parent = path.substring(index + 1, path.length());
            String path3 = path.substring(0, index);

            PLMManufacturerPartType itemType = manufacturerPartTypeRepository.findByName(parent);
            if (itemType == null) {
                PLMManufacturerPartType parentType = manufacturerPartTypeRepository.findByName(path3);
                if (parentType != null) {
                    PLMManufacturerPartType plmItemType = createMfrPartType(parent, parentType, null);
                    return plmItemType;
                } else {
                    parentType = setMfrPartTypeParent(path3);
                    PLMManufacturerPartType itemType2 = createMfrPartType(parent, parentType, null);
                    return itemType2;
                }
            } else {
                return itemType;
            }

        } else {
            PLMManufacturerPartType parentType = manufacturerPartTypeRepository.findByName(path);
            if (parentType != null) {
                return parentType;
            } else {
                parentType = createMfrParentPartType(path);
                return parentType;
            }
        }
    }

    private PLMManufacturerPartType createMfrParentPartType(String name) {
        PLMManufacturerPartType itemType2 = new PLMManufacturerPartType();
        itemType2.setName(name);
        itemType2.setLifecycle(this.lifeCycleKeyMap.get("Manufacturer Part"));
        return manufacturerPartTypeRepository.save(itemType2);
    }


    private List<Integer> createMfrPartTypes(HashMap<String, List<String>> headerValueMap) {
        List<Integer> typeIds = new LinkedList<>();
        if (headerValueMap.containsKey("Manufacturer Part Type")) {
            initLifeCycleValue();
            List<String> paths = headerValueMap.get("Manufacturer Part Type");

            for (int i = 1; i < paths.size(); i++) {
                String path = paths.get(i);
                String desc = getValueFromMap(i, null, "Manufacturer Part Type Desc", headerValueMap);

                if (path == null || path.trim().equals("")) path = "OEM Part";

                Integer index = path.lastIndexOf("/");
                if (index != -1) {
                    String name = path.substring(index + 1, path.length());
                    String path3 = path.substring(0, index);

                    PLMManufacturerPartType itemType = manufacturerPartTypeRepository.findByNameEqualsIgnoreCase(name);
                    if (itemType == null) {
                        Integer index1 = path3.lastIndexOf("/");
                        if (index1 != -1) {
                            PLMManufacturerPartType parentType = setMfrPartTypeParent(path3);
                            PLMManufacturerPartType itemType2 = createMfrPartType(name, parentType, desc);
                            typeIds.add(itemType2.getId());

                        } else {
                            PLMManufacturerPartType parentType = manufacturerPartTypeRepository.findByName(path3);
                            if (parentType != null) {
                                PLMManufacturerPartType plmItemType = createMfrPartType(name, parentType, desc);
                                typeIds.add(plmItemType.getId());
                            } else {
                                parentType = createMfrParentPartType(path3);
                                PLMManufacturerPartType plmType = manufacturerPartTypeRepository.findByName(name);
                                if (plmType == null) {
                                    plmType = createMfrPartType(name, parentType, desc);
                                }
                                typeIds.add(plmType.getId());
                            }
                        }
                    } else {
                        typeIds.add(itemType.getId());
                    }
                } else {
                    PLMManufacturerPartType itemType11 = manufacturerPartTypeRepository.findByName(path);
                    if (itemType11 == null) {
                        itemType11 = createMfrPartType(path, null, desc);
                        typeIds.add(itemType11.getId());
                    } else {
                        typeIds.add(itemType11.getId());
                    }
                }
            }
        }
        return typeIds;
    }


    private String getMfrPartItemStatus(Integer i, HashMap<String, List<String>> headerValueMap) {
        String ss = "ALTERNATE";
        if (headerValueMap.containsKey("Manufacturer Part Item Status")) {
            List<String> s2 = headerValueMap.get("Manufacturer Part Item Status");
            if (s2 != null && s2.size() > 0)
                ss = s2.get(i);
            ss = getRightStatus(ss, Stream.of(ManufacturerPartStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        }
        if (ss == null || ss.trim().equals("")) ss = "ALTERNATE";
        return ss;
    }

    private void addMfrPartsToItems(List<PLMItem> items4, List<PLMManufacturerPart> mfrParts, HashMap<String, List<String>> headerValueMap) {

        for (int i = 0; i < items4.size(); i++) {
            PLMItem item = items4.get(i);
            PLMManufacturerPart part = mfrParts.get(i);
            if (part != null) {
                item.setMakeOrBuy(MakeOrBuy.BUY);
                PLMItem item11 = itemRepository.save(item);
                items4.set(i, item11);
                String status = getMfrPartItemStatus(i + 1, headerValueMap);
                PLMItemManufacturerPart itemPart = new PLMItemManufacturerPart();
                itemPart.setItem(item.getLatestRevision());
                itemPart.setManufacturerPart(part);
                itemPart.setStatus(ManufacturerPartStatus.valueOf(status.toUpperCase()));
                PLMItemManufacturerPart itemPart2 = itemManufacturerPartRepository.findByItemAndManufacturerPart(item.getLatestRevision(), part);
                if (itemPart2 == null) {
                    itemManufacturerPartRepository.save(itemPart);
                }
            }
        }
    }

    public void saveHeadersMap(Map<String, String> dtos, String objectType) {
        this.headersMap = dtos;
        Preference preference = preferenceService.getPreferenceByKey("SYSTEM.IMPORT_MAPPINGS");
        String json1;
        Map<String, Map<String, String>> stringMappingDTOMap = new HashMap();
        if (preference != null) {
            String json = preference.getJsonValue();
            try {
                if (json != null && !json.equals(""))
                    stringMappingDTOMap = new ObjectMapper().readValue(json, Map.class);
                stringMappingDTOMap.put(objectType, dtos);
                json1 = new ObjectMapper().writeValueAsString(stringMappingDTOMap);
                preference.setJsonValue(json1);
            } catch (Exception e) {
            }
        } else {
            try {
                preference = new Preference();
                preference.setContext("SYSTEM");
                preference.setPreferenceKey("SYSTEM.IMPORT_MAPPINGS");
                stringMappingDTOMap.put(objectType, dtos);
                json1 = new ObjectMapper().writeValueAsString(stringMappingDTOMap);
                preference.setJsonValue(json1);
            } catch (Exception e) {
            }
        }

        preferenceService.create(preference);
    }

    @Transactional
    public PLMBom importUploadedFile(Map<String, MultipartFile> fileMap) throws Exception {
        for (MultipartFile file : fileMap.values()) {
            File file1 = importConverter.trimAndConvertMultipartFileToFile(file);

            HashMap<String, List<String>> headerValueMap = readExcel(file1);
            return importData(headerValueMap);
        }
        return null;
    }

    @Transactional
    public PLMBom importData(HashMap<String, List<String>> headerValueMap) {
        Map<String, PLMItem> itemMap = new LinkedHashMap();
        Map<String, PLMItem> dbItemMap = new LinkedHashMap();
        Map<String, PLMItemRevision> plmItemRevisionMap = new LinkedHashMap();
        Map<Integer, PLMItemRevision> dbPlmItemRevisionMap1 = new LinkedHashMap();
        Map<String, PLMBom> dbParentItemBomMap = new LinkedHashMap();
        Map<String, Integer> itemNumberIdMap = new LinkedHashMap();
        Map<String, AutoNumber> autoNumberMap = new LinkedHashMap();
        Map<String, PLMItemType> dbItemTypeMap = new LinkedHashMap();
        String ss = validateImportedFile(headerValueMap);
        List<PLMItem> dbItems = itemRepository.findAll();
        List<PLMItemRevision> dbItemRevisions = itemRevisionRepository.findAll();
        List<PLMBom> dbBoms = bomRepository.findAll();
        List<PLMItemType> dbItemTypes = itemTypeRepository.findAll();
        List<AutoNumber> dbAutoNumbers = autoNumberRepository.findAll();
        dbItemTypeMap = dbItemTypes.stream().collect(Collectors.toMap(t -> t.getName(), t -> t));
        dbItemMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x));
        dbPlmItemRevisionMap1 = dbItemRevisions.stream().collect(Collectors.toMap(r -> r.getId(), r -> r));
        dbParentItemBomMap = dbBoms.stream().collect(Collectors.toMap(b -> b.getParent().getId() + "-" + b.getItem().getId(), b -> b));
        autoNumberMap = dbAutoNumbers.stream().collect(Collectors.toMap(a -> a.getName(), a -> a));
        itemNumberIdMap = dbItems.stream().collect(Collectors.toMap(x -> x.getItemNumber(), x -> x.getId()));
        for (PLMItem item : dbItems) {
            plmItemRevisionMap.put(item.getItemNumber(), dbPlmItemRevisionMap1.get(item.getLatestRevision()));
        }

        if (ss.trim().equals("")) {
            List<PLMBom> bomIds;
            List<Integer> typeIds = createClassification(headerValueMap, dbItemTypeMap);
            if (typeIds != null && typeIds.size() > 0)
                headerValueMap.put("Type Id", typeIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList()));
            List<PLMItem> items4 = createItems(headerValueMap, itemMap, plmItemRevisionMap, itemNumberIdMap, dbItemMap,
                    dbPlmItemRevisionMap1, autoNumberMap);


            if (items4 != null && items4.size() > 0) {
                bomIds = createBomFromMap(headerValueMap, items4, dbPlmItemRevisionMap1, dbParentItemBomMap);
            }
            List<Integer> mfrTypeIds = createMfrTypes(headerValueMap);
            if (mfrTypeIds != null && mfrTypeIds.size() > 0)
                headerValueMap.put("MfrType Id", mfrTypeIds.stream().map(s -> String.valueOf(s)).collect(Collectors.toList()));

        } else {
            throw new CassiniException(ss + " Fields are mandatory in imported file");
        }
        return null;
    }

    public PLMLifeCyclePhase getMfrLifeCycle(PLMManufacturerType plmItemType, RowData stringListHashMap1) {
        PLMLifeCyclePhase ss;
        if (stringListHashMap1.containsKey("Manufacturer LifeCycle")) {
            String s = stringListHashMap1.get("Manufacturer LifeCycle");
            if (s != null && !s.trim().equals("")) {
                s = getRightLifecycle(s, plmItemType.getLifecycle());
                if (s != null && !s.trim().equals("")) {
                    PLMLifeCyclePhase phase1 = lifeCyclePhaseRepository.findByPhaseAndLifeCycle(s, plmItemType.getLifecycle().getId());
                    if (phase1 != null)
                        return phase1;
                }
            }
        }

        List<PLMLifeCyclePhase> phases = lifeCyclePhaseRepository.findByLifeCycle(plmItemType.getLifecycle().getId());
        ss = phases.get(0);

        return ss;
    }

    public PLMLifeCyclePhase getMfrPartLifeCycle(PLMManufacturerPartType plmItemType, RowData stringListHashMap1) {
        PLMLifeCyclePhase ss;
        if (stringListHashMap1.containsKey("Manufacturer Part LifeCycle")) {
            String s = stringListHashMap1.get("Manufacturer Part LifeCycle");
            if (s != null && !s.trim().equals("")) {
                s = getRightLifecycle(s, plmItemType.getLifecycle());
                if (s != null && !s.trim().equals("")) {
                    PLMLifeCyclePhase phase1 = lifeCyclePhaseRepository.findByPhaseAndLifeCycle(s, plmItemType.getLifecycle().getId());
                    if (phase1 != null)
                        return phase1;
                }
            }
        }

        List<PLMLifeCyclePhase> phases = lifeCyclePhaseRepository.findByLifeCycle(plmItemType.getLifecycle().getId());
        ss = phases.get(0);

        return ss;
    }


    public PLMLifeCyclePhase getSupplierLifeCycle(PLMSupplierType supplierType, RowData stringListHashMap1) {
        PLMLifeCyclePhase ss;
        if (stringListHashMap1.containsKey("Supplier LifeCycle".trim())) {
            String s = stringListHashMap1.get("Supplier LifeCycle".trim());
            if (s != null && !s.trim().equals("")) {
                s = getRightLifecycle(s, supplierType.getLifecycle());
                if (s != null && !s.trim().equals("")) {
                    PLMLifeCyclePhase phase1 = lifeCyclePhaseRepository.findByPhaseAndLifeCycle(s, supplierType.getLifecycle().getId());
                    if (phase1 != null)
                        return phase1;
                }
            }
        }

        List<PLMLifeCyclePhase> phases = lifeCyclePhaseRepository.findByLifeCycle(supplierType.getLifecycle().getId());
        ss = phases.get(0);

        return ss;
    }

    public PLMLifeCyclePhase getLifeCycle(PLMItemType plmItemType, RowData stringListHashMap1) {
        PLMLifeCyclePhase ss;
        if (stringListHashMap1.containsKey("Item LifeCycle")) {
            String s = stringListHashMap1.get("Item LifeCycle");
            if (s != null && !s.trim().equals("")) {
                s = getRightLifecycle(s, plmItemType.getLifecycle());
                if (s != null && !s.trim().equals("")) {
                    PLMLifeCyclePhase phase1 = lifeCyclePhaseRepository.findByPhaseAndLifeCycle(s, plmItemType.getLifecycle().getId());
                    if (phase1 != null)
                        return phase1;
                }
            }
        }

        List<PLMLifeCyclePhase> phases = lifeCyclePhaseRepository.findByLifeCycle(plmItemType.getLifecycle().getId());
        ss = phases.get(0);

        return ss;

    }

    public String getValueFromMap(Integer i, String default1, String cName, HashMap<String, List<String>> stringListHashMap) {
        if (!stringListHashMap.containsKey(cName)) return default1;
        else {
            String a = null;
            List<String> values = stringListHashMap.get(cName);
            if (values.size() > 0) {
                a = stringListHashMap.get(cName).get(i);
                if (a == null || a.trim().equals("")) a = default1;
                return a;
            }
            return a;
        }
    }

    private String getRightLifecycle(String s, PLMLifeCycle cycle) {
        String ss = "";
        Integer val = null;
        List<PLMLifeCyclePhase> phases = cycle.getPhases();
        for (PLMLifeCyclePhase phase1 : phases) {
            if (phase1.getPhase().equalsIgnoreCase(s)) return phase1.getPhase();
            Integer k = checkSpellingDistance(phase1.getPhase(), s);
            if (k != null && k < 4) {
                if (val == null) {
                    val = k;
                    ss = phase1.getPhase();
                } else if (k < val) {
                    ss = phase1.getPhase();
                    val = k;
                }
            }
        }

        return ss;
    }

    public String getRightStatus(String s, List<String> values) {
        String ss = "";
        Integer val = null;
        for (String phase1 : values) {
            if (phase1.equalsIgnoreCase(s)) return phase1;
            Integer k = checkSpellingDistance(phase1, s);
            if (k != null && k < 4) {
                if (val == null) {
                    val = k;
                    ss = phase1;
                } else if (k < val) {
                    ss = phase1;
                    val = k;
                }
            }
        }
        return ss;
    }

    private Integer checkSpellingDistance(String str1, String str2) {
        Integer dist = 0;
        if (str2.length() < 3) return null;
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                dist++;
            }
        }
        return dist;
    }

    public PLMLifeCyclePhase getPhaseByName(PLMLifeCycle lifeCycle, String name) {
        List<PLMLifeCyclePhase> phases = lifeCycle.getPhases();
        for (PLMLifeCyclePhase phase : phases) {
            if (phase.getPhase().equalsIgnoreCase(name)) {
                return phase;
            }
        }
        return null;
    }


    private String getObjectTemplateName(String objectType) {
        String fileName = null;
        switch (objectType) {
            case "Classification":
                fileName = "Classification.xlsx";
                break;
            case "Items":
                fileName = "Items.xlsx";
                break;
            case "BOM":
                fileName = "bom.xlsx";
                break;
            case "Manufacturer and Manufacturer Parts":
                fileName = "manufacturerAndManufacturerParts.xlsx";
                break;
            case "Customers":
                fileName = "Customers.xlsx";
                break;
            case "Suppliers":
                fileName = "Suppliers.xlsx";
                break;
            case "Specifications":
                fileName = "Specifications.xlsx";
                break;
            case "Substances":
                fileName = "Substances.xlsx";
                break;
            case "Plants":
                fileName = "Plants.xlsx";
                break;
            case "Assembly Lines":
                fileName = "AssemblyLines.xlsx";
                break;
            case "Work Centers":
                fileName = "WorkCenters.xlsx";
                break;
            case "Machines":
                fileName = "Machines.xlsx";
                break;
            case "Manpowers":
                fileName = "Manpower.xlsx";
                break;
            case "Materials":
                fileName = "Materials.xlsx";
                break;
            case "Instruments":
                fileName = "Instruments.xlsx";
                break;
            case "Equipments":
                fileName = "Equipments.xlsx";
                break;
            case "Jigs & Fixture":
                fileName = "Jigs & Fixtures.xlsx";
                break;
            case "Operations":
                fileName = "Operations.xlsx";
                break;
            case "Shifts":
                fileName = "Shifts.xlsx";
                break;
            case "Tools":
                fileName = "Tools.xlsx";
                break;
            case "Assets":
                fileName = "Assets.xlsx";
                break;
            case "Meters":
                fileName = "Meters.xlsx";
                break;
            case "Spare Parts":
                fileName = "SpareParts.xlsx";
                break;
            case "Projects":
                fileName = "Projects.xlsx";
                break;    
            case "Programs":
                fileName = "Programs.xlsx";
                break;
            default:
                fileName = "";
        }
        return fileName;
    }


    /*
    * Download Object Templates
    * */
    public void downloadObjectTemplate(HttpServletResponse response, String objectType) {
        try {
            String templateName = getObjectTemplateName(objectType);
            Resource classPathResource = resourceLoader
                    .getResource("classpath:templates/email/import/" + templateName);
            if (classPathResource.exists()) {
                response.addHeader("Content-Disposition",
                        "attachment;filename*=utf-8'zh_cn'" + templateName);
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setContentLength((int) classPathResource.contentLength());
                InputStream inputStream = classPathResource.getInputStream();
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            }
        } catch (IOException e) {

        }
    }


    /*
   * Individual Objects Import
   * */
    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'import','all')")
    public void importIndividualObjectFile(String objectType, Map<String, MultipartFile> fileMap) throws Exception {
        for (MultipartFile file : fileMap.values()) {
            TableData tableData = readData(file.getInputStream());
            importIndividualObjectData(objectType, tableData);
        }
    }

    public TableData readData(InputStream is) {
        TableData tableData = new TableData();
        try {
            BufferedInputStream e = new BufferedInputStream(is);
            XSSFWorkbook workBook = new XSSFWorkbook(e);
            int numberOfSheets = workBook.getNumberOfSheets();
            if (numberOfSheets > 0) {
                XSSFSheet sheet = workBook.getSheetAt(0);
                Iterator rowIterator = sheet.rowIterator();
                List var14 = new ArrayList<>();
                for (int rowIndex = 1; rowIterator.hasNext(); ++rowIndex) {
                    XSSFRow row = (XSSFRow) rowIterator.next();
                    Iterator cellIterator;
                    XSSFCell cell;
                    if (rowIndex == 1) {
                        cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            cell = (XSSFCell) cellIterator.next();
                            var14.add(cell.toString());
                            tableData.getColumns().getNames().add(getKeyByValue(headersMap, cell.toString()));
                        }
                    } else {
                        RowData rowData = new RowData();
                        cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            cell = (XSSFCell) cellIterator.next();
                            rowData.put(getKeyByValue(headersMap, var14.get(cell.getColumnIndex()).toString()), cell.toString());
                        }

                        tableData.getRows().add(rowData);
                    }
                }
            }

            return tableData;
        } catch (IOException var13) {
            throw new CassiniException("Error reading excel file. REASON: " + var13.getMessage());
        }
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Transactional
    public void importIndividualObjectData(String objectType, TableData tableData) throws java.text.ParseException {
        if (objectType.equals("Classification")) {
            importClassification(tableData);
        } else if (objectType.equals("Items")) {
            individualItemsImporter.importItems(tableData);
        } else if (objectType.equals("BOM")) {
            individualBomImporter.importBomItems(tableData);
        } else if (objectType.equals("Manufacturer and Manufacturer Parts")) {
            individualMfrAndMfrPartsImporter.importMfrAndMfrParts(tableData);
        } else if (objectType.equals("Customers")) {
            customersImporter.importCustomers(tableData);
        } else if (objectType.equals("Suppliers")) {
            suppliersImporter.importSupplierAndSupplierParts(tableData);
        } else if (objectType.equals("Substances")) {
            substancesImporter.importSubstances(tableData);
        } else if (objectType.equals("Specifications")) {
            specificationsImporter.importSpecificationsAndSubstances(tableData);
        } else if (objectType.equals("Plants")) {
            plantsImporter.importPlants(tableData);
        } else if (objectType.equals("Assembly Lines")) {
            assemblyLinesImporter.importAssemblyLines(tableData);
        } else if (objectType.equals("Machines")) {
            machineImporter.importMachines(tableData);
        } else if (objectType.equals("Equipments")) {
            equipmentImporter.importEquipments(tableData);
        } else if (objectType.equals("Instruments")) {
            instrumentImporter.importInstruments(tableData);
        } else if (objectType.equals("Tools")) {
            toolImporter.importTools(tableData);
        } else if (objectType.equals("Work Centers")) {
            workCentersImporter.importWorkCenters(tableData);
        } else if (objectType.equals("Manpowers")) {
            manpowersImporter.importManpowers(tableData);
        } else if (objectType.equals("Jigs & Fixture")) {
            jigsAndFixturesImporter.importJigsFixtures(tableData);
        } else if (objectType.equals("Materials")) {
            materialImporter.importMaterials(tableData);
        } else if (objectType.equals("Shifts")) {
            shiftImporter.importShifts(tableData);
        } else if (objectType.equals("Operations")) {
            operationImporter.importOperations(tableData);
        } else if (objectType.equals("Assets")) {
            assertImporter.importAssets(tableData);
        } else if (objectType.equals("Meters")) {
            meterImporter.importMeters(tableData);
        } else if (objectType.equals("Spare Parts")) {
            sparePartImporter.importSpareParts(tableData);
        } else if (objectType.equals("Projects") || objectType.equals("Programs")) {
            programImporter.importProjects(tableData);
        } 

    }

    private void importClassification(TableData tableData) {
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Object Type")) {
                String objectType = stringListHashMap.get("Object Type");
                objectType = objectType.toUpperCase();
                if (objectType != null && objectType != "") {
                    if (objectType.equals("ITEMTYPE") && (stringListHashMap.containsKey("Type Class") && stringListHashMap.containsKey("Type Path"))) {
                        itemTypeImporter.importItemTypeObjects(i, stringListHashMap);
                    } else if (objectType.equals("CUSTOMOBJECTTYPE")) {
                        customObjectTypeImporter.importCustomObjectTypes(i, stringListHashMap);
                    } else if ((objectType.equals("ECOTYPE") || objectType.equals("ECRTYPE") || objectType.equals("DCOTYPE") || objectType.equals("DCRTYPE") || objectType.equals("MCOTYPE") || objectType.equals("DEVIATIONTYPE") || objectType.equals("WAIVERTYPE")) && stringListHashMap.containsKey("Type Path")) {
                        if (objectType.equals("ECOTYPE") && stringListHashMap.containsKey("Type Path")) {
                            PLMECOType type = new PLMECOType();
                            changeTypeImporter.importEcoTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "ECO");
                        } else if (objectType.equals("ECRTYPE")) {
                            PLMECRType type = new PLMECRType();
                            changeTypeImporter.importEcrTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "ECR");

                        } else if (objectType.equals("DCOTYPE")) {
                            PLMDCOType type = new PLMDCOType();
                            changeTypeImporter.importDcoTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "DCO");

                        } else if (objectType.equals("DCRTYPE")) {
                            PLMDCRType type = new PLMDCRType();
                            changeTypeImporter.importDcrTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "DCR");

                        } else if (objectType.equals("MCOTYPE")) {
                            PLMMCOType type = new PLMMCOType();
                            changeTypeImporter.importMcoTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "MCO");

                        } else if (objectType.equals("DEVIATIONTYPE")) {
                            PLMDeviationType type = new PLMDeviationType();
                            changeTypeImporter.importDevTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "Deviation");

                        } else if (objectType.equals("WAIVERTYPE")) {
                            PLMWaiverType type = new PLMWaiverType();
                            changeTypeImporter.importWavTypeObjects(i, type.getType(), type.getObjectType(), stringListHashMap, "Waiver");

                        }
                    } else if (objectType.equals("PGCSUBSTANCETYPE")) {
                        complianceTypeImporter.importSubstanceTypes(i, stringListHashMap);
                    } else if (objectType.equals("PGCSPECIFICATIONTYPE")) {
                        complianceTypeImporter.importSpecificationTypes(i, stringListHashMap);
                    } else if (objectType.equals("PGCDECLARATIONTYPE")) {
                        complianceTypeImporter.importDeclarationTypes(i, stringListHashMap);
                    } else if (objectType.equals("REQUIREMENTTYPE")) {
                        pmTypeImporter.importRequirementTypes(i, stringListHashMap);
                    } else if (objectType.equals("REQUIREMENTDOCUMENTTYPE")) {
                        pmTypeImporter.importRequirementDocumentTypes(i, stringListHashMap);
                    } else if (objectType.equals("WORKFLOWTYPE")) {
                        workflowTypeImporter.importWorkflowTypes(i, stringListHashMap);
                    } else if ((objectType.equals("PRODUCTINSPECTIONPLANTYPE") || objectType.equals("MATERIALINSPECTIONPLANTYPE") || objectType.equals("PRTYPE") || objectType.equals("NCRTYPE") || objectType.equals("QCRTYPE")) && stringListHashMap.containsKey("Type Path")) {
                        if (objectType.equals("PRODUCTINSPECTIONPLANTYPE")) {
                            qualityTypeImporter.importPIPObjects(i, QualityType.valueOf("PRODUCTINSPECTIONPLANTYPE"), stringListHashMap, "Inspection Plan");
                        } else if (objectType.equals("MATERIALINSPECTIONPLANTYPE")) {
                            qualityTypeImporter.importMIPObjects(i, QualityType.valueOf("MATERIALINSPECTIONPLANTYPE"), stringListHashMap, "Inspection Plan");
                        } else if (objectType.equals("PRTYPE")) {
                            qualityTypeImporter.importPrObjects(i, QualityType.valueOf("PRTYPE"), stringListHashMap, "PR");
                        } else if (objectType.equals("NCRTYPE")) {
                            qualityTypeImporter.importNcrObjects(i, QualityType.valueOf("NCRTYPE"), stringListHashMap, "NCR");

                        } else if (objectType.equals("QCRTYPE")) {
                            qualityTypeImporter.importQcrObjects(i, QualityType.valueOf("QCRTYPE"), stringListHashMap, "QCR");

                        }
                    } else if ((objectType.equals("PLANTTYPE") || objectType.equals("MACHINETYPE") || objectType.equals("MATERIALTYPE") ||
                            objectType.equals("TOOLTYPE") || objectType.equals("WORKCENTERTYPE") ||
                            objectType.equals("JIGFIXTURETYPE") || objectType.equals("OPERATIONTYPE") || objectType.equals("PRODUCTIONORDERTYPE") ||
                            objectType.equals("SERVICEORDERTYPE") || objectType.equals("MANPOWERTYPE") || objectType.equals("EQUIPMENTTYPE") ||
                            objectType.equals("INSTRUMENTTYPE") || objectType.equals("ASSEMBLYLINETYPE")) && stringListHashMap.containsKey("Type Path")) {
                        if (objectType.equals("PLANTTYPE") && stringListHashMap.containsKey("Type Path")) {
                            MESPlantType type = new MESPlantType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("PLANTTYPE"), type.getObjectType(), stringListHashMap, "Plant");
                        } else if (objectType.equals("MACHINETYPE")) {
                            MESMachineType type = new MESMachineType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("MACHINETYPE"), type.getObjectType(), stringListHashMap, "Machine");
                        } else if (objectType.equals("ASSEMBLYLINETYPE")) {
                            MESAssemblyLineType type = new MESAssemblyLineType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("ASSEMBLYLINETYPE"), type.getObjectType(), stringListHashMap, "Assembly Line");
                        } else if (objectType.equals("MATERIALTYPE")) {
                            MESMaterialType type = new MESMaterialType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("MATERIALTYPE"), type.getObjectType(), stringListHashMap, "Material");
                        } else if (objectType.equals("TOOLTYPE")) {
                            MESToolType type = new MESToolType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("TOOLTYPE"), type.getObjectType(), stringListHashMap, "Tool");
                        } else if (objectType.equals("WORKCENTERTYPE")) {
                            MESWorkCenterType type = new MESWorkCenterType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("WORKCENTERTYPE"), type.getObjectType(), stringListHashMap, "Work Center");
                        } else if (objectType.equals("JIGFIXTURETYPE")) {
                            MESJigsFixtureType type = new MESJigsFixtureType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("JIGFIXTURETYPE"), type.getObjectType(), stringListHashMap, "Jigs and Fixture");
                        } else if (objectType.equals("OPERATIONTYPE")) {
                            MESOperationType type = new MESOperationType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("OPERATIONTYPE"), type.getObjectType(), stringListHashMap, "Operation");
                        } else if (objectType.equals("PRODUCTIONORDERTYPE")) {
                            MESProductionOrderType type = new MESProductionOrderType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("PRODUCTIONORDERTYPE"), type.getObjectType(), stringListHashMap, "Production Order");
                        } else if (objectType.equals("SERVICEORDERTYPE")) {
                            MESServiceOrderType type = new MESServiceOrderType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("SERVICEORDERTYPE"), type.getObjectType(), stringListHashMap, "Service Order");
                        } else if (objectType.equals("MANPOWERTYPE")) {
                            MESManpowerType type = new MESManpowerType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("MANPOWERTYPE"), type.getObjectType(), stringListHashMap, "Manpower");
                        } else if (objectType.equals("EQUIPMENTTYPE")) {
                            MESEquipmentType type = new MESEquipmentType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("EQUIPMENTTYPE"), type.getObjectType(), stringListHashMap, "Equipment");
                        } else if (objectType.equals("INSTRUMENTTYPE")) {
                            MESInstrumentType type = new MESInstrumentType();
                            manufacturingTypeImporter.importManufacturingObjects(i, MESType.valueOf("INSTRUMENTTYPE"), type.getObjectType(), stringListHashMap, "Instrument");
                        }

                    } else if ((objectType.equals("ASSETTYPE") || objectType.equals("METERTYPE") || objectType.equals("SPAREPARTTYPE") || objectType.equals("WORKREQUESTTYPE")) || objectType.equals("WORKORDERTYPE")) {
                        if (objectType.equals("ASSETTYPE")) {
                            mroTypeImporter.importMROTypeObjects(i, PLMObjectType.valueOf("ASSETTYPE"), stringListHashMap, "Asset");
                        } else if (objectType.equals("METERTYPE")) {
                            mroTypeImporter.importMROTypeObjects(i, PLMObjectType.valueOf("METERTYPE"), stringListHashMap, "Meter");
                        } else if (objectType.equals("SPAREPARTTYPE")) {
                            mroTypeImporter.importMROTypeObjects(i, PLMObjectType.valueOf("SPAREPARTTYPE"), stringListHashMap, "Spare Part");
                        } else if (objectType.equals("WORKREQUESTTYPE")) {
                            mroTypeImporter.importMROTypeObjects(i, PLMObjectType.valueOf("WORKREQUESTTYPE"), stringListHashMap, "Work Request");
                        } else if (objectType.equals("WORKORDERTYPE")) {
                            mroTypeImporter.importMROTypeObjects(i, PLMObjectType.valueOf("WORKORDERTYPE"), stringListHashMap, "Work Order");
                        }
                    } else if ((objectType.equals("MANUFACTURERTYPE") || objectType.equals("MANUFACTURERPARTTYPE") || objectType.equals("SUPPLIERTYPE"))) {
                        if (objectType.equals("MANUFACTURERTYPE")) {
                            sourcingTypeImporter.importMfrTypeObjects(i, PLMObjectType.valueOf("MANUFACTURERTYPE"), stringListHashMap, "Manufacturer");
                        } else if (objectType.equals("MANUFACTURERPARTTYPE")) {
                            sourcingTypeImporter.importMfrPartTypeObjects(i, PLMObjectType.valueOf("MANUFACTURERPARTTYPE"), stringListHashMap, "Manufacturer Part");
                        } else if (objectType.equals("SUPPLIERTYPE")) {
                            sourcingTypeImporter.importSupplierTypeObjects(i, PLMObjectType.valueOf("SUPPLIERTYPE"), stringListHashMap, "Supplier");
                        }
                    }
                } else {
                    throw new CassiniException("Please provide Object Type for row number:" + (i));
                }

            } else {
                throw new CassiniException("Please provide Object Type column");
            }
            i++;
        }
    }

    public Date parseDate(String stringDate, String format) throws ParseException {
        Date date = new SimpleDateFormat(format).parse(stringDate);
        return date;
    }
}
