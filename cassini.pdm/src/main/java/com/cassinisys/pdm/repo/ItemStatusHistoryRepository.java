package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItemStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemStatusHistoryRepository extends JpaRepository<PDMItemStatusHistory, Integer>{

}
