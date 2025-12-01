package com.cassinisys.pdm.service;

import com.cassinisys.pdm.filtering.VaultCriteria;
import com.cassinisys.pdm.filtering.VaultPredicateBuilder;
import com.cassinisys.pdm.model.*;
import com.cassinisys.pdm.model.QPDMVault;
import com.cassinisys.pdm.repo.CommitRepository;
import com.cassinisys.pdm.repo.FolderRepository;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.repo.VaultRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMVaultService implements CrudService<PDMVault, Integer> {

	@Autowired
	private VaultRepository vaultRepository;

	@Autowired
	private PDMFileRepository pdmFileRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private VaultPredicateBuilder predicateBuilder;

	@Autowired
	private CommitRepository commitRepository;

	@Override
	public PDMVault create(PDMVault pdmVault) {
		checkNotNull(pdmVault);
		return vaultRepository.save(pdmVault);
	}

	@Override
	public PDMVault update(PDMVault pdmVault) {
		checkNotNull(pdmVault);
		return vaultRepository.save(pdmVault);
	}

	@Override
	public void delete(Integer id) {
		checkNotNull(id);
		vaultRepository.delete(id);
	}

	@Override
	public PDMVault get(Integer id) {
		checkNotNull(id);
		PDMVault pdmVault = vaultRepository.findOne(id);
		if (pdmVault == null) {
			throw new ResourceNotFoundException();
		}
		return vaultRepository.findOne(id);
	}

	@Override
	public List<PDMVault> getAll() {
		return vaultRepository.findAll();
	}

	public List<PDMCommit> getAllCommits() {
		return commitRepository.findAll();
	}

	public Page<PDMVault> findAll(Pageable pageable) {
		return vaultRepository.findAll(pageable);
	}

	public PDMFolder findFolderbypath(String path) {
		return folderRepository.findByPath(path);
	}

	public PDMFile findFilebypath(String path) {
		return pdmFileRepository.findByPath(path);
	}

	public Page<PDMVault> freeTextSearch(Pageable pageable, VaultCriteria vaultCriteria) {
		Predicate predicate = predicateBuilder.build(vaultCriteria, QPDMVault.pDMVault);
		return vaultRepository.findAll(predicate, pageable);
	}

	public List<PDMFile> getCommits(Integer commitId) {
		List<PDMFile> files = pdmFileRepository.findAllByCommitOrderByCreatedDateDesc(commitId);
		return files;
	}


	public PDMCommit createCommit(PDMCommit commit) {
		return commitRepository.save(commit);
	}
}
