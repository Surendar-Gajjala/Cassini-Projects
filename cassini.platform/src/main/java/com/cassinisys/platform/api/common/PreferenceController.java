package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.service.common.PreferenceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by subramanyam on 22-04-2020.
 */
@RestController
@RequestMapping("/preferences")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class PreferenceController extends BaseController {

    @Autowired
    private PreferenceService preferenceService;

    @RequestMapping(method = RequestMethod.POST)
    public Preference create(@RequestBody Preference preference) {
        return preferenceService.create(preference);
    }

    @RequestMapping(value = "{preferenceId}", method = RequestMethod.PUT)
    public Preference update(@PathVariable("preferenceId") Integer preferenceId, @RequestBody Preference preference) {
        return preferenceService.update(preference);
    }

    @RequestMapping(value = "{preferenceId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("preferenceId") Integer preferenceId) {
        preferenceService.delete(preferenceId);
    }

    @RequestMapping(value = "{preferenceId}", method = RequestMethod.GET)
    public Preference get(@PathVariable("preferenceId") Integer preferenceId) {
        return preferenceService.get(preferenceId);
    }

    @RequestMapping(value = "/changeApprovalPassword/{loginId}", method = RequestMethod.GET)
    public Preference getChangeApprovalPasswordByLogin(@PathVariable("loginId") Integer loginId) {
        return preferenceService.getChangeApprovalPasswordByLogin(loginId);
    }

    @RequestMapping(value = "/changeApprovalPassword", method = RequestMethod.POST)
    public Preference createChangeApprovalPassword(@RequestBody Preference preference) {
        return preferenceService.createChangeApprovalPassword(preference);
    }

    @RequestMapping(value = "/changeApprovalPassword/{preferenceId}", method = RequestMethod.PUT)
    public Preference updateChangeApprovalPassword(@PathVariable("preferenceId") Integer preferenceId, @RequestBody Preference preference) {
        return preferenceService.updateChangeApprovalPassword(preferenceId, preference);
    }

    @RequestMapping(value = "/preferenceByKey", method = RequestMethod.GET)
    public Preference getPreferenceByKey(@RequestParam("key") String key) {
        return preferenceService.getPreferenceByKey(key);
    }

    @RequestMapping(value = "/context/{context}", method = RequestMethod.GET)
    public List<Preference> getPreferencesByContext(@PathVariable("context") String context) {
        return preferenceService.getPreferencesByContext(context);
    }

    @RequestMapping(value = "/checkPassword/{loginId}", method = RequestMethod.GET)
    public Preference checkPassword(@PathVariable("loginId") Integer loginId, @RequestParam("password") String password) {
        return preferenceService.checkPassword(loginId, password);
    }

    @RequestMapping(value = "/multiple", method = RequestMethod.POST)
    public List<Preference> createMultiplePreferences(@RequestBody List<Preference> preferenceList) {
        return preferenceService.saveMultiplePreferences(preferenceList);
    }

    @RequestMapping(value = "/holiday", method = RequestMethod.POST)
    public Preference saveHolidays(@RequestBody String holidayList) {
        return preferenceService.saveHolidays(holidayList);
    }

    @RequestMapping(value = "/workingDays/{days}", method = RequestMethod.POST)
    public Preference saveWorkingDays(@PathVariable Integer days) {
        return preferenceService.saveWorkingDays(days);
    }

}
