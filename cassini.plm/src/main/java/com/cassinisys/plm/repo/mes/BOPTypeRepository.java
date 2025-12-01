package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESBOPType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BOPTypeRepository extends JpaRepository<MESBOPType, Integer> {

    List<MESBOPType> findByIdIn(Iterable<Integer> ids);

    List<MESBOPType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESBOPType> findByParentTypeIsNullOrderByIdAsc();

    List<MESBOPType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESBOPType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESBOPType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESBOPType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESBOPType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESBOPType findByName(String name);
}
