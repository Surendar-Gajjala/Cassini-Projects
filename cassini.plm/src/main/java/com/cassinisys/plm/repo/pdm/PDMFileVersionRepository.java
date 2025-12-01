package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMFileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMFileVersionRepository extends JpaRepository<PDMFileVersion, Integer>, QueryDslPredicateExecutor<PDMFileVersion> {

    PDMFileVersion findByFileAndLatestIsTrue(Integer fileId);

    @Query("select v from PDMFileVersion v where v.file.folder = ?1 and v.latest = true")
    List<PDMFileVersion> findByFolderAndLatest(Integer folderId);

    @Query("select v from PDMFileVersion v where v.file.folder= :folderId and v.latest = true and (LOWER(CAST(v.name as text))) LIKE '%' || :searchText || '%'")
    List<PDMFileVersion> findByFolderAndLatestAndSearchText(@Param("folderId") Integer folderId, @Param("searchText") String searchText);

    @Query("select v.attachedTo from PDMFileVersion v where v.latest = true")
    List<Integer> findAttachedTByLatestIsTrue();

    @Query("select v from PDMFileVersion v where v.file.folder = ?1 and v.name = ?2 and v.latest = true")
    PDMFileVersion findByFolderAndNameAndLatest(Integer folderId, String name);

    @Query("select v from PDMFileVersion v where v.file.folder = ?1 and v.commit = ?2")
    List<PDMFileVersion> findByFolderAndCommit(Integer folderId, Integer commitId);

    @Query("select v from PDMFileVersion v where v.file.id = ?1 order by v.id DESC")
    List<PDMFileVersion> findFileVersions(Integer fileMasterId);

    @Query("select v from PDMFileVersion v where v.file.id = ?1 and v.latest = true")
    PDMFileVersion findLatestByMaster(Integer fileMasterId);

    List<PDMFileVersion> findByCommit(Integer commitId);

    PDMFileVersion findByAttachedTo(Integer attachedToId);

    @Query(
            "SELECT DISTINCT i.commit FROM PDMFileVersion i WHERE i.file.id IN :fileIds"
    )
    List<PDMFileVersion> getFileCommits(@Param("fileIds") List<Integer> fileIds);

    @Query(
            "SELECT DISTINCT i.commit FROM PDMFileVersion i WHERE i.file.id IN :fileIds and lower(i.file.name) = lower(:searchText)"
    )
    List<PDMFileVersion> getSearchFileCommits(@Param("fileIds") List<Integer> fileIds, @Param("searchText") String searchText);
}
