package com.cassinisys.plm.repo.mes;

import com.cassinisys.plm.model.mes.MESInstrumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by CassiniSystems on 17-09-2020.
 */
@Repository
public interface InstrumentTypeRepository extends JpaRepository<MESInstrumentType, Integer> {

    List<MESInstrumentType> findByIdIn(Iterable<Integer> ids);

    List<MESInstrumentType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MESInstrumentType> findByParentTypeIsNullOrderByIdAsc();

    List<MESInstrumentType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MESInstrumentType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MESInstrumentType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MESInstrumentType> findByParentTypeOrderByIdAsc(Integer id);

    List<MESInstrumentType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);

    MESInstrumentType findByName(String name);
}
