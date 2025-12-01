package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemInstanceStatus;
import com.cassinisys.drdo.model.bom.LotInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 19-12-2018.
 */
@Repository
public interface LotInstanceRepository extends JpaRepository<LotInstance, Integer> {

    LotInstance findByInstanceAndIssueItem(Integer instance, Integer issueItem);

    List<LotInstance> findByInstance(Integer instance);

    List<LotInstance> findByIssueItemAndInstance(Integer issueItem, Integer instance);

    List<LotInstance> findByIssueItem(Integer issueItem);

    @Query("SELECT i from LotInstance i where i.status = 'FAILURE_PROCESS'")
    List<LotInstance> findFailureProcessLotInstances();


    @Query("SELECT i from LotInstance i where i.status= :status")
    List<LotInstance> getInstancesByStatus(@Param("status") ItemInstanceStatus status);

}
