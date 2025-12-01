package com.cassinisys.plm.controller.pqm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.pm.ShareObjectFileDto;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@RestController
@RequestMapping("/plm/objects")
@Api(tags = "PLM.PQM", description = "Quality Related")
public class ObjectFileController extends BaseController {

    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/{id}/{objectType}/files/{folderId}", method = RequestMethod.POST)
    public ObjectFileDto uploadObjectFiles(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                           @PathVariable("folderId") Integer folderId, MultipartHttpServletRequest request) throws CassiniException, JsonProcessingException {
        String type = "file";
        if (objectType.equals(PLMObjectType.DOCUMENT)) type = "document";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) type = "mfrpartinspectionreport";
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) type = "ppapchecklist";
        return objectFileService.uploadObjectFiles(id, objectType, folderId, request.getFileMap(), type);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/count", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getObjectFilesCount(@PathVariable("id") Integer id,
                                       @PathVariable("objectType") PLMObjectType objectType) {
        return objectFileService.getObjectFilesCount(id, objectType);
    }

    @RequestMapping(value = "/{id}/{objectType}/files", method = RequestMethod.GET)
    public ObjectFileDto getObjectFiles(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        return objectFileService.getObjectFiles(id, objectType, hierarchy);
    }

    @RequestMapping(value = "/{id}/type/{objectType}/files", method = RequestMethod.GET)
    public ObjectFileDto getObjectFilesByType(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType) {
        return objectFileService.getObjectFilesByType(id, objectType, false);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}", method = RequestMethod.GET)
    public FileDto getObjectFile(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId) {
        return objectFileService.getObjectFile(id, objectType, fileId);
    }


    @RequestMapping(value = "/{id}/files/{fileId}/{objectType}", method = RequestMethod.PUT)
    public ObjectFileDto updateObjectFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                          @PathVariable("objectType") PLMObjectType objectType, @RequestBody ObjectFileDto qualityFile) throws JsonProcessingException {
        return objectFileService.updateObjectFile(id, fileId, objectType, qualityFile);
    }

    @RequestMapping(value = "/{id}/{objectType}/folders", method = RequestMethod.POST)
    public ObjectFileDto createFolder(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @RequestBody ObjectFileDto objectFileDto) throws JsonProcessingException {
        return objectFileService.createFolder(id, objectType, objectFileDto);
    }

    @RequestMapping(value = "/{id}/{objectType}/folders/{folderId}", method = RequestMethod.PUT)
    public ObjectFileDto updateFolder(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("folderId") Integer folderId,
                                      @RequestBody ObjectFileDto objectFileDto) throws JsonProcessingException {
        return objectFileService.updateFolder(id, folderId, objectType, objectFileDto);
    }

    @RequestMapping(value = "/{id}/{objectType}/folders/{folderId}/children", method = RequestMethod.GET)
    public ObjectFileDto getFolderChildren(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("folderId") Integer folderId, @RequestParam(value = "hierarchy", required = false) Boolean hierarchy) {
        return objectFileService.getFolderChildren(id, objectType, folderId, hierarchy);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public ObjectFileDto updateFileName(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                        @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return objectFileService.updateFileName(id, objectType, fileId, newFileName);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/replaceFile", method = RequestMethod.POST)
    public ObjectFileDto replaceObjectFile(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                           @PathVariable("fileId") Integer fileId, MultipartHttpServletRequest request) throws CassiniException, JsonProcessingException {
        String type = "file";
        if (objectType.equals(PLMObjectType.DOCUMENT)) type = "document";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) type = "mfrpartinspectionreport";
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) type = "ppapchecklist";
        return objectFileService.replaceObjectFile(id, objectType, fileId, request.getFileMap(), type);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public ObjectFileDto getLatestUploadedFile(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId) {
        return objectFileService.getLatestUploadedFile(id, objectType, fileId);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteObjectFile(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        objectFileService.deleteObjectFile(id, objectType, fileId);
    }

    @RequestMapping(value = "/{id}/{objectType}/folders/{fileId}", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        objectFileService.deleteFolder(id, objectType, fileId);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/move", method = RequestMethod.PUT)
    public ObjectFileDto moveObjectFileToFolder(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                                @PathVariable("fileId") Integer fileId, @RequestBody ObjectFileDto file) throws Exception {
        return objectFileService.moveObjectFileToFolder(id, objectType, file);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/paste", method = RequestMethod.PUT)
    public ObjectFileDto pasteFromClipboard(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @RequestParam("fileId") Integer fileId,
                                            @RequestBody List<PLMFile> files) {
        return objectFileService.pasteFromClipboard(id, objectType, fileId, files);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedItemFiles(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @RequestBody ObjectFileDto objectFileDto) {
        objectFileService.undoCopiedItemFiles(id, objectType, objectFileDto);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        return objectFileService.fileDownloadHistory(id, objectType, fileId);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                 @PathVariable("fileId") Integer fileId, HttpServletResponse response) throws DocumentException {
        String type = "file";
        if (objectType.equals(PLMObjectType.DOCUMENT)) type = "document";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) type = "mfrpartinspectionreport";
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) type = "ppapchecklist";
        objectFileService.downloadFile(id, objectType, fileId, response, type);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, HttpServletResponse response) throws IOException {
        objectFileService.generateZipFile(id, objectType, response);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/byName/{name}", method = RequestMethod.GET)
    public ObjectFileDto getItemFilesByName(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType,
                                            @PathVariable("name") String name) {
        return objectFileService.findByTypeAndFileName(id, objectType, name);
    }

    @RequestMapping(value = "/{objectType}/files/{fileId}/versionComments/{file}", method = RequestMethod.GET)
    private ObjectFileDto getAllFileVersionComments(@PathVariable("objectType") PLMObjectType objectType, @PathVariable("fileId") Integer fileId, @PathVariable("file") ObjectType file) {
        return objectFileService.getAllFileVersionComments(fileId, objectType, file);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                @PathVariable("objectType") PLMObjectType objectType, HttpServletResponse response) throws Exception {
        String type = "file";
        if (objectType.equals(PLMObjectType.DOCUMENT)) type = "document";
        if (objectType.equals(PLMObjectType.MFRPARTINSPECTIONREPORT)) type = "mfrpartinspectionreport";
        if (objectType.equals(PLMObjectType.PPAPCHECKLIST)) type = "ppapchecklist";
        objectFileService.previewFile(id, objectType, fileId, response, type);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/{fileId}/comment/notification/{type}", method = RequestMethod.POST)
    public void sendFileCommentNotification(@PathVariable("id") Integer id, @PathVariable("fileId") Integer fileId,
                                            @PathVariable("objectType") PLMObjectType objectType, @PathVariable("type") String type, @RequestBody Comment comment) {
        objectFileService.sendFileCommentNotification(id, fileId, objectType, type, comment);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/createdByPersons", method = RequestMethod.GET)
    public List<Person> getCreatedByPersons(@PathVariable("id") Integer id,
                                            @PathVariable("objectType") PLMObjectType objectType) {
        return objectFileService.getCreatedByPersons(id, objectType);
    }

    @RequestMapping(value = "/{id}/{objectType}/files/share/objects", method = RequestMethod.POST)
    public List<FileDto> createSharedObjectFiles(@PathVariable("id") Integer id, @PathVariable("objectType") PLMObjectType objectType, @RequestBody ShareObjectFileDto objectFileDto) {
        return objectFileService.createSharedObjectFiles(id, objectType, objectFileDto);
    }
}
