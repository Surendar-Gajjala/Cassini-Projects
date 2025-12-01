package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMPRProblemItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface PRProblemItemRepository extends JpaRepository<PQMPRProblemItem, Integer> {

    @Query("select i from PQMPRProblemItem i where i.problemReport= :problemReport")
    List<PQMPRProblemItem> findByProblemReport(@Param("problemReport") Integer problemReport);

    @Query("select i.item from PQMPRProblemItem i where i.problemReport= :problemReport")
    List<Integer> getItemsByProblemReport(@Param("problemReport") Integer problemReport);

    PQMPRProblemItem findByItemAndProblemReport(Integer item, Integer problemReport);

    List<PQMPRProblemItem> findByItem(Integer itemId);

    @Query("select distinct count (i.problemReport) from PQMPRProblemItem i where i.item= :itemId")
    Integer getProblemReportCountByItem(@Param("itemId") Integer itemId);

    @Query(value = "select j.item_master,count(j.item_master) from pqm_pr_problem_item i JOIN plm_itemrevision j ON j.item_id = i.item group by j.item_master order by count (j.item_master) desc LIMIT 10", nativeQuery = true)
    List<Object[]> getTopProductItems();

    @Query(value = "select i.item_type,count(i.item_id) from plm_item i JOIN plm_itemrevision k ON i.item_id=k.item_master JOIN pqm_pr_problem_item j ON j.item=k.item_id GROUP BY i.item_type  ORDER BY count(i.item_type) DESC LIMIT 10", nativeQuery = true)
    List<Object[]> getTopProblemItemTypes();
}
