package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMRevisionedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMRevisionedObjectRepository extends JpaRepository<PDMRevisionedObject, Integer> {
    List<PDMRevisionedObject> findByIdIn(Iterable<Integer> ids);

    List<PDMRevisionedObject> findByMasterId(Integer master);

    PDMRevisionedObject findByMasterIdAndRevision(Integer master, String revision);
}
