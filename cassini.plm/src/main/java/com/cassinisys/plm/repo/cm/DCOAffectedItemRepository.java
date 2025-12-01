package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCOAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DCOAffectedItemRepository extends JpaRepository<PLMDCOAffectedItem, Integer> {

    List<PLMDCOAffectedItem> findByDco(Integer dcr);

    PLMDCOAffectedItem findByDcoAndItem(Integer dco, Integer item);

    PLMDCOAffectedItem findByDcoAndToItem(Integer dco, Integer item);

    List<PLMDCOAffectedItem> findByItem(Integer id);

    List<PLMDCOAffectedItem> findByToItem(Integer id);
}
