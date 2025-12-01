package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.PLMGlossaryRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@Repository
public interface GlossaryRevisionStatusHistoryRepository extends JpaRepository<PLMGlossaryRevisionStatusHistory, Integer> {

    List<PLMGlossaryRevisionStatusHistory> findByGlossaryOrderByTimeStampDesc(Integer glossary);
}
