package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMFolderRepository extends JpaRepository<PDMFolder, Integer>, QueryDslPredicateExecutor<PDMFolder> {
    PDMFolder findByVaultAndNameAndParentIsNull(Integer vaultId, String name);
    PDMFolder findByParentAndName(Integer parentFolderId, String name);
    List<PDMFolder> findByVaultAndParentIsNull(Integer vaultId);
    List<PDMFolder> findByParent(Integer parentId);
    List<PDMFolder> findByIdIn(Iterable<Integer> ids);
    PDMFolder findByIdPath(String path);
    PDMFolder findByVaultAndNamePathIgnoreCase(Integer vaultId, String folderNamePath);
    PDMFolder findByParentAndNameIgnoreCase(Integer parentId, String name);
}
