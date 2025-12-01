package com.cassinisys.is.api.tm;
/**
 * The Class is for RoleController
 **/

import com.cassinisys.is.service.tm.RoleService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.security.Role;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author reddy
 */
@Api(name = "Roles", description = "Roles endpoint")
@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private RoleService roleService;

    /**
     * The method used for creating the Role
     **/
    @RequestMapping(method = RequestMethod.POST)
    public Role create(@RequestBody Role role) {
        role.setId(null);
        return roleService.create(role);
    }

    /**
     * The method used for updating the Role
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Role update(@PathVariable("id") Integer id,
                       @RequestBody Role role) {
        role.setId(id);
        return roleService.update(role);
    }

    /**
     * The method used for deleting the Role
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        roleService.delete(id);
    }

    /**
     * The method used get the value of  Role
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Role get(@PathVariable("id") Integer id) {
        return roleService.get(id);
    }

    /**
     * The method used getall the values of  Role
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<Role> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return roleService.findAll(pageable);
    }

}
