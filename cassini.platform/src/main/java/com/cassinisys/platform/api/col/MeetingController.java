package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.Meeting;
import com.cassinisys.platform.service.col.MeetingService;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Cassinisys on 20-10-2017.
 */

@RestController
@RequestMapping("/col/meetings")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class MeetingController extends BaseController{

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private MeetingService meetingService;

    @RequestMapping(method = RequestMethod.POST)
    public Meeting create(@RequestBody Meeting meeting) {
        meeting.setId(null);
        return meetingService.create(meeting);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Meeting update(@PathVariable("id") Integer id,
                          @RequestBody Meeting meeting) {
        meeting.setId(id);
        return meetingService.update(meeting);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        meetingService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Meeting get(@PathVariable("id") Integer id) {
        return meetingService.get(id);
    }


}
