package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.AppNotification;
import com.cassinisys.platform.service.col.AppNotificationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by GSR on 15-10-2018.
 */
@RestController
@RequestMapping("/col/appNotifications")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class AppNotificationController extends BaseController {

	@Autowired
	private AppNotificationService appNotificationService;

	@RequestMapping(method = RequestMethod.POST)
	public AppNotification create(@RequestBody AppNotification appNotification) {
		appNotification.setId(null);
		return appNotificationService.create(appNotification);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public AppNotification update(@PathVariable("id") Integer id,
	                              @RequestBody AppNotification appNotification) {
		appNotification.setId(id);
		return appNotificationService.update(appNotification);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Integer id) {
		appNotificationService.delete(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AppNotification get(@PathVariable("id") Integer id) {
		return appNotificationService.get(id);
	}


	@RequestMapping(method = RequestMethod.GET)
	public List<AppNotification> getAll() {
		return appNotificationService.getAll();
	}


}
