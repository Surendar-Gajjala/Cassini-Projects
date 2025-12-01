package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESWorkCenterResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESWorkCenterResourceRepo extends JpaRepository<MESWorkCenterResource,Integer> {
}
