package com.cassinisys.is.repo.tm;
/**
 * The Class is for TaskAssignedToRepository
 **/

import com.cassinisys.is.model.tm.ISTaskAssignedTo;
import com.cassinisys.is.model.tm.ISTaskAssignedToId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskAssignedToRepository extends
        JpaRepository<ISTaskAssignedTo, ISTaskAssignedToId> {

    @Query("select ta.id.personId from ISTaskAssignedTo ta where ta.id.taskId = :taskId")
    /**
     * The method used to findByTaskId of Integer
     **/
    public List<Integer> findByTaskId(@Param(value = "taskId") Integer taskId);

    @Query("select ta.id.taskId from ISTaskAssignedTo ta where ta.id.personId = :personId")
    /**
     * The method used to findByPersonId of Integer
     **/
    public List<Integer> findByPersonId(
            @Param(value = "personId") Integer personId);

}
