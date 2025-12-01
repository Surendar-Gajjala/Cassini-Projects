package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMMCO;
import com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MCOProductAffectedItemRepository extends JpaRepository<PLMMCOProductAffectedItem, Integer> {
    List<PLMMCOProductAffectedItem> findByMco(Integer varl);

    List<PLMMCOProductAffectedItem> findByItem(Integer var2);

    List<PLMMCOProductAffectedItem> findByItemIn(List<Integer> itemIds);

    List<PLMMCOProductAffectedItem> findByItemAndToRevision(Integer item, String revision);

    List<PLMMCOProductAffectedItem> findByToItemAndToRevision(Integer item, String revision);

    List<PLMMCOProductAffectedItem> findByToItem(Integer item);

    PLMMCOProductAffectedItem findByMcoAndItem(Integer mco, Integer item);

    @Query("select i.item from PLMMCOProductAffectedItem i where i.mco= :mcoId")
    List<Integer> getItemIdsByMCO(@Param("mcoId") Integer mcoId);

    @Query("select count (i) from PLMMCOProductAffectedItem i where i.mco= :mcoId")
    Integer getMCOAffectedItemsCount(@Param("mcoId") Integer mcoId);

    @Query("select m from com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem i,com.cassinisys.plm.model.cm.PLMMCO m" +
            " where i.mco = m.id and i.item= :item and i.toRevision= :toRevision and m.statusType != 'RELEASED' and m.statusType != 'REJECTED'")
    List<PLMMCO> getAffectedItemAddedMCOsByItemAndToRevision(@Param("item") Integer item, @Param("toRevision") String toRevision);

    @Query("select m from com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem i,com.cassinisys.plm.model.cm.PLMMCO m" +
            " where i.mco = m.id and i.toItem= :item and i.toRevision= :toRevision and m.statusType != 'RELEASED' and m.statusType != 'REJECTED'")
    List<PLMMCO> getAffectedItemAddedMCOsByToItemAndToRevision(@Param("item") Integer item, @Param("toRevision") String toRevision);

    @Query("select count (m) from com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem i,com.cassinisys.plm.model.cm.PLMMCO m" +
            " where i.mco = m.id and i.item= :item and i.toRevision= :toRevision and m.statusType != 'RELEASED' and m.statusType != 'REJECTED'")
    Integer getAffectedItemAddedMCOsCountByItemAndToRevision(@Param("item") Integer item, @Param("toRevision") String toRevision);

    @Query("select count (m) from com.cassinisys.plm.model.cm.PLMMCOProductAffectedItem i,com.cassinisys.plm.model.cm.PLMMCO m" +
            " where i.mco = m.id and i.toItem= :item and i.toRevision= :toRevision and m.statusType != 'RELEASED' and m.statusType != 'REJECTED'")
    Integer getAffectedItemAddedMCOsCountByToItemAndToRevision(@Param("item") Integer item, @Param("toRevision") String toRevision);
}
