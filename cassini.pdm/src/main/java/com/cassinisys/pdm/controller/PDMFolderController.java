package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.filtering.FolderCriteria;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.PDMFolder;
import com.cassinisys.pdm.model.PDMFolderAttribute;
import com.cassinisys.pdm.model.PDMObjectPermission;
import com.cassinisys.pdm.service.PDMFileService;
import com.cassinisys.pdm.service.PDMFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "Folder", description = "Folder endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/vaults/{vaultId}/folders")
public class PDMFolderController extends BaseController {

    @Autowired
    private PDMFolderService folderService;

    @Autowired
    private PDMFileService fileService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PDMFolder create(@PathVariable("vaultId") Integer vaultId, @RequestBody PDMFolder pdmFolder) {
        pdmFolder.setId(null);
        pdmFolder.setVault(vaultId);
        return folderService.create(pdmFolder);
    }

    @RequestMapping(value = "/bypath", method = RequestMethod.POST)
    public PDMFolder createByPath(@PathVariable("vaultId") Integer vaultId,
                                  @RequestParam("path") String path,
                                  @RequestParam("separator") String separator) {
        return folderService.createByPath(vaultId, path, separator);
    }

    @RequestMapping(value = "/{folderId}/attribute", method = RequestMethod.POST)
    public PDMFolderAttribute create(@PathVariable("folderId") Integer folderId,
                                     @RequestBody PDMFolderAttribute folderAttribute) {
        folderAttribute.setId(null);
        return folderService.createAttribute(folderAttribute);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.PUT)
    private PDMFolder update(@PathVariable("folderId") Integer folderId,
                             @RequestBody PDMFolder pdmFolder) {
        pdmFolder.setId(folderId);
        return folderService.update(pdmFolder);
    }

    @RequestMapping(value = "/{folderId}/attribute", method = RequestMethod.PUT)
    private PDMFolderAttribute update(@PathVariable("folderId") ObjectAttributeId folderId,
                                      @RequestBody PDMFolderAttribute folderAttribute) {
        folderAttribute.setId(folderId);
        return folderService.updateAttribte(folderAttribute);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.DELETE)
    private void delete(@PathVariable("folderId") Integer folderId) {
        folderService.delete(folderId);
    }


    @RequestMapping(value = "/{folderId}", method = RequestMethod.GET)
    public PDMFolder get(@PathVariable("folderId") Integer folderId) {
        return folderService.get(folderId);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PDMFolder> getChildren(@PathVariable("folderId") Integer folderId) {
        return folderService.getChildren(folderId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PDMFolder> getAllForVault(@PathVariable("vaultId") Integer vaultId) {
        return folderService.getRootFolders(vaultId);
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<PDMFolder> getAll(@PathVariable("vaultId") Integer vaultId) {
        return folderService.getByVault(vaultId);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<PDMFolder> findAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return folderService.findAll(pageable);
    }


    /**
     * The method used to getFolderDocuments of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/files", method = RequestMethod.GET)
    public List<PDMFile> getFolderDocuments(
            @PathVariable("vaultId") Integer vaultId,
            @PathVariable("folderId") Integer folderId) {
        return folderService.getFiles(folderId);
    }

    /**
     * The method used to uploadDocuments of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/files",
            method = RequestMethod.POST)
    public List<PDMFile> uploadDocuments(
            @RequestParam("commit") Optional<Integer> commit,
            @PathVariable("vaultId") Integer vaultId,
            @PathVariable("folderId") Integer folderId,
            MultipartHttpServletRequest request) {
        Integer commitId = null;
        if(commit.isPresent()) {
            commitId = commit.get();
        }
        return fileService.uploadFiles(commitId, folderId,
                request.getFileMap());
    }

    /**
     * The method used to updateRolePermissions of  ISObjectPermission
     **/
    @RequestMapping(value = "/{folderId}/permissions",
            method = RequestMethod.POST)
    public List<PDMObjectPermission> updateRolePermissions(@PathVariable("vaultId") Integer vaultId,
                                                           @PathVariable("folderId") Integer folderId,
                                                          @RequestBody List<PDMObjectPermission> objectPermissions) {
        return folderService.updateRolePermissions(folderId, objectPermissions);
    }

    /**
     * The method used to getObjectPermissionsByFolder of  ISObjectPermission
     **/
    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.GET)
    public List<PDMObjectPermission> getObjectPermissionsByFolder(@PathVariable("vaultId") Integer vaultId,
                                                                 @PathVariable("folderId") Integer folderId) {

        return folderService.getObjectPermissionsByFolder(folderId);
    }

    @RequestMapping(value = "/freesearch/{freeText}", method = RequestMethod.GET)
    public Page<PDMFolder> freeTextSearch(@PathVariable("vaultId") Integer vaultId, @PathVariable("freeText") String freeText,
                                          PageRequest pageRequest, FolderCriteria folderCriteria) {
        folderCriteria.setVault(vaultId);
        folderCriteria.setSearchQuery(freeText);
        folderCriteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PDMFolder> pdmFolders = folderService.freeTextSearch(pageable, folderCriteria);
        return pdmFolders;
    }

}
