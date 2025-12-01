package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.DeliverableStatus;
import com.cassinisys.plm.model.pm.PLMActivityDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */
@Repository
public interface ActivityDeliverableRepository extends JpaRepository<PLMActivityDeliverable, Integer> {

    PLMActivityDeliverable findById(Integer id);

    List<PLMActivityDeliverable> findByActivity(Integer activity);

    List<PLMActivityDeliverable> findByItemRevision(Integer itemId);

    List<PLMActivityDeliverable> findByIdIn(Iterable<Integer> ids);

    PLMActivityDeliverable findByActivityAndItemRevision(Integer activity, Integer itemRevision);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMActivityDeliverable i,com.cassinisys.plm.model.pm.PLMActivity activity,com.cassinisys.plm.model.pm.PLMWbsElement wbselement,com.cassinisys.plm.model.pm.PLMProject project" +
            " where i.activity = activity.id and activity.wbs = wbselement.id and wbselement.project.id = project.id and project.program is null and i.deliverableStatus= :deliverableStatus")
    Integer getByActivityDeliverableByStatus(@Param("deliverableStatus") DeliverableStatus deliverableStatus);

    /*@Query(value = "SELECT d.item FROM plm_deliverable d JOIN plm_activitydeliverable ad ON d.id = ad.id WHERE ad.activity in (SELECT a.id FROM plm_activity a JOIN plm_wbselement w ON w.id = a.wbs WHERE w.project.id= :project)", nativeQuery = true)
    List<Integer> getActivityDeliverableItems(@Param("project") Integer project);*/

    @Query("SELECT ad.itemRevision FROM com.cassinisys.plm.model.pm.PLMActivityDeliverable ad WHERE ad.activity in" +
            " (SELECT a.id FROM com.cassinisys.plm.model.pm.PLMActivity a, com.cassinisys.plm.model.pm.PLMWbsElement w WHERE a.wbs = w.id and w.project.id = :project)")
    List<Integer> getActivityDeliverableItems(@Param("project") Integer project);
}
