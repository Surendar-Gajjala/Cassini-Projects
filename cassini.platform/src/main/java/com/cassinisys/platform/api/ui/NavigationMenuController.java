package com.cassinisys.platform.api.ui;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.ui.security.SecureNavigationBuilder;
import com.cassinisys.platform.ui.security.model.NavigationItem;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lakshmi on 7/10/2016.
 */
@RestController
@RequestMapping("/ui/navigation")
@Api(tags = "PLATFORM.NAVIGATION_MENU",description = "Navigation menu endpoints")
public class NavigationMenuController extends BaseController {
    @Autowired
    private SecureNavigationBuilder navigationBuilder;

    @RequestMapping(method = RequestMethod.GET)
    public List<NavigationItem> getNavigationMenu() {
        return navigationBuilder.build();
    }
}
