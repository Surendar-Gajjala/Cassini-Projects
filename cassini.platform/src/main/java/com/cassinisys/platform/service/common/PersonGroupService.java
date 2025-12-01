package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.filtering.PersonGroupPredicateBuilder;
import com.cassinisys.platform.model.common.*;
import com.cassinisys.platform.model.security.GroupPermission;
import com.cassinisys.platform.model.security.GroupPermissionId;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.repo.common.*;
import com.cassinisys.platform.repo.security.GroupPermissionRepository;
import com.cassinisys.platform.repo.security.GroupSecurityPermissionRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 08-01-2016.
 */

@Service
public class PersonGroupService implements CrudService<PersonGroup, Integer>, PageableService<PersonGroup, Integer> {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private PersonGroupRepository personGroupRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private GroupPermissionRepository permissionRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private ProfileService profileService;
    @Autowired
    private PersonGroupPredicateBuilder personGroupPredicateBuilder;
    @Autowired
    private GroupProfileRepository groupProfileRepository;
    @Autowired
    private GroupSecurityPermissionRepository groupSecurityPermissionRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;

    @Override
    @Transactional
    public PersonGroup create(PersonGroup personGroup) {

        checkNotNull(personGroup);

        if (personGroup.getGroupMember() != null && personGroup.getGroupMember().size() > 0) {

            for (GroupMember grpMember : personGroup.getGroupMember()) {

                if (grpMember != null) {
                    grpMember.setPersonGroup(personGroup);
                }
            }
        }

        if (personGroup.getParent() != null) {
            PersonGroup existGroup = personGroupRepository.findByNameEqualsIgnoreCaseAndParent(personGroup.getName(), personGroup.getParent());
            if (existGroup != null) {
                throw new CassiniException("Group Name already exist");
            }
        } else {
            PersonGroup existGroup1 = personGroupRepository.findByNameEqualsIgnoreCaseAndParentIsNull(personGroup.getName());
            if (existGroup1 != null) {
                throw new CassiniException("Group Name already exist");
            }
        }
        personGroup = personGroupRepository.save(personGroup);

        if (personGroup.getParent() != null) {
            PersonGroup parent = personGroupRepository.findOne(personGroup.getParent());
            List<GroupPermission> groupPermissions = permissionRepository.findGroupPermissions(parent);
            for (GroupPermission groupPermission : groupPermissions) {
                GroupPermission permission = new GroupPermission();
                permission.setId(new GroupPermissionId(personGroup, groupPermission.getId().getPermission()));
                permission = permissionRepository.save(permission);
            }
        }

        if (personGroup.getProfile() != null) {
            Profile profile1 = personGroup.getProfile();

            if (profile1.getId() != null) {
                GroupProfile groupProfile3 = groupProfileRepository.findProfileByGroup(personGroup.getGroupId());
                if (groupProfile3 != null) {
                    groupProfileRepository.delete(groupProfile3.getId());
                }
                GroupProfile groupProfile2 = groupProfileRepository.findByProfileAndGroup(profile1.getId(), personGroup.getGroupId());
                if (groupProfile2 == null) {
                    GroupProfile gp = new GroupProfile(profile1, personGroup);
                    groupProfileRepository.save(gp);
                }
            }
        }

        return personGroup;

    }

    @Override
    @Transactional
    public PersonGroup update(PersonGroup personGroup) {
        checkNotNull(personGroup);
        checkNotNull(personGroup.getGroupId());
        if (personGroup.getParent() != null) {
            PersonGroup existGroup = personGroupRepository.findByNameEqualsIgnoreCaseAndParent(personGroup.getName(), personGroup.getParent());
            if (existGroup != null && personGroup.getGroupId() != existGroup.getGroupId()) {
                throw new CassiniException("Group Name already exist");
            }
        } else {
            PersonGroup existGroup1 = personGroupRepository.findByNameEqualsIgnoreCaseAndParentIsNull(personGroup.getName());
            if (existGroup1 != null && personGroup.getGroupId() != existGroup1.getGroupId()) {
                throw new CassiniException("Group Name already exist");
            }
        }

        if (personGroup.getProfile() != null) {
            Profile profile1 = personGroup.getProfile();

            if (profile1.getId() != null) {
                GroupProfile groupProfile3 = groupProfileRepository.findProfileByGroup(personGroup.getGroupId());
                if (groupProfile3 != null) {
                    groupProfileRepository.delete(groupProfile3.getId());
                }
                GroupProfile groupProfile2 = groupProfileRepository.findByProfileAndGroup(profile1.getId(), personGroup.getGroupId());
                if (groupProfile2 == null) {
                    GroupProfile gp = new GroupProfile(profile1, personGroup);
                    groupProfileRepository.save(gp);
                }
            }
        }

        personGroup = personGroupRepository.save(personGroup);
        return personGroup;
    }

