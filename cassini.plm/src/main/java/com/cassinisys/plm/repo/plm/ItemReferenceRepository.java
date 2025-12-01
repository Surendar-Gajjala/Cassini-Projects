package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMItemReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Home on 4/20/2016.
 */
@Repository
public interface ItemReferenceRepository extends JpaRepository<PLMItemReference, Integer> {
    List<PLMItemReference> findByParent(Integer itemId);
}
