package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialTypeAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialTypeAttributeRepository extends JpaRepository<ISMaterialTypeAttribute, Integer> {
    List<ISMaterialTypeAttribute> findByItemTypeOrderByIdAsc(Integer typeId);

    List<ISMaterialTypeAttribute> findByObjectType(ObjectType var1);
}
