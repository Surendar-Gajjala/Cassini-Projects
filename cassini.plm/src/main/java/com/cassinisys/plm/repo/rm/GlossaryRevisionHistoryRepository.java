package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryRevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 19-06-2018.
 */
@Repository
public interface GlossaryRevisionHistoryRepository extends JpaRepository<PLMGlossaryRevisionHistory, Integer> {
}
