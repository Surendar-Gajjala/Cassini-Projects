package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMMarkup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Suresh Cassini on 31-10-2019.
 */
@Repository
public interface MarkupRepository extends CrudRepository<PLMMarkup, Integer> {
}
