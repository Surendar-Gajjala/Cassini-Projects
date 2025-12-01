package com.cassinisys.plm.repo.pm;
/**
 * The Class is for ProjectWbsRepository
 **/

import com.cassinisys.plm.model.pm.PLMLinks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinksRepository extends
        JpaRepository<PLMLinks, Integer> {

        PLMLinks findByProject(Integer projectId);
}
