package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESServiceOrder;
import com.cassinisys.plm.model.mes.MESServiceOrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Repository
public interface MESServiceOrderRepo extends JpaRepository<MESServiceOrder,Integer> {

    List<MESServiceOrder> findByType(MESServiceOrderType type);
}
