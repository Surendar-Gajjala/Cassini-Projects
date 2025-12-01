package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.UserInbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Nageshreddy on 04-05-2016.
 */

public interface UserInboxRepository extends JpaRepository<UserInbox, Integer> {
    Page<UserInbox> findByUserId(Integer id,Pageable pageable);
    List<UserInbox> findByUserIdAndMessageRead(Integer id, Boolean read);
}
