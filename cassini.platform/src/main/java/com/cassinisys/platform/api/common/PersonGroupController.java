package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.model.common.GroupMember;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.security.GroupPermission;
import com.cassinisys.platform.service.common.GroupMemberService;
import com.cassinisys.platform.service.common.PersonGroupService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nageshreddy on 08-01-2016.
 */

@RestController
@RequestMapping("/common/persongroups")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class PersonGroupController extends BaseController {

    @Autowired
    private PersonGroupService groupService;

    @Autowired
    private PageRequestConverter pageConverter;

    @Autowired
    private GroupMemberService groupMemberService;

    @RequestMapping(method = RequestMethod.POST)
    public PersonGroup create(@RequestBody PersonGroup personGroup) {
        return groupService.create(personGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PersonGroup update(@PathVariable("id") Integer id,
                              @RequestBody PersonGroup personGroup) {
        personGroup.setGroupId(id);
        return groupService.update(personGroup);
    }

    //method to make user a member of selected groups
    @RequestMapping(value = "/person/{personId}", method = RequestMethod.POST)
    public List<GroupMember> createGroupsByPersonId(@PathVariable("personId") Integer personId, @RequestBody List<GroupMember> groupMembers) {
        return groupService.createGroupsByPersonId(personId, groupMembers);
    }

    @RequestMapping(value = "/groupMember/{personId}", method = RequestMethod.POST)
    public GroupMember createGroupMember(@PathVariable("personId") Integer personId, @RequestBody GroupMember groupMember) {
        return groupService.createGroupMember(groupMember);
    }

    @RequestMapping(value = "/groupMember/{personId}", method = RequestMethod.PUT)
    public GroupMember updateGroupMember(@PathVariable("personId") Integer personId, @RequestBody GroupMember groupMember) {
        return groupService.updateGroupMember(personId, groupMember);
    }

    @RequestMapping(value = "/groupMember/{personId}/group/{groupId}", method = RequestMethod.GET)
    public GroupMember getGroupMember(@PathVariable("personId") Integer personId, @PathVariable("groupId") Integer groupId) {
        return groupService.getGroupMember(personId, groupId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PersonGroup get(@PathVariable("id") Integer id) {
        return groupService.get(id);
    }

    @RequestMapping(value = "/{id}/defaultusers/count", method = RequestMethod.GET)
    @Produces("text/plain")
    public Integer getDefaultUsersCount(@PathVariable("id") Integer id) {
        return groupService.getDefaultUsersCount(id);
    }

    @RequestMapping(value = "/{id}/defaultvalue", method = RequestMethod.GET)
    public Preference getDefaultValuePreferenceByGroup(@PathVariable("id") Integer id) {
        return groupService.getDefaultValuePreferenceByGroup(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<PersonGroup> getAll(PageRequest page) {
        Pageable pageable = pageConverter.convert(page);
        return groupService.findAll(pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PersonGroup> getAll() {
        return groupService.getAll();
    }

    @RequestMapping(value = "/all/names", method = RequestMethod.GET)
    public List<String> getAllGroupName() {
        return groupService.getAllGroupName();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<PersonGroup> getFilteredGroups(PageRequest pageRequest, PersonCriteria personCriteria) {
        Pageable pageable = pageConverter.convert(pageRequest);
        return groupService.getFilteredGroups(pageable, personCriteria);
    }

    @RequestMapping(value = "/all/groupTree", method = RequestMethod.GET)
    public List<PersonGroup> getGroupTree() {
        return groupService.getGroupTree();
    }


    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<PersonGroup> getMultiple(@PathVariable Integer[] ids) {
        return groupService.findMultiple(Arrays.asList(ids));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        groupService.delete(id);
    }

    @RequestMapping(value = "/groupmember/{personGrpId}/{personId}", method = RequestMethod.DELETE)
    public void deletePersonGroupMember(@PathVariable("personGrpId") Integer personGrpId, @PathVariable("personId") Integer personId) {
        groupService.deleteGroupMember(personGrpId, personId);
    }

    /*@RequestMapping(value = "/{id}/subgroups", method = RequestMethod.GET)
    public List<PersonGroup> getSubGroups(@PathVariable("id") Integer id) {
        return groupService.getSubGroups(id);
    }*/

    @RequestMapping(value = "/hierarchy", method = RequestMethod.GET)
    public List<PersonGroup> getHierarchy() {
        return groupService.getAll();
    }

    @RequestMapping(value = "/groups/byPerson/{id}", method = RequestMethod.GET)
    public List<Integer> findGroupsByPerson(@PathVariable("id") Integer id) {
        return groupMemberService.findGroupIdsByPerson(id);
    }

    @RequestMapping(value = "/grouppermissions", method = RequestMethod.GET)
    public List<GroupPermission> getGroupPermissions() {
        return groupService.getGroupPermissions();
    }

    @RequestMapping(value = "/grouppermissions", method = RequestMethod.POST)
    public List<GroupPermission> saveGroupPermissions(@RequestBody GroupPermission[] groupPermissions) {
        return groupService.saveGroupPermissions(Arrays.asList(groupPermissions));
    }

    @RequestMapping(value = "/grouppermissions/delete", method = RequestMethod.POST)
    public void deletePermissions(@RequestBody List<GroupPermission> groupPermissions) {
        groupService.deleteGroupPermissions(groupPermissions);
    }

    @RequestMapping(value = "/grouppermissions/group/{groupId}", method = RequestMethod.GET)
    public List<GroupPermission> getGroupPermissionsByGroupId(@PathVariable("groupId") Integer groupId) {
        return groupService.findByGroupId(groupId);
    }
}
