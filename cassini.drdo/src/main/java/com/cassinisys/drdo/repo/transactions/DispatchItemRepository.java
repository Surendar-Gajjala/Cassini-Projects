package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.DispatchItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 26-11-2018.
 */
@Repository
public interface DispatchItemRepository extends JpaRepository<DispatchItem, Integer> {

    List<DispatchItem> findByDispatch(Integer dispatch);
}
