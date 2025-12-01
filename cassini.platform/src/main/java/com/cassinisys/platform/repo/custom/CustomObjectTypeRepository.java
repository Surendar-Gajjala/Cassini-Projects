package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomObjectTypeRepository extends JpaRepository<CustomObjectType, Integer> {
    List<CustomObjectType> findByIdIn(Iterable<Integer> ids);

    @Query("select c from CustomObjectType c where c.parentType is null order by c.name, c.createdDate ASC")
    List<CustomObjectType> findByParentTypeIsNullOrderByNameAndCreatedDateAsc();

    List<CustomObjectType> findByParentTypeIsNullOrderByIdAsc();

    List<CustomObjectType> findByShowInNavigationTrueOrderByIdAsc();

    @Query("select c.id from CustomObjectType c where c.parentType is null")
    List<Integer> getByParentTypeIsNullOrderByIdAsc();

    @Query("select c.id from CustomObjectType c where c.parentType= :parent")
    List<Integer> getTypeIdsByParentTypeOrderByIdAsc(@Param("parent") Integer parent);

    @Query("select c from CustomObjectType c where c.parentType= :parent order by c.name, c.createdDate ASC")
    List<CustomObjectType> findByParentTypeOrderByNameAndCreatedDateAsc(@Param("parent") Integer parent);

    List<CustomObjectType> findByParentTypeOrderByIdAsc(Integer parent);

    CustomObjectType findByNameEqualsIgnoreCase(String name);

    List<CustomObjectType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

    @Query("select count (i) from CustomObjectType i where i.numberSource.id= :autoNumber")
    Integer getCustomeObjectTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);

    @Query("select c.id from CustomObjectType c where c.name= :name")
    Integer getTypeIdByName(@Param("name") String name);
}
