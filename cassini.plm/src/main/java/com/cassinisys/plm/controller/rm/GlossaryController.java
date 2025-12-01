package com.cassinisys.plm.controller.rm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.GlossaryCriteria;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.service.rm.GlossaryService;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@RestController
@RequestMapping("/rm/glossarys")
@Api(tags = "PLM.RM",description = "Requirement Related")
public class GlossaryController extends BaseController {

    @Autowired
    private GlossaryService glossaryService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private FileDownloadService fileDownloadService;

    @RequestMapping(method = RequestMethod.POST)
    public PLMGlossary crete(@RequestBody PLMGlossary glossary) {
        return glossaryService.create(glossary);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMGlossary update(@PathVariable("id") Integer id,
                              @RequestBody PLMGlossary glossary) {
        return glossaryService.update(glossary);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        glossaryService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMGlossary get(@PathVariable("id") Integer id) {
        return glossaryService.get(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PLMGlossary> getAll() {
        return glossaryService.getAll();
    }

    @RequestMapping(value = "/getAllGlossarys", method = RequestMethod.GET)
    public Page<PLMGlossary> getAllGlossarys(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return glossaryService.findAll(pageable);
    }

    @RequestMapping(value = "/{glossaryId}/promote", method = RequestMethod.PUT)
    public PLMGlossary promoteGlossary(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.promoteGlossary(glossaryId);
    }

    @RequestMapping(value = "/{glossaryId}/demote", method = RequestMethod.PUT)
    public PLMGlossary demoteGlossary(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.demoteGlossary(glossaryId);
    }

    @RequestMapping(value = "/{glossaryId}/revise", method = RequestMethod.PUT)
    public PLMGlossary reviseGlossary(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.reviseGlossary(glossaryId);
    }

    @RequestMapping(value = "/revisionHistory", method = RequestMethod.POST)
    public PLMGlossaryRevisionHistory createGlossaryRevisionHistory(@RequestBody PLMGlossaryRevisionHistory revisionHistory) {
        return glossaryService.createGlossaryRevisionHistory(revisionHistory);
    }

    @RequestMapping(value = "/revisionHistory/{id}", method = RequestMethod.PUT)
    public PLMGlossaryRevisionHistory updateGlossaryRevisionHistory(@PathVariable("id") Integer id,
                                                                    @RequestBody PLMGlossaryRevisionHistory revisionHistory) {
        return glossaryService.updateGlossaryRevisionHistory(revisionHistory);
    }

    @RequestMapping(value = "/revisionHistory/{id}", method = RequestMethod.GET)
    public PLMGlossaryRevisionHistory getGlossaryRevisionHistory(@PathVariable("id") Integer id) {
        return glossaryService.getGlossaryRevisionHistory(id);
    }

    @RequestMapping(value = "/revisionHistory/all", method = RequestMethod.GET)
    public List<PLMGlossaryRevisionHistory> getAllGlossaryRevisionHistory() {
        return glossaryService.getAllGlossaryRevisionHistory();
    }

    @RequestMapping(value = "{glossaryId}/revisionHistory", method = RequestMethod.GET)
    public List<GlossaryRevisionHistoryDto> getGlossaryRevisionHistories(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.getGlossaryRevisionHistories(glossaryId);
    }

    @RequestMapping(value = "/revisionStatusHistory", method = RequestMethod.POST)
    public PLMGlossaryRevisionStatusHistory createGlossaryRevisionStatusHistory(@RequestBody PLMGlossaryRevisionStatusHistory revisionStatusHistory) {
        return glossaryService.createGlossaryRevisionStatusHistory(revisionStatusHistory);
    }

    @RequestMapping(value = "/revisionStatusHistory/{id}", method = RequestMethod.PUT)
    public PLMGlossaryRevisionStatusHistory updateGlossaryRevisionStatusHistory(@PathVariable("id") Integer id,
                                                                                @RequestBody PLMGlossaryRevisionStatusHistory revisionStatusHistory) {
        return glossaryService.updateGlossaryRevisionStatusHistory(revisionStatusHistory);
    }

    @RequestMapping(value = "/revisionStatusHistory/{id}", method = RequestMethod.GET)
    public PLMGlossaryRevisionStatusHistory getGlossaryRevisionStatusHistory(@PathVariable("id") Integer id) {
        return glossaryService.getGlossaryRevisionStatusHistory(id);
    }

    @RequestMapping(value = "/revisionStatusHistory/all", method = RequestMethod.GET)
    public List<PLMGlossaryRevisionStatusHistory> getAllGlossaryRevisionStatusHistory() {
        return glossaryService.getAllGlossaryRevisionStatusHistory();
    }

    @RequestMapping(value = "/freeTextSearch", method = RequestMethod.GET)
    public Page<PLMGlossary> freeTextSearch(PageRequest pageRequest, GlossaryCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMGlossary> glossaries = glossaryService.freeTextSearch(pageable, criteria);
        return glossaries;
    }

	/*------------------  Glossary files methods ------------------------------*/

    @RequestMapping(value = "/{glossaryId}/files", method = RequestMethod.POST)
    public List<PLMGlossaryFile> uploadFiles(@PathVariable("glossaryId") Integer glossaryId, @RequestParam("folderId") Integer folderId,
                                             MultipartHttpServletRequest request) {
        return glossaryService.uploadGlossaryFiles(glossaryId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{glossaryId}/files", method = RequestMethod.GET)
    public List<PLMGlossaryFile> getGlossaryFiles(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.getGlossaryFiles(glossaryId);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}", method = RequestMethod.DELETE)
    public void deleteFile(@PathVariable("glossaryId") Integer glossaryId,
                           @PathVariable("fileId") Integer fileId) {
        glossaryService.deleteGlossaryFile(glossaryId, fileId);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}", method = RequestMethod.GET)
    public PLMGlossaryFile getFile(@PathVariable("glossaryId") Integer glossaryId,
                                   @PathVariable("fileId") Integer fileId) {
        return glossaryService.getFileById(fileId);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/download/{type}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("glossaryId") Integer glossaryId,
                             @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                             HttpServletResponse response) {
        PLMGlossaryFile glossaryFile = glossaryService.getFileById(fileId);
        File file = glossaryService.getGlossaryFile(glossaryId, fileId, type);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, glossaryFile.getName(), file);
        }
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/preview/{type}", method = RequestMethod.GET)
    public void previewFile(@PathVariable("glossaryId") Integer glossaryId,
                            @PathVariable("fileId") Integer fileId, @PathVariable("type") String type,
                            HttpServletResponse response) throws IOException {
        PLMGlossaryFile glossaryFile = glossaryService.getFileById(fileId);
        String fileName = glossaryFile.getName();
        File file = glossaryService.getGlossaryFile(glossaryId, fileId, type);
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

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/fileDownloadHistory", method = RequestMethod.POST)
    public PLMFileDownloadHistory fileDownloadHistory(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("fileId") Integer fileId) {
        return glossaryService.fileDownloadHistory(glossaryId, fileId);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/versions", method = RequestMethod.GET)
    public List<PLMGlossaryFile> getGlossaryFileVersions(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("fileId") Integer fileId) {
        return glossaryService.getGlossaryFileVersions(glossaryId, fileId);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/versionComments/{objectType}", method = RequestMethod.GET)
    public List<PLMGlossaryFile> getGlossaryFileVersionsAndCommentsAndDownloads(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("fileId") Integer fileId,
                                                                                @PathVariable("objectType") ObjectType objectType) {
        return glossaryService.getGlossaryFileVersionsAndCommentsAndDownloads(glossaryId, fileId, objectType);
    }

    /*-----------------  Glossary Entry Items Methods  --------------------*/

    @RequestMapping(value = "/{glossaryId}/entryItems", method = RequestMethod.GET)
    public List<PLMGlossaryEntryItem> getGlossaryEntryItems(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.getGlossaryEntryItems(glossaryId);
    }

    @RequestMapping(value = "/{glossaryId}/entryItems/multiple", method = RequestMethod.POST)
    public List<PLMGlossaryEntryItem> saveGlossaryEntryItems(@PathVariable("glossaryId") Integer glossaryId,
                                                             @RequestBody List<PLMGlossaryEntryItem> entryItems) {
        return glossaryService.saveGlossaryEntryItems(glossaryId, entryItems);
    }

    @RequestMapping(value = "/{glossaryId}/entryItems/{entryItem}", method = RequestMethod.DELETE)
    public void deleteGlossaryEntryItem(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("entryItem") Integer entryItem) {
        glossaryService.deleteGlossaryEntryItem(glossaryId, entryItem);
    }

    @RequestMapping(value = "/{glossaryId}/import", method = RequestMethod.POST)
    public ImportMessageDto importGlossaryEntryItems(@PathVariable("glossaryId") Integer glossaryId,
                                                     @RequestParam("importComment") String importComment,
                                                     MultipartHttpServletRequest request) throws Exception {
        return glossaryService.importGlossaryEntryItems(glossaryId, importComment, request.getFileMap());
    }

    /*------------------- PrintGlossary -------------------*/

    @RequestMapping(value = "/printGlossary/{id}", method = RequestMethod.GET, produces = "text/html")
    public String printGlossary(@PathVariable("id") Integer id, HttpServletResponse response) {
        String fileName = glossaryService.printGlossary(id, response);
        return fileName;
    }

    @RequestMapping(value = "/printGlossary/{id}/language/{language}", method = RequestMethod.GET, produces = "text/html")
    public String printGlossaryByLanguage(@PathVariable("id") Integer id, @PathVariable("language") String language, HttpServletResponse response) {
        String fileName = glossaryService.printGlossaryByLanguage(id, language, response);
        return fileName;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/file/{fileId}/download")
    public void downloadExportFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        glossaryService.downloadExportFile(fileId, response);

    }

    @RequestMapping(value = "{glossaryId}/files/zip", method = RequestMethod.GET, produces = "application/zip")
    public void generateZip(@PathVariable("glossaryId") Integer glossaryId, HttpServletResponse response) throws FileNotFoundException, IOException {
        glossaryService.generateZipFile(glossaryId, response);
    }

    /*--------------   Glossary Languages Methods  -----------------------*/

    @RequestMapping(value = "/languages", method = RequestMethod.POST)
    public PLMGlossaryLanguages createGlossaryLanguage(@RequestBody PLMGlossaryLanguages glossaryLanguage) {
        return glossaryService.createGlossaryLanguage(glossaryLanguage);
    }

    @RequestMapping(value = "/languages/{languageId}", method = RequestMethod.PUT)
    public PLMGlossaryLanguages updateGlossaryLanguage(@PathVariable("languageId") Integer languageId, @RequestBody PLMGlossaryLanguages glossaryLanguage) {
        return glossaryService.updateGlossaryLanguage(languageId, glossaryLanguage);
    }

    @RequestMapping(value = "/languages/default/{languageId}", method = RequestMethod.PUT)
    public PLMGlossaryLanguages setGlossaryLanguage(@PathVariable("languageId") Integer languageId,
                                                    @RequestBody PLMGlossaryLanguages glossaryLanguage) {
        return glossaryService.setGlossaryLanguage(languageId, glossaryLanguage);
    }

    @RequestMapping(value = "/languages/{languageId}", method = RequestMethod.DELETE)
    public void deleteGlossaryLanguage(@PathVariable("languageId") Integer languageId) {
        glossaryService.deleteGlossaryLanguage(languageId);
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public List<PLMGlossaryLanguages> getGlossaryLanguages() {
        return glossaryService.getGlossaryLanguages();
    }

    /*--------------  Glossary Details Methods  -----------------*/
    @RequestMapping(value = "/details/{detailId}", method = RequestMethod.PUT)
    public PLMGlossaryDetails updateGlossaryDetail(@PathVariable("detailId") Integer detailId, @RequestBody PLMGlossaryDetails plmGlossaryDetail) {
        return glossaryService.updateGlossaryDetail(detailId, plmGlossaryDetail);
    }

    @RequestMapping(value = "/{glossaryId}/glossaryDetails/{language}", method = RequestMethod.GET)
    public GlossaryDto getGlossaryDetails(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("language") String language) {
        return glossaryService.getGlossaryDetails(glossaryId, language);
    }

    @RequestMapping(value = "/{glossaryId}/glossaryDetails/{language}/entrySearch/{searchTerm}", method = RequestMethod.GET)
    public GlossaryDto getGlossaryEntrySearchDetails(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("language") String language,
                                                     @PathVariable("searchTerm") String searchTerm) {
        return glossaryService.getGlossaryEntrySearchDetails(glossaryId, language, searchTerm);
    }

    @RequestMapping(value = "/{glossaryId}/glossaryEntrySearch/{searchTerm}/language/{language}", method = RequestMethod.GET)
    public GlossaryDto getGlossaryEntrySearch(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("searchTerm") String searchTerm, @PathVariable("language") String language) {
        return glossaryService.getGlossaryEntrySearch(glossaryId, searchTerm, language);
    }

    /*----------------------- Glossary Permissions -------------*/

    @RequestMapping(value = "/{glossaryId}/permission/add", method = RequestMethod.POST)
    public List<Person> createGlossaryPersons(@PathVariable("glossaryId") Integer glossaryId,
                                              @RequestBody List<Person> persons) {
        return glossaryService.createGlossaryPersons(glossaryId, persons);
    }

    @RequestMapping(value = "/{glossaryId}/glossaryPersons", method = RequestMethod.GET)
    public List<GlossaryEntryPermission> getAllGlossaryPersons(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.getAllGlossaryPersons(glossaryId);
    }

    @RequestMapping(value = "/{glossaryId}/persons", method = RequestMethod.GET)
    public List<Login> getAllPersons(@PathVariable("glossaryId") Integer glossaryId) {
        return glossaryService.getAllPersons(glossaryId);
    }

    @RequestMapping(value = "/glossaryPermission", method = RequestMethod.POST)
    public GlossaryEntryPermission createGlossaryPermission(@RequestBody GlossaryEntryPermission glossaryEntryPermission) {
        return glossaryService.createGlossaryPermission(glossaryEntryPermission);
    }

    @RequestMapping(value = "/delete/glossaryPerson/{glossaryId}", method = RequestMethod.DELETE)
    public void deleteGlossaryPerson(@PathVariable("glossaryId") Integer glossaryId) {
        glossaryService.deleteGlossaryPerson(glossaryId);
    }

    @RequestMapping(value = "/attributes/{objectType}", method = RequestMethod.GET)
    public List<ObjectTypeAttribute> getAllTypeAttributes(@PathVariable("objectType") String objectType) {
        return glossaryService.getAllTypeAttributes(objectType);
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/renameFile", method = RequestMethod.PUT)
    public PLMGlossaryFile renameFile(@PathVariable("glossaryId") Integer id,
                                      @PathVariable("fileId") Integer fileId, @RequestBody String newFileName) throws IOException {
        return glossaryService.updateFileName(fileId, newFileName);
    }

    @RequestMapping(value = "{glossaryId}/files/replaceFile/{fileId}", method = RequestMethod.POST)
    public List<PLMGlossaryFile> replaceGlossaryFiles(@PathVariable("glossaryId") Integer glossaryId,
                                                      @PathVariable("fileId") Integer fileId,
                                                      MultipartHttpServletRequest request) {
        return glossaryService.replaceGlossaryFiles(glossaryId, fileId, request.getFileMap());
    }

    @RequestMapping(value = "/files/{fileId}", method = RequestMethod.PUT)
    public PLMFile updateFile(@PathVariable("fileId") Integer fileId, @RequestBody PLMGlossaryFile glossaryFile) {
        return glossaryService.updateFile(fileId, glossaryFile);
    }

    @RequestMapping(value = "/{glossaryId}/folder", method = RequestMethod.POST)
    public PLMGlossaryFile createGlossaryFolder(@PathVariable("glossaryId") Integer glossaryId, @RequestBody PLMGlossaryFile plmProjectFile) {
        return glossaryService.createGlossaryFolder(glossaryId, plmProjectFile);
    }

    @RequestMapping(value = "/{glossaryId}/folder/{folderId}/upload", method = RequestMethod.POST)
    public List<PLMGlossaryFile> uploadGlossaryFolderFiles(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("folderId") Integer folderId,
                                                           MultipartHttpServletRequest request) {
        return glossaryService.uploadGlossaryFolderFiles(glossaryId, folderId, request.getFileMap());
    }

    @RequestMapping(value = "/{glossaryId}/files/{fileId}/latest/uploaded", method = RequestMethod.GET)
    public PLMGlossaryFile getLatestUploadedFile(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("fileId") Integer fileId) {
        return glossaryService.getLatestUploadedFile(glossaryId, fileId);
    }

    @RequestMapping(value = "/{folderId}/move", method = RequestMethod.PUT)
    public PLMFile moveGlossaryFileToFolder(@PathVariable("folderId") Integer folderId,
                                            @RequestBody PLMGlossaryFile file) throws Exception {
        return glossaryService.moveGlossaryFileToFolder(folderId, file);
    }

    @RequestMapping(value = "/{folderId}/children", method = RequestMethod.GET)
    public List<PLMGlossaryFile> getGlossaryFolderChildren(@PathVariable("folderId") Integer folderId) {
        return glossaryService.getGlossaryFolderChildren(folderId);
    }

    @RequestMapping(value = "/{glossaryId}/folder/{fileId}/delete", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("fileId") Integer fileId) {
        glossaryService.deleteFolder(glossaryId, fileId);
    }

    @RequestMapping(value = "/{glossaryId}/count/{language}", method = RequestMethod.GET)
    public DetailsCount getGlossaryCounts(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("language") String language) {
        return glossaryService.getGlossaryCounts(glossaryId, language);
    }

    @RequestMapping(value = "{glossaryId}/files/paste", method = RequestMethod.PUT)
    public List<PLMGlossaryFile> pasteFromClipboard(@PathVariable("glossaryId") Integer glossaryId, @RequestParam("fileId") Integer fileId,
                                                    @RequestBody List<PLMFile> files) {
        return glossaryService.pasteFromClipboard(glossaryId, fileId, files);
    }

    @RequestMapping(value = "{glossaryId}/files/undo", method = RequestMethod.PUT)
    public void undoCopiedGlossaryFiles(@PathVariable("glossaryId") Integer glossaryId, @RequestBody List<PLMGlossaryFile> glossaryFiles) {
        glossaryService.undoCopiedGlossaryFiles(glossaryId, glossaryFiles);
    }

    @RequestMapping(value = "/{glossaryId}/byName/{name}", method = RequestMethod.GET)
    public List<PLMGlossaryFile> getByGlossaryAndFileName(@PathVariable("glossaryId") Integer glossaryId,
                                                          @PathVariable("name") String name) {
        return glossaryService.findByGlossaryAndFileName(glossaryId, name);
    }

}
