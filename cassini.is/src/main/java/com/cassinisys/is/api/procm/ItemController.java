package com.cassinisys.is.api.procm;
/**
 * The Class is for ItemController
 **/

import com.cassinisys.is.filtering.MachineCriteria;
import com.cassinisys.is.filtering.ManpowerCriteria;
import com.cassinisys.is.filtering.MaterialCriteria;
import com.cassinisys.is.filtering.ProjectPersonCriteria;
import com.cassinisys.is.model.pm.ISProjectPerson;
import com.cassinisys.is.model.procm.*;
import com.cassinisys.is.model.store.ISStockIssue;
import com.cassinisys.is.model.store.ISStockReceive;
import com.cassinisys.is.service.procm.ItemDTO;
import com.cassinisys.is.service.procm.ItemService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("is/items")
@Api(name = "Items", description = "Items endpoint", group = "IS")
public class ItemController extends BaseController {

    @Autowired
    ItemService itemService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PersonService personService;

    /**
     * The method used for creating the ISMaterialItem
     **/
    @RequestMapping(value = "/material", method = RequestMethod.POST)
    public ISMaterialItem create(@RequestBody @Valid ISMaterialItem item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/machine", method = RequestMethod.POST)
    public ISMachineItem create(@RequestBody @Valid ISMachineItem item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/manpower", method = RequestMethod.POST)
    public ISManpowerItem create(@RequestBody @Valid ISManpowerItem item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/manpower/multiple/items", method = RequestMethod.POST)
    public List<ISManpowerItem> createMultipleManpower(@RequestBody List<ISManpowerItem> items) {
        return itemService.createMultipleManpower(items);
    }

    /**
     * The method used to getall the values of ISMaterialItem
     **/
    @RequestMapping(value = "/allMaterials", method = RequestMethod.GET)
    public Page<ISMaterialItem> getAllMaterial(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findAll(pageable);
    }

    @RequestMapping(value = "/allMachines", method = RequestMethod.GET)
    public Page<ISMachineItem> getAllMachines(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findAllMachine(pageable);
    }

    @RequestMapping(value = "/allManpower", method = RequestMethod.GET)
    public Page<ISManpowerItem> getAllManpower(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findAllManpower(pageable);
    }

    @RequestMapping(value = "/allActiveManpower", method = RequestMethod.GET)
    public List<ISManpowerItem> getAllActiveManpower() {
        return itemService.findAllActiveManpower();
    }

    /**
     * methods used for getAll material, machine & manpower items by type
     **/
    @RequestMapping(value = "/allMaterials/type/{typeId}", method = RequestMethod.GET)
    public Page<ISMaterialItem> getMaterialItemsByType(@PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findMaterialsByType(typeId, pageable);
    }

    @RequestMapping(value = "/allMachines/type/{typeId}", method = RequestMethod.GET)
    public Page<ISMachineItem> getMachineItemsByType(@PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findMachinesByType(typeId, pageable);
    }

    @RequestMapping(value = "/allManpower/type/{typeId}", method = RequestMethod.GET)
    public Page<ISManpowerItem> getManpowerItemsByType(@PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.findManpowerByType(typeId, pageable);
    }

    @RequestMapping(value = "/receives/type/{typeId}", method = RequestMethod.GET)
    public Page<ISStockReceive> getMaterialReceivesByType(@PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getMaterialReceivesByType(typeId, pageable);
    }

    @RequestMapping(value = "/issues/type/{typeId}", method = RequestMethod.GET)
    public Page<ISStockIssue> getMaterialIssuessByType(@PathVariable("typeId") Integer typeId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getMaterialIssuessByType(typeId, pageable);
    }

    @RequestMapping(value = "/allMaterialTypesByName/{itemName}", method = RequestMethod.GET)
    public ISMaterialType getMaterialTypesByName(@PathVariable("itemName") String itemName) {
        return itemService.findMaterialsByName(itemName);
    }

    @RequestMapping(value = "/materialTypeByItemNumber/{itemNumber}", method = RequestMethod.GET)
    public ISMaterialType getMaterialTypeByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return itemService.getMaterialTypeByItemNumber(itemNumber);
    }

    @RequestMapping(value = "/allMachineTypesByName/{itemName}", method = RequestMethod.GET)
    public ISMachineType getMachineTypesByName(@PathVariable("itemName") String itemName) {
        return itemService.findMachinesByName(itemName);
    }

    @RequestMapping(value = "/machineTypeByItemNumber/{itemNumber}", method = RequestMethod.GET)
    public ISMachineType getMachineTypeByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return itemService.getMachineTypeByItemNumber(itemNumber);
    }

    @RequestMapping(value = "/allManpowerTypesByName/{itemName}", method = RequestMethod.GET)
    public List<ISManpowerType> getManpowerTypesByName(@PathVariable("itemName") String itemName) {
        return itemService.findManpowersByName(itemName);
    }

    /**
     * The method used to search the values of ISMaterialItem
     **/
    @RequestMapping(value = "/material/number/{itemNumber}", method = RequestMethod.GET)
    public ISMaterialItem searchMaterialItem(@PathVariable("itemNumber") String itemNumber) {
        ISMaterialItem isMaterialItems = itemService.MaterialfindByItemNumber(itemNumber);
        return isMaterialItems;
    }

    @RequestMapping(value = "/machine/number/{itemNumber}", method = RequestMethod.GET)
    public ISMachineItem searchMachineItem(@PathVariable("itemNumber") String itemNumber) {
        ISMachineItem isMachineItems = itemService.MachinesfindByItemNumber(itemNumber);
        return isMachineItems;
    }

    @RequestMapping(value = "/receiveType/number/{itemNumber}", method = RequestMethod.GET)
    public ISStockReceive searchReceiveTypeItem(@PathVariable("itemNumber") String itemNumber) {
        ISStockReceive stockReceive = itemService.findStockReceiveByNumber(itemNumber);
        return stockReceive;
    }

    @RequestMapping(value = "/issueType/number/{itemNumber}", method = RequestMethod.GET)
    public ISStockIssue searchIssueTypeItem(@PathVariable("itemNumber") String itemNumber) {
        ISStockIssue stockIssue = itemService.findStockIssueByNumber(itemNumber);
        return stockIssue;
    }

    /**
     * The method used to getItemById  of ISMaterialItem
     **/
    @RequestMapping(value = "/materialItem/{id}", method = RequestMethod.GET)
    public ISMaterialItem getMaterialItemById(@PathVariable("id") Integer id) {
        return itemService.get(id);
    }

    @RequestMapping(value = "/machineItem/{id}", method = RequestMethod.GET)
    public ISMachineItem getMachineItemById(@PathVariable("id") Integer id) {
        return itemService.getMachine(id);
    }

    @RequestMapping(value = "/manpowerItem/{id}", method = RequestMethod.GET)
    public ISManpowerItem getManpowerItemById(@PathVariable("id") Integer id) {
        return itemService.getManpower(id);
    }

    /**
     * The method used for updating the ISMaterialItem
     **/
    @RequestMapping(value = "/updateMaterial/{id}", method = RequestMethod.PUT)
    public ISMaterialItem update(@PathVariable("id") Integer id,
                                 @RequestBody ISMaterialItem item) {
        item.setId(id);
        return itemService.update(id, item);
    }

    @RequestMapping(value = "/updateMachine/{id}", method = RequestMethod.PUT)
    public ISMachineItem updateMachineItem(@PathVariable("id") Integer id,
                                           @RequestBody ISMachineItem item) {
        item.setId(id);
        return itemService.updateMachineitem(item);
    }

    @RequestMapping(value = "/updateManpower/{id}", method = RequestMethod.PUT)
    public ISManpowerItem updateManpowerItem(@PathVariable("id") Integer id,
                                             @RequestBody ISManpowerItem item) {
        return itemService.updateManpowerItem(item);
    }

    /**
     * The method used for deleting the ISMaterialItem
     **/
    @RequestMapping(value = "/material/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        itemService.delete(id);
    }

    @RequestMapping(value = "/machine/{id}", method = RequestMethod.DELETE)
    public void deleteMachine(@PathVariable("id") Integer id) {
        itemService.deleteMachine(id);
    }

    @RequestMapping(value = "/manPower/{id}", method = RequestMethod.DELETE)
    public void deleteManPower(@PathVariable("id") Integer id) {
        itemService.deleteManpower(id);
    }

    /**
     * The method used to getItemAttributes of list of ISMaterialItemAttribute
     **/
    @RequestMapping(value = "/{id}/material/attributes", method = RequestMethod.GET)
    public List<ISMaterialItemAttribute> getMaterialItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getMaterialItemAttributes(id);
    }

