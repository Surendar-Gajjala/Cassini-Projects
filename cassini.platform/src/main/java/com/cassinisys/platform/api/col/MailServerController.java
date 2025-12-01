package com.cassinisys.platform.api.col;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.col.MailServer;
import com.cassinisys.platform.model.col.ObjectMailSettings;
import com.cassinisys.platform.service.col.MailServerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Rajabrahmachary on 16-11-2018.
 */
@RestController
@RequestMapping("/col/mailServers")
@Api(tags = "PLATFORM.COMMON",description = "Common endpoints")
public class MailServerController extends BaseController {

    @Autowired
    private MailServerService mailServerService;

   /*  start block of methods for objectMailSettings */

    @RequestMapping(value = "/objectMailSettings",method = RequestMethod.POST)
    public ObjectMailSettings createObjectMailSettings(@RequestBody ObjectMailSettings objectMailSettings) {
        return mailServerService.createObjectMailSettings(objectMailSettings);
    }

    @RequestMapping(value = "/objectMailSettings/{objectId}", method = RequestMethod.PUT)
    public ObjectMailSettings updateObjectMailSettings(@PathVariable("objectId") Integer objectId,@RequestBody ObjectMailSettings objectMailSettings) {
        objectMailSettings.setObjectId(objectId);
        return mailServerService.updateObjectMailSettings(objectMailSettings);
    }

    @RequestMapping(value = "/objectMailSettings/{objectId}", method = RequestMethod.DELETE)
    public void deleteObjectMailSettings(@PathVariable("objectId") Integer objectId) {
        mailServerService.deleteObjectMailSettings(objectId);
    }

    @RequestMapping(value = "/objectMailSettings/{objectId}", method = RequestMethod.GET)
    public ObjectMailSettings getObjectMailSettings(@PathVariable("objectId") Integer objectId) {
        return mailServerService.getObjectMailSettings(objectId);
    }

    @RequestMapping(value = "/objectMailSettings/all", method = RequestMethod.GET)
    public List<ObjectMailSettings> getAllObjectMailSettings() {
        return mailServerService.getAllObjectMailSettings();
    }

      /*  end block of methods for objectMailSettings */

    @RequestMapping(method = RequestMethod.POST)
    public MailServer create(@RequestBody MailServer mailServer) {
        mailServer.setId(null);
        return mailServerService.create(mailServer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public MailServer update(@PathVariable("id") Integer id,@RequestBody MailServer mailServer) {
        mailServer.setId(id);
        return mailServerService.update(mailServer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        mailServerService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public MailServer get(@PathVariable("id") Integer id) {
        return mailServerService.get(id);
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<MailServer> getAll() {
        return mailServerService.getAll();
    }
}
