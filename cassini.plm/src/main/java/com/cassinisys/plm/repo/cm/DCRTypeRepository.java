package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCRType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DCRTypeRepository extends JpaRepository<PLMDCRType, Integer> {

	List<PLMDCRType> findByParentTypeIsNullOrderByCreatedDateAsc();

	List<PLMDCRType> findByParentTypeOrderByCreatedDateAsc(Integer id);
}
