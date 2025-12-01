package com.cassinisys.platform.util;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AppDetails;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.model.core.UserAttempts;
import com.cassinisys.platform.repo.core.AppDetailsRepository;
import com.cassinisys.platform.repo.core.UserAttemptRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Suresh Cassini.
 */
@Aspect
@Service
@Order(1)
public class EventAspect {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserAttemptRepository userAttemptRepository;

    @Autowired
    private AppDetailsRepository appDetailsRepository;
    @Autowired
    private MessageSource messageSource;

    public Integer getLoginAttempts() {
        Integer loginAttempts = null;
        List<AppDetails> detailsList = appDetailsRepository.findAll();
        for (AppDetails details : detailsList) {
            if (details.getOptionKey() == 11) {
                loginAttempts = Integer.parseInt(details.getValue());
            }
        }

        return loginAttempts;
    }

    @AfterThrowing(
            pointcut = "execution(* com.cassinisys.platform.service.security.SecurityService.login(..))")
    private UserAttempts updateFailAttempts(JoinPoint joinPoint) throws Exception {

        Object[] args = joinPoint.getArgs();
        String loginName = (String) args[0];
        String password = (String) args[1];
        Integer loginAttempts = getLoginAttempts();
        UserAttempts userAttempts = null;
        Login managerSaUser = null;
        Date date1 = null;
        if (loginName.indexOf("@") != -1) {
            String[] arr = loginName.split("@");
            loginName = arr[0];
        }
        userAttempts = getUserAttempts(loginName);
        managerSaUser = loginRepository.findByLoginName(loginName);
        if (managerSaUser != null) {
            if (managerSaUser.getIsSuperUser() == false && managerSaUser.getIsActive()) {

                if (userAttempts == null) {
                    if (isUserExists(loginName)) {
                        // if no record, insert a new

                        UserAttempts newRecord = new UserAttempts();
                        newRecord.setUserName(loginName);
                        newRecord.setAttempts(1);
                        newRecord.setLastModified(new Date());
                        newRecord = userAttemptRepository.save(newRecord);
                    }
                } else {

                    if (BCrypt.checkpw(password, managerSaUser.getPassword())) {
                        if (userAttempts.getAttempts() >= loginAttempts) {
                            throw new CassiniException(messageSource.getMessage("login_attempts_reached", null, "Benutzerkonto ist gesperrt. Bitte wenden Sie sich an admin.", LocaleContextHolder.getLocale()));
                        }
                    } else {

                        if (isUserExists(loginName)) {
                            // update attempts count, +1
                            userAttempts.setUserName(loginName);
                        }

                        if (userAttempts.getAttempts() + 1 > loginAttempts) {
                            userAttemptRepository.save(userAttempts);
                            throw new CassiniException(messageSource.getMessage("login_attempts_reached", null, "Benutzerkonto ist gesperrt. Bitte wenden Sie sich an admin.", LocaleContextHolder.getLocale()));

                        } else if (userAttempts.getAttempts() + 1 == loginAttempts) {
                            // locked user
                            userAttempts.setLastModified(new Date());
                            managerSaUser.setLocked(true);
                            userAttempts.setAttempts(userAttempts.getAttempts() + 1);
                            userAttemptRepository.save(userAttempts);
                            loginRepository.save(managerSaUser);
                            throw new CassiniException(messageSource.getMessage("login_attempts_reached", null, "Benutzerkonto ist gesperrt. Bitte wenden Sie sich an admin.", LocaleContextHolder.getLocale()));

                        } else {
                            userAttempts.setLastModified(new Date());
                            userAttempts.setAttempts(userAttempts.getAttempts() + 1);
                            userAttemptRepository.save(userAttempts);
                        }
                    }

                }
            }

        }
        return userAttempts;
    }

    public UserAttempts getUserAttempts(String username) {
        try {
            UserAttempts userAttempts = userAttemptRepository.findByUserName(username);
            return userAttempts;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    private boolean isUserExists(String loginName) {
        boolean result = false;
        int count = loginRepository.getCountOfUsers(loginName);
        if (count > 0) {
            result = true;
        }
        return result;
    }

}
