package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTELocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Repository
public interface IRSTELocationRepository extends JpaRepository<IRSTELocation, Integer> {

    List<IRSTELocation> findByGroupId(Integer groupId);

    List<IRSTELocation> findByIdIn(List<Integer> groupId);

    @Modifying
    @Transactional
    @Query("delete from IRSTELocation list where list.groupId = :groupId")
    void deleteByGroupId(@Param("groupId") Integer groupId);

}
