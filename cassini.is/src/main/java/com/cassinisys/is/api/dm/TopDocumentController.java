package com.cassinisys.is.api.dm;

import com.cassinisys.is.model.dm.ISTopDocument;
import com.cassinisys.is.service.dm.TopDocumentService;
import com.cassinisys.is.service.dm.TopFolderService;
import com.cassinisys.platform.api.core.BaseController;
import org.apache.commons.io.IOUtils;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by subramanyamreddy on 018 18-Nov -17.
 */
@RestController
@RequestMapping("is/documents")
@Api(name = "Top Documents", description = "Top Documents endpoint", group = "Document")
public class TopDocumentController extends BaseController {

    @Autowired
    private TopDocumentService topDocumentService;

    @Autowired
    private TopFolderService topFolderService;

    @RequestMapping(method = RequestMethod.POST)
    public ISTopDocument create(@RequestBody ISTopDocument item) {
        return topDocumentService.create(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ISTopDocument update(@PathVariable("id") Integer id, @RequestBody ISTopDocument item) {
        item.setId(id);
        return topDocumentService.update(item);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        topDocumentService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISTopDocument get(@PathVariable("id") Integer id) {
        return topDocumentService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISTopDocument> get() {
        return topDocumentService.getAll();
    }

    @RequestMapping(value = "/byFolder/{folderId}", method = RequestMethod.GET)
    public List<ISTopDocument> getDocumentsByFolder(@PathVariable("folderId") Integer folderId) {
        return topDocumentService.getDocumentsByFolder(folderId);
    }

    @RequestMapping(value = "/{documentId}/folder/{folderId}/versions", method = RequestMethod.GET)
    public List<ISTopDocument> getDocumentVersions(
            @PathVariable("folderId") Integer folderId,
            @PathVariable("documentId") Integer documentId) {
        return topDocumentService.getDocumentVersions(folderId, documentId);
    }

    @RequestMapping(value = "/files/{fileId}/preview", method = RequestMethod.GET)
    public void previewItemFile(@PathVariable("fileId") Integer fileId,
                                HttpServletResponse response) throws Exception {
        ISTopDocument isTopDocument = topDocumentService.get(fileId);
        String fileName = isTopDocument.getName();
        File file = topFolderService.getDocumentFile(isTopDocument.getFolder(), fileId);
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
}
