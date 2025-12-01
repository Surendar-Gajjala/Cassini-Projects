package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMNCRStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface NCRStatusHistoryRepository extends JpaRepository<PQMNCRStatusHistory, Integer> {
}
