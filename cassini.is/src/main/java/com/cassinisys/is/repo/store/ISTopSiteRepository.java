package com.cassinisys.is.repo.store;

import com.cassinisys.is.model.store.ISTopSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Repository
public interface ISTopSiteRepository extends JpaRepository<ISTopSite, Integer> {
}
