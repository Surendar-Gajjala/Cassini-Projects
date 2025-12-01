package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.BomInstanceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 09-10-2018.
 */
@Repository
public interface BomInstanceItemRepository extends JpaRepository<BomInstanceItem, Integer>, QueryDslPredicateExecutor<BomInstanceItem> {
    List<BomInstanceItem> findByBom(Integer integer);

    List<BomInstanceItem> findByBomOrderByCreatedDateAsc(Integer bomId);

    @Query("SELECT i FROM BomInstanceItem i WHERE i.bom = :bomId and i.typeRef.versity = true")
    List<BomInstanceItem> getVersitySections(@Param("bomId") Integer bomId);

    @Query("SELECT i FROM BomInstanceItem i WHERE i.bom = :bomId and i.typeRef.versity = false")
    List<BomInstanceItem> getWithoutVersitySections(@Param("bomId") Integer bomId);


    List<BomInstanceItem> findByParentOrderByCreatedDateAsc(Integer bomId);

    BomInstanceItem findByParentAndBomItem(Integer bomId, Integer bomItem);

    @Query("SELECT i FROM BomInstanceItem i WHERE i.bomItem = :bomItem and i.idPath LIKE :idPath%")
    BomInstanceItem findByBomItemAndStartWithIdPath(@Param("bomItem") Integer bomItem, @Param("idPath") String idPath);

    @Query("SELECT i FROM BomInstanceItem i WHERE i.idPath LIKE :idPath%")
    List<BomInstanceItem> findByStartWithIdPath(@Param("idPath") String idPath);

    List<BomInstanceItem> findByBomItem(Integer bomItem);

    BomInstanceItem findByBomAndBomItem(Integer bom, Integer bomItem);

    @Query("select i from BomInstanceItem i where i.parent = :parent and (i.bomItemType = 'SUBSYSTEM' or i.bomItemType = 'UNIT')")
    List<BomInstanceItem> getParentChildrenOrderByCreatedDateAsc(@Param("parent") Integer parent);

    @Query("select i from BomInstanceItem i where i.parent = :parent and i.bomItemType = 'PART'")
    List<BomInstanceItem> getPartsByParentOrderByCreatedDateAsc(@Param("parent") Integer parent);
}
