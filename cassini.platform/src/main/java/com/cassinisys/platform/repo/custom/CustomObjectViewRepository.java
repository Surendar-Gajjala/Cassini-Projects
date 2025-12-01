package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomObjectViewRepository extends JpaRepository<CustomObjectView, Integer> {
}
