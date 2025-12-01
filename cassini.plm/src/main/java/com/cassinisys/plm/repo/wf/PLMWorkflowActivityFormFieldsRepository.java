package com.cassinisys.plm.repo.wf;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.wf.PLMWorkflowActivityFormFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 26-10-2021.
 */
@Repository
public interface PLMWorkflowActivityFormFieldsRepository extends JpaRepository<PLMWorkflowActivityFormFields, Integer> {
    List<PLMWorkflowActivityFormFields> findByWorkflowActivityOrderByName(Integer typeId);

    PLMWorkflowActivityFormFields findByWorkflowActivityAndName(Integer typeId, String name);

    List<PLMWorkflowActivityFormFields> findByWorkflowActivity(Integer itemType);

    List<PLMWorkflowActivityFormFields> findByWorkflowActivityAndRequiredTrue(Integer itemType);

    @Query("SELECT i from PLMWorkflowActivityFormFields i where i.dataType= :integerType or i.dataType= :doubleType")
    List<PLMWorkflowActivityFormFields> getIntegerAndDoubleTypeAttributes(@Param("integerType") DataType integerType, @Param("doubleType") DataType doubleType);

    List<PLMWorkflowActivityFormFields> findByIdIn(Iterable<Integer> ids);

    List<PLMWorkflowActivityFormFields> findByNameIn(Iterable<String> names);

    List<PLMWorkflowActivityFormFields> findByName(String name);

    List<PLMWorkflowActivityFormFields> findByWorkflowActivityAndDataTypeAndRefType(Integer typeId, DataType dataType, ObjectType objectType);
}
