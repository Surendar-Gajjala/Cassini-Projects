package com.cassinisys.plm.repo.req;

import com.cassinisys.plm.model.req.PLMRequirementRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by CassiniSystems on 09-11-2020.
 */
@Repository
public interface PLMRequirementRelatedItemRepository extends JpaRepository<PLMRequirementRelatedItem, Integer> {

}
