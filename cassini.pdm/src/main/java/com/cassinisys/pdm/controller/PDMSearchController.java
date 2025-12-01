package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.filtering.FileCriteria;
import com.cassinisys.pdm.filtering.FolderCriteria;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.PDMFolder;
import com.cassinisys.pdm.service.PDMFileService;
import com.cassinisys.pdm.service.PDMFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("pdm/search")
public class PDMSearchController  extends BaseController {

    @Autowired
    private PDMFolderService folderService;

    @Autowired
    private PDMFileService fileService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/folders", method = RequestMethod.GET)
    public Page<PDMFolder> searchFolders(FolderCriteria folderCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return folderService.freeTextSearchAll(pageable, folderCriteria);
    }

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public Page<PDMFile> searchFiles(FileCriteria fileCriteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return fileService.freeTextSearchAll(pageable, fileCriteria);
    }
}
