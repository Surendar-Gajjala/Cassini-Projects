package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.Requirement;
import com.cassinisys.plm.model.rm.RequirementDeliverable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 24-10-2018.
 */
@Repository
public interface RequirementDeliverableRepository extends JpaRepository<RequirementDeliverable, Integer> {
    List<RequirementDeliverable> findByObjectId(Integer projectId);

    List<RequirementDeliverable> findByObjectIdAndObjectType(Integer objectId, String objectType);

    RequirementDeliverable findByObjectIdAndRequirement(Integer projectId, Requirement requirement);

    List<RequirementDeliverable> findByRequirement(Requirement requirement);

    List<RequirementDeliverable> findByRequirementAndObjectType(Requirement requirement, String objectType);

    List<RequirementDeliverable> findByObjectIdAndObjectType(Requirement requirement, String objectType);

    RequirementDeliverable findByRequirementAndObjectIdAndObjectType(Requirement requirement, Integer objectId, String objectType);
}
