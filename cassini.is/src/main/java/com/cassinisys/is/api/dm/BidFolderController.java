package com.cassinisys.is.api.dm;
/**
 * The Class is for BidFolderController
 **/

import com.cassinisys.is.model.dm.ISBidDocument;
import com.cassinisys.is.model.dm.ISBidFolder;
import com.cassinisys.is.service.dm.BidFolderService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Api(name = "Bid folders", description = "Bid folders endpoint", group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/folders")
public class BidFolderController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidFolderService bidFolderService;

    /**
     * The method used for creating the IsBidFolder
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidFolder create(@PathVariable("bidId") Integer bidId,
                              @RequestBody ISBidFolder folder) {
        return bidFolderService.create(folder);
    }

    /**
     * The method used for updating the IsBidFolder
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.PUT)
    public ISBidFolder update(@PathVariable("bidId") Integer bidId,
                              @PathVariable("folderId") Integer folderId,
                              @RequestBody ISBidFolder folder) {
        folder.setId(folderId);
        return bidFolderService.update(folder);
    }

    /**
     * The method used for deleting the IsBidFolder
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("folderId") Integer folderId) {
        bidFolderService.delete(folderId);
    }

    /**
     * The method used to get(or obtain) the IsBidFolder
     **/
    @RequestMapping(value = "/{folderId}", method = RequestMethod.GET)
    public ISBidFolder get(@PathVariable("bidId") Integer bidId,
                           @PathVariable("folderId") Integer folderId) {
        return bidFolderService.get(folderId);
    }

    /**
     * The method is used to getChildren of the IsBidFolder
     **/
    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public Page<ISBidFolder> getChildren(@PathVariable("bidId") Integer bidId,
                                         @PathVariable("folderId") Integer folderId,
                                         PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidFolderService.getChildren(folderId, pageable);
    }

    /**
     * The method used to getFolderDocuments of ISBidDocument
     **/
    @RequestMapping(value = "/{folderId}/documents", method = RequestMethod.GET)
    public Page<ISBidDocument> getFolderDocuments(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("folderId") Integer folderId,
            PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return bidFolderService.getDocuments(folderId, pageable);
    }

    /**
     * The method used to uploadDocuments of ISBidDocument
     **/
    @RequestMapping(value = "/{folderId}/documents",
            method = RequestMethod.POST)
    public List<ISBidDocument> uploadDocuments(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("folderId") Integer folderId,
            MultipartHttpServletRequest request) {
        return bidFolderService.uploadDocuments(folderId, request.getFileMap());
    }

    /**
     * The method used to getDocument of ISBidDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}",
            method = RequestMethod.GET)
    public ISBidDocument getDocument(@PathVariable("bidId") Integer bidId,
                                     @PathVariable("folderId") Integer folderId,
                                     @PathVariable("documentId") Integer documentId) {
        return bidFolderService.getDocument(folderId, documentId);
    }

    /**
     * The method used to getMultipleDocuments of ISBidDocument
     **/
    @RequestMapping(value = "/multiple/documents/[{ids}]", method = RequestMethod.GET)
    public List<ISBidDocument> getMultipleDocuments(@PathVariable Integer[] ids) {
        return bidFolderService.findMultipleDocuments(Arrays.asList(ids));
    }

    /**
     * The method used to getMultipleFolders of ISBidDocument
     **/
    @RequestMapping(value = "/multiple/folders/[{ids}]", method = RequestMethod.GET)
    public List<ISBidFolder> getMultipleFolders(@PathVariable Integer[] ids) {
        return bidFolderService.findMultipleFolders(Arrays.asList(ids));
    }

    /**
     * The method used to getDocumentVersions of ISBidDocument
     **/
    @RequestMapping(value = "/{folderId}/documents/{documentId}/versions",
            method = RequestMethod.GET)
    public List<ISBidDocument> getDocumentVersions(
            @PathVariable("bidId") Integer bidId,
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId,
            MultipartHttpServletRequest request) {
        return bidFolderService.getDocumentVersions(folderId, documentId);
    }
}
