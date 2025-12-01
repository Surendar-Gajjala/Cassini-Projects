package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISLoanReturnItemIssued;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by swapna on 21/08/18.
 */
public interface ISLoanReturnItemIssuedRepository extends JpaRepository<ISLoanReturnItemIssued, Integer>, QueryDslPredicateExecutor<ISLoanReturnItemIssued> {

    List<ISLoanReturnItemIssued> findByLoanAndStore(Integer loanId, ISTopStore storeId);

    List<ISLoanReturnItemIssued> findByLoanAndItem(Integer loanId, Integer itemId);

    List<ISLoanReturnItemIssued> findByLoanAndStoreAndItem(Integer loanId, ISTopStore storeId, Integer itemId);
}
