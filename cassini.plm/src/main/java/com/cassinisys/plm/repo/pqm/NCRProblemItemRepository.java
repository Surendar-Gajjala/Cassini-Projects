package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pqm.PQMNCRProblemItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface NCRProblemItemRepository extends JpaRepository<PQMNCRProblemItem, Integer> {

    @Query("select i from PQMNCRProblemItem i where i.ncr= :id")
    List<PQMNCRProblemItem> findByNcr(@Param("id") Integer id);

    List<PQMNCRProblemItem> findByMaterial(PLMManufacturerPart ncr);

    @Query(value = "select mp.manufacturer,count(DISTINCT i.ncr) from pqm_ncr_problem_item i JOIN plm_manufacturerpart mp ON i.material = mp.mfrpart_id group by mp.manufacturer order by count (i.ncr) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getManufacturersForNCRCounts();

    @Query(value = "select mp.manufacturer,count(DISTINCT i.material) from pqm_ncr_problem_item i JOIN plm_manufacturerpart mp ON i.material = mp.mfrpart_id group by mp.manufacturer order by count (i.material) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getManufacturerCounts();


    @Query(value = "select i.material,count(i.material) from pqm_ncr_problem_item i group by i.material order by count (i.material) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getProblemPartsCounts();
}
