package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementTypeRepository extends JpaRepository<PLMRequirementType, Integer> {

    List<PLMRequirementType> findByIdIn(Iterable<Integer> ids);

    List<PLMRequirementType> findByParentIsNullOrderByCreatedDateAsc();

    List<PLMRequirementType> findByParentIsNullOrderByIdAsc();

    List<PLMRequirementType> findByParentOrderByCreatedDateAsc(Integer id);

    List<PLMRequirementType> findByParentOrderByIdAsc(Integer id);

    List<PLMRequirementType> findByPriorityListId(Integer lovId);

    PLMRequirementType findByParentIsNullAndNameEqualsIgnoreCase(String name);

    PLMRequirementType findByNameEqualsIgnoreCaseAndParent(String name, Integer parentType);

    @Query("select count (i) from PLMRequirementType i where i.priorityList.id= :lovId")
    Integer getRequirementTypesByLov(@Param("lovId") Integer lovId);
    
    @Query("SELECT count(pr) FROM com.cassinisys.plm.model.req.PLMRequirementVersion pr,com.cassinisys.plm.model.req.PLMRequirement r, com.cassinisys.plm.model.req.PLMRequirementType prt WHERE pr.master.id = r.id and r.type.id = prt.id and pr.priority= :lovValue AND prt.priorityList.id= :lov")
    Integer getTypeLovValueCount(@Param("lov") Integer lov, @Param("lovValue") String lovValue);

    PLMRequirementType findByName(String name);

    List<PLMRequirementType> findByParentAndNameEqualsIgnoreCase(Integer parentType, String name);
}
