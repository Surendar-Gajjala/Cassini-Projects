package com.cassinisys.is.repo.procm;
/**
 * The Class is for BidBoqRepository
 **/

import com.cassinisys.is.model.procm.ISItemTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeAttributeRepository extends JpaRepository<ISItemTypeAttribute, Integer> {
    /**
     * The method used to findByItemType of ISItemTypeAttribute
     **/
    List<ISItemTypeAttribute> findByItemType(Integer typeId);
}
