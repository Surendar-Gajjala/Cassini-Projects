package com.cassinisys.is.repo.pm;

import com.cassinisys.is.model.pm.ProjectBoqAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 18-02-2020.
 */
@Repository
public interface ProjectBoqAttachmentsRepository extends JpaRepository<ProjectBoqAttachment, Integer> {

    ProjectBoqAttachment findByProject(Integer project);
}
