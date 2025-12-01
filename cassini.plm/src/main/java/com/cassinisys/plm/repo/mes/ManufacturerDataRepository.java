package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESManufacturerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 17-11-2020.
 */
@Repository
public interface ManufacturerDataRepository extends JpaRepository<MESManufacturerData, Integer> {
}
