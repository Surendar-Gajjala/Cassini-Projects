package com.cassinisys.is.api.login;
/**
 * The Class is for SessionController
 **/

import com.cassinisys.is.service.login.SessionService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(name = "Sessions", description = "Sessions endpoint")
@RestController
@RequestMapping("sessions")
public class SessionController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private SessionService sessionService;

    /**
     * The method used getall values of session
     **/
    @RequestMapping(method = RequestMethod.GET)
    public Page<Session> getAll(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sessionService.findAll(pageable);
    }

}
