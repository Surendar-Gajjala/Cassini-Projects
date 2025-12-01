package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMNprFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR Cassini on 17-11-2020.
 */
@Repository
public interface NprFileRepository extends JpaRepository<PLMNprFile, Integer> {
    List<PLMNprFile> findByIdIn(Iterable<Integer> fileIds);

    PLMNprFile findByNprAndNameAndParentFileIsNullAndLatestTrue(Integer id, String name);

    PLMNprFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMNprFile> findByNprAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(Integer id);

    List<PLMNprFile> findByParentFileAndLatestTrueOrderByFileTypeDescModifiedDateDesc(Integer parent);

    PLMNprFile findByNameEqualsIgnoreCaseAndParentFileAndNprAndLatestTrue(String name, Integer parent, Integer qcr);

    PLMNprFile findByNprAndFileNoAndLatestTrue(Integer id, String fileNo);

    List<PLMNprFile> findByNprAndFileNo(Integer id, String fileNo);

    List<PLMNprFile> findByNprAndFileNoAndLatestFalseOrderByCreatedDateDesc(Integer id, String fileNo);

    List<PLMNprFile> findByNprAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(Integer id);

    List<PLMNprFile> findByNprAndNameContainingIgnoreCaseAndLatestTrue(Integer id, String name);

    List<PLMNprFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMNprFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);

    List<PLMNprFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMNprFile> findAllByNprAndNameOrderByCreatedDateDesc(@Param("nprId") Integer nprId, @Param("name") String name);

    PLMNprFile findByNameEqualsIgnoreCaseAndNprAndLatestTrue(String name, Integer npr);

    List<PLMNprFile> findByNprAndFileTypeAndLatestTrueOrderByModifiedDateDesc(Integer npr, String fileType);

    @Query("select i.id from PLMNprFile i where i.npr= :id and i.parentFile is null and i.latest = true and i.fileType= :fileType order by i.modifiedDate DESC")
    List<Integer> getFileIdsByNprAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("id") Integer id, @Param("fileType") String fileType);

    @Query("select i.id from PLMNprFile i where i.npr= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByNprAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);
}
