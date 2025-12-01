package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.model.plm.PLMItemRevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRevisionHistoryRepository extends JpaRepository<PLMItemRevisionHistory, Integer> {
    List<PLMItemRevisionHistory> findByItem(PLMItem item);
}
