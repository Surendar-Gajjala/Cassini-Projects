package com.cassinisys.erp.repo.store;

import com.cassinisys.erp.model.store.ERPStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ERPStoreRepository extends JpaRepository<ERPStore, Integer>, QueryDslPredicateExecutor<ERPStore> {

    List<ERPStore> findByIdIn(Iterable<Integer> ids);

    ERPStore findByStoreNameEqualsIgnoreCase(String storeName);
}
