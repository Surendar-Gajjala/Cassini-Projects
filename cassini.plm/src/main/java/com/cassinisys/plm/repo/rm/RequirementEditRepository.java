package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RequirementEdit;
import com.cassinisys.plm.model.rm.RequirementEditStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementEditRepository extends JpaRepository<RequirementEdit, Integer> {

    List<RequirementEdit> findByRequirementAndStatusOrderByAcceptedDateDesc(Integer req, RequirementEditStatus status);

    List<RequirementEdit> findByRequirementOrderByEditedDateDesc(Integer reqId);

    List<RequirementEdit> findByRequirementOrderByEditedDateAsc(Integer reqId);

    RequirementEdit findByRequirementAndStatus(Integer reqId, RequirementEditStatus status);

    RequirementEdit findByRequirement(Integer reqId);
}