package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMProjectItemReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 031 31-Jan -18.
 */
@Repository
public interface ProjectItemReferenceRepository extends JpaRepository<PLMProjectItemReference, Integer>, QueryDslPredicateExecutor<PLMProjectItemReference> {

    List<PLMProjectItemReference> findByProject(PLMProject project);

    PLMProjectItemReference findByProjectAndItem(PLMProject project, PLMItemRevision itemRevision);

}
