package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomIndent;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomIndentRepository extends JpaRepository<CustomIndent, Integer>, QueryDslPredicateExecutor<CustomIndent> {
    Page<CustomIndent> findByStore(org.springframework.data.domain.Pageable pageable, Integer storeId);
}
