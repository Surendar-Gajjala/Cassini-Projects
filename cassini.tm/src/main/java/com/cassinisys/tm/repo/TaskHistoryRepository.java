package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMTaskHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 06-07-2016.
 */
public interface TaskHistoryRepository extends JpaRepository<TMTaskHistory,Integer> {
    List<TMTaskHistory> findByTask(Integer taskId);
}
