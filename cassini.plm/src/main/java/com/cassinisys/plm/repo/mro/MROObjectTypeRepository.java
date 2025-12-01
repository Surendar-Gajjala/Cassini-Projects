package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROObjectTypeRepository extends JpaRepository<MROObjectType, Integer> {

    List<MROObjectType> findByIdIn(Iterable<Integer> ids);

    List<MROObjectType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROObjectType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    @Query("select count (i) from MROObjectType i where i.autoNumberSource.id= :autoNumber")
    Integer getMROTypeAutoNumberValueCount(@Param("autoNumber") Integer autoNumber);
}
