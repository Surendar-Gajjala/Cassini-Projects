package com.cassinisys.erp.repo.hrm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cassinisys.erp.model.hrm.ERPPayroll;

@Repository
public interface PayrollRepository extends JpaRepository<ERPPayroll, Integer> {

	List<ERPPayroll> findByYear(Integer year);

	@Query("SELECT p FROM ERPPayroll p where p.year = :year AND p.month = :month")
	public List<ERPPayroll> findByYearAndMonth(@Param("year") Integer year,@Param("month") Integer month);
}
