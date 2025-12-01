package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.req.PLMRequirementDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementDocumentTypeRepository extends JpaRepository<PLMRequirementDocumentType, Integer> {

    List<PLMRequirementDocumentType> findByIdIn(Iterable<Integer> ids);

    List<PLMRequirementDocumentType> findByParentIsNullOrderByCreatedDateAsc();

    List<PLMRequirementDocumentType> findByParentIsNullOrderByIdAsc();

    List<PLMRequirementDocumentType> findByParentOrderByIdAsc(Integer id);

    List<PLMRequirementDocumentType> findByParentOrderByCreatedDateAsc(Integer id);

    List<PLMRequirementDocumentType> findByRevisionSequenceId(Integer lovId);

    PLMRequirementDocumentType findByParentIsNullAndNameEqualsIgnoreCase(String name);

    PLMRequirementDocumentType findByNameEqualsIgnoreCaseAndParent(String name, Integer parentType);

    @Query("select count (i) from PLMRequirementDocumentType i where i.revisionSequence.id= :lovId")
    Integer getReqDocTypeByLov(@Param("lovId") Integer lovId);

    @Query(value = "SELECT count(pr) FROM plm_requirementdocument pr JOIN plm_requirementdocumentrevision rdr ON pr.id = rdr.master JOIN plm_requirementdocumenttype rdt ON rdt.id = pr.type WHERE rdr.revision= :lovValue AND rdt.revision_sequence= :lov", nativeQuery = true)
    Integer getTypeLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);

    PLMRequirementDocumentType findByName(String name);

    List<PLMRequirementDocumentType> findByParentAndNameEqualsIgnoreCase(Integer parentType, String name);

    @Query("select distinct i.lifecycle from PLMRequirementDocumentType i")
    List<PLMLifeCycle> getUniqueReqDocTypeLifeCycles();
}
