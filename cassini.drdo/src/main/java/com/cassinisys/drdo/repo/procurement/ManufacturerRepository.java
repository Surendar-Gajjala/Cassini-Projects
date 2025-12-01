package com.cassinisys.drdo.repo.procurement;

import com.cassinisys.drdo.model.procurement.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyam reddy on 10-12-2018.
 */
@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer>, QueryDslPredicateExecutor<Manufacturer> {

    Manufacturer findByMfrCodeIgnoreCase(String code);
}
