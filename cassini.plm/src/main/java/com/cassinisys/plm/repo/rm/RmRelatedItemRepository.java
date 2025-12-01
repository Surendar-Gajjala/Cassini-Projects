package com.cassinisys.plm.repo.rm;

import com.cassinisys.plm.model.rm.RmRelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RmRelatedItemRepository extends JpaRepository<RmRelatedItem, Integer> {
}