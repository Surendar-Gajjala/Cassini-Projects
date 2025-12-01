package com.cassinisys.plm.controller.cm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.plm.model.cm.PLMChangeFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.cm.ChangeFileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by subramanyam on 15-06-2020.
 */
@RestController
@RequestMapping("/changes")
@Api(tags = "PLM.CM", description = "Changes Related")
public class ChangeFileController extends BaseController {

    @Autowired
    private ChangeFileService changeFileService;
    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(value = "/{id}/files/{folderId}", method = RequestMethod.POST)
    public List<PLMChangeFile> uploadChangeFiles(@PathVariable("id") Integer id,
                                                 @PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException {
        return changeFileService.uploadChangeFiles(id, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{id}/files", method = RequestMethod.GET)
    public List<PLMChangeFile> getChangeFiles(@PathVariable("id") Integer id) {
        return changeFileService.getChangeFiles(id);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/update", method = RequestMethod.PUT)
    public PLMChangeFile updateChangeFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                          @RequestBody PLMChangeFile changeFile) {
        return changeFileService.updateChangeFile(id, fileId, changeFile);
    }

    @RequestMapping(value = "/{id}/folders", method = RequestMethod.POST)
    public PLMChangeFile createFolder(@PathVariable("id") Integer id, @RequestBody PLMChangeFile changeFile) {
        return changeFileService.createFolder(id, changeFile);
    }

    @RequestMapping(value = "/{id}/folders/{folderId}/children", method = RequestMethod.GET)
    public List<PLMChangeFile> getFolderChildren(@PathVariable("id") Integer id, @PathVariable("folderId") Integer folderId) {
        return changeFileService.getFolderChildren(id, folderId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMChangeFile updateFileName(@PathVariable("id") Integer id,
                                        @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return changeFileService.updateFileName(id, fileId, newFileName);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/replaceFile", method = RequestMethod.POST)
    public PLMChangeFile replaceChangeFile(@PathVariable("id") Integer id,
                                           @PathVariable("fileId") Integer fileId, MultipartHttpServletRequest request) throws CassiniException {
        return changeFileService.replaceChangeFile(id, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/{id}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMChangeFile getLatestUploadedFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        return changeFileService.getLatestUploadedFile(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteChangeFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        changeFileService.deleteChangeFile(id, fileId);
    }

    @RequestMapping(value = "/{id}/folders/{fileId}", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        changeFileService.deleteFolder(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/move", method = RequestMethod.PUT)
    public PLMChangeFile moveChangeFileToFolder(@PathVariable("id") Integer id,
                                                @PathVariable("fileId") Integer fileId, @RequestBody PLMChangeFile file) throws Exception {
        return changeFileService.moveChangeFileToFolder(id, file);
    }

    @RequestMapping(value = "/{id}/files/paste", method = RequestMethod.PUT)
    public List<PLMChangeFile> pasteFromClipboard(@PathVariable("id") Integer id, @RequestParam("fileId") Integer fileId,
                                                  @RequestBody List<PLMFile> files) {
        return changeFileService.pasteFromClipboard(id, fileId, files);
    }

    @RequestMapping(value = "/{id}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedItemFiles(@PathVariable("id") Integer id, @RequestBody List<PLMChangeFile> changeFiles) {
        changeFileService.undoCopiedItemFiles(id, changeFiles);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        return changeFileService.fileDownloadHistory(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("id") Integer id,
                                 @PathVariable("fileId") Integer fileId, HttpServletResponse response) {
        changeFileService.downloadFile(id, fileId, response);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, HttpServletResponse response) throws FileNotFoundException, IOException {
        changeFileService.generateZipFile(id, objectType, response);
    }

    @RequestMapping(value = "/{id}/files/byName/{name}", method = RequestMethod.GET)
    public List<PLMChangeFile> getItemFilesByName(@PathVariable("id") Integer id,
                                                  @PathVariable("name") String name) {
        return changeFileService.findByTypeAndFileName(id, name);
    }

    @RequestMapping(value = "/files/{fileId}/versionComments/{file}", method = RequestMethod.GET)
    private List<PLMChangeFile> getAllFileVersionComments(@PathVariable("fileId") Integer fileId, @PathVariable("file") ObjectType file) {
        return changeFileService.getAllFileVersionComments(fileId, file);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        changeFileService.previewFile(id, fileId, response);
    }


    @RequestMapping(value = "{id}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMChangeFile> getAllFileVersions(@PathVariable("id") Integer id,
                                                  @PathVariable("fileId") Integer fileId) {
        PLMFile file = changeFileService.getChangeFile(fileId);
        return changeFileService.getAllFileVersions(fileId);
    }
}
