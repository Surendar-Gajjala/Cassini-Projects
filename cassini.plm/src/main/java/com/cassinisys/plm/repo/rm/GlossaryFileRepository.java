package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 28-06-2018.
 */
@Repository
public interface GlossaryFileRepository extends JpaRepository<PLMGlossaryFile, Integer> {
    List<PLMGlossaryFile> findByIdIn(Iterable<Integer> ids);

    List<PLMGlossaryFile> findByGlossary(Integer glossary);

    PLMGlossaryFile findByGlossaryAndNameAndLatestTrue(Integer glossary, String name);

    List<PLMGlossaryFile> findByGlossaryAndLatestTrueOrderByModifiedDateDesc(Integer glossary);

    List<PLMGlossaryFile> findByGlossaryAndNameOrderByCreatedDateDesc(Integer glossary, String name);

    PLMGlossaryFile findByGlossaryAndId(Integer glossaryId, Integer fileId);

    PLMGlossaryFile findByGlossaryAndIdAndLatestTrue(Integer glossaryId, Integer fileId);

    List<PLMGlossaryFile> findByGlossaryAndName(Integer glossary, String Name);

    List<PLMGlossaryFile> findByGlossaryAndNameAndLatestFalseOrderByCreatedDateDesc(Integer glossary, String name);

    List<PLMGlossaryFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    List<PLMGlossaryFile> findByGlossaryAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer glossary, String fileNo);

    List<PLMGlossaryFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i)from PLMGlossaryFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMGlossaryFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMGlossaryFile> findByGlossaryAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer glossary);

    PLMGlossaryFile findByGlossaryAndNameAndParentFileIsNullAndLatestTrue(Integer glossary, String name);

    PLMGlossaryFile findByNameEqualsIgnoreCaseAndParentFileAndGlossaryAndLatestTrue(String name, Integer parent, Integer glossary);

    PLMGlossaryFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMGlossaryFile> findByGlossaryAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer glossary, String fileType);

    List<PLMGlossaryFile> findByGlossaryAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(Integer glossary, String fileNo);

    PLMGlossaryFile findByGlossaryAndFileNoAndLatestTrue(Integer glossaryId, String fileNo);

    List<PLMGlossaryFile> findByGlossaryAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    @Query("select i.id from PLMGlossaryFile i where i.glossary= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByGlossaryAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMGlossaryFile i where i.glossary= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByGlossaryAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

}
