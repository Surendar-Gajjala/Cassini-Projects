package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMItem;
import com.cassinisys.pdm.model.PDMItemFile;
import com.cassinisys.pdm.model.PDMItemFileVersionHistory;
import com.cassinisys.pdm.service.PDMItemFileService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "ItemFile", description = "ItemFile endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/itemFiles")
public class PDMItemFileController extends BaseController {

    @Autowired
    private PDMItemFileService itemFileService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public PDMItemFile create(@RequestBody PDMItemFile pdmItemFile) {
        pdmItemFile.setId(null);
        return itemFileService.create(pdmItemFile);
    }

    @RequestMapping(value = "/{itemId}/files", method = RequestMethod.POST)
    public List<PDMItemFile> uploadItemFiles(@PathVariable("itemId") Integer itemId,
                                             MultipartHttpServletRequest request) {
        return itemFileService.uploadItemFiles(itemId, request.getFileMap());
    }

    @RequestMapping(value = "/{itemId}/files/{id}", method = RequestMethod.PUT)
    public PDMItemFile update(@PathVariable("itemId") Integer itemId, @PathVariable("id") Integer id, @RequestBody PDMItemFile pdmItemFile) {
        pdmItemFile.setId(id);
        return itemFileService.update(pdmItemFile);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("fileId") Integer fileId) {
        itemFileService.delete(fileId);
    }

    @RequestMapping(value = "/{itemId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteItemFile(@PathVariable("itemId") Integer itemId, @PathVariable("fileId") Integer fileId) {
        itemFileService.deleteItemFile(itemId,fileId);
    }

    @RequestMapping(value = "/{itemId}/files/{id}", method = RequestMethod.GET)
    public PDMItemFile get(@PathVariable("itemId") Integer itemId, @PathVariable("id") Integer id) {
        return itemFileService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PDMItemFile> getAll() {
        return itemFileService.getAll();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Page<PDMItemFile> findAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return itemFileService.findAll(pageable);
    }

    @RequestMapping(value = "/{itemId}/files", method = RequestMethod.GET)
    public List<PDMItemFile> getItemFiles(@PathVariable("itemId") Integer itemId) {
        return itemFileService.findByItem(itemId);
    }

    @RequestMapping(value = "{itemId}/files/{fileId}/download", method = RequestMethod.GET)
    public void downloadItemFile(@PathVariable("itemId") Integer itemId,
                                 @PathVariable("fileId") Integer fileId,
                                 HttpServletResponse response) {
        PDMItemFile itemFile = itemFileService.get(fileId);
        File file = itemFileService.getItemFile(itemId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, itemFile.getName(), file);
        }
    }

    @RequestMapping(value = "{itemId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PDMItemFileVersionHistory> getAllFileVersions(@PathVariable("itemId") Integer itemId,
                                                              @PathVariable("fileId") Integer fileId) {
        PDMItemFile file = itemFileService.get(fileId);
        return itemFileService.getAllFileVersions(itemId, file.getName());
    }
}
