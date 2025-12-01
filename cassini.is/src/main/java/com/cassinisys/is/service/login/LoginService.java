package com.cassinisys.is.service.login;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for LoginService
 */
@Service
@Transactional
public class LoginService implements CrudService<Login, Integer>,
        PageableService<Login, Integer> {

    @Autowired
    private LoginRepository loginRepository;

    /**
     * The method used to create Login
     **/
    @Transactional(readOnly = false)
    @Override
    public Login create(Login login) {
        checkNotNull(login);
        login.setId(null);
        String encryptedPwd = BCrypt.hashpw(login.getPassword(),
                BCrypt.gensalt());
        login.setPassword(encryptedPwd);
        login = loginRepository.save(login);
        return login;

    }

    /**
     * The method used to update Login
     **/
    @Transactional(readOnly = false)
    @Override
    public Login update(Login login) {
        checkNotNull(login);
        checkNotNull(login.getId());
        Login currentLogin = loginRepository.findOne(login.getId());
        if (currentLogin == null) {
            throw new ResourceNotFoundException();
        }
        if (!login.getPassword().equals(currentLogin.getPassword())) {
            String encryptedPwd = BCrypt.hashpw(login.getPassword(),
                    BCrypt.gensalt());
            login.setPassword(encryptedPwd);
        }
        login = loginRepository.save(login);
        return login;
    }

    /**
     * The method used to delete Login
     **/
    @Transactional(readOnly = false)
    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        Login login = loginRepository.findOne(id);
        if (login == null) {
            throw new ResourceNotFoundException();
        }
        loginRepository.delete(login);
    }

    /**
     * The method used to get Login
     **/
    @Transactional(readOnly = true)
    @Override
    public Login get(Integer id) {
        checkNotNull(id);
        Login login = loginRepository.findOne(id);
        if (login == null) {
            throw new ResourceNotFoundException();
        }
        return login;
    }

    @Transactional(readOnly = true)
    public Login getByPersonId(Integer personId) {
        Login login = loginRepository.findByPersonId(personId);
        return login;
    }

    /**
     * The method used to getAll for the list of Login
     **/
    @Transactional(readOnly = true)
    @Override
    public List<Login> getAll() {
        return loginRepository.findAll();
    }

    /**
     * The method used to findAll for the page of Login
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<Login> findAll(Pageable pageable) {
        return loginRepository.findAll(pageable);
    }

}
