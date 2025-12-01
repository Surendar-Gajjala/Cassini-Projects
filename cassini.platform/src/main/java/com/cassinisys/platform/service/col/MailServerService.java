package com.cassinisys.platform.service.col;

import com.cassinisys.platform.model.col.MailServer;
import com.cassinisys.platform.model.col.ObjectMailSettings;
import com.cassinisys.platform.repo.col.MailServerRepository;
import com.cassinisys.platform.repo.col.ObjectMailSettingsRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Rajabrahmachary on 16-11-2018.
 */
@Service
public class MailServerService implements CrudService<MailServer, Integer> {

    @Autowired
    MailServerRepository mailServerRepository;

    @Autowired
    ObjectMailSettingsRepository objectMailSettingsRepository;



    @Transactional
    public ObjectMailSettings createObjectMailSettings(ObjectMailSettings objectMailSettings) {
        checkNotNull(objectMailSettings);
        return objectMailSettingsRepository.save(objectMailSettings);
    }

    @Transactional
    public ObjectMailSettings updateObjectMailSettings(ObjectMailSettings objectMailSettings) {
        checkNotNull(objectMailSettings);
        return objectMailSettingsRepository.save(objectMailSettings);
    }

    @Transactional
    public void deleteObjectMailSettings(Integer id) {
        checkNotNull(id);
        objectMailSettingsRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public ObjectMailSettings getObjectMailSettings(Integer id) {
        checkNotNull(id);
        return objectMailSettingsRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<ObjectMailSettings> getAllObjectMailSettings() {
        return objectMailSettingsRepository.findAll();
    }


    @Override
    @Transactional
    public MailServer create(MailServer mailServer) {
        checkNotNull(mailServer);
        return mailServerRepository.save(mailServer);
    }

    @Override
    @Transactional
    public MailServer update(MailServer mailServer) {
        checkNotNull(mailServer);
        return mailServerRepository.save(mailServer);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
         checkNotNull(id);
         mailServerRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public MailServer get(Integer id) {
        checkNotNull(id);
        return mailServerRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MailServer> getAll() {
        return mailServerRepository.findAll();
    }
}
