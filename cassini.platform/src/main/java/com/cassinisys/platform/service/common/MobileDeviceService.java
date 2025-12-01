package com.cassinisys.platform.service.common;

import com.cassinisys.platform.model.common.MobileDevice;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.repo.common.MobileDeviceRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.security.SessionRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by reddy on 10/24/15.
 */
@Service
public class MobileDeviceService implements CrudService<MobileDevice, String>,
        PageableService<MobileDevice, String> {

    @Autowired
    private MobileDeviceRepository mobileDeviceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    @Transactional
    public MobileDevice create(MobileDevice mobileDevice) {
        return mobileDeviceRepository.save(mobileDevice);
    }

    @Override
    @Transactional
    public MobileDevice update(MobileDevice mobileDevice) {
        return mobileDeviceRepository.save(mobileDevice);
    }

    @Override
    @Transactional
    public void delete(String id) {
        mobileDeviceRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MobileDevice get(String id) {
        return mobileDeviceRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MobileDevice> getAll() {
        return mobileDeviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MobileDevice> findAll(Pageable pageable) {
        return mobileDeviceRepository.findAll(pageable);
    }

    @Transactional
    public List<MobileDevice> findMultiple(List<String> ids) {
        return mobileDeviceRepository.findByDeviceIdIn(ids);
    }

    public void saveMobileDevice(MobileDevice mobileDevice) {
        Person person = null;
        Session session = null;
        if (mobileDevice.getPersonId() != null) {
            person = personRepository.findOne(mobileDevice.getPersonId());
        }
        if (mobileDevice.getSessionId() != null) {
            session = sessionRepository.findBySessionId(mobileDevice.getSessionId());
        }

        MobileDevice mobileDevice1 = mobileDeviceRepository.findOne(mobileDevice.getDeviceId());
        if (mobileDevice1 == null) {
            mobileDevice1 = mobileDeviceRepository.save(mobileDevice);
        }

        if ((person != null && person.getMobileDevice() == null) || (person != null && !person.getMobileDevice().getDeviceId().equals(mobileDevice1.getDeviceId()))) {
            person.setMobileDevice(mobileDevice1);
            personRepository.save(person);
        }

        if ((session != null && session.getMobileDevice() == null) || (session != null && !session.getMobileDevice().getDeviceId().equals(mobileDevice1.getDeviceId()))) {
            session.setMobileDevice(mobileDevice1);
            sessionRepository.save(session);
        }
    }

}
