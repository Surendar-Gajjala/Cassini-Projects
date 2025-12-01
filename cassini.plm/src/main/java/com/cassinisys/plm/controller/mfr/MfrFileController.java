package com.cassinisys.plm.controller.mfr;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.service.mfr.ManufacturerFileService;
import com.cassinisys.plm.service.mfr.ManufacturerPartFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
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
 * Created by Home on 5/2/2016.
 */
@RestController
@RequestMapping("/plm/mfr/{mfrId}")
@Api(tags = "PLM.MFR",description = "Mfr Related")
public class MfrFileController extends BaseController {

    @Autowired
    private ManufacturerFileService manufacturerFileService;

    @Autowired
    private ManufacturerPartFileService manufacturerPartFileService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public List<PLMManufacturerFile> getMfrFiles(@PathVariable("mfrId") Integer mfrId) {
        return manufacturerFileService.findByMfr(mfrId);
    }

    @RequestMapping(value = "/latestFiles", method = RequestMethod.GET)
    public List<PLMManufacturerFile> getMfrLatestFiles(@PathVariable("mfrId") Integer mfrId) {
        return manufacturerFileService.findByMfrLatest(mfrId);
    }

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public List<PLMManufacturerFile> uploadFiles(@PathVariable("mfrId") Integer id, @PathVariable("folderId") Integer folderId,
                                                 MultipartHttpServletRequest request) throws CassiniException {
        return manufacturerFileService.uploadFiles(id, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.PUT)
    public PLMManufacturerFile updateFile(@PathVariable("mfrId") Integer id,
                                          @PathVariable("fileId") Integer fileId,
                                          @RequestBody PLMManufacturerFile manufacturerFile) {
        manufacturerFile.setId(fileId);
        return manufacturerFileService.update(manufacturerFile);
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteFile(@PathVariable("mfrId") Integer mfrId,
                           @PathVariable("fileId") Integer fileId) {
        manufacturerFileService.deleteMfrFile(mfrId, fileId);
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
    public PLMManufacturerFile getFile(@PathVariable("mfrId") Integer id,
                                       @PathVariable("fileId") Integer fileId) {
        return manufacturerFileService.get(fileId);
    }

    @RequestMapping(value = "/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMManufacturerFile renameFile(@PathVariable("mfrId") Integer id,
                                          @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return manufacturerFileService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMManufacturerFile> replaceMfrFiles(@PathVariable("mfrId") Integer mfrId,
                                                     @PathVariable("fileId") Integer fileId,
                                                     MultipartHttpServletRequest request) {
        return manufacturerFileService.replaceMfrFiles(mfrId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("mfrId") Integer mfrId,
                             @PathVariable("fileId") Integer fileId,
                             HttpServletResponse response) {
        PLMManufacturerFile mfrFile = manufacturerFileService.get(fileId);
        File file = manufacturerFileService.getMfrFile(mfrId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, mfrFile.getName(), file);
        }
    }

    @RequestMapping(value = "/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewFile(@PathVariable("mfrId") Integer mfrId,
                            @PathVariable("fileId") Integer fileId,
                            HttpServletResponse response) throws Exception {
        PLMManufacturerFile mfrFile = manufacturerFileService.get(fileId);
        String fileName = mfrFile.getName();
        File file = manufacturerFileService.getMfrFile(mfrId, fileId);
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

    @RequestMapping(value = "/parts/{partId}/files", method = RequestMethod.POST)
    public List<PLMManufacturerPartFile> uploadPartFiles(@PathVariable("mfrId") Integer mfrId,
                                                         @PathVariable("partId") Integer partId, @RequestParam("folderId") Integer folderId,
                                                         MultipartHttpServletRequest request) throws CassiniException, JsonProcessingException {
        return manufacturerPartFileService.uploadPartFiles(partId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}", method = RequestMethod.PUT)
    public PLMManufacturerPartFile updatePartFile(@PathVariable("mfrId") Integer id,
                                                  @PathVariable("partId") Integer partId,
                                                  @PathVariable("fileId") Integer fileId,
                                                  @RequestBody PLMManufacturerPartFile manufacturerPartFile) {
        manufacturerPartFile.setId(fileId);
        return manufacturerPartFileService.update(manufacturerPartFile);
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deletePartFile(@PathVariable("mfrId") Integer id,
                               @PathVariable("partId") Integer partId,
                               @PathVariable("fileId") Integer fileId) throws JsonProcessingException {
        manufacturerPartFileService.deletePartFile(partId, fileId);
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}", method = RequestMethod.GET)
    public PLMManufacturerPartFile getPartFile(@PathVariable("mfrId") Integer id,
                                               @PathVariable("partId") Integer partId,
                                               @PathVariable("fileId") Integer fileId) {
        return manufacturerPartFileService.get(fileId);
    }

    @RequestMapping(value = "/parts/{partId}/files", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getPartFiles(@PathVariable("mfrId") Integer id,
                                                      @PathVariable("partId") Integer partId) {
        return manufacturerPartFileService.findByMfrPart(partId);
    }

    @RequestMapping(value = "/parts/{partId}/latestFiles", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getPartLatestFiles(@PathVariable("mfrId") Integer id,
                                                            @PathVariable("partId") Integer partId) {
        return manufacturerPartFileService.findByMfrPartLatestFiles(partId);
    }

    @RequestMapping(value = "/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMManufacturerFile> getAllFileVersions(@PathVariable("mfrId") Integer mfrId,
                                                        @PathVariable("fileId") Integer fileId) {
        PLMManufacturerFile file = manufacturerFileService.get(fileId);
        return manufacturerFileService.getAllFileVersions(mfrId, file.getName());
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getAllFileVersions(@PathVariable("mfrId") Integer mfrId,
                                                            @PathVariable("partId") Integer partId,
                                                            @PathVariable("fileId") Integer fileId) {
        PLMManufacturerPartFile file = manufacturerPartFileService.get(fileId);
        return manufacturerPartFileService.getAllFileVersions(partId, file.getName());
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadPartFile(@PathVariable("mfrId") Integer mfrId,
                                 @PathVariable("partId") Integer partId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        PLMManufacturerPartFile mfrPartFile = manufacturerPartFileService.get(fileId);
        File file = manufacturerPartFileService.getMfrPartFile(partId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, mfrPartFile.getName(), file);
        }
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewPartFile(@PathVariable("mfrId") Integer mfrId,
                                @PathVariable("partId") Integer partId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws IOException {
        PLMManufacturerPartFile mfrPartFile = manufacturerPartFileService.get(fileId);
        String fileName = mfrPartFile.getName();
        File file = manufacturerPartFileService.getMfrPartFile(partId, fileId);
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

    @RequestMapping(value = "/files/byName/{name}", method = RequestMethod.GET)
    public List<PLMManufacturerFile> getMfrFileName(@PathVariable("mfrId") Integer mfrId, @PathVariable("name") String name) {
        return manufacturerFileService.findByMfrFilesName(mfrId, name);
    }

    @RequestMapping(value = "/parts/{partId}/files/byFileName/{name}", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getMfrPartFileName(@PathVariable("mfrId") Integer mfrId, @PathVariable("partId") Integer partId, @PathVariable("name") String name) {
        return manufacturerPartFileService.findByFilesName(partId, name);
    }

    @RequestMapping(value = "/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId) {
        return manufacturerFileService.fileDownloadHistory(mfrId, fileId);
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getAllFileVersionAndCommentsAndDownloads(@PathVariable("mfrId") Integer mfrId,
                                                                                  @PathVariable("partId") Integer partId,
                                                                                  @PathVariable("fileId") Integer fileId,
                                                                                  @PathVariable("objectType") ObjectType objectType) {
        PLMManufacturerPartFile file = manufacturerPartFileService.get(fileId);
        return manufacturerPartFileService.getAllFileVersionAndCommentsAndDownloads(partId, fileId, objectType);
    }

    @RequestMapping(value = "/parts/{partId}/files/{fileId}/reNameFile", method = RequestMethod.PUT)
    public PLMManufacturerPartFile renamePartFileName(@PathVariable("partId") Integer partId,
                                                      @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return manufacturerPartFileService.renamePartFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/parts/{partId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMManufacturerPartFile> replaceMfrPartFiles(@PathVariable("partId") Integer partId,
                                                             @PathVariable("fileId") Integer fileId,
                                                             MultipartHttpServletRequest request) throws JsonProcessingException {
        return manufacturerPartFileService.replaceMfrPartFiles(partId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/folder", method = RequestMethod.POST)
    public PLMManufacturerFile createMfrFolder(@PathVariable("mfrId") Integer mfrId, @RequestBody PLMManufacturerFile manufacturerFile) {
        return manufacturerFileService.createMfrFolder(mfrId, manufacturerFile);
    }

    @RequestMapping(value = "/move", method = RequestMethod.PUT)
    public PLMFile moveMfrFileToFolder(@PathVariable("mfrId") Integer mfrId,
                                       @RequestBody PLMManufacturerFile file) throws Exception {
        return manufacturerFileService.moveMfrFileToFolder(mfrId, file);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PLMManufacturerFile> getMfrFolderChildren(@PathVariable("mfrId") Integer mfrId, @PathVariable("folderId") Integer folderId) {
        return manufacturerFileService.getMfrFolderChildren(folderId);
    }

    @RequestMapping(value = "/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId) {
        manufacturerFileService.deleteFolder(mfrId, fileId);
    }

    @RequestMapping(value = "/parts/folder", method = RequestMethod.POST)
    public PLMManufacturerPartFile createMfrPartFolder(@PathVariable("mfrId") Integer mfrId, @RequestBody PLMManufacturerPartFile manufacturerFile)throws JsonProcessingException {
        return manufacturerPartFileService.createMfrPartFolder(mfrId, manufacturerFile);
    }

    @RequestMapping(value = "/parts/move", method = RequestMethod.PUT)
    public PLMFile moveMfrPartFileToFolder(@PathVariable("mfrId") Integer mfrId,
                                           @RequestBody PLMManufacturerPartFile file) throws Exception {
        return manufacturerPartFileService.moveMfrPartFileToFolder(mfrId, file);
    }

    @RequestMapping(value = "/parts/{folderId}/children", method = RequestMethod.GET)
    public List<PLMManufacturerPartFile> getMfrPartFolderChildren(@PathVariable("mfrId") Integer mfrId, @PathVariable("folderId") Integer folderId) {
        return manufacturerPartFileService.getMfrPartFolderChildren(folderId);
    }

    @RequestMapping(value = "/parts/{mfrPartId}/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteMfrPartFolder(@PathVariable("mfrId") Integer mfrId, @PathVariable("mfrPartId") Integer mfrPartId, @PathVariable("fileId") Integer fileId)throws JsonProcessingException {
        manufacturerPartFileService.deleteMfrPartFolder(mfrPartId, fileId);
    }

    @RequestMapping(value = "/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("mfrId") Integer mfrId, HttpServletResponse response) throws FileNotFoundException, IOException {
        manufacturerFileService.generateZipFile(mfrId, response);
    }

    @RequestMapping(value = "/mfrParts/{partId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generatePartZip(@PathVariable("partId") Integer partId, HttpServletResponse response) throws FileNotFoundException, IOException {
        manufacturerPartFileService.generateZipFile(partId, response);
    }

    @RequestMapping(value = "/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMManufacturerFile getLatestUploadedMfrFile(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId) {
        return manufacturerFileService.getLatestUploadedMfrFile(mfrId, fileId);
    }

    @RequestMapping(value = "/parts/{mfrPartId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMManufacturerPartFile getLatestUploadedMfrPartFile(@PathVariable("mfrId") Integer mfrId, @PathVariable("mfrPartId") Integer mfrPartId, @PathVariable("fileId") Integer fileId) {
        return manufacturerPartFileService.getLatestUploadedMfrPartFile(mfrPartId, fileId);
    }

    @RequestMapping(value = "/files/{fileId}/update", method = RequestMethod.PUT)
    public PLMFile updateFileDescription(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId, @RequestBody PLMManufacturerFile manufacturerFile) {
        return manufacturerFileService.updateFileDescription(mfrId, fileId, manufacturerFile);
    }

    @RequestMapping(value = "/parts/files/{fileId}/update", method = RequestMethod.PUT)
    public PLMFile updateMfrPartFileDescription(@PathVariable("mfrId") Integer mfrId, @PathVariable("fileId") Integer fileId, @RequestBody PLMManufacturerPartFile manufacturerFile) {
        return manufacturerPartFileService.updateFileDescription(mfrId, fileId, manufacturerFile);
    }

    @RequestMapping(value = "/files/paste", method = RequestMethod.PUT)
    public List<PLMManufacturerFile> pasteFromClipboard(@PathVariable("mfrId") Integer mfrId, @RequestParam("fileId") Integer fileId,
                                                        @RequestBody List<PLMFile> files) {
        return manufacturerFileService.pasteFromClipboard(mfrId, fileId, files);
    }

    @RequestMapping(value = "/parts/files/paste", method = RequestMethod.PUT)
    public List<PLMManufacturerPartFile> pasteMfrPartFilesFromClipboard(@PathVariable("mfrId") Integer mfrId, @RequestParam("fileId") Integer fileId,
                                                                        @RequestBody List<PLMFile> files) {
        return manufacturerPartFileService.pasteFromClipboard(mfrId, fileId, files);
    }

    @RequestMapping(value = "{mfrId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedMfrFiles(@PathVariable("mfrId") Integer mfrId, @RequestBody List<PLMManufacturerFile> manufacturerFiles) {
        manufacturerFileService.undoCopiedMfrFiles(mfrId, manufacturerFiles);
    }

    @RequestMapping(value = "{mfrId}/parts/files/undo", method = RequestMethod.PUT)
    public void undoCopiedMfrPartFiles(@PathVariable("mfrId") Integer mfrId, @RequestBody List<PLMManufacturerPartFile> manufacturerPartFiles) {
        manufacturerPartFileService.undoCopiedMfrPartFiles(mfrId, manufacturerPartFiles);
    }
}
