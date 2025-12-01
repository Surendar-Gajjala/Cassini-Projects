package com.cassinisys.plm.repo.wf;

import com.cassinisys.plm.model.wf.PLMUserTask;
import com.cassinisys.plm.model.wf.UserTaskStatus;
import com.cassinisys.plm.model.wf.dto.TasksByStatusDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PLMUserTaskRepository extends JpaRepository<PLMUserTask, Integer> {
    List<PLMUserTask> findByAssignedToOrderByIdDesc(Integer user);

    List<PLMUserTask> findByAssignedToAndStatusOrderByIdDesc(Integer user, UserTaskStatus status);

    List<PLMUserTask> findByStatusOrderByIdDesc(UserTaskStatus status);

    List<PLMUserTask> findByIdInOrderByIdDesc(Iterable<Integer> ids);

    PLMUserTask findBySourceOrderByIdDesc(Integer source);

    PLMUserTask findBySourceAndAssignedToOrderByIdDesc(Integer source, Integer assigned);

    List<PLMUserTask> findByAssignedToOrderByModifiedDateDesc(Integer user);

    @Query("SELECT count(t) FROM PLMUserTask t where t.status= :status and t.assignedTo= :assignedTo")
    Integer getUserTaskCountsByStatus(@Param("status") UserTaskStatus status, @Param("assignedTo") Integer assignedTo);

    @Query("select new com.cassinisys.plm.model.wf.dto.TasksByStatusDTO(t.status, count(t.status)) from PLMUserTask t group by t.status")
    List<TasksByStatusDTO> findCountsByStatus();

    long countByStatus(UserTaskStatus status);

    List<PLMUserTask> findByContextAndStatus(Integer context, UserTaskStatus status);
}
