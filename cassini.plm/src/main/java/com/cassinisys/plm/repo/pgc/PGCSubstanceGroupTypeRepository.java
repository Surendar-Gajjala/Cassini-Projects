package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSubstanceGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSubstanceGroupTypeRepository extends JpaRepository<PGCSubstanceGroupType, Integer> {

    List<PGCSubstanceGroupType> findByIdIn(Iterable<Integer> ids);

    List<PGCSubstanceGroupType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PGCSubstanceGroupType> findByParentTypeIsNullOrderByIdAsc();

    PGCSubstanceGroupType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PGCSubstanceGroupType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PGCSubstanceGroupType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<PGCSubstanceGroupType> findByParentTypeOrderByIdAsc(Integer id);

    PGCSubstanceGroupType findByName(String name);

    List<PGCSubstanceGroupType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
}
