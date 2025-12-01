package com.cassinisys.tm.repo;

import com.cassinisys.tm.model.TMEmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Rajabrahmachary on 06-07-2016.
 */
@Repository
public interface EmergencyContactRepository extends JpaRepository<TMEmergencyContact,Integer> {
    TMEmergencyContact findByPerson(Integer personId);
}
