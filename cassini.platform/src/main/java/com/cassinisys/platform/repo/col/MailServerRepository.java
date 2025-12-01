package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.MailServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 16-11-2018.
 */
@Repository
public interface MailServerRepository extends JpaRepository<MailServer , Integer> {
    MailServer findByName(String name);
}
