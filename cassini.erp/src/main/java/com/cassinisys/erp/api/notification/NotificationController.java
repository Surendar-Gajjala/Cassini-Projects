package com.cassinisys.erp.api.notification;

import com.cassinisys.erp.api.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by reddy on 9/2/15.
 */
@RestController
@RequestMapping("common/notification")
@Api(name = "Notification", description = "Notification endpoint", group = "COMMON")
public class NotificationController extends BaseController {

}
