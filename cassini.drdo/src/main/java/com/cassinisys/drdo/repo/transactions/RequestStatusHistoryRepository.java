package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.RequestStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Repository
public interface RequestStatusHistoryRepository extends JpaRepository<RequestStatusHistory, Integer> {

    List<RequestStatusHistory> findByRequestOrderByTimestampDesc(Integer request);
}
