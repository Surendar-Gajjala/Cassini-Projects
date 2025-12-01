package com.cassinisys.plm.repo.req;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.req.PLMRequirementDocument;
import com.cassinisys.plm.model.req.PLMRequirementDocumentRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Repository
public interface PLMRequirementDocumentRevisionRepository extends JpaRepository<PLMRequirementDocumentRevision, Integer> {

    List<PLMRequirementDocumentRevision> findByIdIn(Iterable<Integer> varl);

    List<PLMRequirementDocumentRevision> getByMasterOrderByCreatedDateDesc(PLMRequirementDocument master);

    @Query("select i.id from PLMRequirementDocumentRevision i where i.master.id= :reqDocId order by i.id asc")
    List<Integer> getRevisionIdsByReqDocId(@Param("reqDocId") Integer reqDocId);

    @Query("select i.id from PLMRequirementDocumentRevision i where i.lifeCyclePhase.phase= :phase")
    List<Integer> getRevisionIdsByPhase(@Param("phase") String phase);

    @Query("select i.id from PLMRequirementDocumentRevision i where i.owner.id= :owner")
    List<Integer> getRevisionIdsByOwner(@Param("owner") Integer owner);

    @Query("select distinct i.owner from PLMRequirementDocumentRevision i")
    List<Person> getReqDocOwnerIds();

}
