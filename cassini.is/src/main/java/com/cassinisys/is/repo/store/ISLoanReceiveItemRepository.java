package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISLoanReceiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by swapna on 13/08/18.
 */
@Repository
public interface ISLoanReceiveItemRepository extends JpaRepository<ISLoanReceiveItem, Integer>, QueryDslPredicateExecutor<ISLoanReceiveItem> {
}
