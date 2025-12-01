package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.BomGroup;
import com.cassinisys.drdo.model.bom.BomItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam reddy on 08-11-2018.
 */
@Repository
public interface BomGroupRepository extends JpaRepository<BomGroup, Integer>, QueryDslPredicateExecutor<BomGroup> {

    BomGroup findByTypeAndCode(BomItemType type, String code);

    BomGroup findByTypeAndCodeAndVersity(BomItemType type, String code, Boolean versity);

    BomGroup findByTypeAndName(BomItemType type, String name);

    BomGroup findByTypeAndNameAndVersity(BomItemType type, String name, Boolean versity);

    List<BomGroup> findByNameAndType(String name, BomItemType type);

    BomGroup findByNameOrCode(String name, String code);

    List<BomGroup> findByType(BomItemType type);
}
