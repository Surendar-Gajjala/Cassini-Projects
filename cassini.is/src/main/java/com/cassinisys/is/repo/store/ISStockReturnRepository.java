package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISStockReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by swapna on 05/12/18.
 */
@Repository
public interface ISStockReturnRepository extends JpaRepository<ISStockReturn, Integer>, QueryDslPredicateExecutor<ISStockReturn> {

    Page<ISStockReturn> findByStore(Integer storeId, Pageable pageable);
}
