package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.BomInstance;
import com.cassinisys.drdo.model.bom.ItemInstance;
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
public interface BomInstanceRepository extends JpaRepository<BomInstance, Integer>, QueryDslPredicateExecutor<BomInstance> {

    BomInstance findByItem(ItemInstance id);

    BomInstance findByBomAndItem(Integer id, ItemInstance itemInstance);

    List<BomInstance> findByBomOrderByCreatedDateAsc(Integer bomId);

    @Query("SELECT b FROM BomInstance b where b.item.id =:id")
    BomInstance findByItemId(@Param("id") Integer id);

    @Query("SELECT i from BomInstance i where i.bom= :bom and i.item.instanceName= :instance")
    BomInstance getInstanceByBomAndName(@Param("bom") Integer bom, @Param("instance") String instance);
}
