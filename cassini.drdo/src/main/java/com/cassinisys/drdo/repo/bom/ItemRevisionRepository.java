package com.cassinisys.drdo.repo.bom;

import com.cassinisys.drdo.model.bom.ItemRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 04-10-2018.
 */
@Repository
public interface ItemRevisionRepository extends JpaRepository<ItemRevision, Integer> {

    List<ItemRevision> findByIdIn(Iterable<Integer> varl);
}
