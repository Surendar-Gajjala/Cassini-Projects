package com.cassinisys.tm.service;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.tm.model.TMEmergencyContact;
import com.cassinisys.tm.model.TMPersonOtherInfo;
import com.cassinisys.tm.model.TMShift;
import com.cassinisys.tm.repo.EmergencyContactRepository;
import com.cassinisys.tm.repo.PersonOtherInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Rajabrahmachary on 10-08-2016.
 */
@Service
@Transactional
public class EmergencyContactService implements CrudService<TMEmergencyContact, Integer> {

    @Autowired
    private EmergencyContactRepository emergencyContactRepository;

    @Autowired
    private PersonOtherInfoRepository personOtherInfoRepository;

    @Override
    public TMEmergencyContact create(TMEmergencyContact tmEmergencyContact) {
        return emergencyContactRepository.save(tmEmergencyContact);
    }

    public TMPersonOtherInfo create(TMPersonOtherInfo tmPersonOtherInfo) {
        return personOtherInfoRepository.save(tmPersonOtherInfo);
    }

    @Override
    public TMEmergencyContact update(TMEmergencyContact tmEmergencyContact) {
        checkNotNull(tmEmergencyContact);
        return emergencyContactRepository.save(tmEmergencyContact);
    }

    public TMPersonOtherInfo updateOtherInfo(TMPersonOtherInfo tmPersonOtherInfo) {
        checkNotNull(tmPersonOtherInfo);
        return personOtherInfoRepository.save(tmPersonOtherInfo);
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public TMEmergencyContact get(Integer integer) {
        return null;
    }

    @Override
    public List<TMEmergencyContact> getAll() {
        return emergencyContactRepository.findAll();
    }


    public TMEmergencyContact getEmergencyContactByPersonId(Integer personId) {

        TMEmergencyContact tmEmergencyContact = emergencyContactRepository.findByPerson(personId);

        return tmEmergencyContact;
    }


    public TMPersonOtherInfo getPersonOtherInfoByPersonId(Integer personId) {
        return personOtherInfoRepository.findByPerson(personId);
    }

    public TMPersonOtherInfo getPersonRoleByPersonId(Integer personId) {
        return personOtherInfoRepository.findByPerson(personId);
    }


    public List<TMPersonOtherInfo> getPersonsByRole(String personRole) {
        return personOtherInfoRepository.findByRole(personRole);
    }


    public List<TMPersonOtherInfo> getAllPersonOtherInfos() {
        return personOtherInfoRepository.findAll();
    }
}
