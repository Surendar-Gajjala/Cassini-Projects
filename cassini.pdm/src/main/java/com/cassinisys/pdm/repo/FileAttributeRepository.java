package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMFileAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface FileAttributeRepository extends JpaRepository<PDMFileAttribute, Integer>{

}
