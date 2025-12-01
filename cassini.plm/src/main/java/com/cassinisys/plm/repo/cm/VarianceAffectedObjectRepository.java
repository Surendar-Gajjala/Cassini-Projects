package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMVarianceAffectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface VarianceAffectedObjectRepository extends JpaRepository<PLMVarianceAffectedObject, Integer> {

    List<PLMVarianceAffectedObject> findByVariance(Integer integer);

    List<PLMVarianceAffectedObject> findByVarianceOrderByModifiedDateAsc(Integer integer);

    List<PLMVarianceAffectedObject> findByVarianceOrderByModifiedDateDesc(Integer integer);

    List<PLMVarianceAffectedObject> findByVarianceAndIsRecurringTrue(Integer id);


}
