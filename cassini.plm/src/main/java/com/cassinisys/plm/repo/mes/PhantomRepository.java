package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESPhantom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by smukka on 13-05-2022.
 */
@Repository
public interface PhantomRepository extends JpaRepository<MESPhantom, Integer> {
}
