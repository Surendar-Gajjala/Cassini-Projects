package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMPersonOtherInfo;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rajabrahmachary on 10-08-2016.
 */
@Repository
public interface PersonOtherInfoRepository extends JpaRepository<TMPersonOtherInfo,Integer> {

    TMPersonOtherInfo findByPerson(Integer personId);
    List<TMPersonOtherInfo> findByRole(String personRole);
}
