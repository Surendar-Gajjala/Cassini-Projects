package com.cassinisys.erp.repo.security;

import com.cassinisys.erp.model.security.ERPRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.security.ERPLogin;

import java.util.List;

/**
 * Created by reddy on 7/25/15.
 */
@Repository
public interface LoginRepository extends JpaRepository<ERPLogin, Integer> {
    String GET_BY_LOGIN_NAME =
            "SELECT l FROM ERPLogin l WHERE l.loginName= :loginName";
    @Query(GET_BY_LOGIN_NAME)
    ERPLogin findByLoginName(@Param("loginName") String loginName);

    String GET_BY_PERSON_ID =
            "SELECT l FROM ERPLogin l WHERE l.person.id= :personId";
    @Query(GET_BY_PERSON_ID)
    ERPLogin findByPersonId(@Param("personId") Integer personId);


}
