package com.cassinisys.is.api.dm;
/**
 * The Class is for ProjectFolderController
 **/

import com.cassinisys.is.model.dm.ISObjectPermission;
import com.cassinisys.is.model.dm.ISProjectDocument;
import com.cassinisys.is.model.dm.ISProjectFolder;
import com.cassinisys.is.service.dm.ProjectFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Api(name = "Project folders",
        description = "Project folders endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/folders")
public class ProjectFolderController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectFolderService projectFolderService;
    @Autowired
    private FileDownloadService fileDownloadService;

    /**
     * The method used for creating the IsProjectFolder
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectFolder create(@PathVariable("projectId") Integer projectId,
                                  @RequestBody ISProjectFolder folder) {
        return projectFolderService.create(projectId, folder);
    }

    /**
     * The method used for updating the IsProjectFolder
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.PUT)
    public ISProjectFolder update(@PathVariable("projectId") Integer projectId,
                                  @PathVariable("folderId") Integer folderId,
                                  @RequestBody ISProjectFolder folder) {
        return projectFolderService.update(projectId, folder);
    }

    /**
     * The method used for deleting the folderId in IsProjectFolder
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("folderId") Integer folderId) {
        projectFolderService.delete(folderId);
    }

    /**
     * The method used to obtain(or get) IsProjectFolder through folderId
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.GET)
    public ISProjectFolder get(@PathVariable("projectId") Integer projectId,
                               @PathVariable("folderId") Integer folderId) {
        return projectFolderService.get(folderId);
    }

    /**
     * The method used to getChildren of  IsProjectFolder
     **/
    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<ISProjectFolder> getChildren(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId) {
        return projectFolderService.getChildren(folderId);
    }

    /**
     * The method used to getFolderDocuments of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/documents", method = RequestMethod.GET)
    public List<ISProjectDocument> getFolderDocuments(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId) {
        return projectFolderService.getDocuments(folderId);
    }

    /**
     * The method used to uploadDocuments of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/documents",
            method = RequestMethod.POST)
    public List<ISProjectDocument> uploadDocuments(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId,
            MultipartHttpServletRequest request) {
        return projectFolderService.uploadDocuments(folderId,
                request.getFileMap());
    }

    /**
     * The method used to getDocument of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}",
            method = RequestMethod.GET)
    public ISProjectDocument getDocumentByFolder(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId) {
        return projectFolderService.getDocument(folderId, documentId);
    }

    /**
     * The method used to getMultipleDocuments of  ISProjectDocument
     **/
    @RequestMapping(value = "/multiple/documents/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectDocument> getMultipleDocuments(@PathVariable Integer[] ids) {
        return projectFolderService.findMultipleDocuments(Arrays.asList(ids));
    }

    /**
     * The method used to getMultipleFolders of  ISProjectFolder
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectFolder> getMultipleFolders(@PathVariable Integer[] ids) {
        return projectFolderService.findMultipleFolders(Arrays.asList(ids));
    }

    /**
     * The method used to downloadDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}/download",
            method = RequestMethod.GET)
    public void downloadDocument(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId,
            HttpServletResponse response) {
        ISProjectDocument document = projectFolderService.getDocument(folderId, documentId);
        File file = projectFolderService.getDocumentFile(folderId, documentId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, document.getName(), file);
        }
    }

    /**
     * The method used to updateDocument of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}",
            method = RequestMethod.PUT)
    public ISProjectDocument updateDocument(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId,
            @RequestBody ISProjectDocument document) {
        return projectFolderService.updateDocument(folderId, document);
    }

    /**
     * The method used to getDocumentVersions of  IsProjectDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}/versions",
            method = RequestMethod.GET)
    public List<ISProjectDocument> getDocumentVersions(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId) {
        return projectFolderService.getDocumentVersions(folderId, documentId);
    }

    /**
     * The method used to updateRolePermissions of  ISObjectPermission
     **/
    @RequestMapping(value = "/{folderId}/permissions",
            method = RequestMethod.PUT)
    public List<ISObjectPermission> updateRolePermissions(@PathVariable Integer folderId,
                                                          @RequestBody List<ISObjectPermission> objectPermissions) {
        return projectFolderService.updateRolePermissions(folderId, objectPermissions);
    }

    /**
     * The method used to getObjectPermissionsByFolder of  ISObjectPermission
     **/
    @RequestMapping(value = "/{folderId}/permissions", method = RequestMethod.GET)
    public List<ISObjectPermission> getObjectPermissionsByFolder(@PathVariable("projectId") Integer projectId,
                                                                 @PathVariable("folderId") Integer folderId) {
        return projectFolderService.getObjectPermissionsByFolder(folderId);
    }

    @RequestMapping(value = "/{folderId}/documents/{documentId}", method = RequestMethod.DELETE)
    public void deleteDocument(@PathVariable("folderId") Integer folderId,
                               @PathVariable("documentId") Integer documentId) {
        projectFolderService.deleteDocument(folderId, documentId);
    }

}
