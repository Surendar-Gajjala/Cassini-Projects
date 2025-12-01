package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.common.ForgeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * Created by Nageshreddy on 02-05-2019.
 */
@RestController
@RequestMapping("/platform/forge")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class ForgeController extends BaseController {

    @Autowired
    private ForgeService forgeService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    public String getAuthenticate() {
        return forgeService.getAuthentication();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFiles(@PathVariable("id") Integer id,
                              MultipartHttpServletRequest request) {
        return null;
    }

    @RequestMapping(value = "/view/{urn}", method = RequestMethod.GET)
    public void openFile(@PathVariable("urn") String urn) {
        forgeService.openViewer(urn);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public Boolean isForgeActive() {
        return forgeService.isForgeActive();
    }

    @RequestMapping(value = "/authenticate/validate", method = RequestMethod.GET)
    public Boolean checkAuthenticate(@RequestParam("clientId") String clientId, @RequestParam("clientKey") String clientKey) {
        return forgeService.checkAuthenticate(clientId, clientKey);
    }

}
