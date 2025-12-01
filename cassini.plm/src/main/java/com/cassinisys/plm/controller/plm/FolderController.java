package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.plm.PLMFolder;
import com.cassinisys.plm.model.plm.PLMFolderObject;
import com.cassinisys.plm.model.plm.dto.FolderDto;
import com.cassinisys.plm.service.plm.FolderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@RestController
@RequestMapping("/plm/folders")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class FolderController extends BaseController {

    @Autowired
    private FolderService folderService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMFolder creteFolder(@RequestBody PLMFolder PLMFolder) {
        return folderService.create(PLMFolder);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.PUT)
    public PLMFolder updateFolder(@PathVariable("folderId") Integer folderId, @RequestBody PLMFolder PLMFolder) {
        return folderService.update(PLMFolder);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("folderId") Integer folderId) {
        folderService.delete(folderId);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.GET)
    public PLMFolder getFolder(@PathVariable("folderId") Integer folderId) {
        return folderService.get(folderId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMFolder> getFolders() {
        return folderService.getRootFolders();
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PLMFolder> getChildren(@PathVariable("folderId") Integer folderId) {
        return folderService.getChildren(folderId);
    }
    //   api/pdm/folders/{fodlerId}/objects : get folder objects  GET
    //   api/folders/{folderId}/{objectId} : DELETE
    //   api/pdm/folders/objects  : POST : sends list of PDMFolderObjects

    @RequestMapping(value = "/{folderId}/objects", method = RequestMethod.GET)
    public List<PLMFolderObject> getFolderObjects(@PathVariable("folderId") Integer folderId) {
        return folderService.getFolderObjects(folderId);
    }

    @RequestMapping(value = "/{folderId}/objects/{objectId}", method = RequestMethod.DELETE)
    public void deleteFolderObject(@PathVariable("folderId") Integer folderId,
                                   @PathVariable("objectId") Integer objectId) {
        folderService.deleteFolderObject(objectId);
    }

    @RequestMapping(value = "/objects", method = RequestMethod.POST)
    public List<PLMFolderObject> createFolderObject(@RequestBody List<PLMFolderObject> folderObject,
                                                    @RequestParam("objectType") ObjectType objectType) {
        return folderService.createFolderObject(folderObject, objectType);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PLMFolder> getClassificationTree() {
        return folderService.getClassificationTree();
    }

    @RequestMapping(value = "/tree/{person}", method = RequestMethod.GET)
    public FolderDto getFoldersTree(@PathVariable("person") Integer person) {
        return folderService.getFoldersTree(person);
    }
}
