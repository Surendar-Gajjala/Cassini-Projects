package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.MovementType;
import com.cassinisys.is.model.store.ISLoanIssueItem;
import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/08/18.
 */
@Repository
public interface ISLoanIssueItemRepository extends JpaRepository<ISLoanIssueItem, Integer>, QueryDslPredicateExecutor<ISLoanIssueItem> {

    List<ISLoanIssueItem> findByLoan(Integer loanId);

    List<ISLoanIssueItem> findByLoanAndStoreAndMovementType(Integer loanId, ISTopStore storeId, MovementType movementType);
}
