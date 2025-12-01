package com.cassinisys.erp.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.common.ERPDistrict;

@Repository
public interface DistrictRepository  extends JpaRepository<ERPDistrict, Integer> {
	
	public static final String GET_DISTRICTS_BY_STATE =
            "SELECT d FROM ERPDistrict d WHERE d.state= :state";

    @Query(GET_DISTRICTS_BY_STATE)
	List<ERPDistrict> getDistrictByState(@Param("state") Integer state);

}
