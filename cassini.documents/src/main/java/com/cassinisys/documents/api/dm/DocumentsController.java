package com.cassinisys.documents.api.dm;

import com.cassinisys.documents.model.dm.DMDocument;
import com.cassinisys.documents.service.DocumentsService;
import com.cassinisys.platform.api.core.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by swapna on 04/12/18.
 */
@RestController
@RequestMapping("dm/documents")
public class DocumentsController extends BaseController {

    @Autowired
    private DocumentsService documentService;

    @RequestMapping(method = RequestMethod.POST)
    public DMDocument create(@RequestBody DMDocument item) {
        return documentService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DMDocument update(@PathVariable("id") Integer id, @RequestBody DMDocument item) {
        item.setId(id);
        return documentService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        documentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DMDocument get(@PathVariable("id") Integer id) {
        return documentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<DMDocument> get() {
        return documentService.getAll();
    }

    @RequestMapping(value = "/byFolder/{folderId}", method = RequestMethod.GET)
    public List<DMDocument> getDocumentsByFolder(@PathVariable("folderId") Integer folderId) {
        return documentService.getDocumentsByFolder(folderId);
    }

    @RequestMapping(value = "/{documentId}/folder/{folderId}/versions", method = RequestMethod.GET)
    public List<DMDocument> getDocumentVersions(
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId) {
        return documentService.getDocumentVersions(folderId, documentId);
    }
}