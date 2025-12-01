package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECRAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ECRAffectedItemRepository extends JpaRepository<PLMECRAffectedItem, Integer> {
    List<PLMECRAffectedItem> findByEcr(Integer ecr);

    List<PLMECRAffectedItem> findByItem(Integer item);

    PLMECRAffectedItem findByEcrAndItem(Integer ecr, Integer item);

    @Query("select i.item from PLMECRAffectedItem i where i.ecr= :ecr")
    List<Integer> getAffectedItemIdsByEcr(@Param("ecr") Integer ecr);
}
