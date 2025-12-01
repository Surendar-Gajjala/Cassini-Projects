package com.cassinisys.erp.repo.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.common.ERPPersonType;

@Repository
public interface PersonRepository extends JpaRepository<ERPPerson, Integer>{

    @Query (
        "SELECT p FROM ERPPerson p WHERE p.firstName= :firstName AND p.personType= :personType"
    )
    ERPPerson findByFirstNameAndPersonType(@Param("firstName") String firstName, @Param("personType")ERPPersonType personType);
}
