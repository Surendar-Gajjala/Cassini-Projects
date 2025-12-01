package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomRequisition;
import com.cassinisys.is.model.store.ISTopStore;
import com.cassinisys.is.model.store.RequisitionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomRequisitionRepository extends JpaRepository<CustomRequisition, Integer>, QueryDslPredicateExecutor<CustomRequisition> {

    @Query("SELECT i FROM CustomRequisition i WHERE i.project.id =:projectId and i.status =:requestStatus")
    Page<CustomRequisition> findByProjectOrderByIdAsc(@Param("projectId") Integer projectId, @Param("requestStatus") RequisitionStatus requestStatus, Pageable pageable);

    Page<CustomRequisition> findByStoreOrderByIdAsc(ISTopStore topStore, Pageable pageable);

    @Query("SELECT i FROM CustomRequisition i WHERE i.store.id =:storeId")
    List<CustomRequisition> findByStoreOrderByIdAsc(@Param("storeId") Integer storeId);
}
