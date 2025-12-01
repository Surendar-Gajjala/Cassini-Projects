package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISBoqActualCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 23-05-2017.
 */
@Repository
public interface BoqActualCostRepository extends JpaRepository<ISBoqActualCost, Integer> {
    List<ISBoqActualCost> findByBoqItem(Integer itemId);
}
