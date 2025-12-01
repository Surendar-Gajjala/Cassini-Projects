package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.plm.PLMSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 29-09-2018.
 */
@Repository
public interface SubscribeRepository extends JpaRepository<PLMSubscribe, Integer> {

    PLMSubscribe findByObjectId(Integer person);

    @Query("SELECT i from PLMSubscribe i where i.objectId= :itemId and i.person.email is not null and i.subscribe='true' ")
    List<PLMSubscribe> getByObjectIdAndSubscribeTrue(@Param("itemId") Integer itemId);

    PLMSubscribe findByPersonAndObjectId(Person person, Integer itemId);

    Page<PLMSubscribe> findByPersonAndSubscribeTrue(Person person, Pageable pageable);

    List<PLMSubscribe> findByObjectIdAndSubscribeTrue(Integer objectId);

    List<PLMSubscribe> findByPersonAndSubscribeTrue(Person person);

}
