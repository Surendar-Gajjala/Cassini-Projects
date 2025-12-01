package com.cassinisys.drdo.service;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.DRDOUpdates;
import com.cassinisys.drdo.repo.DRDOUpdateRepository;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.security.GroupPermissionRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by subra on 28-07-2019.
 */
@Service
public class DRDOUpdatesService implements CrudService<DRDOUpdates, Integer> {

    @Autowired
    private DRDOUpdateRepository drdoUpdateRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Override
    @Transactional(readOnly = false)
    public DRDOUpdates create(DRDOUpdates drdoUpdates) {
        return drdoUpdateRepository.save(drdoUpdates);
    }

    @Override
    @Transactional(readOnly = false)
    public DRDOUpdates update(DRDOUpdates drdoUpdates) {
        return drdoUpdateRepository.save(drdoUpdates);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        drdoUpdateRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DRDOUpdates get(Integer id) {
        return drdoUpdateRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DRDOUpdates> getAll() {
        return drdoUpdateRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void updateMessage(String message, DRDOObjectType objectType) {

        List<Person> persons = personRepository.findAll();

        persons.forEach(person -> {
            List<Integer> groups = groupMemberRepository.findGroupIdsByPerson(person.getId());

//            List<GroupPermission> groupPermissions = groupPermissionRepository.getPermissionsByGroupIds(groups);
            if (sessionWrapper.getSession() != null) {
                Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
                if (!person.getId().equals(personId)) {
                    DRDOUpdates drdoUpdates = new DRDOUpdates();
                    drdoUpdates.setPerson(person.getId());
                    drdoUpdates.setMessage(message);
                    drdoUpdates.setDate(new Date());
                    drdoUpdates.setObjectType(objectType.toString());

                    drdoUpdates = drdoUpdateRepository.save(drdoUpdates);
                }
            } else {
                DRDOUpdates drdoUpdates = new DRDOUpdates();
                drdoUpdates.setPerson(person.getId());
                drdoUpdates.setMessage(message);
                drdoUpdates.setDate(new Date());
                drdoUpdates.setObjectType(objectType.toString());

                drdoUpdates = drdoUpdateRepository.save(drdoUpdates);
            }
        });
    }

    @Transactional(readOnly = false)
    public void deleteUpdatesByPerson(Integer personId) {
        List<DRDOUpdates> drdoUpdates = drdoUpdateRepository.findByPersonOrderByDateDesc(personId);

        drdoUpdates.forEach(drdoUpdate -> {
            drdoUpdateRepository.delete(drdoUpdate.getId());
        });
    }

    @Transactional(readOnly = false)
    public void updateMessageByPerson(Integer personId) {
        List<DRDOUpdates> drdoUpdates = drdoUpdateRepository.findByPersonOrderByDateDesc(personId);

        drdoUpdates.forEach(drdoUpdate -> {
            drdoUpdate.setRead(true);
            drdoUpdateRepository.save(drdoUpdate);
        });
    }

    @Transactional(readOnly = true)
    public List<DRDOUpdates> getUpdatesByPerson(Integer personId) {
        return drdoUpdateRepository.findByPersonOrderByDateDesc(personId);
    }

    @Transactional(readOnly = false)
    public DRDOUpdates readMessage(DRDOUpdates drdoUpdates) {
        return drdoUpdateRepository.save(drdoUpdates);
    }
}
