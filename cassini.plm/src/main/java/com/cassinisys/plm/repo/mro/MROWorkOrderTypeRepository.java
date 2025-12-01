package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkOrderType;
import com.cassinisys.plm.model.mro.WorkOrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 17-11-2020.
 */
@Repository
public interface MROWorkOrderTypeRepository extends JpaRepository<MROWorkOrderType, Integer> {

    List<MROWorkOrderType> findByIdIn(Iterable<Integer> ids);

    List<MROWorkOrderType> findByParentTypeIsNullOrderByCreatedDateAsc();

    List<MROWorkOrderType> findByParentTypeIsNullOrderByIdAsc();

    List<MROWorkOrderType> findByTypeAndParentTypeIsNullOrderByCreatedDateAsc(WorkOrderType type);

    List<MROWorkOrderType> findByTypeAndParentTypeIsNullOrderByIdAsc(WorkOrderType type);

    MROWorkOrderType findByParentTypeIsNullAndNameEqualsIgnoreCase(String name);

    MROWorkOrderType findByNameEqualsIgnoreCaseAndParentType(String name, Integer parentType);

    List<MROWorkOrderType> findByParentTypeOrderByCreatedDateAsc(Integer id);

    List<MROWorkOrderType> findByParentTypeOrderByIdAsc(Integer id);

    MROWorkOrderType findByName(String name);

    List<MROWorkOrderType> findByParentTypeAndNameEqualsIgnoreCase(Integer parent, String name);
}
