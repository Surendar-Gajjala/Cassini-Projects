package com.cassinisys.drdo.controller.bom;

import com.cassinisys.drdo.model.bom.ItemFile;
import com.cassinisys.drdo.model.bom.ItemRevision;
import com.cassinisys.drdo.service.bom.ItemFileService;
import com.cassinisys.drdo.service.bom.ItemService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@RestController
@RequestMapping("drdo/items/{itemId}/files")
@Api(name = "Files", description = "Files endpoint", group = "DRDO")
public class FileController extends BaseController {

    @Autowired
    private ItemFileService itemFileService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private FileDownloadService fileDownloadService;


    @RequestMapping(method = RequestMethod.GET)
    public List<ItemFile> getItemFiles(@PathVariable("itemId") Integer itemId) {
        ItemRevision item1 = itemService.getRevision(itemId);
        return itemFileService.findByItem(item1);
    }

    @RequestMapping(value = "/byName/{name}", method = RequestMethod.GET)
    public List<ItemFile> getItemFilesByName(@PathVariable("itemId") Integer itemId,
                                             @PathVariable("name") String name) {
        ItemRevision item1 = itemService.getRevision(itemId);
        return itemFileService.findByItemandFileName(item1, name);
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<ItemFile> uploadItemFiles(@PathVariable("itemId") Integer itemId,
                                          MultipartHttpServletRequest request) {
        return itemFileService.uploadItemFiles(itemId, request.getFileMap());
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public ItemFile getItemFile(@PathVariable("fileId") Integer fileId) {
        return itemFileService.get(fileId);
    }

    @RequestMapping(value = "/{fileId}/versions", method = RequestMethod.GET)
    public List<ItemFile> getAllFileVersions(@PathVariable("itemId") Integer itemId,
                                             @PathVariable("fileId") Integer fileId) {
        ItemFile file = itemFileService.get(fileId);
        return itemFileService.getAllFileVersions(itemId, file.getName());
    }

    @RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        ItemFile itemFile = itemFileService.get(fileId);
        File file = itemFileService.getItemFile(itemId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, itemFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("itemId") Integer itemId,
                                @PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        ItemFile itemFile = itemFileService.get(fileId);
        File file = itemFileService.getItemFile(itemId, fileId);
        String fileName = itemFile.getName();
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

    @RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
    public ItemFile updateItemFile(@PathVariable("fileId") Integer fileId,
                                   @RequestBody ItemFile file) {
        return itemFileService.update(file);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public void deleteItemFile(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        itemFileService.deleteItemFile(itemId, fileId);
    }

    @RequestMapping(value = "/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    private List<ItemFile> getAllFileVersionComments(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId,
                                                     @PathVariable("objectType") ObjectType objectType) {
        return itemFileService.getAllFileVersionComments(itemId, fileId, objectType);
    }
}
