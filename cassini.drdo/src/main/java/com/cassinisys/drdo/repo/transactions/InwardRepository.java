package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.InwardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 09-10-2018.
 */
@Repository
public interface InwardRepository extends JpaRepository<Inward, Integer>, QueryDslPredicateExecutor<Inward> {

    List<Inward> findAllByOrderByCreatedDateDesc();

    @Query("SELECT i from Inward i where i.bom.id= :bom order by i.createdDate DESC")
    List<Inward> findInwardsByBom(@Param("bom") Integer bom);

    @Query("SELECT i from Inward i where i.status != 'FINISH'")
    List<Inward> getAllNotFinishedInwards();

    @Query("SELECT i from Inward i where i.status= :status order by i.createdDate DESC")
    List<Inward> getInwardsByStatus(@Param("status") InwardStatus status);

    @Query("SELECT i from Inward i where i.gatePass.id= :gatePass")
    List<Inward> getInwardsByGatePass(@Param("gatePass") Integer gatePass);

    @Query("SELECT i from Inward i where i.supplier.id= :supplier")
    List<Inward> findInwardBySupplier(@Param("supplier") Integer supplier);
}
