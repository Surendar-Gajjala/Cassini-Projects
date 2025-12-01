package com.cassinisys.plm.repo.pgc;

import com.cassinisys.plm.model.pgc.PGCDeclarationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-09-2020.
 */
@Repository
public interface PGCDeclarationTypeRepository extends JpaRepository<PGCDeclarationType, Integer> {

    List<PGCDeclarationType> findByIdIn(Iterable<Integer> ids);

    List<PGCDeclarationType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PGCDeclarationType> findByParentTypeIsNullOrderByIdAsc();

    PGCDeclarationType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    PGCDeclarationType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<PGCDeclarationType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<PGCDeclarationType> findByParentTypeOrderByIdAsc(Integer id);

    PGCDeclarationType findByName(String name);

    List<PGCDeclarationType> findByParentTypeAndNameEqualsIgnoreCase(Integer parentType, String name);
}
