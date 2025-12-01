package com.cassinisys.plm.repo.pqm;

import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.pqm.PQMQualityTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QualityTypeAttributeRepository extends JpaRepository<PQMQualityTypeAttribute, Integer> {

    List<PQMQualityTypeAttribute> findByTypeAndRevisionSpecificFalseOrderBySeq(Integer type);

    List<PQMQualityTypeAttribute> findByTypeAndRevisionSpecificTrueOrderBySeq(Integer type);

    List<PQMQualityTypeAttribute> findByTypeOrderBySeq(Integer type);

    List<PQMQualityTypeAttribute> findByObjectType(ObjectType objectType);

    PQMQualityTypeAttribute findByNameAndObjectType(String name, ObjectType objectType);
    PQMQualityTypeAttribute findByTypeAndName(Integer typeId, String name);
}
