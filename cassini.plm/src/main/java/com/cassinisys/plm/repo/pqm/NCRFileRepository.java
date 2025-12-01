package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMNCRFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface NCRFileRepository extends JpaRepository<PQMNCRFile, Integer> {
    List<PQMNCRFile> findByNcrAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMNCRFile i where i.ncr= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByNCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMNCRFile> findByNcrAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    PQMNCRFile findByNcrAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMNCRFile findByNameEqualsIgnoreCaseAndParentFileAndNcrAndLatestTrue(String name, Integer parent, Integer ncr);

    List<PQMNCRFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMNCRFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMNCRFile findByNcrAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMNCRFile> findByNcrAndFileNo(Integer id, String fileNo);

    List<PQMNCRFile> findByNcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    PQMNCRFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMNCRFile> findByNcrAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMNCRFile i where i.ncr= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByNcrAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMNCRFile> findByNcrAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String file);
}
