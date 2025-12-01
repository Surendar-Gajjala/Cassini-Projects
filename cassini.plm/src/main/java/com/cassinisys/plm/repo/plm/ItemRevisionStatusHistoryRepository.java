package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemRevisionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 023 23-May -17.
 */
@Repository
public interface ItemRevisionStatusHistoryRepository extends JpaRepository<PLMItemRevisionStatusHistory, Integer> {
    List<PLMItemRevisionStatusHistory> findByItemOrderByTimestampDesc(Integer id);
}
