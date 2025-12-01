package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.LotInstanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 01-11-2018.
 */
@Repository
public interface LotInstanceHistoryRepository extends JpaRepository<LotInstanceHistory, Integer>, QueryDslPredicateExecutor<LotInstanceHistory> {

    @Query("SELECT i from LotInstanceHistory i where i.lotInstance= :instance order by i.timestamp desc")
    List<LotInstanceHistory> getHistoryByLotInstance(@Param("instance") Integer instance);

}
