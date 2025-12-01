package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 04-10-2018.
 */
@Repository
public interface ItemTypeAttributeRepository extends JpaRepository<ItemTypeAttribute, Integer> {

    List<ItemTypeAttribute> findByItemTypeOrderByName(Integer typeId);
}
