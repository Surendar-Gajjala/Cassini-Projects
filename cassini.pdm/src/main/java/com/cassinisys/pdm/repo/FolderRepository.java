package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface FolderRepository extends JpaRepository<PDMFolder, Integer>, QueryDslPredicateExecutor<PDMFolder> {

	List<PDMFolder> findByVault(Integer id);

	PDMFolder findByPath(String path);

	List<PDMFolder> findByParentOrderByCreatedDateAsc(Integer parentId);

	@Query(
			"SELECT f FROM PDMFolder f WHERE f.vault=:vaultId AND f.parent IS NULL " +
					"ORDER BY f.createdDate"
	)
	List<PDMFolder> findRootFolders(@Param("vaultId") Integer vaultId);

	PDMFolder findByVaultAndParentAndName(Integer vaultId, Integer parentId, String name);
}
