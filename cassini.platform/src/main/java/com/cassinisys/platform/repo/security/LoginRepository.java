package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.core.Login;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author reddy
 */
@Repository
public interface LoginRepository extends JpaRepository<Login, Integer>, QueryDslPredicateExecutor<Login> {
    String GET_BY_LOGIN_NAME =
            "SELECT l FROM Login l WHERE l.loginName= :loginName";
    String GET_BY_PERSON_ID =
            "SELECT l FROM Login l WHERE l.person.id= :personId";

    @Query(GET_BY_LOGIN_NAME)
    Login findByLoginName(@Param("loginName") String loginName);

    @Query(GET_BY_PERSON_ID)
    Login findByPersonId(@Param("personId") Integer personId);

    @Query("SELECT COUNT(l) FROM Login l WHERE l.isActive= :active")
    Integer findIsActiveLogins(@Param("active") Boolean active);

    @Query("SELECT COUNT(l) FROM Login l WHERE l.isActive= :active and l.external= :external")
    Integer findIsActiveAndExternalLoginsCount(@Param("active") Boolean active, @Param("external") Boolean external);

    @Query(
            "SELECT i FROM Login i WHERE i.person.id IN :personIds"
    )
    List<Login> findByPersonIn(@Param("personIds") List<Integer> personId);

    @Query(
            "SELECT i FROM Login i ORDER BY i.loginName ASC"
    )
    List<Login> getLoginsByAlphabeticalOrder();

    @Query(
            "SELECT i FROM Login i WHERE i.isActive = true ORDER BY i.person.firstName ASC"
    )
    Page<Login> findByIsActiveTrue(Pageable pageable);

    List<Login> findByIsActiveTrueAndExternalFalse();

    List<Login> findByExternalFalse();

    List<Login> findByIsActive(Boolean flag);

    List<Login> findByIsActiveAndExternal(Boolean flag, Boolean flag1);

    @Query(value = "select count(*) from login where  login_name = :loginName", nativeQuery = true)
    Integer getCountOfUsers(@Param("loginName") String loginName);

    @Query("select i.person.id from Login i")
    List<Integer> getLoginPersonIds();

    @Query("select count (i.isAdmin) from Login i where i.isAdmin = true")
    Integer getIsAdminCount();

    @Query("select distinct i from Login i where i.person.id in :ids")
    List<Login> getLoginsByPersonIds(@Param("ids") List<Integer> ids);
}
