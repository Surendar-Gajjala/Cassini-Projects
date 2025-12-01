package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemTypeSpecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 21-10-2018.
 */
@Repository
public interface ItemTypeSpecsRepository extends JpaRepository<ItemTypeSpecs, Integer> {

    List<ItemTypeSpecs> findByItemType(Integer typeId);

    ItemTypeSpecs findByItemTypeAndSpecName(Integer typeId, String name);
}


