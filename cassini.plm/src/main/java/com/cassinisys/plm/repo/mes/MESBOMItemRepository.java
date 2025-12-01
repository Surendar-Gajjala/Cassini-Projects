package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOMItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 10-05-2022.
 */
@Repository
public interface MESBOMItemRepository extends JpaRepository<MESBOMItem, Integer>, QueryDslPredicateExecutor<MESBOMItem> {
    List<MESBOMItem> findByMbomRevision(Integer id);

    List<MESBOMItem> findByMbomRevisionAndParentIsNullOrderByCreatedDateAsc(Integer id);

    List<MESBOMItem> findByParentOrderByCreatedDateAsc(Integer id);

    MESBOMItem findByMbomRevisionAndParentAndBomItem(Integer revision, Integer parent, Integer bomItem);

    List<MESBOMItem> findByMbomRevisionAndBomItem(Integer revision, Integer bomItem);

    MESBOMItem findByMbomRevisionAndBomItemAndParentIsNull(Integer revision, Integer bomItem);

    @Query("select count (i) from MESBOMItem i where i.parent= :parent")
    Integer getChildCountByParent(@Param("parent") Integer parent);

    @Query("select i from MESBOMItem i where i.mbomRevision= :revision and i.bomItem in :ids")
    List<MESBOMItem> getBomItemsByMBOMRevisionAndBomIds(@Param("revision") Integer revision, @Param("ids") List<Integer> ids);

    @Query("select count (i) from MESBOMItem i where i.mbomRevision= :revision and i.type = 'NORMAL'")
    Integer getNormalItemsCountByMBomRevision(@Param("revision") Integer revision);
}
