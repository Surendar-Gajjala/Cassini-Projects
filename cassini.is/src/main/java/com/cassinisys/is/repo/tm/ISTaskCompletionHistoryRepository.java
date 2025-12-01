package com.cassinisys.is.repo.tm;

import com.cassinisys.is.model.tm.ISTaskCompletionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Varsha Malgireddy on 8/3/2018.
 */
public interface ISTaskCompletionHistoryRepository extends JpaRepository<ISTaskCompletionHistory, Integer>, QueryDslPredicateExecutor<ISTaskCompletionHistory> {

    List<ISTaskCompletionHistory> findByTask(Integer taskId);

    List<ISTaskCompletionHistory> findByTaskInAndTimeStampAfterAndTimeStampBefore(List<Integer> taskId, Date startDate, Date endDate);

    @Query("SELECT MIN(h.timeStamp) from ISTaskCompletionHistory h WHERE h.task IN :taskIds")
    Date getMinimumDate(@Param("taskIds") List<Integer> taskIds);
}

