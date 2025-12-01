package com.cassinisys.platform.api.security;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.SecurityPermissionCriteria;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.dto.SecurityPermissionDto;
import com.cassinisys.platform.model.dto.SecurityPermissionsDto;
import com.cassinisys.platform.model.security.DMFolderPermission;
import com.cassinisys.platform.model.security.ModuleType;
import com.cassinisys.platform.model.security.SecurityPermission;
import com.cassinisys.platform.service.security.SecurityPermissionService;
import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by reddy on 7/16/15.
 */
@RestController
@RequestMapping("/securityPermission")
@Api(tags = "PLATFORM.SECURITYPERMISSION", description = "Security endpoints")
public class SecurityPermissionController extends BaseController {

    @Autowired
    private SecurityPermissionService securityPermissionService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public SecurityPermission create(@RequestBody SecurityPermission securityPermission) {
        return securityPermissionService.create(securityPermission);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public SecurityPermission update(@RequestBody SecurityPermission securityPermission) {
        return securityPermissionService.update(securityPermission);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        securityPermissionService.delete(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Page<SecurityPermission> getAll(PageRequest pageRequest, SecurityPermissionCriteria varianceCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return securityPermissionService.findAllSecurityPermission(pageable, varianceCriteria);
    }

    @RequestMapping(value = "/permissions/{groupId}", method = RequestMethod.GET)
    public SecurityPermissionDto getSecurityPermissionsFromGroupId(@PathVariable("groupId") Integer groupId) {
        return securityService.getSecurityPermissionsFromGroupId(groupId);
    }

    @RequestMapping(value = "/objecttypes", method = RequestMethod.GET)
    public SecurityPermissionsDto getObjectTypes() {
        return securityPermissionService.getObjectTypes();
    }

    @RequestMapping(value = "/objecttype/{id}", method = RequestMethod.GET)
    public CassiniObject getObjectType(@PathVariable("id") Integer id) {
        return securityPermissionService.getObjectType(id);
    }

    @RequestMapping(value = "/objecttype/{objectType}/all", method = RequestMethod.GET)
    public List<SecurityPermission> getAllSecurityPermissionsByObjectType(@PathVariable("objectType") String objectType) {
        return securityPermissionService.getAllSecurityPermissionsByObjectType(objectType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SecurityPermission get(@PathVariable("id") Integer permissionId) throws InterruptedException {
        return securityPermissionService.get(permissionId);
    }

    @RequestMapping(value = "/criteria/{id}", method = RequestMethod.POST)
    public SecurityPermission createCriteria(@RequestBody String criteria, @PathVariable("id") Integer id) throws InterruptedException {
        return securityPermissionService.createCriteria(id, criteria);
    }

    @RequestMapping(value = "/create/groupSecurityPermission/{groupId}", method = RequestMethod.POST)
    public void createGroupSecurityPermission(@RequestBody List<SecurityPermission> securityPermissions, @PathVariable("groupId") Integer groupId) throws InterruptedException {
        securityPermissionService.createGroupSecurityPermission(groupId, securityPermissions);
    }

    @RequestMapping(value = "/delete/groupSecurityPermission/{permissionId}/group/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroupSecurityPermission(@PathVariable("groupId") Integer groupId, @PathVariable("permissionId") Integer permissionId) throws InterruptedException {
        securityPermissionService.deleteGroupSecurityPermission(groupId, permissionId);
    }

    @RequestMapping(value = "/gettypeattributes/{typeId}/type/{type}", method = RequestMethod.GET)
    public List getTypeAttributes(@PathVariable("type") String type, @PathVariable("typeId") Integer typeId) {
        return securityPermissionService.getTypeAttributes(type, typeId, true);
    }

    @RequestMapping(value = "/getModuleType/objectType/{objectType}", method = RequestMethod.GET)
    public ModuleType getModuleType(@PathVariable("objectType") String objectType) {
        return securityPermissionService.getModuleType(objectType);
    }

    @RequestMapping(value = "/saveDMPermissions", method = RequestMethod.POST)
    public List<DMFolderPermission> saveDMPermissions(@RequestBody List<DMFolderPermission> dmFolderPermissions) {
        return securityPermissionService.saveDMPermissions(dmFolderPermissions);
    }

    @RequestMapping(value = "/getAllDMPermissions", method = RequestMethod.GET)
    public List<DMFolderPermission> getAllDMFolderPermissions() {
        return securityPermissionService.getAllDMFolderPermissions();
    }

    @RequestMapping(value = "/getDMPermissionByIdAndType/{id}/type/{type}", method = RequestMethod.GET)
    public List<DMFolderPermission> getDMPermissionByIdAndType(@PathVariable("id") Integer id, @PathVariable("type") String type) {
        return securityPermissionService.getDMPermissionByIdAndType(id, type);
    }
}
