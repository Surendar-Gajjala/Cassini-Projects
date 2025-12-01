package com.cassinisys.erp.repo.hrm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPTimeOffType;

@Repository
public interface TimeOffTypeRepository extends JpaRepository<ERPTimeOffType, Integer> {
	
	public static final String GET_TIME_OFF_BY_TYPE_CODE =
            "SELECT off FROM ERPTimeOffType off WHERE off.typeCode= :typeCode";

    @Query(GET_TIME_OFF_BY_TYPE_CODE)
	ERPTimeOffType getTimeOffs(@Param("typeCode") String typeCode);
    

}
