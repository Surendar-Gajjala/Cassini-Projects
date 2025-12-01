package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMBom;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<PLMBom, Integer>, QueryDslPredicateExecutor<PLMBom> {
    List<PLMBom> findByParent(PLMItemRevision parent);

    @Query("select i from PLMBom i where i.parent.id= :parentId")
    List<PLMBom> findByParentId(@Param("parentId") Integer parentId);

    List<PLMBom> findByParentOrderByCreatedDateAsc(PLMItemRevision parent);

    List<PLMBom> findByParentOrderBySequenceAsc(PLMItemRevision parent);

    List<PLMBom> findByParentIdOrderBySequenceAsc(Integer parent);

    List<PLMBom> findByParentIdInOrderBySequenceAsc(Iterable<Integer> parent);

    @Query("select i.item.id from PLMBom i where i.parent.id= :parent")
    List<Integer> getItemIdsByParent(@Param("parent") Integer parent);

    @Query("select count (i) from PLMBom i where i.parent.id= :id")
    Integer getItemBomCount(@Param("id") Integer id);

    List<PLMBom> findByItem(PLMItem item);

    @Query("select i.parent.itemMaster from PLMBom i where i.item.id= :item")
    List<Integer> getParentItemIdsByItem(@Param("item") Integer item);

    List<PLMBom> findByItemOrderByCreatedDateAsc(PLMItem item);

    @Query("select i from PLMBom i where i.item.id= :item")
    List<PLMBom> getByItemOrderByCreatedDateAsc(@Param("item") Integer item);

    @Query("select count (i) from PLMBom i where i.item.id= :item")
    Integer getItemsCountByParent(@Param("item") Integer item);

    @Query(value = "SELECT count(i) FROM plm_bom i JOIN plm_item j ON j.latest_revision = (select item_id FROM plm_itemrevision WHERE item_id = i.parent) WHERE i.item = :itemId", nativeQuery = true)
    public BigInteger getItemWhereUsedCount(@Param("itemId") Integer itemId);

    PLMBom findByItem(PLMItemRevision item);

    List<PLMBom> findByItemIn(Iterable<Integer> varl);

    PLMBom findByParentAndItem(PLMItemRevision itemRevision, PLMItem item);

    @Query("select i from PLMBom i where i.parent.id= :parent and i.item.configurable = true order by i.sequence asc")
    List<PLMBom> findByParentAndConfigurableTrueOrderBySequenceAsc(@Param("parent") Integer parent);

    @Query("select i from PLMBom i where i.parent.id= :parent and i.item.configurable = false order by i.sequence asc")
    List<PLMBom> findByParentAndConfigurableFalseOrderBySequenceAsc(@Param("parent") Integer parent);

    List<PLMBom> findByParentAndSequenceIsNullOrderByCreatedDateAsc(PLMItemRevision parent);

    List<PLMBom> findByParentAndSequenceIsNotNullOrderByCreatedDateAsc(PLMItemRevision parent);

    @Query(value = "SELECT count(i.asReleasedRevision) FROM PLMBom i, com.cassinisys.plm.model.plm.PLMItemRevision ir WHERE i.asReleasedRevision = ir.id and i.parent.id= :parent AND ir.released = true and ir.id not in :ids")
    Integer getReleasedBomItemsCountByItemAndIds(@Param("parent") Integer parent, @Param("ids") List<Integer> ids);

    @Query(value = "SELECT count(i.asReleasedRevision) FROM PLMBom i, com.cassinisys.plm.model.plm.PLMItemRevision ir WHERE i.asReleasedRevision = ir.id and i.parent.id= :parent AND ir.released = true")
    Integer getReleasedBomItemsCountByItem(@Param("parent") Integer parent);

    @Query("SELECT i FROM PLMBom i, com.cassinisys.plm.model.plm.PLMItemRevision ir WHERE i.asReleasedRevision = ir.id and i.parent.id= :parent AND ir.released = true")
    List<PLMBom> getReleasedBomItemIdsByItem(@Param("parent") Integer parent);

    @Query("SELECT i FROM PLMBom i, com.cassinisys.plm.model.plm.PLMItemRevision ir WHERE i.asReleasedRevision = ir.id and i.parent.id= :parent AND ir.released = true and ir.id not in :ids")
    List<PLMBom> getReleasedBomItemIdsByItemAndIds(@Param("parent") Integer parent, @Param("ids") List<Integer> ids);

    @Query("select i.item.latestRevision from PLMBom i where i.parent.id= :parent order by i.sequence asc")
    List<Integer> getBomItemLatestRevisionIds(@Param("parent") Integer parent);

    @Query("select i.item.latestReleasedRevision from PLMBom i where i.parent.id= :parent and i.item.latestReleasedRevision is not null")
    List<Integer> getBomItemLatestReleasedRevisionIds(@Param("parent") Integer parent);

    @Query("select i.asReleasedRevision from PLMBom i where i.parent.id= :parent and i.asReleasedRevision is not null")
    List<Integer> getBomItemAsReleasedRevisionIds(@Param("parent") Integer parent);

    @Query("select i.id from PLMBom i where i.parent.id= :parent and i.asReleasedRevision is not null")
    List<Integer> getBomIdsAsReleasedRevisionIdsByParent(@Param("parent") Integer parent);
}
