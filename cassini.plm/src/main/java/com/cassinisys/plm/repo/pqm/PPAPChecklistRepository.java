package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.pqm.PQMPPAPChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 16-03-2022.
 */
@Repository
public interface PPAPChecklistRepository extends JpaRepository<PQMPPAPChecklist, Integer> {

    PQMPPAPChecklist findByPpapAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PQMPPAPChecklist findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PQMPPAPChecklist> findByPpapAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    @Query("select i.id from PQMPPAPChecklist i where i.ppap= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.id ASC")
    List<Integer> getFileIdsByPpapAndLatestTrueAndParentFileIsNullAndFileTypeOrderByIdAsc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PQMPPAPChecklist i where i.ppap= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByPpapAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    List<PQMPPAPChecklist> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    @Query("select count (i) from PQMPPAPChecklist i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    PQMPPAPChecklist findByNameEqualsIgnoreCaseAndParentFileAndPpapAndLatestTrue(String name, Integer parent, Integer qcr);

    PQMPPAPChecklist findByPpapAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PQMPPAPChecklist> findByPpapAndFileNo(Integer id, String fileNo);

    List<PQMPPAPChecklist> findByPpapAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PQMPPAPChecklist> findByPpapAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PQMPPAPChecklist> findByPpapAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PQMPPAPChecklist i where i.ppap= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByPpapAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

    List<PQMPPAPChecklist> findByPpapAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer id, String fileType);

    @Query("select count (i) from PQMPPAPChecklist i where i.ppap= :id and i.fileType= :fileType and i.latest = true")
    Integer getFilesCountByPpapAndFileTypeAndLatestTrue(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i from PQMPPAPChecklist i where i.parentFile= :parent and i.fileType= :fileType and i.latest = true")
    List<PQMPPAPChecklist> getChildByFileType(@Param("parent") Integer parent, @Param("fileType") String fileType);

    @Query("select count (i) from PQMPPAPChecklist i where i.ppap= :id and i.latest = true")
    Integer getTotalChecklistCount(@Param("id") Integer id);

    @Query("select count (i) from PQMPPAPChecklist i where i.ppap= :id and i.latest = true and i.lifeCyclePhase.phaseType= :phaseType")
    Integer getChecklistCountByPhase(@Param("id") Integer id, @Param("phaseType") LifeCyclePhaseType phaseType);

    @Query("select count(i) from PQMPPAPChecklist i where i.name = :name and i.parentFile is null and i.fileType = 'FOLDER' and i.lifeCyclePhase.phase= :phase")
    Integer getChecklistIdsByName(@Param("name") String name, @Param("phase") String phase);
}
