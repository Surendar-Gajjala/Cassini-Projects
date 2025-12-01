package com.cassinisys.is.service.procm;

/*import com.cassinisys.is.filtering.ItemPredicateBuilder;*/

import com.cassinisys.is.filtering.*;
import com.cassinisys.is.model.pm.ISProjectPerson;
import com.cassinisys.is.model.pm.ISProjectResource;
import com.cassinisys.is.model.pm.QISProjectPerson;
import com.cassinisys.is.model.pm.ResourceType;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.ISStockIssue;
import com.cassinisys.is.model.store.ISStockReceive;
import com.cassinisys.is.repo.pm.ISProjectPersonRepository;
import com.cassinisys.is.repo.pm.ResourceRepository;
import com.cassinisys.is.repo.procm.*;
import com.cassinisys.is.repo.store.*;
import com.cassinisys.is.service.login.LoginService;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for ItemService
 */
@Service
@Transactional
public class ItemService {

    @Autowired
    public MaterialItemRepository materialItemRepository;
    @Autowired
    private ISProjectPersonRepository projectPersonRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private MachineItemRepository machineItemRepository;

    @Autowired
    private MachineTypeRepository machineTypeRepository;

    @Autowired
    private ManpowerItemRepository manpowerItemRepository;

    @Autowired
    private ManpowerTypeRepository manpowerTypeRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private MaterialItemAttributeRepository materialItemAttributeRepository;

    @Autowired
    private MachineItemAttributeRepository machineItemAttributeRepository;

    @Autowired
    private ManpowerItemAttributeRepository manpowerItemAttributeRepository;

    @Autowired
    private ISTopInventoryRepository topInventoryRepository;

    @Autowired
    private MaterialPredicateBuilder materialPredicateBuilder;

    @Autowired
    private MachinePredicateBuilder machinePredicateBuilder;

    @Autowired
    private ManpowerPredicateBuilder manpowerPredicateBuilder;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BoqItemRepository boqItemRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private ProjectPersonPredicateBuilder projectPersonPredicateBuilder;

    @Autowired
    private ISTStockIssueItemRepository topStockIssuedRepository;

    @Autowired
    private ISStockReceiveRepository stockReceiveRepository;

    @Autowired
    private ISStockIssueRepository stockIssueRepository;

    @Autowired
    private ReceiveTypeItemAttributeRepository receiveTypeItemAttributeRepository;

    @Autowired
    private IssueTypeItemAttributeRepository issueTypeItemAttributeRepository;

    @Autowired
    private MaterialReceiveTypeRepository materialReceiveTypeRepository;

    @Autowired
    private MaterialIssueTypeRepository materialIssueTypeRepository;

    @Autowired
    private ISStockReturnItemRepository stockReturnItemRepository;

    @Autowired
    private ISTopStoreRepository storeRepository;

    /**
     * The method used to create ISMaterialItem
     **/
    @Transactional(readOnly = false)
    public ISMaterialItem create(ISMaterialItem item) {
        checkNotNull(item);
        item.setId(null);
        item.setItemType(materialTypeRepository.findOne(item.getItemType().getId()));
        item = materialItemRepository.save(item);
        return item;
    }

    @Transactional(readOnly = false)
    public ISMachineItem create(ISMachineItem item) {
        checkNotNull(item);
        item.setId(null);
        item.setItemType(machineTypeRepository.findOne(item.getItemType().getId()));
        item = machineItemRepository.save(item);
        return item;
    }

    @Transactional(readOnly = false)
    public ISManpowerItem create(ISManpowerItem item) {
        checkNotNull(item);
        if (item.getPerson().getId() == null) {
            Person person = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(item.getPerson().getFirstName(), item.getPerson().getLastName());
            if (person != null) {
                throw new RuntimeException("Person name already exists");
            } else {
                person = personRepository.save(item.getPerson());
                item.setPerson(person);
            }
        }
        ISManpowerItem manpowerItem = manpowerItemRepository.findByItemNumber(item.getItemNumber());
        if (manpowerItem == null) {
            item.setItemType(manpowerTypeRepository.findOne(item.getItemType().getId()));
            item = manpowerItemRepository.save(item);
        } else {
            throw new RuntimeException(item.getItemNumber() + " already exists");
        }
        return item;

    }

    @Transactional(readOnly = false)
    public List<ISManpowerItem> createMultipleManpower(List<ISManpowerItem> manpowerItemList) {
        List<ISManpowerItem> items = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        AutoNumber autoNumber = autoNumberService.getByName("Default Manpower Item Number Source");
        numbers = autoNumberService.getNextNumbers(autoNumber.getId(), manpowerItemList.size());
        for (Integer i = 0; i < manpowerItemList.size(); i++) {
            manpowerItemList.get(i).setItemNumber(numbers.get(i));
            items.add(manpowerItemList.get(i));
        }
        return manpowerItemRepository.save(items);
    }

    /**
     * The method used to update ISMaterialItem
     **/
    @Transactional(readOnly = false)
    //@Override
    public ISMaterialItem update(Integer id, ISMaterialItem item) {
        checkNotNull(item);
        item = materialItemRepository.save(item);
        List<ISBoqItem> isBoqItems = boqItemRepository.findByItemNumber(item.getItemNumber());
        for (ISBoqItem isBoqItem : isBoqItems) {
            if (isBoqItem != null) {
                isBoqItem.setItemName(item.getItemName());
                if (item.getDescription() != null) {
                    isBoqItem.setDescription(item.getDescription());
                }
                isBoqItem.setUnits(item.getUnits());
                boqItemRepository.save(isBoqItem);
            }
        }
        return item;
    }

