package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemTypeRepository extends JpaRepository<PDMItemType, Integer>{
    List<PDMItemType> findByParentTypeIsNullOrderByCreatedDateAsc();
    List<PDMItemType> findByParentTypeOrderByCreatedDateAsc(Integer parent);
    PDMItemType findByName(String itemType);
}
