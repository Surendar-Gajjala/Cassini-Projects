package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMManufacturerTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/25/2016.
 */
@Repository
public interface ManufacturerTypeAttributeRepository extends JpaRepository<PLMManufacturerTypeAttribute, Integer> {
    List<PLMManufacturerTypeAttribute> findByMfrTypeOrderByName(Integer id);

    List<PLMManufacturerTypeAttribute> findByMfrTypeOrderBySeq(Integer id);

    PLMManufacturerTypeAttribute findByMfrTypeAndName(Integer id, String name);

    List<PLMManufacturerTypeAttribute> findByMfrType(Integer id);
}
