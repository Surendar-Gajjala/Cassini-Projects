package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.procm.ISMaterialIssueType;
import com.cassinisys.is.model.store.ISStockIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 01/08/18.
 */
@Repository
public interface ISStockIssueRepository extends JpaRepository<ISStockIssue, Integer>, QueryDslPredicateExecutor<ISStockIssue> {
    List<ISStockIssue> findByStore(Integer storeId);

    ISStockIssue findByIssueNumberSource(String number);

    Page<ISStockIssue> findByMaterialIssueTypeAndStore(ISMaterialIssueType materialIssueType, Integer storeId, Pageable pageable);

    Page<ISStockIssue> findByMaterialIssueType(ISMaterialIssueType materialIssueType, Pageable pageable);
}
