package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentTemplateElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 10-11-2020.
 */
@Repository
public interface PLMRequirementDocumentTemplateElementRepository extends JpaRepository<PLMRequirementDocumentTemplateElement, Integer> {
}
