package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMProblemReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ProblemReportFileRepository extends JpaRepository<PQMProblemReportFile, Integer> {

    List<PQMProblemReportFile> findByProblemReportAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMProblemReportFile i where i.problemReport= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByProblemReportAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMProblemReportFile> findByProblemReportAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    PQMProblemReportFile findByProblemReportAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMProblemReportFile findByNameEqualsIgnoreCaseAndParentFileAndProblemReportAndLatestTrue(String name, Integer paren, Integer reportId);

    List<PQMProblemReportFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMProblemReportFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMProblemReportFile findByProblemReportAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMProblemReportFile> findByProblemReportAndFileNo(Integer id, String name);

    PQMProblemReportFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMProblemReportFile> findByProblemReportAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMProblemReportFile> findByProblemReportAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMProblemReportFile i where i.problemReport= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByProblemReportAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMProblemReportFile> findByProblemReportAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String file);
}
