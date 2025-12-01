package com.cassinisys.is.repo.procm;
/**
 * The Class is for SupplierRepository
 **/

import com.cassinisys.is.model.procm.ISSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<ISSupplier, Integer>, QueryDslPredicateExecutor<ISSupplier> {

    List<ISSupplier> findByIdIn(Iterable<Integer> ids);

    ISSupplier findByName(String Name);
}
