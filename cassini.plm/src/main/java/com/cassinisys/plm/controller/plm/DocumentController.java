package com.cassinisys.plm.controller.plm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.security.DMFolderPermission;
import com.cassinisys.plm.filtering.DocumentCriteria;
import com.cassinisys.plm.model.plm.PLMDocument;
import com.cassinisys.plm.model.plm.PLMDocumentReviewer;
import com.cassinisys.plm.model.plm.PLMObjectDocument;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.service.plm.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam on 18-09-2021.
 */
@RestController
@RequestMapping("plm/documents")
public class DocumentController extends BaseController {
    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/folders/tree", method = RequestMethod.GET)
    public List<PLMDocument> getDocumentFolderTree() {
        return documentService.getDocumentFolderTree();
    }

    @RequestMapping(value = "/folders/{object}/{objectType}/{folder}/tree", method = RequestMethod.GET)
    public List<PLMDocument> getObjectDocumentFolderTree(@PathVariable("object") Integer object, @PathVariable("objectType") PLMObjectType objectType,
                                                         @PathVariable("folder") Integer folder) {
        return documentService.getObjectDocumentFolderTree(object, objectType, folder);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PLMDocument> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @RequestMapping(value = "/filtered", method = RequestMethod.GET)
    public List<PLMDocument> getFilteredDocuments(DocumentCriteria documentCriteria) {
        return documentService.getFilteredDocuments(documentCriteria);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMDocument getDocument(@PathVariable("id") Integer id) {
        return documentService.get(id);
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
    public List<PLMDocument> getDocumentChildren(@PathVariable("id") Integer id) {
        return documentService.getDocumentChildren(id);
    }

    @RequestMapping(value = "/files/count", method = RequestMethod.GET)
    public Integer getTotalDocumentsCount() {
        return documentService.getTotalDocumentsCount();
    }

    @RequestMapping(value = "/folders/{folder}/files/count", method = RequestMethod.GET)
    public Integer getFolderDocumentsCount(@PathVariable("folder") Integer folder) {
        return documentService.getFolderDocumentsCount(folder);
    }

    @RequestMapping(value = "/folders", method = RequestMethod.POST)
    public PLMDocument createDocumentFolder(@RequestBody PLMDocument document) {
        return documentService.createDocumentFolder(document);
    }

    @RequestMapping(value = "/object/multiple", method = RequestMethod.POST)
    public List<FileDto> createMultipleDocuments(@RequestBody List<PLMObjectDocument> objectDocuments) {
        return documentService.createMultipleDocuments(objectDocuments);
    }

    @RequestMapping(value = "/object/document/{fileId}/latest", method = RequestMethod.GET)
    public FileDto updateObjectDocumentToLatest(@PathVariable("fileId") Integer fileId) {
        return documentService.updateObjectDocumentToLatest(fileId);
    }

    @RequestMapping(value = "/{fileId}/promote", method = RequestMethod.PUT)
    public FileDto promoteDocument(@PathVariable("fileId") Integer fileId, @RequestBody FileDto fileDto) {
        return documentService.promoteDocument(fileId, fileDto);
    }

    @RequestMapping(value = "/{fileId}/demote", method = RequestMethod.PUT)
    public FileDto demoteDocument(@PathVariable("fileId") Integer fileId, @RequestBody FileDto fileDto) {
        return documentService.demoteDocument(fileId, fileDto);
    }

    @RequestMapping(value = "/{fileId}/revise", method = RequestMethod.PUT)
    public FileDto reviseDocument(@PathVariable("fileId") Integer fileId, @RequestBody FileDto fileDto) {
        return documentService.reviseDocument(fileId, fileDto);
    }

    @RequestMapping(value = "/folders/{id}", method = RequestMethod.PUT)
    public PLMDocument updateDocumentFolder(@PathVariable("id") Integer id, @RequestBody PLMDocument document) {
        return documentService.updateDocumentFolder(document);
    }

    @RequestMapping(value = "/folders/{id}", method = RequestMethod.DELETE)
    public void deleteDocumentFolder(@PathVariable("id") Integer id) {
        documentService.deleteDocumentFolder(id);
    }

    @RequestMapping(value = "/object/document/{id}", method = RequestMethod.DELETE)
    public void deleteObjectDocument(@PathVariable("id") Integer id) {
        documentService.deleteObjectDocument(id);
    }


    @RequestMapping(value = "/{id}/reviewers", method = RequestMethod.POST)
    public PLMDocumentReviewer addReviewer(@PathVariable("id") Integer id, @RequestBody PLMDocumentReviewer reviewer) {
        return documentService.addReviewer(id, reviewer);
    }

    @RequestMapping(value = "/{id}/reviewers/{reviewerId}", method = RequestMethod.PUT)
    public PLMDocumentReviewer updateReviewer(@PathVariable("id") Integer id, @RequestBody PLMDocumentReviewer reviewer) {
        return documentService.updateReviewer(id, reviewer);
    }

    @RequestMapping(value = "/{id}/reviewers/{reviewerId}", method = RequestMethod.DELETE)
    public void deleteReviewer(@PathVariable("id") Integer id, @PathVariable("reviewerId") Integer reviewerId) {
        documentService.deleteReviewer(id, reviewerId);
    }

    @RequestMapping(value = "/{id}/reviewers", method = RequestMethod.GET)
    public List<PLMDocumentReviewer> getReviewers(@PathVariable("id") Integer id) {
        return documentService.getReviewers(id);
    }

    @RequestMapping(value = "/{id}/reviewer/submit", method = RequestMethod.PUT)
    public PLMDocumentReviewer submitReview(@PathVariable("id") Integer id, @RequestBody PLMDocumentReviewer documentReviewer) {
        return documentService.submitReview(id, documentReviewer);
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public List<DMFolderPermission> getDocumentFolderPermissions() {
        return documentService.getDocumentFolderPermissions(getSessionWrapper().getSession().getLogin());
    }

}
