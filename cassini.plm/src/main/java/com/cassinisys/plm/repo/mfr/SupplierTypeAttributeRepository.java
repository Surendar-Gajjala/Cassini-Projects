package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMSupplierTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Repository
public interface SupplierTypeAttributeRepository extends JpaRepository<PLMSupplierTypeAttribute, Integer> {
    List<PLMSupplierTypeAttribute> findByTypeOrderByName(Integer id);

    List<PLMSupplierTypeAttribute> findByTypeOrderBySeq(Integer id);

    PLMSupplierTypeAttribute findByTypeAndName(Integer id, String name);

    List<PLMSupplierTypeAttribute> findByType(Integer id);
}
