package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerPartTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerPartTypeAttributeRepository extends JpaRepository<PLMManufacturerPartTypeAttribute, Integer> {
    List<PLMManufacturerPartTypeAttribute> findByMfrPartTypeOrderByName(Integer id);

    List<PLMManufacturerPartTypeAttribute> findByMfrPartTypeOrderBySeq(Integer id);

    PLMManufacturerPartTypeAttribute findByMfrPartTypeAndName(Integer id, String name);

    List<PLMManufacturerPartTypeAttribute> findByMfrPartType(Integer id);
}
