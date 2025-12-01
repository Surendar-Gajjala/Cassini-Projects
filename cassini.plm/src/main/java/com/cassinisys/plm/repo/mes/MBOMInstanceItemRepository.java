package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMInstanceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 04-10-2022.
 */
@Repository
public interface MBOMInstanceItemRepository extends JpaRepository<MESMBOMInstanceItem, Integer> {
    MESMBOMInstanceItem findByMbomItemAndMbomInstance(Integer mbomItem, Integer mbomInstance);

    List<MESMBOMInstanceItem> findByMbomInstanceAndParentIsNullOrderByIdAsc(Integer mbomInstance);

    List<MESMBOMInstanceItem> findByParentOrderByIdAsc(Integer parent);

    @Query("select count (i) from MESMBOMInstanceItem i where i.parent= :id")
    Integer getChildCountByParent(@Param("id") Integer id);
}
