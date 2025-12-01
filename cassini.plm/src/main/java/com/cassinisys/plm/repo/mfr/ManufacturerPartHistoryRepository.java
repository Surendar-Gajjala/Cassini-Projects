package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 04-07-2020.
 */
@Repository
public interface ManufacturerPartHistoryRepository extends JpaRepository<PLMManufacturerPartHistory,Integer> {

	PLMManufacturerPartHistory findByAffectedPart(Integer id);

}
