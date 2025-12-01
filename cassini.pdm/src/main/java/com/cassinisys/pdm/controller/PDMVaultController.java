
package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.filtering.VaultCriteria;
import com.cassinisys.pdm.model.PDMCommit;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.PDMFolder;
import com.cassinisys.pdm.model.PDMVault;
import com.cassinisys.pdm.service.PDMVaultService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */

@Api(name = "Vault", description = "Vault endpoint", group = "PDM")
@RestController
@RequestMapping("pdm/vaults")
public class PDMVaultController extends BaseController {

	@Autowired
	private PDMVaultService vaultService;

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@RequestMapping(method = RequestMethod.POST)
	public PDMVault create(@RequestBody PDMVault pdmVault) {
		pdmVault.setId(null);
		return vaultService.create(pdmVault);
	}

	@RequestMapping(value = "/{vaultId}", method = RequestMethod.PUT)
	public PDMVault update(@PathVariable("vaultId") Integer vaultId, @RequestBody PDMVault pdmVault) {
		pdmVault.setId(vaultId);
		return vaultService.update(pdmVault);
	}

	@RequestMapping(value = "/{vaultId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("vaultId") Integer vaultId) {
		vaultService.delete(vaultId);
	}

	@RequestMapping(value = "/{vaultId}", method = RequestMethod.GET)
	public PDMVault get(@PathVariable("vaultId") Integer vaultId) {
		return vaultService.get(vaultId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<PDMVault> getAll() {
		return vaultService.getAll();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public Page<PDMVault> findAll(PageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return vaultService.findAll(pageable);
	}

	@RequestMapping(value = "/find/folders", method = RequestMethod.GET)
	public PDMFolder getFolderByPath(@RequestParam("path") String path) {
		return vaultService.findFolderbypath(path);
	}

	@RequestMapping(value = "/find/files", method = RequestMethod.GET)
	public PDMFile getFilesByPath(@RequestParam("path") String path) {
		return vaultService.findFilebypath(path);
	}

	@RequestMapping(value = "/freesearch", method = RequestMethod.GET)
	public Page<PDMVault> freeTextSearch(PageRequest pageRequest, VaultCriteria vaultCriteria) {
		vaultCriteria.setFreeTextSearch(true);
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		Page<PDMVault> pdmVaults = vaultService.freeTextSearch(pageable, vaultCriteria);
		return pdmVaults;
	}

	@RequestMapping(value = "/commits/getAllCommits", method = RequestMethod.GET)
	public List<PDMCommit> getAllCommits() {
		return vaultService.getAllCommits();
	}

	@RequestMapping(value = "/commits/{commitId}", method = RequestMethod.GET)
	public List<PDMFile> getCommits(@PathVariable("commitId") Integer commitId) {
		return vaultService.getCommits(commitId);
	}

	@RequestMapping(value = "commits/createCommit", method = RequestMethod.POST)
	public PDMCommit createCommit(@RequestBody PDMCommit commit) {
		return vaultService.createCommit(commit);
	}
}

