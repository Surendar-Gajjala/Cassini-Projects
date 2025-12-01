package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.UserAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttemptRepository extends JpaRepository<UserAttempts, Integer> {
    UserAttempts findByUserName(String userName);
    @Modifying
    @Query(value = "update UserAttempts u set u.attempts = 0, u.lastModified = null where u.userName = :userName", nativeQuery = true)
    UserAttempts resetFailAttempts(@Param("userName") String userName);

}