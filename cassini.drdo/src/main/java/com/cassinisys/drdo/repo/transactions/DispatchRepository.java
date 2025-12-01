package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.Dispatch;
import com.cassinisys.drdo.model.transactions.DispatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 26-11-2018.
 */
@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, Integer>, QueryDslPredicateExecutor<Dispatch> {

    @Query("SELECT i from Dispatch i where i.bom.id= :bomId")
    List<Dispatch> findByBom(@Param("bomId") Integer bomId);

    @Query("SELECT i from Dispatch i where i.status= :status")
    List<Dispatch> getDispatchesByStatus(@Param("status") DispatchStatus status);
}
