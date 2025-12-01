package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISTopStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Repository
public interface ISTopStoreRepository extends JpaRepository<ISTopStore, Integer>, QueryDslPredicateExecutor<ISTopStore> {

    List<ISTopStore> findByIdIn(Iterable<Integer> ids);

    ISTopStore findByStoreNameEqualsIgnoreCase(String storeName);
}
