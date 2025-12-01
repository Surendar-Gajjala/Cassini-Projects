package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCRAffectedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DCRAffectedItemRepository extends JpaRepository<PLMDCRAffectedItem, Integer> {

	List<PLMDCRAffectedItem> findByDcr(Integer dcr);

	List<PLMDCRAffectedItem> findByItem(Integer dcr);

	@Query("select i.item from PLMDCRAffectedItem i where i.dcr= :dcr")
	List<Integer> getAffectedItemIdsByDcr(@Param("dcr") Integer dcr);
}
