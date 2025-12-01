package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSubstanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSubstanceTypeRepository extends JpaRepository<PGCSubstanceType, Integer> {

    List<PGCSubstanceType> findByIdIn(Iterable<Integer> ids);

    List<PGCSubstanceType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PGCSubstanceType> findByParentTypeIsNullOrderByIdAsc();

    PGCSubstanceType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PGCSubstanceType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PGCSubstanceType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<PGCSubstanceType> findByParentTypeOrderByIdAsc(Integer id);

    PGCSubstanceType findByName(String name);

    List<PGCSubstanceType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
}
