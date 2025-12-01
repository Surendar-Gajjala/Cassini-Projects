package com.cassinisys.is.repo.pm;
/**
 * The Class is for ProjectWbsRepository
 **/

import com.cassinisys.is.model.pm.ISLinks;
import com.cassinisys.is.model.pm.ISProjectWbs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinksRepository extends
        JpaRepository<ISLinks, Integer> {

        ISLinks findByProject(Integer projectId);
}
