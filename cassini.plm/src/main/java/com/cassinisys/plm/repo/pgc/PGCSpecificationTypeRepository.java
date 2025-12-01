package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCSpecificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCSpecificationTypeRepository extends JpaRepository<PGCSpecificationType, Integer> {

    List<PGCSpecificationType> findByIdIn(Iterable<Integer> ids);

    List<PGCSpecificationType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PGCSpecificationType> findByParentTypeIsNullOrderByIdAsc();

    PGCSpecificationType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PGCSpecificationType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);


    List<PGCSpecificationType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<PGCSpecificationType> findByParentTypeOrderByIdAsc(Integer id);

    PGCSpecificationType findByName(String name);

    List<PGCSpecificationType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
}
