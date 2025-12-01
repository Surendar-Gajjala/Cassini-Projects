package com.cassinisys.is.repo.tm;
/**
 * The Class is for TaskStatusHistoryRepository
 **/

import com.cassinisys.is.model.tm.ISTaskStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusHistoryRepository extends
        JpaRepository<ISTaskStatusHistory, Integer> {
    /**
     * The method used to findByTask of ISTaskStatusHistory
     **/
    public Page<ISTaskStatusHistory> findByTask(Integer taskId,
                                                Pageable pageable);

}
