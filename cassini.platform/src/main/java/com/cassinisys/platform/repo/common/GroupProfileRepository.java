package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.GroupProfile;
import com.cassinisys.platform.model.common.PersonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 28-01-2021.
 */
@Repository
public interface GroupProfileRepository extends JpaRepository<GroupProfile, Integer> {

    @Query(
            "SELECT i FROM GroupProfile i WHERE i.personGroup.groupId = :groupId"
    )
    GroupProfile findProfileByGroup(@Param("groupId") Integer groupId);

    @Query(
            "SELECT i.personGroup FROM GroupProfile i WHERE i.profile.id = :profileId"
    )
    List<PersonGroup> findGroupsByProfile(@Param("profileId") Integer profileId);

    @Query(
            "SELECT i FROM GroupProfile i WHERE i.profile.id = :profileId and i.personGroup.groupId = :groupId"
    )
    GroupProfile findByProfileAndGroup(@Param("profileId") Integer profileId, @Param("groupId") Integer groupId);

}
