package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMLibraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Repository
public interface LibraryItemRepository extends JpaRepository<PLMLibraryItem, Integer> {
}
