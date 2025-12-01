package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementDocumentRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementDocumentRelatedItemRepository extends JpaRepository<PLMRequirementDocumentRelatedItem, Integer> {

}
