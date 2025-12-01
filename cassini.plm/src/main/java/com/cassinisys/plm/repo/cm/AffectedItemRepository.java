package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 022 22-May -17.
 */
@Repository
public interface AffectedItemRepository extends JpaRepository<PLMAffectedItem, Integer> {
    List<PLMAffectedItem> findByChange(Integer varl);

    List<PLMAffectedItem> findByItem(Integer var2);

    List<PLMAffectedItem> findByItemIn(List<Integer> itemIds);

    List<PLMAffectedItem> findByChangeInAndItem(List<Integer> changeIds, Integer itemId);

    PLMAffectedItem findByChangeAndItem(Integer change, Integer itemId);

    PLMAffectedItem findByChangeAndToItem(Integer change, Integer itemId);

    List<PLMAffectedItem> findByItemAndToRevision(Integer item, String revision);

    List<PLMAffectedItem> findByToItemAndToRevision(Integer item, String revision);

    List<PLMAffectedItem> findByToItem(Integer item);

    @Query(value = "SELECT it.type_id,count(i.item_id) from plm_affecteditem a JOIN plm_itemrevision ir ON ir.item_id = a.item JOIN plm_item i ON i.item_id = ir.item_master JOIN plm_itemtype it ON it.type_id = i.item_type GROUP BY it.type_id ORDER BY count(i.item_id) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> getTopFrequentlyChangingItemTypes();

    @Query(value = "SELECT it.type_id,count(i.item_id) from plm_affecteditem a JOIN plm_itemrevision ir ON ir.item_id = a.to_item JOIN plm_item i ON i.item_id = ir.item_master JOIN plm_itemtype it ON it.type_id = i.item_type WHERE i.latest_revision = a.to_item GROUP BY it.type_id ORDER BY count(i.item_id) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> getTopLatestFrequentlyChangingItemTypes();

    @Query(value = "SELECT af from plm_affecteditem af JOIN plm_itemrevision rv ON rv.item_id = af.to_item WHERE rv.is_released = FALSE AND af.to_revision= :revision AND rv.item_master= :itemId", nativeQuery = true)
    List<PLMAffectedItem> getAffectedItemByToItemAndToRevision(@Param("revision") String revision, @Param("itemId") Integer itemId);

    @Query(value = "SELECT af from plm_affecteditem af JOIN plm_itemrevision rv ON rv.item_id = af.item WHERE rv.is_released = FALSE AND af.to_revision= :revision AND rv.item_master= :itemId", nativeQuery = true)
    List<PLMAffectedItem> getAffectedItemByItemAndToRevision(@Param("revision") String revision, @Param("itemId") Integer itemId);
}
