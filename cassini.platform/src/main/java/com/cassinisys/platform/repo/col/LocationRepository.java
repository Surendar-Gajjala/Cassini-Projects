package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam reddy on 29-11-2018.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
}
