package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Repository
public interface ISItemRepository extends JpaRepository<ISItem, Integer>, QueryDslPredicateExecutor<ISItem> {

    List<ISItem> findByIdIn(Iterable<Integer> ids);

    ISItem findByItemNumber(String itemNumber);

}
