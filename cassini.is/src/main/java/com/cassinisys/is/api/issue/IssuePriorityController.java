package com.cassinisys.is.api.issue;
/**
 * The Class is for IssuePriorityController
 **/

import com.cassinisys.is.model.im.IssuePriority;
import com.cassinisys.platform.api.core.BaseController;
import org.jsondoc.core.annotation.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(name = "Issue priorities", description = "Issue priorities management")
@RestController
@RequestMapping("issuepriorities")
public class IssuePriorityController extends BaseController {

    /**
     * The method used to obtainall(getall)values from issuepriority
     **/
    @RequestMapping(method = RequestMethod.GET)
    public IssuePriority[] getAll() {
        return IssuePriority.values();
    }

}
