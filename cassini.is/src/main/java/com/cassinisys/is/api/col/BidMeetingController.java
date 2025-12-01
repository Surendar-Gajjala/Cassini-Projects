package com.cassinisys.is.api.col;
/**
 * The Class is for BidMeetingController
 **/

import com.cassinisys.is.model.col.ISBidMeeting;
import com.cassinisys.is.service.col.BidMeetingService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(name = "Bid meetings",
        description = "Bid meetings endpoint",
        group = "Bid")
@RestController
@RequestMapping("/bids/{bidId}/meetings")
public class BidMeetingController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private BidMeetingService bidMeetingService;

    /**
     * The method used for creating the ISBIDMeeting
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISBidMeeting create(@PathVariable("bidId") Integer bidId,
                               @RequestBody ISBidMeeting meeting) {
        return bidMeetingService.create(meeting);
    }

    /**
     * The method used for updating  the created ISBIDMeeting
     **/
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.PUT)
    public ISBidMeeting update(@PathVariable("bidId") Integer bidId,
                               @PathVariable("meetingId") Integer meetingId,
                               @RequestBody ISBidMeeting meeting) {
        meeting.setId(meetingId);
        return bidMeetingService.update(meeting);
    }

    /**
     * The method used for deleting the created or updated ISBIDMeeting
     **/
    @RequestMapping(value = "/{meetingId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("bidId") Integer bidId,
                       @PathVariable("meetingId") Integer meetingId) {
        bidMeetingService.delete(meetingId);
    }
}
