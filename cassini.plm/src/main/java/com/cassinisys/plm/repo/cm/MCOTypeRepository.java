package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.MCOType;
import com.cassinisys.plm.model.cm.PLMMCOType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface MCOTypeRepository extends JpaRepository<PLMMCOType, Integer> {

	List<PLMMCOType> findByParentTypeIsNullOrderByCreatedDateAsc();

	List<PLMMCOType> findByParentTypeIsNullAndMcoTypeOrderByCreatedDateAsc(MCOType mcoType);

	List<PLMMCOType> findByParentTypeAndMcoTypeOrderByCreatedDateAsc(Integer id, MCOType mcoType);

	List<PLMMCOType> findByParentTypeOrderByCreatedDateAsc(Integer id);
}
