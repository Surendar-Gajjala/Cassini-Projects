package com.cassinisys.plm.repo.mes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cassinisys.plm.model.mes.MESManpowerContact;

import java.util.List;


@Repository
public interface ManpowerContactRepository extends JpaRepository<MESManpowerContact, Integer>, QueryDslPredicateExecutor<MESManpowerContact>{

    List<MESManpowerContact> findByManpower(Integer manpowerId);

    MESManpowerContact findByContactAndManpower(Integer personId, Integer manpowerId);

    @Query("select i.contact from MESManpowerContact i where i.contact not in :ids")
    List<Integer> getContactsByIdNotIn(@Param("ids") Iterable<Integer> ids);

    @Query("select distinct i.contact from MESManpowerContact i where i.active = true")
    List<Integer> getUniqueContacts();

    
}
