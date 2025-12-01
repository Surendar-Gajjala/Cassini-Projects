package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.AppNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by GSR on 15-10-2018.
 */
@Repository
public interface AppNotificationRepository extends JpaRepository<AppNotification, Integer> {
}
