package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.filtering.FileCriteria;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.PDMFileAttribute;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.service.PDMFileService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Api(name = "File", description = "File endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/vaults/folders/{folderId}/files")
public class PDMFileController extends BaseController {

	@Autowired
	private PDMFileService fileService;

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private FileDownloadService fileDownloadService;
	@Autowired
	private PDMFileRepository fileRepository;

	@RequestMapping(method = RequestMethod.POST)
	public List<PDMFile> uploadFiles(@RequestParam("commit") Optional<Integer> commit,
									 @PathVariable("folderId") Integer folderId,
									 MultipartHttpServletRequest request) {
		Integer commitId = null;
		if(commit.isPresent()) {
			commitId = commit.get();
		}
		return fileService.uploadFiles(commitId, folderId, request.getFileMap());
	}

	@RequestMapping(value = "/{fileId}/attribute", method = RequestMethod.POST)
	public PDMFileAttribute create(@PathVariable("fileId") Integer fileId,
	                               @RequestBody PDMFileAttribute fileAttribute) {
		fileAttribute.setId(null);
		return fileService.createAttribute(fileAttribute);
	}

	@RequestMapping(value = "/{fileId}/thumbnail", method = RequestMethod.POST)
	public Boolean uploadThumbnail(@PathVariable("folderId") Integer folderId,
										   @PathVariable("fileId") Integer fileId,
										   MultipartHttpServletRequest request) {
		return fileService.setThumbnail(fileId, request);
	}

	@RequestMapping(value = "/{fileId}/thumbnail", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable("folderId") Integer folderId,
							   @PathVariable("fileId") Integer fileId,
							   HttpServletRequest request, HttpServletResponse response) {
		fileService.getThumbnail(fileId, response);
	}


	@RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
	public PDMFile update(@PathVariable("folderId") Integer folderId,
	                      @PathVariable("fileId") Integer fileId,
	                      @RequestBody PDMFile pdmFile) {
		pdmFile.setId(fileId);
		return fileService.update(pdmFile);
	}

	@RequestMapping(value = "/{fileId}/attribute", method = RequestMethod.PUT)
	public PDMFileAttribute update(@PathVariable("fileId") ObjectAttributeId fileId,
	                               @RequestBody PDMFileAttribute fileAttribute) {
		fileAttribute.setId(fileId);
		return fileService.updateAttribute(fileAttribute);
	}

	@RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("fileId") Integer fileId) {
		fileService.delete(fileId);
	}

	@RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
	public PDMFile get(@PathVariable("fileId") Integer fileId) {
		return fileService.get(fileId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<PDMFile> getAll(@PathVariable("folderId") Integer folderId) {
		return fileService.getByFolder(folderId);
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public Page<PDMFile> findAll(@PathVariable("folderId") Integer folderId, PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return fileService.findAll(pageable);
	}

	@RequestMapping(value = "/latest", method = RequestMethod.GET)
	public List<PDMFile> findAllTest() {
		return fileRepository.findByLatestTrueOrderByModifiedDateDesc();
	}

	@RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("folderId") Integer folderId,
	                         @PathVariable("fileId") Integer fileId,
	                         HttpServletResponse response) {
		PDMFile pdmFile = fileService.get(fileId);
		File file = fileService.getFolderFile(folderId, fileId);
		if (file != null) {
			fileDownloadService.writeFileContentToResponse(response, pdmFile.getName(), file);
		}
	}

	@RequestMapping(value = "/{fromFileId}/move/{toFolderId}", method = RequestMethod.PUT)
	public PDMFile moveFile(@PathVariable("fromFileId") Integer fromFileId,
	                        @PathVariable("toFolderId") Integer toFolderId, @RequestBody PDMFile pdmFile) throws Exception {
		return fileService.moveFile(fromFileId, toFolderId, pdmFile);

	}

	@RequestMapping(value = "/{fileId}/versions", method = RequestMethod.GET)
	public List<PDMFile> getAllFileVersions(@PathVariable("folderId") Integer folderId,
	                                        @PathVariable("fileId") Integer fileId) {
		PDMFile file = fileService.get(fileId);
		return fileService.getAllFileVersions(folderId, file.getName());
	}

	@RequestMapping(value = "/freesearch/{freeText}", method = RequestMethod.GET)
	public Page<PDMFile> freeTextSearch(@PathVariable("folderId") Integer folderId,
										@PathVariable("freeText") String freeText,
	                                    PageRequest pageRequest, FileCriteria fileCriteria) {
		if(folderId != null && folderId != -1) {
			fileCriteria.setFolder(folderId);
		}
		fileCriteria.setSearchQuery(freeText);
		fileCriteria.setFreeTextSearch(true);
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		Page<PDMFile> pdmFiles = fileService.freeTextSearch(pageable, fileCriteria);
		return pdmFiles;
	}
}
