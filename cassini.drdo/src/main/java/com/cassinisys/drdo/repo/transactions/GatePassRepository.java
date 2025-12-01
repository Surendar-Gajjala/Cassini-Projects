package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.GatePass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 27-10-2018.
 */
@Repository
public interface GatePassRepository extends JpaRepository<GatePass, Integer>, QueryDslPredicateExecutor<GatePass> {

    @Query("SELECT i from GatePass i where i.gatePass.name= :name")
    List<GatePass> getGatePassByName(@Param("name") String name);

    @Query("SELECT i from GatePass i where i.finish = false")
    Page<GatePass> getNotFinishedGatePasses(Pageable pageable);

    List<GatePass> findByFinishFalse();
}
