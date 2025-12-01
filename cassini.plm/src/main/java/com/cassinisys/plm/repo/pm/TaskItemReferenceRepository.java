package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pm.PLMTaskItemReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 20-06-2018.
 */
@Repository
public interface TaskItemReferenceRepository extends JpaRepository<PLMTaskItemReference, Integer>, QueryDslPredicateExecutor<PLMTaskItemReference> {

    List<PLMTaskItemReference> findByTask(Integer taskId);

    @Query("select i.item.id from PLMTaskItemReference i where i.task= :taskId")
    List<Integer> getItemIdsByTask(@Param("taskId") Integer taskId);

    PLMTaskItemReference findByTaskAndItem(Integer task, PLMItemRevision revision);
}

