package com.cassinisys.documents.api.dm;

import com.cassinisys.documents.model.dm.DMDocument;
import com.cassinisys.documents.model.dm.DMFolder;
import com.cassinisys.documents.model.dm.DMObjectPermission;
import com.cassinisys.documents.service.DMFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by swapna on 11/12/18.
 */
@RestController
@RequestMapping("dm/folders")
public class DMFolderController extends BaseController {
    @Autowired
    private DMFolderService folderService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public DMFolder create(@RequestBody DMFolder item) {
        return folderService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DMFolder update(@PathVariable("id") Integer id, @RequestBody DMFolder item) {
        item.setId(id);
        return folderService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        folderService.delete(id);
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}", method = RequestMethod.DELETE)
    public void deleteDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId) {
        folderService.deleteDocument(folderId, documentId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DMFolder get(@PathVariable("id") Integer id) {
        return folderService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<DMFolder> get() {
        return folderService.getAll();
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}/download",
            method = RequestMethod.GET)
    public void downloadDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId,
                                 HttpServletResponse response) {
        DMDocument document = folderService.getDocument(folderId, documentId);
        File file = folderService.getDocumentFile(folderId, documentId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, document.getName(), file);
        }
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DMFolder> getClassificationTree() {
        return folderService.getFolderTree();
    }

    @RequestMapping(value = "/{folderId}/documents", method = RequestMethod.POST)
    public List<DMDocument> uploadDocuments(@PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) {
        return folderService.uploadDocuments(folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}", method = RequestMethod.PUT)
    public DMDocument updateDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId,
                                        @RequestBody DMDocument document) {
        return folderService.updateFolderDocument(folderId, document);
    }

    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.GET)
    public List<DMObjectPermission> getObjectPermissionsByFolder(@PathVariable("folderId") Integer folderId) {

        return folderService.getObjectPermissionsByFolder(folderId);
    }

    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.POST)
    public List<DMObjectPermission> updateRolePermissions(@PathVariable Integer folderId,
                                                          @RequestBody List<DMObjectPermission> objectPermissions) {
        return folderService.updateRolePermissions(folderId, objectPermissions);
    }
}
