package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCOType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DCOTypeRepository extends JpaRepository<PLMDCOType, Integer> {

	List<PLMDCOType> findByParentTypeIsNullOrderByCreatedDateAsc();

	List<PLMDCOType> findByParentTypeOrderByCreatedDateAsc(Integer id);
}
