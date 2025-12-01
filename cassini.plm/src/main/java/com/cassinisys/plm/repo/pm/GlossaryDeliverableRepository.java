package com.cassinisys.plm.repo.pm;

import com.cassinisys.plm.model.pm.PLMGlossaryDeliverable;
import com.cassinisys.plm.model.rm.PLMGlossary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 10-09-2018.
 */
@Repository
public interface GlossaryDeliverableRepository extends JpaRepository<PLMGlossaryDeliverable, Integer> {

    List<PLMGlossaryDeliverable> findByObjectId(Integer projectId);

    List<PLMGlossaryDeliverable> findByObjectIdAndObjectType(Integer objectId, String objectType);

    PLMGlossaryDeliverable findByObjectIdAndGlossary(Integer projectId, PLMGlossary glossary);

}
