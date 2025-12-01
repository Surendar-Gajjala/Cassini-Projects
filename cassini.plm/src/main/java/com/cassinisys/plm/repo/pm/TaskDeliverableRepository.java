package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.DeliverableStatus;
import com.cassinisys.plm.model.pm.PLMTaskDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 19-06-2018.
 */
@Repository
public interface TaskDeliverableRepository extends JpaRepository<PLMTaskDeliverable, Integer> {

    List<PLMTaskDeliverable> findByTask(Integer taskId);

    List<PLMTaskDeliverable> findByItemRevision(Integer itemId);

    PLMTaskDeliverable findByTaskAndItemRevision(Integer taskId, Integer itemId);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMTaskDeliverable i,com.cassinisys.plm.model.pm.PLMTask task,com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.task = task.id and task.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.deliverableStatus= :deliverableStatus")
    Integer getByTaskDeliverableByStatus(@Param("deliverableStatus") DeliverableStatus deliverableStatus);

    @Query("SELECT td.itemRevision FROM com.cassinisys.plm.model.pm.PLMTaskDeliverable td WHERE td.task in" +
            " (SELECT t.id FROM com.cassinisys.plm.model.pm.PLMTask t, com.cassinisys.plm.model.pm.PLMActivity a, com.cassinisys.plm.model.pm.PLMWbsElement w" +
            " WHERE t.activity = a.id and a.wbs = w.id and w.project.id= :project)")
    List<Integer> getTaskDeliverableItems(@Param("project") Integer project);
}
