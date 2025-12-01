package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.AppDetails;
import com.cassinisys.platform.model.core.SystemInformation;
import com.cassinisys.platform.service.core.AppDetailsService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2019.
 */
@RestController
@RequestMapping("/app/details")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class AppDetailsController extends BaseController{

    @Autowired
    private AppDetailsService appDetailsService;

    @Autowired
    private FileSystemService fileSystemService;

    @RequestMapping(method = RequestMethod.GET)
    public List<AppDetails> getAll(){
        return appDetailsService.getAppDetails();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public AppDetails updateDetails(@RequestBody AppDetails appDetails){
        return appDetailsService.updateDetails(appDetails);
    }
    @RequestMapping(value = "/system/information", method = RequestMethod.GET)
    public SystemInformation getSystemInformation() {
        return appDetailsService.getSystemInformation();
    }

    @RequestMapping(value = "/filesystem/path", method = RequestMethod.GET, produces ="text/html")
    public String getSystemPath() {
        return fileSystemService.getFileSystemPath();
    }

    @RequestMapping(value = "/ipAddress", method = RequestMethod.POST)
    public String saveIpAddress(@RequestBody String address) {
        return appDetailsService.saveIpAddress(address);
    }
}
