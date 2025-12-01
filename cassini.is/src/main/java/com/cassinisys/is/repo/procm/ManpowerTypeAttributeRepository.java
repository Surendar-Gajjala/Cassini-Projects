package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISManpowerTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManpowerTypeAttributeRepository extends JpaRepository<ISManpowerTypeAttribute, Integer> {
    List<ISManpowerTypeAttribute> findByItemTypeOrderByIdAsc(Integer typeId);
}
