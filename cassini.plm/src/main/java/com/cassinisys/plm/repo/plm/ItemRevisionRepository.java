package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 002 2-May -17.
 */
@Repository
public interface ItemRevisionRepository extends JpaRepository<PLMItemRevision, Integer>, QueryDslPredicateExecutor<PLMItemRevision> {
    List<PLMItemRevision> findByItemMasterOrderByIdAsc(Integer itemMaster);

    List<PLMItemRevision> getByItemMasterOrderByCreatedDateDesc(Integer itemMaster);

    List<PLMItemRevision> findByItemMasterIn(Iterable<Integer> var2);

    List<PLMItemRevision> findByIdIn(Iterable<Integer> varl);

//    PLMItemRevision findByItemMaster(Integer id);

    List<PLMItemRevision> findByRevision(String revision);

    PLMItemRevision getByItemMasterAndRevision(Integer itemMaster, String revision);

    Page<PLMItemRevision> findByReleasedIsTrueOrderByReleasedDateDesc(Pageable pageable);

    List<PLMItemRevision> findByHasBomTrueAndReleasedFalse();

    List<PLMItemRevision> findByHasBomTrue();

    List<PLMItemRevision> findByInstanceOrderByCreatedDateDesc(Integer id);

    PLMItemRevision findByInstanceAndBomConfiguration(Integer instance, Integer bomConfiguration);

    List<PLMItemRevision> findByItemMasterAndReleasedFalse(Integer itemId);

    List<PLMItemRevision> findByReleasedTrueOrderByReleasedDate();

    @Query("select i.revision from PLMItemRevision i where i.itemMaster= :item and i.released = false and i.rejected = false and i.revision != '-' order by i.revision asc")
    List<String> getNotReleasedRevisionItems(@Param("item") Integer item);

    @Query("select ir from PLMItemRevision ir, com.cassinisys.plm.model.plm.PLMItem i where ir.id = i.latestRevision")
    Page<PLMItemRevision> getLatestItemRevisions(Pageable pageable);

    PLMItemRevision findByDesignObject(Integer dsnId);

    List<PLMItemRevision> findByDesignObjectIn(Iterable<Integer> dsnIds);

    @Query("select i.id from PLMItemRevision i where i.itemMaster= :itemId order by i.createdDate desc")
    List<Integer> getRevisionIdsByItemId(@Param("itemId") Integer itemId);

    @Query("select i from PLMItemRevision i where i.rejected = true")
    List<PLMItemRevision> getRejectedItemRevisions();

    @Query("select iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i where iR.lifeCyclePhase.phaseType = 'OBSOLETE' and iR.id = i.latestRevision")
    List<Integer> getObsoleteItemRevisions();

    @Query("select i.id from PLMItemRevision i where i.lifeCyclePhase.phase= :phase")
    List<Integer> getRevisionIdsByPhase(@Param("phase") String phase);

    @Query("select i.id from PLMItemRevision i where i.revision= :revision")
    List<Integer> getRevisionIdsByRevision(@Param("revision") String revision);

    @Query("select distinct i.revision from PLMItemRevision i order by i.revision asc")
    List<String> getUniqueRevisions();

    @Query("select distinct i.itemMaster from PLMItemRevision i where i.id in :ids")
    List<Integer> getItemIdsByRevisionIds(@Param("ids") Iterable<Integer> ids);

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where iR.released = true and iR.id = i.latestRevision")
    List<Integer> getReleasedItemMasterIds();

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where (iR.released = true and i.latestReleasedRevision is not null and iR.id = i.latestReleasedRevision)")
    List<Integer> getLatestReleasedItemMasterIds();

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where (iR.released = true and i.latestReleasedRevision is not null and iR.id = i.latestReleasedRevision and iR.hasBom = true)")
    List<Integer> getLatestReleasedItemMasterIdsAndHasBomTrue();

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where iR.itemMaster in :ids and iR.rejected = true and iR.id = i.latestRevision")
    List<Integer> getItemMasterIdsByLatestReleasedItemIds(@Param("ids") Iterable<Integer> ids);

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where iR.released = true and iR.id = i.latestRevision and i.itemType.itemClass = 'DOCUMENT'")
    List<Integer> getDocumentItemsReleasedItemMasterIds();

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where (iR.released = true and i.latestReleasedRevision is not null and iR.id = i.latestReleasedRevision) and i.itemType.itemClass = 'DOCUMENT'")
    List<Integer> getDocumentLatestReleasedItemMasterIds();

    @Query("select distinct iR.itemMaster from com.cassinisys.plm.model.plm.PLMItemRevision iR,com.cassinisys.plm.model.plm.PLMItem i" +
            " where iR.itemMaster in :ids and iR.rejected = true and iR.id = i.latestRevision and i.itemType.itemClass = 'DOCUMENT'")
    List<Integer> getDocumentItemMasterIdsByLatestReleasedItemIds(@Param("ids") Iterable<Integer> ids);

    List<PLMItemRevision> findByItemMasterAndReleasedTrue(Integer item);
}
