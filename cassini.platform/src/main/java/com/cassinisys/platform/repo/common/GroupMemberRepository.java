package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Anusha on 08-01-2016.
 */
@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
    List<GroupMember> findByPerson(Person person);

    @Modifying
    @Query(
            value = "DELETE FROM GroupMember grpMbr WHERE grpMbr.personGroup.groupId = :personGrpId and grpMbr.person.id= :personId "
    )
    @Transactional
    void deleteGrpMember(@Param("personGrpId") Integer personGrpId, @Param("personId") Integer personId);

    @Query(
            "SELECT grpMber.personGroup.id FROM GroupMember grpMber where grpMber.person.id = :personId"
    )
    List<Integer> findGroupIdsByPerson(@Param("personId") Integer personid);

    @Query(
            "SELECT grpMber.personGroup FROM GroupMember grpMber where grpMber.person.id = :personId"
    )
    List<PersonGroup> findGroupsByPerson(@Param("personId") Integer personid);

    GroupMember findByPersonAndPersonGroup(Person personId, PersonGroup personGroup);

    List<GroupMember> findByPersonGroup(PersonGroup personGroup);

    @Query("select gm from GroupMember gm where gm.personGroup.groupId IN :groupIds")
    List<GroupMember> getGroupsByGroupIds(@Param("groupIds") List<Integer> groupIds);
}
