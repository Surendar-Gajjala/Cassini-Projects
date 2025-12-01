package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECOType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ECOTypeRepository extends JpaRepository<PLMECOType, Integer> {

	List<PLMECOType> findByParentTypeIsNullOrderByCreatedDateAsc();

	List<PLMECOType> findByParentTypeOrderByCreatedDateAsc(Integer id);
}
