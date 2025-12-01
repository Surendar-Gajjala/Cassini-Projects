package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMChangeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lakshmi on 1/3/2016.
 */
@Repository
public interface ChangeFileRepository extends JpaRepository<PLMChangeFile, Integer> {
    List<PLMChangeFile> findByIdIn(Iterable<Integer> fileIds);

    List<PLMChangeFile> findByChangeAndLatestTrue(Integer ecoId);

    List<PLMChangeFile> findByChangeAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    List<PLMChangeFile> findAllByChangeAndNameOrderByCreatedDateDesc(@Param("ecoId") Integer ecoId, @Param("name") String name);

    PLMChangeFile findByChangeAndNameAndLatestTrueOrderByModifiedDateDesc(Integer ecoId, String name);

    List<PLMChangeFile> findByChangeAndNameContainingIgnoreCase(Integer eco, String name);

    List<PLMChangeFile> findByChangeAndNameContainingIgnoreCaseAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer eco, String name);

    List<PLMChangeFile> findByChangeAndNameAndLatestFalseOrderByCreatedDateDesc(Integer eco, String name);

    List<PLMChangeFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    PLMChangeFile findByChangeAndNameAndLatestTrue(Integer ecoId, String name);

    List<PLMChangeFile> findByChangeAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer ecoId, String fileNo);

    List<PLMChangeFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    List<PLMChangeFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMChangeFile> findByChangeAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer ecoId);

    PLMChangeFile findByChangeAndNameAndParentFileIsNullAndLatestTrue(Integer ecoId, String name);

    PLMChangeFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMChangeFile> findByChangeAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer ecoId, String fileType);

    PLMChangeFile findByChangeAndFileNoAndLatestTrue(Integer ecoId, String fileNo);

    PLMChangeFile findByNameEqualsIgnoreCaseAndParentFileAndChangeAndLatestTrue(String name, Integer parent, Integer eco);

    PLMChangeFile findByNameEqualsIgnoreCaseAndChangeAndLatestTrue(String name, Integer eco);

    List<PLMChangeFile> findByParentFileOrderByFileTypeDescModifiedDateDesc(Integer parent);

    List<PLMChangeFile> findByChangeAndFileNo(Integer change, String fileNo);

    List<PLMChangeFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer folder);

    @Query("select i.id from PLMChangeFile i where i.change= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByChangeAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMChangeFile i where i.change= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByChangeAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
