package com.cassinisys.is.repo.col;
/**
 * The Class is for ProjectMeetingRepository
 **/

import com.cassinisys.is.model.col.ISProjectMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMeetingRepository extends
        JpaRepository<ISProjectMeeting, Integer> {
    /**
     * The method used to findByProject for ISProjectMeeting
     **/
    public Page<ISProjectMeeting> findByProject(Integer projectId,
                                                Pageable pageable);

    /**
     * The method used to findByIdIn for ISProjectMeeting
     **/
    public List<ISProjectMeeting> findByIdIn(Iterable<Integer> ids);

}
