package com.cassinisys.is.repo.procm;

import com.cassinisys.is.model.procm.ISMaterialIssueTypeAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by swapna on 20/06/19.
 */
@Repository
public interface IssueTypeAttributeRepository extends JpaRepository<ISMaterialIssueTypeAttribute, Integer> {

    List<ISMaterialIssueTypeAttribute> findByItemTypeOrderByIdAsc(Integer typeId);

    List<ISMaterialIssueTypeAttribute> findByObjectType(ObjectType var1);
}
