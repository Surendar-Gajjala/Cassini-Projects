package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
    UserPreferences findByLogin(Integer loginId);
}