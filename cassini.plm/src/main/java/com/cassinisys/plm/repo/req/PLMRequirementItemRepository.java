package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 08-07-2021.
 */
@Repository
public interface PLMRequirementItemRepository extends JpaRepository<PLMRequirementItem, Integer> {

    @Query("select i from PLMRequirementItem i where i.requirementVersion.id= :id order by i.id desc")
    List<PLMRequirementItem> getItemsByRequirement(@Param("id") Integer id);

    @Query("select count (i) from PLMRequirementItem i where i.requirementVersion.id= :id")
    Integer getItemsByRequirementCount(@Param("id") Integer id);

    @Query("select i from PLMRequirementItem i where i.item.id= :id order by i.id desc")
    List<PLMRequirementItem> getRequirementsByItem(@Param("id") Integer id);

    @Query("select count (i) from PLMRequirementItem i where i.item.id= :id")
    Integer getRequirementsByItemCount(@Param("id") Integer id);
}


