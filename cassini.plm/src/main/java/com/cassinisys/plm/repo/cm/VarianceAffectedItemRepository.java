package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMVarianceAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface VarianceAffectedItemRepository extends JpaRepository<PLMVarianceAffectedItem, Integer> {

    List<PLMVarianceAffectedItem> findByVariance(Integer integer);

    List<PLMVarianceAffectedItem> findByItem(Integer integer);

    List<PLMVarianceAffectedItem> findByVarianceOrderByModifiedDateAsc(Integer integer);

    List<PLMVarianceAffectedItem> findByVarianceOrderByModifiedDateDesc(Integer integer);

    List<PLMVarianceAffectedItem> findByVarianceAndIsRecurringTrue(Integer id);

    @Query("select DISTINCT i.item from PLMVarianceAffectedItem i")
    List<Integer> findDistinctItem();

    @Query("select distinct count(i.variance) from PLMVarianceAffectedItem i where i.item in :itemIds")
    Integer getVarianceCountByItem(@Param("itemIds") List<Integer> itemIds);

}
