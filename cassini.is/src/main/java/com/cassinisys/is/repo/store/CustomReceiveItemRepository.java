package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.CustomReceiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Repository
public interface CustomReceiveItemRepository extends JpaRepository<CustomReceiveItem, Integer>, QueryDslPredicateExecutor<CustomReceiveItem> {
}
