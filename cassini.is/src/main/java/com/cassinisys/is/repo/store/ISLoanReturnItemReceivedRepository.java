package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISLoanReturnItemReceived;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by swapna on 21/08/18.
 */
public interface ISLoanReturnItemReceivedRepository extends JpaRepository<ISLoanReturnItemReceived, Integer>, QueryDslPredicateExecutor<ISLoanReturnItemReceived> {
}
