package com.cassinisys.irste.repo;

import com.cassinisys.irste.model.IRSTEGroupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2018.
 */
@Repository
public interface IRSTEGroupLocationRepository extends JpaRepository<IRSTEGroupLocation, Integer> {

}
