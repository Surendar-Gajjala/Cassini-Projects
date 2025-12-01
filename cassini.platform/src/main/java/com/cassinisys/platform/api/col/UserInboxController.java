package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.UserInbox;
import com.cassinisys.platform.service.col.UserInboxService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 04-05-2016.
 */
@RestController
@RequestMapping("/col/users/{userId}/inbox")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class UserInboxController extends BaseController {

    @Autowired
    private UserInboxService userInboxService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserInbox update(@PathVariable("userId") Integer userId, @PathVariable("id") Integer id,
                            @RequestBody UserInbox inbox) {
        inbox.setId(id);
        inbox.setUserId(userId);
        return userInboxService.update(inbox);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserInbox get(@PathVariable("id") Integer id) {
        return userInboxService.get(id);
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public List<UserInbox> getUnread(@PathVariable("id") Integer id) {
        return userInboxService.getUnread(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<UserInbox> getAll(@PathVariable("userId") Integer userId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return userInboxService.findAll(userId, pageable);
    }

}