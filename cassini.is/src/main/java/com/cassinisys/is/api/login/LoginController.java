package com.cassinisys.is.api.login;
/**
 * The Class is for LoginController
 **/

import com.cassinisys.is.service.login.LoginService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Api(name = "Logins", description = "App logins endpoint")
@RestController
@RequestMapping("logins")
public class LoginController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private LoginService loginService;

    /**
     * The method used for creating the Login
     **/
    @RequestMapping(method = RequestMethod.POST)
    public Login create(@RequestBody Login login) {
        login.setId(null);
        return loginService.create(login);
    }

    /**
     * The method used for updating the Login
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Login update(@PathVariable("id") Integer id,
                        @RequestBody Login login) {
        login.setId(id);
        return loginService.update(login);
    }

    /**
     * The method used for deleting the Login
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        loginService.delete(id);
    }

    /**
     * The method used obtain(get) the Login
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Login get(@PathVariable("id") Integer id) {
        return loginService.get(id);
    }

    /**
     * The method used to getall the logins
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<Login> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return loginService.findAll(pageable);
    }

    @RequestMapping(value = "person/{id}", method = RequestMethod.GET)
    public Login getByPersonId(@PathVariable("id") Integer id) {
        return loginService.getByPersonId(id);
    }

}
