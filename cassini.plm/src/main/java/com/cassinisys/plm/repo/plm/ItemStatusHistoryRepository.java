package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemStatusHistoryRepository extends JpaRepository<PLMItemStatusHistory, Integer> {
    List<PLMItemStatusHistory> findByItem(PLMItem item);
}
