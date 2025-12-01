package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.GlossaryEntryPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Gsr on 23-11-2018.
 */
@Repository
public interface GlossaryEntryPermissionRepository extends JpaRepository<GlossaryEntryPermission, Integer> {

    List<GlossaryEntryPermission> findByGlossary(Integer glossaryId);

    GlossaryEntryPermission findByGlossaryAndGlossaryUser(Integer glossaryId, Integer personId);
}