    @Transactional
    public List<GroupMember> createGroupsByPersonId(Integer personId, List<GroupMember> groupMembers) {
        List<GroupMember> groupMember1 = new ArrayList();
        for (GroupMember groupMember : groupMembers) {
            GroupMember groupMember2 = groupMemberRepository.findByPersonAndPersonGroup(personRepository.findOne(personId), personGroupRepository.findOne(groupMember.getPersonGroup().getGroupId()));
            if (groupMember2 == null) {
                groupMember1.add(groupMemberRepository.save(groupMember));
            }
        }
        return groupMember1;
    }

    @Transactional
    public GroupMember updateGroupMember(Integer personId, GroupMember groupMember) {
        GroupMember groupMember1 = groupMemberRepository.findOne(groupMember.getRowId());
        GroupMember groupMember2 = groupMemberRepository.findByPersonAndPersonGroup(personRepository.findOne(personId), personGroupRepository.findOne(groupMember.getPersonGroup().getGroupId()));
        if (groupMember2 == null) {
            groupMember1.setPerson(personRepository.findOne(personId));
            groupMember1.setPersonGroup(groupMember.getPersonGroup());
            groupMember1 = groupMemberRepository.save(groupMember1);
        } else {
            groupMember1 = groupMember;
        }
        return groupMember1;
    }

    @Transactional(readOnly = true)
    public GroupMember getGroupMember(Integer personId, Integer groupId) {
        return groupMemberRepository.findByPersonAndPersonGroup(personRepository.findOne(personId), personGroupRepository.findOne(groupId));
    }

    @Transactional
    public GroupMember createGroupMember(GroupMember groupMember) {
        return groupMemberRepository.save(groupMember);
    }


    @Override
    @Transactional(readOnly = true)
    public PersonGroup get(Integer id) {
        checkNotNull(id);
        PersonGroup personGroup = personGroupRepository.findOne(id);
        if (personGroup == null) {
            throw new ResourceNotFoundException();
        }
        /*if (personGroup != null && personGroup.getGroupMember() != null && personGroup.getGroupMember().size() > 0) {


            for (GroupMember groupMember : personGroup.getGroupMember()) {

                if (groupMember != null) {

                    Login login = loginRepository.findByPersonId(groupMember.getPerson().getId());

                    if (login != null) {
                        groupMember.setLogin(login);
                    }
                }
            }
        }*/
        return personGroup;
    }

    @Transactional(readOnly = true)
    public Integer getDefaultUsersCount(Integer groupId) {
        Integer count = personRepository.getUsersCountByGroup(groupId);
        return count;
    }

