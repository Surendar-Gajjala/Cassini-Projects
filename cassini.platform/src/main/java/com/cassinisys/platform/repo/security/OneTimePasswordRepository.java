package com.cassinisys.platform.repo.security;

import com.cassinisys.platform.model.security.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 9/8/15.
 */
@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Integer> {
    List<OneTimePassword> findByLoginAndVerifiedFalse(Integer login);

    List<OneTimePassword> findByLogin(Integer login);

    OneTimePassword findByLoginAndOtp(Integer login, String otp);

    List<OneTimePassword> findByLoginAndExpiresBeforeAndVerifiedFalseOrderByCreatedDateDesc(Integer login, Date date);
}
