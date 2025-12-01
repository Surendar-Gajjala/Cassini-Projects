package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-11-2020.
 */
@Repository
public interface MROWorkRequestTypeRepository extends JpaRepository<MROWorkRequestType, Integer> {

    List<MROWorkRequestType> findByIdIn(Iterable<Integer> ids);

    List<MROWorkRequestType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROWorkRequestType> findByParentTypeIsNullOrderByIdAsc();

    List<MROWorkRequestType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MROWorkRequestType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MROWorkRequestType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MROWorkRequestType> findByParentTypeOrderByIdAsc(Integer id);

    MROWorkRequestType findByName(String name);

    List<MROWorkRequestType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
