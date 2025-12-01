package com.cassinisys.is.api.col;
/**
 * The Class is for ProjectMeetingController
 **/

import com.cassinisys.is.model.col.ISProjectMeeting;
import com.cassinisys.is.service.col.ProjectMeetingService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(name = "Project meetings",
        description = "Project meetings endpoint",
        group = "Project")
@RestController
@RequestMapping("/projects/{projectId}/meetings")
public class ProjectMeetingController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private ProjectMeetingService projectMeetingService;

    /**
     * The method used for creating the ISProjectMeeting
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISProjectMeeting create(
            @PathVariable("projectId") Integer projectId,
            @RequestBody ISProjectMeeting meeting) {
        return projectMeetingService.create(meeting);
    }

    @RequestMapping(value = "/{meetingId}/mail", method = RequestMethod.GET)
    public void createMail(@PathVariable("meetingId") Integer meetingId) {
        projectMeetingService.createMail(meetingId);
    }

    /**
     * The method used for updating  the ISProjectMeeting
     **/
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.PUT)
    public ISProjectMeeting update(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("meetingId") Integer meetingId,
            @RequestBody ISProjectMeeting meeting) {
        meeting.setId(meetingId);
        return projectMeetingService.update(meeting);
    }

    /**
     * The method used for deleting the ISProjectMeeting
     **/
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("projectId") Integer projectId,
                       @PathVariable("meetingId") Integer meetingId) {
        projectMeetingService.delete(meetingId);
    }

    /**
     * The method used to obtain(get) the ISProjectMeeting
     **/
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.GET)
    public ISProjectMeeting get(@PathVariable("projectId") Integer projectId,
                                @PathVariable("meetingId") Integer meetingId) {
        return projectMeetingService.get(meetingId);
    }

    /**
     * The method used to get all the ISProjectMeeting in form of page
     **/
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Page<ISProjectMeeting> findAllProjectMeetings(@PathVariable("projectId") Integer projectId,
                                                         PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return projectMeetingService.findAll(pageable);
    }

    /**
     * The method used to getMultiples for ISProjectMeeting
     **/
    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<ISProjectMeeting> getMultiple(@PathVariable Integer[] ids) {
        return projectMeetingService.findMultiple(Arrays.asList(ids));
    }


    /**
     * The method used to deleteAttendee  in MeetingAttendee
     **/
    @RequestMapping(value = "/{meetingId}/attendee/{meetingAttendeeId}",
            method = RequestMethod.DELETE)
    public void deleteAttendee(@PathVariable("projectId") Integer projectId,
                               @PathVariable("meetingId") Integer meetingId,
                               @PathVariable("meetingAttendeeId") Integer meetingAttendeeId) {
        projectMeetingService.deleteAttendee(meetingAttendeeId, meetingId);
    }

}
