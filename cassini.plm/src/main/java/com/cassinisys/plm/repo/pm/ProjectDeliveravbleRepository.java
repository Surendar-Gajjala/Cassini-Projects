package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.DeliverableStatus;
import com.cassinisys.plm.model.pm.PLMProjectDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 12/31/17.
 */
@Repository
public interface ProjectDeliveravbleRepository extends JpaRepository<PLMProjectDeliverable, Integer>, QueryDslPredicateExecutor<PLMProjectDeliverable> {

    List<PLMProjectDeliverable> findByProject(Integer project);

    List<PLMProjectDeliverable> findByIdIn(Iterable<Integer> ids);

    List<PLMProjectDeliverable> findByItemRevision(Integer itemRevision);

    PLMProjectDeliverable findByProjectAndItemRevision(Integer projectId, Integer itemRevision);

    @Query("select count(i) from com.cassinisys.plm.model.pm.PLMProjectDeliverable i,com.cassinisys.plm.model.pm.PLMProject project " +
            "where i.project = project.id and project.program is null and i.deliverableStatus= :deliverableStatus")
    Integer getByProjectDeliverableByStatus(@Param("deliverableStatus") DeliverableStatus deliverableStatus);

    @Query("SELECT d.itemRevision FROM PLMProjectDeliverable d WHERE d.project= :project")
    List<Integer> getProjectDeliverableItem(@Param("project") Integer project);


}
