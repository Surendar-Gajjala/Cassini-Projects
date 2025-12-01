package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialReceiveTypeAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Repository
public interface MaterialReceiveTypeAttributeRepository extends JpaRepository<ISMaterialReceiveTypeAttribute, Integer> {
    List<ISMaterialReceiveTypeAttribute> findByItemTypeOrderByIdAsc(Integer typeId);

    List<ISMaterialReceiveTypeAttribute> findByObjectType(ObjectType var1);
}
