package com.cassinisys.plm.controller.pdm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.PDMFileVersionCriteria;
import com.cassinisys.plm.filtering.PDMObjectCriteria;
import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.pdm.dto.AssemblyDTO;
import com.cassinisys.plm.model.pdm.dto.ComponentReference;
import com.cassinisys.plm.service.pdm.PDMService;
import com.cassinisys.plm.service.pdm.PDMVaultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/pdm/vaults")
@Api(tags = "PLM.PDM", description = "PDM Related")
public class PDMVaultController extends BaseController {

    @Autowired
    private PDMVaultService pdmVaultService;

    @Autowired
    private PDMService pdmService;

    @Autowired
    private PageRequestConverter pageRequestConverter;


    @RequestMapping(method = RequestMethod.POST)
    public PDMVault createVault(@RequestBody PDMVault pdmVault) {
        return pdmVaultService.createVault(pdmVault);
    }

    @RequestMapping(value = "/free/search", method = RequestMethod.GET)
    public List<PDMVault> getSearchVaults(PDMObjectCriteria objectCriteria) {
        return pdmVaultService.getSearchVaults(objectCriteria);
    }

    @RequestMapping
    public List<PDMVault> getVaults() {
        return pdmVaultService.getAllVaults();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<PDMVault> getVaultsByPageable(PageRequest pageRequest, PDMObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.getVaultsByPageable(pageable, objectCriteria);
    }

    @RequestMapping(value = "/{id}")
    public PDMVault getVault(@PathVariable Integer id) {
        return pdmVaultService.getVault(id);
    }

    @RequestMapping(value = "/multiple/[{ids}]")
    public List<PDMVault> getMultipleVaults(@PathVariable Integer[] ids) {
        return pdmVaultService.getMultipleVaults(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PDMVault updateVault(@PathVariable Integer id, @RequestBody PDMVault pdmVault) {
        return pdmVaultService.updateVault(pdmVault);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteVault(@PathVariable Integer id) {
        pdmVaultService.deleteVault(id);
    }

    @RequestMapping(value = "/{vaultId}/folders", method = RequestMethod.POST)
    public PDMFolder createFolder(@PathVariable Integer vaultId, @RequestBody PDMFolder pdmFolder) {
        return pdmVaultService.createRootFolder(vaultId, pdmFolder);
    }

    @RequestMapping(value = "/{vaultId}/folders/path", method = RequestMethod.POST)
    public PDMFolder createFolderByPath(@PathVariable Integer vaultId, @RequestBody PDMFolder pdmFolder) {
        return pdmVaultService.createFolderByPath(vaultId, pdmFolder.getNamePath());
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}", method = RequestMethod.POST)
    public PDMFolder createFolder(@PathVariable Integer vaultId, @PathVariable Integer folderId, @RequestBody PDMFolder pdmFolder) {
        return pdmVaultService.createChildFolder(folderId, pdmFolder);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}", method = RequestMethod.PUT)
    public PDMFolder updateFolder(@PathVariable Integer vaultId, @PathVariable Integer folderId, @RequestBody PDMFolder pdmFolder) {
        return pdmVaultService.updateFolder(pdmFolder);
    }

    @RequestMapping(value = "/{vaultId}/folders")
    public List<PDMFolder> getRootFolders(@PathVariable Integer vaultId) {
        return pdmVaultService.getRootFolders(vaultId);
    }

    @RequestMapping(value = "/{vaultId}/children")
    public Page<PDMVaultObject> getVaultChildrenByPath(@PathVariable Integer vaultId,
                                                       PageRequest pageRequest, PDMObjectCriteria objectCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.getVaultChildrenByPath(vaultId, pageable, objectCriteria);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}")
    public PDMFolder getFolder(@PathVariable Integer vaultId, @PathVariable Integer folderId) {
        return pdmVaultService.getFolder(folderId);
    }

    @RequestMapping(value = "/{vaultId}/folders/byname")
    public PDMFolder getFolderByName(@PathVariable Integer vaultId, @RequestParam String folderName) {
        return pdmVaultService.getRootFolderByName(vaultId, folderName);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/byname")
    public PDMFolder getFolderByNameInParent(@PathVariable Integer vaultId, @PathVariable Integer folderId, @RequestParam String folderName) {
        return pdmVaultService.getFolderByNameInParent(folderId, folderName);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/children")
    public List<PDMFolder> getFolderChildren(@PathVariable Integer vaultId, @PathVariable Integer folderId) {
        return pdmVaultService.getFolderChildren(folderId);
    }

    @RequestMapping(value = "/{vaultId}/folders/multiple/[{folderIds}]")
    public List<PDMFolder> getMultipleFolders(@PathVariable Integer vaultId, @PathVariable Integer[] folderIds) {
        return pdmVaultService.getMultipleFolders(Arrays.asList(folderIds));
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}", method = RequestMethod.DELETE)
    public void deleteFolder(@PathVariable Integer vaultId, @PathVariable Integer folderId) {
        pdmVaultService.deleteFolder(folderId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/versions/{fileVersionId}", method = RequestMethod.GET)
    public PDMFileVersion getFileVersion(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getFileVersion(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/bom", method = RequestMethod.GET)
    public AssemblyDTO getAssemblyBom(@PathVariable Integer vaultId,
                                      @PathVariable Integer folderId,
                                      @PathVariable Integer fileVersionId) {
        return pdmService.getAssemblyBom(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files", method = RequestMethod.POST)
    public List<PDMFileVersion> addFilesToFolder(@PathVariable Integer vaultId, @PathVariable Integer folderId,
                                                 @RequestParam Integer commitId, MultipartHttpServletRequest request) {
        return pdmVaultService.addFilesToFolder(commitId, folderId, request);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileId}", method = RequestMethod.POST)
    public PDMFileVersion updateFile(@PathVariable Integer vaultId, @PathVariable Integer folderId,
                                     @PathVariable Integer fileId,
                                     @RequestParam Integer commitId, MultipartHttpServletRequest request) {
        return pdmVaultService.updateFile(folderId, fileId, commitId, request);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileId}/thumbnail", method = RequestMethod.POST)
    public Boolean addThumbnail(@PathVariable Integer vaultId,
                                @PathVariable Integer folderId,
                                @PathVariable Integer fileId,
                                MultipartHttpServletRequest request) {
        return pdmVaultService.addThumbnail(fileId, request);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileId}/thumbnail")
    public void getThumbnail(@PathVariable Integer vaultId,
                             @PathVariable Integer folderId,
                             @PathVariable Integer fileId,
                             HttpServletResponse response) {
        pdmVaultService.getThumbnail(fileId, response);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files")
    public List<PDMFileVersion> getFolderFiles(@PathVariable Integer vaultId, @PathVariable Integer folderId) {
        return pdmVaultService.getFolderFiles(folderId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/paginated")
    public Page<PDMFileVersion> getFolderFiles(@PathVariable Integer vaultId, @PathVariable Integer folderId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.getFolderFiles(folderId, pageable);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/attachedto")
    public PDMRevisionedObject getAttachedToObject(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getAttachedToObject(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/attachedto/multiple/[{fileVersionIds}]")
    public List<PDMRevisionedObject> getAttachedToObjects(@PathVariable Integer[] fileVersionIds) {
        return pdmVaultService.getAttachedToObjects(Arrays.asList(fileVersionIds));
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/assembly")
    public PDMAssembly getAttachedToAssembly(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getAttachedToAssembly(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/download")
    public void downloadFile(@PathVariable Integer fileVersionId, HttpServletResponse response) {
        pdmVaultService.downloadFile(fileVersionId, response);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/part")
    public PDMPart getAttachedToPart(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getAttachedToPart(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/drawing")
    public PDMDrawing getAttachedToDrawing(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getAttachedToDrawing(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileMasterId}/versions")
    public List<PDMFileVersion> getFileVersions(@PathVariable Integer vaultId, @PathVariable Integer folderId, @PathVariable Integer fileMasterId) {
        return pdmVaultService.getFileVersions(fileMasterId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/visualization")
    public PDMFileVersion getVisualizationId(@PathVariable Integer fileVersionId) {
        return pdmVaultService.generateVisualization(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/commits", method = RequestMethod.POST)
    public PDMCommit createCommit(@PathVariable Integer vaultId, @RequestBody PDMCommit commit) {
        commit.setVault(vaultId);
        return pdmVaultService.createCommit(commit);
    }

    @RequestMapping(value = "/{vaultId}/commits", method = RequestMethod.GET)
    public Page<PDMCommit> getCommits(@PathVariable Integer vaultId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.getCommits(vaultId, pageable);
    }

    @RequestMapping(value = "/{vaultId}/commits/{commitId}", method = RequestMethod.GET)
    public PDMCommit getCommit(@PathVariable Integer vaultId, @PathVariable Integer commitId) {
        return pdmVaultService.getCommit(commitId);
    }

    @RequestMapping(value = "/{vaultId}/commits/{commitId}/files", method = RequestMethod.GET)
    public List<PDMFileVersion> getCommitFiles(@PathVariable Integer vaultId, @PathVariable Integer commitId) {
        return pdmVaultService.getCommitFiles(commitId);
    }

    @RequestMapping(value = "/{vaultId}/commits/multiple/[{ids}]")
    public List<PDMCommit> getMultipleAssemblies(@PathVariable Integer[] ids) {
        return pdmVaultService.getCommits(Arrays.asList(ids));
    }

    @RequestMapping(value = "/search")
    public Page<PDMFileVersion> searchFiles(PDMFileVersionCriteria criteria, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.searchFiles(criteria, pageable);
    }

    @RequestMapping(value = "/files/type/{fileType}")
    public Page<PDMFileVersion> getFilesByType(@PathVariable("fileType") PDMFileType fileType, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return pdmVaultService.getFilesByType(fileType, pageable);
    }

    @RequestMapping(value = "/files/{fileVersionId}/references", method = RequestMethod.POST)
    public PDMFileVersion saveComponentReferences(@PathVariable Integer fileVersionId, @RequestBody List<ComponentReference> references) {
        return pdmVaultService.saveComponentReferences(fileVersionId, references);
    }

    @RequestMapping(value = "/files/{fileVersionId}/references", method = RequestMethod.GET)
    public List<ComponentReference> getComponentReferences(@PathVariable Integer fileVersionId) {
        return pdmVaultService.getComponentReferences(fileVersionId);
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/drawing", method = RequestMethod.POST)
    public PDMFileVersion addDrawing(@PathVariable("fileVersionId") Integer fileVersionId, MultipartHttpServletRequest request) {
        List<MultipartFile> files = new ArrayList<>(request.getFileMap().values());
        return pdmVaultService.addDrawing(fileVersionId, files.get(0));
    }

    @RequestMapping(value = "/{vaultId}/folders/{folderId}/files/{fileVersionId}/pdf", method = RequestMethod.POST)
    public Attachment addDrawingPDF(@PathVariable("fileVersionId") Integer fileVersionId, MultipartHttpServletRequest request) {
        List<MultipartFile> files = new ArrayList<>(request.getFileMap().values());
        return pdmVaultService.addDrawingPDF(fileVersionId, files.get(0));
    }

}
