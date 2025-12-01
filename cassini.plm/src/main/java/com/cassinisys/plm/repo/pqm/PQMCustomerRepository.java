package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface PQMCustomerRepository extends JpaRepository<PQMCustomer, Integer>, QueryDslPredicateExecutor<PQMCustomer> {
    List<PQMCustomer> findByIdIn(Iterable<Integer> ids);

    PQMCustomer findByName(String name);

    @Query("select i.contactPerson from PQMCustomer i where i.contactPerson not in :ids")
    List<Integer> getCustomerByIdNotIn(@Param("ids") Iterable<Integer> ids);

    @Query("select i from PQMCustomer i where i.id=:reportedBy")
    PQMCustomer getQualityCustomer(@Param("reportedBy") Integer reportedBy);


}
