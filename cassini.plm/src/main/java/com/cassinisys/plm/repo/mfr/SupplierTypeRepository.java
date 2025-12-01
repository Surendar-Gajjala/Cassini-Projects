package com.cassinisys.plm.repo.mfr;

import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 25-11-2020.
 */
@Repository
public interface SupplierTypeRepository extends JpaRepository<PLMSupplierType, Integer> {
    List<PLMSupplierType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMSupplierType> findByParentTypeIsNullOrderByIdAsc();

    List<PLMSupplierType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMSupplierType> findByParentTypeOrderByIdAsc(Integer parent);

    PLMSupplierType findByName(String name);

    List<PLMSupplierType> findByIdIn(List<Integer> ids);

    PLMSupplierType findByNameEqualsIgnoreCaseAndParentTypeIsNull(String name);

    PLMSupplierType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PLMSupplierType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<PLMSupplierType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    List<PLMSupplierType> findByLifecycle(PLMLifeCycle lc);

    @Query("select count (i) from PLMSupplierType i where i.autoNumberSource.id= :autoNumber")
    Integer getSupplierTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
