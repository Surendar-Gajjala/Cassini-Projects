package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 10/08/18.
 */
@Repository
public interface ISLoanRepository extends JpaRepository<ISLoan, Integer>, QueryDslPredicateExecutor<ISLoan> {

    List<ISLoan> findByFromStore(Integer storeId);

    List<ISLoan> findByToStore(Integer storeId);
}
