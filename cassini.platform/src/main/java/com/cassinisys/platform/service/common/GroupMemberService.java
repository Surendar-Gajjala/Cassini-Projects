package com.cassinisys.platform.service.common;

import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.repo.common.GroupMemberRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Anusha on 08-01-2016.
 */
@Service
public class GroupMemberService implements CrudService<GroupMember, Integer> {


    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private PersonRepository personRepository;


    @Override
    @Transactional
    public GroupMember create(GroupMember groupMember) {
        return null;
    }

    @Override
    @Transactional
    public GroupMember update(GroupMember groupMember) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Integer integer) {

        groupMemberRepository.delete(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupMember get(Integer integer) {
        return groupMemberRepository.getOne(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMember> getAll() {
        return null;
    }

    @Transactional(readOnly = true)
    public List<Integer> findGroupIdsByPerson(Integer id) {
//		Person person = personRepository.getOne(id);
        List<Integer> groupMembers = groupMemberRepository.findGroupIdsByPerson(id);
        return groupMembers;
    }
}
