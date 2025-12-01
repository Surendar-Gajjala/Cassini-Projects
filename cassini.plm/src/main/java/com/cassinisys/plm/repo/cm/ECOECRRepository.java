package com.cassinisys.plm.repo.cm;

import com.cassinisys.plm.model.cm.PLMECOECR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface ECOECRRepository extends JpaRepository<PLMECOECR, Integer> {

	List<PLMECOECR> findByEcr(Integer id);

	List<PLMECOECR> findByEco(Integer id);
}
