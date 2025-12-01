package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemFile;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFileRepository extends JpaRepository<PLMItemFile, Integer> {
    List<PLMItemFile> findByItem(PLMItemRevision item);

    List<PLMItemFile> findByIdIn(Iterable<Integer> ids);

    List<PLMItemFile> findByParentFileAndLatestTrueOrderByCreatedDateDesc(Integer parent);

    @Query("select count (i) from PLMItemFile i where i.parentFile= :parent and i.latest = true")
    Integer getChildrenCountByParentFileAndLatestTrue(@Param("parent") Integer parent);
//    List<PLMItemFile> findByParentOrderByCreatedDateDesc(Integer parent);

    List<PLMItemFile> findByParentFileOrderByCreatedDateDesc(Integer parent);

    List<PLMItemFile> findByItemAndLatestTrueOrderByModifiedDateDesc(PLMItemRevision item);
//    List<PLMItemFile> findByItemAndTypeAndLatestTrueOrderByModifiedDateDesc(PLMItemRevision item, String fileType);

    List<PLMItemFile> findByItemAndFileTypeAndLatestTrueOrderByModifiedDateDesc(PLMItemRevision item, String fileType);

    @Query("select count (i) from PLMItemFile i where i.item.id= :item and i.fileType= :fileType and i.latest = true")
    Integer getChildrenCountByItemAndParentFileAndLatestTrue(@Param("item") Integer item, @Param("fileType") String fileType);
//    List<PLMItemFile> findByItemAndLatestTrueAndParentIsNullOrderByModifiedDateDesc(PLMItemRevision item);

    List<PLMItemFile> findByItemAndLatestTrueAndParentFileIsNullOrderByFileTypeDescModifiedDateDesc(PLMItemRevision item);

    @Query("select count (i) from PLMItemFile i where i.item.id= :item and i.latest = true")
    Integer getFileCountByItemAndLatestTrueAndParentFileIsNull(@Param("item") Integer item);

    @Query("select i.id from PLMItemFile i where i.item.id= :item and i.parentFile is null and i.latest = true and i.fileType= :fileType")
    List<Integer> getFileIdsByItemAndLatestTrueAndParentFileIsNullAndFileTypeOrderByModifiedDateDesc(@Param("item") Integer item, @Param("fileType") String fileType);

    List<PLMItemFile> findByItemAndNameContainingIgnoreCase(PLMItemRevision item, String name);

    PLMItemFile findByItemAndNameAndLatestTrue(PLMItemRevision item, String name);
//    PLMItemFile findByItemAndNameAndParentIsNullAndLatestTrue(PLMItemRevision item, String name);

    PLMItemFile findByItemAndNameAndParentFileIsNullAndLatestTrue(PLMItemRevision item, String name);
//    PLMItemFile findByParentAndNameAndLatestTrue(Integer parent, String name);

    PLMItemFile findByParentFileAndNameAndLatestTrue(Integer parent, String name);

    List<PLMItemFile> findByItemAndName(Integer itemId, String name);

    @Query(
            "SELECT f FROM PLMItemFile f WHERE f.item.id = :itemId AND f.name = :name ORDER BY f.version DESC"
    )
    List<PLMItemFile> findAllByItemAndName(@Param("itemId") Integer itemId, @Param("name") String name);

    List<PLMItemFile> findByItemIdAndFileNo(Integer id,String fileNo);

    List<PLMItemFile> findByItemAndNameAndLatestFalseOrderByCreatedDateDesc(PLMItemRevision item, String name);

    List<PLMItemFile> findByItemAndFileNoAndLatestFalseOrderByCreatedDateDesc(PLMItemRevision item, String fileNo);

    List<PLMItemFile> findByItemAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(PLMItemRevision item, String fileNo);

    List<PLMItemFile> findByItemAndNameContainingIgnoreCaseAndLatestTrue(PLMItemRevision revision, String name);

    List<PLMItemFile> findByFileNoIsNullAndLatestTrueOrderByIdAsc();

    PLMItemFile findByItemAndFileNoAndLatestTrue(PLMItemRevision itemRevision, String fileNo);

	/*List<PLMItemFile> findByItemAndFileNoLatestFalseOrderByIdAsc(PLMItemRevision revision, String name);*/

    PLMItemFile findByNameEqualsIgnoreCaseAndParentFileAndItemAndLatestTrue(String name, Integer parent, PLMItemRevision itemRevision);

    PLMItemFile findByNameEqualsIgnoreCaseAndItemAndLatestTrue(String name, PLMItemRevision itemRevision);

    void deleteByItem(PLMItemRevision itemRevision);

    @Query("select i.id from PLMItemFile i where i.item.id= :id and i.latest = true and (LOWER(CAST(i.name as text))) LIKE '%' || :name || '%'")
    List<Integer> getFileIdsByItemAndNameContainingIgnoreCaseAndLatestTrue(@Param("id") Integer id, @Param("name") String name);

}
