package com.cassinisys.plm.repo.plm;

import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.plm.model.plm.PLMItemTypeAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeAttributeRepository extends JpaRepository<PLMItemTypeAttribute, Integer> {
    List<PLMItemTypeAttribute> findByItemTypeOrderByName(Integer typeId);

    List<PLMItemTypeAttribute> findByItemTypeOrderBySeq(Integer typeId);

    List<PLMItemTypeAttribute> findByItemTypeAndRevisionSpecificFalseOrderBySeq(Integer typeId);

    List<PLMItemTypeAttribute> findByItemTypeAndRevisionSpecificTrueOrderBySeq(Integer typeId);

    List<PLMItemTypeAttribute> findByItemTypeAndConfigurableTrueOrderBySeq(Integer typeId);

    PLMItemTypeAttribute findByItemTypeAndName(Integer typeId, String name);

    PLMItemTypeAttribute findByItemTypeAndNameAndConfigurableTrue(Integer typeId, String name);

    List<PLMItemTypeAttribute> findByItemType(Integer itemType);

    @Query("SELECT i from PLMItemTypeAttribute i where i.dataType= :integerType or i.dataType= :doubleType")
    List<PLMItemTypeAttribute> getIntegerAndDoubleTypeAttributes(@Param("integerType") DataType integerType, @Param("doubleType") DataType doubleType);

    List<PLMItemTypeAttribute> findByIdIn(Iterable<Integer> ids);

    List<PLMItemTypeAttribute> findByNameIn(Iterable<String> names);

    List<PLMItemTypeAttribute> findByName(String name);

    List<PLMItemTypeAttribute> findByItemTypeAndDataTypeAndRefType(Integer typeId, DataType dataType, ObjectType objectType);
}
