package com.cassinisys.plm.repo.pdm;

import com.cassinisys.plm.model.pdm.PDMObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDMObjectRepository extends JpaRepository<PDMObject, Integer> {
    List<PDMObject> findByIdIn(Iterable<Integer> ids);
}
