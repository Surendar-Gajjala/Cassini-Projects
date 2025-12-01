package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMProjectTask;
import com.cassinisys.tm.model.TaskStatus;
import com.cassinisys.tm.model.dto.KeyAndNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Rajabrahmachary on 06-07-2016.
 */
public interface ProjectTaskRepository extends JpaRepository<TMProjectTask,Integer>,QueryDslPredicateExecutor<TMProjectTask> {
    Page<TMProjectTask> findByProject(Integer projectId, Pageable pageable);
    List<TMProjectTask> findByIdIn(Iterable<Integer> ids);
    List<TMProjectTask> findByAssignedToIn(Iterable<Integer> ids);
    List<TMProjectTask> findByAssignedToAndAssignedDate(Integer assignedTo,Date assignedDate);
    List<TMProjectTask> findByAssignedTo(Integer personId);
    List<TMProjectTask> findByLocationAndStatusIn(String location, Iterable<TaskStatus> statuses);

    @Query("SELECT DISTINCT t.location FROM TMProjectTask AS t")
    List<String> getLocations();

    @Query("SELECT NEW com.cassinisys.tm.model.dto.KeyAndNumber(t.location, COUNT(t)) FROM TMProjectTask t GROUP BY t.location")
    List<KeyAndNumber> findTasksGroupedByLocation();

    @Query("SELECT NEW com.cassinisys.tm.model.dto.KeyAndNumber(t.location, COUNT(t)) FROM TMProjectTask t " +
            "WHERE STATUS =:status GROUP BY t.location")
    List<KeyAndNumber> findTasksByStatusGroupedByLocation(@Param("status")TaskStatus status);

    @Query("SELECT NEW com.cassinisys.tm.model.dto.KeyAndNumber(t.assignedTo, COUNT(t)) FROM TMProjectTask t " +
            "WHERE t.location =:location GROUP BY t.assignedTo")
    List<KeyAndNumber> findTasksByLocationGroupedByPerson(@Param("location") String location);

    @Query("SELECT t.location, t.status, COUNT(t) FROM TMProjectTask t GROUP BY t.location, t.status ORDER BY t.location")
    List<Object[]> getAllTaskStats();
}
