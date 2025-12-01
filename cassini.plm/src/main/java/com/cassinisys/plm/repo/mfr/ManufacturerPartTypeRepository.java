package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerPartTypeRepository extends JpaRepository<PLMManufacturerPartType, Integer> {
    List<PLMManufacturerPartType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMManufacturerPartType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMManufacturerPartType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMManufacturerPartType> findByParentTypeOrderByIdAsc(Integer parent);

    PLMManufacturerPartType findByName(String name);

    PLMManufacturerPartType findByNameEqualsIgnoreCase(String name);

    List<PLMManufacturerPartType> findByIdIn(List<Integer> ids);

    List<PLMManufacturerPartType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMManufacturerPartType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMManufacturerPartType> findByLifecycle(PLMLifeCycle lc);
}
