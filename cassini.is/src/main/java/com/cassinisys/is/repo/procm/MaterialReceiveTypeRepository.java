package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialReceiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Repository
public interface MaterialReceiveTypeRepository extends JpaRepository<ISMaterialReceiveType, Integer> {

    @Query("SELECT m FROM ISMaterialReceiveType m where m.parentType = null ORDER BY lower(m.name)")
    List<ISMaterialReceiveType> findByParentTypeIsNullOrderByName();

    @Query("SELECT m FROM ISMaterialReceiveType m where m.parentType = :parent ORDER BY lower(m.name)")
    List<ISMaterialReceiveType> findByParentTypeOrderByName(@Param("parent") Integer parent);

    ISMaterialReceiveType findByName(String itemType);

    List<ISMaterialReceiveType> findByIdIn(Iterable<Integer> ids);

    List<ISMaterialReceiveType> findByReceiveNumberSourceId(Integer autoNumber);

    List<ISMaterialReceiveType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<ISMaterialReceiveType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
