package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.InwardStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 17-10-2018.
 */
@Repository
public interface InwardStatusHistoryRepository extends JpaRepository<InwardStatusHistory, Integer> {

    List<InwardStatusHistory> findByInwardOrderByTimestampDesc(Integer inward);
}
