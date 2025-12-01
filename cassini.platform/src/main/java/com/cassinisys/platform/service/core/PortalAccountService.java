package com.cassinisys.platform.service.core;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.model.core.PortalAccount;
import com.cassinisys.platform.repo.core.PortalAccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PortalAccountService {
    @AutowiredLogger
    private Logger LOGGER;

    private PortalAccountRepository portalaccountRepository;

    @Autowired
    public PortalAccountService(PortalAccountRepository portalaccountRepository) {
        this.portalaccountRepository = portalaccountRepository;
    }

    public PortalAccount createPortalAccount(PortalAccount portalaccount) {
        return portalaccountRepository.save(portalaccount);
    }

    public PortalAccount updatePortalAccount(PortalAccount portalaccount) {
        return portalaccountRepository.save(portalaccount);
    }

    public PortalAccount getPortalAccount(Integer id) {
        if(id == null) {
            id = 1;
        }
        return portalaccountRepository.findOne(id);
    }

    public void deletePortalAccount(Integer id) {
        portalaccountRepository.delete(id);
    }
}
