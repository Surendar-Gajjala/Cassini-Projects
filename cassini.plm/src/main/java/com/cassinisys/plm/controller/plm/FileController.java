package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.plm.PLMItemFile;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.plm.ItemService;
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
 * Created by reddy on 22/12/15.
 */
@RestController
@RequestMapping("/plm/items/{itemId}/files")
@Api(tags = "PLM.ITEMS",description = "Items Related")
public class FileController extends BaseController {

    @Autowired
    private ItemFileService itemFileService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FileDownloadService fileDownloadService;


    @RequestMapping(method = RequestMethod.GET)
    public List<PLMItemFile> getItemFiles(@PathVariable("itemId") Integer itemId) {
        PLMItemRevision item1 = itemService.getRevision(itemId);
        return itemFileService.findByItem(item1);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public List<PLMItemFile> getItemFilesByName(@PathVariable("itemId") Integer itemId,
                                                @PathVariable("name") String name) {
        PLMItemRevision item1 = itemService.getRevision(itemId);
        return itemFileService.findByItemandFileName(item1, name);
    }

    @RequestMapping(value = "/{folderId}", method = RequestMethod.POST)
    public List<PLMItemFile> uploadItemFiles(@PathVariable("itemId") Integer itemId, @PathVariable("folderId") Integer folderId,
                                             MultipartHttpServletRequest request) throws CassiniException {
        return itemFileService.uploadItemFiles(itemId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMItemFile> replaceItemFiles(@PathVariable("itemId") Integer itemId,
                                              @PathVariable("fileId") Integer fileId,
                                              MultipartHttpServletRequest request) throws CassiniException {
        return itemFileService.replaceItemFiles(itemId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public PLMItemFile getItemFile(@PathVariable("fileId") Integer fileId) {
        return itemFileService.get(fileId);
    }

    @RequestMapping(value = "/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMItemFile getLatestUploadedFile(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        return itemFileService.getLatestUploadedFile(itemId, fileId);
    }

    @RequestMapping(value = "/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMItemFile updateFileName(@PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return itemFileService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMItemFile> getAllFileVersions(@PathVariable("itemId") Integer itemId,
                                                @PathVariable("fileId") Integer fileId) {
        PLMItemFile file = itemFileService.get(fileId);
        return itemFileService.getAllFileVersions(itemId, file.getName());
    }

    @RequestMapping(value = "/{fileId}/downloadHistory", method = RequestMethod.GET)
    public List<PLMFileDownloadHistory> getDownloadHistory(@PathVariable("itemId") Integer itemId,
                                                           @PathVariable("fileId") Integer fileId) {
        return itemFileService.getDownloadHistory(fileId);
    }

    @RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        itemFileService.downloadFile(itemId, fileId, response);
    }

    @RequestMapping(value = "/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("itemId") Integer itemId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        itemFileService.previewFile(itemId, fileId, response);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateItemFile(@PathVariable("fileId") Integer fileId,
                                  @RequestBody PLMItemFile file) {
        return itemFileService.updateFile(fileId, file);
    }

    @RequestMapping(value = "/{fileId}/move", method = RequestMethod.PUT)
    public PLMFile moveItemFileToFolder(@PathVariable("fileId") Integer fileId,
                                        @RequestBody PLMItemFile file) throws Exception {
        return itemFileService.moveItemFileToFolder(fileId, file);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public void deleteItemFile(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        itemFileService.deleteItemFile(itemId, fileId);
    }

    @RequestMapping(value = "/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        return itemFileService.fileDownloadHistory(itemId, fileId);
    }

    @RequestMapping(value = "/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    private List<PLMItemFile> getAllFileVersionComments(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId,
                                                        @PathVariable("objectType") ObjectType objectType) {
        return itemFileService.getAllFileVersionComments(itemId, fileId, objectType);
    }

    @RequestMapping(value = "/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("itemId") Integer itemId, HttpServletResponse response) throws FileNotFoundException, IOException {
        itemFileService.generateZipFile(itemId, response);
    }

    @RequestMapping(value = "/folder", method = RequestMethod.POST)
    public PLMItemFile createFolder(@PathVariable("itemId") Integer itemId, @RequestBody PLMItemFile plmItemFile) {
        return itemFileService.createFolder(itemId, plmItemFile);
    }

    @RequestMapping(value = "/{fileId}/children", method = RequestMethod.GET)
    public List<PLMItemFile> getFolderChildren(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        return itemFileService.getFolderChidren(itemId, fileId);
    }

    @RequestMapping(value = "/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        itemFileService.deleteFolder(itemId, fileId);
    }

    @RequestMapping(value = "/paste", method = RequestMethod.PUT)
    public List<PLMItemFile> pasteFromClipboard(@PathVariable("itemId") Integer itemId, @RequestParam("fileId") Integer fileId,
                                                @RequestBody List<PLMFile> files) {
        return itemFileService.pasteFromClipboard(itemId, fileId, files);
    }

    @RequestMapping(value = "/undo", method = RequestMethod.PUT)
    public void undoCopiedItemFiles(@PathVariable("itemId") Integer itemId, @RequestBody List<PLMItemFile> itemFiles) {
        itemFileService.undoCopiedItemFiles(itemId, itemFiles);
    }

    @RequestMapping(value = "/convert/forge", method = RequestMethod.POST)
    public PLMFile uploadImage(@PathVariable("itemId") Integer id, @RequestBody PLMFile plmfile) {
        return itemFileService.convertForgeFile(id, plmfile);
    }
}
