package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItem;
import com.cassinisys.drdo.model.bom.BomItemType;
import com.cassinisys.drdo.model.bom.ItemRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */
@Repository
public interface BomItemRepository extends JpaRepository<BomItem, Integer>, QueryDslPredicateExecutor<BomItem> {

    List<BomItem> findByBomOrderByCreatedDateAsc(Integer bomId);

    @Query("SELECT i FROM BomItem i WHERE i.bom = :bomId and i.typeRef.versity = true")
    List<BomItem> getVersitySections(@Param("bomId") Integer bomId);

    List<BomItem> findByItem(ItemRevision itemRevision);

    List<BomItem> findByItemAndUniqueCode(ItemRevision itemRevision, String uniqueCode);

    BomItem getByItem(ItemRevision itemRevision);

    List<BomItem> findByTypeRef(BomGroup bomGroup);

    List<BomItem> findByParentOrderByCreatedDateAsc(Integer parent);

    @Query("SELECT i from BomItem i where i.parent= :parent and (i.bomItemType = 'SUBSYSTEM' or i.bomItemType = 'UNIT')")
    List<BomItem> getSubSystemAndUnitChildren(@Param("parent") Integer parent);

    @Query("SELECT i from BomItem i where i.bom= :bom and i.bomItemType != 'PART'")
    List<BomItem> getChildrenByBom(@Param("bom") Integer bom);

    @Query("SELECT i from BomItem i where i.parent= :parent and i.bomItemType != 'PART'")
    List<BomItem> getChildrenByBomItem(@Param("parent") Integer parent);

    @Query("SELECT i from BomItem i where i.parent= :parent and LOWER(i.item.itemMaster.itemName) LIKE LOWER(CONCAT('%',:searchText,'%'))")
    List<BomItem> getItemsByParentAndText(@Param("parent") Integer parent, @Param("searchText") String searchText);

    BomItem findByBomAndTypeRef(Integer bom, BomGroup typeRef);

    List<BomItem> getChildrenByParentAndBomItemType(Integer parent, BomItemType type);

    @Query("SELECT i from BomItem i where (i.uniqueCode is null or i.uniqueCode is empty) and i.bomItemType = 'PART'")
    List<BomItem> getUniqueCodeIsNullAndEmpty();
}
