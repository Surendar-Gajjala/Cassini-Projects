package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMManufacturerMCO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface ManufacturerMCORepository extends JpaRepository<PLMManufacturerMCO,Integer>, QueryDslPredicateExecutor<PLMManufacturerMCO> {

    List<PLMManufacturerMCO> findByIdIn(Iterable<Integer> ids);

    PLMManufacturerMCO findByMcoNumber(String mcoNumber);

    @Query("select distinct i.status from PLMManufacturerMCO i")
    List<String> getStatuses();

}
