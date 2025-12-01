package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMDCODCR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface DCODCRRepository extends JpaRepository<PLMDCODCR, Integer> {

	List<PLMDCODCR> findByDco(Integer id);

	List<PLMDCODCR> findByDcr(Integer id);
}
