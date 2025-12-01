package com.cassinisys.erp.api.ui;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.ui.security.SecureNavigationBuilder;
import com.cassinisys.erp.ui.security.model.NavigationItem;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by reddy on 9/11/15.
 */
@RestController
@RequestMapping("/ui/navigation")
@Api(name = "NavigationMenu", description = "Navigation menu endpoint", group = "UI")
public class NavigationMenuController extends BaseController {
    @Autowired
    private SecureNavigationBuilder navigationBuilder;

    @RequestMapping(method = RequestMethod.GET)
    public List<NavigationItem> getNavigationMenu() {
        return navigationBuilder.build();
    }
}
