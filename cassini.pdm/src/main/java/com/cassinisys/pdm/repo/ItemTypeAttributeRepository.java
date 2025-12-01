package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItemTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemTypeAttributeRepository extends JpaRepository<PDMItemTypeAttribute, Integer> {
    List<PDMItemTypeAttribute> findByItemType(Integer typeId);
}
