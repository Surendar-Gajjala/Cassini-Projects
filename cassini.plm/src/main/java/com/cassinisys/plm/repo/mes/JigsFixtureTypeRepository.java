package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESJigsFixtureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface JigsFixtureTypeRepository extends JpaRepository<MESJigsFixtureType, Integer> {

    List<MESJigsFixtureType> findByIdIn(Iterable<Integer> ids);

    List<MESJigsFixtureType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESJigsFixtureType> findByParentTypeIsNullOrderByIdAsc();

    List<MESJigsFixtureType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESJigsFixtureType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESJigsFixtureType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESJigsFixtureType> findByParentTypeOrderByIdAsc(Integer id);
    List<MESJigsFixtureType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESJigsFixtureType findByName(String name);
}
