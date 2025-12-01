package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.model.rm.SpecificationDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 24-10-2018.
 */
@Repository
public interface SpecificationDeliverableRepository extends JpaRepository<SpecificationDeliverable, Integer> {

    List<SpecificationDeliverable> findByObjectId(Integer projectId);

    List<SpecificationDeliverable> findByObjectIdAndObjectType(Integer objectId, String objectType);

    SpecificationDeliverable findByObjectIdAndSpecification(Integer projectId, Specification specification);
}
