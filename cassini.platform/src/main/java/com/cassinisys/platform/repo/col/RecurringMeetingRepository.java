package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.RecurringMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author reddy
 */
public interface RecurringMeetingRepository extends
		JpaRepository<RecurringMeeting, Integer> {

}