    @RequestMapping(value = "/{id}/machine/attributes", method = RequestMethod.GET)
    public List<ISMachineItemAttribute> getMachineItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getMachineItemAttributes(id);
    }

    @RequestMapping(value = "/{id}/manpower/attributes", method = RequestMethod.GET)
    public List<ISManpowerItemAttribute> getManpowerItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getManpowerItemAttributes(id);
    }

    @RequestMapping(value = "/{id}/receiveType/attributes", method = RequestMethod.GET)
    public List<ISReceiveTypeItemAttribute> getReceiveTypeItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getReceiveTypeItemAttributes(id);
    }

    @RequestMapping(value = "/{id}/issueType/attributes", method = RequestMethod.GET)
    public List<ISIssueTypeItemAttribute> getIssueTypeItemAttributes(@PathVariable("id") Integer id) {
        return itemService.getIssueTypeItemAttributes(id);
    }

    /**
     * The method used to getmultiple values through of list of ISMaterialItem
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISMaterialItem> getMultiple(@PathVariable Integer[] ids) {
        return itemService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/machine/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISMachineItem> getMultipleItem(@PathVariable Integer[] ids) {
        return itemService.findMultipleItem(Arrays.asList(ids));
    }

    @RequestMapping(value = "/materials/itemNumbers/[{itemNumbers}]", method = RequestMethod.GET)
    public List<ISMaterialItem> getMaterialsByItemNumbers(@PathVariable String[] itemNumbers) {
        return itemService.getMaterialsByItemNumbers(Arrays.asList(itemNumbers));
    }

    @RequestMapping(value = "/machines/itemNumbers/[{itemNumbers}]", method = RequestMethod.GET)
    public List<ISMachineItem> getMachinesByItemNumbers(@PathVariable String[] itemNumbers) {
        return itemService.getMachinesByItemNumbers(Arrays.asList(itemNumbers));
    }

    /**
     * The method used to saveItemAttributes with the   list of ISMaterialItemAttribute
     **/
    @RequestMapping(value = "/material/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveMaterialItemAttributes(@PathVariable("id") Integer id,
                                                            @RequestBody List<ISMaterialItemAttribute> attributes) {
        return itemService.saveMaterialItemAttributes(attributes);
    }

    @RequestMapping(value = "/receiveType/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveReceiveTypeItemAttributes(@PathVariable("id") Integer id,
                                                               @RequestBody List<ISReceiveTypeItemAttribute> attributes) {
        return itemService.saveReceiveTypeItemAttributes(attributes);
    }

    @RequestMapping(value = "/manpower/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveManpowerAttributes(@PathVariable("id") Integer id,
                                                        @RequestBody List<ISManpowerItemAttribute> attributes) {
        return itemService.saveManpowerItemAttributes(attributes);
    }

    @RequestMapping(value = "/machine/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveMachineItemAttributes(@PathVariable("id") Integer id,
                                                           @RequestBody List<ISMachineItemAttribute> attributes) {
        return itemService.saveMachineItemAttributes(attributes);
    }

    @RequestMapping(value = "/issueType/{id}/attributes", method = RequestMethod.POST)
    public List<ObjectAttribute> saveIssueTypeItemAttributes(@PathVariable("id") Integer id,
                                                             @RequestBody List<ISIssueTypeItemAttribute> attributes) {
        return itemService.saveIssueTypeItemAttributes(attributes);
    }

    /**
     * The method used to updateItemAttributes with the   list of ISMaterialItemAttribute
     **/
    @RequestMapping(value = "/material/{id}/attributes", method = RequestMethod.PUT)
    public ISMaterialItemAttribute updateMaterialItemAttributes(@PathVariable("id") Integer id,
                                                                @RequestBody ISMaterialItemAttribute attribute) {
        return itemService.updateMaterialItemAttributes(attribute);
    }

    @RequestMapping(value = "/machine/{id}/attributes", method = RequestMethod.PUT)
    public ISMachineItemAttribute updateMachineItemAttributes(@PathVariable("id") Integer id,
                                                              @RequestBody ISMachineItemAttribute attribute) {
        return itemService.updateMachineItemAttributes(attribute);
    }

    @RequestMapping(value = "/manpower/{id}/attributes", method = RequestMethod.PUT)
    public ISManpowerItemAttribute updateManpowerAttributes(@PathVariable("id") Integer id,
                                                            @RequestBody ISManpowerItemAttribute attribute) {
        return itemService.updateManpowerAttributes(attribute);
    }

    @RequestMapping(value = "/receiveType/{id}/attributes", method = RequestMethod.PUT)
    public ISReceiveTypeItemAttribute updateReceiveTypeItemAttributes(@PathVariable("id") Integer id,
                                                                      @RequestBody ISReceiveTypeItemAttribute attribute) {
        return itemService.updateReceiveTypeItemAttributes(attribute);
    }

    @RequestMapping(value = "/issueType/{id}/attributes", method = RequestMethod.PUT)
    public ISIssueTypeItemAttribute updateIssueTypeItemAttributes(@PathVariable("id") Integer id,
                                                                  @RequestBody ISIssueTypeItemAttribute attribute) {
        return itemService.updateIssueTypeItemAttributes(attribute);
    }

    /**
     * The method used for freeTextSearch of ISMaterialItem
     **/

    @RequestMapping(value = "/material/freesearch", method = RequestMethod.POST)
    public Page<ISMaterialItem> materialFreeTextSearch(PageRequest pageRequest, @RequestBody MaterialCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISMaterialItem> items = itemService.materialFreeTextSearch(pageable, criteria);
        return items;
    }

    @RequestMapping(value = "/machine/freesearch", method = RequestMethod.POST)
    public Page<ISMachineItem> machineFreeTextSearch(PageRequest pageRequest, @RequestBody MachineCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISMachineItem> items = itemService.machineFreeTextSearch(pageable, criteria);
        return items;
    }

    @RequestMapping(value = "/manpower/freesearch", method = RequestMethod.POST)
    public Page<ISManpowerItem> manpowerFreeTextSearch(PageRequest pageRequest, @RequestBody ManpowerCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ISManpowerItem> items = itemService.manpowerFreeTextSearch(pageable, criteria);
        return items;
    }

    /**
     * The method used for get the ObjectTypeAttribute by objectType
     **/
    @RequestMapping(value = "/requiredFalseAttributes", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getMaterialAttributesRequiredFalse(@RequestParam("objectType") String objectType) {
        return itemService.getMaterialAttributesRequiredFalse(objectType);
    }

    @RequestMapping(value = "/requiredTrueAttributes", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getMaterialAttributesRequiredTrue(@RequestParam("objectType") String objectType) {
        return itemService.getMaterialAttributesRequiredTrue(objectType);
    }

    @RequestMapping(value = "/objects/objectAttributes", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByIndentAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] objectIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return itemService.getObjectAttributesByObjectIdsAndAttributeIds(objectIds, attIds);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAllItemTypeAttributes(@PathVariable("objectType") String objectType) {
        return itemService.getItemTypeAttributes(objectType);
    }

    @RequestMapping(value = "/machine/item/{autoNum}", method = RequestMethod.GET)
    public void findItemByAutoNumId(@PathVariable("autoNum") Integer autoNum) {
        itemService.findItemByAutoNumId(autoNum);
    }

    @RequestMapping(value = "/autoNumbers/multiple/[{autoNumberIds}]", method = RequestMethod.GET)
    public List<AutoNumber> findItemByAutoNumId(@PathVariable Integer[] autoNumberIds) {
        return itemService.findAutoNumberItems(Arrays.asList(autoNumberIds));
    }

    @RequestMapping(value = "/machine/attributeId/{attributeId}", method = RequestMethod.GET)
    public List<ISMachineItemAttribute> findMachineItemsByAttribute(@PathVariable("attributeId") Integer attributeId) {
        return itemService.findMachineItemsByAttribute(attributeId);
    }

    @RequestMapping(value = "/material/attributeId/{attributeId}", method = RequestMethod.GET)
    public List<ISMaterialItemAttribute> findMaterialItemsByAttribute(@PathVariable("attributeId") Integer attributeId) {
        return itemService.findMaterialItemsByAttribute(attributeId);
    }

    @RequestMapping(value = "/manpower/attributeId/{attributeId}", method = RequestMethod.GET)
    public List<ISManpowerItemAttribute> findManpowerItemsByAttribute(@PathVariable("attributeId") Integer attributeId) {
        return itemService.findManpowerItemsByAttribute(attributeId);
    }

    @RequestMapping(value = "/receiveType/attributeId/{attributeId}", method = RequestMethod.GET)
    public List<ISReceiveTypeItemAttribute> findReceiveTypeItemsByAttribute(@PathVariable("attributeId") Integer attributeId) {
        return itemService.findReceiveTypeItemsByAttribute(attributeId);
    }

    @RequestMapping(value = "/issueType/attributeId/{attributeId}", method = RequestMethod.GET)
    public List<ISIssueTypeItemAttribute> findIssueTypeItemsByAttribute(@PathVariable("attributeId") Integer attributeId) {
        return itemService.findIssueTypeItemsByAttribute(attributeId);
    }

    @RequestMapping(value = "/findByMaterialItemNumber/{itemNumber}", method = RequestMethod.GET)
    public ISMaterialItem getMaterialByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return itemService.getMaterialByItemNumber(itemNumber);
    }

    @RequestMapping(value = "/objectAttributes/material", method = RequestMethod.POST)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByItemAndAttributeId(@RequestBody List<Integer[]> ids) {
        Integer[] itemsIds = ids.get(0);
        Integer[] attIds = ids.get(1);
        return itemService.getObjectAttributesByItemIdsAndAttributeIds(itemsIds, attIds);
    }

    @RequestMapping(value = "/boq/boqItem/{itemNumber}", method = RequestMethod.GET)
    public List<ISBoqItem> getBoqItemByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        return itemService.getBoqItemByItemNumber(itemNumber);
    }

    @RequestMapping(value = "/materials/boq/{boqId}", method = RequestMethod.GET)
    public ISMaterialItem findMaterialItemByBoq(@PathVariable("boqId") Integer boqId) {
        return itemService.findMaterialItemByBoq(boqId);
    }

    @RequestMapping(value = "/machines/boq/{boqId}", method = RequestMethod.GET)
    public ISMachineItem findMachineItemByBoq(@PathVariable("boqId") Integer boqId) {
        return itemService.findMachineItemByBoq(boqId);
    }

    @RequestMapping(value = "/task/{taskId}/multiple/boq/{boqIds}", method = RequestMethod.GET)
    public List<ItemDTO> getItemsByBoqIds(@PathVariable("taskId") Integer taskId, @PathVariable("boqIds") List<Integer> boqIds) {
        return itemService.getItemsByBoqIds(taskId, boqIds);
    }

    @RequestMapping(value = "/getAllMaterials", method = RequestMethod.GET)
    public List<ISMaterialItem> getAllMaterials() {
        return itemService.getAllMaterials();
    }

    @RequestMapping(value = "/itemsDTO", method = RequestMethod.GET)
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @RequestMapping(value = "/project/{projectId}/itemsDTO", method = RequestMethod.GET)
    public List<ItemDTO> getProjectItems(@PathVariable("projectId") Integer projectId) {
        return itemService.getProjectItems(projectId);
    }

    @RequestMapping(value = "/project/{projectId}/materials/pageable", method = RequestMethod.GET)
    public Page<ISMaterialItem> getProjectMaterials(@PathVariable("projectId") Integer projectId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.getProjectMaterials(projectId, pageable);
    }

    @RequestMapping(value = "/project/{projectId}/store/{storeId}/materials/freeSearch", method = RequestMethod.GET)
    public Page<ItemDTO> searchStoreMaterials(@PathVariable("projectId") Integer projectId, @PathVariable("storeId") Integer storeId, PageRequest pageRequest, MaterialCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemService.searchStoreMaterials(projectId, storeId, pageable, criteria);
    }

    @RequestMapping(value = "/{projectId}/filters", method = RequestMethod.GET)
    public List<ISProjectPerson> getProjectPersonsByFilters(@PathVariable("projectId") Integer projectId, ProjectPersonCriteria projectPersonCriteria) {
        return itemService.getProjectPersonsByFilters(projectId, projectPersonCriteria);
    }

    //login persons who were not added into manpower items for the selected type
    @RequestMapping(value = "/itemType/{itemType}/nonManpowerpersons", method = RequestMethod.GET)
    public List<Person> getExistingPersons(@PathVariable("itemType") Integer itemType) {
        return itemService.getExistingPersons(itemType);
    }

    //List of manpowerItems
    @RequestMapping(value = "/itemType/{itemType}/manpowerItems", method = RequestMethod.GET)
    public List<ISManpowerItem> getNonManpowerPersons(@PathVariable("itemType") Integer itemType) {
        return itemService.getNonManpowerPersons(itemType);
    }

    @RequestMapping(value = "/{personId}/uploadImage", method = RequestMethod.POST)
    public Person uploadPersonImage(@PathVariable("personId") Integer personId,
                                    MultipartHttpServletRequest request) {
        Person person = personService.get(personId);
        if (person != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                person.setImage(null);
                MultipartFile file = files.get(0);
                try {
                    person.setImage(file.getBytes());
                    person = itemService.updatePerson(person);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return person;
    }

    @RequestMapping(value = "/{personId}/personImage/download", method = RequestMethod.GET)
    public void downloadPersonImage(@PathVariable("personId") Integer personId,
                                    HttpServletResponse response) {
        itemService.getPersonImage(personId, response);
    }

    @RequestMapping(value = "/materials/store/{storeId}", method = RequestMethod.GET)
    public Page<ItemDTO> getMaterialItemsDTO(@PathVariable("storeId") Integer storeId, PageRequest pageRequest, MaterialCriteria criteria) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<ItemDTO> items = itemService.getMaterialItemsDTO(storeId, pageable, criteria);
        return items;
    }

}