    @Transactional(readOnly = false)
    public ISMachineItem updateMachineitem(ISMachineItem item) {
        checkNotNull(item);
        item = machineItemRepository.save(item);
        List<ISBoqItem> isBoqItems = boqItemRepository.findByItemNumber(item.getItemNumber());
        for (ISBoqItem isBoqItem : isBoqItems) {
            if (isBoqItem != null) {
                isBoqItem.setItemName(item.getItemName());
                if (item.getDescription() != null) {
                    isBoqItem.setDescription(item.getDescription());
                }
                isBoqItem.setUnits(item.getUnits());
                boqItemRepository.save(isBoqItem);
            }
        }
        return item;
    }

    @Transactional(readOnly = false)
    public ISManpowerItem updateManpowerItem(ISManpowerItem item) {
        checkNotNull(item);
        Person person = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(item.getPerson().getFirstName(), item.getPerson().getLastName());
        if (person != null && !person.getId().equals(item.getPerson().getId())) {
            throw new RuntimeException("Person name already exists");
        } else {
            personRepository.save(item.getPerson());
        }
        item = manpowerItemRepository.save(item);
        return item;
    }

    /**
     * The method used to delete
     **/
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISMaterialItem item = materialItemRepository.findOne(id);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        materialItemRepository.delete(id);
    }

    @Transactional(readOnly = false)
    public void deleteManpower(Integer id) {
        checkNotNull(id);
        ISManpowerItem item = manpowerItemRepository.findOne(id);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        item.setPerson(null);
        item = manpowerItemRepository.save(item);
        manpowerItemRepository.delete(item.getId());
    }

    @Transactional(readOnly = false)
    public void deleteMachine(Integer id) {
        checkNotNull(id);
        ISMachineItem item = machineItemRepository.findOne(id);
        if (item == null) {
            throw new ResourceNotFoundException();
        }
        machineItemRepository.delete(id);
    }

    /**
     * The method used to get ISMaterialItem
     **/
    @Transactional(readOnly = true)
    public ISMaterialItem get(Integer id) {
        checkNotNull(id);
        return materialItemRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public ISMachineItem getMachine(Integer id) {
        checkNotNull(id);
        return machineItemRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public ISManpowerItem getManpower(Integer id) {
        checkNotNull(id);
        return manpowerItemRepository.findOne(id);
    }

    /**
     * The method used to getAll for the list of ISMaterialItem
     **/
    @Transactional(readOnly = true)
    public List<ISMaterialItem> getAll() {
        return materialItemRepository.findAll();
    }

    public List<ISMachineItem> getAllMachine() {
        return machineItemRepository.findAll();
    }

    public List<ISManpowerItem> getAllManpower() {
        return manpowerItemRepository.findAll();
    }

    /**
     * The method used to findAll for the page of ISMaterialItem
     **/
    @Transactional(readOnly = true)
    public Page<ISMaterialItem> findAll(Pageable pageable) {
        return materialItemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISMachineItem> findAllMachine(Pageable pageable) {
        return machineItemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISManpowerItem> findAllManpower(Pageable pageable) {
        return manpowerItemRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerItem> findAllActiveManpower() {
        List<ISManpowerItem> manpowerItemList = new ArrayList<>();
        Hashtable<Integer, Person> manPowerTable = new Hashtable();
        List<ISManpowerItem> items = manpowerItemRepository.findAll();
        for (ISManpowerItem item : items) {
            if (item.getPerson().getObjectType() == ObjectType.valueOf("PERSON")) {
                Login login = loginService.getByPersonId(item.getPerson().getId());
                if (login != null) {
                    if (login.getIsActive() == true) {
                        Person person = manPowerTable.get(item.getPerson().getId());
                        if (person == null) {
                            manpowerItemList.add(item);
                            manPowerTable.put(item.getPerson().getId(), item.getPerson());
                        }
                    }
                } else {
                    manpowerItemList.add(item);
                }
            }
        }
        return manpowerItemList;
    }

    /**
     * methods used to find the materials by name
     **/
    @Transactional(readOnly = true)
    public ISMaterialType findMaterialsByName(String name) {
        return materialTypeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public ISMaterialType getMaterialTypeByItemNumber(String itemNumber) {
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(itemNumber);
        return materialTypeRepository.findOne(materialItem.getItemType().getId());
    }

    @Transactional(readOnly = true)
    public List<ISManpowerType> findManpowersByName(String name) {
        return manpowerTypeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public ISMachineType findMachinesByName(String name) {
        return machineTypeRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public ISMachineType getMachineTypeByItemNumber(String itemNumber) {
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(itemNumber);
        return machineTypeRepository.findOne(machineItem.getItemType().getId());
    }

    /**
     * methods used to findAll for the page of ISMaterialItem & type
     **/
    @Transactional(readOnly = true)
    public Page<ISMachineItem> findMachinesByType(Integer typeId, Pageable pageable) {
        return machineItemRepository.findByItemTypeId(typeId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISMaterialItem> findMaterialsByType(Integer typeId, Pageable pageable) {
        return materialItemRepository.findByItemTypeId(typeId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISStockReceive> getMaterialReceivesByType(Integer typeId, Pageable pageable) {
        ISMaterialReceiveType materialReceiveType = materialReceiveTypeRepository.findOne(typeId);
        return stockReceiveRepository.findAllByMaterialReceiveType(materialReceiveType, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISStockIssue> getMaterialIssuessByType(Integer typeId, Pageable pageable) {
        ISMaterialIssueType materialIssueType = materialIssueTypeRepository.findOne(typeId);
        return stockIssueRepository.findByMaterialIssueType(materialIssueType, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ISManpowerItem> findManpowerByType(Integer typeId, Pageable pageable) {
        return manpowerItemRepository.findByItemTypeId(typeId, pageable);
    }

    /**
     * The method used to getItemsByType for the page of ISMaterialItem
     **/
    public Page<ISMaterialItem> getMaterialItemsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        ISMaterialType type = materialTypeRepository.findOne(typeId);
        collectHierarchyMaterialTypeIds(ids, type);
        return materialItemRepository.findByItemTypeIds(ids, pageable);
    }

    public Page<ISMachineItem> getMachineItemsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        ISMachineType type = machineTypeRepository.findOne(typeId);
        collectHierarchyMachineTypeIds(ids, type);
        return machineItemRepository.findByItemTypeIds(ids, pageable);
    }

    public Page<ISManpowerItem> getManpowerItemsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        ISManpowerType type = manpowerTypeRepository.findOne(typeId);
        collectHierarchyManpowerTypeIds(ids, type);
        return manpowerItemRepository.findByItemTypeIds(ids, pageable);
    }

    /**
     * The method used to collectHierarchyTypeIds
     **/
    private void collectHierarchyMaterialTypeIds(List<Integer> collector, ISMaterialType type) {
        if (type != null) {
            collector.add(type.getId());
            List<ISMaterialType> children = materialTypeRepository.findByParentTypeOrderByName(type.getId());
            for (ISMaterialType child : children) {
                collectHierarchyMaterialTypeIds(collector, child);
            }
        }
    }

    private void collectHierarchyMachineTypeIds(List<Integer> collector, ISMachineType type) {
        if (type != null) {
            collector.add(type.getId());
            List<ISMachineType> children = machineTypeRepository.findByParentTypeOrderByName(type.getId());
            for (ISMachineType child : children) {
                collectHierarchyMachineTypeIds(collector, child);
            }
        }
    }

    private void collectHierarchyManpowerTypeIds(List<Integer> collector, ISManpowerType type) {
        if (type != null) {
            collector.add(type.getId());
            List<ISManpowerType> children = manpowerTypeRepository.findByParentTypeOrderByName(type.getId());
            for (ISManpowerType child : children) {
                collectHierarchyManpowerTypeIds(collector, child);
            }
        }
    }

    /**
     * The method used to findMultiple for the list of ISMaterialItem
     **/
    @Transactional(readOnly = true)
    public List<ISMaterialItem> findMultiple(List<Integer> ids) {
        return materialItemRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMachineItem> findMultipleItem(List<Integer> ids) {
        return machineItemRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialItem> getMaterialsByItemNumbers(List<String> itemNumbers) {
        return materialItemRepository.findByItemNumberIn(itemNumbers);
    }

    @Transactional(readOnly = true)
    public List<ISMachineItem> getMachinesByItemNumbers(List<String> itemNumbers) {
        return machineItemRepository.findByItemNumberIn(itemNumbers);
    }

    /**
     * The method used to getItemAttributes for the list of ISMaterialItem
     **/
    @Transactional(readOnly = true)
    public List<ISMaterialItemAttribute> getMaterialItemAttributes(Integer itemId) {
        return materialItemAttributeRepository.findByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<ISMachineItemAttribute> getMachineItemAttributes(Integer itemId) {
        return machineItemAttributeRepository.findByItemId(itemId);
    }

    public List<ISManpowerItemAttribute> getManpowerItemAttributes(Integer itemId) {
        return manpowerItemAttributeRepository.findByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<ISReceiveTypeItemAttribute> getReceiveTypeItemAttributes(Integer itemId) {
        return receiveTypeItemAttributeRepository.findByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<ISIssueTypeItemAttribute> getIssueTypeItemAttributes(Integer itemId) {
        return issueTypeItemAttributeRepository.findByItemId(itemId);
    }

    /**
     * The method used to saveItemAttributes for the list of ISMaterialItem
     **/
    public List<ObjectAttribute> saveMaterialItemAttributes(List<ISMaterialItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISMaterialItemAttribute attribute : attributes) {
            ISMaterialItemAttribute materialAttribute = new ISMaterialItemAttribute();
            ISMaterialItem at = materialItemRepository.findOne(attribute.getId().getObjectId());
            materialAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            materialAttribute.setStringValue(attribute.getStringValue());
            materialAttribute.setIntegerValue(attribute.getIntegerValue());
            materialAttribute.setBooleanValue(attribute.getBooleanValue());
            materialAttribute.setDoubleValue(attribute.getDoubleValue());
            materialAttribute.setTimeValue(attribute.getTimeValue());
            materialAttribute.setTimestampValue(attribute.getTimestampValue());
            materialAttribute.setAttachmentValues(attribute.getAttachmentValues());
            materialAttribute.setDateValue(attribute.getDateValue());
            materialAttribute.setCurrencyType(attribute.getCurrencyType());
            materialAttribute.setCurrencyValue(attribute.getCurrencyValue());
            materialAttribute.setRefValue(attribute.getRefValue());
            materialAttribute.setListValue(attribute.getListValue());
            ISMaterialItemAttribute itemAttribute1 = materialItemAttributeRepository.save(materialAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public List<ObjectAttribute> saveMachineItemAttributes(List<ISMachineItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISMachineItemAttribute attribute : attributes) {
            ISMachineItemAttribute machineAttribute = new ISMachineItemAttribute();
            ISMachineItem at = machineItemRepository.findOne(attribute.getId().getObjectId());
            machineAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            machineAttribute.setStringValue(attribute.getStringValue());
            machineAttribute.setIntegerValue(attribute.getIntegerValue());
            machineAttribute.setBooleanValue(attribute.getBooleanValue());
            machineAttribute.setDoubleValue(attribute.getDoubleValue());
            machineAttribute.setTimeValue(attribute.getTimeValue());
            machineAttribute.setTimestampValue(attribute.getTimestampValue());
            machineAttribute.setAttachmentValues(attribute.getAttachmentValues());
            machineAttribute.setDateValue(attribute.getDateValue());
            machineAttribute.setCurrencyType(attribute.getCurrencyType());
            machineAttribute.setCurrencyValue(attribute.getCurrencyValue());
            machineAttribute.setRefValue(attribute.getRefValue());
            machineAttribute.setListValue(attribute.getListValue());
            ISMachineItemAttribute itemAttribute1 = machineItemAttributeRepository.save(machineAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public List<ObjectAttribute> saveManpowerItemAttributes(List<ISManpowerItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISManpowerItemAttribute attribute : attributes) {
            ISManpowerItemAttribute manpowerAttribute = new ISManpowerItemAttribute();
            ISManpowerItem at = manpowerItemRepository.findOne(attribute.getId().getObjectId());
            manpowerAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            manpowerAttribute.setStringValue(attribute.getStringValue());
            manpowerAttribute.setIntegerValue(attribute.getIntegerValue());
            manpowerAttribute.setBooleanValue(attribute.getBooleanValue());
            manpowerAttribute.setDoubleValue(attribute.getDoubleValue());
            manpowerAttribute.setTimeValue(attribute.getTimeValue());
            manpowerAttribute.setTimestampValue(attribute.getTimestampValue());
            manpowerAttribute.setAttachmentValues(attribute.getAttachmentValues());
            manpowerAttribute.setDateValue(attribute.getDateValue());
            manpowerAttribute.setCurrencyType(attribute.getCurrencyType());
            manpowerAttribute.setCurrencyValue(attribute.getCurrencyValue());
            manpowerAttribute.setRefValue(attribute.getRefValue());
            manpowerAttribute.setListValue(attribute.getListValue());
            ISManpowerItemAttribute itemAttribute1 = manpowerItemAttributeRepository.save(manpowerAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public List<ObjectAttribute> saveReceiveTypeItemAttributes(List<ISReceiveTypeItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISReceiveTypeItemAttribute attribute : attributes) {
            ISReceiveTypeItemAttribute receiveTypeItemAttribute = new ISReceiveTypeItemAttribute();
            ISStockReceive at = stockReceiveRepository.findOne(attribute.getId().getObjectId());
            receiveTypeItemAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            receiveTypeItemAttribute.setStringValue(attribute.getStringValue());
            receiveTypeItemAttribute.setIntegerValue(attribute.getIntegerValue());
            receiveTypeItemAttribute.setBooleanValue(attribute.getBooleanValue());
            receiveTypeItemAttribute.setDoubleValue(attribute.getDoubleValue());
            receiveTypeItemAttribute.setTimeValue(attribute.getTimeValue());
            receiveTypeItemAttribute.setTimestampValue(attribute.getTimestampValue());
            receiveTypeItemAttribute.setAttachmentValues(attribute.getAttachmentValues());
            receiveTypeItemAttribute.setDateValue(attribute.getDateValue());
            receiveTypeItemAttribute.setCurrencyType(attribute.getCurrencyType());
            receiveTypeItemAttribute.setCurrencyValue(attribute.getCurrencyValue());
            receiveTypeItemAttribute.setRefValue(attribute.getRefValue());
            receiveTypeItemAttribute.setListValue(attribute.getListValue());
            ISReceiveTypeItemAttribute itemAttribute1 = receiveTypeItemAttributeRepository.save(receiveTypeItemAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    public List<ObjectAttribute> saveIssueTypeItemAttributes(List<ISIssueTypeItemAttribute> attributes) {
        List<ObjectAttribute> objectAttributes = new ArrayList<>();
        for (ISIssueTypeItemAttribute attribute : attributes) {
            ISIssueTypeItemAttribute isIssueTypeItemAttribute = new ISIssueTypeItemAttribute();
            ISStockIssue at = stockIssueRepository.findOne(attribute.getId().getObjectId());
            isIssueTypeItemAttribute.setId(new ObjectAttributeId(at.getId(), attribute.getId().getAttributeDef()));
            isIssueTypeItemAttribute.setStringValue(attribute.getStringValue());
            isIssueTypeItemAttribute.setIntegerValue(attribute.getIntegerValue());
            isIssueTypeItemAttribute.setBooleanValue(attribute.getBooleanValue());
            isIssueTypeItemAttribute.setDoubleValue(attribute.getDoubleValue());
            isIssueTypeItemAttribute.setTimeValue(attribute.getTimeValue());
            isIssueTypeItemAttribute.setTimestampValue(attribute.getTimestampValue());
            isIssueTypeItemAttribute.setAttachmentValues(attribute.getAttachmentValues());
            isIssueTypeItemAttribute.setDateValue(attribute.getDateValue());
            isIssueTypeItemAttribute.setCurrencyType(attribute.getCurrencyType());
            isIssueTypeItemAttribute.setCurrencyValue(attribute.getCurrencyValue());
            isIssueTypeItemAttribute.setRefValue(attribute.getRefValue());
            isIssueTypeItemAttribute.setListValue(attribute.getListValue());
            ISIssueTypeItemAttribute itemAttribute1 = issueTypeItemAttributeRepository.save(isIssueTypeItemAttribute);
            objectAttributes.add(itemAttribute1);
        }
        return objectAttributes;
    }

    /**
     * The method used to updateItemAttributes for the list of ISMaterialItem
     **/
    @Transactional(readOnly = false)
    public ISMaterialItemAttribute updateMaterialItemAttributes(ISMaterialItemAttribute attribute) {
        return materialItemAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISMachineItemAttribute updateMachineItemAttributes(ISMachineItemAttribute attributes) {
        return machineItemAttributeRepository.save(attributes);
    }

    @Transactional(readOnly = false)
    public ISManpowerItemAttribute updateManpowerAttributes(ISManpowerItemAttribute attribute) {
        return manpowerItemAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISReceiveTypeItemAttribute updateReceiveTypeItemAttributes(ISReceiveTypeItemAttribute attribute) {
        return receiveTypeItemAttributeRepository.save(attribute);
    }

    @Transactional(readOnly = false)
    public ISIssueTypeItemAttribute updateIssueTypeItemAttributes(ISIssueTypeItemAttribute attribute) {
        return issueTypeItemAttributeRepository.save(attribute);
    }

    /**
     * The method used to findByItemNumber for the list of ISMaterialItem
     **/
    public ISMaterialItem MaterialfindByItemNumber(String itemNumber) {
        return materialItemRepository.findByItemNumber(itemNumber);
    }

    public ISMachineItem MachinesfindByItemNumber(String itemNumber) {
        return machineItemRepository.findByItemNumber(itemNumber);
    }

    public ISStockReceive findStockReceiveByNumber(String number) {
        return stockReceiveRepository.findByReceiveNumberSource(number);
    }

    public ISStockIssue findStockIssueByNumber(String number) {
        return stockIssueRepository.findByIssueNumberSource(number);
    }

    /**
     * Methods used for freeTextSearch for material, machine & manpower items
     **/
    public Page<ISMaterialItem> materialFreeTextSearch(Pageable pageable, MaterialCriteria criteria) {
        Predicate predicate = materialPredicateBuilder.build(criteria, QISMaterialItem.iSMaterialItem);
        return materialItemRepository.findAll(predicate, pageable);
    }

    public Page<ISMachineItem> machineFreeTextSearch(Pageable pageable, MachineCriteria criteria) {
        Predicate predicate = machinePredicateBuilder.build(criteria, QISMachineItem.iSMachineItem);
        return machineItemRepository.findAll(predicate, pageable);
    }

    public Page<ISManpowerItem> manpowerFreeTextSearch(Pageable pageable, ManpowerCriteria criteria) {
        Predicate predicate = manpowerPredicateBuilder.build(criteria, QISManpowerItem.iSManpowerItem);
        return manpowerItemRepository.findAll(predicate, pageable);
    }

    /**
     * The method used to getAllItemTypeAttributes for ObjectTypeAttribute
     **/
    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getMaterialAttributesRequiredFalse(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectTypeAndRequiredFalse(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getMaterialAttributesRequiredTrue(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByObjectIdsAndAttributeIds(Integer[] objectIds, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();
        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(objectIds, objectAttributeIds);
        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }
            objectAttributes.add(attribute);
        }
        return objectAttributesMap;

    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getItemTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    @Transactional(readOnly = true)
    public void findItemByAutoNumId(Integer autoNumber) {
        List<ISMachineType> machineTypes = machineTypeRepository.findByMachineNumberSourceId(autoNumber);
        List<ISMachineItem> machineItems = new ArrayList<ISMachineItem>();
        List<Integer> machineIds = new ArrayList<>();
        List<Integer> materialIds = new ArrayList<>();
        List<Integer> manpowIds = new ArrayList<>();
        if (!machineTypes.isEmpty()) {
            for (ISMachineType type : machineTypes) {
                machineIds.add(type.getId());
            }
        }
        machineItems = machineItemRepository.findByItemTypeIdIn(machineIds);
        List<ISMaterialType> materialTypes = materialTypeRepository.findByMaterialNumberSourceId(autoNumber);
        List<ISMaterialItem> materialItems = new ArrayList<ISMaterialItem>();
        if (!materialTypes.isEmpty()) {
            for (ISMaterialType type : materialTypes) {
                materialIds.add(type.getId());
            }
        }
        materialItems = materialItemRepository.findByItemTypeIdIn(materialIds);
        List<ISManpowerType> manpowerTypes = manpowerTypeRepository.findByManpowerNumberSourceId(autoNumber);
        List<ISManpowerItem> manpowerItems = new ArrayList<ISManpowerItem>();
        if (!manpowerTypes.isEmpty()) {
            for (ISManpowerType type : manpowerTypes) {
                manpowIds.add(type.getId());
            }
        }
        manpowerItems = manpowerItemRepository.findByItemTypeIdIn(manpowIds);
        if (!machineItems.isEmpty() || !materialItems.isEmpty() || !manpowerItems.isEmpty()) {
            throw new RuntimeException("This auto number has items, Cannot delete this auto number");
        }

    }

    @Transactional(readOnly = true)
    public List<AutoNumber> findAutoNumberItems(List<Integer> ids) {
        List<AutoNumber> autoNumbers = autoNumberRepository.findByIdIn(ids);
        if (autoNumbers.size() != 0) {
            for (AutoNumber autoNumber : autoNumbers) {
                List<ISMachineType> machineTypes = machineTypeRepository.findByMachineNumberSourceId(autoNumber.getId());
                List<ISMachineItem> machineItems = new ArrayList<ISMachineItem>();
                List<Integer> machineIds = new ArrayList<>();
                List<Integer> materialIds = new ArrayList<>();
                List<Integer> manpowIds = new ArrayList<>();
                if (!machineTypes.isEmpty()) {
                    for (ISMachineType type : machineTypes) {
                        machineIds.add(type.getId());
                    }
                }
                machineItems = machineItemRepository.findByItemTypeIdIn(machineIds);
                List<ISMaterialType> materialTypes = materialTypeRepository.findByMaterialNumberSourceId(autoNumber.getId());
                List<ISMaterialItem> materialItems = new ArrayList<ISMaterialItem>();
                if (!materialTypes.isEmpty()) {
                    for (ISMaterialType type : materialTypes) {
                        materialIds.add(type.getId());
                    }
                }
                materialItems = materialItemRepository.findByItemTypeIdIn(materialIds);
                List<ISManpowerType> manpowerTypes = manpowerTypeRepository.findByManpowerNumberSourceId(autoNumber.getId());
                List<ISManpowerItem> manpowerItems = new ArrayList<ISManpowerItem>();
                if (!manpowerTypes.isEmpty()) {
                    for (ISManpowerType type : manpowerTypes) {
                        manpowIds.add(type.getId());
                    }
                }
                manpowerItems = manpowerItemRepository.findByItemTypeIdIn(manpowIds);
                if (machineItems.size() != 0 || materialItems.size() != 0 || manpowerItems.size() != 0) {
                    autoNumber.setItemsExist(true);
                } else {
                    autoNumber.setItemsExist(false);
                }
            }
        }
        return autoNumbers;
    }

    @Transactional(readOnly = true)
    public List<ISMachineItemAttribute> findMachineItemsByAttribute(Integer attributeId) {
        return machineItemAttributeRepository.findByAttributeId(attributeId);
    }

    @Transactional(readOnly = true)
    public List<ISMaterialItemAttribute> findMaterialItemsByAttribute(Integer attributeId) {
        return materialItemAttributeRepository.findByAttributeId(attributeId);
    }

    @Transactional(readOnly = true)
    public List<ISManpowerItemAttribute> findManpowerItemsByAttribute(Integer attributeId) {
        return manpowerItemAttributeRepository.findByAttributeId(attributeId);
    }

    @Transactional(readOnly = true)
    public List<ISReceiveTypeItemAttribute> findReceiveTypeItemsByAttribute(Integer attributeId) {
        return receiveTypeItemAttributeRepository.findByAttributeId(attributeId);
    }

    @Transactional(readOnly = true)
    public List<ISIssueTypeItemAttribute> findIssueTypeItemsByAttribute(Integer attributeId) {
        return issueTypeItemAttributeRepository.findByAttributeId(attributeId);
    }

    @Transactional(readOnly = true)
    public Page<ISMaterialItem> getProjectMaterials(Integer projectId, Pageable pageable) {
        List<ISBoqItem> boqItems = boqItemRepository.findByProject(projectId);
        List<ISMaterialItem> materialItems = new ArrayList<>();
        Map<String, ISBoqItem> map = new HashMap<>();
        for (ISBoqItem boqItem : boqItems) {
            if (boqItem.getItemType() == ResourceType.MATERIALTYPE && map.get(boqItem.getItemNumber()) == null) {
                ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                if (materialItem != null) {
                    materialItems.add(materialItem);
                    map.put(boqItem.getItemNumber(), boqItem);
                }
            }
        }
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > materialItems.size() ? materialItems.size() : (start + pageable.getPageSize());
        return new PageImpl<ISMaterialItem>(materialItems.subList(start, end), pageable, materialItems.size());
    }

    @Transactional(readOnly = true)
    public ISMaterialItem getMaterialByItemNumber(String itemNumber) {
        return materialItemRepository.getByItemNumber(itemNumber);
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemIdsAndAttributeIds(Integer[] materialItems, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();
        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(materialItems, objectAttributeIds);
        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }
            objectAttributes.add(attribute);
        }
        return objectAttributesMap;

    }

    @Transactional(readOnly = true)
    public List<ISBoqItem> getBoqItemByItemNumber(String itemNumber) {
        return boqItemRepository.findByItemNumber(itemNumber);
    }

    @Transactional(readOnly = true)
    public ISMaterialItem findMaterialItemByBoq(Integer boqId) {
        ISBoqItem boqItem = boqItemRepository.findOne(boqId);
        ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
        return materialItem;
    }

    @Transactional(readOnly = true)
    public ISMachineItem findMachineItemByBoq(Integer boqId) {
        ISBoqItem boqItem = boqItemRepository.findOne(boqId);
        ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
        return machineItem;
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByBoqIds(Integer taskId, List<Integer> boqIds) {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        Map<String, ItemDTO> map = new HashMap<>();
        for (Integer boq : boqIds) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItemIssueQuantity(0.0);
            ISBoqItem boqItem = boqItemRepository.findOne(boq);
            ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
            ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
            List<ISProjectResource> projectResources = resourceRepository.findByTaskAndReferenceId(taskId, boq);
            for (ISProjectResource projectResource : projectResources) {
                if (projectResource.getResourceType() != ResourceType.MANPOWERTYPE) {
                    itemDTO.setResourceQuantity(projectResource.getQuantity());
                    if (machineItem != null) {
                        itemDTO.setItemName(machineItem.getItemName());
                        itemDTO.setDescription(machineItem.getDescription());
                        itemDTO.setId(machineItem.getId());
                        itemDTO.setItemNumber(machineItem.getItemNumber());
                        itemDTO.setItemType(machineItem.getItemType().getName());
                    } else if (materialItem != null) {
                        itemDTO.setItemName(materialItem.getItemName());
                        itemDTO.setDescription(materialItem.getDescription());
                        itemDTO.setId(materialItem.getId());
                        itemDTO.setItemNumber(materialItem.getItemNumber());
                        itemDTO.setItemType(materialItem.getItemType().getName());
                    }
                    itemDTO.setItemIssueQuantity(projectResource.getIssuedQuantity());
                    itemDTO.setBoqReference(boq);
                    itemDTO.setProjectResource(projectResource);
                    map.put(boqItem.getItemNumber(), itemDTO);
                    itemDTOs.add(itemDTO);
                }
            }
        }
        return itemDTOs;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialItem> getAllMaterials() {
        return materialItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        List<ISMachineItem> machineItems = machineItemRepository.findAll();
        List<ISMaterialItem> materialItems = materialItemRepository.findAll();
        for (ISMachineItem machineItem : machineItems) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setDescription(machineItem.getDescription());
            itemDTO.setId(machineItem.getId());
            itemDTO.setItemType(machineItem.getItemType().getName());
            itemDTO.setItemName(machineItem.getItemName());
            itemDTO.setItemNumber(machineItem.getItemNumber());
            itemDTO.setResourceType("MACHINE");
            itemDTOs.add(itemDTO);
        }
        for (ISMaterialItem materialItem : materialItems) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setId(materialItem.getId());
            itemDTO.setItemType(materialItem.getItemType().getName());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setResourceType("MATERIAL");
            itemDTOs.add(itemDTO);
        }
        return itemDTOs;
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getProjectItems(Integer projectId) {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        HashMap<String, ItemDTO> hashMap = new HashMap<>();
        List<ISBoqItem> boqItems = boqItemRepository.findByProject(projectId);
        for (ISBoqItem boqItem : boqItems) {
            ItemDTO itemDTO1 = hashMap.get(boqItem.getItemNumber());
            if (itemDTO1 == null) {
                ItemDTO itemDTO = new ItemDTO();
                if (boqItem.getItemType() == ResourceType.MACHINETYPE) {
                    ISMachineItem machineItem = machineItemRepository.findByItemNumber(boqItem.getItemNumber());
                    if (machineItem != null) {
                        itemDTO.setDescription(machineItem.getDescription());
                        itemDTO.setId(machineItem.getId());
                        itemDTO.setItemType(machineItem.getItemType().getName());
                        itemDTO.setItemName(machineItem.getItemName());
                        itemDTO.setItemNumber(machineItem.getItemNumber());
                        itemDTO.setResourceType("MACHINE");
                        itemDTOs.add(itemDTO);
                    }
                } else if (boqItem.getItemType() == ResourceType.MATERIALTYPE) {
                    ISMaterialItem materialItem = materialItemRepository.findByItemNumber(boqItem.getItemNumber());
                    if (materialItem != null) {
                        itemDTO.setDescription(materialItem.getDescription());
                        itemDTO.setId(materialItem.getId());
                        itemDTO.setItemType(materialItem.getItemType().getName());
                        itemDTO.setItemName(materialItem.getItemName());
                        itemDTO.setItemNumber(materialItem.getItemNumber());
                        itemDTO.setResourceType("MATERIAL");
                        itemDTOs.add(itemDTO);
                    }
                }
                hashMap.put(boqItem.getItemNumber(), itemDTO);
            }
        }
        return itemDTOs;
    }

    public Page<ItemDTO> searchStoreMaterials(Integer projectId, Integer storeId, Pageable pageable, MaterialCriteria criteria) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<ISMaterialItem> materialItems = new ArrayList<>();
        Page<ISMaterialItem> materialItemPage = null;
        int size = 0;
        if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().equals("null")) {
            Predicate predicate = materialPredicateBuilder.build(criteria, QISMaterialItem.iSMaterialItem);
            materialItemPage = materialItemRepository.findAll(predicate, pageable);
            materialItems = materialItemRepository.findAll(predicate, pageable).getContent();
            Long itemsSize = materialItemPage.getTotalElements();
            size = itemsSize.intValue();
        } else {
            materialItems = materialItemRepository.findAll(pageable).getContent();
            size = materialItemRepository.findAll().size();
        }
        for (ISMaterialItem materialItem : materialItems) {
            Double quantity = 0.0;
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(materialItem.getId());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setUnits(materialItem.getUnits());
            itemDTO.setDescription(materialItem.getDescription());
            itemDTO.setMaterialType(materialItem.getItemType());
            itemDTO.setStoreInventory(quantity);
            itemDTO.setItemIssueQuantity(topStockIssuedRepository.getTotalIssuedQuantityByProjectAndItemAndStore(projectId, materialItem.getId(), storeRepository.findOne(storeId)));
            itemDTO.setItemReturnQuantity(stockReturnItemRepository.getTotalReturnQuantityByProjectAndItemAndStore(projectId, materialItem.getId(), storeRepository.findOne(storeId)));
            itemDTOList.add(itemDTO);
        }
        int pages = 0;
        if (size % pageable.getPageSize() == 0) {
            pages = size / pageable.getPageSize();
        } else {
            pages = size / pageable.getPageSize() + 1;
        }
        if (itemDTOList.size() > 0) {
            itemDTOList.get(0).setTotalPages(pages);
            itemDTOList.get(0).setTotalElements(size);
        }
        return new PageImpl<ItemDTO>(itemDTOList, pageable, size);
    }

    @Transactional(readOnly = true)
    public List<ISProjectPerson> getProjectPersonsByFilters(Integer projectId, ProjectPersonCriteria projectPersonCriteria) {
        List<ISProjectPerson> projectPersons = new ArrayList<>();
        List<ISProjectPerson> isProjectPersons = new ArrayList<>();
        Predicate predicate = projectPersonPredicateBuilder.build(projectPersonCriteria, QISProjectPerson.iSProjectPerson);
        Iterator<ISProjectPerson> itr = projectPersonRepository.findByProject(predicate, projectId).iterator();
        while (itr.hasNext()) {
            projectPersons.add(itr.next());
        }
        for (ISProjectPerson isProjectPerson : projectPersons) {
            isProjectPersons.add(isProjectPerson);

        }
        return isProjectPersons;
    }

    @Transactional(readOnly = true)
    public List<Person> getExistingPersons(Integer itemType) {
        List<Person> persons = new ArrayList<>();
        Hashtable<Integer, Person> hashtable = new Hashtable<>();
        List<ISManpowerItem> manpowerItems = manpowerItemRepository.findByItemTypeId(itemType);
        List<Person> personList = personRepository.findAll();
        for (ISManpowerItem manpowerItem : manpowerItems) {
            for (Person person : personList) {
                if (person.getId() == manpowerItem.getPerson().getId()) {
                    hashtable.put(person.getId(), person);
                }
            }
        }
        for (Person person : personList) {
            if (hashtable.get(person.getId()) == null) {
                persons.add(person);
            }
        }
        return persons;
    }

    @Transactional(readOnly = true)
    public List<ISManpowerItem> getNonManpowerPersons(Integer itemType) {
        List<ISManpowerItem> manpowerItems = manpowerItemRepository.findByItemTypeId(itemType);
        return manpowerItems;
    }

    @Transactional(readOnly = false)
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional(readOnly = true)
    public void getPersonImage(Integer personId, HttpServletResponse response) {
        Person person = personRepository.findOne(personId);
        if (person != null && person.getImage() != null) {
            InputStream is = new ByteArrayInputStream(person.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Page<ItemDTO> getMaterialItemsDTO(Integer storeId, Pageable pageable, MaterialCriteria criteria) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        List<ISMaterialItem> materialItems = new ArrayList<>();
        Page<ISMaterialItem> materialItemPage = null;
        int size = 0;
        if (criteria.getSearchQuery() != null && !criteria.getSearchQuery().equals("null")) {
            Predicate predicate = materialPredicateBuilder.build(criteria, QISMaterialItem.iSMaterialItem);
            materialItemPage = materialItemRepository.findAll(predicate, pageable);
            materialItems = materialItemRepository.findAll(predicate, pageable).getContent();
            Long itemsSize = materialItemPage.getTotalElements();
            size = itemsSize.intValue();
        } else {
            materialItems = materialItemRepository.findAll(pageable).getContent();
            size = materialItemRepository.findAll().size();
        }
        for (ISMaterialItem materialItem : materialItems) {
            Double quantity = 0.0;
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(materialItem.getId());
            itemDTO.setItemName(materialItem.getItemName());
            itemDTO.setItemNumber(materialItem.getItemNumber());
            itemDTO.setUnits(materialItem.getUnits());
            itemDTO.setMaterialType(materialItem.getItemType());
            quantity = topInventoryRepository.getItemInventory(storeId, materialItem.getId());
            itemDTO.setStoreInventory(quantity);
            itemDTOList.add(itemDTO);
        }
        int pages = 0;
        if (size % pageable.getPageSize() == 0) {
            pages = size / pageable.getPageSize();
        } else {
            pages = size / pageable.getPageSize() + 1;
        }
        if (itemDTOList.size() > 0) {
            itemDTOList.get(0).setTotalPages(pages);
            itemDTOList.get(0).setTotalElements(size);
        }
        return new PageImpl<ItemDTO>(itemDTOList, pageable, size);
    }
}
