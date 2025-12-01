package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMProjectRequirementDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 08-07-2021.
 */
@Repository
public interface PLMProjectRequirementDocumentRepository extends JpaRepository<PLMProjectRequirementDocument, Integer> {

    @Query("select i from PLMProjectRequirementDocument i where i.project.id= :id order by i.id desc")
    List<PLMProjectRequirementDocument> getReqDocumentsByProject(@Param("id") Integer id);

    @Query("select count (i) from PLMProjectRequirementDocument i where i.project.id= :id")
    Integer getReqDocumentsByProjectCount(@Param("id") Integer id);

}
