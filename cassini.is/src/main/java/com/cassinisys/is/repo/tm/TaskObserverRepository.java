package com.cassinisys.is.repo.tm;
/**
 * The Class is for TaskObserverRepository
 **/

import com.cassinisys.is.model.tm.ISTaskObserver;
import com.cassinisys.is.model.tm.ISTaskObserverId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskObserverRepository extends
        JpaRepository<ISTaskObserver, ISTaskObserverId> {

    @Query("select to.id.personId from ISTaskObserver to where to.id.taskId = :taskId")
    /**
     * The method used to findByTaskId of Integer
     **/
    public Page<Integer> findByTaskId(@Param(value = "taskId") Integer taskId,
                                      Pageable pageable);

}
