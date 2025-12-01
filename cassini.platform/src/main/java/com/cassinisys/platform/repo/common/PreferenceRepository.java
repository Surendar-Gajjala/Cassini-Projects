package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by raghav on 18-11-2019.
 */
@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Integer> {

    List<Preference> findByContextOrderByIdAsc(String context);

    Preference findByPreferenceKey(String name);

    Preference findOne(Integer id);
}
