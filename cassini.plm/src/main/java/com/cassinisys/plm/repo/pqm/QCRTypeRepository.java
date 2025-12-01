package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.pqm.PQMQCRType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRTypeRepository extends JpaRepository<PQMQCRType, Integer> {
    List<PQMQCRType> findByIdIn(Iterable<Integer> ids);

    List<PQMQCRType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PQMQCRType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    PQMQCRType findByName(String name);

    PQMQCRType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PQMQCRType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PQMQCRType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
}
