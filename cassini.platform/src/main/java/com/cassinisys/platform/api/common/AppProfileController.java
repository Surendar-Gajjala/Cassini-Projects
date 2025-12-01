package com.cassinisys.platform.api.common;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Profile;
import com.cassinisys.platform.service.common.ProfileService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@RestController
@RequestMapping("/common/profiles")
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
public class AppProfileController extends BaseController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PageRequestConverter pageConverter;

    @RequestMapping(method = RequestMethod.POST)
    public Profile create(@RequestBody Profile profile) {
        profile.setId(null);
        return profileService.create(profile);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Profile update(@PathVariable("id") Integer id,
                          @RequestBody Profile profile) {
        profile.setId(id);
        return profileService.update(profile);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        profileService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Profile get(@PathVariable("id") Integer id) {
        return profileService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Profile> getAll(PageRequest page) {
        Pageable pageable = pageConverter.convert(page);
        return profileService.findAll(pageable);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Profile> getAll() {
        return profileService.getAll();
    }

    @RequestMapping(value = "/by/profile/{profileId}", method = RequestMethod.GET)
    public List<PersonGroup> getPersonGroupsByProfile(@PathVariable("profileId") Integer profileId) {
        return profileService.getPersonGroupsByProfile(profileId);
    }

}
