package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.BomItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 07-10-2018.
 */
@Repository
public interface BomItemInstanceRepository extends JpaRepository<BomItemInstance, Integer> {

    List<BomItemInstance> findByBomInstanceItem(Integer integer);

    BomItemInstance findByItemInstance(Integer itemInstance);

    List<BomItemInstance> getByItemInstance(Integer itemInstance);

    BomItemInstance findByItemInstanceAndBomInstanceItem(Integer itemInstance, Integer bomInstanceItem);
}
