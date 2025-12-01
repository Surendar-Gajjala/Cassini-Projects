package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Repository
public interface PDMFileRepository extends JpaRepository<PDMFile, Integer>, QueryDslPredicateExecutor<PDMFile> {

	List<PDMFile> findByFolder(Integer id);

	PDMFile findByPath(String path);

	PDMFile findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(Integer folderId, String name);

	PDMFile findByFolderAndIdAndLatestTrueOrderByModifiedDateDesc(Integer folderId, Integer fileId);

	List<PDMFile> findByFolderAndLatestTrue(Integer folderId);

	List<PDMFile> findByLatestTrueOrderByModifiedDateDesc();

	List<PDMFile> findByFolderAndLatestTrueOrderByModifiedDateDesc(Integer folderId);

	List<PDMFile> findAllByFolderAndNameOrderByTimeStampDesc(Integer folder, String name);

	List<PDMFile> findByFolderAndNameContainingIgnoreCaseAndLatestTrue(Integer folder, String name);

	List<PDMFile> findAllByCommitOrderByCreatedDateDesc(Integer commit);

	PDMFile findByVaultAndNameIgnoreCase(Integer vault, String name);

}
