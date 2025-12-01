package com.cassinisys.platform.api.security;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.dto.LicenseDto;
import com.cassinisys.platform.service.security.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by reddy on 7/05/21.
 */
@RestController
@RequestMapping("/license")
public class LicenseController extends BaseController {
    @Autowired
    private LicenseService licenseService;

    @RequestMapping(method = RequestMethod.POST)
    public Boolean saveLicense(@RequestBody String encLicense) {
        return licenseService.saveLicense(encLicense);
    }

    @RequestMapping(method = RequestMethod.GET)
    public LicenseDto getLicense() {
        return licenseService.getLicense();
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public LicenseDto isLicenseValid() {
        return licenseService.isLicenseValid();
    }

    @RequestMapping(value = "/days/expire", method = RequestMethod.GET)
    public Integer getDaysToExpire() {
        return licenseService.getNoOfDays();
    }

    @RequestMapping(value = "/check/active/users", method = RequestMethod.POST)
    public LicenseDto validateActiveUserLicenses(@RequestBody LicenseDto licenseDto) {
        return licenseService.checkingActiveUserLicenses(licenseDto);
    }

}
