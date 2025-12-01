package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.MessageSentTo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author reddy
 */
public interface MessageSentToRepository extends
		JpaRepository<MessageSentTo, Integer> {

}
