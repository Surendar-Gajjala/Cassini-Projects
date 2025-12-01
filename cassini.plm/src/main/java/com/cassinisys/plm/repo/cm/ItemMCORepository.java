package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMItemMCO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface ItemMCORepository extends JpaRepository<PLMItemMCO,Integer>, QueryDslPredicateExecutor<PLMItemMCO> {

    List<PLMItemMCO> findByIdIn(Iterable<Integer> ids);

    PLMItemMCO findByMcoNumber(String mcoNumber);

    @Query("select distinct i.status from PLMItemMCO i")
    List<String> getStatuses();

}
