package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.SharedObjectCriteria;
import com.cassinisys.plm.model.dto.*;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.service.plm.SharedObjectService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GSR on 20-10-2017.
 */
@RestController
@RequestMapping("/plm/sharedObjects")
@Api(tags = "PLM.ITEMS", description = "Items Related")
public class SharedObjectController extends BaseController {

    @Autowired
    private SharedObjectService sharedObjectService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMSharedObject createSharedObject(@RequestBody PLMSharedObject sharedObject) {
        return sharedObjectService.create(sharedObject);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public PLMSharedObject updateSharedObject(@RequestBody PLMSharedObject sharedObject) {
        return sharedObjectService.update(sharedObject);
    }

    @RequestMapping(value = "/{sharedObjectId}", method = RequestMethod.DELETE)
    public void deleteSharedObject(@PathVariable("sharedObjectId") Integer sharedObjectId) {
        sharedObjectService.delete(sharedObjectId);
    }

    @RequestMapping(value = "/{sharedObjectId}", method = RequestMethod.GET)
    public PLMSharedObject getSharedObject(@PathVariable("sharedObjectId") Integer sharedObjectId) {
        return sharedObjectService.get(sharedObjectId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMSharedObject> getSharedObjects() {
        return sharedObjectService.getAll();
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleSharedObjects(PLMSharedObject sharedObject,
                                                           @RequestBody List<PLMItem> plmItems) {
        return sharedObjectService.saveMultipleSharedObjects(plmItems, sharedObject);
    }

    @RequestMapping(value = "/{sharedObjectId}/{personId}", method = RequestMethod.GET)
    public List<PLMSharedObject> getSharedObjectsByPersonAndItem(@PathVariable("sharedObjectId") Integer sharedObjectId,
                                                                 @PathVariable("personId") Integer personId) {
        return sharedObjectService.getSharedObjectsByPersonAndItem(sharedObjectId, personId);
    }

    @RequestMapping(value = "/getSharedObjects", method = RequestMethod.POST)
    public List<PLMSharedObject> getSharedObjects(@RequestBody List<Integer[]> ids) {
        Integer[] objectIds = ids.get(0);
        Integer[] personIds = ids.get(1);
        return sharedObjectService.getSharedObjectsByItemsAndPerson(objectIds, personIds);
    }

    @RequestMapping(value = "/byItem/{objectId}", method = RequestMethod.GET)
    public List<PLMSharedObject> getSharedObjectByItem(@PathVariable("objectId") Integer objectId) {
        return sharedObjectService.getSharedObjectByItem(objectId);
    }

    @RequestMapping(value = "/items/person", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getSharedItemsByPerson(PageRequest pageRequest,
                                                            SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedItemsBySharedTo(objectCriteria, pageable);
    }

    @RequestMapping(value = "/projects/person/[{personIds}]", method = RequestMethod.GET)
    public List<PLMSharedObject> getSharedProjectsByPerson(@PathVariable("personIds") Integer[] personIds) {
        return sharedObjectService.getSharedProjectsBySharedTo(Arrays.asList(personIds));
    }

    @RequestMapping(value = "/programs/person/[{personIds}]", method = RequestMethod.GET)
    public List<PLMSharedObject> getSharedProgramsByPerson(@PathVariable("personIds") Integer[] personIds) {
        return sharedObjectService.getSharedProgramsBySharedTo(Arrays.asList(personIds));
    }

    @RequestMapping(value = "/items/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getSharedItemsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                  PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedItemsBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/items/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getItemsBySharedToPerson(@PathVariable("sharedTo") Integer sharedTo,
                                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getItemsBySharedToPerson(sharedTo, pageable);
    }

    @RequestMapping(value = "/items/externalGroup/{group}", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getItemsByExternalGroup(@PathVariable("group") Integer group,
                                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getItemsByExternalGroup(group, pageable);
    }

    @RequestMapping(value = "/items/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getSharedItemsBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedItemsBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getAllSharedObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.findAllSharedObjects(pageable);
    }

    @RequestMapping(value = "/items/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedItemObjectDto> getSharedItemsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                  @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedItemsBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/multiple/mfrs", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleMfrSharedObjects(PLMSharedObject sharedObject,
                                                              @RequestBody List<PLMManufacturer> manufacturers) {
        return sharedObjectService.saveMultipleMfrSharedObjects(manufacturers, sharedObject);
    }

    @RequestMapping(value = "/mfrs/person", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrsByPerson(PageRequest pageRequest,
                                                              SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrsByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/mfrs", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedMfrObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedMfrObjects(pageable);
    }

    @RequestMapping(value = "/programs", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedProgramsObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedProgramsObjects(pageable);
    }

    @RequestMapping(value = "/multiple/mfr/parts", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleMfrPartsSharedObjects(PLMSharedObject sharedObject,
                                                                   @RequestBody List<PLMManufacturerPart> parts) {
        return sharedObjectService.saveMultipleMfrPartsSharedObjects(parts, sharedObject);
    }

    @RequestMapping(value = "/multiple/suppliers", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleSuppliersSharedObjects(PLMSharedObject sharedObject,
                                                                    @RequestBody List<PLMSupplier> parts) {
        return sharedObjectService.saveMultipleSuppliersSharedObjects(parts, sharedObject);
    }


    @RequestMapping(value = "/multiple/customobjects", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleCustomObjectsSharedObjects(PLMSharedObject sharedObject,
                                                                        @RequestBody List<CustomObject> customObjects) {
        return sharedObjectService.saveMultipleCustomObjectsSharedObjects(customObjects, sharedObject);
    }

    @RequestMapping(value = "/multiple/folders", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleFoldersSharedObjects(PLMSharedObject sharedObject,
                                                                  @RequestBody List<PLMDocument> parts) {
        return sharedObjectService.saveMultipleFoldersSharedObjects(parts, sharedObject);
    }

    @RequestMapping(value = "/mfr/parts/person", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrPartsByPerson(PageRequest pageRequest,
                                                                  SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrPartsByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/suppliers/person", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedSuppliersByPerson(PageRequest pageRequest,
                                                                   SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedSuppliersByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/customobjects/person", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsByPerson(PageRequest pageRequest,
                                                                       SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedCustomObjectsByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/folders/person", method = RequestMethod.GET)
    public Page<SharedFolderObjectDto> getSharedFoldersByPerson(PageRequest pageRequest,
                                                                SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedFoldersByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/mfr/parts", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedMfrPartObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedMfrPartObjects(pageable);
    }

    @RequestMapping(value = "/mfrs/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                    PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrsBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/mfrs/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                    @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrsBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/mfr/parts/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrPartsBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/mfr/parts/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getMfrPartsBySharedTo(@PathVariable("sharedTo") Integer sharedTo,
                                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getMfrPartsBySharedTo(sharedTo, pageable);
    }

    @RequestMapping(value = "/mfr/parts/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                        @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrPartsBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/mfr/parts/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrPartsBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                          PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrPartsBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    /*
     * Suppliers search
     */

    @RequestMapping(value = "/suppliers/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedSuppliersBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                         PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedSuppliersBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/suppliers/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSuppliersBySharedTo(@PathVariable("sharedTo") Integer sharedTo,
                                                               PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSuppliersBySharedTo(sharedTo, pageable);
    }

    @RequestMapping(value = "/suppliers/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedSuppliersBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                         @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedSuppliersBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/suppliers/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedSuppliersBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                           PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSuppliersBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/suppliers", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedSupplierObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedSupplierObjects(pageable);
    }




     /*
     * CustomObject search
     */

    @RequestMapping(value = "/customobjects/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedCustomObjectsBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/customobjects/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getCustomObjectsBySharedTo(@PathVariable("sharedTo") Integer sharedTo,
                                                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getCustomObjectsBySharedTo(sharedTo, pageable);
    }

    @RequestMapping(value = "/customobjects/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                             @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedCustomObjectsBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/customobjects/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedCustomObjectsBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                               PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getCustomObjectsBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/customobjects", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedCustomObjectsObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedCustomObjects(pageable);
    }

    /*
     * Folders search
     */

    @RequestMapping(value = "/folders/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedFoldersBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                       PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedFoldersBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/folders/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getFoldersBySharedTo(@PathVariable("sharedTo") Integer sharedTo,
                                                             PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getFoldersBySharedTo(sharedTo, pageable);
    }

    @RequestMapping(value = "/folders/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedFoldersBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                       @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedFoldersBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/folders/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedFoldersBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                         PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getFoldersBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/folders", method = RequestMethod.GET)
    public Page<SharedFolderObjectDto> getAllSharedFolderObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedFolderObjects(pageable);
    }

    @RequestMapping(value = "/mfrs/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedMfrsBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                      PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedMfrsBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/declarations/person", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedDeclarationsByPerson(PageRequest pageRequest,
                                                                      SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedDeclarationsByPerson(objectCriteria, pageable);
    }

    @RequestMapping(value = "/declarations", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getAllSharedDeclarationObjects(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedDeclarationObjects(pageable);
    }

    @RequestMapping(value = "/declarations/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedToPerson(@PathVariable("ids") Integer[] sharedTo,
                                                                              PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedDeclarationsBySharedToPerson(Arrays.asList(sharedTo), pageable);
    }

    @RequestMapping(value = "/declarations/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedDeclarationBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                           @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedDeclarationsBySharedByAndSharedToPerson(sharedBy, sharedTo, pageable);
    }

    @RequestMapping(value = "/declarations/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedPerson(@PathVariable("sharedBy") Integer sharedBy,
                                                                            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedDeclarationsBySharedPerson(sharedBy, pageable);
    }

    @RequestMapping(value = "/declarations/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedMfrPartObjectDto> getSharedDeclarationsBySharedTo(@PathVariable("sharedTo") Integer sharedTo,
                                                                        PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedDeclarationsBySharedTo(sharedTo, pageable);
    }

    @RequestMapping(value = "/project/project/person", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProjectsByPerson(PageRequest pageRequest,
                                                                  SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProjectsByPerson(objectCriteria, pageable, PLMObjectType.PROJECT);
    }

    @RequestMapping(value = "/program/person", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProgramsByPerson(PageRequest pageRequest,
                                                                  SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProgramsByPerson(objectCriteria, pageable, PLMObjectType.PROGRAM);
    }

    @RequestMapping(value = "/project/activity/person", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProjectActivitiesByPerson(PageRequest pageRequest,
                                                                           SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProjectsByPerson(objectCriteria, pageable, PLMObjectType.PROJECTACTIVITY);
    }

    @RequestMapping(value = "/project/task/person", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProjectTasksByPerson(PageRequest pageRequest,
                                                                      SharedObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProjectsByPerson(objectCriteria, pageable, PLMObjectType.PROJECTTASK);
    }

    @RequestMapping(value = "/multiple/project", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleProjectSharedObjects(PLMSharedObject sharedObject,
                                                                  @RequestBody List<SharedMultipleProjectObjectsDto> plmItems) {
        return sharedObjectService.saveMultipleProjectSharedObjects(plmItems, sharedObject);
    }

    @RequestMapping(value = "/multiple/program", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleProgramSharedObjects(PLMSharedObject sharedObject,
                                                                  @RequestBody List<SharedMultipleProjectObjectsDto> plmItems) {
        return sharedObjectService.saveMultipleProgramSharedObjects(plmItems, sharedObject);
    }

    @RequestMapping(value = "/objectId/{objectId}", method = RequestMethod.GET)
    public List<PLMSharedObject> getByObjectId(@PathVariable("objectId") Integer objectId) {
        return sharedObjectService.getByObjectId(objectId);
    }

    @RequestMapping(value = "/counts/{type}", method = RequestMethod.GET)
    public SharedObjectCounts getSharedCounts(@PathVariable("type") String type) {
        return sharedObjectService.getSharedCounts(type);
    }

    
    @RequestMapping(value = "/person/{personId}/counts", method = RequestMethod.GET)
    public PersonSharedObjectCounts getPersonSharedCounts(@PathVariable("personId") Integer personId) {
        return sharedObjectService.getPersonSharedCounts(personId);
    }

    @RequestMapping(value = "/project/{type}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getAllSharedProjectObjects(@PathVariable("type") String type,
                                                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedProjectObjects(pageable, type);
    }

    @RequestMapping(value = "/program/{type}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getAllSharedProgramObjects(@PathVariable("type") String type,
                                                                   PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getAllSharedProgramObjects(pageable, type);
    }

    @RequestMapping(value = "/project/{type}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getProjectObjectsBySharedTo(@PathVariable("type") String type,
                                                                    @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getProjectObjectsBySharedTo(sharedTo, pageable, type);
    }

    @RequestMapping(value = "/program/{type}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getProgramObjectsBySharedTo(@PathVariable("type") String type,
                                                                    @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getProgramObjectsBySharedTo(sharedTo, pageable, type);
    }

    @RequestMapping(value = "/project/{type}/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getProjectObjectsBySharedBy(@PathVariable("type") String type,
                                                                    @PathVariable("sharedBy") Integer sharedBy, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getProjectObjectsBySharedBy(sharedBy, pageable, type);
    }

    @RequestMapping(value = "/program/{type}/sharedBy/{sharedBy}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getProgramObjectsBySharedBy(@PathVariable("type") String type,
                                                                    @PathVariable("sharedBy") Integer sharedBy, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getProgramObjectsBySharedBy(sharedBy, pageable, type);
    }

    @RequestMapping(value = "/project/{type}/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProjectObjectBySharedPerson(@PathVariable("type") String type,
                                                                             @PathVariable("sharedBy") Integer sharedBy,
                                                                             @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProjectObjectBySharedPerson(sharedBy, sharedTo, pageable, type);
    }

    @RequestMapping(value = "/program/{type}/sharedBy/{sharedBy}/sharedTo/{sharedTo}", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProgramObjectBySharedPerson(@PathVariable("type") String type,
                                                                             @PathVariable("sharedBy") Integer sharedBy,
                                                                             @PathVariable("sharedTo") Integer sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProgramObjectBySharedPerson(sharedBy, sharedTo, pageable, type);
    }

    @RequestMapping(value = "/project/{type}/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProjectObjectssBySharedToPerson(@PathVariable("type") String type,
                                                                                 @PathVariable("ids") Integer[] sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProjectObjectssBySharedToPerson(Arrays.asList(sharedTo), pageable, type);
    }

    @RequestMapping(value = "/program/{type}/sharedTo/[{ids}]", method = RequestMethod.GET)
    public Page<SharedProjectObjectDto> getSharedProgramObjectssBySharedToPerson(@PathVariable("type") String type,
                                                                                 @PathVariable("ids") Integer[] sharedTo, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sharedObjectService.getSharedProgramObjectssBySharedToPerson(Arrays.asList(sharedTo), pageable, type);
    }


    @RequestMapping(value = "/folders/{folderId}/children", method = RequestMethod.GET)
    public List<SharedFolderObjectDto> getFolderChildren(@PathVariable("folderId") Integer folderId) {
        return sharedObjectService.getFolderChildren(folderId);
    }

    @RequestMapping(value = "/multiple/files/by/object", method = RequestMethod.POST)
    public List<PLMSharedObject> saveMultipleSharedFileObjects(PLMSharedObject sharedObject,
                                                               @RequestBody List<FileDto> plmFiles) {
        return sharedObjectService.saveMultipleSharedFileObjects(plmFiles, sharedObject);
    }

    /*
    * External user Shared folders and files by person for every object
    *
    * */

    @RequestMapping(value = "/shared/external/folders/by/person/{personId}", method = RequestMethod.GET)
    public List<FileDto> getExternalSharedFoldersByPerson(@PathVariable("personId") Integer personId) {
        return sharedObjectService.getExternalSharedFoldersByPerson(personId);
    }


    @RequestMapping(value = "/external/root/files/by/{personId}", method = RequestMethod.GET)
    public List<FileDto> getExternalSharedRootFilesByPerson(@PathVariable("personId") Integer personId) {
        return sharedObjectService.getExternalSharedRootFilesByPerson(personId);
    }

    @RequestMapping(value = "/external/folder/files/by/{personId}/{folderId}/{objectType}", method = RequestMethod.GET)
    public List<FileDto> getExternalSharedFolderFiles(@PathVariable("personId") Integer personId, @PathVariable("folderId") Integer folderId, @PathVariable("objectType") String objectType) {
        return sharedObjectService.getExternalSharedFolderFiles(folderId, personId,objectType);
    }

    @RequestMapping(value = "/sharedTo", method = RequestMethod.GET)
    public List<Person> getSharedToPersons() {
        return sharedObjectService.getSharedToPersons();
    }


}
