package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smukka on 04-10-2022.
 */
@Repository
public interface MBOMInstanceRepository extends JpaRepository<MESMBOMInstance, Integer> {
    List<MESMBOMInstance> findByProductionOrderItemOrderByIdAsc(Integer id);

    MESMBOMInstance findBySerialNumber(String serialNumber);
}
