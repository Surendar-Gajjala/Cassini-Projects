package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.ObjectMailSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 16-11-2018.
 */
@Repository
public interface ObjectMailSettingsRepository extends JpaRepository<ObjectMailSettings , Integer> {
    ObjectMailSettings findByObjectId(Integer id);
}
