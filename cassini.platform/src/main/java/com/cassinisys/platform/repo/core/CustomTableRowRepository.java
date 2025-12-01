package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.CustomTableRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 02-11-2018.
 */
@Repository
public interface CustomTableRowRepository extends
        JpaRepository<CustomTableRow, Integer> {

}
