package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMWaiverType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface WaiverTypeRepository extends JpaRepository<PLMWaiverType, Integer> {
    List<PLMWaiverType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<PLMWaiverType> findByParentTypeOrderByCreatedDateAsc(Integer parent);

    List<PLMWaiverType> findByIdIn(Iterable<Integer> ids);
}
