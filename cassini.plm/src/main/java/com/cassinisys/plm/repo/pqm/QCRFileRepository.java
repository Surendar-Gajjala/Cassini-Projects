package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMNCRFile;
import com.cassinisys.plm.model.pqm.PQMQCRFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRFileRepository extends JpaRepository<PQMQCRFile, Integer> {
    List<PQMQCRFile> findByQcrAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMQCRFile i where i.qcr= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByQCRAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMQCRFile> findByQcrAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    PQMQCRFile findByQcrAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMQCRFile findByNameEqualsIgnoreCaseAndParentFileAndQcrAndLatestTrue(String name, Integer parent, Integer qcr);

    List<PQMQCRFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMQCRFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMQCRFile findByQcrAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMQCRFile> findByQcrAndFileNo(Integer id, String fileNo);

    PQMQCRFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMQCRFile> findByQcrAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMQCRFile> findByQcrAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMQCRFile i where i.qcr= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByQcrAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMNCRFile> findByQcrAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String file);
}