    @Transactional(readOnly = true)
    public Preference getDefaultValuePreferenceByGroup(Integer groupId) {
        Preference usedPreference = null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Preference> preferences = preferenceRepository.findByContextOrderByIdAsc("DEFAULT");
        for (Preference preference : preferences) {
            if (preference.getJsonValue() != null) {
                try {
                    DefaultValueDto valueDto = objectMapper.readValue(preference.getJsonValue(), new TypeReference<DefaultValueDto>() {
                    });
                    preference.setDefaultValue(valueDto);
                    if (valueDto != null && valueDto.getTypeId() != null) {
                        if (valueDto.getType().equals("ROLE")) {
                            PersonGroup personGroup = personGroupRepository.findOne(valueDto.getTypeId());
                            if (personGroup != null && personGroup.getGroupId().equals(groupId)) {
                                usedPreference = preference;
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return usedPreference;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonGroup> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("groupId")));
        }
        return personGroupRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PersonGroup> getAll() {
        List<PersonGroup> personGroups = personGroupRepository.findAll();
        for (PersonGroup personGroup : personGroups) {
            securityService.loadGroupPermissions(personGroup);
        }
        return personGroups;
    }

    @Transactional(readOnly = true)
    public List<String> getAllGroupName() {
        List<PersonGroup> personGroups = personGroupRepository.findAll();
        List<String> groupNames = new ArrayList();
        for (PersonGroup personGroup : personGroups) {
            groupNames.add(personGroup.getName());
        }
        return groupNames;
    }

    @Transactional(readOnly = true)
    public Page<PersonGroup> getFilteredGroups(Pageable pageable, PersonCriteria criteria) {
        Predicate predicate = personGroupPredicateBuilder.build(criteria, QPersonGroup.personGroup);
        Page<PersonGroup> personGroups = personGroupRepository.findAll(predicate, pageable);
        personGroups.getContent().forEach(personGroup -> {
            personGroup.getGroupMembers().addAll(groupMemberRepository.findByPersonGroup(personGroup));
        });
        return personGroups;
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> getGroupTree() {
        List<PersonGroup> personGroups = personGroupRepository.findByParentIsNull();
        for (PersonGroup personGroup : personGroups) {
            personGroup.setLevel(0);
            securityService.loadGroupPermissions(personGroup);
            loadGroupChildren(personGroup);
        }
        return personGroups;
    }

    @Transactional(readOnly = true)
    public void loadGroupChildren(PersonGroup personGroup) {
        List<PersonGroup> groupChildren = personGroupRepository.findByParent(personGroup.getGroupId());
        if (groupChildren.size() != 0) {
            personGroup.setGroupChildren(groupChildren);
            for (PersonGroup personGroup1 : groupChildren) {
                personGroup1.setLevel(personGroup.getLevel() + 1);
                securityService.loadGroupPermissions(personGroup1);
                loadGroupChildren(personGroup1);
            }
        }

    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Preference> preferences = preferenceRepository.findByContextOrderByIdAsc("DEFAULT");
        for (Preference preference : preferences) {
            if (preference.getJsonValue() != null) {
                try {
                    DefaultValueDto valueDto = objectMapper.readValue(preference.getJsonValue(), new TypeReference<DefaultValueDto>() {
                    });
                    preference.setDefaultValue(valueDto);
                    if (valueDto != null && valueDto.getTypeId() != null) {
                        if (valueDto.getType().equals("ROLE")) {
                            PersonGroup personGroup = personGroupRepository.findOne(valueDto.getTypeId());
                            if (personGroup != null && personGroup.getGroupId().equals(id)) {
                                valueDto.setTypeId(null);
                                preference.setJsonValue(objectMapper.writeValueAsString(valueDto));
                                preference = preferenceRepository.save(preference);
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        personGroupRepository.delete(id);
    }

    @Transactional
    public void deleteGroupMember(Integer personGrpId, Integer personId) {
        groupMemberRepository.deleteGrpMember(personGrpId, personId);
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> findMultiple(List<Integer> ids) {
        return personGroupRepository.findByGroupIdIn(ids);
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> getPersonGroups(Integer personId) {
        Person person = personRepository.findOne(personId);
        List<GroupMember> members = groupMemberRepository.findByPerson(person);
        List<Integer> ids = new ArrayList<>();
        members.forEach(m -> ids.add(m.getPersonGroup().getGroupId()));
        return findMultiple(ids);
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> getPersonGroupPermissions(Integer personId) {
        List<PersonGroup> personGroups = groupMemberRepository.findGroupsByPerson(personId);
        for (PersonGroup personGroup : personGroups) {
            List<SecurityPermission> securityPermissions = groupSecurityPermissionRepository.getSecurityPermissionsByGroupId(personGroup.getGroupId());
            personGroup.setGroupSecurityPermissions(securityPermissions);
        }
        return personGroups;
    }

    @Transactional(readOnly = true)
    public List<GroupPermission> getGroupPermissions() {
        List<GroupPermission> groupPermissions = permissionRepository.findAll();
        return groupPermissions;
    }

    @Transactional
    public List<GroupPermission> saveGroupPermissions(List<GroupPermission> groupPermissions) {

//        permissionRepository.deleteGroupPermissions();
        List<GroupPermission> permissions = new ArrayList();
        for (GroupPermission groupPermission : groupPermissions) {
            GroupPermission permission = permissionRepository.saveAndFlush(groupPermission);
            permissions.add(permission);
        }
        return permissions;
    }

    @Transactional
    public void deleteGroupPermissions(List<GroupPermission> groupPermissions) {
        permissionRepository.delete(groupPermissions);
    }

    @Transactional(readOnly = true)
    public List<GroupPermission> findByGroupId(Integer groupId) {
        Set<Integer> groupIds = new TreeSet();
        groupIds.add(groupId);
        List<GroupPermission> groupPermissions = permissionRepository.findBygroupIds(groupIds);
        return groupPermissions;
    }

}
