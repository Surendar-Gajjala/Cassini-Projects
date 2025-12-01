package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMNprItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NprItemRepository extends JpaRepository<PLMNprItem, Integer> {
    List<PLMNprItem> findByNpr(Integer npr);

    @Query("select count (i) from PLMNprItem i where i.npr= :npr and i.assignedNumber = false")
    Integer getUnAssignedNprItems(@Param("npr") Integer npr);
}
