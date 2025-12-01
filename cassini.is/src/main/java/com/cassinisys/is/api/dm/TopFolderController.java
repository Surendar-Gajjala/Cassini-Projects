package com.cassinisys.is.api.dm;

import com.cassinisys.is.model.dm.ISObjectPermission;
import com.cassinisys.is.model.dm.ISTopDocument;
import com.cassinisys.is.model.dm.ISTopFolder;
import com.cassinisys.is.service.dm.TopDocumentService;
import com.cassinisys.is.service.dm.TopFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@RestController
@RequestMapping("is/folders")
@Api(name = "Top folders", description = "Top folders endpoint", group = "Folder")
public class TopFolderController extends BaseController {

    @Autowired
    private TopFolderService topFolderService;

    @Autowired
    private TopDocumentService topDocumentService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopFolder create(@RequestBody ISTopFolder item) {
        return topFolderService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopFolder update(@PathVariable("id") Integer id, @RequestBody ISTopFolder item) {
        item.setId(id);
        return topFolderService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topFolderService.delete(id);
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}", method = RequestMethod.DELETE)
    public void deleteDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId) {
        topFolderService.deleteDocument(folderId, documentId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopFolder get(@PathVariable("id") Integer id) {
        return topFolderService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopFolder> get() {
        return topFolderService.getAll();
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}/download",
            method = RequestMethod.GET)
    public void downloadDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId,
                                 HttpServletResponse response) {
        ISTopDocument document = topFolderService.getDocument(folderId, documentId);
        File file = topFolderService.getDocumentFile(folderId, documentId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, document.getName(), file);
        }
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<ISTopFolder> getClassificationTree() {
        return topFolderService.getFolderTree();
    }

    @RequestMapping(value = "/{folderId}/documents", method = RequestMethod.POST)
    public List<ISTopDocument> uploadDocuments(@PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) {
        return topFolderService.uploadDocuments(folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.GET)
    public List<ISObjectPermission> getObjectPermissionsByFolder(@PathVariable("folderId") Integer folderId) {
        return topFolderService.getObjectPermissionsByFolder(folderId);
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}", method = RequestMethod.PUT)
    public ISTopDocument updateDocument(@PathVariable("folderId") Integer folderId, @PathVariable("documentId") Integer documentId,
                                        @RequestBody ISTopDocument document) {
        return topFolderService.updateFolderDocument(folderId, document);
    }

    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.POST)
    public List<ISObjectPermission> updateRolePermissions(@PathVariable Integer folderId,
                                                          @RequestBody List<ISObjectPermission> objectPermissions) {
        return topFolderService.updateRolePermissions(folderId, objectPermissions);
    }
}
