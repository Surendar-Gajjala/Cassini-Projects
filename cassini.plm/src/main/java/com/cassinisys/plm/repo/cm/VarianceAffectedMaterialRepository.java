package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMVarianceAffectedMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface VarianceAffectedMaterialRepository extends JpaRepository<PLMVarianceAffectedMaterial, Integer> {

    List<PLMVarianceAffectedMaterial> findByVariance(Integer integer);

    List<PLMVarianceAffectedMaterial> findByMaterial(Integer integer);

    List<PLMVarianceAffectedMaterial> findByVarianceOrderByModifiedDateAsc(Integer integer);

    List<PLMVarianceAffectedMaterial> findByVarianceOrderByModifiedDateDesc(Integer integer);

    List<PLMVarianceAffectedMaterial> findByVarianceAndIsRecurringTrue(Integer id);

    @Query("select DISTINCT i.material from PLMVarianceAffectedMaterial i")
    List<Integer> findDistinctMaterial();

    @Query(value = "select i.material,count(i.material) from  plm_variance_affected_material i group by i.material order by count (i.material) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getRecurringParts();

}
