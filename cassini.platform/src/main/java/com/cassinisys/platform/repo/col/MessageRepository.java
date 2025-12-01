package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author reddy
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

	public Page<Message> findBySentToId(Integer sentToId, Pageable pageable);
	
	public Page<Message> findBySentBy(Integer personId, Pageable pageable);

}
