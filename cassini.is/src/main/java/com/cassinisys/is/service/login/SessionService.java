package com.cassinisys.is.service.login;

import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.repo.security.SessionRepository;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The class is for SessionService
 */
@Service
@Transactional
public class SessionService implements PageableService<Session, Integer> {

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * The method used to findAll for the page of Session
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<Session> findAll(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }

}
