package com.cassinisys.plm.repo.pqm;

import java.util.List;

import com.cassinisys.plm.model.pqm.PQMPPAPType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PPAPTypeRepository extends JpaRepository<PQMPPAPType,Integer> {
    List<PQMPPAPType> findByIdIn(Iterable<Integer> ids);

    List<PQMPPAPType> findByParentTypeIsNullOrderByCreatedDateAsc();

    PQMPPAPType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMPPAPType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMPPAPType> findByParentTypeOrderByCreatedDateAsc(Integer parent);
}
