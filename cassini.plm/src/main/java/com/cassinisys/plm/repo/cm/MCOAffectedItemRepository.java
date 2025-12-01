package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMMCOAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface MCOAffectedItemRepository extends JpaRepository<PLMMCOAffectedItem, Integer> {

    List<PLMMCOAffectedItem> findByMco(Integer mco);

    List<PLMMCOAffectedItem> findByReplacement(Integer mfrPartId);

    @Query("select count (i) from PLMMCOAffectedItem i where i.mco= :mcoId")
    Integer getMCOAffectedItemsCount(@Param("mcoId") Integer mcoId);
}
