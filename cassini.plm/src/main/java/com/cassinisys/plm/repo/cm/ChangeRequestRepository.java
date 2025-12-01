package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMChangeRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ChangeRequestRepository extends JpaRepository<PLMChangeRequest, Integer>, QueryDslPredicateExecutor<PLMChangeRequest> {
    @Query(value = "SELECT count(i) FROM plm_change_request i  JOIN plm_change ct ON ct.change_id = i.id JOIN plm_changetype changetype ON changetype.type_id = i.cr_type" +
            " WHERE changetype.change_reason_types= :lov AND ct.change_reason_type= :lovValue", nativeQuery = true)
    Integer getChangeTypeLovValueCount(@Param("lovValue") String lovValue, @Param("lov") Integer lov);


    
}
