package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.MeetingAttendee;
import com.cassinisys.platform.model.col.MeetingAttendeeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author reddy
 */
public interface MeetingAttendeeRepository extends
		JpaRepository<MeetingAttendee, Integer> {

	public Page<MeetingAttendee> findById(MeetingAttendeeId id, Pageable pageable);

	@Query(
			"SELECT a FROM MeetingAttendee a WHERE a.id.meetingId = :meetingId"
	)
	List<MeetingAttendee> findByMeetingId(@Param("meetingId") Integer meetingId);

}
