package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryEntryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 19-06-2018.
 */
@Repository
public interface GlossaryEntryHistoryRepository extends JpaRepository<PLMGlossaryEntryHistory, Integer> {
}
