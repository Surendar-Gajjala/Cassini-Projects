package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectViewGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomObjectViewGroupRepository extends JpaRepository<CustomObjectViewGroup, Integer> {
}
