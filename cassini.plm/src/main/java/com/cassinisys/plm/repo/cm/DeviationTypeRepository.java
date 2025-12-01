package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDeviationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DeviationTypeRepository extends JpaRepository<PLMDeviationType, Integer> {
    List<PLMDeviationType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMDeviationType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMDeviationType> findByIdIn(Iterable<Integer> ids);

}
