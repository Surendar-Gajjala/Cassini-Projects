package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 20-11-2018.
 */
@Repository
public interface IRSTEUserRepository extends JpaRepository<IRSTEUser, Integer>,
        QueryDslPredicateExecutor<IRSTEUser> {

    Page<IRSTEUser> findByPersonType(Pageable pageable, Integer personType);

    List<IRSTEUser> findByPersonType(Integer personType);

}
