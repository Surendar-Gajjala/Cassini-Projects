package com.cassinisys.platform.api.custom;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.custom.CustomFile;
import com.cassinisys.platform.model.custom.CustomFileDownloadHistory;
import com.cassinisys.platform.model.custom.CustomObjectFile;
import com.cassinisys.platform.service.custom.CustomObjectFileService;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by GSR on 16-10-2020.
 */
@RestController
@RequestMapping("/customObjects")
public class CustomObjectFileController extends BaseController {

    @Autowired
    private CustomObjectFileService customObjectFileService;
    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(value = "/{id}/files/{folderId}", method = RequestMethod.POST)
    public List<CustomFile> uploadCustomObjectFiles(@PathVariable("id") Integer id,
                                                    @PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException {
        return customObjectFileService.uploadCustomObjectFiles(id, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{id}/files", method = RequestMethod.GET)
    public List<CustomObjectFile> getCustomObjectFiles(@PathVariable("id") Integer id) {
        return customObjectFileService.getCustomObjectFiles(id);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/update", method = RequestMethod.PUT)
    public CustomObjectFile updateCustomObjectFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                                   @RequestBody CustomObjectFile changeFile) {
        return customObjectFileService.updateCustomObjectFile(id, fileId, changeFile);
    }

    @RequestMapping(value = "/{id}/folders", method = RequestMethod.POST)
    public CustomObjectFile createFolder(@PathVariable("id") Integer id, @RequestBody CustomObjectFile changeFile) {
        return customObjectFileService.createFolder(id, changeFile);
    }

    @RequestMapping(value = "/{id}/folders/{folderId}", method = RequestMethod.PUT)
    public CustomObjectFile updateFolder(@PathVariable("id") Integer id, @PathVariable("folderId") Integer folderId,
                                         @RequestBody CustomObjectFile customObjectFile) {
        return customObjectFileService.updateFolder(id, folderId, customObjectFile);
    }

    @RequestMapping(value = "/{id}/folders/{folderId}/children", method = RequestMethod.GET)
    public List<CustomObjectFile> getFolderChildren(@PathVariable("id") Integer id, @PathVariable("folderId") Integer folderId) {
        return customObjectFileService.getFolderChildren(id, folderId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public CustomObjectFile updateFileName(@PathVariable("id") Integer id,
                                           @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return customObjectFileService.updateFileName(id, fileId, newFileName);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/replaceFile", method = RequestMethod.POST)
    public CustomObjectFile replaceCustomObjectFile(@PathVariable("id") Integer id,
                                                    @PathVariable("fileId") Integer fileId, MultipartHttpServletRequest request) throws CassiniException {
        return customObjectFileService.replaceCustomObjectFile(id, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/{id}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public CustomObjectFile getLatestUploadedFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        return customObjectFileService.getLatestUploadedFile(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteCustomObjectFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        customObjectFileService.deleteCustomObjectFile(id, fileId);
    }

    @RequestMapping(value = "/{id}/folders/{fileId}", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        customObjectFileService.deleteFolder(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/move", method = RequestMethod.PUT)
    public CustomObjectFile moveCustomObjectFileToFolder(@PathVariable("id") Integer id,
                                                         @PathVariable("fileId") Integer fileId, @RequestBody CustomObjectFile file) throws Exception {
        return customObjectFileService.moveCustomObjectFileToFolder(id, file);
    }

    @RequestMapping(value = "/{id}/files/paste", method = RequestMethod.PUT)
    public List<CustomFile> pasteFromClipboard(@PathVariable("id") Integer id, @RequestParam("fileId") Integer fileId,
                                               @RequestBody List<CustomFile> files) {
        return customObjectFileService.pasteFromClipboard(id, fileId, files);
    }

    @RequestMapping(value = "/{id}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedItemFiles(@PathVariable("id") Integer id, @RequestBody List<CustomObjectFile> changeFiles) {
        customObjectFileService.undoCopiedItemFiles(id, changeFiles);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("id") Integer id,
                                 @PathVariable("fileId") Integer fileId, HttpServletResponse response) {
        CustomFile customFile = customObjectFileService.getCustomObjectFile(fileId);
        File file = customObjectFileService.getFile(id, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, '"' + customFile.getName() + '"', file);
        }
    }

    @RequestMapping(value = "/{id}/files/byName/{name}", method = RequestMethod.GET)
    public List<CustomObjectFile> getItemFilesByName(@PathVariable("id") Integer id,
                                                     @PathVariable("name") String name) {
        return customObjectFileService.findByTypeAndFileName(id, name);
    }

    @RequestMapping(value = "/files/{fileId}/versionComments/{file}", method = RequestMethod.GET)
    private List<CustomObjectFile> getAllFileVersionComments(@PathVariable("fileId") Integer fileId, @PathVariable("file") ObjectType file) {
        return customObjectFileService.getAllFileVersionComments(fileId, file);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        CustomFile customFile = customObjectFileService.getCustomObjectFile(fileId);
        File file = customObjectFileService.getFile(id, fileId);
        String fileName = customFile.getName();
        if (file != null) {
            try {
                String e = URLDecoder.decode(fileName, "UTF-8");
                response.setHeader("Content-disposition", "inline; filename=" + e);
            } catch (UnsupportedEncodingException var6) {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            ServletOutputStream e1 = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), e1);
            e1.flush();
        }
    }

    @RequestMapping(value = "{id}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<CustomObjectFile> getAllFileVersions(@PathVariable("id") Integer id,
                                                     @PathVariable("fileId") Integer fileId) {
        CustomFile file = customObjectFileService.getCustomObjectFile(fileId);
        return customObjectFileService.getAllFileVersions(id, file.getName());
    }

    @RequestMapping(value = "/{fileId}/downloadHistory", method = RequestMethod.GET)
    public List<CustomFileDownloadHistory> getDownloadHistory(@PathVariable("objectId") Integer objectId,
                                                              @PathVariable("fileId") Integer fileId) {
        return customObjectFileService.getDownloadHistory(fileId);
    }

    @RequestMapping(value = "/{id}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public CustomFileDownloadHistory fileDownloadHistory(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId) {
        return customObjectFileService.fileDownloadHistory(id, fileId);
    }

    @RequestMapping(value = "/{id}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("id") Integer id, HttpServletResponse response) throws FileNotFoundException, IOException {
        customObjectFileService.generateZipFile(id, response);
    }
}
