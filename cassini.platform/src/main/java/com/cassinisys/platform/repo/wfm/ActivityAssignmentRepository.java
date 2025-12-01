package com.cassinisys.platform.repo.wfm;

import com.cassinisys.platform.model.wfm.ActivityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Created by Rajabrahmachary on 16-05-2016.
 */
public interface ActivityAssignmentRepository  extends JpaRepository<ActivityAssignment, Integer> {



    List<ActivityAssignment> findByActivity(Integer activity);
    List<ActivityAssignment> findByAssignedTo(Integer activity);
}
