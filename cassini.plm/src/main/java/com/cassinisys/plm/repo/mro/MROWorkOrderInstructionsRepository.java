package com.cassinisys.plm.repo.mro;

import com.cassinisys.plm.model.mro.MROWorkOrderInstructions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MROWorkOrderInstructionsRepository extends JpaRepository<MROWorkOrderInstructions, Integer> {
    MROWorkOrderInstructions findByworkOrder(Integer workOrder);

}
