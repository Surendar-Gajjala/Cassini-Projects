package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerTypeRepository extends JpaRepository<PLMManufacturerType, Integer> {
    List<PLMManufacturerType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMManufacturerType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMManufacturerType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMManufacturerType> findByParentTypeOrderByIdAsc(Integer parent);

    PLMManufacturerType findByName(String name);

    List<PLMManufacturerType> findByIdIn(List<Integer> ids);

    List<PLMManufacturerType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMManufacturerType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMManufacturerType> findByLifecycle(PLMLifeCycle lc);

}
