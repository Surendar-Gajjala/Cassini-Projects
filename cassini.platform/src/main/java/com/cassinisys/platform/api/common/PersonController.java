package com.cassinisys.platform.api.common;


import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.service.common.PersonGroupService;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author reddy
 */
@RestController
@Api(tags = "PLATFORM.COMMON", description = "Common endpoints")
@RequestMapping("/common/persons")
public class PersonController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonGroupService groupService;

    @Autowired
    private PreferenceRepository preferenceRepository;


    @RequestMapping(method = RequestMethod.POST)
    public Person create(@RequestBody Person person) {
        return personService.create(person);
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.PUT)
    public Person update(@PathVariable("personId") Integer personId,
                         @RequestBody Person person) {
        person.setId(personId);
        return personService.update(person);
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("personId") Integer personId) {
        personService.delete(personId);
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public Person get(@PathVariable("personId") Integer personId) {
        return personService.get(personId);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Person> getAll() {
        return personService.getAll();
    }

    @RequestMapping(value = "/activePersons", method = RequestMethod.GET)
    public List<Person> getAllActivePersons() {
        return personService.getAllActivePerson();
    }


    @RequestMapping(value = "/persontype/{persontype}/pageable", method = RequestMethod.GET)
    public Page<Person> getPersonsByPersonType(@PathVariable Integer persontype, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return personService.getPersonsByPersonType(persontype, pageable);
    }

    @RequestMapping(value = "/persontype/{persontype}", method = RequestMethod.GET)
    public List<Person> getAllPersonsByPersonType(@PathVariable Integer persontype) {

        return personService.getAllPersonsByPersonType(persontype);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Person> getAll(PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return personService.findAll(pageable);
    }

    @RequestMapping(value = "/multiple/[{ids}]", method = RequestMethod.GET)
    public List<Person> getMultiple(@PathVariable Integer[] ids) {
        return personService.findMultiple(Arrays.asList(ids));
    }

    @RequestMapping(value = "/{personId}/groups", method = RequestMethod.GET)
    public List<PersonGroup> getPersonGroups(@PathVariable("personId") Integer personId) {
        return groupService.getPersonGroups(personId);
    }

    @RequestMapping(value = "/{personId}/groups/permissions", method = RequestMethod.GET)
    public List<PersonGroup> getPersonGroupPermissions(@PathVariable("personId") Integer personId) {
        return groupService.getPersonGroupPermissions(personId);
    }

    @RequestMapping(value = "/freesearch", method = RequestMethod.GET)
    public Page<Person> freeTextSearch(PersonCriteria criteria,
                                       PageRequest pageRequest) {
        criteria.setFreeTextSearch(true);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return personService.findAll(criteria, pageable);
    }

    @RequestMapping(value = "/{email}/email", method = RequestMethod.GET)
    public Person findPersonByEmail(@PathVariable("email") String email) {
        return personService.findByEmail(email);
    }


    @RequestMapping(value = "/issue/person", method = RequestMethod.POST)
    public Person createIssuePerson(@RequestBody Person person) {
        return personService.createIssuePerson(person);
    }

    @RequestMapping(value = "/savePassword/{context}/password/{password}", method = RequestMethod.POST)
    public Preference saveSystemPassword(@PathVariable("context") String context, @PathVariable("password") String password) {
        Preference preference = new Preference();
        preference.setContext(context);
        preference.setPreferenceKey(context + ".PASSWORD");
        preference.setJsonValue(password);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/saveLogoutTime/{context}/time/{time}", method = RequestMethod.POST)
    public Preference saveSystemTime(@PathVariable("context") String context, @PathVariable("time") Integer time) {
        Preference preference = new Preference();
        preference.setContext(context);
        preference.setPreferenceKey(context + ".LOGOUTTIME");
        preference.setIntegerValue(time);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/saveTheme/{context}/theme/{theme}", method = RequestMethod.POST)
    public Preference saveSystemTheme(@PathVariable("context") String context, @PathVariable("theme") String theme) {
        Preference preference = new Preference();
        preference.setContext(context);
        preference.setPreferenceKey(context + ".THEME");
        preference.setStringValue(theme);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/updateTheme/{id}/theme/{theme}", method = RequestMethod.PUT)
    public Preference updateSystemTheme(@PathVariable("id") Integer id, @PathVariable("theme") String theme) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setStringValue(theme);
        return preferenceRepository.save(preference);
    }


    @RequestMapping(value = "/update/dateformat/{id}", method = RequestMethod.PUT)
    public Preference updateSystemDateFormat(@RequestBody Preference preference, @PathVariable("id") Integer preferenceId) {
        Preference object = preferenceRepository.findOne(preferenceId);
        object.setStringValue(preference.getStringValue());
        return preferenceRepository.save(object);
    }

    @RequestMapping(value = "/updateLogoutTime/{id}/time/{time}", method = RequestMethod.PUT)
    public Preference updateLogoutTime(@PathVariable("id") Integer id, @PathVariable("time") Integer time) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setIntegerValue(time);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/updatePassword/{id}/password/{password}", method = RequestMethod.PUT)
    public Preference updatePassword(@PathVariable("id") Integer id, @PathVariable("password") String password) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setJsonValue(password);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/getPreferenceById/{Id}", method = RequestMethod.GET)
    public Preference getPreferenceById(@PathVariable("Id") Integer Id) {
        return preferenceRepository.findOne(Id);
    }

    @RequestMapping(value = "/getPreferenceByContext/{context}", method = RequestMethod.GET)
    public List<Preference> getPreferenceByContext(@PathVariable("context") String context) {
        return preferenceRepository.findByContextOrderByIdAsc(context);
    }

    @RequestMapping(value = "/saveFileSize/{context}/fileSize/{fileSize}", method = RequestMethod.POST)
    public Preference saveFileSize(@PathVariable("context") String context, @PathVariable("fileSize") Integer fileSize) {
        Preference preference = new Preference();
        preference.setContext(context);
        preference.setPreferenceKey(context + ".FILESIZE");
        preference.setIntegerValue(fileSize);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/saveFileType/{context}/fileType", method = RequestMethod.POST)
    public Preference saveFileType(@RequestBody String fileType, @PathVariable("context") String context) {
        Preference preference = new Preference();
        if (fileType.equals("NONE")) preference.setStringValue(null);
        else preference.setStringValue(fileType);
        preference.setContext(context);
        preference.setPreferenceKey(context + ".FILETYPE");
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/updateFileSize/{id}/fileSize/{fileSize}", method = RequestMethod.PUT)
    public Preference updateFileSize(@PathVariable("id") Integer id, @PathVariable("fileSize") Integer fileSize) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setIntegerValue(fileSize);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/updateFileType/{id}/fileType", method = RequestMethod.POST)
    public Preference updateFileType(@RequestBody String fileType, @PathVariable("id") Integer id) {
        Preference preference = preferenceRepository.findOne(id);
        if (fileType.equals("NONE")) preference.setStringValue(null);
        else preference.setStringValue(fileType);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/updateRecurringItem/{id}/recurringItem/{itemSize}", method = RequestMethod.POST)
    public Preference updateRecurringItem(@PathVariable("id") Integer id, @PathVariable("itemSize") Integer itemSize) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setIntegerValue(itemSize);
        return preferenceRepository.save(preference);
    }


    @RequestMapping(value = "/getSessionTime", method = RequestMethod.GET)
    public Integer getSessionTime() {
        String name = "SYSTEM.LOGOUTTIME";
        Preference preference = preferenceRepository.findByPreferenceKey(name);
        return preference.getIntegerValue();
    }

    @RequestMapping(value = "/saveCustomLogo/uploadLogo", method = RequestMethod.POST)
    public Preference uploadImage(MultipartHttpServletRequest request) {
        Preference preference = new Preference();
        preference.setPreferenceKey("SYSTEM.LOGO");
        preference.setContext("SYSTEM");
        Map<String, MultipartFile> filesMap = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(filesMap.values());
        if (files.size() > 0) {
            MultipartFile file = files.get(0);
            try {
                preference.setCustomLogo(null);
                preference.setCustomLogo(file.getBytes());
                preference = preferenceRepository.save(preference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return preference;
    }

    @RequestMapping(value = "/customLogo/{id}/uploadLogo", method = RequestMethod.POST)
    public Preference uploadImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {

        Preference preference = preferenceRepository.findOne(id);
        if (preference != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    preference.setCustomLogo(null);
                    preference.setCustomLogo(file.getBytes());
                    preference = preferenceRepository.save(preference);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return preference;
    }

    @RequestMapping(value = "/deleteCustomLogo/{id}", method = RequestMethod.PUT)
    public Preference deleteCustomLogo(@PathVariable("id") Integer id) {
        Preference preference = preferenceRepository.findOne(id);
        preference.setCustomLogo(null);
        return preferenceRepository.save(preference);
    }

    @RequestMapping(value = "/{id}/customImageAttribute/download", method = RequestMethod.GET)
    public void downloadImageItem(@PathVariable("id") Integer id,
                                  HttpServletResponse response) {

        Preference preference = preferenceRepository.findOne(id);
        if (preference != null) {
            InputStream is = new ByteArrayInputStream(preference.getCustomLogo());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/preferences", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public List<Preference> getPreferenceByContext() {
        List<Preference> preferences = preferenceRepository.findAll();
        List<Preference> preferences1 = new ArrayList<>();
        if (preferences.size() > 0) {
            for (Preference preference : preferences) {
                if (preference.getPreferenceKey().equals("SYSTEM.LOGO") || preference.getPreferenceKey().equals("SYSTEM.THEME") || preference.getPreferenceKey().equals("SYSTEM.DATE.FORMAT")) {
                    preferences1.add(preference);
                }
            }
        }
        return preferences1;
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.DELETE)
    public void deletePersonImage(@PathVariable("id") Integer id) {
        personService.deletePersonImage(id);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public Person uploadPersonImage(@PathVariable("id") Integer id, MultipartHttpServletRequest request) {
        Person person = personService.get(id);
        if (person != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    person.setImage(file.getBytes());
                    person = personService.updatePerson(person);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return person;
    }

    @RequestMapping(value = "/{id}/image/download", method = RequestMethod.GET)
    public void downloadPersonImage(@PathVariable("id") Integer id,
                                    HttpServletResponse response) {
        personService.downloadPersonImage(id, response);
    }


    @RequestMapping(value = "/withoutlogincount", method = RequestMethod.GET)
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getPersonCountWithoutLogin() {
        return personService.getPersonCountWithoutLogin();
    }

}
