package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESWorkCenterOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MESWorkCenterOperationRepo extends JpaRepository<MESWorkCenterOperation,Integer> {

    List<MESWorkCenterOperation> findByWorkCenter(Integer workcenterId);
}
