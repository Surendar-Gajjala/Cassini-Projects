package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMLayoutDrawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface LayoutDrawingRepository extends JpaRepository<TMLayoutDrawing, Integer>{

    List<TMLayoutDrawing> findByDateIn(LocalDate date);
}
