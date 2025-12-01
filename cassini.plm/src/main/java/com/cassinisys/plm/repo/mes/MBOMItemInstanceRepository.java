package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESMBOMItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by smukka on 04-10-2022.
 */
@Repository
public interface MBOMItemInstanceRepository extends JpaRepository<MESMBOMItemInstance, Integer> {
}
