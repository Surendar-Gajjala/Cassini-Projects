package com.cassinisys.plm.repo.pqm;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pqm.PQMQCRProblemMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 03-06-2020.
 */
@Repository
public interface QCRProblemMaterialRepository extends JpaRepository<PQMQCRProblemMaterial, Integer> {

    PQMQCRProblemMaterial findByQcrAndMaterial(Integer qcr, PLMManufacturerPart item);

    List<PQMQCRProblemMaterial> findByQcr(Integer qcr);

    List<PQMQCRProblemMaterial> findByQcrAndNcr(Integer qcr, Integer ncr);

    List<PQMQCRProblemMaterial> findByMaterial(PLMManufacturerPart ncr);
}
