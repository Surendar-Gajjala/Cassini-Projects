package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialIssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Repository
public interface MaterialIssueTypeRepository extends JpaRepository<ISMaterialIssueType, Integer> {

    @Query("SELECT m FROM ISMaterialIssueType m where m.parentType = null ORDER BY lower(m.name)")
    List<ISMaterialIssueType> findByParentTypeIsNullOrderByName();

    @Query("SELECT m FROM ISMaterialIssueType m where m.parentType = :parent ORDER BY lower(m.name)")
    List<ISMaterialIssueType> findByParentTypeOrderByName(@Param("parent") Integer parent);

    ISMaterialIssueType findByName(String itemType);

    List<ISMaterialIssueType> findByIdIn(Iterable<Integer> ids);

    List<ISMaterialIssueType> findByIssueNumberSourceId(Integer autoNumber);

    List<ISMaterialIssueType> findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    List<ISMaterialIssueType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
