package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEComplaintHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Repository
public interface IRSTEComplaintHistoryRepository extends JpaRepository<IRSTEComplaintHistory, Integer> {

    List<IRSTEComplaintHistory> findByComplaint(Integer id);
}
