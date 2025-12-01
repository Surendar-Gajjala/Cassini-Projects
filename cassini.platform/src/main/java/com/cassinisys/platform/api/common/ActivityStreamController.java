package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.ActivityStreamCriteria;
import com.cassinisys.platform.filtering.SessionCriteria;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.activitystream.ActivityStreamDto;
import com.cassinisys.platform.model.activitystream.ActivityStreamSessionsDto;
import com.cassinisys.platform.service.activitystream.ActivityStreamService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/common/activitystream")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class ActivityStreamController extends BaseController {
    @Autowired
    private ActivityStreamService activityStreamService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.GET)
    public Page<ActivityStream> getActivityStream(PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return activityStreamService.getActivityStream(pageable);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Page<ActivityStream> getObjectActivityStream(@PathVariable Integer id, PageRequest page, ActivityStreamCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(page);
        return activityStreamService.getObjectActivityStream(id, pageable, criteria);
    }

    @RequestMapping(value = "/{id}/datewise", method = RequestMethod.GET)
    public ActivityStreamDto getObjectActivityStreamByDate(@PathVariable Integer id, PageRequest page, ActivityStreamCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(page);
        return activityStreamService.getObjectActivityStreamByDate(id, pageable, criteria);
    }

    @RequestMapping(value = "/datewise", method = RequestMethod.GET)
    public ActivityStreamDto getActivityStreamByDate(PageRequest page, ActivityStreamCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(page);
        return activityStreamService.getAllActivityStream(pageable, criteria);
    }

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public ActivityStreamSessionsDto getAllActivityStreamSessions(PageRequest page, SessionCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(page);
        return activityStreamService.getAllActivityStreamSessions(pageable, criteria);
    }

    @RequestMapping(value = "/objecttypes", method = RequestMethod.GET)
    public List<String> getActivityStreamObjectTypes() {
        return activityStreamService.getActivityStreamObjectTypes();
    }

    @RequestMapping(value = "/{objectId}/objecttypes/{objectType}/actions", method = RequestMethod.GET)
    public List<String> getActivityStreamObjectTypeActions(@PathVariable("objectId") Integer objectId, @PathVariable("objectType") String objectType) {
        return activityStreamService.getActivityStreamObjectTypeActions(objectId, objectType);
    }

    @RequestMapping(value = "/[{objectIds}]/objecttypes/{objectType}/actions/multiple", method = RequestMethod.GET)
    public List<String> getActivityStreamObjectTypeActionsByIds(@PathVariable("objectIds") Integer[] objectIds, @PathVariable("objectType") String objectType) {
        return activityStreamService.getActivityStreamObjectTypeActionsByIds(Arrays.asList(objectIds), objectType);
    }
}
