package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface PQMSupplierRepository extends JpaRepository<PQMSupplier, Integer>, QueryDslPredicateExecutor<PQMSupplier> {
    List<PQMSupplier> findByIdIn(Iterable<Integer> ids);

    PQMSupplier findByName(String name);
}

