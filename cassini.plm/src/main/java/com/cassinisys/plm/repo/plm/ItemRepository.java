package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.ItemClass;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<PLMItem, Integer>, QueryDslPredicateExecutor<PLMItem> {
    @Query(
            "SELECT i FROM PLMItem i WHERE i.itemType.id= :typeId"
    )
    Page<PLMItem> findByItemTypeId(@Param("typeId") Integer typeId, Pageable pageable);

    List<PLMItem> findByItemType(PLMItemType plmItemType);

    @Query("select i from PLMItem i where i.itemType.id= :itemType and i.configurable = false and i.configured = false")
    List<PLMItem> getNormalItemByItemType(@Param("itemType") Integer itemType);

    @Query("select i from PLMItem i where i.itemType.id= :itemType and i.configured = false")
    List<PLMItem> getNormalAndConfigurableItemByItemType(@Param("itemType") Integer itemType);

    @Query(
            "SELECT i FROM PLMItem i WHERE i.itemType.id IN :typeIds"
    )
    Page<PLMItem> getByItemTypeIds(@Param("typeIds") List<Integer> typeId, Pageable pageable);

    List<PLMItem> findByIdIn(Iterable<Integer> ids);

    List<PLMItem> findByItemNumber(String itemNumber);

    PLMItem findByItemNameEqualsIgnoreCase(String name);

    @Query(
            "SELECT i FROM PLMItem i WHERE i.itemNumber= :itemNumber"
    )
    PLMItem findOneByItemNumber(@Param("itemNumber") String itemNumber);

    PLMItem findById(Integer id);

    /*PLMItem findByLatestRevision(Integer latestRevision);*/

    List<PLMItem> findByLatestRevisionIn(Iterable<Integer> revisionIds);

    List<PLMItem> findByItemTypeIdIn(Iterable<Integer> typeIds);

    List<PLMItem> findByIdIn(Iterable<Integer> ids, Pageable pageable);

    @Query(
            "SELECT pi FROM PLMItem pi WHERE LOWER(CONCAT(pi.itemNumber, ' ',pi.description)) LIKE LOWER(CONCAT('%',:tearm, '%'))"
    )
    List<PLMItem> freeTextSearch(@Param("tearm") String tearm, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "COPY (select * from plm_item) TO 'F:\\tmp\\persons.csv' DELIMITER ',' CSV HEADER;",
            nativeQuery = true)
    void exportItems();

    List<PLMItem> findBylatestRevisionIn(List<Integer> revisionIds);

    PLMItem findByItemNumberEqualsIgnoreCase(String itemNumber);

    List<PLMItem> findByItemTypeAndConfigurableTrue(PLMItemType itemType);

    List<PLMItem> findByInstanceOrderByCreatedDateDesc(Integer id);

    List<PLMItem> findByConfiguredFalse();

    List<PLMItem> findByItemNumberIn(List<String> itemNumbers);

    @Query("select i from PLMItem i where i.itemType.itemClass= :itemClass order by i.modifiedDate desc")
    Page<PLMItem> findByItemClass(@Param("itemClass") ItemClass itemClass, Pageable pageable);

    @Query(
            "SELECT count (i) FROM PLMItem i WHERE i.itemType.lifecycle.id= :lifecycle"
    )
    Integer getItemByLifeCycle(@Param("lifecycle") Integer lifecycle);

    @Query("SELECT i FROM PLMItem i WHERE i.itemType.id IN :typeIds")
    List<PLMItem> findByItemTypeIds(@Param("typeIds") List<Integer> ids);

    @Query("select count(i) from PLMItem i where i.itemType.itemClass= :itemClass")
    Integer getItemCountByItemClass(@Param("itemClass") ItemClass itemClass);

    @Query("select count(i) from PLMItem i where i.configurable = false and i.configured = false")
    Integer getNormalItemsCount();

    @Query("select count(i) from PLMItem i where i.configurable = true and i.configured = false")
    Integer getConfigurableItemsCount();

    @Query("select count(i) from PLMItem i where i.configurable = false and i.configured = true")
    Integer getConfiguredItemsCount();

    @Query("select count(i) from PLMItem i where i.itemType.softwareType = true")
    Integer getSoftwareItemsCount();

    @Query("select count(i) from PLMItem i")
    Integer getAllItemCount();

    /*@Query(value = "select count(i) from plm_item i JOIN plm_itemrevision j ON i.latest_revision = j.item_id WHERE i.item_type = (SELECT plm_itemtype.type_id FROM plm_itemtype WHERE cast(plm_itemtype.item_class as text)= :itemClass) and j.is_released = true", nativeQuery = true)
    Integer getReleasedProductItems(@Param("itemClass") String itemClass);*/

    /*@Query(value = "select count(i) from plm_item i JOIN plm_itemrevision j ON i.latest_revision = j.item_id WHERE i.item_type = (SELECT plm_itemtype.type_id FROM plm_itemtype WHERE  plm_itemtype.item_class = 'PRODUCT') and j.is_released = true", nativeQuery = true)
    Integer getReleasedProductItems();*/

    @Query("select count(i) from PLMItem i, com.cassinisys.plm.model.plm.PLMItemRevision j where i.latestRevision = j.id and i.itemType.itemClass= :itemClass and j.released = true")
    Integer getReleasedProductItems(@Param("itemClass") ItemClass itemClass);

    @Query("select count(i) from PLMItem i, com.cassinisys.plm.model.plm.PLMItemRevision j where i.latestRevision = j.id and i.itemType.itemClass= :itemClass and j.rejected = true")
    Integer getRejectedProductItems(@Param("itemClass") ItemClass itemClass);

    @Query("select count(i) from PLMItem i, com.cassinisys.plm.model.plm.PLMItemRevision j where i.latestRevision = j.id and i.itemType.itemClass= :itemClass and j.rejected = false and j.released = false")
    Integer getPendingProductItems(@Param("itemClass") ItemClass itemClass);

    @Query("SELECT i FROM PLMItem i, com.cassinisys.plm.model.plm.PLMItemRevision ir WHERE i.latestReleasedRevision = ir.id and i.itemType.id= :itemType AND i.configured = FALSE AND ir.released = TRUE")
    List<PLMItem> getReleasedNormalAndConfigurableItemByItemType(@Param("itemType") Integer itemType);

    @Query("select count(i) from PLMItem i, com.cassinisys.plm.model.plm.PLMItemRevision j where i.latestRevision = j.id and i.itemType.itemClass= :itemClass and j.lifeCyclePhase.phase= :phase")
    Integer getItemsByClassAndPhase(@Param("itemClass") ItemClass itemClass, @Param("phase") String phase);

    @Query("select i.latestRevision from PLMItem i where i.id IN :itemIds")
    List<Integer> getLatestRevisionIdsByItemIds(@Param("itemIds") List<Integer> itemIds);

    @Query(value = "SELECT count(i) FROM plm_item i JOIN plm_itemrevision ir ON i.item_id = ir.item_master JOIN plm_itemtype it ON it.type_id = i.item_type" +
            " WHERE ir.revision= :lovValue AND it.revision_sequence= :lov", nativeQuery = true)
    Integer getItemTypeLovValueCount(@Param("lovValue") String lovValue, @Param("lov") Integer lov);

    PLMItem findByDesignObject(Integer dsnId);

    List<PLMItem> findByDesignObjectIn(Iterable<Integer> dsnIds);

    @Query("select count (i) from PLMItem i where (LOWER(CAST(i.itemNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.itemType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.itemName as text))) LIKE '%' || :searchText || '%'")
    Integer getItemCountBySearchQuery(@Param("searchText") String searchText);

    @Query(value = "select count (i) from plm_item i where (LOWER(i.item_name)) ~* :searchText", nativeQuery = true)
    Integer getItemCountBySearchQueryIn(@Param("searchText") String searchText);

    @Query("select i.latestRevision from PLMItem i where (LOWER(CAST(i.itemNumber as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.itemType.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.itemName as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getItemRevisionIdsBySearchQuery(@Param("searchText") String searchText);

    @Query("select i.latestRevision from PLMItem i where (LOWER(CAST(i.itemName as text))) LIKE '%' || :searchText || '%'")
    List<Integer> getItemRevisionIdsByItemName(@Param("searchText") String searchText);

}
