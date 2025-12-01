package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObject;
import com.cassinisys.platform.model.custom.CustomObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectRepository extends JpaRepository<CustomObject, Integer>,
        QueryDslPredicateExecutor<CustomObject> {
    List<CustomObject> findByIdIn(Iterable<Integer> ids);

    CustomObject findByNumber(String number);

    @Query("select count (i) from CustomObject i where i.type.id= :typeId")
    Integer getCustomObjectByType(@Param("typeId") Integer typeId);

    @Query("select count (i) from CustomObject i where (LOWER(CAST(i.number as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.name as text))) LIKE '%' || :searchText || '%' or (LOWER(CAST(i.description as text))) LIKE '%' || :searchText || '%'" +
            " or (LOWER(CAST(i.type.name as text))) LIKE '%' || :searchText || '%'")
    Integer getCustomObjectCountBySearchQuery(@Param("searchText") String searchText);

    List<CustomObject> findBySupplier(Integer supplier);

    List<CustomObject> findByTypeIdAndSupplier(Integer type, Integer supplier);

    @Query("select count(i) from CustomObject i where i.supplier= :supplier")
    Integer getCustomObjectCountBySupplier(@Param("supplier") Integer supplier);

    @Query("select count(i) from CustomObject i where i.supplier= :supplier and i.type.id= :typeId")
    Integer getCustomObjectCountBySupplierAndType(@Param("supplier") Integer supplier, @Param("typeId") Integer typeId);

    List<CustomObject> findByType(CustomObjectType type);

    List<CustomObject> findByTypeId(Integer type);

    @Query("select distinct i.createdBy from CustomObject i where i.type.id= :id ")
    List<Integer> getCreatedByIdsByType(@Param("id") Integer id);

}
