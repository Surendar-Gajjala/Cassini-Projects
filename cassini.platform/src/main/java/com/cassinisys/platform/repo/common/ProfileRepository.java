package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    List<Profile> findAllByOrderByNameAsc();

    Profile findByNameContainingIgnoreCase(String name);

    Profile findByNameEqualsIgnoreCase(String name);
}