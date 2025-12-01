package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROAssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR Cassini on 17-11-2020.
 */
@Repository
public interface MROAssetTypeRepository extends JpaRepository<MROAssetType, Integer> {

    List<MROAssetType> findByIdIn(Iterable<Integer> ids);

    List<MROAssetType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROAssetType> findByParentTypeIsNullOrderByIdAsc();

    List<MROAssetType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    MROAssetType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MROAssetType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MROAssetType> findByParentTypeOrderByIdAsc(Integer id);

    MROAssetType findByName(String name);

    List<MROAssetType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);

}
