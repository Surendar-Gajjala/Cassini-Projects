package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 20-01-2017.
 */
@Repository
public interface ItemAttributeRepository extends JpaRepository<PDMItemAttribute, Integer> {

    @Query(
            "SELECT a FROM PDMItemAttribute a WHERE a.id.objectId= :itemId"
    )
    List<PDMItemAttribute> findByItemId(@Param("itemId") Integer itemId);
}
