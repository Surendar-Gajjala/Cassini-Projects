package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROSparePartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Suresh Cassini on 17-11-2020.
 */
@Repository
public interface MROSparePartTypeRepository extends JpaRepository<MROSparePartType, Integer> {

    List<MROSparePartType> findByIdIn(Iterable<Integer> ids);

    List<MROSparePartType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROSparePartType> findByParentTypeIsNullOrderByIdAsc();

    List<MROSparePartType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MROSparePartType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MROSparePartType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MROSparePartType> findByParentTypeOrderByIdAsc(Integer id);

    MROSparePartType findByName(String name);

    List<MROSparePartType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

}
