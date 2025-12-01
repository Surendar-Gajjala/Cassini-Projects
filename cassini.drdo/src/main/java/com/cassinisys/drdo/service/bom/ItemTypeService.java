package com.cassinisys.drdo.service.bom;

import com.cassinisys.drdo.model.bom.*;
import com.cassinisys.drdo.model.dto.TypeCodeDto;
import com.cassinisys.drdo.repo.bom.*;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.converter.ImportConverter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 03-10-2018.
 */
@Service
public class ItemTypeService implements CrudService<ItemType, Integer> {

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemAttributeValueRepository itemAttributeValueRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ItemTypeSpecsRepository itemTypeSpecsRepository;
    @Autowired
    private LovRepository lovRepository;

    @Override
    @Transactional(readOnly = false)
    public ItemType create(ItemType itemType) {

        if (itemType.getTypeCode() != null) {
            ItemType existCodeItemType = itemTypeRepository.findByTypeCode(itemType.getTypeCode());
            if (existCodeItemType != null) {
                throw new CassiniException(itemType.getTypeCode() + " Code already exist");
            }
            itemType.setTypeCode(itemType.getTypeCode().toUpperCase());
        }
        return itemTypeRepository.save(itemType);
    }

    @Override
    @Transactional(readOnly = false)
    public ItemType update(ItemType itemType) {

        if (itemType.getTypeCode() != null) {
            ItemType existCodeItemType = itemTypeRepository.findByTypeCode(itemType.getTypeCode());
            if (existCodeItemType != null && !existCodeItemType.getId().equals(itemType.getId())) {
                throw new CassiniException(itemType.getTypeCode() + " Code already exist");
            }
            itemType.setTypeCode(itemType.getTypeCode().toUpperCase());
        }

        if (!itemType.getHasSpec()) {
            List<ItemTypeSpecs> itemTypeSpecs = itemTypeSpecsRepository.findByItemType(itemType.getId());
            if (itemTypeSpecs.size() > 0) {
                for (ItemTypeSpecs itemTypeSpec : itemTypeSpecs) {
                    itemTypeSpecsRepository.delete(itemTypeSpec.getId());
                }
            }
        }
        return itemTypeRepository.save(itemType);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer typeId) {
        itemTypeRepository.delete(typeId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemType get(Integer typeId) {
        ItemType itemType = itemTypeRepository.findOne(typeId);
        itemType.setParentNodeItemType(getParentType(itemType).getName());
        return itemType;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemType> getAll() {
        return itemTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ItemType> getClassificationTree() {
        List<ItemType> types = getRootTypes();

        for (ItemType type : types) {
            type.setParentNodeItemType(getParentType(type).getName());
            visitChildren(type);
        }

        return types;
    }

    private void visitChildren(ItemType parent) {
        List<ItemType> children = getChildren(parent.getId());

        for (ItemType child : children) {
            child.setParentNodeItemType(getParentType(child).getName());
            visitChildren(child);
        }

        parent.setChildren(children);
    }

    @Transactional(readOnly = true)
    public List<ItemType> getRootTypes() {
        return itemTypeRepository.findByParentTypeIsNullOrderByNameCreatedDateAsc();
    }

    @Transactional(readOnly = true)
    public List<ItemType> getChildren(Integer parent) {
        return itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(parent);
    }

    @Transactional(readOnly = true)
    public List<ItemType> getHierarchicalChildren(ItemType itemType) {
        List<ItemType> itemTypes = new ArrayList();
        itemTypes.add(itemType);
        List<ItemType> itemTypes1 = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemType.getId());
        if (itemTypes1.size() > 0) {
            for (ItemType type : itemTypes1) {
                itemTypes.add(type);
                getHierarchicalChildren(type, itemTypes);
            }
        }
        return itemTypes;
    }

    private void getHierarchicalChildren(ItemType itemType, List<ItemType> itemTypes) {
        List<ItemType> itemTypes1 = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemType.getId());
        if (itemTypes1.size() > 0) {
            for (ItemType type : itemTypes1) {
                itemTypes.add(type);
                getHierarchicalChildren(type, itemTypes);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ItemType> findMultiple(List<Integer> ids) {
        return itemTypeRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByTypeId(Integer itemTypeId) {
        ItemType itemType = itemTypeRepository.findOne(itemTypeId);
        List<Item> items = itemRepository.findByItemType(itemType);

        List<ItemType> itemTypeChildren = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemTypeId);


        return items;
    }

    @Transactional
    public ItemTypeAttribute createAttribute(ItemTypeAttribute attribute) {
        return itemTypeAttributeRepository.save(attribute);
    }

    @Transactional
    public ItemTypeAttribute updateAttribute(ItemTypeAttribute attribute) {
        return itemTypeAttributeRepository.save(attribute);

    }

    @Transactional
    public void deleteAttribute(Integer id) {
        ItemTypeAttribute attribute = itemTypeAttributeRepository.findOne(id);
        itemTypeAttributeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ItemTypeAttribute getAttribute(Integer id) {
        return itemTypeAttributeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<ItemTypeAttribute> getAttributes(Integer typeId, Boolean hierarchy) {
        if (!hierarchy) {
            return itemTypeAttributeRepository.findByItemTypeOrderByName(typeId);
        } else {
            return getAttributesFromHierarchy(typeId);
        }
    }

    private List<ItemTypeAttribute> getAttributesFromHierarchy(Integer typeId) {
        List<ItemTypeAttribute> collector = new ArrayList<>();

        List<ItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderByName(typeId);
        collector.addAll(atts);
        collector = collectAttributesFromHierarchy(collector, typeId);

        return collector;
    }

    private List<ItemTypeAttribute> collectAttributesFromHierarchy(List<ItemTypeAttribute> collector, Integer typeId) {
        ItemType itemType = itemTypeRepository.findOne(typeId);
        if (itemType != null) {
            Integer parentType = itemType.getParentType();
            if (parentType != null) {
                List<ItemTypeAttribute> atts = itemTypeAttributeRepository.findByItemTypeOrderByName(parentType);
                collector.addAll(atts);
                collector = collectAttributesFromHierarchy(collector, parentType);
            }
        }

        return collector;
    }

    @Transactional(readOnly = true)
    public List<ItemAttributeValue> getItemUsedAttributes(Integer attributeId) {
        List<ItemAttributeValue> itemAttributes = itemAttributeValueRepository.findByAttributeId(attributeId);
        return itemAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getAllObjectTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByAutoNumberId(Integer autoNumberId) {
        AutoNumber autoNumber = autoNumberRepository.findOne(autoNumberId);
        List<ItemType> itemTypes = itemTypeRepository.findByItemNumberSource(autoNumber);

        List<Integer> ItemIds = new ArrayList<>();
        if (!itemTypes.isEmpty()) {
            for (ItemType type : itemTypes) {
                ItemIds.add(type.getId());
            }
        }
        List<Item> items = itemRepository.findByItemTypeIdIn(ItemIds);

        return items;
    }

    @Transactional(readOnly = true)
    public TypeCodeDto getTypeParentNode(Integer typeId) {
        TypeCodeDto typeCodeDto = new TypeCodeDto();

        ItemType itemType = itemTypeRepository.findOne(typeId);

        typeCodeDto.getItemTypeSpecs().addAll(itemTypeSpecsRepository.findByItemType(itemType.getId()));

        if (itemType.getParentType() != null) {
            itemType = getParentType(itemType);
        }

        typeCodeDto.setItemType(itemType);


        /*typeCodeDto.getItems().addAll(itemRepository.findByItemType(itemType));


        typeCodeDto.getItems().addAll(getChildrenItems(itemType, typeCodeDto.getItems()));*/

        return typeCodeDto;
    }

    @Transactional(readOnly = true)
    public List<Item> getItemWithTypeParentNode(Integer typeId) {
        TypeCodeDto typeCodeDto = new TypeCodeDto();

        ItemType itemType = itemTypeRepository.findOne(typeId);

        if (itemType.getParentType() != null) {
            itemType = getParentType(itemType);
        }

        List<Item> itemList = new ArrayList<>();

        itemList.addAll(itemRepository.findByItemType(itemType));


        itemList.addAll(getChildrenItems(itemType, typeCodeDto.getItems()));

        return itemList;
    }

    @Transactional(readOnly = true)
    public ItemType getParentType(ItemType itemType) {
        if (itemType.getParentType() != null) {
            itemType = itemTypeRepository.findOne(itemType.getParentType());
            itemType = getParentType(itemType);
        }

        return itemType;
    }

    private List<Item> getChildrenItems(ItemType itemType, List<Item> items) {

        List<ItemType> children = itemTypeRepository.findByParentTypeOrderByNameCreatedDateAsc(itemType.getId());

        for (ItemType child : children) {
            items.addAll(itemRepository.findByItemType(child));
            items = getChildrenItems(child, items);
        }

        return items;
    }

    @Transactional(readOnly = false)
    public List<ItemTypeSpecs> getItemTypeSpecs(Integer typeId) {
        return itemTypeSpecsRepository.findByItemType(typeId);
    }

    @Transactional(readOnly = false)
    public ItemTypeSpecs createItemTypeSpec(Integer typeId, ItemTypeSpecs itemTypeSpec) {
        itemTypeSpec.setItemType(typeId);

        ItemTypeSpecs existSpec = itemTypeSpecsRepository.findByItemTypeAndSpecName(typeId, itemTypeSpec.getSpecName());

        if (existSpec != null) {
            throw new CassiniException(itemTypeSpec.getSpecName() + " : Specification already Exist");
        }

        ItemType itemType = itemTypeRepository.findOne(typeId);
        if (!itemType.getHasSpec()) {
            itemType.setHasSpec(true);

            itemType = itemTypeRepository.save(itemType);
        }

        return itemTypeSpecsRepository.save(itemTypeSpec);
    }

    @Transactional(readOnly = false)
    public ItemTypeSpecs updateItemTypeSpec(Integer typeId, Integer specId, ItemTypeSpecs itemTypeSpec) {
        itemTypeSpec.setItemType(typeId);

        ItemTypeSpecs existSpec = itemTypeSpecsRepository.findByItemTypeAndSpecName(typeId, itemTypeSpec.getSpecName());

        if (existSpec != null && !existSpec.getId().equals(itemTypeSpec.getId())) {
            throw new CassiniException(itemTypeSpec.getSpecName() + " : Specification already Exist");
        }

        return itemTypeSpecsRepository.save(itemTypeSpec);
    }

    @Transactional(readOnly = false)
    public void deleteItemTypeSpec(Integer typeId, Integer specId) {
        itemTypeSpecsRepository.delete(specId);
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsBySpec(Integer typeId, Integer specId) {
        ItemTypeSpecs itemTypeSpec = itemTypeSpecsRepository.findOne(specId);

        List<Item> itemList = itemRepository.findByPartSpec(itemTypeSpec);

        return itemList;
    }

    @Transactional(readOnly = true)
    public List<ItemType> getAllItemTypes() {
        List<ItemType> itemTypes = itemTypeRepository.getAllItemTypesWithTypeCodeIsNotNull();

        List<ItemType> itemTypeList = new ArrayList<>();
        itemTypes.forEach(itemType -> {
            if (itemType.getTypeCode() != null && !itemType.getTypeCode().equals("") && !itemType.getTypeCode().isEmpty()) {
                itemTypeList.add(itemType);
            }
        });
        return itemTypeList;
    }

    @Transactional(readOnly = true)
    public List<ItemType> getAllItemTypesWithoutSystem() {
        List<ItemType> itemTypes = itemTypeRepository.getAllItemTypesWithTypeCodeIsNotNullAndHasBomIsFalse();

        List<ItemType> itemTypeList = new ArrayList<>();
        for (ItemType itemType : itemTypes) {
            ItemType parentItemType = getParentType(itemType);

            if (parentItemType.getName().equals("Part") && itemType.getTypeCode() != null && !itemType.getTypeCode().equals("") && !itemType.getTypeCode().isEmpty()) {
                itemTypeList.add(itemType);
            }
        }

        return itemTypeList;
    }

    @Transactional(readOnly = false)
    public void importClassification(Integer parentId, MultipartHttpServletRequest request) throws Exception {
        for (MultipartFile file1 : request.getFileMap().values()) {
            java.io.File file = ImportConverter.trimAndConvertMultipartFileToFile(file1);
            if (file != null && file.getName().trim().endsWith(".xls") || file.getName().trim().endsWith(".xlsx")) {
                Workbook workbook = WorkbookFactory.create(file);
                int totalSheets = workbook.getNumberOfSheets();
                AutoNumber number = autoNumberRepository.findByName("Default Part Number Source");
                Lov revisionSequence = lovRepository.findByName("Default Revision Sequence");
                for (int j = 0; j < totalSheets; j++) {
                    int i = 0;
                    Sheet worksheet = workbook.getSheetAt(j);
                    int lastRow = worksheet.getLastRowNum();
                    if (lastRow > 1) {
                        while (i <= lastRow) {
                            Row row = worksheet.getRow(i++);
                            ItemType parentType = itemTypeRepository.findOne(parentId);
                            if (row.getRowNum() == 0) continue;
                            ItemType itemType1 = new ItemType();
                            if (row.getCell(2) != null) {
                                itemType1.setDescription(row.getCell(2).getStringCellValue());
                            }
                            itemType1.setItemNumberSource(number);
                            itemType1.setRevisionSequence(revisionSequence);
                            if (row.getCell(3) != null && row.getCell(3).getStringCellValue() != null && row.getCell(3).getStringCellValue() != "") {
                                ItemType existCodeItemType = itemTypeRepository.findByTypeCode(row.getCell(3).getStringCellValue().toUpperCase());
                                if (existCodeItemType != null) {
                                    throw new CassiniException(row.getCell(3).getStringCellValue().toUpperCase() + " Code already exist");
                                }
                                itemType1.setTypeCode(row.getCell(3).getStringCellValue().toUpperCase());
                            }
                            if (row.getCell(4) != null && row.getCell(4).getStringCellValue() != null && row.getCell(4).getStringCellValue() != "") {
                                itemType1.setUnits(row.getCell(4).getStringCellValue());
                            } else {
                                itemType1.setUnits("Nos");
                            }

                            if (row.getCell(1) != null && row.getCell(1).getStringCellValue() != null && row.getCell(1).getStringCellValue() != "") {
                                parentType = itemTypeRepository.findByNameIgnoreCase(row.getCell(1).getStringCellValue());
                                if (parentType != null) {
                                    itemType1.setParentType(parentType.getId());
                                } else {
                                    parentType = new ItemType();
                                    parentType.setName(row.getCell(1).getStringCellValue());
                                    parentType.setDescription(row.getCell(1).getStringCellValue());
                                    parentType.setParentType(parentId);
                                    parentType.setUnits(row.getCell(4).getStringCellValue());
                                    parentType.setItemNumberSource(number);
                                    parentType.setRevisionSequence(revisionSequence);
                                    parentType = itemTypeRepository.save(parentType);
                                    itemType1.setParentType(parentType.getId());
                                }

                            } else {
                                itemType1.setParentType(parentType.getId());
                            }

                            if (row.getCell(0) != null && row.getCell(0).getStringCellValue() != null && row.getCell(0).getStringCellValue() != "") {
                                List<ItemType> itemTypeList2 = itemTypeRepository.findByNameIgnoreCaseAndParentType(row.getCell(0).getStringCellValue(), parentType.getId());

                                if (itemTypeList2.size() > 0) {
                                    throw new CassiniException(row.getCell(0).getStringCellValue() + " Name already exist");
                                }
                                itemType1.setName(row.getCell(0).getStringCellValue());
                            }

                            if (row.getCell(5) != null && row.getCell(5).getStringCellValue().equalsIgnoreCase("TRUE")) {
                                itemType1.setHasLots(Boolean.TRUE);
                            }

                            if (row.getCell(6) != null && row.getCell(6).getStringCellValue() != null && row.getCell(6).getStringCellValue() != "") {
                                String[] specs = row.getCell(6).getStringCellValue().split(",");
                                List<ItemTypeSpecs> itemTypeSpecses = new ArrayList();
                                if (specs.length > 0) {
                                    itemType1.setHasSpec(Boolean.TRUE);
                                    ItemType itemType2 = itemTypeRepository.save(itemType1);
                                    for (String spec : specs) {
                                        ItemTypeSpecs itemTypeSpecs = new ItemTypeSpecs();
                                        spec = spec.replaceAll("\\s", "");
                                        itemTypeSpecs.setItemType(itemType2.getId());
                                        itemTypeSpecs.setSpecName(spec);
                                        itemTypeSpecses.add(itemTypeSpecs);
                                    }
                                } else {
                                    ItemType itemType2 = itemTypeRepository.save(itemType1);
                                }
                                itemTypeSpecsRepository.save(itemTypeSpecses);
                            } else {
                                ItemType itemType2 = itemTypeRepository.save(itemType1);
                            }
                        }
                    } else {
                        throw new CassiniException("Provided excel file is empty!");
                    }

                }

                workbook.close();

            } else {
                throw new CassiniException("Please upload excel sheet with proper Name & Code data");
            }
        }
    }
}